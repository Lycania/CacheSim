/*
 * Copyright (C) 2015 leo
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package application.model;

import Utils.Numbers;
import cache.Block;
import cache.Cache;
import cache.DirectMappedCache;
import cache.FullAssociativeCache;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author leo
 */
public class TagTableModel extends AbstractTableModel{
    public static enum Method {HEXA, BIN, DEC};
    
    private final List<String> header;
    private final List<Block> data;
    private final Cache cac;
    public int tagBitLong, indexBitLong, offsetBitLong;   // Nombre de bits nécessaires
    public int tagHexLong, indexHexLong, offsetHexLong;
    private Method howToWrite;
    private boolean writeValue;
    
    public TagTableModel(Cache c) {
        header      = new ArrayList<>();
        data        = new ArrayList<>();
        this.cac    = c;
        
        if (c instanceof DirectMappedCache) {
            header.add("V");
            header.add("Tag");
            header.add("Index");
            header.add("Offset");
            
        } else if (c instanceof FullAssociativeCache) {
            header.add("V");
            header.add("Tag");
            header.add("Offset");
            
        } else {
            header.add("V");
            header.add("Addres");
        }
        
        // calcul du nombre de bits necessaire
        indexBitLong   = (byte) ((Math.log(cac.size())) / Math.log(2));
        offsetBitLong  = (byte) ((Math.log(cac.getWordSize() *
                cac.getBlockSize())) / Math.log(2));
        tagBitLong     = (byte) (32 - indexBitLong - offsetBitLong);
        
        // calcul du nombre d'hexa nécessaire
        tagHexLong      = (tagBitLong + (tagBitLong % 4)) / 4;
        offsetHexLong   = (offsetBitLong + (offsetBitLong % 4)) / 4;
        indexHexLong    = (indexBitLong + (indexBitLong % 4)) / 4;
        
        howToWrite = Method.BIN;
        writeValue = true;
    }

    @Override
    public int getRowCount() {
        return data.size();
    }
    
    public Block getRow(int rowIndex) {
        return data.get(rowIndex);
    }

    @Override
    public int getColumnCount() {
        return header.size();
    }

    @Override
    public String getColumnName(int columnIndex) {
        return header.get(columnIndex);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
    
    public void changeMethod(Method m) {
        howToWrite = m;
        update();
    }
    
    public void toggleWrite() {
        writeValue = !writeValue;
        update();
    }

    @Override
    public Object getValueAt(int r, int c) {
        Block line = data.get(r);
        
        if (cac instanceof DirectMappedCache) {
            switch (c) {
                case 0: return line.isValid();
                case 1: return toText(line.getTag(), 0);
                case 2: return toText(line.getIndex(), 1);
                case 3: return toText(0, 2);
            }
            
        } else if (cac instanceof FullAssociativeCache) {
            switch (c) {
                case 0: return line.isValid();
                case 1: return toText(line.getTag(), 0);
                case 2: return toText(0, 2);
            }
            
        } else {
            if (c == 0) return line.isValid();
        }
        
        return line;
    }

    public void ajouterLigne(Block... lines) {
        data.addAll(Arrays.asList(lines));
        fireTableRowsInserted(data.size() - 1, data.size() - 1);
    }
    
    public void supprimerLigne(int ligne) {
        data.remove(ligne);
        fireTableRowsDeleted(ligne, ligne);
    }
    
    public void viderTable() {
        for (int i = getRowCount(); i > 0; --i) {
            supprimerLigne(i-1);
        }
    }
    
    public void update() {
        if (cac != null) {
            data.clear();
            
            // Mise à jour des donnée à afficher pour correspondra avec celle du cache
            if (cac instanceof DirectMappedCache) {
                List<Block> blocks = ((DirectMappedCache) cac).getBlocks();
                
                for (Block b : blocks) {
                    ajouterLigne(b);
                }
            }
            
            else if (cac instanceof FullAssociativeCache) {
                List<Block> blocks = ((FullAssociativeCache) cac).getBlocks();
                
                for (Block b : blocks) {
                    ajouterLigne(b);
                }
            }
        }
    }
    
    public List<Block> getData() {
        return data;
    }
    
    public Block getFirst() {
        if (!data.isEmpty()) return data.get(0);
        else return null;
    }
    
    private String toText(int valeur, int code) {
        // code : (0, tag), (1, index), (2, offset)
        if (!writeValue) return "";
        
        switch (howToWrite) {
            case BIN: switch (code) {
                case 0: return Numbers.toBin(valeur, tagBitLong);
                case 1: return Numbers.toBin(valeur, indexBitLong);
                case 2: return Numbers.toBin(valeur, offsetBitLong);
            }
            
            case HEXA: switch (code) {
                case 0: return Numbers.toHex(valeur, tagHexLong);
                case 1: return Numbers.toHex(valeur, indexHexLong);
                case 2: return Numbers.toHex(valeur, offsetHexLong);
            }
        }
        
        return Integer.toString(valeur);
    }
}

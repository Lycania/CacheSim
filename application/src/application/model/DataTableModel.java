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
import cache.type.Unit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author leo
 */
public class DataTableModel extends AbstractTableModel {
    private final List<String> header;
    private final List<Block> data;
    private final Cache cac;
    public int tagBitLong, indexBitLong, offsetBitLong;   // Nombre de bits nécessaires
    public int tagHexLong, indexHexLong, offsetHexLong;
    private TagTableModel.Method howToWrite;
    private boolean writeValue;
    
    public DataTableModel(Cache c) {
        super();
        header  = new ArrayList<>();
        data = new ArrayList<>();
        this.cac = c;
        
        for (int i = 0; i < cac.getBlockSize(); ++i) {
            header.add("Word " + (i + 1));
        }
        
        howToWrite = TagTableModel.Method.HEXA;
        writeValue = false;
    }

    public void changeMethod(TagTableModel.Method m) {
        howToWrite = m;
        update();
    }
    
    public void toggleWrite() {
        writeValue = !writeValue;
        update();
    }
    
    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return header.size();
    }

    @Override
    public String getColumnName(int columnIndex) {
        return header.get(columnIndex);
    }

    public Block getRow(int rowIndex) {
        return data.get(rowIndex);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int r, int c) {
        Block line = data.get(r);
        
        return toText(Numbers.bytesToInt(Unit.WORD, line.getData().get(c)), 0);
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

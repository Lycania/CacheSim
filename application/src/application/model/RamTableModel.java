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
import cache.Ram;
import cache.type.Unit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author leo
 */
public class RamTableModel extends AbstractTableModel{
    private final String[] header = {"Adresses" , "Word"};
    private final List<String[]> data;
    private Ram ram;

    public RamTableModel(Ram ram) {
        data = new ArrayList<>();
        
        this.ram = ram;
    }
    
    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return header.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data.get(rowIndex)[columnIndex];
    }
    
    @Override
    public String getColumnName(int columnIndex) {
        return header[columnIndex];
    }
    
    public void ajouterLigne(String[] line) {
        data.add(line);
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
        if (ram != null) {
            // Mise à jour des donnée pour correspondre avec celle de la RAM
            data.clear();
            byte[] ramData = ram.getData();

            int compteur = 0;
            while (compteur < ramData.length) {
                String[] line = new String[2];

                String word = Numbers.toBin(Unit.WORD, 
                        Arrays.copyOfRange(ramData, compteur, compteur+4));
                String addr = Numbers.toHex(compteur);

                line[0] = addr;
                line[1] = word;


                ajouterLigne(line);
                compteur += 4;
            }
        }
    }
}

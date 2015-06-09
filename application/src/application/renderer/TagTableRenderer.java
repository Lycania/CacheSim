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
package application.renderer;

import application.model.TagTableModel;
import cache.Block;
import cache.Controler;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author leo
 */
public class TagTableRenderer extends DefaultTableCellRenderer {
    
    @Override
    public Component getTableCellRendererComponent( JTable table,
            Object value,
            boolean isSelected,
            boolean hasFocus,
            int row, int column) {
        
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
//        if (row % 2 == 0) {
//            setBackground(Color.GREEN);
//        } else {
//            setBackground(Color.LIGHT_GRAY);
//        }
//        
//        if (hasFocus)
//            setBackground(Color.YELLOW);
   
        if (value instanceof Boolean) {
            if ((Boolean) value) {
                setText("1");
                setBackground(Color.GREEN);

            } else {
                setText("0");
                setBackground(Color.RED);
            }
        }
        
        // Changement de la couleur de fond de la ligne correspondant à la ou
        // les dernières modifications
        Block b = ((TagTableModel) table.getModel()).getRow(row);
        if (b.wc == Controler.wc)
            setBackground(Color.orange);
        
        if (isSelected)
            setBackground(Color.cyan);
        
        return this;
    }
}

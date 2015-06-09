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
package application.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

/**
 *
 * @author leo
 */
public class TabbedPaneUI extends BasicTabbedPaneUI{
    private final Color BORDER      = new Color(19, 19, 19);
    private final Color BACKGROUND  = new Color(160, 180, 179);
    
    private final Color BACKGROUND_SELECTED = new Color(62, 180, 179);
    private final Color BORDER_SELECTED     = new Color(50, 50, 50);

    
    @Override
    protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
        g.setColor(new Color(160, 180, 179));
        g.fillRect(x, y, h, h);
        
        if (isSelected) {
            g.setColor(new Color(62, 193, 189));
            g.fillRect(x, y, h, h);
        }
    }

    @Override
    protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
        Rectangle rect = getTabBounds(tabIndex, new Rectangle(x, y, h, h));
        g.setColor(new Color(19, 19, 19));
        g.drawRect(x, y, h, h);
        
        super.paintTabBorder(g, tabPlacement, tabIndex, x, y, w, h, isSelected);
    }
}

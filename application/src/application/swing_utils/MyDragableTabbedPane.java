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
package application.swing_utils;

import application.swing_utils.ClosableTabComponent;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTabbedPane;

/**
 *
 * @author leo
 */
public class MyDragableTabbedPane extends JTabbedPane {
    private final List<Component> inside = new ArrayList<>();
    
    // Nécessaire pour déplacer un onglet
    private Image tabImage = null;
    private boolean dragging = false;
    private Point cMousePos = null;
    private int draggedTabIndex;
    
    public MyDragableTabbedPane() {
        super();
//        setUI(new TabbedPaneUI());
  
        addMouseMotionListener(new MouseMotionAdapter() {

            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                
                if (!dragging) {
                    final int x = e.getX();
                    final int y = e.getY();

                    int tabNumber = getUI().tabForCoordinate(MyDragableTabbedPane.this, x, y);

                    if (tabNumber >= 0) {
                        draggedTabIndex = tabNumber;
                        Rectangle bounds = getUI().getTabBounds(MyDragableTabbedPane.this, tabNumber);

                        // On dessine tout le composant dans une première image
                        Image totalImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
                        Graphics g = totalImage.getGraphics();
                        g.setClip(bounds);  // Diminution des dimensions à dessiner
                        setDoubleBuffered(false);
                        paintComponent(g);


                        // On ne récupère qu'une partie de cette image
                        // celle qui nous interesse
                        tabImage = new BufferedImage(bounds.width, bounds.height, BufferedImage.TYPE_INT_ARGB);
                        g = tabImage.getGraphics();
                        g.drawImage(totalImage, 0, 0, bounds.width, bounds.height, bounds.x, bounds.y, bounds.x + bounds.width, bounds.y+bounds.height, MyDragableTabbedPane.this);

                        dragging = true;
                        repaint();
                    }
                
                } else {
                    cMousePos = e.getPoint();
                    repaint();
                }
            }
        });
        
        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                
                if (dragging) {
                    int tabNumber = getUI().tabForCoordinate(MyDragableTabbedPane.this, e.getX(), 10);
                    if (tabNumber >= 0) {
                        Component comp = getComponentAt(draggedTabIndex);
                        String title = getTitleAt(draggedTabIndex);
                        removeTabAt(draggedTabIndex);
                        
                        add(comp, tabNumber);
                        setSelectedIndex(tabNumber);
                    }
                }
                
                tabImage = null;
                dragging = false;
                cMousePos = null;
                repaint();
            }
        });
    }
    
    public List<Component> getAll() {
        return inside;
    }
    
    
    public int insideSize() {
        return inside.size();
    }
    
    @Override
    public void paintComponent(Graphics g) {
        if (tabImage != null && cMousePos != null) {
            g.drawImage(tabImage, cMousePos.x, cMousePos.y, this);
            
        } else {
            super.paintComponent(g);
        }
    }

    @Override
    public Component add(String title, Component component) {        
        // On récupérer cette onglet et on modifie sont contenue
        super.add(title, component);
        inside.add(component);
        
        final int index = indexOfTab(title);
        ClosableTabComponent in = new ClosableTabComponent(this);        
        setTabComponentAt(index, in);
        
        return component;
    }

    @Override
    public Component add(Component component, int index) {
        // La listes des composants contenue par ce panneau est dans le bon ordre
        inside.remove(draggedTabIndex);
        inside.add(index, component);
        List<Component> tmp = new ArrayList<>(inside);
        inside.clear();
        
        // On supprime maintenant tout les onglets SANS VIDER LA LISTE et
        // on rajoute les composants dans le bon ordre grace à la liste
        removeAll();
        
        for (Component c : tmp) {
            String title = "L" + (inside.size() + 1);
            add(title, c);
        }
        
        return component;
    }
    
    @Override
    public void remove(int index) {
        super.remove(index);
        inside.remove(index);
    }
    
    
}

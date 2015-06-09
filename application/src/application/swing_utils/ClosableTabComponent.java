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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.plaf.basic.BasicButtonUI;

/**
 *
 * @author leo
 */
public class ClosableTabComponent extends JPanel {
    private final JTabbedPane pane;
    
    public ClosableTabComponent(final JTabbedPane pane) {
        super(new FlowLayout(FlowLayout.LEFT, 0, 0));
        
        if (pane == null)
            throw new NullPointerException("TabbedPane is null");
        
        this.pane = pane;
        setOpaque(false);
        
        // On ajoute un nom et on fait en sorte que son texte corresponde au
        // titre de l'onglet correspondant
        JLabel label = new JLabel() {
            
            @Override
            public String getText() {
                int i = pane.indexOfTabComponent(ClosableTabComponent.this);
                
                if (i != -1) {
                    return pane.getTitleAt(i);
                }
                
                return null;
            }
        };
        
        add(label);
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 4));

        // On ajoute le bouton
        final JButton button = new TabButton();
        add(button);
        setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
    }

    private class TabButton extends JButton implements ActionListener {

        public TabButton() {
            int size = 16;
            setPreferredSize(new Dimension(size, size));
            setToolTipText("close this tab");
            setText("x");
            setHorizontalAlignment(CENTER);
            setVerticalAlignment(CENTER);

            setUI(new BasicButtonUI());
            setContentAreaFilled(false);        // Pas de fond
    //        setFocusable(false);
            setBorder(BorderFactory.createEtchedBorder());
            setBorderPainted(false);
            addActionListener(this);

            addMouseListener(new MouseAdapter() {

                @Override
                public void mouseEntered(MouseEvent e) {
                    setBorderPainted(true);
                    setForeground(Color.red);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    setBorderPainted(false);
                    setForeground(Color.black);
                }
    });

        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int i = pane.indexOfTabComponent(ClosableTabComponent.this);
            
            if (i != -1) pane.remove(i);
        }
    }
}

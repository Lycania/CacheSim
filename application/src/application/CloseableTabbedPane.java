/*
 * Copyright (C) 2015 maignial
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
package application;

import java.awt.Component;
import java.awt.FlowLayout;
import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author maignial
 */
public class CloseableTabbedPane extends javax.swing.JTabbedPane{
    /* Permet d'acceder à ce tabbed pane depuis les classes internes */
    private final CloseableTabbedPane moi;
    private final List<CloseTabPanel> tabs;
    
    public CloseableTabbedPane() {
        super();
        this.tabs = new ArrayList<>();
        moi = this;
    }
    
    /**
     * Permet de trouver l'index d'un onglet via son contenu
     * @param cPanel le panneau de l'onglet recherché
     * @return l'index de l'onglet recherché
     */
    public int getTabIndex(CloseTabPanel cPanel) {
        return this.tabs.indexOf(cPanel);
    }

    /**
     * Ajoute un onglet.
     * @param title le titre de l'onglet
     * @param component le composant désigné par l'onglet
     */
    @Override
    public void addTab(String title, Component component) {
        
        super.addTab(title, component);
        
        /* On applique le CloseTabPanel à l'onglet créé */
        int index = super.getTabCount() - 1;
        CloseTabPanel cPanel = new CloseTabPanel(title + "  ");
        super.setTabComponentAt(index, cPanel);
        tabs.add(cPanel);
    }

    /**
     * Le panneau contenu dans l'onglet fermable.
     */
    class CloseTabPanel extends JPanel{
 
        public CloseTabPanel(String titre) {
            super(new FlowLayout(FlowLayout.LEFT, 0, 0));
            setOpaque(false);
            JLabel label = new JLabel(titre);
            add(label);
            add(new TabButton());
            setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
	}
    }
    
    /**
     * Le bouton de fermeture de l'onglet.
     */
    class TabButton extends JButton implements ActionListener {
        
        public TabButton() {
            setToolTipText("Close this tab");
            setText("X");
            setUI(new BasicButtonUI());
            setBorder(BorderFactory.createEtchedBorder());
            setBorderPainted(true);
            addActionListener(this);
        }

        /**
         * Ferme l'onglet lors du clic sur le bouton fermer
         * @param e evenement
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            int index = moi.getTabIndex((CloseTabPanel)this.getParent());
            moi.remove(index);
            moi.tabs.remove(index);;
        }
    }
}

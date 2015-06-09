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

import application.model.DataTableModel;
import application.model.TagTableModel;
import application.ui.VerticalLabelUI;
import cache.Block;
import cache.Cache;
import cache.DirectMappedCache;
import cache.FullAssociativeCache;
import cache.SetAssociativeCache;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JScrollBar;

/**
 *
 * @author leo
 */
public class CacheVisualisator1 extends javax.swing.JPanel {
    private enum State {BASIC, COMPLEX};
    
    private State state = State.BASIC;
    private TagTableModel tagModelSet;
    private DataTableModel dataModelSet;
    private Cache cacheTovisualise;
    
    /**
     * Creates new form CacheVisualisator
     */
    public CacheVisualisator1() {
        initComponents();
        initView();
    }
    
    private void initView() {
        if (state == State.BASIC) {
            add(basicView);
            
        } else {
            add(setAssoaciativeCacheView);
        }
    }
    
    public void changeMethod(TagTableModel.Method m) {
        if (tagModelSet != null && dataModelSet != null) {
            tagModelSet.changeMethod(m);
            dataModelSet.changeMethod(m);
        }
    }
    
    public void toggleWrite() {
        if (tagModelSet != null && dataModelSet != null) {
//            tagModelSet.toggleWrite();
            dataModelSet.toggleWrite();
        }
    }
    
    public int getLineVisible() {
        // Le nombre de vue visible dépend de la vue en cour
        if (state == State.BASIC) return (basicView.getHeight() / 16) - 1;
        return (setAssoaciativeCacheView.getHeight() / 16) - 1;
    }
    
    public void setRegion(int percent, int gap) {
        final int ratio = 16 / gap;
        
        // Encore une fois il faut faire attention à la vue en cours
        JScrollBar YTagBar = null, YDataBar = null;
        switch (state) {
            case BASIC:
                YTagBar = basicView.leftScrollPane.getVerticalScrollBar();
                YDataBar = basicView.rightScrollPane.getVerticalScrollBar();
                break;
                
            case COMPLEX:
                // Ici il faut récupérer les scrollBars du layers que nous somme
                // en train d'afficher
                YTagBar = ((CacheVisualisator_SubView) setTabbedPane.
                        getSelectedComponent()).
                        leftScrollPane.getVerticalScrollBar();
                YDataBar = ((CacheVisualisator_SubView) setTabbedPane.
                        getSelectedComponent()).rightScrollPane.
                        getVerticalScrollBar();
        }
        
        if (YTagBar != null && YDataBar != null) {
            YTagBar.setValue(percent * ratio);
            YDataBar.setValue(percent * ratio);
        }
    }
    
    public Cache getCache() {
        // Si on est dans la vue basique, on récupère le cache qui lui est associé
        // SInon on récupére le cache correspondant à l'onglet selectionné
        return cacheTovisualise;
    }
    
    public void setCache(Cache c) {
        System.out.println(state.toString());
        // On construit le visualisateur en fonction du cache à afficher
        if ((c instanceof DirectMappedCache) || (c instanceof FullAssociativeCache))
            buildClassicView(c);
        
        else 
            buildSetAssociativeCacheView((SetAssociativeCache) c);
    }
    
    private void buildClassicView(Cache c) {
        // On change la vue que si nécessaire mais on n'oublie pas de la mettre
        // à jour
        if (state == State.BASIC) {
            cacheTovisualise = c;
            basicView.cacheToVisualize = c;

            basicView.tagModel = new TagTableModel(cacheTovisualise);
            basicView.dataModel = new DataTableModel(cacheTovisualise);
            basicView.tagTable.setModel(basicView.tagModel);
            basicView.dataTable.setModel(basicView.dataModel);

            dataModelSet = basicView.dataModel;
            tagModelSet = basicView.tagModel;
            
            basicView.tagModel.update();
            basicView.dataModel.update();
            
            return;
        }
        
        state = State.BASIC;
        // On supprimer le master élément de la vue complex pour dégager le panel
        remove(setAssoaciativeCacheView);
        
        // On ajoute la vue classic et on configure ses éléments
        add(basicView);
        cacheTovisualise = c;
        basicView.cacheToVisualize = (DirectMappedCache) c;
        
        basicView.tagModel = new TagTableModel(cacheTovisualise);
        basicView.dataModel = new DataTableModel(cacheTovisualise);
        basicView.tagTable.setModel(basicView.tagModel);
        basicView.dataTable.setModel(basicView.dataModel);

        dataModelSet = basicView.dataModel;
        tagModelSet = basicView.tagModel;

        basicView.tagModel.update();
        basicView.dataModel.update();
    }
    
    private void buildSetAssociativeCacheView(SetAssociativeCache c) {
        System.out.println("visualisation setAssociative cache");
        // Pour permettre la mise à jour, on reconstruit la vue quoi qu'il arrive.
        state = State.COMPLEX;
        
        // On vide le mainPanel et le tableau d'onglet d'affichage
        removeAll();
        setTabbedPane.removeAll();
        
        // On ajoute la vue complexe et on créer autant de panneau contenue la
        // subView qu'il y a de cadre
        add(setAssoaciativeCacheView);
        cacheTovisualise = c;
        
        int compteur = 0;
        System.out.println(c.getCadres().size());
        for (DirectMappedCache dmc : c.getCadres()) {
            CacheVisualisator_SubView newSubView = new CacheVisualisator_SubView();
            newSubView.cacheToVisualize = dmc;
            
            String title = "Set " + (compteur + 1);
            setTabbedPane.addTab(title, newSubView);
            
            JLabel lTitle = new JLabel(title);
            lTitle.setUI(new VerticalLabelUI());
            lTitle.setBorder(BorderFactory.createEmptyBorder(1, 6, 0, 6));
            setTabbedPane.setTabComponentAt(compteur++, lTitle);
            
            newSubView.tagModel = new TagTableModel(newSubView.cacheToVisualize);
            newSubView.dataModel = new DataTableModel(newSubView.cacheToVisualize);
            newSubView.tagTable.setModel(newSubView.tagModel);
            newSubView.dataTable.setModel(newSubView.dataModel);
            
            newSubView.tagModel.update();
            newSubView.dataModel.update();
        }
        
        CacheVisualisator_SubView sv = (CacheVisualisator_SubView) setTabbedPane.getComponentAt(setTabbedPane.getSelectedIndex());
        dataModelSet = sv.dataModel;
        tagModelSet = sv.tagModel;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setAssoaciativeCacheView = new javax.swing.JPanel();
        setTabbedPane = new javax.swing.JTabbedPane();
        basicView = new application.swing_utils.CacheVisualisator_SubView();

        setAssoaciativeCacheView.setLayout(new java.awt.GridLayout(1, 0));

        setTabbedPane.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        setTabbedPane.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        setTabbedPane.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                setTabbedPaneMouseClicked(evt);
            }
        });
        setAssoaciativeCacheView.add(setTabbedPane);

        setAutoscrolls(true);
        setLayout(new java.awt.GridLayout(1, 0));
    }// </editor-fold>//GEN-END:initComponents

    private void setTabbedPaneMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_setTabbedPaneMouseClicked
        // Si on clique sur l'un des onglet du panneau, il faut modifier les 
        // models courant utilisé par les tables pour qu'elles correspondent
        // au cache affiché. Nécessaire pour la méthode getInfo
        CacheVisualisator_SubView sv = (CacheVisualisator_SubView) setTabbedPane.getComponentAt(setTabbedPane.getSelectedIndex());
        dataModelSet = sv.dataModel;
        tagModelSet = sv.tagModel;
    }//GEN-LAST:event_setTabbedPaneMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private application.swing_utils.CacheVisualisator_SubView basicView;
    private javax.swing.JPanel setAssoaciativeCacheView;
    public javax.swing.JTabbedPane setTabbedPane;
    // End of variables declaration//GEN-END:variables

    public List<Block> getInfo() {
        return tagModelSet.getData();
    }
}

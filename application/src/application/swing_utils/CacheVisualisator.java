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
import application.model.LineInfo;
import application.model.TagTableModel;
import application.renderer.DataTableRenderer;
import application.renderer.MyTableHeaderRenderer;
import application.renderer.TagTableRenderer;
import cache.Block;
import cache.Cache;
import cache.DirectMappedCache;
import cache.FullAssociativeCache;
import cache.SetAssociativeCache;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JScrollBar;
import javax.swing.JTabbedPane;

/**
 *
 * @author leo
 */
public class CacheVisualisator extends javax.swing.JPanel {
    private Cache cacheToVisualize = null;
    private TagTableModel tagModel = null;
    private DataTableModel dataModel = null;
    
    /**
     * Creates new form CacheVisualisator
     */
    public CacheVisualisator() {
        initComponents();
        initTable();
    }
    
    private void initTable() {
        if (cacheToVisualize != null) {
            tagModel = new TagTableModel(cacheToVisualize);
            dataModel = new DataTableModel(cacheToVisualize);
        }
        
        tagTable.setDefaultRenderer(Object.class, new TagTableRenderer());
        dataTable.setDefaultRenderer(Object.class, new DataTableRenderer());
        tagTable.getTableHeader().setDefaultRenderer(new MyTableHeaderRenderer());
        dataTable.getTableHeader().setDefaultRenderer(new MyTableHeaderRenderer());
    }
    
    public void changeMethod(TagTableModel.Method m) {
        if (tagModel != null && dataModel != null) {
            tagModel.changeMethod(m);
            dataModel.changeMethod(m);
        }
    }
    
    public void toggleWrite() {
        if (tagModel != null && dataModel != null) {
            tagModel.toggleWrite();
            dataModel.toggleWrite();
        }
    }
    
    public int getLineVisible() {
        return (jScrollPane1.getHeight() / 16) - 1;
    }
    
    public void setRegion(int percent, int gap) {
        final int ratio = 16 / gap;
        
        JScrollBar YTagBar = jScrollPane1.getVerticalScrollBar();
        JScrollBar YDataBar = jScrollPane2.getVerticalScrollBar();
        YTagBar.setValue(percent * ratio);
        YDataBar.setValue(percent * ratio);
    }
    
    public Cache getCache() {
        return cacheToVisualize;
    }
    
    public void setCache(Cache c) {
        LineInfo info;

        if (c instanceof DirectMappedCache) {
           cacheToVisualize = (DirectMappedCache) c;
           tagModel = new TagTableModel(cacheToVisualize);
           dataModel = new DataTableModel(cacheToVisualize);
           tagTable.setModel(tagModel);
           dataTable.setModel(dataModel);
        }
        
        else if (c instanceof FullAssociativeCache) {
            cacheToVisualize = (FullAssociativeCache) c;
            tagModel = new TagTableModel(cacheToVisualize);
            dataModel = new DataTableModel(cacheToVisualize);
            tagTable.setModel(tagModel);
            dataTable.setModel(dataModel);
        }
        
        else if (c instanceof SetAssociativeCache) {
            // Pour les cache associatif par ensemble, il faut un traitement
            // particulier. A savoir l'insertion d'un tableau permettant de sé
            // lectionner un ensemble.
            cacheToVisualize = (SetAssociativeCache) c;
            final int nbSet = ((SetAssociativeCache) c).setCount();
            
            // On prépare le tableau d'onglet qui va accueillir chacun de ces cadres
            JTabbedPane setPane = new JTabbedPane();
            
            // On ajoute autant de panneau qu'il y a de cadre, sans oublier
            // d'écrire les nom à la verticales et de leur insérier à chacun 
            // un cacheVisualisator relier au directMappedCache correspondant
            int compteur = 0;
            /*
            for (DirectMappedCache dmc : ((SetAssociativeCache) c).getCadres()) {
                String title = "L" + (compteur + 1);
                
                // On construit le composant servant pour l'onglet
                JLabel lTitle = new JLabel(title);
                lTitle.setUI(new VerticalLabelUI());
                
                // On construit le visualisateur et on l'associe au bon cache
                CacheVisualisator cv = new CacheVisualisator();
                cv.setCache(dmc);
                
                // On ajoute ce visualisateur, modifie sont onglet et passe au suivant
                setPane.addTab(title, cv);
                setPane.setTabComponentAt(compteur++, lTitle);
            }
            */
            
            // on supprimer le JSplitPane présent et on le remplace par notre
            // JTabbedPane
            
            remove(jSplitPane1);
            add(setPane);
            setPane.setVisible(true);
            
            tagModel = new TagTableModel(cacheToVisualize);
            dataModel = new DataTableModel(cacheToVisualize);
            tagTable.setModel(tagModel);
            dataTable.setModel(dataModel);
        }
        
        tagModel.update();
        dataModel.update();
        
        // Ajustement taille des colonnes en fonctions du nombre de byte à afficher
        if (c instanceof DirectMappedCache) {

            if (tagTable.getColumnModel().getColumnCount() > 0) {
                tagTable.getColumnModel().getColumn(0).setMaxWidth(18);
                tagTable.getColumnModel().getColumn(0).setMinWidth(18);

                final int tagLong = tagModel.tagBitLong;
                tagTable.getColumnModel().getColumn(1).setMaxWidth(12+8 * tagLong);
                tagTable.getColumnModel().getColumn(1).setMinWidth(12+8 * tagLong);

                final int indexLong = tagModel.indexBitLong;
                tagTable.getColumnModel().getColumn(2).setMaxWidth(12+8 * indexLong);
                tagTable.getColumnModel().getColumn(2).setMinWidth(12+8 * indexLong);

                final int offsetLong = tagModel.offsetBitLong;
                tagTable.getColumnModel().getColumn(3).setMaxWidth(12 +8 * offsetLong);
                tagTable.getColumnModel().getColumn(3).setMinWidth(12 +8 * offsetLong);

                final int width = 16 + 18 + 32 + 8 * indexLong + 8 * offsetLong + 8 * tagLong;
                jSplitPane1.setDividerLocation(width);
            }

        } else if (c instanceof FullAssociativeCache) {
             if (tagTable.getColumnModel().getColumnCount() > 0) {
                tagTable.getColumnModel().getColumn(0).setMaxWidth(18);
                tagTable.getColumnModel().getColumn(0).setMinWidth(18);

                final int tagLong = tagModel.tagBitLong;
                tagTable.getColumnModel().getColumn(1).setMaxWidth(12+8 * tagLong);
                tagTable.getColumnModel().getColumn(1).setMinWidth(12+8 * tagLong);

                final int indexLong = tagModel.indexBitLong;
                tagTable.getColumnModel().getColumn(2).setMaxWidth(12+8 * indexLong);
                tagTable.getColumnModel().getColumn(2).setMinWidth(12+8 * indexLong);

                final int width = 16 + 18 + 24 + 8 * indexLong + 8 * tagLong;
                jSplitPane1.setDividerLocation(width);
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tagTable = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        dataTable = new javax.swing.JTable();

        setAutoscrolls(true);

        jSplitPane1.setBorder(null);
        jSplitPane1.setDividerLocation(220);
        jSplitPane1.setDividerSize(6);

        jPanel1.setBackground(new java.awt.Color(155, 155, 155));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setBorder(null);
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane1.setPreferredSize(new java.awt.Dimension(20, 20));

        tagTable.setShowVerticalLines(false);
        jScrollPane1.setViewportView(tagTable);

        jPanel1.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jSplitPane1.setLeftComponent(jPanel1);

        jPanel2.setBackground(new java.awt.Color(155, 155, 155));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jScrollPane2.setBorder(null);
        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane2.setPreferredSize(new java.awt.Dimension(20, 20));
        jScrollPane2.setViewportView(dataTable);

        jPanel2.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jSplitPane1.setRightComponent(jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 785, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 348, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable dataTable;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTable tagTable;
    // End of variables declaration//GEN-END:variables

    public List<Block> getInfo() {
        return tagModel.getData();
    }
}

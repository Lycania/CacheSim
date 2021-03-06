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
import application.renderer.DataTableRenderer;
import application.renderer.MyTableHeaderRenderer;
import application.renderer.TagTableRenderer;
import cache.Cache;

/**
 *
 * @author leo
 */
public class CacheVisualisator_SubView extends javax.swing.JPanel {
    public Cache cacheToVisualize = null;
    public TagTableModel tagModel = null;
    public DataTableModel dataModel = null;

    /**
     * Creates new form CacheVisualisator_SubView
     */
    public CacheVisualisator_SubView() {
        initComponents();
        
        if (cacheToVisualize != null) {
            tagModel = new TagTableModel(cacheToVisualize);
            dataModel = new DataTableModel(cacheToVisualize);
        }
        
        tagTable.setDefaultRenderer(Object.class, new TagTableRenderer());
        dataTable.setDefaultRenderer(Object.class, new DataTableRenderer());
        tagTable.getTableHeader().setDefaultRenderer(new MyTableHeaderRenderer());
        dataTable.getTableHeader().setDefaultRenderer(new MyTableHeaderRenderer());
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
        leftScrollPane = new javax.swing.JScrollPane();
        tagTable = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        rightScrollPane = new javax.swing.JScrollPane();
        dataTable = new javax.swing.JTable();

        jSplitPane1.setDividerLocation(220);
        jSplitPane1.setDividerSize(6);

        jPanel1.setBackground(new java.awt.Color(155, 155, 155));
        jPanel1.setLayout(new java.awt.BorderLayout());

        leftScrollPane.setBorder(null);
        leftScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        leftScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        leftScrollPane.setPreferredSize(new java.awt.Dimension(20, 20));

        tagTable.setShowVerticalLines(false);
        leftScrollPane.setViewportView(tagTable);

        jPanel1.add(leftScrollPane, java.awt.BorderLayout.CENTER);

        jSplitPane1.setLeftComponent(jPanel1);

        jPanel2.setBackground(new java.awt.Color(155, 155, 155));
        jPanel2.setLayout(new java.awt.BorderLayout());

        rightScrollPane.setBorder(null);
        rightScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        rightScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        rightScrollPane.setPreferredSize(new java.awt.Dimension(20, 20));
        rightScrollPane.setViewportView(dataTable);

        jPanel2.add(rightScrollPane, java.awt.BorderLayout.CENTER);

        jSplitPane1.setRightComponent(jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JTable dataTable;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSplitPane jSplitPane1;
    public javax.swing.JScrollPane leftScrollPane;
    public javax.swing.JScrollPane rightScrollPane;
    public javax.swing.JTable tagTable;
    // End of variables declaration//GEN-END:variables
}

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

import application.model.TagTableModel;
import cache.Cache;

/**
 *
 * @author leo
 */
public class MemoryViewer extends javax.swing.JPanel {

    /**
     * Creates new form MemoryViewer
     */
    public MemoryViewer() {
        initComponents();
    }
    
    public void setCache(Cache c) {
        System.out.println("instance of c : " + c.getClass().toString());
        cacheVisualisator1.setCache(c);
        scrollViewer1.linkTo(cacheVisualisator1);
        update();
    }
    
    public Cache getCache() {
        return cacheVisualisator1.getCache();
    }
    
    public void update() {
        scrollViewer1.update();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        affichageGroupButton = new javax.swing.ButtonGroup();
        scrollViewer1 = new application.swing_utils.ScrollViewer();
        jPanel1 = new javax.swing.JPanel();
        hexMethodButton = new javax.swing.JButton();
        binMethodButton = new javax.swing.JButton();
        decMethodButton = new javax.swing.JButton();
        jToggleButton1 = new javax.swing.JToggleButton();
        cacheVisualisator1 = new application.swing_utils.CacheVisualisator1();

        setLayout(new java.awt.BorderLayout());

        scrollViewer1.setLayout(new java.awt.GridLayout());
        add(scrollViewer1, java.awt.BorderLayout.LINE_START);

        jPanel1.setLayout(new java.awt.GridBagLayout());

        hexMethodButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/hexa.png"))); // NOI18N
        hexMethodButton.setBorderPainted(false);
        affichageGroupButton.add(hexMethodButton);
        hexMethodButton.setContentAreaFilled(false);
        hexMethodButton.setMargin(new java.awt.Insets(1, 0, 0, 1));
        hexMethodButton.setMaximumSize(new java.awt.Dimension(24, 22));
        hexMethodButton.setMinimumSize(new java.awt.Dimension(24, 22));
        hexMethodButton.setPreferredSize(new java.awt.Dimension(24, 22));
        hexMethodButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hexMethodButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(1, 19, 2, 2);
        jPanel1.add(hexMethodButton, gridBagConstraints);

        binMethodButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/Bina.png"))); // NOI18N
        binMethodButton.setBorderPainted(false);
        affichageGroupButton.add(binMethodButton);
        binMethodButton.setContentAreaFilled(false);
        binMethodButton.setMargin(new java.awt.Insets(1, 0, 0, 1));
        binMethodButton.setMaximumSize(new java.awt.Dimension(24, 22));
        binMethodButton.setMinimumSize(new java.awt.Dimension(24, 22));
        binMethodButton.setPreferredSize(new java.awt.Dimension(24, 22));
        binMethodButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                binMethodButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 2, 2);
        jPanel1.add(binMethodButton, gridBagConstraints);

        decMethodButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/deci.png"))); // NOI18N
        decMethodButton.setBorderPainted(false);
        affichageGroupButton.add(decMethodButton);
        decMethodButton.setContentAreaFilled(false);
        decMethodButton.setMargin(new java.awt.Insets(1, 0, 0, 1));
        decMethodButton.setMaximumSize(new java.awt.Dimension(24, 22));
        decMethodButton.setMinimumSize(new java.awt.Dimension(24, 22));
        decMethodButton.setPreferredSize(new java.awt.Dimension(24, 22));
        decMethodButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                decMethodButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 2, 2);
        jPanel1.add(decMethodButton, gridBagConstraints);

        jToggleButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/toggleText.png"))); // NOI18N
        jToggleButton1.setBorderPainted(false);
        jToggleButton1.setContentAreaFilled(false);
        jToggleButton1.setMargin(new java.awt.Insets(1, 0, 0, 1));
        jToggleButton1.setMaximumSize(new java.awt.Dimension(24, 22));
        jToggleButton1.setMinimumSize(new java.awt.Dimension(24, 22));
        jToggleButton1.setPreferredSize(new java.awt.Dimension(24, 22));
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 48, 0, 0);
        jPanel1.add(jToggleButton1, gridBagConstraints);

        add(jPanel1, java.awt.BorderLayout.PAGE_START);
        add(cacheVisualisator1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void hexMethodButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hexMethodButtonActionPerformed
        cacheVisualisator1.changeMethod(TagTableModel.Method.HEXA);
    }//GEN-LAST:event_hexMethodButtonActionPerformed

    private void binMethodButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_binMethodButtonActionPerformed
        cacheVisualisator1.changeMethod(TagTableModel.Method.BIN);
    }//GEN-LAST:event_binMethodButtonActionPerformed

    private void decMethodButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_decMethodButtonActionPerformed
        cacheVisualisator1.changeMethod(TagTableModel.Method.DEC);
    }//GEN-LAST:event_decMethodButtonActionPerformed

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        cacheVisualisator1.toggleWrite();
    }//GEN-LAST:event_jToggleButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup affichageGroupButton;
    private javax.swing.JButton binMethodButton;
    private application.swing_utils.CacheVisualisator1 cacheVisualisator1;
    private javax.swing.JButton decMethodButton;
    private javax.swing.JButton hexMethodButton;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JToggleButton jToggleButton1;
    private application.swing_utils.ScrollViewer scrollViewer1;
    // End of variables declaration//GEN-END:variables
}

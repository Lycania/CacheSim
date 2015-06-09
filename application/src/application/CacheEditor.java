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
package application;

import Utils.Numbers;
import application.swing_utils.MemoryViewer;
import application.swing_utils.MyTabbedPane;
import cache.Cache;
import cache.Controler;
import cache.DirectMappedCache;
import cache.FullAssociativeCache;
import cache.Layer;
import cache.Ram;
import cache.SetAssociativeCache;
import cache.scheduler.Scheduler;
import cache.type.PolicyType;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTabbedPane;

/**
 *
 * @author leo
 */
public class CacheEditor extends javax.swing.JFrame {
    private int jSlider1CurrentValue = 32;
    private int jSlider2CurrentValue = 4;
    private int jSlider3CurrentValue = 4;
    private MemoryViewer connected;
    private JTabbedPane currentLayer;
    private final CacheSimulator master;
    
    // Variable pour le binding
    private String      formatS, policyS, typeS, schedulerS;
    private PolicyType  policy;
    int                 scheduler;
    cache.type.Type     type;
    MemoryViewer        view;

    /**
     * Creates new form CacheEditor
     * @param master
     */
    public CacheEditor(CacheSimulator master) {
        this.master = master;
        
        initComponents();
        initProperties();
        initTabbedPane();
        
//        cacheVisualisator1.setCache(new FullyAssociativeCache(cache.type.Type.DATA, Scheduler.FIFO, PolicyType.DIRECT, 16, 4, 4));
//        scrollViewer2.linkTo(cacheVisualisator1);
    }
    
    private void initProperties() {
        // liaison avec leur panel
        collapse1.linkTo(collapsePanel1);
        collapse2.linkTo(collapsePanel2);
        
        collapse1.setTitle("Properties");
        collapse2.setTitle("size");
    }
    
    private void initTabbedPane() {
        // Ajouter un layer
        // Génération du memoryViewer avec les propriété donné par l'utilisateur
        MemoryViewer v = generate();
        connected = v;
        
        // Création du composant pour le nouveau layer
        MyTabbedPane subPane = createSubTabbedPane();
        
        // insertion
        String name = "L" + (layersTabbedPane.insideSize() + 1);
        layersTabbedPane.add(name, subPane);
        labelInfo.setText("ajouter avec succès");
        labelInfo.setForeground(Color.black);
    }
    
    private MyTabbedPane createSubTabbedPane() {
        final MyTabbedPane out = new MyTabbedPane();
        
        out.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                connected = (MemoryViewer) out.getComponentAt(out.getSelectedIndex());
                
                jTextField1.setText(Integer.toString(connected.getCache().size()));
                jTextField2.setText(Integer.toString(connected.getCache().getBlockSize()));
            }
            
        });
        
        return out;
    }
    
    private MemoryViewer generate() {
        // Récupération de tout les attribus du cache
        getProperties();
        
        // Création du cache et mise à jour de l'affichage 
        Cache toConnect = generateCache();
        view = new MemoryViewer();
        view.setCache(toConnect);
        connected = view;
        
        return view;
    }
    
    private Cache generateCache() {
        // Récupération de tout les attribus du cache
        getProperties();
        
        Cache c;
        // Création du cache et mise à jour de l'affichage 
        if (formatS.equals("Direct mapped cache"))
            c = new DirectMappedCache(
                    type, policy, jSlider1CurrentValue, 
                    jSlider2CurrentValue, 4);
        else if (formatS.equals("Fully associative cache"))
            c = new FullAssociativeCache(type, scheduler, policy,
                    jSlider1CurrentValue, jSlider2CurrentValue, 4);
        else
            c = new SetAssociativeCache(type, scheduler, policy,
                    jSlider1CurrentValue, jSlider3CurrentValue, jSlider2CurrentValue, 4);
        
        return c;
    }
    
    private void getProperties() {
        // Récupération de tout les attribus du cache
        formatS = formatCombo.getSelectedItem().toString();
        policyS = policyCombo.getSelectedItem().toString();
        typeS   = typeCombo.getSelectedItem().toString();
        schedulerS = schedulerCombo.getSelectedItem().toString();

        
        if (policyS.equals("Direct write")) policy = PolicyType.DIRECT;
        else policy = PolicyType.DELAYED;
         
        if      (typeS.equals("Data")) type = cache.type.Type.DATA;
        else if (typeS.equals("Instruction")) type = cache.type.Type.INSTRUCTION;
        else    type = cache.type.Type.UNITED;
        
        if (schedulerS.equals("FIFO")) scheduler = Scheduler.FIFO;
        else if (schedulerS.equals("LIFO")) scheduler = Scheduler.LIFO;
        else if (schedulerS.equals("LRU")) scheduler = Scheduler.LRU;
        else if (schedulerS.equals("LFU")) scheduler = Scheduler.LFU;
        else if (schedulerS.equals("NMRU")) scheduler = Scheduler.NMRU;
        else scheduler = Scheduler.RANDOM;
        
        jSlider1CurrentValue = Numbers.nearest2Power(Integer.parseInt(jTextField1.getText()));
        jSlider2CurrentValue = Numbers.nearest2Power(Integer.parseInt(jTextField2.getText()));
        jSlider3CurrentValue = Numbers.nearest2Power(Integer.parseInt(jTextField3.getText()));
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

        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel11 = new javax.swing.JPanel();
        collapse1 = new application.swing_utils.CollapsePanel();
        collapse2 = new application.swing_utils.CollapsePanel();
        collapsePanel1 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        schedulerCombo = new javax.swing.JComboBox();
        typeCombo = new javax.swing.JComboBox();
        policyCombo = new javax.swing.JComboBox();
        formatCombo = new javax.swing.JComboBox();
        jPanel4 = new javax.swing.JPanel();
        collapsePanel2 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jPanel13 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        infoPanel = new javax.swing.JPanel();
        labelInfo = new javax.swing.JLabel();
        layersTabbedPane = new application.swing_utils.MyDragableTabbedPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jSplitPane1.setDividerLocation(200);
        jSplitPane1.setDividerSize(8);

        jScrollPane1.setBorder(null);
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jPanel11.setMinimumSize(new java.awt.Dimension(100, 48));
        jPanel11.setLayout(new java.awt.GridBagLayout());

        collapse1.setMinimumSize(new java.awt.Dimension(10, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        jPanel11.add(collapse1, gridBagConstraints);

        collapse2.setMinimumSize(new java.awt.Dimension(10, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        jPanel11.add(collapse2, gridBagConstraints);

        collapsePanel1.setMinimumSize(new java.awt.Dimension(10, 140));
        collapsePanel1.setPreferredSize(new java.awt.Dimension(100, 140));
        collapsePanel1.setLayout(new java.awt.GridBagLayout());

        jLabel5.setText("Format ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 0, 0);
        collapsePanel1.add(jLabel5, gridBagConstraints);

        jLabel8.setText("Policy");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 0);
        collapsePanel1.add(jLabel8, gridBagConstraints);

        jLabel9.setText("Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 0);
        collapsePanel1.add(jLabel9, gridBagConstraints);

        jLabel10.setText("Scheduleur");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 12);
        collapsePanel1.add(jLabel10, gridBagConstraints);

        schedulerCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "FIFO", "LIFO", "LRU", "LFU", "NMRU", "RANDOM" }));
        schedulerCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                schedulerComboActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 0, 0, 0);
        collapsePanel1.add(schedulerCombo, gridBagConstraints);

        typeCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Data", "Instruction", "United" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 0, 0, 0);
        collapsePanel1.add(typeCombo, gridBagConstraints);

        policyCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Direct write", "delayed write" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 0, 0);
        collapsePanel1.add(policyCombo, gridBagConstraints);

        formatCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Direct mapped cache", "Fully associative cache", "Set associative cache" }));
        formatCombo.setMinimumSize(new java.awt.Dimension(100, 23));
        formatCombo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                formatComboItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 0, 0);
        collapsePanel1.add(formatCombo, gridBagConstraints);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        collapsePanel1.add(jPanel4, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        jPanel11.add(collapsePanel1, gridBagConstraints);

        collapsePanel2.setMinimumSize(new java.awt.Dimension(10, 74));
        collapsePanel2.setPreferredSize(new java.awt.Dimension(100, 74));
        java.awt.GridBagLayout collapsePanel2Layout = new java.awt.GridBagLayout();
        collapsePanel2Layout.rowHeights = new int[] {0};
        collapsePanel2Layout.rowWeights = new double[] {0.0};
        collapsePanel2.setLayout(collapsePanel2Layout);

        jLabel11.setText("block's size");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        collapsePanel2.add(jLabel11, gridBagConstraints);

        jLabel12.setText("number of block");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 6, 13);
        collapsePanel2.add(jLabel12, gridBagConstraints);

        jTextField1.setText("32");
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField1KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        collapsePanel2.add(jTextField1, gridBagConstraints);

        jTextField2.setText("4");
        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField2KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        collapsePanel2.add(jTextField2, gridBagConstraints);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        collapsePanel2.add(jPanel2, gridBagConstraints);

        jLabel1.setText("set count");
        jLabel1.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 8, 0);
        collapsePanel2.add(jLabel1, gridBagConstraints);

        jTextField3.setText("2");
        jTextField3.setEnabled(false);
        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        collapsePanel2.add(jTextField3, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        jPanel11.add(collapsePanel2, gridBagConstraints);

        jPanel13.setMinimumSize(new java.awt.Dimension(10, 48));
        jPanel13.setPreferredSize(new java.awt.Dimension(100, 48));
        jPanel13.setLayout(new java.awt.GridBagLayout());

        jButton1.setText("Sauvegarder");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 0.1;
        jPanel13.add(jButton1, gridBagConstraints);

        jButton3.setText("new layers");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        jPanel13.add(jButton3, gridBagConstraints);

        jButton4.setText("new cache");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel13.add(jButton4, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LAST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel11.add(jPanel13, gridBagConstraints);

        jScrollPane1.setViewportView(jPanel11);

        jSplitPane1.setLeftComponent(jScrollPane1);

        jPanel1.setLayout(new java.awt.BorderLayout());

        infoPanel.setMaximumSize(new java.awt.Dimension(32767, 24));
        infoPanel.setMinimumSize(new java.awt.Dimension(100, 24));
        infoPanel.setPreferredSize(new java.awt.Dimension(961, 24));
        infoPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        labelInfo.setText("jLabel1");
        infoPanel.add(labelInfo);

        jPanel1.add(infoPanel, java.awt.BorderLayout.PAGE_END);

        layersTabbedPane.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                layersTabbedPaneMouseClicked(evt);
            }
        });
        jPanel1.add(layersTabbedPane, java.awt.BorderLayout.CENTER);

        jSplitPane1.setRightComponent(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 507, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void generateControler() {
        List<Layer> layers = new ArrayList<>(layersTabbedPane.insideSize());
        
        // On va parcourir l'ensemble de nos layers pour en récupérer le contenu
        for (int i = 0; i < layersTabbedPane.getTabCount(); ++i) {
            MyTabbedPane subPane = (MyTabbedPane) layersTabbedPane.getComponentAt(i);
            
            switch (subPane.getTabCount()) {
                // Aucun cache dans un layer ? Erreur de conception
                case 0: 
                    labelInfo.setText("Il est impossible de posséder un " +
                            "niveau de cache sans aucun cache dedans ! ");
                    labelInfo.setForeground(Color.red);
                    return;
                
                case 1:
                    layers.add(new Layer(((MemoryViewer) subPane.getComponentAt(0)).getCache()));
                    break;
                    
                case 2:
                    Cache c1 = ((MemoryViewer) subPane.getComponentAt(0)).getCache();
                    Cache c2 = ((MemoryViewer) subPane.getComponentAt(1)).getCache();
                    
                    if (c1.getType() == cache.type.Type.DATA)
                        layers.add(new Layer(c1, c2));
                    else 
                        layers.add(new Layer(c2, c1));
            }
        }
        
        // Ajout d'une ram pour défaut
        layers.add(new Layer(new Ram(512, 4)));
        
        // ---- Création du controleur de cache
        CacheSimulator.controler = new Controler(layers);
        master.refresh();
        labelInfo.setText("controler de cache créer avec succès");
        labelInfo.setForeground(Color.black);
    }
    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        generateControler();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextField2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyPressed
        switch (evt.getKeyCode()) {
            case KeyEvent.VK_ENTER:
            try {
                jSlider2CurrentValue = Numbers.nearest2Power(Integer.parseInt(jTextField2.getText()));
                jTextField2.setText(Integer.toString(jSlider2CurrentValue));
                jTextField2.setBackground(Color.GREEN);
                
                // On modifie les informations du cache qui est connecté
                connected.setCache(generateCache());

        } catch (NumberFormatException e) {
            jTextField2.setText("un nombre !");
            jTextField2.setBackground(Color.RED);
        }
        default: break;
        }
    }//GEN-LAST:event_jTextField2KeyPressed

    private void jTextField1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyPressed
        switch (evt.getKeyCode()) {
            case KeyEvent.VK_ENTER:
            try {
                jSlider1CurrentValue = Numbers.nearest2Power(Integer.parseInt(jTextField1.getText()));
                jTextField1.setText(Integer.toString(jSlider1CurrentValue));
                jTextField1.setBackground(Color.GREEN);

                connected.setCache(generateCache());

        } catch (NumberFormatException e) {
            jTextField1.setText("un nombre !");
            jTextField1.setBackground(Color.RED);
        }
        default: break;
        }
    }//GEN-LAST:event_jTextField1KeyPressed

    private void schedulerComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_schedulerComboActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_schedulerComboActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        String title = "";
        
        // Ajouter un cache au layers courant
        final int layer = layersTabbedPane.getSelectedIndex();
        final MyTabbedPane subPane = (MyTabbedPane) layersTabbedPane.getComponentAt(layer);
        
        // Génération du memoryViewer avec les propriété donné par l'utilisateur
        MemoryViewer view = generate();
        cache.type.Type toAdd = view.getCache().getType();
        connected = view;
        
        System.out.println(subPane.getComponentCount());
        
        // insertion du cache en fonction.
        // Si déjà présent : data. On ajouter un Instruction et vise-versa
        // Si united, impossible
        if (subPane.getTabCount() == 1) {
            for (Component c : subPane.getComponents()) {
                System.out.println(c.getClass().toString());
            }
            cache.type.Type alreadyUsed = ((MemoryViewer) subPane.getComponentAt(0)).getCache().getType();
            
            if (alreadyUsed == toAdd  || alreadyUsed == cache.type.Type.UNITED) {
                labelInfo.setText("Impossible de rajouter ce cache car il" +
                        " existe déja.");
                labelInfo.setForeground(Color.red);
                return;
            }
            
            else {
                if (toAdd == cache.type.Type.UNITED) return;
                else if (toAdd == cache.type.Type.INSTRUCTION) title = "Instruction";
                else if (toAdd == cache.type.Type.DATA) title = "Data";
            }
        }
        
        // SI il n'y a aucun cache
        if (subPane.getTabCount() == 0) {
            if (toAdd == cache.type.Type.DATA) title = "Data";
            else if (toAdd == cache.type.Type.INSTRUCTION) title = "Instruction";
            else if (toAdd == cache.type.Type.UNITED) title = "United";
        }
        
        // SI il y a déja deux cache, impossible d'en rajouter un.
        if (subPane.getTabCount() == 2) {
            labelInfo.setText("Impossible d'avoir plus de deux cache dans le " +
                    "même niveau.");
            labelInfo.setForeground(Color.red);
            return;
        }
        
        subPane.add(title, view);
        subPane.setSelectedIndex(subPane.indexOfComponent(connected));
        labelInfo.setText("ajouter avec succès");
        labelInfo.setForeground(Color.black);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // Ajouter un layer
        // Génération du memoryViewer avec les propriété donné par l'utilisateur
        MemoryViewer view = generate();
        connected = view;
        
        // Création du composant pour le nouveau layer
        MyTabbedPane subPane = createSubTabbedPane();
        
        // insertion
        String name = "L" + (layersTabbedPane.insideSize() + 1);
        layersTabbedPane.add(name, subPane);
        labelInfo.setText("ajouter avec succès");
        labelInfo.setForeground(Color.black);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void layersTabbedPaneMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_layersTabbedPaneMouseClicked
        System.out.println("event run");
        currentLayer = (MyTabbedPane) layersTabbedPane.getComponentAt(layersTabbedPane.getSelectedIndex());
        if (currentLayer.getSelectedIndex() != -1) 
            connected = (MemoryViewer) currentLayer.getComponentAt(currentLayer.getSelectedIndex());
    }//GEN-LAST:event_layersTabbedPaneMouseClicked

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3ActionPerformed

    private void formatComboItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_formatComboItemStateChanged
        if (formatCombo.getSelectedIndex() == 2) {
            jLabel1.setEnabled(true);
            jTextField3.setEnabled(true);
            
        } else {
            jLabel1.setEnabled(false);
            jTextField3.setEnabled(false);
        }
    }//GEN-LAST:event_formatComboItemStateChanged

    
//    /**
//     * @param args the command line arguments
//     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                System.out.println(info.getName());
//                if ("Metal".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
////                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(CacheEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(CacheEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(CacheEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(CacheEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new CacheEditor().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private application.swing_utils.CollapsePanel collapse1;
    private application.swing_utils.CollapsePanel collapse2;
    private javax.swing.JPanel collapsePanel1;
    private javax.swing.JPanel collapsePanel2;
    private javax.swing.JComboBox formatCombo;
    private javax.swing.JPanel infoPanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JLabel labelInfo;
    private application.swing_utils.MyDragableTabbedPane layersTabbedPane;
    private javax.swing.JComboBox policyCombo;
    private javax.swing.JComboBox schedulerCombo;
    private javax.swing.JComboBox typeCombo;
    // End of variables declaration//GEN-END:variables
}

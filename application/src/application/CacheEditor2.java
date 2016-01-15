/*
 * Copyright (C) 2016 leoca
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

import application.model.SpinnerPowerOfTwo;
import application.model.TagTableModel;

import cache.Cache;
import cache.Controler;
import cache.DirectMappedCache;
import cache.FullAssociativeCache;
import cache.Layer;
import cache.Ram;
import cache.SetAssociativeCache;
import cache.scheduler.SchedulerType;
import cache.type.PolicyType;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import javax.swing.UIManager;

/**
 *
 * @author leo
 */
public class CacheEditor2 extends javax.swing.JFrame {
    private String formatValue;
    private String policyValue;
    private String typeValue;
    private String schedulerValue;
    private String layerValue;
    
    private int nbBlock;
    private int blockSize;
    private int nbSet;
    
    private PolicyType policy;
    cache.type.Type    type;
    SchedulerType      scheduler;
    
    Cache L1Cache = null;
    Cache L2Cache = null;
    Cache L3Cache = null;
    Cache currentCache = L1Cache;
    
    //private final CacheSimulator master;
            

    /**
     * Creates new form CacheEditor2
     */
    public CacheEditor2() {
        initComponents();
        refresh();
    }
    
    private void getCacheInfo() {
        // Récupération de tout les attribus du cache
        formatValue     = formatCombo.getSelectedItem().toString();
        policyValue     = policyCombo.getSelectedItem().toString();
        typeValue       = typeCombo.getSelectedItem().toString();
        schedulerValue  = schedulerCombo.getSelectedItem().toString();
        layerValue      = layerCombo.getSelectedItem().toString();
        
        nbBlock         = (int) nbBlockSpinner.getValue();
        blockSize       = (int) blockSizeSpinner.getValue();
        nbSet           = (int) nbSetSpinner.getValue();
        
        if (policyValue.equals("Direct write")) policy = PolicyType.DIRECT;
        else policy = PolicyType.DELAYED;
         
        switch (typeValue) {
            case "Data":        type = cache.type.Type.DATA; break;
            case "Instruction": type = cache.type.Type.INSTRUCTION; break;
            case "Both":        type = cache.type.Type.UNITED; break;
            default:            type = cache.type.Type.DATA; break;
        }
        
        switch (schedulerValue) {
            case "FIFO": scheduler = SchedulerType.FIFO; break;
            case "LIFO": scheduler = SchedulerType.LIFO; break;
            case "LRU":  scheduler = SchedulerType.LRU;  break;
            case "LFU":  scheduler = SchedulerType.LFU;  break;
            case "NMRU": scheduler = SchedulerType.NMRU; break;
            default:     scheduler = SchedulerType.RANDOM; break;
        }
        
        switch (layerValue) {
            case "L1" : L1Cache = currentCache; break;
            case "L2" : L2Cache = currentCache; break;
            default   : L3Cache = currentCache; break;
        }
    }
    
    private void initCacheInfo() {
        nbBlockSpinner.setValue(1);
        blockSizeSpinner.setValue(1);
        nbSetSpinner.setValue(1);
        
        formatCombo.setSelectedIndex(0);
        typeCombo.setSelectedIndex(0);
        policyCombo.setSelectedIndex(0);
        schedulerCombo.setSelectedIndex(0);
    }
    
    private void setCacheInfo() {
        nbBlockSpinner.setValue(currentCache.size());
        blockSizeSpinner.setValue(currentCache.getBlockSize());
        
        if (currentCache instanceof SetAssociativeCache)
            nbSetSpinner.setValue((currentCache.size()));
        
        nbBlock         = (int) nbBlockSpinner.getValue();
        blockSize       = (int) blockSizeSpinner.getValue();
        nbSet           = (int) nbSetSpinner.getValue();
        
        switch (currentCache.getFormat()) {
            case DIRECT: formatCombo.setSelectedIndex(0); break;
            case FULL: formatCombo.setSelectedIndex(1); break;
            case SET: formatCombo.setSelectedIndex(2); break;
            default: break;
        }
        
        switch (currentCache.getType()) {
            case DATA: typeCombo.setSelectedIndex(1); break;
            case INSTRUCTION: typeCombo.setSelectedIndex(2); break;
            case UNITED: typeCombo.setSelectedIndex(3);
            default: break;
        }
        
        switch (currentCache.getPolicy()) {
            case DIRECT: policyCombo.setSelectedIndex(0); break;
            case DELAYED: policyCombo.setSelectedIndex(1); break;
            default: break;
        }
        
        
        switch (currentCache.getScheduler()) {
            case FIFO: schedulerCombo.setSelectedIndex(0);
            case LIFO: schedulerCombo.setSelectedIndex(1);
            case LRU: schedulerCombo.setSelectedIndex(2);
            case LFU: schedulerCombo.setSelectedIndex(3);
            case NMRU: schedulerCombo.setSelectedIndex(4);
            case RANDOM: schedulerCombo.setSelectedIndex(5);
            default: break;
        }
    }
    
    private Cache generateCache() {        
        if (! typeValue.equals("None")) {

            Cache c;

            // Création du cache et mise à jour de l'affichage
            switch (formatValue) {
                case "Direct mapped cache":
                    c = new DirectMappedCache(type, policy, nbBlock, blockSize, 4);
                    break;
                
                case "Fully associative cache":
                    c = new FullAssociativeCache(type, scheduler, policy, nbBlock,
                            blockSize, 4);
                    break;
                
                default:
                    c = new SetAssociativeCache(type, scheduler, policy, nbBlock, nbSet,
                            blockSize, 4);
                    break;
            }
            
            return c;
        }
        
        return null;
    }
    
    public void generateControler() {        
        List<Layer> layers = new ArrayList<>(4);
        
        if (L1Cache != null) layers.add(new Layer(L1Cache));
        if (L2Cache != null) layers.add(new Layer(L2Cache));
        if (L3Cache != null) layers.add(new Layer(L3Cache));
        
        layers.add(new Layer(new Ram(512, 4)));
        
         // ---- Création du controleur de cache
        CacheSimulator.controler = new Controler(layers);
    }
    
    private String buildSizeMsg(int size) {
        String start, end;
        int nb1024Mult = (int) (Math.log(size) / Math.log(1024));
        
        start = Integer.toString(size);
        if (nb1024Mult != 0)
            start = Integer.toString(size / (nb1024Mult * 1024));
        
        end = " o";
        if (nb1024Mult == 1) end = " Ko";
        if (nb1024Mult == 2) end = " Mo";
        if (nb1024Mult == 3) end = " Go";
        if (nb1024Mult == 4) end = " To";
        if (nb1024Mult == 5) end = " Po";
        
        return start + end;
    }
    
    private void refresh() {
        setInfo("Récupération des informations du cache", Color.blue);
        getCacheInfo();
        setInfo("Génération des caches", Color.blue);
        Cache c = generateCache();
        setInfo("création du controler", Color.blue);
        generateControler();
        setInfo("controler de cache créer avec succès", Color.green);
        
        if (c != null) {
            view.setCache(c);
            currentCache = c;

            String sizeMsg = buildSizeMsg(nbBlock * blockSize * 4);

            labelCacheLayerInfo.setText("cache de niveau " + this.layerValue);
            labelCacheTypeInfo.setText(this.typeValue);
            labelCacheInfoSize.setText(sizeMsg);
        
        }
    }
    
    private void setInfo(String msg, Color color) {
        labelInfo.setText(msg);
        labelInfo.setForeground(color);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel5 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        infoPanel = new javax.swing.JPanel();
        labelCacheLayerInfo = new javax.swing.JLabel();
        labelCacheTypeInfo = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        labelCacheInfoSize = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        setLabel = new javax.swing.JLabel();
        formatCombo = new javax.swing.JComboBox();
        policyCombo = new javax.swing.JComboBox();
        typeCombo = new javax.swing.JComboBox();
        schedulerCombo = new javax.swing.JComboBox();
        nbBlockSpinner = new javax.swing.JSpinner(new SpinnerPowerOfTwo());
        blockSizeSpinner = new javax.swing.JSpinner(new SpinnerPowerOfTwo());
        nbSetSpinner = new javax.swing.JSpinner(new SpinnerPowerOfTwo());
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        layerCombo = new javax.swing.JComboBox<>();
        buttonHexMethod = new javax.swing.JButton();
        buttonBinMethod = new javax.swing.JButton();
        buttonDecMethod = new javax.swing.JButton();
        jToggleButton1 = new javax.swing.JToggleButton();
        jLabel1 = new javax.swing.JLabel();
        view = new application.swing_utils.MemoryViewer();
        pannelInfo = new javax.swing.JPanel();
        labelInfo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(225, 225, 225));

        jPanel5.setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(225, 225, 225));

        infoPanel.setBackground(new java.awt.Color(225, 225, 225));
        infoPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(200, 200, 200)));

        labelCacheLayerInfo.setText("Cache de niveau L1");

        labelCacheTypeInfo.setText("jLabel11");

        jLabel8.setText("Size");

        labelCacheInfoSize.setText("jLabel10");

        javax.swing.GroupLayout infoPanelLayout = new javax.swing.GroupLayout(infoPanel);
        infoPanel.setLayout(infoPanelLayout);
        infoPanelLayout.setHorizontalGroup(
            infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(infoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(infoPanelLayout.createSequentialGroup()
                        .addComponent(labelCacheLayerInfo)
                        .addGap(18, 18, 18)
                        .addComponent(labelCacheTypeInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(infoPanelLayout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(18, 18, 18)
                        .addComponent(labelCacheInfoSize)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        infoPanelLayout.setVerticalGroup(
            infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(infoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelCacheLayerInfo)
                    .addComponent(labelCacheTypeInfo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(labelCacheInfoSize))
                .addContainerGap(90, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(225, 225, 225));

        jLabel2.setText("Format");

        jLabel3.setText("Policy");

        jLabel4.setText("Type");

        jLabel5.setText("Scheduler");

        jLabel6.setText("Number block");

        jLabel7.setText("block's size");

        setLabel.setText("set count");

        formatCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Direct mapped cache", "Fully associative cache", "Set associative cache" }));
        formatCombo.setMinimumSize(new java.awt.Dimension(100, 23));
        formatCombo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                formatComboItemStateChanged(evt);
            }
        });

        policyCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Direct write", "delayed write" }));
        policyCombo.setMinimumSize(new java.awt.Dimension(100, 23));
        policyCombo.setPreferredSize(new java.awt.Dimension(134, 20));
        policyCombo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                policyComboItemStateChanged(evt);
            }
        });

        typeCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "None", "Data", "Instruction", "United" }));
        typeCombo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                typeComboItemStateChanged(evt);
            }
        });

        schedulerCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "FIFO", "LIFO", "LRU", "LFU", "NMRU", "RANDOM" }));
        schedulerCombo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                schedulerComboItemStateChanged(evt);
            }
        });

        nbBlockSpinner.setValue(1);
        nbBlockSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                nbBlockSpinnerStateChanged(evt);
            }
        });

        blockSizeSpinner.setValue(1);
        blockSizeSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                blockSizeSpinnerStateChanged(evt);
            }
        });
        blockSizeSpinner.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                blockSizeSpinnerMouseClicked(evt);
            }
        });

        nbSetSpinner.setValue(2);
        nbSetSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                nbSetSpinnerStateChanged(evt);
            }
        });
        nbSetSpinner.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                nbSetSpinnerMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(formatCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7)
                            .addComponent(setLabel)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(policyCombo, 0, 1, Short.MAX_VALUE)
                                    .addComponent(typeCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(schedulerCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(nbBlockSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(blockSizeSpinner, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(nbSetSpinner, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(formatCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(policyCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(typeCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(schedulerCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(38, 38, 38)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(nbBlockSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(blockSizeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(setLabel)
                    .addComponent(nbSetSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(infoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(infoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.add(jPanel1, java.awt.BorderLayout.LINE_START);

        jPanel2.setBackground(new java.awt.Color(225, 225, 225));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel3.setBackground(new java.awt.Color(225, 225, 225));

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Layer");

        layerCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "L1", "L2", "L3" }));
        layerCombo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                layerComboItemStateChanged(evt);
            }
        });

        buttonHexMethod.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/hexa.png"))); // NOI18N
        buttonGroup1.add(buttonHexMethod);
        buttonHexMethod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonHexMethodActionPerformed(evt);
            }
        });

        buttonBinMethod.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/Bina.png"))); // NOI18N
        buttonGroup1.add(buttonBinMethod);
        buttonBinMethod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonBinMethodActionPerformed(evt);
            }
        });

        buttonDecMethod.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/deci.png"))); // NOI18N
        buttonGroup1.add(buttonDecMethod);
        buttonDecMethod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonDecMethodActionPerformed(evt);
            }
        });

        jToggleButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/toggleText.png"))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Properties");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(layerCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 383, Short.MAX_VALUE)
                .addComponent(buttonHexMethod)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonBinMethod)
                .addGap(2, 2, 2)
                .addComponent(buttonDecMethod)
                .addGap(50, 50, 50)
                .addComponent(jToggleButton1))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(layerCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jToggleButton1)
                            .addComponent(buttonDecMethod)
                            .addComponent(buttonBinMethod)
                            .addComponent(buttonHexMethod))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel3, java.awt.BorderLayout.PAGE_START);

        jPanel5.add(jPanel2, java.awt.BorderLayout.PAGE_START);
        jPanel5.add(view, java.awt.BorderLayout.CENTER);

        pannelInfo.setBackground(new java.awt.Color(225, 225, 225));
        pannelInfo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(208, 208, 208)));
        pannelInfo.setMinimumSize(new java.awt.Dimension(100, 24));
        pannelInfo.setName(""); // NOI18N
        pannelInfo.setPreferredSize(new java.awt.Dimension(942, 24));

        labelInfo.setText("jLabel8");

        javax.swing.GroupLayout pannelInfoLayout = new javax.swing.GroupLayout(pannelInfo);
        pannelInfo.setLayout(pannelInfoLayout);
        pannelInfoLayout.setHorizontalGroup(
            pannelInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pannelInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelInfo, javax.swing.GroupLayout.DEFAULT_SIZE, 1015, Short.MAX_VALUE))
        );
        pannelInfoLayout.setVerticalGroup(
            pannelInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labelInfo, javax.swing.GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE)
        );

        jPanel5.add(pannelInfo, java.awt.BorderLayout.PAGE_END);
        pannelInfo.getAccessibleContext().setAccessibleName("");

        getContentPane().add(jPanel5, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /* new selection */
    private void formatComboItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_formatComboItemStateChanged
        refresh();
    }//GEN-LAST:event_formatComboItemStateChanged

    private void policyComboItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_policyComboItemStateChanged
        refresh();
    }//GEN-LAST:event_policyComboItemStateChanged

    private void typeComboItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_typeComboItemStateChanged
        refresh();
    }//GEN-LAST:event_typeComboItemStateChanged

    private void schedulerComboItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_schedulerComboItemStateChanged
        refresh();
    }//GEN-LAST:event_schedulerComboItemStateChanged

    private void blockSizeSpinnerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_blockSizeSpinnerMouseClicked
        refresh();
    }//GEN-LAST:event_blockSizeSpinnerMouseClicked

    private void nbSetSpinnerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_nbSetSpinnerMouseClicked
        refresh();
    }//GEN-LAST:event_nbSetSpinnerMouseClicked

    private void nbBlockSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_nbBlockSpinnerStateChanged
        refresh();
    }//GEN-LAST:event_nbBlockSpinnerStateChanged

    private void blockSizeSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_blockSizeSpinnerStateChanged
        refresh();
    }//GEN-LAST:event_blockSizeSpinnerStateChanged

    private void nbSetSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_nbSetSpinnerStateChanged
        refresh();
    }//GEN-LAST:event_nbSetSpinnerStateChanged

    private void buttonDecMethodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonDecMethodActionPerformed
        view.changeMethod(TagTableModel.Method.DEC);
    }//GEN-LAST:event_buttonDecMethodActionPerformed

    private void buttonBinMethodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonBinMethodActionPerformed
        view.changeMethod(TagTableModel.Method.BIN);
    }//GEN-LAST:event_buttonBinMethodActionPerformed

    private void buttonHexMethodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonHexMethodActionPerformed
        view.changeMethod(TagTableModel.Method.HEXA);
    }//GEN-LAST:event_buttonHexMethodActionPerformed

    private void layerComboItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_layerComboItemStateChanged
        // save the current cache to the layer
        switch (layerValue) {
            case "L1":
                if (L1Cache != null) {
                    currentCache = L1Cache;
                    setCacheInfo();
                    break;
                } else {
                    initCacheInfo();
                }
                break;
                
            case "L2": 
                if (L2Cache != null) {
                    currentCache = L2Cache;
                    setCacheInfo();
                    break;
                } else {
                    initCacheInfo();
                }
                break;
                
            default: 
                if (L3Cache != null) {
                    currentCache = L3Cache;
                    setCacheInfo();
                    break;
                } else {
                    initCacheInfo();
                } 
                break;
        }
        
        refresh();
    }//GEN-LAST:event_layerComboItemStateChanged

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CacheEditor2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CacheEditor2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CacheEditor2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CacheEditor2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CacheEditor2().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSpinner blockSizeSpinner;
    private javax.swing.JButton buttonBinMethod;
    private javax.swing.JButton buttonDecMethod;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton buttonHexMethod;
    private javax.swing.JComboBox formatCombo;
    private javax.swing.JPanel infoPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JLabel labelCacheInfoSize;
    private javax.swing.JLabel labelCacheLayerInfo;
    private javax.swing.JLabel labelCacheTypeInfo;
    private javax.swing.JLabel labelInfo;
    private javax.swing.JComboBox<String> layerCombo;
    private javax.swing.JSpinner nbBlockSpinner;
    private javax.swing.JSpinner nbSetSpinner;
    private javax.swing.JPanel pannelInfo;
    private javax.swing.JComboBox policyCombo;
    private javax.swing.JComboBox schedulerCombo;
    private javax.swing.JLabel setLabel;
    private javax.swing.JComboBox typeCombo;
    private application.swing_utils.MemoryViewer view;
    // End of variables declaration//GEN-END:variables
}

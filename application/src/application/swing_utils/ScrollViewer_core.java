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

import Utils.Range;
import Utils.Rect;
import application.model.LineInfo;
import cache.Block;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JPanel;

/**
 *
 * @author leo
 */
public class ScrollViewer_core extends JPanel {
    private ScrollViewer master;
    
    private final Color modified    = Color.ORANGE;
    private final Color valid       = Color.GREEN;      // Couleur d'un block valide
    private final Color notValid    = Color.RED;        // Couleur d'un block non valide
    private final Color isFocus     = Color.ORANGE;     // Couleur du block sélectionné ou en cour de modification
    private final Color regionC     = new Color(238, 225, 241, 120);    // Couleur de la région sélectionné
    private final Color background  = new Color(0xd4d4d4, false);       // Couleur de fond
    private final Color shape       = new Color(0x999999, false);       // Couleur du trait
    private CacheVisualisator1 linkedCache = null;
    
    private final Rect region;
    private boolean drag;
    
    private final List<Rect> rectangles;
    
    public ScrollViewer_core() {
        super();
        this.master = (ScrollViewer) getParent();
        
        rectangles = new ArrayList<>(20);
        drag    = false;
        region  = new Rect(0, 0, getWidth(),getHeight(), regionC);
        
        addMouseMotionListener(new MouseMotionAdapter() {

            @Override
            public void mouseDragged(MouseEvent e) {
                System.out.println("eeeeeeeeeeeee");
                setRegion(e.getY());
            }
        
        });
    }
    
    public void linkTo(CacheVisualisator1 cV) {
        linkedCache = cV;
        
        // modification de la liste des rectangles pour la rendre compatible
        // avec le Cache à visualizer
        rectangles.clear();
        List<Block> data = cV.getInfo();
        for (Block b : data) {
            addRect(new LineInfo(linkedCache.getCache(), b));
        }
        
        repaint();
    }
    
    public void update() {
        if (linkedCache != null) linkTo(linkedCache);
    }
    
    public void setRegion(int yPos) {
        if (!rectangles.isEmpty()) {
            final int gap = calcGap();
            final int y = snapToClosest(yPos);
            

            region.top = y;
            region.setHeight(gap * linkedCache.getLineVisible());
            System.out.println("gap : " + gap + ", y = " + yPos + ", snap : " + y);
            System.out.println(region.toSting());

            // Scroll de la table
            linkedCache.setRegion(y, gap);
            repaint();
        }
    }
    
    public void addRect(Rect... rectangles) {
        this.rectangles.addAll(Arrays.asList(rectangles));
        updateRectDim();
    }
    
    public void addRect(LineInfo... infos) {
        Rect tmp = new Rect(0, 0, 0, 0, null);
        
        for (LineInfo l : infos) {
            if (l.validity) tmp.setColor(valid);
            else tmp.setColor(notValid);
            
            if (l.modified) tmp.setColor(modified);
        }
        
        rectangles.add(new Rect(tmp));
        
        updateRectDim();
    }

    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        final int w = getWidth();
        final int h = getHeight();
        
        // dessin de tout les rectangles
        if (!rectangles.isEmpty()) {
            drawRect(g);
        }
        
        // dessin de la région
//        snapToGrid(region);
        g.setColor(regionC);
        g.fillRect(region.left, region.top, w, region.getHeight());
        g.setColor(background);
    }
    
    public Range getRange() {
        // calcul de la region a afficher a faire
        return null;
    }
    
    private int calcGap() {
        int out = getHeight() / rectangles.size();
        if (out < 3) {
            out = 3;
        }
        
         return out;
    }
    
    private void updateRectDim() {
        if (!rectangles.isEmpty()) {
            final int gap = calcGap();
            final int w = getWidth();
            
            for (Rect r : rectangles) {
                r.set(r.left, snapToClosest(r.top), w, gap);
            }
        }
    }
    
    private void snapToGrid(Rect r) {
        if (!rectangles.isEmpty()) {
            final int gap = calcGap();

            r.top -= (r.top % gap);
            r.bottom += (r.bottom % gap);
        }
    }
    
    private int snapToClosest(int v) {
        if (!rectangles.isEmpty()) {
            final int gap = calcGap();
            
            if (gap != 0) return v - v % gap;
            return v;
        }
        
        return v;
    }

    // GESTION DES DESSINS -----------------------------------------------------
    public void drawRect(Graphics g) {
        
        final int gap = calcGap();    // hauteur d'un rectangle
        final int w = getWidth();     // largeur = largeur de la scrollBar
        int index;

        int compteur = 0;
        for (Rect r : rectangles) {
            
            if (r.hasColor()) {
                g.setColor(r.color);
                g.fillRect(r.left, compteur * gap + r.top, w, gap);
                g.setColor(shape);
                g.drawRect(r.left, compteur * gap + r.top, w, gap);
                g.setColor(background);

            } else {
                g.setColor(shape);
                g.drawRect(r.left, compteur * gap + r.top, w, gap);
                g.setColor(background);
            }
            
            compteur++;
        }
    }
    
    // GESTION DES EVENEMENTS --------------------------------------------------
}

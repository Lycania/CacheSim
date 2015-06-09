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
package Utils;

import java.awt.Color;

/**
 *
 * @author leo
 */
public class Rect {
    public int left, top, bottom, right;
    public Color color;
    
    /**
     * constructeur d'un rectangle.
     * @param left
     * @param top
     * @param bottom
     * @param right 
     * @param color
     */
    public Rect(int left, int top, int right, int bottom, Color color) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.color = color;
    }
    
    public Rect(Rect r) {
        left = r.left;
        top = r.top;
        right = r.right;
        bottom = r.bottom;
        color = r.color;
    }
    
    /**
     * Permet de définer les nouvelles coordonnées d'un rectangle
     * @param left
     * @param top
     * @param bottom
     * @param right 
     */
    public void set(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.bottom = bottom;
        this.right = right;
    }
    
    /**
     * Retourne vrai si le rectangle possède une couleur de remplissage
     * Faux sinon.
     * @return boolean
     */
    public boolean hasColor() {
        return (color != null);
    }
    
    /**
     * Change la couleur du rectangle.
     * @param color 
     */
    public void setColor(Color color) {
        this.color = color;
    }
    
    /**
     * retourne la largeur du rectangle
     * @return int
     */
    public int getWidth() {
        return right - left;
    }
    
    /**
     * retourne la hauteur du rectangle
     * @return int
     */
    public int getHeight() {
        return bottom - top;
    }
    
    public void setWidth(int w) {
        right = left + w;
    }
    
    public void setHeight(int h) {
        bottom = top + h;
    }
    
    public String toSting() {
        return "" + left + ":" + top + ":" + right + ":" + bottom;
    }
}

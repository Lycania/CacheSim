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

import java.awt.Point;

/**
 *
 * @author leo
 */
public class Segment {
    public int x1, y1, x2, y2;
    public int width;
    
    /**
     * Definition d'un segment.
     * @param x1
     * @param y1
     * @param x2
     * @param y2 
     */
    public Segment (int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }
    
    /**
     * Redéfinition d'un segment.
     * @param x1
     * @param y1
     * @param x2
     * @param y2 
     */
    public void set(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }
    
    /**
     * Redéfinition du début du segment.
     * @param p 
     */
    public void setStart(Point p) {
        this.x1 = p.x;
        this.y1 = p.y;
    }
        
    /**
     * Redéfinition du début du segment.
     * @param x1
     * @param y1 
     */
    public void setStart(int x1, int y1) {
        setStart(new Point(x1, y1));
    }
    
    /**
     * Redéfinition de la fin du segment.
     * @param x2
     * @param y2 
     */
    public void setEnd(int x2, int y2) {
        setEnd(new Point(x2, y2));
    }
    
    /**
     * Redéfinition de la fin du segment.
     * @param p 
     */
    public void setEnd(Point p) {
        this.x2 = p.x;
        this.y2 = p.y;
    }
    
    /**
     * retourne le début du segment
     * @return Point
     */
    public Point getStart() {
        return new Point(x1, y1);
    }
    
    /**
     * Retourne lafin du segment.
     * @return Point
     */
    public Point getStop() {
        return new Point(x2, y2);
    }   
}

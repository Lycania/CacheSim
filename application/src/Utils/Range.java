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

/**
 *
 * Représente une intervalle entre deux entiers
 * @author leo
 */
public class Range {
    public int start, end;
    
    /**
     * Création d'un range avec une intervale précise.
     * @param start
     * @param end 
     */
    public Range(int start, int end) {
        this.start  = start;
        this.end    = end;
    }
    
    /**
     * Création d'un range avec une intervalle allant de 0 à end.
     * @param end 
     */
    public Range(int end) {
        this.start = 0;
        this.end = end;
    }
    
    /**
     * Modifie l'intervalle du range.
     * @param s
     * @param e 
     */
    public void set(int s, int e) {
        start   = s;
        end     = e;
    }
    
    @Override
    public String toString() {
        return "(" + start + ", " + end + ")";
    }
}

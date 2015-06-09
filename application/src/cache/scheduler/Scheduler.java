/*
 * Copyright (C) 2015 maignial
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
package cache.scheduler;

/**
 *
 * @author maignial
 */
public abstract class Scheduler implements IScheduler{
    /**
     * La valeur maximum (excluse) de l'interval.
     */
    final int max;
    int current;
    
    /* types */
    public static final int FIFO = 0;
    public static final int LIFO = 1;
    public static final int LRU = 2;
    public static final int NMRU = 3;
    public static final int RANDOM = 4;
    public static final int LFU = 5;
    
    
    public Scheduler(int topRange) {
        this.max = topRange;
        this.current = -1;
    }
    
    @Override
    public int maxIndex() {
        return this.max;
    }
    
    @Override
    public int getCurrent() {
        return current;
    }
    
}

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

import java.util.ArrayDeque;
import java.util.Queue;

/**
 *
 * @author maignial
 */
public class LruScheduler extends Scheduler implements IScheduler{
    /**
     * Une file contenant les index triés par ordre inverse de date d'utilisation.
     */
    private final Queue<Integer> usedOrder;
            
    public LruScheduler(int topRange) {
        super(topRange);
        this.usedOrder = new ArrayDeque<>(topRange);
        
        for (int i=0 ; i<topRange ; ++i)
            this.usedOrder.offer(i);
    }

    @Override
    public int nextIndex() {
        return  this.usedOrder.peek();
    }

    @Override
    public void indexAccessed(int index) {
        this.usedOrder.remove(index);
        this.usedOrder.offer(index);
    }

    @Override
    public void indexUpdated(int index) {
        this.usedOrder.remove(index);
        this.usedOrder.offer(index);
    }
}

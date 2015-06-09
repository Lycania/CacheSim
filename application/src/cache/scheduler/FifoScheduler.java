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

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author maignial
 */
public class FifoScheduler extends Scheduler implements IScheduler{
    private final List<Integer> order;
            
    public FifoScheduler(int topRange) {
        super(topRange);
        this.order = new ArrayList<>(topRange);
        
        for (int i=0 ; i<topRange ; ++i)
            this.order.add(i);
    }

    @Override
    public int nextIndex() {
        current = this.order.get(0);
        return this.order.get(0);
    }

    @Override
    public void indexUpdated(int index) {
        this.order.remove(new Integer(index));
        this.order.add(index);
    }

    @Override
    public void indexAccessed(int index) {
    }
}

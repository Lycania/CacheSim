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
import java.util.Deque;

/**
 *
 * @author maignial
 */
public class LifoScheduler extends Scheduler implements IScheduler {
    private final Deque<Integer> arrivedOrder;

    public LifoScheduler(int topRange) {
        super(topRange);
        this.arrivedOrder = new ArrayDeque<Integer>() {};
        
        for (int i = topRange-1 ; i >= 0 ; --i)
            this.arrivedOrder.offerFirst(i);
    }

    @Override
    public int nextIndex() {
//        System.out.println(arrivedOrder.toString());
//        return this.arrivedOrder.peek();
        current = 0;
        return 0;
    }

    @Override
    public void indexAccessed(int index) {
    }

    @Override
    public void indexUpdated(int index) {
        arrivedOrder.offerLast(arrivedOrder.pollFirst());
    }
}

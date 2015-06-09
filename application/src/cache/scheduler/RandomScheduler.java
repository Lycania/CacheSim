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

import java.util.Random;

/**
 *
 * @author maignial
 */
public class RandomScheduler extends Scheduler implements IScheduler {
    private final Random r = new Random(System.currentTimeMillis());

    public RandomScheduler(int topRange) {
        super(topRange);
    }

    @Override
    public int nextIndex() {
        return r.nextInt(this.max);
    }

    @Override
    public void indexAccessed(int index) {
    }

    @Override
    public void indexUpdated(int index) {
    }
}

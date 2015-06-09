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
public class LfuScheduler extends Scheduler implements IScheduler {
    private final int[] usagesFrequences;
            
    public LfuScheduler(int topRange) {
        super(topRange);
        this.usagesFrequences = new int[topRange];
        for (int i=0 ; i<topRange ; ++i)
            this.usagesFrequences[i] = 0;
    }

    @Override
    public int nextIndex() {
        int minFreq = this.usagesFrequences[0];
        int theLFU = 0; // l'index utilisé le moins récement
        
        for (int i=1 ; i<this.max ; ++i) {
            if(this.usagesFrequences[i] < minFreq){
                minFreq = this.usagesFrequences[i];
                theLFU = i;
            }
        }
        
        return theLFU;
    }

    @Override
    public void indexAccessed(int index) {
        ++this.usagesFrequences[index];
    }

    @Override
    public void indexUpdated(int index) {
        this.usagesFrequences[index] = 1;
    }
}

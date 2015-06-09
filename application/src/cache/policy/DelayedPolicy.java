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
package cache.policy;

import cache.type.PolicyType;
import java.util.Arrays;

/**
 *
 * @author maignial
 */
public class DelayedPolicy extends Policy{
    /**
     * true pour dirty et false pour not dirty.
     */
    private final boolean[] dirtyBits;
    
    public DelayedPolicy(int blocCount) {
        super(blocCount);
        
        this.dirtyBits = new boolean[this.size];
        Arrays.fill(dirtyBits, false);
    }

    @Override
    public boolean readyToUpdate(int blocNumber) {
        return !this.dirtyBits[blocNumber];
    }

    @Override
    public void writeData(int blocNumber) {
        this.dirtyBits[blocNumber] = true;
    }

    @Override
    public void updateBlock(int blocNumber) {
        this.dirtyBits[blocNumber] = false;
    }

    @Override
    public PolicyType getPolicyType() {
        return PolicyType.DELAYED;
    }
}

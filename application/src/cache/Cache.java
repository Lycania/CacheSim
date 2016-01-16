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
package cache;

import cache.policy.DelayedPolicy;
import cache.policy.DirectWritePolicy;
import cache.type.Type;
import cache.policy.Policy;
import cache.scheduler.SchedulerType;
import cache.type.PolicyType;

/**
 * .
 * @author maignial
 */
public abstract class Cache implements ICache, IIO {
    protected final Type  type;
    protected Policy      policy;
    protected SchedulerType   schedulerType;
    
    protected final int wordsPerBlock;
    protected final int setCount;
    protected int wordSize;

    
    public Cache(Type t, PolicyType policyType, int setCount, int wordsPerBlock, int wordSize) {
        this.type = t;
        this.setCount = setCount;
        this.wordsPerBlock = wordsPerBlock;
        this.wordSize = wordSize;
        this.schedulerType = SchedulerType.NONE;
        
        switch (policyType) {
            case DELAYED :
                this.policy = new DelayedPolicy(setCount);
                break;
            case DIRECT :
                this.policy = new DirectWritePolicy(setCount);
                break;
        }
    }
    
    @Override
    public String toString() {
        String out = "";
        out += "type : " + type.toString() + "\n";
        out += "nb block : " + setCount + "\n";
        out += "blockSize : " + wordsPerBlock + "\n";
        return out;
    }
    
    /**
     * Calcule le tag d'un bloc à partir de l'adresse
     * @param addr adresse mémoire
     * @return le tag d'un bloc
     */
    @Override
    public final int addrToTag(int addr) {
        return (addr / (wordsPerBlock * wordSize)) % setCount;
    }
    
    /**
     * Calcule l'index mémoire à partir de l'adresse
     * @param addr adresse mémoire
     * @return l'index (le numero de page) mémoire contenant l'adresse
     */
    @Override
    public final int addrToIndex(int addr) {
//        System.out.println("-------------------");
//        System.out.println("addr : " + addr);
//        System.out.println("setCount : " + setCount);
//        System.out.println("blockSize : " + blockSize);
//        System.out.println("wordSize : " + wordSize);
//        System.out.println("index : " + (addr/(this.setCount*this.blockSize*this.wordSize)+1));
        return addr/(this.setCount*this.wordsPerBlock*this.wordSize);
    }
    
    @Override
    public final int blockAlignedAddress(int addr) {
        final int offset = addr % (this.wordsPerBlock * this.wordSize);
        return addr - offset;
    }
    
    @Override
    public final int getBlockSize() {
        return this.wordsPerBlock;
    }

    @Override
    public final int getWordSize() {
        return this.wordSize;
    }

    @Override
    public final SchedulerType getScheduler() {
        return this.schedulerType;
    }

    @Override
    public final int size() {
        return this.setCount;
    }

    @Override
    public final PolicyType getPolicy() {
        return this.policy.getPolicyType();
    }
    
    @Override
    public Type getType() {
        return type;
    }
}

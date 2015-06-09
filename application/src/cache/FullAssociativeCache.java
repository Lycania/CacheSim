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

import cache.exception.AddressNotAlignedException;
import cache.scheduler.*;
import cache.scheduler.Scheduler;
import cache.type.Format;
import cache.type.PolicyType;
import cache.type.Type;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author maignial
 */
public class FullAssociativeCache extends Cache {
    private final List<Block> blocks;
    private final List<Integer> addresses;
    private final Scheduler scheduler;
    
    public FullAssociativeCache(Type t, int schedulerType,
            PolicyType policyType, int blocksCount, int wordsPerBlock,
            int wordSize) {
        super(t, policyType, blocksCount, wordsPerBlock, wordSize);
        
        /* initialisation des blocks */
        this.blocks = new ArrayList<>(blocksCount);
        for (int i=0 ; i<blocksCount ; ++i) {
            this.blocks.add(new Block(0, i, 0, wordsPerBlock, wordSize));
        }
        
        /* Initialisation du scheduler */
        switch (schedulerType) {
            case Scheduler.FIFO : 
                scheduler = new FifoScheduler(blocksCount);break;
                
            case Scheduler.LFU :
                scheduler = new LfuScheduler(blocksCount);break;
                
            case Scheduler.LIFO :
                scheduler = new LifoScheduler(blocksCount);break;
                
            case Scheduler.LRU :
                scheduler = new LruScheduler(blocksCount);break;
                
            case Scheduler.NMRU :
                scheduler = new NmruScheduler(blocksCount);break;
                
            case Scheduler.RANDOM :
                scheduler = new RandomScheduler(blocksCount);break;
                
            default : scheduler = null;
        }
        
        /* Initialisation de la liste des tags de blocs */
        this.addresses = new ArrayList<>(blocksCount);
        for (int i = 0 ; i < blocksCount ; ++i){
            this.addresses.add(-1);
        }
    }
    
    public List<Block> getBlocks() {
        return blocks;
    }
    
    @Override
    public Format getFormat() {
        return Format.FULL;
    }

    @Override
    public boolean hitOnWrite(int addr) {
        final int blockNumber = addresses.indexOf(blockAlignedAddress(addr));
        
        if (blockNumber == -1)
            return false;

        if (! blocks.get(blockNumber).compareIndexTo(addrToIndex(addr)))
            return false;

        return blocks.get(blockNumber).isValid();
    }

    @Override
    public boolean hitOnRead(int addr) {
        final int blockNumber = addresses.indexOf(blockAlignedAddress(addr));
        
        if (blockNumber == -1)
            return false;
        
        if (! this.blocks.get(blockNumber).compareIndexTo(this.addrToIndex(addr)))
            return false;
        
        return this.blocks.get(blockNumber).isValid();
    }

    
    @Override
    public boolean writeWord(int addr, byte[] bytes) throws AddressNotAlignedException {
        final int blockNumber;
        final Block b;
        
        if (bytes.length != this.wordSize)
            return false;
        
        /* Si nouvelle adresse alors on considére un update */
        if(! this.hitOnWrite(addr)) {
            blockNumber = scheduler.nextIndex();
            b = blocks.get(blockNumber);
            b.setStartAddr(addr);
            b.setIndex(addrToIndex(addr));
            b.setTag(addrToTag(addr));
            b.setValidity(true);
            /* màj des métadonées du cache */
            addresses.set(blockNumber, blockAlignedAddress(addr));
            policy.updateBlock(blockNumber);
            scheduler.indexUpdated(blockNumber);
        } else {
            blockNumber = addresses.indexOf(blockAlignedAddress(addr));
            b = blocks.get(blockNumber);
            /* màj des métadonées du cache */
            policy.writeData(blockNumber);
            scheduler.indexAccessed(blockNumber);
        }
        
        /* Ecriture des données dans le bloc */
        b.writeWord(addr % (wordSize * wordsPerBlock), bytes);
        
        return true;
    }
    
    
    @Override
    public boolean writeHalf(int addr, byte[] bytes) throws AddressNotAlignedException {
        final int blockNumber;
        final Block b;
        
        if (bytes.length != this.wordSize/2)
            return false;
        
        /* Si nouvelle adresse alors on considére un update */
        if(! this.hitOnWrite(addr)) {
            blockNumber = scheduler.nextIndex();
            b = blocks.get(blockNumber);
            b.setStartAddr(addr);
            b.setIndex(addrToIndex(addr));
            b.setTag(addrToTag(addr));
            b.setValidity(true);
            /* màj des métadonées du cache */
            addresses.set(blockNumber, blockAlignedAddress(addr));
            policy.updateBlock(blockNumber);
            scheduler.indexUpdated(blockNumber);
        } else {
            blockNumber = addresses.indexOf(blockAlignedAddress(addr));
            b = blocks.get(blockNumber);
            /* màj des métadonées du cache */
            policy.writeData(blockNumber);
            scheduler.indexAccessed(blockNumber);
        }
        
        /* Ecriture des données dans le bloc */
        b.writeHalf(addr % (wordSize / 2 * wordsPerBlock), bytes);
        
        return true;
    }
    
    
    @Override
    public boolean writeByte(int addr, byte[] bytes) {
        final int blockNumber;
        final Block b;
        
        if (bytes.length != 1)
            return false;
        
        /* Si nouvelle adresse alors on considére un update */
        if(! this.hitOnWrite(addr)) {
            blockNumber = scheduler.nextIndex();
            b = blocks.get(blockNumber);
            b.setStartAddr(addr);
            b.setIndex(addrToIndex(addr));
            b.setTag(addrToTag(addr));
            b.setValidity(true);
            /* màj des métadonées du cache */
            addresses.set(blockNumber, blockAlignedAddress(addr));
            policy.updateBlock(blockNumber);
            scheduler.indexUpdated(blockNumber);
        } else {
            blockNumber = addresses.indexOf(blockAlignedAddress(addr));
            b = blocks.get(blockNumber);
            /* màj des métadonées du cache */
            policy.writeData(blockNumber);
            scheduler.indexAccessed(blockNumber);
        }
        
        /* Ecriture des données dans le bloc */
        b.writeByte(addr % wordsPerBlock, bytes);
        
        return true;
    }
    
    
    @Override
    public byte[] readWord(int addr) throws AddressNotAlignedException {

        final int blockNumber = this.addresses.indexOf(blockAlignedAddress(addr));

        /* notification au scheduler */
        this.scheduler.indexAccessed(blockNumber);
        return this.blocks.get(blockNumber).readWord(addr);
    }

    @Override
    public byte[] readHalf(int addr) throws AddressNotAlignedException {
//        if (! this.hitOnRead(addr))
//            return null;
        
        final int blockNumber = this.addresses.indexOf(blockAlignedAddress(addr));
        /* notification au scheduler */
        this.scheduler.indexAccessed(blockNumber);
        return this.blocks.get(blockNumber).readHalf(addr);
    }
    
    @Override
    public byte[] readByte(int addr) {
//        if (! this.hitOnRead(addr))
//            return null;

        final int blockNumber = this.addresses.indexOf(blockAlignedAddress(addr));
        /* notification au scheduler */
        this.scheduler.indexAccessed(blockNumber);
        return this.blocks.get(blockNumber).readByte(addr);
    }

    @Override
    public boolean isRam() {
        return false;
    }

    @Override
    public int totalSize() {
        return setCount * wordsPerBlock * wordSize;
    }

    @Override
    public Block getNextBlock(int addr) {
        return blocks.get(scheduler.nextIndex());
    }

    @Override
    public boolean isNextBlockToSave(int addr) {
        return !this.policy.readyToUpdate(scheduler.nextIndex());
    }
}

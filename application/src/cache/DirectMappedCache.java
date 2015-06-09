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
import cache.type.Format;
import cache.type.PolicyType;
import cache.type.Type;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author maignial
 */
public class DirectMappedCache extends Cache {
    private final List<Block> blocks;
    
    public DirectMappedCache(Type t, PolicyType policyType, int blocksCount, int wordsPerBlock, int wordSize) {
        super(t, policyType, blocksCount, wordsPerBlock, wordSize);
        
        /* initialisation des blocks */
        this.blocks = new ArrayList<>(blocksCount);
        for (int i=0 ; i<blocksCount ; ++i){
            this.blocks.add(new Block(0, i, -1, wordsPerBlock, wordSize));
        }
    }
    
    public DirectMappedCache(int wordsPerBlock, int wordSize, int val) {
        super(Type.UNITED, PolicyType.DELAYED, 1, wordsPerBlock, wordSize);
        blocks = new ArrayList<>(1);
        blocks.add(new Block(wordsPerBlock, wordSize, val));
        blocks.get(0).setValidity(false);
    }
    
    public List<Block> getBlocks() {
        return blocks;
    }
    
    @Override
    public Format getFormat() {
        return Format.DIRECT;
    }

    @Override
    public boolean hitOnWrite(int addr) {
        final int blockNumber = addrToTag(addr);
        final int index = addrToIndex(addr);
//        System.out.println("------------------------------------------------");
//        System.out.println("Affichage hitOnWrite : ");
//        System.out.println("addrToTag("+addr+") = " + blockNumber);
//        System.out.println("addrToIndex("+addr+") = " + index);
//        System.out.println("bloc tag : " + blocks.get(blockNumber).getTag());
//        System.out.println("bloc index : " + blocks.get(blockNumber).getIndex());
        
        return blocks.get(blockNumber).compareIndexTo(index);
    
//        return this.blocks.get(blockNumber).isValid();
    }

    @Override
    public boolean hitOnRead(int addr) {
        final int blockNumber = addrToTag(addr);
        final int index = addrToIndex(addr);
        
        if (! this.blocks.get(blockNumber).compareIndexTo(index))
            return false;
        return true;
        
//        return this.blocks.get(blockNumber).isValid();
    }

    @Override
    public boolean writeWord(int addr, byte[] bytes) throws AddressNotAlignedException {
        if (bytes.length != this.wordSize)
            return false;
        
        final int blockNumber = this.addrToTag(addr);
        final Block b;
        
        /* si nouvelle adresse, on considère ça comme update */
        if (! this.hitOnWrite(addr)) {
            b = getNextBlock(addr);
            b.setStartAddr(addr);
            b.setIndex(addrToIndex(addr));
            b.setTag(blockNumber);
            b.setValidity(true);
            this.policy.updateBlock(blockNumber);
        } else {
            b = this.blocks.get(blockNumber);
            this.policy.writeData(blockNumber);
        }
        
        /* écriture des données dans le bloc */
        b.writeWord(addr, bytes);
        
        return true;
    }

    @Override
    public boolean writeHalf(int addr, byte[] bytes) throws AddressNotAlignedException {
        if (bytes.length != this.wordSize/2)
            return false;
        
        final int blockNumber = this.addrToTag(addr);
        final Block b;
        
        /* si nouvelle adresse, on considère ça comme update */
        if (! this.hitOnWrite(addr)) {
            b = getNextBlock(addr);
            b.setStartAddr(addr);
            b.setIndex(addrToIndex(addr));
            b.setTag(blockNumber);
            b.setValidity(true);
            this.policy.updateBlock(blockNumber);
        } else {
            b = this.blocks.get(blockNumber);
            this.policy.writeData(blockNumber);
        }
        
        /* écriture des données dans le bloc */
        b.writeHalf(addr, bytes);
        
        return true;
    }

    @Override
    public boolean writeByte(int addr, byte[] bytes) {
        if (bytes.length != 1)
            return false;
        
        final int blockNumber = this.addrToTag(addr);
        final Block b;
        
        /* si nouvelle adresse, on considère ça comme update */
        if (! this.hitOnWrite(addr)) {
            b = getNextBlock(addr);
            b.setStartAddr(addr);
            b.setIndex(addrToIndex(addr));
            b.setTag(blockNumber);
            b.setValidity(true);
            this.policy.updateBlock(blockNumber);
        } else {
            b = this.blocks.get(blockNumber);
            this.policy.writeData(blockNumber);
        }
        
        /* écriture des données dans le bloc */
        b.writeByte(addr, bytes);
        
        return true;
    }

    @Override
    public byte[] readWord(int addr) throws AddressNotAlignedException {
//        if (!this.hitOnRead(addr))
//            return null;
        
        final int blockNumber = this.addrToTag(addr);
        return this.blocks.get(blockNumber).readWord(addr);
    }

    @Override
    public byte[] readHalf(int addr) throws AddressNotAlignedException {
//        if (!this.hitOnRead(addr))
//            return null;
        
        final int blockNumber = this.addrToTag(addr);
        return this.blocks.get(blockNumber).readHalf(addr);
    }

    @Override
    public byte[] readByte(int addr) {
//        if (!this.hitOnRead(addr))
//            return null;
        
        final int blockNumber = this.addrToTag(addr);
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
        return this.blocks.get(addrToTag(addr));
    }

    @Override
    public boolean isNextBlockToSave(int addr) {
        return !this.policy.readyToUpdate(addrToTag(addr));
    }
}

/*
 * Copyright (C) 2015 leo
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
import cache.scheduler.SchedulerType;
import cache.type.Format;
import cache.type.PolicyType;
import cache.type.Type;

/**
 *
 * @author leo
 */
public class Layer implements IIO, ICache{    
    private Cache dataC = null;
    private Cache instC = null;
    private Cache unitedC = null;
    private Ram ram = null;
    private Cache selected = null;
    
    public Layer(Cache cache) {
        switch (cache.getType()) {
            case DATA: 
                dataC = selected = cache;
                instC = unitedC = ram = null; break;
                
            case INSTRUCTION: 
                instC = selected = cache;
                dataC = unitedC = ram = null; break;
                
            case UNITED: 
                unitedC = selected = cache; 
                dataC = instC = ram = null; break;
                
            case RAM: 
                ram = (Ram) cache;
                selected = cache;
                dataC = instC = unitedC = null;
        }
    }
    
    public Layer(Cache dataC, Cache instuctionC, Cache unitedC) {
        this.dataC       = dataC;
        this.instC       = instuctionC;
        this.unitedC     = unitedC;
        ram         = null;
        selected    = null;
        primaryConnect();
    }
    
    public Layer(Cache data, Cache instruction) {
        this.dataC      = data;
        this.instC      = instruction;
        this.unitedC    = null;
        this.ram        = null;
        this.selected   = null;
        primaryConnect();
        
    }
    
    public Layer(Ram ram) {
        dataC = instC = unitedC = null;
        this.ram = ram;
        selected = ram;
    }
    
    private void primaryConnect() {
        if (dataC != null) selected = dataC;
        else if (instC != null) selected = dataC;
        else if (unitedC != null) selected = instC;
        else if (ram != null) selected = ram;
    }
    
    public Cache getSelected() {
        return selected;
    }
    
    public Cache getDataCache() {
        return dataC;
    }
    
    public Cache getInstructionCache() {
        return instC;
    }
    
    public Cache getUnitedCache() {
        return unitedC;
    }
    
    public boolean isRam() {
        return selected.isRam();
    }
    
    /**
     * Permet de connecter le layer à un de ses caches donnée en paramètre.
     * @param t
     * @return Vrai si le cache existe, faux sinon.
     */
    public boolean connectTo(Type t) {
        switch (t) {
            case DATA:
                if(dataC != null) {selected = dataC; return true;}
                if (unitedC != null) {selected = unitedC; return true;}
                if (ram != null) {selected = ram; return true;}
                break;

            case INSTRUCTION:
                if (instC != null) {selected = instC; return true;}
                if (unitedC != null) {selected = unitedC; return true;}
                if (ram != null) {selected = ram; return true;}
                break;

            case UNITED:
                if (unitedC != null) {selected = unitedC; return true;}
                if (ram != null) {selected = ram; return true;}
            case RAM:
                if (ram != null) {selected = ram; return true;}
            break;
        }

        return false;
    }
    
    
    /***************************************************************************
     *      
     *      GESTION DES HIT & MISS
     * 
     * ************************************************************************/
    
    @Override
    public boolean hitOnRead (int addr) {
        return selected.hitOnRead(addr);
    }
    
    @Override
    public boolean hitOnWrite(int addr) {
        return selected.hitOnWrite(addr);
    }
    
    
    /***************************************************************************
     *      
     *      GESTION DES ENTRÉES / SORTIES
     * 
     * ************************************************************************/
    
    @Override
    public boolean writeWord(int addr, byte[] bytes) 
            throws AddressNotAlignedException {
        return selected.writeWord(addr, bytes);
    }

    @Override
    public boolean writeHalf(int addr, byte[] bytes) 
            throws AddressNotAlignedException {
        return selected.writeHalf(addr, bytes);
    }

    @Override
    public boolean writeByte(int addr, byte[] b) {
        return selected.writeByte(addr, b);
    }

    @Override
    public byte[] readWord(int addr) 
            throws AddressNotAlignedException {
        return selected.readWord(addr);
    }

    @Override
    public byte[] readHalf(int addr) 
            throws AddressNotAlignedException {
        return selected.readHalf(addr);
    }

    @Override
    public byte[] readByte(int addr) {
        return selected.readByte(addr);
    }


    @Override
    public int blockAlignedAddress(int addr) {
        return selected.blockAlignedAddress(addr);
    }

    @Override
    public int getBlockSize() {
        return selected.getBlockSize();
    }

    @Override
    public int getWordSize() {
        return selected.getWordSize();
    }

    @Override
    public int size() {
        return selected.size();
    }

    @Override
    public PolicyType getPolicy() {
        return this.selected.getPolicy();
    }

    @Override
    public int addrToTag(int addr) {
        return selected.addrToTag(addr);
    }
    
    @Override
    public int addrToIndex(int addr) {
        return selected.addrToIndex(addr);
    }

    @Override
    public int totalSize() {
        return selected.totalSize();
    }

    @Override
    public Block getNextBlock(int addr) {
        return selected.getNextBlock(addr);
    }

    @Override
    public boolean isNextBlockToSave(int addr) {
        return selected.isNextBlockToSave(addr);
    }

    @Override
    public Type getType() {
        return selected.getType();
    }

    @Override
    public Format getFormat() {
        return selected.getFormat();
    }

    @Override
    public SchedulerType getScheduler() {
        return selected.getScheduler();
    }
}
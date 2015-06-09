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
import cache.type.Format;
import cache.type.PolicyType;
import cache.type.Type;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author maignial
 */
public class SetAssociativeCache extends Cache {
    private final List<DirectMappedCache> cadres;
    private final List<Scheduler> schedulers;
    private final int nuSet;
    
    public SetAssociativeCache(Type t, int schedulerType, PolicyType policyType, int setCount, int cadreCount, int wordsPerBlock, int wordSize) {
        super(t, policyType, setCount, wordsPerBlock, wordSize);

        this.nuSet = cadreCount;
        
        /* initialisation des cadres */
        this.cadres = new ArrayList<>(cadreCount);
        for (int i = 0 ; i < cadreCount ; ++i) {
            this.cadres.add(new DirectMappedCache(t, policyType, setCount, wordsPerBlock, wordSize));
        }
        
        /* Initialisation des schedulers */
        this.schedulers = new ArrayList<>(setCount);
        for (int i = 0 ; i < setCount ; ++i){
            switch (schedulerType) {
                case Scheduler.FIFO : 
                    this.schedulers.add(new FifoScheduler(cadreCount));
                break;
                case Scheduler.LFU :
                    this.schedulers.add(new LfuScheduler(cadreCount));
                break;
                case Scheduler.LIFO :
                    this.schedulers.add(new LifoScheduler(cadreCount));
                break;
                case Scheduler.LRU :
                    this.schedulers.add(new LruScheduler(cadreCount));
                break;
                case Scheduler.NMRU :
                    this.schedulers.add(new NmruScheduler(cadreCount));
                break;
                case Scheduler.RANDOM :
                    this.schedulers.add(new RandomScheduler(cadreCount));
                break;
            }
        }
    }
    
    @Override
    public Format getFormat() {
        return Format.SET;
    }
    
    public int setCount() {
        return nuSet;
    }

    @Override
    public boolean hitOnWrite(int addr) {
        for (DirectMappedCache cadre : this.cadres) {
            if (cadre.hitOnWrite(addr))
                return true;
        }
        
        return false;
    }

    @Override
    public boolean hitOnRead(int addr) {
        for (DirectMappedCache cadre : this.cadres) {
            if (cadre.hitOnRead(addr))
                return true;
        }

        return false;
    }

    /**
     * Recherche le cadre qui contiens le block associé à l'adresse fournie.
     * @param addr adresse mémoire
     * @return le numero du cadre, ou -1 si aucun cadre ne contiens cette adresse
     */
    private int addrToCadre(int addr) {
        for(int i = 0 ; i < this.nuSet ; ++i) {
           if (this.cadres.get(i).hitOnRead(addr))
               return i;               
        }
        
        return -1;
    }
    
    @Override
    public boolean writeWord(int addr, byte[] bytes) throws AddressNotAlignedException {
        int cadre = this.addrToCadre(addr);
        if (cadre == -1) {
            cadre = schedulers.get(addrToTag(addr)).nextIndex();
            schedulers.get(addrToTag(addr)).indexUpdated(cadre);
        }

        /* Notification au scheduler */
        this.schedulers.get(addrToTag(addr)).indexAccessed(cadre);

        return this.cadres.get(cadre).writeWord(addr, bytes);
    }

    @Override
    public boolean writeHalf(int addr, byte[] bytes) throws AddressNotAlignedException {
        int cadre = this.addrToCadre(addr);
        if (cadre == -1)
            cadre = schedulers.get(addrToTag(addr)).nextIndex();
        
        /* Notification au scheduler */
        final int ensembleNumber = this.addrToTag(addr);
        this.schedulers.get(ensembleNumber).indexAccessed(cadre);
        
        return this.cadres.get(cadre).writeHalf(addr, bytes);
    }

    @Override
    public boolean writeByte(int addr, byte[] bytes) {
        int cadre = this.addrToCadre(addr);
        if (cadre == -1)
            cadre = schedulers.get(addrToTag(addr)).nextIndex();
        
        /* Notification au scheduler */
        final int ensembleNumber = this.addrToTag(addr);
        this.schedulers.get(ensembleNumber).indexAccessed(cadre);
        
        return this.cadres.get(cadre).writeByte(addr, bytes);
    }

    @Override
    public byte[] readWord(int addr) throws AddressNotAlignedException {
        int cadre = this.addrToCadre(addr);
        if (cadre == -1)
            return null;
        
        /* Notification au scheduler */
        final int ensembleNumber = this.addrToTag(addr);
        this.schedulers.get(ensembleNumber).indexAccessed(cadre);
        
        return this.cadres.get(cadre).readWord(addr);
    }

    @Override
    public byte[] readHalf(int addr) throws AddressNotAlignedException {
        int cadre = this.addrToCadre(addr);
        if (cadre == -1)
            return null;
        
        /* Notification au scheduler */
        final int setNumber = this.addrToTag(addr);
        this.schedulers.get(setNumber).indexAccessed(cadre);
        
        return this.cadres.get(cadre).readHalf(addr);
    }

    @Override
    public byte[] readByte(int addr) {
        int cadre = this.addrToCadre(addr);
        if (cadre == -1)
            return null;
        
        /* Notification au scheduler */
        final int setNumber = this.addrToTag(addr);
        this.schedulers.get(setNumber).indexAccessed(cadre);
        
        return this.cadres.get(cadre).readByte(addr);
    }

    @Override
    public boolean isRam() {
        return false;
    }

    @Override
    public int totalSize() {
        return this.nuSet * nuSet * wordsPerBlock * wordSize;
    }

    @Override
    public Block getNextBlock(int addr) {
        
        final int cadre = schedulers.get(addrToTag(addr)).nextIndex();
        return cadres.get(cadre).getNextBlock(addr);
    }
    
    @Override
    public boolean isNextBlockToSave(int addr) {
        final int cadre = schedulers.get(addrToTag(addr)).nextIndex();
        return cadres.get(cadre).isNextBlockToSave(addr);
    }
    
    /* ----- DEBUG ----- */
    public List<DirectMappedCache> getCadres () {
        return cadres;
    }
}

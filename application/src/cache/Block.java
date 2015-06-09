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
import Utils.Range;
import cache.type.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Un bloc représente une ligne d'une cache
 * @author leo
 */
public class Block implements IIO {
    private Type type;
    private boolean validity;
    private int tag, index;
    private final int wordSize, nuWord;
    private final List<byte[]> data;
    private int addr;
    public int wc = -1;
    
    public Block (int addr, int tag, int index, int nuWord, int wordSize) {
        
        this.type   = Type.DATA;
        this.tag    = tag;
        this.index  = index;
        this.nuWord = nuWord;
        this.wordSize   = wordSize;
        this.validity   = false;
        this.addr       = addr;
        this.data       = new ArrayList<>(nuWord);
        
        for (int i = 0 ; i < nuWord ; ++i) {
            data.add(new byte[wordSize]);
        }
    }
    
    /**
     * Deep clone
     * @param b 
     */
    public Block (Block b) {
        this.type       = b.type;
        this.validity   = b.validity;
        this.tag        = b.tag;
        this.index      = b.index;
        this.nuWord     = b.nuWord;
        this.wordSize   = b.wordSize;
        this.data       = new ArrayList<>(b.data.size());
        this.addr       = b.addr;
        
        // deep clone de la liste
        
        int compteur = 0;
        while (compteur < b.data.size()) {
            byte[] word = Arrays.copyOf(b.data.get(compteur++), wordSize);
            this.data.add(word);
        }
    }
    
     public Block (int nuWord, int wordSize, int val) {
        
        this.type = Type.RAM;
        this.tag    = 0;
        this.index  = 0;
        this.nuWord = nuWord;
        this.wordSize   = wordSize;
        this.validity   = false;
        
        this.data  = new ArrayList<>(4);
        byte[] words = new byte[wordSize * nuWord];
        Arrays.fill(words, (byte) val);
        
        data.add(new byte[wordSize * nuWord]);        
    }
     
    
    // METHODE -----------------------------------------------------------------
    /**
     * Permet de déterminer si le bloc est valide ou non.
     * @return vrai si le bloc est valide, faux sinon.
     */
    public boolean isValid() {
        return validity;
    }
    
    /**
     * Compare le tag du bloc avec le tag (entier) passé en paramètre.
     * @param tagToCompare
     * @return vrai si il s'agit du même tag, faux sinon
     */
    public boolean compareTagTo(int tagToCompare) {
        return (tagToCompare == tag);
    }
    
    /**
     * Compare l'index du bloc avec l'index (entier) passé en paramètre.
     * @param indexToCompare
     * @return vrai si il s'agit du même index, faux sinon
     */    
    public boolean compareIndexTo(int indexToCompare) {
        return (indexToCompare == index);
    }
    
    // GETTER & SETTER ---------------------------------------------------------
    public int getWordSize() {
        return data.get(0).length;
    }
    
    public void setStartAddr(int addr) {
        final int offset = addr % (this.nuWord * this.wordSize);
        this.addr =  addr - offset;
    }
    
    public void setTag(int tag) {
        this.tag = tag;
    }
    
    public void setIndex(int index) {
        this.index = index;
    }
    
    public void setValidity(boolean validity) {
        this.validity = validity;
    }
    
    public int getStartAddr() {
        return addr;
    }
    
    public int getTag() {
        return index;
    }
    
    public int getIndex() {
        return tag;
    }
    
    public List<byte[]> getData() {
        return data;
    }
    
    /**
     * Donne la longueur d'un bloc en mot
     * @return le nombre de mot dans le bloc
     */
    public int size() {
        return data.size();
    }
    
    /**
     * Donne la taille total en octet d'un bloc
     * @return le nombre d'octet
     */
    public int totalSize() {
        return data.size() * wordSize;
    }
    
    /**
     * Retourne un couple (start, end) représentant l'intervalle entre le
     * premier mot et le dernier. ( en adresse )
     * @return un couple (start, end);
     */
    public Range getRange() {
        int start = 0;
        int end = nuWord * wordSize -1;
        
        return new Range(start, end);
    }
    
    /***************************************************************************
     *      
     *      GESTION DES ENTRÉES / SORTIES
     * 
     * ************************************************************************/

    /**
     * @author maignial
     */
    @Override
    public boolean writeWord(int addr, byte[] bytes) throws AddressNotAlignedException {
        if (addr%(wordSize) != 0)
            throw new AddressNotAlignedException(wordSize);

        if (bytes.length != wordSize) {
            return false;
        }
        
        // Traitement général
        this.data.set((addr % totalSize()) / wordSize, bytes);
        wc = Controler.wc;
        return true;
    }
    
    
    @Override
    public boolean writeHalf(int addr, byte[] bytes) throws AddressNotAlignedException {
        byte[] word;
        
        if (addr % (wordSize / 2) != 0) throw new AddressNotAlignedException(wordSize/2);
        if (bytes.length != wordSize / 2) return false;
        
        if (data.size() == 1) word = data.get(0);
        else word = data.get((addr % totalSize())/ wordSize);
        int offset  = addr % wordSize;
        
        for (int i = 0; i < bytes.length; ++i)  {
            word[offset++] = bytes[i];
        }

        data.set((addr % totalSize())/ wordSize, word);
        
        wc = Controler.wc;
        return true;
    }

    /**
     * @author maignial
     */

    
    @Override
    public boolean writeByte(int addr, byte[] bytes) {
        final byte[] word;

                
        if (bytes.length != 1) return false;
        
        if (data.size() == 1) word = data.get(0);
        else word = data.get((addr % totalSize())/ wordSize);
        
        final int offset = addr % wordSize;
        word[offset] = bytes[0];
        data.set((addr % totalSize())/ wordSize, word);
        
        wc = Controler.wc;
        return true;
    }

    @Override
    public byte[] readWord(int addr) throws AddressNotAlignedException {
        if (addr%(this.wordSize) != 0)
            throw new AddressNotAlignedException(wordSize);

        wc = Controler.wc;
        return this.data.get((addr % totalSize())/ wordSize);
    }

    @Override
    public byte[] readHalf(int addr) throws AddressNotAlignedException {
        byte[] word;
        
        if (addr%(this.wordSize/2) != 0)
            throw new AddressNotAlignedException(this.wordSize/2);
        
        byte[] halfWord = new byte[wordSize / 2];
        if (data.size() == 1) word = data.get(0);
        else word = this.data.get((addr % totalSize())/ wordSize);

        final int offset = addr % wordSize;
        for (int i = 0 ; i < wordSize / 2 ; ++i) {
            halfWord[i] = word[i + offset];
        }
        
        wc = Controler.wc;
        return halfWord;
    }

    @Override
    public byte[] readByte(int addr) {

        byte[] b = new byte[1];
        byte[] word;
        if (data.size() == 1)   word = data.get(0);
        else                    word = data.get((addr % totalSize())/ wordSize);
        
        b[0] = word[addr % wordSize];
        
        wc = Controler.wc;
        return b;
    }
}
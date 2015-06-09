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
import cache.type.PolicyType;
import cache.type.Type;
import java.util.Arrays;
import test.TU;

/**
 *
 * @author leo
 */
public class Ram extends DirectMappedCache implements IIO {
    private byte[] data;
    public boolean[] validity;
    
    /**
     *
     * @param nuWord
     * @param virtual_wordSize
     */
    public Ram(int nuWord, int virtual_wordSize) {
        super(Type.RAM, PolicyType.DELAYED, 1, 1, nuWord);
        wordSize = virtual_wordSize;
        data = new byte[nuWord];
        validity = new boolean[nuWord];
    }

//    private int validityBitAccess(int addr, boolean modify) {
//         vérification de la validitée
//        final int alpha = addr - (addr % wordSize);
//        final int epsilon = alpha / wordSize;
//        int index = epsilon / validity.length;
//        if (index > 0) index--;
//        
//        int offset = epsilon % Integer.SIZE;
//        if (offset > 0) offset--;
//        
//        int tmp = (validity[index] >> (Integer.SIZE - offset));
//        if (modify) {
//            int mask = (1 << (Integer.SIZE - offset));
//            if (tmp == 0)
//                validity[index] = validity[index] | mask;
//            else
//                validity[index] = validity[index] & ~mask;
//        }
//        
//        System.out.println("result : "+ Integer.toBinaryString(validity[index]));
//        return tmp;
//    }
    
    public byte[] getData() {
        return data;
    }
    
    // TODO à réécrire
    private void sizeUp(int min) {
        final int newSize = (data.length + (min - data.length)) + 512;
        byte[] cpy = new byte[newSize];
        boolean[] cpyValidity = new boolean[newSize];
        
        // copy des données dans le nouveau tableau
        for (int i = 0; i < data.length; ++i) {
            cpy[i] = data[i];
            cpyValidity[i] = validity[i];
        }
        
        // copy du tableau de validité
        
        data = cpy;
        validity = cpyValidity;
    }
    
    @Override
    public boolean hitOnWrite(int addr) {
        return true;
//        System.out.println("RAM : validité = " + validity[addr]);
//        return validity[addr];
//        return (validityBitAccess(addr, false) == 1);
    }

    @Override
    public boolean hitOnRead(int addr) {
        return true;
//        return validity[addr];
//        return (validityBitAccess(addr, false) == 1);
    }
    

    @Override
    public boolean writeWord(int addr, byte[] bytes) throws AddressNotAlignedException {
//        System.out.println("RAM : addr = " + addr + ". bytes : " + Arrays.toString(bytes));
        if (addr > data.length) sizeUp(addr);

        if (bytes.length != wordSize) return false;
        if (addr % wordSize != 0)
            throw new AddressNotAlignedException(wordSize);
        
//        if (validityBitAccess(addr, true) == 0) return false;
//        if (!hitOnWrite(addr)) return false;
        for (int i = 0; i < wordSize; ++i) {
            data[addr] = bytes[i];
            validity[addr++] = true;
        }
        return true;
    }

    @Override
    public boolean writeHalf(int addr, byte[] bytes) throws AddressNotAlignedException {
        if (addr > data.length) sizeUp(addr);
        if (bytes.length != wordSize / 2) return false;
        if (addr % (wordSize / 2) != 0)
            throw new AddressNotAlignedException(wordSize/2);
        
//        if (validityBitAccess(addr, true) == 0) return false;
//        if (!hitOnWrite(addr)) return false;
        
        for (int i = 0; i < wordSize / 2; ++i) {
            data[addr] = bytes[i];
            validity[addr++] = true;
        }
        return true;
    }

    @Override
    public boolean writeByte(int addr, byte[] bytes) {
        if (addr > data.length) sizeUp(addr);
        if (bytes.length != 1) return false;
//        if (validityBitAccess(addr, true) == 0) return false;
//        if (!hitOnWrite(addr)) return false;
        
        data[addr] = bytes[0];
        validity[addr] = true;
        return true;
    }

    @Override
    public byte[] readWord(int addr) throws AddressNotAlignedException {
        if (addr > data.length) sizeUp(addr);
        if (addr % wordSize != 0)
            throw new AddressNotAlignedException(wordSize);
//        if (validityBitAccess(addr, false) == 0) return null;
        if (!hitOnRead(addr)) return null;
                
        return Arrays.copyOfRange(data, addr, addr+wordSize);
    }

    @Override
    public byte[] readHalf(int addr) throws AddressNotAlignedException {
        if (addr > data.length) sizeUp(addr);
        if (addr % (wordSize / 2) != 0)
            throw new AddressNotAlignedException(wordSize/2);
//        if (validityBitAccess(addr, false) == 0) return null;
        if (!hitOnRead(addr)) return null;
        
        return Arrays.copyOfRange(data, addr, addr+(wordSize / 2));
    }

    @Override
    public byte[] readByte(int addr) {
        if (addr > data.length) sizeUp(addr);
        byte[] out = new byte[1];
//        if (validityBitAccess(addr, false) == 0) return null;
        if (!hitOnRead(addr)) return null;
        out[0] = data[addr];
        return out;
    }
    
    public void draw(int decoupe) {
        System.err.println(TU.ANSI_RESET);
        for (int i = 0; i < data.length; ++i) {
            if ((i % decoupe * wordSize) == 0)
                System.err.println("");
            System.err.print(data[i] + " ");
        }
        System.err.println("");
    }

    @Override
    public boolean isRam() {
        return true;
    }

    @Override
    public int totalSize() {
        return data.length;
    }
}

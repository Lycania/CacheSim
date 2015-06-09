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
package test;

import cache.exception.AddressNotAlignedException;
import cache.Block;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author maignial
 */
public class BlockTester {
    
    public static void main(String[] args) {
        Block b = new Block(0, 0, 0, 4, 4);
        byte[] data = new byte[4];
        byte[] result = null;

        /* Test d'écriture et de lecture d'un mot  ---------------------------*/
        data[0] = Byte.MIN_VALUE;
        data[1] = Byte.MIN_VALUE;
        data[2] = Byte.MIN_VALUE;
        data[3] = Byte.MIN_VALUE;
        
        try {
            b.writeWord(0, data);
        } catch (AddressNotAlignedException ex) {}
        

        try {
            result = b.readWord(0);
        } catch (AddressNotAlignedException ex) {}
        
       TU.log("Test écriture / lecture | mot", Arrays.equals(data, result));
        
        
        /* Test d'écriture et de lecture d'un demi mot -----------------------*/
        b = new Block(0, 0, 0, 4, 4);
        result = null; data = new byte[2];
        data[0] = Byte.MIN_VALUE;
        data[1] = Byte.MIN_VALUE;

        
        try {
            b.writeHalf(2, data);
        } catch (AddressNotAlignedException ex) {}
        
        try {
            result = b.readHalf(2);
        } catch (AddressNotAlignedException ex) {}

        TU.log("Test écriture / lecture | half", Arrays.equals(data, result));
        
        
         /* Test d'écriture et de lecture d'un octet -------------------------*/
        b = new Block(0, 0, 0, 4, 4);
        data = new byte[1];
        data[0] = Byte.MIN_VALUE;
        
        b.writeByte(0, data);
        result = b.readByte(0);
        
        TU.log("Test écriture / lecture | byte", Arrays.equals(data, result));
        
        
        /* Test d'écriture et de lecture générale -------------------------*/
        b = new Block(0, 0, 0, 10, 4);
        List<byte[]> toWrite = new ArrayList<>();
        List<byte[]> forRead = new ArrayList<>();
        
        for (int i = 0; i < 10; ++i) {
            toWrite.add(data);
            try {
                b.writeWord(i * 4, data);
            } catch (AddressNotAlignedException ex) {}
        }
        
        for (int i = 0; i < 10; ++i) {
            try {
                forRead.add(b.readWord(i * 4));
            } catch (AddressNotAlignedException ex) {}
        }
        
        TU.log("Test écriture / lecture | gen", Arrays.equals(data, result));
    }
}

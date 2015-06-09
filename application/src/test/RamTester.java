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
package test;

import cache.exception.AddressNotAlignedException;
import cache.Ram;
import java.util.Arrays;

/**
 *
 * @author leo
 */
public class RamTester {
    public static void main(String[] args) {
        Ram ram = new Ram(64, 4);
        byte[] test, result;
        
        for (int i = 0; i < 16; ++i) {
            try {
                ram.writeWord(i*4, TU.wordBuilder(4, (byte)i));   
            } catch (AddressNotAlignedException ex) {
            }
        }
        
        try {
            test = TU.wordBuilder(4, (byte)3);
            result = ram.readWord(12);
            TU.log("Ecriture d'un mot : ", Arrays.equals(test, result));
            
            test = TU.wordBuilder(2, (byte)11);
            result = ram.readHalf(46);
            TU.log("Ecriture d'un demi mot : ", Arrays.equals(test, result));
            
            test = TU.wordBuilder(1, (byte)15);
            result = ram.readByte(61);
            TU.log("Ecriture d'un octet : ", Arrays.equals(test, result));
        } catch (AddressNotAlignedException ex) {
        }
        ram.draw(4);
    }
}

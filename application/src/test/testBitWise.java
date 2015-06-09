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

import Utils.Numbers;
import cache.type.Unit;
import java.util.Arrays;

/**
 *
 * @author leo
 */
public class testBitWise {
    public static void main(String[] args) {
        System.out.println(Arrays.toString(Numbers.intToBytes(Unit.WORD, 0x87569845)));
        System.out.println(Arrays.toString(Numbers.intToBytes(Unit.HALF, 0x87569845)));
        System.out.println(Arrays.toString(Numbers.intToBytes(Unit.BYTE, 0x87569845)));
        System.out.println(Numbers.toBin(2486));
        System.out.println(Numbers.toHex(2486));
        System.out.println(2486);
    }
}

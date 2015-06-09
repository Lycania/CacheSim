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

import cache.Cache;
import cache.DirectMappedCache;
import cache.Layer;
import cache.Ram;
import cache.type.PolicyType;
import cache.type.Type;

/**
 *
 * @author leo
 */
public class LayerTester {
    public static void main(String[] args) {
        Cache dataL1 = new DirectMappedCache(Type.DATA, PolicyType.DELAYED, 2, 1, 4);
        Cache dataL2 = new DirectMappedCache(Type.DATA, PolicyType.DELAYED, 2, 1, 4);
        Cache instL2 = new DirectMappedCache(Type.INSTRUCTION, PolicyType.DELAYED, 2, 1, 4);
        Cache unitL3 = new DirectMappedCache(Type.UNITED, PolicyType.DELAYED, 2, 1, 4);
        Ram ram = new Ram(32, 4);

        Layer l1 = new Layer(dataL1, null, null);
        Layer l2 = new Layer(dataL2, instL2, null);
        Layer l3 = new Layer(null, null, unitL3);
        Layer lr = new Layer(ram);
        
        System.out.println("\n layer L1");
        TU.log("\tL1 connection data ", l1.connectTo(Type.DATA));
        TU.log("\tL1 connection inst", l1.connectTo(Type.INSTRUCTION));
        TU.log("\tL1 connection unit", l1.connectTo(Type.UNITED));
        TU.log("\tL1 connection ram ", l1.connectTo(Type.RAM));
        
        System.out.println("\n layer L2");
        TU.log("\tL2 connection data", l2.connectTo(Type.DATA));
        TU.log("\tL2 connection inst", l2.connectTo(Type.INSTRUCTION));
        TU.log("\tL2 connection unit", l2.connectTo(Type.UNITED));
        TU.log("\tL2 connection ram ", l2.connectTo(Type.RAM));
        
        System.out.println("\n layer L3");
        TU.log("\tL3 connection data", l3.connectTo(Type.DATA));
        TU.log("\tL3 connection inst", l3.connectTo(Type.INSTRUCTION));
        TU.log("\tL3 connection unit", l3.connectTo(Type.UNITED));
        TU.log("\tL3 connection ram ", l3.connectTo(Type.RAM));
        System.out.println("\n RAM");
        TU.log("\tRAM connection data", lr.connectTo(Type.DATA));
        TU.log("\tRAM connection inst", lr.connectTo(Type.INSTRUCTION));
        TU.log("\tRAM connection unit", lr.connectTo(Type.UNITED));
        TU.log("\tRAM connection ram ", lr.connectTo(Type.RAM));
    }
}

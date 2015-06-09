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

import AssemblyModule.ArmParser;
import AssemblyModule.MemoryAccessBuffer;

/**
 *
 * @author maignial
 */
public class SocketTester {
    public static void main(String[] args) {
        ArmParser parser = new ArmParser();
        
        boolean result = parser.isMemoryAccess("str pc, [r4, r2, lsl #2]");
        System.out.println("Is memory Access: " + result);
        
        MemoryAccessBuffer buffer =
                parser.decodeInstruction("str pc, [r4, r2, rrx]\n{0,4,8,12,16,20,24,28,32,36,40,44,48,52,56,60}\n536870912;\n");
        System.out.println("Type d'accès: " + buffer.accessType);
        System.out.println("Taille d'accès: " + buffer.dataSize);
        System.out.println("Adresses: " + buffer.addresses);
        System.out.println("Données: " + buffer.data);
    }
}

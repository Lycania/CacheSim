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
import cache.Cache;
import cache.Controler;
import cache.FullAssociativeCache;
import cache.Layer;
import cache.Ram;
import cache.scheduler.Scheduler;
import cache.type.PolicyType;
import cache.type.Type;
import cache.type.Unit;
import java.util.Arrays;



/**
 *
 * @author Léo
 */
public class cacheSystemTester_FAC {
    public static void main(String[] args) {
        
        Cache dataL1 = new FullAssociativeCache(Type.DATA, Scheduler.FIFO,
                PolicyType.DELAYED, 4, 2, 4);
        Cache instrL1 = new FullAssociativeCache(Type.INSTRUCTION, Scheduler.FIFO,
                PolicyType.DELAYED, 4, 2, 4);
        
        Cache dataL2 = new FullAssociativeCache(Type.UNITED, Scheduler.FIFO,
                PolicyType.DELAYED, 4, 4, 4);
        Ram ram = new Ram(512, 4);
        
        Layer l1 = new Layer(dataL1, instrL1, null);
        Layer l2 = new Layer(null, null, dataL2);
        Layer randomAccessMemory = new Layer(ram);
        
                /* init du system de cache */
        Controler controler = new Controler();
        controler.add(l1);
        controler.add(l2);
        controler.add(randomAccessMemory);
        System.err.println("nb layer : " + controler.size());
        
        byte[] dataWord;
        byte[] readResult = null;
        
        try {
            controler.write(Type.DATA, Unit.WORD, 0, TU.wordBuilder(4, (byte)1));
            controler.write(Type.DATA, Unit.WORD, 4, TU.wordBuilder(4, (byte)2));
            controler.write(Type.DATA, Unit.WORD, 8, TU.wordBuilder(4, (byte)3));
            controler.write(Type.DATA, Unit.WORD, 12, TU.wordBuilder(4, (byte)4));
            controler.write(Type.DATA, Unit.WORD, 16, TU.wordBuilder(4, (byte)5));
            controler.write(Type.DATA, Unit.WORD, 20, TU.wordBuilder(4, (byte)6));
            controler.write(Type.DATA, Unit.WORD, 24, TU.wordBuilder(4, (byte)7));
            controler.write(Type.DATA, Unit.WORD, 28, TU.wordBuilder(4, (byte)8));
            controler.write(Type.DATA, Unit.WORD, 32, TU.wordBuilder(4, (byte)9));
            
            //         =============== AFFICHAGE ===========
            System.err.println("\n");
            System.err.println("L0 -- data");
            TU.getFACache((FullAssociativeCache) dataL1, true);
            System.err.println("\nL0 -- instruction");
            TU.getFACache((FullAssociativeCache) instrL1, true);
            System.err.println("\n");
            System.err.println("L1");
            TU.getFACache((FullAssociativeCache) dataL2, true);
            System.err.println("\n");
            System.err.println("RAM");

            // affichage de la ram
            ram.draw(32);
            
            dataWord = controler.read(Type.DATA, Unit.WORD, 0);
//            System.err.println("(0) : " + Arrays.toString(dataWord));

            dataWord = controler.read(Type.DATA, Unit.WORD, 4);
//            System.err.println("(76) : " + Arrays.toString(dataWord));
//            
//            dataWord = controler.read(Type.DATA, Unit.WORD, 80);
//            System.err.println("(80) : " + Arrays.toString(dataWord));
            
        } catch (AddressNotAlignedException ex) {
        }
        
        
//         =============== AFFICHAGE ===========
        System.err.println("\n");
        System.err.println("L0 -- data");
        TU.getFACache((FullAssociativeCache) dataL1, true);
        System.err.println("\nL0 -- instruction");
        TU.getFACache((FullAssociativeCache) instrL1, true);
        System.err.println("\n");
        System.err.println("L1");
        TU.getFACache((FullAssociativeCache) dataL2, true);
        System.err.println("\n");
        System.err.println("RAM");
//        TU.getDMCache((DirectMappedCache) ramC, true);
        
        // affichage de la ram
        ram.draw(32);
//        tampon.draw(8);
        String toString = Arrays.toString(ram.validity);
        System.err.println(toString);
    }
}

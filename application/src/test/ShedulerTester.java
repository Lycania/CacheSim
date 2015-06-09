///*
// * Copyright (C) 2015 leo
// *
// * This program is free software; you can redistribute it and/or
// * modify it under the terms of the GNU General Public License
// * as published by the Free Software Foundation; either version 2
// * of the License, or (at your option) any later version.
// *
// * This program is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// * GNU General Public License for more details.
// *
// * You should have received a copy of the GNU General Public License
// * along with this program; if not, write to the Free Software
// * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
// */
//package test;
//
//import cache.AddressNotAlignedException;
//import cache.FullyAssociativeCache;
//import cache.scheduler.Scheduler;
//import cache.type.PolicyType;
//import cache.type.Type;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// *
// * @author leo
// */
//public class ShedulerTester {
//    public static void main(String[] args) {
//        FullyAssociativeCache cacheFA;
//        List<List<byte[]>> waited = new ArrayList<>();
//        List<List<byte[]>> result = new ArrayList<>();
//        List<byte[]> dataBlock;
//        
//
//        /* Test du sheduler FIFO ---------------------------------------------*/
//        cacheFA = new FullyAssociativeCache(Type.DATA, Scheduler.FIFO, PolicyType.DIRECT, 8, 4, 4);
//        
//        waited.add(TU.blockBuilder(4, TU.wordBuilder(4, (byte) 8)));
//        waited.add(TU.blockBuilder(4, TU.wordBuilder(4, (byte) 9)));
//        waited.add(TU.blockBuilder(4, TU.wordBuilder(4, (byte) 10)));
//        waited.add(TU.blockBuilder(4, TU.wordBuilder(4, (byte) 11)));
//        waited.add(TU.blockBuilder(4, TU.wordBuilder(4, (byte) 12)));
//        waited.add(TU.blockBuilder(4, TU.wordBuilder(4, (byte) 13)));
//        waited.add(TU.blockBuilder(4, TU.wordBuilder(4, (byte) 14)));
//        waited.add(TU.blockBuilder(4, TU.wordBuilder(4, (byte) 15)));
//        
//        // ---- remplissage du cache
//        for (int i = 0; i < 16; ++i) {
//            dataBlock = TU.blockBuilder(4, TU.wordBuilder(4, (byte) i));
//            cacheFA.updateBlock(16*i, dataBlock);
//        }
//
//        result = TU.getFACache(cacheFA, false);
//        TU.log("Sheduler FullyAssociativeCache | FIFO",
//                TU.LLbTest(waited, result));
//        
//        
//        
//        /* Test du sheduler LFU ----------------------------------------------*/
//        cacheFA = new FullyAssociativeCache(Type.DATA, Scheduler.LFU, PolicyType.DIRECT, 4, 4, 4);
//        
//        waited.clear();
//        waited.add(TU.blockBuilder(4, TU.wordBuilder(4, (byte) 0)));
//        waited.add(TU.blockBuilder(4, TU.wordBuilder(4, (byte) 1)));
//        waited.add(TU.blockBuilder(4, TU.wordBuilder(4, (byte) 2)));
//        waited.add(TU.blockBuilder(4, TU.wordBuilder(4, (byte) 4)));
//        
//        // ---- remplissage du cache
//        for (int i = 0; i < 4; ++i) {
//            dataBlock = TU.blockBuilder(4, TU.wordBuilder(4, (byte) i));
//            cacheFA.updateBlock(16*i, dataBlock);
//        }
//        
//        try {
//            cacheFA.readWord(0);cacheFA.readWord(16); cacheFA.readWord(32);
//        } catch (AddressNotAlignedException ex) {}
//        
//        dataBlock = TU.blockBuilder(4, TU.wordBuilder(4, (byte) 4));
//        cacheFA.updateBlock(96, dataBlock);
//
//        result = TU.getFACache(cacheFA, false);
//        TU.log("Sheduler FullyAssociativeCache | LFU",
//                TU.LLbTest(waited, result));
//
//        
//        
//        /* Test du sheduler LIFO ---------------------------------------------*/
//        cacheFA = new FullyAssociativeCache(Type.DATA, Scheduler.LIFO, PolicyType.DIRECT, 4, 4, 4);
//        
//        waited.clear();
//        waited.add(TU.blockBuilder(4, TU.wordBuilder(4, (byte) 5)));
//        waited.add(TU.blockBuilder(4, TU.wordBuilder(4, (byte)0)));
//        waited.add(TU.blockBuilder(4, TU.wordBuilder(4, (byte)0)));
//        waited.add(TU.blockBuilder(4, TU.wordBuilder(4, (byte)0)));
//        
//        // ---- remplissage du cache
//        for (int i = 0; i < 4; ++i) {
//            dataBlock = TU.blockBuilder(4, TU.wordBuilder(4, (byte) i));
//            cacheFA.updateBlock(16*i, dataBlock);
//        }
//        
//        dataBlock = TU.blockBuilder(4, TU.wordBuilder(4, (byte) 4));
//        cacheFA.updateBlock(96, dataBlock);
//        dataBlock = TU.blockBuilder(4, TU.wordBuilder(4, (byte) 5));
//        cacheFA.updateBlock(128, dataBlock);
//
//        result = TU.getFACache(cacheFA, false);
//        TU.log("Sheduler FullyAssociativeCache | LIFO",
//                TU.LLbTest(waited, result));
//        
//        
//        /* Test du sheduler LRU ---------------------------------------------*/
//        cacheFA = new FullyAssociativeCache(Type.DATA, Scheduler.LRU, PolicyType.DIRECT, 4, 4, 4);
//        
//        waited.clear();
//        waited.add(TU.blockBuilder(4, TU.wordBuilder(4, (byte) 4)));
//        waited.add(TU.blockBuilder(4, TU.wordBuilder(4, (byte) 1)));
//        waited.add(TU.blockBuilder(4, TU.wordBuilder(4, (byte) 2)));
//        waited.add(TU.blockBuilder(4, TU.wordBuilder(4, (byte) 3)));
//        
//        // ---- remplissage du cache
//        for (int i = 0; i < 4; ++i) {
//            dataBlock = TU.blockBuilder(4, TU.wordBuilder(4, (byte) i));
//            cacheFA.updateBlock(16*i, dataBlock);
//        }
//        
//        try {
//            cacheFA.readWord(0);cacheFA.readWord(16); cacheFA.readWord(32);
//            cacheFA.readWord(48);
//        } catch (AddressNotAlignedException ex) {}
//        
//        dataBlock = TU.blockBuilder(4, TU.wordBuilder(4, (byte) 4));
//        cacheFA.updateBlock(96, dataBlock);
//
//        result = TU.getFACache(cacheFA, false);
//        TU.log("Sheduler FullyAssociativeCache | LRU",
//                TU.LLbTest(waited, result));
//        
//        
//        
//        /* Test du sheduler NMRU ---------------------------------------------*/
//        cacheFA = new FullyAssociativeCache(Type.DATA, Scheduler.NMRU, PolicyType.DIRECT, 4, 4, 4);
//        
//        waited.clear();
//        waited.add(TU.blockBuilder(4, TU.wordBuilder(4, (byte) 4)));
//        waited.add(TU.blockBuilder(4, TU.wordBuilder(4, (byte) 5)));
//        waited.add(TU.blockBuilder(4, TU.wordBuilder(4, (byte) 2)));
//        waited.add(TU.blockBuilder(4, TU.wordBuilder(4, (byte) 3)));
//        
//        // ---- remplissage du cache
//        for (int i = 0; i < 4; ++i) {
//            dataBlock = TU.blockBuilder(4, TU.wordBuilder(4, (byte) i));
//            cacheFA.updateBlock(16*i, dataBlock);
//            try {
//                cacheFA.readWord(16 * i);
//            } catch (AddressNotAlignedException ex) {}
//        }
//        
//        try {
//            cacheFA.readWord(0);cacheFA.readWord(16); cacheFA.readWord(32);
//            cacheFA.readWord(48);
//        } catch (AddressNotAlignedException ex) {}
//        
//        dataBlock = TU.blockBuilder(4, TU.wordBuilder(4, (byte) 4));
//        cacheFA.updateBlock(64, dataBlock);
//        try {
//            cacheFA.readWord(64);
//        } catch (AddressNotAlignedException ex) {}
//        
//        dataBlock = TU.blockBuilder(4, TU.wordBuilder(4, (byte) 5));
//        cacheFA.updateBlock(123, dataBlock);
//
//
//        result = TU.getFACache(cacheFA, false);
//        TU.log("Sheduler FullyAssociativeCache | NMRU",
//                TU.LLbTest(waited, result));
//        TU.log("Sheduler FullyAssociativeCache | RANDOM", false);
//    }
//}

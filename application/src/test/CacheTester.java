///*
// * Copyright (C) 2015 maignial
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
//import cache.Block;
//import cache.Cache;
//import cache.DirectMappedCache;
//import cache.FullyAssociativeCache;
//import cache.SetAssociativeCache;
//import cache.scheduler.Scheduler;
//import cache.type.PolicyType;
//import cache.type.Type;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
///**
// *
// * @author maignial
// */
//public class CacheTester {
//    public static void main(String[] args) {
//        final int addr = 816;
//        
//        Cache cacheDM = new DirectMappedCache(Type.DATA, PolicyType.DIRECT, 4, 4, 4);
//        Cache cacheFA = new FullyAssociativeCache(Type.DATA, Scheduler.FIFO, PolicyType.DIRECT, 4, 4, 4);
//        Cache cacheSA = new SetAssociativeCache(Type.DATA, Scheduler.FIFO, PolicyType.DIRECT, 2, 4, 4, 4);
//        
//        byte[] dataWord = new byte[4];
//        dataWord[0] = dataWord[1] = dataWord[2] = dataWord[3] = (byte) 0b10110110;
//        
//        List<byte[]> dataBlock = new ArrayList<>();
//        dataBlock.add(dataWord);
//        dataBlock.add(dataWord);
//        dataBlock.add(dataWord);
//        dataBlock.add(dataWord);
//        
///******************************************************************************/
//        System.out.println("Test de chargement d'un bloc: ");
//        Block toSave;
//        boolean hitResult;
//        toSave = cacheDM.updateBlock(addr, dataBlock);
//        hitResult = cacheDM.hitOnWrite(addr);
//        
//        TU.log("\tChargement DirectMapped  ", toSave == null && hitResult);
//        
//        /************/
//        toSave = cacheFA.updateBlock(addr, dataBlock);
//        hitResult = cacheFA.hitOnWrite(addr);
//        
//        TU.log("\tChargement FullAssociative", toSave == null && hitResult);
//        
//        /************/
//        toSave = cacheSA.updateBlock(addr, dataBlock);
//        hitResult = cacheSA.hitOnWrite(addr);
//        
//        TU.log("\tChargement SetAssociative", toSave == null && hitResult);
//        
///******************************************************************************/
//        System.out.println("\nTest d'écriture/lecture dans le cache: ");
//        dataWord[0] = dataWord[1] = dataWord[2] = dataWord[3] = (byte) 0b10110000;
//
//        
//        boolean writeResult = false;
//        byte[] readResult = null;
//        try {
//            writeResult = cacheDM.writeWord(addr, dataWord);
//        } catch (AddressNotAlignedException ex) {
//            Logger.getLogger(CacheTester.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
//        try {
//            readResult = cacheDM.readWord(addr);
//        } catch (AddressNotAlignedException ex) {
//            Logger.getLogger(CacheTester.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        TU.log("\t DirectMapped    ", 
//                writeResult && readResult!=null && readResult.equals(dataWord));
//        
//        /************/
//        try {
//            writeResult = cacheFA.writeWord(addr, dataWord);
//        } catch (AddressNotAlignedException ex) {
//            Logger.getLogger(CacheTester.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
//        try {
//            readResult = cacheFA.readWord(addr);
//        } catch (AddressNotAlignedException ex) {
//            Logger.getLogger(CacheTester.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        TU.log("\t FullAssociative", 
//                writeResult && readResult!=null && readResult.equals(dataWord));
//        
//        /************/
//        try {
//            writeResult = cacheSA.writeWord(addr, dataWord);
//        } catch (AddressNotAlignedException ex) {
//            Logger.getLogger(CacheTester.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
//        try {
//            readResult = cacheSA.readWord(addr);
//        } catch (AddressNotAlignedException ex) {
//            Logger.getLogger(CacheTester.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        TU.log("\t SetAssociative    ", 
//                writeResult && readResult!=null && readResult.equals(dataWord));
//    }
//}

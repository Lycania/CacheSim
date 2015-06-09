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

import cache.Block;
import cache.DirectMappedCache;
import cache.FullAssociativeCache;
import cache.SetAssociativeCache;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


/**
 *
 * @author leo
 */
public class TU {
    public static final String ANSI_RESET  = "\u001B[0m";
    public static final String ANSI_BLACK  = "\u001B[30m";
    public static final String ANSI_RED    = "\u001B[31m";
    public static final String ANSI_GREEN  = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE   = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN   = "\u001B[36m";
    public static final String ANSI_WHITE  = "\u001B[37m";
    
    
    static void log(String msg, boolean result) {
        if (result) 
            System.out.println(msg + "\t:\t" + ANSI_GREEN + "OK" + ANSI_RESET);
        else
            System.out.println(msg + "\t:\t" + ANSI_RED + "KO" + ANSI_RESET);

    }
    
    
    static byte[] wordBuilder(int wordSize, byte val) {
        byte[] out = new byte[wordSize];
        Arrays.fill(out, val);
        return out;
    }
    
    
    static List<byte[]> blockBuilder(int blocksize, byte[] word) {
        List<byte[]> out = new ArrayList<>();
        for (int i = 0; i < blocksize; ++i) out.add(word);
        return out;
    }
    
    
    static List<List<byte[]>> getFACache(FullAssociativeCache c, boolean aff) {
        List<List<byte[]>> out = new ArrayList<>();
        
        for (Block b : c.getBlocks()) {
            out.add(b.getData());
            if (aff) {
                List<byte[]> data = b.getData();
                Iterator<byte[]> it = data.iterator();
                while (it.hasNext()) {
                    System.err.print(" " + Arrays.toString(it.next()));
                }
                System.err.println("");
                
            }
        }
        return out;
    }
    
    static void printSACache (SetAssociativeCache c) {
        DirectMappedCache[] cadres = c.getCadres().toArray(new DirectMappedCache[c.getCadres().size()]);
        Block[][] blocks = new Block[cadres.length][];
        
        for (int j=0 ; j<cadres.length ; ++j) {
            blocks[j] = cadres[j].getBlocks().toArray(new Block[cadres[0].size()]);
        }
        
        for (int j=0 ; j<cadres[0].size() ; ++j) {
            for (int i=0 ; i<cadres.length ; ++i) {
                TU.drawData(blocks[i][j].getData(), ANSI_BLACK);
                System.err.print("\t\t");
            }
            System.err.print("\n");
        }
    }
    
    
    static List<List<byte[]>> getDMCache(DirectMappedCache c, boolean aff) {
        List<List<byte[]>> out = new ArrayList<>();
        
        for (Block b : c.getBlocks()) {
            out.add(b.getData());
            if (aff) {
                List<byte[]> data = b.getData();
                Iterator<byte[]> it = data.iterator();

                while (it.hasNext()) {
                    System.err.print(" " + Arrays.toString(it.next()));
                    
                }
                System.err.println("");
                
            }
        }
        return out;
    }
    
    
    static boolean LLbTest(List<List<byte[]>> a, List<List<byte[]>> b) {
        int compteurL, compteurLL;
        compteurL = 0;
        
        while (compteurL < a.size()) {
            List<byte[]> blockA = a.get(compteurL);
            List<byte[]> blockB = b.get(compteurL++);
            compteurLL = 0;
            
            while (compteurLL < blockA.size()) {
                byte[] wordA = blockA.get(compteurLL);
                byte[] wordB = blockB.get(compteurLL++);
                
                if (!Arrays.equals(wordA, wordB)) return false;
                
            }
        }
        return true;
    }
    
    
    public static void drawData(List<byte[]> data, String color) {
        Iterator<byte[]> iterator = data.iterator();
        System.err.print(color + "\t");
        while (iterator.hasNext()) {
            System.err.print(Arrays.toString(iterator.next()) + " ");
        }
        System.err.print("");
    }
}

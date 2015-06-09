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
package Utils;

import cache.type.Unit;

/**
 * int32    : xxxxxxxx  xxxxxxxx xxxxxxxx xxxxxxxx
 * masque   : m1 > 24   m2 > 16  m3 > 8   m4
 * 
 * @author leo
 */
public class Numbers {
    /**
     * int32    : xxxxxxxx  xxxxxxxx xxxxxxxx xxxxxxxx
     * masque   : m1 > 24   m2 > 16  m3 > 8   m4
     * Decoupe un entier en un nombre d'octet dépendant de la taille spécifié, 
     * Mot, demi-Mot, octet.
     * @param u
     * @param val l'entier à découper
     * @return un tableau d'octet
     */
    public static byte[] intToBytes(Unit u, int val) {
        byte[] out;
        final int m1 = 0xFF000000;
        final int m2 = 0x00FF0000;
        final int m3 = 0x0000FF00;
        final int m4 = 0x000000FF;
        
        switch (u) {
            case WORD:
                out = new byte[4];
                out[0] = (byte) ((val & m1) >>> 24);
                out[1] = (byte) ((val & m2) >>> 16);
                out[2] = (byte) ((val & m3) >>> 8);
                out[3] = (byte) (val & m4);
                return out;
                
            case HALF:
                out = new byte[2];
                out[0] = (byte) ((val & m3) >>> 8);
                out[1] = (byte) (val & m4);
                return out;
                
            case BYTE:
                out = new byte[1];
                out[0] = (byte) (val & m4);
                return out;
        }
    
        return null;
    }
    
    /**
     * Regroupe un ensemble d'octet entre eux pour former un entier.
     * @param u Mot, Demi-mot, octet : La taille du l'entier final
     * @param bytes l'ensemble des octets à regrouper
     * @return un entier
     */
    public static int bytesToInt(Unit u, byte[] bytes) {
        int out = 0;
        
        switch (u) {
            case WORD:
                out += bytes[0] << 24;
                out += bytes[1] << 16;
                out += bytes[2] << 8;
                out += bytes[3];
            
            case HALF:
                out += bytes[0] << 8;
                out += bytes[1];
            
            case BYTE:
                out += bytes[0];
        }
        
        return out;
    }
    
    public static int nearest2Power(int val) {
        int compteur = 0;
        
        while (val != 0) {
            val /= 2;
            ++compteur;
        }
        
        return (1 << (compteur-1));
    }
    
    /**
     * Renvoi une représentation binaire d'un entier.
     * @param val entier à convertir
     * @return String
     */
    public static String toBin(int val) {
        String out = new String();
        
        for (int i = 0; i < 32; ++i) {
            out = "" + val % 2 + out;
            val /= 2;
        }
        
        return out;
    }
    
    /**
     * Renvoi une représentation binaire d'un entier.
     * @param val entier à convertir
     * @param len longueur en nombre de bits
     * @return String
     */
    public static String toBin(int val, int len) {
        String out = new String();
        
        for (int i = 0; i < len; ++i) {
            out = "" + val % 2 + out;
            val /= 2;
        }
        
        return out;
    }
    
    /**
     * Renvoi une représentation binaire d'un entier.
     * @param u taille des données
     * @param bytes tableau d'octet
     * @return String
     */
    public static String toBin(Unit u, byte[] bytes) {
        return Numbers.toBin(bytesToInt(u, bytes));
    }
    
    /**
     * Renvoi une représentation binaire d'un entier.
     * @param u taille des données
     * @param bytes tableau d'octet
     * @param len longueur en nombre de bits
     * @return String
     */
    public static String toBin(Unit u, byte[] bytes, int len) {
        return Numbers.toBin(bytesToInt(u, bytes), len);
    }
    
    
    /**
     * Renvoi une représentation hexadécimale d'un entier.
     * @param val entier à convertir
     * @return String
     */
    public static String toHex(int val) {
        String out = new String();
        int reste;
        
        while (val != 0) {
            reste = val % 16;
            
            if (reste == 15)        out = "F" + out;
            else if (reste == 14)   out = "E" + out;
            else if (reste == 13)   out = "D" + out;
            else if (reste == 12)   out = "C" + out;
            else if (reste == 11)   out = "B" + out;
            else if (reste == 10)   out = "A" + out;
            else                    out = "" + reste + out;
            
            val /= 16;
        }
        
        // Bourrage de 0 avant la valeur
        final int len = out.length();
        for (int i = 8; i > len; --i) {
            out = "0" + out;
        }
        out = "0x" + out;
        
        return out;
    }
    
    /**
     * Renvoi une représentation hexadécimale d'un entier.
     * @param val entier à convertir
     * @param len longueur en nombre de valeur hexa. (doit être un multiple de 4)
     * @return String
     */
    public static String toHex(int val, int len) {
        if (len % 4 != 0) len += 1;
        
        String out = new String();
        int reste;
        
        while (val != 0 || len >= 0) {
            reste = val % 16;
            
            if (reste == 15)        out = "F" + out;
            else if (reste == 14)   out = "E" + out;
            else if (reste == 13)   out = "D" + out;
            else if (reste == 12)   out = "C" + out;
            else if (reste == 11)   out = "B" + out;
            else if (reste == 10)   out = "A" + out;
            else                    out = "" + reste + out;
            
            val /= 16;
            --len;
        }
        
        // Bourrage de 0 avant la valeur
        final int l = out.length();
        for (int i = 8; i > l; --i) {
            out = "0" + out;
        }
        out = "0x" + out;
        return out;
    }
    
    /**
     * Renvoi une représentation hexadécimale d'un entier.
     * @param u taille des données
     * @param bytes tableau d'octet
     * @return String
     */
    public static String toHex(Unit u, byte[] bytes) {
        return toHex(bytesToInt(u, bytes));
    }
    
    /**
     * Renvoi une représentation hexadécimale d'un entier.
     * @param u taille des données
     * @param bytes tableau d'octet
     * @param len longueur en nombre de valeur hexa. (doit être un multiple de 4)
     * @return String
     */
    public static String toHex(Unit u, byte[] bytes, int len) {
        return toHex(bytesToInt(u, bytes), len);
    }
}
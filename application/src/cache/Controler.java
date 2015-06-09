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


package cache;

import cache.exception.AddressNotAlignedException;
import Utils.Range;
import cache.type.PolicyType;
import cache.type.Type;
import cache.type.Unit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import test.TU;


/**
 *
 * @author leo
 * @author maignial
 */
public class Controler {
    public static int wc = 0;
    private final List<Layer> layers;
    private int[] missCount;
    private int[] hitCount;
    
    private Integer _final;
    
    /* pour l'affichage du debug */
    private int indentLevel = -1;
    private String indent() {
        String output = new String();
        for (int i=0 ; i<indentLevel ; ++i) output += "\t";
        return output;
    }
    
    // CONSTRUCTEURS -----------------------------------------------------------
    public Controler() {
        layers = new ArrayList<>();
        missCount = new int[0];
        hitCount = new int[0];
        _final = null;
        
    }
    
    public Controler(List<Layer> layers) {
        this.layers = layers;
        missCount = new int[layers.size()];
        hitCount = new int[layers.size()];
    }
    
    // METHODES ----------------------------------------------------------------
    public int size() {
        return layers.size();
    }
    
    public void add(Layer l) {
        layers.add(l);
        missCount = new int[layers.size()];
        hitCount = new int[layers.size()];
    }
    
    public void add(Layer... layers) {
        for (Layer l : layers) {
            add(l);
        }
        
        missCount = new int[this.layers.size()];
        hitCount = new int[this.layers.size()];
    }
    
    public void add(int pos, Layer l) {
        layers.add(pos, l);
        missCount = new int[layers.size()];
        hitCount = new int[layers.size()];
    }
    
    public Layer remove(int pos) {
        missCount = new int[layers.size()-1];
        hitCount = new int[layers.size()-1];
        return layers.remove(pos);
    }
    
    public Layer get(int pos) {
        return layers.get(pos);
    }
    
    
    public void reset() {
        layers.clear();
        Arrays.fill(missCount, 0);
    }
    
    public int getTotalMiss() {
        int sum = 0;
        for (int i=0 ; i<missCount.length ; ++i)
            sum += missCount[i];
        return sum;
    }
    
    public int getTotalHit() {
        int sum = 0;
        for (int i=0 ; i<hitCount.length ; ++i)
            sum += hitCount[i];
        return sum;
    }
    
    @Override
    public String toString() {
        String out = "" + layers.size() + " layers danc ce controleur\n";
        
        int compteur = 1;
        for (Layer l : layers) {
            out += "L" + compteur + " connecté à un cache de " + l.getSelected().toString() + "\n";
            compteur++;
        }
        
        return out;
    }
            
    
    /***************************************************************************
     *      
     *      GESTION DES ENTRÉES / SORTIES
     * 
     * ************************************************************************/
    
    /**
     * Effectue la lecture dans le cache.
     * @param source le cache lu
     * @param u Longueur des données lues (half, byte, word)
     * @param addr Addresse mémoire demandée
     * @return Les octets lus
     * @throws cache.AddressNotAlignedException
     */
    private byte[] read(Layer source, Unit u, int addr)
    throws AddressNotAlignedException {
        final byte[] buffer;
        switch (u) {
            case WORD: buffer = source.readWord(addr);
                break;
            case HALF: buffer = source.readHalf(addr);
                break;
            case BYTE: buffer = source.readByte(addr);
                break;
            default : buffer = null;
                break;
        }
        
        ++ indentLevel;
        System.err.println(TU.ANSI_GREEN + indent() +
                "READ L" + layers.indexOf(source) +
                ": ("+addr+") " + 
                Arrays.toString(buffer) +
                TU.ANSI_RESET);
        -- indentLevel;
        
        return buffer;
    }

    /**
     * Interface entre le processeur et la mémoire, Permet de lire une donnée
     * ou une instruction, Assure le même fonctionnement qu'une implémentation
     * physique des mémoires caches.
     * @param t Type de donnée lue (donnée instruction) 
     * @param u Longueur des données lues (half, byte, word)
     * @param addr Addresse mémoire demandée
     * @return un int contenant l'information demandé par le processeur
     * @throws cache.exception.AddressNotAlignedException
     */
    public byte[] read(Type t, Unit u, int addr) 
    throws AddressNotAlignedException {
        Layer source;
        _final = null;
        
        wc++;
        System.err.println("\n" + TU.ANSI_RED +
                "UC READ AT (" + addr + ")" + TU.ANSI_RESET);
        
        // Vérification de la présence de mémoire (en théorie on a toujours au moins la ram)
        if ((source = firstValidLayer(t, 0)) != null) {
            // Si données absentes dans le cache on ramène depuis le lvl supérieur
            if (!source.hitOnRead(addr)) {
                updateDown(t, addr, source);
                missCount[layers.indexOf(source)] += 1;
            } else {
                hitCount[layers.indexOf(source)] += 1;
            }
            
            return read(source, u, addr);
        }
        
        // Mémoire absente...
        return null;
    }
    
    /**
     * Effectue l'écriture dans un cache.
     * @param t le type de donnée écrite (data, instruction)
     * @param dest le cache où écrire
     * @param u Longueur des données lues (half, byte, word)
     * @param addr addresse mémoire où écrire
     * @param bytes données à écrire
     * @param flag effectue ou non le contrôle de hit
     * @return vrai si l'écriture à fonctionnée, faux sinon
     * @throws cache.AddressNotAlignedException
     */
    private boolean write(Type t, Layer dest, Unit u, int addr, byte[] bytes, boolean flag)
    throws AddressNotAlignedException {
        ++ indentLevel;
        boolean state = false;
        
        
        System.err.println(TU.ANSI_CYAN + indent() +
                "WRITE L" + (layers.indexOf(dest)+1) + ": ("+addr+") " +
                Arrays.toString(bytes) +
                TU.ANSI_RESET);
        
        
        /* on vérifie qu'on peut accéder aux données dans dest */
        if (flag && !dest.hitOnWrite(addr)) {
            
            
            System.err.println(TU.ANSI_CYAN + indent() +
                    "miss...");
            missCount[layers.indexOf(dest)] += 1;
            
            --indentLevel;
            updateDown(t, addr, dest);
            ++indentLevel;
        } else {
                hitCount[layers.indexOf(dest)] += 1;
        }

        switch (u) {
            case WORD: state = dest.writeWord(addr, bytes); break;
            case HALF: state = dest.writeHalf(addr, bytes); break;
            case BYTE: state = dest.writeByte(addr, bytes); break;
        }
        if (state == false){
            --indentLevel;
            System.err.println("write to L" + layers.indexOf(dest)+1 + " failed");
            return false;
        }
        
        /* gestion de l'écriture directe */
        if (dest.getPolicy() == PolicyType.DIRECT) {
            Layer up = firstValidLayer(t, layers.indexOf(dest)+1);
            -- indentLevel;
            if (up == null) {
                System.err.println("no up layer to write");
                return state;
            }
            state = write(t, up, u, addr, bytes, flag);
            ++ indentLevel;
        }
        
        -- indentLevel;
        return state;
    }

    /**
     * Interface entre le processeur et la mémoire, Permet d'écrire une donnée
     * ou une instruction, Assure le même fonctionnement qu'une implémentation
     * physique des mémoires caches.
     * @param t type de donnée écrite (donnée instruction)
     * @param u Longueur des données écrites (byte, half, word)
     * @param addr addresse mémoire où écrire
     * @param bytes données à écrire
     * @return vrai si l'écriture à fonctionnée, faux sinon
     * @throws cache.exception.AddressNotAlignedException
     */
    public boolean write(Type t, Unit u, int addr, byte[] bytes)
    throws AddressNotAlignedException {
        Layer dest;
        int i = -1;
        boolean writeOK;
        
        System.err.println(TU.ANSI_RED +
                "\nUC WRITE AT (" + addr +")" +
                TU.ANSI_RESET);

        /* On vérifie qu'on a bien de la mémoire */
        if ((dest = firstValidLayer(t, ++i)) != null) {
            wc++;
            writeOK = write(t, dest, u, addr, bytes, true);
        } else { // pas de mémoire...
            return false;
        }
        
        return writeOK;
    }
    
    /**
     * Met à jour le layer affin qu'il contienne les données d'adresse donnée.
     * @param t type des données (data, instruction)
     * @param addr addresse mémoire des données
     * @param toUpdate le layer à mettre à jour
     * @return vrais si l'update a fonctionné, faux sinon
     */
    private boolean updateDown (Type t, int addr, Layer toUpdate) {
        ++ this.indentLevel;
        System.err.println(TU.ANSI_PURPLE + indent() +
                "START UPDATE DOWN: L" +
                (layers.indexOf(toUpdate)+1) +
                " (" + addr + ")" +
                TU.ANSI_RESET);
        // on peut pas mettre la ram à jour depuis un niveau supérieur...
        if (toUpdate.isRam()) {
            System.err.println("Vous n'allez pas mettre à jour une ram non?!");
            return false;
        }
        
        /* Le layer supérieur et le bloc à mettre à jour */
        Layer from = firstValidLayer(t, layers.indexOf(toUpdate)+1);
        if (from == null) {
            System.err.println(TU.ANSI_PURPLE + indent() +
                "END UPDATE DOWN: L" +
                (layers.indexOf(toUpdate)+1) +
                " (" + addr + ")" +
                    TU.ANSI_RESET);
            -- this.indentLevel;
            return false;
        }
        Block updated = toUpdate.getNextBlock(addr);
        
        Range upR = updated.getRange();
        upR.start += updated.getStartAddr();
        upR.end += updated.getStartAddr();
        byte[] buffer;
        
        /* en cas de validité du bloc on doit le sauvegarder */
        if (toUpdate.isNextBlockToSave(addr)) {
            System.err.print(TU.ANSI_PURPLE + indent() +
                    "\tSauvegarde du bloc d'adresse " + upR.start +
                    " " +
                    TU.ANSI_RESET);
            TU.drawData(updated.getData(), TU.ANSI_PURPLE);
            System.err.println("");
            /* Lecture du bloc et écriture dans le layer supérieur */
            for (int i=upR.start ; i<=upR.end ; i+=toUpdate.getWordSize()) {
                try {
                    buffer = read(toUpdate, Unit.WORD, i);
                    //toUpdate.readWord(i);
                    write(t, from, Unit.WORD, i, buffer, true);
                } catch (AddressNotAlignedException ex) {
                    System.err.println("On a un soucis lors de la sauvegarde"
                            + " du bloc dans le layer supérieur.");
                    return false;
                }
            }
        }
        
        /* màj des métadonnées du bloc */
        upR = updated.getRange();
        upR.start += toUpdate.blockAlignedAddress(addr);
        upR.end += toUpdate.blockAlignedAddress(addr);

        /* lecture dans le layer supérieur et écriture dans le layer à màj */
        System.err.println(TU.ANSI_PURPLE + indent() +
                "\tRécupération des données de L" + (layers.indexOf(from)+1) +
                TU.ANSI_RESET);
        for (int i=upR.start ; i<=upR.end ; i+=updated.getWordSize()) {
            if (!from.hitOnRead(i)) {
                System.err.println(TU.ANSI_PURPLE + indent() +
                        "\tmiss..." +
                        TU.ANSI_RESET);
                missCount[layers.indexOf(from)] += 1;
                updateDown(t, i, from);
            } else {
                hitCount[layers.indexOf(from)] += 1;
            }
            try {
                buffer = read(from, Unit.WORD, i);
                write(t, toUpdate, Unit.WORD, i, buffer, false);
            } catch (AddressNotAlignedException ex) {
                System.err.println("On a un soucis sur la màj du bloc depuis "
                        + "le layer supérieur...");
                return false;
            }
        }
        System.err.println(TU.ANSI_PURPLE + indent() +
                "END UPDATE DOWN: L" +
                (layers.indexOf(toUpdate)+1) +
                " (" + addr + ")" +
                TU.ANSI_RESET);
        -- this.indentLevel;
        return true;
    }
    
    /**
     * Se connecte au premier layer supérieur possédant un cache de type t
     * @param t type attendut
     * @param start index du layer
     * @return Le layer concerné
     */
    private Layer firstValidLayer(Type t, int start){
        Layer source = null;
        boolean find = false;

        for (int j = start; j < layers.size() && !find; ++j) {
            source = layers.get(j);

            find = (source.connectTo(t));
        }
        return source;
    }
    
    private Layer lastFirstValidLayer(Type t, int start) {
        Layer source = null;
        boolean find = false;
        
        for (int j = start; j >= 0 && !find; --j) {
            source = layers.get(j);
            find = (source.connectTo(t));
        }
        return source;
    }
}

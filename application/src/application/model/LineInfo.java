package application.model;

// TODO Cacluler l'offset !!

import Utils.Numbers;
import cache.Block;
import cache.Cache;
import cache.Controler;
import cache.type.Unit;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

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

/**
 *
 * @author leo
 */
public class LineInfo {
    public boolean modified;                    // Bloc modifié
    public boolean validity;                    // validité du bloc
    public final Cache reference;               // Le cache dont on veut représenter les informations
    public String tag, index, offset;           // Informations relatives aux TAG du bloc
    public int tagLong, indexLong, offsetLong;   // Nombre de bits nécessaires
    List<String> words;                         // les données du bloc
    
    public LineInfo(Cache reference, boolean validity, int tag, int index, int offset) {
        this.reference  = reference;
        this.validity   = validity;
        
        // calcul du nombre de bits necessaire
        indexLong   = (byte) ((Math.log(reference.size())) / Math.log(2));
        offsetLong  = (byte) ((Math.log(reference.getWordSize() *
                reference.getBlockSize())) / Math.log(2));
        tagLong     = (byte) (32 - indexLong - offsetLong);
        
        this.tag    = Numbers.toBin(tag, tagLong);
        this.index  = Numbers.toBin(index, indexLong);
        this.offset = Numbers.toBin(offset, offsetLong);
    }
    
    public LineInfo(Cache reference, Block b) {
        this.reference = reference;
        
         // calcul du nombre de bits necessaire
        indexLong   = (byte) ((Math.log(reference.size())) / Math.log(2));
        offsetLong  = (byte) ((Math.log(reference.getWordSize() *
                reference.getBlockSize())) / Math.log(2));
        tagLong     = (byte) (32 - indexLong - offsetLong);
        
        this.tag        = Numbers.toBin(b.getIndex(), tagLong);
        this.index      = Numbers.toBin(b.getTag(), indexLong);
        this.offset     = Numbers.toBin(0, offsetLong);
        this.validity   = b.isValid();
        this.modified   = isModified(b);
        this.words      = new ArrayList<>(b.getData().size());
                
        for (byte[] word : b.getData()) {
            words.add(Numbers.toHex(Unit.WORD, word));
        }
    }
    
    private boolean isModified(Block b) {
        return b.wc == Controler.wc;
    }
    
    @Override
    public String toString() {
        return validity + " " + tag + " " + index + " " + offset + " = " +
                Arrays.toString(words.toArray());
    }
}

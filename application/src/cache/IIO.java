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

/**
 *
 * @author leo
 */
public interface IIO {
    
    /**
     * Permet d'écrire un mot à l'adresse donnée.
     * @param addr
     * @param bytes
     * @return vrai si l'écriture a réussi, faux sinon.
     * @throws cache.exception.AddressNotAlignedException
     */
    public boolean writeWord(int addr, byte[] bytes) throws AddressNotAlignedException;
    
    /**
     * Permet d'écrire un demi mot à l'adresse donnée, pos détermine dans quelle
     * moitié du mot il faut écrire.
     * @param addr
     * @param bytes
     * @return vrai si l'écriture a réussi, faux sinon.
     * @throws cache.exception.AddressNotAlignedException
     */
    public boolean writeHalf(int addr, byte[] bytes)throws AddressNotAlignedException;
    
    /**
     * Permet d'écrire un octet à l'adresse donnée, offset détermine sa position
     * dans le mot.
     * @param addr
     * @param bytes
     * @return vrai si l'écriture a réussi, faux sinon.
     */
    public boolean writeByte(int addr, byte[] bytes);
    
    /**
     * Permet de lire le mot à l'adresse donnée.
     * @param addr
     * @return une liste de Byte représentant un mot
     * @throws cache.exception.AddressNotAlignedException
     */
    public byte[] readWord(int addr) throws AddressNotAlignedException;
    
    /**
     * Permet de lire un demi mot à l'adresse donnée, pos détermine quelle
     * moitié du mot à lire.
     * @param addr
     * @return une liste de Byte représentant le demi mot
     * @throws cache.exception.AddressNotAlignedException
     */
    public byte[] readHalf(int addr) throws AddressNotAlignedException;
    
    /**
     * Permet de lire un octet à l'adresse donnée, offset détermine la position
     * de l'octet à lire.
     * @param addr
     * @return un octet.
     */
    public byte[] readByte(int addr);
}

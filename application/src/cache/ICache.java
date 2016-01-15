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

import cache.scheduler.Scheduler;
import cache.scheduler.SchedulerType;
import cache.type.Format;
import cache.type.PolicyType;
import cache.type.Type;

/**
 *
 * @author leo
 */
public interface ICache{
    /**
     * Détermine si l'écriture déclanche un hit ou un miss.
     * @param addr adresse mémoire
     * @return true si hit, false si miss
     */
    public boolean hitOnWrite(int addr);
    
    /**
     * Détermine si la lecture déclanche un hit ou un miss.
     * @param addr adresse mémoire
     * @return true si hit, false si miss
     */
    public boolean hitOnRead(int addr);
    
    /**
     * Permet de déterminer un adresse alignée sur un bloc du cache.
     * @param addr adresse mémoire quelconque
     * @return l'adresse alignée sur le début d'un bloc
     */
    public int blockAlignedAddress(int addr);
    
    /**
     * Renvoi le nombre de mots par blocs
     * @return la taille d'un bloc
     */
    public int getBlockSize();
    
    /**
     * Renvoie la taille en octet d'un mot.
     * @return la taille d'un mot
     */
    public int getWordSize();
    
    /**
     * Permet de connître la politique d'un cache.
     * @return le type de politique
     */
    public PolicyType getPolicy();
    
    /**
     * Permet de connaître le type d'un cache (DATA, INSTRUCTION, UNIFIED, (RAM))
     * @return Le type du cache
     */
    public Type getType();
    
    /**
     * Permet de connaître le format d'un cache( Direct mapped cache, 
     * Full associative cache, Set associative cache)
     * @return le format du cache
     */
    public Format getFormat();
    
    /**
     * Permet de connaitre le scheduler untilisé par le cache
     * @return type du scheduler
     */
    public SchedulerType getScheduler();
    
    /**
     * Renvoie la taille d'un cache en nombre de bloc.
     * @return le nombre de bloc
     */
    public int size();
    
    /**
     * Renvoie la taille totale en octet d'un cache
     * @return le nombre d'octet du bloc
     */
    public int totalSize();
    /**
     * Calcule le tag d'un bloc à partir de l'adresse
     * @param addr adresse mémoire
     * @return le tag d'un bloc
     */
    public int addrToTag(int addr);
    
    /**
     * Calcule l'index mémoire à partir de l'adresse
     * @param addr adresse mémoire
     * @return l'index (le numero de page) mémoire contenant l'adresse
     */
    public int addrToIndex(int addr);
    
    /**
     * Détermine si le cache est utilisé pour simuler une RAM
     * @return true si c'est un RAM, faux sinon
     */
    public boolean isRam();
    
    /**
     * Détermine le prochain block à écrire dans le cache.
     * @param addr l'adresse mémoire où l'on souhaite écrire.
     * @return l'identifiant du bloc.
     */
    public Block getNextBlock(int addr);
    
    /**
     * Détermine si le prochain bloc écrit dans le cache doit être sauvé.
     * @param addr addresse mémoire à accéder.
     * @return vrai si le bloc doit être sauvé, faux sinon.
     */
    public boolean isNextBlockToSave(int addr);
}

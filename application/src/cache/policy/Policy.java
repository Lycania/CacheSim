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
package cache.policy;

import cache.type.PolicyType;

/**
 *
 * @author maignial
 */
public abstract class Policy {
    protected final int size;

    public Policy(int blocksCount) {
        this.size = blocksCount;
    }
    
    /**
     * Dit si la mise à jour du bloc force l'écriture d'anciennes données dans la mémoire de niveau supérieur.
     * @param blocNumber adresse de la donnée à accéder
     * @return
     */
    public abstract boolean readyToUpdate(int blocNumber);
    
    /**
     * Permet de prendre en compte une écriture par une entité de niveau inferieur (cache ou UC).
     * @param blocNumber le numero du bloc où est écrite la donnée
     */
    public abstract void writeData(int blocNumber);
    
    /**
     * Permet de dire à la politique que la donnée contenue dans ce bloc
     * à été mise à jour (écrite par le niveau supérieur).
     * @param blocNumber le numero du bloc mis à jour
     */
    public abstract void updateBlock(int blocNumber);
    
    /**
     * Permet de connaître le type de politique.
     * @return le type de politique.
     */
    public abstract PolicyType getPolicyType();
}

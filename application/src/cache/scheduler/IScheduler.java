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
package cache.scheduler;

/**
 *
 * @author maignial
 */
public interface IScheduler {
    
    /**
     * Calcul le prochain index à traiter selon le scheduler.
     * @return le procahin index
     */
    public int nextIndex();
    
    /**
     * Indique au scheduler qu'un index a été accedé (lecture écriture)
     * @param index l'index accedé
     */
    public void indexAccessed(int index);
    
    /**
     * Indique au scheduler qu'un index a été remplacé
     * @param index l'index accedé
     */
    public void indexUpdated(int index);
    
    /**
     * 
     * @return l'index maximum du scheduler
     */
    public int maxIndex();
    
    /**
     * Renvoi l'index du bloc courant dans le cache
     * @return l'index du bloc courant
     */
    public int getCurrent();
}

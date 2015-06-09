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
package AssemblyModule;

import cache.type.Type;
import cache.type.Unit;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author maignial
 */
public class MemoryAccessBuffer {
    public static final int READ = 0;
    public static final int WRITE = 1;
    
    /**
     * Word, Half ou Byte.
     */
    public Unit dataSize;
    
    /**
     * Lecture ou Ecriture.
     */
    public final int accessType;
    
    /**
     * Données ou Instructions
     */
    public final Type cacheType;
    
    /**
     * Adresses accedées.
     */
    public List<Integer> addresses = new ArrayList<>();
    
    /**
     * Données fournies (utiles uniquement en écriture).
     */
    public List<Integer> data;

    public MemoryAccessBuffer(int accessType, Unit dataType, Type cacheType) {
        this.accessType = accessType;
        this.dataSize = dataType;
        this.cacheType = cacheType;
        
        if (this.accessType == WRITE)
            this.data = new ArrayList<>();
        else
            this.data = null;
    }
}

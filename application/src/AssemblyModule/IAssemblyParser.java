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

/**
 *
 * @author maignial
 */
public interface IAssemblyParser {
    
    /**
     * Cherche si l'instruction effectue un accès mémoire ou non.
     * @param instr l'instruction (sous forme texte).
     * @return vrai si un accès mémoire est effectué, faux sinon.
     */
    public boolean isMemoryAccess(String instr);
    
    /**
     * Decode une instruction d'accès mémoire pour fournir les adresses et données
     * accédées.
     * @param stepInResponse la réponse fournie par une commande StepIn (voir protocole)
     * @return Une structure de donnée contenant les informations.
     */
    public MemoryAccessBuffer decodeInstruction(String stepInResponse);
}

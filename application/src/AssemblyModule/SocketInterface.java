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

import cache.exception.AddressNotAlignedException;
import cache.Ram;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe servant à la communication avec le simulateur d'execution
 * @author maignial
 */
public class SocketInterface {
    private final Socket client;

    public SocketInterface(Socket client) {
        this.client = client;
    }
    
 
    
    /**
     * Demande au simulateur d'effectuer la prochaine instruction.
     * @return l'instruction et les registres avant execution (voir le protocole dans la doc)
     */
    public String stepIn() {
        return sendCommand("StepIn");
    }
    
    /**
     * Demande la ram au simulateur et l'initialise.
     * @return la ram initialisée
     */
    public Ram getMemory() {
        String memoryRange = getMemoryRange();
        int indexStart = memoryRange.lastIndexOf(' ') + 1;
        int indexStop = memoryRange.lastIndexOf(';');
        int endAddress = Integer.parseInt(memoryRange.substring(indexStart, indexStop));
        indexStart = memoryRange.indexOf(' ') + 1;
        indexStop = memoryRange.indexOf('\n');
        int addr = Integer.parseInt(memoryRange.substring(indexStart, indexStop));
        
        Ram memory = new Ram(endAddress+64, 4);
        
        byte[] buffer = new byte[4];
        InputStream input;
        int n = 0;
        String command = "getMemory";
        
        try {
            client.getOutputStream().write(command.getBytes());
            input = client.getInputStream();
            while (-1 != (n=input.read(buffer, 0, 4)) && buffer[n] != ';') {
                memory.writeWord(addr, buffer);
                addr += 4;
            }
            
        } catch (IOException | AddressNotAlignedException ex) {
            Logger.getLogger(SocketInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return memory;
    }
    
    /**
     * Demande au simulateur quel est le segment de mémoire utile.
     * @return l'adresse de début et de fin de l mémoire utile (voir protocole dans la doc)
     */
    public String getMemoryRange() {
        return sendCommand("GetMemoryRange");
    }
    
    /**
     * Demande l'arrêt du simulateur.
     * @return true si le socket est bien fermé, false sinon; Attention, ce résultat peut parfois être incohérent!
     */
    public boolean abortSim () {
        sendCommand("AbortSim");
        try {
            Thread.sleep(10);
        } catch (InterruptedException ex) {
            Logger.getLogger(SocketInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
        return !this.client.isClosed();
    }
    
    /**
     * envoie une commande au simulateur.
     * @param command la commande
     * @return le résultat de la commande (voir le protocole dans la doc)
     */
    private String sendCommand(String command) {
        byte[] buffer = new byte[1024];
        
        try {
            client.getOutputStream().write(command.getBytes());
            client.getInputStream().read(buffer, 0, 1024);

        } catch (IOException ex) {
            Logger.getLogger(SocketInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return new String(buffer);
    }
}
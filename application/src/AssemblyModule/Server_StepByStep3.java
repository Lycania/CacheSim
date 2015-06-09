package AssemblyModule;


import Utils.Numbers;
import cache.exception.AddressNotAlignedException;
import cache.Controler;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

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
public class Server_StepByStep3 implements Runnable {
    /* Initialisation ------------------------------------------------------- */
    private final ArmParser p = new ArmParser();
    private final Controler controler;
    private SocketInterface si;
    private boolean end, stepIn;
    
    private MemoryAccessBuffer  decoded;
    private String              instruction;
    ServerSocket    socket;
    Socket          client;
    

    public Server_StepByStep3(Controler controler) {
        this.controler = controler;
        end = stepIn = false;
    }
    
    @Override
    public void run() {
        
        // connection en localHost sur le port 5321
        try {
            socket = new ServerSocket(5321);
            client = socket.accept();
            si = new SocketInterface(client);
            
        } catch (IOException ex) {
            System.err.println("Impossible de se connecter.");
            kill();
        }
        
        
        while (!end) {
            try {
                if (stepIn) {
                    stepIn = false;
                    instruction = si.stepIn();
                    decoded = p.decodeInstruction(instruction);

                    if (instruction.startsWith("ExecutionFinished")) {
                        end = true;
                        continue;
                    }
                    if (decoded == null) continue;

                    // Écriture ---------
                    if (decoded.accessType == MemoryAccessBuffer.WRITE) {
                        try {
                            for (int i = 0; i < decoded.addresses.size(); ++i) {
                                byte[] word = Numbers.intToBytes(decoded.dataSize, decoded.data.get(i));
                                controler.write(decoded.cacheType, decoded.dataSize, decoded.addresses.get(i), word);
                            }
                        } catch (AddressNotAlignedException ex) {
                        }

                    // Lecture --------
                    } else {
                        try {
                            for (int addr : decoded.addresses) {
                                controler.read(decoded.cacheType, decoded.dataSize, addr);
                            }
                        } catch (AddressNotAlignedException ex) {
                        }
                    }
                }

               Thread.sleep(100);
                
            } catch (InterruptedException e) {
                System.out.println("Une interruption a été demandé");
                Thread.interrupted();
            }
        }
        
        kill();
    }
    
    /**
     * Demande au client d'executer l'instruction suivante
     */
    public void nextStep() {
        stepIn = true;
        Thread.interrupted();
    }
    
    public void kill() {
        si.abortSim();
        
        try {
            socket.close();
        } catch (IOException ex) {
        }
        
        Thread.interrupted();
    }
}

package AssemblyModule;


import Utils.Numbers;
import application.CacheSimulator;
import cache.exception.AddressNotAlignedException;
import cache.Controler;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

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
public class Server_StepByStep extends Thread {
    /* Initialisation ------------------------------------------------------- */
    private final ArmParser p = new ArmParser();
    private final Controler controler;
    private final CacheSimulator simulator;
    private SocketInterface si;
    private boolean stepIn;
    private boolean run = true;
    
    private MemoryAccessBuffer  decoded;
    private String              instruction;
    ServerSocket    socket;
    Socket          client;
    

    public Server_StepByStep(Controler controler, CacheSimulator simulator) {
        this.controler = controler;
        this.simulator = simulator;
    }
    
    @Override
    public void run() {
        while (run) {
            if (stepIn) {
                stepIn = false;
                System.err.println(instruction);
                
                if (instruction.startsWith("ExecutionFinished")) {
                    System.out.println("ExecutionFinished!");
                    continue;
                }
                
                decoded = p.decodeInstruction(instruction);
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
                
                simulator.refresh();
            }
            
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                System.err.println("interompu :)");
            }
        }
        
        kill();
    }
    
    /**
     * Demande au client d'executer l'instruction suivante
     * @return l'instruction executée
     */
    public String nextStep() {
        instruction = si.stepIn();
        stepIn = true;
        
        return instruction.split("\n")[0];
    }
    
    public void kill() {
        run = false;
        si.abortSim();
        
        try {
            socket.close();
        } catch (IOException ex) {
        }
    }

    @Override
    public synchronized void start() {
        // connection en localHost sur le port 5321
        try {
            socket = new ServerSocket(5321);
            client = socket.accept();
            si = new SocketInterface(client);
            super.start();
            
        } catch (IOException ex) {
            System.err.println("Impossible de se connecter.");
        }
    }
}

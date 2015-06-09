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

/**
 *
 * @author maignial
 */
public class ArmParser implements IAssemblyParser {
    
    @Override
    public boolean isMemoryAccess(String instr) {
       
        return  instr.startsWith("ldr") ||
                instr.startsWith("str") ||
                instr.startsWith("ldm") ||
                instr.startsWith("stm");
    }

    @Override
    public MemoryAccessBuffer decodeInstruction(String stepInResponse) {
        MemoryAccessBuffer output = null;
        String[] split1, split2;
        String tmp;
        String instr;
        String registers;
        int cpsr;
        int startAddr;
        
        split1 = stepInResponse.split("\n");
        instr = split1[0];
        registers = split1[1];
        cpsr = Integer.parseInt(split1[2].substring(0, split1[2].length()-1));
        startAddr = this.processingAddress(instr, registers, cpsr);
        
        /* famille ldr */
        if (instr.startsWith("ldrh")) {
            output = new MemoryAccessBuffer(MemoryAccessBuffer.READ, Unit.HALF,
                    Type.DATA);
            output.addresses.add(startAddr);
        } else if (instr.startsWith("ldrb")) {
            output = new MemoryAccessBuffer(MemoryAccessBuffer.READ, Unit.BYTE,
                    Type.DATA);
            output.addresses.add(startAddr);
        } else if (instr.startsWith("ldr")) {
            output = new MemoryAccessBuffer(MemoryAccessBuffer.READ, Unit.WORD,
                    Type.DATA);
            output.addresses.add(startAddr);
        }
        
        /* famille ldm */
        if (instr.startsWith("ldm")) {
            output = new MemoryAccessBuffer(MemoryAccessBuffer.READ, Unit.WORD,
                    Type.DATA);
            /* nombre de registres concernés */
            tmp = instr.substring(instr.indexOf('{')+1, instr.indexOf('}'));
            int regCount = 0;
            split1 = tmp.split(",");
            for (int i=0 ; i<split1.length ; ++i) {
                if (split1[i].contains("-")) {
                    split2 = split1[i].split("-");
                    regCount += this.registerNameToNumber(split2[1]) -
                                this.registerNameToNumber(split2[0]);
                } else {
                    ++ regCount;
                }
            }
            
            int incrementalValue = 0;
            
            if (instr.startsWith("ldmed") || instr.startsWith("ldmib")) {
                incrementalValue = 4;
            }
            if (instr.startsWith("ldmfd") || instr.startsWith("ldmia")) {
                startAddr += 4;
                incrementalValue = 4;
            }
            if (instr.startsWith("ldmea") || instr.startsWith("ldmdb")) {
                incrementalValue = -4;
            }
            if (instr.startsWith("ldmfa") || instr.startsWith("ldmda")) {
                startAddr -= 4;
                incrementalValue = -4;
            }
            
            for (int i=0 ; i<regCount ; ++i) {
                output.addresses.add(startAddr);
                startAddr += incrementalValue;
            }
        }
        
        /* famille str */
        if (instr.startsWith("str")) {
            if (instr.startsWith("strh"))
                output = new MemoryAccessBuffer(MemoryAccessBuffer.WRITE,
                        Unit.HALF, Type.DATA);
            else if (instr.startsWith("strb"))
                output = new MemoryAccessBuffer(MemoryAccessBuffer.WRITE,
                        Unit.BYTE, Type.DATA);
            else
                output = new MemoryAccessBuffer(MemoryAccessBuffer.WRITE,
                        Unit.WORD, Type.DATA);
            
            output.addresses.add(startAddr);
            split1 = instr.split(",");
            split1 = split1[0].split(" ");
            int dataReg = this.registerNameToNumber(split1[1]);
            int dataValue = this.regNumberToValue(registers, dataReg);
            output.data.add(dataValue);
        }
        
        /* famille stm */
        if (instr.startsWith("stm")) {
            output = new MemoryAccessBuffer(MemoryAccessBuffer.WRITE,
                        Unit.WORD, Type.DATA);
            /* nombre de registres concernés */
            tmp = instr.substring(instr.indexOf('{')+1, instr.indexOf('}'));
            int regCount = 0;
            split1 = tmp.split(",");
            int dataReg;
            int dataValue;
            for (int i=0 ; i<split1.length ; ++i) {
                if (split1[i].contains("-")) {
                    split2 = split1[i].split("-");
                    for (   dataReg = registerNameToNumber(split2[0]) ;
                            dataReg <= registerNameToNumber(split2[1]) ;
                            ++ dataReg){
                        ++ regCount;
                        dataValue = regNumberToValue(registers, dataReg);
                        output.data.add(dataValue);
                    }
                } else {
                    dataReg = registerNameToNumber(split1[i]);
                    dataValue = regNumberToValue(registers, dataReg);
                    output.data.add(dataValue);
                    ++ regCount;
                }
            }
            
            int incrementalValue = 0;
            
            if (instr.startsWith("stmfa") || instr.startsWith("stmib")) {
                incrementalValue = 4;
            }
            if (instr.startsWith("stmea") || instr.startsWith("stmia")) {
                startAddr += 4;
                incrementalValue = 4;
            }
            if (instr.startsWith("stmfd") || instr.startsWith("stmdb")) {
                incrementalValue = -4;
            }
            if (instr.startsWith("stmed") || instr.startsWith("stmda")) {
                startAddr -= 4;
                incrementalValue = -4;
            }
            
            for (int i=0 ; i<regCount ; ++i) {
                output.addresses.add(startAddr);
                startAddr += incrementalValue;
            }
        }
        
        return output;
    }
    
    /**
     * Calcule l'adresse (de départ) concernée par un accès mémoire.
     * @param instr l'instruction d'accès mémoire.
     * @param registers l'état des registres avant execution de l'instruction.
     * @return l'adresse de départ.
     */
    private int processingAddress(String instr, String registers, int cpsr) {
        String[] split;
        String tmp;
        int addrRegister;
        int addrRegister2;
        
        if (instr.startsWith("ldm") || instr.startsWith("stm")) {
            split = instr.split(",");
            split = split[0].split(" ");
            split = split[1].split("!");
            addrRegister = this.registerNameToNumber(split[0]);
            return regNumberToValue(registers, addrRegister);
        }
        
        if (instr.startsWith("str") || instr.startsWith("ldr")) {
            int addrStart = instr.indexOf('[')+1;
            int addrEnd   = instr.indexOf(']');
            tmp = instr.substring(addrStart, addrEnd);
            split = tmp.split(",");
            
            /* un seul registre */
            if (split.length == 1) {
                addrRegister = this.registerNameToNumber(split[0]);
                return this.regNumberToValue(registers, addrRegister);
            }
            
            /* deux registre (base + index) */
            if (split.length == 2 && !split[1].contains("#")) {
                addrRegister  = this.registerNameToNumber(split[0]);
                addrRegister2 = this.registerNameToNumber(split[1]);
                return  this.regNumberToValue(registers, addrRegister) +
                        this.regNumberToValue(registers, addrRegister2);
            }
            
            /* registre base + index imédiat  */
            if (split.length == 2 && split[1].contains("#")) {
                tmp = split[1].substring(split[1].indexOf('#')+1);
                addrRegister = this.registerNameToNumber(split[0]);
                return  Integer.parseInt(tmp) +
                        this.regNumberToValue(registers, addrRegister);
            }
            
            /* Registre base + registre index et shift */
            if(split.length == 3) {
                addrRegister  = this.registerNameToNumber(split[0]);
                addrRegister2 = this.registerNameToNumber(split[1]);
                addrRegister2 = this.regNumberToValue(registers, addrRegister2);
                int shiftValue = 0;
                tmp = null;

                int n = split[2].indexOf('#')+1;
                if (n>0) tmp = split[2].substring(n);
                if (tmp != null) shiftValue = Integer.parseInt(tmp);
                
                if (split[2].contains("lsl"))
                    addrRegister2 = addrRegister2 << shiftValue;
                
                if (split[2].contains("lsr"))
                    addrRegister2 = addrRegister2 >>> shiftValue;
                
                if (split[2].contains("asr"))
                    addrRegister2 = addrRegister2 >> shiftValue;
                
                if (split[2].contains("ror")) {
                    n = addrRegister2 << (32 - shiftValue);
                    addrRegister2 = addrRegister2 >>> shiftValue;
                    
                    addrRegister2 |= n;
                }
                
                if (split[2].contains("rrx")){
                    addrRegister2 = addrRegister2 >>> 1;
                    final int mask = 0x20000000;
                    cpsr &= mask;
                    cpsr = cpsr << 2;
                    addrRegister2 |= cpsr;
                }
                
                return  this.regNumberToValue(registers, addrRegister) + 
                        addrRegister2;
            }
        }
        
        return -1;
    }
    
    /**
     * Prend les registres sous la forme décrite par le protocole de communication
     * entre le simulateur C et cachesSim et récupère la valeur de l'un d'eux.
     * @param registers les registres
     * @param number le numero du registre à récupérer
     * @return la valeur du registre recherché
     */
    private int regNumberToValue(String registers, int number) {
        if (number > 15 || number < 0) return -1;

        String[] split = registers.split(",");
        String value = split[number];
        
        if (number == 0) value = value.substring(1);
        if (number == 15) value = value.substring(0, value.length()-2);
        
        return Integer.parseInt(value);
    }
    
    /**
     * Convertis le nom d'un registre en so numéro.
     * @param reg le nom du registre.
     * @return le numero du registre.
     */
    private int registerNameToNumber (String reg) {
        reg = reg.toLowerCase();
        reg = reg.replaceAll(" ", "");

        if (reg.equals("r0") || reg.equals("a1")) return 0;
        if (reg.equals("r1") || reg.equals("a2")) return 1;
        if (reg.equals("r2") || reg.equals("a3")) return 2;
        if (reg.equals("r3") || reg.equals("a4")) return 3;
        if (reg.equals("r4") || reg.equals("v1")) return 4;
        if (reg.equals("r5") || reg.equals("v2")) return 5;
        if (reg.equals("r6") || reg.equals("v3")) return 6;
        if (reg.equals("r7") || reg.equals("v4")) return 7;
        if (reg.equals("r8") || reg.equals("v5")) return 8;
        if (reg.equals("r9") || reg.equals("sb") || reg.equals("v6")) return 9;
        if (reg.equals("r10") || reg.equals("sl") || reg.equals("v7")) return 10;
        if (reg.equals("r11") || reg.equals("fp") || reg.equals("v8")) return 11;
        if (reg.equals("r12") || reg.equals("ip")) return 12;
        if (reg.equals("r13") || reg.equals("sp")) return 13;
        if (reg.equals("r14") || reg.equals("lr")) return 14;
        if (reg.equals("r15") || reg.equals("pc")) return 15;
        
        return -1;
    }
}

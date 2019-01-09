package com.jacobhicks;

import com.sun.org.apache.bcel.internal.generic.NOP;

import java.lang.reflect.Array;
import java.util.Arrays;

enum ADDRESSINGMODE {
    IP,
    IM,
    ZP,
    AS,
    A,
    AX,
    AY,
    ZX,
    ZY,
    IN,
    XI,
    YI,
    IX,
    IY,
    RA
}

public class CPU {
    MEMORY memory;
    int accumulator = 0;
    int X = 0, Y = 0;
    int PC = 0;
    int tempaddr;
    boolean[] status_register = new boolean[8];
    byte P;

    CPU(MEMORY mem){
        memory = mem;
        status_register[2] = true;
        status_register[5] = true;
    }
    public void start_cpu(int pos) {
        PC = pos;
        new Thread(() -> {
            while(PC < 0xFFFF) {
                int high = (int)memory.getByte(PC);
                P = statusByte(status_register);
                //DEBUGCPU.pulse(PC, high, accumulator, X, Y, P, memory.getStack_pointer());
                //System.out.printf("%04X |%04X|: %02X ", PC, memory.getStack_pointer(), high);
                PC++;
                if(PC == 0xD444) {
                    new NOP();
                    new NOP();
                }
                //if (accumulator == -256) accumulator = ~accumulator;
                switch (high) {
                    case (0x00):
                        __BRK();
                        break;
                    case (0x01):
                        __ORA(ADDRESSINGMODE.XI);
                        break;
                    case (0x04):
                        __NOP();
                        __NOP();
                        PC++;
                        break;
                    case (0x05):
                        __ORA(ADDRESSINGMODE.ZP);
                        break;
                    case (0x06):
                        __ASL(ADDRESSINGMODE.ZP);
                        break;
                    case (0x08):
                        __PHP();
                        break;
                    case (0x09):
                        __ORA(ADDRESSINGMODE.IM);
                        break;
                    case (0x0A):
                        __ASL(ADDRESSINGMODE.A);
                        break;
                    case (0x0C):
                        __NOP();
                        __NOP();
                        __NOP();
                        PC+=2;
                        break;
                    case (0x0D):
                        __ORA(ADDRESSINGMODE.AS);
                        break;
                    case (0x0E):
                        __ASL(ADDRESSINGMODE.AS);
                        break;
                    case (0x10):
                        __BPL();
                        break;
                    case (0x11):
                        __ORA(ADDRESSINGMODE.IY);
                        break;
                    case (0x14):
                        __NOP();
                        __NOP();
                        PC++;
                        break;
                    case (0x15):
                        __ORA(ADDRESSINGMODE.ZX);
                        break;
                    case (0x16):
                        __ASL(ADDRESSINGMODE.ZX);
                        break;
                    case (0x18):
                        __CLC();
                        break;
                    case (0x19):
                        __ORA(ADDRESSINGMODE.AY);
                        break;
                    case (0x1A):
                        __NOP();
                        break;
                    case (0x1C):
                        __NOP();
                        __NOP();
                        __NOP();
                        PC+=2;
                        break;
                    case (0x1D):
                        __ORA(ADDRESSINGMODE.AX);
                        break;
                    case (0x1E):
                        __ASL(ADDRESSINGMODE.AX);
                        break;
                    case (0x20):
                        __JSR();
                        break;
                    case (0x21):
                        __AND(ADDRESSINGMODE.XI);
                        break;
                    case (0x24):
                        __BIT(ADDRESSINGMODE.ZP);
                        break;
                    case (0x25):
                        __AND(ADDRESSINGMODE.ZP);
                        break;
                    case (0x26):
                        __ROL(ADDRESSINGMODE.ZP);
                        break;
                    case (0x28):
                        __PLP();
                        break;
                    case (0x29):
                        __AND(ADDRESSINGMODE.IM);
                        break;
                    case (0x2A):
                        __ROL(ADDRESSINGMODE.A);
                        break;
                    case (0x2C):
                        __BIT(ADDRESSINGMODE.AS);
                        break;
                    case (0x2D):
                        __AND(ADDRESSINGMODE.AS);
                        break;
                    case (0x2E):
                        __ROL(ADDRESSINGMODE.AS);
                        break;
                    case (0x30):
                        __BMI();
                        break;
                    case (0x31):
                        __AND(ADDRESSINGMODE.IY);
                        break;
                    case (0x34):
                        __NOP();
                        __NOP();
                        PC++;
                        break;
                    case (0x35):
                        __AND(ADDRESSINGMODE.ZX);
                        break;
                    case (0x36):
                        __ROL(ADDRESSINGMODE.ZX);
                        break;
                    case (0x38):
                        __SEC();
                        break;
                    case (0x39):
                        __AND(ADDRESSINGMODE.AY);
                        break;
                    case (0x3A):
                        __NOP();
                        break;
                    case (0x3C):
                        __NOP();
                        __NOP();
                        __NOP();
                        PC+=2;
                        break;
                    case (0x3D):
                        __AND(ADDRESSINGMODE.AX);
                        break;
                    case (0x3E):
                        __ROL(ADDRESSINGMODE.AX);
                        break;
                    case (0x40):
                        __RTI();
                        break;
                    case (0x41):
                        __EOR(ADDRESSINGMODE.IX);
                        break;
                    case (0x44):
                        __NOP();
                        __NOP();
                        PC++;
                        break;
                    case (0x45):
                        __EOR(ADDRESSINGMODE.ZP);
                        break;
                    case (0x46):
                        __LSR(ADDRESSINGMODE.ZP);
                        break;
                    case (0x48):
                        __PHA();
                        break;
                    case (0x49):
                        __EOR(ADDRESSINGMODE.IM);
                        break;
                    case (0x4A):
                        __LSR(ADDRESSINGMODE.A);
                        break;
                    case (0x4C):
                        __JMP(ADDRESSINGMODE.AS);
                        break;
                    case (0x4D):
                        __EOR(ADDRESSINGMODE.AS);
                        break;
                    case (0x4E):
                        __LSR(ADDRESSINGMODE.AS);
                        break;
                    case (0x50):
                        __BVC();
                        break;
                    case (0x51):
                        __EOR(ADDRESSINGMODE.IY);
                        break;
                    case (0x54):
                        __NOP();
                        __NOP();
                        PC++;
                        break;
                    case (0x55):
                        __EOR(ADDRESSINGMODE.ZX);
                        break;
                    case (0x56):
                        __LSR(ADDRESSINGMODE.ZX);
                        break;
                    case (0x58):
                        __CLI();
                        break;
                    case (0x59):
                        __EOR(ADDRESSINGMODE.AY);
                        break;
                    case (0x5A):
                        __NOP();
                        break;
                    case (0x5C):
                        __NOP();
                        __NOP();
                        __NOP();
                        PC+=2;
                        break;
                    case (0x5D):
                        __EOR(ADDRESSINGMODE.AX);
                        break;
                    case (0x5E):
                        __LSR(ADDRESSINGMODE.AX);
                        break;
                    case (0x60):
                        __RTS();
                        break;
                    case (0x61):
                        __ADC(ADDRESSINGMODE.IX);
                        break;
                    case (0x64):
                        __NOP();
                        __NOP();
                        PC++;
                        break;
                    case (0x65):
                        __ADC(ADDRESSINGMODE.ZP);
                        break;
                    case (0x66):
                        __ROR(ADDRESSINGMODE.ZP);
                        break;
                    case (0x68):
                        __PLA();
                        break;
                    case (0x69):
                        __ADC(ADDRESSINGMODE.IM);
                        break;
                    case (0x6A):
                        __ROR(ADDRESSINGMODE.A);
                        break;
                    case (0x6C):
                        __JMP(ADDRESSINGMODE.IM);
                        break;
                    case (0x6D):
                        __ADC(ADDRESSINGMODE.AS);
                        break;
                    case (0x6E):
                        __ROR(ADDRESSINGMODE.AS);
                        break;
                    case (0x70):
                        __BVS();
                        break;
                    case (0x71):
                        __ADC(ADDRESSINGMODE.IY);
                        break;
                    case (0x74):
                        __NOP();
                        __NOP();
                        PC++;
                        break;
                    case (0x75):
                        __ADC(ADDRESSINGMODE.ZX);
                        break;
                    case (0x76):
                        __ROR(ADDRESSINGMODE.ZX);
                        break;
                    case (0x78):
                        __SEI();
                        break;
                    case (0x79):
                        __ADC(ADDRESSINGMODE.AY);
                        break;
                    case (0x7A):
                        __NOP();
                        break;
                    case (0x7C):
                        __NOP();
                        __NOP();
                        __NOP();
                        PC+=2;
                        break;
                    case (0x7D):
                        __ADC(ADDRESSINGMODE.AX);
                        break;
                    case (0x7E):
                        __ROR(ADDRESSINGMODE.AX);
                        break;
                    case (0x80):
                        __NOP();
                        __NOP();
                        PC++;
                        break;
                    case (0x81):
                        __STA(ADDRESSINGMODE.IX);
                        break;
                    case (0x82):
                        __NOP();
                        __NOP();
                        PC++;
                        break;
                    case (0x84):
                        __STY(ADDRESSINGMODE.ZP);
                        break;
                    case (0x85):
                        __STA(ADDRESSINGMODE.ZP);
                        break;
                    case (0x86):
                        __STX(ADDRESSINGMODE.ZP);
                        break;
                    case (0x88):
                        __DEY();
                        break;
                    case (0x89):
                        __NOP();
                        __NOP();
                        PC++;
                        break;
                    case (0x8A):
                        __TXA();
                        break;
                    case (0x8C):
                        __STY(ADDRESSINGMODE.AS);
                        break;
                    case (0x8D):
                        __STA(ADDRESSINGMODE.AS);
                        break;
                    case (0x8E):
                        __STX(ADDRESSINGMODE.AS);
                        break;
                    case (0x90):
                        __BCC();
                        break;
                    case (0x91):
                        __STA(ADDRESSINGMODE.IY);
                        break;
                    case (0x94):
                        __STY(ADDRESSINGMODE.ZX);
                        break;
                    case (0x95):
                        __STA(ADDRESSINGMODE.ZX);
                        break;
                    case (0x96):
                        __STX(ADDRESSINGMODE.ZY);
                        break;
                    case (0x98):
                        __TYA();
                        break;
                    case (0x99):
                        __STA(ADDRESSINGMODE.AY);
                        break;
                    case (0x9A):
                        __TXS();
                        break;
                    case (0x9D):
                        __STA(ADDRESSINGMODE.AX);
                        break;
                    case (0xA0):
                        __LDY(ADDRESSINGMODE.IM);
                        break;
                    case (0xA1):
                        __LDA(ADDRESSINGMODE.IX);
                        break;
                    case (0xA2):
                        __LDX(ADDRESSINGMODE.IM);
                        break;
                    case(0xA3):
                        __LAX(ADDRESSINGMODE.IX);
                        break;
                    case (0xA4):
                        __LDY(ADDRESSINGMODE.ZP);
                        break;
                    case (0xA5):
                        __LDA(ADDRESSINGMODE.ZP);
                        break;
                    case (0xA6):
                        __LDX(ADDRESSINGMODE.ZP);
                        break;
                    case(0xA7):
                        __LAX(ADDRESSINGMODE.ZP);
                        break;
                    case (0xA8):
                        __TAY();
                        break;
                    case (0xA9):
                        __LDA(ADDRESSINGMODE.IM);
                        break;
                    case (0xAA):
                        __TAX();
                        break;
                    case (0xAC):
                        __LDY(ADDRESSINGMODE.AS);
                        break;
                    case (0xAD):
                        __LDA(ADDRESSINGMODE.AS);
                        break;
                    case (0xAE):
                        __LDX(ADDRESSINGMODE.AS);
                        break;
                    case(0xAF):
                        __LAX(ADDRESSINGMODE.AS);
                        break;
                    case (0xB0):
                        __BCS();
                        break;
                    case (0xB1):
                        __LDA(ADDRESSINGMODE.IY);
                        break;
                    case (0xB3):
                        __LAX(ADDRESSINGMODE.IY);
                        break;
                    case (0xB4):
                        __LDY(ADDRESSINGMODE.ZX);
                        break;
                    case (0xB5):
                        __LDA(ADDRESSINGMODE.ZX);
                        break;
                    case (0xB6):
                        __LDX(ADDRESSINGMODE.ZY);
                        break;
                    case (0xB7):
                        __LAX(ADDRESSINGMODE.ZY);
                        break;
                    case (0xB8):
                        __CLV();
                        break;
                    case (0xB9):
                        __LDA(ADDRESSINGMODE.AY);
                        break;
                    case (0xBA):
                        __TSX();
                        break;
                    case (0xBC):
                        __LDY(ADDRESSINGMODE.AX);
                        break;
                    case (0xBD):
                        __LDA(ADDRESSINGMODE.AX);
                        break;
                    case (0xBE):
                        __LDX(ADDRESSINGMODE.AY);
                        break;
                    case(0xBF):
                        __LAX(ADDRESSINGMODE.AY);
                        break;
                    case (0xC0):
                        __CPY(ADDRESSINGMODE.IM);
                        break;
                    case (0xC1):
                        __CMP(ADDRESSINGMODE.IX);
                        break;
                    case (0xC2):
                        __NOP();
                        __NOP();
                        PC++;
                        break;
                    case (0xC4):
                        __CPY(ADDRESSINGMODE.ZP);
                        break;
                    case (0xC5):
                        __CMP(ADDRESSINGMODE.ZP);
                        break;
                    case (0xC6):
                        __DEC(ADDRESSINGMODE.ZP);
                        break;
                    case (0xC8):
                        __INY();
                        break;
                    case (0xC9):
                        __CMP(ADDRESSINGMODE.IM);
                        break;
                    case (0xCA):
                        __DEX();
                        break;
                    case (0xCC):
                        __CPY(ADDRESSINGMODE.AS);
                        break;
                    case (0xCD):
                        __CMP(ADDRESSINGMODE.AS);
                        break;
                    case (0xCE):
                        __DEC(ADDRESSINGMODE.AS);
                        break;
                    case (0xD0):
                        __BNE();
                        break;
                    case (0xD1):
                        __CMP(ADDRESSINGMODE.IY);
                        break;
                    case (0xD4):
                        __NOP();
                        __NOP();
                        PC++;
                        break;
                    case (0xD5):
                        __CMP(ADDRESSINGMODE.ZX);
                        break;
                    case (0xD6):
                        __DEC(ADDRESSINGMODE.ZX);
                        break;
                    case (0xD8):
                        status_register[3] = false;
                        break;
                    case (0xD9):
                        __CMP(ADDRESSINGMODE.AY);
                        break;
                    case (0xDA):
                        __NOP();
                        break;
                    case (0xDC):
                        __NOP();
                        __NOP();
                        __NOP();
                        PC+=2;
                        break;
                    case (0xDD):
                        __CMP(ADDRESSINGMODE.AX);
                        break;
                    case (0xDE):
                        __DEC(ADDRESSINGMODE.AX);
                        break;
                    case (0xE0):
                        __CPX(ADDRESSINGMODE.IM);
                        break;
                    case (0xE1):
                        __SBC(ADDRESSINGMODE.IX);
                        break;
                    case (0xE2):
                        __NOP();
                        __NOP();
                        PC++;
                        break;
                    case (0xE4):
                        __CPX(ADDRESSINGMODE.ZP);
                        break;
                    case (0xE5):
                        __SBC(ADDRESSINGMODE.ZP);
                        break;
                    case (0xE6):
                        __INC(ADDRESSINGMODE.ZP);
                        break;
                    case (0xE8):
                        __INX();
                        break;
                    case (0xE9):
                        __SBC(ADDRESSINGMODE.IM);
                        break;
                    case (0xEA):
                        __NOP();
                        break;
                    case (0xEC):
                        __CPX(ADDRESSINGMODE.AS);
                        break;
                    case (0xED):
                        __SBC(ADDRESSINGMODE.AS);
                        break;
                    case (0xEE):
                        __INC(ADDRESSINGMODE.AS);
                        break;
                    case (0xF0):
                        __BEQ();
                        break;
                    case (0xF1):
                        __SBC(ADDRESSINGMODE.IY);
                        break;
                    case (0xF4):
                        __NOP();
                        __NOP();
                        PC++;
                        break;
                    case (0xF5):
                        __SBC(ADDRESSINGMODE.ZX);
                        break;
                    case (0xF6):
                        __INC(ADDRESSINGMODE.ZX);
                        break;
                    case (0xF8):
                        status_register[3] = true;
                        break;
                    case (0xF9):
                        __SBC(ADDRESSINGMODE.AY);
                        break;
                    case (0xFA):
                        __NOP();
                        break;
                    case (0xFC):
                        __NOP();
                        __NOP();
                        __NOP();
                        PC+=2;
                        break;
                    case (0xFD):
                        __SBC(ADDRESSINGMODE.AX);
                        break;
                    case (0xFE):
                        __INC(ADDRESSINGMODE.AX);
                        break;
                    default:
                        System.out.print("Unsupported Opcode");
                        System.exit(1);
                }
                //System.out.println();//" " + Arrays.toString(status_register) + " " + statusByte());
            }
        }).start();
    }

    private void __BRK() {
        memory.push(PC);
        memory.push(status_register);
        status_register[2] = true;
        PC = memory.breakvector;
    }

    private void __ORA(ADDRESSINGMODE am) {
        accumulator |= getValue(am);
        update_flags();
    }

    private void __ASL(ADDRESSINGMODE am) {
        int LSB = getValue(am);
        int tmp = (LSB << 1);
        status_register[0] = (tmp > 0xFF);
        tmp &= 0xFF;
        if(am != ADDRESSINGMODE.A) {
            memory.setByte(tempaddr, tmp);
        }
        else {
            accumulator = tmp;
        }
        status_register[1] = (tmp == 0);
        status_register[7] = (tmp >= 0x80);
        //update_flags();
    }

    private void __PHP() {
        if(PC == 0xC709) {
            new NOP();
        }
        status_register[4] = true;
        status_register[5] = true;
        memory.push(status_register.clone());
        //System.out.printf("%n%02X @ $%02X%n", statusByte((boolean[])memory.peak()), memory.getStack_pointer());
        status_register[4] = false;
    }

    private void __BPL() {
        int Val = getRA();
        PC++;
        if(!status_register[7]) {
            PC = Val;
        }
    }

    private void __CLC() {
        status_register[0] = false;
    }

    private void __JSR() {
        int LSB = (int)memory.getByte(PC++);
        int MSB = (int)memory.getByte(PC);
        int operand = (int)(((MSB << 8) | (LSB)));
        memory.push((PC & 0xFF00)>>8);
        memory.push(PC & 0x00FF);
        PC = operand;
    }

    private void __AND(ADDRESSINGMODE am) {
        //if(accumulator > Byte.MAX_VALUE || accumulator < Byte.MIN_VALUE) accumulator = ~accumulator;
        accumulator &= getValue(am);
        //if(accumulator > Byte.MAX_VALUE) accumulator = ~accumulator - 0x10;
        update_flags();
    }

    private void __BIT(ADDRESSINGMODE am) {
        int LSB = getValue(am);
        //System.out.print(" " + LSB);
        int tmp = accumulator;
        if(accumulator < 0) accumulator = ~accumulator;
        if(LSB < 0) LSB = ~LSB;
        status_register[1] = ((LSB & accumulator) == 0);
        status_register[7] = ((LSB >> 7) == 1);
        status_register[6] = (((LSB >> 6) & 1) == 1);
        accumulator = tmp;
    }

    private void __ROL(ADDRESSINGMODE am) {
        int Val = getValue(am);
        boolean tmp = status_register[0];
        status_register[0] = ((Val >> 7) & 1) == 1;
        Val <<= 1;
        Val += tmp ? 1 : 0;
        Val &= 0xFF;
        if(am != ADDRESSINGMODE.A) {
            memory.setByte(tempaddr, Val);
        }
        else {
            accumulator = Val;
        }
        update_flags();
    }

    private void __PLP() {
        try {
            status_register = (boolean[]) memory.peak();
        } catch (Exception e) {
            int byt = (int) memory.peak();
            if (byt == -256) byt = ~byt;
            for (byte i = 0; i < 8; i++) {
                status_register[i] = ((byt >> i) & 1) == 1;
            }
        }
        status_register[4] = false;
        status_register[5] = true;
        memory.setStack_pointer(memory.getStack_pointer() + 1);
    }

    private void __BMI() {
        int Val = getRA();
        PC++;
        if(status_register[7]) {
            PC = Val;
        }
    }

    private void __SEC() {
        status_register[0] = true;
    }

    private void __RTI() {
        try {
            status_register = (boolean[]) memory.peak();
        } catch (ClassCastException e) {
            int byt = (int) memory.peak();
            if (byt == -256) byt = ~byt;
            for (byte i = 0; i < 8; i++) {
                status_register[i] = ((byt >> i) & 1) == 1;
            }
        }
        status_register[5] = true;
        memory.setStack_pointer(memory.getStack_pointer() + 1);
        PC = (((int) memory.pop()) |  ((int) memory.pop() << 8));
    }

    private void __EOR(ADDRESSINGMODE am) {
        accumulator ^= getValue(am);
        update_flags();
    }

    private void __CLI() {
        status_register[2] = false;
    }

    private void __LSR(ADDRESSINGMODE am) {
        int Val = getValue(am);
        status_register[0] = (Val & 1) == 1;
        Val >>= 1;
        Val &= 0xFF;
        if(am != ADDRESSINGMODE.A) {
            memory.setByte(tempaddr, Val);
        }
        else {
            accumulator = Val;
        }
        status_register[1] = (Val == 0);
        status_register[7] = (Val >= 0x80);
        //update_flags();
    }

    private void __PHA() {
        memory.push(accumulator);
        //System.out.printf("%n%02X @ $%02X%n", statusByte(status_register), memory.getStack_pointer());
    }

    private void __JMP(ADDRESSINGMODE am) {
        if(am.equals(ADDRESSINGMODE.AS)) {
            PC = getIM();
        }
        else {
            PC = getIN();
        }
    }

    private void __BVC() {
        int Val = getValue(ADDRESSINGMODE.RA);
        if(!status_register[6]) PC = Val - 1;
        PC++;
    }

    private void __RTS() {
        PC = ((int) memory.pop() | ((int) memory.pop() << 8)) + 1;
    }

    private void __ADC(ADDRESSINGMODE am) {
        int Val = getValue(am);
        int tmp = accumulator;
        accumulator += (Val & 0xFF) + (status_register[0] ? 1 : 0);
        status_register[0] = accumulator > 0xFF;
        update_flags();
        status_register[6] = (!(((tmp ^ Val) & 0x80) > 0) && ((tmp ^ accumulator) & 0x80) > 0);
        accumulator &= 0xFF;
    }

    private void __ROR(ADDRESSINGMODE am) {
        int Val = getValue(am);
        boolean tmp = status_register[0];
        status_register[0] = ((Val) & 1) == 1;
        Val >>= 1;
        Val += tmp ? (1<<7) : 0;
        Val &= 0xFF;
        if(am != ADDRESSINGMODE.A) {
            memory.setByte(tempaddr, Val);
        }
        else {
            accumulator = Val;
        }
        status_register[1] = (Val == 0);
        status_register[7] = Val >= 0x80;
        //update_flags();
    }

    private void __PLA() {
        if(PC == 0xC71C) {
            //System.out.println();
        }
        try {
            accumulator = (int) memory.peak();
        }
        catch (ClassCastException e) {
            accumulator = statusByte((boolean[]) memory.peak()) | 0b00110000;
        }
        //System.out.printf("-: %02X @ $%02X%n", accumulator, memory.getStack_pointer());
        memory.setStack_pointer(memory.getStack_pointer() + 1);
        update_flags();
    }

    private void __BVS() {
        int Val = getValue(ADDRESSINGMODE.RA);
        if(status_register[6]) PC = Val - 1;
        PC++;
    }

    private void __SEI() {
        status_register[2] = true;
    }

    private void __STA(ADDRESSINGMODE am) {
        getValue(am);
        memory.setByte(tempaddr, accumulator);
    }

    private void __STX(ADDRESSINGMODE am) {
        getValue(am);
        memory.setByte(tempaddr, X);
    }

    private void __STY(ADDRESSINGMODE am) {
        getValue(am);
        memory.setByte(tempaddr, Y);
    }

    private void __DEY() {
        Y--;
        update_flags_Y();
    }

    private void __TXA() {
        accumulator = X;
        update_flags();
    }

    private void __BCC() {
        int Val = getValue(ADDRESSINGMODE.RA);
        if(!status_register[0]) PC = Val - 1;
        PC++;
    }

    private void __TXS() {
        memory.setStack_pointer(X);
        update_flags_S();
    }

    private void __LDY(ADDRESSINGMODE am) {
        int Val = getValue(am);
        if(Val != 0xFFFF) {
            Y = Val & 0xFF;
        }
        update_flags_Y();
    }

    private void __LDA(ADDRESSINGMODE am) {
        int Val = getValue(am);
        //if((Val & 0xFFFF) != 0xFFFF) {
            accumulator = Val & 0xFF;
        //}
        update_flags();
    }
    private void __LDX(ADDRESSINGMODE am) {
        int Val = getValue(am);
        if(Val != 0xFFFF) {
            X = Val & 0xFF;
        }
        update_flags_X();
    }

    private void __TYA() {
        accumulator = Y;
        update_flags();
    }

    private void __TAY() {
        Y = accumulator;
        update_flags_Y();
    }

    private void __TAX() {
        X = accumulator;
        update_flags_X();
    }

    private void __BCS() {
        int Val = getValue(ADDRESSINGMODE.RA);
        if(status_register[0]) PC = Val - 1;
        PC++;
    }

    private void __CLV() {
        status_register[6] = false;
    }

    private void __TSX() {
        X = memory.getStack_pointer();
        update_flags_X();
    }

    private void __CPY(ADDRESSINGMODE am) {
        int Val = getValue(am);
        Val &= 0xFF;
        int tmp = Y;
        //System.out.print(" " + Y + " - " + Val + " = " + (Y - Val) + " ");
        Y -= Val;
        status_register[0] = (Y >= 0);
        status_register[7] = (Y >= 0x80 || Y < 0);
        status_register[1] = (Y == 0);
        Y = tmp;
    }

    private void __CMP(ADDRESSINGMODE am) {
        int Val = getValue(am);
        Val &= 0xFF;
        int tmp = accumulator;
        //System.out.print(" " + accumulator + " - " + Val + " = " + (accumulator - Val) + " ");
        accumulator -= Val;
        status_register[0] = (accumulator >= 0);
        status_register[7] = (accumulator >= 0x80 || accumulator < 0);
        status_register[1] = (accumulator == 0);
        accumulator = tmp;
    }

    private void __DEC(ADDRESSINGMODE am) {
        int Val = getValue(am) - 1;
        Val &= 0xFF;
        status_register[1] = Val == 0;
        status_register[7] = Val >= 0x80;
        memory.setByte(tempaddr, Val);
    }

    private void __INY() {
        Y++;
        update_flags_Y();
    }

    private void __DEX() {
        X--;
        update_flags_X();
    }

    private void __BNE() {
        int Val = getValue(ADDRESSINGMODE.RA);
        if(!status_register[1]) PC = Val - 1;
        PC++;
    }

    private void __CPX(ADDRESSINGMODE am) {
        int Val = getValue(am);
        Val &= 0xFF;
        int tmp = X;
        //System.out.print(" " + X + " - " + Val + " = " + (X - Val) + " ");
        X -= Val;
        status_register[0] = (X >= 0);
        status_register[7] = (X >= 0x80 || X < 0);
        status_register[1] = (X == 0);
        X = tmp;
    }

    private void __SBC(ADDRESSINGMODE am) {
        int Val = getValue(am);
        int tmp = accumulator;
        accumulator -= (Val & 0xFF) + (status_register[0] ? 0 : 1);
        status_register[0] = !(accumulator < 0 || accumulator > 0xFF);
        if(accumulator < 0) accumulator = 0xFF - Math.abs(accumulator) + 1;
        update_flags();
        status_register[6] = ((((tmp ^ Val) & 0x80) != 0) && ((tmp ^ accumulator) & 0x80) != 0);
        accumulator &= 0xFF;
    }

    private void __INC(ADDRESSINGMODE am) {
        int Val = getValue(am) + 1;
        Val &= 0xFF;
        status_register[1] = Val == 0;
        status_register[7] = Val >= 0x80;
        memory.setByte(tempaddr, Val);
    }

    private void __INX() {
        X++;
        update_flags_X();
    }

    private void __NOP() {}

    private void __BEQ() {
        int Val = getValue(ADDRESSINGMODE.RA);
        if(status_register[1]) PC = Val - 1;
        PC++;
    }

    private void __LAX(ADDRESSINGMODE am) {
        __LDA(am);
        X = accumulator;
    }

    private int getIX() {
        int LSB = (int) memory.getByte(PC++);
        //int MSB = (int) memory.getByte(PC);
        int addr = (LSB + X) & 0xFF;// | (MSB<<8)));
        //System.out.printf("%02X", LSB);//, MSB);
        ////System.out.printf("%n%02X %02X %04X", (int) memory.getByte(addr), ((int) memory.getByte((addr + 1) & 0xFF)), ((int) memory.getByte(addr) | ((int) memory.getByte((addr + 1) & 0xFF) << 8)));
        tempaddr = addr = ((int) memory.getByte(addr) | ((int) memory.getByte((addr + 1) & 0xFF) << 8));
        return (int) memory.getByte(addr);
    }

    private int getXI() {
        int LSB = (int) memory.getByte(PC++);
        //int MSB = (int) memory.getByte(PC);
        int addr = (LSB + X) & 0xFF;// | (MSB<<8)));
        //System.out.printf("%02X", LSB);//, MSB);
        //System.out.printf("%n%02X %02X %04X", (int) memory.getByte(addr), ((int) memory.getByte((addr + 1) & 0xFF)), ((int) memory.getByte(addr) | ((int) memory.getByte((addr + 1) & 0xFF) << 8)));
        tempaddr = addr = ((int) memory.getByte(addr) | ((int) memory.getByte((addr + 1) & 0xFF) << 8)) + X;
        return (int) memory.getByte(addr);
    }

    private int getIY() {
        int LSB = (int) memory.getByte(PC++);
        //int MSB = (int) memory.getByte(PC);
        int addr = (LSB) & 0xFF;// | (MSB<<8)));
        //System.out.printf("%02X", LSB);//, MSB);
        //System.out.printf("%n%02X %02X %02X %04X", addr, (int) memory.getByte(addr), ((int) memory.getByte((addr + 1) & 0xFF)), (((int) memory.getByte(addr) | ((int) memory.getByte((addr + 1) & 0xFF) << 8)) + Y) & 0xFFFF);
        tempaddr = addr = ((((int) memory.getByte(addr) | ((int) memory.getByte((addr + 1) & 0xFF) << 8)) + Y) & 0xFFFF);
        return (int) memory.getByte(addr);
    }

    private int getYI() {
        int LSB = (int) memory.getByte(PC++);
        //int MSB = (int) memory.getByte(PC);
        int addr = (LSB + Y) & 0xFF;// | (MSB<<8)));
        //System.out.printf("%02X", LSB);//, MSB);
        //System.out.printf("%n%02X %02X %04X", (int) memory.getByte(addr), ((int) memory.getByte((addr + 1) & 0xFF)), ((int) memory.getByte(addr) | ((int) memory.getByte((addr + 1) & 0xFF) << 8)));
        tempaddr = addr = ((int) memory.getByte(addr) | ((int) memory.getByte((addr + 1) & 0xFF) << 8)) + X;
        return (int) memory.getByte(addr);
    }

    private  int getZP() {
        int LSB = (int) memory.getByte(PC++);
        //System.out.printf("%02X", LSB);
        tempaddr = LSB;
        return (int) memory.getByte(tempaddr);
    }

    private int getZX() {
        int LSB = (int) (memory.getByte(PC++));
        //System.out.printf("%02X", LSB);
        tempaddr = (int) ((LSB + X) & 0xFF);
        return (int) memory.getByte(tempaddr);
    }

    private int getZY() {
        int LSB = (int) (memory.getByte(PC++));
        //System.out.printf("%02X", LSB);
        tempaddr = (int) ((LSB + Y) & 0xFF);
        return (int) memory.getByte(tempaddr);
    }

    private int getAX() {
        int LSB = (int) memory.getByte(PC++);
        int MSB = (int) memory.getByte(PC++);
        //System.out.printf("%02X %02X", LSB, MSB);
        int operand = (int)((MSB << 8) | (LSB));
        tempaddr = (int) (operand + X);
        return (int) memory.getByte(operand + X);
    }

    private int getAY() {
        int LSB = (int) memory.getByte(PC++);
        int MSB = (int) memory.getByte(PC++);
        //System.out.printf("%02X %02X", LSB, MSB);
        int operand = (int)((MSB << 8) | (LSB));
        tempaddr = (int) ((operand + Y) & 0xFFFF);
        return (int) memory.getByte(tempaddr);
    }

    private int getIM() {
        int LSB = (int) memory.getByte(PC++);
        int MSB = (int) memory.getByte(PC);
        int addr = ((MSB << 8) | LSB);
        //System.out.printf("%02X %02X", LSB, MSB);
        //System.out.printf("%n%02X %02X %04X", (int) memory.getByte(addr), ((int) memory.getByte((addr + 1) & 0xFF)), ((int) memory.getByte(addr) | ((int) memory.getByte((addr + 1) & 0xFF) << 8)));
        return addr;
    }

    private int getAS() {
        int LSB = (int) memory.getByte(PC++);
        int MSB = (int) memory.getByte(PC++);
        //System.out.printf("%02X %02X", LSB, MSB);
        tempaddr = (int)((MSB << 8) | LSB);
        return (int) memory.getByte(tempaddr);
    }

    private int getA() {
        return accumulator;
    }

    private int getIN() {
        int LSB = (int) memory.getByte(PC++);
        int MSB = (int) memory.getByte(PC++);
        //System.out.printf("%02X %02X", LSB, MSB);
        tempaddr = (int)((MSB << 8) | LSB);
        //System.out.printf("%n%02X %02X %04X", (int) memory.getByte(tempaddr), ((int) memory.getByte((tempaddr + 1) & 0xFF)), ((int) memory.getByte(tempaddr) | ((int) memory.getByte((tempaddr + 1) & 0xFF) << 8)));
        return (int) memory.getByte(tempaddr) | ((int) memory.getByte((tempaddr & 0xFF00) | ((tempaddr+1) & 0xFF)) << 8);
    }

    private int getRA() {
        int LSB = (int) memory.getByte(PC);
        //System.out.printf("%02X", LSB);
        if(LSB >= 0x80){
            return (PC + (LSB - 0xFF));
        }
        return (PC + LSB) + 1;
    }

    private int getValue(ADDRESSINGMODE am) {
        if (am.equals(ADDRESSINGMODE.IM)) return getIM();
        else if (am.equals(ADDRESSINGMODE.ZP)) return getZP();
        else if (am.equals(ADDRESSINGMODE.AS)) return getAS();
        else if (am.equals(ADDRESSINGMODE.A)) return getA();
        else if (am.equals(ADDRESSINGMODE.AX)) return getAX();
        else if (am.equals(ADDRESSINGMODE.AY)) return getAY();
        else if (am.equals(ADDRESSINGMODE.ZX)) return getZX();
        else if (am.equals(ADDRESSINGMODE.ZY)) return getZY();
        else if (am.equals(ADDRESSINGMODE.IN)) return getIN();
        else if (am.equals(ADDRESSINGMODE.XI)) return getXI();
        else if (am.equals(ADDRESSINGMODE.YI)) return getYI();
        else if (am.equals(ADDRESSINGMODE.IX)) return getIX();
        else if (am.equals(ADDRESSINGMODE.IY)) return getIY();
        else if (am.equals(ADDRESSINGMODE.RA)) return getRA();
        return 0;
    }

    private void update_flags() {
        accumulator &= 0xFF;
        status_register[7] = (accumulator >= 0x80);
        status_register[1] = accumulator == 0;
    }

    private void update_flags_Y() {
        Y &= 0xFF;
        status_register[7] = (Y > Byte.MAX_VALUE);
        status_register[1] = Y == 0;
    }

    private void update_flags_X() {
        X &= 0xFF;
        status_register[7] = (X > Byte.MAX_VALUE);
        status_register[1] = X == 0;
    }

    private void update_flags_S() {
        memory.setStack_pointer(memory.getStack_pointer() & 0xFF);
        if(memory.getStack_pointer() > Byte.MAX_VALUE) {
            status_register[7] = true;
        }
        status_register[1] = memory.getStack_pointer() == 0;
    }

    private static byte statusByte(boolean[] status_register) {
        byte res = 0;
        for(int i = 0; i < status_register.length; i++) {
            res += status_register[i] ? (1 << i) : 0;
        }
        return res;
    }
}
package com.jacobhicks;

import java.util.Stack;

public class CPU {
    MEMORY memory;
    char accumulator = 0;
    char X = 0, Y = 0;
    char reset_vector = 0;
    char break_vector = 0;
    char PC = 0;
    char stack_pointer = 0;
    boolean[] status_register = new boolean[8];
    Object[] stack = new Object[0xFF];

    CPU(MEMORY mem){
        memory = mem;
        reset_vector = (char)(memory.getByte(0xFFFD * 256) + memory.getByte(0xFFFC));
        break_vector = (char)(memory.getByte(0xFFFF * 256) + memory.getByte(0xFFFE));
        PC = reset_vector;
    }
    public void start_cpu() {
        new Thread(() -> {
            while(true) {
                char high = memory.getByte(PC++);
                switch (high) {
                    case (0x00):
                        __BRK();
                        break;
                    case (0x01):
                        __ORA_XI();
                        break;
                    case (0x05):
                        __ORA_ZP();
                        break;
                    case (0x06):
                        __ASL_ZP();
                        break;
                    case (0x08):
                        __PHP();
                        break;
                    case (0x09):
                        __ORA_IM();
                        break;
                    case (0x0A):
                        __ASL_A();
                        break;
                    case (0x0D):
                        __ORA_AS();
                        break;
                    case (0x0E):
                        __ASL_AS();
                        break;
                    case (0x10):
                        __BPL();
                        break;
                    case (0x11):
                        __ORA_IY();
                        break;
                    case (0x15):
                        __ORA_ZX();
                        break;
                    case (0x16):
                        __ASL_ZX();
                        break;
                    case (0x18):
                        __CLC();
                        break;
                    case (0x19):
                        __ORA_AY();
                        break;
                    case (0x1D):
                        __ORA_AX();
                        break;
                    case (0x1E):
                        __ASL_AX();
                        break;
                    case (0x20):
                        __JSR();
                        break;
                    case (0x21):
                        __AND_XI();
                        break;
                    case (0x24):
                        __BIT_ZP();
                        break;
                    case (0x25):
                        __AND_ZP();
                        break;
                    case (0x26):
                        __ROL_ZP();
                        break;
                    case (0x28):
                        __PLP();
                        break;
                    case (0x29):
                        __AND_IM();
                        break;
                    case (0x2A):
                        __ROL_A();
                        break;
                    case (0x2C):
                        __BIT_AS();
                        break;
                    case (0x2D):
                        __AND_AS();
                        break;
                    case (0x2E):
                        __ROL_AS();
                        break;
                    case (0x30):
                        __BMI();
                        break;
                    case (0x31):
                        __AND_IY();
                        break;
                    case (0x35):
                        __AND_ZX();
                        break;
                    case (0x36):
                        __ROL_ZX();
                        break;
                    case (0x38):
                        __SEC();
                        break;
                    case (0x39):
                        __AND_AY();
                        break;
                    case (0x3D):
                        __AND_AX();
                        break;
                    case (0x3E):
                        __ROL_AX();
                        break;
                    case (0x40):
                        __RTI();
                        break;
                    case (0x41):
                        __EOR_IX();
                        break;
                    case (0x45):
                        __EOR_ZP();
                        break;
                    case (0x46):
                        __LSR_ZP();
                        break;
                    case (0x48):
                        __PHA();
                        break;
                    case (0x49):
                        __EOR_IM();
                        break;
                    case (0x4A):
                        __LSR_A();
                        break;
                    case (0x4C):
                        __JMP_AS();
                        break;
                    case (0x4D):
                        __EOR_AS();
                        break;
                    case (0x4E):
                        __LSR_AS();
                        break;
                    case (0x50):
                        __BVC();
                        break;
                    case (0x51):
                        __EOR_IY();
                        break;
                    case (0x55):
                        __EOR_ZX();
                        break;
                }
            }
        }).start();
    }

    private void __BRK() {
        stack[stack_pointer++] = (char) (PC + 2);
        stack[stack_pointer++] = status_register;
        status_register[2] = true;
        PC = break_vector;
    }

    private void __ORA_XI() {
        char LSB = memory.getByte(PC++);
        char addr = (char) ((memory.getByte(LSB+1 + X)<<8) | memory.getByte(LSB + X));
        accumulator |= memory.getByte(addr);
        update_flags();
    }

    private void __ORA_ZP() {
        char LSB = memory.getByte(PC++);
        PC++;
        accumulator |= memory.getByte(LSB);
        update_flags();
    }

    private void __ASL_ZP() {
        char LSB = memory.getByte(PC++);
        PC++;
        char tmp = (char)(memory.getByte(LSB) << 1);
        if(tmp>0xFF) status_register[0x0] = true;
        tmp &= 0xFF;
        memory.setByte(LSB, tmp);
        update_flags();
    }

    private void __PHP() {
        stack[stack_pointer++] = status_register;
    }

    private void __ORA_IM() {
        char LSB = memory.getByte(PC++);
        char MSB = memory.getByte(PC++);
        char operand = (char)((MSB << 8) | (LSB));
        accumulator |= memory.getByte(operand);
        update_flags();
    }

    private void __ASL_A() {
        accumulator = (char)(accumulator << 1);
        if(accumulator>0xFF) status_register[0x0] = true;
        accumulator &= 0xFF;
        update_flags();
    }

    private void __ORA_AS() {
        char LSB = memory.getByte(PC++);
        char MSB = memory.getByte(PC++);
        char operand = (char)((MSB << 8) | (LSB));
        accumulator |= memory.getByte(operand);
        update_flags();
    }

    private void __ASL_AS() {
        char LSB = memory.getByte(PC++);
        char MSB = memory.getByte(PC++);
        char operand = (char)((MSB << 8) | (LSB));
        char tmp = (char)(memory.getByte(operand) << 1);
        if(tmp>0xFF) status_register[0x0] = true;
        tmp &= 0xFF;
        memory.setByte(operand, tmp);
        update_flags();
    }

    private void __BPL() {
        if(!status_register[0x7]) {
            PC += memory.getByte(PC++);
        }
    }

    private void __ORA_IY() {
        char LSB = memory.getByte(PC++);
        char addr = (char) ((memory.getByte(LSB+1)<<8) | memory.getByte(LSB));
        accumulator |= memory.getByte(addr + Y);
        update_flags();
    }

    private void __ORA_ZX() {
        char LSB = memory.getByte(PC++);
        PC++;
        accumulator |= memory.getByte(LSB + X);
        update_flags();
    }

    private void __ASL_ZX() {
        char LSB = memory.getByte(PC++);
        PC++;
        char tmp = (char)(memory.getByte(LSB + X) << 1);
        if(tmp>0xFF) status_register[0x0] = true;
        tmp &= 0xFF;
        memory.setByte(LSB + X, tmp);
        update_flags();
    }

    private void __CLC() {
        memory.setByte(0, (char) 0);
    }

    private void __ORA_AY() {
        char LSB = memory.getByte(PC++);
        char MSB = memory.getByte(PC++);
        char operand = (char)((MSB << 8) | (LSB));
        accumulator |= memory.getByte(operand + Y);
        update_flags();
    }

    private void __ORA_AX() {
        char LSB = memory.getByte(PC++);
        char MSB = memory.getByte(PC++);
        char operand = (char)((MSB << 8) | (LSB));
        accumulator |= memory.getByte(operand + X);
        update_flags();
    }

    private void __ASL_AX() {
        char LSB = memory.getByte(PC++);
        char MSB = memory.getByte(PC++);
        char operand = (char)(((MSB << 8) | (LSB)) + X);
        char tmp = (char)(memory.getByte(operand) << 1);
        if(tmp>0xFF) status_register[0x0] = true;
        tmp &= 0xFF;
        memory.setByte(operand, tmp);
        update_flags();
    }

    private void __JSR() {
        char LSB = memory.getByte(PC++);
        char MSB = memory.getByte(PC++);
        char operand = (char)(((MSB << 8) | (LSB)) + X);
        stack[stack_pointer++] = (PC & 0xFF00)>>8;
        stack[stack_pointer++] = PC & 0x0000;
        PC = operand;
    }

    private void __AND_XI() {
        char LSB = memory.getByte(PC++);
        char addr = (char) ((memory.getByte(LSB+1 + X)<<8) | memory.getByte(LSB + X));
        accumulator &= memory.getByte(addr);
        update_flags();
    }

    private void __BIT_ZP() {
        char LSB = memory.getByte(PC++);
        PC++;
        status_register[1] = ((memory.getByte(LSB) & accumulator) != 0);
        status_register[7] = ((memory.getByte(LSB) >> 7) & 1) == 1;
        status_register[6] = ((memory.getByte(LSB) >> 6) & 1) == 1;
        update_flags();
    }

    private void __AND_ZP() {
        char LSB = memory.getByte(PC++);
        accumulator &= memory.getByte(LSB);
        update_flags();
    }

    private void __ROL_ZP() {
        char LSB = memory.getByte(PC++);
        char Val = memory.getByte(LSB);
        boolean tmp = status_register[0];
        status_register[0] = ((Val >> 7) & 1) == 1;
        Val <<= 1;
        Val += tmp ? 1 : 0;
        Val &= 0xFF;
        memory.setByte(LSB, Val);
        update_flags();
    }

    private void __PLP() {
        char byt = (char)stack[--stack_pointer];
        for(byte i = 0; i < 8; i++){
            status_register[i] = ((byt >> i) & 1) == 1;
        }
    }

    private void __AND_IM() {
        char LSB = memory.getByte(PC++);
        accumulator &= LSB;
        update_flags();
    }

    private void __ROL_A() {
        boolean tmp = status_register[0];
        status_register[0] = ((accumulator >> 7) & 1) == 1;
        accumulator <<= 1;
        accumulator += tmp ? 1 : 0;
        accumulator &= 0xFF;
        update_flags();
    }

    private void __BIT_AS() {
        char LSB = memory.getByte(PC++);
        char MSB = memory.getByte(PC++);
        char operand = (char)((MSB << 8) | LSB);
        status_register[1] = ((memory.getByte(operand) & accumulator) != 0);
        status_register[7] = ((memory.getByte(operand) >> 7) & 1) == 1;
        status_register[6] = ((memory.getByte(operand) >> 6) & 1) == 1;
        update_flags();
    }

    private void __AND_AS() {
        char LSB = memory.getByte(PC++);
        char MSB = memory.getByte(PC++);
        char operand = (char)((MSB << 8) | LSB);
        accumulator &= memory.getByte(operand);
        update_flags();
    }

    private void __ROL_AS() {
        char LSB = memory.getByte(PC++);
        char MSB = memory.getByte(PC++);
        char operand = (char)((MSB << 8) | LSB);
        char Val = memory.getByte(operand);
        boolean tmp = status_register[0];
        status_register[0] = ((Val >> 7) & 1) == 1;
        Val <<= 1;
        Val += tmp ? 1 : 0;
        Val &= 0xFF;
        memory.setByte(operand, Val);
        update_flags();
    }

    private void __BMI() {
        char LSB = memory.getByte(PC++);
        char MSB = memory.getByte(PC++);
        char operand = (char)(((MSB << 8) | LSB));
        if(status_register[7]) PC = operand;
    }

    private void __AND_IY() {
        char LSB = memory.getByte(PC++);
        char addr = (char) ((memory.getByte(LSB+1)<<8) | memory.getByte(LSB));
        accumulator &= memory.getByte(addr + Y);
        update_flags();
    }

    private void __AND_ZX() {
        char LSB = memory.getByte(PC++);
        accumulator &= memory.getByte(LSB + X);
        update_flags();
    }

    private void __ROL_ZX() {
        char LSB = (char)(memory.getByte(PC++) + X);
        char Val = memory.getByte(LSB);
        boolean tmp = status_register[0];
        status_register[0] = ((Val >> 7) & 1) == 1;
        Val <<= 1;
        Val += tmp ? 1 : 0;
        Val &= 0xFF;
        memory.setByte(LSB, Val);
        update_flags();
    }

    private void __SEC() {
        status_register[0] = true;
    }

    private void __AND_AY() {
        char LSB = memory.getByte(PC++);
        char MSB = memory.getByte(PC++);
        char operand = (char)((MSB << 8) | LSB);
        accumulator &= memory.getByte(operand + Y);
        update_flags();
    }

    private void __AND_AX() {
        char LSB = memory.getByte(PC++);
        char MSB = memory.getByte(PC++);
        char operand = (char)((MSB << 8) | LSB);
        accumulator &= memory.getByte(operand + X);
        update_flags();
    }

    private void __ROL_AX() {
        char LSB = memory.getByte(PC++);
        char MSB = memory.getByte(PC++);
        char operand = (char) (((MSB << 8) | LSB) + X);
        char Val = memory.getByte(operand);
        boolean tmp = status_register[0];
        status_register[0] = ((Val >> 7) & 1) == 1;
        Val <<= 1;
        Val += tmp ? 1 : 0;
        Val &= 0xFF;
        memory.setByte(operand, Val);
        update_flags();
    }

    private void __RTI() {
        for(int i = 7; i != 0; i--) {
            status_register[i] = (boolean) stack[--stack_pointer];
        }
        PC = (char) (((char) stack[--stack_pointer]) |  ((char) stack[stack_pointer] << 8));
    }

    private void __EOR_IX() {

    }

    private void __EOR_ZP() {

    }

    private void __LSR_ZP() {

    }

    private void __PHA() {

    }

    private void __EOR_IM() {

    }

    private void __LSR_A() {

    }

    private void __JMP_AS() {

    }

    private void __EOR_AS() {

    }

    private void __LSR_AS() {

    }

    private void __BVC() {

    }

    private void __EOR_IY() {

    }

    private void __EOR_ZX() {

    }

    private void update_flags() {

    }
}

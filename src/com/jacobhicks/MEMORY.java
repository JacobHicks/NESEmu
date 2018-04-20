// 0x0000 - 0x07FF : Internal RAM
// 0x0800 - 0x0FFF : Mirror of Internal RAM
// 0x1000 - 0x17FF : Mirror of Internal RAM
// 0x1800 - 0x1FFF : Mirror of Internal RAM
// 0x2000 - 0x2007 : PPU Registers
// 0x2008 - 0x3FFF : Mirrors of PPU Registers
// 0x4000 - 0x4017 : APU and IO Registers
// 0x4018 - 0x401F : APU and IO Test Mode Functionality
// 0x4020 - 0xFFFF : Cartridge Space


package com.jacobhicks;

import java.util.Arrays;

public class MEMORY {
    private char[] mem = new char[0xFFFF];
    MEMORY() {
        Arrays.fill(mem, 0x0000, 0xFFFF, (char) 0);
    }

    char getByte(int i){
        return mem[i];
    }

    void setByte(int i, char x){
        mem[i] = x;
    }

    void flash_rom(char[] rom){
        for(int i = 0x4020 - 0x401F; i < 0xFFFF - 0x401F; i++){
            mem[i + 0x4020] = rom[i];
        }
    }
}

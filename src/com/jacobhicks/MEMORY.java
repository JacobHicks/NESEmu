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
    private int prgromspace;
    private int chrromspace;
    private boolean[] flags6 = new boolean[8];
    private boolean[] flags7 = new boolean[8];
    private boolean[] flags9 = new boolean[8];
    private boolean[] flags10 = new boolean[8];
    private int prgramspace;
    private int[] mem = new int[0x10000];
    private int mapper;
    int resetvector;
    int breakvector;
    private String publisher;
    private String board;

    //No trainer support, not worth it sorry
    MEMORY() {
        Arrays.fill(mem, 0x0000, mem.length, 0);
    }

    int getByte(int i){
        return mem[i];
    }

    void setByte(int i, int x){
        mem[i] = x;
    }

    int flash_rom(int[] rom){
        int[] header = new int[16];
        for(int i = 0; i < 16; i++) {
            header[i] = rom[i];
        }
        if(header[0] == 0x4E && header[1] == 0x45 && header[2] == 0x53 && header[3] == 0x1A) {
            prgromspace = header[4] * 16384;
            chrromspace = header[5] * 8192;
            for(int i = 0; i < 8; i++) {
                flags6[i] = ((header[6] >> i) & 1) == 1;
            }
            for(int i = 0; i < 8; i++) {
                flags7[i] = ((header[7] >> i) & 1) == 1;
            }
            prgramspace = (header[8] == 0) ? (8192) : (header[8] * 1024);
            for(int i = 0; i < 8; i++) {
                flags9[i] = ((header[9] >> i) & 1) == 1;
            }
            for(int i = 0; i < 8; i++) {
                flags10[i] = ((header[10] >> i) & 1) == 1;
            }
            for(int i = 0; i < prgromspace; i++) {
                mem[0x4020 + i] = rom[16 + i];
            }
            char lownybble = 0;
            char highnybble = 0;
            for(int i = 4; i < 8; i++) {
                if(flags6[i]) {
                    lownybble += 1 << (i-4);
                }
                if(flags7[i]) {
                    highnybble += 1 << (i-4);
                }
            }
            mapper = (highnybble << 8) | lownybble;
            if(mapper == 0) {
                publisher = "Nintendo";
                if(prgromspace == 16384) {
                    board = "NROM-128";
                }
                else if(prgromspace == 32768) {
                    board = "NROM-256";
                }
                for(int i = 0; i < mem.length - 0x8000; i++) {
                    mem[0x8000 + i] = rom[16 + (i & (prgromspace-1))];
                }
            }
            resetvector = mem[0xFFFC] | (mem[0xFFFD] << 8);
            breakvector = mem[0xFFFE] | (mem[0xFFFF] << 8);
            System.out.println(publisher + " " + board + " " + resetvector);
            //System.exit(0);
            return resetvector;
        }
        else {
            System.out.println("Flash failed, incorrect Header start");
            System.exit(1);
            return 0;
        }
    }
}

package com.jacobhicks;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class CARTRIDGE_READER {
    FileInputStream in;
    CARTRIDGE_READER(MEMORY m, String file) throws Exception {
        char[] tempmem = new char[0xFFFF - 0x4020];
        int tmp2 = 0;
        in = new FileInputStream(file);
        char opcode;
        do {
            opcode = (char)((in.read() << 8) | (in.read()));
            tempmem[tmp2++] = (char)((opcode & 0xFF00) >> 8);
            tempmem[tmp2++] = (char)(opcode & 0x00FF);
        } while(opcode != 0xFFFF);
        m.flash_rom(tempmem);
    }
}

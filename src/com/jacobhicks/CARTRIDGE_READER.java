package com.jacobhicks;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class CARTRIDGE_READER {
    FileInputStream in;
    CARTRIDGE_READER(MEMORY m, String file) throws Exception {
        in = new FileInputStream(file);
        int[] tempmem = new int[in.available()];
        for(int i = 0; i < tempmem.length; i++) {
            tempmem[i] = in.read();
        }
        m.flash_rom(tempmem);
    }
}

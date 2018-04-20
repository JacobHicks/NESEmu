package com.jacobhicks;

public class BOOT {
    public static void main(String[] args) throws Exception{
        MEMORY memory = new MEMORY();
        //CARTRIDGE_READER cartridge_reader = new CARTRIDGE_READER(memory, "nestest.nes");
        CPU cpu = new CPU(memory);
    }
}

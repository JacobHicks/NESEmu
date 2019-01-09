package com.jacobhicks;

public class BOOT {
    public static void main(String[] args) throws Exception{
        MEMORY memory = new MEMORY();
        CARTRIDGE_READER.flash(memory, "donkey kong.nes");
        CPU cpu = new CPU(memory);

        //cpu.start_cpu(0xC000);
        cpu.start_cpu(memory.resetvector);
    }
}

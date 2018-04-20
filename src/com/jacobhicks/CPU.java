package com.jacobhicks;

public class CPU {
    MEMORY memory;
    char accumulator = 0;
    char X = 0, Y = 0;
    char PC = 0;
    char stack_pointer = 0;
    boolean[] status_register = new boolean[8];

    CPU(MEMORY mem){
        memory = mem;
    }
    public void start_cpu() {
        new Thread(() -> {
            char high = memory.getByte(PC++);
            char low = memory.getByte(PC++);
            switch(high){
                case (0x00) : __BRK(); break;
                case (0x01) : __ORA_X(); break;
                case (0x05) : __ORA_ZP(); break;
                case (0x06) : __ASL_ZP(); break;
                case (0x08) : __PHP(); break;
                case (0x09) : __ORA_IM(); break;
                case (0x0A) : __ASL_A(); break;
                case (0x0D) : __ORA_AS(); break;
                case (0x0E) : __ASL_AS(); break;
            }
        }).start();
    }

    private void __BRK() {

    }

    private void __ORA_X() {

    }

    private void __ORA_ZP() {

    }

    private void __ASL_ZP() {

    }

    private void __PHP() {

    }

    private void __ORA_IM() {

    }

    private void __ASL_A() {

    }

    private void __ORA_AS() {

    }

    private void __ASL_AS() {

    }
}

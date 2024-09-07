package com.lx862.svrutil;

import net.minecraft.entity.player.PlayerEntity;

public class Util {
    private static final int OP_LEVEL = 4;

    public static int getPermLevel(PlayerEntity player) {
        for(int i = 0; i < OP_LEVEL; i++) {
            if(player.hasPermissionLevel(OP_LEVEL - i)) {
                return OP_LEVEL - i;
            }
        }
        return 0;
    }
}

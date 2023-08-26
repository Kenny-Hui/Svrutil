package com.lx862.svrutil.mixin;

import com.lx862.svrutil.config.MainConfig;
import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FallingBlock.class)
public class FallingBlockMixin {
    @Inject(method = "getFallDelay", at = @At("HEAD"), cancellable = true)
    private void getFallDelay(CallbackInfoReturnable<Integer> cir) {
        if(MainConfig.getFallingBlockDelay() > 0) {
            cir.setReturnValue(MainConfig.getFallingBlockDelay());
        }
    }


    @Inject(method = "canFallThrough", at = @At("HEAD"), cancellable = true)
    private static void canFallThrough(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        // Disable block from falling entirely if delay is negative
        if(MainConfig.getFallingBlockDelay() < 0) {
            cir.setReturnValue(false);
        }
    }
}

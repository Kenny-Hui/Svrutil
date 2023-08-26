package com.lx862.svrutil.mixin;

import com.lx862.svrutil.config.MainConfig;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemFrameEntity.class)
public abstract class ItemFrameMixin extends AbstractDecorationEntity {

    protected ItemFrameMixin(EntityType<? extends AbstractDecorationEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "canStayAttached", at = @At("HEAD"), cancellable = true)
    public void canStayAttached(CallbackInfoReturnable<Boolean> cir) {
        if(MainConfig.getFixedItemFrame()) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    public void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if(MainConfig.getFixedItemFrame()) {
            #if MC_VERSION >= "11904"
                if(source.isIn(net.minecraft.registry.tag.DamageTypeTags.IS_EXPLOSION)) {
                    cir.setReturnValue(false);
                }
            #else
                if(source.isExplosive() || source.isProjectile()) {
                    cir.setReturnValue(false);
                }
            #endif
        }
    }
}

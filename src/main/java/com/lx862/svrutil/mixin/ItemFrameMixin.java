package com.lx862.svrutil.mixin;

import com.lx862.svrutil.feature.FeatureSet;
import com.lx862.svrutil.feature.VanillaMechanicsFeature;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
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
        if(FeatureSet.VANILLA_MECHANICS.feature.enabled && ((VanillaMechanicsFeature) FeatureSet.VANILLA_MECHANICS.feature).getImmutableItemFrame()) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    public void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if(FeatureSet.VANILLA_MECHANICS.feature.enabled && ((VanillaMechanicsFeature) FeatureSet.VANILLA_MECHANICS.feature).getImmutableItemFrame()) {
            if(source.isExplosive() || source.isProjectile()) {
                cir.setReturnValue(false);
            }
        }
    }

    @Inject(method = "interact", at = @At("HEAD"), cancellable = true)
    public void interact(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        if(!player.getWorld().isClient() && FeatureSet.VANILLA_MECHANICS.feature.enabled) {
            if(!player.hasPermissionLevel(((VanillaMechanicsFeature) FeatureSet.VANILLA_MECHANICS.feature).getMinItemFrameInteractOpLevel())) {
                cir.setReturnValue(ActionResult.PASS);
            }
        }
    }
}

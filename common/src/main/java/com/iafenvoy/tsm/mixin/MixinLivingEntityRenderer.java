package com.iafenvoy.tsm.mixin;

import com.iafenvoy.tsm.cursed.DummyClientPlayerEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public class MixinLivingEntityRenderer<T extends LivingEntity> {
    @Inject(method = "hasLabel(Lnet/minecraft/entity/LivingEntity;)Z", at = @At("HEAD"), cancellable = true)
    private void handleLabel(T livingEntity, CallbackInfoReturnable<Boolean> cir) {
        if (livingEntity instanceof DummyClientPlayerEntity || MinecraftClient.getInstance().currentScreen instanceof TitleScreen) cir.setReturnValue(false);
    }
}

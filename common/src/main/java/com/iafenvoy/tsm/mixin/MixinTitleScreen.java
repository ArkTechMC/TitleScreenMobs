package com.iafenvoy.tsm.mixin;

import com.iafenvoy.tsm.RenderHelper;
import com.iafenvoy.tsm.cursed.DummyClientPlayerEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class MixinTitleScreen {
    @Inject(method = "<init>()V", at = @At("RETURN"))
    private void mobsMainMenu_resetEntity(CallbackInfo ci) {
        RenderHelper.livingEntity = null;
    }

    @Inject(method = "render", at = @At("RETURN"))
    private void mobsMainMenu_render(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        TitleScreen sc = (TitleScreen) (Object) this;
        if (MinecraftClient.getInstance() != null) {
            ClientPlayerEntity player = DummyClientPlayerEntity.getInstance();
            int height = sc.height / 4 + 132;
            int playerX = sc.width / 2 - 160;
            InventoryScreen.drawEntity(matrices, playerX, height, 30, -mouseX + playerX, -mouseY + height - 30, player);
            int entityX = sc.width / 2 + 160;
            LivingEntity livingEntity = RenderHelper.livingEntity;
            if (livingEntity != null) {
                try {
                    RenderHelper.renderEntity(matrices, entityX, height, 30, -mouseX + entityX, -mouseY + height - 30, livingEntity);
                } catch (Exception e) {
                    RenderHelper.livingEntity = null;
                }
            }
        }
    }
}

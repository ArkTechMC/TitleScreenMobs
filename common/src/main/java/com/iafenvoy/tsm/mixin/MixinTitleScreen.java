package com.iafenvoy.tsm.mixin;

import com.iafenvoy.tsm.RenderHelper;
import com.iafenvoy.tsm.config.TsmConfig;
import com.iafenvoy.tsm.cursed.DummyClientPlayerEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TitleScreen.class)
public class MixinTitleScreen {
    @Inject(method = "<init>()V", at = @At("RETURN"))
    private void mobsMainMenu_resetEntity(CallbackInfo ci) {
        RenderHelper.livingEntity = null;
    }

    @Inject(method = "render", at = @At("RETURN"))
    private void mobsMainMenu_render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        TitleScreen sc = (TitleScreen) (Object) this;
        TsmConfig config = TsmConfig.getInstance();
        if (MinecraftClient.getInstance() != null) {
            ClientPlayerEntity player = DummyClientPlayerEntity.getInstance();
            int height = sc.height / 4 + 132;
            int playerX = sc.width / 2 - 160;
            if (config.left.visible)
                InventoryScreen.drawEntity(context, playerX - 25 + config.left.x, height - 70 + config.left.y, playerX + 25 + config.left.x, height + config.left.y, (int) (30 * config.left.scale), 0, mouseX, mouseY, player);
            int entityX = sc.width / 2 + 160;
            LivingEntity livingEntity = RenderHelper.livingEntity;
            if (livingEntity != null && config.right.visible) {
                try {
                    RenderHelper.renderEntity(context.getMatrices(), entityX + config.right.x, height + config.right.y, 30, -mouseX + entityX, -mouseY + height - 30, livingEntity, config.right.scale);
                } catch (Exception e) {
                    RenderHelper.livingEntity = null;
                    RenderHelper.foxRotate = false;
                }
            }
        }
    }

    @Inject(method = "mouseClicked", at = @At("RETURN"))
    private void handleFoxClick(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        TitleScreen sc = (TitleScreen) (Object) this;
        if (MinecraftClient.getInstance() != null) {
            int height = sc.height / 4 + 132;
            int entityX = sc.width / 2 + 160;
            if (mouseX >= entityX - 10 && mouseY >= height - 20 && mouseX <= entityX + 10 && mouseY <= height)
                RenderHelper.foxRotate = true;
        }
    }
}

package com.iafenvoy.tsm;

import com.iafenvoy.tsm.config.TsmConfig;
import com.iafenvoy.tsm.cursed.DummyClientWorld;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionf;

import java.util.List;
import java.util.Random;

public class RenderHelper {
    private static final Random RANDOM = new Random();
    public static LivingEntity livingEntity = null;
    public static boolean foxRotate = false, isNeoForge = false;
    private static final List<? extends EntityType<?>> ALLOW_ENTITIES;

    static {
        List<? extends EntityType<?>> collect = TsmConfig.getInstance().whitelist.stream().map(Identifier::new).map(Registries.ENTITY_TYPE::get).toList();
        if (collect.isEmpty())
            collect = Registries.ENTITY_TYPE.stream().filter((e) -> !TsmConfig.getInstance().blacklist.contains(Registries.ENTITY_TYPE.getId(e).toString())).toList();
        ALLOW_ENTITIES = collect;
    }

    public static void endClientTick() {
        if (livingEntity == null) {
            Entity entity = ALLOW_ENTITIES.get(RANDOM.nextInt(ALLOW_ENTITIES.size())).create(DummyClientWorld.getInstance());
            if (entity instanceof LivingEntity) livingEntity = (LivingEntity) entity;
        }
        if (!(MinecraftClient.getInstance().currentScreen instanceof TitleScreen)) foxRotate = false;
    }

    public static void renderEntity(MatrixStack matrices, int x, int y, int size, float mouseX, float mouseY, LivingEntity entity) {
        float f = (float) Math.atan(mouseX / 40.0F);
        float g = (float) Math.atan(mouseY / 40.0F);
        matrices.push();
        matrices.translate((float) x, (float) y, 1050.0F);
        matrices.scale(1.0F, 1.0F, -1.0F);
        matrices.translate(0.0D, 0.0D, 1000.0D);
        matrices.scale((float) size, (float) size, (float) size);
        Quaternionf quaternion = RotationAxis.POSITIVE_Z.rotationDegrees(180.0F);
        Quaternionf quaternion2 = RotationAxis.POSITIVE_X.rotationDegrees(g * 20.0F);
        quaternion.mul(quaternion2);
        if (foxRotate && isNeoForge) {
            Quaternionf quaternion3 = RotationAxis.POSITIVE_X.rotationDegrees(-20);
            Quaternionf quaternion4 = RotationAxis.POSITIVE_Y.rotationDegrees(System.nanoTime() / 1000000f);
            quaternion.mul(quaternion3);
            quaternion.mul(quaternion4);
        }
        matrices.multiply(quaternion);
        float h = entity.bodyYaw;
        float i = entity.getYaw();
        float j = entity.getPitch();
        float k = entity.prevHeadYaw;
        float l = entity.headYaw;
        if (!foxRotate || !isNeoForge) {
            entity.bodyYaw = 180.0F + f * 20.0F;
            entity.setYaw(180.0F + f * 40.0F);
            entity.setPitch(-g * 20.0F);
            entity.headYaw = entity.getYaw();
            entity.prevHeadYaw = entity.getYaw();
        }
        EntityRenderDispatcher entityRenderDispatcher = MinecraftClient.getInstance().getEntityRenderDispatcher();
        quaternion2.conjugate();
        entityRenderDispatcher.setRotation(quaternion2);
        entityRenderDispatcher.setRenderShadows(false);
        VertexConsumerProvider.Immediate immediate = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
        RenderSystem.runAsFancy(() -> {
            double width = entity.getBoundingBox().getLengthX();
            double height = entity.getBoundingBox().getLengthY();
            if (width > 0.6) {
                width *= 1f / ((float) width / 0.6f);
                height = entity.getBoundingBox().getLengthY() * (width / entity.getBoundingBox().getLengthX());
            }
            if (height > 2.0) {
                width *= 1f / (height / 2f);
            }
            matrices.scale((float) (width / entity.getBoundingBox().getLengthX()), (float) (width / entity.getBoundingBox().getLengthX()), (float) (width / entity.getBoundingBox().getLengthX()));
            entityRenderDispatcher.render(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, matrices, immediate, 15728880);
        });
        immediate.draw();
        entityRenderDispatcher.setRenderShadows(true);
        entity.bodyYaw = h;
        entity.setYaw(i);
        entity.setPitch(j);
        entity.prevHeadYaw = k;
        entity.headYaw = l;
        matrices.pop();
    }
}

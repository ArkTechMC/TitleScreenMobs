package com.iafenvoy.tsm.cursed;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.Function;

//Code from CICADA under MIT license
public class DummyClientPlayerEntity extends ClientPlayerEntity {
    private static DummyClientPlayerEntity instance;
    private Identifier skinIdentifier = null, capeIdentifier = null;
    private String model = null;
    private PlayerEntity player = null;
    private Text name = null;
    public Function<EquipmentSlot, ItemStack> equippedStackSupplier = slot -> ItemStack.EMPTY;

    public static DummyClientPlayerEntity getInstance() {
        if (instance == null) instance = new DummyClientPlayerEntity();
        return instance;
    }

    private DummyClientPlayerEntity() {
        super(MinecraftClient.getInstance(), DummyClientWorld.getInstance(), DummyClientPlayNetworkHandler.getInstance(), null, null, false, false);
        setUuid(UUID.randomUUID());
        MinecraftClient.getInstance().getSkinProvider().loadSkin(getGameProfile(), (type, identifier, texture) -> {
            if (type == MinecraftProfileTexture.Type.SKIN) {
                skinIdentifier = identifier;
                model = texture.getMetadata("model");
                if (model == null) {
                    model = "default";
                }
            }
            if (type == MinecraftProfileTexture.Type.CAPE)
                capeIdentifier = identifier;
        }, true);
    }

    @Override
    public boolean isPartVisible(PlayerModelPart modelPart) {
        return true;
    }

    @Override
    public boolean hasSkinTexture() {
        return true;
    }

    @Override
    public Identifier getSkinTexture() {
        return skinIdentifier == null ? DefaultSkinHelper.getTexture(getUuid()) : skinIdentifier;
    }

    @Override
    public boolean canRenderCapeTexture() {
        return true;
    }

    @Override
    public @Nullable Identifier getCapeTexture() {
        return this.capeIdentifier;
    }

    @Override
    public String getModel() {
        return model == null ? DefaultSkinHelper.getModel(getUuid()) : model;
    }

    @Nullable
    @Override
    protected PlayerListEntry getPlayerListEntry() {
        return null;
    }

    @Override
    public boolean isSpectator() {
        return false;
    }

    @Override
    public boolean isCreative() {
        return true;
    }

    @Override
    public ItemStack getEquippedStack(EquipmentSlot slot) {
        if (player != null) {
            return player.getEquippedStack(slot);
        }
        return equippedStackSupplier.apply(slot);
    }

    @Override
    public Text getName() {
        if (name == null) {
            return super.getName();
        } else {
            return name;
        }
    }
}

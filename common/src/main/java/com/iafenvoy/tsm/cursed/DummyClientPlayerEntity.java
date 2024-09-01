package com.iafenvoy.tsm.cursed;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.Function;

//Code from CICADA under MIT license
public class DummyClientPlayerEntity extends ClientPlayerEntity {
    private static DummyClientPlayerEntity instance;
    private SkinTextures skinTextures = null;
    public Function<EquipmentSlot, ItemStack> equippedStackSupplier = slot -> ItemStack.EMPTY;

    public static DummyClientPlayerEntity getInstance() {
        if (instance == null) instance = new DummyClientPlayerEntity();
        return instance;
    }

    private DummyClientPlayerEntity() {
        super(MinecraftClient.getInstance(), DummyClientWorld.getInstance(), DummyClientPlayNetworkHandler.getInstance(), null, null, false, false);
        setUuid(UUID.randomUUID());
        MinecraftClient.getInstance().getSkinProvider().fetchSkinTextures(getGameProfile()).thenAccept((textures) -> skinTextures = textures);
    }

    @Override
    public boolean isPartVisible(PlayerModelPart modelPart) {
        return true;
    }

    @Override
    public SkinTextures getSkinTextures() {
        return skinTextures == null ? DefaultSkinHelper.getSkinTextures(this.getUuid()) : skinTextures;
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
        return equippedStackSupplier.apply(slot);
    }

    @Override
    public Text getName() {
        return super.getName();
    }
}

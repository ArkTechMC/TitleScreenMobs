package com.iafenvoy.tsm.cursed;

import com.iafenvoy.tsm.TitleScreenMobs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.Difficulty;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.DimensionTypes;

import java.util.OptionalLong;

//Code from CICADA under MIT license
public class DummyClientWorld extends ClientWorld {
    private static DummyClientWorld instance;

    public static DummyClientWorld getInstance() {
        if (instance == null) instance = new DummyClientWorld();
        return instance;
    }

    private DummyClientWorld() {
        super(
                DummyClientPlayNetworkHandler.getInstance(),
                new Properties(Difficulty.EASY, false, true),
                RegistryKey.of(RegistryKeys.WORLD, Identifier.of(TitleScreenMobs.MOD_ID, "dummy")),
                new CursedRegistryEntry<>(DummyDimensionType.getInstance(), RegistryKeys.DIMENSION_TYPE),
                0,
                0,
                () -> MinecraftClient.getInstance().getProfiler(),
                MinecraftClient.getInstance().worldRenderer,
                false,
                0L
        );
    }

    @Override
    public DynamicRegistryManager getRegistryManager() {
        return super.getRegistryManager();
    }

    public static class DummyDimensionType {
        private static DimensionType instance;

        public static DimensionType getInstance() {
            if (instance == null)
                instance = new DimensionType(OptionalLong.empty(), true, false, false, false, 1.0, false, false, 16, 32, 0, BlockTags.INFINIBURN_OVERWORLD, DimensionTypes.OVERWORLD_ID, 1f, new DimensionType.MonsterSettings(false, false, UniformIntProvider.create(0, 0), 0));
            return instance;
        }
    }
}

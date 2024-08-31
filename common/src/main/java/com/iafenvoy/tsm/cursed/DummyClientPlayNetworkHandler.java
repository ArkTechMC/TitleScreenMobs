package com.iafenvoy.tsm.cursed;

import com.iafenvoy.tsm.TitleScreenMobs;
import com.mojang.serialization.Lifecycle;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.damage.DamageScaling;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkSide;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.DimensionTypes;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.stream.Stream;

//Code from CICADA under MIT license
public class DummyClientPlayNetworkHandler extends ClientPlayNetworkHandler {
    public static final Registry<DimensionType> CURSED_DIMENSION_TYPE_REGISTRY = new SimpleRegistry<>(RegistryKeys.DIMENSION_TYPE, Lifecycle.stable());

    static {
        Registry.register(CURSED_DIMENSION_TYPE_REGISTRY, new Identifier(TitleScreenMobs.MOD_ID, "dummy"), new DimensionType(
                OptionalLong.of(6000L),
                true,
                false,
                false,
                true,
                1.0,
                true,
                false,
                -64,
                384,
                384,
                BlockTags.INFINIBURN_OVERWORLD,
                DimensionTypes.OVERWORLD_ID,
                0.0f,
                new DimensionType.MonsterSettings(
                        false,
                        true,
                        UniformIntProvider.create(0, 7),
                        0
                )
        ));
    }

    private static DummyClientPlayNetworkHandler instance;

    public static DummyClientPlayNetworkHandler getInstance() {
        if (instance == null) instance = new DummyClientPlayNetworkHandler();
        return instance;
    }

    private static final Registry<Biome> cursedBiomeRegistry = new SimpleDefaultedRegistry<>("dummy", RegistryKeys.BIOME, Lifecycle.stable(), true) {
        @Override
        public RegistryEntry.Reference<Biome> entryOf(RegistryKey<Biome> key) {
            return null;
        }
    };

    private static final Registry<BannerPattern> cursedBannerRegistry = new SimpleDefaultedRegistry<>("dummy", RegistryKeys.BANNER_PATTERN, Lifecycle.stable(), true);

    private static final DynamicRegistryManager.Immutable cursedRegistryManager = new DynamicRegistryManager.Immutable() {
        private final CursedRegistry<DamageType> damageTypes = new CursedRegistry<>(RegistryKeys.DAMAGE_TYPE, new Identifier("fake_damage"),
                new DamageType("", DamageScaling.NEVER, 0));

        @SuppressWarnings({"unchecked", "rawtypes"})
        @Override
        public Optional<Registry> getOptional(RegistryKey key) {
            var x = Registries.REGISTRIES.get(key);
            if (x != null) {
                return Optional.of(x);
            } else if (RegistryKeys.DAMAGE_TYPE.equals(key)) {
                return Optional.of(damageTypes);
            } else if (RegistryKeys.BIOME.equals(key)) {
                return Optional.of(cursedBiomeRegistry);
            } else if (RegistryKeys.DIMENSION_TYPE.equals(key)) {
                return Optional.of(CURSED_DIMENSION_TYPE_REGISTRY);
            } else if (RegistryKeys.BANNER_PATTERN.equals(key)) {
                // This fixes lithium compat post-1.20.5
                return Optional.of(cursedBannerRegistry);
            }

            return Optional.empty();
        }

        @Override
        public Stream<Entry<?>> streamAllRegistries() {
            return Stream.empty();
        }
    };

    private DummyClientPlayNetworkHandler() {
        /*super(
                MinecraftClient.getInstance(),
                null,
                new ClientConnection(NetworkSide.CLIENTBOUND),
                MinecraftClient.getInstance().getCurrentServerEntry(),
                MinecraftClient.getInstance().getSession().getProfile(),
                MinecraftClient.getInstance().getTelemetryManager().createWorldSession(true, Duration.of(0, ChronoUnit.SECONDS), null)
        );
        *//*?} else {*/
        super(
                MinecraftClient.getInstance(),
                null,
                new ClientConnection(NetworkSide.CLIENTBOUND),
                MinecraftClient.getInstance().getCurrentServerEntry(),
                MinecraftClient.getInstance().getSession().getProfile(),
                MinecraftClient.getInstance().getTelemetryManager().createWorldSession(true, Duration.of(0, ChronoUnit.SECONDS))
        );
    }

    @Override
    public DynamicRegistryManager.Immutable getRegistryManager() {
        return cursedRegistryManager;
    }
}

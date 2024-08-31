package com.iafenvoy.tsm;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.datafixers.util.Either;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkSide;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.Difficulty;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.DimensionTypes;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.OptionalLong;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class DummyClientPlayerEntity extends ClientPlayerEntity {
    private static DummyClientPlayerEntity instance;
    private Identifier skinIdentifier = null;
    private Identifier capeIdentifier = null;

    public static DummyClientPlayerEntity getInstance() {
        if (instance == null) instance = new DummyClientPlayerEntity();
        return instance;
    }

    private DummyClientPlayerEntity() {
        super(MinecraftClient.getInstance(), DummyClientWorld.getInstance(), DummyClientPlayNetworkHandler.getInstance(), null, null, false, false);
        MinecraftClient.getInstance().getSkinProvider().loadSkin(getGameProfile(), (type, identifier, texture) -> {
            if (type == MinecraftProfileTexture.Type.SKIN)
                skinIdentifier = identifier;
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
    public boolean canRenderCapeTexture() {
        return true;
    }

    @Override
    public Identifier getSkinTexture() {
        return skinIdentifier == null ? DefaultSkinHelper.getTexture(this.getUuid()) : skinIdentifier;
    }

    @Nullable
    @Override
    public Identifier getCapeTexture() {
        return capeIdentifier;
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

    public static class DummyClientPlayNetworkHandler extends ClientPlayNetworkHandler {
        private static DummyClientPlayNetworkHandler instance;

        public static DummyClientPlayNetworkHandler getInstance() {
            if (instance == null) instance = new DummyClientPlayNetworkHandler();
            return instance;
        }

        private DummyClientPlayNetworkHandler() {
            super(MinecraftClient.getInstance(), null, new ClientConnection(NetworkSide.CLIENTBOUND), MinecraftClient.getInstance().getSession().getProfile(), MinecraftClient.getInstance().createTelemetrySender());
        }
    }

    public static class DummyClientWorld extends ClientWorld {
        private static DummyClientWorld instance;

        public static DummyClientWorld getInstance() {
            if (instance == null) instance = new DummyClientWorld();
            return instance;
        }

        private DummyClientWorld() {
            super(DummyClientPlayNetworkHandler.getInstance(), new Properties(Difficulty.EASY, false, true), null, new DummyEntry<>(RegistryKey.of(Registry.DIMENSION_TYPE_KEY, new Identifier(TitleScreenMobs.MOD_ID, "dummy")), DummyDimensionType.getInstance()), 0, 0, () -> null, null, false, 0L);
        }
    }

    public static class DummyDimensionType {
        private static DimensionType instance;

        public static DimensionType getInstance() {
            if (instance == null)
                instance = new DimensionType(OptionalLong.empty(), true, false, false, false, 1.0, false, false, 16, 32, 0, BlockTags.INFINIBURN_OVERWORLD, DimensionTypes.OVERWORLD_ID, 1f, new DimensionType.MonsterSettings(false, false, UniformIntProvider.create(0, 0), 0));
            return instance;
        }
    }

    public record DummyEntry<T>(RegistryKey<T> key, T value) implements RegistryEntry<T> {
        public boolean hasKeyAndValue() {
            return true;
        }

        public boolean matchesId(Identifier id) {
            return false;
        }

        public boolean matchesKey(RegistryKey<T> key) {
            return false;
        }

        public boolean isIn(TagKey<T> tag) {
            return false;
        }

        public boolean matches(Predicate<RegistryKey<T>> predicate) {
            return false;
        }

        public Either<RegistryKey<T>, T> getKeyOrValue() {
            return Either.right(this.value);
        }

        public Optional<RegistryKey<T>> getKey() {
            return Optional.of(this.key);
        }

        public Type getType() {
            return Type.REFERENCE;
        }

        public String toString() {
            return "Dummy{" + this.value + "}";
        }

        public boolean matchesRegistry(Registry<T> registry) {
            return true;
        }

        public Stream<TagKey<T>> streamTags() {
            return Stream.of();
        }

        public T value() {
            return this.value;
        }
    }
}

package com.iafenvoy.tsm.forge;

import com.iafenvoy.tsm.TitleScreenMobs;
import com.iafenvoy.tsm.config.TsmConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod(TitleScreenMobs.MOD_ID)
@Mod.EventBusSubscriber(bus= Mod.EventBusSubscriber.Bus.MOD,value = Dist.CLIENT)
public final class TitleScreenMobsForge {
    public TitleScreenMobsForge() {
        TitleScreenMobs.onInitializeClient();
    }

    @SubscribeEvent
    public static void initClient(FMLClientSetupEvent event) {
        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () -> new ConfigScreenHandler.ConfigScreenFactory((client, screen) -> AutoConfig.getConfigScreen(TsmConfig.class, screen).get()));
    }
}

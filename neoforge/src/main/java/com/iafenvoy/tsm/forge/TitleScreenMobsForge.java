package com.iafenvoy.tsm.forge;

import com.iafenvoy.tsm.RenderHelper;
import com.iafenvoy.tsm.TitleScreenMobs;
import com.iafenvoy.tsm.config.TsmConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(TitleScreenMobs.MOD_ID)
@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD,value = Dist.CLIENT)
public final class TitleScreenMobsForge {
    public TitleScreenMobsForge() {
        TitleScreenMobs.onInitializeClient();
        RenderHelper.isNeoForge = true;
    }

    @SubscribeEvent
    public static void clientInit(FMLClientSetupEvent event) {
        ModList.get().getModContainerById(TitleScreenMobs.MOD_ID).get().registerExtensionPoint(IConfigScreenFactory.class, (container, parent) -> AutoConfig.getConfigScreen(TsmConfig.class, parent).get());
    }
}

package com.iafenvoy.tsm.fabric;

import com.iafenvoy.tsm.TitleScreenMobs;
import net.fabricmc.api.ClientModInitializer;

public class TitleScreenMobsFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        TitleScreenMobs.onInitializeClient();
    }
}

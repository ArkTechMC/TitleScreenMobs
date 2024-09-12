package com.iafenvoy.tsm;

import com.iafenvoy.tsm.config.TsmConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;

public final class TitleScreenMobs {
    public static final String MOD_ID = "title_screen_mobs";

    public static void onInitializeClient() {
        AutoConfig.register(TsmConfig.class, GsonConfigSerializer::new);
    }
}

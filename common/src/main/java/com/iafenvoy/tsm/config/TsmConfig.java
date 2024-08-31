package com.iafenvoy.tsm.config;

import java.util.ArrayList;
import java.util.List;

public class TsmConfig {
    private static TsmConfig INSTANCE = null;

    public List<String> whitelist = new ArrayList<>();
    public List<String> blacklist = List.of("minecraft:wither", "minecraft:ender_dragon");

    public static TsmConfig getInstance() {
        if (INSTANCE == null)
            INSTANCE = ConfigLoader.load(TsmConfig.class, "./config/title-screen-mob.json", new TsmConfig());
        return INSTANCE;
    }
}

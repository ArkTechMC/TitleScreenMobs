package com.iafenvoy.tsm.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

import java.util.ArrayList;
import java.util.List;

@Config(name = "title-screen-mob")
public class TsmConfig implements ConfigData {
    public List<String> whitelist = new ArrayList<>();
    public List<String> blacklist = List.of("minecraft:wither", "minecraft:ender_dragon");
    @ConfigEntry.Gui.CollapsibleObject
    public ModelConfig left = new ModelConfig();
    @ConfigEntry.Gui.CollapsibleObject
    public ModelConfig right = new ModelConfig();

    public static class ModelConfig implements ConfigData {
        public boolean visible = true;
        public int x = 0;
        public int y = 0;
        public float scale = 1;
    }

    public static TsmConfig getInstance() {
        return AutoConfig.getConfigHolder(TsmConfig.class).getConfig();
    }
}

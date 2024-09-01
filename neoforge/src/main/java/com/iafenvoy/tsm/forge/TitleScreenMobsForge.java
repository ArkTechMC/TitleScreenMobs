package com.iafenvoy.tsm.forge;

import com.iafenvoy.tsm.RenderHelper;
import com.iafenvoy.tsm.TitleScreenMobs;
import net.neoforged.fml.common.Mod;

@Mod(TitleScreenMobs.MOD_ID)
public final class TitleScreenMobsForge {
    public TitleScreenMobsForge() {
        RenderHelper.isNeoForge = true;
    }
}

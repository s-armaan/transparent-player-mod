package io.github.s_armaan.transparentplayer;

import io.github.s_armaan.transparentplayer.config.TransparentPlayerConfig;
import io.github.s_armaan.transparentplayer.platform.Services;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class CommonClass {

    /** Called by each loader's entry point once the loader is ready. */
    public static void init() {
        Constants.LOG.info("Initialising {} on {}", Constants.MOD_NAME, Services.PLATFORM.getPlatformName());
        TransparentPlayerConfig.load();
    }

    /** Called by each loader at the end of every client tick. */
    public static void onClientTick(Minecraft minecraft) {
        if (TransparentPlayerKeys.toggle == null) {
            return;
        }
        while (TransparentPlayerKeys.toggle.consumeClick()) {
            TransparentPlayerConfig config = TransparentPlayerConfig.get();
            config.enabled = !config.enabled;
            TransparentPlayerConfig.save();
            if (minecraft.player != null) {
                minecraft.player.sendOverlayMessage(Component.translatable(
                        "message." + Constants.MOD_ID + (config.enabled ? ".enabled" : ".disabled")));
            }
        }
    }
}

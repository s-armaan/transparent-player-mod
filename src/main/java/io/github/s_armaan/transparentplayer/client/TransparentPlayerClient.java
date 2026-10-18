package io.github.s_armaan.transparentplayer.client;

import io.github.s_armaan.transparentplayer.Constants;
import io.github.s_armaan.transparentplayer.TransparentPlayerKeys;
import io.github.s_armaan.transparentplayer.config.TransparentPlayerConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

public class TransparentPlayerClient {

    public static void init() {
        Constants.LOG.info("Initialising {} on Forge", Constants.MOD_NAME);
        TransparentPlayerConfig.load(FMLPaths.CONFIGDIR.get());
        FMLJavaModLoadingContext.get().getModEventBus().addListener(TransparentPlayerClient::registerKeyMappings);
        MinecraftForge.EVENT_BUS.addListener(TransparentPlayerClient::onClientTick);
    }

    private static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(TransparentPlayerKeys.TOGGLE);
    }

    private static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        Minecraft minecraft = Minecraft.getInstance();
        while (TransparentPlayerKeys.TOGGLE.consumeClick()) {
            TransparentPlayerConfig config = TransparentPlayerConfig.get();
            config.enabled = !config.enabled;
            TransparentPlayerConfig.save();
            if (minecraft.player != null) {
                minecraft.player.displayClientMessage(Component.translatable(
                        "message." + Constants.MOD_ID + (config.enabled ? ".enabled" : ".disabled")), true);
            }
        }
    }
}

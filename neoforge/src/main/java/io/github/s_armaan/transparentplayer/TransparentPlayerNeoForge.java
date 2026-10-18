package io.github.s_armaan.transparentplayer;

import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = Constants.MOD_ID, dist = Dist.CLIENT)
public class TransparentPlayerNeoForge {

    public TransparentPlayerNeoForge(IEventBus eventBus) {
        CommonClass.init();
        eventBus.addListener(TransparentPlayerNeoForge::registerKeyMappings);
        NeoForge.EVENT_BUS.addListener((ClientTickEvent.Post event) -> CommonClass.onClientTick(net.minecraft.client.Minecraft.getInstance()));
    }

    private static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        KeyMapping.Category category = new KeyMapping.Category(TransparentPlayerKeys.CATEGORY_ID);
        event.registerCategory(category);
        event.register(TransparentPlayerKeys.createToggle(category));
    }
}

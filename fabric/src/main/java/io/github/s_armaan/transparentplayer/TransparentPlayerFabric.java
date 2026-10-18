package io.github.s_armaan.transparentplayer;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;

public class TransparentPlayerFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        CommonClass.init();
        KeyMapping.Category category = KeyMapping.Category.register(TransparentPlayerKeys.CATEGORY_ID);
        KeyMappingHelper.registerKeyMapping(TransparentPlayerKeys.createToggle(category));
        ClientTickEvents.END_CLIENT_TICK.register(CommonClass::onClientTick);
    }
}

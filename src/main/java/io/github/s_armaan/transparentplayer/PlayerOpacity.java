package io.github.s_armaan.transparentplayer;

import io.github.s_armaan.transparentplayer.config.TransparentPlayerConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;

/** Decides which players get the translucent treatment. Used by the renderer mixin. */
public class PlayerOpacity {

    /** True when {@code entity} is a player that should currently be drawn translucent. */
    public static boolean appliesTo(LivingEntity entity) {
        TransparentPlayerConfig config = TransparentPlayerConfig.get();
        if (!config.enabled || !config.applyToSelf || entity == null) {
            return false;
        }
        return entity == Minecraft.getInstance().player;
    }
}

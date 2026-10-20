package io.github.s_armaan.transparentplayer;

import io.github.s_armaan.transparentplayer.config.TransparentPlayerConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;

/** Decides which parts of the local player get the translucent treatment. Used by the renderer mixins. */
public class PlayerOpacity {

    // True only while the local player's layers or first-person hands are being rendered. Rendering is single
    // threaded, so the render type hooks can simply read this flag.
    private static boolean fading;

    /** True when {@code entity} is the local player and the effect is on for the body. */
    public static boolean appliesTo(LivingEntity entity) {
        TransparentPlayerConfig config = TransparentPlayerConfig.get();
        if (!config.enabled || !config.applyToSelf || entity == null) {
            return false;
        }
        return entity == Minecraft.getInstance().player;
    }

    /** True when armor, cape and the other layers of {@code entity} should fade too. */
    public static boolean layersApply(LivingEntity entity) {
        return appliesTo(entity) && TransparentPlayerConfig.get().applyToLayers;
    }

    /** True when the first-person arm and held item should fade. */
    public static boolean firstPersonApplies() {
        TransparentPlayerConfig config = TransparentPlayerConfig.get();
        return config.enabled && config.applyToSelf && config.applyToFirstPerson;
    }

    public static boolean isFading() {
        return fading;
    }

    /** Sets the flag and returns the previous value so callers can restore it. */
    public static boolean setFading(boolean value) {
        boolean previous = fading;
        fading = value;
        return previous;
    }

    public static float opacity() {
        return TransparentPlayerConfig.get().effectiveOpacity();
    }
}

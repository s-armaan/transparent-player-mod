package io.github.s_armaan.transparentplayer;

import io.github.s_armaan.transparentplayer.config.TransparentPlayerConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

/** Decides which parts of the local player get the translucent treatment. Used by the renderer mixins. */
public class PlayerOpacity {

    // True only while the local player's layers or first-person hands are being submitted. Rendering is single
    // threaded and submission is synchronous, so the render type and colour hooks can simply read this flag.
    private static boolean fading;

    /** True when {@code state} belongs to the local player and the effect is on for the body. */
    public static boolean appliesTo(LivingEntityRenderState state) {
        TransparentPlayerConfig config = TransparentPlayerConfig.get();
        if (!config.enabled || !config.applyToSelf || !(state instanceof AvatarRenderState avatar)) {
            return false;
        }
        LocalPlayer player = Minecraft.getInstance().player;
        return player != null && avatar.id == player.getId();
    }

    /** True when armor, cape and the other layers of {@code state} should fade too. */
    public static boolean layersApply(LivingEntityRenderState state) {
        return appliesTo(state) && TransparentPlayerConfig.get().applyToLayers;
    }

    /** True when the first-person arm and held item should fade. */
    public static boolean firstPersonApplies() {
        TransparentPlayerConfig config = TransparentPlayerConfig.get();
        return config.enabled && config.applyToSelf && config.applyToFirstPerson;
    }

    public static boolean isFading() {
        return fading;
    }

    /** Sets the flag and returns the previous value so callers can restore it in a finally block. */
    public static boolean setFading(boolean value) {
        boolean previous = fading;
        fading = value;
        return previous;
    }

    public static float opacity() {
        return TransparentPlayerConfig.get().effectiveOpacity();
    }
}

package io.github.s_armaan.transparentplayer;

import io.github.s_armaan.transparentplayer.config.TransparentPlayerConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

/** Decides which players get the translucent treatment. Used by the renderer mixin. */
public class PlayerOpacity {

    /** True when {@code state} belongs to a player that should currently be drawn translucent. */
    public static boolean appliesTo(LivingEntityRenderState state) {
        TransparentPlayerConfig config = TransparentPlayerConfig.get();
        if (!config.enabled || !config.applyToSelf || !(state instanceof AvatarRenderState avatar)) {
            return false;
        }
        LocalPlayer player = Minecraft.getInstance().player;
        return player != null && avatar.id == player.getId();
    }
}

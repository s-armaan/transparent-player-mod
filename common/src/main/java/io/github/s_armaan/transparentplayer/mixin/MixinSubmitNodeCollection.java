package io.github.s_armaan.transparentplayer.mixin;

import io.github.s_armaan.transparentplayer.PlayerOpacity;
import net.minecraft.client.renderer.SubmitNodeCollection;
import net.minecraft.util.ARGB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(SubmitNodeCollection.class)
public class MixinSubmitNodeCollection {

    // Every model (armor, cape, elytra, the first-person arm...) goes through submitModel. Its int arguments are the
    // light coords, overlay coords, tinted colour and outline colour, so the tint is the third int (ordinal 2).
    // Targeting by name keeps this working when a version changes the trailing parameters.
    @ModifyVariable(method = "submitModel", at = @At("HEAD"), argsOnly = true, ordinal = 2)
    private int transparentplayer$fadeModel(int tintedColor) {
        if (PlayerOpacity.isFading()) {
            return ARGB.multiplyAlpha(tintedColor, PlayerOpacity.opacity());
        }
        return tintedColor;
    }
}

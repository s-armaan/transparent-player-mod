package io.github.s_armaan.transparentplayer.mixin;

import io.github.s_armaan.transparentplayer.PlayerOpacity;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Armor, cape and the other layers ask for opaque or cutout render types, which ignore alpha. While the local
 * player's layers are being submitted, hand them the translucent entity type for the same texture instead. These
 * factories exist with the same signatures on every supported Minecraft version, unlike the translucent variants.
 */
@Mixin(RenderTypes.class)
public class MixinRenderTypes {

    private static final String RETURNS = ")Lnet/minecraft/client/renderer/rendertype/RenderType;";
    private static final String ID = "Lnet/minecraft/resources/Identifier;";

    @Inject(method = "armorCutoutNoCull(" + ID + RETURNS, at = @At("HEAD"), cancellable = true)
    private static void transparentplayer$armor(Identifier texture, CallbackInfoReturnable<RenderType> cir) {
        transparentplayer$translucent(texture, cir);
    }

    @Inject(method = "entitySolid(" + ID + RETURNS, at = @At("HEAD"), cancellable = true)
    private static void transparentplayer$solid(Identifier texture, CallbackInfoReturnable<RenderType> cir) {
        transparentplayer$translucent(texture, cir);
    }

    @Inject(method = "entitySolidZOffsetForward(" + ID + RETURNS, at = @At("HEAD"), cancellable = true)
    private static void transparentplayer$solidZOffset(Identifier texture, CallbackInfoReturnable<RenderType> cir) {
        transparentplayer$translucent(texture, cir);
    }

    @Inject(method = "entityCutoutCull(" + ID + RETURNS, at = @At("HEAD"), cancellable = true)
    private static void transparentplayer$cutoutCull(Identifier texture, CallbackInfoReturnable<RenderType> cir) {
        transparentplayer$translucent(texture, cir);
    }

    // entityCutout(Identifier) delegates to this overload.
    @Inject(method = "entityCutout(" + ID + "Z" + RETURNS, at = @At("HEAD"), cancellable = true)
    private static void transparentplayer$cutout(Identifier texture, boolean affectsOutline, CallbackInfoReturnable<RenderType> cir) {
        transparentplayer$translucent(texture, cir);
    }

    @Inject(method = "entityCutoutZOffset(" + ID + "Z" + RETURNS, at = @At("HEAD"), cancellable = true)
    private static void transparentplayer$cutoutZOffset(Identifier texture, boolean affectsOutline, CallbackInfoReturnable<RenderType> cir) {
        transparentplayer$translucent(texture, cir);
    }

    private static void transparentplayer$translucent(Identifier texture, CallbackInfoReturnable<RenderType> cir) {
        if (PlayerOpacity.isFading()) {
            cir.setReturnValue(RenderTypes.entityTranslucent(texture));
        }
    }
}

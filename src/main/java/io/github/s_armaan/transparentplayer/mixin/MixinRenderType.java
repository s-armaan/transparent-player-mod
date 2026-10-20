package io.github.s_armaan.transparentplayer.mixin;

import io.github.s_armaan.transparentplayer.PlayerOpacity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Armor, cape and the other layers ask for opaque or cutout render types, which ignore alpha. While the local
 * player's layers are being rendered, hand them the translucent entity type for the same texture instead.
 */
@Mixin(RenderType.class)
public class MixinRenderType {

    private static final String RL = "Lnet/minecraft/resources/ResourceLocation;";
    private static final String RETURNS = ")Lnet/minecraft/client/renderer/RenderType;";

    @Inject(method = "armorCutoutNoCull(" + RL + RETURNS, at = @At("HEAD"), cancellable = true)
    private static void transparentplayer$armor(ResourceLocation texture, CallbackInfoReturnable<RenderType> cir) {
        transparentplayer$translucent(texture, cir);
    }

    @Inject(method = "entitySolid(" + RL + RETURNS, at = @At("HEAD"), cancellable = true)
    private static void transparentplayer$solid(ResourceLocation texture, CallbackInfoReturnable<RenderType> cir) {
        transparentplayer$translucent(texture, cir);
    }

    @Inject(method = "entityCutout(" + RL + RETURNS, at = @At("HEAD"), cancellable = true)
    private static void transparentplayer$cutout(ResourceLocation texture, CallbackInfoReturnable<RenderType> cir) {
        transparentplayer$translucent(texture, cir);
    }

    // entityCutoutNoCull(ResourceLocation) delegates to this overload.
    @Inject(method = "entityCutoutNoCull(" + RL + "Z" + RETURNS, at = @At("HEAD"), cancellable = true)
    private static void transparentplayer$cutoutNoCull(ResourceLocation texture, boolean affectsOutline, CallbackInfoReturnable<RenderType> cir) {
        transparentplayer$translucent(texture, cir);
    }

    @Inject(method = "entityCutoutNoCullZOffset(" + RL + "Z" + RETURNS, at = @At("HEAD"), cancellable = true)
    private static void transparentplayer$cutoutNoCullZOffset(ResourceLocation texture, boolean affectsOutline, CallbackInfoReturnable<RenderType> cir) {
        transparentplayer$translucent(texture, cir);
    }

    @Inject(method = "entitySmoothCutout(" + RL + RETURNS, at = @At("HEAD"), cancellable = true)
    private static void transparentplayer$smoothCutout(ResourceLocation texture, CallbackInfoReturnable<RenderType> cir) {
        transparentplayer$translucent(texture, cir);
    }

    private static void transparentplayer$translucent(ResourceLocation texture, CallbackInfoReturnable<RenderType> cir) {
        if (PlayerOpacity.isFading()) {
            cir.setReturnValue(RenderType.entityTranslucent(texture));
        }
    }
}

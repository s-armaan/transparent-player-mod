package io.github.s_armaan.transparentplayer.mixin;

import io.github.s_armaan.transparentplayer.PlayerOpacity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Items are drawn on the block atlas with the solid or cutout sheet. Use the translucent one while fading. */
@Mixin(Sheets.class)
public class MixinSheets {

    @Inject(method = "solidBlockSheet", at = @At("HEAD"), cancellable = true)
    private static void transparentplayer$solid(CallbackInfoReturnable<RenderType> cir) {
        if (PlayerOpacity.isFading()) {
            cir.setReturnValue(Sheets.translucentCullBlockSheet());
        }
    }

    @Inject(method = "cutoutBlockSheet", at = @At("HEAD"), cancellable = true)
    private static void transparentplayer$cutout(CallbackInfoReturnable<RenderType> cir) {
        if (PlayerOpacity.isFading()) {
            cir.setReturnValue(Sheets.translucentCullBlockSheet());
        }
    }
}

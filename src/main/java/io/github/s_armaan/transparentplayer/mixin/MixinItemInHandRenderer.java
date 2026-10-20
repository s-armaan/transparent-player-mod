package io.github.s_armaan.transparentplayer.mixin;

import io.github.s_armaan.transparentplayer.PlayerOpacity;
import io.github.s_armaan.transparentplayer.render.TranslucentBufferSource;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public class MixinItemInHandRenderer {

    // Covers the first-person arm and the item held in it. The buffer is wrapped so alpha applies, and the flag is
    // raised for the duration so the render type hooks swap to the translucent types.
    @ModifyVariable(method = "renderArmWithItem", at = @At("HEAD"), argsOnly = true)
    private MultiBufferSource transparentplayer$wrapBuffer(MultiBufferSource buffer) {
        return PlayerOpacity.firstPersonApplies() ? new TranslucentBufferSource(buffer) : buffer;
    }

    @Inject(method = "renderArmWithItem", at = @At("HEAD"))
    private void transparentplayer$begin(CallbackInfo ci) {
        PlayerOpacity.setFading(PlayerOpacity.firstPersonApplies());
    }

    @Inject(method = "renderArmWithItem", at = @At("RETURN"))
    private void transparentplayer$end(CallbackInfo ci) {
        PlayerOpacity.setFading(false);
    }
}

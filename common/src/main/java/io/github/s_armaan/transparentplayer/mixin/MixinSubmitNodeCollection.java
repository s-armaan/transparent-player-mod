package io.github.s_armaan.transparentplayer.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.s_armaan.transparentplayer.ItemFade;
import io.github.s_armaan.transparentplayer.PlayerOpacity;
import net.minecraft.client.renderer.SubmitNodeCollection;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.util.ARGB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

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

    // Items (held, worn on the head) arrive as baked quads rather than models, with their render type and tint baked
    // in, so they are rewritten before being submitted.
    @WrapMethod(method = "submitItem")
    private void transparentplayer$fadeItem(PoseStack poseStack, ItemDisplayContext displayContext, int lightCoords, int overlayCoords, int outlineColor, int[] tintLayers, List<BakedQuad> quads, ItemStackRenderState.FoilType foilType, Operation<Void> original) {
        if (PlayerOpacity.isFading()) {
            ItemFade.Faded faded = ItemFade.fade(tintLayers, quads);
            tintLayers = faded.tintLayers();
            quads = faded.quads();
        }
        original.call(poseStack, displayContext, lightCoords, overlayCoords, outlineColor, tintLayers, quads, foilType);
    }
}

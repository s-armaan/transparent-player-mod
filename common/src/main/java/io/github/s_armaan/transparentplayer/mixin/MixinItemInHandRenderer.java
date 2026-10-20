package io.github.s_armaan.transparentplayer.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.s_armaan.transparentplayer.PlayerOpacity;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ItemInHandRenderer.class)
public class MixinItemInHandRenderer {

    // Covers the first-person arm and the item held in it.
    @WrapMethod(method = "submitHandsWithItems")
    private void transparentplayer$fadeHands(float frameInterp, PoseStack poseStack, SubmitNodeCollector collector, LocalPlayer player, int lightCoords, Operation<Void> original) {
        boolean previous = PlayerOpacity.setFading(PlayerOpacity.firstPersonApplies());
        try {
            original.call(frameInterp, poseStack, collector, player, lightCoords);
        } finally {
            PlayerOpacity.setFading(previous);
        }
    }
}

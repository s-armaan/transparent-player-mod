package io.github.s_armaan.transparentplayer.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.s_armaan.transparentplayer.PlayerOpacity;
import net.minecraft.client.renderer.FirstPersonHandsAndItemsRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.FirstPersonHandsAndItemsRenderState;
import net.minecraft.client.renderer.state.level.PlayerRenderState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(FirstPersonHandsAndItemsRenderer.class)
public class MixinFirstPersonHandsAndItemsRenderer {

    // Covers the first-person arm and the item held in it.
    @WrapMethod(method = "submitHandsWithItems")
    private void transparentplayer$fadeHands(float partialTicks, PoseStack poseStack, SubmitNodeCollector collector, PlayerRenderState playerState, FirstPersonHandsAndItemsRenderState state, Operation<Void> original) {
        boolean previous = PlayerOpacity.setFading(PlayerOpacity.firstPersonApplies());
        try {
            original.call(partialTicks, poseStack, collector, playerState, state);
        } finally {
            PlayerOpacity.setFading(previous);
        }
    }
}

package io.github.s_armaan.transparentplayer.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.s_armaan.transparentplayer.PlayerOpacity;
import io.github.s_armaan.transparentplayer.config.TransparentPlayerConfig;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntityRenderer.class)
public abstract class MixinLivingEntityRenderer<T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>> {

    // Vanilla already draws "ghost" bodies (invisible entities you can still see) with a translucent render type, so
    // ask for that branch for our player instead of choosing a render type ourselves. The translucent type's name
    // differs between Minecraft versions, but vanilla always picks the right one.
    @WrapOperation(
            method = "submit(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;getRenderType(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;ZZZ)Lnet/minecraft/client/renderer/rendertype/RenderType;")
    )
    private RenderType transparentplayer$forceTranslucent(LivingEntityRenderer<T, S, M> instance, S state, boolean isBodyVisible, boolean forceTransparent, boolean appearGlowing, Operation<RenderType> original) {
        if (isBodyVisible && PlayerOpacity.appliesTo(state)) {
            forceTransparent = true;
        }
        return original.call(instance, state, isBodyVisible, forceTransparent, appearGlowing);
    }

    // The model colour passed to submitModel is baseColor multiplied by the model tint. Replace its alpha with ours.
    @ModifyExpressionValue(
            method = "submit(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/ARGB;multiply(II)I")
    )
    private int transparentplayer$applyOpacity(int color, @Local(argsOnly = true) S state) {
        if (PlayerOpacity.appliesTo(state)) {
            return ARGB.color(Math.round(TransparentPlayerConfig.get().effectiveOpacity() * 255.0F), color);
        }
        return color;
    }
}

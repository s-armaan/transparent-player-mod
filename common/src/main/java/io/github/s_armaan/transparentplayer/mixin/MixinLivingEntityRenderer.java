package io.github.s_armaan.transparentplayer.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.s_armaan.transparentplayer.PlayerOpacity;
import io.github.s_armaan.transparentplayer.config.TransparentPlayerConfig;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntityRenderer.class)
public abstract class MixinLivingEntityRenderer<T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>> {

    @Shadow
    public abstract Identifier getTextureLocation(S state);

    // Vanilla already draws "ghost" bodies (invisible entities you can still see) with a translucent render type,
    // so reuse that type for our player instead of the opaque one.
    @ModifyReturnValue(method = "getRenderType", at = @At("RETURN"))
    private RenderType transparentplayer$translucentRenderType(RenderType original, S state, boolean isBodyVisible, boolean forceTransparent, boolean appearGlowing) {
        if (original != null && isBodyVisible && !forceTransparent && PlayerOpacity.appliesTo(state)) {
            return RenderTypes.entityTranslucentCullItemTarget(this.getTextureLocation(state));
        }
        return original;
    }

    // The model colour passed to submitModel is baseColor multiplied by the model tint. Scale its alpha.
    @ModifyExpressionValue(
            method = "submit(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/ARGB;multiply(II)I")
    )
    private int transparentplayer$applyOpacity(int color, @Local(argsOnly = true) S state) {
        if (PlayerOpacity.appliesTo(state)) {
            return ARGB.multiplyAlpha(color, TransparentPlayerConfig.get().effectiveOpacity());
        }
        return color;
    }
}

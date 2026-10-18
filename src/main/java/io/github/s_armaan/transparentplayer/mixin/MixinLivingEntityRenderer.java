package io.github.s_armaan.transparentplayer.mixin;

import io.github.s_armaan.transparentplayer.PlayerOpacity;
import io.github.s_armaan.transparentplayer.config.TransparentPlayerConfig;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public abstract class MixinLivingEntityRenderer<T extends LivingEntity, M extends EntityModel<T>> {

    // The entity being rendered, so the colour hook below knows whether to apply. Render calls are not re-entrant.
    @Unique
    private LivingEntity transparentplayer$current;

    @Inject(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At("HEAD"))
    private void transparentplayer$begin(T entity, float yaw, float partialTicks, com.mojang.blaze3d.vertex.PoseStack poseStack, net.minecraft.client.renderer.MultiBufferSource buffer, int light, CallbackInfo ci) {
        this.transparentplayer$current = entity;
    }

    @Inject(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At("RETURN"))
    private void transparentplayer$end(T entity, float yaw, float partialTicks, com.mojang.blaze3d.vertex.PoseStack poseStack, net.minecraft.client.renderer.MultiBufferSource buffer, int light, CallbackInfo ci) {
        this.transparentplayer$current = null;
    }

    // Vanilla draws "ghost" bodies (invisible entities you can still see) with the translucent render type, so use
    // the same one for our player.
    @Inject(method = "getRenderType", at = @At("RETURN"), cancellable = true)
    @SuppressWarnings("unchecked")
    private void transparentplayer$translucentRenderType(T entity, boolean isBodyVisible, boolean forceTransparent, boolean appearGlowing, CallbackInfoReturnable<RenderType> cir) {
        if (cir.getReturnValue() != null && isBodyVisible && !forceTransparent && PlayerOpacity.appliesTo(entity)) {
            LivingEntityRenderer<T, M> self = (LivingEntityRenderer<T, M>) (Object) this;
            cir.setReturnValue(RenderType.entityTranslucent(self.getTextureLocation(entity)));
        }
    }

    // The last argument of renderToBuffer is the alpha of the model colour.
    @ModifyArg(
            method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/EntityModel;renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;IIFFFF)V"),
            index = 7
    )
    private float transparentplayer$applyOpacity(float alpha) {
        if (PlayerOpacity.appliesTo(this.transparentplayer$current)) {
            return TransparentPlayerConfig.get().effectiveOpacity();
        }
        return alpha;
    }
}

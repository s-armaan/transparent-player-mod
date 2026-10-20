package io.github.s_armaan.transparentplayer.render;

import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.s_armaan.transparentplayer.PlayerOpacity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;

/** A buffer source whose consumers scale alpha by the configured opacity. */
public class TranslucentBufferSource implements MultiBufferSource {

    private final MultiBufferSource delegate;

    public TranslucentBufferSource(MultiBufferSource delegate) {
        this.delegate = delegate;
    }

    @Override
    public VertexConsumer getBuffer(RenderType renderType) {
        return new AlphaVertexConsumer(this.delegate.getBuffer(renderType), PlayerOpacity.opacity());
    }
}

package io.github.s_armaan.transparentplayer;

import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.util.ARGB;

import java.util.ArrayList;
import java.util.List;

/**
 * Rewrites the quads of an item that is being submitted so it draws translucent. An item quad bakes in its render
 * type (cutout or translucent sheet) and which tint it uses, so both are replaced with copies: the quad moves to the
 * translucent sheet, and every quad gets a tint entry whose alpha is the configured opacity.
 */
public class ItemFade {

    public record Faded(int[] tintLayers, List<BakedQuad> quads) {
    }

    public static Faded fade(int[] tintLayers, List<BakedQuad> quads) {
        int alpha = Math.round(PlayerOpacity.opacity() * 255.0F);
        int white = ARGB.color(alpha, 0xFFFFFF);

        // Make the tint array cover every index the quads use, then add one extra slot for untinted quads.
        int size = tintLayers.length;
        for (BakedQuad quad : quads) {
            size = Math.max(size, quad.materialInfo().tintIndex() + 1);
        }
        int[] tints = new int[size + 1];
        for (int i = 0; i < size; i++) {
            tints[i] = i < tintLayers.length ? ARGB.color(alpha, tintLayers[i]) : white;
        }
        tints[size] = white;

        List<BakedQuad> faded = new ArrayList<>(quads.size());
        for (BakedQuad quad : quads) {
            faded.add(fadeQuad(quad, size));
        }
        return new Faded(tints, faded);
    }

    private static BakedQuad fadeQuad(BakedQuad quad, int untintedSlot) {
        BakedQuad.MaterialInfo info = quad.materialInfo();
        RenderType renderType = info.itemRenderType();
        if (!renderType.hasBlending()) {
            renderType = info.sprite().atlasLocation().equals(TextureAtlas.LOCATION_BLOCKS)
                    ? Sheets.translucentBlockItemSheet()
                    : Sheets.translucentItemSheet();
        }
        int tintIndex = info.isTinted() ? info.tintIndex() : untintedSlot;
        BakedQuad.MaterialInfo fadedInfo = new BakedQuad.MaterialInfo(info.sprite(), info.layer(), renderType, tintIndex, info.shade(), info.lightEmission());
        return new BakedQuad(
                quad.position0(), quad.position1(), quad.position2(), quad.position3(),
                quad.packedUV0(), quad.packedUV1(), quad.packedUV2(), quad.packedUV3(),
                quad.direction(), fadedInfo);
    }
}

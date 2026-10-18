package io.github.s_armaan.transparentplayer;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;

public class TransparentPlayerKeys {

    public static final KeyMapping TOGGLE = new KeyMapping(
            "key." + Constants.MOD_ID + ".toggle", InputConstants.Type.KEYSYM, InputConstants.KEY_J, "key.categories." + Constants.MOD_ID);
}

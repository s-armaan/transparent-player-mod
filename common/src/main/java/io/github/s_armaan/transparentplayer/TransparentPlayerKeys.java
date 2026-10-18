package io.github.s_armaan.transparentplayer;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;

public class TransparentPlayerKeys {

    public static final Identifier CATEGORY_ID = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "main");

    public static KeyMapping toggle;

    /**
     * Creates the toggle keybind. The category has to be registered through the loader first, so each loader
     * calls this with the category it registered.
     */
    public static KeyMapping createToggle(KeyMapping.Category category) {
        toggle = new KeyMapping("key." + Constants.MOD_ID + ".toggle", InputConstants.Type.KEYSYM, InputConstants.KEY_J, category);
        return toggle;
    }
}

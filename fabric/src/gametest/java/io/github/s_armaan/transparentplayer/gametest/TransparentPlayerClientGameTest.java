package io.github.s_armaan.transparentplayer.gametest;

import io.github.s_armaan.transparentplayer.config.TransparentPlayerConfig;
import net.fabricmc.fabric.api.client.gametest.v1.FabricClientGameTest;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.fabricmc.fabric.api.client.gametest.v1.context.TestSingleplayerContext;
import net.minecraft.client.CameraType;

/** Takes screenshots of the player in third person with the effect off and on, for a visual check. */
public class TransparentPlayerClientGameTest implements FabricClientGameTest {

    @Override
    public void runTest(ClientGameTestContext context) {
        try (TestSingleplayerContext world = context.worldBuilder().create()) {
            context.runOnClient(client -> client.options.setCameraType(CameraType.THIRD_PERSON_FRONT));
            context.waitTicks(60);

            setConfig(false, 0.5F);
            context.waitTicks(5);
            context.takeScreenshot("player-effect-off");

            setConfig(true, 0.5F);
            context.waitTicks(5);
            context.takeScreenshot("player-effect-on-50");

            setConfig(true, 0.15F);
            context.waitTicks(5);
            context.takeScreenshot("player-effect-on-15");
        }
    }

    private static void setConfig(boolean enabled, float opacity) {
        TransparentPlayerConfig config = TransparentPlayerConfig.get();
        config.enabled = enabled;
        config.opacity = opacity;
    }
}

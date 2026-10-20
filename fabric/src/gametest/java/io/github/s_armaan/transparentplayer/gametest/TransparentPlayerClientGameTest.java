package io.github.s_armaan.transparentplayer.gametest;

import io.github.s_armaan.transparentplayer.config.TransparentPlayerConfig;
import net.fabricmc.fabric.api.client.gametest.v1.FabricClientGameTest;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.fabricmc.fabric.api.client.gametest.v1.context.TestSingleplayerContext;
import net.minecraft.client.CameraType;

/**
 * Takes screenshots of the player with the effect off and on, for a visual check. Each scene is shot with the effect
 * off, at 50% and at 15% opacity. Screenshots land in {@code build/run/clientGameTest/screenshots}.
 */
public class TransparentPlayerClientGameTest implements FabricClientGameTest {

    @Override
    public void runTest(ClientGameTestContext context) {
        try (TestSingleplayerContext world = context.worldBuilder().create()) {
            context.waitTicks(40);

            scene(context, "body", CameraType.THIRD_PERSON_FRONT);

            world.getServer().runCommand("item replace entity @p armor.head with minecraft:netherite_helmet");
            world.getServer().runCommand("item replace entity @p armor.chest with minecraft:netherite_chestplate");
            world.getServer().runCommand("item replace entity @p armor.legs with minecraft:netherite_leggings");
            world.getServer().runCommand("item replace entity @p armor.feet with minecraft:netherite_boots");
            world.getServer().runCommand("item replace entity @p weapon.mainhand with minecraft:diamond_sword");
            world.getServer().runCommand("item replace entity @p weapon.offhand with minecraft:shield");
            scene(context, "armor-and-items", CameraType.THIRD_PERSON_FRONT);

            world.getServer().runCommand("item replace entity @p armor.chest with minecraft:elytra");
            scene(context, "elytra", CameraType.THIRD_PERSON_BACK);

            world.getServer().runCommand("item replace entity @p armor.chest with minecraft:air");
            world.getServer().runCommand("item replace entity @p armor.legs with minecraft:air");
            world.getServer().runCommand("item replace entity @p armor.feet with minecraft:air");
            world.getServer().runCommand("item replace entity @p armor.head with minecraft:diamond_block");
            scene(context, "head-block", CameraType.THIRD_PERSON_FRONT);

            scene(context, "first-person-item", CameraType.FIRST_PERSON);

            world.getServer().runCommand("item replace entity @p weapon.mainhand with minecraft:air");
            scene(context, "first-person-arm", CameraType.FIRST_PERSON);
        }
    }

    private static void scene(ClientGameTestContext context, String name, CameraType camera) {
        context.runOnClient(client -> client.options.setCameraType(camera));
        context.waitTicks(20);

        setConfig(false, 0.5F);
        context.waitTicks(5);
        context.takeScreenshot(name + "-off");

        setConfig(true, 0.5F);
        context.waitTicks(5);
        context.takeScreenshot(name + "-on-50");

        setConfig(true, 0.15F);
        context.waitTicks(5);
        context.takeScreenshot(name + "-on-15");
    }

    private static void setConfig(boolean enabled, float opacity) {
        TransparentPlayerConfig config = TransparentPlayerConfig.get();
        config.enabled = enabled;
        config.opacity = opacity;
    }
}

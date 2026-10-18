package io.github.s_armaan.transparentplayer;

import io.github.s_armaan.transparentplayer.client.TransparentPlayerClient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class TransparentPlayerForge {

    public TransparentPlayerForge() {
        // Client-only mod: keep client classes off the dedicated server.
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> TransparentPlayerClient::init);
    }
}

package io.github.s_armaan.transparentplayer.platform;

import io.github.s_armaan.transparentplayer.Constants;
import io.github.s_armaan.transparentplayer.platform.services.IPlatformHelper;

import java.util.ServiceLoader;

// Common code cannot call loader APIs directly, so loader-specific behaviour sits behind interfaces that are
// looked up with Java's ServiceLoader. Each loader module registers its implementation in META-INF/services.
public class Services {

    public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);

    public static <T> T load(Class<T> clazz) {

        final T loadedService = ServiceLoader.load(clazz, Services.class.getClassLoader())
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        Constants.LOG.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }
}

package io.github.s_armaan.transparentplayer.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.s_armaan.transparentplayer.Constants;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Plain JSON config kept by common code so neither loader needs its own config API.
 * Edit {@code config/transparentplayer.json} and restart (or toggle in game) to change it.
 */
public class TransparentPlayerConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String FILE_NAME = Constants.MOD_ID + ".json";

    private static TransparentPlayerConfig instance = new TransparentPlayerConfig();
    private static Path path;

    /** Master switch, also flipped by the toggle keybind. */
    public boolean enabled = true;
    /** 0.0 is fully invisible, 1.0 is fully opaque. */
    public float opacity = 0.5F;
    /** Apply the effect to the local player. */
    public boolean applyToSelf = true;

    public static TransparentPlayerConfig get() {
        return instance;
    }

    /** Opacity clamped to the valid range, safe to hand straight to the renderer. */
    public float effectiveOpacity() {
        return Math.max(0.0F, Math.min(1.0F, opacity));
    }

    /** Reads the config from {@code configDir} (creating it with defaults if needed) and remembers it for {@link #save()}. */
    public static void load(Path configDir) {
        path = configDir.resolve(FILE_NAME);
        if (Files.exists(path)) {
            try (Reader reader = Files.newBufferedReader(path)) {
                TransparentPlayerConfig loaded = GSON.fromJson(reader, TransparentPlayerConfig.class);
                if (loaded != null) {
                    instance = loaded;
                }
            } catch (IOException | RuntimeException e) {
                Constants.LOG.warn("Could not read {}, using defaults", path, e);
            }
        }
        // Write back so new fields appear in older files and the file exists for the player to edit.
        save();
    }

    public static void save() {
        if (path == null) {
            return;
        }
        try {
            Files.createDirectories(path.getParent());
            try (Writer writer = Files.newBufferedWriter(path)) {
                GSON.toJson(instance, writer);
            }
        } catch (IOException e) {
            Constants.LOG.warn("Could not write {}", path, e);
        }
    }
}

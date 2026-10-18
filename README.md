# Transparent Player (Minecraft 1.20.1, Forge)

A client-side Minecraft mod that renders your own player model translucent, with a configurable opacity and a
keybind to toggle it.

This is the **1.20.1 / Forge** branch. The Fabric and NeoForge versions for Minecraft 26.2+ live on `main`. The two
lines use different toolchains and renderer internals, so they are kept on separate branches. Loader-neutral code
(`config/TransparentPlayerConfig.java`, `Constants.java`) is kept identical between them so fixes can be ported by
hand.

| Minecraft | Loader | Java |
|---|---|---|
| 1.20.1 | Forge 47.x | 17 |

## Usage

- Press **J** (rebindable under *Transparent Player* in the controls screen) to toggle the effect.
- The config lives in `config/transparentplayer.json`:

  | Key | Default | Meaning |
  |---|---|---|
  | `enabled` | `true` | Master switch (also flipped by the keybind) |
  | `opacity` | `0.5` | `0.0` invisible to `1.0` opaque |
  | `applyToSelf` | `true` | Apply the effect to your own player |

### Known limitations

- Only the player body is made translucent. Armor, held items, the cape and other render layers are still opaque.
- The first-person arm is not affected.
- The ground shadow under the player is not faded.

## Building

Requires JDK 17.

```
./gradlew build
```

The jar is written to `build/libs`. Use `./gradlew runClient` to start a development client.

## License

MIT, see [LICENSE](LICENSE).

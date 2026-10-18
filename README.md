# Transparent Player

A client-side Minecraft mod that renders your own player model translucent, with a configurable opacity and a
keybind to toggle it.

## Branches

| Branch | Minecraft | Loaders | Java |
|---|---|---|---|
| `main` | 26.2+ (built against 26.2, smoke-test 26.3) | Fabric, NeoForge | 25 |
| `1.20.1` | 1.20.1 | Forge | 17 |

The two lines use different toolchains and renderer internals, so they are kept on separate branches. Loader-neutral
code (config model, opacity logic) is kept identical between them so fixes can be ported by hand.

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

## Building (`main`)

Requires JDK 25.

```
./gradlew build
```

Jars are written to `fabric/build/libs` and `neoforge/build/libs`. Use `./gradlew :fabric:runClient` or
`./gradlew :neoforge:runClient` to start a development client.

## Layout (`main`)

```
common/    vanilla-only code: Mixin, config, opacity logic, platform interfaces
fabric/    fabric.mod.json, client entry point, Fabric platform implementation
neoforge/  neoforge.mods.toml, client entry point, NeoForge platform implementation
```

Loader differences go through interfaces in `common` that are found with Java's `ServiceLoader`
(see `platform/Services.java`).

## License

MIT, see [LICENSE](LICENSE).

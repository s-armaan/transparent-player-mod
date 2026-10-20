# Transparent Player

A client-side Minecraft mod that renders your own player model translucent, with a configurable opacity and a
keybind to toggle it.

## Branches

| Branch | Minecraft | Loaders | Java |
|---|---|---|---|
| `main` | 26.2 | Fabric, NeoForge | 25 |
| `26.3` | 26.3 | Fabric, NeoForge | 25 |
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
  | `applyToLayers` | `true` | Also fade armor, cape, elytra and the shield on the player model |
  | `applyToFirstPerson` | `true` | Also fade the first-person arm |

### Known limitations

- Items held in the hand or worn on the head (swords, blocks, skulls) are not faded yet.
- The cape has not been checked in game (the development client has no cape skin).
- The ground shadow under the player is not faded.

## Building (`main`)

Requires JDK 25.

```
./gradlew build
```

Jars are written to `fabric/build/libs` and `neoforge/build/libs`. Use `./gradlew :fabric:runClient` or
`./gradlew :neoforge:runClient` to start a development client.

`./gradlew :fabric:runClientGameTest` creates a world and saves screenshots of the player with the effect off and on
to `fabric/build/run/clientGameTest/screenshots`.

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

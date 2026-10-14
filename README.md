# DinoWorld

DinoWorld is a minimal Fabric mod demo for Minecraft. The mod builds as a client-only mod and renders the text `DinoWorld` in the HUD.

## Requirements

- Java 25+
- Gradle 9.7.1 (via the included wrapper)
- Minecraft 26.3
- Fabric Loader 0.19.5+
- Fabric API 0.162.0+26.3

## Build

```bash
./gradlew build
```

This generates the mod JAR in `build/libs/`.

## Run locally

```bash
./gradlew runClient
```

This launches a Fabric client with the mod enabled.

## Install

Copy the built JAR from `build/libs/` into your Minecraft `mods` directory and launch the Fabric client.

## Modpack

```bash
./gradlew modpack
```

This builds `build/distributions/dinoworld-<version>.mrpack`, a Modrinth-format modpack containing DinoWorld and Fabric API. In Prism Launcher, use **Add Instance → Import from zip** and pick the file, or paste a direct download URL to it. Prism sets up the instance with the Minecraft and Fabric Loader versions from `gradle.properties` automatically.

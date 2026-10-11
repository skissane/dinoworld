# DinoWorld

DinoWorld is a minimal Fabric mod demo for Minecraft. The mod builds as a client-only mod and renders the text `DinoWorld` in the HUD.

## Requirements

- Java 17+
- Gradle 8.7+
- Minecraft 1.21.1
- Fabric Loader 0.16.10+

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

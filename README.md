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

## Auto-updating install (packwiz + GitHub Releases)

```bash
./gradlew packwizRelease
```

This writes `pack.toml`, `index.toml` and the DinoWorld and Fabric API jars to `build/packwiz-release/`. CI attaches these to the GitHub Release it creates when you push a `v*` tag, so releasing a new version is just:

```bash
git tag v1.2.0 && git push origin v1.2.0
```

or run the **Tag Release** workflow (Actions tab → Run workflow, or `gh workflow run tag-release.yml -f bump=patch`), which picks `patch`, `minor` or `major` and tags the tip of `main` with the next version. It needs a repository secret `RELEASE_TOKEN` (a fine-grained PAT or GitHub App token with `contents: write`), because a tag pushed with the default `GITHUB_TOKEN` doesn't trigger the CI release.

One-time setup in Prism Launcher: **Add Instance → Import from zip**, paste

```
https://github.com/skissane/dinoworld/releases/latest/download/dinoworld-prism.zip
```

and click OK. That zip (built by `./gradlew prismInstance`) is a Minecraft 26.3 + Fabric Loader instance with `packwiz-installer-bootstrap.jar` in its `minecraft` folder and this pre-launch command already set:

```
"$INST_JAVA" -jar packwiz-installer-bootstrap.jar https://github.com/skissane/dinoworld/releases/latest/download/pack.toml
```

Each launch then installs whatever the latest release contains, and removes jars from older versions.

The installer also updates the instance's Minecraft and Fabric Loader versions to match `pack.toml`, so you don't need to change the instance when you bump `minecraft_version` or `loader_version`. `latest` skips drafts and pre-releases, so a release marked as a pre-release won't reach the instance.

# FeverVisuals 0.1.0

Neon Purple client-side visual mod for **Minecraft Java 1.21.11 + Fabric**.

## Controls
- `Right Shift` — open/close FeverVisuals GUI.
- Click a module card to toggle it.

## Included in v0.1.0
- Neon Purple click GUI
- FeverVisuals watermark
- Enabled-module ArrayList
- FPS HUD
- Coordinates HUD
- Ping HUD
- Fullbright (client gamma; restores the previous gamma value)
- Module states saved in `config/fevervisuals.json`

The first release is intentionally a stable visual foundation. Features such as player ESP, tracers, nametags, storage ESP and chams are not represented as finished toggles until their render implementations are added and tested.

## Build with GitHub Actions (no local JDK required)
1. Create a new GitHub repository.
2. Upload the **contents** of this folder to the repository (not the outer folder itself).
3. Open the **Actions** tab.
4. Select **Build FeverVisuals** and click **Run workflow**, or push to `main`.
5. Wait for the workflow to finish successfully.
6. Open the completed workflow run, find **Artifacts**, and download `FeverVisuals-1.21.11`.
7. Put the built `FeverVisuals-0.1.0.jar` into `.minecraft/mods`.

Fabric API is required in the Minecraft installation.

## Versions
- Minecraft: 1.21.11
- Fabric Loader: 0.19.5
- Fabric API: 0.141.6+1.21.11
- Java target: 21
- Fabric Loom: 1.14-SNAPSHOT

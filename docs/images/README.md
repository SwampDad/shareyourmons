# Screenshot provenance

Public images in this directory and the build they represent.

| File | Shows | Source | Image SHA-256 |
| --- | --- | --- | --- |
| `shareyourmons-v0.3.1.png` | Full 0.3.1 collection view with the version label `ShareYourMons · v0.3.1` | Owner-approved 0.3.1 screenshot | `647b47a68e21ada9757797149ab5d100fda697cd668ceaea547cebcf25296cda` |
| `shareyourmons-filters.png` | Shallow full-width crop of the 0.3.1 title and filter controls | Crop of `shareyourmons-v0.3.1.png` | `fbd4381bd0a212c52fecbaa51e42f45adca4806fb4cbe92bbb98bff0b6055d19` |
| `shareyourmons-collection.png` | Tall, narrow crop of the 0.3.1 collection results | Crop of `shareyourmons-v0.3.1.png` | `925eb7101638714ae6c6a66f9bf282b9f5b1be3a7c945b8432c2f45b064d6c31` |
| `shareyourmons-logo.png` | Project logo supplied by the owner | Owner-supplied project image | `a9fa5854019c949a7f21cc45fd53b069a80fb9564548da5e7354e9cb067a34ef` |
| `install/01-curseforge-modpack-menu.png` | CurseForge My Modpacks with the three-dot modpack menu highlighted | Owner-supplied 2026-10-26 screenshot | `dee0d72ce72e98683e57b0c9cc98682f97b85b69298d20f2117d5f31d7d8a36a` |
| `install/04-curseforge-open-folder.png` | CurseForge profile menu with Open Folder highlighted | Owner-supplied 2026-10-26 screenshot | `4c24a14da68e8f332fb2098e97bc926703cf3b46c3280a625082c169a8dbc837` |
| `install/05-swamp-mods-menu.png` | Minecraft Escape menu with the Swamp Mods entry | Owner-supplied 2026-10-26 screenshot | `277174fd8d83a0b2b065a4d85908cbec6c1ec095f6b8afa933707667e6544ce8` |
| `install/06-shareyourmons-menu.png` | Minecraft Escape menu with the direct ShareYourMons entry | Owner-supplied 2026-10-26 screenshot | `9d20f93d1598ece7893455635f5e14734c25a6709e8028463029a70f26232a1a` |

The 0.3.1 screenshot was captured from the owner-approved 0.3.1 build. The filter and
collection crops use the same image at native pixel scale; neither was stretched or
resampled.

## Rules for new screenshots

1. Capture from the exact JAR that will be published. Do not use a development, test, or
   different-version build.
2. Do not edit the image to hide or change the version label, player data, or UI state.
3. Inspect the image for chat, player names, coordinates, server-private UI, and unrelated
   desktop content before committing it.
4. Keep Minecraft pixel text legible. Crop or resize proportionally; never stretch.
5. Record the source image, version, artifact SHA-256, image SHA-256, and capture date.
6. Use the current stable screenshot in the README and store listing.
7. Keep release-candidate screenshots out of the public README unless the image itself is
   clearly labeled as release-candidate evidence.

The release procedure is in [release/CHECKLIST.md](../../release/CHECKLIST.md).

# ShareYourMons

See what’s in your Cobblemon PC, find your special Pokémon, and share your collection without sorting through every box.

ShareYourMons is a client-side Minecraft mod I made for the Cobblemon Town community. It brings your PC collection into one organized view, making it easier to find custom skins, shinies, and Legendary or Mythical Pokémon.

You can browse the results in-game or copy a text report to your clipboard.

ShareYourMons was previously called Collection Exporter.

## What you can do

* Find your special Pokémon. Filter for custom skins, Legendary and Mythical Pokémon, and shinies.
* Browse everything in one place. View an organized, scrollable report instead of checking each PC box individually.
* See where Pokémon are stored. Turn on PC locations to show box and slot numbers.
* Share your collection. Copy a readable report and paste it wherever you choose.
* Refresh when your collection changes. Update the report without restarting Minecraft.
* Check anything the mod couldn’t identify. Unknown or conflicting skin information is kept separate rather than guessed.

The mod works with the PC information Cobblemon has already synchronized to your game. It does not change your Pokémon.

## Requirements

ShareYourMons 0.3.1 is built for the following setup:

| Component | Requirement |
| --- | --- |
| Minecraft | 1.21.1 |
| Fabric Loader | 0.18.0 or newer |
| Fabric API | 0.116.7+1.21.1 or newer |
| Cobblemon | 1.8.2 |
| Java | 21 or newer |
| Installation | Client only |

These are the supported target versions. Other Minecraft versions, newer Cobblemon releases, and other modpacks have not been established as compatible.

You do not need PC Plus or PC Exporter.

## Installation

1. Install the Cobblemon Town modpack and its required dependencies.
2. Download one ShareYourMons JAR from the [GitHub Releases page](https://github.com/SwampDad/shareyourmons/releases).
3. Place that JAR in your Minecraft mods folder.
4. Start Minecraft.
5. Open the Escape menu and select ShareYourMons.

If multiple compatible Swamp Mods are installed, you may see a Swamp Mods selector instead.

## Which file should I download?

For version 0.3.1, the release includes three filenames:

| File | Purpose |
| --- | --- |
| shareyourmons-0.3.1.jar | Version-specific download |
| shareyourmons.jar | Stable download filename |
| collection-exporter.jar | Compatibility filename from the old project name |

All three contain the same mod. Install only one.

[Download the latest ShareYourMons JAR](https://github.com/SwampDad/shareyourmons/releases/latest/download/shareyourmons.jar)

The internal Fabric mod ID is still collection-exporter. That is intentional, so the rename does not create a second mod identity.

## How does it recognize custom skins?

This turned out to be more complicated than I expected.

Cobblemon exposes information about a Pokémon’s species, form, and internal appearance markers, but those markers do not always match the names players recognize.

For example, the skin collection called Neon uses the internal marker tron2026.

I compared community information, the Cobblemon Town wiki, observed game data, and the game’s own definitions to build a carefully reviewed list of skin markers.

ShareYourMons only labels something as a custom skin when it recognizes a confirmed marker. If something is unfamiliar, it keeps that information visible for review instead of making a guess.

I wrote about the investigation, including the mistakes and corrections, in [How ShareYourMons Recognizes Cobblemon Town Skins](docs/research/how-shareyourmons-recognizes-skins.md).

## Privacy and limitations

ShareYourMons reads the PC data available to your Minecraft client.

It does not:

* Send your collection to a website or external service.
* Collect telemetry.
* Modify Pokémon or PC storage.
* Run server commands or require a server-side installation.
* Export files automatically.

The clipboard feature only copies a report when you choose to use it.

The skin catalog is based on reviewed information available during development. It is not an official or exhaustive list, and new server content may require an update.

If your PC information is incomplete or unavailable, the report may be limited.

## Questions or problems?

Use [GitHub Issues](https://github.com/SwampDad/shareyourmons/issues).

Include the ShareYourMons version, what happened, and any relevant species, form, or appearance marker.

Please do not post full PC exports, private player information, or other players’ collection data.

## For developers

This is a Fabric project built with Java 21.

Run the tests:

```sh
./gradlew test
```

Build the mod:

```sh
./gradlew build
```

The generated JAR is written to build/libs/.

For implementation details, see the [classification reference](docs/classification.md).

## Documentation

* [How ShareYourMons Recognizes Cobblemon Town Skins](docs/research/how-shareyourmons-recognizes-skins.md)
* [Classification and developer reference](docs/classification.md)
* [Documentation index](docs/README.md)
* [Release history and checksums](release/README.md)
* [Attribution and notices](NOTICE.md)

## Independence and license

ShareYourMons is an independent community project. It is not an official Cobblemon or Cobblemon Town release and is not endorsed by either project.

ShareYourMons is released under the MIT License. Third-party projects and materials retain their own licenses.

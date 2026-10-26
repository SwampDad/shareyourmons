# Classification and Developer Reference

This document explains how ShareYourMons reads a collection, identifies special categories, and builds its report.

For the investigation behind the skin catalog, see [How ShareYourMons Recognizes Cobblemon Town Skins](research/how-shareyourmons-recognizes-skins.md).

## Scope

ShareYourMons is a client-side Fabric mod for the Cobblemon Town Minecraft 1.21.1 environment.

It reads PC information already synchronized to the Minecraft client, classifies the Pokémon it can observe, and presents a local report.

It does not mutate storage, issue server commands, collect telemetry, or send collection data to an external service.

## Collection data

The live reader obtains Pokémon information from Cobblemon’s client-side PC storage.

Relevant fields include:

* Species identifier and display name.
* Form.
* Appearance aspects.
* Shiny status.
* Level, gender, and nickname where available.
* PC box and slot.
* Pokémon UUID, used internally for record identity.

The live reader is separate from the offline snapshot adapter used for development and testing.

The report distinguishes complete, partial, and unavailable PC observations. When the available storage is ambiguous, the mod does not pretend it has a complete collection.

## Classification

Each observed Pokémon is checked against the bundled classification catalog.

The central rule is exact matching against confirmed skin markers.

The process is:

1. Read the Pokémon’s species, form, and aspects.
2. Ignore neutral markers such as gender and shiny status for skin identification.
3. Exclude known non-skin markers from custom-skin classification.
4. Compare the remaining markers with the confirmed skin catalog.
5. Resolve exactly one confirmed skin marker to its family.
6. Report multiple confirmed skin markers as a conflict.
7. Preserve unfamiliar markers as unresolved information.
8. Classify species categories independently.

Unfamiliar markers are never promoted to confirmed skins solely because they contain a year or resemble a known naming pattern.

This is intentional: false positives make a collection report less trustworthy.

### Evidence levels

The catalog separates four levels:

* Source evidence: a public wiki page or server-owner/staff announcement documents server
  content.
* Implemented recognition: ShareYourMons contains an exact catalog rule or identifier for it.
* Automated validation: tests confirm that the implementation handles the documented token
  or path.
* Live validation: an actual client collection report confirms the result in-game.

Server-owner acceptance of a skin progression is source evidence. It does not by itself
prove that the live client supplies the expected identifier or that runtime recognition
succeeds. Unverified classifications remain unresolved.

ShareYourMons independently interprets documented information and maps it to identifiers
exposed by Cobblemon. It is not an official server registry, and a wiki entry or
announcement does not guarantee current availability.

## The bundled catalog

The public classification catalog is stored at:

src/main/resources/assets/collection-exporter/classification-catalog.json

It contains the reviewed information used by the released mod.

For the current version, that includes:

| Catalog content | Count |
| --- | --- |
| Confirmed skin families | 10 |
| Known non-skin aspect markers | 54 |
| Base species | 1,025 |
| Legendary species | 71 |
| Mythical species | 23 |
| Paradox species | 20 |
| Ultra Beasts | 11 |
| Observed custom species | 8 |
| Documented species entries | 10 (8 expected IDs, 2 observed) |
| Documented regional/alternate forms without mapped runtime tokens | 5 |

These counts describe the bundled catalog, not the total number of skins or Pokémon currently available on Cobblemon Town. The Legendary/Mythical/Paradox/Ultra Beast counts are the standard Cobblemon 1.8.2 lists; documented custom-species category mappings are recorded separately in `documentedSpecies` and are added at classification time.

The catalog is version-specific. It should not be treated as an official or exhaustive server registry. The full evidence audit is in [Classification Evidence Audit — 2026-10-25](classification-audit-2026-10-25.md).

### Authoritative historical skin progression

As of 2026-10-24, server ownership accepts this nine-set historical progression and its
order. The runtime tokens are exact and case-sensitive.

| Order | Collection | Runtime token | Public introduction |
| ---: | --- | --- | --- |
| 1 | Mario | `mario2026` | 2026-03-04 owner announcement |
| 2 | Adventure | `adventure2026` | 2026-03-08 owner announcement |
| 3 | Petal | `petal2026` | 2026-04-12 owner announcement |
| 4 | Relic | `relic2026` | 2026-05-14 owner announcement |
| 5 | Neon | `tron2026` | 2026-06-11 owner announcement |
| 6 | Kimono | `july2026kimono` | 2026-07-15 owner announcement |
| 7 | Wild West | `august2026western` | 2026-08-11 owner announcement |
| 8 | Crystal | `september2026crystall` | 2026-09-14 owner announcement |
| 9 | Spooky | `october2026spooky` | 2026-10-13 owner announcement |

Easter (`easter2026`) is a separately evidenced event offering, not part of the nine-set
progression. It remains in the bundled catalog at its existing report position so the
established display behavior does not change.

Owner acceptance of identity and order is not proof of live recognition. Recognition is
implemented by exact aspect matching and covered by synthetic tests; a live collection
report remains the separate runtime check.

## Species categories

Legendary, Mythical, Paradox, and Ultra Beast labels are derived from the reviewed Cobblemon 1.8.2 species data.

These categories are independent of custom-skin identification.

Custom species are also tracked separately. A species being custom does not automatically make it Legendary, Mythical, or part of a skin family.

## Building the report

The report engine classifies the available PC records and organizes them for display.

The visible filters are:

* All.
* Custom Skins.
* Legend & Mythic.
* Shiny.

The filters operate as a union: a Pokémon matching any enabled filter is included.

By default, Custom Skins, Legend & Mythic, and Shiny are enabled; All is disabled.

Each Pokémon appears in one primary report section, using this order:

1. Custom Skins.
2. Legendary & Mythical.
3. Shiny.
4. Other.

Skin families follow their configured report order. The report groups duplicate entries and can optionally include PC box and slot locations.

Unresolved markers and conflicting skin classifications are evaluated across the observed PC, not just the Pokémon selected by the visible filters.

That prevents an unfamiliar marker from disappearing simply because its Pokémon is outside the current view.

## Privacy and clipboard behavior

ShareYourMons reads the client-visible PC state without modifying it.

It has no collection-upload service, telemetry system, or automatic file export.

Copying a report uses Minecraft’s local clipboard integration. The player decides whether and where to paste that report.

Issue reports should avoid complete PC snapshots, player identifiers, or private server information.

## Source map

The main implementation areas are:

| Component | Responsibility |
| --- | --- |
| CobblemonPcInventoryReader | Read client-visible PC records |
| PcStoreSelection | Select the appropriate PC store and identify ambiguous states |
| ClassificationCatalogLoader | Load and validate the bundled catalog |
| ClassificationService | Apply skin and category rules |
| DocumentedSpecies | Preserve expected IDs, documented categories, evolution relationships, and unknowns |
| DocumentedForm | Preserve regional/alternate-form relationships without inventing form tokens |
| AspectHeuristics | Flag potentially interesting unknown markers |
| ReportEngine | Filter, group, and summarize classified records |
| ReportTextFormatter | Generate the readable text report |
| ReportViewBuilder | Build the in-game report view |
| ExportController | Refresh, rebuild, and copy |
| MinecraftClipboardService | Write text to the local clipboard |
| SwampModsLauncher | Register the shared Swamp Mods launcher entry |
| BuildIdentity | Load generated version, source fingerprint, catalog hash, schema, and timestamp |
| ClassificationDiagnostic | Preserve per-instance development diagnostics without locations |

The internal Fabric mod ID remains collection-exporter for compatibility with the original project identity.

## Building and testing

Use Java 21 or newer.

Run tests:

```sh
./gradlew test
```

Build:

```sh
./gradlew build
```

The public checkout includes an automated test suite. It covers classification, exclusions,
conflicts, filters, report behavior, storage selection, interface layout, build identity,
and related components.

Record test results for the exact build being released. These are automated code tests, not
proof of complete live-server coverage.

## Development build identity

Development versions use `0.3.2-dev.N+<source-fingerprint-prefix>`. The build generates
`assets/collection-exporter/build-info.properties` with the version, source fingerprint,
catalog version, catalog SHA-256, catalog schema, build timestamp, and development flag.
It also writes `build/build-manifest.json` with the external JAR SHA-256 and the same
provenance. The JAR hash is not embedded in the JAR.

The source fingerprint is a deterministic hash of the relevant main/test source and build
files. It is not a Git commit claim. Public releases use the simple version and keep detailed
provenance in the embedded build identity and external build manifest.

Release candidates and public releases use the simple version without a `-dev` suffix.
They show `ShareYourMons · v<version>` in both the screen footer and the copied report, and
they do not expose the development diagnostics control. Only versions containing `-dev`
add the catalog/schema provenance to the footer and enable the `Dev Diagnostics` toggle.

Development builds expose a `Dev Diagnostics` toggle in the export screen. The copied report
then includes a separate diagnostic section with raw species ID, display name, form, aspects,
matched catalog entry, categories, mapping status, evidence, and unresolved aspects. It never
includes PC locations or player identity. Ordinary reports only show the compact build footer.

## Updating the catalog

The released repository includes the catalog consumed by ShareYourMons.

The research and regeneration process also used private gameplay observations, reviewed source material, and internal research tools. Those inputs and tools are not bundled as a public regeneration workflow.

Do not assume that the catalog can be regenerated from the public repository alone.

Any future catalog update should preserve the distinction between confirmed skins, known non-skin markers, unresolved observations, and independently classified species categories.

## Limitations

* Only the supported Minecraft 1.21.1 and Cobblemon 1.8.2 target environment has been established.
* The catalog is not exhaustive or automatically updated.
* Client PC data can be partial or unavailable.
* Some custom species do not have established standard category labels.
* Skin collection membership does not establish current availability.
* Automated tests do not replace live compatibility testing.

For third-party attribution, see [NOTICE](../NOTICE.md).

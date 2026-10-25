# Classification update — 2026-10-24

**Status:** implemented and synthetically validated; owner in-game validation pending.
**Build:** `shareyourmons-0.3.2-dev.2+acac2b3472f0.jar` (unreleased development build).
**Scope:** classification catalog, regression tests, and documentation. No release, upload,
or public publication.

This update separates four evidence levels:

1. **Source-supported** — an official announcement or public wiki page establishes a name,
   token, or relationship.
2. **Implemented recognition** — the mod contains an exact rule or catalog entry for it.
3. **Synthetic test success** — an automated test proves the implementation handles the
   documented token or path.
4. **Actual in-game recognition** — a real collection report confirms the same result in the
   owner's client. This update does **not** claim this level for any new item.

**Correction:** an earlier pass classified Gearex and Tyranogear as Mythical from an older
wiki stub title. The current wiki, the stub body, and the shared #1172 index do not establish
that category. They are now explicitly unresolved rather than Mythical.

The full follow-up audit is in
[Classification Evidence Audit — 2026-10-25](classification-audit-2026-10-25.md). It also
found and corrected the missing Iron Lamp Paradox classification and preserved Seraphang's
conflicting Legend/Mythical evidence as unresolved.

## 1. Authoritative skin progression

Server ownership accepted the following nine-set historical progression on 2026-10-24.
The order and runtime tokens are exact.

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

The catalog marks these nine families `evidenceStatus: accepted`. Their existing
`reportOrder` values are preserved. Easter (`easter2026`) remains `separate-event` and
keeps its existing report position; it is not part of the nine-set progression.

Owner acceptance of identity and order does not prove that a runtime data path works.
That distinction is why the Mario/Adventure tests below exist.

### Catalog delta

| Collection | Runtime token | Previous evidence status | New evidence status | Recognition changed? |
| --- | --- | --- | --- | --- |
| Mario | `mario2026` | candidate | accepted | No; exact-match path was already present |
| Adventure | `adventure2026` | candidate | accepted | No; exact-match path was already present |
| Easter | `easter2026` | candidate | separate-event | No; retained at existing report position |
| Petal | `petal2026` | reviewed | accepted | No |
| Relic | `relic2026` | reviewed | accepted | No |
| Neon | `tron2026` | proposed | accepted | No |
| Kimono | `july2026kimono` | reviewed | accepted | No |
| Wild West | `august2026western` | reviewed | accepted | No |
| Crystal | `september2026crystall` | reviewed | accepted | No |
| Spooky | `october2026spooky` | proposed | accepted | No |

No skin token, display name, alias, or report order changed. The update records owner
authority and adds regression coverage; it does not rewrite classification behavior.

## 2. Mario and Adventure recognition

### Evidence

- Mario is supported by the 2026-03-04 owner announcement of the first skin collection.
- Adventure is supported by the 2026-03-08 owner announcement of 14 adventure-themed skins.
- Historical observed evidence includes `mario2026` on Tyrunt and `adventure2026` on Eevee
  and Torterra. The Adventure Zoroark relationship is separately supported by the previous
  server-resource-pack and owner-report investigation; `ranked` remains a non-skin marker.
- Raw market, shop, and PC records remain internal research material and are not copied into
  this repository.

### Implemented path

`PokemonRecord.aspects` → `ClassificationService` exact aspect match → `SkinFamily` →
`ReportEngine` grouping → `ReportTextFormatter` / `ReportViewBuilder`.

Recognition is species-independent. The catalog resolves a family from the aspect token, not
from a species allowlist. The tests use both historically observed species and unrelated
species to make that property explicit.

### Regression coverage

| Test | Path exercised | Result |
| --- | --- | --- |
| `ClassificationCatalogTest.nineSetSkinProgressionIsOwnerAcceptedInExactOrder` | Catalog statuses, tokens, and order | Pass |
| `ClassificationServiceTest.mario2026ResolvesOnRecordedAndUnrelatedSpecies` | Mario on Tyrunt, Charmander, Eevee | Pass |
| `ClassificationServiceTest.adventure2026ResolvesOnRecordedSpeciesAndKeepsMarkersNonSkin` | Adventure on Eevee, Torterra, Zoroark; `tree-none`/`ranked` stay non-skin | Pass |
| `ReportEngineTest.authoritativeSkinProgressionOrderIsPreserved` | Default skin filtering and family order | Pass |
| `ReportTextFormatterTest.marioAndAdventureAppearInClipboardOutput` | Clipboard report labels and sections | Pass |
| `ReportViewBuilderTest.viewerShowsMarioAndAdventureAsSkinFamilies` | Native viewer headings and entries | Pass |

The synthetic tests prove the documented tokens resolve consistently. They do not prove that
the owner's live PC contains those tokens or that the live client supplies aspects exactly as
expected.

### Documented metadata coverage

| Test | Path exercised | Result |
| --- | --- | --- |
| `ClassificationCatalogTest.documentedSpeciesPreserveExpectedIdsAndUnknownOrDocumentedCategories` | Expected IDs, unresolved category status, `customSpeciesObserved` separation | Pass |
| `ClassificationCatalogTest.documentedFormsRemainUnmappedUntilRuntimeEvidenceExists` | Five documented forms with null runtime tokens | Pass |
| `ClassificationServiceTest.documentedCustomSpeciesUseExpectedIdsWithoutInventingCategories` | Sweetclust expected ID → custom only; other categories stay unset | Pass |
| `ClassificationServiceTest.unresolvedDocumentedCategoriesStayUnknownInsteadOfBecomingMythical` | Gearex/Tyranogear remain custom only | Pass |
| `ReportEngineTest.unresolvedDocumentedCategoriesDoNotAppearInLegendMythSection` | Unresolved categories do not appear under Legend & Mythic | Pass |
| `ReportEngineTest.allFilterKeepsDocumentedCustomSpeciesWithUnknownCategory` | All filter preserves Sweetclust and Gearex as custom, with no Mythical count | Pass |


## 3. Newly announced Pokémon and forms

Source: the 2026-10-21 staff announcement plus the corresponding public wiki pages. The
announcement introduced new custom species, Archelian regional forms, and two Ogerpon forms.

The catalog now distinguishes four things that were previously collapsed:

- **Documented identity** — the name, evolution relationship, and wiki page.
- **Documented classification attributes** — categories such as Mythical, when a source
  establishes them. Unknown attributes are preserved as unknown.
- **Expected runtime identifier** — a name-derived `cobblemon:<slug>` identifier marked
  `expected`, not observed.
- **Observed runtime recognition** — an actual client/PC observation. None is claimed here.

| Name | Documented identity | Expected runtime ID | Documented classification | Catalog action |
| --- | --- | --- | --- | --- |
| Sweetclust | Custom fakemon; Luvdisc evolution; wiki #4052 | `cobblemon:sweetclust` (expected) | Custom; other categories unknown | Added documented metadata; generic custom species path |
| Symphoneo | Custom fakemon; Chatot evolution; wiki #4056 | `cobblemon:symphoneo` (expected) | Custom; other categories unknown | Added documented metadata; generic custom species path |
| Venobra | Custom fakemon; wiki #4055 | `cobblemon:venobra` (expected) | Custom; other categories unknown | Added documented metadata; generic custom species path |
| Gearex | Custom fakemon; evolves into Tyranogear; wiki #4053 | `cobblemon:gearex` (expected) | Custom; Legendary/Mythical/Paradox/Ultra Beast unresolved | Added documented metadata; no category promotion |
| Tyranogear | Custom fakemon; evolves from Gearex; wiki #4054 | `cobblemon:tyranogear` (expected) | Custom; Legendary/Mythical/Paradox/Ultra Beast unresolved | Added documented metadata; no category promotion |
| Lameyser | Custom fakemon; Archelian Camerupt evolution; wiki #4029 | `cobblemon:lameyser` (expected) | Custom; other categories unknown | Added documented metadata; generic custom species path |
| Drimpact | Custom fakemon; Archelian Dwebble evolution; wiki #4030 | `cobblemon:drimpact` (expected) | Custom; other categories unknown | Added documented metadata; generic custom species path |
| Queentana | Custom fakemon; female Pawniard evolution; wiki #4034 | `cobblemon:queentana` (expected) | Custom; other categories unknown | Added documented metadata; generic custom species path |
| Archelian Numel | Regional form of known base `cobblemon:numel` | Form/aspect **unknown** | Base species; form unmapped | Added documented form metadata |
| Archelian Camerupt | Regional form of known base `cobblemon:camerupt` | Form/aspect **unknown** | Base species; form unmapped | Added documented form metadata |
| Archelian Dwebble | Regional form of known base `cobblemon:dwebble` | Form/aspect **unknown** | Base species; form unmapped | Added documented form metadata |
| Ogerpon Thunder Mask | Alternate form of `cobblemon:ogerpon` | Mask aspect **unknown** | Base species is Legendary; form unmapped | Added documented form metadata |
| Ogerpon Windstorm Mask | Alternate form of `cobblemon:ogerpon` | Mask aspect **unknown** | Base species is Legendary; form unmapped | Added documented form metadata |

### Classifier fields and evidence coverage

| Existing field | Populated from Discord/wiki in this update | Unresolved or not applicable |
| --- | --- | --- |
| `CUSTOM_SPECIES` | All eight documented fakemon | None; expected IDs and generic non-base fallback |
| `LEGENDARY` | None new | Ogerpon base already standard Legendary; no new legendary claim |
| `MYTHICAL` | None new | Gearex/Tyranogear older title was not sufficient; all eight custom species remain unresolved for this category |
| `PARADOX` | None new | No source label |
| `ULTRA_BEAST` | None new | No source label |
| Regional/alternate form | Archelian Numel/Camerupt/Dwebble; Ogerpon Thunder/Windstorm | Runtime form/aspect tokens unknown |
| Evolution relationship | Seven documented relationships | Venobra has no documented evolution relationship |
| Skin family | None new | New names are not skins |
| Species-level rarity | Not supported by the classifier | Do not invent a rarity field |
| Observed runtime recognition | None | Owner in-game test only |

### Outcome summary

| Outcome | Items |
| --- | --- |
| Already supported generically | Any non-base custom species via the custom/unknown fallback; Archelian forms of known base species; Ogerpon as a base Legendary |
| Documented custom species added with expected runtime IDs | Sweetclust, Symphoneo, Venobra, Gearex, Tyranogear, Lameyser, Drimpact, Queentana |
| Documented category mappings implemented | None for the 10/21 batch; the later audit added Iron Lamp as a custom Paradox elsewhere in the catalog |
| Documented forms retained without mapping | Archelian Numel/Camerupt/Dwebble; Ogerpon Thunder/Windstorm masks |
| Observed runtime recognition | None claimed |

### What is supported

- Any runtime species absent from the Cobblemon base-species list is marked as a
  custom/unknown species. This does not require a prior observation.
- The eight documented fakemon have expected species IDs. If the live identifier matches,
  they are recognized as custom.
- Gearex, Tyranogear, and the other six 10/21 species have no sufficiently established
  Legendary/Mythical/Paradox/Ultra Beast label. Their category status is `unresolved`, not
  silently ordinary and not promoted to Mythical from a title alone.
- The separate full-catalog audit added Iron Lamp as a documented custom Paradox and kept
  Seraphang's conflicting Legend/Mythical evidence unresolved.
- Archelian forms and Ogerpon masks preserve the base species and documented form
  relationship, but their form/aspect tokens remain unmapped.
- `customSpeciesObserved` is unchanged: it remains a snapshot-derived observation set.
- No species-level rarity field is implemented. Do not invent one.

### Why Gearex/Tyranogear are not classified Mythical

An older wiki capture rendered the stubs “#1172 - Gearex (Mythical)” and
“#1173 - Tyranogear (Mythical)” and the parser extracted `Mythical` from the title. That is
not sufficient evidence:

- The current wiki #4053/#4054 pages do not state Mythical or any other species category.
- The older pages were stubs; no body prose, stat block, or acquisition text established the
  classification.
- The old #1172 number is shared with Regifighting, which the wiki labels Legend/Legendary.
  The numbering collision makes the old title unreliable as a classification source.
- The 2026-10-21 staff announcement introduces the names but does not label either as
  Mythical.

The catalog now records `categoryStatus: unresolved` and no categories for Gearex and
Tyranogear. This is a deliberate correction, not a runtime failure.

### Not included

- `Lumin Tail` is a Past Paradox Pokémon, not part of the 2026-10-21 batch. It was not added
  or reclassified.
- No new skin family was created from any announcement name.
- No runtime form/aspect token was invented for the new regional or Ogerpon forms.
- No name-based promotion was added. Expected IDs are matched exactly; a mismatch remains a
  preserved custom/unknown species.

## 4. Development build identity and diagnostics

Development versions use the form `0.3.2-dev.2+<source-fingerprint-prefix>`. The full
source fingerprint is a deterministic SHA-256 of the relevant source and build files; this
worktree is not treated as a Git commit authority, so the manifest records `gitCommit: null`.

| Identity field | Current value |
| --- | --- |
| Mod version | `0.3.2-dev.2+acac2b3472f0` |
| Source fingerprint | `acac2b3472f095e93dab30742bb1a00d0cee26485dd8fe9a42d340b4831c680e` |
| Catalog version | `2026-10-22T09:05:37.446242+00:00` |
| Catalog SHA-256 | `16a856365679c861a12fec71354c70dbe65ee750d01641a3846d7d381724023c` |
| Catalog schema | `1` |
| Build timestamp | `2026-10-25T10:18:33.596816Z` |

The generated `assets/collection-exporter/build-info.properties` is embedded in the JAR.
`build/build-manifest.json` records the external JAR SHA-256. The JAR hash is not embedded
in the JAR itself. The shared Swamp Mods launcher source is vendored under
`src/main/java/com/swampmods/launcher`; the public checkout builds without a sibling
workbench directory.

Build identity is visible in:
1. Fabric mod metadata (`version`).
2. The report footer (development builds include a short catalog hash and schema; public
   releases use a simple version).
3. Startup diagnostic logging.
4. The external manifest.

Development builds expose a `Dev Diagnostics` checkbox. When enabled, the copied report
contains a separate `DEVELOPMENT DIAGNOSTICS` section with raw species ID, display name,
form, aspects, catalog match, categories, mapping status, evidence, and unresolved aspects.
It does not include PC locations or player identity. Ordinary reports remain player-facing
and contain only the compact footer.

## 5. Validation

Commands run in the authoritative public checkout:

```sh
JAVA_HOME=/opt/homebrew/opt/openjdk ./gradlew clean test build --console=plain
```

Result:

- 146 tests, 0 failures, 0 errors, 0 skipped.
- Build successful.
- Artifact: `build/libs/shareyourmons-0.3.2-dev.2+acac2b3472f0.jar`
- SHA-256: `d48a1580ab58ec52caae9537956c7b5937f6e353c6e759e100f6e648bc4ec4b4`
- Source fingerprint: `acac2b3472f095e93dab30742bb1a00d0cee26485dd8fe9a42d340b4831c680e`
- Catalog SHA-256: `16a856365679c861a12fec71354c70dbe65ee750d01641a3846d7d381724023c`
- Catalog schema: `1`
- Fabric metadata version: `0.3.2-dev.2+acac2b3472f0`
- External manifest: `build/build-manifest.json`
- Catalog embedded in the JAR contains the accepted statuses and existing report order.

No live Minecraft client was launched, no owner collection was inspected, and no runtime
recognition claim is made.

Build identity and diagnostic coverage:

| Test | Verification |
| --- | --- |
| `BuildIdentityTest.generatedBuildIdentityIsDevelopmentAndComplete` | Development version, fingerprints, schema, timestamp, footer |
| `BuildIdentityTest.fabricMetadataUsesTheSameGeneratedVersion` | Fabric metadata matches generated build identity |
| `BuildIdentityTest.embeddedCatalogHashMatchesTheBundledCatalog` | Embedded catalog SHA-256 matches the built catalog |
| `BuildIdentityTest.catalogContentChangesProduceDifferentFingerprints` | Different catalog bytes produce different hashes |
| `BuildIdentityTest.sourceContentChangesProduceDifferentFingerprints` | Relevant source changes alter the source fingerprint |
| `BuildIdentityTest.publicBuildFooterStaysSimple` | Public release footer omits development provenance |
| `ReportTextFormatterTest.developmentDiagnosticsExposeRawIdentifiersAndBuildProvenance` | Raw IDs/aspects, categories, mapping, evidence, footer |
| `ReportTextFormatterTest.diagnosticsAreAbsentFromOrdinaryReports` | Diagnostics stay out of ordinary reports |

## 6. Owner testing instructions

Do not disclose the expected Pokémon or expected skin families before the test. The point is
to compare an independent observation with the evidence-driven implementation.

1. Close Minecraft completely.
2. Preserve the existing ShareYourMons JAR rather than deleting it.
3. Install only `shareyourmons-0.3.2-dev.2+acac2b3472f0.jar` from `build/libs/`.
4. Confirm the installed file's SHA-256 matches the value above.
5. Launch the normal Cobblemon Town profile and open ShareYourMons from the Escape menu.
6. Preserve the version footer
   (`ShareYourMons · v0.3.2-dev.2+acac2b3472f0 · catalog 16a856365679 · schema 1`).
7. With PC locations off, copy the report with `All` enabled and the default
   `Custom Skins` / `Legend & Mythic` / `Shiny` selection.
8. Enable `Dev Diagnostics` and copy a second report. Save both reports.
9. Save a client log excerpt containing the `ShareYourMons initialized` build line.
10. Preserve any `UNRESOLVED` section verbatim, including aspect tokens.
11. For any new custom species or form, preserve its displayed species, form, and aspects if
    the report exposes them. Do not edit the catalog to match the result.
12. Do not post the full report or PC information publicly. The independent comparison is
    the owner's, not a public release artifact.

This build is not a release and has not been accepted as a runtime success. A successful
compile and synthetic test run only establish the implementation and evidence levels above.

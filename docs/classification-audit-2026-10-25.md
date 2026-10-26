# Classification evidence audit — 2026-10-25

**Status:** completed evidence review; corrections implemented; runtime recognition remains
unverified. This report covers the entire bundled classification catalog, not only the
recently announced species.

**Starting baseline:** `shareyourmons-0.3.2-dev.2+f350c0dd316e` (catalog
`77b52c599bb8fb3921b0303f14a91483e8acb52d1fe246a2a3999b9509ef9cc2`).

**Final audited build:** `shareyourmons-0.3.2-dev.2+acac2b3472f0`, SHA-256
`d48a1580ab58ec52caae9537956c7b5937f6e353c6e759e100f6e648bc4ec4b4`, catalog SHA-256
`16a856365679c861a12fec71354c70dbe65ee750d01641a3846d7d381724023c`, 146 tests.

## 1. Scope and method

The audit treated each catalog record as one of:

- standard Cobblemon species category membership;
- custom skin family;
- documented species identity and category;
- observed custom species;
- documented form;
- non-skin aspect exclusion;
- neutral aspect.

Standard categories were checked against the installed Cobblemon 1.8.2 species data, not
against a copied list. The JAR SHA-256 matched the catalog provenance:
`cae8ac328b61354fbe415208b8c6ffc12e7155094d0a87082e85e7914fb380c2`.

Evidence reviewed:

- Cobblemon 1.8.2 JAR `data/cobblemon/species/**/*.json` (1,025 species).
- Current and older Cobblemon Town wiki captures, including page bodies, titles, URL slugs,
  stub status, and Wiki index entries.
- Discord owner/staff announcements and the Discord archive navigation record.
- Historical PC snapshot / observed custom-species list.
- Reviewed skin registry and the accepted nine-set owner progression.
- Existing classification audit and avatar research records.

## 2. Counts audited

| Surface | Records |
| --- | ---: |
| Legendary memberships | 71 |
| Mythical memberships | 23 |
| Paradox memberships | 20 |
| Ultra Beast memberships | 11 |
| Skin families | 10 |
| Observed custom species | 8 |
| Documented species entries | 10 |
| Documented forms | 5 |
| Non-skin aspect exclusions | 54 |
| Neutral aspects | 4 |
| **Total catalog records audited** | **216** |

The observed/documented-species overlap (Iron Lamp and Seraphang) is counted in both
surfaces because the records serve different roles.

## 3. Results

### Standard Cobblemon categories — verified

All 125 standard category memberships match the Cobblemon 1.8.2 `labels` data exactly:

- Legendary 71/71, no missing or extra IDs.
- Mythical 23/23, no missing or extra IDs.
- Paradox 20/20, no missing or extra IDs.
- Ultra Beast 11/11, no missing or extra IDs.
- All 125 IDs are unique, are in `baseSpecies`, and have no cross-category duplicate.
- Every entry's display name and National Dex number matched its species JSON.

These are standard Cobblemon classifications, not server availability claims.

### Skin families — verified against owner evidence

Ten families are present, with no duplicate aspect, alias-as-family, or non-skin overlap.
The nine-set progression is owner-accepted and in order. Easter is `separate-event` and is
not part of the nine-set progression. Exact tokens, including `mario2026`,
`adventure2026`, `tron2026`, and `september2026crystall`, are preserved.

### Custom species and forms

- Eight announced custom species have expected IDs but no observed IDs.
- Iron Lamp and Seraphang are observed custom species; their documented status was audited.
- Five documented forms have valid base species but no runtime form/aspect token.
- No documented ID collides with a base species.
- No custom species is automatically placed in Legendary, Mythical, Paradox, or Ultra Beast.

### Corrections

| Species | Previous | New | Evidence |
| --- | --- | --- | --- |
| Gearex | `MYTHICAL` | category unresolved; `CUSTOM_SPECIES` only | Older wiki stub title/slug only; current #4053 page has no label; old #1172 collides with Regifighting |
| Tyranogear | `MYTHICAL` | category unresolved; `CUSTOM_SPECIES` only | Older wiki stub title/slug only; current #4054 page has no label |
| Iron Lamp | no category | `PARADOX` plus `CUSTOM_SPECIES` | Wiki page title “The Paradox Pokémon”, Future Paradox page category, and observed `cobblemon:ironlamp`; 2026-08-24 announcement describes it as a Paradox Dimension design addition |
| Seraphang | no category | category unresolved/conflicting; `CUSTOM_SPECIES` only | Wiki title “Seraphang (Legend)” and habitat “Legendary Spawns” conflict with body prose “mythical serpent”; no category is inferred |

The Gearex/Tyranogear correction was accepted in the preceding review. This audit confirms
it and records the same evidence handling for Seraphang.

### Intentionally unresolved

- Gearex, Tyranogear, Sweetclust, Symphoneo, Venobra, Lameyser, Drimpact, Queentana:
  Legendary/Mythical/Paradox/Ultra Beast categories remain unresolved unless separately
  documented.
- Seraphang: conflicting Legend/Legendary/Mythical evidence; no category applied.
- Archelian Numel/Camerupt/Dwebble and Ogerpon Thunder/Windstorm forms: runtime form/aspect
  tokens remain unknown.
- All expected custom-species IDs remain unobserved.
- No species-level rarity field is used or invented.

## 4. Implementation findings

The classification path was traced through `ClassificationCatalogLoader`,
`ClassificationCatalog`, `ClassificationService`, `ReportEngine`, `ReportTextFormatter`,
`ReportViewBuilder`, and the UI filters.

- Category matching is exact on `speciesId`; it does not use display names, URL slugs, or
  evolution relationships.
- Documented category mappings are keyed by exact expected species ID. A different runtime
  ID stays a preserved custom/unknown species.
- `customSpeciesObserved` is not an allowlist and is not consulted for category inheritance.
- Forms remain separate from species identity; no form inherits a category merely from its
  base species.
- There is no name-based fallback and no silent default to a special category.
- Standard category counts and the report's category counts are derived from the same loaded
  catalog; malformed or duplicate entries fail in the loader tests.
- **Fixed:** `ReportCounts` previously returned zero for Paradox and Ultra Beast even though
  the report summary counted them. It now carries and returns both counts. Paradox/Ultra
  Beast remain internal categories without visible filters in the approved screen; a
  non-shiny custom Paradox such as Iron Lamp appears under `All` and contributes correctly
  to the category count.

## 5. Regression coverage added

| Test | Coverage |
| --- | --- |
| `ClassificationCatalogTest.documentedSpeciesPreserveExpectedIdsAndUnknownOrDocumentedCategories` | Expected IDs, unresolved/conflicting status, no category inheritance, base-species separation |
| `ClassificationCatalogTest.documentedFormsRemainUnmappedUntilRuntimeEvidenceExists` | Forms do not become species categories |
| `ClassificationServiceTest.observedCustomParadoxResolvesFromDocumentedCategory` | Iron Lamp → custom + Paradox |
| `ClassificationServiceTest.conflictingCustomCategoryIsNotPromoted` | Seraphang stays custom; no Legend/Mythic promotion |
| `ClassificationServiceTest.unresolvedDocumentedCategoriesStayUnknownInsteadOfBecomingMythical` | Gearex/Tyranogear remain custom only |
| `ReportEngineTest.customParadoxIsCountedAndReachableByParadoxFilter` | All/Paradox filter and counts |
| `ReportEngineTest.unresolvedDocumentedCategoriesDoNotAppearInLegendMythSection` | Unresolved categories stay out of Legend & Mythic |
| `ReportEngineTest.allFilterKeepsDocumentedCustomSpeciesWithUnknownCategory` | Unknown categories preserved without false Mythical count |

Additional invariants checked:
- Gearex/Tyranogear must remain custom-only and must not appear under Legend & Mythic.
- Documented species must not collide with base species.
- Custom species are not automatically Legendary or Mythical.
- Standard category lists remain exactly 71/23/20/11.
- Existing skin order, Easter separation, Mario/Adventure recognition, unknown-ID
  preservation, and build/catalog identity tests remain intact.
- `ReportCounts.countFor(PARADOX)` returns the actual Paradox count; Ultra Beast remains
  zero when no Ultra Beast is present.

## 6. Final validation

- 146 tests, 0 failures, 0 errors, 0 skipped.
- Artifact `build/libs/shareyourmons-0.3.2-dev.2+acac2b3472f0.jar`.
- Artifact SHA-256 `d48a1580ab58ec52caae9537956c7b5937f6e353c6e759e100f6e648bc4ec4b4`.
- Source fingerprint `acac2b3472f095e93dab30742bb1a00d0cee26485dd8fe9a42d340b4831c680e`.
- Catalog SHA-256 `16a856365679c861a12fec71354c70dbe65ee750d01641a3846d7d381724023c`.
- `verifyBuildInfo` passed; external manifest matches the final JAR.

## 7. Remaining limitations

- The audit verifies catalog-to-source consistency, not live server behavior.
- Wiki evidence can be wrong or stale; conflicting pages remain unresolved.
- Custom species identities are expected by convention until a live export confirms them.
- Form/aspect tokens for regional and Ogerpon forms are not established.
- No owner collection was inspected, and no in-game recognition claim is made.
- Paradox/Ultra Beast visibility is intentionally outside the current UI approval.

# Changelog

## 0.3.2-dev.2 — unreleased

### Changed

* Recorded the nine-set historical skin progression as owner-accepted, preserving the
  exact runtime tokens and existing report order. Easter remains a separate event offering.
* Added regression coverage for Mario and Adventure recognition across recorded and
  unrelated species, plus clipboard and native-view output.
* Added documented species metadata with expected runtime IDs for eight announced fakemon.
  Gearex/Tyranogear remain category-unresolved; an older stub title was not sufficient
  evidence for Mythical.
* Preserved five regional/alternate forms as documented but unmapped until runtime form or
  aspect tokens are established. No runtime identifiers were invented.
* Added generated development build identity: source fingerprint, catalog SHA-256, catalog
  schema, build timestamp, mod metadata, report footer, startup log, and external manifest.
* Added a development diagnostic report section with raw identifiers, evidence, mapping
  status, categories, and unresolved aspects, without PC locations or player identity.
* Completed a full classification-catalog audit. Standard category lists match Cobblemon
  1.8.2 exactly; Gearex/Tyranogear remain unresolved; Iron Lamp is classified as a custom
  Paradox; Seraphang remains category-conflicting. No category is inferred from titles alone.
* Fixed filter counts so Paradox and Ultra Beast counts no longer silently return zero.
* Vendored the canonical Swamp Mods launcher source so the public checkout builds without a
  sibling workbench directory.

No release was published from this development build.

## 0.3.1 — 2026-10-23

### Changed

* Renamed the player-facing project from Collection Exporter to ShareYourMons.
* Updated the in-game title, launcher label, and version footer to use the new name.
* Retained the internal Fabric mod ID collection-exporter for compatibility.

No intentional functional changes were made in this release.

## 0.3.0 — 2026-10-22

### Added

* Client-side Cobblemon PC collection viewer.
* Custom skin, Legendary/Mythical, and Shiny filtering.
* Scrollable report with grouped collection sections.
* Optional PC box and slot locations.
* Local clipboard reporting.
* PC refresh and report summaries.
* Escape-menu integration.

## Historical tester build

0.3.0-dev.1 was an earlier tester release. It is retained in the release history but is not the recommended download.

# Changelog

A history of ShareYourMons updates.

For planned features, see [ROADMAP.md](ROADMAP.md).

## Latest Stable Release

### 0.3.1 — October 23, 2026

Collection Exporter becomes ShareYourMons.

* Updated the project name and in-game interface branding.
* Updated the launcher label and version footer.
* Preserved compatibility with the original internal mod identity.

This was primarily a branding release.

### 0.3.0 — October 22, 2026

The first stable collection viewer.

* Browse your Cobblemon PC in one organized view.
* Find custom skins, Legendary and Mythical Pokémon, and shinies.
* View optional PC box and slot locations.
* Copy collection reports to your clipboard.
* Refresh the report in-game.

## In Development — Not Yet Released

### 0.3.2

Work completed or under validation includes:

* Expanded documentation and verification of custom-skin classifications.
* Improved handling of custom Pokémon with uncertain classifications.
* Additional tests for skin recognition and report behavior.
* Improvements to classification counts and development diagnostics.
* Release and build-verification improvements.

These changes are not part of the current stable download until a new release is approved.

#### Technical development record

##### 0.3.2-rc.1 — release candidate, not released

This candidate packages the 0.3.2 changes with release-candidate presentation. The screen
and copied-report footer show `ShareYourMons · v0.3.2-rc.1`; the `Dev Diagnostics` control
and development footer provenance are absent because the build is not marked as a
development build.

The candidate is uncommitted, has no tag or GitHub release, and has not completed owner
runtime acceptance. The current public stable release remains 0.3.1. Do not link this
candidate as a public download.

##### 0.3.2-dev.2 — unreleased

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

## Historical Tester Build

0.3.0-dev.1 was an earlier tester release. It is retained in the release history but is not
the recommended download.

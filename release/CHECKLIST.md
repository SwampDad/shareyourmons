# Release checklist

Repeatable path from development to public distribution. This is a template. Do not mark a
step complete without recording the evidence, and do not publish an artifact that skipped a
step.

The single source of truth for the version is `mod_version` in `gradle.properties`. The
Fabric metadata version, generated build info, screen label, and report footer all derive
from that value and `archives_base_name`. Do not edit generated files or hand-edit a version
into a JAR.

## 1. Authorize and freeze

- [ ] Owner authorizes the release and the public version string.
- [ ] The approved version is a stable release version, not a `-rc` or `-dev` candidate.
- [ ] The intended changes are committed. Record the full commit:
      `git rev-parse HEAD`.
- [ ] `git status --short` contains no unintended tracked changes.
- [ ] The working tree contains no private player data, research material, diagnostic
      tooling, local paths, or unrelated mods.
- [ ] `mod_version` is the approved public version. Do not include `-dev` or an
      unreviewed release-candidate suffix in a final stable version.
- [ ] `archives_base_name` is `shareyourmons`.

## 2. Build from the public checkout

Build from this repository only. Do not build a public release through a workbench script
or a sibling directory.

```sh
./gradlew clean test build
```

Record:

- [ ] Test result count (expected: all pass, zero failures, errors, or skips).
- [ ] Build result.
- [ ] Commit SHA.
- [ ] `gradle.properties` version.
- [ ] Artifact filename under `build/libs/`.
- [ ] Artifact SHA-256.
- [ ] `build/build-manifest.json` values: version, artifact SHA-256, source fingerprint,
      catalog SHA-256, catalog schema, build timestamp.
- [ ] Embedded `fabric.mod.json` version and mod ID.
- [ ] Embedded `assets/collection-exporter/build-info.properties` version and
      `developmentBuild` value.

`build/build-manifest.json` currently records `gitCommit: null`; the commit SHA must be
recorded in the release notes or release record as an external fact.

## 3. Verify artifact identity

- [ ] The artifact filename matches the approved stable version.
- [ ] The embedded version matches the filename and `gradle.properties`.
- [ ] The artifact is a normal remapped runtime JAR, not a `-dev`, `-rc`, or sources JAR.
- [ ] The artifact is the approved stable artifact, not a release candidate.
- [ ] The JAR contains no Cobblemon, Fabric API, PC Plus, or unrelated bundled classes.
- [ ] The recorded SHA-256 was computed after the final build and no later command replaced
      the artifact.

## 4. Runtime validation

- [ ] Minecraft is closed before installation.
- [ ] The exact target instance is identified from its own current client log and game
      directory.
- [ ] Exactly one ShareYourMons JAR is active; the previous JAR is preserved for rollback.
- [ ] The installed file matches the verified SHA-256 and embedded metadata.
- [ ] Open the Escape menu and confirm the launcher path (direct entry or Swamp Mods
      selector).
- [ ] Confirm the screen title and bottom-right version label.
- [ ] Confirm category filters, collapsible sections, scrolling, Show PC Locations,
      Refresh, Copy to Clipboard, and Back/Escape.
- [ ] Confirm the copied report footer matches the screen version label.
- [ ] Confirm a complete, partial, and unavailable PC state is presented truthfully when
      each state can be produced.
- [ ] Confirm coexistence with the other installed Swamp Mods at representative GUI scales.
- [ ] Record the tested profile, Minecraft/Cobblemon versions, date, and observed result.
- [ ] Owner explicitly accepts the runtime result for this exact artifact.

Runtime validation is a separate evidence level. A successful build and unit tests do not
prove in-game behavior.

## 5. Screenshots

- [ ] Capture screenshots from the exact accepted public release artifact. Do not use
      another build and do not edit the version label.
- [ ] Confirm the visible version matches the release artifact.
- [ ] Inspect the image for chat, player names, coordinates, private UI, and unrelated
      desktop content.
- [ ] Save as `docs/images/shareyourmons-v<version>.png`.
- [ ] Record the image, image SHA-256, source version, artifact SHA-256, commit, and
      capture date in [docs/images/README.md](../docs/images/README.md).
- [ ] Use the current stable screenshot in the public README and store listing.

## 6. Release notes and checksum record

- [ ] Add `release/RELEASE-NOTES-v<version>.md`.
- [ ] State the version, date, supported Minecraft/Cobblemon/Fabric/Java target, changes,
      installation, known limitations, and verification level.
- [ ] Include the artifact SHA-256 and commit SHA.
- [ ] Add `release/SHA256SUMS-v<version>.txt`.
- [ ] Update `release/README.md` and `CHANGELOG.md`.
- [ ] Do not describe a release candidate as a stable release.

## 7. Tag and publish

- [ ] Create tag `shareyourmons-v<version>` at the recorded commit.
- [ ] Publish a GitHub Release from that tag.
- [ ] Attach the versioned runtime JAR.
- [ ] For a stable release, attach a byte-identical alias named exactly `shareyourmons.jar`.
- [ ] Keep development/test builds as prereleases. `/releases/latest` excludes
      prereleases.
- [ ] Verify the versioned JAR and stable alias have the same SHA-256.
- [ ] Verify
      `https://github.com/SwampDad/shareyourmons/releases/latest/download/shareyourmons.jar`
      resolves to the intended stable asset.
- [ ] Confirm no release-candidate JAR is published as stable or used as the stable alias.
- [ ] Verify the release page lists the intended assets and no accidental files.

Do not attach `-dev` or `-rc` JARs, source archives, private research, or screenshots from
another build to a stable release.

## 8. CurseForge distribution

- [ ] Upload the same verified JAR; do not rebuild for CurseForge.
- [ ] Select Minecraft 1.21.1 and the Fabric loader.
- [ ] Set the supported Cobblemon version in the description, not as a false dependency.
- [ ] Use the current stable README wording and the matching screenshot.
- [ ] Link the GitHub repository and releases page.
- [ ] Confirm the uploaded file version matches the release artifact and checksum.
- [ ] Do not mark the project or file as released until the owner approves publication.

## 9. Post-publication checks

- [ ] Recheck the public release asset checksums.
- [ ] Recheck
      `https://github.com/SwampDad/shareyourmons/releases/latest/download/shareyourmons.jar`
      and confirm it resolves to the intended stable artifact.
- [ ] Confirm the README, release notes, and screenshot all show the same version.
- [ ] Confirm the published JAR is the tested JAR.
- [ ] Confirm no release-candidate artifact is publicly advertised as stable.
- [ ] Record the released commit, version, artifact SHA-256, tag, and publication date in
      the work log and release documentation.

## Current gaps to close before the next release

- The 0.3.2-rc.1 source and version changes are uncommitted.
- Owner runtime acceptance of the release candidate is pending.
- No public screenshot exists for the 0.3.2 line.
- `build/build-manifest.json` records a source fingerprint but not a Git commit.
- No automated CI currently runs the build, tests, checksum verification, or release-asset
  verification.
- The CurseForge project has been submitted and is awaiting approval; the public listing
  has not been verified as accessible.

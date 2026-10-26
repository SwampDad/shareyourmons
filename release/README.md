# ShareYourMons Releases

## Current Stable Release: 0.3.1

Published October 23, 2026. Tag: `shareyourmons-v0.3.1`.

This is the current public stable release.

### Permanent download

[Download ShareYourMons](https://github.com/SwampDad/shareyourmons/releases/latest/download/shareyourmons.jar)

This link always points to the current stable `shareyourmons.jar` asset. Every future stable
release must keep that asset name so the link remains valid.

The 0.3.1 release also includes `shareyourmons-0.3.1.jar` and the legacy compatibility
filename `collection-exporter.jar`. All three contain the same mod. Install only one.

### Supported environment

| Component | Supported target |
| --- | --- |
| Minecraft | 1.21.1 |
| Mod loader | Fabric |
| Fabric Loader | 0.18.0 or newer |
| Fabric API | 0.116.7+1.21.1 or newer |
| Cobblemon | 1.8.2 |
| Java | 21 or newer |
| Modpack | Cobblemon Optimized: Your Pokémon Journey |
| Installation | Client only |

Other Minecraft versions, Cobblemon versions, and modpacks have not been verified as
compatible.

### Release notes

See [ShareYourMons 0.3.1](RELEASE-NOTES-v0.3.1.md).

### Checksums

All three 0.3.1 JARs have this SHA-256 checksum:

```text
1ea387524f34dcf26ff2951d327e87170199bdbd61d53d2cfe5a545bf745fa7d
```

The repository also contains [SHA256SUMS-v0.3.1.txt](SHA256SUMS-v0.3.1.txt). That checksum
record is stored in the repository and is not currently attached as a separate asset to the
GitHub release.

Checked 2026-10-25: all three published 0.3.1 assets report the same SHA-256 above, and the
permanent download link resolves to the intended release asset.

## Previous Releases

### 0.3.0 — October 22, 2026

Collection Exporter 0.3.0 was the stable release before the ShareYourMons rename.

Its original release assets and checksum record remain available.

* [0.3.0 release notes](RELEASE-NOTES-v0.3.0.md)
* [0.3.0 checksums](SHA256SUMS-v0.3.0.txt)

### 0.3.0-dev.1 — Historical tester build

This was an earlier tester release, not the recommended download.

* [Tester release notes](RELEASE-NOTES-v0.3.0-dev.1.md)
* [Tester checksum](SHA256SUMS-v0.3.0-dev.1.txt)

## Maintainer Information

### Unreleased release candidate

`0.3.2-rc.1` is a local release candidate. It is uncommitted, has no Git tag or GitHub
release, and has not completed owner runtime acceptance. It is not a public download.

The current public stable release remains 0.3.1.

### Release process

Use [CHECKLIST.md](CHECKLIST.md) for the repeatable development → release-candidate →
verified-release → public-distribution path. The checklist is a procedure, not a record that
a release has been performed.

### Build from source

Use Java 21 or newer.

```sh
./gradlew test
./gradlew build
```

Build outputs are generated in `build/libs/`. Release binaries are distributed through
GitHub Releases rather than stored in the current source tree.

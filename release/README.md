# ShareYourMons Releases

The current stable release is ShareYourMons 0.3.1.

Download it from [GitHub Releases](https://github.com/SwampDad/shareyourmons/releases).

## Current release: 0.3.1

* Published: October 23, 2026.
* Tag: shareyourmons-v0.3.1.
* Minecraft: 1.21.1.
* Cobblemon: 1.8.2.
* Installation: client only.

### Download files

| File | Purpose |
| --- | --- |
| shareyourmons-0.3.1.jar | Versioned download |
| shareyourmons.jar | Stable filename |
| collection-exporter.jar | Legacy compatibility filename |

All three files contain the same mod.

Install only one.

[Download the stable JAR](https://github.com/SwampDad/shareyourmons/releases/latest/download/shareyourmons.jar)

## SHA-256

All three 0.3.1 JARs have this SHA-256 checksum:

```text
1ea387524f34dcf26ff2951d327e87170199bdbd61d53d2cfe5a545bf745fa7d
```

The repository also contains [SHA256SUMS-v0.3.1.txt](SHA256SUMS-v0.3.1.txt).

That checksum record is stored in the repository; it is not currently attached as a separate asset to the GitHub release.

See [0.3.1 release notes](RELEASE-NOTES-v0.3.1.md).

## Previous stable release: 0.3.0

Collection Exporter 0.3.0 was the stable release before the ShareYourMons rename.

Its original release assets and checksum record remain available.

* [0.3.0 release notes](RELEASE-NOTES-v0.3.0.md)
* [0.3.0 checksums](SHA256SUMS-v0.3.0.txt)

## Historical tester build: 0.3.0-dev.1

This was an earlier tester release, not the recommended download.

* [Tester release notes](RELEASE-NOTES-v0.3.0-dev.1.md)
* [Tester checksum](SHA256SUMS-v0.3.0-dev.1.txt)

## Build from source

Use Java 21 or newer.

```sh
./gradlew test
./gradlew build
```

Build outputs are generated in build/libs/. Release binaries are distributed through GitHub Releases rather than stored in the current source tree.

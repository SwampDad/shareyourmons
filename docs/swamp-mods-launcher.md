# Embedded Swamp Mods launcher contract

The canonical development source is
`minecraft-mods/shared/swamp-mods-launcher/src/main/java`. Each standalone public repository
vendors an exact copy under `src/main/java/com/swampmods/launcher` and builds from that copy;
a sibling workbench directory is not required for a public checkout build. The coordinated
build verifies that the two embedded `com.swampmods.launcher` packages remain byte-identical. Each participating Swamp Mod calls this API from its client
initializer:

```java
SwampModsLauncher.register(
    new SwampModDescriptor("my-mod", "My Mod", "One-line description", 200),
    parent -> new MySettingsScreen(parent));
```

The descriptor's `modId` must be the installed Fabric ID and is the uniqueness key. The
factory must be lazy, return a screen whose parent is the supplied `parent`, and must not
perform feature work while registering. Lower `order` values sort first; ties sort by name
then mod ID.

The vendored source copies must be kept byte-identical to the canonical development source.
The coordinated build script byte-compares every embedded launcher class before accepting the
pair. Fabric's shared game class loader then provides one registry and one Game Menu listener.
There is no required core JAR and no feature-mod dependency. A duplicate registration is
rejected and logged. Factory failures are logged; other registered entries and a feature's
own settings access remain available.

The launcher shows the only installed entry directly. With two or more entries it shows
`Swamp Mods` and preserves the established selector and Back navigation. It filters against
Fabric's loaded mod IDs, deduplicates by ID, and removes/re-adds its one button per menu
screen so initialization order cannot create duplicate buttons. Future entries beyond one
screen are accessible by scrolling the selector.

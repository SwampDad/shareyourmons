package com.cobbletown.collectionexporter;

import com.cobbletown.collectionexporter.build.BuildIdentity;
import com.cobbletown.collectionexporter.ui.CollectionExporterScreen;
import com.swampmods.launcher.SwampModDescriptor;
import com.swampmods.launcher.SwampModsLauncher;
import net.fabricmc.api.ClientModInitializer;

/** Client entry point: initialize the core and register the single Swamp Mods launcher entry. */
public final class CollectionExporterClient implements ClientModInitializer {
    private static CollectionExporterRuntime runtime;

    @Override
    public void onInitializeClient() {
        try {
            runtime = CollectionExporterRuntime.createDefault();
            if (!SwampModsLauncher.register(new SwampModDescriptor(
                    "collection-exporter", "ShareYourMons",
                    "Copy a readable report of your Cobblemon PC collection.", 100),
                    parent -> new CollectionExporterScreen(parent, runtime.controller()))) {
                CollectionExporterMod.LOGGER.error(
                        "ShareYourMons could not register its Swamp Mods entry; no settings launcher was added");
            }
            BuildIdentity build = BuildIdentity.loadDefault();
            CollectionExporterMod.LOGGER.info(
                    "ShareYourMons initialized: version={}, sourceFingerprint={}, catalogSha256={}, catalogSchema={}, buildTimestamp={}; Swamp Mods launcher registered",
                    build.version(), build.sourceFingerprint(), build.catalogSha256(),
                    build.catalogSchemaVersion(), build.buildTimestamp());
        } catch (RuntimeException failure) {
            runtime = null;
            CollectionExporterMod.LOGGER.error(
                    "ShareYourMons core disabled: catalog or service initialization failed",
                    failure);
        }
    }

    public static CollectionExporterRuntime runtime() {
        return runtime;
    }
}

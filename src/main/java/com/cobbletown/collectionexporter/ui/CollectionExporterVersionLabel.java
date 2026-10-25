package com.cobbletown.collectionexporter.ui;

import com.cobbletown.collectionexporter.build.BuildIdentity;

/** Builds the always-current version label from generated build provenance. */
final class CollectionExporterVersionLabel {
    private static final String MOD_NAME = "ShareYourMons";

    private CollectionExporterVersionLabel() {
    }

    static String text() {
        return format(BuildIdentity.loadDefault().version());
    }

    static String format(String version) {
        String displayed = version == null || version.isBlank() ? "unknown" : version;
        return MOD_NAME + " · v" + displayed;
    }
}

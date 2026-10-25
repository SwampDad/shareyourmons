package com.cobbletown.collectionexporter.build;

import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BuildIdentityTest {
    @Test
    void generatedBuildIdentityIsDevelopmentAndComplete() {
        BuildIdentity identity = BuildIdentity.loadDefault();

        assertTrue(identity.development());
        assertTrue(identity.version().startsWith("0.3.2-dev.2+"));
        assertEquals(64, identity.sourceFingerprint().length());
        assertEquals(64, identity.catalogSha256().length());
        assertEquals(1, identity.catalogSchemaVersion());
        assertFalse(identity.buildTimestamp().isBlank());
        assertTrue(identity.reportFooter().contains("catalog " + identity.shortCatalogHash()));
    }

    @Test
    void fabricMetadataUsesTheSameGeneratedVersion() throws Exception {
        BuildIdentity identity = BuildIdentity.loadDefault();
        try (InputStream stream = getClass().getResourceAsStream("/fabric.mod.json")) {
            assertTrue(stream != null);
            String metadata = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
            assertTrue(metadata.contains("\"version\": \"" + identity.version() + "\""));
        }
    }

    @Test
    void embeddedCatalogHashMatchesTheBundledCatalog() throws Exception {
        BuildIdentity identity = BuildIdentity.loadDefault();
        try (InputStream stream = getClass().getResourceAsStream(
                "/assets/collection-exporter/classification-catalog.json")) {
            assertTrue(stream != null);
            assertEquals(identity.catalogSha256(), BuildIdentity.sha256Hex(stream.readAllBytes()));
        }
    }

    @Test
    void catalogContentChangesProduceDifferentFingerprints() {
        byte[] first = "{\"schemaVersion\":1}".getBytes(StandardCharsets.UTF_8);
        byte[] second = "{\"schemaVersion\":2}".getBytes(StandardCharsets.UTF_8);
        assertNotEquals(BuildIdentity.sha256Hex(first), BuildIdentity.sha256Hex(second));
    }

    @Test
    void sourceContentChangesProduceDifferentFingerprints() {
        Map<String, byte[]> first = Map.of(
                "src/main/A.java", "class A {}".getBytes(StandardCharsets.UTF_8),
                "src/main/B.java", "class B {}".getBytes(StandardCharsets.UTF_8));
        Map<String, byte[]> second = Map.of(
                "src/main/A.java", "class A { int changed; }".getBytes(StandardCharsets.UTF_8),
                "src/main/B.java", "class B {}".getBytes(StandardCharsets.UTF_8));

        assertNotEquals(BuildIdentity.sourceFingerprint(first), BuildIdentity.sourceFingerprint(second));
        assertEquals(BuildIdentity.sourceFingerprint(first), BuildIdentity.sourceFingerprint(first));
    }

    @Test
    void publicBuildFooterStaysSimple() {
        BuildIdentity identity = new BuildIdentity("0.3.2", "sha", "abcdef1234567890",
                "catalog", 1, "2026-10-25T00:00:00Z", false);
        assertEquals("ShareYourMons · v0.3.2", identity.reportFooter());
        assertFalse(identity.reportFooter().contains("catalog"));
    }
}

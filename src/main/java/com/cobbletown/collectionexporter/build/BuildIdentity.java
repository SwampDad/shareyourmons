package com.cobbletown.collectionexporter.build;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

/**
 * Immutable build provenance loaded from the generated build-info resource.
 *
 * <p>The version may contain a deterministic source fingerprint suffix for development
 * builds. The JAR hash is intentionally not embedded; it belongs in the external build
 * manifest, where it cannot become self-referential.</p>
 */
public record BuildIdentity(
        String version,
        String sourceFingerprint,
        String catalogSha256,
        String catalogVersion,
        int catalogSchemaVersion,
        String buildTimestamp,
        boolean development
) {
    public static final String RESOURCE_PATH = "/assets/collection-exporter/build-info.properties";
    private static final BuildIdentity UNKNOWN =
            new BuildIdentity("unknown", "", "", "unknown", 0, "unknown", true);

    public BuildIdentity {
        version = version == null || version.isBlank() ? "unknown" : version;
        sourceFingerprint = sourceFingerprint == null ? "" : sourceFingerprint;
        catalogSha256 = catalogSha256 == null ? "" : catalogSha256;
        catalogVersion = catalogVersion == null ? "unknown" : catalogVersion;
        buildTimestamp = buildTimestamp == null ? "unknown" : buildTimestamp;
    }

    public static BuildIdentity loadDefault() {
        try (InputStream stream = BuildIdentity.class.getResourceAsStream(RESOURCE_PATH)) {
            if (stream == null) {
                return UNKNOWN;
            }
            Properties properties = new Properties();
            properties.load(stream);
            return fromProperties(properties);
        } catch (IOException failure) {
            return UNKNOWN;
        }
    }

    public static BuildIdentity fromProperties(Properties properties) {
        String version = properties.getProperty("version", "unknown");
        String fingerprint = properties.getProperty("sourceFingerprint", "");
        String catalogSha = properties.getProperty("catalogSha256", "");
        String catalogVersion = properties.getProperty("catalogVersion", "unknown");
        int schema = parseInt(properties.getProperty("catalogSchemaVersion"), 0);
        String timestamp = properties.getProperty("buildTimestamp", "unknown");
        boolean development = parseBoolean(properties.getProperty("developmentBuild"),
                version.contains("-dev") || version.contains("+"));
        return new BuildIdentity(version, fingerprint, catalogSha, catalogVersion, schema,
                timestamp, development);
    }

    public String reportFooter() {
        if (!development) {
            return "ShareYourMons · v" + version;
        }
        return "ShareYourMons · v" + version + " · catalog " + shortCatalogHash()
                + " · schema " + catalogSchemaVersion;
    }

    public String shortCatalogHash() {
        return catalogSha256.length() > 12 ? catalogSha256.substring(0, 12) : catalogSha256;
    }

    public List<String> diagnosticLines() {
        List<String> lines = new ArrayList<>();
        lines.add("Build: ShareYourMons v" + version);
        lines.add("Source fingerprint: sha256:" + sourceFingerprint);
        lines.add("Catalog version: " + catalogVersion);
        lines.add("Catalog SHA-256: " + catalogSha256);
        lines.add("Catalog schema: " + catalogSchemaVersion);
        lines.add("Build timestamp: " + buildTimestamp);
        return List.copyOf(lines);
    }

    public static String sha256Hex(byte[] bytes) {
        try {
            return hex(MessageDigest.getInstance("SHA-256").digest(bytes));
        } catch (Exception failure) {
            throw new IllegalStateException("SHA-256 is unavailable", failure);
        }
    }

    /**
     * Deterministic source fingerprint used by tests and the build script: sorted relative
     * path + NUL + bytes + NUL for every source input.
     */
    public static String sourceFingerprint(Map<String, byte[]> sources) {
        Objects.requireNonNull(sources, "sources");
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            sources.keySet().stream().sorted().forEach(path -> {
                digest.update(path.getBytes(StandardCharsets.UTF_8));
                digest.update((byte) 0);
                digest.update(sources.get(path));
                digest.update((byte) 0);
            });
            return hex(digest.digest());
        } catch (Exception failure) {
            throw new IllegalStateException("SHA-256 is unavailable", failure);
        }
    }

    private static String hex(byte[] bytes) {
        StringBuilder out = new StringBuilder(bytes.length * 2);
        for (byte value : bytes) {
            out.append(String.format("%02x", value & 0xff));
        }
        return out.toString();
    }

    private static int parseInt(String value, int fallback) {
        try {
            return Integer.parseInt(value);
        } catch (RuntimeException invalid) {
            return fallback;
        }
    }

    private static boolean parseBoolean(String value, boolean fallback) {
        return value == null || value.isBlank() ? fallback : Boolean.parseBoolean(value);
    }
}

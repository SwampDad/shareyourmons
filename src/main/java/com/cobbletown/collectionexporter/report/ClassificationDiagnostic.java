package com.cobbletown.collectionexporter.report;

import com.cobbletown.collectionexporter.classify.PokemonCategory;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * One source-visible classification record for development diagnostics.
 *
 * <p>This record intentionally omits PC locations and player identity. It exposes the raw
 * identifiers and the evidence status needed to compare a live report with the catalog.</p>
 */
public record ClassificationDiagnostic(
        String speciesId,
        String displayName,
        String formId,
        List<String> aspects,
        String matchedCatalogEntry,
        Set<PokemonCategory> categories,
        String evidence,
        String mappingStatus,
        List<String> unresolvedAspects,
        boolean customSpecies
) {
    public ClassificationDiagnostic {
        Objects.requireNonNull(speciesId, "speciesId");
        displayName = displayName == null ? speciesId : displayName;
        formId = formId == null || formId.isBlank() ? "normal" : formId;
        aspects = aspects == null ? List.of() : List.copyOf(aspects);
        matchedCatalogEntry = matchedCatalogEntry == null ? "none" : matchedCatalogEntry;
        categories = categories == null ? Set.of() : Set.copyOf(categories);
        evidence = evidence == null ? "none" : evidence;
        mappingStatus = mappingStatus == null ? "unresolved" : mappingStatus;
        unresolvedAspects = unresolvedAspects == null ? List.of() : List.copyOf(unresolvedAspects);
    }
}

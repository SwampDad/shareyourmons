package com.cobbletown.collectionexporter.classify;

import java.util.List;
import java.util.Objects;

/**
 * A regional or alternate form documented before its runtime form/aspect token is known.
 *
 * <p>This metadata preserves the documented relationship without turning an expected form
 * label into a runtime identifier. A null {@code expectedFormId} means no safe identifier
 * has been established.</p>
 */
public record DocumentedForm(
        String name,
        String kind,
        String baseSpeciesId,
        String expectedFormId,
        List<String> expectedAspects,
        String status,
        String notes,
        List<String> evidence
) {
    public DocumentedForm {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(baseSpeciesId, "baseSpeciesId");
        kind = kind == null ? "alternate_form" : kind;
        expectedAspects = expectedAspects == null ? List.of() : List.copyOf(expectedAspects);
        status = status == null ? "documented_unmapped" : status;
        notes = notes == null ? "" : notes;
        evidence = evidence == null ? List.of() : List.copyOf(evidence);
    }
}

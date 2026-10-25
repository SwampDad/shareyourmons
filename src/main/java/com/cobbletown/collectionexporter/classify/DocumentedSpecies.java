package com.cobbletown.collectionexporter.classify;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * A species documented from Discord or wiki evidence before runtime observation.
 *
 * <p>{@code expectedSpeciesId} is the identifier expected from the documented name and
 * Cobblemon naming convention. It is not an observed runtime fact. {@code categoryStatus}
 * preserves whether the documented source established a category, left it unknown, or
 * contains a conflict.</p>
 */
public record DocumentedSpecies(
        String name,
        String expectedSpeciesId,
        String speciesIdStatus,
        String kind,
        Set<PokemonCategory> categories,
        String categoryStatus,
        String evolution,
        List<String> evidence
) {
    public DocumentedSpecies {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(expectedSpeciesId, "expectedSpeciesId");
        speciesIdStatus = speciesIdStatus == null ? "expected" : speciesIdStatus;
        kind = kind == null ? "custom_species" : kind;
        categories = categories == null ? Set.of() : Set.copyOf(categories);
        categoryStatus = categoryStatus == null ? "unknown" : categoryStatus;
        evolution = evolution == null ? "unknown" : evolution;
        evidence = evidence == null ? List.of() : List.copyOf(evidence);
    }

    public boolean expectedRuntimeId() {
        return "expected".equalsIgnoreCase(speciesIdStatus);
    }
}

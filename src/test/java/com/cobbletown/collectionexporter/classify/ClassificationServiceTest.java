package com.cobbletown.collectionexporter.classify;

import com.cobbletown.collectionexporter.data.PokemonRecord;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ClassificationServiceTest {
    private final ClassificationCatalog catalog = ClassificationCatalogLoader.loadDefault();
    private final ClassificationService service = new ClassificationService(catalog);

    @Test
    void easterSnorlaxResolvesToEaster() {
        ClassifiedPokemon result = service.classify(record("cobblemon:snorlax", "Snorlax",
                List.of("male", "easter2026"), false));
        assertTrue(result.hasSkin());
        assertEquals("Easter", result.skin().family().displayName());
        assertFalse(result.pokemon().shiny());
    }

    @Test
    void adventureZoroarkResolvesToAdventureAndKeepsRankedMarker() {
        ClassifiedPokemon result = service.classify(record("cobblemon:zoroark", "Zoroark",
                List.of("male", "adventure2026", "ranked"), false));
        assertTrue(result.hasSkin());
        assertEquals("Adventure", result.skin().family().displayName());
        assertTrue(result.unknownAspects().isEmpty());
        assertTrue(result.nonSkinAspects().contains("ranked"));
    }

    @Test
    void unverifiedZoroartTokenRemainsUnknown() {
        ClassifiedPokemon result = service.classify(record("cobblemon:zoroark", "Zoroark",
                List.of("male", "zoroart"), false));
        assertFalse(result.hasSkin());
        assertEquals(SkinResolutionStatus.UNRESOLVED, result.skin().status());
        assertTrue(result.unknownAspects().contains("zoroart"));
    }

    @Test
    void mario2026ResolvesToMario() {
        ClassifiedPokemon result = service.classify(record("cobblemon:charmander", "Charmander",
                List.of("male", "mario2026"), false));
        assertTrue(result.hasSkin());
        assertEquals("Mario", result.skin().family().displayName());
    }

    @Test
    void mario2026ResolvesOnRecordedAndUnrelatedSpecies() {
        for (String[] candidate : new String[][]{
                {"cobblemon:tyrunt", "Tyrunt"},
                {"cobblemon:charmander", "Charmander"},
                {"cobblemon:eevee", "Eevee"}}) {
            ClassifiedPokemon result = service.classify(record(candidate[0], candidate[1],
                    List.of("male", "mario2026"), false));
            assertTrue(result.hasSkin(), candidate[0]);
            assertEquals("Mario", result.skin().family().displayName(), candidate[0]);
            assertEquals(SkinResolutionStatus.RESOLVED, result.skin().status(), candidate[0]);
            assertTrue(result.unknownAspects().isEmpty(), candidate[0]);
        }
    }

    @Test
    void adventure2026ResolvesOnRecordedSpeciesAndKeepsMarkersNonSkin() {
        List<PokemonRecord> records = List.of(
                record("cobblemon:eevee", "Eevee", List.of("male", "adventure2026"), false),
                record("cobblemon:torterra", "Torterra",
                        List.of("male", "tree-none", "adventure2026"), false),
                record("cobblemon:zoroark", "Zoroark",
                        List.of("male", "adventure2026", "ranked"), false));
        for (var record : records) {
            ClassifiedPokemon result = service.classify(record);
            assertTrue(result.hasSkin(), record.speciesId());
            assertEquals("Adventure", result.skin().family().displayName(), record.speciesId());
            assertEquals(SkinResolutionStatus.RESOLVED, result.skin().status(), record.speciesId());
            assertTrue(result.unknownAspects().isEmpty(), record.speciesId());
        }

        ClassifiedPokemon zoroark = service.classify(records.get(2));
        assertTrue(zoroark.nonSkinAspects().contains("ranked"));
        ClassifiedPokemon torterra = service.classify(records.get(1));
        assertTrue(torterra.nonSkinAspects().contains("tree-none"));
    }

    @Test
    void documentedCustomSpeciesUseExpectedIdsWithoutInventingCategories() {
        ClassifiedPokemon sweetclust = service.classify(record("cobblemon:sweetclust", "Sweetclust",
                List.of("female"), false));
        assertTrue(sweetclust.customSpecies());
        assertTrue(sweetclust.hasCategory(PokemonCategory.CUSTOM_SPECIES));
        assertFalse(sweetclust.hasCategory(PokemonCategory.LEGENDARY));
        assertFalse(sweetclust.hasCategory(PokemonCategory.MYTHICAL));
        assertFalse(sweetclust.hasCategory(PokemonCategory.PARADOX));
        assertFalse(sweetclust.hasCategory(PokemonCategory.ULTRA_BEAST));
        assertTrue(sweetclust.unknownAspects().isEmpty());
    }

    @Test
    void unresolvedDocumentedCategoriesStayUnknownInsteadOfBecomingMythical() {
        List<PokemonRecord> records = List.of(
                record("cobblemon:gearex", "Gearex", List.of("male"), false),
                record("cobblemon:tyranogear", "Tyranogear", List.of("male"), false));
        for (PokemonRecord pokemon : records) {
            ClassifiedPokemon result = service.classify(pokemon);
            assertTrue(result.customSpecies(), pokemon.speciesId());
            assertTrue(result.hasCategory(PokemonCategory.CUSTOM_SPECIES), pokemon.speciesId());
            assertFalse(result.hasCategory(PokemonCategory.MYTHICAL), pokemon.speciesId());
            assertFalse(result.hasCategory(PokemonCategory.LEGENDARY), pokemon.speciesId());
        }
    }

    @Test
    void observedCustomParadoxResolvesFromDocumentedCategory() {
        ClassifiedPokemon result = service.classify(record("cobblemon:ironlamp", "Iron Lamp",
                List.of("genderless"), false));
        assertTrue(result.customSpecies());
        assertTrue(result.hasCategory(PokemonCategory.CUSTOM_SPECIES));
        assertTrue(result.hasCategory(PokemonCategory.PARADOX));
    }

    @Test
    void conflictingCustomCategoryIsNotPromoted() {
        ClassifiedPokemon result = service.classify(record("cobblemon:seraphang", "Seraphang",
                List.of("genderless"), false));
        assertTrue(result.customSpecies());
        assertTrue(result.hasCategory(PokemonCategory.CUSTOM_SPECIES));
        assertFalse(result.hasCategory(PokemonCategory.LEGENDARY));
        assertFalse(result.hasCategory(PokemonCategory.MYTHICAL));
    }

    @Test
    void radiationBreloomIsNotASkin() {
        ClassifiedPokemon result = service.classify(record("cobblemon:breloom", "Breloom",
                List.of("male", "radiation"), false));
        assertFalse(result.hasSkin());
        assertEquals(SkinResolutionStatus.NONE, result.skin().status());
        assertTrue(result.nonSkinAspects().contains("radiation"));
    }

    @Test
    void eiscueIceFaceIsAFormNotASkin() {
        ClassifiedPokemon result = service.classify(record("cobblemon:eiscue", "Eiscue",
                List.of("male", "ice_face"), false));
        assertFalse(result.hasSkin());
        assertEquals(SkinResolutionStatus.NONE, result.skin().status());
        assertTrue(result.nonSkinAspects().contains("ice_face"));
    }

    @Test
    void archelianIsAFormNotASkin() {
        ClassifiedPokemon result = service.classify(record("cobblemon:snivy", "Snivy",
                List.of("female", "archelian"), false));
        assertFalse(result.hasSkin());
        assertEquals(SkinResolutionStatus.NONE, result.skin().status());
        assertTrue(result.nonSkinAspects().contains("archelian"));
    }

    @Test
    void tron2026ResolvesAsNeonWithAlias() {
        ClassifiedPokemon result = service.classify(record("cobblemon:dragapult", "Dragapult",
                List.of("shiny", "male", "tron2026"), true));
        assertTrue(result.hasSkin());
        assertEquals("Neon", result.skin().family().displayName());
        assertTrue(result.skin().family().aliases().contains("Tron"));
        assertTrue(result.pokemon().shiny());
    }

    @Test
    void formTokenAlongsideSkinDoesNotBlockResolution() {
        ClassifiedPokemon result = service.classify(record("cobblemon:aegislash", "Aegislash",
                List.of("shiny", "male", "shield-forme", "july2026kimono"), true));
        assertTrue(result.hasSkin());
        assertEquals("Kimono", result.skin().family().displayName());
        assertTrue(result.nonSkinAspects().contains("shield-forme"));
    }

    @Test
    void unknownAspectIsUnresolvedNotSkin() {
        ClassifiedPokemon result = service.classify(record("cobblemon:eevee", "Eevee",
                List.of("female", "totally_unknown_token"), false));
        assertFalse(result.hasSkin());
        assertEquals(SkinResolutionStatus.UNRESOLVED, result.skin().status());
        assertTrue(result.unknownAspects().contains("totally_unknown_token"));
    }

    @Test
    void knownAppearanceTokensAreNotUnresolved() {
        ClassifiedPokemon magikarp = service.classify(record("cobblemon:magikarp", "Magikarp",
                List.of("male", "magikarp-jump-standard"), false));
        assertEquals(SkinResolutionStatus.NONE, magikarp.skin().status());
        assertTrue(magikarp.nonSkinAspects().contains("magikarp-jump-standard"));

        ClassifiedPokemon gyarados = service.classify(record("cobblemon:gyarados", "Gyarados",
                List.of("male", "gyarados-eye-color-red"), false));
        assertEquals(SkinResolutionStatus.NONE, gyarados.skin().status());
        assertTrue(gyarados.nonSkinAspects().contains("gyarados-eye-color-red"));

        ClassifiedPokemon slowpoke = service.classify(record("cobblemon:slowpoke", "Slowpoke",
                List.of("male", "regrown-tail-3"), false));
        assertEquals(SkinResolutionStatus.NONE, slowpoke.skin().status());
        assertTrue(slowpoke.nonSkinAspects().contains("regrown-tail-3"));
    }

    @Test
    void unknownNonYearTokenIsStillUnresolved() {
        ClassifiedPokemon result = service.classify(record("cobblemon:magikarp", "Magikarp",
                List.of("male", "mystery-token"), false));
        assertFalse(result.hasSkin());
        assertEquals(SkinResolutionStatus.UNRESOLVED, result.skin().status());
    }

    @Test
    void multipleConfirmedSkinTokensConflict() {
        ClassifiedPokemon result = service.classify(record("cobblemon:snorlax", "Snorlax",
                List.of("male", "easter2026", "petal2026"), false));
        assertFalse(result.hasSkin());
        assertEquals(SkinResolutionStatus.CONFLICT, result.skin().status());
        assertEquals(2, result.skin().matchedAspects().size());
    }

    @Test
    void customSpeciesIsMarkedSeparately() {
        ClassifiedPokemon result = service.classify(record("cobblemon:bambolt", "Bambolt",
                List.of("male"), false));
        assertTrue(result.customSpecies());
        assertTrue(result.hasCategory(PokemonCategory.CUSTOM_SPECIES));
    }

    @Test
    void officialSpeciesCategoriesResolve() {
        assertTrue(service.classify(record("cobblemon:mewtwo", "Mewtwo", List.of("genderless"), false))
                .hasCategory(PokemonCategory.LEGENDARY));
        assertTrue(service.classify(record("cobblemon:mew", "Mew", List.of("genderless"), false))
                .hasCategory(PokemonCategory.MYTHICAL));
        assertTrue(service.classify(record("cobblemon:greattusk", "Great Tusk", List.of("genderless"), false))
                .hasCategory(PokemonCategory.PARADOX));
        assertTrue(service.classify(record("cobblemon:buzzwole", "Buzzwole", List.of("male"), false))
                .hasCategory(PokemonCategory.ULTRA_BEAST));
    }

    private PokemonRecord record(String species, String name, List<String> aspects, boolean shiny) {
        return new PokemonRecord(
                UUID.fromString("00000000-0000-0000-0000-000000000001"),
                0, 0, species, name, 0, "normal", "Normal", aspects, shiny, 50, "male", null);
    }
}

package com.cobbletown.collectionexporter.classify;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ClassificationCatalogTest {
    private final ClassificationCatalog catalog = ClassificationCatalogLoader.loadDefault();

    @Test
    void loadsBundledCatalogWithProvenance() {
        assertEquals(1, catalog.schemaVersion());
        assertNotNull(catalog.provenance());
        assertEquals("Cobblemon-fabric-1.8.2+1.21.1.jar", catalog.provenance().cobblemonJar());
        assertFalse(catalog.provenance().cobblemonJarSha256().isBlank());
        assertFalse(catalog.provenance().skinRegistrySha256().isBlank());
        assertEquals(10, catalog.skins().size());
    }

    @Test
    void easterIsAConfirmedSkin() {
        SkinFamily easter = catalog.skin("easter2026");
        assertNotNull(easter);
        assertEquals("Easter", easter.displayName());
        assertTrue(easter.identityConfirmed());
    }

    @Test
    void marioIsAConfirmedSkin() {
        SkinFamily mario = catalog.skin("mario2026");
        assertNotNull(mario);
        assertEquals("Mario", mario.displayName());
        assertTrue(mario.identityConfirmed());
        assertEquals(1, mario.reportOrder());
    }

    @Test
    void adventureIsAConfirmedSkin() {
        SkinFamily adventure = catalog.skin("adventure2026");
        assertNotNull(adventure);
        assertEquals("Adventure", adventure.displayName());
        assertTrue(adventure.identityConfirmed());
    }

    @Test
    void tronIsNeonWithTronAlias() {
        SkinFamily neon = catalog.skin("tron2026");
        assertNotNull(neon);
        assertEquals("Neon", neon.displayName());
        assertEquals("June 2026 Neon Skins", neon.officialName());
        assertTrue(neon.aliases().contains("Tron"));
    }

    @Test
    void nineSetSkinProgressionIsOwnerAcceptedInExactOrder() {
        var accepted = catalog.skins().stream()
                .filter(skin -> "accepted".equals(skin.evidenceStatus()))
                .toList();

        assertEquals(List.of(
                        "mario2026", "adventure2026", "petal2026", "relic2026",
                        "tron2026", "july2026kimono", "august2026western",
                        "september2026crystall", "october2026spooky"),
                accepted.stream().map(SkinFamily::aspect).toList());
        assertEquals(List.of(1, 2, 4, 5, 6, 7, 8, 9, 10),
                accepted.stream().map(SkinFamily::reportOrder).toList());
        assertEquals("separate-event", catalog.skin("easter2026").evidenceStatus());
        assertEquals(3, catalog.skin("easter2026").reportOrder());
    }

    @Test
    void documentedSpeciesPreserveExpectedIdsAndUnknownOrDocumentedCategories() {
        assertEquals(10, catalog.documentedSpecies().size());
        assertEquals("cobblemon:sweetclust", catalog.documentedSpecies("cobblemon:sweetclust").expectedSpeciesId());
        assertEquals("unknown", catalog.documentedSpecies("cobblemon:sweetclust").categoryStatus());
        assertTrue(catalog.documentedSpecies("cobblemon:sweetclust").categories().isEmpty());
        assertTrue(catalog.documentedSpecies("cobblemon:sweetclust").expectedRuntimeId());

        DocumentedSpecies gearex = catalog.documentedSpecies("cobblemon:gearex");
        assertNotNull(gearex);
        assertEquals("unresolved", gearex.categoryStatus());
        assertTrue(gearex.categories().isEmpty());
        assertFalse(catalog.categoriesFor("cobblemon:gearex").contains(PokemonCategory.MYTHICAL));
        assertFalse(catalog.categoriesFor("cobblemon:tyranogear").contains(PokemonCategory.MYTHICAL));
        assertFalse(catalog.categoriesFor("cobblemon:sweetclust").contains(PokemonCategory.MYTHICAL));
        assertFalse(catalog.customSpeciesObserved().contains("cobblemon:gearex"));
        assertTrue(catalog.documentedSpecies("cobblemon:ironlamp").categories()
                .contains(PokemonCategory.PARADOX));
        assertTrue(catalog.categoriesFor("cobblemon:ironlamp").contains(PokemonCategory.PARADOX));
        assertEquals("conflicting", catalog.documentedSpecies("cobblemon:seraphang").categoryStatus());
        assertTrue(catalog.documentedSpecies("cobblemon:seraphang").categories().isEmpty());
        assertFalse(catalog.categoriesFor("cobblemon:seraphang").contains(PokemonCategory.LEGENDARY));
        assertFalse(catalog.categoriesFor("cobblemon:seraphang").contains(PokemonCategory.MYTHICAL));
    }

    @Test
    void documentedFormsRemainUnmappedUntilRuntimeEvidenceExists() {
        assertEquals(5, catalog.documentedForms().size());
        DocumentedForm archelian = catalog.documentedForms().stream()
                .filter(form -> form.name().equals("Archelian Numel"))
                .findFirst()
                .orElseThrow();
        assertEquals("cobblemon:numel", archelian.baseSpeciesId());
        assertNull(archelian.expectedFormId());
        assertTrue(archelian.expectedAspects().isEmpty());
        assertEquals("documented_unmapped", archelian.status());
    }

    @Test
    void nonSkinCorrectionsArePresent() {
        assertTrue(catalog.nonSkinAspects().contains("radiation"));
        assertTrue(catalog.nonSkinAspects().contains("archelian"));
        assertTrue(catalog.nonSkinAspects().contains("alatian"));
        assertTrue(catalog.nonSkinAspects().contains("mega"));
        assertTrue(catalog.nonSkinAspects().contains("alpha"));
        assertTrue(catalog.nonSkinAspects().contains("gmax"));
        assertTrue(catalog.nonSkinAspects().contains("magikarp-jump-standard"));
        assertTrue(catalog.nonSkinAspects().contains("gyarados-eye-color-red"));
        assertTrue(catalog.nonSkinAspects().contains("regrown-tail-3"));
    }

    @Test
    void categoryListsAreComplete() {
        assertEquals(71, catalog.categorySpecies(PokemonCategory.LEGENDARY).size());
        assertEquals(23, catalog.categorySpecies(PokemonCategory.MYTHICAL).size());
        assertEquals(20, catalog.categorySpecies(PokemonCategory.PARADOX).size());
        assertEquals(11, catalog.categorySpecies(PokemonCategory.ULTRA_BEAST).size());
        assertTrue(catalog.categoriesFor("cobblemon:mewtwo").contains(PokemonCategory.LEGENDARY));
        assertTrue(catalog.categoriesFor("cobblemon:mew").contains(PokemonCategory.MYTHICAL));
        assertTrue(catalog.categoriesFor("cobblemon:greattusk").contains(PokemonCategory.PARADOX));
        assertTrue(catalog.categoriesFor("cobblemon:buzzwole").contains(PokemonCategory.ULTRA_BEAST));
    }

    @Test
    void baseSpeciesAndObservedCustomSpeciesAreSeparated() {
        assertTrue(catalog.isBaseSpecies("cobblemon:pikachu"));
        assertFalse(catalog.isBaseSpecies("cobblemon:bambolt"));
        assertTrue(catalog.customSpeciesObserved().contains("cobblemon:bambolt"));
    }
}

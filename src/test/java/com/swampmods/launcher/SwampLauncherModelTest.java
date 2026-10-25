package com.swampmods.launcher;

import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class SwampLauncherModelTest {
    private static final SwampModDescriptor EGG = new SwampModDescriptor("egg-badges", "Egg Badges", "Settings", 0);
    private static final SwampModDescriptor SHARE = new SwampModDescriptor("collection-exporter", "ShareYourMons", "Collection", 100);

    @Test void matrixCoversNeitherAndBoth() {
        assertEquals(SwampLauncherModel.Mode.NONE, SwampLauncherModel.installed(List.of(EGG, SHARE), id -> false).mode());
        SwampLauncherModel both = SwampLauncherModel.installed(List.of(SHARE, EGG), Set.of("egg-badges", "collection-exporter")::contains);
        assertEquals(SwampLauncherModel.Mode.SELECTOR, both.mode());
        assertEquals("Swamp Mods", both.buttonLabel());
        assertEquals(List.of("Egg Badges", "ShareYourMons"), both.entries().stream().map(SwampModDescriptor::displayName).toList());
    }

    @Test void eitherSingleModUsesItsOwnDirectButton() {
        assertEquals("Egg Badges", SwampLauncherModel.installed(List.of(EGG, SHARE), Set.of("egg-badges")::contains).buttonLabel());
        assertEquals("ShareYourMons", SwampLauncherModel.installed(List.of(EGG, SHARE), Set.of("collection-exporter")::contains).buttonLabel());
    }

    @Test void duplicateAndUninstalledDescriptorsCannotProduceExtraRows() {
        SwampLauncherModel model = SwampLauncherModel.installed(List.of(EGG, EGG, SHARE), Set.of("egg-badges")::contains);
        assertEquals(1, model.entries().size());
        assertEquals("egg-badges", model.directEntry().orElseThrow().modId());
    }

    @Test void placementPrefersImmediatelyBelowDisconnectWithoutOverlap() {
        SwampLauncherPlacement.Bounds disconnect = new SwampLauncherPlacement.Bounds(58, 200, 204, 20);
        SwampLauncherPlacement.Bounds placement = SwampLauncherPlacement.choose(
                320, 300, disconnect, List.of(disconnect), 204).orElseThrow();
        assertEquals(224, placement.y());
        assertEquals(58, placement.x());
        assertFalse(placement.intersects(disconnect));
    }
}

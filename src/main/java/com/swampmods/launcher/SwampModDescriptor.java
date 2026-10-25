package com.swampmods.launcher;

import java.util.Objects;

/** Immutable public description of one independently-installed Swamp Mod. */
public record SwampModDescriptor(String modId, String displayName, String description, int order) {
    public SwampModDescriptor {
        Objects.requireNonNull(modId, "modId");
        Objects.requireNonNull(displayName, "displayName");
        description = description == null ? "" : description;
        if (modId.isBlank() || displayName.isBlank()) {
            throw new IllegalArgumentException("modId and displayName must not be blank");
        }
    }
}

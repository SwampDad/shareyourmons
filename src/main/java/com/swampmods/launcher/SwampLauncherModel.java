package com.swampmods.launcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

/** Pure routing policy shared by the embedded launcher implementation. */
public final class SwampLauncherModel {
    public enum Mode { NONE, DIRECT, SELECTOR }

    private static final Comparator<SwampModDescriptor> ORDER = Comparator
            .comparingInt(SwampModDescriptor::order)
            .thenComparing(SwampModDescriptor::displayName, String.CASE_INSENSITIVE_ORDER)
            .thenComparing(SwampModDescriptor::modId);

    private final List<SwampModDescriptor> entries;

    private SwampLauncherModel(List<SwampModDescriptor> entries) {
        this.entries = List.copyOf(entries);
    }

    public static SwampLauncherModel installed(Collection<SwampModDescriptor> candidates,
                                               Predicate<String> isModLoaded) {
        Map<String, SwampModDescriptor> unique = new LinkedHashMap<>();
        for (SwampModDescriptor candidate : candidates) {
            if (candidate != null && isModLoaded.test(candidate.modId())) {
                unique.putIfAbsent(candidate.modId(), candidate);
            }
        }
        List<SwampModDescriptor> sorted = new ArrayList<>(unique.values());
        sorted.sort(ORDER);
        return new SwampLauncherModel(sorted);
    }

    public Mode mode() { return entries.isEmpty() ? Mode.NONE : entries.size() == 1 ? Mode.DIRECT : Mode.SELECTOR; }
    public String buttonLabel() { return mode() == Mode.DIRECT ? entries.get(0).displayName() : mode() == Mode.SELECTOR ? "Swamp Mods" : ""; }
    public Optional<SwampModDescriptor> directEntry() { return mode() == Mode.DIRECT ? Optional.of(entries.get(0)) : Optional.empty(); }
    public List<SwampModDescriptor> entries() { return entries; }
}

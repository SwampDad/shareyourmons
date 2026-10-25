package com.swampmods.launcher;

import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Small embedded Swamp Mods launcher. Each participating mod packages identical source under
 * this package and registers only itself. Fabric's shared game class loader makes its static
 * registry and one screen listener common to every installed participant; no core JAR exists.
 */
public final class SwampModsLauncher {
    private static final Logger LOG = LoggerFactory.getLogger("swamp-mods/launcher");
    private static final int LAUNCHER_WIDTH = 204;
    private static final Map<String, Entry> ENTRIES = new LinkedHashMap<>();
    private static final Map<Screen, List<ClickableWidget>> INJECTED = new IdentityHashMap<>();
    private static boolean hooked;

    private SwampModsLauncher() { }

    /** Registers this mod's reachable settings screen. Duplicate IDs are rejected and logged. */
    public static synchronized boolean register(SwampModDescriptor descriptor, ScreenFactory screenFactory) {
        try {
            if (ENTRIES.containsKey(descriptor.modId())) {
                LOG.warn("Ignoring duplicate Swamp Mods registration for {}", descriptor.modId());
                return false;
            }
            ENTRIES.put(descriptor.modId(), new Entry(descriptor, screenFactory));
            if (!hooked) {
                hooked = true;
                ScreenEvents.AFTER_INIT.register((client, screen, width, height) -> {
                    if (screen instanceof GameMenuScreen) attach(screen, width, height);
                });
            }
            LOG.info("Registered Swamp Mods entry for {}", descriptor.modId());
            return true;
        } catch (RuntimeException | LinkageError failure) {
            LOG.error("Could not register a Swamp Mods entry", failure);
            return false;
        }
    }

    private static synchronized SwampLauncherModel model() {
        return SwampLauncherModel.installed(ENTRIES.values().stream().map(Entry::descriptor).toList(), FabricLoader.getInstance()::isModLoaded);
    }
    static synchronized Entry find(String modId) { return ENTRIES.get(modId); }

    private static void attach(Screen screen, int width, int height) {
        SwampLauncherModel model = model();
        if (model.mode() == SwampLauncherModel.Mode.NONE) return;
        remove(screen);
        ButtonWidget exit = findExitButton(screen);
        SwampLauncherPlacement.Bounds exitBounds = exit == null
                ? new SwampLauncherPlacement.Bounds(Math.max(4, width / 2 - LAUNCHER_WIDTH / 2), Math.max(4, height - 28), Math.min(LAUNCHER_WIDTH, Math.max(1, width - 8)), SwampLauncherPlacement.BUTTON_HEIGHT)
                : new SwampLauncherPlacement.Bounds(exit.getX(), exit.getY(), exit.getWidth(), exit.getHeight());
        List<SwampLauncherPlacement.Bounds> occupied = Screens.getButtons(screen).stream().map(widget -> new SwampLauncherPlacement.Bounds(widget.getX(), widget.getY(), widget.getWidth(), widget.getHeight())).toList();
        SwampLauncherPlacement.choose(width, height, exitBounds, occupied, LAUNCHER_WIDTH).ifPresent(placement -> {
            ButtonWidget launcher = ButtonWidget.builder(Text.literal(model.buttonLabel()), button -> open(model, screen)).dimensions(placement.x(), placement.y(), placement.width(), placement.height()).build();
            Screens.getButtons(screen).add(launcher);
            INJECTED.put(screen, new ArrayList<>(List.of(launcher)));
            ScreenEvents.remove(screen).register(SwampModsLauncher::remove);
        });
    }

    private static void open(SwampLauncherModel model, Screen parent) {
        if (model.mode() == SwampLauncherModel.Mode.DIRECT) {
            model.directEntry().ifPresent(descriptor -> open(MinecraftClient.getInstance(), parent, descriptor));
        } else if (model.mode() == SwampLauncherModel.Mode.SELECTOR) {
            MinecraftClient.getInstance().setScreen(new SwampModsScreen(parent, model.entries()));
        }
    }
    static void open(MinecraftClient client, Screen parent, SwampModDescriptor descriptor) {
        Entry entry = find(descriptor.modId());
        if (entry == null) return;
        try {
            Screen child = entry.screenFactory().create(parent);
            if (child != null) client.setScreen(child);
            else LOG.error("Swamp Mods entry {} returned no screen", descriptor.modId());
        } catch (RuntimeException | LinkageError failure) {
            LOG.error("Could not open the {} screen; its other settings access remains available", descriptor.displayName(), failure);
        }
    }
    private static ButtonWidget findExitButton(Screen screen) {
        ButtonWidget bottomMost = null;
        for (ClickableWidget widget : Screens.getButtons(screen)) {
            if (!(widget instanceof ButtonWidget button)) continue;
            String label = button.getMessage().getString();
            if (label.equals("Disconnect") || label.startsWith("Save and Quit")) return button;
            if (bottomMost == null || button.getY() > bottomMost.getY()) bottomMost = button;
        }
        return bottomMost;
    }
    private static void remove(Screen screen) { List<ClickableWidget> widgets = INJECTED.remove(screen); if (widgets != null) Screens.getButtons(screen).removeAll(widgets); }
    record Entry(SwampModDescriptor descriptor, ScreenFactory screenFactory) { }
    @FunctionalInterface public interface ScreenFactory { Screen create(Screen parent); }
}

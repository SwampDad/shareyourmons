package com.swampmods.launcher;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import java.util.List;

/** Existing selector layout, with scrolling only when future registrations exceed its viewport. */
final class SwampModsScreen extends Screen {
    private static final int BUTTON_WIDTH = 200;
    private final Screen parent;
    private final List<SwampModDescriptor> entries;
    private int firstEntry;
    private int visibleRows;
    SwampModsScreen(Screen parent, List<SwampModDescriptor> entries) { super(Text.literal("Swamp Mods")); this.parent = parent; this.entries = List.copyOf(entries); }
    @Override protected void init() { rebuild(); }
    private void rebuild() {
        clearChildren();
        int x = (width - BUTTON_WIDTH) / 2, start = Math.max(38, height / 4);
        visibleRows = Math.max(1, (height - 32 - start) / 24);
        firstEntry = Math.max(0, Math.min(firstEntry, Math.max(0, entries.size() - visibleRows)));
        for (int index = firstEntry; index < Math.min(entries.size(), firstEntry + visibleRows); index++) {
            SwampModDescriptor descriptor = entries.get(index);
            int y = start + (index - firstEntry) * 24;
            addDrawableChild(ButtonWidget.builder(Text.literal(descriptor.displayName()), pressed -> SwampModsLauncher.open(MinecraftClient.getInstance(), this, descriptor)).dimensions(x, y, BUTTON_WIDTH, 20).tooltip(Tooltip.of(Text.literal(descriptor.description()))).build());
        }
        addDrawableChild(ButtonWidget.builder(Text.literal("Back"), button -> close()).dimensions((width - 100) / 2, height - 28, 100, 20).build());
    }
    @Override public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (entries.size() <= visibleRows || verticalAmount == 0) return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
        int next = Math.max(0, Math.min(firstEntry - (verticalAmount > 0 ? 1 : -1), entries.size() - visibleRows));
        if (next != firstEntry) { firstEntry = next; rebuild(); }
        return true;
    }
    @Override public void close() { MinecraftClient.getInstance().setScreen(parent); }
    @Override public void render(DrawContext context, int mouseX, int mouseY, float delta) { super.render(context, mouseX, mouseY, delta); context.drawCenteredTextWithShadow(textRenderer, getTitle(), width / 2, 16, 0xFFFFFFFF); }
    @Override public boolean shouldPause() { return false; }
}

package net.hfstack.rallyguard.screen;

import net.hfstack.rallyguard.network.NetworkConstants;
import net.hfstack.rallyguard.network.payload.AssignSquadC2SPayload;
import net.hfstack.rallyguard.network.payload.GuardActionC2SPayload;
import net.hfstack.rallyguard.network.payload.GuardListS2CPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Commander's Ledger command panel (Phase 2E) - lists a player's owned guards and lets
 * them summon or toggle patrol on each. Ported from the Forge 1.20.1
 * {@code GuardCommandScreen}, which was already close to 1.21.1-shaped vanilla client
 * code ({@code GuiGraphics}/{@code Button}/{@code Screen}). Two changes applied, both
 * already verified/established earlier in this project:
 * <ul>
 *     <li>{@code renderBackground(GuiGraphics)} -&gt;
 *     {@code renderBackground(GuiGraphics, int, int, float)} - the same MC 1.20.1 -&gt;
 *     1.21.1 vanilla signature change already found and fixed in
 *     {@link HireGuardScreen} (Phase 2C, PORTING_PLAN.md section 6.3).</li>
 *     <li>{@code NetworkBootstrap.CHANNEL.sendToServer(...)} (Forge {@code SimpleChannel})
 *     -&gt; {@code PacketDistributor.sendToServer(...)} - the NeoForge 1.21.1
 *     {@code CustomPacketPayload} send call, the same one Guard Villagers' own
 *     {@code GuardInventoryScreen.java} uses for its own follow/patrol buttons.</li>
 * </ul>
 * <p>
 * Phase 3A adds multi-select ("Sel" toggle per row, state kept in {@link #selected} by
 * entity id so it survives page changes) plus a squad name {@link EditBox} and
 * Assign/Clear buttons at the bottom - the Ledger's half of squad assignment, sending
 * {@link AssignSquadC2SPayload} for every checked guard at once. {@code EditBox}'s
 * constructor shape ({@code Font, x, y, width, height, Component}) is this project's
 * first use of that widget - verified against mappings.dev's 1.21.1-pinned page rather
 * than assumed, same discipline as every other new vanilla API this project has touched.
 */
public class GuardCommandScreen extends Screen {

    public record Entry(int entityId, String name, boolean patrolling, String squadName) {
    }

    private final List<Entry> all = new ArrayList<>();
    private final Set<Integer> selected = new LinkedHashSet<>();
    private int page = 0;
    private String squadNameDraft = "";

    private static final int PER_PAGE = 4;
    private static final int PANEL_W = 440;
    private static final int PANEL_H = 232;

    private static final int MARGIN_L = 16;
    private static final int MARGIN_R = 16;

    private static final int HEADER_Y = 24;
    private static final int HEADER_LINE_Y = 38;

    private static final int ROW_TOP = 38;
    private static final int ROW_HEIGHT = 28;

    private static final int SELECT_W = 16;
    private static final int SELECT_GAP = 6;
    private static final int COL_NAME_X = MARGIN_L + SELECT_W + SELECT_GAP;

    private static final int BTN_W1 = 80;
    private static final int BTN_W2 = 80;
    private static final int BTN_H = 18;
    private static final int BTN_GAP = 2;

    private static final int SQUAD_BAR_OFFSET = PANEL_H - 56;
    private static final int SQUAD_EDIT_W = 210;
    private static final int SQUAD_ASSIGN_W = 90;
    private static final int SQUAD_CLEAR_W = 70;

    public GuardCommandScreen(List<Entry> entries) {
        super(Component.translatable("gui.rallyguard.command.title"));
        this.all.addAll(entries);
    }

    /**
     * Called from {@code GuardCommandNetworking.handleGuardList}, which already runs on
     * the client main thread via {@code IPayloadContext.enqueueWork(...)} - no extra
     * thread marshaling is needed here (the Fabric donor's
     * {@code MinecraftClient.getInstance().execute(...)} wrapper is a Fabric-specific
     * habit, not required by NeoForge's payload handler contract).
     */
    public static void openFromPayload(GuardListS2CPayload payload) {
        List<Entry> list = new ArrayList<>(payload.entries().size());
        for (GuardListS2CPayload.Entry e : payload.entries()) {
            list.add(new Entry(e.entityId(), e.name(), e.patrolling(), e.squadName()));
        }
        Minecraft.getInstance().setScreen(new GuardCommandScreen(list));
    }

    @Override
    protected void init() {
        super.init();
        rebuildButtons();
    }

    private void rebuildButtons() {
        this.clearWidgets();

        int x = (this.width - PANEL_W) / 2;
        int y = (this.height - PANEL_H) / 2;

        int start = page * PER_PAGE;
        int end = Math.min(start + PER_PAGE, all.size());

        int groupWidth = BTN_W1 + BTN_GAP + BTN_W2;
        int actionsRight = x + PANEL_W - MARGIN_R;
        int actionsLeft = actionsRight - groupWidth;

        for (int i = start; i < end; i++) {
            final int idx = i;
            Entry e = all.get(i);

            int rowTop = y + ROW_TOP + (i - start) * ROW_HEIGHT;
            int rowMidY = rowTop + (ROW_HEIGHT / 2);
            int btnY = rowMidY - (BTN_H / 2);

            Button select = Button.builder(
                    Component.literal(selected.contains(e.entityId()) ? "x" : " "),
                    b -> {
                        if (!selected.remove(e.entityId())) {
                            selected.add(e.entityId());
                        }
                        rebuildButtons();
                    }
            ).bounds(x + MARGIN_L, btnY, SELECT_W, BTN_H).build();

            Button summon = Button.builder(
                    Component.translatable("gui.rallyguard.command.summon"),
                    b -> sendAction(e.entityId(), NetworkConstants.ACTION_SUMMON)
            ).bounds(actionsLeft, btnY, BTN_W1, BTN_H).build();

            Component label = e.patrolling()
                    ? Component.translatable("gui.rallyguard.command.stop")
                    : Component.translatable("gui.rallyguard.command.patrol");

            Button patrol = Button.builder(label, b -> {
                sendAction(e.entityId(), NetworkConstants.ACTION_TOGGLE_PATROL);
                Entry curr = all.get(idx);
                all.set(idx, new Entry(curr.entityId(), curr.name(), !curr.patrolling(), curr.squadName()));
                rebuildButtons();
            }).bounds(actionsLeft + BTN_W1 + BTN_GAP, btnY, BTN_W2, BTN_H).build();

            this.addRenderableWidget(select);
            this.addRenderableWidget(summon);
            this.addRenderableWidget(patrol);
        }

        Button prev = Button.builder(Component.literal("<"), b -> {
            if (page > 0) {
                page--;
                rebuildButtons();
            }
        }).bounds(x + 8, y + PANEL_H - 28, 22, 20).build();

        Button next = Button.builder(Component.literal(">"), b -> {
            if ((page + 1) * PER_PAGE < all.size()) {
                page++;
                rebuildButtons();
            }
        }).bounds(x + PANEL_W - 30, y + PANEL_H - 28, 22, 20).build();

        this.addRenderableWidget(prev);
        this.addRenderableWidget(next);

        buildSquadBar(x, y);
    }

    /**
     * Phase 3A's "assign selected guards to a squad" row: a name field plus
     * Assign/Clear, both acting on every currently-{@link #selected} entity id in one
     * {@link AssignSquadC2SPayload}. The typed name is kept in {@link #squadNameDraft}
     * across rebuilds (page changes, toggling other rows) since {@code clearWidgets()}
     * above throws away the previous {@code EditBox} instance every time.
     */
    private void buildSquadBar(int x, int y) {
        int barY = y + SQUAD_BAR_OFFSET;
        int editX = x + MARGIN_L;

        EditBox squadNameBox = new EditBox(this.font, editX, barY, SQUAD_EDIT_W, BTN_H,
                Component.translatable("gui.rallyguard.command.squad_placeholder"));
        squadNameBox.setMaxLength(32);
        squadNameBox.setValue(squadNameDraft);
        // FIX: the real 1.21.1 EditBox method is setHint(Component), not setPlaceholder(...) -
        // an earlier mappings.dev summary invented "setPlaceholder," caught by the actual
        // compiler error (symbol: method setPlaceholder(MutableComponent)) and confirmed
        // against a second, more literal source (a version-pinned NeoForge javadoc mirror)
        // before reapplying the fix.
        squadNameBox.setHint(Component.translatable("gui.rallyguard.command.squad_placeholder"));
        squadNameBox.setResponder(value -> squadNameDraft = value);
        this.addRenderableWidget(squadNameBox);

        int assignX = editX + SQUAD_EDIT_W + BTN_GAP + 4;
        Button assign = Button.builder(Component.translatable("gui.rallyguard.command.squad_assign"), b -> {
            if (!selected.isEmpty() && !squadNameDraft.trim().isEmpty()) {
                sendAssignSquad(squadNameDraft.trim());
            }
        }).bounds(assignX, barY, SQUAD_ASSIGN_W, BTN_H).build();
        this.addRenderableWidget(assign);

        int clearX = assignX + SQUAD_ASSIGN_W + BTN_GAP + 4;
        Button clear = Button.builder(Component.translatable("gui.rallyguard.command.squad_clear"), b -> {
            if (!selected.isEmpty()) {
                sendAssignSquad("");
            }
        }).bounds(clearX, barY, SQUAD_CLEAR_W, BTN_H).build();
        this.addRenderableWidget(clear);
    }

    private void sendAssignSquad(String squadName) {
        PacketDistributor.sendToServer(new AssignSquadC2SPayload(new ArrayList<>(selected), squadName));
    }

    private void sendAction(int entityId, int action) {
        PacketDistributor.sendToServer(new GuardActionC2SPayload(entityId, action));
    }

    /**
     * Don't darken the world behind the screen - same intent as {@link HireGuardScreen}'s
     * override, just on {@code Screen} directly instead of
     * {@code AbstractContainerScreen}.
     */
    @Override
    public void renderBackground(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float delta) {
        drawPanel(g);
        drawRows(g);
        super.render(g, mouseX, mouseY, delta);
    }

    private void drawPanel(GuiGraphics g) {
        int x = (this.width - PANEL_W) / 2;
        int y = (this.height - PANEL_H) / 2;

        int bg = 0xF0101010;
        g.fill(x, y, x + PANEL_W, y + PANEL_H, bg);

        int border = 0xFFFFFFFF;
        g.fill(x, y, x + PANEL_W, y + 1, border);
        g.fill(x, y + PANEL_H - 1, x + PANEL_W, y + PANEL_H, border);
        g.fill(x, y, x + 1, y + PANEL_H, border);
        g.fill(x + PANEL_W - 1, y, x + PANEL_W, y + PANEL_H, border);

        String title = Component.translatable("gui.rallyguard.command.title").getString();
        int titleW = this.font.width(title);
        g.drawString(this.font, title, (this.width - titleW) / 2, y + 8, 0xFFFFFF, true);

        g.drawString(this.font,
                Component.translatable("gui.rallyguard.command.name"),
                x + COL_NAME_X, y + HEADER_Y, 0xCCCCCC, true);

        int groupWidth = BTN_W1 + BTN_GAP + BTN_W2;
        int actionsRight = x + PANEL_W - MARGIN_R;
        int actionsLeft = actionsRight - groupWidth;
        int actionsCenterX = actionsLeft + groupWidth / 2;

        String actions = Component.translatable("gui.rallyguard.command.actions").getString();
        int actionsW = this.font.width(actions);
        g.drawString(this.font, actions, actionsCenterX - (actionsW / 2), y + HEADER_Y, 0xCCCCCC, true);

        g.fill(x + 6, y + HEADER_LINE_Y, x + PANEL_W - 6, y + HEADER_LINE_Y + 1, 0x33FFFFFF);
    }

    private void drawRows(GuiGraphics g) {
        int x = (this.width - PANEL_W) / 2;
        int y = (this.height - PANEL_H) / 2;

        if (all.isEmpty()) {
            String msg = Component.translatable("gui.rallyguard.command.empty").getString();
            int w = this.font.width(msg);
            g.drawString(this.font, msg, (this.width - w) / 2, y + (PANEL_H / 2), 0xAAAAAA, true);
            drawPageIndicator(g, x, y);
            return;
        }

        int start = page * PER_PAGE;
        int end = Math.min(start + PER_PAGE, all.size());

        int groupWidth = BTN_W1 + BTN_GAP + BTN_W2;
        int actionsRight = x + PANEL_W - MARGIN_R;
        int actionsLeft = actionsRight - groupWidth;

        for (int i = start; i < end; i++) {
            Entry e = all.get(i);

            int rowTop = y + ROW_TOP + (i - start) * ROW_HEIGHT;
            int rowMidY = rowTop + (ROW_HEIGHT / 2);
            int rowBot = rowTop + ROW_HEIGHT;

            g.fill(x + 6, rowBot - 1, x + PANEL_W - 6, rowBot, 0x22FFFFFF);

            // Phase 3A: append the guard's squad (if any) right after its name, dimmed,
            // then reuse the exact same truncation logic that already existed for the
            // name alone - it works on any string, not just a bare name.
            String display = e.squadName().isEmpty()
                    ? e.name()
                    : e.name() + " — " + e.squadName();

            int maxNameW = (actionsLeft - 12) - (x + COL_NAME_X);
            if (this.font.width(display) > maxNameW) {
                String ell = "...";
                while (!display.isEmpty() && this.font.width(display + ell) > maxNameW) {
                    display = display.substring(0, display.length() - 1);
                }
                display = display + ell;
            }
            int nameY = rowMidY - (this.font.lineHeight / 2);
            g.drawString(this.font, display, x + COL_NAME_X, nameY, 0xFFFFFF, true);
        }

        drawPageIndicator(g, x, y);
    }

    private void drawPageIndicator(GuiGraphics g, int x, int y) {
        int totalPages = Math.max(1, (all.size() + PER_PAGE - 1) / PER_PAGE);
        String pg = (page + 1) + " / " + totalPages;
        int w = this.font.width(pg);
        g.drawString(this.font, pg, x + (PANEL_W - w) / 2, y + PANEL_H - 24, 0xFFFFFF, true);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}

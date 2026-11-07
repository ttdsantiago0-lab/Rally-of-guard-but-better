package net.hfstack.rallyguard.screen;

import net.hfstack.rallyguard.network.NetworkConstants;
import net.hfstack.rallyguard.network.payload.AssignSquadC2SPayload;
import net.hfstack.rallyguard.network.payload.AssignStanceC2SPayload;
import net.hfstack.rallyguard.network.payload.Destination;
import net.hfstack.rallyguard.network.payload.GuardActionC2SPayload;
import net.hfstack.rallyguard.network.payload.GuardListS2CPayload;
import net.hfstack.rallyguard.network.payload.GuardManageC2SPayload;
import net.hfstack.rallyguard.network.payload.HqActionC2SPayload;
import net.hfstack.rallyguard.network.payload.LedgerPageC2SPayload;
import net.hfstack.rallyguard.network.payload.SetSelectionC2SPayload;
import net.hfstack.rallyguard.network.payload.IssueAttackOrderC2SPayload;
import net.hfstack.rallyguard.network.payload.IssueKamikazeOrderC2SPayload;
import net.hfstack.rallyguard.network.payload.IssueMusterOrderC2SPayload;
import net.hfstack.rallyguard.network.payload.IssueOrderC2SPayload;
import net.hfstack.rallyguard.network.payload.IssueRetreatOrderC2SPayload;
import net.hfstack.rallyguard.network.payload.SquadActionC2SPayload;
import net.hfstack.rallyguard.util.SquadColors;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
 * <p>
 * Phase 3A.1 (post in-game-test follow-up) adds a small "▾" picker button next to
 * the squad name field: real testing surfaced that typing a squad name by hand risks a
 * typo silently creating a second, different squad instead of reusing the intended one.
 * The button cycles {@link #squadNameDraft} through every distinct squad name already
 * visible in the current guard list ({@link #knownSquadNames()}) - no new network
 * payload, just reusing data the client already has.
 * <p>
 * Phase 3D.1 (2026-09-25, Ledger convenience) makes that same "▾" picker also
 * replace the current {@link #selected} set with every online guard in the squad it
 * lands on - see {@link #cycleSquadPicker} for the full reasoning. Picking a squad and
 * acting on it (Muster it, set its Stance, issue it an Order) is now one click instead
 * of manually ticking every row.
 * <p>
 * Phase 3E ("Commander's Intent"):
 * <ul>
 *     <li>A target row: a selector cycling through the server-supplied target options
 *     (nearest hostile, counterattack, the entity marked with the Ledger, each other
 *     player in the dimension, no target), then Attack / Hold Fire / Release Fire for
 *     the selected guards. The Order row became Recall / Hold Ground / Reform / Break
 *     Ranks (Attack moved to the target row, since it now needs a target).</li>
 *     <li>Rows show each guard's squad in its squad color (same color as its world
 *     name tag), a red HOLD FIRE flag, and its designated target.</li>
 *     <li>The panel sizes itself to the screen: as many guard rows per page as fit
 *     (the fixed 316px panel overflowed at GUI scale 4 on a 1080p screen).</li>
 *     <li>Selection, page, squad draft and target choice survive the refresh the server
 *     sends after every order - before, every order reopened a fresh screen with nothing
 *     selected, so giving the same squad two orders in a row meant reselecting it.</li>
 *     <li>Clarity pass (user priority: "extremely useful, not confusing"): click a row to
 *     select it (check box + tint); header chips select All / None / a whole squad;
 *     every button has a hover tooltip saying exactly what it does; buttons that act on
 *     the selection are disabled while nothing is selected; rows are sorted (loaded
 *     guards first, grouped by squad) and show how far away each guard is - or its
 *     dimension - so guards of villages thousands of blocks apart are easy to tell
 *     apart; the per-row patrol button reads Post here / Release.</li>
 * </ul>
 */
public class GuardCommandScreen extends Screen {

    public record Entry(int entityId, String name, boolean patrolling, String squadName,
                         String mount, String armor, String stance, boolean holdFire, String target,
                         int distance, String dimension, String guardId, String ward,
                         String fireMode, String pack, String command) {

        /** E2A: veterancy rank 0..5 (0 when unknown). */
        public int rank() {
            if (command == null || command.isEmpty()) return 0;
            try {
                return Integer.parseInt(command.split("\\|", -1)[0]);
            } catch (NumberFormatException e) {
                return 0;
            }
        }

        /** E2A: the computed captain of its squad/unit. */
        public boolean captain() {
            String[] p = command == null ? new String[0] : command.split("\\|", -1);
            return p.length > 1 && "1".equals(p[1]);
        }

        /** E2A block 5: "m" melee, "r" bow/crossbow, "g" gun, "h" mounted melee ("" unknown). */
        public String role() {
            String[] p = command == null ? new String[0] : command.split("\\|", -1);
            return p.length > 3 ? p[3] : "";
        }

        /** E3A V: field {@code i} of the command tag ("" when absent). */
        public String field(int i) {
            String[] p = command == null ? new String[0] : command.split("\\|", -1);
            return p.length > i ? p[i] : "";
        }

        /** E2A block 5: the guard's doctrine (Tactics), "" unknown. */
        public String doctrine() {
            String[] p = command == null ? new String[0] : command.split("\\|", -1);
            return p.length > 4 ? p[4] : "";
        }

        /** E2A: the server's configured rank name, "" for the translated default. */
        public String rankName() {
            String[] p = command == null ? new String[0] : command.split("\\|", -1);
            return p.length > 2 ? p[2] : "";
        }
        /** Mirrors {@code GuardListS2CPayload.Entry.OFFLINE_ENTITY_ID} (Phase 3B) - a
         *  row with this entityId has no live entity behind it (a durable
         *  {@code GuardRoster} snapshot only), so nothing here can select/act on it. */
        public static final int OFFLINE_ENTITY_ID = -1;

        public boolean isOnline() {
            return entityId != OFFLINE_ENTITY_ID;
        }
    }

    private static final String[] ROMAN = {"", "I", "II", "III", "IV", "V"};

    private final List<Entry> all = new ArrayList<>();
    private final Set<Integer> selected = new LinkedHashSet<>();
    private int page = 0;
    private String squadNameDraft = "";
    /** Phase 3A.1: which entry {@link #knownSquadNames()} the picker button last landed
     *  on, so repeated clicks cycle forward through the player's existing squads instead
     *  of always jumping back to the first one. -1 means "not cycling yet." */
    private int squadPickerIndex = -1;

    /** Phase 3E: the target selector's options (from the server) and the chosen option's
     *  id - kept by id, not index, so it survives a refresh that reorders the list. */
    private final List<GuardListS2CPayload.TargetOption> targets = new ArrayList<>();
    private String targetId = "NEAREST";
    /** "" = Muster/Retreat anchor on the commander, else the marked position "x y z". */
    private String anchor = "";

    /** Phase 3G: the Ledger has two views over the same army - individual guards, and
     *  squads (group management: select / rename / disband a whole squad at once). The
     *  command rows at the bottom act on the selection in both. */
    private static final int TAB_GUARDS = 0;
    private static final int TAB_SQUADS = 1;
    /** E1-C: the commander's headquarters. */
    private static final int TAB_HQ = 2;
    private final List<GuardListS2CPayload.HqInfo> hqs = new ArrayList<>();
    private String selectedHq = "";
    private String pendingHqDelete = null;
    /** E1-E: which server batch of rows is shown (armies bigger than one batch). */
    private int batch;
    private int batches = 1;
    private int tab = TAB_GUARDS;
    /** Squad whose Disband button was clicked once and now asks for confirmation. */
    private String pendingDisband = null;
    /** Phase 3H: guard (UUID) whose Forget button was clicked once. */
    private String pendingForget = null;
    /** Phase 3H: the Guards tab shows only this squad or "squad/unit" ("" = everyone). */
    private String filterPath = "";
    /** What the server last knew as the selection - changes are sent on rebuild. */
    private Set<Integer> lastSentSelection = new LinkedHashSet<>();

    /** One row of the Squads tab, summarized client-side from the guard list. */
    private record SquadSummary(String name, int total, int online, int nearest, String hq) {
        boolean isUnit() {
            return name.contains("/");
        }
    }

    private static final int PANEL_W = 440;
    // Phase 3E: the panel height and rows per page are computed in init() from the
    // screen height (see layout()), instead of the fixed 316px / 4 rows of Phase 3D.
    private static final int MIN_PER_PAGE = 2;
    private static final int MAX_PER_PAGE = 8;
    private static final int BAR_STEP = 24;
    private static final int PAGER_H = 26;
    /** E3B G4: one more row on top of the bars for the group card. */
    private static final int BAR_COUNT = 6;
    private int perPage = 4;
    private int panelH = 316;

    private static final int MARGIN_L = 16;
    private static final int MARGIN_R = 16;

    private static final int HEADER_LINE_Y = 38;
    /** Phase 3E: the selection chips (All / None / squads) sit in the old header row. */
    private static final int CHIP_Y = 20;
    private static final int CHIP_H = 16;

    private static final int ROW_TOP = 38;
    private static final int ROW_HEIGHT = 22;

    private static final int SELECT_W = 16;
    private static final int SELECT_GAP = 6;
    private static final int COL_NAME_X = MARGIN_L + SELECT_W + SELECT_GAP;

    private static final int BTN_W1 = 80;
    private static final int PACK_BTN_W = 20;
    private static final int BTN_W2 = 80;
    private static final int BTN_H = 18;
    private static final int BTN_GAP = 2;

    private static final int SQUAD_EDIT_W = 178;
    private static final int SQUAD_PICK_W = 22;
    private static final int SQUAD_ASSIGN_W = 90;
    private static final int SQUAD_CLEAR_W = 70;

    /** E2A block 5: the four bars above the squad bar are grouped - Style (2), Attack (3),
     *  Formation (4), Position (5) - each with a caption on the left; their buttons share
     *  the width right of {@link #BAR_LEFT} equally ({@link #barSlots}). */
    /** Phase 3E: the target selector at the start of the Attack bar. */
    private static final int TARGET_SELECT_W = 112;
    private static final int TAB_W = 70;
    /** E2A block 5: bars start right of their group caption. */
    private static final int BAR_LEFT = 64;
    private static final int SQUAD_ATTACK_W = 20;

    /** Bars stack upward from the pager at the panel's bottom edge: squad (1), style
     *  (2), attack (3), formation (4), position (5). */
    private int barOffset(int indexFromBottom) {
        return panelH - PAGER_H - indexFromBottom * BAR_STEP;
    }

    /** Fits as many guard rows as the screen allows (within [MIN, MAX]). */
    private void layout() {
        int fixed = ROW_TOP + 6 + PAGER_H + BAR_COUNT * BAR_STEP;
        int fit = (this.height - 8 - fixed) / ROW_HEIGHT;
        perPage = Math.max(MIN_PER_PAGE, Math.min(MAX_PER_PAGE, fit));
        panelH = fixed + perPage * ROW_HEIGHT;
        int lastPage = Math.max(0, (listSize() - 1) / perPage);
        page = Math.min(page, lastPage);
    }

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
    /** E3B R6 (B9): client time the player last asked for the Ledger (J, the item); refreshes never open it. */
    private static long requestedAt = Long.MIN_VALUE / 2;

    public static void markRequested() {
        requestedAt = System.currentTimeMillis();
    }

    public static void openFromPayload(GuardListS2CPayload payload) {
        // E3B R6 (B9): a list that arrives while the Ledger is closed only opens it if the player asked for it
        // in the last 5 s - a late refresh never pops the screen over the game (or over another screen).
        boolean open = Minecraft.getInstance().screen instanceof GuardCommandScreen;
        if (!open && System.currentTimeMillis() - requestedAt > 5000L) return;
        if (!open) requestedAt = Long.MIN_VALUE / 2;
        List<Entry> list = new ArrayList<>(payload.entries().size());
        for (GuardListS2CPayload.Entry e : payload.entries()) {
            list.add(new Entry(e.entityId(), e.name(), e.patrolling(), e.squadName(),
                    e.condition().mount(), e.condition().armor(), e.status().stance(),
                    e.status().holdFire(), e.status().target(), e.status().distance(), e.status().dimension(),
                    e.condition().guardId(), e.status().ward(), e.condition().fireMode(), e.condition().pack(),
                    e.condition().command()));
        }
        // Phase 3E: guards you can act on first, grouped by squad (unsquadded last).
        // Phase 3H (user review: "no por orden de creación"): within a squad/unit, the
        // nearest guard first - the one in front of you is at the top of its group.
        list.sort(Comparator.comparing((Entry e) -> !e.isOnline())
                .thenComparing(e -> e.squadName().isEmpty())
                .thenComparing(e -> e.squadName().toLowerCase(Locale.ROOT))
                .thenComparingInt(e -> e.distance() < 0 ? Integer.MAX_VALUE : e.distance())
                .thenComparing(e -> e.name().toLowerCase(Locale.ROOT)));
        GuardCommandScreen screen = new GuardCommandScreen(list);
        screen.targets.addAll(payload.targets());
        for (GuardListS2CPayload.SquadInfo info : payload.squads()) {
            screen.serverSquads.add(new SquadSummary(info.path(), info.total(), info.online(), info.nearest(), info.hq()));
        }
        screen.omitted = payload.omitted();
        screen.anchor = payload.extras().anchor();
        screen.hqs.addAll(payload.extras().hqs());
        screen.selectedHq = payload.extras().selectedHq();
        screen.batch = payload.extras().batch();
        screen.batches = payload.extras().batches();

        // Phase 3E: a refresh (the server re-sends the list after every order) keeps the
        // commander's working state instead of starting from a blank screen.
        // Phase 3H: the selection is the SERVER's (shared with the hotkeys and shown over
        // the guards in the world), so it is always taken from the payload.
        for (Entry e : list) {
            if (e.isOnline() && payload.selectedIds().contains(e.entityId())) {
                screen.selected.add(e.entityId());
            }
        }
        screen.lastSentSelection = new LinkedHashSet<>(screen.selected);
        if (Minecraft.getInstance().screen instanceof GuardCommandScreen previous) {
            screen.filterPath = previous.filterPath;
            screen.page = previous.page;
            screen.squadNameDraft = previous.squadNameDraft;
            screen.squadPickerIndex = previous.squadPickerIndex;
            screen.targetId = previous.targetId;
            screen.tab = previous.tab;
        } else if (!screen.selected.isEmpty()) {
            // Phase 3H.1 (user: "ver en el Ledger el guardia seleccionado con G"): opening
            // the Ledger with a selection puts the selected guards on the first page.
            screen.all.sort(Comparator.comparing((Entry e) -> !screen.selected.contains(e.entityId())));
        }
        if (screen.targets.stream().noneMatch(t -> t.id().equals(screen.targetId)) && !screen.targets.isEmpty()) {
            screen.targetId = screen.targets.get(0).id();
        }
        Minecraft.getInstance().setScreen(screen);
    }

    @Override
    protected void init() {
        super.init();
        layout();
        rebuildButtons();
    }

    private void rebuildButtons() {
        this.clearWidgets();

        int x = (this.width - PANEL_W) / 2;
        int y = (this.height - panelH) / 2;

        buildTabs(x, y);

        List<Entry> rows = visible();
        int start = page * perPage;
        int end = Math.min(start + perPage, rows.size());

        int groupWidth = BTN_W1 + BTN_GAP + BTN_W2;
        int actionsRight = x + PANEL_W - MARGIN_R;
        int actionsLeft = actionsRight - groupWidth;

        if (tab == TAB_SQUADS) {
            buildSquadRows(x, y, actionsLeft);
            end = start; // no guard rows in this view
        } else if (tab == TAB_HQ) {
            buildHqRows(x, y, actionsLeft);
            end = start;
        } else {
            buildSelectionChips(x, y);
        }

        for (int i = start; i < end; i++) {
            Entry e = rows.get(i);

            int rowTop = y + ROW_TOP + (i - start) * ROW_HEIGHT;
            int rowMidY = rowTop + (ROW_HEIGHT / 2);
            int btnY = rowMidY - (BTN_H / 2);

            if (!e.isOnline()) {
                // Phase 3B: a roster-only (offline) row has no live entity behind it, so
                // Summon/Post have nothing to act on. Phase 3H: it gets Forget instead
                // (asks once) - the way to clear the "cemetery" of guards that are gone
                // (died unloaded, killed by a command, deleted with a chunk...).
                if (e.guardId() != null && !e.guardId().isEmpty()) {
                    boolean confirming = e.guardId().equals(pendingForget);
                    Component label = confirming
                            ? Component.translatable("gui.rallyguard.command.forget_confirm").withStyle(ChatFormatting.RED)
                            : Component.translatable("gui.rallyguard.command.forget");
                    addButton(label, "forget", false, b -> {
                        if (e.guardId().equals(pendingForget)) {
                            pendingForget = null;
                            PacketDistributor.sendToServer(new GuardManageC2SPayload(e.guardId(), "FORGET", ""));
                        } else {
                            pendingForget = e.guardId();
                            rebuildButtons();
                        }
                    }, actionsLeft + BTN_W1 + BTN_GAP, btnY, BTN_W2);
                }
                continue;
            }

            // Phase 3E: selecting is done by clicking the row itself (see mouseClicked),
            // so the old 16px "x" toggle button is gone.
            // E1-A: the guard's war pack (opens a 6-slot container; the guard must be near).
            addButton(Component.literal("▤"), "pack", false,
                    b -> PacketDistributor.sendToServer(new GuardManageC2SPayload(e.guardId(), "PACK", "")),
                    actionsLeft, btnY, PACK_BTN_W);
            // E3A F8: mount / dismount (its own mount if free, else the nearest free one).
            addButton(Component.literal(e.mount() != null && !e.mount().isEmpty() ? "⇩" : "♞"), "mount_toggle", false,
                    b -> PacketDistributor.sendToServer(new GuardManageC2SPayload(e.guardId(), "MOUNT", "")),
                    actionsLeft - PACK_BTN_W - BTN_GAP, btnY, PACK_BTN_W);
            addButton(Component.translatable("gui.rallyguard.command.summon"), "summon", false,
                    b -> sendAction(e.entityId(), NetworkConstants.ACTION_SUMMON),
                    actionsLeft + PACK_BTN_W + BTN_GAP, btnY, BTN_W1 - PACK_BTN_W - BTN_GAP);

            Component label = e.patrolling()
                    ? Component.translatable("gui.rallyguard.command.stop")
                    : Component.translatable("gui.rallyguard.command.patrol");
            addButton(label, e.patrolling() ? "unpost" : "post", false, b -> {
                sendAction(e.entityId(), NetworkConstants.ACTION_TOGGLE_PATROL);
                int idx = all.indexOf(e);
                if (idx >= 0) {
                    all.set(idx, new Entry(e.entityId(), e.name(), !e.patrolling(), e.squadName(),
                            e.mount(), e.armor(), e.stance(), e.holdFire(), e.target(),
                            e.distance(), e.dimension(), e.guardId(), e.ward(), e.fireMode(), e.pack(), e.command()));
                }
                rebuildButtons();
            }, actionsLeft + BTN_W1 + BTN_GAP, btnY, BTN_W2);
        }

        Button prev = Button.builder(Component.literal("<"), b -> {
            if (page > 0) {
                page--;
                rebuildButtons();
            }
        }).bounds(x + 8, y + panelH - 24, 22, 20).build();

        Button next = Button.builder(Component.literal(">"), b -> {
            if ((page + 1) * perPage < listSize()) {
                page++;
                rebuildButtons();
            }
        }).bounds(x + PANEL_W - 30, y + panelH - 24, 22, 20).build();

        this.addRenderableWidget(prev);
        this.addRenderableWidget(next);

        if (batches > 1 && tab == TAB_GUARDS) {
            Button prevBatch = Button.builder(Component.literal("«"), b -> {
                if (batch > 0) PacketDistributor.sendToServer(new LedgerPageC2SPayload(batch - 1));
            }).bounds(x + 34, y + panelH - 24, 16, 20)
                    .tooltip(Tooltip.create(Component.translatable("gui.rallyguard.tip.batch"))).build();
            Button nextBatch = Button.builder(Component.literal("»"), b -> {
                if (batch + 1 < batches) PacketDistributor.sendToServer(new LedgerPageC2SPayload(batch + 1));
            }).bounds(x + 52, y + panelH - 24, 16, 20)
                    .tooltip(Tooltip.create(Component.translatable("gui.rallyguard.tip.batch"))).build();
            prevBatch.active = batch > 0;
            nextBatch.active = batch + 1 < batches;
            this.addRenderableWidget(prevBatch);
            this.addRenderableWidget(nextBatch);
        }

        buildSquadBar(x, y);
        buildStanceBar(x, y);
        buildOrderBar(x, y);
        buildTargetBar(x, y);
        buildMusterBar(x, y);
        buildCardBar(x, y);

        // Phase 3H: every selection change (rows, chips, squads) reaches the server, where
        // the hotkeys use it and the selected guards get their marker in the world.
        if (!selected.equals(lastSentSelection)) {
            lastSentSelection = new LinkedHashSet<>(selected);
            PacketDistributor.sendToServer(new SetSelectionC2SPayload(selectionIds()));
        }
    }

    /** The selection as sent to the server - never more ids than one C2S payload may
     *  carry (the order payloads' lists are capped at 4096; more would be refused). */
    private List<Integer> selectionIds() {
        List<Integer> ids = new ArrayList<>(Math.min(selected.size(), MAX_SEND));
        for (int id : selected) {
            if (ids.size() >= MAX_SEND) break;
            ids.add(id);
        }
        return ids;
    }

    private static final int MAX_SEND = 4096;

    /**
     * Phase 3H: the Guards tab's rows - one squad / unit (see filterPath), or with no filter
     * only the guards in NO squad (E2A B8, FEEDBACK_E1 B8: a squad member is listed under
     * Squads, not twice; opening its squad from the Squads tab shows it here filtered).
     */
    private List<Entry> visible() {
        List<Entry> out = new ArrayList<>();
        for (Entry e : all) {
            if (filterPath.isEmpty() ? isUnsquadded(e) : inGroup(e, filterPath)) out.add(e);
        }
        return out;
    }

    private static boolean isUnsquadded(Entry e) {
        return e.squadName() == null || e.squadName().isBlank();
    }

    private int unsquaddedCount() {
        int n = 0;
        for (Entry e : all) if (isUnsquadded(e)) n++;
        return n;
    }

    /** True if the guard is in the squad (any unit) or in exactly that "squad/unit". */
    private static boolean inGroup(Entry e, String path) {
        String own = e.squadName() == null ? "" : e.squadName().toLowerCase(Locale.ROOT);
        String p = path.toLowerCase(Locale.ROOT);
        return !p.isEmpty() && (own.equals(p) || own.startsWith(p + "/"));
    }

    /** "Squad › Unit" for display. */
    private static String prettyPath(String path) {
        return path == null ? "" : path.replace("/", " › ");
    }

    /** The squad part of a path - its color is the squad's. */
    private static String topSquad(String path) {
        int i = path == null ? -1 : path.indexOf('/');
        return i < 0 ? (path == null ? "" : path) : path.substring(0, i);
    }

    /**
     * Phase 3E: every command button goes through here, so every one of them explains
     * itself on hover ({@code gui.rallyguard.tip.<tipKey>}) and every button that acts on
     * the selection is visibly disabled while nothing is selected - the two things that
     * made the Ledger confusing ("what does this do?" / "why did nothing happen?").
     */
    private Button addButton(Component label, String tipKey, boolean needsSelection, Button.OnPress onPress,
                             int bx, int by, int w) {
        Button button = Button.builder(label, onPress)
                .bounds(bx, by, w, BTN_H)
                .tooltip(Tooltip.create(Component.translatable("gui.rallyguard.tip." + tipKey)))
                .build();
        if (needsSelection) {
            button.active = !selected.isEmpty();
        }
        return this.addRenderableWidget(button);
    }

    /**
     * Phase 3E: quick selection in the header - "All" (every guard currently loaded near
     * you), "None", then one colored chip per squad (every loaded member of that squad;
     * also puts the squad's name in the squad field). Chips that don't fit are simply not
     * shown - the squad field's cycle button still reaches every squad.
     */
    private void buildSelectionChips(int x, int y) {
        int cx = x + MARGIN_L;
        int maxX = x + PANEL_W - MARGIN_R;

        if (!filterPath.isEmpty()) {
            Component label = Component.literal("✕ ").withStyle(ChatFormatting.RED)
                    .append(Component.literal(prettyPath(filterPath)).withStyle(SquadColors.colorFor(topSquad(filterPath))));
            cx = addChip(label, "filter_clear", cx, y, maxX, () -> {
                filterPath = "";
                page = 0;
            });
        }
        cx = addChip(Component.translatable("gui.rallyguard.command.select_all"), "select_all", cx, y, maxX, () -> {
            selected.clear();
            for (Entry e : visible()) {
                if (e.isOnline()) selected.add(e.entityId());
            }
        });
        cx = addChip(Component.translatable("gui.rallyguard.command.select_none"), "select_none", cx, y, maxX,
                selected::clear);

        // Phase 3H.1: inside a squad's view the chips are that squad's UNITS (select one
        // with a click); everywhere else they are the squads.
        String viewSquad = topSquad(filterPath);
        if (!filterPath.isEmpty()) {
            for (String unitPath : knownSquadNames()) {
                if (!unitPath.contains("/") || !topSquad(unitPath).equalsIgnoreCase(viewSquad)) continue;
                Component label = Component.literal("└ ").withStyle(ChatFormatting.DARK_GRAY)
                        .append(Component.literal(unitPath.substring(unitPath.indexOf('/') + 1)).withStyle(ChatFormatting.WHITE));
                int next = addChip(label, "select_unit", cx, y, maxX, () -> {
                    selectSquad(unitPath);
                    squadNameDraft = unitPath.substring(unitPath.indexOf('/') + 1);
                });
                if (next == cx) break;
                cx = next;
            }
            return;
        }
        for (String squad : knownTopSquads()) {
            Component label = Component.literal("■ ").withStyle(SquadColors.colorFor(squad))
                    .append(Component.literal(squad).withStyle(ChatFormatting.WHITE));
            int next = addChip(label, "select_squad", cx, y, maxX, () -> selectSquad(squad));
            if (next == cx) break; // out of room
            cx = next;
        }
    }

    /** Adds one header chip if it fits before {@code maxX}; returns the next x (or the
     *  same x if it didn't fit). */
    private int addChip(Component label, String tipKey, int cx, int y, int maxX, Runnable action) {
        int w = Math.min(90, this.font.width(label) + 10);
        if (cx + w > maxX) return cx;
        Button chip = Button.builder(label, b -> {
                    action.run();
                    rebuildButtons();
                })
                .bounds(cx, y + CHIP_Y, w, CHIP_H)
                .tooltip(Tooltip.create(Component.translatable("gui.rallyguard.tip." + tipKey)))
                .build();
        this.addRenderableWidget(chip);
        return cx + w + BTN_GAP;
    }

    /** Guards / Squads tab buttons in the title row (the active one is shown pressed). */
    private void buildTabs(int x, int y) {
        Button guards = Button.builder(Component.translatable("gui.rallyguard.command.tab_guards", unsquaddedCount()), b -> switchTab(TAB_GUARDS))
                .bounds(x + 6, y + 4, TAB_W, CHIP_H)
                .tooltip(Tooltip.create(Component.translatable("gui.rallyguard.tip.tab_guards")))
                .build();
        guards.active = tab != TAB_GUARDS;
        this.addRenderableWidget(guards);

        Button squads = Button.builder(Component.translatable("gui.rallyguard.command.tab_squads", squadSummaries().size()), b -> switchTab(TAB_SQUADS))
                .bounds(x + 6 + TAB_W + BTN_GAP, y + 4, TAB_W, CHIP_H)
                .tooltip(Tooltip.create(Component.translatable("gui.rallyguard.tip.tab_squads")))
                .build();
        squads.active = tab != TAB_SQUADS;
        this.addRenderableWidget(squads);

        Button hqTab = Button.builder(Component.translatable("gui.rallyguard.command.tab_hq", hqs.size()), b -> switchTab(TAB_HQ))
                .bounds(x + 6 + 2 * (TAB_W + BTN_GAP), y + 4, TAB_W, CHIP_H)
                .tooltip(Tooltip.create(Component.translatable("gui.rallyguard.tip.tab_hq")))
                .build();
        hqTab.active = tab != TAB_HQ;
        this.addRenderableWidget(hqTab);
    }

    /**
     * E1-C HQ tab: one row per HQ (name, position, squads using it). Row click or "Pick"
     * makes it the selected HQ - a banner you sneak-command then becomes its position.
     * Assign gives it to the squad named in the field; ✎ renames it to the field's text;
     * ✕ deletes it (asks once). The header chip creates a new HQ where you stand.
     */
    private void buildHqRows(int x, int y, int actionsLeft) {
        addChip(Component.translatable("gui.rallyguard.hq.create_here"), "hq_create", x + MARGIN_L, y,
                x + PANEL_W - MARGIN_R, () -> PacketDistributor.sendToServer(
                        new HqActionC2SPayload("CREATE", "", squadNameDraft.trim())));
        int start = page * perPage;
        int end = Math.min(start + perPage, hqs.size());
        int w = (BTN_W1 + BTN_GAP + BTN_W2 - 3 * BTN_GAP) / 4;
        String draft = squadNameDraft.trim();
        for (int i = start; i < end; i++) {
            GuardListS2CPayload.HqInfo hq = hqs.get(i);
            int rowTop = y + ROW_TOP + (i - start) * ROW_HEIGHT;
            int btnY = rowTop + (ROW_HEIGHT / 2) - (BTN_H / 2);
            boolean picked = hq.id().equals(selectedHq);
            int bx = actionsLeft;
            addButton(Component.literal(picked ? "✓" : "◯"), "hq_select", false,
                    b -> PacketDistributor.sendToServer(new HqActionC2SPayload("SELECT", picked ? "" : hq.id(), "")),
                    bx, btnY, w);
            bx += w + BTN_GAP;
            Button assign = addButton(Component.translatable("gui.rallyguard.hq.assign"), "hq_assign", false,
                    b -> PacketDistributor.sendToServer(new HqActionC2SPayload("ASSIGN", hq.id(), topSquad(draft))),
                    bx, btnY, w);
            assign.active = !draft.isEmpty();
            bx += w + BTN_GAP;
            Button rename = addButton(Component.literal("✎"), "hq_rename", false,
                    b -> PacketDistributor.sendToServer(new HqActionC2SPayload("RENAME", hq.id(), draft)),
                    bx, btnY, w);
            rename.active = !draft.isEmpty();
            bx += w + BTN_GAP;
            boolean confirming = hq.id().equals(pendingHqDelete);
            addButton(confirming ? Component.literal("?").withStyle(ChatFormatting.RED) : Component.literal("✕"), "hq_delete", false, b -> {
                if (hq.id().equals(pendingHqDelete)) {
                    pendingHqDelete = null;
                    PacketDistributor.sendToServer(new HqActionC2SPayload("DELETE", hq.id(), ""));
                } else {
                    pendingHqDelete = hq.id();
                    rebuildButtons();
                }
            }, bx, btnY, w);
        }
    }

    private void drawHqRows(GuiGraphics g, int x, int y) {
        if (hqs.isEmpty()) {
            String msg = Component.translatable("gui.rallyguard.hq.none").getString();
            g.drawString(this.font, msg, (this.width - this.font.width(msg)) / 2, y + ROW_TOP + ROW_HEIGHT, 0xAAAAAA, true);
            return;
        }
        int actionsLeft = x + PANEL_W - MARGIN_R - (BTN_W1 + BTN_GAP + BTN_W2);
        int start = page * perPage;
        int end = Math.min(start + perPage, hqs.size());
        for (int i = start; i < end; i++) {
            GuardListS2CPayload.HqInfo hq = hqs.get(i);
            int rowTop = y + ROW_TOP + (i - start) * ROW_HEIGHT;
            int rowMidY = rowTop + ROW_HEIGHT / 2;
            g.fill(x + 6, rowTop + ROW_HEIGHT - 1, x + PANEL_W - 6, rowTop + ROW_HEIGHT, 0x22FFFFFF);
            if (hq.id().equals(selectedHq)) g.fill(x + 6, rowTop, x + PANEL_W - 6, rowTop + ROW_HEIGHT - 1, 0x22FFFF55);
            String dim = hq.dimension().substring(hq.dimension().indexOf(':') + 1).replace('_', ' ');
            String text = ChatFormatting.YELLOW + "⚑ " + ChatFormatting.WHITE + hq.name() + ChatFormatting.GRAY + "  " + hq.pos()
                    + (dim.equals("overworld") ? "" : " (" + dim + ")")
                    + (hq.squads().isEmpty() ? "" : ChatFormatting.DARK_GRAY + "  · " + hq.squads());
            int maxW = actionsLeft - 8 - (x + MARGIN_L);
            while (this.font.width(text) > maxW && text.length() > 4) text = text.substring(0, text.length() - 1);
            g.drawString(this.font, text, x + MARGIN_L, rowMidY - this.font.lineHeight / 2, 0xFFFFFF, true);
        }
    }

    private void switchTab(int newTab) {
        tab = newTab;
        page = 0;
        pendingDisband = null;
        pendingForget = null;
        pendingHqDelete = null;
        rebuildButtons();
    }

    private int listSize() {
        if (tab == TAB_HQ) return hqs.size();
        return tab == TAB_SQUADS ? squadSummaries().size() : visible().size();
    }

    /** Phase 3H.1: the squads and their units, totals computed by the server over the
     *  whole army (the row list may be capped). Each squad is followed by its units. */
    private final List<SquadSummary> serverSquads = new ArrayList<>();
    /** Rows the server left out (army bigger than GuardListS2CPayload.MAX_ROWS). */
    private int omitted;

    private List<SquadSummary> orderedSquads;

    private List<SquadSummary> squadSummaries() {
        if (orderedSquads == null) orderedSquads = orderSquads(); // once per payload, not per frame
        return orderedSquads;
    }

    private List<SquadSummary> orderSquads() {
        Map<String, List<SquadSummary>> unitsByTop = new LinkedHashMap<>();
        for (SquadSummary sq : serverSquads) {
            if (sq.isUnit()) unitsByTop.computeIfAbsent(topSquad(sq.name()).toLowerCase(Locale.ROOT), k -> new ArrayList<>()).add(sq);
        }
        List<SquadSummary> tops = new ArrayList<>();
        for (SquadSummary sq : serverSquads) if (!sq.isUnit()) tops.add(sq);
        List<SquadSummary> ordered = new ArrayList<>();
        for (SquadSummary top : tops) {
            ordered.add(top);
            List<SquadSummary> units = unitsByTop.getOrDefault(top.name().toLowerCase(Locale.ROOT), List.of());
            units.stream().sorted(Comparator.comparing(u -> u.name().toLowerCase(Locale.ROOT))).forEach(ordered::add);
        }
        return ordered;
    }

    /**
     * Phase 3G Squads tab: one row per squad - and since 3H one indented row per unit
     * under it. Clicking the row selects every loaded member; View opens the Guards tab
     * showing only that squad/unit (individual access); Rename (to the name typed in the
     * squad field below) and Disband (asks once) act by name on the server, so they also
     * cover members that are far away and unloaded. Disbanding a unit keeps its guards in
     * the squad.
     */
    private void buildSquadRows(int x, int y, int actionsLeft) {
        List<SquadSummary> squads = squadSummaries();
        int start = page * perPage;
        int end = Math.min(start + perPage, squads.size());
        int w = (BTN_W1 + BTN_GAP + BTN_W2 - 2 * BTN_GAP) / 3;
        for (int i = start; i < end; i++) {
            SquadSummary squad = squads.get(i);
            int rowTop = y + ROW_TOP + (i - start) * ROW_HEIGHT;
            int btnY = rowTop + (ROW_HEIGHT / 2) - (BTN_H / 2);

            addButton(Component.translatable("gui.rallyguard.command.squad_view"), "squad_view", false, b -> {
                filterPath = squad.name();
                selectSquad(squad.name());
                switchTab(TAB_GUARDS);
            }, actionsLeft, btnY, w);

            // E2A block 5: attack with the WHOLE squad in one click, at the target chosen in
            // the Attack bar (its online members; the server spreads them over the target).
            Button attack = addButton(Component.literal("⚔"), "squad_attack", false, b -> {
                selectSquad(squad.name());
                if (!selected.isEmpty()) {
                    PacketDistributor.sendToServer(new IssueAttackOrderC2SPayload(selectionIds(), targetId));
                }
            }, actionsLeft - SQUAD_ATTACK_W - BTN_GAP, btnY, SQUAD_ATTACK_W);
            attack.active = squad.online() > 0;

            String draft = squadNameDraft.trim();
            String ownName = squad.isUnit() ? squad.name().substring(squad.name().indexOf('/') + 1) : squad.name();
            Button rename = addButton(Component.translatable("gui.rallyguard.command.squad_rename"), "squad_rename", false, b -> {
                PacketDistributor.sendToServer(new SquadActionC2SPayload(squad.name(), "RENAME", squadNameDraft.trim()));
            }, actionsLeft + w + BTN_GAP, btnY, w);
            rename.active = !draft.isEmpty() && !draft.equalsIgnoreCase(ownName) && !draft.equalsIgnoreCase(squad.name());

            boolean confirming = squad.name().equals(pendingDisband);
            Component disbandLabel = confirming
                    ? Component.translatable("gui.rallyguard.command.squad_disband_confirm").withStyle(ChatFormatting.RED)
                    : Component.translatable("gui.rallyguard.command.squad_disband");
            addButton(disbandLabel, "squad_disband", false, b -> {
                if (squad.name().equals(pendingDisband)) {
                    pendingDisband = null;
                    PacketDistributor.sendToServer(new SquadActionC2SPayload(squad.name(), "DISBAND", ""));
                } else {
                    pendingDisband = squad.name();
                    rebuildButtons();
                }
            }, actionsLeft + 2 * (w + BTN_GAP), btnY, w);
        }
    }

    private void selectSquad(String squad) {
        squadNameDraft = squad;
        selected.clear();
        for (Entry e : all) {
            if (e.isOnline() && inGroup(e, squad)) {
                selected.add(e.entityId());
            }
        }
    }

    /** Phase 3E: clicking anywhere on a guard's row (left of its buttons) selects or
     *  deselects it - a far bigger target than the old 16px toggle. */
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (super.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }
        int x = (this.width - PANEL_W) / 2;
        int y = (this.height - panelH) / 2;
        int actionsLeft = x + PANEL_W - MARGIN_R - (BTN_W1 + BTN_GAP + BTN_W2);
        int start = page * perPage;
        if (tab == TAB_SQUADS) {
            List<SquadSummary> squads = squadSummaries();
            int end = Math.min(start + perPage, squads.size());
            for (int i = start; i < end; i++) {
                int rowTop = y + ROW_TOP + (i - start) * ROW_HEIGHT;
                if (mouseY >= rowTop && mouseY < rowTop + ROW_HEIGHT
                        && mouseX >= x + MARGIN_L && mouseX < actionsLeft - 4) {
                    selectSquad(squads.get(i).name());
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                    rebuildButtons();
                    return true;
                }
            }
            return false;
        }
        if (tab == TAB_HQ) {
            int end = Math.min(start + perPage, hqs.size());
            for (int i = start; i < end; i++) {
                int rowTop = y + ROW_TOP + (i - start) * ROW_HEIGHT;
                if (mouseY >= rowTop && mouseY < rowTop + ROW_HEIGHT && mouseX >= x + MARGIN_L && mouseX < actionsLeft - 4) {
                    String id = hqs.get(i).id();
                    PacketDistributor.sendToServer(new HqActionC2SPayload("SELECT", id.equals(selectedHq) ? "" : id, ""));
                    return true;
                }
            }
            return false;
        }
        List<Entry> rows = visible();
        int end = Math.min(start + perPage, rows.size());
        for (int i = start; i < end; i++) {
            int rowTop = y + ROW_TOP + (i - start) * ROW_HEIGHT;
            if (mouseY >= rowTop && mouseY < rowTop + ROW_HEIGHT
                    && mouseX >= x + MARGIN_L && mouseX < actionsLeft - 4) {
                Entry e = rows.get(i);
                if (!e.isOnline()) return false;
                if (!selected.remove(e.entityId())) {
                    selected.add(e.entityId());
                }
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                rebuildButtons();
                return true;
            }
        }
        return false;
    }

    /**
     * Phase 3A's "assign selected guards to a squad" row: a name field plus
     * Assign/Clear, both acting on every currently-{@link #selected} entity id in one
     * {@link AssignSquadC2SPayload}. The typed name is kept in {@link #squadNameDraft}
     * across rebuilds (page changes, toggling other rows) since {@code clearWidgets()}
     * above throws away the previous {@code EditBox} instance every time.
     */
    private void buildSquadBar(int x, int y) {
        int barY = y + barOffset(1);
        int editX = x + MARGIN_L;

        EditBox squadNameBox = new EditBox(this.font, editX, barY, SQUAD_EDIT_W, BTN_H,
                Component.translatable("gui.rallyguard.command.squad_placeholder"));
        squadNameBox.setMaxLength(32);
        squadNameBox.setValue(squadNameDraft);
        // The real 1.21.1 EditBox method is setHint(Component), not setPlaceholder(...) -
        // see PORTING_PLAN.md section 21.
        squadNameBox.setHint(filterPath.isEmpty()
                ? Component.translatable("gui.rallyguard.command.squad_placeholder")
                : Component.translatable("gui.rallyguard.command.unit_placeholder", topSquad(filterPath)));
        squadNameBox.setResponder(value -> squadNameDraft = value);
        squadNameBox.setTooltip(Tooltip.create(Component.translatable(filterPath.isEmpty()
                ? "gui.rallyguard.tip.squad_name" : "gui.rallyguard.tip.unit_name")));
        this.addRenderableWidget(squadNameBox);

        // Phase 3A.1: cycles through existing squad names (and, since 3D.1, selects that
        // squad) so reusing a squad is a click instead of a retype that could typo into
        // a brand-new squad. The header chips do the same for the squads that fit there.
        int pickX = editX + SQUAD_EDIT_W + BTN_GAP + 4;
        List<String> knownSquads = knownSquadNames();
        Button pick = addButton(Component.literal("▾"), "squad_pick", false, b -> cycleSquadPicker(knownSquads),
                pickX, barY, SQUAD_PICK_W);
        pick.active = !knownSquads.isEmpty();

        int assignX = pickX + SQUAD_PICK_W + BTN_GAP + 4;
        Button assign = addButton(Component.translatable("gui.rallyguard.command.squad_assign"), "squad_assign", true, b -> {
            if (!selected.isEmpty() && !squadNameDraft.trim().isEmpty()) {
                sendAssignSquad(assignPath(squadNameDraft.trim()));
            }
        }, assignX, barY, SQUAD_ASSIGN_W);
        assign.active = !selected.isEmpty() && !squadNameDraft.trim().isEmpty();

        int clearX = assignX + SQUAD_ASSIGN_W + BTN_GAP + 4;
        addButton(Component.translatable("gui.rallyguard.command.squad_clear"), "squad_clear", true, b -> {
            if (!selected.isEmpty()) {
                // Inside a squad's view, Clear takes the guards out of their UNIT only.
                sendAssignSquad(filterPath.isEmpty() ? "" : topSquad(filterPath));
            }
        }, clearX, barY, SQUAD_CLEAR_W);

        // Phase 3H: rename ONE guard to the text in the field (select just that guard -
        // e.g. look at it and press the Command key - type, click the pen).
        int renameX = clearX + SQUAD_CLEAR_W + BTN_GAP + 4;
        Entry only = selected.size() == 1 ? entryById(selected.iterator().next()) : null;
        Button renameGuard = addButton(Component.literal("✎"), "rename_guard", false, b -> {
            Entry target = selected.size() == 1 ? entryById(selected.iterator().next()) : null;
            String name = squadNameDraft.trim();
            if (target != null && !name.isEmpty() && !target.guardId().isEmpty()) {
                PacketDistributor.sendToServer(new GuardManageC2SPayload(target.guardId(), "RENAME", name));
            }
        }, renameX, barY, SQUAD_PICK_W);
        renameGuard.active = only != null && !only.guardId().isEmpty() && !squadNameDraft.trim().isEmpty();
    }

    /**
     * Phase 3H.1 (user: sub-groups "no son muy intuitivos"): what the squad field means
     * depends on where you are. In the full list it's a squad (or "Squad/Unit"); inside a
     * squad's view (Squads tab → View ›) a plain name is a UNIT of that squad - so making
     * "Archers" in "Village1" is: View › Village1, select, type Archers, Assign.
     */
    private String assignPath(String typed) {
        if (filterPath.isEmpty() || typed.contains("/") || typed.equalsIgnoreCase(topSquad(filterPath))) return typed;
        return topSquad(filterPath) + "/" + typed;
    }

    private Entry entryById(int entityId) {
        for (Entry e : all) {
            if (e.entityId() == entityId) return e;
        }
        return null;
    }

    /**
     * E2A block 5 (FEEDBACK_E1 §C "ya hay demasiados botones"): the STYLE group - one cycle
     * button each for stance, fire style and doctrine, showing the first selected guard's
     * value; a click gives the whole selection the next one. The fire-style button only
     * exists when the selection has a bow, crossbow or gun (no dead buttons).
     * <p>
     * E3A A7 (David: no individual micro-management): fire style and doctrine are GROUP
     * settings - they only appear for a selection of 2+ guards (fire style also needs a
     * shooter in it). A lone guard fights with the style its personality and weapon pick
     * (DecisionLadder traits); only its stance is set by hand.
     */
    private void buildStanceBar(int x, int y) {
        // E3B G0: a single guard's panel has no group settings (posture is a group's; its own style
        // comes from its personality, weapon and rank).
        if (individual()) return;
        int barY = y + barOffset(2);
        int[] xs = barSlots(x, 3);
        int w = barSlotWidth(3);
        Entry first = selected.isEmpty() ? null : entryById(selected.iterator().next());

        String stance = first == null || first.stance() == null || first.stance().isEmpty() ? "DEFENSIVE" : first.stance();
        String nextStance = cycle(STANCES, stance);
        addButton(Component.translatable("gui.rallyguard.style.stance",
                        Component.translatable("gui.rallyguard.stance." + stance.toLowerCase(Locale.ROOT))),
                "stance_cycle", true, b -> {
                    if (!selected.isEmpty()) sendAssignStance(nextStance);
                }, xs[0], barY, w);

        boolean group = selected.size() >= 2;
        if (group && selectionHasRanged()) buildFireModeButton(xs[1], barY, w);
        if (!group) return;

        String doctrine = first == null || first.doctrine().isEmpty() ? "STANDARD" : first.doctrine();
        String nextDoctrine = cycle(DOCTRINES, doctrine);
        addButton(Component.translatable("gui.rallyguard.style.doctrine",
                        Component.translatable("gui.rallyguard.doctrine." + doctrine.toLowerCase(Locale.ROOT))),
                "doctrine_cycle", true, b -> {
                    if (!selected.isEmpty()) sendIssueOrder("DOCTRINE_" + nextDoctrine);
                }, xs[2], barY, w);
    }

    private static final String[] STANCES = {"PASSIVE", "DEFENSIVE", "AGGRESSIVE", "RUTHLESS"};
    private static final String[] DOCTRINES = {"STANDARD", "HARASS", "AVOID_CASUALTIES", "DISTRACT", "ALL_OUT",
            "HOLD_POSITION", "AMBUSH"};

    private static String cycle(String[] values, String current) {
        for (int i = 0; i < values.length; i++) {
            if (values[i].equalsIgnoreCase(current)) return values[(i + 1) % values.length];
        }
        return values[0];
    }

    /** E2A: true when some selected guard carries a bow, crossbow or gun. */
    private boolean selectionHasRanged() {
        for (int id : selected) {
            Entry e = entryById(id);
            if (e != null && ("r".equals(e.role()) || "g".equals(e.role()))) return true;
        }
        return false;
    }

    /** E2A: x of each of {@code n} equal buttons in a bar, right of the group caption. */
    private int[] barSlots(int x, int n) {
        int w = barSlotWidth(n);
        int[] out = new int[n];
        for (int i = 0; i < n; i++) out[i] = x + BAR_LEFT + i * (w + BTN_GAP);
        return out;
    }

    private int barSlotWidth(int n) {
        return (PANEL_W - BAR_LEFT - MARGIN_R / 2 - (n - 1) * BTN_GAP) / n;
    }

    private void sendAssignStance(String stanceName) {
        PacketDistributor.sendToServer(new AssignStanceC2SPayload(selectionIds(), stanceName));
    }

    /**
     * E1-G: how the selected ranged guards move while they shoot; a click gives the whole
     * selection the NEXT mode (Normal -> Still -> Advancing -> Retreating -> Volley). Melee
     * guards in the selection get the movement it implies (OrderSemantics, server side).
     */
    private void buildFireModeButton(int bx, int barY, int w) {
        Entry first = null;
        for (int id : selected) {
            Entry e = entryById(id);
            if (e != null && ("r".equals(e.role()) || "g".equals(e.role()))) {
                first = e;
                break;
            }
        }
        String current = first == null || first.fireMode() == null || first.fireMode().isEmpty() ? "NORMAL" : first.fireMode();
        String next = nextFireMode(current);
        Component label = Component.translatable("gui.rallyguard.fire.button",
                Component.translatable("gui.rallyguard.fire." + current.toLowerCase(Locale.ROOT)));
        addButton(label, "fire_mode", true, b -> {
            if (!selected.isEmpty()) sendIssueOrder("FIRE_" + next);
        }, bx, barY, w);
    }

    private static String nextFireMode(String mode) {
        String[] order = {"NORMAL", "STILL", "ADVANCING", "RETREATING", "VOLLEY"};
        for (int i = 0; i < order.length; i++) {
            if (order[i].equalsIgnoreCase(mode)) return order[(i + 1) % order.length];
        }
        return "STILL";
    }

    /**
     * E2A block 5: the POSITION group (top bar) - where the selection goes, from "with me" to
     * "on your own", plus Retreat. Every role understands each of these (OrderSemantics).
     */
    private void buildOrderBar(int x, int y) {
        int barY = y + barOffset(5);
        // E3A A6: no Break Ranks button - guards break ranks by themselves on reaching their HQ.
        // E3A A7: Hold moved to the Battle Planner (objective HOLD / operation HOLD_LINE).
        String[] orderIds = {"FOLLOW", "RECALL"};
        if (individual()) {
            // E3B G0: the individual actions - Move (follow me) and Stay (hold right here).
            String[] mine = {"FOLLOW", "HOLD"};
            int[] ixs = barSlots(x, mine.length);
            int iw = barSlotWidth(mine.length);
            for (int i = 0; i < mine.length; i++) {
                String orderId = mine[i];
                String key = i == 0 ? "move" : "stay";
                addButton(Component.translatable("gui.rallyguard.order." + key), "order_" + key, true, b -> {
                    if (!selected.isEmpty()) sendIssueOrder(orderId);
                }, ixs[i], barY, iw);
            }
            return;
        }
        int[] xs = barSlots(x, orderIds.length + 1);
        int w = barSlotWidth(orderIds.length + 1);
        for (int i = 0; i < orderIds.length; i++) {
            String orderId = orderIds[i];
            String key = orderId.toLowerCase(Locale.ROOT);
            addButton(Component.translatable("gui.rallyguard.order." + key), "order_" + key, true, b -> {
                if (!selected.isEmpty()) {
                    sendIssueOrder(orderId);
                }
            }, xs[i], barY, w);
        }
        addButton(Component.translatable("gui.rallyguard.order.retreat"), "order_retreat", true, b -> {
            if (!selected.isEmpty()) {
                sendRetreatOrder();
            }
        }, xs[orderIds.length], barY, w);
    }

    private void sendIssueOrder(String orderId) {
        PacketDistributor.sendToServer(new IssueOrderC2SPayload(selectionIds(), orderId));
    }

    /**
     * Phase 3E / E2A block 5: the ATTACK group - WHO the selection fights (the selector cycles
     * the server's options, including "enemy group" = a whole squad or pack), Attack, and
     * the fire permission (Hold / Release Fire), plus Kamikaze.
     */
    private void buildTargetBar(int x, int y) {
        int barY = y + barOffset(3);
        int avail = PANEL_W - BAR_LEFT - MARGIN_R / 2;
        int btnW = (avail - TARGET_SELECT_W - 4 * BTN_GAP) / 4;
        int startX = x + BAR_LEFT;

        Button selector = addButton(Component.literal(targetButtonLabel()), "target_select", false, b -> {
            cycleTarget();
            rebuildButtons();
        }, startX, barY, TARGET_SELECT_W);
        selector.active = !targets.isEmpty();

        int bx = startX + TARGET_SELECT_W + BTN_GAP;
        addButton(Component.translatable("gui.rallyguard.order.attack"), "order_attack", true, b -> {
            if (!selected.isEmpty()) {
                PacketDistributor.sendToServer(new IssueAttackOrderC2SPayload(selectionIds(), targetId));
            }
        }, bx, barY, btnW);

        if (individual()) {
            // E3B G0: Attack, Defend (me) and Kamikaze - fire permission is a group setting.
            bx += btnW + BTN_GAP;
            addButton(Component.translatable("gui.rallyguard.order.defend"), "order_defend", true, b -> {
                if (!selected.isEmpty()) sendIssueOrder("DEFEND");
            }, bx, barY, btnW);
            bx += btnW + BTN_GAP;
            addButton(Component.translatable("gui.rallyguard.order.kamikaze").withStyle(ChatFormatting.RED), "order_kamikaze", true, b -> {
                if (!selected.isEmpty()) {
                    PacketDistributor.sendToServer(new IssueKamikazeOrderC2SPayload(selectionIds(), targetId));
                }
            }, bx, barY, btnW);
            return;
        }

        bx += btnW + BTN_GAP;
        addButton(Component.translatable("gui.rallyguard.order.hold_fire"), "order_hold_fire", true, b -> {
            if (!selected.isEmpty()) {
                sendIssueOrder("HOLD_FIRE");
            }
        }, bx, barY, btnW);

        bx += btnW + BTN_GAP;
        addButton(Component.translatable("gui.rallyguard.order.release_fire"), "order_release_fire", true, b -> {
            if (!selected.isEmpty()) {
                sendIssueOrder("RELEASE_FIRE");
            }
        }, bx, barY, btnW);

        // Kamikaze uses the same chosen target; only guards carrying a TNT-like block in
        // their off hand go (the server says how many did).
        bx += btnW + BTN_GAP;
        addButton(Component.translatable("gui.rallyguard.order.kamikaze").withStyle(ChatFormatting.RED), "order_kamikaze", true, b -> {
            if (!selected.isEmpty()) {
                PacketDistributor.sendToServer(new IssueKamikazeOrderC2SPayload(selectionIds(), targetId));
            }
        }, bx, barY, btnW);
    }

    private void cycleTarget() {
        if (targets.isEmpty()) return;
        int current = 0;
        for (int i = 0; i < targets.size(); i++) {
            if (targets.get(i).id().equals(targetId)) {
                current = i;
                break;
            }
        }
        targetId = targets.get((current + 1) % targets.size()).id();
    }

    /** "◀ label ▶"-style text for the selector, truncated to fit the button. */
    private String targetButtonLabel() {
        String label = Component.translatable("gui.rallyguard.target.none").getString();
        for (GuardListS2CPayload.TargetOption option : targets) {
            if (option.id().equals(targetId)) {
                label = describeTarget(option);
                break;
            }
        }
        String text = "▸ " + label;
        int max = TARGET_SELECT_W - 8;
        if (this.font.width(text) > max) {
            while (!text.isEmpty() && this.font.width(text + "...") > max) {
                text = text.substring(0, text.length() - 1);
            }
            text = text + "...";
        }
        return text;
    }

    private static String describeTarget(GuardListS2CPayload.TargetOption option) {
        String id = option.id();
        if (id.equals("NEAREST")) return Component.translatable("gui.rallyguard.target.nearest").getString();
        if (id.equals("GROUP")) return Component.translatable("gui.rallyguard.target.group").getString();
        if (id.equals("NONE")) return Component.translatable("gui.rallyguard.target.none").getString();
        if (id.equals("ATTACKER")) return Component.translatable("gui.rallyguard.target.attacker", option.name()).getString();
        if (id.equals("MARKED")) return Component.translatable("gui.rallyguard.target.marked", option.name()).getString();
        return Component.translatable("gui.rallyguard.target.player", option.name()).getString();
    }

    /**
     * Phase 3D / E2A block 5: the FORMATION group - Muster in one of the four shapes (on the
     * Ledger's marked point when one is set, else where the commander stands), Advance and
     * Reform. Large groups form per subunit (block 1).
     */
    private void buildMusterBar(int x, int y) {
        // E3A A7: a formation needs a group; Advance moved to the Battle Planner (ADVANCE_BY_TURNS).
        if (selected.size() < 2) return;
        int barY = y + barOffset(4);
        String[] shapeNames = {"LINE", "COLUMN", "V", "CIRCLE", "TESTUDO"};
        int[] xs = barSlots(x, shapeNames.length + 1);
        int w = barSlotWidth(shapeNames.length + 1);
        for (int i = 0; i < shapeNames.length; i++) {
            String shapeName = shapeNames[i];
            String key = shapeName.toLowerCase(Locale.ROOT);
            // Leading spaces leave room for the shape icon drawn at the button's left edge.
            addButton(Component.literal("    ").append(Component.translatable("gui.rallyguard.formation." + key)), "muster", true, b -> {
                if (!selected.isEmpty()) {
                    sendMusterOrder(shapeName);
                }
            }, xs[i], barY, w);
        }
        addButton(Component.translatable("gui.rallyguard.order.reform"), "order_reform", true, b -> {
            if (!selected.isEmpty()) sendIssueOrder("REFORM");
        }, xs[shapeNames.length], barY, w);
    }

    /** E2A block 5: the group captions left of the bars (Position · Formation · Attack · Style). */
    private void renderGroupCaptions(GuiGraphics g, int x, int y) {
        String[] keys = {"style", "attack", "formation", "position"};
        for (int i = 0; i < keys.length; i++) {
            if ("formation".equals(keys[i]) && selected.size() < 2) continue; // E3A A7: bar hidden
            if ("style".equals(keys[i]) && individual()) continue; // E3B G0: no group settings for one guard
            int barY = y + barOffset(i + 2);
            Component caption = Component.translatable("gui.rallyguard.group." + keys[i]);
            g.drawString(this.font, caption, x + MARGIN_L / 2, barY + (BTN_H - this.font.lineHeight) / 2 + 1, 0x9A9A9A, false);
        }
    }

    // ==== E3B G0/G4: individual panel and group card ====

    /** E3B G0: exactly one guard selected - the individual panel (Attack, Defend, Move, Stay, Kamikaze, Mount). */
    private boolean individual() {
        return selected.size() == 1;
    }

    /** The last group card the server sent ("" = none); see {@code command.GroupCards}. */
    private static String groupCard = "";

    public static void onGroupCard(String card) {
        groupCard = card == null ? "" : card;
        if (Minecraft.getInstance().screen instanceof GuardCommandScreen screen) screen.rebuildButtons();
    }

    private String cardField(int i) {
        String[] f = groupCard.split("\\|", -1);
        return i < f.length ? f[i] : "";
    }

    private boolean hasCard() {
        return selected.size() >= 2 && !groupCard.isEmpty() && tab == TAB_GUARDS;
    }

    private static final int CARD_INFO_W = 196;

    /**
     * E3B G4: the card's three cycle buttons - the group's instruction (each click gives the next
     * one; the last is "none"), density and load of its formation. Nothing new outside the card.
     */
    private void buildCardBar(int x, int y) {
        if (!hasCard()) return;
        int barY = y + barOffset(6);
        int left = x + MARGIN_L / 2 + CARD_INFO_W;
        int avail = x + PANEL_W - MARGIN_R / 2 - left;
        int w = (avail - 2 * BTN_GAP) / 3;
        String instr = cardField(8);
        String nextInstr = cycle(INSTRUCTIONS, instr.isEmpty() ? "NONE" : instr);
        Component instrLabel = instr.isEmpty() ? Component.translatable("gui.rallyguard.instr.none")
                : Component.translatable("gui.rallyguard.instr." + instr.toLowerCase(Locale.ROOT));
        addButton(Component.translatable("gui.rallyguard.card.instr", instrLabel), "card_instr", true,
                b -> sendIssueOrder("INSTR_" + nextInstr), left, barY, w);
        String density = cardField(9).isEmpty() ? "NORMAL" : cardField(9);
        addButton(Component.translatable("gui.rallyguard.card.density",
                        Component.translatable("gui.rallyguard.layout.density." + density.toLowerCase(Locale.ROOT))), "card_density", true,
                b -> sendIssueOrder("LAYOUT_DENSITY_" + cycle(DENSITIES, density)), left + w + BTN_GAP, barY, w);
        String load = cardField(10).isEmpty() ? "EQUILIBRADA" : cardField(10);
        addButton(Component.translatable("gui.rallyguard.card.load",
                        Component.translatable("gui.rallyguard.layout.load." + load.toLowerCase(Locale.ROOT))), "card_load", true,
                b -> sendIssueOrder("LAYOUT_LOAD_" + cycle(LOADS, load)), left + 2 * (w + BTN_GAP), barY, w);
    }

    private static final String[] INSTRUCTIONS = {"HOSTIGAR", "MANTENER_LINEA", "AVANZAR", "RETIRARSE", "FLANQUEAR", "NONE"};
    private static final String[] DENSITIES = {"CERRADA", "NORMAL", "ABIERTA"};
    private static final String[] LOADS = {"EQUILIBRADA", "CENTRO", "ALA_IZQUIERDA", "ALA_DERECHA", "AMBAS_ALAS"};

    /** Morale colour: green ≥ 60, yellow ≥ 40, orange ≥ 20, red below; grey when unmeasured. */
    static int moraleColor(int morale) {
        if (morale < 0) return 0xFF808080;
        if (morale >= 60) return 0xFF55CC55;
        if (morale >= 40) return 0xFFE0D040;
        if (morale >= 20) return 0xFFE08830;
        return 0xFFD04040;
    }

    /** E3B G4: identity icon and name, consistency bar, morale/cohesion pips; warnings on hover. */
    private void drawGroupCard(GuiGraphics g, int mouseX, int mouseY) {
        if (!hasCard()) return;
        int x = (this.width - PANEL_W) / 2 + MARGIN_L / 2;
        int y = (this.height - panelH) / 2 + barOffset(6);
        int consistency = parseInt(cardField(1), 0);
        int morale = parseInt(cardField(3), -1), cohesion = parseInt(cardField(4), -1);
        String dominant = cardField(6);
        // Identity icon: a small banner in the archetype's colour with its initial.
        int color = archetypeColor(dominant);
        g.fill(x, y + 1, x + 14, y + BTN_H - 1, color);
        g.fill(x + 3, y + BTN_H - 1, x + 11, y + BTN_H + 1, color);
        String initial = dominant.isEmpty() ? "?" : dominant.substring(0, 1);
        g.drawString(this.font, initial, x + 7 - this.font.width(initial) / 2, y + 5, 0xFFFFFF, true);
        Component name = cardField(0).equals("gui.rallyguard.profile.mixed")
                ? Component.translatable("gui.rallyguard.profile.mixed", consistency)
                : Component.translatable(cardField(0));
        String text = fit(name.getString() + " · " + cardField(7), 104);
        g.drawString(this.font, text, x + 18, y + 1, 0xFFFFFF, false);
        // Consistency bar.
        int bx = x + 18, by = y + 12, bw = 70;
        g.fill(bx, by, bx + bw, by + 4, 0xFF303030);
        g.fill(bx, by, bx + bw * Math.max(0, Math.min(100, consistency)) / 100, by + 4,
                consistency >= 65 ? 0xFF55AA55 : consistency >= 40 ? 0xFFC0A040 : 0xFFB04040);
        g.drawString(this.font, String.valueOf(consistency), bx + bw + 3, by - 2, 0xAAAAAA, false);
        // Morale / cohesion pips.
        int px = x + 128;
        g.fill(px, y + 3, px + 8, y + 11, moraleColor(morale));
        g.drawString(this.font, morale < 0 ? "-" : String.valueOf(morale), px + 10, y + 3, 0xCCCCCC, false);
        g.fill(px + 34, y + 3, px + 42, y + 11, moraleColor(cohesion));
        g.drawString(this.font, cohesion < 0 ? "-" : String.valueOf(cohesion), px + 44, y + 3, 0xCCCCCC, false);
        String warnings = cardField(5);
        if (!warnings.isEmpty()) g.drawString(this.font, "⚠", x + CARD_INFO_W - 10, y + 5, 0xFFCC33, true);
        // Hover: the full card.
        if (mouseX >= x && mouseX < x + CARD_INFO_W && mouseY >= y && mouseY < y + BTN_H) {
            List<Component> lines = new ArrayList<>();
            lines.add(name.copy().withStyle(ChatFormatting.WHITE, ChatFormatting.BOLD));
            lines.add(Component.translatable("gui.rallyguard.card.consistency", consistency).withStyle(ChatFormatting.GRAY));
            if (!cardField(2).isEmpty()) {
                lines.add(Component.translatable("gui.rallyguard.card.second", Component.translatable(cardField(2))).withStyle(ChatFormatting.GRAY));
            }
            lines.add(Component.translatable("gui.rallyguard.card.morale", morale < 0 ? "-" : String.valueOf(morale),
                    cohesion < 0 ? "-" : String.valueOf(cohesion)).withStyle(ChatFormatting.GRAY));
            if (!cardField(11).isEmpty()) {
                lines.add(Component.translatable("gui.rallyguard.card.morale_state",
                        Component.translatable("gui.rallyguard.morale." + cardField(11).toLowerCase(Locale.ROOT)),
                        cardField(12).isEmpty() ? Component.literal("-") : Component.translatable(cardField(12))).withStyle(ChatFormatting.GRAY));
            }
            if (!warnings.isEmpty()) {
                for (String w : warnings.split("~")) {
                    String[] parts = w.split("\\^");
                    Object[] args = new Object[parts.length - 1];
                    for (int a = 1; a < parts.length; a++) {
                        args[a - 1] = parts[a].startsWith("gui.") ? Component.translatable(parts[a]) : parts[a];
                    }
                    lines.add(Component.translatable(parts[0], args).withStyle(ChatFormatting.GOLD));
                }
            }
            g.renderComponentTooltip(this.font, lines, mouseX, mouseY);
        }
    }

    /** Truncates to {@code max} pixels with "…" (same approach as the target selector's label). */
    private String fit(String text, int max) {
        if (this.font.width(text) <= max) return text;
        String t = text;
        while (!t.isEmpty() && this.font.width(t + "…") > max) t = t.substring(0, t.length() - 1);
        return t + "…";
    }

    private static int parseInt(String s, int fallback) {
        try {
            return Integer.parseInt(s.trim());
        } catch (NumberFormatException e) {
            return fallback;
        }
    }

    /** One colour per archetype family (identity at a glance; accessible contrast on the dark panel). */
    static int archetypeColor(String archetype) {
        return switch (archetype) {
            case "ARQUEROS", "BALLESTEROS", "TIRADORES" -> 0xFF3A7D44;
            case "CABALLERIA_DE_RANGO" -> 0xFF2E7F8F;
            case "CABALLERIA_LIGERA", "CABALLERIA_PESADA" -> 0xFF8F5A2E;
            case "INFANTERIA_PESADA", "LANCEROS" -> 0xFF5A5F8F;
            case "INFANTERIA_LINEA" -> 0xFF6F6F7F;
            default -> 0xFF7F4F7F;
        };
    }

    /**
     * The player's own current position/facing, as the {@link Destination} v1 always
     * uses (see {@link #buildMusterBar}'s javadoc) - floored to block coordinates and a
     * normalized {@code [0, 360)} yaw degree, matching {@link Destination}'s
     * deliberately integer wire format. Returns a destination at the local player's feet
     * even if, implausibly, {@code Minecraft.getInstance().player} were unavailable -
     * this method is only ever called from a button already inside this player's own
     * open screen, so that player is always present in practice.
     */
    private Destination currentPlayerDestination() {
        Player player = Minecraft.getInstance().player;
        int px = (int) Math.floor(player.getX());
        int py = (int) Math.floor(player.getY());
        int pz = (int) Math.floor(player.getZ());
        int yaw = (((int) Math.floor(player.getYRot())) % 360 + 360) % 360;
        return new Destination(px, py, pz, yaw);
    }

    private void sendMusterOrder(String shapeName) {
        PacketDistributor.sendToServer(new IssueMusterOrderC2SPayload(
                selectionIds(), currentPlayerDestination(), shapeName));
    }

    private void sendRetreatOrder() {
        PacketDistributor.sendToServer(new IssueRetreatOrderC2SPayload(
                selectionIds(), currentPlayerDestination()));
    }

    /**
     * Every distinct, non-blank squad name currently visible on this Ledger's guard
     * list, sorted case-insensitively. Derived purely from client-side data the server
     * already sent (each {@link Entry#squadName()}) - deliberately NOT a new network
     * round-trip, since the guard list is already kept in sync after every assignment
     * (see {@code GuardCommandNetworking#assignSquad}).
     */
    private List<String> knownSquadNames() {
        // Phase 3H: squads AND their units ("Squad/Unit"), so the picker reaches both -
        // from the server's totals, so far-away squads are included too.
        Set<String> seen = new java.util.TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        for (SquadSummary sq : serverSquads) seen.add(sq.name());
        return new ArrayList<>(seen);
    }

    /** Top-level squads only - the header chips (units are one click away in the
     *  Squads tab). */
    private List<String> knownTopSquads() {
        return knownSquadNames().stream().filter(n -> !n.contains("/")).collect(Collectors.toList());
    }

    /**
     * Phase 3D.1 (Ledger convenience, requested 2026-09-25 after real testing surfaced
     * that selecting an entire squad still required checking each row by hand):
     * cycling to a squad name now ALSO replaces the current selection with every
     * ONLINE guard in that squad, in one click - "pick a squad" and "act on that
     * squad" become the same action, since every batch action (Assign/Clear/Stance/
     * Order/Muster/Retreat) already operates on whatever is in {@link #selected}.
     * Offline (roster-only) rows are skipped, same tolerance selection has everywhere
     * else - there's no live entity id to act on for those. Deliberately REPLACES
     * rather than adds to the existing selection - "select this squad" reads as one
     * clean action; a player who wants to add more guards on top can still tick
     * individual rows afterward with the existing per-row checkbox. No new button or
     * panel space needed - reuses the existing "▾" picker, which otherwise only ever
     * changed the squad name draft text.
     */
    private void cycleSquadPicker(List<String> knownSquads) {
        if (knownSquads.isEmpty()) {
            return;
        }
        squadPickerIndex = (squadPickerIndex + 1) % knownSquads.size();
        squadNameDraft = knownSquads.get(squadPickerIndex);

        selected.clear();
        for (Entry e : all) {
            if (e.isOnline() && inGroup(e, squadNameDraft)) {
                selected.add(e.entityId());
            }
        }

        rebuildButtons();
    }

    private void sendAssignSquad(String squadName) {
        PacketDistributor.sendToServer(new AssignSquadC2SPayload(selectionIds(), squadName));
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
        drawFormationIcons(g);
        renderGroupCaptions(g, (this.width - PANEL_W) / 2, (this.height - panelH) / 2);
        drawGroupCard(g, mouseX, mouseY);
        drawRowDetails(g, mouseX, mouseY);
    }

    /** Icon dots per shape, from the REAL formation geometry (7 guards), normalized to
     *  a 12x12 box with "forward" pointing up; index 0 (the leader slot) first. */
    private static final Map<String, List<int[]>> ICONS = new java.util.HashMap<>();

    private static List<int[]> iconFor(String shapeName) {
        return ICONS.computeIfAbsent(shapeName, name -> {
            List<net.minecraft.world.phys.Vec3> pts = new net.hfstack.rallyguard.formation.Formation(
                    net.hfstack.rallyguard.formation.FormationShape.valueOf(name), 1.0D)
                    .positions(net.minecraft.world.phys.Vec3.ZERO, 0.0F, 7);
            double minX = Double.MAX_VALUE, maxX = -Double.MAX_VALUE, minZ = Double.MAX_VALUE, maxZ = -Double.MAX_VALUE;
            for (net.minecraft.world.phys.Vec3 v : pts) {
                minX = Math.min(minX, v.x); maxX = Math.max(maxX, v.x);
                minZ = Math.min(minZ, v.z); maxZ = Math.max(maxZ, v.z);
            }
            double span = Math.max(1.0E-3, Math.max(maxX - minX, maxZ - minZ));
            List<int[]> dots = new ArrayList<>();
            for (net.minecraft.world.phys.Vec3 v : pts) {
                int px = (int) Math.round((v.x - minX) / span * 10.0D + (10.0D - (maxX - minX) / span * 10.0D) / 2.0D);
                int py = (int) Math.round((maxZ - v.z) / span * 10.0D + (10.0D - (maxZ - minZ) / span * 10.0D) / 2.0D);
                dots.add(new int[]{px, py});
            }
            return dots;
        });
    }

    /**
     * Phase 3H.1 (user: "busca los íconos de las formaciones de Villager Recruits, me
     * parecen copiables"). Recruits is "All Rights Reserved", so its textures are not
     * copied; instead each Muster button draws its formation from Rally's own geometry
     * (FormationShape) - the icon can never disagree with what the guards will do.
     */
    private void drawFormationIcons(GuiGraphics g) {
        int x = (this.width - PANEL_W) / 2;
        int y = (this.height - panelH) / 2;
        if (selected.size() < 2) return; // E3B: the bar is hidden for one guard (icons were drawn over nothing)
        String[] shapeNames = {"LINE", "COLUMN", "V", "CIRCLE", "TESTUDO"};
        int[] xs = barSlots(x, shapeNames.length + 1); // same slots as buildMusterBar (Advance left in E3A)
        int top = y + barOffset(4) + (BTN_H - 12) / 2;
        for (int i = 0; i < shapeNames.length; i++) {
            int left = xs[i] + 3;
            List<int[]> dots;
            try {
                dots = iconFor(shapeNames[i]);
            } catch (RuntimeException e) {
                continue;
            }
            for (int d = 0; d < dots.size(); d++) {
                int[] p = dots.get(d);
                int color = d == 0 ? 0xFFFFCC33 : 0xFFE0E0E0;
                g.fill(left + p[0], top + p[1], left + p[0] + 2, top + p[1] + 2, color);
            }
        }
    }

    /**
     * Phase 3H ("ver más información de cada guardia"): hovering a guard's row shows
     * everything the Ledger knows about it, untruncated, one fact per line.
     */
    private void drawRowDetails(GuiGraphics g, int mouseX, int mouseY) {
        if (tab != TAB_GUARDS) return;
        int x = (this.width - PANEL_W) / 2;
        int y = (this.height - panelH) / 2;
        int actionsLeft = x + PANEL_W - MARGIN_R - (BTN_W1 + BTN_GAP + BTN_W2);
        if (mouseX < x + MARGIN_L || mouseX >= actionsLeft - 4) return;
        int rowIndex = (mouseY - (y + ROW_TOP)) / ROW_HEIGHT;
        if (mouseY < y + ROW_TOP || rowIndex < 0 || rowIndex >= perPage) return;
        List<Entry> rows = visible();
        int i = page * perPage + rowIndex;
        if (i >= rows.size()) return;
        Entry e = rows.get(i);

        List<Component> lines = new ArrayList<>();
        lines.add(Component.literal(e.name()).withStyle(ChatFormatting.WHITE, ChatFormatting.BOLD));
        if (!e.squadName().isEmpty()) {
            lines.add(Component.translatable("gui.rallyguard.detail.squad", prettyPath(e.squadName()))
                    .withStyle(SquadColors.colorFor(topSquad(e.squadName()))));
        }
        if (e.command() != null && !e.command().isEmpty()) {
            Component rankName = e.rankName().isEmpty()
                    ? Component.translatable("gui.rallyguard.rank." + e.rank())
                    : Component.literal(e.rankName());
            lines.add(Component.translatable("gui.rallyguard.detail.rank", rankName, e.rank()).withStyle(ChatFormatting.GRAY));
            if (e.captain()) {
                lines.add(Component.translatable("gui.rallyguard.detail.captain").withStyle(ChatFormatting.GOLD));
            }
            // E3A V4: what this rank adds; V3 traits; A1 leadership; V7 story; V11 last decision.
            lines.add(Component.translatable("gui.rallyguard.rankdesc." + e.rank()).withStyle(ChatFormatting.DARK_GRAY));
            if (!e.field(5).isEmpty()) {
                net.minecraft.network.chat.MutableComponent traits = Component.empty();
                String[] ts = e.field(5).split(",");
                for (int ti = 0; ti < ts.length; ti++) {
                    if (ti > 0) traits.append(", ");
                    traits.append(Component.translatable("gui.rallyguard.trait." + ts[ti].toLowerCase(Locale.ROOT)));
                }
                lines.add(Component.translatable("gui.rallyguard.detail.traits", traits).withStyle(ChatFormatting.AQUA));
            }
            if (!e.field(8).isEmpty() && !"-".equals(e.field(8))) {
                lines.add(Component.translatable("gui.rallyguard.detail.leadership", e.field(8)).withStyle(ChatFormatting.GRAY));
            }
            if (!e.field(6).isEmpty()) {
                for (String entry : e.field(6).split("~")) {
                    String[] st = entry.split("\\^", 3);
                    if (st.length < 3) continue;
                    lines.add(Component.translatable("gui.rallyguard.story." + st[1], st[0], st[2]).withStyle(ChatFormatting.DARK_AQUA));
                }
            }
            if (!e.field(9).isEmpty()) { // E3B M: morale value, state (colour) and its main cause
                String[] mo = e.field(9).split("\\^", -1);
                int mv = parseInt(mo[0], -1);
                Component why = mo.length > 2 && !mo[2].isEmpty() ? Component.translatable(mo[2]) : Component.literal("-");
                lines.add(Component.translatable("gui.rallyguard.detail.morale", mv,
                        Component.translatable("gui.rallyguard.morale." + (mo.length > 1 ? mo[1] : "FIRME").toLowerCase(Locale.ROOT)), why)
                        .withStyle(net.minecraft.network.chat.Style.EMPTY.withColor(moraleColor(mv) & 0xFFFFFF)));
            }
            if (!e.field(10).isEmpty()) { // E3B V1-V3/V7: paths, hiring value, next rank, threat memory
                String[] pv = e.field(10).split("\\^", -1);
                if (pv.length >= 8) {
                    int bond = parseInt(pv[0], 0);
                    lines.add(Component.translatable("gui.rallyguard.detail.paths", bond / 20, pv[1], pv[2], pv[3])
                            .withStyle(ChatFormatting.DARK_GREEN));
                    lines.add(Component.translatable("gui.rallyguard.detail.hire_value", pv[4]).withStyle(ChatFormatting.GOLD));
                    int next = parseInt(pv[6], -1);
                    if (next < 0) {
                        lines.add(Component.translatable("gui.rallyguard.detail.max_rank").withStyle(ChatFormatting.DARK_AQUA));
                    } else {
                        int nextRank = Math.min(5, e.rank() + 1);
                        lines.add(Component.translatable("gui.rallyguard.detail.next_rank", Component.translatable("gui.rallyguard.rank." + nextRank),
                                pv[5], next, Component.translatable("gui.rallyguard.rankdesc." + nextRank)).withStyle(ChatFormatting.DARK_AQUA));
                    }
                    if (!pv[7].isEmpty()) {
                        lines.add(Component.translatable("gui.rallyguard.detail.dread", pv[7].replace(",", ", ").replace(":", " ") + " %")
                                .withStyle(ChatFormatting.RED));
                    }
                }
            }
            if (!e.field(11).isEmpty()) { // E3B V6: weapon masteries and what each one changes
                for (String m : e.field(11).split("~")) {
                    String[] mp = m.split(":", -1);
                    if (mp.length < 3) continue;
                    net.minecraft.network.chat.MutableComponent effects = Component.empty();
                    String[] effs = mp[2].isEmpty() ? new String[0] : mp[2].split(",");
                    for (int ei = 0; ei < effs.length; ei++) {
                        if (ei > 0) effects.append(", ");
                        effects.append(Component.translatableWithFallback("gui.rallyguard.mastery." + effs[ei], effs[ei]));
                    }
                    lines.add(Component.translatable("gui.rallyguard.detail.mastery",
                            Component.translatableWithFallback("gui.rallyguard.weapon." + mp[0], mp[0]), mp[1], effects)
                            .withStyle(ChatFormatting.BLUE));
                }
            }
            if (!e.field(7).isEmpty() && !"NONE".equals(e.field(7))) {
                lines.add(Component.translatable("gui.rallyguard.detail.why", Component.translatableWithFallback(
                        "gui.rallyguard.reason." + e.field(7).toLowerCase(Locale.ROOT), e.field(7))).withStyle(ChatFormatting.GRAY));
            }
        }
        if (e.stance() != null && !e.stance().isEmpty()) {
            lines.add(Component.translatable("gui.rallyguard.detail.stance", Component.translatable(
                    "gui.rallyguard.stance." + e.stance().toLowerCase(Locale.ROOT))).withStyle(ChatFormatting.GRAY));
        }
        lines.add(Component.translatable(e.patrolling() ? "gui.rallyguard.detail.posted" : "gui.rallyguard.detail.not_posted")
                .withStyle(ChatFormatting.GRAY));
        if (e.holdFire()) {
            lines.add(Component.translatable("gui.rallyguard.command.hold_fire_tag").withStyle(ChatFormatting.RED));
        }
        if (e.target() != null && !e.target().isEmpty()) {
            lines.add(Component.translatable("gui.rallyguard.detail.target", e.target()).withStyle(ChatFormatting.GOLD));
        }
        if (e.ward() != null && !e.ward().isEmpty()) {
            lines.add(Component.translatable("gui.rallyguard.detail.ward", e.ward()).withStyle(ChatFormatting.AQUA));
        }
        if (e.mount() != null && !e.mount().isEmpty()) {
            lines.add(Component.translatable("gui.rallyguard.detail.mount", e.mount()).withStyle(ChatFormatting.GRAY));
        }
        if (e.fireMode() != null && !e.fireMode().isEmpty() && !"NORMAL".equals(e.fireMode())) {
            lines.add(Component.translatable("gui.rallyguard.detail.fire_mode", Component.translatable(
                    "gui.rallyguard.fire." + e.fireMode().toLowerCase(Locale.ROOT))).withStyle(ChatFormatting.GRAY));
        }
        if (e.pack() != null && e.pack().contains(":")) {
            String[] pk = e.pack().split(":", 2);
            lines.add(Component.translatable("gui.rallyguard.detail.pack", pk[0], pk[1]).withStyle(ChatFormatting.GRAY));
        }
        String armor = formatArmorSummary(e.armor());
        if (!armor.isEmpty()) {
            lines.add(Component.literal(armor).withStyle(ChatFormatting.GRAY));
        }
        String where = formatWhereabouts(e);
        if (!where.isEmpty()) {
            lines.add(Component.translatable("gui.rallyguard.detail.where", where).withStyle(ChatFormatting.GRAY));
        }
        if (!e.isOnline()) {
            lines.add(Component.translatable("gui.rallyguard.detail.away").withStyle(ChatFormatting.DARK_GRAY));
        }
        g.renderComponentTooltip(this.font, lines, mouseX, mouseY);
    }

    private void drawPanel(GuiGraphics g) {
        int x = (this.width - PANEL_W) / 2;
        int y = (this.height - panelH) / 2;

        int bg = 0xF0101010;
        g.fill(x, y, x + PANEL_W, y + panelH, bg);

        int border = 0xFFFFFFFF;
        g.fill(x, y, x + PANEL_W, y + 1, border);
        g.fill(x, y + panelH - 1, x + PANEL_W, y + panelH, border);
        g.fill(x, y, x + 1, y + panelH, border);
        g.fill(x + PANEL_W - 1, y, x + PANEL_W, y + panelH, border);

        // Phase 3G: the Guards / Squads tabs (widgets) replace the centered title.

        // Phase 3E: how many guards the next order goes to, and where Muster/Retreat
        // will form up if a position is marked (otherwise: on the commander).
        String count = selected.isEmpty()
                ? Component.translatable("gui.rallyguard.command.select_hint").getString()
                : Component.translatable("gui.rallyguard.command.selected", selected.size()).getString();
        g.drawString(this.font, count, x + 6 + 3 * (TAB_W + BTN_GAP) + 4, y + 8, selected.isEmpty() ? 0xAAAAAA : 0x55FF55, true);
        if (!anchor.isEmpty()) {
            String anchorText = Component.translatable("gui.rallyguard.command.anchor", anchor).getString();
            g.drawString(this.font, anchorText, x + PANEL_W - 8 - this.font.width(anchorText), y + 8, 0xFFFF55, true);
        }

        g.fill(x + 6, y + HEADER_LINE_Y, x + PANEL_W - 6, y + HEADER_LINE_Y + 1, 0x33FFFFFF);
    }

    private void drawRows(GuiGraphics g) {
        int x = (this.width - PANEL_W) / 2;
        int y = (this.height - panelH) / 2;

        if (tab == TAB_SQUADS) {
            drawSquadRows(g, x, y);
            drawPageIndicator(g, x, y);
            return;
        }
        if (tab == TAB_HQ) {
            drawHqRows(g, x, y);
            drawPageIndicator(g, x, y);
            return;
        }

        List<Entry> rows = visible();
        if (rows.isEmpty()) {
            String msg = Component.translatable("gui.rallyguard.command.empty").getString();
            int w = this.font.width(msg);
            g.drawString(this.font, msg, (this.width - w) / 2, y + ROW_TOP + ROW_HEIGHT, 0xAAAAAA, true);
            drawPageIndicator(g, x, y);
            return;
        }

        int start = page * perPage;
        int end = Math.min(start + perPage, rows.size());

        int groupWidth = BTN_W1 + BTN_GAP + BTN_W2;
        int actionsRight = x + PANEL_W - MARGIN_R;
        // E3A F8: the row's mount button sits left of the action group - text stops before it.
        int actionsLeft = actionsRight - groupWidth - (tab == TAB_GUARDS ? PACK_BTN_W + BTN_GAP : 0);

        for (int i = start; i < end; i++) {
            Entry e = rows.get(i);

            int rowTop = y + ROW_TOP + (i - start) * ROW_HEIGHT;
            int rowMidY = rowTop + (ROW_HEIGHT / 2);
            int rowBot = rowTop + ROW_HEIGHT;

            g.fill(x + 6, rowBot - 1, x + PANEL_W - 6, rowBot, 0x22FFFFFF);

            // Phase 3E: the row itself is the selection control (see mouseClicked) - a
            // check box plus a green tint make the selection readable at a glance.
            boolean isSelected = e.isOnline() && selected.contains(e.entityId());
            if (isSelected) {
                g.fill(x + 6, rowTop, x + PANEL_W - 6, rowBot - 1, 0x2255FF55);
            }
            if (e.isOnline()) {
                int bx = x + MARGIN_L + 2;
                int by = rowMidY - 5;
                g.fill(bx, by, bx + 10, by + 10, 0xFFAAAAAA);
                g.fill(bx + 1, by + 1, bx + 9, by + 9, isSelected ? 0xFF55FF55 : 0xFF202020);
            }

            // Where the guard is: distance (or dimension) right-aligned before its buttons.
            String where = formatWhereabouts(e);
            int whereW = this.font.width(where);
            g.drawString(this.font, where, actionsLeft - 6 - whereW, rowMidY - (this.font.lineHeight / 2), 0x888888, true);

            // Phase 3A: append the guard's squad (if any) right after its name, dimmed,
            // then reuse the exact same truncation logic that already existed for the
            // name alone - it works on any string, not just a bare name. Phase 3B adds
            // a mount summary (bracketed, when the guard is/was mounted), a compact
            // armor-condition summary (see formatArmorSummary), and, for a roster-only
            // (offline) row, an "(away)" tag plus a dimmer color - the visual half of
            // the online/offline distinction whose data already exists on Entry.
            // Phase 3E: legacy formatting codes color the squad (same color as the
            // guard's world name tag - SquadColors), the stance (gray), HOLD FIRE (red)
            // and the designated target (gold); Font renders them in plain strings and
            // Font#width ignores them, so the truncation below still measures correctly.
            String reset = ChatFormatting.RESET.toString();
            StringBuilder sb = new StringBuilder();
            // E2A: the computed captain gets a gold star; rank 1+ shows as a small numeral.
            if (e.captain()) sb.append(ChatFormatting.GOLD).append("★ ").append(reset);
            sb.append(e.name());
            if (e.rank() > 0) sb.append(ChatFormatting.DARK_AQUA).append(" ").append(ROMAN[Math.min(e.rank(), 5)]).append(reset);
            if (!e.squadName().isEmpty()) {
                sb.append(" ").append(SquadColors.colorFor(topSquad(e.squadName()))).append("■ ")
                        .append(prettyPath(e.squadName())).append(reset);
            }
            if (e.mount() != null && !e.mount().isEmpty()) {
                sb.append(" [").append(e.mount()).append("]");
            }
            String armorSummary = formatArmorSummary(e.armor());
            if (!armorSummary.isEmpty()) {
                sb.append(" ").append(armorSummary);
            }
            if (e.stance() != null && !e.stance().isEmpty()) {
                String stanceKey = "gui.rallyguard.stance." + e.stance().toLowerCase(Locale.ROOT);
                sb.append(ChatFormatting.GRAY).append(" (").append(Component.translatable(stanceKey).getString())
                        .append(")").append(reset);
            }
            if (e.holdFire()) {
                sb.append(ChatFormatting.RED).append(" ")
                        .append(Component.translatable("gui.rallyguard.command.hold_fire_tag").getString()).append(reset);
            }
            if (e.target() != null && !e.target().isEmpty()) {
                sb.append(ChatFormatting.GOLD).append(" → ").append(e.target()).append(reset);
            }
            if (e.ward() != null && !e.ward().isEmpty()) {
                sb.append(ChatFormatting.AQUA).append(" ")
                        .append(Component.translatable("gui.rallyguard.command.ward_tag", e.ward()).getString()).append(reset);
            }
            if (!e.isOnline()) {
                sb.append(" ").append(Component.translatable("gui.rallyguard.command.away").getString());
            }
            String display = sb.toString();

            int maxNameW = (actionsLeft - 12 - whereW) - (x + COL_NAME_X);
            if (this.font.width(display) > maxNameW) {
                String ell = "...";
                while (!display.isEmpty() && this.font.width(display + ell) > maxNameW) {
                    display = display.substring(0, display.length() - 1);
                }
                while (display.endsWith("§")) { // never leave half a formatting code
                    display = display.substring(0, display.length() - 1);
                }
                display = display + reset + ell;
            }
            int nameY = rowMidY - (this.font.lineHeight / 2);
            int nameColor = e.isOnline() ? 0xFFFFFF : 0x999999;
            g.drawString(this.font, display, x + COL_NAME_X, nameY, nameColor, true);
        }

        drawPageIndicator(g, x, y);
    }

    /**
     * Phase 3B: turns the compact HEAD,CHEST,LEGS,FEET wire string (see
     * {@code GuardOwnership#describeArmor} and {@code GuardRoster.Entry#armor} for the
     * exact format - each slot is {@code "-"} for empty or a 0-100 durability-remaining
     * percent) into a single at-a-glance number: the average durability percent across
     * only the slots that actually have armor in them. Returns "" when the guard has no
     * armor equipped/cached at all, so callers can just skip appending anything.
     */
    private String formatArmorSummary(String armor) {
        if (armor == null || armor.isEmpty()) {
            return "";
        }
        String[] parts = armor.split(",", -1);
        int sum = 0;
        int count = 0;
        for (String part : parts) {
            if (!"-".equals(part)) {
                try {
                    sum += Integer.parseInt(part.trim());
                    count++;
                } catch (NumberFormatException ignored) {
                    // Malformed slot value - skip it rather than let one bad slot
                    // corrupt the whole average.
                }
            }
        }
        if (count == 0) {
            return "";
        }
        int avg = Math.round(sum / (float) count);
        return Component.translatable("gui.rallyguard.command.armor", avg).getString();
    }

    private void drawSquadRows(GuiGraphics g, int x, int y) {
        List<SquadSummary> squads = squadSummaries();
        if (squads.isEmpty()) {
            String msg = Component.translatable("gui.rallyguard.command.no_squads").getString();
            g.drawString(this.font, msg, (this.width - this.font.width(msg)) / 2, y + ROW_TOP + ROW_HEIGHT, 0xAAAAAA, true);
            return;
        }
        int actionsLeft = x + PANEL_W - MARGIN_R - (BTN_W1 + BTN_GAP + BTN_W2);
        int start = page * perPage;
        int end = Math.min(start + perPage, squads.size());
        for (int i = start; i < end; i++) {
            SquadSummary squad = squads.get(i);
            int rowTop = y + ROW_TOP + (i - start) * ROW_HEIGHT;
            int rowMidY = rowTop + ROW_HEIGHT / 2;
            g.fill(x + 6, rowTop + ROW_HEIGHT - 1, x + PANEL_W - 6, rowTop + ROW_HEIGHT, 0x22FFFFFF);
            if (squad.online() > 0 && isSquadSelected(squad.name())) {
                g.fill(x + 6, rowTop, x + PANEL_W - 6, rowTop + ROW_HEIGHT - 1, 0x2255FF55);
            }

            // Units are indented under their squad: "  └ Arqueros".
            String label = squad.isUnit()
                    ? ChatFormatting.DARK_GRAY + "   └ " + ChatFormatting.WHITE + squad.name().substring(squad.name().indexOf('/') + 1)
                    : SquadColors.colorFor(squad.name()) + "■ " + ChatFormatting.WHITE + squad.name();
            String text = label + ChatFormatting.GRAY + "  " + Component.translatable("gui.rallyguard.command.squad_counts",
                    squad.total(), squad.online()).getString();
            if (squad.nearest() >= 0) {
                text = text + "  " + formatDistance(squad.nearest());
            }
            if (!squad.isUnit() && squad.hq() != null && !squad.hq().isEmpty()) {
                text = text + ChatFormatting.YELLOW + "  " + Component.translatable("gui.rallyguard.command.hq_tag", squad.hq()).getString();
            }
            g.drawString(this.font, text, x + MARGIN_L, rowMidY - this.font.lineHeight / 2, 0xFFFFFF, true);
        }
    }

    /** True when every loaded member of the squad is in the current selection. */
    private boolean isSquadSelected(String squad) {
        for (Entry e : all) {
            if (e.isOnline() && inGroup(e, squad) && !selected.contains(e.entityId())) {
                return false;
            }
        }
        return true;
    }

    private static String formatDistance(int distance) {
        if (distance < 1000) return distance + " m";
        return String.format(Locale.ROOT, "%.1f km", distance / 1000.0);
    }

    /** "35 m", "2.4 km", a dimension name for a guard in another dimension, or "" if
     *  unknown (a roster snapshot saved before Phase 3E). */
    private static String formatWhereabouts(Entry e) {
        if (e.dimension() != null && !e.dimension().isEmpty()) {
            String path = e.dimension().substring(e.dimension().indexOf(':') + 1).replace('_', ' ');
            return path.startsWith("the ") ? path.substring(4) : path;
        }
        if (e.distance() < 0) return "";
        if (e.distance() < 1000) return e.distance() + " m";
        return String.format(Locale.ROOT, "%.1f km", e.distance() / 1000.0);
    }

    private void drawPageIndicator(GuiGraphics g, int x, int y) {
        if (omitted > 0 && tab == TAB_GUARDS) {
            String more = batches > 1
                    ? Component.translatable("gui.rallyguard.command.batch", batch + 1, batches).getString()
                    : Component.translatable("gui.rallyguard.command.omitted", omitted).getString();
            g.drawString(this.font, more, x + (batches > 1 ? 72 : 36), y + panelH - 18, 0x888888, true);
        }
        int totalPages = Math.max(1, (listSize() + perPage - 1) / perPage);
        String pg = (page + 1) + " / " + totalPages;
        int w = this.font.width(pg);
        g.drawString(this.font, pg, x + (PANEL_W - w) / 2, y + panelH - 18, 0xFFFFFF, true);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}

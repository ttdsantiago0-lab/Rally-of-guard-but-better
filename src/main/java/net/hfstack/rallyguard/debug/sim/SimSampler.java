package net.hfstack.rallyguard.debug.sim;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * F201 (O9, measure only, sim only - tag {@code sample:<ms>}): a daemon thread takes the server thread's stack every
 * {@code ms} milliseconds while {@link net.hfstack.rallyguard.debug.Perf#inRegime()} (the steady state of the run) and
 * counts who owns each sample. No JFR (it may not be reachable from NeoForge's module layer): plain
 * {@link Thread#getStackTrace()}, standard Java. It never touches a decision or the world; the only shared state is
 * this class' counters, written by the sampler thread and read by the server thread after {@link #stop} joined it.
 *
 * <p>Owner = the first frame from the top whose class is in a watched mod package (Alex's Caves, Citadel, Rally,
 * Guard Villagers); a frame of another class whose method name carries {@code $alexscaves$} / {@code $citadel$}
 * (a mixin handler merged into a Minecraft class) counts for that mod too. No watched frame = {@code minecraft};
 * a top frame parked or sleeping = {@code idle} (the server waiting for the next tick). For Alex's Caves / Citadel
 * samples the owner frame's {@code Class.method} is counted, with the 6 frames below it (who calls it).
 *
 * <p>Keys (extra, numbers only): {@code smpN} (samples in the steady state), {@code smpShare:<owner>} (fraction of
 * all samples), {@code smpAcShare} (Alex's Caves + Citadel over the non-idle samples), {@code smpTop<k>:<Class.method>}
 * (k = 1..10, fraction of the Alex's Caves + Citadel samples; the name goes in the key as rally_peakSys_ does) and
 * {@code smpMcTop<k>:<Class.method>} (top frame of the {@code minecraft} samples). Text (the call chains of the top
 * three) goes to {@code sampler.txt} in the batch folder; the main chain of each of the top three also rides in a key
 * {@code smpChain<k>:<chain>} (= its fraction of that method's samples), because extra holds numbers only and the
 * workflow copies summary/runs only.
 */
public final class SimSampler {
    private SimSampler() {
    }

    private static final String[][] OWNERS = {
            {"alexscaves", "com.github.alexmodguy.alexscaves."},
            {"citadel", "com.github.alexthe666.citadel."},
            {"rallyguard", "net.hfstack.rallyguard."},
            {"guardvillagers", "tallestegg.guardvillagers."}};
    private static final int CHAIN = 6, TOP = 10;

    private static volatile boolean running;
    private static Thread sampler;
    private static Thread server;
    private static String runLabel = "";
    // written only by the sampler thread while it runs; read by the server thread after join()
    private static long n;
    private static final Map<String, Long> OWNER = new HashMap<>();
    private static final Map<String, Long> AC_METHOD = new HashMap<>();
    private static final Map<String, Map<String, Long>> AC_CHAIN = new HashMap<>();
    private static final Map<String, Long> MC_TOP = new HashMap<>();

    /** SimExtras.begin (server thread): starts the sampler when the run has {@code sample:<ms>}; stops any old one. */
    public static void start(SimScenario s) {
        stopThread();
        clear();
        String v = s.tag("sample");
        if (v.isEmpty()) return;
        int ms;
        try {
            ms = Math.max(1, Math.min(1000, Integer.parseInt(v.trim())));
        } catch (NumberFormatException e) {
            return;
        }
        server = Thread.currentThread(); // SimRunner.setupRun runs inside the server tick
        runLabel = s.id();
        running = true;
        final int period = ms;
        Thread t = new Thread(() -> loop(period), "rallyguard-sim-sampler");
        t.setDaemon(true);
        t.setPriority(Thread.MAX_PRIORITY);
        sampler = t;
        t.start();
    }

    private static void loop(int period) {
        while (running) {
            try {
                Thread.sleep(period);
            } catch (InterruptedException e) {
                return;
            }
            if (!running) return;
            if (!net.hfstack.rallyguard.debug.Perf.inRegime()) continue;
            Thread srv = server;
            if (srv == null) return;
            try {
                take(srv.getStackTrace());
            } catch (RuntimeException ignored) {
                // a stack that can't be read is one sample less, never a crash
            }
        }
    }

    private static void take(StackTraceElement[] st) {
        if (st == null || st.length == 0) return;
        n++;
        if (idle(st[0])) {
            OWNER.merge("idle", 1L, Long::sum);
            return;
        }
        for (int i = 0; i < st.length; i++) {
            String owner = ownerOf(st[i]);
            if (owner == null) continue;
            OWNER.merge(owner, 1L, Long::sum);
            if (owner.equals("alexscaves") || owner.equals("citadel")) {
                String m = st[i].getClassName() + "." + st[i].getMethodName();
                AC_METHOD.merge(m, 1L, Long::sum);
                StringBuilder sb = new StringBuilder();
                for (int j = i + 1; j < Math.min(st.length, i + 1 + CHAIN); j++) {
                    if (sb.length() > 0) sb.append(" <- ");
                    sb.append(st[j].getClassName()).append('.').append(st[j].getMethodName());
                }
                AC_CHAIN.computeIfAbsent(m, k -> new HashMap<>()).merge(sb.toString(), 1L, Long::sum);
            }
            return;
        }
        OWNER.merge("minecraft", 1L, Long::sum);
        MC_TOP.merge(st[0].getClassName() + "." + st[0].getMethodName(), 1L, Long::sum);
    }

    private static boolean idle(StackTraceElement top) {
        String c = top.getClassName();
        String m = top.getMethodName();
        return c.equals("jdk.internal.misc.Unsafe") && m.equals("park")
                || c.equals("java.lang.Thread") && (m.startsWith("sleep") || m.equals("yield") || m.equals("onSpinWait"))
                || c.equals("java.util.concurrent.locks.LockSupport");
    }

    private static String ownerOf(StackTraceElement e) {
        String c = e.getClassName();
        for (String[] o : OWNERS) if (c.startsWith(o[1])) return o[0];
        String m = e.getMethodName();
        if (m.contains("$alexscaves$")) return "alexscaves"; // mixin handler merged into another class
        if (m.contains("$citadel$")) return "citadel";
        return null;
    }

    private static void stopThread() {
        running = false;
        Thread t = sampler;
        sampler = null;
        if (t != null) {
            t.interrupt();
            try {
                t.join(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        server = null;
    }

    private static void clear() {
        n = 0;
        OWNER.clear();
        AC_METHOD.clear();
        AC_CHAIN.clear();
        MC_TOP.clear();
        runLabel = "";
    }

    /** SimExtras.finish (server thread): joins the sampler and publishes its keys; nothing if it never ran. */
    public static void stop(Map<String, Double> out) {
        boolean was = sampler != null;
        stopThread();
        if (!was) return;
        out.put("smpN", (double) n);
        if (n <= 0) {
            clear();
            return;
        }
        for (Map.Entry<String, Long> e : OWNER.entrySet()) out.put("smpShare:" + e.getKey(), e.getValue() / (double) n);
        long ac = OWNER.getOrDefault("alexscaves", 0L) + OWNER.getOrDefault("citadel", 0L);
        long busy = n - OWNER.getOrDefault("idle", 0L);
        out.put("smpAcShare", busy > 0 ? ac / (double) busy : 0.0D);
        List<Map.Entry<String, Long>> top = sorted(AC_METHOD);
        StringBuilder txt = new StringBuilder();
        txt.append(String.format(Locale.ROOT, "== %s  smpN=%d  ac+citadel=%d  busy=%d%n", runLabel, n, ac, busy));
        for (int k = 0; k < Math.min(TOP, top.size()); k++) {
            Map.Entry<String, Long> e = top.get(k);
            double f = ac > 0 ? e.getValue() / (double) ac : 0.0D;
            out.put("smpTop" + (k + 1) + ":" + e.getKey(), f);
            txt.append(String.format(Locale.ROOT, "top%d %.4f %s%n", k + 1, f, e.getKey()));
            if (k < 3) {
                List<Map.Entry<String, Long>> ch = sorted(AC_CHAIN.getOrDefault(e.getKey(), Map.of()));
                if (!ch.isEmpty()) // the workflow keeps summary/runs only, so the main chain also rides in a key
                    out.put("smpChain" + (k + 1) + ":" + ch.get(0).getKey(), ch.get(0).getValue() / (double) e.getValue());
                for (int j = 0; j < Math.min(3, ch.size()); j++)
                    txt.append(String.format(Locale.ROOT, "  chain%d.%d %.4f %s%n", k + 1, j + 1,
                            ch.get(j).getValue() / (double) e.getValue(), ch.get(j).getKey()));
            }
        }
        List<Map.Entry<String, Long>> mc = sorted(MC_TOP);
        long mcN = OWNER.getOrDefault("minecraft", 0L);
        for (int k = 0; k < Math.min(TOP, mc.size()); k++) {
            Map.Entry<String, Long> e = mc.get(k);
            double f = mcN > 0 ? e.getValue() / (double) mcN : 0.0D;
            out.put("smpMcTop" + (k + 1) + ":" + e.getKey(), f);
            txt.append(String.format(Locale.ROOT, "mctop%d %.4f %s%n", k + 1, f, e.getKey()));
        }
        write(txt.toString());
        clear();
    }

    private static List<Map.Entry<String, Long>> sorted(Map<String, Long> m) {
        List<Map.Entry<String, Long>> l = new ArrayList<>(m.entrySet());
        l.sort((a, b) -> Long.compare(b.getValue(), a.getValue()));
        return l;
    }

    private static void write(String txt) {
        Path dir = net.hfstack.rallyguard.debug.SimRunner.batchDir();
        if (dir == null) return;
        try {
            Files.createDirectories(dir);
            Files.writeString(dir.resolve("sampler.txt"), txt, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            com.mojang.logging.LogUtils.getLogger().warn("[rallyguard] F201 sampler.txt not written", e);
        }
    }
}

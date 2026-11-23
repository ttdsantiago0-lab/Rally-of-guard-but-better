#!/usr/bin/env python3
"""F83 (O10): modelo sin entidades v0. Lanchester de dos ecuaciones ajustado a las corridas que ya hay.

    dA/dt = -b * R * A^(2 - pR)      dR/dt = -a * A * R^(2 - pA)

p es la "ley" del arma del bando que dispara: 1 = lineal (cuerpo a cuerpo: las bajas crecen con los dos bandos),
2 = cuadrada (tiro: las bajas solo dependen de cuántos tiran). a y b, uno por emparejamiento; p global por clase de arma.
Integrado en pasos de 1 s hasta que un bando llega a 0 o al capSeconds (ambos vivos = empate, como la sim).

Uso: python3 tools/fuera_de_vista.py            -> escribe tools/torneo-data/fuera_de_vista.md
     python3 tools/fuera_de_vista.py --v1       -> lo mismo + sección F111 (v1: P(azul gana) = logística del margen
                                                    final de la v0, una pendiente global ajustada en semillas pares)
     python3 tools/fuera_de_vista.py --v2       -> v0 + v1 + sección F113 (v2: término independiente b y un factor de
                                                    daño azul d; vivos v2 = escala global s sobre los vivos de la v0)
Solo biblioteca estándar.
"""
import collections
import glob
import json
import math
import os
import sys
import time

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
DATA = os.path.join(ROOT, 'tools', 'torneo-data')
SCEN = os.path.join(ROOT, 'src', 'main', 'resources', 'rallyguard_sim', 'scenarios.json')

# Escenarios fuera de la v0 ("campo abierto, sin órdenes especiales"): prefijo de etiqueta -> motivo
EXCLUDE_TAGS = {
    'refuge': 'refugio', 'instruction': 'instrucción', 'instructionAt': 'instrucción', 'unitInstr': 'instrucción',
    'retreat': 'retirada', 'escort': 'escolta', 'march': 'marcha', 'marchCharge': 'marcha', 'relay': 'relevos',
    'relayNoCharge': 'relevos', 'waves': 'oleadas', 'wavePause': 'oleadas', 'waveKinds': 'oleadas',
    'village': 'aldea', 'door': 'aldea', 'force': 'fuerzas', 'mirror': 'espejo guardia v guardia',
    'scale': 'escala espejo', 'rider': 'montados', 'flank': 'flanqueo', 'lab': 'improvisación',
}
BOSSES = ('cataclysm:', 'alexscaves:')
LAWS = (1.0, 1.25, 1.5, 1.75, 2.0)
RANGED_RED = ('pillager', 'skeleton', 'stray', 'blaze')


def load_rows():
    rows = []
    for f in sorted(glob.glob(os.path.join(DATA, '**', 'runs.jsonl'), recursive=True)):
        with open(f, encoding='utf-8') as fh:
            for line in fh:
                line = line.strip()
                if not line:
                    continue
                try:
                    rows.append(json.loads(line))
                except ValueError:
                    pass
    return rows


def excluded(sc):
    red = (sc.get('red') or {}).get('kind') or ''
    if not red:
        return 'espejo guardia v guardia'
    if red.startswith(BOSSES):
        return 'jefes'
    for t in sc.get('tags', ()):
        head = t.split(':')[0]
        if head == 'lab' and t != 'lab':  # lab:C etc. = familia del laboratorio; solo 'lab' es improvisación
            continue
        if head in EXCLUDE_TAGS:
            return EXCLUDE_TAGS[head]
    return None


def blue_class(row, sc):
    if 'sg:guardGuns' in sc.get('tags', ()):
        return 'pistola'
    n = max(1, row['blue']['start'])
    bows = row.get('extra', {}).get('kit:bow', 0.0) / n
    return 'arco' if bows >= 0.75 else 'cuerpo' if bows <= 0.25 else 'mixto'


def red_class(sc):
    kind = sc['red'].get('kind', '').split(':')[-1]
    if any(t.startswith('redHand:scguns') for t in sc.get('tags', ())):
        return 'pistola'
    return 'tiro' if kind in RANGED_RED else 'cuerpo'


def keys(row, sc):
    ex = row.get('extra', {})
    n = max(1, row['blue']['start'])
    kit = ','.join(f"{k[4:]}={round(v / n, 1)}" for k, v in sorted(ex.items())
                   if k.startswith('kit:') and k != 'kit:foodOffhand' and v)
    rally = 'gv' if 'norally' in sc.get('tags', ()) else 'rally'
    hand = next((t.split(':', 1)[1] for t in sc.get('tags', ()) if t.startswith('redHand:')), '')
    red = sc['red'].get('kind', '') + (f'[{hand}]' if hand else '')
    tier = next((t for t in sc.get('tags', ()) if t.startswith('tier:')), '')
    base = (rally, kit or sc['blue'].get('role', '?'), tier, row['blue']['start'], red, sc.get('terrain', '?'))
    return base + (row['red']['start'],), base


def simulate(A0, R0, a, b, pA, pR, cap):
    """Pasos de 1 s. -> (A final, R final, segundos, ganador)."""
    A, R, t = float(A0), float(R0), 0
    ea, er = 2.0 - pR, 2.0 - pA
    while t < cap:
        dA = b * R * (A ** ea)
        dR = a * A * (R ** er)
        t += 1
        nA, nR = A - dA, R - dR
        if nA <= 0 or nR <= 0:
            # fracción del paso en que cada bando llega a 0: gana el que aguanta más
            fa = A / dA if nA <= 0 else 2.0
            fr = R / dR if nR <= 0 else 2.0
            if fr <= fa:
                return max(0.0, A - dA * fr), 0.0, t - 1 + fr, 'blue'
            return 0.0, max(0.0, R - dR * fa), t - 1 + fa, 'red'
        A, R = nA, nR
    return A, R, float(cap), 'draw'


def loss(runs, pred):
    A, R, T, W = pred
    s = 0.0
    for r in runs:
        s += ((A - r['A']) / r['A0']) ** 2 + ((R - r['R']) / r['R0']) ** 2 + ((T - r['T']) / r['cap']) ** 2
        s += 1.0 if W != r['W'] else 0.0
    return s / len(runs)


def fit(groups, pA, pR):
    """groups = lista de (A0, R0, cap, runs) que comparten a y b. Rejilla log gruesa y luego fina."""
    def total(la, lb):
        a, b = math.exp(la), math.exp(lb)
        s = 0.0
        for A0, R0, cap, runs in groups:
            s += loss(runs, simulate(A0, R0, a, b, pA, pR, cap)) * len(runs)
        return s

    best = None
    coarse = [math.log(10.0) * (x / 2.0) for x in range(-12, 3)]  # 1e-6 .. 10
    for la in coarse:
        for lb in coarse:
            v = total(la, lb)
            if best is None or v < best[0]:
                best = (v, la, lb)
    step = math.log(10.0) / 2.0
    for _ in range(3):
        step /= 3.0
        v0, la0, lb0 = best
        for i in range(-3, 4):
            for j in range(-3, 4):
                v = total(la0 + i * step, lb0 + j * step)
                if v < best[0]:
                    best = (v, la0 + i * step, lb0 + j * step)
    return math.exp(best[1]), math.exp(best[2]), best[0]


def grouped(runs, keyf):
    g = collections.defaultdict(list)
    for r in runs:
        g[keyf(r)].append(r)
    return g


def to_groups(runs):
    g = collections.defaultdict(list)
    for r in runs:
        g[(r['A0'], r['R0'], r['cap'])].append(r)
    return [(k[0], k[1], k[2], v) for k, v in g.items()]


def fit_all(runs, laws):
    params = {}
    for key, rs in grouped(runs, lambda r: r['key']).items():
        bc, rc = rs[0]['bc'], rs[0]['rc']
        params[key] = fit(to_groups(rs), laws[bc], laws['r:' + rc])
    return params


def evaluate(runs, params, laws, keyf):
    out = collections.defaultdict(list)
    for r in runs:
        k = keyf(r)
        if k not in params:
            continue
        a, b, _ = params[k]
        A, R, T, W = simulate(r['A0'], r['R0'], a, b, laws[r['bc']], laws['r:' + r['rc']], r['cap'])
        out[r['key']].append({'ok': W == r['W'], 'eA': (A - r['A']) / r['A0'], 'eT': T - r['T'], 'r': r, 'W': W})
    return out


def summary(ev):
    flat = [e for v in ev.values() for e in v]
    if not flat:
        return 0, float('nan'), float('nan'), float('nan')
    return (len(flat), sum(e['ok'] for e in flat) / len(flat), sum(abs(e['eA']) for e in flat) / len(flat),
            sum(abs(e['eT']) for e in flat) / len(flat))


def why(rs):
    n = len(rs)
    ex = lambda k: sum(r['row'].get('extra', {}).get(k, 0.0) for r in rs) / n
    w = collections.Counter(r['W'] for r in rs)
    bits = []
    if w['draw'] / n >= 0.2:
        bits.append(f"límite de tiempo {round(100 * w['draw'] / n)} %")
    top = w.most_common(2)
    if len(top) > 1 and top[1][1] / n >= 0.25:
        bits.append(f"resultado partido ({top[0][0]} {top[0][1]}/{n}, {top[1][0]} {top[1][1]}/{n})")
    ret = sum(r['row']['blue'].get('retreats', 0) for r in rs) / n
    if ret >= 1:
        bits.append(f"retiradas {ret:.1f}/corrida")
    if ex('act:kite') >= 10:
        bits.append(f"kite {ex('act:kite'):.0f}/corrida")
    if ex('act:standOff') >= 10:
        bits.append(f"stand-off {ex('act:standOff'):.0f}/corrida")
    tags = [t for t in rs[0]['tags'] if t.startswith(('off:', 'standoff:', 'fire:', 'tier:', 'kite'))
            and t != 'off:morale']
    if tags:
        bits.append('etiquetas ' + ' '.join(sorted(set(tags))[:4]))
    if rs[0]['terrain'] not in ('flat', '?'):
        bits.append(f"terreno {rs[0]['terrain']}")
    return '; '.join(bits) or 'sin rasgo obvio (varianza propia)'


def fmt_key(k):
    rally, kit, tier, nb, red, terr = k[:6]
    s = f"{rally} {nb} [{kit}{(' ' + tier) if tier else ''}] v "
    return s + (f"{k[6]} " if len(k) > 6 else '') + f"{red} · {terr}"


# ---- F111 (v1): probabilidad de victoria. Solo se usa con --v1; la v0 no cambia. ----

def margin(r, params, laws, keyf):
    """Margen final de la v0: vivos azules − vivos rojos, cada uno en fracción de su bando, al acabar la simulación."""
    a, b, _ = params[keyf(r)]
    A, R, _, _ = simulate(r['A0'], r['R0'], a, b, laws[r['bc']], laws['r:' + r['rc']], r['cap'])
    return A / max(1, r['A0']) - R / max(1, r['R0'])


def won(r):
    return 1.0 if r['W'] == 'blue' else 0.0  # empate (tope de tiempo) = no gana


def logistic(k, m):
    z = max(-60.0, min(60.0, k * m))
    return 1.0 / (1.0 + math.exp(-z))


def brier(pairs):
    return sum((p - y) ** 2 for p, y in pairs) / len(pairs) if pairs else float('nan')


def fit_slope(pairs_m):
    """pairs_m = [(margen, y)]. Una sola pendiente k >= 0 (sin término independiente) que minimiza el Brier."""
    best = None
    for i in range(0, 401):
        k = 0.25 * i  # 0 .. 100
        v = brier([(logistic(k, m), y) for m, y in pairs_m])
        if best is None or v < best[0]:
            best = (v, k)
    v0, k0 = best
    for i in range(-50, 51):
        k = max(0.0, k0 + 0.005 * i)
        v = brier([(logistic(k, m), y) for m, y in pairs_m])
        if v < best[0]:
            best = (v, k)
    return best[1]


def majority_ref(train, test, keyf):
    """Referencia: clase mayoritaria (gana / no gana) del emparejamiento en el ajuste, como P = 0 o 1."""
    maj, freq = {}, {}
    for k, rs in grouped(train, keyf).items():
        f = sum(won(r) for r in rs) / len(rs)
        maj[k], freq[k] = (1.0 if f > 0.5 else 0.0), f
    tm = [(maj[keyf(r)], won(r)) for r in test if keyf(r) in maj]
    tf = [(freq[keyf(r)], won(r)) for r in test if keyf(r) in freq]
    return brier(tm), brier(tf)


def calibration(pairs, bins=5):
    rows = []
    for i in range(bins):
        lo, hi = i / bins, (i + 1) / bins
        sub = [(p, y) for p, y in pairs if lo <= p < hi or (i == bins - 1 and p == 1.0)]
        if sub:
            rows.append((lo, hi, len(sub), sum(p for p, _ in sub) / len(sub), sum(y for _, y in sub) / len(sub)))
        else:
            rows.append((lo, hi, 0, float('nan'), float('nan')))
    return rows


def v1_section(train, test, params, laws, small, big, sparams, shared, ev_out, ev_size):
    key = lambda r: r['key']
    skey = lambda r: r['skey']
    tr = [(margin(r, params, laws, key), won(r)) for r in train if key(r) in params]
    k = fit_slope(tr)
    out = [(logistic(k, margin(r, params, laws, key)), won(r)) for r in test if key(r) in params]
    ins = [(logistic(k, m), y) for m, y in tr]
    ref_maj, ref_freq = majority_ref(train, test, key)
    # deja fuera un tamaño: pendiente ajustada en los pequeños (donde se ajustaron a, b), aplicada a los grandes
    sm = [r for r in small if r['skey'] in shared]
    ks = fit_slope([(margin(r, sparams, laws, skey), won(r)) for r in sm if skey(r) in sparams])
    bg = [r for r in big if r['skey'] in shared and skey(r) in sparams]
    size = [(logistic(ks, margin(r, sparams, laws, skey)), won(r)) for r in bg]
    size_maj, size_freq = majority_ref(sm, bg, skey)
    cal = calibration(out)
    worst = max((abs(c[3] - c[4]) for c in cal if c[2] >= 50), default=float('nan'))
    cal_ok = all(abs(c[3] - c[4]) <= 0.10 for c in cal if c[2] >= 50)
    ok = (brier(out) <= ref_maj) and cal_ok
    L = ['', '# F111 · modelo sin entidades v1 (P(azul gana) = logística del margen final de la v0)\n',
         'Mismas filas, claves, exclusiones, leyes y a, b que la v0 de arriba. Margen m = vivos azules/azules iniciales '
         '− vivos rojos/rojos iniciales al final de la simulación v0 (bando a 0 o `capSeconds`). P = 1/(1+e^(−k·m)), '
         'una sola pendiente k global (sin término independiente) que minimiza el Brier en las semillas pares. '
         'Empate (tope de tiempo) = "no gana".\n',
         f'Pendiente ajustada k = {k:.3f} (deja fuera un tamaño: k = {ks:.3f}, ajustada en rojos ≤ 32).\n',
         '## Brier\n',
         '| validación | n | Brier v1 | referencia: clase mayoritaria del emparejamiento (0/1) | frecuencia del emparejamiento (informativo) | moneda |',
         '|---|---|---|---|---|---|',
         f'| dentro de muestra (pares) | {len(ins)} | {brier(ins):.4f} | — | — | 0.25 |',
         f'| fuera de muestra (pares → impares) | {len(out)} | {brier(out):.4f} | {ref_maj:.4f} | {ref_freq:.4f} | 0.25 |',
         f'| deja fuera un tamaño (rojos ≤ 32 → ≥ 48) | {len(size)} | {brier(size):.4f} | {size_maj:.4f} | {size_freq:.4f} | 0.25 |',
         '',
         '## Calibración fuera de muestra (5 tramos de P)\n',
         '| tramo de P | n | P media predicha | frecuencia real de victoria azul | diferencia |',
         '|---|---|---|---|---|']
    for lo, hi, n, pm, fr in cal:
        if n:
            L.append(f'| {lo:.1f}–{hi:.1f} | {n} | {pm:.3f} | {fr:.3f} | {pm - fr:+.3f} |')
        else:
            L.append(f'| {lo:.1f}–{hi:.1f} | 0 | — | — | — |')
    n_o, _, mae_o, _ = summary(ev_out)
    n_s, _, mae_s, _ = summary(ev_size)
    L.append('')
    L.append(f'Error de vivos azules (sin cambio, es el de la v0): fuera de muestra {mae_o:.3f} (n {n_o}); '
             f'deja fuera un tamaño {mae_s:.3f} (n {n_s}).\n')
    L.append(f'Máxima diferencia de calibración en tramos con n ≥ 50: {worst:.3f}.\n')
    L.append(f'Veredicto (regla de F111): **{"APRUEBA" if ok else "NO APRUEBA"}** — Brier v1 {brier(out):.4f} '
             f'{"≤" if brier(out) <= ref_maj else ">"} referencia {ref_maj:.4f}; tramos n ≥ 50 '
             f'{"todos" if cal_ok else "no todos"} a ≤ 0,10.\n')
    return L, {'k': k, 'brier_out': brier(out), 'ref_maj': ref_maj, 'ref_freq': ref_freq, 'brier_size': brier(size),
               'size_maj': size_maj, 'size_freq': size_freq, 'worst': worst, 'ok': ok}


# ---- F113 (v2): término independiente y un factor de daño. Solo se usa con --v2; v0 y v1 no cambian. ----

def margin_d(r, params, laws, keyf, d):
    """Margen de la v0 con el ritmo de bajas que hace el azul (a) multiplicado por d (d = 1: el de la v1)."""
    a, b, _ = params[keyf(r)]
    A, R, _, _ = simulate(r['A0'], r['R0'], a * d, b, laws[r['bc']], laws['r:' + r['rc']], r['cap'])
    return A / max(1, r['A0']) - R / max(1, r['R0'])


def logistic2(k, b, m):
    z = max(-60.0, min(60.0, k * m + b))
    return 1.0 / (1.0 + math.exp(-z))


def argmin_1d(f, lo, hi, scan=40, it=30):
    """Barrido grueso y luego sección áurea entre los vecinos del mejor punto."""
    xs = [lo + (hi - lo) * i / scan for i in range(scan + 1)]
    vs = [f(x) for x in xs]
    i = min(range(len(xs)), key=lambda j: vs[j])
    a, c = xs[max(0, i - 1)], xs[min(scan, i + 1)]
    g = (math.sqrt(5.0) - 1.0) / 2.0
    x1, x2 = c - g * (c - a), a + g * (c - a)
    f1, f2 = f(x1), f(x2)
    for _ in range(it):
        if f1 <= f2:
            c, x2, f2 = x2, x1, f1
            x1 = c - g * (c - a)
            f1 = f(x1)
        else:
            a, x1, f1 = x1, x2, f2
            x2 = a + g * (c - a)
            f2 = f(x2)
    best = min((vs[i], xs[i]), (f1, x1), (f2, x2))
    return best[1]


def fit_kb(pairs_m, fix_b=False):
    """k >= 0 y b (término independiente) que minimizan el Brier; descenso por coordenadas. fix_b: b = 0."""
    k, b = 3.0, 0.0
    for _ in range(1 if fix_b else 5):
        k = argmin_1d(lambda x: brier([(logistic2(x, b, m), y) for m, y in pairs_m]), 0.0, 40.0)
        if fix_b:
            break
        b = argmin_1d(lambda x: brier([(logistic2(k, x, m), y) for m, y in pairs_m]), -6.0, 6.0)
    return k, b


def fit_dkb(rows, params, laws, keyf):
    """Factor de daño d (rejilla log 1/4..4, luego áurea en log) con k, b reajustados para cada d."""
    cache = {}

    def score(ld):
        if ld not in cache:
            pm = [(margin_d(r, params, laws, keyf, math.exp(ld)), won(r)) for r in rows]
            k, b = fit_kb(pm)
            cache[ld] = (brier([(logistic2(k, b, m), y) for m, y in pm]), k, b)
        return cache[ld][0]
    ld = argmin_1d(score, math.log(0.25), math.log(4.0), scan=8, it=12)
    score(ld)
    best = min(cache.items(), key=lambda kv: kv[1][0])
    return math.exp(best[0]), best[1][1], best[1][2]


def blue_frac(r, params, laws, keyf):
    a, b, _ = params[keyf(r)]
    A, _, _, _ = simulate(r['A0'], r['R0'], a, b, laws[r['bc']], laws['r:' + r['rc']], r['cap'])
    return A / max(1, r['A0'])


def fit_scale(fr):
    """fr = [(vivos predichos v0 en fracción, vivos reales en fracción)]. Una escala s que minimiza el error medio."""
    f = lambda s: sum(abs(min(1.0, s * p) - o) for p, o in fr) / len(fr)
    return argmin_1d(f, 0.0, 2.0, scan=40, it=30)


def v2_section(train, test, params, laws, small, big, sparams, shared, v1):
    key = lambda r: r['key']
    skey = lambda r: r['skey']
    tr = [r for r in train if key(r) in params]
    te = [r for r in test if key(r) in params]
    # prueba de vida: b = 0 y d = 1 tiene que dar el Brier de la v1 (+-0,001)
    pm1 = [(margin_d(r, params, laws, key, 1.0), won(r)) for r in tr]
    k1, _ = fit_kb(pm1, fix_b=True)
    life = brier([(logistic2(k1, 0.0, margin_d(r, params, laws, key, 1.0)), won(r)) for r in te])
    life_ok = abs(life - v1['brier_out']) <= 0.001
    d, k, b = fit_dkb(tr, params, laws, key)
    ins = [(logistic2(k, b, margin_d(r, params, laws, key, d)), won(r)) for r in tr]
    out = [(logistic2(k, b, margin_d(r, params, laws, key, d)), won(r)) for r in te]
    sm = [r for r in small if r['skey'] in shared and skey(r) in sparams]
    bg = [r for r in big if r['skey'] in shared and skey(r) in sparams]
    ds, ks, bs = fit_dkb(sm, sparams, laws, skey)
    size = [(logistic2(ks, bs, margin_d(r, sparams, laws, skey, ds)), won(r)) for r in bg]
    cal = calibration(out)
    worst = max((abs(c[3] - c[4]) for c in cal if c[2] >= 50), default=float('nan'))
    # vivos: escala s sobre los vivos azules de la v0, ajustada en pares (y en rojos <= 32 para dejar fuera un tamaño)
    fobs = lambda r: r['A'] / max(1, r['A0'])
    s = fit_scale([(blue_frac(r, params, laws, key), fobs(r)) for r in tr])
    ss = fit_scale([(blue_frac(r, sparams, laws, skey), fobs(r)) for r in sm])
    e0 = sum(abs(blue_frac(r, params, laws, key) - fobs(r)) for r in te) / max(1, len(te))
    e2 = sum(abs(min(1.0, s * blue_frac(r, params, laws, key)) - fobs(r)) for r in te) / max(1, len(te))
    e0s = sum(abs(blue_frac(r, sparams, laws, skey) - fobs(r)) for r in bg) / max(1, len(bg))
    e2s = sum(abs(min(1.0, ss * blue_frac(r, sparams, laws, skey)) - fobs(r)) for r in bg) / max(1, len(bg))
    bo, bsz = brier(out), brier(size)
    if bo <= 0.11 and bsz <= 0.176 and worst <= 0.08:
        verdict = 'APRUEBA'
    elif bo >= 0.115:
        verdict = 'NO'
    else:
        verdict = 'ENTRE MEDIAS'
    L = ['', '# F113 · modelo sin entidades v2 (término independiente + factor de daño azul; vivos con escala)\n',
         'Mismas filas, claves, particiones y a, b que la v0/v1. P = 1/(1+e^(−(k·m + b))), m = margen final de la v0 '
         'con el ritmo de bajas del azul multiplicado por d (un solo d global, nada por emparejamiento). d por rejilla log '
         '1/4..4 + sección áurea; k, b por descenso por coordenadas, minimizando el Brier en pares. Vivos v2 = '
         'min(1, s · vivos v0), una escala global s ajustada en pares (error medio absoluto).\n',
         f'Filas: ajuste (pares) {len(tr)}, prueba (impares) {len(te)}; deja fuera un tamaño: ajuste {len(sm)}, '
         f'prueba {len(bg)}.\n',
         f'Prueba de vida (b = 0, d = 1, k reajustado = {k1:.3f}): Brier fuera {life:.4f} frente a v1 '
         f'{v1["brier_out"]:.4f} → **{"VÁLIDA" if life_ok else "NO VÁLIDA"}** (±0,001).\n',
         f'Parámetros v2: d = {d:.3f}, k = {k:.3f}, b = {b:+.3f} (deja fuera un tamaño: d = {ds:.3f}, k = {ks:.3f}, '
         f'b = {bs:+.3f}). Escala de vivos s = {s:.3f} (deja fuera un tamaño: {ss:.3f}).\n',
         '## Brier\n',
         '| validación | n | v1 | v2 | frecuencia del emparejamiento (informativo) |',
         '|---|---|---|---|---|',
         f'| dentro de muestra (pares) | {len(ins)} | — | {brier(ins):.4f} | — |',
         f'| fuera de muestra (pares → impares) | {len(out)} | {v1["brier_out"]:.4f} | {bo:.4f} | {v1["ref_freq"]:.4f} |',
         f'| deja fuera un tamaño (rojos ≤ 32 → ≥ 48) | {len(size)} | {v1["brier_size"]:.4f} | {bsz:.4f} | {v1["size_freq"]:.4f} |',
         '',
         '## Calibración v2 fuera de muestra (5 tramos de P)\n',
         '| tramo de P | n | P media predicha | frecuencia real de victoria azul | diferencia |',
         '|---|---|---|---|---|']
    for lo, hi, n, pmn, fr in cal:
        if n:
            L.append(f'| {lo:.1f}–{hi:.1f} | {n} | {pmn:.3f} | {fr:.3f} | {pmn - fr:+.3f} |')
        else:
            L.append(f'| {lo:.1f}–{hi:.1f} | 0 | — | — | — |')
    signs = ''.join('+' if c[3] - c[4] > 0 else '−' for c in cal if c[2])
    L.append('')
    L.append(f'Signo de la diferencia por tramo (con n > 0): {signs}. Máxima diferencia con n ≥ 50: {worst:.3f}.\n')
    L.append('## Vivos azules (error medio absoluto, fracción del bando)\n')
    L.append('| validación | n | v0/v1 | v2 (escala s) |')
    L.append('|---|---|---|---|')
    L.append(f'| fuera de muestra (pares → impares) | {len(te)} | {e0:.3f} | {e2:.3f} |')
    L.append(f'| deja fuera un tamaño (rojos ≤ 32 → ≥ 48) | {len(bg)} | {e0s:.3f} | {e2s:.3f} |')
    L.append('')
    L.append(f'Veredicto (regla de F113): **{verdict}** — Brier fuera {bo:.4f} (≤ 0,11 aprueba, ≥ 0,115 NO), dejando '
             f'fuera un tamaño {bsz:.4f} (≤ 0,176), peor tramo n ≥ 50 {worst:.3f} (≤ 0,08). '
             f'**VIVOS {"APRUEBA" if e2 <= 0.12 else "NO APRUEBA"}** — error de vivos v2 fuera {e2:.3f} (≤ 0,12).\n')
    return L


def main():
    t0 = time.time()
    scen = {s['id']: s for s in json.load(open(SCEN, encoding='utf-8'))['scenarios']}
    raw = load_rows()
    ignored = collections.Counter()
    excl = collections.Counter()
    runs = []
    for row in raw:
        if row.get('control') or row.get('stuck') or row.get('retried'):
            ignored['control/stuck/retried'] += 1
            continue
        sc = scen.get(row['scenario'])
        if sc is None:
            ignored['escenario desconocido'] += 1
            continue
        why_ex = excluded(sc)
        if why_ex:
            excl[why_ex] += 1
            continue
        k, ks = keys(row, sc)
        runs.append({'row': row, 'key': k, 'skey': ks, 'tags': sc.get('tags', []), 'terrain': sc.get('terrain', '?'),
                     'bc': blue_class(row, sc), 'rc': red_class(sc), 'seed': row['seed'],
                     'A0': row['blue']['start'], 'R0': row['red']['start'], 'cap': row.get('capSeconds', 180.0),
                     'A': row['blue']['survivors'], 'R': row['red']['survivors'], 'T': row['seconds'],
                     'W': row['winner']})
    classes = sorted({r['bc'] for r in runs}) + sorted({'r:' + r['rc'] for r in runs})
    laws = {c: 1.5 for c in classes}
    train = [r for r in runs if r['seed'] % 2 == 0]
    test = [r for r in runs if r['seed'] % 2 == 1]

    # leyes por clase: una pasada de descenso por coordenadas sobre las semillas pares
    def class_loss(c, lw):
        sub = [r for r in train if r['bc'] == c or 'r:' + r['rc'] == c]
        return sum(p[2] for p in fit_all(sub, lw).values())
    for c in classes:
        best = None
        for v in LAWS:
            lw = dict(laws)
            lw[c] = v
            s = class_loss(c, lw)
            if best is None or s < best[0]:
                best = (s, v)
        laws[c] = best[1]

    params = fit_all(train, laws)
    ev_in = evaluate(train, params, laws, lambda r: r['key'])
    ev_out = evaluate(test, params, laws, lambda r: r['key'])

    # deja fuera un tamaño: ajusta con rojos <= 32 y predice rojos >= 48 (misma clave sin número de rojos)
    small = [r for r in runs if r['R0'] <= 32]
    big = [r for r in runs if r['R0'] >= 48]
    shared = {r['skey'] for r in small} & {r['skey'] for r in big}
    sparams = {}
    for sk, rs in grouped([r for r in small if r['skey'] in shared], lambda r: r['skey']).items():
        sparams[sk] = fit(to_groups(rs), laws[rs[0]['bc']], laws['r:' + rs[0]['rc']])
    ev_size = evaluate([r for r in big if r['skey'] in shared], sparams, laws, lambda r: r['skey'])

    secs = time.time() - t0
    L = []
    L.append('# F83 · modelo sin entidades v0 (Lanchester por emparejamiento)\n')
    L.append(f'Generado por `tools/fuera_de_vista.py` en {secs:.0f} s. Filas leídas {len(raw)}; usadas {len(runs)} '
             f'en {len(grouped(runs, lambda r: r["key"]))} emparejamientos.\n')
    L.append('Modelo: dA/dt = −b·R·A^(2−p), dR/dt = −a·A·R^(2−q); p, q = ley del arma del que dispara (1 lineal, '
             '2 cuadrada), pasos de 1 s hasta un bando a 0 o `capSeconds` (empate). a, b por emparejamiento (rejilla '
             'log); clave = Rally/GV, kit azul (fracciones de `extra.kit:*`), tier, nº azul, enemigo (+arma), terreno, nº rojo.\n')
    L.append('Leyes ajustadas: ' + ', '.join(f'`{c}` = {v}' for c, v in laws.items()) + '\n')
    L.append('Excluidas (no campo abierto o jefes): ' + ', '.join(f'{k} {v}' for k, v in excl.most_common())
             + '; ignoradas: ' + ', '.join(f'{k} {v}' for k, v in ignored.items()) + '\n')
    L.append('## Totales\n')
    L.append('| validación | n | ganador acertado | error medio vivos azules (fracción del bando) | (de 8) | error medio segundos |')
    L.append('|---|---|---|---|---|---|')
    for name, ev in (('dentro de muestra (semillas pares)', ev_in), ('fuera de muestra (semillas impares)', ev_out),
                     ('deja fuera un tamaño (rojos ≤ 32 → ≥ 48)', ev_size)):
        n, acc, mae, met = summary(ev)
        L.append(f'| {name} | {n} | {100 * acc:.1f} % | {mae:.3f} | {8 * mae:.2f} | {met:.1f} |')
    # techo: un modelo determinista da un solo ganador por emparejamiento -> como mucho acierta la clase mayoritaria
    maj = sum(collections.Counter(r['W'] for r in rs).most_common(1)[0][1] for rs in grouped(train, lambda r: r['key']).values())
    L.append(f'\nTecho de acierto de ganador de cualquier modelo determinista por emparejamiento (clase mayoritaria, '
             f'semillas pares): {100 * maj / max(1, len(train)):.1f} %.\n')
    L.append('## Por emparejamiento (fuera de muestra)\n')
    L.append('| emparejamiento | n | ganador acertado | error vivos (fracción) | sesgo vivos | error s | sesgo s | a | b |')
    L.append('|---|---|---|---|---|---|---|---|---|')
    scored = []
    for k, es in sorted(ev_out.items(), key=lambda kv: fmt_key(kv[0])):
        n = len(es)
        acc = sum(e['ok'] for e in es) / n
        mae = sum(abs(e['eA']) for e in es) / n
        bias = sum(e['eA'] for e in es) / n
        met = sum(abs(e['eT']) for e in es) / n
        bt = sum(e['eT'] for e in es) / n
        a, b, _ = params[k]
        L.append(f'| {fmt_key(k)} | {n} | {100 * acc:.0f} % | {mae:.2f} | {bias:+.2f} | {met:.0f} | {bt:+.0f} | {a:.2g} | {b:.2g} |')
        scored.append(((1 - acc) + mae, k, acc, mae, [e['r'] for e in es]))
    L.append('')
    L.append('## Los 10 peor predichos (fuera de muestra: (1 − acierto) + error de vivos)\n')
    for s, k, acc, mae, rs in sorted(scored, key=lambda x: -x[0])[:10]:
        allrs = [r for r in runs if r['key'] == k]
        L.append(f'- {fmt_key(k)} — acierto {100 * acc:.0f} %, error {mae:.2f}: {why(allrs)}')
    L.append('')
    if ev_size:
        L.append('## Deja fuera un tamaño, por clave\n')
        L.append('| clave (sin nº rojo) | rojos predichos | n | ganador acertado | error vivos (fracción) |')
        L.append('|---|---|---|---|---|')
        for k, es in sorted(ev_size.items(), key=lambda kv: fmt_key(kv[0])):
            n = len(es)
            L.append(f"| {fmt_key(es[0]['r']['skey'])} | {es[0]['r']['R0']} | {n} | "
                     f"{100 * sum(e['ok'] for e in es) / n:.0f} % | {sum(abs(e['eA']) for e in es) / n:.2f} |")
    if '--v1' in sys.argv[1:] or '--v2' in sys.argv[1:]:
        V, res = v1_section(train, test, params, laws, small, big, sparams, shared, ev_out, ev_size)
        L.extend(V)
        print('\n'.join(V))
        print(f"v1 total {time.time() - t0:.0f} s")
        if '--v2' in sys.argv[1:]:
            V2 = v2_section(train, test, params, laws, small, big, sparams, shared, res)
            L.extend(V2)
            print('\n'.join(V2))
            print(f"v2 total {time.time() - t0:.0f} s")
    out = os.path.join(DATA, 'fuera_de_vista.md')
    with open(out, 'w', encoding='utf-8') as fh:
        fh.write('\n'.join(L) + '\n')
    print('\n'.join(L[:16]))
    print(f'-> {out} ({secs:.0f} s)')


if __name__ == '__main__':
    sys.exit(main())

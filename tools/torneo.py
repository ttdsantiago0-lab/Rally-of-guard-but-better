#!/usr/bin/env python3
"""Torneo de doctrinas (docs/TORNEO_DOCTRINAS.md): genera escenarios en el JSON de la simulación y analiza resultados.

  python3 tools/torneo.py gen-cal   <ronda>            # calibración: respuesta base a varios números de rojos
  python3 tools/torneo.py gen-rank  <ronda> <cal.json> # descarte: todas las respuestas al número calibrado
  python3 tools/torneo.py analyze   <runs.jsonl...>    # tabla por (amenaza, composición, respuesta)
  python3 tools/torneo.py gen-duel   <ronda>           # duelos: 1 guardia (espada / pesado sin escudo / escudo) contra k mobs
  python3 tools/torneo.py gen-volea  <ronda> <cal.json> # tiradores: base / quietos / volea, a 30 y a 70 bloques
  python3 tools/torneo.py gen-creeper <ronda>          # creepers: ignorarlos (Guard Villagers) o atacarlos (gv:creepers)
  python3 tools/torneo.py duel     <runs.jsonl...>     # tabla de duelos
  python3 tools/torneo.py groups   <runs.jsonl...>     # tabla genérica por escenario (sin el -r/-d final) con lo que HICIERON

Las ids siguen el patrón  t<ronda>-<fase>-<amenaza>-<comp>-<respuesta>[-r<N>]  para poder agruparlas.
"""
import json, sys, os, math, collections, copy

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
SCN = os.path.join(ROOT, 'src/main/resources/rallyguard_sim/scenarios.json')

THREATS = {  # amenaza -> números de rojos a probar en calibración (8 guardias azules)
    # 1ª pasada (6-18 / 4-13 / 3-12): los mobs nativos son mucho más débiles que los "igualados" de antes - 8 arqueros
    # ganaban el 100 % incluso contra 18 zombis o 13 esqueletos. 2ª pasada más arriba; calibrate() junta ambas.
    'zombie': [22, 30, 40],
    'skeleton': [18, 24, 32],
    'creeper': [16, 22, 30],
}
COMPS = {  # nombre corto -> rol del bando azul
    'mele': 'melee',
    'escudo': 'shield',
    'arco': 'archer',
    'linea': 'mix:shield=4,archer=4',
    'cab': 'mix:melee@m=4,archer@m=4',
}
RANGED = {'arco', 'linea', 'cab'}
RESPONSES = {  # nombre -> (tags extra, cambios al bando azul)
    'base': ([], {}),
    'mantener': (['instruction:MANTENER_LINEA'], {}),
    'hostigar': (['instruction:HOSTIGAR'], {}),
    'avanzar': (['instruction:AVANZAR'], {}),
    'flanquear': (['instruction:FLANQUEAR'], {}),
    'circulo': ([], {'formation': 'CIRCLE'}),
    'prudente': (['force', 'unit:8', 'nodrive', 'doctrine:AVOID_CASUALTIES'], {}),
    'hostil': (['force', 'unit:8', 'nodrive', 'doctrine:HARASS'], {}),
    'lineafija': (['force', 'unit:8', 'nodrive', 'op:HOLD_LINE'], {}),
    'volea': ([], {'fireMode': 'VOLLEY'}),        # solo composiciones con rango
    'quietos': ([], {'fireMode': 'STILL'}),       # solo composiciones con rango
}
BLUE_N = 8


def scenario(sid, threat, comp, resp, reds):
    tags, blue_extra = RESPONSES[resp]
    blue = {'kind': 'guard', 'count': BLUE_N, 'role': COMPS[comp], 'rank': 1}
    blue.update(blue_extra)
    return {'id': sid, 'terrain': 'flat', 'distance': 30, 'position': 'front', 'minutes': 2,
            'tags': ['torneo', 'threat:' + threat, 'comp:' + comp, 'resp:' + resp] + tags,
            'note': f'Torneo: {threat} vs {comp} ({COMPS[comp]}), respuesta {resp}, {reds} rojos',
            # red never inherits blue's knobs (the sim copies unset red fields from blue): before this fix the
            # "circulo" response also made the ZOMBIES spawn in a circle, so its result was confounded.
            'blue': blue, 'red': {'kind': threat, 'count': reds, 'role': 'native', 'rank': 0, 'formation': 'LINE',
                                  'fireMode': 'NORMAL', 'mounted': False}}


# Ronda 2 (2026-11-04, David): primero se MIDE qué comportamiento funciona (duelos, conteos de lo que hicieron:
# disparos por arco, disparos no hechos y por qué, golpes parados con el escudo...) y luego se le pone nombre.
DUEL_ROLES = {'espada': 'melee', 'pesado': 'heavy', 'escudo': 'shield'}  # pesado = el hierro del escudo, sin escudo
DUEL_REDS = {'zombie': [1, 2, 3, 4], 'skeleton': [1, 2, 3], 'creeper': [1, 2]}


def red_side(threat, reds):
    return {'kind': threat, 'count': reds, 'role': 'native', 'rank': 0, 'formation': 'LINE', 'fireMode': 'NORMAL',
            'mounted': False}


def gen_duel(rnd, seeds=30):
    d = load(); new = []; presets = {}
    for t, counts in DUEL_REDS.items():
        ids = []
        for name, role in DUEL_ROLES.items():
            for k in counts:
                sid = f't{rnd}-duel-{t}-{name}-r{k}'
                tags = ['torneo', 'duel', 'threat:' + t, 'comp:' + name] + (['gv:creepers'] if t == 'creeper' else [])
                new.append({'id': sid, 'terrain': 'flat', 'distance': 10, 'position': 'front', 'minutes': 2, 'tags': tags,
                            'note': f'Duelo: 1 guardia {name} ({role}) contra {k} {t}',
                            'blue': {'kind': 'guard', 'count': 1, 'role': role, 'rank': 1}, 'red': red_side(t, k)})
                ids.append(sid)
        presets[f't{rnd}-duel-{t}'] = {'scenarios': ids, 'seeds': seeds, 'minutes': 2}
    # zombie es el más largo: va solo; esqueleto y creeper juntos
    presets[f't{rnd}-duel-sc'] = {'scenarios': presets.pop(f't{rnd}-duel-skeleton')['scenarios']
                                  + presets.pop(f't{rnd}-duel-creeper')['scenarios'], 'seeds': seeds, 'minutes': 2}
    replace_round(d, f't{rnd}-duel-', new, presets); save(d)
    print('presets:', ','.join(presets))


def gen_volea(rnd, cal_path, seeds=30):
    """Ranged comps, base / still / volley, at the usual 30 blocks and at 70 (where only a volley reaches)."""
    cal = json.load(open(cal_path))
    d = load(); new = []; presets = {}
    for dist in (30, 70):
        ids = []
        for t in ('zombie', 'skeleton'):
            for c in ('arco', 'linea'):
                n = cal[f'{t}|{c}']['reds']
                for resp in ('base', 'quietos', 'volea'):
                    sid = f't{rnd}-volea-{t}-{c}@{dist}-{resp}'
                    sc = scenario(sid, t, c, resp, n)
                    sc['distance'] = dist
                    new.append(sc); ids.append(sid)
        presets[f't{rnd}-volea-{dist}'] = {'scenarios': ids, 'seeds': seeds, 'minutes': 2}
    replace_round(d, f't{rnd}-volea-', new, presets); save(d)
    print('presets:', ','.join(presets))


def gen_creeper(rnd, seeds=10):
    """Every comp, creepers ignored (GV's default list) or attacked (gv:creepers), at three counts: curve + duel."""
    d = load(); new = []; ids = []
    for c in COMPS:
        for mode in ('ignorar', 'atacar'):
            for n in (12, 24, 40):
                sid = f't{rnd}-creeper-{c}-{mode}-r{n}'
                sc = scenario(sid, 'creeper', c, 'base', n)
                sc['tags'] = [x for x in sc['tags'] if not x.startswith('resp:')] + ['resp:' + mode]
                if mode == 'atacar':
                    sc['tags'].append('gv:creepers')
                new.append(sc); ids.append(sid)
    replace_round(d, f't{rnd}-creeper-', new, {f't{rnd}-creeper': {'scenarios': ids, 'seeds': seeds, 'minutes': 2}})
    save(d)
    print('presets:', f't{rnd}-creeper')


def _mean(v):
    return sum(v) / len(v) if v else float('nan')


def _se(v):
    if len(v) < 2:
        return float('nan')
    m = _mean(v)
    return math.sqrt(sum((x - m) ** 2 for x in v) / (len(v) - 1) / len(v))


def acts(rs):
    """Mean per run of every 'act:' counter (what the guards did)."""
    keys = sorted({k for r in rs for k in r.get('extra', {}) if k.startswith('act:')})
    return {k[4:]: _mean([r['extra'].get(k, 0.0) for r in rs]) for k in keys}


def duel(paths):
    by = collections.defaultdict(list)
    for r in read_runs(paths):
        p = r['scenario'].split('-')
        if len(p) == 5 and p[1] == 'duel':
            by[(p[2], p[3], int(p[4][1:]))].append(r)
    out = ['| Amenaza | Guardia | Rojos | n | Victoria | Vida que le queda | Segundos | Golpes parados | Daño parado | Golpes con escudo bajo |',
           '|---|---|---|---|---|---|---|---|---|---|']
    for (t, g, k), rs in sorted(by.items(), key=lambda x: (x[0][0], x[0][2], list(DUEL_ROLES).index(x[0][1]))):
        w = [win(r) for r in rs]
        hp = [r['blue']['healthLeft'] for r in rs]
        a = acts(rs)
        out.append(f"| {t} | {g} | {k} | {len(rs)} | {_mean(w):.0%} ± {_se(w):.0%} | {_mean(hp):.1f} | "
                   f"{_mean([r['seconds'] for r in rs]):.0f} | {a.get('shieldBlocks', 0):.1f} | {a.get('shieldBlockedDmg', 0):.0f} | "
                   f"{a.get('hitShieldDown', 0):.1f} |")
    return '\n'.join(out)


def groups(paths):
    """Generic table: one row per scenario family (the id without its trailing -r<N>), outcome + behaviour."""
    by = collections.defaultdict(list)
    for r in read_runs(paths):
        by[r['scenario']].append(r)
    out = []
    for sid, rs in sorted(by.items()):
        w = [win(r) for r in rs]
        sh = _mean([r['blue']['shots'] for r in rs]); hi = _mean([r['blue']['hits'] for r in rs])
        a = acts(rs)
        top = ', '.join(f'{k}={v:.1f}' for k, v in sorted(a.items(), key=lambda x: -x[1])[:6])
        out.append(f"{sid:42} n={len(rs):3} win={_mean(w):.0%}±{_se(w):.0%} surv={_mean([r['blue']['survivors'] / max(1, r['blue']['start']) for r in rs]):.0%} "
                   f"redSurv={_mean([r['red']['survivors'] for r in rs]):.1f} shots={sh:.0f} hit%={hi / sh if sh else 0:.0%} | {top}")
    return '\n'.join(out)


def load():
    return json.loads(open(SCN, encoding='utf-8').read())


def save(d):
    open(SCN, 'w', encoding='utf-8', newline='\n').write(json.dumps(d, ensure_ascii=False, indent=1) + '\n')


def replace_round(d, prefix, new, presets):
    d['scenarios'] = [s for s in d['scenarios'] if not s['id'].startswith(prefix)] + new
    for k in [k for k in d['presets'] if k.startswith(prefix)]:
        del d['presets'][k]
    d['presets'].update(presets)


def gen_cal(rnd):
    d = load(); new = []; presets = {}
    for t, counts in THREATS.items():
        ids = []
        for c in COMPS:
            for n in counts:
                sid = f't{rnd}-cal-{t}-{c}-base-r{n}'
                new.append(scenario(sid, t, c, 'base', n)); ids.append(sid)
        presets[f't{rnd}-cal-{t}'] = {'scenarios': ids, 'seeds': 4, 'minutes': 2}
    replace_round(d, f't{rnd}-cal-', new, presets); save(d)
    print('presets:', ','.join(presets))


def win(r):
    return 1.0 if r['winner'] == 'blue' else 0.5 if r['winner'] == 'draw' else 0.0


def read_runs(paths):
    out = []
    for p in paths:
        for line in open(p, encoding='utf-8'):
            line = line.strip()
            if line:
                out.append(json.loads(line))
    return out


def calibrate(paths):
    """(threat, comp) -> red count where the base response wins ~50 % (linear interpolation, clamped)."""
    by = collections.defaultdict(lambda: collections.defaultdict(list))
    for r in read_runs(paths):
        p = r['scenario'].split('-')
        if len(p) < 6 or p[1] != 'cal':
            continue
        by[(p[2], p[3])][int(p[-1][1:])].append(win(r))
    cal = {}
    for key, rows in sorted(by.items()):
        pts = sorted((n, sum(v) / len(v)) for n, v in rows.items())
        pick = pts[-1][0]
        for (n0, w0), (n1, w1) in zip(pts, pts[1:]):
            if w0 >= 0.5 >= w1:
                pick = n0 if w0 == w1 else round(n0 + (w0 - 0.5) * (n1 - n0) / (w0 - w1))
                break
        else:
            if pts[0][1] < 0.5:
                pick = max(1, pts[0][0] - 2)
        cal[f'{key[0]}|{key[1]}'] = {'reds': pick, 'curve': pts}
    return cal


def gen_rank(rnd, cal_path):
    cal = json.load(open(cal_path))
    d = load(); new = []; presets = {}
    for t in THREATS:
        ids = []
        for c in COMPS:
            n = cal.get(f'{t}|{c}', {}).get('reds')
            if not n:
                continue
            for resp in RESPONSES:
                if resp in ('volea', 'quietos') and c not in RANGED:
                    continue
                sid = f't{rnd}-rank-{t}-{c}-{resp}'
                new.append(scenario(sid, t, c, resp, n)); ids.append(sid)
        half = (len(ids) + 1) // 2  # two machines per threat (CI runs presets in parallel)
        presets[f't{rnd}-rank-{t}-a'] = {'scenarios': ids[:half], 'seeds': 6, 'minutes': 2}
        presets[f't{rnd}-rank-{t}-b'] = {'scenarios': ids[half:], 'seeds': 6, 'minutes': 2}
    replace_round(d, f't{rnd}-rank-', new, presets); save(d)
    print('presets:', ','.join(presets))


def analyze(paths):
    rows = collections.defaultdict(list)
    for r in read_runs(paths):
        p = r['scenario'].split('-')
        if len(p) < 5 or p[0][0] != 't' or p[1] not in ('rank', 'final'):
            continue
        rows[(p[2], p[3], p[4])].append(r)
    table = []
    for (t, c, resp), rs in sorted(rows.items()):
        n = len(rs)
        w = [win(r) for r in rs]
        surv = [r['blue']['survivors'] / max(1, r['blue']['start']) for r in rs]
        secs = [r['seconds'] for r in rs if r['winner'] == 'blue']
        m = lambda v: sum(v) / len(v) if v else float('nan')
        se = lambda v: (math.sqrt(sum((x - m(v)) ** 2 for x in v) / (len(v) - 1) / len(v)) if len(v) > 1 else float('nan'))
        table.append({'threat': t, 'comp': c, 'resp': resp, 'n': n, 'win': m(w), 'win_se': se(w),
                      'surv': m(surv), 'surv_se': se(surv), 'kill_s': m(secs)})
    return table


def gen_final(rnd, cal_path, paths, seeds=30):
    """Final: per (threat, comp) the base and the 2 best responses of the elimination round, n = seeds."""
    cal = json.load(open(cal_path))
    rows = analyze(paths)
    d = load(); new = []; ids_by_t = collections.defaultdict(list)
    for t in THREATS:
        for c in COMPS:
            rs = [r for r in rows if r['threat'] == t and r['comp'] == c]
            if not rs:
                continue
            cand = sorted((r for r in rs if r['resp'] != 'base'), key=lambda r: (-r['win'], -r['surv']))[:2]
            n = cal[f'{t}|{c}']['reds']
            for resp in ['base'] + [r['resp'] for r in cand]:
                sid = f't{rnd}-final-{t}-{c}-{resp}'
                new.append(scenario(sid, t, c, resp, n)); ids_by_t[t].append(sid)
    presets = {}
    for t, ids in ids_by_t.items():
        half = (len(ids) + 1) // 2
        presets[f't{rnd}-final-{t}-a'] = {'scenarios': ids[:half], 'seeds': seeds, 'minutes': 2}
        presets[f't{rnd}-final-{t}-b'] = {'scenarios': ids[half:], 'seeds': seeds, 'minutes': 2}
    replace_round(d, f't{rnd}-final-', new, presets); save(d)
    print('presets:', ','.join(presets))


def report(cal_path, paths):
    """Markdown: capacity per composition (from calibration) and, per (threat, comp), responses vs base."""
    cal = json.load(open(cal_path))
    rows = analyze(paths)
    out = ['## Capacidad de cada composición (8 guardias): enemigos con los que la respuesta base gana ≈ 50 %', '',
           '| Amenaza | ' + ' | '.join(COMPS) + ' |', '|---|' + '---|' * len(COMPS)]
    for t in THREATS:
        out.append(f'| {t} | ' + ' | '.join(str(cal.get(f'{t}|{c}', {}).get('reds', '–')) for c in COMPS) + ' |')
    out += ['', '## Respuestas frente a la base (descarte, n por fila; ± = error estándar)', '',
            '| Amenaza | Comp. | Respuesta | n | Victoria | Superv. | Δ victoria vs base | Veredicto |', '|---|---|---|---|---|---|---|---|']
    manual = {}
    for t in THREATS:
        for c in COMPS:
            rs = [r for r in rows if r['threat'] == t and r['comp'] == c]
            base = next((r for r in rs if r['resp'] == 'base'), None)
            if not base:
                continue
            ranked = sorted(rs, key=lambda r: (-r['win'], -r['surv']))
            for r in ranked:
                d = r['win'] - base['win']
                noise = 2 * math.sqrt((r['win_se'] or 0) ** 2 + (base['win_se'] or 0) ** 2)
                verdict = 'base' if r is base else ('MEJOR' if d > noise and d > 0 else 'PEOR' if -d > noise else 'ruido')
                out.append(f"| {t} | {c} | {r['resp']} | {r['n']} | {r['win']:.0%} ± {r['win_se']:.0%} | {r['surv']:.0%} | {d:+.0%} | {verdict} |")
            best = ranked[0]
            manual[f'{t}|{c}'] = {'best': best['resp'], 'win': round(best['win'], 2), 'base_win': round(base['win'], 2),
                                  'top3': [r['resp'] for r in ranked[:3]]}
    return '\n'.join(out), manual


if __name__ == '__main__':
    cmd = sys.argv[1]
    if cmd == 'gen-cal':
        gen_cal(sys.argv[2])
    elif cmd == 'calibrate':
        print(json.dumps(calibrate(sys.argv[2:]), indent=1))
    elif cmd == 'gen-rank':
        gen_rank(sys.argv[2], sys.argv[3])
    elif cmd == 'gen-final':
        gen_final(sys.argv[2], sys.argv[3], sys.argv[4:])
    elif cmd == 'report':
        md, manual = report(sys.argv[2], sys.argv[3:])
        print(md)
        print('\n<!-- manual -->\n' + json.dumps(manual, ensure_ascii=False))
    elif cmd == 'gen-duel':
        gen_duel(sys.argv[2])
    elif cmd == 'gen-volea':
        gen_volea(sys.argv[2], sys.argv[3])
    elif cmd == 'gen-creeper':
        gen_creeper(sys.argv[2])
    elif cmd == 'duel':
        print(duel(sys.argv[2:]))
    elif cmd == 'groups':
        print(groups(sys.argv[2:]))
    elif cmd == 'analyze':
        for row in analyze(sys.argv[2:]):
            print('{threat:9} {comp:7} {resp:11} n={n:3} win={win:.2f}±{win_se:.2f} surv={surv:.2f}±{surv_se:.2f} kill_s={kill_s:.0f}'.format(**row))

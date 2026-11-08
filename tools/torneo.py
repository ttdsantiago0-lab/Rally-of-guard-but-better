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
# Ronda 3 = la ronda 1 rehecha (2026-11-04): hasta el commit de este cambio Guard Villagers pisaba el kit de cada
# guardia de la simulación en su primer tick (espada o ballesta, armadura al azar, escudo ~47 %), así que las
# composiciones de t1 eran mezclas al azar. Con los kits de verdad la capacidad cambia: abanico más ancho.
CAL_COUNTS = {3: {'zombie': [8, 16, 24, 32, 40], 'skeleton': [6, 12, 18, 24, 32]},
              # Ronda 7 (2026-11-05): el techo con el manual de campo puesto en el juego (sin comida, como la ronda 3)
              7: {'zombie': [16, 24, 32, 48, 64], 'skeleton': [24, 36, 48, 64, 80]}}
COMPS = {  # nombre corto -> rol del bando azul
    'mele': 'melee',
    'escudo': 'shield',
    'arco': 'archer',
    'linea': 'mix:shield=4,archer=4',
    'cab': 'mix:melee@m=4,archer@m=4',
    'scutum': 'scutum',                  # 2026-11-06: el escudo romano (mismo hierro que 'escudo')
    'lineascutum': 'mix:scutum=4,archer=4',
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
    # ronda 3 (2026-11-04): comportamientos medidos, aún sin nombre de doctrina
    'quemarropa': (['clearance:1'], {}),          # tirar aunque haya un amigo a < 5 bloques del blanco (las flechas
                                                  # propias ya atraviesan a los nuestros); solo con rango
    'escudoarriba': (['gv:shieldAlways'], {}),    # escudo arriba siempre que haya blanco; solo con escudos
    # ronda 4 (comer y rotar, David 2026-11-04): con comida. En la mano izquierda la usa Guard Villagers (se aleja
    # del atacante, come y se cura); en la mochila la come Rally en el sitio por debajo del 40 % de vida.
    'comidamano': (['food:offhand'], {}),
    'comidamochila': (['food:pack', 'off:leftHand'], {}),  # off:leftHand: since the field manual Rally moves pack food
                                                           # to a free left hand, which would turn this into comidamano
    # Manual de campo en el juego (2026-11-04): 'base' ya trae las 4 reglas; 'antes' = las 4 apagadas (cómo era).
    'antes': (['off:clearance', 'off:creepers', 'off:shieldUp', 'off:leftHand', 'off:hold', 'off:dodge', 'off:calmEat'], {}),
    'sinesquiva': (['off:dodge'], {}),
    # Sonda de techo (2026-11-08, docs/SONDA_DE_TECHO.md): el guardia con trampa, una trampa cada vez y todas juntas
    'orapunta': (['oracle:aim'], {}), 'oracadencia': (['oracle:cadence'], {}), 'orablanco': (['oracle:target'], {}),
    'orakite': (['oracle:kite'], {}), 'oratodo': (['oracle:all'], {}),  # el manual de hoy sin apartarse de los creepers (para medir solo la esquiva)
}
ONLY = {'volea': RANGED, 'quietos': RANGED, 'quemarropa': RANGED, 'escudoarriba': {'escudo', 'linea'},
        'comidamano': {'mele', 'arco', 'linea', 'cab'}}  # escudos: la mano izquierda está ocupada
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
DUEL_ROLES = {'espada': 'melee', 'pesado': 'heavy', 'escudo': 'shield',  # pesado = el hierro del escudo, sin escudo
              'escudoarriba': 'shield'}  # escudo arriba todo el tiempo que haya blanco (GuardRaiseShield de GV)
DUEL_REDS = {'zombie': [1, 2, 3, 4], 'skeleton': [1, 2, 3], 'creeper': [1, 2]}


def red_side(threat, reds):
    return {'kind': threat, 'count': reds, 'role': 'native', 'rank': 0, 'formation': 'LINE', 'fireMode': 'NORMAL',
            'mounted': False}


def gen_duel(rnd, seeds=30, only=None):
    d = load(); new = []; presets = {}
    for t, counts in DUEL_REDS.items():
        ids = []
        for name, role in DUEL_ROLES.items():
            if only and name not in only:
                continue
            for k in counts:
                sid = f't{rnd}-duel-{t}-{name}-r{k}'
                tags = ['torneo', 'duel', 'threat:' + t, 'comp:' + name] + (['gv:creepers'] if t == 'creeper' else []) \
                    + (['gv:shieldAlways'] if name == 'escudoarriba' else [])
                new.append({'id': sid, 'terrain': 'flat', 'distance': 10, 'position': 'front', 'minutes': 2, 'tags': tags,
                            'note': f'Duelo: 1 guardia {name} ({role}) contra {k} {t}',
                            'blue': {'kind': 'guard', 'count': 1, 'role': role, 'rank': 1}, 'red': red_side(t, k)})
                ids.append(sid)
        presets[f't{rnd}-duel-{t}'] = {'scenarios': ids, 'seeds': seeds, 'minutes': 2}
    if only:  # a later addition (e.g. escudoarriba): one preset, the earlier duels stay as they were
        ids = [x for v in presets.values() for x in v['scenarios']]
        presets = {f't{rnd}-duel-{"-".join(only)}': {'scenarios': ids, 'seeds': seeds, 'minutes': 2}}
        d['scenarios'] = [x for x in d['scenarios'] if x['id'] not in ids] + new
        d['presets'].update(presets); save(d)
        print('presets:', ','.join(presets))
        return
    # zombie es el más largo: va solo; esqueleto y creeper juntos
    presets[f't{rnd}-duel-sc'] = {'scenarios': presets.pop(f't{rnd}-duel-skeleton')['scenarios']
                                  + presets.pop(f't{rnd}-duel-creeper')['scenarios'], 'seeds': seeds, 'minutes': 2}
    replace_round(d, f't{rnd}-duel-', new, presets); save(d)
    print('presets:', ','.join(presets))


def gen_volea(rnd, cal_path, seeds=20, dists=(70,)):
    """Ranged comps, base / still / volley at 70 blocks, where only a volley reaches (the descarte already has them at 30)."""
    cal = json.load(open(cal_path))
    d = load(); new = []; presets = {}
    for dist in dists:
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


# Ronda 6 (2026-11-05, David): oleadas. "Un grupo bien organizado debería sobrevivir varias oleadas; el límite es la
# comida o demasiados a la vez." Cada escenario: hasta WAVES olas, pausa de 20 s entre olas (comer, reagruparse).
WAVES = 10
WAVE_SETS = {'zombie': ('zombie', 12, None),                              # 12 zombis por ola
             'mixta': ('zombie', 10, 'zombie,skeleton,creeper')}          # 10 por ola, alternando el tipo


def gen_waves(rnd, seeds=10, comps=('linea', 'escudo', 'arco')):
    d = load(); new = []; presets = {}
    for wk, (kind, count, kinds) in WAVE_SETS.items():
        for c in comps:
            ids = []
            for food in ('sincomida', 'comida'):
                for resp in ('base', 'antes'):
                    sid = f't{rnd}-waves-{wk}-{c}-{food}-{resp}'
                    sc = scenario(sid, kind, c, resp, count)
                    sc['tags'] += [f'waves:{WAVES}', 'wavePause:20', 'waves'] + ([f'waveKinds:{kinds}'] if kinds else []) \
                        + (['food:pack'] if food == 'comida' else [])
                    sc['minutes'] = 12
                    sc['note'] = f'Oleadas: hasta {WAVES} olas de {count} ({kinds or kind}), {c}, {food}, {resp}'
                    new.append(sc); ids.append(sid)
            presets[f't{rnd}-waves-{wk}-{c}'] = {'scenarios': ids, 'seeds': seeds, 'minutes': 12}
    replace_round(d, f't{rnd}-waves-', new, presets); save(d)
    print('presets:', ','.join(presets))


# Escolta (2026-11-06, docs/ESCOLTA_Y_TESTUDO.md): un aldeano sin IA cruza la arena de oeste a este por delante de los
# rojos, que van a por él. 'defender' = DEFEND con la escolta v1 (order/Escort); 'antes' = el DEFEND de la fase 3H
# (off:escort); 'atacar' = sin escolta, la orden de ataque de siempre mientras el aldeano camina; 'sinpantalla' = la
# escolta v1 sin la pantalla de escudos contra tiradores (off:screen).
ESCORT_SETS = {'zombis': ('zombie', 16, ''), 'esqueletos': ('skeleton', 10, ''),
               'mezcla': ('zombie', 12, 'zombie,skeleton,creeper')}
ESCORT_RESP = {'defender': ['escort:defend'], 'sinpantalla': ['escort:defend', 'off:screen'],
               'antes': ['escort:defend', 'off:escort'], 'atacar': ['escort:attack']}


def gen_escort(rnd, seeds=30, comps=('linea', 'escudo', 'arco')):
    d = load(); new = []; presets = {}
    for wk, (kind, count, kinds) in ESCORT_SETS.items():
        ids = []
        for c in comps:
            for resp, tags in ESCORT_RESP.items():
                sid = f't{rnd}-escolta-{wk}-{c}-{resp}'
                sc = scenario(sid, kind, c, 'base', count)
                sc['tags'] += tags + ['waves:3', 'wavePause:3', 'food:pack'] + ([f'waveKinds:{kinds}'] if kinds else [])
                sc['minutes'] = 4
                sc['note'] = f'Escolta: aldeano que cruza la arena, 3 olas de {count} ({kinds or kind}), {c}, {resp}'
                new.append(sc); ids.append(sid)
        presets[f't{rnd}-escolta-{wk}'] = {'scenarios': ids, 'seeds': seeds, 'minutes': 4}
    replace_round(d, f't{rnd}-escolta-', new, presets); save(d)
    print('presets:', ','.join(presets))


def escort_table(paths):
    """Per escort scenario: how often the villager arrived, its health at the end, who hurt it, guards standing."""
    by = collections.defaultdict(list)
    for r in read_runs(paths):
        if '-escolta-' in r['scenario']:
            by[r['scenario']].append(r)
    out = ['| Amenaza | Comp. | Respuesta | n | Llega vivo | Vida del aldeano al final | Daño de rojos | Daño propio | Guardias en pie | Olas superadas |',
           '|---|---|---|---|---|---|---|---|---|---|']
    for sid, rs in sorted(by.items()):
        p = sid.split('-')
        arrived = [r['extra'].get('vipArrived', 0) for r in rs]
        out.append(f"| {p[2]} | {p[3]} | {p[4]} | {len(rs)} | {_mean(arrived):.0%} ± {_se(arrived):.0%} | "
                   f"{_mean([r['extra'].get('vipHpEnd', 0) for r in rs]):.1f} | {_mean([r['extra'].get('vipDmgRed', 0) for r in rs]):.1f} | "
                   f"{_mean([r['extra'].get('vipDmgBlue', 0) for r in rs]):.1f} | {_mean([r['blue']['survivors'] for r in rs]):.1f} | "
                   f"{_mean([r['extra'].get('wavesCleared', 0) for r in rs]):.1f} |")
    return '\n'.join(out)


# Testudo (2026-11-06, docs/ESCOLTA_Y_TESTUDO.md §4): avanzar en formación contra tiradores. 'base' = el ataque libre
# de siempre; 'marchalinea' = muster en línea y avanzar (AdvanceOrder) hasta el contacto; 'testudo' = lo mismo en
# tortuga (escudos arriba, sin romper filas); 'romano' = tortuga cuyos escudos paran proyectiles por todos lados
# (testudo:anyShield: lo que valdría el escudo romano).
TESTUDO_SETS = {'esqueletos': ('skeleton', 40, '', 1), 'mezcla': ('zombie', 20, 'zombie,skeleton,creeper', 3)}
TESTUDO_RESP = {'base': [], 'marchalinea': ['march:LINE'], 'testudo': ['march:TESTUDO'],
                'romano': ['march:TESTUDO', 'testudo:anyShield']}
# Ronda 19: el escudo romano de verdad (rol 'scutum') y la carga: avanzar en tortuga y romper filas a 6 bloques.
TESTUDO_RESP19 = {'base': [], 'marchalinea': ['march:LINE', 'marchCharge:6'], 'testudo': ['march:TESTUDO'],
                  'testudocarga': ['march:TESTUDO', 'marchCharge:6']}


def gen_testudo(rnd, seeds=30, comps=('escudo', 'linea'), responses=None):
    responses = responses or (TESTUDO_RESP19 if int(rnd) >= 19 else TESTUDO_RESP)
    if int(rnd) >= 19: comps = ('escudo', 'scutum', 'linea', 'lineascutum')
    d = load(); new = []; presets = {}
    for wk, (kind, count, kinds, waves) in TESTUDO_SETS.items():
        ids = []
        for c in comps:
            for resp, tags in responses.items():
                sid = f't{rnd}-testudo-{wk}-{c}-{resp}'
                sc = scenario(sid, kind, c, 'base', count)
                sc['distance'] = 44
                sc['tags'] += tags + (['waves:%d' % waves, 'wavePause:10', 'food:pack'] if waves > 1 else []) \
                    + ([f'waveKinds:{kinds}'] if kinds else [])
                sc['minutes'] = 6 if waves > 1 else 3
                sc['note'] = f'Testudo: {count} {kinds or kind} x{waves}, {c}, {resp}'
                new.append(sc); ids.append(sid)
        presets[f't{rnd}-testudo-{wk}'] = {'scenarios': ids, 'seeds': seeds, 'minutes': 6}
    replace_round(d, f't{rnd}-testudo-', new, presets); save(d)
    print('presets:', ','.join(presets))


def testudo_table(paths):
    by = collections.defaultdict(list)
    for r in read_runs(paths):
        if '-testudo-' in r['scenario'] and not r['control']:
            by[r['scenario']].append(r)
    out = ['| Amenaza | Comp. | Respuesta | n | Victoria azul | Supervivientes | Vida que queda | Olas | Paradas por el techo/flancos | Golpes con el escudo arriba que entraron |',
           '|---|---|---|---|---|---|---|---|---|---|']
    for sid, rs in sorted(by.items()):
        p = sid.split('-')
        w = [win(r) for r in rs]
        sv = [r['blue']['survivors'] / max(1, r['blue']['start']) for r in rs]
        out.append(f"| {p[2]} | {p[3]} | {p[4]} | {len(rs)} | {_mean(w):.0%} ± {_se(w):.0%} | {_mean(sv):.0%} ± {_se(sv):.0%} | "
                   f"{_mean([r['blue']['healthLeft'] for r in rs]):.0f} | {_mean([r['extra'].get('wavesCleared', 0) for r in rs]):.1f} | "
                   f"{_mean([r['extra'].get('act:testudoCover', 0) for r in rs]):.0f} | {_mean([r['extra'].get('act:hitThroughRaisedShield', 0) for r in rs]):.0f} |")
    return '\n'.join(out)


# Ronda 20 (2026-11-06): escudo arriba contra lo que escupe o lanza sin ser "tirador" (#rallyguard:shield_threats,
# veteranía >= 1): blazes. 'base' = la regla nueva; 'sinamenazas' = sin ella (off:threats, como antes).
def gen_threats(rnd, seeds=30, comps=('escudo', 'scutum', 'linea'), counts=(6, 10)):
    d = load(); new = []; presets = {}
    ids = []
    for n in counts:
        for c in comps:
            for resp, tags in {'base': [], 'sinamenazas': ['off:threats']}.items():
                sid = f't{rnd}-amenaza-blaze-{c}-{resp}-r{n}'
                sc = scenario(sid, 'blaze', c, 'base', n)
                sc['tags'] += tags
                sc['minutes'] = 3
                sc['note'] = f'Escudo contra blazes: {n} blazes, {c}, {resp}'
                new.append(sc); ids.append(sid)
    presets[f't{rnd}-amenaza-blaze'] = {'scenarios': ids, 'seeds': seeds, 'minutes': 3}
    replace_round(d, f't{rnd}-amenaza-', new, presets); save(d)
    print('presets:', ','.join(presets))


def waves_table(paths):
    """Waves cleared (mean, and how many runs cleared them all), guards standing, health recovered in the pauses."""
    by = collections.defaultdict(list)
    for r in read_runs(paths):
        if '-waves-' in r['scenario']:
            by[r['scenario']].append(r)
    out = ['| Olas | Comp. | Comida | Manual | n | Olas superadas (media) | Todas | Guardias en pie al final | Vida recuperada por pausa |',
           '|---|---|---|---|---|---|---|---|---|']
    for sid, rs in sorted(by.items()):
        p = sid.split('-')
        cleared = [r['extra'].get('wavesCleared', 0) for r in rs]
        total = max(r['extra'].get('waves', 1) for r in rs)
        rec = []
        for r in rs:
            e = r['extra']
            for i in range(1, int(e.get('wavesCleared', 0)) + 1):
                if f'wave{i}HpNext' in e:
                    rec.append(e[f'wave{i}HpNext'] - e[f'wave{i}HpClear'])
        out.append(f"| {p[2]} | {p[3]} | {p[4]} | {p[5]} | {len(rs)} | {_mean(cleared):.1f} ± {_se(cleared):.1f} | "
                   f"{sum(1 for c in cleared if c >= total)}/{len(rs)} | {_mean([r['blue']['survivors'] for r in rs]):.1f} | "
                   f"{_mean(rec) if rec else 0:+.1f} |")
    return '\n'.join(out)


def _vs_base(paths):
    """(threat, comp, resp) -> {win, surv, kill_s, n, dwin, dwin_se, dsurv, dsurv_se} against that row's base."""
    by = collections.defaultdict(list)
    for r in read_runs(paths):
        p = r['scenario'].split('-')
        if len(p) >= 5 and p[0][0] == 't' and p[1] in ('rank', 'final', 'confirm'):
            by[(p[2], p[3], p[4])].append(r)
    out = {}
    for (t, c, resp), rs in by.items():
        base = by.get((t, c, 'base'))
        if not base:
            continue
        w = [win(r) for r in rs]; wb = [win(r) for r in base]
        sv = [r['blue']['survivors'] / max(1, r['blue']['start']) for r in rs]
        sb = [r['blue']['survivors'] / max(1, r['blue']['start']) for r in base]
        ks = [r['seconds'] for r in rs if r['winner'] == 'blue']
        out[(t, c, resp)] = {'n': len(rs), 'win': _mean(w), 'surv': _mean(sv), 'kill_s': _mean(ks),
                             'dwin': _mean(w) - _mean(wb), 'dwin_se': math.sqrt((_se(w) or 0) ** 2 + (_se(wb) or 0) ** 2),
                             'dsurv': _mean(sv) - _mean(sb), 'dsurv_se': math.sqrt((_se(sv) or 0) ** 2 + (_se(sb) or 0) ** 2)}
    return out


def confirm(final_paths, confirm_paths):
    """A response is CONFIRMADA when it beats its base by > 2 SE (in victory or in survivors) in BOTH batches."""
    a, b = _vs_base(final_paths), _vs_base(confirm_paths)
    out = ['| Amenaza | Comp. | Respuesta | Final: victoria / superv. (Δ vs base) | Confirmación: victoria / superv. (Δ vs base) | Veredicto |',
           '|---|---|---|---|---|---|']
    rows = {}
    for key in sorted(set(a) & set(b)):
        if key[2] == 'base':
            continue
        def sig(x):
            up = x['dwin'] > 2 * x['dwin_se'] or x['dsurv'] > 2 * x['dsurv_se']
            down = -x['dwin'] > 2 * x['dwin_se'] or -x['dsurv'] > 2 * x['dsurv_se']
            return 1 if up and not down else -1 if down and not up else 0
        sa, sb = sig(a[key]), sig(b[key])
        verdict = 'CONFIRMADA' if sa == sb == 1 else 'PEOR (confirmado)' if sa == sb == -1 else 'no confirmada'
        f = lambda x: f"{x['win']:.0%} / {x['surv']:.0%} ({x['dwin']:+.0%} / {x['dsurv']:+.0%})"
        out.append(f'| {key[0]} | {key[1]} | {key[2]} | {f(a[key])} | {f(b[key])} | {verdict} |')
        rows['|'.join(key)] = {'verdict': verdict, 'final': a[key], 'confirm': b[key]}
    return '\n'.join(out), rows


def load():
    return json.loads(open(SCN, encoding='utf-8').read())


def save(d):
    open(SCN, 'w', encoding='utf-8', newline='\n').write(json.dumps(d, ensure_ascii=False, indent=1) + '\n')


def replace_round(d, prefix, new, presets):
    d['scenarios'] = [s for s in d['scenarios'] if not s['id'].startswith(prefix)] + new
    for k in [k for k in d['presets'] if k.startswith(prefix)]:
        del d['presets'][k]
    d['presets'].update(presets)


def gen_cal(rnd, seeds=4):
    d = load(); new = []; presets = {}
    for t, counts in CAL_COUNTS.get(int(rnd), THREATS).items():
        ids = []
        for c in COMPS:
            for n in counts:
                sid = f't{rnd}-cal-{t}-{c}-base-r{n}'
                new.append(scenario(sid, t, c, 'base', n)); ids.append(sid)
        presets[f't{rnd}-cal-{t}'] = {'scenarios': ids, 'seeds': seeds, 'minutes': 2}
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
                if resp in ONLY and c not in ONLY[resp]:
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


def gen_food(rnd, cal_path, seeds=30):
    """Comer y rotar: base / comida en la mochila / comida en la mano, n = seeds, al número calibrado (zombi y esqueleto)."""
    cal = json.load(open(cal_path))
    d = load(); new = []; presets = {}
    for t in ('zombie', 'skeleton'):
        ids = []
        for c in COMPS:
            n = cal.get(f'{t}|{c}', {}).get('reds')
            if not n:
                continue
            for resp in ('base', 'comidamochila', 'comidamano'):
                if resp in ONLY and c not in ONLY[resp]:
                    continue
                sid = f't{rnd}-final-{t}-{c}-{resp}'
                new.append(scenario(sid, t, c, resp, n)); ids.append(sid)
        half = (len(ids) + 1) // 2
        presets[f't{rnd}-food-{t}-a'] = {'scenarios': ids[:half], 'seeds': seeds, 'minutes': 2}
        presets[f't{rnd}-food-{t}-b'] = {'scenarios': ids[half:], 'seeds': seeds, 'minutes': 2}
    d['scenarios'] = [x for x in d['scenarios'] if not x['id'].startswith(f't{rnd}-final-')] + new
    for k in [k for k in d['presets'] if k.startswith(f't{rnd}-food-')]:
        del d['presets'][k]
    d['presets'].update(presets); save(d)
    print('presets:', ','.join(presets))


def gen_check(rnd, cal_path, seeds=30):
    """Field manual in play: new defaults ('base') vs the old rules ('antes'), every threat and composition."""
    cal = json.load(open(cal_path))
    d = load(); new = []; presets = {}
    for t in ('zombie', 'skeleton', 'creeper'):
        ids = []
        for c in COMPS:
            n = 24 if t == 'creeper' else cal.get(f'{t}|{c}', {}).get('reds')
            if not n:
                continue
            for resp in ('base', 'antes'):
                sid = f't{rnd}-final-{t}-{c}-{resp}'
                new.append(scenario(sid, t, c, resp, n)); ids.append(sid)
        presets[f't{rnd}-check-{t}'] = {'scenarios': ids, 'seeds': seeds, 'minutes': 2}
    d['scenarios'] = [x for x in d['scenarios'] if not x['id'].startswith(f't{rnd}-final-')] + new
    for k in [k for k in d['presets'] if k.startswith(f't{rnd}-check-')]:
        del d['presets'][k]
    d['presets'].update(presets); save(d)
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


# Ronda 22 (2026-11-08, etapa 1 del PLAN_MAESTRO): bestiario del modpack. Exploratorio: 8 guardias en línea
# (4 escudos + 4 arcos, rango 1, pan en la mochila: lo mejor medido hasta hoy) contra cada mob a varios números, para
# ubicar dónde empata. Necesita los mods en la CI: dispatch con mods=bestiario. Ids de mob leídas en los jar del perfil.
BESTIARY = {
    'melee': {  # cuerpo a cuerpo, pequeños y en grupo
        'iceandfire:dread_thrall': [4, 8, 16], 'alexscaves:vallumraptor': [4, 8, 16], 'cataclysm:deepling': [4, 8, 16],
        'iceandfire:troll': [1, 2, 4], 'cataclysm:deepling_brute': [1, 2, 4], 'alexscaves:forsaken': [1, 2],
    },
    'ranged': {  # tiradores, pistoleros y magos
        'scguns:redcoat': [2, 4, 8], 'scguns:dissident': [2, 4, 8], 'iceandfire:dread_lich': [1, 2, 4],
        'alexscaves:deep_one_mage': [1, 2, 4], 'cataclysm:ignited_revenant': [1, 2, 4],
    },
    'flyers': {  # voladores
        'minecraft:phantom': [2, 4, 8], 'minecraft:vex': [2, 4, 8], 'minecraft:ghast': [1, 2, 4],
        'iceandfire:stymphalian_bird': [2, 4, 8], 'cataclysm:endermaptera': [2, 4, 8],
    },
    'bosses': {  # jefes y grandes
        'cataclysm:netherite_monstrosity': [1], 'cataclysm:ignis': [1], 'cataclysm:the_harbinger': [1],
        'cataclysm:ender_guardian': [1], 'iceandfire:cyclops': [1, 2], 'iceandfire:hydra': [1],
        'minecraft:ravager': [1, 2, 4], 'minecraft:warden': [1],
    },
}


def gen_bestiary(rnd, seeds=4):
    d = load(); new = []; presets = {}
    smoke = []
    for fam, mobs in BESTIARY.items():
        ids = []
        for mob, counts in mobs.items():
            short = mob.split(':')[1]
            for n in counts:
                sid = f't{rnd}-best-{fam}-{short}-r{n}'
                sc = scenario(sid, mob, 'linea', 'base', n)
                sc['tags'] += ['food:pack', 'family:' + fam]
                sc['minutes'] = 3
                sc['note'] = f'Bestiario: 8 en línea con pan contra {n} {mob}'
                new.append(sc); ids.append(sid)
            smoke.append(ids[-len(counts)])
        presets[f't{rnd}-best-{fam}'] = {'scenarios': ids, 'seeds': seeds, 'minutes': 3}
    presets[f't{rnd}-smoke'] = {'scenarios': smoke, 'seeds': 1, 'minutes': 3}
    replace_round(d, f't{rnd}-', new, presets); save(d)
    print('presets:', ','.join(presets))


# Ronda 23 (2026-11-08): sonda de techo, laboratorio A. Rojos en el punto de empate medido en la ronda 7 (sin comida),
# para que una mejora se note. Un preset por respuesta: corren en paralelo en la CI.
CEILING = {  # amenaza -> {composición: número de rojos}
    'zombie': {'arco': 40, 'linea': 38},
    'skeleton': {'arco': 68, 'linea': 80},
    'phantom': {'arco': 10, 'linea': 10},
}
CEILING_RESP = ['base', 'orapunta', 'oracadencia', 'orablanco', 'orakite', 'oratodo']


def gen_ceiling(rnd, seeds=30):
    d = load(); new = []; presets = {}
    for t, comps in CEILING.items():
        for resp in CEILING_RESP:
            ids = []
            for c, n in comps.items():
                sid = f't{rnd}-techo-{t}-{c}-{resp}-r{n}'
                sc = scenario(sid, t, c, resp, n)
                sc['tags'].append('lab:A')
                new.append(sc); ids.append(sid)
            presets[f't{rnd}-techo-{t}-{resp}'] = {'scenarios': ids, 'seeds': seeds if t != 'phantom' else 15, 'minutes': 2}
    replace_round(d, f't{rnd}-', new, presets); save(d)
    print('presets:', ','.join(presets))


# Ronda 24 (2026-11-08): la ronda 23 cayó lejos del empate (Rally de hoy ya ganaba 70–97 % donde la ronda 7 empataba;
# los fantasmas, 100 %): el techo no se veía. Escalera de dos puntos más duros por composición; sin 'orablanco'
# (en la 23 empeoró: elegir al más débil deja libres a los que están encima).
CEILING24 = {
    'zombie': {'arco': [60, 85], 'linea': [55, 75]},
    'skeleton': {'arco': [68, 95], 'linea': [110, 150]},
    'phantom': {'arco': [20, 35], 'linea': [20, 35]},
}
CEILING24_RESP = ['base', 'oracadencia', 'orakite', 'orapunta', 'oratodo']


def gen_ceiling24(rnd, seeds=20):
    d = load(); new = []; presets = {}
    for t, comps in CEILING24.items():
        for resp in CEILING24_RESP:
            ids = []
            for c, counts in comps.items():
                for n in counts:
                    sid = f't{rnd}-techo-{t}-{c}-{resp}-r{n}'
                    sc = scenario(sid, t, c, resp, n)
                    sc['tags'].append('lab:A')
                    sc['minutes'] = 3
                    new.append(sc); ids.append(sid)
            presets[f't{rnd}-techo-{t}-{resp}'] = {'scenarios': ids, 'seeds': seeds, 'minutes': 3}
    replace_round(d, f't{rnd}-', new, presets); save(d)
    print('presets:', ','.join(presets))


# Ronda 25 (2026-11-08): el bestiario vanilla que faltaba (la 22 trae los del modpack más fantasma, vex, ghast,
# devastador y warden). Base contra 'antes' (el manual apagado) para ver qué familias dependen del manual.
# Fuera: piglins y hoglins (en el Overworld se vuelven zombificados a los 15 s), guardianes (solo en agua), Wither y
# dragón del End (rompen la arena).
BESTIARY25 = {
    'hordas': {'minecraft:spider': [12, 24], 'minecraft:cave_spider': [12, 24], 'minecraft:husk': [24, 40],
               'minecraft:silverfish': [16, 32], 'minecraft:slime': [8, 16], 'minecraft:magma_cube': [8, 16]},
    'tiradores': {'minecraft:stray': [16, 32], 'minecraft:bogged': [16, 32], 'minecraft:pillager': [8, 16],
                  'minecraft:drowned': [12, 24], 'minecraft:breeze': [2, 4]},
    'raros': {'minecraft:witch': [4, 8], 'minecraft:vindicator': [6, 12], 'minecraft:evoker': [2, 4],
              'minecraft:enderman': [3, 6], 'minecraft:wither_skeleton': [6, 12]},
}


def gen_bestiary25(rnd, seeds=10):
    d = load(); new = []; presets = {}
    for fam, mobs in BESTIARY25.items():
        for resp in ('base', 'antes'):
            ids = []
            for mob, counts in mobs.items():
                short = mob.split(':')[1]
                for n in counts:
                    sid = f't{rnd}-vanilla-{fam}-{short}-{resp}-r{n}'
                    sc = scenario(sid, mob, 'linea', resp, n)
                    sc['tags'] += ['food:pack', 'family:' + fam]
                    sc['minutes'] = 3
                    sc['note'] = f'Bestiario vanilla: 8 en línea con pan contra {n} {mob} ({resp})'
                    new.append(sc); ids.append(sid)
            presets[f't{rnd}-vanilla-{fam}-{resp}'] = {'scenarios': ids, 'seeds': seeds, 'minutes': 3}
    replace_round(d, f't{rnd}-', new, presets); save(d)
    print('presets:', ','.join(presets))


if __name__ == '__main__':
    cmd = sys.argv[1]
    if cmd == 'gen-bestiary25':
        gen_bestiary25(sys.argv[2])
    elif cmd == 'gen-ceiling24':
        gen_ceiling24(sys.argv[2])
    elif cmd == 'gen-cal':
        gen_cal(sys.argv[2], int(sys.argv[3]) if len(sys.argv) > 3 else 4)
    elif cmd == 'calibrate':
        print(json.dumps(calibrate(sys.argv[2:]), indent=1))
    elif cmd == 'gen-rank':
        gen_rank(sys.argv[2], sys.argv[3])
    elif cmd == 'gen-check':
        gen_check(sys.argv[2], sys.argv[3])
    elif cmd == 'gen-food':
        gen_food(sys.argv[2], sys.argv[3])
    elif cmd == 'gen-final':
        gen_final(sys.argv[2], sys.argv[3], sys.argv[4:])
    elif cmd == 'report':
        md, manual = report(sys.argv[2], sys.argv[3:])
        print(md)
        print('\n<!-- manual -->\n' + json.dumps(manual, ensure_ascii=False))
    elif cmd == 'gen-duel':
        gen_duel(sys.argv[2], only=sys.argv[3].split(',') if len(sys.argv) > 3 else None)
    elif cmd == 'gen-volea':
        gen_volea(sys.argv[2], sys.argv[3])
    elif cmd == 'gen-ceiling':
        gen_ceiling(sys.argv[2])
    elif cmd == 'gen-bestiary':
        gen_bestiary(sys.argv[2])
    elif cmd == 'gen-creeper':
        gen_creeper(sys.argv[2])
    elif cmd == 'gen-waves':
        gen_waves(sys.argv[2])
    elif cmd == 'gen-testudo':
        gen_testudo(sys.argv[2])
    elif cmd == 'testudo':
        print(testudo_table(sys.argv[2:]))
    elif cmd == 'gen-threats':
        gen_threats(sys.argv[2])
    elif cmd == 'gen-escort':
        gen_escort(sys.argv[2])
    elif cmd == 'escort':
        print(escort_table(sys.argv[2:]))
    elif cmd == 'waves':
        print(waves_table(sys.argv[2:]))
    elif cmd == 'confirm':  # confirm <final.jsonl,...> <confirm.jsonl,...>
        md, rows = confirm(sys.argv[2].split(','), sys.argv[3].split(','))
        print(md)
        print('\n<!-- rows -->\n' + json.dumps(rows, ensure_ascii=False))
    elif cmd == 'duel':
        print(duel(sys.argv[2:]))
    elif cmd == 'groups':
        print(groups(sys.argv[2:]))
    elif cmd == 'analyze':
        for row in analyze(sys.argv[2:]):
            print('{threat:9} {comp:7} {resp:11} n={n:3} win={win:.2f}±{win_se:.2f} surv={surv:.2f}±{surv_se:.2f} kill_s={kill_s:.0f}'.format(**row))

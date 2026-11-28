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
    'orakite': (['oracle:kite'], {}), 'oratodo': (['oracle:all'], {}),
    # Ronda 27 (2026-11-08): apartarse del cuerpo a cuerpo como regla real del manual (la base ya la trae)
    'sinkite': (['off:kite'], {}), 'kitecadencia': (['oracle:cadence'], {}),
    'sinender': (['off:enderman'], {}),  # ronda 27: sin pelear contra endermen enojados (como hace Guard Villagers)  # el manual de hoy sin apartarse de los creepers (para medir solo la esquiva)
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


# Ronda 26 (2026-11-08, David: "quiero que probemos con todo tipo de dragones"): dragones de Ice and Fire por edad
# (AgeTicks = días × 24000: joven 30 días, adulto 60, anciano 90), depredadores de Alex's Mobs y gigantes de Alex's
# Caves e Ice and Fire. Necesita mods=bestiario en el dispatch. Línea (4 escudos + 4 arcos) y 8 arqueros.
DRAGON_AGES = {'joven': 30, 'adulto': 60, 'anciano': 90}
BESTIARY26 = {
    'depredadores': {'alexsmobs:crocodile': [2, 4], 'alexsmobs:komodo_dragon': [2, 4], 'alexsmobs:grizzly_bear': [1, 2],
                     'alexsmobs:tiger': [1, 2], 'alexsmobs:rhinoceros': [1, 2], 'alexsmobs:anaconda': [1, 2],
                     'alexsmobs:tusklin': [1, 2], 'alexsmobs:soul_vulture': [4, 8]},
    'gigantes': {'alexscaves:tremorzilla': [1], 'alexscaves:tremorsaurus': [1, 2], 'alexscaves:luxtructosaurus': [1],
                 'alexsmobs:void_worm': [1], 'alexsmobs:warped_mosco': [1], 'iceandfire:dread_queen': [1],
                 'iceandfire:gorgon': [1], 'iceandfire:cockatrice': [2, 4], 'iceandfire:dread_knight': [2, 4]},
}


def gen_bestiary26(rnd, seeds=6):
    d = load(); new = []; presets = {}
    ids = []
    for dragon in ('fire_dragon', 'ice_dragon', 'lightning_dragon'):
        for age, days in DRAGON_AGES.items():
            for c in ('linea', 'arco'):
                sid = f't{rnd}-dragon-{dragon}-{age}-{c}'
                sc = scenario(sid, 'iceandfire:' + dragon, c, 'base', 1)
                sc['tags'] += ['food:pack', 'family:dragones', 'redNbt:{AgeTicks:%d}' % (days * 24000)]
                sc['minutes'] = 4
                sc['note'] = f'Dragón {dragon} {age} ({days} días) contra 8 ({c})'
                new.append(sc); ids.append(sid)
    presets[f't{rnd}-dragones'] = {'scenarios': ids, 'seeds': seeds, 'minutes': 4}
    for fam, mobs in BESTIARY26.items():
        ids = []
        for mob, counts in mobs.items():
            short = mob.split(':')[1]
            for n in counts:
                for c in ('linea', 'arco'):
                    sid = f't{rnd}-best-{fam}-{short}-{c}-r{n}'
                    sc = scenario(sid, mob, c, 'base', n)
                    sc['tags'] += ['food:pack', 'family:' + fam]
                    sc['minutes'] = 3
                    sc['note'] = f'Bestiario del modpack: 8 ({c}) contra {n} {mob}'
                    new.append(sc); ids.append(sid)
        presets[f't{rnd}-best-{fam}'] = {'scenarios': ids, 'seeds': seeds, 'minutes': 3}
    replace_round(d, f't{rnd}-', new, presets); save(d)
    print('presets:', ','.join(presets))


# Ronda 27 (2026-11-08): "apartarse del cuerpo a cuerpo" ya es regla del manual (combat/FieldManual#kite, sin trampa).
# Base (con la regla) contra 'sinkite' (off:kite) y 'kitecadencia' (la regla + el arco a su cadencia física, todavía
# trampa de simulación) en los puntos de empate de la ronda 24, más fantasmas y creepers para ver que no estorba.
KITE27 = {'zombie': {'arco': [60, 85], 'linea': [55, 75]}, 'skeleton': {'arco': [68], 'linea': [150]},
          'phantom': {'arco': [35], 'linea': [35]}, 'creeper': {'arco': [16], 'linea': [16]},
          'vindicator': {'arco': [8], 'linea': [12]}}
ENDER27 = {'linea': [3, 6], 'escudo': [3], 'arco': [3]}


def gen_kite27(rnd, seeds=20):
    d = load(); new = []; presets = {}
    for t, comps in KITE27.items():
        for resp in ('base', 'sinkite', 'kitecadencia'):
            ids = []
            for c, counts in comps.items():
                for n in counts:
                    sid = f't{rnd}-kite-{t}-{c}-{resp}-r{n}'
                    sc = scenario(sid, t, c, resp, n)
                    sc['minutes'] = 3
                    new.append(sc); ids.append(sid)
            presets[f't{rnd}-kite-{t}-{resp}'] = {'scenarios': ids, 'seeds': seeds, 'minutes': 3}
    for resp in ('base', 'sinender'):
        ids = []
        for c, counts in ENDER27.items():
            for n in counts:
                sid = f't{rnd}-ender-{c}-{resp}-r{n}'
                sc = scenario(sid, 'enderman', c, resp, n)
                sc['minutes'] = 3
                new.append(sc); ids.append(sid)
        presets[f't{rnd}-ender-{resp}'] = {'scenarios': ids, 'seeds': seeds, 'minutes': 3}
    replace_round(d, f't{rnd}-', new, presets); save(d)
    print('presets:', ','.join(presets))


# Ronda 28 (2026-11-08): cuántos guardias hacen falta para cada jefe. En la 22/26, 8 en línea no le hacen nada a los
# jefes de Cataclysm, al warden, al cíclope, a Tremorzilla ni a un dragón anciano. Escalera 16 / 32 / 64 guardias
# (mitad escudos, mitad arcos, rango 1, con pan). Necesita mods=bestiario.
BOSSES28 = {
    'dragonfuego': ('iceandfire:fire_dragon', 'redNbt:{AgeTicks:%d}' % (90 * 24000)),
    'dragonhielo': ('iceandfire:ice_dragon', 'redNbt:{AgeTicks:%d}' % (90 * 24000)),
    'dragonrayo': ('iceandfire:lightning_dragon', 'redNbt:{AgeTicks:%d}' % (90 * 24000)),
    'ignis': ('cataclysm:ignis', None), 'harbinger': ('cataclysm:the_harbinger', None),
    'monstruosidad': ('cataclysm:netherite_monstrosity', None), 'guardianender': ('cataclysm:ender_guardian', None),
    'warden': ('minecraft:warden', None), 'ciclope': ('iceandfire:cyclops', None),
    'tremorzilla': ('alexscaves:tremorzilla', None), 'luxtructosaurio': ('alexscaves:luxtructosaurus', None),
    'gusanovacio': ('alexsmobs:void_worm', None),
}


def gen_bosses28(rnd, seeds=6, sizes=(16, 32, 64)):
    d = load(); new = []; presets = {}
    for name, (mob, extra) in BOSSES28.items():
        ids = []
        for n in sizes:
            sid = f't{rnd}-jefe-{name}-g{n}'
            sc = scenario(sid, mob, 'linea', 'base', 1)
            sc['blue']['count'] = n
            sc['blue']['role'] = 'mix:shield=1,archer=1'
            sc['tags'] += ['food:pack', 'family:jefes'] + ([extra] if extra else [])
            sc['minutes'] = 5
            sc['distance'] = 36
            sc['note'] = f'Jefe {mob} contra {n} guardias (mitad escudos, mitad arcos)'
            new.append(sc); ids.append(sid)
        presets[f't{rnd}-jefe-{name}'] = {'scenarios': ids, 'seeds': seeds, 'minutes': 5}
    replace_round(d, f't{rnd}-', new, presets); save(d)
    print('presets:', ','.join(presets))


# Ronda 29 (2026-11-08, David: "pelear en algunas estructuras, para que no sean inútiles ahí"): las mismas peleas en
# campo abierto (control) y en cuatro estructuras de la arena (SimArena): aldea (casas con puerta, rojos dentro, en la
# calle y en los tejados), pasillos (bloque techado, corredores de 3), fuerte (patio amurallado con una puerta) y
# salón (techo a 5). Amenazas: zombis ×16, esqueletos ×12, vindicadores ×6 (el asalto a una aldea).
STRUCT29 = {'campo': 'flat', 'aldea': 'village', 'pasillos': 'corridors', 'fuerte': 'fort', 'salon': 'hall'}
STRUCT29_THREATS = {'zombie': 16, 'skeleton': 12, 'vindicator': 6}


def gen_struct29(rnd, seeds=10):
    d = load(); new = []; presets = {}
    for tname, terrain in STRUCT29.items():
        ids = []
        for threat, reds in STRUCT29_THREATS.items():
            for comp in ('linea', 'arco'):
                sid = f't{rnd}-estructura-{tname}-{threat}-{comp}'
                sc = scenario(sid, threat, comp, 'base', reds)
                sc['terrain'] = terrain
                sc['distance'] = 36
                sc['minutes'] = 4
                sc['tags'] += ['food:pack', 'family:estructuras', 'terrain:' + tname]
                sc['note'] = f'{threat} ×{reds} contra {comp} en {tname} ({terrain})'
                new.append(sc); ids.append(sid)
        presets[f't{rnd}-estructura-{tname}'] = {'scenarios': ids, 'seeds': seeds, 'minutes': 4}
    replace_round(d, f't{rnd}-', new, presets); save(d)
    print('presets:', ','.join(presets))


# Ronda 30 (2026-11-08, David): "contra un monstruo grande la solución no puede ser 1000 soldados". Mismos 16
# guardias de la ronda 28 contra los jefes que 64 no ganaban, cambiando solo la CALIDAD del equipo: hierro (como
# hoy), diamante, netherita y netherita encantada (Filo V, Poder V, Protección IV). Cadencia de 1 s ya por defecto.
GIANTS30 = ['dragonfuego', 'ignis', 'monstruosidad', 'harbinger', 'guardianender', 'tremorzilla', 'luxtructosaurio']
TIERS30 = {'hierro': [], 'diamante': ['tier:diamond'], 'netherita': ['tier:netherite'], 'netherita+': ['tier:netherite+']}


def gen_giants30(rnd, seeds=6):
    d = load(); new = []; presets = {}
    by = {s['id']: s for s in d['scenarios']}
    for boss in GIANTS30:
        base = by[f't28-jefe-{boss}-g16']
        ids = []
        for tier, tags in TIERS30.items():
            sc = json.loads(json.dumps(base))
            sc['id'] = f't{rnd}-gigante-{boss}-{tier.replace("+", "mas")}'
            sc['tags'] = [t for t in sc['tags'] if not t.startswith('resp:')] + ['resp:' + tier, 'lab:C'] + tags
            sc['note'] = base['note'] + f', equipo {tier}'
            new.append(sc); ids.append(sc['id'])
        presets[f't{rnd}-gigante-{boss}'] = {'scenarios': ids, 'seeds': seeds, 'minutes': 5}
    replace_round(d, f't{rnd}-', new, presets); save(d)
    print('presets:', ','.join(presets))


# Ronda 31 (2026-11-09): qué parte del equipo encantado gana a los jefes (ronda 30: 16 en netherita encantada ganan
# 100 % al dragón de fuego anciano, al Guardián del End, al Harbinger y a la Monstruosidad; 64 en hierro, 0 %).
# Hierro con un solo encantamiento cada vez, y netherita encantada de control. Dice qué debería dar la consagración.
GIANTS31 = ['monstruosidad', 'harbinger', 'guardianender', 'ignis', 'tremorzilla']
TIERS31 = {'hierro': [], 'poder': ['ench:power'], 'filo': ['ench:sharpness'], 'proteccion': ['ench:protection'],
           'netheritamas': ['tier:netherite+']}


def gen_giants31(rnd, seeds=8):
    d = load(); new = []; presets = {}
    by = {s['id']: s for s in d['scenarios']}
    for boss in GIANTS31:
        base = by[f't28-jefe-{boss}-g16']
        ids = []
        for tier, tags in TIERS31.items():
            sc = json.loads(json.dumps(base))
            sc['id'] = f't{rnd}-encanto-{boss}-{tier}'
            sc['tags'] = [t for t in sc['tags'] if not t.startswith('resp:')] + ['resp:' + tier, 'lab:C'] + tags
            sc['note'] = base['note'] + f', {tier}'
            new.append(sc); ids.append(sc['id'])
        presets[f't{rnd}-encanto-{boss}'] = {'scenarios': ids, 'seeds': seeds, 'minutes': 5}
    replace_round(d, f't{rnd}-', new, presets); save(d)
    print('presets:', ','.join(presets))


# Ronda 32 (2026-11-09): táctica contra los jefes que el equipo no basta (Ignis 38 %, Tremorzilla 0 %, Harbinger 75 %
# con 16 en netherita encantada). Mismo equipo; cambia composición, distancia y formación.
GIANTS32 = ['ignis', 'tremorzilla', 'harbinger']
TACTICS32 = {  # nombre -> (rol azul, distancia, cambios al azul, etiquetas)
    'linea': ('mix:shield=1,archer=1', 36, {}, []),
    'arcos': ('archer', 36, {}, []),
    'escudos': ('shield', 36, {}, []),
    'espadas': ('melee', 36, {}, []),
    'volea60': ('mix:shield=1,archer=1', 60, {'fireMode': 'VOLLEY'}, []),
    'tortuga': ('mix:scutum=1,archer=1', 36, {}, ['march:TESTUDO', 'marchCharge:6']),
}


def gen_giants32(rnd, seeds=8):
    d = load(); new = []; presets = {}
    by = {s['id']: s for s in d['scenarios']}
    for boss in GIANTS32:
        base = by[f't28-jefe-{boss}-g16']
        ids = []
        for name, (role, dist, blue_extra, tags) in TACTICS32.items():
            sc = json.loads(json.dumps(base))
            sc['id'] = f't{rnd}-tactica-{boss}-{name}'
            sc['distance'] = dist
            sc['blue']['role'] = role
            sc['blue'].update(blue_extra)
            sc['tags'] = [t for t in sc['tags'] if not t.startswith('resp:') and not t.startswith('comp:')] + \
                ['resp:' + name, 'lab:C', 'tier:netherite+'] + tags
            sc['note'] = base['note'].split(' contra ')[0] + f' contra 16 en netherita encantada, táctica {name}'
            new.append(sc); ids.append(sc['id'])
        presets[f't{rnd}-tactica-{boss}'] = {'scenarios': ids, 'seeds': seeds, 'minutes': 5}
    replace_round(d, f't{rnd}-', new, presets); save(d)
    print('presets:', ','.join(presets))


# Ronda 33 (2026-11-09, David): el muñeco de práctica. Un zombi quieto (sin IA, sin armadura, sin retroceso, 1024 de
# vida) para medir el daño máximo por segundo de un guardia según arma y equipo (extra.dps = daño real / segundos
# vivos). Luego se compara con el daño que hace a cada monstruo (rondas 28–32) y con cuánto duran vivos.
DUMMY_NBT = ('redNbt:{NoAI:1b,Silent:1b,PersistenceRequired:1b,Health:1024.0f,attributes:['
             '{id:"minecraft:generic.max_health",base:1024.0d},{id:"minecraft:generic.armor",base:0.0d},'
             '{id:"minecraft:generic.knockback_resistance",base:1.0d}]}')
DUMMY_COMPS = {1: ['mele', 'arco', 'escudo'], 8: ['mele', 'arco', 'escudo', 'linea', 'cab']}
DUMMY_TIERS = {'hierro': [], 'netheritamas': ['tier:netherite+']}


def gen_dummy(rnd, seeds=6):
    d = load(); new = []; presets = {}
    ids = []
    for n, comps in DUMMY_COMPS.items():
        for c in comps:
            for tier, tags in DUMMY_TIERS.items():
                sid = f't{rnd}-muneco-{c}-{tier}-g{n}'
                sc = scenario(sid, 'zombie', c, 'base', 1)
                sc['blue']['count'] = n
                sc['distance'] = 12
                sc['minutes'] = 2
                sc['tags'] += ['lab:A', 'dummy', DUMMY_NBT] + tags
                sc['note'] = f'Muñeco: {n} guardias {c} en {tier} contra un zombi quieto de 1024 de vida'
                new.append(sc); ids.append(sid)
    presets[f't{rnd}-muneco'] = {'scenarios': ids, 'seeds': seeds, 'minutes': 2}
    replace_round(d, f't{rnd}-', new, presets); save(d)
    print('presets:', ','.join(presets))


# Ronda 34 (2026-11-09): la moral. En las rondas 31–33 el 70–100 % de los guardias se rompe y huye contra cualquier
# jefe (primer roto a los ~20 s); hasta el muñeco de 1024 de vida los asusta ("monstruo"). Se repiten el muñeco y la
# mejor táctica medida contra cada jefe con la moral apagada (off:morale): cuánto les cuesta el miedo.
MORALE34 = ['t32-tactica-harbinger-linea', 't32-tactica-ignis-arcos', 't32-tactica-tremorzilla-escudos',
            't31-encanto-guardianender-netheritamas', 't31-encanto-monstruosidad-netheritamas']


def gen_morale34(rnd, seeds=8):
    d = load(); new = []; presets = {}
    by = {s['id']: s for s in d['scenarios']}
    dummy = []
    for sid0, sc0 in by.items():
        if sid0.startswith('t33-muneco-'):
            sc = json.loads(json.dumps(sc0)); sc['id'] = sid0.replace('t33-', f't{rnd}-') + '-sinmoral'
            sc['tags'].append('off:morale'); new.append(sc); dummy.append(sc['id'])
    presets[f't{rnd}-muneco-sinmoral'] = {'scenarios': dummy, 'seeds': 6, 'minutes': 2}
    for sid0 in MORALE34:
        boss = sid0.split('-')[2]
        ids = []
        for moral, tags in (('conmoral', []), ('sinmoral', ['off:morale'])):
            sc = json.loads(json.dumps(by[sid0])); sc['id'] = f't{rnd}-moral-{boss}-{moral}'
            sc['tags'] += tags; new.append(sc); ids.append(sc['id'])
        presets[f't{rnd}-moral-{boss}'] = {'scenarios': ids, 'seeds': seeds, 'minutes': 5}
    replace_round(d, f't{rnd}-', new, presets); save(d)
    print('presets:', ','.join(presets))


# Ronda 35 (2026-11-09, David: "números lógicos": cuántos guardias por amenaza, que bajen con la calidad). Sin moral
# (David: la funcionalidad primero; en el juego los veteranos ya no temen). Línea mitad escudos, mitad arcos; hierro
# contra netherita encantada; varios tamaños de grupo por amenaza.
NUMBERS35 = {  # nombre -> (tipo de mob, NBT, tamaños)
    'dragonjoven': ('iceandfire:fire_dragon', '{AgeTicks:720000}', [2, 4, 8, 16]),
    'dragonadulto': ('iceandfire:fire_dragon', '{AgeTicks:1440000}', [4, 8, 16, 32]),
    'dragonanciano': ('iceandfire:fire_dragon', '{AgeTicks:2160000}', [8, 16, 32]),
    'ignis': ('cataclysm:ignis', '', [8, 16, 32]),
    'harbinger': ('cataclysm:the_harbinger', '', [4, 8, 16]),
    'monstruosidad': ('cataclysm:netherite_monstrosity', '', [4, 8, 16]),
    'guardianender': ('cataclysm:ender_guardian', '', [4, 8, 16]),
    'tremorzilla': ('alexscaves:tremorzilla', '', [8, 16, 32]),
}


def gen_numbers35(rnd, seeds=5):
    d = load(); new = []; presets = {}
    for name, (kind, nbt, sizes) in NUMBERS35.items():
        ids = []
        for n in sizes:
            for tier, tags in (('hierro', []), ('netheritamas', ['tier:netherite+'])):
                sid = f't{rnd}-numeros-{name}-{tier}-g{n}'
                sc = scenario(sid, kind, 'linea', 'base', 1)
                sc['blue'] = {'kind': 'guard', 'count': n, 'role': 'mix:shield=1,archer=1', 'rank': 1}
                sc['distance'] = 36
                sc['minutes'] = 4
                sc['tags'] += ['food:pack', 'off:morale', 'lab:D'] + tags + ([f'redNbt:{nbt}'] if nbt else [])
                sc['note'] = f'Números: {n} guardias en {tier} contra {name}'
                new.append(sc); ids.append(sid)
        presets[f't{rnd}-numeros-{name}'] = {'scenarios': ids, 'seeds': seeds, 'minutes': 4}
    replace_round(d, f't{rnd}-', new, presets); save(d)
    print('presets:', ','.join(presets))


# Ronda 36 (2026-11-10, David): armas de fuego. Con Scorched Guns, saqueadores, vindicadores y piglins sacan pistolas
# y aparecen sus propios pistoleros. Meta: que los guardias saquen contra ellos resultados parecidos a los de contra
# esqueletos sin tocar el daño de las armas ni la defensa del escudo: por coordinación. Primero la línea base: qué
# composición y qué respuesta ya existente aguanta mejor. Sin moral (la funcionalidad primero).
GUNS36 = {  # nombre -> (mob, mano, números)
    'esqueletos': ('skeleton', '', [16]),
    'casacas': ('scguns:redcoat', '', [8, 16]),
    'disidentes': ('scguns:dissident', '', [8]),
    'saqueadorespax': ('pillager', 'scguns:pax', [8, 16]),
    'vindicadorescallwell': ('vindicator', 'scguns:callwell', [8]),
}
GUN_RESP36 = {  # nombre -> (composiciones, cambios de escenario, cambios al azul, etiquetas)
    'base': (['linea', 'escudo', 'arco', 'lineascutum'], {}, {}, []),
    'volea60': (['linea', 'arco', 'lineascutum'], {'distance': 60}, {'fireMode': 'VOLLEY'}, []),
    'cobertura': (['linea', 'escudo', 'arco', 'lineascutum'], {'terrain': 'cover'}, {}, []),
    'tortuga': (['escudo', 'lineascutum'], {}, {}, ['march:TESTUDO', 'marchCharge:6']),
}


def gen_guns36(rnd, seeds=6):
    d = load(); new = []; presets = {}
    for name, (mob, hand, counts) in GUNS36.items():
        ids = []
        for n in counts:
            for resp, (comps, sc_extra, blue_extra, tags) in GUN_RESP36.items():
                for c in comps:
                    sid = f't{rnd}-fuego-{name}-r{n}-{c}-{resp}'
                    sc = scenario(sid, mob, c, 'base', n)
                    sc.update(sc_extra)
                    sc['blue'].update(blue_extra)
                    sc['tags'] = [t for t in sc['tags'] if not t.startswith('resp:')] + \
                        ['resp:' + resp, 'food:pack', 'off:morale', 'lab:C', 'family:fuego'] + tags + \
                        ([f'redHand:{hand}'] if hand else [])
                    sc['minutes'] = 3
                    sc['note'] = f'Armas de fuego: {n} {mob}{" con " + hand if hand else ""} contra {c}, {resp}'
                    new.append(sc); ids.append(sid)
        presets[f't{rnd}-fuego-{name}'] = {'scenarios': ids, 'seeds': seeds, 'minutes': 3}
    replace_round(d, f't{rnd}-', new, presets); save(d)
    print('presets:', ','.join(presets))


# Noche 2026-11-11 (David: "avanza tanto como quieras, deja tantas simulaciones como sea posible"). Cuatro bloques,
# un preset por máquina. Ver docs/RALLY_TACTICAL_SYSTEM.md (fases 1-3) para el porqué de cada uno.
#  A (t37) armas de fuego: confirmar la volea a 60 contra saqueadores con Pax, barrer la distancia, separar "distancia"
#     de "modo volea", escala, y el pistolero propio de Scorched Guns (hornlin).
#  B (t38) fuego coordinado (combat.FireWindows): repartir disparos (fire:spread) o esperar la ventana de
#     invulnerabilidad (fire:sync) contra hordas, tiradores, saqueadores con Pax, el muñeco y jefes.
#  C (t39) laboratorio de retirada: escapar de dragones (instrucción RETIRARSE contra pelear), a pie y a caballo.
#  D (t40) techo del cerebro contra Tremorzilla e Ignis: oráculo completo con netherita.
FIRE_TAGS = {'base': [], 'reparto': ['fire:spread'], 'sincro': ['fire:sync']}


def _gun(sid, mob, hand, comp, reds, dist=30, mode=None, extra=()):
    sc = scenario(sid, mob, comp, 'base', reds)
    sc['distance'] = dist
    if mode:
        sc['blue']['fireMode'] = mode
    sc['tags'] = [t for t in sc['tags'] if not t.startswith('resp:')] + ['food:pack', 'off:morale', 'lab:C',
                                                                          'family:fuego'] + list(extra) + \
        ([f'redHand:{hand}'] if hand else [])
    sc['minutes'] = 3
    return sc


def gen_night37(seeds=8):
    d = load(); new = []; presets = {}

    def add(preset, sc, note, n=seeds, minutes=3):
        sc['note'] = note; new.append(sc)
        presets.setdefault(preset, {'scenarios': [], 'seeds': n, 'minutes': minutes})['scenarios'].append(sc['id'])

    # A1 confirmación (lanzar con otra semilla base, 1001)
    for c in ('linea', 'arco', 'lineascutum'):
        for resp, dist, mode in (('base', 30, None), ('volea60', 60, 'VOLLEY')):
            add('t37-fuego-confirma', _gun(f't37-confirma-pax16-{c}-{resp}', 'pillager', 'scguns:pax', c, 16, dist, mode),
                f'Confirmación: 16 saqueadores con Pax contra {c}, {resp}', n=20)
    # A2 distancia de apertura y modo
    for c in ('linea', 'arco'):
        for dist in (40, 50, 60, 70, 80):
            add('t37-fuego-distancia', _gun(f't37-dist-pax16-{c}-volea{dist}', 'pillager', 'scguns:pax', c, 16, dist, 'VOLLEY'),
                f'Distancia: 16 Pax contra {c}, volea empezando a {dist}')
        add('t37-fuego-distancia', _gun(f't37-dist-pax16-{c}-normal60', 'pillager', 'scguns:pax', c, 16, 60, None),
            f'Distancia sin volea: 16 Pax contra {c}, fuego normal empezando a 60')
    # A3 escala y otros pistoleros
    for reds in (24, 32):
        for c in ('linea', 'lineascutum'):
            add('t37-fuego-escala', _gun(f't37-escala-pax{reds}-{c}-volea60', 'pillager', 'scguns:pax', c, reds, 60, 'VOLLEY'),
                f'Escala: {reds} Pax contra 8 guardias {c}, volea a 60')
    for mob, hand, reds in (('scguns:hornlin', '', 8), ('vindicator', 'scguns:callwell', 16), ('piglin', 'scguns:winnie', 8)):
        nm = mob.split(':')[-1]
        for c in ('linea', 'escudo', 'arco'):
            for resp, dist, mode in (('base', 30, None), ('volea60', 60, 'VOLLEY')):
                if c == 'escudo' and resp == 'volea60':
                    continue
                add('t37-fuego-otros', _gun(f't37-otros-{nm}{reds}-{c}-{resp}', mob, hand, c, reds, dist, mode),
                    f'Pistoleros: {reds} {mob}{" con " + hand if hand else ""} contra {c}, {resp}', n=6)

    # B fuego coordinado
    for fname, ftags in FIRE_TAGS.items():
        for mob, reds in (('zombie', 24), ('skeleton', 16)):
            for c in ('arco', 'linea'):
                sc = scenario(f't38-coord-{mob}{reds}-{c}-{fname}', mob, c, 'base', reds)
                sc['tags'] += ['food:pack', 'off:morale', 'lab:C'] + ftags
                add('t38-coord-hordas', sc, f'Fuego coordinado {fname}: {reds} {mob} contra {c}')
        for c in ('linea', 'arco'):
            add('t38-coord-pax', _gun(f't38-coord-pax16-{c}-volea60-{fname}', 'pillager', 'scguns:pax', c, 16, 60, 'VOLLEY',
                                      ftags), f'Fuego coordinado {fname}: 16 Pax contra {c}, volea a 60')
        sc = scenario(f't38-coord-muneco-arco-g8-{fname}', 'zombie', 'arco', 'base', 1)
        sc['distance'] = 12; sc['minutes'] = 2
        sc['tags'] += ['lab:A', 'dummy', DUMMY_NBT, 'off:morale'] + ftags
        add('t38-coord-jefes', sc, f'Muñeco, 8 arqueros, {fname}', n=6, minutes=4)
        for name, kind, nbt, tier in (('dragonadulto', 'iceandfire:fire_dragon', '{AgeTicks:1440000}', []),
                                      ('ignis', 'cataclysm:ignis', '', ['tier:netherite+']),
                                      ('harbinger', 'cataclysm:the_harbinger', '', ['tier:netherite+'])):
            sc = scenario(f't38-coord-{name}-g8-{fname}', kind, 'linea', 'base', 1)
            sc['blue'] = {'kind': 'guard', 'count': 8, 'role': 'mix:shield=1,archer=1', 'rank': 1}
            sc['distance'] = 36; sc['minutes'] = 4
            sc['tags'] += ['food:pack', 'off:morale', 'lab:D'] + tier + ftags + ([f'redNbt:{nbt}'] if nbt else [])
            add('t38-coord-jefes', sc, f'Fuego coordinado {fname}: 8 guardias contra {name}', n=6, minutes=4)

    # C retirada
    for dname, nbt in (('joven', '{AgeTicks:720000}'), ('adulto', '{AgeTicks:1440000}')):
        for nd in (1, 3, 5):
            for c, mounted in (('linea', False), ('cab', True)):
                for resp, tags in (('pelear', []), ('retirarse', ['instruction:RETIRARSE'])):
                    sc = scenario(f't39-retirada-{dname}x{nd}-{c}-{resp}', 'iceandfire:fire_dragon', c, 'base', nd)
                    sc['distance'] = 40; sc['minutes'] = 2
                    sc['tags'] += ['food:pack', 'off:morale', 'lab:D', 'measuredist', f'redNbt:{nbt}'] + tags
                    add('t39-retirada', sc, f'Retirada: 8 guardias {c} contra {nd} dragón(es) {dname}, {resp}',
                        n=6, minutes=2)

    # D techo del cerebro
    for name, kind, sizes in (('tremorzilla', 'alexscaves:tremorzilla', (16, 32)), ('ignis', 'cataclysm:ignis', (4, 8))):
        for n in sizes:
            for brain, tags in (('rally', []), ('oraculo', ['oracle:all']), ('sincro', ['fire:sync'])):
                sc = scenario(f't40-techo-{name}-g{n}-{brain}', kind, 'linea', 'base', 1)
                sc['blue'] = {'kind': 'guard', 'count': n, 'role': 'mix:shield=1,archer=1', 'rank': 1}
                sc['distance'] = 36; sc['minutes'] = 4
                sc['tags'] += ['food:pack', 'off:morale', 'lab:A', 'tier:netherite+'] + tags
                add('t40-techo', sc, f'Techo del cerebro: {n} guardias netherita contra {name}, {brain}', n=5, minutes=4)

    for pre in ('t37-', 't38-', 't39-', 't40-'):
        replace_round(d, pre, [x for x in new if x['id'].startswith(pre)], {k: v for k, v in presets.items() if k.startswith(pre)})
    save(d)
    for k, v in presets.items():
        print(k, len(v['scenarios']), 'escenarios x', v['seeds'], '=', len(v['scenarios']) * v['seeds'])


# Ronda 41 (noche 2026-11-11): la regla "mantener distancia contra armas de fuego" (FieldManual.standOff, config
# standOffVsGuns) puesta por defecto tras las rondas 36-37; se mide contra la misma pelea sin ella (off:standoff),
# empezando a 30 bloques como cualquier encuentro del juego.
def gen_standoff41(seeds=10):
    d = load(); new = []; presets = {}
    for mob, hand, reds in (('pillager', 'scguns:pax', 16), ('pillager', 'scguns:pax', 32), ('vindicator', 'scguns:callwell', 16),
                            ('scguns:redcoat', '', 16), ('skeleton', '', 16)):
        nm = mob.split(':')[-1] + (hand.split(':')[-1] if hand else '')
        for c in ('linea', 'arco', 'lineascutum', 'escudo'):
            for regla, tags in (('regla', []), ('sinregla', ['off:standoff'])):
                sc = _gun(f't41-distancia-{nm}{reds}-{c}-{regla}', mob, hand, c, reds, 30, None, tags)
                sc['note'] = f'Regla de distancia: {reds} {mob}{" con " + hand if hand else ""} contra {c}, {regla}'
                new.append(sc)
                presets.setdefault(f't41-distancia-{nm}{reds}', {'scenarios': [], 'seeds': seeds, 'minutes': 3})['scenarios'].append(sc['id'])
    replace_round(d, 't41-', new, presets); save(d)
    for k, v in presets.items():
        print(k, len(v['scenarios']) * v['seeds'])


# Ronda 42 (noche 2026-11-11): la tabla "cuántos guardias hacen falta" (ronda 35) repetida con fuego sincronizado
# (fire:sync: esperar a que termine la invulnerabilidad del blanco en vez de gastar la flecha). Mismos escenarios,
# mismas semillas: cambia solo la etiqueta.
def gen_numbers42(seeds=5):
    d = load(); new = []; presets = {}
    for sc0 in d['scenarios']:
        if not sc0['id'].startswith('t35-numeros-'):
            continue
        sc = json.loads(json.dumps(sc0)); sc['id'] = sc0['id'].replace('t35-', 't42-') + '-sincro'
        sc['tags'].append('fire:sync'); sc['note'] += ', fuego sincronizado'
        new.append(sc)
        name = sc0['id'].split('-')[2]
        presets.setdefault(f't42-numeros-{name}', {'scenarios': [], 'seeds': seeds, 'minutes': 4})['scenarios'].append(sc['id'])
    replace_round(d, 't42-', new, presets); save(d)
    print(','.join(presets))


# Ronda 43 (noche 2026-11-11): el fuego coordinado donde los guardias pierden: los techos de la ronda 24 (zombis,
# esqueletos y fantasmas en masa) y el punto de quiebre de la ronda 10 (olas grandes). Base y sincro en el mismo lote.
def gen_coordceiling43(seeds=8):
    d = load(); new = []; presets = {}
    by = {x['id']: x for x in d['scenarios']}
    groups = {'t43-coord-techo-zombie': [i for i in by if i.startswith('t24-techo-zombie-') and '-base-' in i],
              't43-coord-techo-skeleton': [i for i in by if i.startswith('t24-techo-skeleton-') and '-base-' in i],
              't43-coord-techo-phantom': [i for i in by if i.startswith('t24-techo-phantom-') and '-base-' in i],
              't43-coord-quiebre': [i for i in by if i.startswith('t10-quiebre-')]}
    for pre, ids in groups.items():
        for sid0 in ids:
            for fname, tags in (('base', []), ('sincro', ['fire:sync'])):
                sc = json.loads(json.dumps(by[sid0])); sc['id'] = 't43-' + sid0[4:] + '-' + fname
                sc['tags'] = sc['tags'] + tags; sc['note'] = sc.get('note', '') + f', fuego {fname}'
                new.append(sc)
                presets.setdefault(pre, {'scenarios': [], 'seeds': seeds if 'quiebre' not in pre else 6,
                                         'minutes': 12 if 'quiebre' in pre else 3})['scenarios'].append(sc['id'])
    replace_round(d, 't43-', new, presets); save(d)
    print(','.join(presets))


# Ronda 44 (2026-11-11, David: "hay peleas que perderé; una retirada exitosa es importante, con tanta supervivencia como
# sea posible"): la retirada nueva (RETREAT_FIGHTING: nunca deja de pelear, retrocede por saltos) contra la vieja
# (retreat:old) y contra pelear, en peleas que no se pueden ganar o casi.
RETREAT44 = {  # nombre -> (mob, NBT, cantidad)
    'joven3': ('iceandfire:fire_dragon', '{AgeTicks:720000}', 3),
    'joven5': ('iceandfire:fire_dragon', '{AgeTicks:720000}', 5),
    'adulto1': ('iceandfire:fire_dragon', '{AgeTicks:1440000}', 1),
    'adulto3': ('iceandfire:fire_dragon', '{AgeTicks:1440000}', 3),
    'vindicadores32': ('vindicator', '', 32),
}


def gen_retreat44(seeds=6):
    d = load(); new = []; presets = {}
    for name, (mob, nbt, n) in RETREAT44.items():
        for c in ('linea', 'arco', 'cab'):
            for resp, tags in (('pelear', []), ('vieja', ['instruction:RETIRARSE', 'retreat:old']),
                               ('nueva', ['instruction:RETIRARSE'])):
                sc = scenario(f't44-retirada-{name}-{c}-{resp}', mob, c, 'base', n)
                sc['distance'] = 40; sc['minutes'] = 2
                sc['tags'] += ['food:pack', 'off:morale', 'lab:D', 'measuredist'] + tags + ([f'redNbt:{nbt}'] if nbt else [])
                sc['note'] = f'Retirada: 8 guardias {c} contra {n} {name}, {resp}'
                new.append(sc)
                presets.setdefault(f't44-retirada-{name}', {'scenarios': [], 'seeds': seeds, 'minutes': 2})['scenarios'].append(sc['id'])
    replace_round(d, 't44-', new, presets); save(d)
    print(','.join(presets))


# Ronda 45 (2026-11-11, David: "alejarse con fuego directo y a la distancia correcta; volea solo cuando sea efectiva,
# p. ej. a caballo"): distancia fija 48-56 con tiro largo (la regla de la ronda 41) contra distancia aprendida en batalla
# (standoff:adaptive: empieza a 36, +4 por bala recibida, -1 cada 2 s sin balas; fuego directo dentro del alcance del arco)
# y contra sin regla; también a caballo.
def gen_standoff45(seeds=8):
    d = load(); new = []; presets = {}
    for mob, hand, reds in (('pillager', 'scguns:pax', 16), ('pillager', 'scguns:pax', 32), ('vindicator', 'scguns:callwell', 16)):
        nm = mob + hand.split(':')[-1] + str(reds)
        for c in ('arco', 'linea', 'lineascutum', 'cab'):
            for regla, tags in (('fija', ['standoff:fixed']), ('aprendida', ['standoff:adaptive']), ('sinregla', ['off:standoff'])):
                sc = _gun(f't45-distancia-{nm}-{c}-{regla}', mob, hand, c, reds, 30, None, tags)
                sc['note'] = f'Distancia {regla}: {reds} {mob} con {hand} contra {c}'
                new.append(sc)
                presets.setdefault(f't45-distancia-{nm}', {'scenarios': [], 'seeds': seeds, 'minutes': 3})['scenarios'].append(sc['id'])
    replace_round(d, 't45-', new, presets); save(d)
    print(','.join(presets))


# Ronda 46 = ficha F2 de la cola (claude/RALLY_COLA_POR_MODELO.md, diseño de Fable): caballería, ¿cuerpo o táctica?
# n = 30 por brazo, semillas pareadas (mismo lote de semillas en todos los brazos).
F2_ARMS = {  # brazo -> (rol azul, etiquetas)
    'a-base': ('mix:melee@m=4,archer@m=4', []),
    'b-apie': ('mix:melee=4,archer=4', []),
    'c-caballoinmortal': ('mix:melee@m=4,archer@m=4', ['mount:invulnerable']),
    'd-soloarqueros': ('mix:archer@m=1', []),
    'e-quietos48': ('mix:melee@m=4,archer@m=4', ['rider:standoff']),
    'f-kite': ('mix:melee@m=4,archer@m=4', ['rider:kite']),
    'g-arqueroskite': ('mix:archer@m=1', ['rider:kite']),
}


def gen_cavalry46(seeds=30):
    d = load(); new = []; presets = {}
    for ename, (mob, hand, reds) in {'pax16': ('pillager', 'scguns:pax', 16), 'vindicadores32': ('vindicator', '', 32)}.items():
        for arm, (role, tags) in F2_ARMS.items():
            sc = _gun(f't46-caballeria-{ename}-{arm}', mob, hand, 'cab', reds, 30, None, tags + ['lab:A'])
            sc['blue']['role'] = role
            sc['note'] = f'F2 caballería: 8 guardias ({arm}) contra {reds} {mob}{" con " + hand if hand else ""}'
            new.append(sc)
            half = 'ad' if arm[0] in 'abcd' else 'eg'
            presets.setdefault(f't46-caballeria-{ename}-{half}', {'scenarios': [], 'seeds': seeds, 'minutes': 3})['scenarios'].append(sc['id'])
    replace_round(d, 't46-', new, presets); save(d)
    print(','.join(presets))


# Ronda 47 = ficha F1 de la cola: tabla de atracción por tipo de mob (n = 10 por mob).
ATTR47 = {
    'vanilla1': ['zombie', 'husk', 'skeleton', 'stray', 'spider', 'wither_skeleton'],
    'vanilla2': ['pillager', 'vindicator', 'piglin', 'piglin_brute', 'ravager', 'witch', 'blaze', 'enderman'],
    'grandes': ['iceandfire:fire_dragon', 'cataclysm:ignis', 'cataclysm:the_harbinger', 'cataclysm:netherite_monstrosity',
                'cataclysm:ender_guardian', 'alexscaves:tremorzilla'],
}


def gen_attraction47(seeds=10):
    d = load(); new = []; presets = {}
    for grp, mobs in ATTR47.items():
        for mob in mobs:
            nm = mob.split(':')[-1]
            sc = scenario(f't47-atraccion-{nm}', mob, 'arco', 'base', 1)
            sc['blue']['count'] = 2
            sc['distance'] = 16; sc['minutes'] = 1
            sc['tags'] += ['probe:attraction', 'off:morale', 'lab:A'] + (['redNbt:{AgeTicks:720000}'] if 'dragon' in mob else []) \
                + (['redHand:scguns:pax'] if mob == 'pillager' else [])
            sc['note'] = f'F1 atracción: {mob}'
            new.append(sc)
            presets.setdefault(f't47-atraccion-{grp}', {'scenarios': [], 'seeds': seeds, 'minutes': 1})['scenarios'].append(sc['id'])
    replace_round(d, 't47-', new, presets); save(d)
    print(','.join(presets))


# Ronda 48 (2026-11-11, norma aprobada por David: n = 30 por brazo, semillas pareadas, para todo valor por defecto):
# reconfirmar los tres valores por defecto de hoy, decididos con n = 6-20, y la distancia contra pistoleros también en
# terreno con obstáculos (Fable: "48-56 puede ser un efecto de la arena").
def gen_confirm48(seeds=30):
    d = load(); new = []; presets = {}

    def add(preset, sc):
        new.append(sc)
        presets.setdefault(preset, {'scenarios': [], 'seeds': seeds, 'minutes': sc['minutes']})['scenarios'].append(sc['id'])

    for mob, hand, reds in (('pillager', 'scguns:pax', 16), ('pillager', 'scguns:pax', 32), ('vindicator', 'scguns:callwell', 16)):
        nm = mob + hand.split(':')[-1] + str(reds)
        for terr in ('flat', 'village', 'forest'):
            for c in ('arco', 'linea', 'lineascutum'):
                for regla, tags in (('regla', []), ('sinregla', ['off:standoff'])):
                    sc = _gun(f't48-distancia-{nm}-{terr}-{c}-{regla}', mob, hand, c, reds, 30, None, tags)
                    sc['terrain'] = terr
                    sc['note'] = f'Confirmación distancia ({terr}): {reds} {mob} con {hand} contra {c}, {regla}'
                    add(f't48-distancia-{nm}-{terr}', sc)
    by = {x['id']: x for x in d['scenarios']}
    for sid0 in ('t43-techo-skeleton-arco-base-r95-base', 't43-techo-zombie-linea-base-r75-base',
                 't43-techo-skeleton-arco-base-r68-base', 't38-coord-muneco-arco-g8-base', 't38-coord-ignis-g8-base'):
        for fname, tags in (('sincro', []), ('sinsincro', ['fire:off'])):
            sc = json.loads(json.dumps(by[sid0])); sc['id'] = 't48-sincro-' + sid0[4:] + '-' + fname
            sc['tags'] = [t for t in sc['tags'] if not t.startswith('fire:')] + tags
            add('t48-sincro-' + sid0.split('-')[2], sc)
    for sid0 in [i for i in by if i.startswith('t44-retirada-') and i.split('-')[2] in ('joven5', 'adulto1', 'vindicadores32')
                 and i.split('-')[3] in ('linea', 'cab')]:
        sc = json.loads(json.dumps(by[sid0])); sc['id'] = 't48-' + sid0[4:]
        add('t48-retirada-' + sid0.split('-')[2], sc)
    replace_round(d, 't48-', new, presets); save(d)
    for k, v in presets.items():
        print(k, len(v['scenarios']) * v['seeds'])


# Ronda 49 (2026-11-11): la regla de jinete "kite" (F2 brazo f) contra la de hoy, en más enemigos, n = 30, otra semilla.
def gen_rider49(seeds=30):
    d = load(); new = []; presets = {}
    for ename, (mob, hand, reds) in {'pax16': ('pillager', 'scguns:pax', 16), 'pax32': ('pillager', 'scguns:pax', 32),
                                     'callwell16': ('vindicator', 'scguns:callwell', 16), 'vindicadores32': ('vindicator', '', 32),
                                     'zombis40': ('zombie', '', 40), 'esqueletos16': ('skeleton', '', 16)}.items():
        for arm, tags in (('hoy', []), ('kite', ['rider:kite'])):
            sc = _gun(f't49-jinete-{ename}-{arm}', mob, hand, 'cab', reds, 30, None, tags)
            sc['note'] = f'Regla de jinete {arm}: caballería contra {reds} {mob}{" con " + hand if hand else ""}'
            new.append(sc)
            presets.setdefault(f't49-jinete-{ename}', {'scenarios': [], 'seeds': seeds, 'minutes': 3})['scenarios'].append(sc['id'])
    replace_round(d, 't49-', new, presets); save(d)
    print(','.join(presets))


# Ronda 50 = F2b (David 2026-11-12: "optimizar las monturas es sumamente relevante"): qué mantiene vivo al caballo.
# Con la regla de jinete por defecto (riderKite). n = 30, semillas pareadas.
HORSE50 = {
    '1-sinarmadura': [],
    '2-hierro': ['horseArmor:iron'],
    '3-diamante': ['horseArmor:diamond'],
    '4-superarmadura': ['horseArmor:diamond', 'horseArmorPts:9', 'horseToughness:8'],  # ~ netherita: 20 de armadura, 8 de dureza
    '5-vida50': ['horseHealth:50'],
    '6-diamantevida50': ['horseArmor:diamond', 'horseHealth:50'],
    '7-escudofrontal': ['horse:frontShield'],
    '8-inmortal': ['mount:invulnerable'],
}


def gen_horse50(seeds=30):
    d = load(); new = []; presets = {}
    for ename, (mob, hand, reds) in {'pax16': ('pillager', 'scguns:pax', 16), 'pax32': ('pillager', 'scguns:pax', 32),
                                     'vindicadores32': ('vindicator', '', 32)}.items():
        for cname, role in (('mixta', 'mix:melee@m=4,archer@m=4'), ('arqueros', 'mix:archer@m=1')):
            for arm, tags in HORSE50.items():
                sc = _gun(f't50-caballo-{ename}-{cname}-{arm}', mob, hand, 'cab', reds, 30, None, tags + ['lab:A'])
                sc['blue']['role'] = role
                sc['note'] = f'F2b caballo ({arm}): 8 jinetes {cname} contra {reds} {mob}{" con " + hand if hand else ""}'
                new.append(sc)
                presets.setdefault(f't50-caballo-{ename}-{cname}', {'scenarios': [], 'seeds': seeds, 'minutes': 3})['scenarios'].append(sc['id'])
    replace_round(d, 't50-', new, presets); save(d)
    print(','.join(presets))


# Ronda 51 = F2c: los jinetes con espada. Hoy cargan y se quedan dentro; "golpear y salir" (melee:hitrun) carga, golpea,
# sale 12 bloques 1,5 s y vuelve. Mixta 4 espadas + 4 arcos, y 8 espadas montadas. n = 30.
def gen_melee51(seeds=30):
    d = load(); new = []; presets = {}
    for ename, (mob, hand, reds) in {'vindicadores32': ('vindicator', '', 32), 'zombis40': ('zombie', '', 40),
                                     'pax16': ('pillager', 'scguns:pax', 16), 'esqueletos16': ('skeleton', '', 16)}.items():
        for cname, role in (('mixta', 'mix:melee@m=4,archer@m=4'), ('espadas', 'mix:melee@m=1')):
            for arm, tags in (('hoy', []), ('golpearysalir', ['melee:hitrun'])):
                sc = _gun(f't51-espadas-{ename}-{cname}-{arm}', mob, hand, 'cab', reds, 30, None, tags)
                sc['blue']['role'] = role
                sc['note'] = f'F2c jinetes con espada ({arm}): {cname} contra {reds} {mob}'
                new.append(sc)
                presets.setdefault(f't51-espadas-{ename}', {'scenarios': [], 'seeds': seeds, 'minutes': 3})['scenarios'].append(sc['id'])
    replace_round(d, 't51-', new, presets); save(d)
    print(','.join(presets))


# Ronda 52 = F5 (cola de Fable; David: "hay peleas que perderé, una retirada exitosa es importante"): laboratorio de
# escape real. Arena de 160 sin estorbos, refugio 50 bloques detrás de los azules; la orden llega a los 10 s de pelea.
# Brazos: pelear / retirada que pelea hacia el refugio / huida al refugio sin pelear. Métricas: % vivos, % en el refugio,
# % fuera de contacto (ningún enemigo los tiene de blanco) al final, segundos hasta llegar. n = 30.
ESCAPE52 = {  # nombre -> (mob, NBT, cantidad)
    'vindicadores80': ('vindicator', '', 80),   # v2: enemigos de tierra que no se pueden ganar
    'devastadores8': ('ravager', '', 8),
    'zombis40': ('zombie', '', 40),
    'vindicadores32': ('vindicator', '', 32),
    'devastadores4': ('ravager', '', 4),
    'dragonesjovenes3': ('iceandfire:fire_dragon', '{AgeTicks:720000}', 3),
    'dragonesadultos3': ('iceandfire:fire_dragon', '{AgeTicks:1440000}', 3),
}


def gen_escape52(seeds=30):
    d = load(); new = []; presets = {}
    for name, (mob, nbt, n) in ESCAPE52.items():
        for c in ('linea', 'arco', 'cab'):
            for arm, tags in (('pelear', []), ('retirada', ['instruction:RETIRARSE', 'instructionAt:10']),
                              ('huida', ['instruction:RETIRARSE', 'instructionAt:10', 'retreat:flee'])):
                sc = scenario(f't52-escape-{name}-{c}-{arm}', mob, c, 'base', n)
                sc['distance'] = 30; sc['minutes'] = 2
                sc['tags'] += ['food:pack', 'off:morale', 'lab:D', 'measuredist', 'arena:160', 'refuge:50'] + tags \
                    + ([f'redNbt:{nbt}'] if nbt else [])
                sc['note'] = f'F5 escape: 8 guardias {c} contra {n} {name}, {arm}; refugio a 50'
                new.append(sc)
                presets.setdefault(f't52-escape-{name}', {'scenarios': [], 'seeds': seeds, 'minutes': 2})['scenarios'].append(sc['id'])
    replace_round(d, 't52-', new, presets); save(d)
    print(','.join(presets))


# Ronda 53 = F9: pistoleros entre casas. En la aldea la regla de distancia salvaba a todos pero no remataba (ronda 48).
# Brazo nuevo: soltar la distancia cuando no hay línea de visión (standoff:losRelease) para ir a buscarla. n = 30.
def gen_village53(seeds=30):
    d = load(); new = []; presets = {}
    for mob, hand, reds in (('pillager', 'scguns:pax', 16), ('pillager', 'scguns:pax', 32), ('vindicator', 'scguns:callwell', 16)):
        nm = mob + hand.split(':')[-1] + str(reds)
        for c in ('arco', 'linea', 'lineascutum'):
            for arm, tags in (('regla', []), ('sinregla', ['off:standoff']), ('buscarvista', ['standoff:losRelease'])):
                sc = _gun(f't53-aldea-{nm}-{c}-{arm}', mob, hand, c, reds, 30, None, tags)
                sc['terrain'] = 'village'
                sc['note'] = f'F9 aldea: {reds} {mob} con {hand} contra {c}, {arm}'
                new.append(sc)
                presets.setdefault(f't53-aldea-{nm}', {'scenarios': [], 'seeds': seeds, 'minutes': 3})['scenarios'].append(sc['id'])
    replace_round(d, 't53-', new, presets); save(d)
    print(','.join(presets))


# Ronda 54 = F11, laboratorio de estrategias reales (David 2026-11-13): tácticas históricas traducidas a Minecraft y
# medidas contra la base. Ver docs/LAB_ESTRATEGIAS_REALES.md. n = 30.
#  supresion: arcos con Retroceso II (la "artillería" frena el avance cuerpo a cuerpo).
#  pinza (Cannas): instrucción FLANQUEAR frente a mantener la línea y frente a la base.
def gen_realstrat54(seeds=30):
    d = load(); new = []; presets = {}
    for ename, mob, reds in (('zombis60', 'zombie', 60), ('vindicadores32', 'vindicator', 32), ('devastadores4', 'ravager', 4)):
        for c in ('arco', 'linea'):
            for arm, tags in (('base', []), ('retroceso', ['ench:punch'])):
                sc = scenario(f't54-supresion-{ename}-{c}-{arm}', mob, c, 'base', reds)
                sc['tags'] += ['food:pack', 'off:morale', 'lab:C', 'family:estrategiasreales'] + tags
                sc['minutes'] = 3
                sc['note'] = f'F11 supresión: {reds} {mob} contra {c}, {arm}'
                new.append(sc)
                presets.setdefault(f't54-supresion-{ename}', {'scenarios': [], 'seeds': seeds, 'minutes': 3})['scenarios'].append(sc['id'])
    for ename, mob, hand, reds in (('zombis40', 'zombie', '', 40), ('vindicadores32', 'vindicator', '', 32), ('pax16', 'pillager', 'scguns:pax', 16)):
        for c in ('linea', 'cab'):
            for arm, tags in (('base', []), ('mantener', ['instruction:MANTENER_LINEA']), ('pinza', ['instruction:FLANQUEAR'])):
                sc = scenario(f't54-pinza-{ename}-{c}-{arm}', mob, c, 'base', reds)
                sc['tags'] += ['food:pack', 'off:morale', 'lab:C', 'family:estrategiasreales'] + tags + ([f'redHand:{hand}'] if hand else [])
                sc['blue']['count'] = 16
                sc['tags'].append('unit:4')
                sc['minutes'] = 3
                sc['note'] = f'F11 pinza (Cannas): 16 guardias {c} contra {reds} {mob}, {arm}'
                new.append(sc)
                presets.setdefault(f't54-pinza-{ename}', {'scenarios': [], 'seeds': seeds, 'minutes': 3})['scenarios'].append(sc['id'])
    replace_round(d, 't54-', new, presets); save(d)
    print(','.join(presets))


# Ronda 55 = F15 (F5 v3 paso 1): caballos de relevo. 8 guardias de línea contra 32 vindicadores (ronda 52 v2: pelear 50 %
# vivos, huida 26 %), refugio a 50, orden a los 10 s. Brazos: pelear / huida a pie / huida con relay:8:20 (8 caballos
# mansos y ensillados sin jinete 20 bloques detrás de azul; un guardia a pie que huye y pasa a <= 12 monta). n = 30.
def gen_relay55(seeds=30):
    d = load(); new = []; presets = {}
    flee = ['instruction:RETIRARSE', 'instructionAt:10', 'retreat:flee']
    for arm, tags in (('pelear', []), ('huida', flee), ('relevo', flee + ['relay:8:20'])):
        sc = scenario(f't55-relevo-vindicadores32-linea-{arm}', 'vindicator', 'linea', 'base', 32)
        sc['distance'] = 30; sc['minutes'] = 2
        sc['tags'] += ['food:pack', 'off:morale', 'lab:D', 'measuredist', 'arena:160', 'refuge:50'] + tags
        sc['note'] = f'F15 relevos: 8 guardias linea contra 32 vindicadores, {arm}; refugio a 50'
        new.append(sc)
        presets.setdefault('t55-relevo-vindicadores32', {'scenarios': [], 'seeds': seeds, 'minutes': 2})['scenarios'].append(sc['id'])
    replace_round(d, 't55-', new, presets); save(d)
    print(','.join(presets))


# Ronda 55b = F15b: relevo sin carga. Mismo montaje que la ronda 55; el brazo de relevo lleva ademas relayNoCharge (un
# guardia montado por relevo, con la huida activa y la orden dada, suelta el blanco y el conductor lo lleva a su puesto
# de FALLBACK sin cargar). Brazos pelear / huida / relevo sin carga. n = 30, seed 5501 (las semillas de la ronda 55).
def gen_relay55b(seeds=30):
    d = load(); new = []; presets = {}
    flee = ['instruction:RETIRARSE', 'instructionAt:10', 'retreat:flee']
    for arm, tags in (('pelear', []), ('huida', flee), ('relevonocarga', flee + ['relay:8:20', 'relayNoCharge'])):
        sc = scenario(f't55b-relevo-vindicadores32-linea-{arm}', 'vindicator', 'linea', 'base', 32)
        sc['distance'] = 30; sc['minutes'] = 2
        sc['tags'] += ['food:pack', 'off:morale', 'lab:D', 'measuredist', 'arena:160', 'refuge:50'] + tags
        sc['note'] = f'F15b relevo sin carga: 8 guardias linea contra 32 vindicadores, {arm}; refugio a 50'
        new.append(sc)
        presets.setdefault('t55b-relevo-vindicadores32', {'scenarios': [], 'seeds': seeds, 'minutes': 2})['scenarios'].append(sc['id'])
    replace_round(d, 't55b-', new, presets); save(d)
    print(','.join(presets))


# Ronda 56 = F16 (F12 paso 1): instrucciones por unidad (unitInstr). 16 guardias de línea (2 escudos + 2 arqueros por
# unidad) en 4 unidades de 4 contra 32 vindicadores. Brazos: (a) todos MANTENER_LINEA; (b) U1-U2 RETIRARSE a los 15 s,
# U3-U4 MANTENER_LINEA; (c) U1-U2 MANTENER_LINEA, U3-U4 ESPERAR y FLANQUEAR a los 20 s. Exploración: n = 8, seed 5601.
def gen_units56(seeds=8):
    d = load(); new = []; presets = {}
    hold = 'MANTENER_LINEA'
    arms = (('todos', f'U1={hold}@0,U2={hold}@0,U3={hold}@0,U4={hold}@0'),
            ('retira12', f'U1=RETIRARSE@15,U2=RETIRARSE@15,U3={hold}@0,U4={hold}@0'),
            ('flanco34', f'U1={hold}@0,U2={hold}@0,U3=ESPERAR@0,U3=FLANQUEAR@20,U4=ESPERAR@0,U4=FLANQUEAR@20'))
    for arm, spec in arms:
        sc = scenario(f't56-unidades-vindicadores32-linea-{arm}', 'vindicator', 'linea', 'base', 32)
        sc['blue']['count'] = 16; sc['blue']['role'] = 'mix:shield=2,archer=2'
        sc['distance'] = 30; sc['minutes'] = 2
        sc['tags'] += ['food:pack', 'off:morale', 'lab:D', 'measuredist', 'arena:160', 'unit:4',
                       f'instruction:{hold}', 'unitInstr:' + spec]
        sc['note'] = f'F16 instrucciones por unidad: 16 linea en 4 unidades contra 32 vindicadores, {arm}'
        new.append(sc)
        presets.setdefault('t56-unidades-vindicadores32', {'scenarios': [], 'seeds': seeds, 'minutes': 2})['scenarios'].append(sc['id'])
    replace_round(d, 't56-', new, presets); save(d)
    print(','.join(presets))


# Ronda 56b = F16b: cerrar la capacidad unitInstr. Mismo montaje que la ronda 56, solo el brazo (b) con RETIRARSE a los
# 5 s (a los 15 s el lote ya había terminado). n = 8, seed 5601 (mismas semillas que la ronda 56).
def gen_units56b(seeds=8):
    d = load(); new = []; presets = {}
    hold = 'MANTENER_LINEA'
    spec = f'U1=RETIRARSE@5,U2=RETIRARSE@5,U3={hold}@0,U4={hold}@0'
    sc = scenario('t56b-unidades-vindicadores32-linea-retira12', 'vindicator', 'linea', 'base', 32)
    sc['blue']['count'] = 16; sc['blue']['role'] = 'mix:shield=2,archer=2'
    sc['distance'] = 30; sc['minutes'] = 2
    sc['tags'] += ['food:pack', 'off:morale', 'lab:D', 'measuredist', 'arena:160', 'unit:4',
                   f'instruction:{hold}', 'unitInstr:' + spec]
    sc['note'] = 'F16b retirada por unidad: 16 linea en 4 unidades contra 32 vindicadores, U1-U2 RETIRARSE@5'
    new.append(sc)
    presets['t56b-retira-vindicadores32'] = {'scenarios': [sc['id']], 'seeds': seeds, 'minutes': 2}
    replace_round(d, 't56b-', new, presets); save(d)
    print(','.join(presets))


# Ronda 57 = F17: calibrar la horda para el paso 2 de F12. Control del paso 2 (todos MANTENER_LINEA, 16 linea en
# unit:4, mix:shield=2,archer=2, montaje de la ronda 56) contra 48, 64 y 80 vindicadores. Exploración: n = 8, seed 5701.
def gen_horde57(seeds=8, sizes=(48, 64, 80)):
    d = load(); new = []; presets = {}
    hold = 'MANTENER_LINEA'
    spec = f'U1={hold}@0,U2={hold}@0,U3={hold}@0,U4={hold}@0'
    for reds in sizes:
        sc = scenario(f't57-horda-vindicadores{reds}-linea-todos', 'vindicator', 'linea', 'base', reds)
        sc['blue']['count'] = 16; sc['blue']['role'] = 'mix:shield=2,archer=2'
        sc['distance'] = 30; sc['minutes'] = 2
        sc['tags'] += ['food:pack', 'off:morale', 'lab:D', 'measuredist', 'arena:160', 'unit:4',
                       f'instruction:{hold}', 'unitInstr:' + spec]
        sc['note'] = f'F17 calibrar horda: 16 linea en 4 unidades, todos MANTENER_LINEA, contra {reds} vindicadores'
        new.append(sc)
        presets.setdefault('t57-horda-vindicadores', {'scenarios': [], 'seeds': seeds, 'minutes': 2})['scenarios'].append(sc['id'])
    replace_round(d, 't57-', new, presets); save(d)
    print(','.join(presets))


# Ronda 58 (F18, 2026-11-15): Ignis, ¿dónde se pierde el daño? Misma pelea que dio el 44 % de O6 (t38-coord-ignis-g8-base:
# 8 guardias línea escudo+arco en netherita+, a 36, moral apagada; el fuego sincronizado ya va por defecto). Solo
# telemetría nueva (`ignis:*` en SimCombatStats); ninguna regla cambia.
def gen_ignis58(seeds=8):
    d = load()
    by = {s['id']: s for s in d['scenarios']}
    sc = json.loads(json.dumps(by['t38-coord-ignis-g8-base']))
    sc['id'] = 't58-ignis-g8-telemetria'
    sc['note'] = 'F18: 8 guardias netherita+ contra Ignis, sumideros de daño (escudo, distancia, tope por segundo)'
    presets = {'t58-ignis-g8-telemetria': {'scenarios': [sc['id']], 'seeds': seeds, 'minutes': 4}}
    replace_round(d, 't58-', [sc], presets); save(d)
    print(','.join(presets))


# Ronda 60 (F20, 2026-11-15): O5, cifra de partida con n = 30 contra Tremorzilla. Misma pelea que la ronda 30
# (t30-gigante-tremorzilla-netheritamas: 16 guardias línea escudo+arco en netherita+, a 36) con la moral apagada
# (off:morale, como piden las pruebas de jefes desde la ronda 34). Ninguna regla cambia; solo lectura de SimCombatStats.
def gen_tremor60(seeds=30):
    d = load()
    by = {s['id']: s for s in d['scenarios']}
    sc = json.loads(json.dumps(by['t30-gigante-tremorzilla-netheritamas']))
    sc['id'] = 't60-tremorzilla-g16-netheritamas'
    if 'off:morale' not in sc['tags']:
        sc['tags'].append('off:morale')
    sc['note'] = 'F20: 16 guardias netherita+ contra Tremorzilla, moral apagada, cifra de O5 con n = 30'
    presets = {'t60-tremorzilla-g16-netheritamas': {'scenarios': [sc['id']], 'seeds': seeds, 'minutes': 5}}
    replace_round(d, 't60-', [sc], presets); save(d)
    print(','.join(presets))


# Ronda 61 (F21, 2026-11-16): jefes sin moral, ¿qué composición? Copia exacta de t60 (Tremorzilla, 16) y t58 (Ignis, 8);
# solo cambia blue.role. mixRole reparte por proporción cíclica: 3:1 da 12 + 4 con 16 y 6 + 2 con 8; 1:3 da 2 + 6.
def gen_jefes61(seeds=30):
    d = load()
    by = {s['id']: s for s in d['scenarios']}
    arms = [('t60-tremorzilla-g16-netheritamas', 't61-tremor-comp', 't61-tremor-',
             [('linea', 'mix:shield=1,archer=1'), ('escudos', 'shield'), ('espadas', 'melee'),
              ('3a1', 'mix:shield=3,archer=1')], 5),
            ('t58-ignis-g8-telemetria', 't61-ignis-comp', 't61-ignis-',
             [('linea', 'mix:shield=1,archer=1'), ('arcos', 'archer'), ('1a3', 'mix:shield=1,archer=3'),
              ('3a1', 'mix:shield=3,archer=1')], 4)]
    new, presets = [], {}
    for base, preset, prefix, roles, minutes in arms:
        for name, role in roles:
            sc = json.loads(json.dumps(by[base]))
            sc['id'] = prefix + name
            sc['blue']['role'] = role
            sc['note'] = f'F21: {base} con blue.role = {role} (moral apagada)'
            new.append(sc)
            presets.setdefault(preset, {'scenarios': [], 'seeds': seeds, 'minutes': minutes})['scenarios'].append(sc['id'])
    replace_round(d, 't61-', new, presets); save(d)
    print(','.join(presets))


def gen_jefes62(seeds=30):
    """F22: confirmación de F21 con semillas nuevas (copia exacta de los t61-*) y curva de equipo del muro de escudos."""
    d = load()
    by = {s['id']: s for s in d['scenarios']}
    arms = [('t62-tremor-conf', 5, [('t62-tremor-linea', 't61-tremor-linea', None),
                                    ('t62-tremor-escudos', 't61-tremor-escudos', None),
                                    ('t62-tremor-escudos-netherita', 't61-tremor-escudos', 'tier:netherite'),
                                    ('t62-tremor-escudos-diamante', 't61-tremor-escudos', 'tier:diamond')]),
            ('t62-ignis-conf', 4, [('t62-ignis-linea', 't61-ignis-linea', None),
                                   ('t62-ignis-1a3', 't61-ignis-1a3', None),
                                   ('t62-ignis-arcos', 't61-ignis-arcos', None)])]
    new, presets = [], {}
    for preset, minutes, rows in arms:
        for sid, base, tier in rows:
            sc = json.loads(json.dumps(by[base]))
            sc['id'] = sid
            if tier:
                sc['tags'] = [tier if t.startswith('tier:') else t for t in sc['tags']]
            sc['note'] = f'F22: copia de {base}' + (f' cambiando solo la etiqueta de equipo a {tier}' if tier else '')
            new.append(sc)
            presets.setdefault(preset, {'scenarios': [], 'seeds': seeds, 'minutes': minutes})['scenarios'].append(sid)
    replace_round(d, 't62-', new, presets); save(d)
    print(','.join(presets))


def gen_o1_63(seeds=30):
    """F24 (O1): ¿el 57 % es el muro de la arena? Copias exactas de t48-distancia-pillagerpax32-flat-arco-regla cambiando
    solo la arena (96 por defecto / arena:160) y el modo de fuego (fire:sync = control, fire:spread, fire:off). El tamaño
    de arena se fija por lote (SimRunner: máximo de las etiquetas arena: del lote), por eso un preset por arena."""
    d = load()
    base = next(s for s in d['scenarios'] if s['id'] == 't48-distancia-pillagerpax32-flat-arco-regla')
    new, presets = [], {}
    for arena in (96, 160):
        preset = f't63-o1-arena{arena}'
        for fire in ('sync', 'spread', 'off'):
            sc = json.loads(json.dumps(base))
            sc['id'] = f't63-o1-a{arena}-{fire}'
            sc['tags'] += ['measuredist'] + ([] if fire == 'sync' else ['fire:' + fire]) + ([f'arena:{arena}'] if arena != 96 else [])
            sc['note'] = f'F24: copia de {base["id"]}, arena {arena}, fuego {fire}' + (' (control, sin etiqueta: valor del juego)' if fire == 'sync' else '')
            new.append(sc)
            presets.setdefault(preset, {'scenarios': [], 'seeds': seeds, 'minutes': base['minutes']})['scenarios'].append(sc['id'])
    replace_round(d, 't63-', new, presets); save(d)
    print(','.join(presets))


def gen_o1o4_67(seeds=30):
    """F28 (O1 y O4): confirmar la arena de 160 con semillas nuevas y ver si la caballería choca con el mismo muro.
    Copias exactas de t63-o1-a96-sync (O1) y t49-jinete-pax32-kite (O4) cambiando solo lo indicado; todas con
    measuredist; un preset por arena (el tamaño se fija por lote, como en gen_o1_63). n = 30, seed 6701."""
    d = load()
    by = {s['id']: s for s in d['scenarios']}
    o1, cab = by['t63-o1-a96-sync'], by['t49-jinete-pax32-kite']
    arms = {96: (('o1', o1, {}), ('cab', cab, {}), ('cabarcos', cab, {'role': 'mix:archer@m=1'})),
            160: (('o1', o1, {}), ('o1spread', o1, {'tag': 'fire:spread'}), ('o1pax48', o1, {'reds': 48}),
                  ('cab', cab, {}), ('cabarcos', cab, {'role': 'mix:archer@m=1'}))}
    new, presets = [], {}
    for arena, rows in arms.items():
        preset = f't67-arena{arena}'
        for arm, base, ch in rows:
            sc = json.loads(json.dumps(base))
            sc['id'] = f't67-a{arena}-{arm}'
            if 'measuredist' not in sc['tags']:
                sc['tags'].append('measuredist')
            if 'tag' in ch:
                sc['tags'].append(ch['tag'])
            if arena != 96:
                sc['tags'].append(f'arena:{arena}')
            if 'role' in ch:
                sc['blue']['role'] = ch['role']
            if 'reds' in ch:
                sc['red']['count'] = ch['reds']
            sc['note'] = f'F28: copia de {base["id"]}, arena {arena}' + ''.join(f', {k} = {v}' for k, v in ch.items())
            new.append(sc)
            presets.setdefault(preset, {'scenarios': [], 'seeds': seeds, 'minutes': 3})['scenarios'].append(sc['id'])
    replace_round(d, 't67-', new, presets); save(d)
    print(','.join(presets))


def gen_o2_68(seeds=30):
    """F29 (O2): ¿dónde están los Pax que quedan en la aldea? Copias exactas de t53-aldea-pillagerpax16/32-linea-
    buscarvista cambiando solo la etiqueta indicada (nostuck: sin corte por atasco; village:rowB: aldea sin la hilera A).
    La telemetría aldea:* sale sola en terreno village. n = 30, seed 6801, un preset."""
    d = load()
    by = {s['id']: s for s in d['scenarios']}
    b16, b32 = by['t53-aldea-pillagerpax16-linea-buscarvista'], by['t53-aldea-pillagerpax32-linea-buscarvista']
    arms = (('pax16', b16, None), ('pax16-nostuck', b16, 'nostuck'), ('pax16-rowB', b16, 'village:rowB'),
            ('pax32', b32, None), ('pax32-rowB', b32, 'village:rowB'))
    new, presets = [], {}
    for arm, base, tag in arms:
        sc = json.loads(json.dumps(base))
        sc['id'] = f't68-{arm}'
        if tag:
            sc['tags'].append(tag)
        sc['note'] = f'F29: copia de {base["id"]}' + (f' + {tag}' if tag else ' (control)')
        new.append(sc)
        presets.setdefault('t68-aldea', {'scenarios': [], 'seeds': seeds, 'minutes': 3})['scenarios'].append(sc['id'])
    replace_round(d, 't68-', new, presets); save(d)
    print(','.join(presets))


def gen_cebo64(seeds=30):
    """F25 (F12 paso 2): cebo y pinza contra 80 vindicadores. Montaje de gen_horde57 (16 guardias en unit:4, distancia 30,
    arena 160) con 4 minutos y la composición separada por unidad: mix:shield=8,archer=8 reparte por orden de aparición
    (mixRole: índice % 16; unitSetup: unidad = índice / 4 + 1), así U1 y U2 = 8 escudos y U3 y U4 = 8 arqueros. Solo
    cambia unitInstr entre brazos. n = 30, seed 6401."""
    d = load(); new = []; presets = {}
    hold = 'MANTENER_LINEA'
    pinza = 'U3=ESPERAR@0,U3=FLANQUEAR@12,U4=ESPERAR@0,U4=FLANQUEAR@12'
    arms = (('todos', f'U1={hold}@0,U2={hold}@0,U3={hold}@0,U4={hold}@0'),
            ('cebo', f'U1=RETIRARSE@5,U2=RETIRARSE@5,U3={hold}@0,U4={hold}@0'),
            ('pinza', f'U1={hold}@0,U2={hold}@0,' + pinza),
            ('cebopinza', 'U1=RETIRARSE@5,U2=RETIRARSE@5,' + pinza))
    for arm, spec in arms:
        sc = scenario(f't64-cebo-{arm}', 'vindicator', 'linea', 'base', 80)
        sc['blue']['count'] = 16; sc['blue']['role'] = 'mix:shield=8,archer=8'
        sc['distance'] = 30; sc['minutes'] = 4
        sc['tags'] += ['food:pack', 'off:morale', 'lab:D', 'measuredist', 'arena:160', 'unit:4',
                       f'instruction:{hold}', 'unitInstr:' + spec]
        sc['note'] = f'F25 cebo y pinza: U1-U2 = 8 escudos, U3-U4 = 8 arqueros, contra 80 vindicadores, brazo {arm}'
        new.append(sc)
        presets.setdefault('t64-cebo-vindicadores80', {'scenarios': [], 'seeds': seeds, 'minutes': 4})['scenarios'].append(sc['id'])
    replace_round(d, 't64-', new, presets); save(d)
    print(','.join(presets))


def gen_retaguardia66(seeds=30):
    """F27 (O3 paso 2): retaguardia contra 80 vindicadores. Copia de t52-escape-vindicadores80 (8 guardias, arena 160,
    refugio a 50, 2 minutos) con unit:2 = 4 unidades de 2; solo cambian unitInstr y retreat:flee. Sin instrucción
    general: una unidad sin entrada propia todavía pelea como la base (igual que t52 antes del segundo 10). En línea
    (mix:shield=4,archer=4, por orden de aparición) U1 y U2 = escudos, U3 y U4 = arqueros: la retaguardia son los
    escudos. n = 30, seed 5201 (las de F23)."""
    d = load(); new = []; presets = {}
    hold, flee = 'MANTENER_LINEA@0', 'RETIRARSE@10'
    arms = (('pelear', (hold, hold, hold, hold), False),
            ('huida', (flee, flee, flee, flee), True),
            ('reta2', (hold, flee, flee, flee), True),
            ('reta4', (hold, hold, flee, flee), True))
    for c in ('arco', 'linea'):
        for arm, units, fl in arms:
            sc = scenario(f't66-retaguardia-{c}-{arm}', 'vindicator', c, 'base', 80)
            sc['distance'] = 30; sc['minutes'] = 2
            sc['tags'] += ['food:pack', 'off:morale', 'lab:D', 'measuredist', 'arena:160', 'refuge:50', 'unit:2',
                           'unitInstr:' + ','.join(f'U{k + 1}={u}' for k, u in enumerate(units))] \
                + (['retreat:flee'] if fl else [])
            sc['note'] = f'F27 retaguardia: 8 guardias {c} en 4 unidades de 2 contra 80 vindicadores, {arm}; refugio a 50'
            new.append(sc)
            presets.setdefault('t66-retaguardia-vindicadores80', {'scenarios': [], 'seeds': seeds, 'minutes': 2})['scenarios'].append(sc['id'])
    replace_round(d, 't66-', new, presets); save(d)
    print(','.join(presets))


def gen_tremor65(seeds=30):
    """F26 (O5): por qué se atascan las peleas contra Tremorzilla. Copia exacta de t62-tremor-escudos (control) y el
    brazo 14 escudos + 2 arqueros (mix:shield=14,archer=2: con 16 guardias, índices 0-13 escudo y 14-15 arco). La
    telemetría nueva (boss:*, stuck:*) sale sola en SimCombatStats con cualquier rojo de 100 o más de vida máxima."""
    d = load()
    base = next(s for s in d['scenarios'] if s['id'] == 't62-tremor-escudos')
    new, presets = [], {}
    for sid, role in (('t65-tremor-escudos', None), ('t65-tremor-14a2', 'mix:shield=14,archer=2')):
        sc = json.loads(json.dumps(base))
        sc['id'] = sid
        if role:
            sc['blue']['role'] = role
        sc['note'] = 'F26: copia de t62-tremor-escudos' + (f' con blue.role = {role}' if role else ' (control)')
        new.append(sc)
        presets.setdefault('t65-tremor-atasco', {'scenarios': [], 'seeds': seeds, 'minutes': 5})['scenarios'].append(sid)
    replace_round(d, 't65-', new, presets); save(d)
    print(','.join(presets))


def gen_pinza69(seeds=30):
    """F30 (O7): pinza de arqueros con posición de flanco fija (etiqueta flank:fixed). Montaje de gen_cebo64 (80
    vindicadores, 16 guardias en unit:4, mix:shield=8,archer=8, arena 160, 4 minutos); solo cambian unitInstr y la
    etiqueta. n = 30, seed 6901."""
    d = load(); new = []; presets = {}
    hold = 'MANTENER_LINEA'
    flank = 'U3=FLANQUEAR@0,U4=FLANQUEAR@0'
    arms = (('todos', f'U1={hold}@0,U2={hold}@0,U3={hold}@0,U4={hold}@0', []),
            ('pinza0', f'U1={hold}@0,U2={hold}@0,' + flank, []),
            ('pinzafija', f'U1={hold}@0,U2={hold}@0,' + flank, ['flank:fixed']),
            ('cebopinzafija', 'U1=RETIRARSE@5,U2=RETIRARSE@5,' + flank, ['flank:fixed']))
    for arm, spec, extra in arms:
        sc = scenario(f't69-{arm}', 'vindicator', 'linea', 'base', 80)
        sc['blue']['count'] = 16; sc['blue']['role'] = 'mix:shield=8,archer=8'
        sc['distance'] = 30; sc['minutes'] = 4
        sc['tags'] += ['food:pack', 'off:morale', 'lab:D', 'measuredist', 'arena:160', 'unit:4',
                       f'instruction:{hold}', 'unitInstr:' + spec] + extra
        sc['note'] = f'F30 pinza fija: U1-U2 = 8 escudos, U3-U4 = 8 arqueros, contra 80 vindicadores, brazo {arm}'
        new.append(sc)
        presets.setdefault('t69-pinza', {'scenarios': [], 'seeds': seeds, 'minutes': 4})['scenarios'].append(sc['id'])
    replace_round(d, 't69-', new, presets); save(d)
    print(','.join(presets))


def gen_aldea70(seeds=30):
    """F31 (O2): ¿un guardia cuerpo a cuerpo entra en la casa? Copias exactas de t68-pax16 / t68-pax32 cambiando solo
    lo indicado; nostuck en todos. espadas8 = rol 'melee' (8 espadas A PIE: el rol de la ronda 51 era montado,
    mix:melee@m=1); escudos8 = rol 'shield' (F21). n = 30, seed 7001."""
    d = load()
    by = {s['id']: s for s in d['scenarios']}
    b16, b32 = by['t68-pax16'], by['t68-pax32']
    arms = (('pax16', b16, {}), ('pax16-sinregla', b16, {'sinregla': True}), ('pax16-espadas8', b16, {'role': 'melee'}),
            ('pax16-escudos8', b16, {'role': 'shield'}), ('pax32', b32, {}), ('pax32-espadas8', b32, {'role': 'melee'}))
    new, presets = [], {}
    for arm, base, ch in arms:
        sc = json.loads(json.dumps(base))
        sc['id'] = f't70-{arm}'
        sc['tags'].append('nostuck')
        if ch.get('sinregla'):
            sc['tags'] = ['off:standoff' if t == 'standoff:losRelease' else t for t in sc['tags']]
        if 'role' in ch:
            sc['blue']['role'] = ch['role']
        sc['note'] = f'F31: copia de {base["id"]} + nostuck' + ''.join(f', {k} = {v}' for k, v in ch.items())
        new.append(sc)
        presets.setdefault('t70-aldea', {'scenarios': [], 'seeds': seeds, 'minutes': 3})['scenarios'].append(sc['id'])
    replace_round(d, 't70-', new, presets); save(d)
    print(','.join(presets))


def gen_cab71(seeds=30):
    """F32 (O4): qué hacen los 4 jinetes con espada. Copias exactas de t67-a160-cab con unit:4 (U1 = índices 0-3 =
    melee@m, U2 = 4-7 = archer@m). Sin unitInstr no hay unidades (unitSetup no corre): en cab y cabarcos unit:4 no
    cambia nada y no hay u1:act:*. pie = mix:melee=4,archer@m=4 (espadas a pie). n = 30, seed 7101."""
    d = load()
    base = next(s for s in d['scenarios'] if s['id'] == 't67-a160-cab')
    arms = (('cab', {}), ('cabarcos', {'role': 'mix:archer@m=1'}),
            ('reserva', {'unitInstr': 'U1=ESPERAR@0,U1=AVANZAR@20'}), ('flanco', {'unitInstr': 'U1=FLANQUEAR@0'}),
            ('pie', {'role': 'mix:melee=4,archer@m=4'}))
    new, presets = [], {}
    for arm, ch in arms:
        sc = json.loads(json.dumps(base))
        sc['id'] = f't71-{arm}'
        sc['tags'].append('unit:4')
        if 'unitInstr' in ch:
            sc['tags'].append('unitInstr:' + ch['unitInstr'])
        if 'role' in ch:
            sc['blue']['role'] = ch['role']
        sc['note'] = 'F32: copia de t67-a160-cab + unit:4' + ''.join(f', {k} = {v}' for k, v in ch.items())
        new.append(sc)
        presets.setdefault('t71-cab160', {'scenarios': [], 'seeds': seeds, 'minutes': 3})['scenarios'].append(sc['id'])
    replace_round(d, 't71-', new, presets); save(d)
    print(','.join(presets))


def gen_tremor72(seeds=30):
    """F33 (O5): qué hace cada guardia en los huecos sin daño. Copia exacta de t65-tremor-escudos (control) y la misma
    con la etiqueta nogrief (mobGriefing apagado durante la corrida). La telemetría gap:* sale sola. n = 30, seed 7201."""
    d = load()
    base = next(s for s in d['scenarios'] if s['id'] == 't65-tremor-escudos')
    new, presets = [], {}
    for sid, tag in (('t72-escudos', None), ('t72-escudos-nogrief', 'nogrief')):
        sc = json.loads(json.dumps(base))
        sc['id'] = sid
        if tag:
            sc['tags'].append(tag)
        sc['note'] = 'F33: copia de t65-tremor-escudos' + (f' + {tag}' if tag else ' (control)')
        new.append(sc)
        presets.setdefault('t72-tremor-hueco', {'scenarios': [], 'seeds': seeds, 'minutes': 5})['scenarios'].append(sid)
    replace_round(d, 't72-', new, presets); save(d)
    print(','.join(presets))


def gen_ignis73(seeds=30):
    """F34 (O6): distancia de retroceso frente a un enemigo grande. Copias exactas de t62-ignis-1a3 (2 escudos + 6
    arcos) y t62-ignis-arcos (8 arcos); solo se añade la etiqueta kite:big=<near>-<far>. n = 30, seed 7301."""
    d = load()
    by = {s['id']: s for s in d['scenarios']}
    rows = (('t73-2a6', 't62-ignis-1a3', None), ('t73-2a6-k10', 't62-ignis-1a3', 'kite:big=10-16'),
            ('t73-2a6-k14', 't62-ignis-1a3', 'kite:big=14-22'),
            ('t73-arcos', 't62-ignis-arcos', None), ('t73-arcos-k14', 't62-ignis-arcos', 'kite:big=14-22'))
    new, presets = [], {}
    for sid, base, tag in rows:
        sc = json.loads(json.dumps(by[base]))
        sc['id'] = sid
        if tag:
            sc['tags'].append(tag)
        sc['note'] = f'F34: copia de {base}' + (f' + {tag}' if tag else ' (control)')
        new.append(sc)
        presets.setdefault('t73-ignis-kite', {'scenarios': [], 'seeds': seeds, 'minutes': by[base]['minutes']})['scenarios'].append(sid)
    replace_round(d, 't73-', new, presets); save(d)
    print(','.join(presets))


def gen_o1_74(seeds=30):
    """F35 (O1 duro): 8 arqueros contra 48 Pax. Copias exactas de t67-a160-o1pax48; solo cambia la etiqueta.
    standOffAdaptive vale false en RallyConfig, así que el brazo de distancia usa standoff:adaptive. n = 30, seed 7401."""
    d = load()
    base = next(s for s in d['scenarios'] if s['id'] == 't67-a160-o1pax48')
    new, presets = [], {}
    for sid, tags in (('t74-pax48', []), ('t74-pax48-spread', ['fire:spread']), ('t74-pax48-dist', ['standoff:adaptive']),
                      ('t74-pax48-spread-dist', ['fire:spread', 'standoff:adaptive'])):
        sc = json.loads(json.dumps(base))
        sc['id'] = sid
        sc['tags'] += tags
        sc['note'] = 'F35: copia de t67-a160-o1pax48' + (' + ' + ' + '.join(tags) if tags else ' (control: valor del juego)')
        new.append(sc)
        presets.setdefault('t74-o1duro', {'scenarios': [], 'seeds': seeds, 'minutes': 3})['scenarios'].append(sid)
    replace_round(d, 't74-', new, presets); save(d)
    print(','.join(presets))


def _copies(d, rows, prefix, ficha, preset_of, seeds):
    """Copias exactas de escenarios existentes; rows = (id nuevo, id base, cambios). cambios: 'tags' (se añaden),
    'role' (rol azul), 'red' / 'blue' (número), 'redSpec' (dict que se mezcla en el rojo), 'minutes' (tope del escenario), 'distance' (distancia de
    salida azul-rojo; SimRunner la limita a arena.size - 16). preset_of(id nuevo) -> nombre del preset."""
    by = {s['id']: s for s in d['scenarios']}
    new, presets = [], {}
    for sid, base, ch in rows:
        sc = json.loads(json.dumps(by[base]))
        sc['id'] = sid
        sc['tags'] += list(ch.get('tags', ()))
        if 'role' in ch:
            sc['blue']['role'] = ch['role']
        if 'red' in ch:
            sc['red']['count'] = ch['red']
        if 'redSpec' in ch:  # F160: campos del rojo que cambian (p. ej. kind/role)
            sc['red'].update(ch['redSpec'])
        if 'blue' in ch:
            sc['blue']['count'] = ch['blue']
        if 'minutes' in ch:
            sc['minutes'] = ch['minutes']
        if 'distance' in ch:  # F177
            sc['distance'] = ch['distance']
        sc['note'] = f'{ficha}: copia de {base}' + ''.join(f', {k} = {v}' for k, v in ch.items()) if ch \
            else f'{ficha}: copia de {base} (control)'
        new.append(sc)
        presets.setdefault(preset_of(sid), {'scenarios': [], 'seeds': seeds, 'minutes': sc['minutes']})['scenarios'].append(sid)
    replace_round(d, prefix, new, presets)
    return presets


def gen_ignis75(seeds=30):
    """F36 (O6): confirmar kite:big con semillas nuevas. Copias exactas de los t73 (es decir de t62-ignis-arcos y
    t62-ignis-1a3) y de t62-tremor-linea (no empeora otro jefe); solo se añade la etiqueta. Cada escenario lleva sus
    minutos (Ignis 4, Tremorzilla 5). n = 30."""
    d = load()
    k14, k18 = {'tags': ['kite:big=14-22']}, {'tags': ['kite:big=18-26']}
    rows = (('t75-arcos', 't62-ignis-arcos', {}), ('t75-arcos-k14', 't62-ignis-arcos', k14),
            ('t75-arcos-k18', 't62-ignis-arcos', k18), ('t75-2a6', 't62-ignis-1a3', {}),
            ('t75-2a6-k14', 't62-ignis-1a3', k14),
            ('t75-tremor-linea', 't62-tremor-linea', {}), ('t75-tremor-linea-k14', 't62-tremor-linea', k14))
    presets = _copies(d, rows, 't75-', 'F36', lambda sid: 't75-ignis-conf', seeds)
    save(d); print(','.join(presets))


def gen_o1_76(seeds=30):
    """F37 (O1): confirmar fire:spread. Copias exactas de t67-a160-o1pax48, t67-a160-o1 (32 Pax, arena 160) y
    t67-a96-o1 (32 Pax, arena 96); solo se añade fire:spread. El tamaño de arena se fija por lote (máximo de las
    etiquetas arena: del preset), así que el brazo de arena 96 va en su propio preset t76-o1conf-a96.
    Además t76-reg-*: los presets de tools/regresion.py con fire:spread en cada escenario (ids t76-spread-<id>);
    se comparan con `python3 tools/regresion.py check t76-reg- t76-spread-`."""
    d = load()
    sp = {'tags': ['fire:spread']}
    rows = [('t76-pax48', 't67-a160-o1pax48', {}), ('t76-pax48-spread', 't67-a160-o1pax48', sp),
            ('t76-pax32', 't67-a160-o1', {}), ('t76-pax32-spread', 't67-a160-o1', sp),
            ('t76-a96-pax32', 't67-a96-o1', {}), ('t76-a96-pax32-spread', 't67-a96-o1', sp)]
    sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
    import regresion
    reg_of = {}
    for name, group in regresion.SUITE.items():
        for sid, _ in group:
            rows.append((f't76-spread-{sid}', sid, sp))
            reg_of[f't76-spread-{sid}'] = 't76-' + name
    presets = _copies(d, rows, 't76-', 'F37', lambda sid: reg_of.get(sid) or ('t76-o1conf-a96' if sid.startswith('t76-a96-')
                                                                         else 't76-o1conf'), seeds)
    for name in regresion.SUITE:
        presets['t76-' + name].update(seeds=regresion.SEEDS, minutes=12)
    save(d); print(','.join(presets))


def gen_aldea77(seeds=30):
    """F38 (O2): escudos delante contra 32 Pax. Copias exactas de t70-pax32 / t70-pax16 (nostuck y standoff:losRelease
    incluidos); solo cambia el rol azul. Nota: el control ya es mix:shield=4,archer=4, así que 4e4a
    (mix:shield=1,archer=1) tiene la misma composición y solo cambia el orden (alterno) en la línea. n = 30."""
    d = load()
    rows = (('t77-pax32', 't70-pax32', {}), ('t77-pax32-escudos8', 't70-pax32', {'role': 'shield'}),
            ('t77-pax32-4e4a', 't70-pax32', {'role': 'mix:shield=1,archer=1'}),
            ('t77-pax32-2e6a', 't70-pax32', {'role': 'mix:shield=1,archer=3'}),
            ('t77-pax16', 't70-pax16', {}), ('t77-pax16-4e4a', 't70-pax16', {'role': 'mix:shield=1,archer=1'}))
    presets = _copies(d, rows, 't77-', 'F38', lambda sid: 't77-aldea-escudos', seeds)
    save(d); print(','.join(presets))


def gen_cab78(seeds=30):
    """F39 (O4): ¿el jinete con escudo tiene papel contra balas? Copias exactas de t71-cab (arena 160, unit:4 sin
    unitInstr = sin efecto); solo cambia el rol azul. mixRole admite @m en cualquier rol (shield@m incluido). n = 30."""
    d = load()
    rows = (('t78-cab', 't71-cab', {}), ('t78-cabarcos', 't71-cab', {'role': 'mix:archer@m=1'}),
            ('t78-cab-escudos', 't71-cab', {'role': 'mix:shield@m=4,archer@m=4'}),
            ('t78-escudospie', 't71-cab', {'role': 'mix:shield=4,archer@m=4'}))
    presets = _copies(d, rows, 't78-', 'F39', lambda sid: 't78-cab-escudos', seeds)
    save(d); print(','.join(presets))


def gen_cab79(seeds=30):
    """F40 (O4): confirmar jinetes con escudo y subir a 48 Pax. Copias exactas de t78-* (arena 160); en los p48 solo
    cambia el número de Pax (32 -> 48). escudos8 = mix:shield@m=8 (exploración). n = 30."""
    d = load()
    p48 = {'red': 48}
    rows = (('t79-cab', 't78-cab', {}), ('t79-cab-escudos', 't78-cab-escudos', {}),
            ('t79-p48-cab', 't78-cab', p48), ('t79-p48-cabarcos', 't78-cabarcos', p48),
            ('t79-p48-cab-escudos', 't78-cab-escudos', p48),
            ('t79-p48-escudos8', 't78-cab', {'red': 48, 'role': 'mix:shield@m=8'}))
    presets = _copies(d, rows, 't79-', 'F40', lambda sid: 't79-cab-conf', seeds)
    save(d); print(','.join(presets))


def gen_o1_80(seeds=30):
    """F41 (O1, composición): ¿escudos delante de los arcos en abierto contra 48 Pax? Copias exactas de
    t76-pax48-spread (arena 160, fire:spread); solo cambia el rol azul. n = 30."""
    d = load()
    b = 't76-pax48-spread'
    rows = (('t80-arcos8', b, {}), ('t80-2e6a', b, {'role': 'mix:shield=1,archer=3'}),
            ('t80-4e4a', b, {'role': 'mix:shield=1,archer=1'}), ('t80-escudos8', b, {'role': 'shield'}))
    presets = _copies(d, rows, 't80-', 'F41', lambda sid: 't80-abierto-escudos', seeds)
    save(d); print(','.join(presets))


def gen_aldea81(seeds=30):
    """F42 (O2): confirmar 8 escudos contra 16 y ver si contra 32 el límite es el reloj. Copias exactas de t77-*
    (nostuck y standoff:losRelease incluidos). 5min = tope de 5 min por escenario (el resto 3); escudos12 = 12
    guardias con rol shield. n = 30."""
    d = load()
    rows = (('t81-pax16', 't77-pax16', {}), ('t81-pax16-escudos8', 't77-pax16', {'role': 'shield'}),
            ('t81-pax32', 't77-pax32', {}), ('t81-pax32-escudos8', 't77-pax32-escudos8', {}),
            ('t81-pax32-escudos8-5min', 't77-pax32-escudos8', {'minutes': 5}),
            ('t81-pax32-escudos12', 't77-pax32-escudos8', {'blue': 12}))
    presets = _copies(d, rows, 't81-', 'F42', lambda sid: 't81-aldea-conf', seeds)
    save(d); print(','.join(presets))


def gen_tremor83(seeds=30):
    """F43 (O5): tope de cuerpo a cuerpo para blancos grandes. Copias exactas de t72-escudos; solo se añade la etiqueta
    cap:big=<n> (Engagement.simBigCap, blancos de ancho >= 2). n = 30, seed 8300."""
    d = load()
    rows = (('t83-escudos', 't72-escudos', {}), ('t83-cap12', 't72-escudos', {'tags': ['cap:big=12']}),
            ('t83-cap16', 't72-escudos', {'tags': ['cap:big=16']}))
    presets = _copies(d, rows, 't83-', 'F43', lambda sid: 't83-tremor-cap', seeds)
    save(d); print(','.join(presets))


def gen_aldea84(seeds=30):
    """F44 (O2, diagnóstico): ¿es la puerta cerrada? Copias exactas de t81-*; el brazo -abierta solo añade la etiqueta
    village:openDoors (SimArena coloca las puertas abiertas). n = 30, seed 8300."""
    d = load()
    op = {'tags': ['village:openDoors']}
    rows = (('t84-pax16', 't81-pax16', {}), ('t84-pax16-abierta', 't81-pax16', op),
            ('t84-pax16-escudos8', 't81-pax16-escudos8', {}), ('t84-pax16-escudos8-abierta', 't81-pax16-escudos8', op),
            ('t84-pax32-escudos8', 't81-pax32-escudos8', {}), ('t84-pax32-escudos8-abierta', 't81-pax32-escudos8', op))
    presets = _copies(d, rows, 't84-', 'F44', lambda sid: 't84-aldea-puertas', seeds)
    save(d); print(','.join(presets))


def gen_reg85():
    """F45: la suite de tools/regresion.py con kite:big=14-22 en cada escenario (ids t85-kite-<id>, presets
    t85-reg-*), mismo generador que t76-reg-*. Se compara contra el lote 'regresion' de la misma llamada."""
    d = load()
    sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
    import regresion
    kb = {'tags': ['kite:big=14-22']}
    rows, reg_of = [], {}
    for name, group in regresion.SUITE.items():
        for sid, _ in group:
            rows.append((f't85-kite-{sid}', sid, kb))
            reg_of[f't85-kite-{sid}'] = 't85-' + name
    presets = _copies(d, rows, 't85-', 'F45', lambda sid: reg_of[sid], regresion.SEEDS)
    for name in regresion.SUITE:
        presets['t85-' + name].update(seeds=regresion.SEEDS, minutes=12)
    save(d); print(','.join(presets))


def gen_aldea86(seeds=30):
    """F46 (O2): regla "abrir brecha". Copias exactas de los t84-* con puertas cerradas; el brazo -brecha solo añade la
    etiqueta door:breach (combat/Breach). n = 30, seed 8600."""
    d = load()
    br = {'tags': ['door:breach']}
    rows = (('t86-pax16', 't84-pax16', {}), ('t86-pax16-brecha', 't84-pax16', br),
            ('t86-pax16-escudos8', 't84-pax16-escudos8', {}), ('t86-pax16-escudos8-brecha', 't84-pax16-escudos8', br),
            ('t86-pax32-escudos8', 't84-pax32-escudos8', {}), ('t86-pax32-escudos8-brecha', 't84-pax32-escudos8', br))
    presets = _copies(d, rows, 't86-', 'F46', lambda sid: 't86-aldea-brecha', seeds)
    save(d); print(','.join(presets))


def gen_tremor87(seeds=30):
    """F47 (O5): menos guardias dentro, el resto de relevo. Copias exactas de t83-escudos; solo se añade cap:big=<n>
    (Engagement.simBigCap: sin recorte, vale también bajo 8). n = 30, seed 8600."""
    d = load()
    rows = (('t87-escudos', 't83-escudos', {}), ('t87-cap4', 't83-escudos', {'tags': ['cap:big=4']}),
            ('t87-cap6', 't83-escudos', {'tags': ['cap:big=6']}))
    presets = _copies(d, rows, 't87-', 'F47', lambda sid: 't87-tremor-relevo', seeds)
    save(d); print(','.join(presets))


def gen_def88(seeds=30):
    """F48 (O1, O6): valores por defecto spreadFire y kiteBig. Sin etiqueta = el valor nuevo del juego. Copias exactas de
    t76-pax48 (arena 160) y t75-arcos (Ignis); -sync = fire:sync (el valor anterior), -kiteoff = kite:big=off. El tamaño
    de arena se fija por preset (máximo de las etiquetas arena:), así que Ignis va en su propio preset t88-defecto-ignis
    para no pasar a la arena 160 de los Pax. n = 30, seed 8600."""
    d = load()
    sync = {'tags': ['fire:sync']}
    rows = (('t88-pax48', 't76-pax48', {}), ('t88-pax48-sync', 't76-pax48', sync),
            ('t88-ignis', 't75-arcos', {}), ('t88-ignis-sync', 't75-arcos', sync),
            ('t88-ignis-kiteoff', 't75-arcos', {'tags': ['kite:big=off']}))
    presets = _copies(d, rows, 't88-', 'F48', lambda sid: 't88-defecto-ignis' if 'ignis' in sid else 't88-defecto', seeds)
    save(d); print(','.join(presets))



def gen_aldea89(seeds=30):
    """F49 (O2): confirmar la brecha y cerrar la puerta al acabar. Copias exactas de los t86-* de control (puertas
    cerradas); -brecha = door:breach, -cierra = door:breach + door:close (Breach.closeStep). n = 30, seed 8900."""
    d = load()
    br, cl = {'tags': ['door:breach']}, {'tags': ['door:breach', 'door:close']}
    rows = []
    for b in ('pax16', 'pax16-escudos8', 'pax32-escudos8'):
        rows += [(f't89-{b}', f't86-{b}', {}), (f't89-{b}-brecha', f't86-{b}', br), (f't89-{b}-cierra', f't86-{b}', cl)]
    presets = _copies(d, rows, 't89-', 'F49', lambda sid: 't89-aldea-cierra', seeds)
    save(d); print(','.join(presets))


def gen_refugio90(seeds=30):
    """F50 (O3): el refugio es una casa con puerta. Copias de t52-escape-<enemigo>-arco-pelear (8 arqueros, arena 160,
    2 min) con refuge:50 -> refuge:16 EN SU SITIO (SimScenario.tag devuelve la primera etiqueta refuge:, y SimExtras
    necesita la del número) y nostuck. pelear = control con la casa (mismo terreno); campo = RETIRARSE desde el
    segundo 0 con retreat:flee, sin casa; casa = campo + refuge:house (SimArena.refugeHouse). n = 30."""
    d = load()
    by = {s['id']: s for s in d['scenarios']}
    flee = ['instruction:RETIRARSE', 'instructionAt:0', 'retreat:flee']
    new, presets = [], {}
    for name in ('vindicadores80', 'devastadores8'):
        base = by[f't52-escape-{name}-arco-pelear']
        for arm, extra in (('pelear', ['refuge:house']), ('campo', flee), ('casa', flee + ['refuge:house'])):
            sc = json.loads(json.dumps(base))
            sc['id'] = f't90-{name}-{arm}'
            sc['tags'] = ['refuge:16' if t == 'refuge:50' else t for t in sc['tags']] + ['nostuck'] + extra
            sc['note'] = f'F50: copia de {base["id"]} con refuge:16, nostuck' + (', ' + ', '.join(extra))
            new.append(sc)
            presets.setdefault('t90-refugio-casa', {'scenarios': [], 'seeds': seeds, 'minutes': sc['minutes']})['scenarios'].append(sc['id'])
    replace_round(d, 't90-', new, presets); save(d)
    print(','.join(presets))


def gen_aldea91(seeds=30):
    """F53 (O2): "abrir brecha" por defecto (config breachDoors, cierre a 24 bloques). Copias exactas de los t89-* de
    control (sin etiquetas de puerta); -off = off:breach (la regla apagada), -defecto = sin etiqueta (el valor nuevo:
    abre y cierra), -noclose = door:noclose (abre, no cierra). n = 30, seed 9100."""
    d = load()
    rows = []
    for b in ('pax16', 'pax16-escudos8', 'pax32-escudos8'):
        rows += [(f't91-{b}-off', f't89-{b}', {'tags': ['off:breach']}), (f't91-{b}-defecto', f't89-{b}', {}),
                 (f't91-{b}-noclose', f't89-{b}', {'tags': ['door:noclose']})]
    presets = _copies(d, rows, 't91-', 'F53', lambda sid: 't91-aldea-defecto', seeds)
    save(d); print(','.join(presets))


def gen_refugio92(seeds=30):
    """F54 (O3): la casa donde pelear pierde (8 de línea) y la forma compacta. Copias de t52-escape-<enemigo>-linea-pelear
    con refuge:50 -> refuge:16 en su sitio (como F50) y nostuck. pelear = control con la casa; casa = RETIRARSE desde
    el segundo 0, retreat:flee, refuge:house; casa-compacta = casa + retreat:compact (huida en TESTUDO). n = 30."""
    d = load()
    by = {s['id']: s for s in d['scenarios']}
    flee = ['instruction:RETIRARSE', 'instructionAt:0', 'retreat:flee', 'refuge:house']
    new, presets = [], {}
    for name in ('vindicadores80', 'devastadores8'):
        base = by[f't52-escape-{name}-linea-pelear']
        for arm, extra in (('pelear', ['refuge:house']), ('casa', flee), ('casa-compacta', flee + ['retreat:compact'])):
            sc = json.loads(json.dumps(base))
            sc['id'] = f't92-{name}-{arm}'
            sc['tags'] = ['refuge:16' if t == 'refuge:50' else t for t in sc['tags']] + ['nostuck'] + extra
            sc['note'] = f'F54: copia de {base["id"]} con refuge:16, nostuck, ' + ', '.join(extra)
            new.append(sc)
            presets.setdefault('t92-refugio-linea', {'scenarios': [], 'seeds': seeds, 'minutes': sc['minutes']})['scenarios'].append(sc['id'])
    replace_round(d, 't92-', new, presets); save(d)
    print(','.join(presets))


def gen_escala93(seeds=5):
    """F55 (O8): coste a escala hoy. Copias exactas de scale-50/100/200/400 (preset scale), 5 semillas, sin mods."""
    d = load()
    rows = [(f't93-scale-{n}', f'scale-{n}', {}) for n in (50, 100, 200, 400)]
    presets = _copies(d, rows, 't93-', 'F55', lambda sid: 't93-escala', seeds)
    save(d); print(','.join(presets))


def gen_escala94(seeds=5):
    """F56 (O8): coste completo a escala (FIELD_MANUAL, BREACH, SCAN medidos; total del servidor srv_ms). Copias exactas
    de scale-50/100/200/400 y dos de composición a 400 (mirror: el rol vale para los dos bandos). 5 semillas, sin mods.
    Nota: F55 trajo 7-10 corridas por tamaño porque SimRunner reintenta una vez cada corrida "sin enfrentamiento" con
    semilla + 7777 (las 168xx) y sprint añade una corrida de control; aquí pasará igual (se cuentan aparte)."""
    d = load()
    rows = [(f't94-scale-{n}', f'scale-{n}', {}) for n in (50, 100, 200, 400)]
    rows += [('t94-400-melee', 'scale-400', {'role': 'melee'}), ('t94-400-arcos', 'scale-400', {'role': 'archer'})]
    presets = _copies(d, rows, 't94-', 'F56', lambda sid: 't94-escala-total', seeds)
    save(d); print(','.join(presets))


def gen_escala96(seeds=5):
    """F58 (O8): tiempo propio de Rally (sin anidados), percentiles en régimen (sin los 100 primeros ticks), ticks con
    recolección de la JVM aparte, y la curva hasta 800. Copias exactas de los t94-* / scale-*; sinmoral = off:morale.
    Dos presets porque arena:160 agranda la arena de todo el lote: t96-escala-regimen (arena por defecto) y
    t96-escala-800 (scale-800 = 400 por bando, mixed; y scale-400 en la misma arena). 5 semillas, sin mods, seed 9400."""
    d = load()
    reg = lambda sid: 't96-escala-regimen'
    rows = [(f't96-scale-{n}', f'scale-{n}', {}) for n in (100, 200, 400)]
    rows += [('t96-400-arcos', 't94-400-arcos', {}), ('t96-400-melee', 't94-400-melee', {}),
             ('t96-400-sinmoral', 'scale-400', {'tags': ['off:morale']}),
             ('t96-scale-800', 'scale-400', {'blue': 400, 'tags': ['arena:160']}),
             ('t96-400-arena160', 'scale-400', {'tags': ['arena:160']})]
    presets = _copies(d, rows, 't96-', 'F58',
                      lambda sid: 't96-escala-800' if sid in ('t96-scale-800', 't96-400-arena160') else 't96-escala-regimen',
                      seeds)
    save(d); print(','.join(presets))


def gen_fuego95(seeds=30):
    """F57 (O1): ¿spreadFire (por defecto desde F48) vale fuera del llano? Por contexto, -defecto = copia sin etiqueta
    fire:* (el valor del juego) y -sync = fire:sync. Dos presets porque los minutos/arena van por preset: terreno
    (aldea, bosque; 3 min) y horda (vindicadores 80, devastadores 8; arena:160, 2 min). n = 30, mods=bestiario."""
    d = load()
    by = {s['id']: s for s in d['scenarios']}
    ctx = (('aldea', 't91-pax16-defecto', 't95-fuego-terreno'),
           ('bosque', 't48-distancia-pillagerpax32-forest-arco-regla', 't95-fuego-terreno'),
           ('horda', 't52-escape-vindicadores80-arco-pelear', 't95-fuego-horda'),
           ('vida', 't52-escape-devastadores8-arco-pelear', 't95-fuego-horda'))
    new, presets = [], {}
    for name, base_id, preset in ctx:
        base = by[base_id]
        for arm, extra in (('defecto', []), ('sync', ['fire:sync'])):
            sc = json.loads(json.dumps(base))
            sc['id'] = f't95-{name}-{arm}'
            sc['tags'] = [t for t in sc['tags'] if not t.startswith('fire:')] + extra
            sc['note'] = f'F57: copia de {base_id} sin fire:*' + (', fire:sync' if extra else ' (valor del juego: spreadFire)')
            new.append(sc)
            presets.setdefault(preset, {'scenarios': [], 'seeds': seeds, 'minutes': sc['minutes']})['scenarios'].append(sc['id'])
    replace_round(d, 't95-', new, presets); save(d)
    print(','.join(presets))


def gen_refugio97(seeds=30):
    """F59 (O3): 2.º lote de la casa contra vindicadores y la orden dada tarde. Copias exactas de t92-vindicadores80-
    pelear / -casa; casa-tarde = casa con instructionAt:0 -> instructionAt:15 EN SU SITIO (SimScenario.tag devuelve la
    primera; la unidad son segundos: SimExtras.sample cuenta una muestra por segundo). n = 30, seed 9500, bestiario."""
    d = load()
    by = {s['id']: s for s in d['scenarios']}
    new, presets = [], {}
    for arm, base_id, late in (('pelear', 't92-vindicadores80-pelear', False), ('casa', 't92-vindicadores80-casa', False),
                               ('casa-tarde', 't92-vindicadores80-casa', True)):
        sc = json.loads(json.dumps(by[base_id]))
        sc['id'] = f't97-{arm}'
        if late:
            sc['tags'] = ['instructionAt:15' if t == 'instructionAt:0' else t for t in sc['tags']]
        sc['note'] = f'F59: copia de {base_id}' + (', instructionAt:15 (segundos)' if late else ' (control)')
        new.append(sc)
        presets.setdefault('t97-refugio-conf', {'scenarios': [], 'seeds': seeds, 'minutes': sc['minutes']})['scenarios'].append(sc['id'])
    replace_round(d, 't97-', new, presets); save(d)
    print(','.join(presets))


def gen_escala98(seeds=5):
    """F60 (O8): el informe de fuerza repartido en la rebanada (perf:sliceReports -> SituationReports.simSliceReports).
    Copias exactas de t96-scale-400 / t96-scale-800; -on = + perf:sliceReports. Dos presets (arena:160 va por lote).
    5 semillas, sin mods, seed 9500."""
    d = load()
    on = {'tags': ['perf:sliceReports']}
    rows = [('t98-400-off', 't96-scale-400', {}), ('t98-400-on', 't96-scale-400', on),
            ('t98-800-off', 't96-scale-800', {}), ('t98-800-on', 't96-scale-800', on)]
    presets = _copies(d, rows, 't98-', 'F60',
                      lambda sid: 't98-escala-rebanada-800' if '-800-' in sid else 't98-escala-rebanada', seeds)
    save(d); print(','.join(presets))


def gen_escala99(seeds=5):
    """F61 (O8): la experiencia de unidad en una pasada por época (perf:unitXp -> Leadership.simOnePassUnitXp).
    Copias exactas de t98-800-off (arena:160); -on = + perf:unitXp. Un preset, 5 semillas, sin mods, seed 9600."""
    d = load()
    rows = [('t99-800-off', 't98-800-off', {}), ('t99-800-on', 't98-800-off', {'tags': ['perf:unitXp']})]
    presets = _copies(d, rows, 't99-', 'F61', lambda sid: 't99-escala-unitxp', seeds)
    save(d); print(','.join(presets))


def gen_escala100(seeds=10):
    """F62 (O8): índice de unidades por tick + cohesión con la lista en mano (perf:unitIndex -> UnitOrders.simUnitIndex).
    Copias exactas de t99-800-off (arena:160); -xp = + perf:unitXp, -xp-idx = + perf:unitXp + perf:unitIndex.
    Un preset, 10 semillas, sin mods, seed 9700."""
    d = load()
    rows = [('t100-800-off', 't99-800-off', {}), ('t100-800-xp', 't99-800-off', {'tags': ['perf:unitXp']}),
            ('t100-800-xp-idx', 't99-800-off', {'tags': ['perf:unitXp', 'perf:unitIndex']})]
    presets = _copies(d, rows, 't100-', 'F62', lambda sid: 't100-escala-indice', seeds)
    save(d); print(','.join(presets))


def gen_refugio101(seeds=30):
    """F63 (laboratorio de retirada): ¿hasta qué segundo sirve la orden de ir a la casa? Copias exactas de t97-pelear y
    t97-casa; casa-k = casa con instructionAt:0 -> instructionAt:k EN SU SITIO (segundos), k = 0, 3, 6, 9, 12.
    n = 30, seed 9700, mods=bestiario."""
    d = load()
    by = {s['id']: s for s in d['scenarios']}
    new, presets = [], {}
    arms = [('pelear', 't97-pelear', None)] + [(f'casa-{k}', 't97-casa', k) for k in (0, 3, 6, 9, 12)]
    for arm, base_id, k in arms:
        sc = json.loads(json.dumps(by[base_id]))
        sc['id'] = f't101-{arm}'
        if k is not None:
            assert 'instructionAt:0' in sc['tags'], base_id
            sc['tags'] = [f'instructionAt:{k}' if t == 'instructionAt:0' else t for t in sc['tags']]
        sc['note'] = f'F63: copia de {base_id}' + (f', instructionAt:{k} (segundos)' if k is not None else ' (control)')
        new.append(sc)
        presets.setdefault('t101-refugio-curva', {'scenarios': [], 'seeds': seeds, 'minutes': sc['minutes']})['scenarios'].append(sc['id'])
    replace_round(d, 't101-', new, presets); save(d)
    print(','.join(presets))


def gen_aldea102(seeds=30):
    """F64 (O2, precio de la brecha): el escudo cruza primero (door:meleeFirst -> Breach.simMeleeFirst). Copias exactas
    de t91-pax16-defecto y t91-pax32-escudos8-defecto con blue.role = mix:shield=4,archer=4 (la de pax32 ya lleva
    comp:linea); -escudoprimero = + door:meleeFirst. n = 30, seed 9700, mods=bestiario."""
    d = load()
    by = {s['id']: s for s in d['scenarios']}
    new, presets = [], {}
    for red, base_id in (('pax16', 't91-pax16-defecto'), ('pax32', 't91-pax32-escudos8-defecto')):
        for arm, extra in (('defecto', []), ('escudoprimero', ['door:meleeFirst'])):
            sc = json.loads(json.dumps(by[base_id]))
            sc['id'] = f't102-{red}-{arm}'
            sc['blue']['role'] = 'mix:shield=4,archer=4'
            sc['tags'] = sc['tags'] + extra
            sc['note'] = f'F64: copia de {base_id}, linea mixta' + (', door:meleeFirst' if extra else ' (control)')
            new.append(sc)
            presets.setdefault('t102-aldea-cruce', {'scenarios': [], 'seeds': seeds, 'minutes': sc['minutes']})['scenarios'].append(sc['id'])
    replace_round(d, 't102-', new, presets); save(d)
    print(','.join(presets))


def gen_bosque103(seeds=30):
    """F65 (laboratorio de armas): qué composición vale contra Pax en bosque. Copias exactas de t95-bosque-defecto;
    solo cambian blue.role, la etiqueta comp:* y red.count. arco = control (archer, comp:arco), linea =
    mix:shield=4,archer=4 (comp:linea), escudo = shield (comp:escudo); contra 32 y 16 Pax. n = 30, seed 9700, bestiario."""
    d = load()
    base = {s['id']: s for s in d['scenarios']}['t95-bosque-defecto']
    new, presets = [], {}
    for count in (32, 16):
        for arm, role, comp in (('arco', 'archer', 'comp:arco'), ('linea', 'mix:shield=4,archer=4', 'comp:linea'),
                                ('escudo', 'shield', 'comp:escudo')):
            sc = json.loads(json.dumps(base))
            sc['id'] = f't103-pax{count}-{arm}'
            sc['blue']['role'] = role
            sc['red']['count'] = count
            sc['tags'] = [comp if t.startswith('comp:') else t for t in sc['tags']]
            sc['note'] = f'F65: copia de t95-bosque-defecto, {role} contra {count} Pax'
            new.append(sc)
            presets.setdefault('t103-bosque-comp', {'scenarios': [], 'seeds': seeds, 'minutes': sc['minutes']})['scenarios'].append(sc['id'])
    replace_round(d, 't103-', new, presets); save(d)
    print(','.join(presets))


def gen_aldea104(seeds=30):
    """F66 (O2, precio de la brecha): dónde y cómo muere el arquero (solo medida). Copias exactas de t102-pax16-defecto
    y t102-pax32-defecto + probe:deaths (SimExtras.deathProbe: dk/dlos/din/ddoor/dmove/dsec/dn por papel R|M).
    n = 30, seed 9800, mods=bestiario."""
    d = load()
    by = {s['id']: s for s in d['scenarios']}
    new, presets = [], {}
    for red in ('pax16', 'pax32'):
        sc = json.loads(json.dumps(by[f't102-{red}-defecto']))
        sc['id'] = f't104-{red}-defecto'
        sc['tags'] = sc['tags'] + ['probe:deaths']
        sc['note'] = f'F66: copia de t102-{red}-defecto + probe:deaths'
        new.append(sc)
        presets.setdefault('t104-aldea-muertes', {'scenarios': [], 'seeds': seeds, 'minutes': sc['minutes']})['scenarios'].append(sc['id'])
    replace_round(d, 't104-', new, presets); save(d)
    print(','.join(presets))


def gen_bosque105(seeds=30):
    """F67 (laboratorio de armas): techo de la línea mixta en bosque y proporción de escudos. Copias exactas de
    t103-pax32-linea; solo cambian blue.role, red.count y comp:*. n = 30, seed 9800, mods=bestiario."""
    d = load()
    base = {s['id']: s for s in d['scenarios']}['t103-pax32-linea']
    new, presets = [], {}
    for count, arm, role in ((32, 'linea', 'mix:shield=4,archer=4'), (32, '2e6a', 'mix:shield=2,archer=6'),
                             (32, '6e2a', 'mix:shield=6,archer=2'), (48, 'linea', 'mix:shield=4,archer=4'),
                             (48, '6e2a', 'mix:shield=6,archer=2'), (64, 'linea', 'mix:shield=4,archer=4')):
        sc = json.loads(json.dumps(base))
        sc['id'] = f't105-pax{count}-{arm}'
        sc['blue']['role'] = role
        sc['red']['count'] = count
        comp = 'comp:linea' if arm == 'linea' else f'comp:{arm}'
        sc['tags'] = [comp if t.startswith('comp:') else t for t in sc['tags']]
        sc['note'] = f'F67: copia de t103-pax32-linea, {role} contra {count} Pax'
        new.append(sc)
        presets.setdefault('t105-bosque-techo', {'scenarios': [], 'seeds': seeds, 'minutes': sc['minutes']})['scenarios'].append(sc['id'])
    replace_round(d, 't105-', new, presets); save(d)
    print(','.join(presets))


def gen_aldea106(seeds=30):
    """F68 (O2, precio de la brecha): el arquero recula hacia detrás del escudo propio más cercano (standoff:cover ->
    FieldManual.simCover). Copias exactas de t104-pax16-defecto y t104-pax32-defecto (con probe:deaths);
    -cobertura = + standoff:cover. n = 30, seed 9900, mods=bestiario."""
    d = load()
    by = {s['id']: s for s in d['scenarios']}
    new, presets = [], {}
    for red in ('pax16', 'pax32'):
        for arm, extra in (('defecto', []), ('cobertura', ['standoff:cover'])):
            sc = json.loads(json.dumps(by[f't104-{red}-defecto']))
            sc['id'] = f't106-{red}-{arm}'
            sc['tags'] = sc['tags'] + extra
            sc['note'] = f'F68: copia de t104-{red}-defecto' + (', standoff:cover' if extra else ' (control)')
            new.append(sc)
            presets.setdefault('t106-aldea-cobertura', {'scenarios': [], 'seeds': seeds, 'minutes': sc['minutes']})['scenarios'].append(sc['id'])
    replace_round(d, 't106-', new, presets); save(d)
    print(','.join(presets))


def gen_refugio107(seeds=30):
    """F70 (laboratorio de retirada): ¿manda el reloj o el margen de distancia? Copias exactas de t101-pelear y
    t101-casa-0; solo cambian distance y, en su sitio, instructionAt:k. distance 60 cabe en arena:160 (azul en
    cz+30, refugio a 16 detrás, mitad 80). n = 30, seed 10000, mods=bestiario."""
    d = load()
    by = {s['id']: s for s in d['scenarios']}
    new, presets = [], {}
    arms = [(30, 'pelear', None), (30, 'casa', 0), (45, 'pelear', None), (45, 'casa', 0), (45, 'casa', 3),
            (45, 'casa', 6), (60, 'casa', 6), (60, 'casa', 9)]
    for dist, kind, k in arms:
        base_id = 't101-pelear' if k is None else 't101-casa-0'
        sc = json.loads(json.dumps(by[base_id]))
        sc['id'] = f't107-d{dist}-{kind}' + ('' if k is None else f'-{k}')
        sc['distance'] = dist
        if k is not None:
            assert 'instructionAt:0' in sc['tags'], base_id
            sc['tags'] = [f'instructionAt:{k}' if t == 'instructionAt:0' else t for t in sc['tags']]
        sc['note'] = f'F70: copia de {base_id}, distance {dist}' + ('' if k is None else f', instructionAt:{k}')
        new.append(sc)
        presets.setdefault('t107-refugio-margen', {'scenarios': [], 'seeds': seeds, 'minutes': sc['minutes']})['scenarios'].append(sc['id'])
    replace_round(d, 't107-', new, presets); save(d)
    print(','.join(presets))


def gen_cobertura108(seeds=30):
    """F71 (O2 + auditoría de valor por defecto): 2.º lote de standoff:cover en la aldea y pareja con/sin regla en
    bosque y llano. Aldea: copias exactas de los cuatro t106-* (solo cambia el id). Bosque: copia de t105-pax48-linea
    + probe:deaths. Llano: copia de t48-distancia-pillagerpax32-flat-linea-regla + probe:deaths. -cobertura añade
    standoff:cover. n = 30, seed 10100, mods=bestiario."""
    d = load()
    by = {s['id']: s for s in d['scenarios']}
    new, presets = [], {}
    pairs = []
    for red in ('pax16', 'pax32'):
        for arm in ('defecto', 'cobertura'):
            pairs.append((f't106-{red}-{arm}', f't108-aldea-{red}-{arm}', [], f'F71: copia exacta de t106-{red}-{arm}'))
    for ctx, base_id in (('bosque-pax48', 't105-pax48-linea'),
                         ('llano-pax32', 't48-distancia-pillagerpax32-flat-linea-regla')):
        pairs.append((base_id, f't108-{ctx}-defecto', ['probe:deaths'], f'F71: copia de {base_id} + probe:deaths (control)'))
        pairs.append((base_id, f't108-{ctx}-cobertura', ['probe:deaths', 'standoff:cover'],
                      f'F71: copia de {base_id} + probe:deaths, standoff:cover'))
    for base_id, new_id, extra, note in pairs:
        sc = json.loads(json.dumps(by[base_id]))
        sc['id'] = new_id
        sc['tags'] = sc['tags'] + [t for t in extra if t not in sc['tags']]
        sc['note'] = note
        new.append(sc)
        presets.setdefault('t108-cobertura-conf', {'scenarios': [], 'seeds': seeds, 'minutes': sc['minutes']})['scenarios'].append(sc['id'])
    replace_round(d, 't108-', new, presets); save(d)
    print(','.join(presets))


def gen_escala109(seeds=30):
    """F72 (O8): 2.º lote del índice de unidades a 800. Copias exactas de t100-800-off y t100-800-xp-idx (solo cambia el
    id). Un preset, 30 semillas, sin mods, seed 10200."""
    d = load()
    rows = [('t109-800-off', 't100-800-off', {}), ('t109-800-xp-idx', 't100-800-xp-idx', {})]
    presets = _copies(d, rows, 't109-', 'F72', lambda sid: 't109-escala-conf', seeds)
    save(d); print(','.join(presets))


def gen_aldea110(seeds=30):
    """F73 (O2): la aldea tal como corre en el juego con standOffCover por defecto. Copia de t95-aldea-defecto QUITANDO
    standoff:losRelease (no es valor del juego) y añadiendo probe:deaths; -sincob = + off:cover. n = 30, seed 10300,
    mods=bestiario."""
    d = load()
    by = {s['id']: s for s in d['scenarios']}
    new, presets = [], {}
    # F82: standOffCover is false by default now - both arms pinned by tag so the preset keeps its meaning
    for sid, extra in (('t110-aldea-juego', ['standoff:cover']), ('t110-aldea-juego-sincob', ['off:cover'])):
        sc = json.loads(json.dumps(by['t95-aldea-defecto']))
        assert 'standoff:losRelease' in sc['tags']
        sc['id'] = sid
        sc['tags'] = [t for t in sc['tags'] if t != 'standoff:losRelease'] + ['probe:deaths'] + extra
        sc['note'] = 'F73: copia de t95-aldea-defecto sin standoff:losRelease, + probe:deaths, ' + extra[0]
        new.append(sc)
        presets.setdefault('t110-aldea-juego', {'scenarios': [], 'seeds': seeds, 'minutes': sc['minutes']})['scenarios'].append(sid)
    replace_round(d, 't110-', new, presets); save(d)
    print(','.join(presets))


def gen_multiplicador111(seeds=30):
    """F75 (O7): multiplicador de fuerza, linea base. Rally = 8 guardias; sin Rally (tag norally: Guard Villagers nativo,
    sin duenio, sin reglas ni orden ni fuego coordinado) = 8, 12, 16, 24 con el mismo equipo. Horda = copia de
    t103-pax32-linea (4 escudos + 4 arcos contra 32 Pax) + arena:160; pistoleros = copia de t88-pax48 (8 arcos contra
    48 Pax). Un preset, n = 30, mods=bestiario, seed 10400."""
    d = load()
    by = {s['id']: s for s in d['scenarios']}
    new, presets = [], {'t111-multiplicador': {'scenarios': [], 'seeds': seeds, 'minutes': 3}}
    for name, base, extra in (('horda', 't103-pax32-linea', ['arena:160']), ('pistoleros', 't88-pax48', [])):
        for label, n, nr in (('rally8', 8, False), ('sin8', 8, True), ('sin12', 12, True), ('sin16', 16, True), ('sin24', 24, True)):
            sc = json.loads(json.dumps(by[base]))
            sc['id'] = f't111-{name}-{label}'
            sc['tags'] = [t for t in sc['tags'] if t not in extra] + extra + (['norally'] if nr else [])
            sc['blue']['count'] = n
            sc['note'] = f'F75: copia de {base}' + (f', norally, {n} guardias' if nr else ' (Rally, 8)')
            new.append(sc)
            presets['t111-multiplicador']['scenarios'].append(sc['id'])
            presets['t111-multiplicador']['minutes'] = max(presets['t111-multiplicador']['minutes'], sc['minutes'])
    replace_round(d, 't111-', new, presets); save(d)
    print(','.join(presets))


def gen_gvdispara112(seeds=30):
    """F76 (O7): preset t112-gv-dispara = pistoleros y horda con norally 8/16 + Rally 8 (control) y pistoleros norally 32/48.
    Telemetria extra.blueProj (todo proyectil azul) y extra.guardSeconds."""
    d = load()
    by = {s['id']: s for s in d['scenarios']}
    new, presets = [], {'t112-gv-dispara': {'scenarios': [], 'seeds': seeds, 'minutes': 3}}
    for name, base, extra in (('horda', 't103-pax32-linea', ['arena:160']), ('pistoleros', 't88-pax48', [])):
        arms = [('rally8', 8, False), ('sin8', 8, True), ('sin16', 16, True)]
        if name == 'pistoleros':
            arms += [('sin32', 32, True), ('sin48', 48, True)]
        for label, n, nr in arms:
            sc = json.loads(json.dumps(by[base]))
            sc['id'] = f't112-{name}-{label}'
            sc['tags'] = [t for t in sc['tags'] if t not in extra] + extra + (['norally'] if nr else [])
            sc['blue']['count'] = n
            sc['note'] = f'F76: copia de {base}' + (f', norally, {n} guardias' if nr else ' (Rally, 8)')
            new.append(sc)
            presets['t112-gv-dispara']['scenarios'].append(sc['id'])
            presets['t112-gv-dispara']['minutes'] = max(presets['t112-gv-dispara']['minutes'], sc['minutes'])
    replace_round(d, 't112-', new, presets); save(d)
    print(','.join(presets))


def gen_gvimpactos113(seeds=30):
    """F77 (O7): preset t113-gv-impactos = horda (t112-horda-*) Rally 8, norally 8 y 16, con extra.blueProjHit / blueProjDmg.
    Copias de los escenarios de F76 (mismas semillas pareadas)."""
    d = load()
    by = {s['id']: s for s in d['scenarios']}
    new, presets = [], {'t113-gv-impactos': {'scenarios': [], 'seeds': seeds, 'minutes': 3}}
    for label in ('rally8', 'sin8', 'sin16'):
        sc = json.loads(json.dumps(by[f't112-horda-{label}']))
        sc['id'] = f't113-horda-{label}'
        sc['note'] = f'F77: copia de t112-horda-{label}'
        new.append(sc)
        presets['t113-gv-impactos']['scenarios'].append(sc['id'])
    replace_round(d, 't113-', new, presets); save(d)
    print(','.join(presets))


def gen_base400_114(seeds=10):
    """F78 (O9): linea base 400 v 400: preset t114-base400 reutiliza t96-scale-400 / t96-400-melee / t96-400-arcos (n = 10)."""
    d = load()
    d['presets']['t114-base400'] = {'scenarios': ['t96-scale-400', 't96-400-melee', 't96-400-arcos'], 'seeds': seeds, 'minutes': 3}
    save(d)
    print('t114-base400')


def gen_ablacion115(seeds=30):
    """F79 (O7): ablacion de la capa de grupo. Copias exactas de t111-horda-rally8 y t111-pistoleros-rally8 (F75);
    -indiv8 = + off:group (sin capitan, formacion, ordenes de unidad, fuerza, ventanas de fuego ni blanco compartido;
    todo lo individual sigue). Un preset t115-ablacion, n = 30, mods=bestiario, seed 10600. Sin espejo (ver F79)."""
    d = load()
    rows = []
    for name in ('horda', 'pistoleros'):
        rows += [(f't115-{name}-rally8', f't111-{name}-rally8', {}),
                 (f't115-{name}-indiv8', f't111-{name}-rally8', {'tags': ['off:group']})]
    presets = _copies(d, rows, 't115-', 'F79', lambda sid: 't115-ablacion', seeds)
    save(d); print(','.join(presets))


def gen_tope116():
    """F80 (O9): barrido del tope tecnico. Copias de t96-scale-800 (espejo, mixed, rango 1) con blue.count = 400 /
    600 / 800 y arena:160 / 224 / 256 (+ maxSide:N por encima de 400, el tope por defecto de la sim). Un preset por tamanio (t116-tope-400 n = 10, -600 n = 8, -800 n = 5), una
    llamada a build.yml por preset. mods=bestiario, seed 10700."""
    d = load()
    by = {s['id']: s for s in d['scenarios']}
    new, presets = [], {}
    for n, arena, seeds in ((400, 160, 10), (600, 224, 8), (800, 256, 5)):
        sc = json.loads(json.dumps(by['t96-scale-800']))
        sc['id'] = f't116-tope-{n}'
        sc['tags'] = [t for t in sc['tags'] if not t.startswith('arena:')] + [f'arena:{arena}'] + \
            ([f'maxSide:{n}'] if n > 400 else [])  # maxUnitsPerSide = 400 por defecto: sin esto 600/800 se recortan
        sc['blue']['count'] = n
        sc['note'] = f'F80: copia de t96-scale-800, {n} v {n}, arena:{arena}'
        new.append(sc)
        presets[sc['id']] = {'scenarios': [sc['id']], 'seeds': seeds, 'minutes': sc['minutes']}
    replace_round(d, 't116-', new, presets); save(d)
    print(','.join(presets))


def gen_piezas117(seeds=30):
    """F81 (O7): la capa de grupo pieza a pieza. Copias de t111-horda-rally8 y t111-pistoleros-rally8 (como F79): rally8,
    indiv8 (off:group), sinfuego (off:group:fire), sinblanco (off:group:target), sinforma (off:group:form). Un preset
    t117-piezas, n = 30, mods=bestiario, seed 10800."""
    d = load()
    rows = []
    for name in ('horda', 'pistoleros'):
        for label, tag in (('rally8', None), ('indiv8', 'off:group'), ('sinfuego', 'off:group:fire'),
                           ('sinblanco', 'off:group:target'), ('sinforma', 'off:group:form')):
            rows.append((f't117-{name}-{label}', f't111-{name}-rally8', {'tags': [tag]} if tag else {}))
    presets = _copies(d, rows, 't117-', 'F81', lambda sid: 't117-piezas', seeds)
    save(d); print(','.join(presets))

def gen_retirada118(seeds=30):
    """F69 (O3, laboratorio de retirada): disparador anticipado por margen de distancia. Copias de t107-d30/d45-pelear y
    -casa-0; dano = casa con instructionAt:0 -> retreat:hurt; early-K = casa con instructionAt:0 -> retreat:early:K.
    Control de falsa alarma en d30 contra 16 vindicadores: pelear y early-4. Un preset t118-retirada-disparador,
    n = 30, mods=bestiario, seed 11000."""
    d = load()
    by = {s['id']: s for s in d['scenarios']}
    new, presets = [], {}
    arms = []
    for dist in (30, 45):
        arms += [(f'd{dist}-pelear', f't107-d{dist}-pelear', None, None), (f'd{dist}-casa-0', f't107-d{dist}-casa-0', None, None),
                 (f'd{dist}-dano', f't107-d{dist}-casa-0', 'retreat:hurt', None),
                 (f'd{dist}-early-4', f't107-d{dist}-casa-0', 'retreat:early:4', None),
                 (f'd{dist}-early-2', f't107-d{dist}-casa-0', 'retreat:early:2', None)]
    # r16-espera: instruida pero sin orden nunca (instructionAt:9999), como early-4 mientras no dispara: separa la falsa
    # alarma de no ser conducida al enemigo (los brazos instruidos no los conduce la sim, igual que casa-k en F63/F70).
    arms += [('d30-r16-pelear', 't107-d30-pelear', None, 16), ('d30-r16-espera', 't107-d30-casa-0', 'instructionAt:9999', 16),
             ('d30-r16-early-4', 't107-d30-casa-0', 'retreat:early:4', 16)]
    for arm, base_id, trig, red in arms:
        sc = json.loads(json.dumps(by[base_id]))
        sc['id'] = f't118-{arm}'
        if trig:
            assert 'instructionAt:0' in sc['tags'], base_id
            sc['tags'] = [trig if t == 'instructionAt:0' else t for t in sc['tags']]
        if red:
            sc['red']['count'] = red
        sc['note'] = f'F69: copia de {base_id}' + (f', instructionAt:0 -> {trig}' if trig else '') + (f', red {red}' if red else '')
        new.append(sc)
        presets.setdefault('t118-retirada-disparador', {'scenarios': [], 'seeds': seeds, 'minutes': sc['minutes']})['scenarios'].append(sc['id'])
    replace_round(d, 't118-', new, presets); save(d)
    print(','.join(presets))

def gen_foco119(seeds=30):
    """F84 (O7): foco compartido contra pistoleros (2.o lote, se suma a F81) y la capa de grupo contra cuerpo a cuerpo
    real. (a) copias de t111-pistoleros-rally8: rally8, sinblanco (off:group:target), sinfuego (off:group:fire).
    (b) copias de t111-horda-rally8 con red.kind pillager -> vindicator, threat:pillager -> threat:vindicator y SIN
    redHand:scguns:pax (si no, cada vindicador cambiaria el hacha por una pistola): vind-rally8, vind-indiv8 (off:group),
    vind-sinfuego, vind-sinblanco. Mismo numero de rojos (32). Un preset t119-foco, n = 30, mods=bestiario, seed 11100."""
    d = load()
    by = {s['id']: s for s in d['scenarios']}
    new, presets = [], {'t119-foco': {'scenarios': [], 'seeds': seeds, 'minutes': 3}}
    arms = [('pistoleros-rally8', 't111-pistoleros-rally8', None, False),
            ('pistoleros-sinblanco', 't111-pistoleros-rally8', 'off:group:target', False),
            ('pistoleros-sinfuego', 't111-pistoleros-rally8', 'off:group:fire', False),
            ('vind-rally8', 't111-horda-rally8', None, True), ('vind-indiv8', 't111-horda-rally8', 'off:group', True),
            ('vind-sinfuego', 't111-horda-rally8', 'off:group:fire', True),
            ('vind-sinblanco', 't111-horda-rally8', 'off:group:target', True)]
    for arm, base, tag, vind in arms:
        sc = json.loads(json.dumps(by[base]))
        sc['id'] = f't119-{arm}'
        if vind:
            assert sc['red']['kind'] == 'pillager' and 'threat:pillager' in sc['tags'], base
            sc['red']['kind'] = 'vindicator'
            sc['tags'] = ['threat:vindicator' if t == 'threat:pillager' else t for t in sc['tags'] if not t.startswith('redHand:')]
        if tag:
            sc['tags'].append(tag)
        sc['note'] = f'F84: copia de {base}' + (', rojo vindicator sin redHand' if vind else '') + (f', {tag}' if tag else ' (control)')
        new.append(sc)
        presets['t119-foco']['scenarios'].append(sc['id'])
        presets['t119-foco']['minutes'] = max(presets['t119-foco']['minutes'], sc['minutes'])
    replace_round(d, 't119-', new, presets); save(d)
    print(','.join(presets))


def gen_espejo120(seeds=30):
    """F85 (O7): espejo 8 v 8, Rally completo contra Rally individual. Copias de mirror-archer-20 (arcos) y
    mirror-melee-50 (cuerpo a cuerpo) con blue.count = 8, rango 1 y off:morale (como F79/F81; el rojo espejo copia al
    azul). Brazos: ambos (control), rojoindiv (off:group:red), azulindiv (off:group). Un preset t120-espejo, n = 30,
    mods=bestiario, seed 11200."""
    d = load()
    by = {s['id']: s for s in d['scenarios']}
    new, presets = [], {'t120-espejo': {'scenarios': [], 'seeds': seeds, 'minutes': 3}}
    for comp, base in (('arco', 'mirror-archer-20'), ('melee', 'mirror-melee-50')):
        for arm, tag in (('ambos', None), ('rojoindiv', 'off:group:red'), ('azulindiv', 'off:group')):
            sc = json.loads(json.dumps(by[base]))
            sc['id'] = f't120-espejo-{comp}-{arm}'
            sc['blue']['count'] = 8
            sc['blue']['rank'] = 1
            sc['minutes'] = 3
            sc['tags'] = list(sc.get('tags', [])) + ['off:morale'] + ([tag] if tag else [])
            sc['note'] = f'F85: copia de {base}, 8 v 8, rango 1, off:morale' + (f', {tag}' if tag else ' (control)')
            new.append(sc)
            presets['t120-espejo']['scenarios'].append(sc['id'])
    replace_round(d, 't120-', new, presets); save(d)
    print(','.join(presets))

def gen_espejo126(seeds=60):
    """F92 (O7): espejo 2 x 2 de arqueros con vida a los 20 s. Copia de gen_espejo120 solo arco (mirror-archer-20,
    8 v 8, rango 1, off:morale) con cuatro celdas: ambos, rojoindiv (off:group:red), azulindiv (off:group), ninguno
    (off:group + off:group:red); probe:hp en todas. Un preset t126-espejo2x2, n = 60, mods=bestiario, seed 11700."""
    d = load()
    by = {s['id']: s for s in d['scenarios']}
    new, presets = [], {'t126-espejo2x2': {'scenarios': [], 'seeds': seeds, 'minutes': 3}}
    base = 'mirror-archer-20'
    for arm, tags in (('ambos', []), ('rojoindiv', ['off:group:red']), ('azulindiv', ['off:group']),
                      ('ninguno', ['off:group', 'off:group:red'])):
        sc = json.loads(json.dumps(by[base]))
        sc['id'] = f't126-espejo2x2-arco-{arm}'
        sc['blue']['count'] = 8
        sc['blue']['rank'] = 1
        sc['minutes'] = 3
        sc['tags'] = list(sc.get('tags', [])) + ['off:morale', 'probe:hp'] + tags
        sc['note'] = f'F92: copia de {base}, 8 v 8, rango 1, off:morale, probe:hp' + (', ' + ' + '.join(tags) if tags else ' (control)')
        new.append(sc)
        presets['t126-espejo2x2']['scenarios'].append(sc['id'])
    replace_round(d, 't126-', new, presets); save(d)
    print(','.join(presets))

def gen_persecucion121(seeds=30):
    """F87 (O4): caballeria con espada en persecucion. Azul 8 espadas rango 1 (a pie: melee; montado: mix:melee@m=1)
    contra rojo 12 guardias cuerpo a cuerpo rango 0 a 20 bloques, llano, 2 min, off:morale. Con fuga: redflee:10; sin
    fuga: fleemeasure:10 (solo mide desde los 10 s). Medidas: redKilledAfterFlee, redFledAlive, fleeDist0/Max,
    fleeChaseMax/Sec. Un preset t121-persecucion, n = 30, mods=bestiario, seed 11300."""
    d = load()
    new, presets = [], {'t121-persecucion': {'scenarios': [], 'seeds': seeds, 'minutes': 2}}
    for arm, role, tag in (('pie-fuga', 'melee', 'redflee:10'), ('jinete-fuga', 'mix:melee@m=1', 'redflee:10'),
                           ('pie-sinfuga', 'melee', 'fleemeasure:10'), ('jinete-sinfuga', 'mix:melee@m=1', 'fleemeasure:10')):
        sc = {'id': f't121-persecucion-{arm}', 'terrain': 'flat', 'distance': 20, 'position': 'front', 'minutes': 2,
              'tags': ['torneo', 'pursuit', 'off:morale', tag],
              'note': f'F87: 8 espadas rango 1 {"montadas" if "@m" in role else "a pie"} contra 12 guardias melee rango 0, {tag}',
              'blue': {'kind': 'guard', 'count': 8, 'role': role, 'rank': 1},
              'red': {'kind': 'guard', 'count': 12, 'role': 'melee', 'rank': 0}}
        new.append(sc)
        presets['t121-persecucion']['scenarios'].append(sc['id'])
    replace_round(d, 't121-', new, presets); save(d)
    print(','.join(presets))

def gen_tope122():
    """F88 (O9): el barrido de F80 sin mods (solo Guard Villagers). Copia de gen_tope116 con ids t122-tope-400/600/800
    (arena 160 / 224 / 256, maxSide:N en 600 y 800). Un preset por tamanio (n = 10 / 8 / 5), una llamada a build.yml
    por preset, mods= vacio, seed 10700 (pareada con F80)."""
    d = load()
    by = {s['id']: s for s in d['scenarios']}
    new, presets = [], {}
    for n, arena, seeds in ((400, 160, 10), (600, 224, 8), (800, 256, 5)):
        sc = json.loads(json.dumps(by['t96-scale-800']))
        sc['id'] = f't122-tope-{n}'
        sc['tags'] = [t for t in sc['tags'] if not t.startswith('arena:')] + [f'arena:{arena}'] + \
            ([f'maxSide:{n}'] if n > 400 else [])
        sc['blue']['count'] = n
        sc['note'] = f'F88: copia de t96-scale-800, {n} v {n}, arena:{arena}, sin mods'
        new.append(sc)
        presets[sc['id']] = {'scenarios': [sc['id']], 'seeds': seeds, 'minutes': sc['minutes']}
    replace_round(d, 't122-', new, presets); save(d)
    print(','.join(presets))


def gen_tope131():
    """F97 (O9): tope sin mods entre 200 y 400 por bando. Copia de gen_tope122 con ids t131-tope-300 y t131-tope-350,
    arena 160 (la del 400 de F88; <= 400, sin maxSide). Un preset por tamanio (n = 10), una llamada a build.yml por
    preset, mods= vacio, seed 10700 (pareada con F88)."""
    d = load()
    by = {s['id']: s for s in d['scenarios']}
    new, presets = [], {}
    for n, arena, seeds in ((300, 160, 10), (350, 160, 10)):
        sc = json.loads(json.dumps(by['t96-scale-800']))
        sc['id'] = f't131-tope-{n}'
        sc['tags'] = [t for t in sc['tags'] if not t.startswith('arena:')] + [f'arena:{arena}']
        sc['blue']['count'] = n
        sc['note'] = f'F97: copia de t96-scale-800, {n} v {n}, arena:{arena}, sin mods'
        new.append(sc)
        presets[sc['id']] = {'scenarios': [sc['id']], 'seeds': seeds, 'minutes': sc['minutes']}
    replace_round(d, 't131-', new, presets); save(d)
    print(','.join(presets))


def gen_tope149():
    """F119 (O9): tope CON bestiario a 100/150/200/300 por bando. Copia de gen_tope131 con ids t149-tope-b<n>,
    arena 160, espejo, sin maxSide. Un preset por tamanio (n = 8, 300 v 300 n = 6), una llamada a build.yml por
    preset, mods=bestiario, seed 10700 (pareada con F80/F97)."""
    d = load()
    by = {s['id']: s for s in d['scenarios']}
    new, presets = [], {}
    for n, arena, seeds in ((100, 160, 8), (150, 160, 8), (200, 160, 8), (300, 160, 6)):
        sc = json.loads(json.dumps(by['t96-scale-800']))
        sc['id'] = f't149-tope-b{n}'
        sc['tags'] = [t for t in sc['tags'] if not t.startswith('arena:') and not t.startswith('maxSide:')] + \
            [f'arena:{arena}']
        sc['blue']['count'] = n
        sc['note'] = f'F119: copia de t96-scale-800, {n} v {n}, arena:{arena}, con bestiario'
        new.append(sc)
        presets[sc['id']] = {'scenarios': [sc['id']], 'seeds': seeds, 'minutes': sc['minutes']}
    replace_round(d, 't149-', new, presets); save(d)
    print(','.join(presets))


def gen_persecucion123(seeds=30):
    """F89 (O4): 2.o lote de F87 con un azul que gana el choque. Igual que gen_persecucion121 pero rojo 6 guardias melee
    rango 0 (no 12) y redflee:5 / fleemeasure:5 (no 10). Un preset t123-persecucion, n = 30, mods=bestiario, seed 11400."""
    d = load()
    new, presets = [], {'t123-persecucion': {'scenarios': [], 'seeds': seeds, 'minutes': 2}}
    for arm, role, tag in (('pie-fuga', 'melee', 'redflee:5'), ('jinete-fuga', 'mix:melee@m=1', 'redflee:5'),
                           ('pie-sinfuga', 'melee', 'fleemeasure:5'), ('jinete-sinfuga', 'mix:melee@m=1', 'fleemeasure:5')):
        sc = {'id': f't123-persecucion-{arm}', 'terrain': 'flat', 'distance': 20, 'position': 'front', 'minutes': 2,
              'tags': ['torneo', 'pursuit', 'off:morale', tag],
              'note': f'F89: 8 espadas rango 1 {"montadas" if "@m" in role else "a pie"} contra 6 guardias melee rango 0, {tag}',
              'blue': {'kind': 'guard', 'count': 8, 'role': role, 'rank': 1},
              'red': {'kind': 'guard', 'count': 6, 'role': 'melee', 'rank': 0}}
        new.append(sc)
        presets['t123-persecucion']['scenarios'].append(sc['id'])
    replace_round(d, 't123-', new, presets); save(d)
    print(','.join(presets))


def gen_veterania124(seeds=30):
    """F90: veterano contra novato con el mismo equipo. Copias de los dos controles de F84 (t119-pistoleros-rally8,
    t119-vind-rally8) cambiando SOLO blue.rank (0 / 3 / 5); etiquetas iguales (off:morale), family:fuego -> family:veterania, +
    rank:<n> (rank:/family: solo agrupan el informe). Un preset t124-veterania, n = 30, mods=bestiario, seed 11500."""
    d = load()
    by = {s['id']: s for s in d['scenarios']}
    new, presets = [], {'t124-veterania': {'scenarios': [], 'seeds': seeds, 'minutes': 3}}
    for ctx, base in (('pist', 't119-pistoleros-rally8'), ('vind', 't119-vind-rally8')):
        for rank in (0, 3, 5):
            sc = json.loads(json.dumps(by[base]))
            assert 'off:morale' in sc['tags'], base
            sc['id'] = f't124-veterania-{ctx}-r{rank}'
            sc['blue']['rank'] = rank
            sc['tags'] = [t for t in sc['tags'] if not t.startswith('family:')] + ['family:veterania', f'rank:{rank}']
            sc['note'] = f'F90: copia de {base}, solo blue.rank = {rank}'
            new.append(sc)
            presets['t124-veterania']['scenarios'].append(sc['id'])
            presets['t124-veterania']['minutes'] = max(presets['t124-veterania']['minutes'], sc['minutes'])
    replace_round(d, 't124-', new, presets); save(d)
    print(','.join(presets))


def gen_persecucion125(seeds=30):
    """F91 (O4): 3.er escenario de F87/F89. Igual que gen_persecucion123 pero el rojo va montado (6 guardias melee rango 0,
    role mix:melee@m=1). Un preset t125-persecucion, n = 30, mods=bestiario, seed 11600."""
    d = load()
    new, presets = [], {'t125-persecucion': {'scenarios': [], 'seeds': seeds, 'minutes': 2}}
    for arm, role, tag in (('pie-fuga', 'melee', 'redflee:5'), ('jinete-fuga', 'mix:melee@m=1', 'redflee:5'),
                           ('pie-sinfuga', 'melee', 'fleemeasure:5'), ('jinete-sinfuga', 'mix:melee@m=1', 'fleemeasure:5')):
        sc = {'id': f't125-persecucion-{arm}', 'terrain': 'flat', 'distance': 20, 'position': 'front', 'minutes': 2,
              'tags': ['torneo', 'pursuit', 'off:morale', tag],
              'note': f'F91: 8 espadas rango 1 {"montadas" if "@m" in role else "a pie"} contra 6 guardias melee rango 0 montados, {tag}',
              'blue': {'kind': 'guard', 'count': 8, 'role': role, 'rank': 1},
              'red': {'kind': 'guard', 'count': 6, 'role': 'mix:melee@m=1', 'rank': 0}}
        new.append(sc)
        presets['t125-persecucion']['scenarios'].append(sc['id'])
    replace_round(d, 't125-', new, presets); save(d)
    print(','.join(presets))


def gen_retirada127(seeds=30):
    """F93 (O3): 2.o lote de F69 con solo los brazos que deciden. Copias exactas de t118 (d45-pelear, d45-casa-0,
    d45-dano, d45-early-4, d30-r16-espera, d30-r16-early-4) con ids t127-. Un preset t127-retirada-conf, n = 30,
    mods=bestiario, seed 12000."""
    d = load()
    by = {s['id']: s for s in d['scenarios']}
    new, presets = [], {}
    for arm, base_id in (('d45-pelear', 't118-d45-pelear'), ('d45-casa-0', 't118-d45-casa-0'), ('d45-dano', 't118-d45-dano'),
                         ('d45-early-4', 't118-d45-early-4'), ('r16-espera', 't118-d30-r16-espera'),
                         ('r16-early-4', 't118-d30-r16-early-4')):
        sc = json.loads(json.dumps(by[base_id]))
        sc['id'] = f't127-{arm}'
        sc['note'] = f'F93: copia de {base_id}'
        new.append(sc)
        pr = presets.setdefault('t127-retirada-conf', {'scenarios': [], 'seeds': seeds, 'minutes': sc['minutes']})
        pr['scenarios'].append(sc['id']); pr['minutes'] = max(pr['minutes'], sc['minutes'])
    replace_round(d, 't127-', new, presets); save(d)
    print(','.join(presets))


def gen_veterania128(seeds=30):
    """F94: ablacion de las reglas de rango que estorban en F90. Copias de t124-veterania-{pist,vind}-r{0,3,5};
    offBand = + off:rankBand (quita solo la banda want*1.6 / *1.4), offOutnumbered = + off:outnumbered (quita solo el
    paso 4 de AdvancedCore). Un preset t128-veterania-ablacion, n = 30, mods=bestiario, seed 12100."""
    d = load()
    by = {s['id']: s for s in d['scenarios']}
    new, presets = [], {'t128-veterania-ablacion': {'scenarios': [], 'seeds': seeds, 'minutes': 3}}
    for arm, base_id, tag in (('pist-r0', 't124-veterania-pist-r0', None), ('pist-r5', 't124-veterania-pist-r5', None),
                              ('pist-r5-offBand', 't124-veterania-pist-r5', 'off:rankBand'),
                              ('pist-r3-offBand', 't124-veterania-pist-r3', 'off:rankBand'),
                              ('vind-r0', 't124-veterania-vind-r0', None), ('vind-r5', 't124-veterania-vind-r5', None),
                              ('vind-r5-offOutnumbered', 't124-veterania-vind-r5', 'off:outnumbered')):
        sc = json.loads(json.dumps(by[base_id]))
        sc['id'] = f't128-veterania-{arm}'
        if tag:
            sc['tags'].append(tag)
        sc['note'] = f'F94: copia de {base_id}' + (f' + {tag}' if tag else '')
        new.append(sc)
        pr = presets['t128-veterania-ablacion']
        pr['scenarios'].append(sc['id']); pr['minutes'] = max(pr['minutes'], sc['minutes'])
    replace_round(d, 't128-', new, presets); save(d)
    print(','.join(presets))


def gen_veterania138(seeds=30):
    """F106: la otra mitad del hueco de la veteranía contra pistoleros. Copias de t124-veterania-pist-r{0,3,5};
    offBand = + off:rankBand (F94), offFocus = + off:rankFocus (quita solo el peldaño FOCUS_FIRE), offScoot =
    + off:scoot (quita solo SCOOT / PAIR_BOUND). Un preset t138-veterania-foco, n = 30, mods=bestiario, seed 12100."""
    d = load()
    by = {s['id']: s for s in d['scenarios']}
    new, presets = [], {'t138-veterania-foco': {'scenarios': [], 'seeds': seeds, 'minutes': 3}}
    for arm, base_id, tags in (('pist-r0', 't124-veterania-pist-r0', []),
                               ('pist-r5-offBand', 't124-veterania-pist-r5', ['off:rankBand']),
                               ('pist-r5-offBand-offFocus', 't124-veterania-pist-r5', ['off:rankBand', 'off:rankFocus']),
                               ('pist-r5-offBand-offScoot', 't124-veterania-pist-r5', ['off:rankBand', 'off:scoot']),
                               ('pist-r3-offBand-offFocus', 't124-veterania-pist-r3', ['off:rankBand', 'off:rankFocus'])):
        sc = json.loads(json.dumps(by[base_id]))
        sc['id'] = f't138-veterania-{arm}'
        sc['tags'].extend(tags)
        sc['note'] = f'F106: copia de {base_id}' + (' + ' + ' + '.join(tags) if tags else '')
        new.append(sc)
        pr = presets['t138-veterania-foco']
        pr['scenarios'].append(sc['id']); pr['minutes'] = max(pr['minutes'], sc['minutes'])
    replace_round(d, 't138-', new, presets); save(d)
    print(','.join(presets))


def gen_caballeria129(seeds=30):
    """F95 (O4): caballeria con espada en espacios pequenos. Azul 8 espadas rango 1 (pie: melee; jinete:
    mix:melee@m=1) contra 12 vindicadores native LINE a 30 bloques, 3 min, terrenos village / corridors / flat
    (control). Un preset t129-caballeria-calles, n = 30, mods=bestiario, seed 12200."""
    d = load()
    new, presets = [], {'t129-caballeria-calles': {'scenarios': [], 'seeds': seeds, 'minutes': 3}}
    for terrain in ('village', 'corridors', 'flat'):
        for arm, role in (('pie', 'melee'), ('jinete', 'mix:melee@m=1')):
            sc = {'id': f't129-caballeria-{terrain}-{arm}', 'terrain': terrain, 'distance': 30, 'position': 'front', 'minutes': 3,
                  'tags': ['torneo', 'off:morale', 'threat:vindicator', 'nostuck', 'family:caballeria'],
                  'note': f'F95: 8 espadas rango 1 {"montadas" if "@m" in role else "a pie"} contra 12 vindicadores, {terrain}',
                  'blue': {'kind': 'guard', 'count': 8, 'role': role, 'rank': 1},
                  'red': {'kind': 'vindicator', 'count': 12, 'role': 'native', 'rank': 0, 'formation': 'LINE',
                          'fireMode': 'NORMAL', 'mounted': False}}
            new.append(sc)
            presets['t129-caballeria-calles']['scenarios'].append(sc['id'])
    replace_round(d, 't129-', new, presets); save(d)
    print(','.join(presets))


def gen_desigual130(seeds=30):
    """F96 (O7): pocos coordinados contra muchos. Copia de gen_espejo126 (mirror-archer-20, azul 8 rango 1, off:morale,
    probe:hp) con el rojo siempre off:group:red y red.count = N (8 / 10 / 12; el resto del rojo se copia del azul).
    capa-N = solo off:group:red; indiv-N = off:group + off:group:red. Un preset t130-desigual, n = 30, mods=bestiario,
    seed 12300."""
    d = load()
    by = {s['id']: s for s in d['scenarios']}
    new, presets = [], {'t130-desigual': {'scenarios': [], 'seeds': seeds, 'minutes': 3}}
    base = 'mirror-archer-20'
    for n in (8, 10, 12):
        for arm, tags in (('capa', ['off:group:red']), ('indiv', ['off:group', 'off:group:red'])):
            sc = json.loads(json.dumps(by[base]))
            sc['id'] = f't130-desigual-{arm}-{n}'
            sc['blue']['count'] = 8
            sc['blue']['rank'] = 1
            sc['red'] = {'count': n}
            sc['minutes'] = 3
            sc['tags'] = list(sc.get('tags', [])) + ['off:morale', 'probe:hp'] + tags
            sc['note'] = f'F96: copia de {base}, 8 v {n}, rango 1, off:morale, probe:hp, ' + ' + '.join(tags)
            new.append(sc)
            presets['t130-desigual']['scenarios'].append(sc['id'])
    replace_round(d, 't130-', new, presets); save(d)
    print(','.join(presets))


def gen_porpartes132(seeds=30):
    """F99 (O7): ¿derrotar al enemigo por partes paga? Copia de t118-d30-r16-pelear (flat, arena 160, 8 azules
    mix:shield=4,archer=4 rango 1 contra 16 vindicadores native LINE a 30) sin refugio (refuge:*), 3 min. Brazos:
    once (16 a la vez), w15 / w30 / w60 = redWave:8:S. Un preset t132-por-partes, n = 30, sin mods, seed 12400."""
    d = load()
    by = {s['id']: s for s in d['scenarios']}
    base = 't118-d30-r16-pelear'
    new, presets = [], {'t132-por-partes': {'scenarios': [], 'seeds': seeds, 'minutes': 3}}
    for arm, tag in (('once', None), ('w15', 'redWave:8:15'), ('w30', 'redWave:8:30'), ('w60', 'redWave:8:60')):
        sc = json.loads(json.dumps(by[base]))
        sc['id'] = f't132-por-partes-{arm}'
        sc['minutes'] = 3
        sc['tags'] = [t for t in sc['tags'] if not t.startswith('refuge:')] + (['family:porpartes'] + ([tag] if tag else []))
        sc['note'] = f'F99: copia de {base} sin refugio, 3 min' + (f' + {tag}' if tag else ' (16 a la vez)')
        new.append(sc)
        presets['t132-por-partes']['scenarios'].append(sc['id'])
    replace_round(d, 't132-', new, presets); save(d)
    print(','.join(presets))


def gen_outnumbered133(seeds=30):
    """F101 (O7): ¿el paso OUTNUMBERED (paso 4 de AdvancedCore) ayuda con algun tamanio de enemigo? Copias de
    t128-veterania-vind-r5 y t128-veterania-vind-r5-offOutnumbered cambiando solo red.count (16/32/48/64). Un preset
    t133-outnumbered (8 brazos), n = 30, mods=bestiario, seed 12500. 3 min en todos (subir a 4 si 64 agota > 50 %)."""
    d = load()
    by = {s['id']: s for s in d['scenarios']}
    new, presets = [], {'t133-outnumbered': {'scenarios': [], 'seeds': seeds, 'minutes': 3}}
    for n in (16, 32, 48, 64):
        for arm, base_id in (('r5', 't128-veterania-vind-r5'), ('r5-offOutnumbered', 't128-veterania-vind-r5-offOutnumbered')):
            sc = json.loads(json.dumps(by[base_id]))
            sc['id'] = f't133-outnumbered-{n}-{arm}'
            sc['red']['count'] = n
            sc['note'] = f'F101: copia de {base_id}, red {n}'
            new.append(sc)
            presets['t133-outnumbered']['scenarios'].append(sc['id'])
    replace_round(d, 't133-', new, presets); save(d)
    print(','.join(presets))


def gen_termopilas134(seeds=30):
    """F102 (O7): ¿el paso estrecho multiplica la fuerza? Copia de t132-por-partes-once (flat, 8 azules
    mix:shield=4,archer=4 rango 1, off:morale, distancia 30, 3 min, vindicadores nativos LINE, sin refugio) cambiando
    solo terrain (flat / corridors) y red.count (16 / 32). Un preset t134-termopilas, n = 30, sin mods, seed 12600."""
    d = load()
    by = {s['id']: s for s in d['scenarios']}
    base = 't132-por-partes-once'
    new, presets = [], {'t134-termopilas': {'scenarios': [], 'seeds': seeds, 'minutes': 3}}
    for n in (16, 32):
        for arm, terrain in (('flat', 'flat'), ('corr', 'corridors')):
            sc = json.loads(json.dumps(by[base]))
            sc['id'] = f't134-termopilas-{arm}-{n}'
            sc['terrain'] = terrain
            sc['red']['count'] = n
            sc['tags'] = [t for t in sc['tags'] if not t.startswith('family:')] + ['family:termopilas']
            sc['note'] = f'F102: copia de {base}, terrain {terrain}, red {n}'
            new.append(sc)
            presets['t134-termopilas']['scenarios'].append(sc['id'])
    replace_round(d, 't134-', new, presets); save(d)
    print(','.join(presets))


def gen_foco135():
    """F103 (O7): foco de fuego abierto. Copia de t130-desigual-capa-N (azul 8 arcos rango 1, off:morale, probe:hp,
    rojo off:group:red) con N = 8 / 10; foco-N = + focus:open. Dos presets lanzados en la misma llamada (mismas
    semillas, pareadas): t135-foco (N = 8, n = 60) y t135-foco-10 (N = 10, n = 30); 180 corridas, mods=bestiario,
    seed 12700."""
    d = load()
    by = {s['id']: s for s in d['scenarios']}
    new = []
    presets = {'t135-foco': {'scenarios': [], 'seeds': 60, 'minutes': 3},
               't135-foco-10': {'scenarios': [], 'seeds': 30, 'minutes': 3}}
    for n, pr in ((8, 't135-foco'), (10, 't135-foco-10')):
        base = f't130-desigual-capa-{n}'
        for arm, tags in (('capa', []), ('foco', ['focus:open'])):
            sc = json.loads(json.dumps(by[base]))
            sc['id'] = f't135-foco-{arm}-{n}'
            sc['tags'] = list(sc['tags']) + tags
            sc['note'] = f'F103: copia de {base}' + (' + focus:open' if tags else '')
            new.append(sc)
            presets[pr]['scenarios'].append(sc['id'])
    replace_round(d, 't135-', new, presets); save(d)
    print(','.join(presets))


def gen_cabvelocidad136(seeds=30):
    """F104 (O4): jinetes con espada, ¿llegan sueltos por la velocidad de cada caballo? Copia de
    t129-caballeria-<terreno>-<pie|jinete> en flat y corridors; jinete-igual = jinete + mount:speed:0.225 (misma
    velocidad base para todos los caballos). Un preset t136-cab-velocidad, n = 30, mods=bestiario, seed 12200 (las de t129)."""
    d = load()
    by = {s['id']: s for s in d['scenarios']}
    new, presets = [], {'t136-cab-velocidad': {'scenarios': [], 'seeds': seeds, 'minutes': 3}}
    for terrain in ('flat', 'corridors'):
        for arm, src, tags in (('pie', 'pie', []), ('jinete', 'jinete', []), ('jinete-igual', 'jinete', ['mount:speed:0.225'])):
            base = f't129-caballeria-{terrain}-{src}'
            sc = json.loads(json.dumps(by[base]))
            sc['id'] = f't136-cab-velocidad-{terrain}-{arm}'
            sc['tags'] = [t for t in sc['tags'] if not t.startswith('family:')] + ['family:cabvelocidad'] + tags
            sc['note'] = f'F104: copia de {base}' + (' + mount:speed:0.225' if tags else '')
            new.append(sc)
            presets['t136-cab-velocidad']['scenarios'].append(sc['id'])
    replace_round(d, 't136-', new, presets); save(d)
    print(','.join(presets))


def gen_porpartescorto137(seeds=30):
    """F105 (O7): por partes, ¿cuánto tiene que durar la separación? Copia de t132-por-partes-<once|w15> con
    redWave:8:S para S = 4 / 8 / 12. Un preset t137-por-partes-corto, n = 30, sin mods, seed 12400 (las de F99)."""
    d = load()
    by = {s['id']: s for s in d['scenarios']}
    base = 't132-por-partes-once'
    new, presets = [], {'t137-por-partes-corto': {'scenarios': [], 'seeds': seeds, 'minutes': 3}}
    for arm, tag in (('once', None), ('w4', 'redWave:8:4'), ('w8', 'redWave:8:8'), ('w12', 'redWave:8:12')):
        sc = json.loads(json.dumps(by[base]))
        sc['id'] = f't137-por-partes-corto-{arm}'
        sc['tags'] = [t for t in sc['tags'] if not t.startswith('redWave:')] + ([tag] if tag else [])
        sc['note'] = f'F105: copia de {base}' + (f' + {tag}' if tag else ' (16 a la vez)')
        new.append(sc)
        presets['t137-por-partes-corto']['scenarios'].append(sc['id'])
    replace_round(d, 't137-', new, presets); save(d)
    print(','.join(presets))


def gen_porpartescalma139(seeds=30):
    """F107 (O7): por partes, ¿el hueco paga porque comen en calma? Copia de t132-por-partes-once con
    redWave:8:S (S = 8 / 12), con y sin off:calmEat. Un preset t139-por-partes-calma, n = 30, sin mods, seed 12400."""
    d = load()
    by = {s['id']: s for s in d['scenarios']}
    base = 't132-por-partes-once'
    new, presets = [], {'t139-por-partes-calma': {'scenarios': [], 'seeds': seeds, 'minutes': 3}}
    for arm, tags in (('w8', ['redWave:8:8']), ('w8-sincalma', ['redWave:8:8', 'off:calmEat']),
                      ('w12', ['redWave:8:12']), ('w12-sincalma', ['redWave:8:12', 'off:calmEat'])):
        sc = json.loads(json.dumps(by[base]))
        sc['id'] = f't139-por-partes-calma-{arm}'
        sc['tags'] = [t for t in sc['tags'] if not t.startswith('redWave:') and t != 'off:calmEat'] + tags
        sc['note'] = f'F107: copia de {base} + ' + ' + '.join(tags)
        new.append(sc)
        presets['t139-por-partes-calma']['scenarios'].append(sc['id'])
    replace_round(d, 't139-', new, presets); save(d)
    print(','.join(presets))


def gen_retirada140(seeds=30):
    """F109 (O3): falsa alarma de retreat:early:4. Copias de t127-r16-espera / t127-r16-early-4 cambiando solo red.count
    (32 y 48), brazo early-8 = early-4 con retreat:early:8; d45-early-4 / d45-early-8 = copias de t127-d45-early-4
    (80 vindicadores). Ids t140-. Un preset t140-retirada-falsa, n = 30, mods=bestiario, seed 12000 (= F93)."""
    d = load()
    by = {s['id']: s for s in d['scenarios']}
    arms = []
    for n in (32, 48):
        arms += [(f'r{n}-espera', 't127-r16-espera', n, None), (f'r{n}-early-4', 't127-r16-early-4', n, None),
                 (f'r{n}-early-8', 't127-r16-early-4', n, 'retreat:early:8')]
    arms += [('d45-early-4', 't127-d45-early-4', None, None), ('d45-early-8', 't127-d45-early-4', None, 'retreat:early:8')]
    new, presets = [], {}
    for arm, base_id, count, tag in arms:
        sc = json.loads(json.dumps(by[base_id]))
        sc['id'] = f't140-{arm}'
        if count is not None:
            sc['red']['count'] = count
        if tag is not None:
            sc['tags'] = [tag if t == 'retreat:early:4' else t for t in sc['tags']]
        sc['note'] = f'F109: copia de {base_id}' + (f', red.count = {count}' if count else '') + (f', {tag}' if tag else '')
        new.append(sc)
        pr = presets.setdefault('t140-retirada-falsa', {'scenarios': [], 'seeds': seeds, 'minutes': sc['minutes']})
        pr['scenarios'].append(sc['id']); pr['minutes'] = max(pr['minutes'], sc['minutes'])
    replace_round(d, 't140-', new, presets); save(d)
    print(','.join(presets))


def gen_jefes141(seeds=30):
    """F110 (O6): kiteBig fuera de Ignis. Copias de t88-ignis / t88-ignis-kiteoff cambiando solo red.kind y la
    etiqueta threat: a netherite_monstrosity, tremorzilla y ender_guardian. Ids t141-. Un preset t141-kitebig-jefes,
    n = 30, mods=bestiario, seed 12800."""
    d = load()
    by = {s['id']: s for s in d['scenarios']}
    new, presets = [], {}
    for name, kind in (('monstrosity', 'cataclysm:netherite_monstrosity'), ('tremorzilla', 'alexscaves:tremorzilla'),
                       ('enderguardian', 'cataclysm:ender_guardian')):
        for arm, base_id in (('defecto', 't88-ignis'), ('kiteoff', 't88-ignis-kiteoff')):
            sc = json.loads(json.dumps(by[base_id]))
            sc['id'] = f't141-{name}-{arm}'
            sc['red']['kind'] = kind
            sc['tags'] = [f'threat:{kind}' if t.startswith('threat:') else t for t in sc['tags']]
            sc['note'] = f'F110: copia de {base_id}, red.kind = {kind}'
            new.append(sc)
            pr = presets.setdefault('t141-kitebig-jefes', {'scenarios': [], 'seeds': seeds, 'minutes': sc['minutes']})
            pr['scenarios'].append(sc['id']); pr['minutes'] = max(pr['minutes'], sc['minutes'])
    replace_round(d, 't141-', new, presets); save(d)
    print(','.join(presets))


def gen_jefes143(seeds=30):
    """F114 (O6): kiteBig contra el Ender Guardian, 2.º lote. Copias de t141-enderguardian-<defecto|kiteoff>:
    arcos-* sin cambios (otra semilla), mixto-* con blue.role mix:shield=4,archer=4 (mismo kit netherite+ de t141).
    Un preset t143-kitebig-ender, n = 30, mods=bestiario, seed 13000."""
    d = load()
    by = {s['id']: s for s in d['scenarios']}
    new, presets = [], {}
    for comp, role in (('arcos', None), ('mixto', 'mix:shield=4,archer=4')):
        for arm in ('defecto', 'kiteoff'):
            base_id = f't141-enderguardian-{arm}'
            sc = json.loads(json.dumps(by[base_id]))
            sc['id'] = f't143-kitebig-ender-{comp}-{arm}'
            if role:
                sc['blue']['role'] = role
            sc['note'] = f'F114: copia de {base_id}' + (f', blue.role = {role}' if role else ' (2.º lote)')
            new.append(sc)
            pr = presets.setdefault('t143-kitebig-ender', {'scenarios': [], 'seeds': seeds, 'minutes': sc['minutes']})
            pr['scenarios'].append(sc['id']); pr['minutes'] = max(pr['minutes'], sc['minutes'])
    replace_round(d, 't143-', new, presets); save(d)
    print(','.join(presets))


def gen_altura144(seeds=30):
    """F115 (O7): ¿tomar la altura paga? Copias de t132-por-partes-once cambiando solo terrain (flat / hill) y, en el
    contexto pill, red.kind = pillager (vanilla, ballesta). Un preset t144-altura, n = 30, sin mods, seed 13100."""
    d = load()
    by = {s['id']: s for s in d['scenarios']}
    base = 't132-por-partes-once'
    new, presets = [], {'t144-altura': {'scenarios': [], 'seeds': seeds, 'minutes': 3}}
    for ctx, kind in (('vind', 'vindicator'), ('pill', 'pillager')):
        for terrain in ('flat', 'hill'):
            sc = json.loads(json.dumps(by[base]))
            sc['id'] = f't144-altura-{ctx}-{terrain}'
            sc['terrain'] = terrain
            sc['red']['kind'] = kind
            sc['tags'] = [f'threat:{kind}' if t.startswith('threat:') else t for t in sc['tags']
                          if not t.startswith('family:')] + ['family:altura']
            sc['note'] = f'F115: copia de {base}, terrain {terrain}, red {kind}'
            new.append(sc)
            presets['t144-altura']['scenarios'].append(sc['id'])
    replace_round(d, 't144-', new, presets); save(d)
    print(','.join(presets))


def gen_altura146(seeds=30):
    """F116 (O7): mantener la altura con MANTENER_LINEA. Copias de t144-altura-<vind|pill>-<flat|hill> con
    instruction:MANTENER_LINEA en todos; la colina pasa a terrain hillwide (meseta +4 de radio 8: los arcos de la
    orden quedan a -6 del ancla, donde hill solo mide +1) y en el contexto pill red.count = 48. Un preset
    t146-altura-hold, n = 30, sin mods, seed 13300."""
    d = load()
    by = {s['id']: s for s in d['scenarios']}
    new, presets = [], {'t146-altura-hold': {'scenarios': [], 'seeds': seeds, 'minutes': 3}}
    for ctx, name, count in (('vind', 'vind', None), ('pill', 'pill48', 48)):
        for terrain in ('flat', 'hill'):
            base_id = f't144-altura-{ctx}-{terrain}'
            sc = json.loads(json.dumps(by[base_id]))
            sc['id'] = f't146-altura-{name}-{terrain}-hold'
            sc['terrain'] = 'hillwide' if terrain == 'hill' else 'flat'
            if count:
                sc['red']['count'] = count
            sc['tags'] = [t for t in sc['tags'] if not t.startswith('instruction:')] + ['instruction:MANTENER_LINEA']
            sc['note'] = (f'F116: copia de {base_id}, instruction:MANTENER_LINEA, terrain {sc["terrain"]}'
                          + (f', red.count = {count}' if count else ''))
            new.append(sc)
            presets['t146-altura-hold']['scenarios'].append(sc['id'])
    replace_round(d, 't146-', new, presets); save(d)
    print(','.join(presets))


def gen_jefes147(seeds=30):
    """F117 (O5): kiteBig contra Tremorzilla (no es Enemy). Copias de t141-tremorzilla-defecto: arcos-* sin cambio de
    kit, mixto-* con blue.role mix:shield=4,archer=4; *-hostil añade la etiqueta kite:hostileBig. Un preset
    t147-kite-hostil, n = 30, mods=bestiario, seed 13400."""
    d = load()
    by = {s['id']: s for s in d['scenarios']}
    base_id = 't141-tremorzilla-defecto'
    new, presets = [], {}
    for comp, role in (('arcos', None), ('mixto', 'mix:shield=4,archer=4')):
        for arm in ('defecto', 'hostil'):
            sc = json.loads(json.dumps(by[base_id]))
            sc['id'] = f't147-kite-hostil-{comp}-{arm}'
            if role:
                sc['blue']['role'] = role
            if arm == 'hostil':
                sc['tags'] = sc['tags'] + ['kite:hostileBig']
            sc['note'] = (f'F117: copia de {base_id}' + (f', blue.role = {role}' if role else '')
                          + (', + kite:hostileBig' if arm == 'hostil' else ''))
            new.append(sc)
            pr = presets.setdefault('t147-kite-hostil', {'scenarios': [], 'seeds': seeds, 'minutes': sc['minutes']})
            pr['scenarios'].append(sc['id']); pr['minutes'] = max(pr['minutes'], sc['minutes'])
    replace_round(d, 't147-', new, presets); save(d)
    print(','.join(presets))


def gen_altura148(seeds=30):
    """F118 (O7): arco alto desde arriba. Copias de t146-altura-<vind|pill48>-hill-hold; *-noHigh añade la etiqueta
    fire:noHighAbove (sin línea y >= 2 bloques por encima del blanco: no se dispara en arco alto). Un preset
    t148-altura-arco, n = 30, sin mods, seed 13300 (= F116, pareadas)."""
    d = load()
    by = {s['id']: s for s in d['scenarios']}
    new, presets = [], {'t148-altura-arco': {'scenarios': [], 'seeds': seeds, 'minutes': 3}}
    for name in ('vind', 'pill48'):
        base_id = f't146-altura-{name}-hill-hold'
        for arm in ('defecto', 'noHigh'):
            sc = json.loads(json.dumps(by[base_id]))
            sc['id'] = f't148-altura-arco-{name}-hill-{arm}'
            if arm == 'noHigh':
                sc['tags'] = sc['tags'] + ['fire:noHighAbove']
            sc['note'] = f'F118: copia de {base_id}' + (', + fire:noHighAbove' if arm == 'noHigh' else '')
            new.append(sc)
            pr = presets['t148-altura-arco']
            pr['scenarios'].append(sc['id']); pr['minutes'] = max(pr['minutes'], sc.get('minutes', 3))
    replace_round(d, 't148-', new, presets); save(d)
    print(','.join(presets))


def gen_municion150(seeds=30):
    """F120 (O7): munición escasa desde la altura. Copias de t146-altura-pill48-hill-hold con ammo:3 / ammo:5 (el azul
    lleva N objetos de munición en total, ranura 0); *-noHigh añade fire:noHighAbove. Un preset t150-municion-altura,
    n = 30, sin mods, seed 13300 (= F116/F118, pareadas)."""
    d = load()
    by = {s['id']: s for s in d['scenarios']}
    base_id = 't146-altura-pill48-hill-hold'
    new, presets = [], {'t150-municion-altura': {'scenarios': [], 'seeds': seeds, 'minutes': 3}}
    for n in (3, 5):
        for arm in ('defecto', 'noHigh'):
            sc = json.loads(json.dumps(by[base_id]))
            sc['id'] = f't150-municion-pill48-hill-ammo{n}-{arm}'
            sc['tags'] = sc['tags'] + [f'ammo:{n}'] + (['fire:noHighAbove'] if arm == 'noHigh' else [])
            sc['note'] = f'F120: copia de {base_id}, + ammo:{n}' + (', + fire:noHighAbove' if arm == 'noHigh' else '')
            new.append(sc)
            pr = presets['t150-municion-altura']
            pr['scenarios'].append(sc['id']); pr['minutes'] = max(pr['minutes'], sc.get('minutes', 3))
    replace_round(d, 't150-', new, presets); save(d)
    print(','.join(presets))


def gen_municion151(seeds=30):
    """F121 (O7): munición escasa desde la altura, versión dura. Copias de t146-altura-pill48-hill-hold con ammo:3 y
    red.count = 96 / 144; *-noHigh añade fire:noHighAbove. Un preset t151-municion-dura, n = 30, sin mods, seed 13300
    (= F120, pareadas)."""
    d = load()
    by = {s['id']: s for s in d['scenarios']}
    base_id = 't146-altura-pill48-hill-hold'
    new, presets = [], {'t151-municion-dura': {'scenarios': [], 'seeds': seeds, 'minutes': 3}}
    for n in (96, 144):
        for arm in ('defecto', 'noHigh'):
            sc = json.loads(json.dumps(by[base_id]))
            sc['id'] = f't151-municion-dura-pill{n}-ammo3-{arm}'
            sc['red']['count'] = n
            sc['minutes'] = 5  # F121: 48 pillagers ya tardaban ~80 s en F120; 144 no cabe en 3 min
            sc['tags'] = sc['tags'] + ['ammo:3'] + (['fire:noHighAbove'] if arm == 'noHigh' else [])
            sc['note'] = f'F121: copia de {base_id}, red {n}, + ammo:3' + (', + fire:noHighAbove' if arm == 'noHigh' else '')
            new.append(sc)
            pr = presets['t151-municion-dura']
            pr['scenarios'].append(sc['id']); pr['minutes'] = max(pr['minutes'], sc.get('minutes', 3))
    replace_round(d, 't151-', new, presets); save(d)
    print(','.join(presets))


def gen_porpartes152(seeds=30):
    """F122 (O7): ¿por partes paga también contra tiradores? Copia de t132-por-partes-once con threat:pillager,
    red.kind = pillager, red.count = 32; brazos once / w8 / w15 / w30 = redWave:16:S. Un preset t152-por-partes-pill,
    n = 30, sin mods, seed 13400."""
    d = load()
    by = {s['id']: s for s in d['scenarios']}
    base = 't132-por-partes-once'
    new, presets = [], {'t152-por-partes-pill': {'scenarios': [], 'seeds': seeds, 'minutes': 3}}
    for arm, tag in (('once', None), ('w8', 'redWave:16:8'), ('w15', 'redWave:16:15'), ('w30', 'redWave:16:30')):
        sc = json.loads(json.dumps(by[base]))
        sc['id'] = f't152-por-partes-pill-{arm}'
        sc['red']['kind'] = 'pillager'
        sc['red']['count'] = 32
        sc['tags'] = [('threat:pillager' if t.startswith('threat:') else t) for t in sc['tags']
                      if not t.startswith('redWave:')] + ([tag] if tag else [])
        sc['note'] = f'F122: copia de {base}, 32 pillagers' + (f' + {tag}' if tag else ' (32 a la vez)')
        new.append(sc)
        presets['t152-por-partes-pill']['scenarios'].append(sc['id'])
    replace_round(d, 't152-', new, presets); save(d)
    print(','.join(presets))


def gen_porpartes155(seeds=30):
    """F125 (O7): por partes contra tiradores, versión dura. Copia de gen_porpartes152 con red.count = 48 / 64 y
    brazos once / w8 / w15 = redWave:(count/2):S. Un preset t155-por-partes-pill-duro, n = 30, sin mods, seed 13400."""
    d = load()
    by = {s['id']: s for s in d['scenarios']}
    base = 't132-por-partes-once'
    pname = 't155-por-partes-pill-duro'
    new, presets = [], {pname: {'scenarios': [], 'seeds': seeds, 'minutes': 3}}
    for count in (48, 64):
        half = count // 2
        for arm, tag in (('once', None), ('w8', f'redWave:{half}:8'), ('w15', f'redWave:{half}:15')):
            sc = json.loads(json.dumps(by[base]))
            sc['id'] = f'{pname}-pill{count}-{arm}'
            sc['red']['kind'] = 'pillager'
            sc['red']['count'] = count
            sc['tags'] = [('threat:pillager' if t.startswith('threat:') else t) for t in sc['tags']
                          if not t.startswith('redWave:')] + ([tag] if tag else [])
            sc['note'] = f'F125: copia de {base}, {count} pillagers' + (f' + {tag}' if tag else f' ({count} a la vez)')
            new.append(sc)
            presets[pname]['scenarios'].append(sc['id'])
    replace_round(d, 't155-', new, presets); save(d)
    print(','.join(presets))


def gen_lod157(seeds=12):
    """F127 (O9): IA por distancia. Copias de t122-tope-400 (400 v 400 espejo, arena:160, sin mods): ctl, n2
    (lod:ai:2:0), n4 (lod:ai:4:0), d32 (lod:ai:4:32). Un preset t157-lod, n = 12, mods vacio, seed 13600."""
    d = load()
    by = {s['id']: s for s in d['scenarios']}
    base = 't122-tope-400'
    pname = 't157-lod'
    new, presets = [], {pname: {'scenarios': [], 'seeds': seeds, 'minutes': by[base]['minutes']}}
    for arm, tag in (('ctl', None), ('n2', 'lod:ai:2:0'), ('n4', 'lod:ai:4:0'), ('d32', 'lod:ai:4:32')):
        sc = json.loads(json.dumps(by[base]))
        sc['id'] = f'{pname}-{arm}'
        sc['tags'] = [t for t in sc['tags'] if not t.startswith('lod:')] + ([tag] if tag else [])
        sc['note'] = f'F127: copia de {base}' + (f' + {tag}' if tag else ' (control)')
        new.append(sc)
        presets[pname]['scenarios'].append(sc['id'])
    replace_round(d, 't157-', new, presets); save(d)
    print(','.join(presets))


def gen_porpartes158(seeds=30):
    """F128 (O7): un cebo de 2 arqueros (U4 con unit:2) se lleva a parte del enemigo. Copias de t132-por-partes-once:
    once, w15 (redWave:8:15), mont-sin, cebo-pie, cebo-mont, once-d45, cebo-mont-d45. Un preset
    t158-por-partes-cebo, n = 30, sin mods, seed 13700. riderKite ya es valor por defecto: sin etiqueta rider:*."""
    d = load()
    by = {s['id']: s for s in d['scenarios']}
    base = 't132-por-partes-once'
    pname = 't158-por-partes-cebo'
    mont = 'mix:shield=4,archer=2,archer@m=2'
    cebo = ['unit:2', 'unitInstr:U4=HOSTIGAR@0', 'lure:2']
    arms = (('once', None, [], 30), ('w15', None, ['redWave:8:15'], 30), ('mont-sin', mont, ['lure:2'], 30),
            ('cebo-pie', None, cebo, 30), ('cebo-mont', mont, cebo, 30), ('once-d45', None, [], 45),
            ('cebo-mont-d45', mont, cebo, 45))
    new, presets = [], {pname: {'scenarios': [], 'seeds': seeds, 'minutes': by[base]['minutes']}}
    for arm, role, tags, dist in arms:
        sc = json.loads(json.dumps(by[base]))
        sc['id'] = f'{pname}-{arm}'
        if role:
            sc['blue']['role'] = role
        sc['distance'] = dist
        sc['tags'] = [t for t in sc['tags'] if not t.startswith(('redWave:', 'unit:', 'unitInstr:', 'lure:'))] + tags
        sc['note'] = f'F128: copia de {base}, brazo {arm}'
        new.append(sc)
        presets[pname]['scenarios'].append(sc['id'])
    replace_round(d, 't158-', new, presets); save(d)
    print(','.join(presets))


def gen_cebo160(seeds=30):
    """F130 (O7): 2.º lote del cebo a pie de F128 + control sin instrucción. Copias de t132-por-partes-once:
    once, w15 (redWave:8:15), cebo-pie (unit:2 + HOSTIGAR@0 + lure:2, = F128), pie-sin (unit:2 + lure:2, sin orden).
    Un preset t160-cebo-pie-2, n = 30, sin mods, seed 13900."""
    d = load()
    by = {s['id']: s for s in d['scenarios']}
    base = 't132-por-partes-once'
    pname = 't160-cebo-pie-2'
    arms = (('once', []), ('w15', ['redWave:8:15']),
            ('cebo-pie', ['unit:2', 'unitInstr:U4=HOSTIGAR@0', 'lure:2']), ('pie-sin', ['unit:2', 'lure:2']))
    new, presets = [], {pname: {'scenarios': [], 'seeds': seeds, 'minutes': by[base]['minutes']}}
    for arm, tags in arms:
        sc = json.loads(json.dumps(by[base]))
        sc['id'] = f'{pname}-{arm}'
        sc['distance'] = 30
        sc['tags'] = [t for t in sc['tags'] if not t.startswith(('redWave:', 'unit:', 'unitInstr:', 'lure:'))] + tags
        sc['note'] = f'F130: copia de {base}, brazo {arm}'
        new.append(sc)
        presets[pname]['scenarios'].append(sc['id'])
    replace_round(d, 't160-', new, presets); save(d)
    print(','.join(presets))


def gen_cebo161(seeds=30):
    """F131 (O7): dosis del cebo a pie. Copias de t132-por-partes-once a 30: once, cebo-1 (unit:1 + U8=HOSTIGAR@0 +
    lure:1), cebo-2 (unit:2 + U4=HOSTIGAR@0 + lure:2, = F128/F130), cebo-4 (unit:2 + U3,U4=HOSTIGAR@0 + lure:4).
    mix:shield=4,archer=4 aparece en orden 4 escudos y 4 arcos (SimRunner.mixRole), asi que U8 (unit:1) y U3+U4
    (unit:2) son arqueros. Un preset t161-cebo-dosis, n = 30, sin mods, seed 14000."""
    d = load()
    by = {s['id']: s for s in d['scenarios']}
    base = 't132-por-partes-once'
    pname = 't161-cebo-dosis'
    arms = (('once', []),
            ('cebo-1', ['unit:1', 'unitInstr:U8=HOSTIGAR@0', 'lure:1']),
            ('cebo-2', ['unit:2', 'unitInstr:U4=HOSTIGAR@0', 'lure:2']),
            ('cebo-4', ['unit:2', 'unitInstr:U3=HOSTIGAR@0,U4=HOSTIGAR@0', 'lure:4']))
    new, presets = [], {pname: {'scenarios': [], 'seeds': seeds, 'minutes': by[base]['minutes']}}
    for arm, tags in arms:
        sc = json.loads(json.dumps(by[base]))
        sc['id'] = f'{pname}-{arm}'
        sc['distance'] = 30
        sc['tags'] = [t for t in sc['tags'] if not t.startswith(('redWave:', 'unit:', 'unitInstr:', 'lure:'))] + tags
        sc['note'] = f'F131: copia de {base}, brazo {arm}'
        new.append(sc)
        presets[pname]['scenarios'].append(sc['id'])
    replace_round(d, 't161-', new, presets); save(d)
    print(','.join(presets))


def gen_cebo163(seeds=30):
    """F133 (O7): 2.o lote de la dosis del cebo (seed 14200). Los 4 brazos de F131 identicos + cebo-2u1 (unit:1 +
    U7,U8=HOSTIGAR@0 + lure:2): dos arqueros en dos unidades de 1 (indices 6 y 7 = los 2 ultimos que marca lure:2).
    Un preset t163-cebo-dosis-2, n = 30, sin mods."""
    d = load()
    by = {s['id']: s for s in d['scenarios']}
    base = 't132-por-partes-once'
    pname = 't163-cebo-dosis-2'
    arms = (('once', []),
            ('cebo-1', ['unit:1', 'unitInstr:U8=HOSTIGAR@0', 'lure:1']),
            ('cebo-2', ['unit:2', 'unitInstr:U4=HOSTIGAR@0', 'lure:2']),
            ('cebo-4', ['unit:2', 'unitInstr:U3=HOSTIGAR@0,U4=HOSTIGAR@0', 'lure:4']),
            ('cebo-2u1', ['unit:1', 'unitInstr:U7=HOSTIGAR@0,U8=HOSTIGAR@0', 'lure:2']))
    new, presets = [], {pname: {'scenarios': [], 'seeds': seeds, 'minutes': by[base]['minutes']}}
    for arm, tags in arms:
        sc = json.loads(json.dumps(by[base]))
        sc['id'] = f'{pname}-{arm}'
        sc['distance'] = 30
        sc['tags'] = [t for t in sc['tags'] if not t.startswith(('redWave:', 'unit:', 'unitInstr:', 'lure:'))] + tags
        sc['note'] = f'F133: copia de {base}, brazo {arm}'
        new.append(sc)
        presets[pname]['scenarios'].append(sc['id'])
    replace_round(d, 't163-', new, presets); save(d)
    print(','.join(presets))


def gen_cebo165(seeds=30):
    """F135 (O7): cebo de 1 arquero contra 16/24/32 vindicadores. Copias de t132-por-partes-once a 30 cambiando solo
    red.count: once-N y cebo1-N (unit:1 + U8=HOSTIGAR@0 + lure:1, = cebo-1 de F133). Un preset t165-cebo-duro,
    n = 30, sin mods, seed 14400."""
    d = load()
    by = {s['id']: s for s in d['scenarios']}
    base = 't132-por-partes-once'
    pname = 't165-cebo-duro'
    cebo = ['unit:1', 'unitInstr:U8=HOSTIGAR@0', 'lure:1']
    new, presets = [], {pname: {'scenarios': [], 'seeds': seeds, 'minutes': by[base]['minutes']}}
    for n in (16, 24, 32):
        for arm, tags in (('once', []), ('cebo1', cebo)):
            sc = json.loads(json.dumps(by[base]))
            sc['id'] = f'{pname}-{arm}-{n}'
            sc['distance'] = 30
            sc['red']['count'] = n
            sc['tags'] = [t for t in sc['tags']
                          if not t.startswith(('redWave:', 'unit:', 'unitInstr:', 'lure:'))] + tags
            sc['note'] = f'F135: copia de {base}, brazo {arm}, {n} vindicadores'
            new.append(sc)
            presets[pname]['scenarios'].append(sc['id'])
    replace_round(d, 't165-', new, presets); save(d)
    print(','.join(presets))


def gen_foco156(seeds=30):
    """F126 (O7): foco con cupo. t156-foco-cupo-pist = copias de t124-veterania-pist-r3 + off:rankBand (r3-foco,
    r3-sinfoco + off:rankFocus, r3-cupo2/3 + focus:quota:K); t156-foco-cupo-espejo = copias de t135-foco-capa-8
    (capa-8, siembra-8 + focus:open + focus:seed, siembra-cupo3-8 + focus:quota:3). n = 30, mods=bestiario, seed 13500."""
    d = load()
    by = {s['id']: s for s in d['scenarios']}
    new = []
    presets = {'t156-foco-cupo-pist': {'scenarios': [], 'seeds': seeds, 'minutes': 3},
               't156-foco-cupo-espejo': {'scenarios': [], 'seeds': seeds, 'minutes': 3}}
    arms = (('t156-foco-cupo-pist', 'r3-foco', 't124-veterania-pist-r3', ['off:rankBand']),
            ('t156-foco-cupo-pist', 'r3-sinfoco', 't124-veterania-pist-r3', ['off:rankBand', 'off:rankFocus']),
            ('t156-foco-cupo-pist', 'r3-cupo2', 't124-veterania-pist-r3', ['off:rankBand', 'focus:quota:2']),
            ('t156-foco-cupo-pist', 'r3-cupo3', 't124-veterania-pist-r3', ['off:rankBand', 'focus:quota:3']),
            ('t156-foco-cupo-espejo', 'capa-8', 't135-foco-capa-8', []),
            ('t156-foco-cupo-espejo', 'siembra-8', 't135-foco-capa-8', ['focus:open', 'focus:seed']),
            ('t156-foco-cupo-espejo', 'siembra-cupo3-8', 't135-foco-capa-8', ['focus:open', 'focus:seed', 'focus:quota:3']))
    for pname, arm, base_id, tags in arms:
        sc = json.loads(json.dumps(by[base_id]))
        sc['id'] = f'{pname}-{arm}'
        sc['tags'] = list(sc['tags']) + tags
        sc['note'] = f'F126: copia de {base_id}' + (' + ' + ' + '.join(tags) if tags else '')
        new.append(sc)
        pr = presets[pname]
        pr['scenarios'].append(sc['id']); pr['minutes'] = max(pr['minutes'], sc['minutes'])
    replace_round(d, 't156-', new, presets); save(d)
    print(','.join(presets))


def gen_retirada159(seeds=30):
    """F129 (O3): disparador por fuerza (retreat:force:1500) y cabezas con denominador fijo (retreat:early0:5).
    Copias de t140-r32-espera (instructionAt:9999) cambiando red.count / red.kind y la etiqueta; d45-* = copias de
    t140-d45-early-4. Un preset t159-retirada-fuerza, n = 30, mods=bestiario, seed 12000 (= F93/F109)."""
    d = load()
    by = {s['id']: s for s in d['scenarios']}
    pname = 't159-retirada-fuerza'
    force, early0 = 'retreat:force:1500', 'retreat:early0:5'
    arms = (('r32-espera', 't140-r32-espera', 32, 'vindicator', None),
            ('r32-force', 't140-r32-espera', 32, 'vindicator', force),
            ('r48-espera', 't140-r32-espera', 48, 'vindicator', None),
            ('r48-force', 't140-r32-espera', 48, 'vindicator', force),
            ('r48-early0-5', 't140-r32-espera', 48, 'vindicator', early0),
            ('d45-force', 't140-d45-early-4', None, None, force),
            ('d45-early0-5', 't140-d45-early-4', None, None, early0),
            ('p64-espera', 't140-r32-espera', 64, 'pillager', None),
            ('p64-force', 't140-r32-espera', 64, 'pillager', force),
            ('p64-early0-5', 't140-r32-espera', 64, 'pillager', early0))
    new, presets = [], {pname: {'scenarios': [], 'seeds': seeds, 'minutes': 2}}
    for arm, base_id, count, kind, tag in arms:
        sc = json.loads(json.dumps(by[base_id]))
        sc['id'] = f't159-{arm}'
        if count is not None:
            sc['red']['count'] = count
        if kind is not None:
            sc['red']['kind'] = kind
            sc['tags'] = [f'threat:{kind}' if t.startswith('threat:') else t for t in sc['tags']]
        tags = [t for t in sc['tags'] if not t.startswith(('retreat:early:', 'instructionAt:'))]
        tags += [tag] if tag else ['instructionAt:9999']
        sc['tags'] = tags
        sc['note'] = f'F129: copia de {base_id}, brazo {arm}'
        new.append(sc)
        presets[pname]['scenarios'].append(sc['id']); presets[pname]['minutes'] = max(presets[pname]['minutes'], sc['minutes'])
    replace_round(d, 't159-', new, presets); save(d)
    print(','.join(presets))


def gen_retirada166(seeds=30):
    """F136 (O3): 2.o lote del disparador por fuerza (otra semilla) + brazo T = 1340. Mismas bases que F129
    (gen_retirada159), sin early0-5. Un preset t166-retirada-fuerza-2, n = 30, mods=bestiario, seed 14600."""
    d = load()
    by = {s['id']: s for s in d['scenarios']}
    pname = 't166-retirada-fuerza-2'
    f15, f13 = 'retreat:force:1500', 'retreat:force:1340'
    arms = (('r32-espera', 't140-r32-espera', 32, 'vindicator', None),
            ('r32-force', 't140-r32-espera', 32, 'vindicator', f15),
            ('r32-force1340', 't140-r32-espera', 32, 'vindicator', f13),
            ('r48-espera', 't140-r32-espera', 48, 'vindicator', None),
            ('r48-force', 't140-r32-espera', 48, 'vindicator', f15),
            ('r48-force1340', 't140-r32-espera', 48, 'vindicator', f13),
            ('d45-force', 't140-d45-early-4', None, None, f15),
            ('p64-espera', 't140-r32-espera', 64, 'pillager', None),
            ('p64-force', 't140-r32-espera', 64, 'pillager', f15),
            ('p64-force1340', 't140-r32-espera', 64, 'pillager', f13))
    new, presets = [], {pname: {'scenarios': [], 'seeds': seeds, 'minutes': 2}}
    for arm, base_id, count, kind, tag in arms:
        sc = json.loads(json.dumps(by[base_id]))
        sc['id'] = f't166-{arm}'
        if count is not None:
            sc['red']['count'] = count
        if kind is not None:
            sc['red']['kind'] = kind
            sc['tags'] = [f'threat:{kind}' if t.startswith('threat:') else t for t in sc['tags']]
        tags = [t for t in sc['tags'] if not t.startswith(('retreat:early:', 'instructionAt:'))]
        tags += [tag] if tag else ['instructionAt:9999']
        sc['tags'] = tags
        sc['note'] = f'F136: copia de {base_id}, brazo {arm}'
        new.append(sc)
        presets[pname]['scenarios'].append(sc['id']); presets[pname]['minutes'] = max(presets[pname]['minutes'], sc['minutes'])
    replace_round(d, 't166-', new, presets); save(d)
    print(','.join(presets))


def gen_dispersion162(seeds=30):
    """F132 (O6): dispersion contra el rayo de Tremorzilla (spread:S -> SimExtras.spreadTick). Copias exactas de
    t147-kite-hostil-mixto-defecto; -spread4 / -spread7 = + spread:4 / spread:7. Un preset t162-tremor-spread,
    n = 30, mods=bestiario, seed 14100."""
    d = load()
    base = 't147-kite-hostil-mixto-defecto'
    rows = (('t162-mixto-defecto', base, {}), ('t162-mixto-spread4', base, {'tags': ['spread:4']}),
            ('t162-mixto-spread7', base, {'tags': ['spread:7']}))
    presets = _copies(d, rows, 't162-', 'F132', lambda sid: 't162-tremor-spread', seeds)
    save(d); print(','.join(presets))


def gen_escala164(seeds=10):
    """F134 (O8): consulta corta de kite salvo enemigo grande cerca (perf:kiteLazy -> FieldManual.simKiteLazy).
    t164-kite-lazy (sin mods): copias de t96-400-arcos (del preset t114-base400); t164-kite-lazy-ignis (bestiario):
    copias de t88-ignis (del preset t88-defecto-ignis). -lazy = + perf:kiteLazy. n = 10, seed 14300."""
    d = load()
    lazy = {'tags': ['perf:kiteLazy']}
    rows = (('t164-arcos', 't96-400-arcos', {}), ('t164-arcos-lazy', 't96-400-arcos', lazy),
            ('t164-ignis', 't88-ignis', {}), ('t164-ignis-lazy', 't88-ignis', lazy))
    presets = _copies(d, rows, 't164-', 'F134',
                      lambda sid: 't164-kite-lazy-ignis' if 'ignis' in sid else 't164-kite-lazy', seeds)
    save(d); print(','.join(presets))


def gen_ladder167(seeds=10):
    """F137 (O8): solo medida, LADDER partido en cinco (Perf.LADDER_*). Sin mods, seed 14700, n = 10.
    t167-ladder: copias exactas de t96-400-arcos y t96-scale-400; t167-ladder-800: copia de t122-tope-400 (arena:160)."""
    d = load()
    rows = (('t167-arcos', 't96-400-arcos', {}), ('t167-mixto', 't96-scale-400', {}),
            ('t167-tope400', 't122-tope-400', {}))
    presets = _copies(d, rows, 't167-', 'F137',
                      lambda sid: 't167-ladder-800' if 'tope' in sid else 't167-ladder', seeds)
    save(d); print(','.join(presets))


def gen_apply171(seeds=10):
    """F141 (O8): solo medida, LADDER_APPLY partido por peldaño (perf_APPLY_<RUNG>, cnt_APPLY_<RUNG>). Como
    gen_ladder167 con prefijo t171, sin mods, seed 14900, n = 10. t171-apply: arcos + mixto; t171-apply-800: tope400."""
    d = load()
    rows = (('t171-arcos', 't96-400-arcos', {}), ('t171-mixto', 't96-scale-400', {}),
            ('t171-tope400', 't122-tope-400', {}))
    presets = _copies(d, rows, 't171-', 'F141',
                      lambda sid: 't171-apply-800' if 'tope' in sid else 't171-apply', seeds)
    save(d); print(','.join(presets))


def gen_dispersion168(seeds=30):
    """F138 (O6): 2.o lote de gen_dispersion162 (mismos brazos, prefijo t168). Un preset t168-tremor-spread, n = 30,
    mods=bestiario, seed 14800."""
    d = load()
    base = 't147-kite-hostil-mixto-defecto'
    rows = (('t168-mixto-defecto', base, {}), ('t168-mixto-spread4', base, {'tags': ['spread:4']}),
            ('t168-mixto-spread7', base, {'tags': ['spread:7']}))
    presets = _copies(d, rows, 't168-', 'F138', lambda sid: 't168-tremor-spread', seeds)
    save(d); print(','.join(presets))


def gen_multiplicador169(seeds=30):
    """F139 (O7): multiplicador de fuerza, 3.er escenario, guardias contra guardias cuerpo a cuerpo. Copias de
    mirror-melee-20 con blue.count = 8, rango 1, off:morale, probe:hp y 3 minutos (como F85). espejo-8 = rojo Rally
    espejo (8); gv-N = rojo Guard Villagers nativo (tag norally:red) con red.count = N. Un preset
    t169-multiplicador-gvg, n = 30, sin mods, seed 15100."""
    d = load()
    by = {s['id']: s for s in d['scenarios']}
    new, presets = [], {'t169-multiplicador-gvg': {'scenarios': [], 'seeds': seeds, 'minutes': 3}}
    for label, n in (('espejo-8', None), ('gv-8', 8), ('gv-12', 12), ('gv-16', 16), ('gv-24', 24)):
        sc = json.loads(json.dumps(by['mirror-melee-20']))
        sc['id'] = f't169-gvg-{label}'
        sc['blue']['count'] = 8
        sc['blue']['rank'] = 1
        sc['minutes'] = 3
        sc['tags'] = list(sc.get('tags', [])) + ['off:morale', 'probe:hp'] + (['norally:red'] if n else [])
        if n:
            sc['red'] = {'count': n}
        sc['note'] = 'F139: copia de mirror-melee-20, 8 Rally, rango 1, off:morale, probe:hp' + \
            (f', rojo norally:red x {n}' if n else ', rojo Rally espejo x 8 (control)')
        new.append(sc)
        presets['t169-multiplicador-gvg']['scenarios'].append(sc['id'])
    replace_round(d, 't169-', new, presets); save(d)
    print(','.join(presets))


def gen_dispersion170(seeds=30):
    """F140 (O6): 3.er lote de gen_dispersion162/168, solo defecto y spread7 (prefijo t170). Un preset
    t170-tremor-spread, n = 30, mods=bestiario, seed 15200."""
    d = load()
    base = 't147-kite-hostil-mixto-defecto'
    rows = (('t170-mixto-defecto', base, {}), ('t170-mixto-spread7', base, {'tags': ['spread:7']}))
    presets = _copies(d, rows, 't170-', 'F140', lambda sid: 't170-tremor-spread', seeds)
    save(d); print(','.join(presets))


def gen_fuerza172(seeds=30):
    """F142 (O3): ¿retreat:force:1500 generaliza fuera de los vindicadores? Copias exactas de t88-ignis (8 arcos
    netherita+ contra Ignis, kiteBig por defecto), t88-pax48 (8 arcos contra 48 Pax, arena 160) y, como contexto que
    se pierde, t88-ignis con rol melee y sin tier (= hierro; el manual: ni 64 de hierro ganan a Ignis). A cada una se
    le añade la instrucción en espera RETIRARSE con refugio-casa a 16 como en t140/t166; espera = instructionAt:9999,
    force = retreat:force:1500. Dos presets (la arena se fija por preset): t172-fuerza-ig (ig + pierde, arena 96) y
    t172-fuerza-pax (arena 160). n = 30, mods=bestiario, seed 15300 (pareadas en cada contexto)."""
    d = load()
    by = {s['id']: s for s in d['scenarios']}
    retreat = ['refuge:16', 'nostuck', 'instruction:RETIRARSE', 'retreat:flee', 'refuge:house']
    ctx = (('ig', 't88-ignis', 't172-fuerza-ig', None),
           ('pierde', 't88-ignis', 't172-fuerza-ig', 'melee'),
           ('pax', 't88-pax48', 't172-fuerza-pax', None))
    new, presets = [], {}
    for name, base_id, pname, role in ctx:
        for arm, tag in (('espera', 'instructionAt:9999'), ('force', 'retreat:force:1500')):
            sc = json.loads(json.dumps(by[base_id]))
            sc['id'] = f't172-{name}-{arm}'
            if role:
                sc['blue']['role'] = role
                sc['tags'] = [t for t in sc['tags'] if not t.startswith('tier:')]
            sc['tags'] = sc['tags'] + [t for t in retreat if t not in sc['tags']] + [tag]
            sc['note'] = f'F142: copia de {base_id}' + (f', rol {role}, sin tier (hierro)' if role else '') + \
                f', RETIRARSE en espera refugio 16, brazo {arm}'
            new.append(sc)
            pr = presets.setdefault(pname, {'scenarios': [], 'seeds': seeds, 'minutes': sc['minutes']})
            pr['scenarios'].append(sc['id']); pr['minutes'] = max(pr['minutes'], sc['minutes'])
    replace_round(d, 't172-', new, presets); save(d)
    print(','.join(presets))


def gen_closer173(seeds=10):
    """F144 (O8): REPOSITION_CLOSER no rehace un camino que ya va a <= 3 bloques del sitio (perf:closerReuse ->
    Tactics.simCloserReuse). Como gen_apply171 con prefijo t173, sin mods, seed 15500, n = 10, semillas pareadas.
    t173-closer: arcos + mixto (hoy / reuse); t173-closer-800: tope400 (hoy / reuse)."""
    d = load()
    reuse = {'tags': ['perf:closerReuse']}
    rows = (('t173-arcos-hoy', 't96-400-arcos', {}), ('t173-arcos-reuse', 't96-400-arcos', reuse),
            ('t173-mixto-hoy', 't96-scale-400', {}), ('t173-mixto-reuse', 't96-scale-400', reuse),
            ('t173-tope400-hoy', 't122-tope-400', {}), ('t173-tope400-reuse', 't122-tope-400', reuse))
    presets = _copies(d, rows, 't173-', 'F144',
                      lambda sid: 't173-closer-800' if 'tope' in sid else 't173-closer', seeds)
    save(d); print(','.join(presets))


def gen_closer177(seeds=10):
    """F149 (O8): de quien es el camino lejano de REPOSITION_CLOSER (closerEndTarget / EndOurs / EndOther, NewTarget,
    Spot3 / SpotFar) con perf:closerReuse. Copia de gen_closer176 con prefijo t177, sin mods, seed 15500 (= F144/F148),
    n = 10. Preset t177-closer."""
    d = load()
    reuse = {'tags': ['perf:closerReuse']}
    rows = (('t177-arcos-reuse', 't96-400-arcos', reuse), ('t177-mixto-reuse', 't96-scale-400', reuse))
    presets = _copies(d, rows, 't177-', 'F149', lambda sid: 't177-closer', seeds)
    save(d); print(','.join(presets))


def gen_aldea180(seeds=30):
    """F152 (O2): aldea del juego contra 32 Pax, solo cambia la composicion de los 8. Copias de t110-aldea-juego-sincob
    con red.count = 32 y el rol azul: e4a4 (control), e6a2, e2a6, s4a4 (scutum). Un preset t180-aldea-pax32, n = 30,
    mods=bestiario, seed 16400."""
    d = load()
    base = 't110-aldea-juego-sincob'
    rows = (('t180-aldea32-e4a4', base, {'red': 32}),
            ('t180-aldea32-e6a2', base, {'red': 32, 'role': 'mix:shield=6,archer=2'}),
            ('t180-aldea32-e2a6', base, {'red': 32, 'role': 'mix:shield=2,archer=6'}),
            ('t180-aldea32-s4a4', base, {'red': 32, 'role': 'mix:scutum=4,archer=4'}))
    presets = _copies(d, rows, 't180-', 'F152', lambda sid: 't180-aldea-pax32', seeds)
    save(d); print(','.join(presets))


def gen_escudos179(seeds=30):
    """F151 (O7): cuando y como mueren los escudos a 4:1. Copias de t132-por-partes-once a 30 bloques, sin
    redWave:/unit:/unitInstr:/lure:, + probe:deaths (F66): once-16 (red.count 16) y once-32 (red.count 32, = once-32
    de F150 + la sonda). Un preset t179-escudos, n = 30, sin mods, seed 16200 (= F150)."""
    d = load()
    by = {s['id']: s for s in d['scenarios']}
    base = 't132-por-partes-once'
    pname = 't179-escudos'
    new, presets = [], {pname: {'scenarios': [], 'seeds': seeds, 'minutes': by[base]['minutes']}}
    for arm, count in (('once-16', 16), ('once-32', 32)):
        sc = json.loads(json.dumps(by[base]))
        sc['id'] = f'{pname}-{arm}'
        sc['distance'] = 30
        sc['red']['count'] = count
        sc['tags'] = [t for t in sc['tags'] if not t.startswith(('redWave:', 'unit:', 'unitInstr:', 'lure:'))] \
            + ['probe:deaths']
        sc['note'] = f'F151: copia de {base}, {count} vindicadores + probe:deaths'
        new.append(sc)
        presets[pname]['scenarios'].append(sc['id'])
    replace_round(d, 't179-', new, presets); save(d)
    print(','.join(presets))


def gen_cebo178(seeds=30):
    """F150 (O7): dosis del cebo contra 32 vindicadores. Copias de t132-por-partes-once a 30 con red.count = 32:
    once-32, cebo1-32 (unit:1 + U8), cebo2-32 (unit:1 + U7,U8), cebo4-32 (unit:2 + U3,U4). lure:k marca los k
    ultimos azules = los arqueros (indices 4-7 de mix:shield=4,archer=4) de las unidades con orden. Un preset
    t178-cebo-dosis-32, n = 30, sin mods, seed 16200."""
    d = load()
    by = {s['id']: s for s in d['scenarios']}
    base = 't132-por-partes-once'
    pname = 't178-cebo-dosis-32'
    arms = (('once-32', []),
            ('cebo1-32', ['unit:1', 'unitInstr:U8=HOSTIGAR@0', 'lure:1']),
            ('cebo2-32', ['unit:1', 'unitInstr:U7=HOSTIGAR@0,U8=HOSTIGAR@0', 'lure:2']),
            ('cebo4-32', ['unit:2', 'unitInstr:U3=HOSTIGAR@0,U4=HOSTIGAR@0', 'lure:4']))
    new, presets = [], {pname: {'scenarios': [], 'seeds': seeds, 'minutes': by[base]['minutes']}}
    for arm, tags in arms:
        sc = json.loads(json.dumps(by[base]))
        sc['id'] = f'{pname}-{arm}'
        sc['distance'] = 30
        sc['red']['count'] = 32
        sc['tags'] = [t for t in sc['tags'] if not t.startswith(('redWave:', 'unit:', 'unitInstr:', 'lure:'))] + tags
        sc['note'] = f'F150: copia de {base}, brazo {arm}, 32 vindicadores'
        new.append(sc)
        presets[pname]['scenarios'].append(sc['id'])
    replace_round(d, 't178-', new, presets); save(d)
    print(','.join(presets))


def gen_closer176(seeds=10):
    """F148 (O8): contadores de REPOSITION_CLOSER (closerDone / NoEnd / Far6 / Far12 / FarMax) con perf:closerReuse.
    Como gen_closer173 con prefijo t176, solo brazos reuse, sin mods, seed 15500 (= F144), n = 10. Preset t176-closer."""
    d = load()
    reuse = {'tags': ['perf:closerReuse']}
    rows = (('t176-arcos-reuse', 't96-400-arcos', reuse), ('t176-mixto-reuse', 't96-scale-400', reuse))
    presets = _copies(d, rows, 't176-', 'F148', lambda sid: 't176-closer', seeds)
    save(d); print(','.join(presets))


def gen_zombie174(seeds=30):
    """F145: ¿t5-final-zombie-mele-base regresó o es ruido? Un preset t174-zombie-mele con el escenario ORIGINAL
    (mismo id, sin copia, para parear semilla a semilla con la referencia 20261108-191727-t5-check-zombie, commit
    48bfdbc), n = 30, minutes como t5-check-zombie. Lanzar sin mods, seed 1 (semillas 1-30 como la referencia)."""
    d = load()
    ref = d['presets']['t5-check-zombie']
    presets = {'t174-zombie-mele': {'scenarios': ['t5-final-zombie-mele-base'], 'seeds': seeds,
                                    'minutes': ref['minutes']}}
    replace_round(d, 't174-', [], presets); save(d)
    print(','.join(presets))


def gen_lod175(seeds=20):
    """F147 (O9): LOD simetrico. Copias de t122-tope-400 con red {"count": 320} (400 azul v 320 rojo; el resto del
    rojo hereda del azul) y SIN la etiqueta mirror: ctl, n2 (lod:ai:2:0), n3 (lod:ai:3:0); minutes = 4. Un preset
    t175-lod, n = 20, mods vacio, seed 16000."""
    d = load()
    by = {s['id']: s for s in d['scenarios']}
    base = 't122-tope-400'
    pname = 't175-lod'
    new, presets = [], {pname: {'scenarios': [], 'seeds': seeds, 'minutes': 4}}
    for arm, tag in (('ctl', None), ('n2', 'lod:ai:2:0'), ('n3', 'lod:ai:3:0')):
        sc = json.loads(json.dumps(by[base]))
        sc['id'] = f'{pname}-{arm}'
        sc['tags'] = [t for t in sc['tags'] if not t.startswith('lod:') and t != 'mirror'] + ([tag] if tag else [])
        sc['red'] = {'count': 320}
        sc['minutes'] = 4
        sc['note'] = f'F147: copia de {base}, 400 v 320, sin mirror' + (f' + {tag}' if tag else ' (control)')
        new.append(sc)
        presets[pname]['scenarios'].append(sc['id'])
    replace_round(d, 't175-', new, presets); save(d)
    print(','.join(presets))


def gen_nostuck181(seeds=20):
    """F153 (O9): 400 v 320, ¿la pelea se para o la corta stuck? Copia de t175-lod-ctl (escenario de F147) + nostuck,
    minutes = 4. Un preset t181-nostuck, n = 20, mods vacio, seed 16000 (pareado con la serie 1 de F147)."""
    d = load()
    by = {s['id']: s for s in d['scenarios']}
    base = 't175-lod-ctl'
    pname = 't181-nostuck'
    sc = json.loads(json.dumps(by[base]))
    sc['id'] = 't181-ctl-nostuck'
    sc['tags'] = [t for t in sc['tags'] if t != 'nostuck'] + ['nostuck']
    sc['minutes'] = 4
    sc['note'] = f'F153: copia de {base} (400 v 320) + nostuck'
    presets = {pname: {'scenarios': [sc['id']], 'seeds': seeds, 'minutes': 4}}
    replace_round(d, 't181-', [sc], presets); save(d)
    print(','.join(presets))


def gen_gvg182(seeds=30):
    """F155 (O7): ablacion gvg. Copias exactas de t169-gvg-gv-8 (8 Rally rango 1, off:morale, probe:hp, rojo 8
    norally:red, 3 minutos) con una sola etiqueta por brazo: gv8-rally (control), gv8-sinblanco (+ off:group:target),
    gv8-solo (+ off:group). Un preset t182-gvg-ablacion, n = 30, sin mods, seed 15100 (pareada con F139)."""
    d = load()
    base = 't169-gvg-gv-8'
    rows = (('t182-gv8-rally', base, {}),
            ('t182-gv8-sinblanco', base, {'tags': ['off:group:target']}),
            ('t182-gv8-solo', base, {'tags': ['off:group']}))
    presets = _copies(d, rows, 't182-', 'F155', lambda sid: 't182-gvg-ablacion', seeds)
    save(d); print(','.join(presets))


def gen_aldea183(seeds=30):
    """F156 (O2): 2.o lote del scutum en la aldea contra 32 Pax. Copias exactas de t180-aldea32-e4a4 (control) y
    t180-aldea32-s4a4 (F152) sin cambios. Un preset t183-aldea-scutum, n = 30, mods=bestiario, seed 16500."""
    d = load()
    rows = (('t183-aldea32-e4a4', 't180-aldea32-e4a4', {}),
            ('t183-aldea32-s4a4', 't180-aldea32-s4a4', {}))
    presets = _copies(d, rows, 't183-', 'F156', lambda sid: 't183-aldea-scutum', seeds)
    save(d); print(','.join(presets))


def gen_closer185(seeds=30):
    """F158 (O8): REPOSITION_CLOSER cede el camino en curso si ya acaba a <= 3 bloques del blanco (perf:closerYield ->
    Tactics.simCloserYield). Prefijo t185, sin mods, seed 15500 (= F144/F148/F149), n = 30. Preset t185-closer."""
    d = load()
    yld = {'tags': ['perf:closerYield']}
    rows = (('t185-arcos-base', 't96-400-arcos', {}), ('t185-arcos-yield', 't96-400-arcos', yld),
            ('t185-mixto-base', 't96-scale-400', {}), ('t185-mixto-yield', 't96-scale-400', yld))
    presets = _copies(d, rows, 't185-', 'F158', lambda sid: 't185-closer', seeds)
    save(d); print(','.join(presets))


def gen_emboscada184(seeds=30):
    """F157 (O7): emboscada azul (E5, enlace AMBUSH de PlanV2). Copias exactas de planner2-ambush-base (control,
    plan:hold) y planner2-ambush (plan:ambush) a 15 rojos (sin cambios) y a 22 rojos. Un preset t184-emboscada,
    n = 30, sin mods, seed 16600. Cada pareja solo difiere en plan:hold / plan:ambush."""
    d = load()
    rows = (('t184-base-15', 'planner2-ambush-base', {}),
            ('t184-ambush-15', 'planner2-ambush', {}),
            ('t184-base-22', 'planner2-ambush-base', {'red': 22}),
            ('t184-ambush-22', 'planner2-ambush', {'red': 22}))
    presets = _copies(d, rows, 't184-', 'F157', lambda sid: 't184-emboscada', seeds)
    save(d); print(','.join(presets))


def gen_gvg186(seeds=30):
    """F159 (O7): ¿Rally pierde el gvg por la retirada con vida baja? Copias exactas de t169-gvg-gv-8 (control y
    + off:lowRetreat) y de t169-gvg-gv-12 (+ off:lowRetreat). Un preset t186-gvg-retirada, n = 30, sin mods,
    seed 15100 (pareada con F139/F155)."""
    d = load()
    off = {'tags': ['off:lowRetreat']}
    rows = (('t186-gv8-rally', 't169-gvg-gv-8', {}),
            ('t186-gv8-sinretirada', 't169-gvg-gv-8', off),
            ('t186-gv12-sinretirada', 't169-gvg-gv-12', off))
    presets = _copies(d, rows, 't186-', 'F159', lambda sid: 't186-gvg-retirada', seeds)
    save(d); print(','.join(presets))


def gen_emboscada187(seeds=30):
    """F160 (O7): emboscada azul en escenario duro. Copias de planner2-ambush-base (plan:hold) y planner2-ambush
    (plan:ambush) cambiando solo el rojo: 48 zombis, 16 y 24 vindicadores (nativos). Un preset t187-emboscada-dura,
    n = 30, sin mods, seed 16700."""
    d = load()
    vind = {'kind': 'vindicator', 'role': 'native'}
    rows = []
    for name, ch in (('zombi-48', {'red': 48}), ('vind-16', {'red': 16, 'redSpec': vind}),
                     ('vind-24', {'red': 24, 'redSpec': vind})):
        rows.append((f't187-base-{name}', 'planner2-ambush-base', ch))
        rows.append((f't187-ambush-{name}', 'planner2-ambush', ch))
    presets = _copies(d, rows, 't187-', 'F160', lambda sid: 't187-emboscada-dura', seeds)
    save(d); print(','.join(presets))


def gen_split188(seeds=20):
    """F161 (O9): que hacen los supervivientes de 400 v 320 cuando la pelea se para (~63 s). Copia de t181-ctl-nostuck
    (escenario de F153) + probe:split, minutes = 3 (la ficha pide 2,5; minutes es entero en SimScenario). Un preset
    t188-split, n = 20, mods vacio, seed 16000 (pareado con F147 / F153)."""
    d = load()
    by = {s['id']: s for s in d['scenarios']}
    base = 't181-ctl-nostuck'
    pname = 't188-split'
    sc = json.loads(json.dumps(by[base]))
    sc['id'] = 't188-split'
    sc['tags'] = [t for t in sc['tags'] if t != 'probe:split'] + ['probe:split']
    sc['minutes'] = 3
    sc['note'] = f'F161: copia de {base} (400 v 320, nostuck) + probe:split'
    presets = {pname: {'scenarios': [sc['id']], 'seeds': seeds, 'minutes': 3}}
    replace_round(d, 't188-', [sc], presets); save(d)
    print(','.join(presets))


def gen_gvg189(seeds=30):
    """F162 (O7): ablacion gvg sin la retirada por vida baja. Copias de t169-gvg-gv-8 con off:lowRetreat en los tres
    brazos y una etiqueta mas: gv8-nr (control = t186-gv8-sinretirada), gv8-nr-sinblanco (+ off:group:target),
    gv8-nr-solo (+ off:group). Un preset t189-gvg-grupo, n = 30, sin mods, seed 15100 (pareada con F139/F155/F159)."""
    d = load()
    base = 't169-gvg-gv-8'
    rows = (('t189-gv8-nr', base, {'tags': ['off:lowRetreat']}),
            ('t189-gv8-nr-sinblanco', base, {'tags': ['off:lowRetreat', 'off:group:target']}),
            ('t189-gv8-nr-solo', base, {'tags': ['off:lowRetreat', 'off:group']}))
    presets = _copies(d, rows, 't189-', 'F162', lambda sid: 't189-gvg-grupo', seeds)
    save(d); print(','.join(presets))


def gen_emboscada190(seeds=30):
    """F164 (O7): gen_emboscada187 solo con la dureza vind-24 (2.o lote de vind-24) y la telemetria por unidad de
    SimExtras (u1Dead, u2Dead, u1DeadAtSpring, u2ContactS, u1FirstDeathS, missingCapS). Un preset
    t190-emboscada-unidad, n = 30, sin mods, seed 16900."""
    d = load()
    ch = {'red': 24, 'redSpec': {'kind': 'vindicator', 'role': 'native'}}
    rows = (('t190-vind-24-base', 'planner2-ambush-base', ch), ('t190-vind-24-ambush', 'planner2-ambush', ch))
    presets = _copies(d, rows, 't190-', 'F164', lambda sid: 't190-emboscada-unidad', seeds)
    save(d); print(','.join(presets))


def gen_reorder193(seeds=20):
    """F167 (O9): ¿la pelea 400 v 320 se para porque caduca la orden de ataque a los 60 s? t193-split-ctl = copia de
    t188-split; t193-split-reorder = + reorder:30 (SimRunner.reorderTick). Contadores uo:SOURCE:KIND (UnitOrders).
    Un preset t193-reorder, n = 20, mods vacio, seed 16000 (pareado con F153 / F161), 3 min."""
    d = load()
    rows = (('t193-split-ctl', 't188-split', {}), ('t193-split-reorder', 't188-split', {'tags': ['reorder:30']}))
    presets = _copies(d, rows, 't193-', 'F167', lambda sid: 't193-reorder', seeds)
    save(d); print(','.join(presets))


def gen_closer194(seeds=30):
    """F166 (O8): 2.o lote de perf:closerYield en 2 x 2 con la pelea viva (reorder:30 de F167). Copias de
    t185-arcos-base / t185-arcos-yield, sin y con reorder:30. Un preset t194-closer, n = 30, sin mods, seed 17300."""
    d = load()
    ro = {'tags': ['reorder:30']}
    rows = (('t194-arcos-base', 't185-arcos-base', {}), ('t194-arcos-yield', 't185-arcos-yield', {}),
            ('t194-arcos-base-ro', 't185-arcos-base', ro), ('t194-arcos-yield-ro', 't185-arcos-yield', ro))
    presets = _copies(d, rows, 't194-', 'F166', lambda sid: 't194-closer', seeds)
    save(d); print(','.join(presets))


def gen_emboscada195(seeds=30):
    """F168 (O7): emboscada con punto de espera de verdad. Copias de t190-vind-24-base (plan:hold) y
    t190-vind-24-ambush (plan:ambush) + ambushwait:20:0 (U2 teletransportada 20 bloques a la izquierda al empezar el
    plan), y lo mismo + ambushtrig:SEEN. Un preset t195-emboscada-flanco, n = 30, sin mods, seed 16900 (= F164)."""
    d = load()
    w = ['ambushwait:20:0']
    rows = (('t195-vind-24-base', 't190-vind-24-base', {}),
            ('t195-vind-24-flanco', 't190-vind-24-ambush', {'tags': w}),
            ('t195-vind-24-flanco-visto', 't190-vind-24-ambush', {'tags': w + ['ambushtrig:SEEN']}))
    presets = _copies(d, rows, 't195-', 'F168', lambda sid: 't195-emboscada-flanco', seeds)
    save(d); print(','.join(presets))


def gen_escolta196(seeds=30):
    """F169: ¿t17-escolta-esqueletos-escudo-defender regresó o es ruido? Un preset t196-escolta-esq con el escenario
    ORIGINAL (mismo id, sin copia, para parear semilla a semilla con la referencia
    20261108-191727-t17-escolta-esqueletos, commit 48bfdbc, semillas 1-30), n = 30, minutes como
    t17-escolta-esqueletos. Lanzar sin mods, seed 1."""
    d = load()
    ref = d['presets']['t17-escolta-esqueletos']
    presets = {'t196-escolta-esq': {'scenarios': ['t17-escolta-esqueletos-escudo-defender'], 'seeds': seeds,
                                    'minutes': ref['minutes']}}
    replace_round(d, 't196-', [], presets); save(d)
    print(','.join(presets))


def gen_retirada197(seeds=20):
    """F170 (O3): ¿RETIRARSE aguanta a 100 guardias? Copias de t52-escape-vindicadores80-linea-pelear + instruction:
    RETIRARSE, instructionAt:0, retreat:flee, nostuck; solo cambia el tamaño: azul = rojo = 8 / 25 / 50 / 100
    (mix:shield=N/2,archer=N/2; con N impar el escudo lleva el de más). Un preset t197-retirada-escala, n = 20, sin
    mods, seed 17000."""
    d = load()
    tags = ['instruction:RETIRARSE', 'instructionAt:0', 'retreat:flee', 'nostuck']
    rows = tuple((f't197-ret-b{n}', 't52-escape-vindicadores80-linea-pelear',
                  {'tags': tags, 'blue': n, 'red': n, 'role': f'mix:shield={n - n // 2},archer={n // 2}'})
                 for n in (8, 25, 50, 100))
    presets = _copies(d, rows, 't197-', 'F170', lambda sid: 't197-retirada-escala', seeds)
    save(d); print(','.join(presets))


def gen_norule199(seeds=20):
    """F172 (O9): ¿qué regla FORCE manda el FALLBACK que congela 400 v 320? t199-ctl = copia de t193-split-ctl;
    t199-nofb = + noRule:unit-fallback; t199-nomor = + noRule:unit-morale-fallback (ForceBrain.simNoRules).
    Contadores uoRule:<id> / uoRuleRed:<id> (UnitOrders). Un preset t199-norule, n = 20, sin mods, seed 16000
    (pareado con F167), 3 min."""
    d = load()
    rows = (('t199-ctl', 't193-split-ctl', {}),
            ('t199-nofb', 't193-split-ctl', {'tags': ['noRule:unit-fallback']}),
            ('t199-nomor', 't193-split-ctl', {'tags': ['noRule:unit-morale-fallback']}))
    presets = _copies(d, rows, 't199-', 'F172', lambda sid: 't199-norule', seeds)
    save(d); print(','.join(presets))


def gen_retirada198(seeds=20):
    """F171 (O3): RETIRARSE a escala, ¿falla el tamaño o la presión? Igual que gen_retirada197 pero azul y rojo se
    cruzan en 2 x 2: azul 8 / 100 x rojo 8 / 100. Un preset t198-retirada-cruce, n = 20, sin mods, seed 17000 (=
    F170: b8r8 y b100r100 repiten sus brazos b8 y b100)."""
    d = load()
    tags = ['instruction:RETIRARSE', 'instructionAt:0', 'retreat:flee', 'nostuck']
    rows = tuple((f't198-ret-b{b}r{r}', 't52-escape-vindicadores80-linea-pelear',
                  {'tags': tags, 'blue': b, 'red': r, 'role': f'mix:shield={b - b // 2},archer={b // 2}'})
                 for b in (8, 100) for r in (8, 100))
    presets = _copies(d, rows, 't198-', 'F171', lambda sid: 't198-retirada-cruce', seeds)
    save(d); print(','.join(presets))


def gen_mantener200(seeds=20):
    """F173 (O3): ¿MANTENER_LINEA aguanta a 100 guardias? Igual que gen_retirada197 pero con instruction:
    MANTENER_LINEA, instructionAt:0, nostuck, measuredist (sin retreat:flee); azul = rojo = 8 / 25 / 50 / 100.
    Medidas holdDrift / holdBroken / holdN (SimExtras). Un preset t200-mantener-escala, n = 20, sin mods, seed 17000."""
    d = load()
    tags = ['instruction:MANTENER_LINEA', 'instructionAt:0', 'nostuck', 'measuredist']
    rows = tuple((f't200-man-b{n}', 't52-escape-vindicadores80-linea-pelear',
                  {'tags': tags, 'blue': n, 'red': n, 'role': f'mix:shield={n - n // 2},archer={n // 2}', 'minutes': 2})
                 for n in (8, 25, 50, 100))
    presets = _copies(d, rows, 't200-', 'F173', lambda sid: 't200-mantener-escala', seeds)
    save(d); print(','.join(presets))


def gen_mantener201(seeds=20):
    """F174 (O3): corrige la medida de F173. Copia exacta de gen_mantener200 con solo b8 y b100; SimExtras añade
    hold* por familia (R = arco/ballesta, I = resto), settle* (posición 5 s después de la orden) y post* /
    patrolShare (distancia al puesto de GV). Un preset t201-mantener-medida, n = 20, sin mods, seed 17000."""
    d = load()
    tags = ['instruction:MANTENER_LINEA', 'instructionAt:0', 'nostuck', 'measuredist']
    rows = tuple((f't201-man-b{n}', 't52-escape-vindicadores80-linea-pelear',
                  {'tags': tags, 'blue': n, 'red': n, 'role': f'mix:shield={n - n // 2},archer={n // 2}', 'minutes': 2})
                 for n in (8, 100))
    presets = _copies(d, rows, 't201-', 'F174', lambda sid: 't201-mantener-medida', seeds)
    save(d); print(','.join(presets))


def gen_mantener202(seeds=20):
    """F175 (O3): MANTENER_LINEA asentada, ¿se rompe por el tamaño o por el contacto? Copia exacta de
    gen_mantener201 pero azul y rojo se cruzan en 2 x 2: azul 8 / 100 x rojo 8 / 100 (role por el tamaño azul, como
    gen_retirada198). Un preset t202-mantener-cruce, n = 20, sin mods, seed 17000 (= F173 / F174)."""
    d = load()
    tags = ['instruction:MANTENER_LINEA', 'instructionAt:0', 'nostuck', 'measuredist']
    rows = tuple((f't202-man-b{b}r{r}', 't52-escape-vindicadores80-linea-pelear',
                  {'tags': tags, 'blue': b, 'red': r, 'role': f'mix:shield={b - b // 2},archer={b // 2}', 'minutes': 2})
                 for b in (8, 100) for r in (8, 100))
    presets = _copies(d, rows, 't202-', 'F175', lambda sid: 't202-mantener-cruce', seeds)
    save(d); print(','.join(presets))


def gen_mantener203(seeds=20):
    """F177 (O3): MANTENER_LINEA a 100 sin presión, ¿se asientan si el contacto llega tarde? Copia exacta de
    gen_mantener202 con distance: b100r8 d30 (control F175), b100r8 d100, b8r8 d100 (referencia de tamaño) y
    b100r100 d30 (control F174/F175). Un preset t203-mantener-tarde, n = 20, sin mods, seed 17000."""
    d = load()
    tags = ['instruction:MANTENER_LINEA', 'instructionAt:0', 'nostuck', 'measuredist']
    rows = tuple((f't203-man-b{b}r{r}-d{dist}', 't52-escape-vindicadores80-linea-pelear',
                  {'tags': tags, 'blue': b, 'red': r, 'role': f'mix:shield={b - b // 2},archer={b // 2}', 'minutes': 2,
                   'distance': dist})
                 for b, r, dist in ((100, 8, 30), (100, 8, 100), (8, 8, 100), (100, 100, 30)))
    presets = _copies(d, rows, 't203-', 'F177', lambda sid: 't203-mantener-tarde', seeds)
    save(d); print(','.join(presets))


def gen_avanzar204(seeds=20):
    """F178 (O3): AVANZAR a escala, ¿100 guardias avanzan juntos o se deshilachan? Molde de gen_mantener203 con
    instruction:AVANZAR, instructionAt:0, nostuck, measuredist, redwait:30; distance 100; rojo 8; azul 8 / 100 (role
    mix escudo/arco). Medidas adv10* / adv20* (SimExtras). Un preset t204-avanzar-escala, n = 20, sin mods, seed 17000."""
    d = load()
    tags = ['instruction:AVANZAR', 'instructionAt:0', 'nostuck', 'measuredist', 'redwait:30']
    rows = tuple((f't204-av-b{b}', 't52-escape-vindicadores80-linea-pelear',
                  {'tags': tags, 'blue': b, 'red': 8, 'role': f'mix:shield={b - b // 2},archer={b // 2}', 'minutes': 2,
                   'distance': 100})
                 for b in (8, 100))
    presets = _copies(d, rows, 't204-', 'F178', lambda sid: 't204-avanzar-escala', seeds)
    save(d); print(','.join(presets))


def gen_avanzar205(seeds=20):
    """F179 (O3): AVANZAR a escala a los 20 s. Molde de gen_avanzar204 con redwait:40 (y redwait ya sujeta a los mobs
    rojos, F179). Un preset t205-avanzar-20s, n = 20, sin mods, seed 17000."""
    d = load()
    tags = ['instruction:AVANZAR', 'instructionAt:0', 'nostuck', 'measuredist', 'redwait:40']
    rows = tuple((f't205-av-b{b}', 't52-escape-vindicadores80-linea-pelear',
                  {'tags': tags, 'blue': b, 'red': 8, 'role': f'mix:shield={b - b // 2},archer={b // 2}', 'minutes': 2,
                   'distance': 100})
                 for b in (8, 100))
    presets = _copies(d, rows, 't205-', 'F179', lambda sid: 't205-avanzar-20s', seeds)
    save(d); print(','.join(presets))


def gen_flanquear206(seeds=20):
    """F180 (O3): FLANQUEAR a escala. Molde exacto de gen_avanzar205 (redwait:40) con instruction:FLANQUEAR. Medidas
    flk10* / flk20* (SimExtras). Un preset t206-flanquear-escala, n = 20, sin mods, seed 17000."""
    d = load()
    tags = ['instruction:FLANQUEAR', 'instructionAt:0', 'nostuck', 'measuredist', 'redwait:40']
    rows = tuple((f't206-fl-b{b}', 't52-escape-vindicadores80-linea-pelear',
                  {'tags': tags, 'blue': b, 'red': 8, 'role': f'mix:shield={b - b // 2},archer={b // 2}', 'minutes': 2,
                   'distance': 100})
                 for b in (8, 100))
    presets = _copies(d, rows, 't206-', 'F180', lambda sid: 't206-flanquear-escala', seeds)
    save(d); print(','.join(presets))


def gen_avanzar207(seeds=20):
    """F181 (O3): AVANZAR a escala a los 90 s. Molde exacto de gen_avanzar205 salvo redwait:100 y minutes 3. Medidas
    adv/flk a 10/20/30/60/90 s, instrAlive{m}, act:instrEnd:*, instrEndSec (SimExtras). Un preset t207-avanzar-90s,
    n = 20, sin mods, seed 17000."""
    d = load()
    tags = ['instruction:AVANZAR', 'instructionAt:0', 'nostuck', 'measuredist', 'redwait:100']
    rows = tuple((f't207-av-b{b}', 't52-escape-vindicadores80-linea-pelear',
                  {'tags': tags, 'blue': b, 'red': 8, 'role': f'mix:shield={b - b // 2},archer={b // 2}', 'minutes': 3,
                   'distance': 100})
                 for b in (8, 100))
    presets = _copies(d, rows, 't207-', 'F181', lambda sid: 't207-avanzar-90s', seeds)
    save(d); print(','.join(presets))


def gen_flanquear208(seeds=20):
    """F182 (O3): FLANQUEAR con la medida de angulo. Copias exactas de t206-fl-b8 / t206-fl-b100 (molde de
    gen_flanquear206). Medidas flkAng{m}*, flkAngGuard{m}*, flkAngOpen{m}*, act:flankSpot:* (SimExtras). Un preset
    t208-flanquear-angulo, n = 20, sin mods, seed 17400."""
    d = load()
    rows = (('t208-fl-b8', 't206-fl-b8', {}), ('t208-fl-b100', 't206-fl-b100', {}))
    presets = _copies(d, rows, 't208-', 'F182', lambda sid: 't208-flanquear-angulo', seeds)
    save(d); print(','.join(presets))


def gen_flanquear211(seeds=20):
    """F185 (O3): FLANQUEAR con los arcos en su propia unidad. Copias exactas de t208-fl-b8 / t208-fl-b100 con
    unit:4 / unit:50 (U1 = escudos, U2 = arcos por orden de aparicion), unitInstr:U1=FLANQUEAR@0,U2=FLANQUEAR@0 y
    unitMark (SimExtras.unitTick llama holdMark y anota u{k}Family), SIN instruction:/instructionAt. Un preset
    t211-flanquear-unidades, n = 20, sin mods, seed 17400 (= F182)."""
    d = load()
    ui = 'unitInstr:U1=FLANQUEAR@0,U2=FLANQUEAR@0'
    rows = (('t211-fl-b8', 't208-fl-b8', {'tags': ['unit:4', ui, 'unitMark']}),
            ('t211-fl-b100', 't208-fl-b100', {'tags': ['unit:50', ui, 'unitMark']}))
    presets = _copies(d, rows, 't211-', 'F185', lambda sid: 't211-flanquear-unidades', seeds)
    for sc in d['scenarios']:
        if sc['id'].startswith('t211-'):
            sc['tags'] = [t for t in sc['tags'] if not t.startswith('instruction:') and not t.startswith('instructionAt:')]
    save(d); print(','.join(presets))


def gen_cacescala220(seeds=30):
    """F197 (O7): golpes escalonados a escala. Copias de t189-gv8-nr (= t217-cac-capa) y + melee:sync (= t217-cac-sync)
    con 16 y 32 guardias por bando (azul y rojo iguales), misma arena (96 por defecto; mirror-melee-100 ya cabe).
    Un preset t220-cac-escala, n = 30, sin mods, seed 19000."""
    d = load()
    ms = ['melee:sync']
    rows = tuple((f't220-{n}-{arm}', 't189-gv8-nr', dict({'blue': n, 'red': n}, **({'tags': ms} if arm == 'sync' else {})))
                 for n in (16, 32) for arm in ('capa', 'sync'))
    presets = _copies(d, rows, 't220-', 'F197', lambda sid: 't220-cac-escala', seeds)
    save(d); print(','.join(presets))


def gen_cacsync217(seeds=30):
    """F193 (O7): golpes escalonados (melee:sync, mixin solo-sim). Copias de t189-gv8-nr-solo (cac-solo), t189-gv8-nr
    (cac-capa) y t189-gv8-nr + melee:sync (cac-sync); contexto 8 v 4: t189-gv8-nr con 4 rojos, con y sin melee:sync.
    Un preset t217-cac-sync, n = 30, sin mods, seed 15100 (pareada con F186/F187)."""
    d = load()
    ms = ['melee:sync']
    rows = (('t217-cac-solo', 't189-gv8-nr-solo', {}), ('t217-cac-capa', 't189-gv8-nr', {}),
            ('t217-cac-sync', 't189-gv8-nr', {'tags': ms}),
            ('t217-8v4-capa', 't189-gv8-nr', {'red': 4}), ('t217-8v4-sync', 't189-gv8-nr', {'tags': ms, 'red': 4}))
    presets = _copies(d, rows, 't217-', 'F193', lambda sid: 't217-cac-sync', seeds)
    save(d); print(','.join(presets))


def gen_o8anatomia218(seeds=30):
    """F195 (O8): anatomia del 1 % peor. Copia exacta de t216-400-arcos (= t96-400-arcos + reorder:30), preset
    t218-o8-anatomia, n = 30, sin mods, seed 17800. Solo medida: claves p99Shots, p99Gc, p99Phase, p99Early... (Perf)."""
    d = load()
    rows = (('t218-400-arcos', 't216-400-arcos', {}),)
    presets = _copies(d, rows, 't218-', 'F195', lambda sid: 't218-o8-anatomia', seeds)
    save(d); print(','.join(presets))


def gen_o8rgpartes216(seeds=30):
    """F192 (O8): de qué está hecho RANGED_GOAL. Copia de t96-400-arcos + reorder:30 (= t213-400-arcos), preset
    t216-o8-rgpartes, n = 30, sin mods, seed 17800 (pareada con F183/F188). Solo medida: cubos Perf RG_*."""
    d = load()
    rows = (('t216-400-arcos', 't96-400-arcos', {'tags': ['reorder:30']}),)
    presets = _copies(d, rows, 't216-', 'F192', lambda sid: 't216-o8-rgpartes', seeds)
    save(d); print(','.join(presets))


def gen_o8planmemo215(seeds=30):
    """F190 (O8): 2.o lote del memo, ya por defecto. A = t96-400-arcos + reorder:30 + perf:noPlanMemo; B = t96-400-arcos
    + reorder:30 (memo por defecto). Preset t215-o8-planmemo2, n = 30, sin mods, seed 18400 (nueva)."""
    d = load()
    rows = (('t215-400-arcos-nomemo', 't96-400-arcos', {'tags': ['reorder:30', 'perf:noPlanMemo']}),
            ('t215-400-arcos', 't96-400-arcos', {'tags': ['reorder:30']}))
    presets = _copies(d, rows, 't215-', 'F190', lambda sid: 't215-o8-planmemo2', seeds)
    save(d); print(','.join(presets))


def gen_o8planmemo214(seeds=30):
    """F189 (O8): el plan del arco, una vez por tick. A = t96-400-arcos + reorder:30 (= t213-400-arcos); B = A +
    perf:planMemo. Preset t214-o8-planmemo, n = 30, sin mods, seed 17800 (pareada con F183/F188)."""
    d = load()
    rows = (('t214-400-arcos', 't96-400-arcos', {'tags': ['reorder:30']}),
            ('t214-400-arcos-memo', 't96-400-arcos', {'tags': ['reorder:30', 'perf:planMemo']}))
    presets = _copies(d, rows, 't214-', 'F189', lambda sid: 't214-o8-planmemo', seeds)
    save(d); print(','.join(presets))


def gen_o8partes213(seeds=30):
    """F188 (O8): de qué está hecho el p99 de 400 arcos. Copia de t96-400-arcos + reorder:30 (= t209-400-arcos),
    preset t213-o8-partes, n = 30, sin mods, seed 17800 (pareada con F183). Solo medida: cubos Perf ST_* y RANGED_PLAN."""
    d = load()
    rows = (('t213-400-arcos', 't96-400-arcos', {'tags': ['reorder:30']}),)
    presets = _copies(d, rows, 't213-', 'F188', lambda sid: 't213-o8-partes', seeds)
    save(d); print(','.join(presets))


def gen_o8hoy209(seeds=30, seeds800=10):
    """F183 (O8): coste con la pelea viva y los valores por defecto de hoy. Copias de t96-scale-400, t96-400-arcos
    (preset t209-o8-hoy, n = 30) y t96-scale-800 (arena:160, preset aparte t209-o8-hoy-800, n = 10: un preset lleva
    un solo n), todas + reorder:30. Sin mods, seed 17800, minutes 3; los dos presets en una sola llamada."""
    d = load()
    ro = {'tags': ['reorder:30']}
    rows = (('t209-400-mixto', 't96-scale-400', ro), ('t209-400-arcos', 't96-400-arcos', ro),
            ('t209-800-mixto', 't96-scale-800', ro))
    presets = _copies(d, rows, 't209-', 'F183', lambda sid: 't209-o8-hoy-800' if '800' in sid else 't209-o8-hoy', seeds)
    d['presets']['t209-o8-hoy-800']['seeds'] = seeds800
    save(d); print(','.join(presets))


def gen_avanzar210(seeds=20):
    """F184 (O3): AVANZAR a los 90 s sin fuego. Molde exacto de gen_avanzar207 salvo role shield (todos escudos),
    distance 140 (arena 160), redwait:150 y minutes 3; control t210-av-b8-fin = t210-av-b8 + unitInstr:U1=ESPERAR@45
    (unit por defecto 8: U1 = los 8 guardias; ESPERAR llama a InstructionRunner.release -> end:released a ~45 s).
    Un preset t210-avanzar-90s-sinfuego, n = 20, sin mods, seed 17000."""
    d = load()
    tags = ['instruction:AVANZAR', 'instructionAt:0', 'nostuck', 'measuredist', 'redwait:150']
    rows = tuple((f't210-av-b{b}{suf}', 't52-escape-vindicadores80-linea-pelear',
                  {'tags': tags + extra, 'blue': b, 'red': 8, 'role': 'shield', 'minutes': 3, 'distance': 140})
                 for b, suf, extra in ((8, '', []), (100, '', []), (8, '-fin', ['unitInstr:U1=ESPERAR@45'])))
    presets = _copies(d, rows, 't210-', 'F184', lambda sid: 't210-avanzar-90s-sinfuego', seeds)
    save(d); print(','.join(presets))


def gen_reparto212(seeds=30):
    """F186 (O7): cuanto dano se tira en exceso e i-frames. Copias exactas de los controles de F96 (t130-desigual-capa-8 /
    -indiv-8, arco) y de F162 (t189-gv8-nr / -nr-solo, cuerpo a cuerpo). Medidas overkillDmg, overkillKills, tgtMax,
    tgtTop, tgtNone, tgtN (SimCombatStats). Dos presets (una seed por llamada): t212-reparto-arco (seed 12300) y
    t212-reparto-cac (seed 15100), n = 30, sin mods."""
    d = load()
    rows = (('t212-rep-arco-capa', 't130-desigual-capa-8', {}), ('t212-rep-arco-indiv', 't130-desigual-indiv-8', {}),
            ('t212-rep-cac-capa', 't189-gv8-nr', {}), ('t212-rep-cac-solo', 't189-gv8-nr-solo', {}))
    presets = _copies(d, rows, 't212-', 'F186',
                      lambda sid: 't212-reparto-arco' if '-arco-' in sid else 't212-reparto-cac', seeds)
    save(d); print(','.join(presets))


def gen_duelosync221(seeds=30):
    """F198 (O7): golpes escalonados en duelo directo, los dos bandos con Rally. Copias de t169-gvg-espejo-8 (8 Rally
    por bando, rango 1, off:morale, probe:hp; mismo equipo que t189-gv8-nr pero sin norally:red) + off:lowRetreat (vale
    para los dos bandos: FieldManual.simNoLowRetreat es global). espejo = control; capa-solo = off:group:red;
    sync-solo = melee:sync + off:group:red; sync-sync = melee:sync; 16-sync-solo = sync-solo con 16 por bando.
    Un preset t221-duelo-sync, n = 30, sin mods, seed 19400."""
    d = load()
    base, nr, ms, solo = 't169-gvg-espejo-8', ['off:lowRetreat'], ['melee:sync'], ['off:group:red']
    rows = (('t221-espejo', base, {'tags': nr}),
            ('t221-capa-solo', base, {'tags': nr + solo}),
            ('t221-sync-solo', base, {'tags': nr + ms + solo}),
            ('t221-sync-sync', base, {'tags': nr + ms}),
            ('t221-16-sync-solo', base, {'tags': nr + ms + solo, 'blue': 16, 'red': 16}))
    presets = _copies(d, rows, 't221-', 'F198', lambda sid: 't221-duelo-sync', seeds)
    save(d); print(','.join(presets))


def gen_o8desfase222(seeds=30):
    """F199 (O8): primera andanada desfasada. Copia exacta de t218-400-arcos (t222-base) y + fire:stagger
    (t222-desfase: RangedFireGoal.start usa cooldown = 10 + floorMod(id, 20)). Medidas nuevas en Perf:
    rally_p99_late, rally_worst_late, srv_p99_late (sin los primeros 200 ticks del regimen). Un preset
    t222-o8-desfase, n = 30, sin mods, seed 17800."""
    d = load()
    rows = (('t222-base', 't218-400-arcos', {}), ('t222-desfase', 't218-400-arcos', {'tags': ['fire:stagger']}))
    presets = _copies(d, rows, 't222-', 'F199', lambda sid: 't222-o8-desfase', seeds)
    save(d); print(','.join(presets))


def gen_flanquear223(seeds=20):
    """F200 (O3): FLANQUEAR por unidades con el rojo a la vista. Copias exactas de t211-fl-b8 / t211-fl-b100 con la
    distancia inicial 36 (antes 100; arena:160). Un preset t223-flanquear-vista, n = 20, sin mods, seed 17400."""
    d = load()
    rows = (('t223-fl-b8', 't211-fl-b8', {'distance': 36}), ('t223-fl-b100', 't211-fl-b100', {'distance': 36}))
    presets = _copies(d, rows, 't223-', 'F200', lambda sid: 't223-flanquear-vista', seeds)
    save(d); print(','.join(presets))


def gen_acmuestra224(seeds=8):
    """F201 (O9): muestreo de la pila del servidor con Alex's Caves. Copia exacta de t149-tope-b100 (100 v 100,
    arena:160) + sample:5 (SimSampler). Un preset t224-ac-muestra, n = 8, seed 10700; dos despachos: mods = L +
    alexscaves y mods = L (control), con >= 2 min entre ellos."""
    d = load()
    rows = (('t224-ac-muestra', 't149-tope-b100', {'tags': ['sample:5']}),)
    presets = _copies(d, rows, 't224-', 'F201', lambda sid: 't224-ac-muestra', seeds)
    save(d); print(','.join(presets))

def gen_syncsuperior225(seeds=30):
    """F202 (O7): golpes escalonados con superioridad, Rally contra Rally. Copias de t221-espejo (mirror, off:morale,
    probe:hp, off:lowRetreat) con red = 8 y off:group:red en los cuatro brazos; azul 12 o 16; solo cambia melee:sync.
    Un preset t225-sync-superior, n = 30, sin mods, seed 19800."""
    d = load()
    base, solo, ms = 't221-espejo', ['off:group:red'], ['melee:sync']
    rows = (('t225-12-base', base, {'tags': solo, 'blue': 12, 'red': 8}),
            ('t225-12-sync', base, {'tags': solo + ms, 'blue': 12, 'red': 8}),
            ('t225-16-base', base, {'tags': solo, 'blue': 16, 'red': 8}),
            ('t225-16-sync', base, {'tags': solo + ms, 'blue': 16, 'red': 8}))
    presets = _copies(d, rows, 't225-', 'F202', lambda sid: 't225-sync-superior', seeds)
    save(d); print(','.join(presets))

def gen_acsuelo226(seeds=8):
    """F204 (O9): ¿son los objetos del suelo? Copias de t149-tope-b100 (= t224-ac-muestra con sample:10 en lugar de
    sample:5); el segundo brazo añade noloot (reglas doMobLoot / doEntityDrops en false durante la corrida). Las claves
    items* (SimGround) van en los dos. Un preset t226-ac-suelo, n = 8, seed 10700, mods = lista de AC escrita."""
    d = load()
    rows = (('t226-base', 't149-tope-b100', {'tags': ['sample:10']}),
            ('t226-noloot', 't149-tope-b100', {'tags': ['sample:10', 'noloot']}))
    presets = _copies(d, rows, 't226-', 'F204', lambda sid: 't226-ac-suelo', seeds)
    save(d); print(','.join(presets))

def gen_acsuelo232(seeds=8):
    """F210 (O9): ¿qué objetos caen aún con noloot, y el suelo vacío de verdad? Tres brazos como gen_acsuelo226:
    base, noloot, y nodrop (SimGround cancela la entrada de todo ItemEntity en la caja de la arena; sin noloot). Las
    claves itemTop:* van en los tres. Un preset t232-ac-suelo2, n = 8, seed 10700, mods = lista de AC escrita."""
    d = load()
    rows = (('t232-base', 't149-tope-b100', {'tags': ['sample:10']}),
            ('t232-noloot', 't149-tope-b100', {'tags': ['sample:10', 'noloot']}),
            ('t232-nodrop', 't149-tope-b100', {'tags': ['sample:10', 'nodrop']}))
    presets = _copies(d, rows, 't232-', 'F210', lambda sid: 't232-ac-suelo2', seeds)
    save(d); print(','.join(presets))

def gen_sesgo227(seeds=30):
    """F205 (O7): Rally contra GV sin sesgo de lado. gv-gv = t189-gv8-nr + norally + norally:fight (GV contra GV);
    sync-gv = t217-cac-sync (Rally + melee:sync azul contra GV rojo); gv-sync = t169-gvg-espejo-8 + off:lowRetreat +
    melee:sync + norally + norally:fight (GV azul contra Rally + melee:sync rojo). Un preset t227-sesgo, n = 30, sin
    mods, seed 21000."""
    d = load()
    nf = ['norally', 'norally:fight']
    rows = (('t227-gv-gv', 't189-gv8-nr', {'tags': nf}),
            ('t227-sync-gv', 't217-cac-sync', {}),
            ('t227-gv-sync', 't169-gvg-espejo-8', {'tags': ['off:lowRetreat', 'melee:sync'] + nf}))
    presets = _copies(d, rows, 't227-', 'F205', lambda sid: 't227-sesgo', seeds)
    save(d); print(','.join(presets))

def gen_spreadsuperior228(seeds=30):
    """F206 (O7): repartir el blanco cuerpo a cuerpo sobre melee:sync, Rally contra Rally con superioridad. Copias de
    t221-espejo como gen_syncsuperior225 (red = 8, off:group:red); azul 12 o 16; el brazo spread añade melee:spread.
    Un preset t228-spread-superior, n = 30, sin mods, seed 21400."""
    d = load()
    base, solo, ms = 't221-espejo', ['off:group:red', 'melee:sync'], ['melee:spread']
    rows = (('t228-12-sync', base, {'tags': solo, 'blue': 12, 'red': 8}),
            ('t228-12-spread', base, {'tags': solo + ms, 'blue': 12, 'red': 8}),
            ('t228-16-sync', base, {'tags': solo, 'blue': 16, 'red': 8}),
            ('t228-16-spread', base, {'tags': solo + ms, 'blue': 16, 'red': 8}))
    presets = _copies(d, rows, 't228-', 'F206', lambda sid: 't228-spread-superior', seeds)
    save(d); print(','.join(presets))

def gen_regsync229(seeds=30):
    """F207 (O7): la regresión completa con melee:sync puesto. Como gen_o1_76: por cada preset de tools/regresion.py
    un preset t229-<nombre> con copias exactas t229-sync-<id> de cada escenario y solo melee:sync añadida; semillas
    regresion.SEEDS y minutes 12. Se compara con `python3 tools/regresion.py check t229-reg- t229-sync-`. Seed 21800,
    sin mods, los cuatro presets en una sola llamada."""
    d = load()
    ms = {'tags': ['melee:sync']}
    sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
    import regresion
    rows, reg_of = [], {}
    for name, group in regresion.SUITE.items():
        for sid, _ in group:
            rows.append((f't229-sync-{sid}', sid, ms))
            reg_of[f't229-sync-{sid}'] = 't229-' + name
    presets = _copies(d, rows, 't229-', 'F207', lambda sid: reg_of[sid], seeds)
    for name in regresion.SUITE:
        presets['t229-' + name].update(seeds=regresion.SEEDS, minutes=12)
    save(d); print(','.join(presets))

def gen_flanquear230(seeds=20):
    """F208 (O3): FLANQUEAR a 8 con un rojo que dure. Copias exactas de t223-fl-b8 cambiando solo red.count (8 -> 16
    y 8 -> 24); redwait:40, distancia 36 y arena 160 se quedan. Un preset t230-flanquear-dura, n = 20, sin mods,
    seed 17400 (la de F200)."""
    d = load()
    rows = (('t230-fl-r16', 't223-fl-b8', {'red': 16}), ('t230-fl-r24', 't223-fl-b8', {'red': 24}))
    presets = _copies(d, rows, 't230-', 'F208', lambda sid: 't230-flanquear-dura', seeds)
    save(d); print(','.join(presets))

def gen_spreadsuperior231(seeds=30):
    """F209 (O7): 2.º lote de melee:spread, copia exacta de gen_spreadsuperior228 con prefijo t231- y otra semilla.
    Un preset t231-spread-superior, n = 30, sin mods, seed 22600."""
    d = load()
    base, solo, ms = 't221-espejo', ['off:group:red', 'melee:sync'], ['melee:spread']
    rows = (('t231-12-sync', base, {'tags': solo, 'blue': 12, 'red': 8}),
            ('t231-12-spread', base, {'tags': solo + ms, 'blue': 12, 'red': 8}),
            ('t231-16-sync', base, {'tags': solo, 'blue': 16, 'red': 8}),
            ('t231-16-spread', base, {'tags': solo + ms, 'blue': 16, 'red': 8}))
    presets = _copies(d, rows, 't231-', 'F209', lambda sid: 't231-spread-superior', seeds)
    save(d); print(','.join(presets))

def gen_dueloescala233(seeds=30):
    """F211 (O7): duelo de iguales a escala. Copia de gen_duelosync221 (base t169-gvg-espejo-8 + off:lowRetreat); solo
    cambia el número por bando (32 y 64). espejo = control; sync-solo = melee:sync + off:group:red. Arena por defecto
    (96; t96-scale-400 corre 200 v 200 en ella). Un preset t233-duelo-escala, n = 30, sin mods, seed 23000."""
    d = load()
    base, nr, ms, solo = 't169-gvg-espejo-8', ['off:lowRetreat'], ['melee:sync'], ['off:group:red']
    rows = tuple((f't233-{n}-{arm}', base, {'tags': nr + (ms + solo if arm == 'sync-solo' else []), 'blue': n, 'red': n})
                 for n in (32, 64) for arm in ('espejo', 'sync-solo'))
    presets = _copies(d, rows, 't233-', 'F211', lambda sid: 't233-duelo-escala', seeds)
    save(d); print(','.join(presets))

def gen_flanquear234(seeds=20):
    """F212 (O3): FLANQUEAR a 8 con un rojo de 40 y 56. Copia de gen_flanquear230 cambiando solo red.count (40 y 56);
    redwait:40, distancia 36 y arena 160 se quedan (base t223-fl-b8). Un preset t234-flanquear-masa, n = 20, sin mods,
    seed 17400 (la de F200 / F208)."""
    d = load()
    rows = (('t234-fl-r40', 't223-fl-b8', {'red': 40}), ('t234-fl-r56', 't223-fl-b8', {'red': 56}))
    presets = _copies(d, rows, 't234-', 'F212', lambda sid: 't234-flanquear-masa', seeds)
    save(d); print(','.join(presets))

def gen_dueloescala235(seeds=30):
    """F213 (O7): duelo a escala con los lados cambiados. Copia de gen_dueloescala233 (base t169-gvg-espejo-8 +
    off:lowRetreat, arena por defecto) con un tercer brazo: solo-sync = melee:sync + off:group (azul individual, Rally
    completo en rojo). Un preset t235-duelo-lados, n = 30, sin mods, seed 23100."""
    d = load()
    base, nr = 't169-gvg-espejo-8', ['off:lowRetreat']
    arms = {'espejo': [], 'sync-solo': ['melee:sync', 'off:group:red'], 'solo-sync': ['melee:sync', 'off:group']}
    rows = tuple((f't235-{n}-{arm}', base, {'tags': nr + t, 'blue': n, 'red': n})
                 for n in (32, 64) for arm, t in arms.items())
    presets = _copies(d, rows, 't235-', 'F213', lambda sid: 't235-duelo-lados', seeds)
    save(d); print(','.join(presets))

def gen_costesync236(seeds=30):
    """F214 (O8): coste de melee:sync a 400 cuerpo a cuerpo. Copias de t96-400-melee + reorder:30 (pelea viva, como
    F183), sin y con melee:sync. Un preset t236-o8-sync, n = 30, minutes 3, sin mods, seed 23200."""
    d = load()
    rows = (('t236-400-melee', 't96-400-melee', {'tags': ['reorder:30']}),
            ('t236-400-melee-sync', 't96-400-melee', {'tags': ['reorder:30', 'melee:sync']}))
    presets = _copies(d, rows, 't236-', 'F214', lambda sid: 't236-o8-sync', seeds)
    for p in presets.values():
        p['minutes'] = 3
    save(d); print(','.join(presets))

def gen_coste600237(seeds=15):
    """F215 (O8): coste a 600 en total (meta de David, 6 x 100). Copias de t96-scale-800 (mirror, mixto, arena:160,
    unit:10) con blue = 300 (300 v 300) + reorder:30 (pelea viva, como F183), sin y con melee:sync. Un preset
    t237-o8-600, n = 15, minutes 3, sin mods, seed 23300."""
    d = load()
    rows = (('t237-600-mixto', 't96-scale-800', {'blue': 300, 'tags': ['reorder:30']}),
            ('t237-600-mixto-sync', 't96-scale-800', {'blue': 300, 'tags': ['reorder:30', 'melee:sync']}))
    presets = _copies(d, rows, 't237-', 'F215', lambda sid: 't237-o8-600', seeds)
    for p in presets.values():
        p['minutes'] = 3
    save(d); print(','.join(presets))

if __name__ == '__main__':
    cmd = sys.argv[1]
    if cmd == 'gen-coste600237':
        gen_coste600237()
    if cmd == 'gen-dueloescala235':
        gen_dueloescala235()
    if cmd == 'gen-costesync236':
        gen_costesync236()
    if cmd == 'gen-flanquear234':
        gen_flanquear234()
    if cmd == 'gen-dueloescala233':
        gen_dueloescala233()
    if cmd == 'gen-acsuelo232':
        gen_acsuelo232()
    if cmd == 'gen-spreadsuperior231':
        gen_spreadsuperior231()
    if cmd == 'gen-regsync229':
        gen_regsync229()
    if cmd == 'gen-flanquear230':
        gen_flanquear230()
    if cmd == 'gen-spreadsuperior228':
        gen_spreadsuperior228()
    if cmd == 'gen-sesgo227':
        gen_sesgo227()
    if cmd == 'gen-acsuelo226':
        gen_acsuelo226()
    if cmd == 'gen-syncsuperior225':
        gen_syncsuperior225()
    if cmd == 'gen-duelosync221':
        gen_duelosync221()
    if cmd == 'gen-o8desfase222':
        gen_o8desfase222()
    if cmd == 'gen-flanquear223':
        gen_flanquear223()
    if cmd == 'gen-acmuestra224':
        gen_acmuestra224()
    if cmd == 'gen-cacescala220':
        gen_cacescala220()
    if cmd == 'gen-cacsync217':
        gen_cacsync217()
    if cmd == 'gen-o8anatomia218':
        gen_o8anatomia218()
    if cmd == 'gen-o8rgpartes216':
        gen_o8rgpartes216()
    if cmd == 'gen-o8planmemo215':
        gen_o8planmemo215()
    if cmd == 'gen-o8planmemo214':
        gen_o8planmemo214()
    if cmd == 'gen-o8partes213':
        gen_o8partes213()
    if cmd == 'gen-reparto212':
        gen_reparto212()
    if cmd == 'gen-avanzar210':
        gen_avanzar210()
    if cmd == 'gen-flanquear211':
        gen_flanquear211()
    if cmd == 'gen-flanquear208':
        gen_flanquear208()
    if cmd == 'gen-o8hoy209':
        gen_o8hoy209()
    if cmd == 'gen-avanzar207':
        gen_avanzar207()
    if cmd == 'gen-flanquear206':
        gen_flanquear206()
    if cmd == 'gen-avanzar205':
        gen_avanzar205()
    if cmd == 'gen-avanzar204':
        gen_avanzar204()
    if cmd == 'gen-mantener203':
        gen_mantener203()
    if cmd == 'gen-mantener202':
        gen_mantener202()
    if cmd == 'gen-mantener201':
        gen_mantener201()
    if cmd == 'gen-mantener200':
        gen_mantener200()
    if cmd == 'gen-norule199':
        gen_norule199()
    if cmd == 'gen-retirada198':
        gen_retirada198()
    if cmd == 'gen-retirada197':
        gen_retirada197()
    if cmd == 'gen-escolta196':
        gen_escolta196()
    if cmd == 'gen-reorder193':
        gen_reorder193()
    if cmd == 'gen-closer194':
        gen_closer194()
    if cmd == 'gen-emboscada195':
        gen_emboscada195()
    if cmd == 'gen-emboscada190':
        gen_emboscada190()
    if cmd == 'gen-gvg189':
        gen_gvg189()
    if cmd == 'gen-split188':
        gen_split188()
    if cmd == 'gen-gvg186':
        gen_gvg186()
    if cmd == 'gen-emboscada187':
        gen_emboscada187()
    if cmd == 'gen-closer185':
        gen_closer185()
    if cmd == 'gen-emboscada184':
        gen_emboscada184()
    if cmd == 'gen-aldea183':
        gen_aldea183()
    if cmd == 'gen-gvg182':
        gen_gvg182()
    if cmd == 'gen-nostuck181':
        gen_nostuck181()
    if cmd == 'gen-closer177':
        gen_closer177()
    if cmd == 'gen-aldea180':
        gen_aldea180()
    if cmd == 'gen-escudos179':
        gen_escudos179()
    if cmd == 'gen-cebo178':
        gen_cebo178()
    if cmd == 'gen-closer176':
        gen_closer176()
    if cmd == 'gen-lod175':
        gen_lod175()
    if cmd == 'gen-zombie174':
        gen_zombie174()
    if cmd == 'gen-closer173':
        gen_closer173()
    if cmd == 'gen-fuerza172':
        gen_fuerza172()
    if cmd == 'gen-apply171':
        gen_apply171()
    if cmd == 'gen-multiplicador169':
        gen_multiplicador169()
    if cmd == 'gen-dispersion170':
        gen_dispersion170()
    if cmd == 'gen-ladder167':
        gen_ladder167()
    if cmd == 'gen-dispersion168':
        gen_dispersion168()
    if cmd == 'gen-dispersion162':
        gen_dispersion162()
    if cmd == 'gen-escala164':
        gen_escala164()
    if cmd == 'gen-cebo165':
        gen_cebo165()
    if cmd == 'gen-cebo163':
        gen_cebo163()
    if cmd == 'gen-cebo161':
        gen_cebo161()
    if cmd == 'gen-cebo160':
        gen_cebo160()
    if cmd == 'gen-foco156':
        gen_foco156()
    if cmd == 'gen-retirada166':
        gen_retirada166()
    if cmd == 'gen-retirada159':
        gen_retirada159()
    if cmd == 'gen-lod157':
        gen_lod157()
    if cmd == 'gen-por-partes158':
        gen_porpartes158()
    if cmd == 'gen-por-partes155':
        gen_porpartes155()
    if cmd == 'gen-municion151':
        gen_municion151()
    if cmd == 'gen-por-partes152':
        gen_porpartes152()
    if cmd == 'gen-municion150':
        gen_municion150()
    if cmd == 'gen-tope149':
        gen_tope149()
    if cmd == 'gen-altura148':
        gen_altura148()
    if cmd == 'gen-jefes147':
        gen_jefes147()
    if cmd == 'gen-jefes143':
        gen_jefes143()
    if cmd == 'gen-altura146':
        gen_altura146()
    if cmd == 'gen-altura144':
        gen_altura144()
    if cmd == 'gen-retirada140':
        gen_retirada140()
    if cmd == 'gen-jefes141':
        gen_jefes141()
    if cmd == 'gen-porpartescalma139':
        gen_porpartescalma139()
    if cmd == 'gen-cabvelocidad136':
        gen_cabvelocidad136()
    if cmd == 'gen-porpartescorto137':
        gen_porpartescorto137()
    if cmd == 'gen-foco135':
        gen_foco135()
    if cmd == 'gen-outnumbered133':
        gen_outnumbered133()
    if cmd == 'gen-termopilas134':
        gen_termopilas134()
    if cmd == 'gen-porpartes132':
        gen_porpartes132()
    if cmd == 'gen-tope131':
        gen_tope131()
    if cmd == 'gen-desigual130':
        gen_desigual130()
    if cmd == 'gen-retirada127':
        gen_retirada127()
    if cmd == 'gen-veterania138':
        gen_veterania138()
    if cmd == 'gen-veterania128':
        gen_veterania128()
    if cmd == 'gen-caballeria129':
        gen_caballeria129()
    if cmd == 'gen-persecucion125':
        gen_persecucion125()
    if cmd == 'gen-veterania124':
        gen_veterania124()
    if cmd == 'gen-tope122':
        gen_tope122()
    if cmd == 'gen-persecucion123':
        gen_persecucion123()
    if cmd == 'gen-persecucion121':
        gen_persecucion121()
    if cmd == 'gen-foco119':
        gen_foco119()
    if cmd == 'gen-espejo120':
        gen_espejo120()
    elif cmd == 'gen-espejo126':
        gen_espejo126()
    if cmd == 'gen-retirada118':
        gen_retirada118()
    if cmd == 'gen-piezas117':
        gen_piezas117()
    if cmd == 'gen-ablacion115':
        gen_ablacion115()
    if cmd == 'gen-tope116':
        gen_tope116()
    if cmd == 'gen-base400-114':
        gen_base400_114()
    if cmd == 'gen-gvimpactos113':
        gen_gvimpactos113()
    if cmd == 'gen-struct29':
        gen_struct29(sys.argv[2])
    elif cmd == 'gen-bosses28':  # gen-bosses28 <ronda> [tamaños, p. ej. 8 o 16,32,64]
        gen_bosses28(sys.argv[2], sizes=tuple(int(x) for x in sys.argv[3].split(',')) if len(sys.argv) > 3 else (16, 32, 64))
    elif cmd == 'gen-kite27':
        gen_kite27(sys.argv[2])
    elif cmd == 'gen-bestiary26':
        gen_bestiary26(sys.argv[2])
    elif cmd == 'gen-bestiary25':
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
    elif cmd == 'gen-o1o4-67':
        gen_o1o4_67()
    elif cmd == 'gen-pinza69':
        gen_pinza69()
    elif cmd == 'gen-aldea70':
        gen_aldea70()
    elif cmd == 'gen-cab71':
        gen_cab71()
    elif cmd == 'gen-tremor72':
        gen_tremor72()
    elif cmd == 'gen-ignis73':
        gen_ignis73()
    elif cmd == 'gen-o1-74':
        gen_o1_74()
    elif cmd == 'gen-ignis75':
        gen_ignis75()
    elif cmd == 'gen-o1-76':
        gen_o1_76()
    elif cmd == 'gen-aldea77':
        gen_aldea77()
    elif cmd == 'gen-cab78':
        gen_cab78()
    elif cmd == 'gen-cab79':
        gen_cab79()
    elif cmd == 'gen-o1-80':
        gen_o1_80()
    elif cmd == 'gen-aldea81':
        gen_aldea81()
    elif cmd == 'gen-tremor83':
        gen_tremor83()
    elif cmd == 'gen-aldea84':
        gen_aldea84()
    elif cmd == 'gen-reg85':
        gen_reg85()
    elif cmd == 'gen-aldea86':
        gen_aldea86()
    elif cmd == 'gen-tremor87':
        gen_tremor87()
    elif cmd == 'gen-def88':
        gen_def88()
    elif cmd == 'gen-aldea89':
        gen_aldea89()
    elif cmd == 'gen-refugio90':
        gen_refugio90()
    elif cmd == 'gen-aldea91':
        gen_aldea91()
    elif cmd == 'gen-refugio92':
        gen_refugio92()
    elif cmd == 'gen-escala93':
        gen_escala93()
    elif cmd == 'gen-escala94':
        gen_escala94()
    elif cmd == 'gen-escala96':
        gen_escala96()
    elif cmd == 'gen-fuego95':
        gen_fuego95()
    elif cmd == 'gen-refugio97':
        gen_refugio97()
    elif cmd == 'gen-escala98':
        gen_escala98()
    elif cmd == 'gen-escala99':
        gen_escala99()
    elif cmd == 'gen-escala100':
        gen_escala100()
    elif cmd == 'gen-refugio101':
        gen_refugio101()
    elif cmd == 'gen-aldea102':
        gen_aldea102()
    elif cmd == 'gen-bosque103':
        gen_bosque103()
    elif cmd == 'gen-aldea104':
        gen_aldea104()
    elif cmd == 'gen-bosque105':
        gen_bosque105()
    elif cmd == 'gen-aldea106':
        gen_aldea106()
    elif cmd == 'gen-refugio107':
        gen_refugio107()
    elif cmd == 'gen-cobertura108':
        gen_cobertura108()
    elif cmd == 'gen-escala109':
        gen_escala109()
    elif cmd == 'gen-gvdispara112':
        gen_gvdispara112()
    elif cmd == 'gen-multiplicador111':
        gen_multiplicador111()
    elif cmd == 'gen-aldea110':
        gen_aldea110()
    elif cmd == 'gen-o2-68':
        gen_o2_68()
    elif cmd == 'gen-cebo64':
        gen_cebo64()
    elif cmd == 'gen-retaguardia66':
        gen_retaguardia66()
    elif cmd == 'gen-tremor65':
        gen_tremor65()
    elif cmd == 'gen-units56':
        gen_units56()
    elif cmd == 'gen-units56b':
        gen_units56b()
    elif cmd == 'gen-ignis58':
        gen_ignis58()
    elif cmd == 'gen-o1-63':
        gen_o1_63()
    elif cmd == 'gen-jefes62':
        gen_jefes62()
    elif cmd == 'gen-jefes61':
        gen_jefes61()
    elif cmd == 'gen-tremor60':
        gen_tremor60()
    elif cmd == 'gen-horde57':
        gen_horde57()
    elif cmd == 'gen-relay55':
        gen_relay55()
    elif cmd == 'gen-relay55b':
        gen_relay55b()
    elif cmd == 'gen-realstrat54':
        gen_realstrat54()
    elif cmd == 'gen-village53':
        gen_village53()
    elif cmd == 'gen-escape52':
        gen_escape52()
    elif cmd == 'gen-melee51':
        gen_melee51()
    elif cmd == 'gen-horse50':
        gen_horse50()
    elif cmd == 'gen-rider49':
        gen_rider49()
    elif cmd == 'gen-confirm48':
        gen_confirm48()
    elif cmd == 'gen-attraction47':
        gen_attraction47()
    elif cmd == 'gen-cavalry46':
        gen_cavalry46()
    elif cmd == 'gen-retreat44':
        gen_retreat44()
    elif cmd == 'gen-standoff45':
        gen_standoff45()
    elif cmd == 'gen-coordceiling43':
        gen_coordceiling43()
    elif cmd == 'gen-numbers42':
        gen_numbers42()
    elif cmd == 'gen-standoff41':
        gen_standoff41()
    elif cmd == 'gen-night37':
        gen_night37()
    elif cmd == 'gen-guns36':
        gen_guns36(sys.argv[2])
    elif cmd == 'gen-numbers35':
        gen_numbers35(sys.argv[2])
    elif cmd == 'gen-morale34':
        gen_morale34(sys.argv[2])
    elif cmd == 'gen-dummy':
        gen_dummy(sys.argv[2])
    elif cmd == 'gen-giants32':
        gen_giants32(sys.argv[2])
    elif cmd == 'gen-giants31':
        gen_giants31(sys.argv[2])
    elif cmd == 'gen-giants30':
        gen_giants30(sys.argv[2])
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

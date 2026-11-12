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


if __name__ == '__main__':
    cmd = sys.argv[1]
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

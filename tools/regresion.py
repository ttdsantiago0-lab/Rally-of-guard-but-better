#!/usr/bin/env python3
"""Suite de regresión (PLAN_MAESTRO etapa 0): una fila por comportamiento confirmado del Manual de campo.

  python3 tools/regresion.py presets     escribe los presets reg-* en scenarios.json (no crea escenarios: reusa los
                                         de las rondas que confirmaron cada comportamiento)
  python3 tools/regresion.py ref [RE]    guarda en tools/regresion-ref.json el valor de referencia de cada fila,
                                         tomado del lote más nuevo que la tenga (o del más nuevo cuyo nombre case RE)
  python3 tools/regresion.py check [RE]  compara el lote reg-* más nuevo (o el que case RE) con la referencia

En la CI: preset "regresion" = los cuatro reg-* en paralelo. Una fila cae si empeora más que el ruido
(2,5 errores estándar combinados) y más que el margen de la métrica (10 puntos; 1 ola; 25 % más daño recibido).
"""
import json, math, os, re, subprocess, sys

HERE = os.path.dirname(os.path.abspath(__file__))
REPO = os.path.dirname(HERE)
SCEN = os.path.join(REPO, 'src', 'main', 'resources', 'rallyguard_sim', 'scenarios.json')
REF = os.path.join(HERE, 'regresion-ref.json')
SEEDS = 8

# fila del manual -> (escenario, métricas que vigila)
SUITE = {
    'reg-cuerpo': [  # quemarropa, creepers, escudo arriba bajo fuego, tirar desde donde están, apartarse del creeper
        *[(f't5-final-{m}-{c}-base', ('win', 'alive'))
          for m in ('zombie', 'skeleton', 'creeper') for c in ('mele', 'escudo', 'arco', 'linea', 'cab')],
        ('t11-skeleton-cab-base', ('win', 'alive')), ('t11-creeper-cab-base', ('win', 'alive')),
    ],
    'reg-olas': [  # olas mezcladas con comida y todo el manual; moral que no asusta sin bajas
        ('t8-waves-mixta-linea-comida-base', ('waves', 'alive')),
        ('t8-waves-mixta-arco-comida-base', ('waves', 'alive')),
        ('t8-waves-mixta-escudo-comida-base', ('waves', 'alive')),
        ('t16-moral-linea-nueva', ('waves', 'alive')), ('t16-moral-r0-nueva', ('waves', 'alive')),
    ],
    'reg-escolta': [  # escolta v1 + pantalla, tortuga y carga, escudo contra lo que escupe
        ('t17-escolta-zombis-linea-defender', ('vip',)), ('t17-escolta-esqueletos-linea-defender', ('vip',)),
        ('t17-escolta-mezcla-linea-defender', ('vip',)), ('t17-escolta-esqueletos-escudo-defender', ('vip',)),
        ('t19-testudo-esqueletos-escudo-testudocarga', ('win', 'alive', 'taken')),
        ('t19-testudo-mezcla-escudo-testudocarga', ('win', 'alive', 'taken')),
        ('t20-amenaza-blaze-escudo-base-r6', ('taken',)), ('t20-amenaza-blaze-escudo-base-r10', ('taken',)),
    ],
    'reg-vanilla': [  # ronda 25: donde el manual se nota
        ('t25-vanilla-hordas-husk-base-r40', ('win', 'alive')),
        ('t25-vanilla-raros-wither_skeleton-base-r12', ('win', 'alive')),
        ('t25-vanilla-tiradores-drowned-base-r24', ('win', 'alive')),
        ('t25-vanilla-raros-vindicator-base-r6', ('win', 'alive')),
        ('t25-vanilla-hordas-spider-base-r24', ('win', 'alive')),
        ('t25-vanilla-tiradores-stray-base-r32', ('win', 'alive')),
    ],
}
# métrica -> (extractor, mayor es mejor, margen absoluto o relativo)
METRICS = {
    'win': (lambda r: 1.0 if r['winner'] == 'blue' else 0.5 if r['winner'] == 'draw' else 0.0, True, 0.10),
    'alive': (lambda r: r['blue']['survivors'] / max(1, r['blue']['start']), True, 0.10),
    'waves': (lambda r: r.get('extra', {}).get('wavesCleared'), True, 1.0),
    'vip': (lambda r: r.get('extra', {}).get('vipArrived'), True, 0.10),
    'taken': (lambda r: r['red']['damage'], False, 0.25),  # daño que nos hicieron; relativo
}


def git(*a):
    return subprocess.run(['git', '-C', REPO, *a], capture_output=True, text=True).stdout


def batches():
    git('fetch', '-q', 'origin', 'sim-results:refs/remotes/origin/sim-results')
    return sorted(b for b in git('ls-tree', '--name-only', 'origin/sim-results').split() if b[:8].isdigit())


def rows(batch):
    out = []
    for line in git('show', f'origin/sim-results:{batch}/runs.jsonl').splitlines():
        try:
            r = json.loads(line)
        except ValueError:
            continue
        if not r.get('control'):
            out.append(r)
    return out


def stats(rs, metric):
    v = [x for x in (METRICS[metric][0](r) for r in rs) if x is not None]
    if not v:
        return None
    m = sum(v) / len(v)
    se = math.sqrt(sum((x - m) ** 2 for x in v) / (len(v) - 1) / len(v)) if len(v) > 1 else 0.0
    return {'mean': round(m, 4), 'se': round(se, 4), 'n': len(v)}


def collect(pattern=None):
    """Para cada escenario de la suite, las corridas del lote más nuevo que lo tiene."""
    want = {sid for group in SUITE.values() for sid, _ in group}
    found = {}
    for b in reversed(batches()):
        if pattern and not re.search(pattern, b):
            continue
        if want <= set(found):
            break
        rs = rows(b)
        for sid in {r['scenario'] for r in rs} & want - set(found):
            found[sid] = (b, [r for r in rs if r['scenario'] == sid])
    return found


def cmd_presets():
    d = json.load(open(SCEN, encoding='utf-8'))
    known = {s['id'] for s in d['scenarios']}
    for name, group in SUITE.items():
        ids = [sid for sid, _ in group]
        missing = [i for i in ids if i not in known]
        if missing:
            sys.exit(f'{name}: faltan escenarios {missing}')
        d['presets'][name] = {'scenarios': ids, 'seeds': SEEDS, 'minutes': 12}
    with open(SCEN, 'w', encoding='utf-8', newline='\n') as f:
        f.write(json.dumps(d, ensure_ascii=False, indent=1) + '\n')  # mismo formato que tools/torneo.py
    print('presets:', ','.join(SUITE))


def cmd_ref(pattern=None):
    found = collect(pattern)
    ref = {}
    for group in SUITE.values():
        for sid, metrics in group:
            if sid not in found:
                print('sin datos:', sid)
                continue
            b, rs = found[sid]
            ref[sid] = {'batch': b, **{m: stats(rs, m) for m in metrics}}
    with open(REF, 'w', encoding='utf-8', newline='\n') as f:
        json.dump(ref, f, ensure_ascii=False, indent=1)
        f.write('\n')
    print(f'referencia: {len(ref)} filas -> {os.path.relpath(REF, REPO)}')


def cmd_check(pattern=r'-reg-'):
    ref = json.load(open(REF, encoding='utf-8'))
    found = collect(pattern)
    bad = 0
    print('| Fila | Métrica | Referencia | Ahora | |')
    print('|---|---|---|---|---|')
    for group in SUITE.values():
        for sid, metrics in group:
            if sid not in found or sid not in ref:
                print(f'| {sid} | — | — | sin datos | ? |')
                continue
            rs = found[sid][1]
            for m in metrics:
                a, b = ref[sid].get(m), stats(rs, m)
                if not a or not b:
                    continue
                _, higher, tol = METRICS[m]
                noise = 2.5 * math.hypot(a['se'], b['se'])
                delta = b['mean'] - a['mean']
                margin = tol * a['mean'] if m == 'taken' else tol
                worse = -delta if higher else delta
                flag = '✗' if worse > max(noise, margin) else '✓'
                bad += flag == '✗'
                fmt = (lambda x: f'{x:.0f}') if m == 'taken' else (lambda x: f'{x:.1f}') if m == 'waves' \
                    else (lambda x: f'{x:.0%}')
                print(f"| {sid} | {m} | {fmt(a['mean'])} (n={a['n']}) | {fmt(b['mean'])} (n={b['n']}) | {flag} |")
    print(f'\n{bad} filas empeoran más que el ruido.' if bad else '\nNinguna fila empeora más que el ruido.')
    return bad


if __name__ == '__main__':
    cmd = sys.argv[1] if len(sys.argv) > 1 else 'check'
    arg = sys.argv[2] if len(sys.argv) > 2 else None
    if cmd == 'presets':
        cmd_presets()
    elif cmd == 'ref':
        cmd_ref(arg)
    elif cmd == 'check':
        sys.exit(1 if cmd_check(arg or r'-reg-') else 0)
    else:
        sys.exit(__doc__)

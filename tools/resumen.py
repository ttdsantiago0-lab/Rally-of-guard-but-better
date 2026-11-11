"""Resumen por escenario de uno o varios runs.jsonl (2026-11-11).
Uso: python3 tools/resumen.py archivo.jsonl [...] [--extra clave1,clave2]
Columnas: n, % victorias azules, % de azules vivos, segundos, y la media de cada clave extra pedida."""
import json, sys
from collections import defaultdict

args = [a for a in sys.argv[1:] if not a.startswith('--')]
extra = []
for a in sys.argv[1:]:
    if a.startswith('--extra='):
        extra = a.split('=', 1)[1].split(',')
rows = defaultdict(list)
for p in args:
    for line in open(p):
        try:
            r = json.loads(line)
        except ValueError:
            continue
        if r.get('control'):
            continue
        rows[r['scenario']].append(r)
print('| escenario | n | victoria | vivos | s | ' + ' | '.join(extra) + ' |')
print('|---|---|---|---|---|' + '---|' * len(extra))
for sid in sorted(rows):
    rs = rows[sid]
    n = len(rs)
    win = 100 * sum(r['winner'] == 'blue' for r in rs) / n
    alive = 100 * sum(r['blue']['survivors'] / max(1, r['blue']['start']) for r in rs) / n
    sec = sum(r['seconds'] for r in rs) / n
    ex = []
    for k in extra:
        v = [r.get('extra', {}).get(k, r.get(k, 0)) or 0 for r in rs]
        v = [x if isinstance(x, (int, float)) else 0 for x in v]
        ex.append(f'{sum(v) / n:.1f}')
    print(f'| {sid} | {n} | {win:.0f} | {alive:.0f} | {sec:.0f} | ' + ' | '.join(ex) + ' |')

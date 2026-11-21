#!/usr/bin/env python3
"""Grafo de diseño (David, 2026-11-20): cada decisión en diseno/Decisiones/ declara `temas:` y `preguntas:` en su
frontmatter. Este script escribe en cada nota de tema la lista de sus decisiones y, en el archivo de preguntas, un
enlace a la decisión al final de cada respuesta. Correrlo después de crear o cambiar decisiones."""
import os, re
R = os.path.join(os.path.dirname(os.path.abspath(__file__)), '..', 'diseno')
A, B = '<!-- decisiones: generado por tools/diseno_grafo.py -->', '<!-- /decisiones -->'

def rng(s):
    out = set()
    for part in re.split(r',\s*', s):
        m = re.match(r'(\d+)(?:-(\d+))?$', part.strip())
        if m:
            out |= set(range(int(m.group(1)), int(m.group(2) or m.group(1)) + 1))
    return out

decs = []
for f in sorted(os.listdir(os.path.join(R, 'Decisiones'))):
    if not f.endswith('.md'):
        continue
    s = open(os.path.join(R, 'Decisiones', f), encoding='utf-8').read()
    fm = s.split('\n---', 1)[0] if s.startswith('---') else ''
    temas = re.search(r'^temas:\s*\[(.*)\]', fm, re.M)
    preg = re.search(r'^preguntas:\s*(.*)$', fm, re.M)
    title = next((l[2:] for l in s.split('\n') if l.startswith('# ')), f[:-3])
    decs.append((f[:-3], [t.strip() for t in temas.group(1).split(',')] if temas else [], rng(preg.group(1)) if preg else set(), title))

OBJ = os.path.join(R, '..', 'docs', 'equipo', 'objetivos')
labs = {}
for f in (sorted(os.listdir(OBJ)) if os.path.isdir(OBJ) else []):
    m = re.search(r'^Temas de diseño: (.*)$', open(os.path.join(OBJ, f), encoding='utf-8').read(), re.M)
    for t in re.findall(r'\[\[(.+?)\]\]', m.group(1) if m else ''):
        labs.setdefault(t, []).append(f[:-3])

for t in os.listdir(os.path.join(R, 'Temas')):
    p = os.path.join(R, 'Temas', t); name = t[:-3]
    s = open(p, encoding='utf-8').read()
    mine = [d for d in decs if name in d[1]]
    block = A + '\n## Decisiones\n' + ('\n'.join(f'- [[{d[0]}]]' for d in mine) if mine else '- (ninguna todavía)')
    if labs.get(name):
        block += '\n\n## En el laboratorio\n' + ' · '.join(f'[[{o}]]' for o in labs[name]) + ' (objetivos del equipo, con sus fichas)'
    block += '\n' + B
    s = re.sub(re.escape(A) + r'.*?' + re.escape(B), block, s, flags=re.S) if A in s else s.replace('\n## Lo abierto', '\n' + block + '\n\n## Lo abierto', 1) if '\n## Lo abierto' in s else s.rstrip('\n') + '\n\n' + block
    open(p, 'w', encoding='utf-8').write(s.rstrip('\n') + '\n')

q = os.path.join(R, 'Preguntas de diseño 2026-11.md')
lines = open(q, encoding='utf-8').read().split('\n'); n = None
for i, l in enumerate(lines):
    m = re.match(r'^(\d+)\. ', l)
    if m:
        n = int(m.group(1))
    elif n and l.strip().startswith('- **R:**') and l.strip() != '- **R:**':
        links = ' · '.join(f'[[{d[0]}]]' for d in decs if n in d[2])
        l = re.sub(r' → \[\[D-.*$', '', l)
        lines[i] = l + (f' → {links}' if links else '')
open(q, 'w', encoding='utf-8').write('\n'.join(lines))
print(f'diseno_grafo: {len(decs)} decisiones enlazadas')

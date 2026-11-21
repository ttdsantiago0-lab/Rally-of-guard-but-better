#!/usr/bin/env python3
"""Cola del equipo como grafo de notas (David, 2026-11-20).

Cada ficha es una nota (docs/equipo/fichas/<id>.md), cada objetivo otra (docs/equipo/objetivos/<O>.md), y la bitácora
de turnos e indicaciones de David van por día (docs/equipo/turnos/, docs/equipo/indicaciones/). COLA.md queda como
índice corto. Así un turno lee el índice y abre solo las notas que necesita, y Obsidian dibuja el mismo grafo.

Uso:  python3 tools/cola_grafo.py      (migra lo escrito en formato antiguo debajo del índice y regenera índice y objetivos)
"""
import os, re, sys, collections

EQ = os.path.join(os.path.dirname(os.path.abspath(__file__)), '..', 'docs', 'equipo')
COLA = os.path.join(EQ, 'COLA.md')
D_F, D_O, D_T, D_I = (os.path.join(EQ, d) for d in ('fichas', 'objetivos', 'turnos', 'indicaciones'))
FIN = '<!-- fin del índice: lo que se escriba debajo de esta línea se migra a notas con tools/cola_grafo.py -->'
TEMAS = {'O1': ['Armas de fuego'], 'O2': ['Armas de fuego', 'Aldeanos y población'], 'O3': ['Retirada'],
         'O4': ['Caballería'], 'O5': ['Jefes'], 'O6': ['Jefes'], 'O7': ['Motor táctico'], 'O8': ['Simulación']}
FID = r'F\d+[a-z]?(?:/F\d+[a-z]?)?(?: v\d+)?'
DATE = re.compile(r'(\d{4}-\d{2}-\d{2})')

def fname(fid):
    return fid.replace('/', '-')

def read(p):
    return open(p, encoding='utf-8').read() if os.path.exists(p) else ''

def write(p, s):
    os.makedirs(os.path.dirname(p), exist_ok=True)
    open(p, 'w', encoding='utf-8').write(s.rstrip('\n') + '\n')

def append(p, s, header):
    old = read(p)
    write(p, (old.rstrip('\n') + '\n\n' if old else header + '\n\n') + s.strip('\n'))

def split_blocks(text, level):
    """Splits text into (heading, body) at headings of exactly `level` hashes; the first item has heading None."""
    pat = re.compile(r'^' + '#' * level + r' (?!#)', re.M)
    idx = [m.start() for m in pat.finditer(text)]
    out = [(None, text[:idx[0]] if idx else text)]
    for a, b in zip(idx, idx[1:] + [len(text)]):
        h, _, body = text[a:b].partition('\n')
        out.append((h[level + 1:].strip(), body))
    return out

def migrate(legacy, start_date):
    """Moves legacy-format text (## sections with ### fichas) into notes."""
    day = start_date
    for sec, body in split_blocks(legacy, 2):
        if sec is not None:
            m = DATE.search(sec)
            if m:
                day = m.group(1)
        is_ind = sec is not None and sec.lower().startswith('indicaciones')
        log_path = os.path.join(D_I if is_ind else D_T, ('Indicaciones ' if is_ind else 'Turnos ') + day + '.md')
        log_name = os.path.splitext(os.path.basename(log_path))[0]
        log_header = f'# {log_name}\n\nBitácora migrada de `COLA.md`. Las fichas citadas están en `docs/equipo/fichas/`.'
        parts = split_blocks(body, 3)
        prose = parts[0][1].strip('\n')
        rest = []
        for h, b in parts[1:]:
            m_new = re.match(r'(' + FID + r')\s*\[', h)
            m_any = re.match(r'(' + FID + r')\b', h)
            if m_new:
                fid = m_new.group(1)
                p = os.path.join(D_F, fname(fid) + '.md')
                obj = re.findall(r'\bO\d\b', h.split('→', 1)[1]) if '→' in h else []
                if read(p):   # same id written again: keep it as follow-up
                    append(p, f'### {h}\n_(de [[{log_name}]])_\n{b}', '')
                else:
                    fm = f'---\nid: {fid}\nobjetivo: {", ".join(dict.fromkeys(obj))}\nfecha: {day}\n---\n'
                    links = ' · '.join([f'[[{o}]]' for o in dict.fromkeys(obj)] + [f'escrita en [[{log_name}]]'])
                    write(p, f'{fm}# {h}\n\n{links}\n\n{b.strip()}')
                rest.append(f'- Ficha nueva [[{fname(fid)}]]: {h[len(fid):].strip()}')
            elif m_any:
                fid = m_any.group(1)
                p = os.path.join(D_F, fname(fid) + '.md')
                if read(p):
                    s = read(p)
                    if '\n## Seguimiento' not in s:
                        s = s.rstrip('\n') + '\n\n## Seguimiento'
                    write(p, s.rstrip('\n') + f'\n\n### {h}\n_(de [[{log_name}]])_\n{b.strip()}')
                    rest.append(f'- {h[:110]} → [[{fname(fid)}]]')
                else:
                    rest.append(f'### {h}\n{b.strip()}')
            else:
                rest.append(f'### {h}\n{b.strip()}')
        if sec is None and not prose.strip() and not rest:
            continue
        if sec is not None and sec.strip() == 'Cola' and not prose.strip():
            prose = ''
        chunk = (f'## {sec}\n\n' if sec else '') + prose + ('\n\n' + '\n'.join(rest) if rest else '')
        if chunk.strip():
            append(log_path, chunk, log_header)
    return day

VKEY = re.compile(r'Veredicto(?!, dicho antes)(?! nuevo: ninguno)|APRUEBA|\bNO\b|aprueba|confirmad|cumplid')

def log_verdicts(logs):
    """Verdicts written in the day logs (the analyst often wrote them in its own section): id -> (log, line)."""
    out = {}
    for name, text in logs.items():
        for l in text.split('\n'):
            if not VKEY.search(l) or 'dicho antes' in l or l.startswith('- Ficha nueva'):
                continue
            ids = set(re.findall(r'\bF(\d+)([a-z]?)\b', l))
            for a, b in re.findall(r'\bF(\d+)\s*[–-]\s*F?(\d+)\b', l):
                ids |= {(str(i), '') for i in range(int(a), int(b) + 1)}
            for n, suf in ids:
                out[f'F{n}{suf}'] = (name[:-3], l)
    return out

def verdict_info(text, ref=None):
    vs = [l for l in text.split('\n') if re.search(r'Veredicto(?!, dicho antes)(?! nuevo: ninguno)', l)
          and not l.strip().startswith('- Veredicto, dicho antes')]
    closed = any('**' in l or 'APRUEBA' in l or ' NO' in l for l in vs)
    line = next((l for l in vs if '**' in l), vs[0] if vs else '')
    if not closed and ref:
        return True, ref[1], ref[0]
    return closed, line, None

def summary(line):
    b = re.findall(r'\*\*(.+?)\*\*', line)
    s = b[0] if b else re.sub(r'^[-#\s]*', '', line)
    s = re.sub(r'\s+', ' ', s).replace('|', '/')
    return s[:150] + ('…' if len(s) > 150 else '')

def objetivos():
    rows = {}
    for l in read(os.path.join(EQ, 'OBJETIVOS.md')).split('\n'):
        m = re.match(r'\|\s*(O\d)\s*\|(.*)\|\s*$', l)
        if m:
            rows[m.group(1)] = [c.strip() for c in m.group(2).split('|')]
    return rows

def regenerate(preamble_extra):
    fichas = []
    logs = {f: read(os.path.join(D_T, f)) for f in sorted(os.listdir(D_T))} if os.path.isdir(D_T) else {}
    lv = log_verdicts(logs)
    for f in sorted(os.listdir(D_F), key=lambda x: [int(t) if t.isdigit() else t for t in re.split(r'(\d+)', x)]):
        if not f.endswith('.md') or f.startswith('_'):
            continue
        s = read(os.path.join(D_F, f))
        fid = f[:-3]
        meta = dict(re.findall(r'^(\w+): ?(.*)$', s.split('\n---', 1)[0], re.M)) if s.startswith('---') else {}
        title = next((l[2:] for l in s.split('\n') if l.startswith('# ')), fid)
        title = re.sub(r'^' + re.escape(meta.get('id', fid)) + r'\s*', '', title)
        title = re.sub(r'\[[^\]]*\]\s*', '', title, count=1).split('→')[0].strip()
        closed, line, where = verdict_info(s, lv.get(fid))
        launched = bool(re.search(r'Estado: \*{0,2}lanzad', s))
        estado = meta.get('estado') or ('cerrada' if closed else 'lanzada' if launched else 'pendiente')
        objs = [o.strip() for o in meta.get('objetivo', '').split(',') if o.strip()]
        fichas.append(dict(id=fid, title=title, estado=estado, fecha=meta.get('fecha', ''), objs=objs,
                           ver=(f'(en [[{where}]]) ' if where else '') + summary(line) if closed else ''))
    rows = objetivos()
    for o, r in rows.items():
        mine = [x for x in fichas if o in x['objs']]
        lines = [f'---\nid: {o}\ngenerado: tools/cola_grafo.py\n---', f'# {o} · {r[0] if r else ""}', '',
                 'Temas de diseño: ' + ' · '.join(f'[[{t}]]' for t in TEMAS.get(o, [])), '',
                 f'- **Medida:** {r[1] if len(r) > 1 else ""}', f'- **Cifra hoy:** {r[2] if len(r) > 2 else ""}',
                 f'- **Meta:** {r[3] if len(r) > 3 else ""}', '', '(Fuente: `OBJETIVOS.md`; esta nota se regenera, no la edites.)',
                 '', '## Fichas', '']
        for x in reversed(mine):
            lines.append(f'- [[{x["id"]}]] · {x["estado"]} · {x["title"]}' + (f' → {x["ver"]}' if x['ver'] else ''))
        write(os.path.join(D_O, o + '.md'), '\n'.join(lines))
    vivas = [x for x in fichas if x['estado'] != 'cerrada']
    cerr = [x for x in fichas if x['estado'] == 'cerrada']
    ind = sorted(f[:-3] for f in os.listdir(D_I) if f.endswith('.md')) if os.path.isdir(D_I) else []
    tur = sorted(f[:-3] for f in os.listdir(D_T) if f.endswith('.md')) if os.path.isdir(D_T) else []
    o = ['# Cola de fichas del equipo (índice)', '',
         '<!-- Generado por tools/cola_grafo.py. No edites las tablas a mano: edita la nota de la ficha y corre el script. -->',
         '', 'La cola es un grafo de notas (David, 2026-11-20): **lee este índice y abre solo las notas que te tocan.**', '',
         '- Fichas: `docs/equipo/fichas/<id>.md` (hipótesis, brazos, estado, veredicto, seguimiento).',
         '- Objetivos: `docs/equipo/objetivos/<O>.md` (cifra, meta y sus fichas; se regeneran).',
         '- Bitácora de turnos por día: `docs/equipo/turnos/Turnos <fecha>.md` (el porqué de cada turno).',
         '- Indicaciones de David: `docs/equipo/indicaciones/` (**el orquestador las lee todas**; son cortas).',
         '- Plantilla de ficha nueva: `docs/equipo/fichas/_plantilla.md`. Después de editar notas: `python3 tools/cola_grafo.py`.',
         '', preamble_extra.strip(), '', '## Indicaciones de David', '']
    o += [f'- [[{i}]] (`docs/equipo/indicaciones/{i}.md`)' for i in ind]
    o += ['', f'## Vivas ({len(vivas)})', '', '| Ficha | Objetivo | Estado | Fecha | Qué |', '|---|---|---|---|---|']
    o += [f'| [[{x["id"]}]] | {", ".join(x["objs"])} | {x["estado"]} | {x["fecha"]} | {x["title"][:110]} |' for x in vivas]
    o += ['', f'## Cerradas recientes (20 de {len(cerr)}; todas en las notas de objetivo)', '',
          '| Ficha | Objetivo | Veredicto |', '|---|---|---|']
    o += [f'| [[{x["id"]}]] | {", ".join(x["objs"])} | {x["ver"]} |' for x in cerr[-20:]]
    o += ['', '## Objetivos', ''] + [f'- [[{k}]] · {v[0][:80] if v else ""}' for k, v in rows.items()]
    o += ['', '## Bitácora', '', ' · '.join(f'[[{t}]]' for t in tur[-7:]) + ' (las anteriores en `docs/equipo/turnos/`)',
          '', FIN, '']
    write(COLA, '\n'.join(o))
    return len(fichas), len(vivas)

NORMA = ('Norma estadística (aprobada por David el 2026-11-11): semillas pareadas entre brazos; métrica principal continua '
         'cuando exista; n ≥ 30 por brazo para valores por defecto; n = 5–8 solo para explorar.')

def main():
    s = read(COLA)
    if FIN in s:
        legacy = s.split(FIN, 1)[1]
        last = sorted(os.listdir(D_T))[-1] if os.path.isdir(D_T) and os.listdir(D_T) else 'Turnos 2026-11-13'
        start = DATE.search(last).group(1)
    else:   # first run: the whole file is legacy; its preamble becomes a note
        head, sep, legacy = s.partition('\n## ')
        legacy = sep.lstrip('\n') + legacy
        write(os.path.join(D_T, 'Turnos 2026-11-13.md'), '# Turnos 2026-11-13\n\nPreámbulo original de `COLA.md` '
              '(2026-11-11/13).\n\n' + re.sub(r'^# .*\n', '', head).strip())
        start = '2026-11-13'
    if legacy.strip():
        migrate(legacy, start)
    n, v = regenerate(NORMA)
    print(f'cola_grafo: {n} fichas, {v} vivas; índice {COLA}')

if __name__ == '__main__':
    sys.exit(main())

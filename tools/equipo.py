#!/usr/bin/env python3
"""Freno del equipo (docs/equipo/). Uso:
  python3 tools/equipo.py turno ROL   -> sale 0 y suma un turno si el rol tiene presupuesto hoy y no hay pausa; si no, 1
Lee el presupuesto de docs/equipo/OBJETIVOS.md (tabla "Presupuesto") y escribe docs/equipo/estado.json."""
import datetime, json, os, re, sys

HERE = os.path.dirname(os.path.abspath(__file__))
EQ = os.path.join(os.path.dirname(HERE), 'docs', 'equipo')


def budget():
    lim = {}
    for line in open(os.path.join(EQ, 'OBJETIVOS.md'), encoding='utf-8'):
        m = re.match(r'\|\s*(orquestador|ejecutor|analista)\s*\|\s*(\d+)\s*\|', line)
        if m:
            lim[m.group(1)] = int(m.group(2))
    return lim


def cooldown():
    """Minutes each role must wait between two turns (OBJETIVOS.md: '| rol | turnos | ... |' plus 'Espera mínima')."""
    wait = {}
    for line in open(os.path.join(EQ, 'OBJETIVOS.md'), encoding='utf-8'):
        m = re.match(r'-\s*Espera mínima\s+(orquestador|ejecutor|analista):\s*(\d+)\s*min', line)
        if m:
            wait[m.group(1)] = int(m.group(2))
    return wait


def main():
    if len(sys.argv) < 3 or sys.argv[1] != 'turno':
        print(__doc__)
        return 2
    rol = sys.argv[2]
    path = os.path.join(EQ, 'estado.json')
    st = json.load(open(path, encoding='utf-8'))
    hoy = datetime.datetime.utcnow().strftime('%Y-%m-%d')
    if st.get('dia') != hoy:
        st['dia'] = hoy
        st['turnos_hoy'] = {k: 0 for k in st.get('turnos_hoy', {})}
    if st.get('pausa') and rol != 'orquestador':
        print('equipo en pausa: solo el orquestador puede revisar si David respondió')
        return 1
    lim = budget().get(rol, 0)
    used = st.setdefault('turnos_hoy', {}).get(rol, 0)
    if used >= lim:
        print(f'sin presupuesto hoy para {rol}: {used}/{lim}')
        return 1
    # 2026-11-15 (David): spread the turns over the day instead of spending them all in one night.
    now = datetime.datetime.utcnow()
    last = st.setdefault('ultimo_por_rol', {}).get(rol)
    wait = cooldown().get(rol, 0)
    if last and wait:
        gap = (now - datetime.datetime.strptime(last, '%Y-%m-%dT%H:%M:%SZ')).total_seconds() / 60.0
        if gap < wait:
            print(f'{rol}: espera mínima {wait} min, han pasado {gap:.0f}')
            return 1
    st['ultimo_por_rol'][rol] = now.strftime('%Y-%m-%dT%H:%M:%SZ')
    st['turnos_hoy'][rol] = used + 1
    json.dump(st, open(path, 'w', encoding='utf-8'), ensure_ascii=False, indent=2)
    print(f'turno {used + 1}/{lim} de {rol}')
    return 0


if __name__ == '__main__':
    sys.exit(main())

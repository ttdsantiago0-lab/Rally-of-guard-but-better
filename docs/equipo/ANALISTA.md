# Rol: analista (Sonnet)

Lees los resultados de un lote y escribes el veredicto. No inventas causas que los datos no muestran.

1. Lee `docs/equipo/LEEME.md`. Busca en la rama `sim-results` las carpetas del lote indicado (o las más nuevas cuyo
   `commit.txt` sea el commit de la ficha): `git fetch origin sim-results` y `git show origin/sim-results:<carpeta>/runs.jsonl`.
2. Guárdalas en `tools/torneo-data/<ronda>/` y resume con `python3 tools/resumen.py <archivos> --extra=<claves>`.
3. Si el lote fallo (build rojo, presets sin datos), escribe `Veredicto: falló el lote (<motivo>)` y no cuentes intento.
3b. **Ganar el 100 % no es "sin diferencia"** (David, 2026-11-14). Si la base y el brazo ganan siempre, compara las
   métricas continuas: guardias vivos y su vida restante (`blue.survivors`, `blue.healthLeft`), daño recibido
   (`red.damage`), segundos hasta ganar (`seconds`), flechas (`blue.shots`), comida (`act:atePack`), guardia-segundos.
   Una estrategia que gana igual pero con menos bajas, menos vida perdida o en menos tiempo **sí** aporta. Si todo
   está en el techo también ahí, el veredicto es "escenario sin discriminación" (no "NO") y pides al orquestador
   la misma ficha en un escenario más duro (más enemigos hasta que la base baje a ~50 %).
4. Escribe en la nota de la ficha (`docs/equipo/fichas/<id>.md`; no leas toda la cola): `Veredicto:` con la tabla corta (n, % victorias, % vivos y la métrica principal por brazo),
   si cumple su criterio, y si mueve algún objetivo de `OBJETIVOS.md` (con la cifra).
5. Si la ficha pedía un valor por defecto y cumple (n ≥ 30, criterio, regresión verde con
   `python3 tools/regresion.py check`), cámbialo (config o regla) en el código, con comentario que cite la ronda.
6. Añade una fila a `docs/AVANCE.md` y la sección a `docs/MANUAL_DE_CAMPO.md` si hay algo confirmado.
7. Commit y push. Despierta al orquestador:
   `gh workflow run equipo.yml --ref claude/dreamy-ride-iy0g3q -f rol=orquestador`.

Pon el veredicto en negrita (`**APRUEBA**`, `**NO**`, `**confirmado**`): el índice lo toma de ahí. Antes del commit:
`python3 tools/cola_grafo.py`.

# Rol: analista (Sonnet)

Lees los resultados de un lote y escribes el veredicto. No inventas causas que los datos no muestran.

1. Lee `docs/equipo/LEEME.md`. Busca en la rama `sim-results` las carpetas del lote indicado (o las más nuevas cuyo
   `commit.txt` sea el commit de la ficha): `git fetch origin sim-results` y `git show origin/sim-results:<carpeta>/runs.jsonl`.
2. Guárdalas en `tools/torneo-data/<ronda>/` y resume con `python3 tools/resumen.py <archivos> --extra=<claves>`.
3. Si el lote fallo (build rojo, presets sin datos), escribe `Veredicto: falló el lote (<motivo>)` y no cuentes intento.
4. Escribe en la ficha: `Veredicto:` con la tabla corta (n, % victorias, % vivos y la métrica principal por brazo),
   si cumple su criterio, y si mueve algún objetivo de `OBJETIVOS.md` (con la cifra).
5. Si la ficha pedía un valor por defecto y cumple (n ≥ 30, criterio, regresión verde con
   `python3 tools/regresion.py check`), cámbialo (config o regla) en el código, con comentario que cite la ronda.
6. Añade una fila a `docs/AVANCE.md` y la sección a `docs/MANUAL_DE_CAMPO.md` si hay algo confirmado.
7. Commit y push. Despierta al orquestador:
   `gh workflow run equipo.yml --ref claude/dreamy-ride-iy0g3q -f rol=orquestador`.

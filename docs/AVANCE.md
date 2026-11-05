# Avance medido

Una fila por cambio, con su número antes y después y dónde está la prueba. Todo es simulación (8 guardias de rango 1,
mobs reales, campo plano, medianoche fija) salvo que diga otra cosa. "Confirmado" = se repitió con otras semillas.
El detalle de cada fila está en `docs/MANUAL_DE_CAMPO.md`; los datos en `tools/torneo-data/`.

## Resumen

| Fecha | Cambio | Antes → después | Estado |
|---|---|---|---|
| 2026-11-04 | **Volea corregida** (arco bajo con el blanco a la vista; anticipación con el tiempo de vuelo real) | acierto 8–22 % → 82–94 % | medido (ronda 3) |
| 2026-11-04 | **Kits reales en la simulación** (Guard Villagers pisaba el equipo en el primer tick) | — (la ronda 1 entera era una mezcla al azar; se rehízo) | corregido; cada corrida registra el kit |
| 2026-11-04 | **Tiro a quemarropa** por defecto en el juego | zombis, % victorias / % supervivientes: arqueros 83/62 → 100/100, línea 67/45 → 100/98, caballería 73/68 → 100/99 | confirmado |
| 2026-11-04 | **Creepers**: tiradores y escudos los atacan | supervivientes: arqueros 39 → 63 %, línea 35 → 64 %, escudos 40 → 74 %, caballería 26 → 48 % | confirmado |
| 2026-11-04 | **Comida** (pan ×16; en la mano Guard Villagers se aleja a comer) | zombis, % victorias: espadas 26 → 92–95 %, caballería 63 → 100 %; esqueletos, línea 42 → 100 % | confirmado (ronda 4) |
| 2026-11-05 | **Escudo arriba bajo fuego** (tirador que lo ve a ≤ 24 bloques) | escudos contra esqueletos, supervivientes 10–14 % → 65–73 %; línea 36–44 % → 60–64 % | confirmado |
| 2026-11-05 | **Tirar desde donde están** contra tiradores | esqueletos, % victorias / % supervivientes: arqueros 63–77/52–57 → 100/98, línea 33/21–28 → 100/99–100, caballería 47–53/32–37 → 83–90/73–74 | confirmado |
| 2026-11-05 | **Oleadas** (10 olas de 12 zombis, o 10 olas mezcladas de 10: zombi → esqueleto → creeper; pausa de 20 s) | olas superadas de 10, antes → ahora: **zombis**: arqueros 2,0–2,6 → **10** (todas las corridas, 8 de 8 en pie), línea sin comida 5,4 → **10**; **mezcladas**: línea con comida 6,3 → **9,9** (9 de 10 corridas las pasan todas), línea sin comida 3,9 → 7,8, arqueros 1,5–2,1 → 5,2–5,5; escudos casi igual (5,5–8,1 → 6,1–8,6) | medido (n ≈ 10 por fila) |

## Lo que sigue
1. Arqueros contra los últimos esqueletos de una ola: se quedan quietos y los esqueletos lejos, nadie hace daño (13 de 28
   corridas de arqueros en olas mezcladas terminaron así). Corregir "tirar desde donde están": solo con el tirador a tiro.
2. Medir el **techo nuevo**: con cuántos mobs a la vez empata ahora cada composición (antes: 13–30 zombis).
3. Rotar a comer de verdad (alguien cubre al que se va a comer y otro tapa su hueco).

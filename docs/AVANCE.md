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
| 2026-11-05 | **Tirar desde donde están** contra tiradores | (en el torneo: arqueros y línea contra esqueletos ≈ 52 % → 97–100 %) | en el juego; verificación en curso |
| 2026-11-05 | **Oleadas** en la simulación (hasta 10 olas, pausa de 20 s para comer) | primera medida: cuántas olas aguanta cada composición, con y sin comida, ahora y antes | en curso |

## Lo que sigue
1. Verificar en la simulación "tirar desde donde están" (arqueros contra esqueletos con los valores del juego).
2. Oleadas: cuántas olas aguantan línea, escudos y arqueros; cuánto pesa la comida.
3. Medir el **techo nuevo**: con cuántos mobs a la vez empata ahora cada composición (antes: 13–30 zombis).
4. Rotar a comer de verdad (alguien cubre al que se va a comer y otro tapa su hueco).

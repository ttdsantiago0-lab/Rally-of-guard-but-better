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
| 2026-11-05 | **Techo nuevo** (sin comida; mobs a la vez con los que 8 guardias empatan) | zombis: arqueros 13 → **40**, línea 21 → **38**, caballería 12 → 19, escudos 30 → 32, espadas 24 → 24; esqueletos: arqueros 46 → **68**, línea 34 → **más de 80** (gana el 92 % con 80) | medido (ronda 7, n = 6 por punto) |
| 2026-11-05 | **Apartarse del creeper que se hincha** | supervivientes contra creepers: arqueros 70 → 96 %, línea 73 → 97 %, escudos 76 → 98 %, espadas 42 → 58 %; olas mezcladas con comida: escudos 7,4 → 9,3 olas | medido (ronda 8, n = 30) |
| 2026-11-05 | **Arqueros atascados** frente a los últimos esqueletos de una ola: tiran quietos solo con el tirador a su alcance (40 con arco) y a la vista | atascos en olas de arqueros 13/28 → 1/22; contra esqueletos siguen en 100 % (97–100 % supervivientes) | confirmado |
| 2026-11-05 | **Oleadas mezcladas con todo lo anterior** (comida en la mochila) | olas superadas de 10: escudos 8,1 → **10** (10/10 corridas), línea 6,3 → 9,6, arqueros 2,1 → 9,0; sin comida: arqueros 1,5 → 9,4, línea 3,9 → 8,9, escudos 5,5 → 7,6 | medido (n ≈ 10) |
| 2026-11-05 | **Punto de quiebre** (línea de 8 con comida y todo el manual) | olas superadas de 10: mezcladas de 16 por ola 8,5 (6/11 todas), de 22 por ola 5,3; zombis 24 por ola 5,7, 32 por ola 1,1. En olas grandes de zombis los mata el cuerpo a cuerpo (quedan rodeados) | medido (ronda 10, n ≈ 10) |
| 2026-11-05 | **Moral** encendida frente a apagada (olas mezcladas con comida) | arqueros: 9,6 olas y 5,5 en pie con moral, 10 olas y 7,3 en pie sin moral (más muertes por explosión con moral: 2,3 frente a 0,7); línea igual | medido (n = 10); la moral cuesta un poco en peleas que se ganan |
| 2026-11-05 | **Caballería**: plantar al jinete arquero a su alcance frente a tiradores y galopar lejos del creeper | contra esqueletos las victorias cayeron de 77–87 % a 9–11 %; contra creepers, misma supervivencia y menos victorias (36–45 % frente a 54–71 %) (n = 30, dos lotes) | **revertido** (lo de a pie sigue); en olas la caballería se atasca: jinetes sin línea de tiro y huida a galope con la moral rota |
| 2026-11-05 | **Comer en la calma** (sin blanco ni hostiles a 12 bloques, comer de la mochila hasta el 90 %) | escudos comen 21 veces en las pausas (antes 0) y recuperan +11 de vida por pausa (antes +8); en olas de 10 no cambia el resultado (todo pasa ya casi las 10) | puesto; falta medirlo en olas más duras |

## Lo que sigue
1. Olas más duras (24+ zombis por ola): qué ayuda cuando el grupo queda rodeado (corro de escudos, retroceder disparando).
2. Caballería: por qué los jinetes arqueros pierden la línea de tiro.
3. Veteranía: rango 0 / 1 / 3 / 5 en olas (en curso).
4. Rotar a comer de verdad (alguien cubre al que se va a comer y otro tapa su hueco).

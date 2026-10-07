# Rally of the Guard — Simulación v2 (E2A, plataforma de aprendizaje)

**Comando para David** (admin, en un **mundo plano** de pruebas, sin atenderlo):

```
/rally sim batch quick                      ≈ 15 min (10 escenarios, 1 semilla)
/rally sim batch standard                   ≈ 60 min (24 escenarios, 2 semillas)
/rally sim batch long --tickrate sprint     ≈ 2 h a velocidad normal; con sprint, menos
/rally sim batch all --seeds 3 --minutes 2
/rally sim stop                             detiene el lote y escribe el resumen
/rally sim A | B | C | D | ALL [semilla] [repeticiones]   (alias de E1; también cualquier id)
```

Opciones: `--minutes N` (tope por corrida), `--seeds N` (repeticiones con semillas 1..N),
`--tickrate sprint|normal` (sprint usa el `/tick sprint` de vanilla; con sprint se añade una
**corrida de control** a velocidad normal con la misma semilla y se compara).

## Arena
- Se busca un sitio a `simulation.arenaDistance` (128) bloques del jugador, en 8 direcciones
  (y 1,5× más lejos). Se descarta si algún chunk tiene entidades de bloque (cofres, hornos…),
  si la superficie tiene agua/lava, más de 10 bloques de desnivel, o tablones, lana, escaleras,
  losas, puertas, camas, vallas o muros. **Sin sitio libre, aborta con mensaje y no toca nada.**
- Suelo plano y sólido de `simulation.floorBlock` (piedra) de `simulation.arenaSize` (96) de
  lado, aire encima y muro perimetral de 4. Se construye unas filas por tick. Los chunks quedan
  forzados mientras corre el lote (el combate avanza aunque el jugador esté lejos).
- Terreno por escenario: `flat`, `hill` (montículo +4 bajo el azul), `cover` (pilares de 2),
  `wall` (muro con hueco de 4 = estrechamiento), `forest` (troncos con hojas). Se retira al
  terminar cada corrida.

## Escenarios (datos, no código)
`config/rallyguard-sim-scenarios.json` (se crea desde el jar la primera vez; edítalo ahí).
Cada escenario: `id`, `terrain`, `distance` (20/40/80), `position` (`front`, `flank`,
`height`), `minutes` (opcional), `tags` (`mirror` = control, debe dar ≈50 %), `expect`
(`blueWinMin`, `blueWinMax`) y dos bandos `blue`/`red`:
`{kind: guard|zombie|husk|skeleton|pillager|vindicator|iron_golem|ravager, count, role:
melee|archer|gunner|mixed, rank 0-5, leader, formation: LINE|COLUMN|V|CIRCLE, mounted,
fireMode: NORMAL|STILL|ADVANCING|RETREATING|VOLLEY}`. Lo que el rojo no diga se copia del azul.
`checks` define expectativas entre escenarios (p. ej. `D > B` en `blueSurvivors`).
**Añadir uno:** copia un objeto en `scenarios`, cambia `id` y campos, y nómbralo en un preset.

- Los dos bandos reciben **el mismo equipo** (arma del rol, casco y peto de hierro, 2×64 de
  munición en la mochila) y **la misma vida** (20; los jefes golem/ravager conservan la suya).
  Azul = comandante de simulación; rojo = otro comandante de simulación, o mobs.
- Aparecen alternando azul/rojo (ninguno gana el primer turno), se calculan subunidades y
  capitanes (Bloque 1), 3 s para asentarse y ambos atacan. Un conductor ligero solo da un
  enemigo a quien no tiene ninguno vivo; nunca anula una retirada ni a un tirador sin munición.
- Fin: cae un bando, tope de tiempo, o **atasco** (sin daño `simulation.stuckSeconds`): se
  reintenta una vez con otra semilla. Todo lo creado se elimina; la XP de veteranía no cuenta.
- `gunner` necesita `simulation.gunItem` (id de un arma de Scorched Guns); vacío = ballesta.
- Tope de rendimiento: `simulation.maxUnitsPerSide` (100); el resumen avisa si recorta.

## Métricas por corrida (`runs.jsonl`)
Ganador (azul/rojo/empate), tope y atasco; por bando: inicio, supervivientes, vida restante,
daño hecho, disparos/impactos de Rally, munición, cambios de arma, órdenes, retiradas,
ahogados, muertes de líder (capitanes calculados); cohesión azul (distancia media a su
centro), distancia media al enemigo, usos de cada peldaño de la escalera de decisión
(`ladder`, índice = `DecisionLadder.Rung`), coste medio y pico de Rally (ms/tick, `Perf`).

## Salida
`rally-sim/<fecha>-<preset>/`: `runs.jsonl` (se escribe al acabar cada corrida),
`summary.json`, y `summary.md` (≤150 líneas, para copiar al repo): tabla por escenario con
media ± error estándar y tasa de victoria, comparación espejo, control de velocidad, y
**Hallazgos** automáticos: expectativas incumplidas (`expect`, `checks`), espejos fuera de
≈50 %, corridas atascadas o al tope, ahogados, resoluciones < 3 s, picos de Rally > 20 ms.

## Resultados E1 (línea base, para comparar)
A sobrev. 18,3 · B 18,0 · C 19,3 · D 20,7 (3 rep., semilla 1); distancia ≈13 en las cuatro.

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

## Simulación v3 (E3A A9)

- **Terrenos** (`"terrain"`): `plain`/`flat`, `hill`, `mountain` (monte de 5 en medio), `river` (río de 5 de ancho y
  1 de hondo: nadie se ahoga, `drowned` debe quedar en 0), `lava` (canal con borde y puente de 5), `cliff` (rojo en
  meseta de 5 con rampa). Preset `terrain`. Estructuras (2026-11-08, ronda 29): `village` (dos filas de casas de
  piedra con puerta de roble; los rojos empiezan dentro, en la calle o en los tejados), `corridors` (bloque techado de
  lado a lado con pasillos de 3), `fort` (patio amurallado de 4 de alto con una puerta de 3), `hall` (salón con techo
  a 5 y pilares).
- **Fuerzas** (etiqueta `force`): cada bando es una escuadra con unidades de `unit:N` bajo una fuerza real;
  `doctrine:X` / `rdoctrine:X`, `op:TIPO` / `rop:TIPO` (HOLD_LINE, FALLBACK_FIGHTING, ADVANCE_BY_TURNS, FLANK,
  ORDERLY_RETREAT). Presets `force`, `scale` (`scale-50/100/200/400`, total de guardias) y `v3`.
- **Laboratorio** (etiqueta `lab`, preset `lab`): la mitad de las corridas prueba una acción fuera de lo establecido
  (`flanco-corto`, `retirada-fingida`, `foco-mas-debil`, `dispersarse`; probabilidad `simulation.labChance`). Si supera a
  su línea base por más de 10 puntos y del ruido en 3+ corridas, `summary.md` la marca "candidata a táctica". Nunca se adopta sola.
- `summary.md` compara con el lote anterior (la carpeta más reciente de `rally-sim/`) y resume en "Qué aprendimos".

## Resultados E1 (línea base, para comparar)
A sobrev. 18,3 · B 18,0 · C 19,3 · D 20,7 (3 rep., semilla 1); distancia ≈13 en las cuatro.

## Correr en un PC propio (2026-11-06)

GitHub cobra los minutos de sus máquinas en repositorios privados (plan gratuito: 12 USD de uso incluido al mes,
unos 2000 minutos de Linux; después 0,006 USD/min). Un PC propio con el *runner* de GitHub no gasta esos minutos.
El flujo es el mismo (botón "Run workflow", resultados en la rama `sim-results`); solo cambia dónde corre.

1. En el PC: Ubuntu 24.04 (o Windows con WSL2 y Ubuntu dentro). `sudo apt install -y git curl python3 unzip`.
   Java no hace falta instalarlo: el flujo lo descarga.
2. En GitHub: repositorio → Settings → Actions → Runners → **New self-hosted runner** → Linux x64. Copiar y pegar en
   el PC los comandos que muestra (descarga y `./config.sh --url … --token …`; Enter a todo).
3. Dejarlo como servicio, para que arranque solo con el PC: `sudo ./svc.sh install && sudo ./svc.sh start`.
4. En GitHub: Settings → Secrets and variables → Actions → **Variables** → New repository variable:
   `RALLY_RUNNER` = `self-hosted`. Desde ese momento compilación y simulaciones van al PC. Borrar la variable =
   volver a las máquinas de GitHub.
5. El PC tiene que estar encendido y sin suspenderse mientras corre algo. Un servidor de simulación usa unos 2 GB
   de RAM: con 8 GB, un runner (dos como máximo). Con un solo runner, los presets de un lote van uno tras otro.

Solo para repositorios privados: el runner ejecuta lo que haya en el repositorio.

En el espacio de trabajo de Claude también corren (sin GitHub): `./gradlew runServer -PsimAutorun=<preset>
-PsimSpeed=sprint -PsimSeed=1 -Prally_mods_dir=`. Desde el 2026-11-08 toda corrida sin pantalla (CI o autorun local) carga
los escenarios del jar recién compilado y sobrescribe la copia de `config/`: ya no hay escenarios viejos por accidente.
Cada lote guarda en `commit.txt` el commit exacto con que se midió.

## Qué mide cada tramo de la campaña (2026-11-07)
Las puertas de cada etapa (qué tiene que quedar probado, con qué umbral) están en `docs/PLAN_MAESTRO.md` §4, y las
alarmas que revisa cada `summary.md` en §5. Además de ganar: sobrevivir, calidad táctica (atascos, órdenes perdidas,
integridad de formación), coste por tick y, fuera de la simulación, si se ve bien y es divertido (pruebas de David).
Con dos portátiles: un runner por portátil (los dos con la etiqueta `self-hosted`); el `max-parallel` del flujo reparte
los presets de un lote entre los dos.

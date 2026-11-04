# PROMPT_E3COORD para Claude Code — Coordinación y contingencias (2026-11-03)

**Orden (decidido por David, 2026-11-03):** E3B (hecho) → **E3-COORD (este)** → E3C logística (`PROMPT_E3C.md`, en espera) → E3D → E4.
**Propósito que manda:** `docs/PROPOSITO_BATTLE_PLANNER.md`. Si algo de aquí lo contradice, gana ese documento.
**Meta:** guardias preparados contra la amenaza **prevista** ganan; contra una **no prevista** rinden como la doctrina normal;
contingencias contradictorias no rompen nada. Sin comportamientos nuevos: solo coordinar los que ya existen.

**Reglas de siempre:** las del Bloque 0 de `PROMPT_E3C.md` (lista de compilación a–i, lang en 4 idiomas, sin inventar ids de
otros mods, `PORTING_PLAN.md` ≤ 40 líneas por bloque, un commit por bloque, sin preguntas a David, informe final ≤ 40 líneas con
**qué NO quedó** y **qué probar**). Todo con interruptor en `RallyConfig` (`coord.*`); apagado = E3B exacto.
**Compilación:** existe `.github/workflows/build.yml`: compila en cada push y publica el jar como artefacto `rallyguard-jar`.
Un bloque solo cuenta como "compila" si **esa corrida de Actions está en verde** (nunca por suposición). **Push solo cuando
David lo autorice** (a 2026-11-03 la rama local va 67+ commits por delante; David dijo "todavía no").

---

## 0. Diagnóstico de partida (lote `20261011-015530-v4`, 258 corridas; hecho por Cowork 2026-11-03)
Por qué hoy "con plan" rinde igual o peor que "sin plan". **No es un solo fallo: las pruebas no miden lo que creemos.**

| # | Hallazgo | Evidencia |
|---|---|---|
| D1 | **n=3 y resultado bimodal decidido por la moral.** Azul pierde casi exactamente cuando su moral se rompe (primer ROTO a 12–35 s, 5–11 rotos). El efecto del plan queda enterrado. | Las 4 parejas perdedoras: todas las derrotas tienen `morale_ROTO ≥ 3`; las victorias, 0–1. |
| D2 | **`scenario-defensa` compara "mantener posición" contra "pelea libre" frente a zombis cuerpo a cuerpo**, no "contingencia acertada" contra "sin plan". El plan es `plan:hold` desde t=0. | Con plan: `ORDER_OVERRIDE` 43, `FORMATION_HOLD` 41, `RELAXED` 54; sin plan: `ENGAGE` domina. 33 % vs 100 %. Viola el criterio 1 (activación condicional). |
| D3 | **La emboscada nunca se dispara.** `planner2-ambush` es en realidad "una unidad de brazos cruzados". | `ambushSprung` = 0 **y** `ambushDiscovered` = 0 en las 6 corridas. Sospecha: `Plans.java:403` salta el vínculo en silencio si `missing(link,to)` (con 12 guardias "mixed" en 6 unidades hay 2 por unidad: puede faltar `MELEE_LINE`/`MOUNTED_CHARGE`) o si `busy`. Solo se avisa en la interfaz, nunca en la simulación. |
| D4 | **DELAY se juzga mal.** Su función es ganar tiempo, no ganar. | Con DELAY la batalla dura 57–76 s; sin él, 34 s (una tablas a 120 s). `delaySteps` 16–18: **sí funciona** por su propia métrica. |
| D5 | **El control de FILL no es control.** `planner2-fill-base` (`plan:hold`) también rellena huecos. | `fills` 6/6/9 en el control. Con el vínculo: más rellenos (4/8/10), más líderes muertos, primer ROTO a 12–20 s: mover unidades a puestos lejanos **en pleno contacto** las expone. |
| D6 | **La comprobación global "vida/daño/velocidad iguales" es un artefacto.** | Pasa en **121/121** victorias rojas y falla en **75/75** victorias azules: sin supervivientes el bucle está vacío y da "igual"; con supervivientes compara `getAttributeValue`, que incluye modificadores de **equipo** (cambio de arma, escudo) y efectos. No prueba nada sobre Rally. |

### 0b. Lo que mostraron las simulaciones en la nube (GitHub Actions, 2026-11-04) — cambia el diagnóstico
| # | Hallazgo | Evidencia (lotes `coord`, 91 corridas, sprint) |
|---|---|---|
| D7 | **Tercer bando oculto.** Los guardias que matan los zombis se convierten en **aldeanos zombi**; la limpieza entre corridas no los retiraba y peleaban en las corridas siguientes contra azul. **Esto contaminó también los lotes del juego (v4).** | Antes: 84–100 % de las bajas azules por un mob de ningún bando, azul ganaba 0–30 %. Tras limpiar el campo: 69 `zombie_villager` retirados, azul gana 100 % en 7 de 9 escenarios. |
| D8 | **La prueba de precedencia estaba mal, el mod bien.** El "¡ataquen!" inicial del simulador iba por el canal del jugador y dejaba a todos 60 s "bajo orden del jugador". | Corregida la prueba: precedencia correcta 100 % (10/10). |
| D9 | **Los escenarios son demasiado fáciles para medir un plan (techo).** Con el campo limpio, con y sin plan ganan 100 %; FILL ni siquiera llega a matar a la unidad (la pelea acaba a los 15 s, la baja programada era a los 15 s). | `planner2-fill` 18/18 vivos; `scenario-defensa` 12/12. |
| D10 | DELAY sí cede terreno y gana tiempo (51 s vs 20 s) al precio de bajas: medirlo por tiempo ganado (M4). Dentro de la misma pelea, guardias convertidos (aldeanos zombi) causaron 20 bajas azules. | `planner2-delay` 50 % vs base 100 %. |
**Decisión de David (2026-11-04):** el guardia que muere por un zombi **se queda como zombi y sigue peleando** (mecánica del juego, no se toca). En la simulación ese mob se une al bando rojo (`convertedToRed`).
**Calibración (lote `calib`, n=5):** el control pasa de ganar a perder entre ~1,3 y ~1,6 zombis por guardia. Fijados: fill 22, ambush 15, delay 18, defensa 17 rojos.
**Consecuencia:** las conclusiones previas ("el plan rinde peor que no tenerlo") **no son válidas**. Siguiente paso: **calibrar** cada pareja para que "sin plan" gane ≈ 50 % (más enemigos o enemigos con arco), y solo entonces medir si el plan suma.

Arreglado ya por Cowork (sin compilar, una línea): NPE en `SimRunner.endRun` — `precedenceTick()` cerraba la corrida y
`fightTick()` la volvía a cerrar con `job == null`. Guardas en `endRun` y tras `precedenceTick()`.

---

## Bloque M · Medir bien (primero; sin esto nada de lo demás se puede juzgar) ≤ 30 %
- **M1 · Atributos (D6):** comparar solo modificadores cuyo id sea del espacio `rallyguard` (más el valor base); registrar
  **qué** atributo cambió (`attrChanged:<attr>`); sin supervivientes azules el resultado es "n/a", no "igual".
- **M2 · Vínculos que no se ejecutan (D3):** contador `linkSkipped:<TIPO>:<motivo>` (`missing_cap`, `busy`, `no_report`,
  `no_enemy`) y fase alcanzada por vínculo. `summary.md` lista todo vínculo que **nunca** pasó de su fase 0, como hallazgo.
  Arregla la causa real de la emboscada que encuentres (si es la composición del escenario, corrige el escenario, no el código).
- **M3 · Controles honestos (D5):** `*-base` apaga `FILL_POLICY` cuando prueba FILL; cada pareja difiere en **una sola** cosa.
- **M4 · Métrica por propósito (D4):** DELAY → segundos ganados y bajas propias; FILL → tiempo con el puesto cubierto;
  AMBUSH → daño en los primeros 10 s del golpe. La victoria se sigue mostrando, pero no es el criterio de esos vínculos.
- **M5 · Repeticiones (D1):** preset nuevo `coord` con **n ≥ 10** por escenario; tablas con media ± error e indicación
  "diferencia significativa / ruido" (la comparación de lotes ya existente). Además: tiempo hasta primer ROTO.
- **M6 · Métricas de coordinación** de `PROPOSITO_BATTLE_PLANNER.md` §6: tiempo de respuesta (≥ 80 % de los asignados ya
  cambió), cobertura de roles, concentración de fuego sobre el objetivo prioritario, primer golpe correcto, bajas evitables,
  ping-pong de órdenes.

## Bloque C · Contingencias ≤ 45 %
- **C1 · `ThreatClass`** (enum, configurable por JSON de firmas, sin ids de otros mods en código): `GROUND_MELEE_SWARM`,
  `RANGED_LINE`, `FLYING`, `FLYING_CASTER`, `MOUNTED_AIR`, `MOUNTED_GROUND`, `BOSS`, `SIEGE`, `PLAYER_LED`. La clasificación la
  hace **el capitán con lo que su unidad ha visto** (`SituationReport` + `ThreatMemory`, composición enemiga ya prevista en
  VISION §3.6), a 1 Hz, con histéresis. Nadie sabe lo que no ha visto.
- **C2 · `Contingency`** dentro del plan (JSON en `PlanRegistry`, persiste): `trigger` (ThreatClass + condiciones: distancia,
  número, moral propia), `response` (reasignar roles de Fuerza, formación, `TARGET_PRIORITY`, **quién ataca primero**,
  postura por unidad, instrucción al capitán), `priority`, `exit` (sin amenaza N s, o condición). Alta/baja/lista por comando
  `/rally plan contingency add|remove|list` (sin interfaz nueva todavía). Límite duro configurable por plan (8).
- **C3 · Activación condicional (criterio 1):** hasta que el disparador se cumple, el plan **no cambia nada** respecto a la
  doctrina base. Al salir, restaura la doctrina sin residuos (criterio 6), reutilizando la restauración de operaciones (R4).
- **C4 · Jerarquía (criterio 2):** las contingencias entran en `ORDENES_JERARQUIA.md` entre "orden explícita del jugador" y
  "directiva automática". Choques: gana la de mayor prioridad; empate → la más antigua; siempre con `DecisionReason`
  `CONTINGENCY_ACTIVE / CONTINGENCY_OVERRULED / CONTINGENCY_EXIT` y línea en `/rally debug why`. Duración mínima 5 s.
- **C5 · Primer golpe del arma más poderosa:** el capitán designa al guardia cuyo **ítem** en mano tiene mayor daño (dato del
  ítem, no estadística del guardia); los demás mantienen su puesto/fuego hasta que el designado enganche o pasen N s. Usa
  `FOCUS_FIRE` / `TARGET_PRIORITY` existentes.

## Bloque A · Aceptación (el ejemplo canónico de David) ≤ 25 %
- **A1 · Mago volador:** amenaza `FLYING_CASTER` en sim con mobs vanilla que vuelan y disparan (`blaze`; `phantom` como volador
  de mele; `ghast` si cabe en la arena). Dragón de Ice and Fire **solo** si su id existe en el registro en tiempo de ejecución
  (si no, se omite y se dice). Contingencia: pantalla protege a la artillería, la artillería concentra en el volador, cuando
  baje a mele golpea primero el arma más fuerte.
  Cuatro versiones, n ≥ 10: **acertado**, **sin plan**, **plan que prevé otra cosa** (p. ej. `SIEGE`), y **control sin la
  amenaza** (mismo plan, zombis). Se exige: acertado > sin plan ≈ prevé otra cosa; control ≈ sin plan (± ruido).
- **A2 · Contradicción:** 3 contingencias que se contradicen sobre el mismo disparador → termina, 0 excepciones, 0 bucles,
  `perfMsPerTick` sin subir > 10 %, y el informe muestra quién ganó y por qué.
- **A3 · Rehacer** `scenario-defensa` y `planner2-ambush` como contingencias de verdad (disparador explícito) con sus
  controles; volver a medir `planner2-*` con las métricas de M4.

**Fuera de alcance:** interfaz de playbook, Ledger por legión (§5 del propósito; se deciden con las cifras de A1), E3C.
**Qué probar (para el informe):** `/rally sim batch coord` en mundo plano y pegar `summary.md`; luego una batalla real
contra blazes con y sin la contingencia.

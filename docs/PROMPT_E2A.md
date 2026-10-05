# PROMPT_E2A para Claude Code

Presupuesto ajustado. Reglas de este prompt:
1. Lee `docs/PLAN_ENTREGAS.md`, `docs/DECISIONES_TACTICAS.md`, `CLAUDE.md` y solo los archivos que vayas a modificar. No re-explores el repo.
2. Sin investigación web: lo investigado está en `docs/BACKLOG.md`, `docs/ARQUITECTURA.md`, `docs/VETERANIA.md`. Si falta un dato, anótalo como pregunta; no lo busques.
3. Escribe cada archivo completo en una sola pasada; no lo reescribas más de dos veces.
4. No compiles intentos parciales: un solo parse + chequeo de tipos al final.
5. Haz los commits en los puntos de control que indique el prompt (si no indica ninguno, uno al terminar). Actualiza la lista de “APIs usadas por primera vez” en `PORTING_PLAN.md`, para que un fallo de build se arregle barato.
6. Informe final de máximo 25 líneas: qué quedó, qué NO quedó, qué debe probar David.
7. **No leas `PORTING_PLAN.md` entero** (más de 2200 líneas): lee solo su última sección (`tail -n 120`) y usa `grep` para lo demás. En `PORTING_PLAN.md` añade como máximo ~40 líneas por fase; en `CLAUDE.md` actualiza solo la sección “Current status”. No crees documentos nuevos que no se pidan.
8. No pidas confirmaciones ni plantees preguntas a David durante el trabajo: decide con el plan, anótalo en el informe y sigue.
9. Si algo de lo pedido no cabe, **recorta siguiendo la línea de corte de `docs/PLAN_ENTREGAS.md` §2** y dilo en el informe. No dejes nada a medias sin avisar.

---

**Contexto.** Eres Claude Code en el repo de Rally of the Guard (NeoForge 1.21.1). Lee `docs/DECISIONES_TACTICAS.md` completo (es la especificación) y `docs/VETERANIA.md` §0–4. E1 ya está compilado y probado por David. Tras este prompt David dará feedback; no te detengas a pedir confirmaciones y haz **un único commit al terminar** (si el cupo se agota, haz commit de lo que esté coherente y dilo).

**E2-A · Capacidad de mando y capitán calculado.**
- Cada guardia tiene `commandCapacity` derivada de su veteranía (tabla en el playbook; ilustrativa y configurable).
- **Capitán = el guardia con mayor capacidad dentro de una unidad/escuadra; se calcula, no se crea.** Si no hay líder veterano, la escuadra se comporta como hoy.
- Jerarquía: el líder decide → emite una **directiva** de escuadra (push, una vez) → cada soldado la resuelve con información local. **Nunca** una decisión por guardia por tick.
- Sucesión: si cae el líder, manda el siguiente; máximo 3 relevos (4 en ejércitos grandes); después la escuadra se desarticula (moral, huidas).


**E2-B · Veteranía = calidad de decisión.** Rango 0–5 (nombres: Recluta, Entrenado, Veterano, Élite, Maestro; solo ilustrativos, configurables). XP **solo en combate real** (daño causado, pelea de más de N s, variedad de enemigos, rendimientos decrecientes por tipo repetido). Cada rango desbloquea comportamiento de la escalera de decisión del playbook, **no daño bruto**. Un rasgo por rango según genética (MCA si está; si no, afinidad al azar) e historia. Maestro **no** es inmortal. Los tótems no se usan al instante (retraso; el rasgo Tanque lo reduce); revivir varias veces seguidas puede volverlo kamikaze. Contadores por UUID, guardados como el roster.


**E2-C · Valoración de terreno (conceptos simples).** Implementa `TerrainSense` exactamente como lo define el playbook §3: puntúa **como máximo 8 posiciones candidatas** alrededor del líder, **una vez por segundo por escuadra**, con muestreo barato (≤12 lecturas de bloque por candidata; sin raycasts masivos), caché de 2–4 s. Salida: una etiqueta por candidata (`COVER_GOOD/BAD`, `HEIGHT_GOOD/BAD`, `DEFENSIBLE/OPEN`, más banderas `NEAR_WATER`, `IN_FOREST`, `CHOKEPOINT`). Solo líderes de rango ≥ Veterano la usan; el resto sigue como hoy.


**E2-D · Escalera de decisión** del playbook §2 (objetivo → formación → cobertura → rango óptimo → amenaza prioritaria → avanzar/disparar/cubrirse/retirarse). Una función pura `DecisionLadder.decide(situation, rank)` fácil de probar, llamada desde el bucle ya repartido en 20 ticks. Reutiliza `Engagement`, `RetreatPlanner`, `ScorchedGunsCompat`.


**E2-F · Simulación.** Conecta `/rally debug rank` al sistema real (los guardias de prueba de B y D usan el rango). **No puedes ejecutar el juego**: deja `docs/SIMULACIONES.md` con la tabla antes/después vacía y el comando exacto (`/rally sim ALL`) para que David la rellene; el orden esperado es D > B > A en supervivientes y C ≥ A en distancia media y bajas.


**ARREGLOS DE E1 (David los pega aquí):**
_(vacío si no hay)_

**Entrega:** un commit, `PORTING_PLAN.md` y `CLAUDE.md` actualizados, informe ≤25 líneas con qué NO quedó.

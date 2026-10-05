# PROMPT_E2B para Claude Code

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

**Antes de empezar:** el prompt E2a ya se ejecutó (líderes, veteranía, terreno, escalera); apóyate en ello.

**E2-0 · Inventario (corrección de David, 2026-10-05; hazlo primero, es pequeño).** Los **6 espacios extra** actuales (además de manos y armadura) se quedan, con una regla nueva: **como máximo 1 maleta** (ítem que exponga `Capabilities.ItemHandler.ITEM` con más de 6 espacios, p. ej. Sophisticated Backpacks) y **5 espacios libres** para objetos (armas, flechas, munición, comida, tótems). La maleta guarda muchísimos ítems. El guardia **gestiona su inventario solo**: coloca lo que recoge donde corresponde (munición/comida/tótems a la maleta si hay hueco; armas útiles a los 5 libres), descarta lo inútil dejándolo en el cuartel, y busca primero en los 5 espacios y luego en la maleta. El menú ▤ muestra la maleta como espacio especial y rechaza una segunda. Migración: si una mochila guardada tiene 2+ maletas, deja una y mueve el resto a los espacios libres o al suelo del cuartel. Sin dependencia dura de Sophisticated Backpacks.


**E2-E · Entrenamiento y saqueo.** Muñeco de práctica junto al cuartel (bloque o entidad simple): descubre afinidades y lleva a rango 1, **nunca más**. **Saqueo:** al morir un enemigo en combate de la escuadra, un guardia libre con espacio en la mochila recoge el botín cercano (≤8 bloques, tras terminar la pelea, política configurable: todo / solo munición y comida / nada).


**E2-H · Logística y entrenamiento en el cuartel (la base de un sistema eventual de logística).**
1. **Reabastecer en el cuartel:** los guardias dentro del radio del cuartel (config, por defecto 12 bloques) con la escuadra en reposo (no en combate) revisan —repartido en el bucle de 20 ticks, **un guardia por tick de la escuadra, nunca todos a la vez**— los **contenedores cercanos** (cofres, barriles, **maletas colocadas en el suelo** de Sophisticated Backpacks por `Capabilities.ItemHandler.BLOCK`, y maletas soltas como ítems en el suelo) y **toman**: comida, munición que usan, y **armas solo del mismo tipo que la suya** (misma clase: espada↔espada, arco↔arco; solo si es mejor que la actual). Respeta cupos (no vaciar el cofre: máx. N ítems por visita) y deja los sobrantes.
2. **Entregar el botín:** al volver, depositan en un contenedor del cuartel el **botín de guerra** que lleven (todo lo que no sea su equipo ni su munición), de modo que haya material para el futuro sistema de saqueo (que busquen mejor armadura, etc.). No equipan armaduras de ahí todavía.
3. **Punto de suministro temporal:** el comandante puede **designar un cofre o maleta en el terreno** (agachado + G sobre él, como el punto de retirada temporal; mismo mecanismo y caducidad que `CommanderMarks`) y la escuadra seleccionada va a abastecerse allí con las mismas reglas, una vez, y vuelve a su puesto o a seguirte. Si es una maleta en el suelo o un cofre sin nada útil, lo dice con un mensaje corto.
4. **Entrenamiento:** el muñeco de práctica de E2-E se coloca junto al cuartel y los guardias del cuartel en reposo lo golpean (sin gastar durabilidad ni munición) hasta rango 1, **nunca más** (ver E2-E).
5. Todo con tope de config (`supplyRadius`, `supplyItemsPerVisit`, `supplyIntervalTicks`), sin escanear bloques por tick (cachea los contenedores del cuartel y refréscalos cada ~5 s).


**ARREGLOS DE E1 (David los pega aquí):**
_(vacío si no hay)_

**Entrega:** un commit, `PORTING_PLAN.md` y `CLAUDE.md` actualizados, informe ≤25 líneas con qué NO quedó.

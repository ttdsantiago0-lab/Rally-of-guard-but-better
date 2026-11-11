# Rally Tactical System: el cerebro que coordina

2026-11-11 · A partir de la conversación de David con ChatGPT y de sus ideas, adaptado al código que ya existe.
Complementa `docs/PLAN_MAESTRO.md` (etapas), `docs/LABORATORIO.md` (cómo se descubre) y `docs/MANUAL_DE_CAMPO.md`
(lo confirmado). Si este documento y el código no coinciden, manda el código y este documento se corrige.

## 1. La idea en una frase

Los guardias de Guard Villagers son el cuerpo; Rally es el cerebro. Lo que da miedo no es el arma, es la
**organización**: atacar, defender, cubrir y elegir blancos en equipo. Contra 1 blanco, 1000 guardias se enfocan;
2 guardias contra 100 mobs cambian de prioridad rápido; si un jefe suelta voladores, la fuerza se reorganiza. Es
simple pero no aburrido, y **no juega solo**: el tributo, los sueldos de veteranos, la logística, las campañas del
Battle Planner, las formaciones propias y los muros que arma el jugador siguen importando.

No es un manual rígido. Es una **evaluación ponderada**: variables y condiciones que clasifican la amenaza, generan
una respuesta colectiva y dan a cada guardia un rol. Es un "programa" pequeño, hecho de datos, que crece con las
simulaciones.

## 2. Los cuatro niveles, y qué hay ya en el repo

| Nivel | Pregunta | Ya existe | Falta |
|---|---|---|---|
| 1. Evaluación | ¿Qué hay delante y cuánto importa? | `tactics/Situation` (señales baratas, 1 vez/s por guardia), `force/SituationReports` (los reportes suben por la cadena; enemigos conocidos con caducidad), `veterancy/ThreatMemory` (miedo aprendido por tipo de enemigo), `combat/FieldManual` (familias de amenaza) | **Agregación de amenazas** (un "grupo de 30 flechas" es una amenaza, no 30); las cuatro dimensiones de §3 |
| 2. Planificador táctico | ¿Qué maniobra hace la fuerza? | `force/ForceBrain` (1 vez/s por fuerza, escalonado), `force/Operations` (HOLD_LINE, FALLBACK_FIGHTING, ADVANCE_BY_TURNS, FLANK, ORDERLY_RETREAT), `force/PlanV2` + `PlannerService` (el plan del jugador), `Doctrine` | La **biblioteca de maniobras** medida (§5); la elección ponderada entre buenas opciones (§6) |
| 3. Asignación de roles | ¿Quién hace qué? | `force/Roles` (ANVIL, HAMMER, SCREEN, SUPPORT, FLANK, RESERVE), `force/Forces` (capitán y general derivados), `force/Leadership` (0..100), `command/CommandStructure`, `CaptainSuccession`, `SquadDirectives`, `UnitOrders` (órdenes versionadas por unidad) | Roles **dinámicos** que cambian con la amenaza; límite de ≤10 comandantes + 1 general (§7) |
| 4. Ejecución individual | ¿Qué hace este guardia este segundo? | `tactics/DecisionLadder` (función pura; peldaños de UNSTUCK a PAIR_BOUND), `Tactics`, `AdvancedCore` (rango 5), `combat/RangedFireGoal`, `Engagement`, `Escort`, `Testudo`, nuevo `combat/FireWindows` | Variación controlada (§6) |

**Lectura honesta del estado (medido, no supuesto):** la estructura es ancha, la cooperación medida es poca. En la
ronda de calibración un plan rindió igual que no tener plan. Las victorias medidas por coordinación son: la pantalla
de escudos de la escolta, la tortuga, la volea a 60 contra pistoleros (ronda 36) y, en prueba esta noche, el fuego
coordinado. El trabajo de las próximas etapas no es añadir clases: es que cada nivel **gane algo medible**.

## 3. Evaluación: cuatro dimensiones, no una

| Dimensión | Qué mide | Ejemplo |
|---|---|---|
| **Peligro** | Cuánto daño puede hacer si nadie lo para | Tremorzilla: enorme |
| **Urgencia** | Cuánto tarda en hacerlo | Un creeper a 3 bloques: inmediato |
| **Prioridad** | Cuánto importa para el objetivo de la misión | El que ataca al aldeano escoltado |
| **Viabilidad** | Si podemos hacer algo útil contra él ahora | Un volador fuera de alcance: baja |

Se calculan por **grupo de amenaza**, no por entidad: los enemigos cercanos del mismo tipo se agregan (centro,
cantidad, salud total, alcance efectivo). Con 1000 flechas en el aire no se evalúa cada flecha: "cubrirse" es una
decisión barata y clara que dispara un solo evento.

Primera regla del programa salida de una simulación (ronda 36): **si nuestro alcance efectivo supera el suyo,
mantener la distancia y disparar en volea** (saqueadores con Pax: arqueros 0 % → 83 %, línea de 46 % → 94 % de
supervivencia). Esta noche se confirma con otras semillas, se barre la distancia y se separa "distancia" de "modo
volea" (ronda 37).

## 4. Tres capas de conocimiento separadas

1. **Restricciones físicas** (no se discuten): i-frames de medio segundo, alcance y cadencia del arco, velocidad,
   física de la flecha. Las mide el laboratorio A (`SONDA_DE_TECHO.md`).
2. **Reglas de mando** (las pone el diseño): precedencia jugador > plan > adaptación táctica > individuo; el
   compromiso del plan (§8); nunca disparar con un amigo cerca del punto de impacto.
3. **Conocimiento táctico experimental** (lo descubren las simulaciones): qué maniobra gana contra qué. Vive en datos
   (`MANUAL_DE_CAMPO.md` y su versión en código, `FieldManual`) y crece con cada ronda confirmada.

## 5. Biblioteca de maniobras (primera versión)

| Maniobra | Estado | Evidencia |
|---|---|---|
| Tiro a quemarropa, tirar desde donde están | Confirmada | Manual v0 |
| Escudo arriba / corro de escudos contra tiradores | Confirmada | Manual v0 |
| Pantalla de escudos en la escolta | Confirmada | Ronda 17 |
| Tortuga (testudo) | Confirmada (contextos concretos) | Ronda 18 |
| Volea a distancia contra pistoleros | Medida, confirmando | Ronda 36 → 37 |
| Fuego repartido / sincronizado con la invulnerabilidad | En prueba | Ronda 38 (esta noche) |
| Retirada ordenada | Existe la orden (RETIRARSE, ORDERLY_RETREAT), sin medir | Ronda 39 (esta noche) |
| Calidad sobre cantidad contra jefes | Confirmada como dato | Rondas 30, 35 |
| Kitear | Oráculo lo hace; regla propia pendiente | Ronda 27 |
| Flanqueo, avance por turnos, yunque y martillo | Existen en código, sin ganancia medida | Pendiente |

## 6. Variación controlada

Dos guardias idénticos pueden elegir distinto entre las **buenas** opciones (≈6), nunca entre buenas y malas:
selección ponderada por puntuación, con una semilla reproducible por guardia y por batalla (el mismo replay da lo
mismo). La personalidad (etapa 7) solo mueve los pesos, nunca abre opciones malas.

## 7. Jerarquía de mando y escala

- Un **general** por fuerza y **hasta ~10 comandantes** (uno por escuadrón). Solo ellos corren el motor de
  planificación; los soldados ejecutan su peldaño de la escalera con señales baratas.
- Coste: el motor corre 1 vez por segundo por comandante, escalonado (ya es así en `ForceBrain`). Con 10 escuadrones
  son 10 evaluaciones por segundo, no 1000.
- Si muere el comandante, sucede otro (`CaptainSuccession`); mientras tanto la unidad sigue la última orden.

## 8. Compromiso del plan (idea de David)

El Battle Planner tiene un control de **compromiso** de 0 a 100 %:
- **0 %**: el motor puede cancelar o cambiar la maniobra si aparece una amenaza nueva (más inteligente, menos
  predecible).
- **100 %**: la maniobra se ejecuta pase lo que pase (el jugador manda).
- En medio: el motor solo cambia si la nueva amenaza supera un umbral que crece con el compromiso.

No es "certeza táctica": es cuánto se respeta la orden. Se implementa sobre `PlanV2` cuando el planificador gane algo
medible (fase 4).

## 9. Moral: del motor, no de la vida

Decisión de David: la moral **no** sale del % de vida, sale de la evaluación colectiva.
- **Novato**: huye cuando ve que su batallón pierde, no cuando cree que va a morir.
- **Experimentado**: sabe que una batalla se puede voltear; se debilita tras muchas campañas perdidas o una explosión
  cerca.
- **Veterano**: acepta morir si la guerra se gana. Hoy, en código: rango ≥ `fearlessRank` (3) nunca teme ni se rompe.

Las pruebas siguen **sin moral** (`off:morale`) hasta que el motor funcione: la ronda 34 mostró que la moral actual
rompía al 79–90 % de los guardias contra jefes y escondía todo lo demás. Se reimplementa en la fase 6, alimentada por
la evaluación (§3) y el estado de la batalla, y se mide igual que cualquier maniobra.

## 10. Historia del veterano ("cazadragones")

Cada guardia lleva un registro (ya existe `VeterancyRegistry`: rango, maestría por arma, miedo por enemigo). Se añade
la historia por **tipo de enemigo vencido**: un veterano hecho contra dragones gana maestría con su arma (p. ej. el
arco de dragón) y, más adelante, quizá rasgos leves (resistencia al fuego). "Ya veremos": entra después de que la
moral nueva funcione, y llegar a veterano o maestro sigue siendo lento y difícil.

## 11. Laboratorio de retirada

Pregunta: ¿cuántos escapan y cuánto tardan, si 5 dragones los persiguen? Primera versión esta noche (ronda 39):
8 guardias (a pie o a caballo) contra 1, 3 o 5 dragones jóvenes o adultos; RETIRARSE contra pelear; supervivientes a
los 2 minutos y distancia mínima al enemigo. Límite conocido: la arena mide 96 bloques con muro; la versión buena
necesita terreno abierto (`LABORATORIO.md` §3) y medir llegada a un punto seguro.

## 12. Seguridad: el motor nunca tumba el juego

- **Por eventos**, no por tick: se reevalúa cuando cambia algo (aparece o muere un grupo de amenaza, llega una orden),
  y como mucho 1 vez por segundo por comandante.
- **Agregación**: grupos de amenaza, nunca entidad por entidad; proyectiles nunca se evalúan uno a uno.
- **Presupuesto**: cada sistema mide su coste (`debug/Perf`) y tiene un tope por tick. Ya existe para el núcleo
  avanzado (rango 5 → escalera simple si se pasa, motivo `DEGRADED_BUDGET`) y se extiende a todo el motor.
- **Niveles de degradación**: normal → alto (evaluar cada 2 s, sin variación) → crítico (solo la última orden y la
  escalera) → falla (el motor se apaga, Guard Villagers sigue solo, aviso en el registro). Nunca una excepción sin
  atrapar en el tick del servidor.
- **Topes duros**: consultas de entidades con radio y número máximo (p. ej. `FireWindows` revisa como mucho 16
  candidatos y solo cuando hay conflicto).
- **Trazas**: cada decisión deja su motivo (`DecisionReason`, `act:*` en la simulación) para poder explicar qué pasó.
- **Prueba de estrés**: escenarios de 200–500 entidades en el laboratorio D antes de cada versión.

## 13. Armas de fuego y el kit de los guardias

Medido (ronda 36): Scorched Guns rearma con pistolas a **todo** guardia que aparece (probabilidad 1.0). En la
simulación ya no lo hace salvo con `sg:guardGuns`. En el juego, **decisión pendiente de David**: dejar que Scorched
Guns arme a los guardias de Rally, o protegerles el kit (recomendado: el equipo lo decide el jugador y la logística).

## 14. Fases y recursos

| Fase | Qué | Puerta (qué tiene que medirse) | Sesiones de Claude | Corridas |
|---|---|---|---|---|
| 0 | Auditoría y este documento | — | hecho | — |
| 1 | Medir lo que hay: cada maniobra existente contra su base | Lista de maniobras con ganancia real | 2–3 | 500–1000 |
| 2 | Fuego coordinado y distancia contra pistoleros | Ganancia confirmada en 2 lotes | 1–2 | 300–600 |
| 3 | Evaluación con agregación y las 4 dimensiones | Mismo resultado con 10× entidades y coste plano | 3–4 | 200–500 de estrés |
| 4 | Planificador: biblioteca de maniobras, selección ponderada, compromiso del plan | El plan gana a no tener plan | 4–6 | 1000–2000 |
| 5 | Roles dinámicos y jerarquía (≤10 comandantes + general) | Reorganización medible (jefe con voladores) | 3–4 | 500–1000 |
| 6 | Moral desde el motor | No esconde la funcionalidad; novato/experto/veterano distinguibles | 3–4 | 500–1000 |
| 7 | Historia del veterano y cazadragones | Progresión lenta y visible | 2–3 | 200–500 |
| 8 | Seguridad completa: degradación, topes, trazas | Ninguna caída en estrés; coste por tick bajo el tope | 2–3 | 100–500 de estrés |
| 9 | Pulido en el juego con David | David lo aprueba jugando | según David | — |

Total: unas **20–30 sesiones de Claude** y **5000–10000 corridas** (gratis en las máquinas de GitHub: unas pocas
noches de 16 máquinas). La estimación de ChatGPT (34–66 días de desarrollo) es para una persona; aquí el coste real
es tu tiempo de revisión (≈1 h al día) y las decisiones de diseño que solo tú puedes tomar.

**Riesgos:** que una maniobra gane en la arena y no en terreno real (por eso el laboratorio de terreno va antes del
planificador grande); que la moral vuelva a esconder todo (por eso va después); que el coste crezca con la escala
(por eso la agregación va en la fase 3, antes de los roles).

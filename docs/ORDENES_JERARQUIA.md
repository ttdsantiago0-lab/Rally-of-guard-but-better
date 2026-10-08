# Jerarquía de órdenes y precedencia (E3A A2, 2026-10-08)

Responde a `docs/CONTROLES.md` §4 (preguntas abiertas). Código: `force/UnitOrders`, `force/ForceBrain`, `command/SquadDirectives`, `tactics/DecisionLadder`. Probado solo en simulación (`precedence-player-vs-force`), no en juego.

## 1. Niveles (cada uno compila órdenes para el de abajo y nunca lo salta)
| Nivel | Quién decide | Produce | Lo aplica |
|---|---|---|---|
| Plan | jugador (Battle Planner) | fases, vínculos, reglas | la fuerza |
| Fuerza | general calculado | papeles, maniobra, reglas de doctrina | **directiva versionada por unidad** (`UnitOrders`) |
| Unidad | capitán calculado | formación, estilo de fuego, ritmo (calidad = puntaje de liderazgo) | cada guardia en su turno |
| Guardia | su escalera de decisión | objetivo, cobertura, distancia, retirada | él mismo |

Atacar/defender significan cosas distintas en cada nivel: **guardia** = objetivo y forma de pelear; **unidad** = maniobra con formación (mantener, retroceder atacando, avance por turnos); **fuerza** = reparto de papeles (yunque, martillo, pantalla, apoyo, flanco, reserva); **plan** = secuencia de fases.

## 2. Precedencia (de mayor a menor)
1. **Mantener fuego** (H) es absoluto: solo H / Soltar fuego lo levanta.
2. **La orden explícita del jugador** (Libro, G, H agachado, Formar, Atacar…) a un guardia **gana durante `leadership.playerOverrideSeconds` (60 s)** sobre cualquier directiva automática (plan, fuerza, capitán). Pasado ese tiempo, el guardia vuelve a aplicar la versión más nueva de la directiva de su unidad.
3. **Directivas automáticas** de la unidad: hay **un solo registro por unidad**; la versión más nueva gana, venga de una regla de fuerza, de la maniobra o de una fase del plan.
4. **Reglas de fuerza**: entre reglas activas sobre la misma unidad gana la de **mayor prioridad** (por regla, no por tipo); empate = orden de definición. Histéresis: entrar al 40 % de bajas y salir al 20 %, con duración mínima; no alternan en 39↔41 % (autoprueba en cada `summary.md`).
5. **Escalera de decisión** del guardia: resuelve el "cómo" dentro de lo que dicen 1–4 (nunca cambia el modo ni el puesto).

## 3. Respuestas a CONTROLES §4
- *Capitán emite directiva y el jugador da una orden individual*: gana el jugador durante 60 s; luego vuelve la directiva vigente de su unidad (la simulación lo mide: "Precedencia" en `summary.md`).
- *Pasiva + Atacar + Mantener fuego*: Mantener fuego manda (solo designa el objetivo). Sin Mantener fuego, una orden de Atacar explícita se cumple aunque sea Pasiva; Pasiva sin orden solo se defiende de quien la golpea (F5).
- *Puesto + Implacable*: sin cambios (ver CONTROLES §3).

## 4. Escala
Una orden de fuerza a 200 guardias = unas pocas escrituras (una por unidad) + una comparación de versión por guardia en su turno de 1 s. Los informes de unidad suben **solo cuando cambian** (`SituationReports`).

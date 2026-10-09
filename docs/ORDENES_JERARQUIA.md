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

## 5. Instrucciones generales e interpretación por perfil (E3B G3, 2026-10-09)
Una **instrucción** (Hostigar, Mantener la línea, Avanzar, Retirarse, Flanquear; Libro de Mando, barra de grupo) no es una orden nueva: `command/InstructionInterpreter` la traduce, **para cada subgrupo**, según su familia de perfil (`command/GroupProfile`: a distancia, caballería de rango, infantería, caballería) en piezas que ya existían: doctrina, estilo de fuego, densidad, `UnitOrders.Kind`, distancias y ciclo. Tabla editable en `config/rallyguard-instructions.json` (se lee una vez por arranque).
- Un grupo con subgrupos recibe **una** instrucción y cada subgrupo la interpreta con su perfil (los arqueros hostigan a distancia; la infantería pesada amenaza un flanco con escudos; la caballería golpea y se va).
- `command/InstructionRunner` la ejecuta a 1 Hz por subgrupo emitiendo directivas versionadas de origen PLAYER; **cualquier orden explícita** a esos guardias la termina.
- Precedencia: igual que una orden del jugador (nivel 2 de §2). Mantener fuego sigue siendo absoluto.

## 6. Escenarios, plan y moral (E3B P2/M)
De mayor a menor, a nivel de fuerza: **retirada de fuerza** (regla con histéresis) > **escenario detectado** (`force/Scenarios`, respuesta por papel; congela el plan y aparta la operación, que vuelven al terminar) > **fase del plan** y sus vínculos > operación. La orden explícita del jugador sigue ganando 60 s a cualquiera de ellos.
A nivel de guardia, la **moral** entra en la escalera justo después de las órdenes de retirada: un guardia ROTO huye a su capitán o al HQ aunque tenga una directiva (salvo acorralado); VACILANTE en unidad cohesionada se niega a atacar pero mantiene el puesto. Moral = preferencias; nunca cambia vida, daño ni velocidad (comprobado en cada `summary.md`).

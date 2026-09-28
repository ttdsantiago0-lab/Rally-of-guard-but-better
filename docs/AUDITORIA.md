# Rally of the Guard — Auditoría de arquitectura y diseño (2026-09-28)

Auditoría hecha sobre el código real del repo (commit `f3cf182`), `CLAUDE.md`,
`PORTING_PLAN.md` (§0–§33) y el **código fuente real de Guard Villagers 1.21.1**
(`seymourimadeit/guardvillagers`, rama `1.21.1`, `Guard.java`) y de NeoForge
(`Mob.java.patch`, `CommonHooks`). Cada afirmación marcada como "verificado" se leyó
en ese código, no se dedujo.

> **Límite importante:** este entorno no puede llegar a los Maven de NeoForge ni de
> Mojang, así que **no compilé**. Todo lo implementado en la Fase 3E pasó un chequeo
> de sintaxis (`javac` en modo parse) y una revisión manual de tipos e imports, pero
> **necesita tu `.\gradlew.bat build`** antes de darlo por bueno.

---

## 1. Arquitectura actual

```
┌───────────────────────── Guard Villagers (dependencia) ─────────────────────────┐
│ Entidad Guard · objetivos de IA (melee, arco, ballesta, escudo, patada, comer)   │
│ FollowHeroGoal (seguir) · WalkBackToCheckPointGoal (volver al puesto/patrulla)   │
│ HurtByTarget / HeroHurt* / NearestAttackableTarget (elección de objetivo)        │
│ Guard#setTarget (override: rechaza blacklist, Héroe de la Aldea, aliados, dueño) │
└─────────────────────────────────────────────────────────────────────────────────┘
            ▲ API tipada (owner, following, patrolling, patrolPos, setTarget)
┌───────────────────────────── Rally of the Guard ────────────────────────────────┐
│ contract/   GuardOwnership + 5 SavedData por UUID: Squad, Roster, Stance,        │
│             Formation y (3E) Directive                                            │
│ formation/  geometría pura: FormationShape (LINE/COLUMN/V/CIRCLE) → Formation     │
│ order/      OrderExecutor (1 método) + órdenes; (3E) CommandTargeting, GuardPosts,│
│             TargetSpec, CommanderMarks                                            │
│ event/      GuardStanceHandler = puerta de combate + escaneo 1/s; fuego amigo,    │
│             neutralidad, caída, disciplina de montura, limpieza al morir          │
│ network/    payloads C2S por acción + lista S2C; GuardCommandNetworking valida    │
│             dueño por guardia en el servidor                                      │
│ screen/     Commander's Ledger (GuardCommandScreen), menú de contratación         │
│ item/       Libro del Comandante (panel + puntero 3E), Pergamino de Reunión       │
└─────────────────────────────────────────────────────────────────────────────────┘
```

Flujo de una orden: clic en el Ledger → payload C2S con los `entityId` elegidos → el
servidor resuelve **solo** guardias cargados y tuyos → `OrderExecutor` escribe estado
de Guard Villagers y/o de los registros de Rally → reenvía la lista → el Ledger se
refresca.

## 2. Lo que está bien (y hay que preservar)

- **La API tipada de Guard Villagers en lugar de NBT a mano.** Es correcta y
  sincroniza con los clientes.
- **La regla de no usar mixins, que obliga a composición.** Los handlers son pequeños
  y cada uno tiene un propósito.
- **`OrderExecutor` con un solo método y parámetros por constructor.** Es la
  abstracción correcta para un futuro Battle Planner: un paso del plan es simplemente
  "construir una orden y ejecutarla".
- **Formaciones como geometría pura.** Agregar una forma es agregar un `localOffsets`.
- **IDs en texto que fallan en silencio.** Stance, órdenes, formas y (3E) objetivos
  viajan como texto, así que dos versiones distintas no se desincronizan.
- **Registros por UUID.** Permiten ver guardias descargados y sobreviven recargas.
- **Validación en el servidor.** Nunca se confía en el cliente sobre la propiedad de
  un guardia.
- **La disciplina de documentación y de "no afirmar sin verificar".** Gracias a eso
  esta auditoría pudo encontrar los errores de abajo.

## 3. Áreas frágiles (con bugs reales encontrados)

Todos los bugs de esta sección están **verificados en el código fuente** y
**corregidos en la Fase 3E**.

1. **Crítico: Defensivo y Pasivo nunca podían soltar un objetivo.** NeoForge dispara
   `LivingChangeTargetEvent` también para `setTarget(null)`. La puerta de postura
   cancelaba ese evento, así que un guardia Defensivo (la postura por defecto de
   **todos**) que peleó una vez quedaba con su objetivo pegado, incluso muerto. Eso
   además rompía lo siguiente:
   - volver al puesto de patrulla y el teletransporte al dueño (Guard Villagers exige
     `getTarget() == null` para ambos);
   - la orden Hold;
   - el botón de Patrulla;
   - el "soltar" del Pergamino;
   - el arreglo de "Pasivo corta la pelea" de §29, que en realidad nunca funcionó.
2. **Atacar no hacía nada con guardias Defensivos o Pasivos.** La misma puerta vetaba
   el `setTarget` de la orden.
3. **Implacable no puede atacar creepers ni nada de la blacklist.** `Guard#setTarget`
   de Guard Villagers los rechaza antes que cualquier evento, así que la premisa de §27
   era falsa. Peor todavía: el escaneo seguía eligiendo al creeper como "el más
   cercano" cada segundo e **ignoraba a todos los demás mobs**.
4. **Las formaciones no se sostenían.** Muster, Reform y Retreat eran un solo
   `navigation.moveTo`: el siguiente objetivo de IA lo pisaba, y un guardia que
   patrullaba volvía a su puesto **anterior**.
5. **Cada orden cerraba y reabría el Ledger sin selección.** Dar dos órdenes seguidas
   a la misma escuadra obligaba a volver a seleccionarla.
6. **El Ledger medía 316 px fijos.** Se salía de la pantalla con escala de GUI 4 en
   1080p.
7. **Escaneo cuadrático.** Agresivo e Implacable recorrían **todas** las entidades del
   nivel, por cada guardia, cada segundo.

Pendientes, **no corregidos**:

- **El datapack de monturas (`GuardVillagersMounts-v1.2.zip`) vive fuera de git.** Si
  se pierde, se pierde lógica real del mod.
- **Los mapas estáticos persisten entre mundos en un jugador.** Pasa con
  `HiredGuardsNeutralityHandler.TRACK`; los de 3E sí se limpian al detener el
  servidor.
- **`HireGuardMenu.stillValid` devuelve siempre `true`.** No verifica distancia.
- **Las órdenes solo alcanzan guardias cargados en tu dimensión.** Es inherente, pero
  el Ledger debería decirlo.

## 4. Deuda técnica

- **Javadocs de 60–100 líneas mezclan historia y comportamiento.** La historia ya vive
  en `PORTING_PLAN.md`. Recomiendo que el código describa lo que hace hoy y recortarlo
  poco a poco, sin un pase masivo.
- **Cinco `SavedData` casi idénticos.** Riesgo: cada limpieza al morir hay que
  acordarse de agregarla en cinco lugares. Ver §8.
- **Un payload por cada orden con parámetros.** Muster, Retreat y Attack tienen cada
  una el suyo. Se podrían unificar en `IssueCommand(ids, orderId, arg)` más adelante.
- **Constantes sin configuración.** El coste de 3 esmeraldas, la correa de 16 bloques,
  los rangos de 32 y 16 bloques. Falta un `ModConfigSpec` pequeño.
- **`pt_br` desactualizado.** Le faltan unas 40 claves. En esta fase agregué `es_es` y
  `es_mx` completos.
- **Sin gametests ni CI**, y tampoco hay forma de compilar desde las sesiones de
  Claude. Todo depende de tus builds.
- **La versión se llama `0.1.0-neoforge-skeleton`**, aunque ya no es un esqueleto.

## 5. Sistemas que faltan

| Sistema | Estado |
|---|---|
| Objetivo explícito (jugador, entidad, contraataque) | **Hecho en 3E** |
| Alto el fuego / Fuego libre | **Hecho en 3E** |
| Mantener posición real (muro, puerta) | **Hecho en 3E**: puesto + correa |
| Destino fuera de tu posición | **Hecho en 3E**: marca con el Libro |
| PvP entre ejércitos (los guardias de otro comandante no respondían) | **Hecho en 3E** |
| Órdenes por escuadra resueltas en el servidor | Falta. Hoy solo por selección |
| Battle Planner (señales, secuencia, condiciones) | Falta |
| Guarnición/hogar + notificaciones | Falta |
| Logística (munición, comida, reposición) | Falta |
| Monturas que se muevan (guardia conduciendo el caballo) | Falta. Hoy siempre desmonta |
| Feedback en el mundo (vista previa de formación, indicador de objetivo) | Parcial: partículas al marcar |
| Kamikaze, artillero de CBC | Diseño, ver §14 y las preguntas al final |

## 6. Oportunidades de jugabilidad (lo que 3E ya permite combinar)

| Composición | Resultado |
|---|---|
| Arqueros + Muster Línea sobre el muro marcado + objetivo "Jugador X" + Alto el fuego | Emboscada: esperan en el muro, **no se mueven**, y con "Fuego libre" disparan todos a la vez contra X |
| Caballería/infantería libre (Romper filas) + Atacar "Jugador X" | Carga: persiguen a X hasta 32 bloques y lo re-adquieren cada vez que lo pierden |
| Escuadra en V + Mantener + Defensivo | Muralla viviente: solo responden a quien les pega y vuelven a su hueco |
| Pasivo + objetivo designado | "Asesinos": ignoran todo excepto su blanco |
| Cualquiera + Retirada al punto marcado | Rompen el combate, olvidan el blanco y se reforman allá en su última formación |
| Tu ejército contra el de otro jugador | Si un guardia enemigo los ataca, responden (sin que una flecha perdida de un aliado desate una guerra) |

## 7. Oportunidades visuales y de UX

1. **Chips de escuadra en el Ledger.** Una fila de botones de color, uno por escuadra,
   que reemplace el botón "▾". Un clic la selecciona entera.
2. **Vista previa de formación.** Partículas visibles solo para el comandante en los
   huecos, durante 2 segundos después de un Muster. Comunica "aquí van".
3. **Indicador de objetivo designado.** Partículas solo para el comandante sobre el
   blanco, cada 2 segundos. El enemigo no lo ve.
4. **Sonido de orden.** Un cuerno corto al dar Muster, Atacar o Fuego libre (el cuerno
   de cabra de vanilla sirve). Se vuelve el "sonido del comandante".
5. **Notificaciones en la barra de acción en lugar del chat.** Hoy cada orden escribe
   en el chat y lo llena.
6. **Ledger por pestañas** cuando llegue el Planner: **Ejército | Escuadras | Plan**.
   No agregar más filas al panel actual; ya tiene 5 barras.

Todo esto comunica algo: dónde, a quién o qué pasó. Nada es solo espectáculo.

## 8. Simplificaciones posibles

- **Un solo `ArmyRegistry` por guardia** (escuadra, postura, directiva, hueco de
  formación) en lugar de cuatro `SavedData`. Implica migrar partidas guardadas.
  Hacerlo **antes** del Planner, no después.
- **Un solo escaneo por segundo.** `GuardStanceHandler`, `GuardMountDisciplineHandler`
  y la neutralidad recorren los guardias cada uno por su lado. Uniéndolos en un
  "ArmyTick" se recorre una sola vez.
- **Un solo payload de orden con argumento opcional.**
- **Postura y directiva se leen juntas en todas partes.** Una vez unificado el
  registro, el `Status` del payload sale directo.

No hice ninguna de estas en 3E, a propósito: cambian el guardado y conviene hacerlas
en una fase dedicada, con un build verificado de por medio.

## 9. Roadmap mejorado

1. **3E Intención del comandante: HECHO, falta tu build y prueba.** Puerta de combate
   corregida, puestos, correa, objetivo explícito, alto el fuego, marcas y PvP entre
   ejércitos.
2. **3F Consolidación.** El `ArmyRegistry` único, un tick único, config de valores
   ajustables y guardar el datapack de monturas en el repo. Corto, sin jugabilidad
   nueva.
3. **4 Battle Planner v1 ("Señales").** Un plan es una lista ordenada de pasos. Cada
   paso es: *escuadra + orden (Muster/Mantener/Atacar/Retirada/Fuego) + forma +
   objetivo*, y se dispara por *señal manual N / X segundos después del paso anterior
   / al ser atacada la escuadra / al perder X% / al llegar a su puesto*. Son solo 5
   condiciones: no es un lenguaje de programación. Se ejecuta con los mismos
   `OrderExecutor`, y la escuadra se resuelve en el servidor por `SquadRegistry`.
   Planes guardados por jugador. La señal se da con el Pergamino, el Libro o una
   tecla.
4. **5 Guarnición.** Un "hogar" por escuadra (una marca guardada). Las escuadras en
   guarnición se defienden solas. Alertas en la barra de acción: enemigo detectado,
   guardia en combate, guardia muerto, intruso dentro del radio. Orden "Volver a
   casa".
5. **6 Logística.** Cofre de guarnición: los guardias que vuelven a casa reponen
   flechas, comida y armadura rota. "Volver a reabastecerse" automático por debajo de
   un umbral.
6. **7 Monturas.** Que el guardia *conduzca* la montura (pasarle el destino de
   navegación a la montura en lugar de desmontar). Primero caballo, después
   Hipogrifo, Candicorn y Komodo por configuración, **reusando su propia IA de vuelo
   o de movimiento**.
7. **8 Roles especiales como órdenes.** Kamikaze y artillero de CBC (ver §14).
8. **Después:** renegados, Aeronautics/Sable (ojo: Sable ya causó el bug del AABB) y
   la UI a gran escala.

## 10. IMPRESCINDIBLE

- Compilar y probar 3E. Los bugs de §3.1 afectan a **todas** las partidas.
- Órdenes por escuadra resueltas en el servidor. Es la base del Planner.
- Battle Planner v1 con señales manuales y 5 condiciones.
- Consolidar los registros antes de que el Planner agregue más estado.

## 11. ALTO VALOR

- Guarnición con notificaciones.
- Reabastecimiento automático de munición.
- Feedback en el mundo: vista previa de formación, indicador de objetivo, cuerno.
- Chips de escuadra y Ledger por pestañas.
- Guardias que conducen su montura.

## 12. BUENO TENERLO

- Más formas: U, Cuña, Flancos. Cada una es un `localOffsets`.
- Traducción `pt_br` completa.
- Kamikaze, artillero de CBC, Hipogrifo.
- Renegados.

## 13. EVITAR

- **Reescribir la IA de combate o navegación de Guard Villagers.** 3E demuestra que su
  puesto de patrulla + `setTarget` alcanzan.
- **Una entidad "guardia de muro".** Puesto + correa + postura + alto el fuego ya lo
  hacen.
- **Un sistema de adquisición de objetivos general.** Los 5 modos de `TargetSpec`
  bastan.
- **Condiciones libres o scripting en el Planner.**
- **Inflar stats para vencer a jugadores.** El poder debe venir de la coordinación.
- **Más filas en el Ledger actual.** El próximo cambio de UI son pestañas.
- **Una IA de vuelo propia.** Usar la del mod de la montura.

## 14. Implementación recomendada

**Ya implementado en esta sesión: Fase 3E, Intención del comandante** (detalle en
`PORTING_PLAN.md` §34). **Siguiente paso recomendado:** probar 3E y, si funciona,
hacer **3F (consolidación) y luego el Battle Planner v1**.

### Kamikaze: propuesta de diseño (no implementada, requiere tu OK)

Una **orden**, no una postura. El guardia designado lleva el explosivo en la mano
secundaria; el jugador se lo da en el inventario de Guard Villagers.

- **Sin lista blanca.** Al llegar, el guardia coloca ese bloque y llama al gancho
  genérico de NeoForge `onCaughtFire`, que es lo que hace el fuego o un encendedor con
  cualquier TNT, incluida la de Lucky TNT si su bloque lo implementa. Hay que
  verificarlo con el jar de Lucky TNT.
- **Mecha corta y fiable.** Si la entidad cebada es un `PrimedTnt`, se le fija una
  mecha corta.
- **Protección de aliados.** El dueño de la explosión es el guardia, así que el
  filtro existente de fuego amigo entre guardias del mismo comandante ya protege a
  sus aliados.
- **Secuencia:** sale de la formación, recibe Velocidad y Resistencia temporales,
  persigue al objetivo designado (el mismo `TargetSpec`) y a 3 bloques detona.
  **Siempre muere.**

### Artillero de Create Big Cannons: propuesta (no implementada, requiere tu OK)

Un guardia en su puesto junto a una montura de cañón de CBC. Rally solo le da *a
quién* y *cuándo* (objetivo designado + alto el fuego/fuego libre). El apuntado y el
disparo usan la API del propio cañón de CBC, igual que la Kamikaze usa el explosivo
de otro mod.

Contra un mago de Ars Nouveau, el candidato natural es el **autocañón**, por su
proyectil rápido y cadencia alta. La artillería pesada no sirve contra blancos que
vuelan. Primero haría una investigación corta del código de CBC para NeoForge 1.21.1
(cómo fijar yaw/pitch y disparar) antes de prometer nada.

---

## Cómo probar la Fase 3E (checklist)

1. Compilar: `.\gradlew.bat build`, copiar el jar y verificar el checksum.
2. **El bug del objetivo pegado (el más importante):** con un guardia Defensivo por
   defecto, que mate un zombi. Después debe volver a su puesto (si patrulla) o
   teletransportarse a ti (si te sigue). Antes se quedaba "trabado".
3. **Formación que se sostiene:** selecciona 4 guardias y haz Muster en Línea.
   - Deben llegar y **quedarse**, sin dispersarse.
   - Pelean con lo que se acerque a menos de 16 bloques y **vuelven a su hueco**.
   - El botón de fila muestra "Detener" porque ahora son un puesto.
4. **Puntero:** agachado + clic derecho con el Libro sobre un bloque (por ejemplo la
   cima de un muro).
   - Aparece un mensaje y partículas verdes.
   - El panel muestra "Punto de formación: x y z" en amarillo.
   - Un Muster ahora forma **allá**.
   - Agachado + clic al aire borra la marca.
5. **Objetivo explícito:** agachado + clic derecho sobre un mob (partícula de enfado,
   solo la ves tú). Luego, en el panel:
   - elige "Marcado: …" con el selector y pulsa Atacar;
   - la fila debe mostrar "→ nombre" en dorado.
6. **Alto el fuego:** con guardias en su puesto, pulsa Alto el fuego.
   - Aparece "ALTO EL FUEGO" en rojo.
   - Aunque llegue un zombi o les peguen, **no atacan**.
   - Con Fuego libre atacan todos de inmediato a su objetivo designado, si está a
     menos de 16 bloques del puesto.
7. **Mantener, Romper filas y Reagrupar:**
   - Mantener: se plantan donde están.
   - Romper filas: vuelven a moverse libres.
   - Reagrupar: vienen a ti y te **siguen**.
8. **Implacable:** cerca de un creeper y de otros mobs, debe atacar **a los otros**
   (antes se quedaba mirando al creeper).
9. **PvP, si puedes con un segundo jugador o en LAN:**
   - elige "Jugador: X" y pulsa Atacar;
   - si los guardias de X atacan a los tuyos, los tuyos responden.
10. **El Ledger:**
    - La selección se mantiene después de cada orden.
    - Con escala de GUI 4 el panel cabe en pantalla.
    - Las escuadras se ven con su color.

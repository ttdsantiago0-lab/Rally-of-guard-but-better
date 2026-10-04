# Rally of the Guard — Arquitectura y decisiones (vivo, desde 2026-09-29)

Este documento es la referencia de **quién manda sobre qué** dentro de un guardia y de
los principios de robustez del mod. Cada cambio que toque un sistema existente debe
revisarse contra la matriz de la sección 3.

---

## 1. Jerarquía de autoridad (una sola fuente de verdad por concepto)

```
Orden (evento puntual del comandante)
   │  escribe, SOLO a través de sus APIs, en ▼
┌───────────────────────────────────────────────────────────────────────┐
│ MODO            FREE / FOLLOW / POST     ← flags nativos de Guard       │
│                 (GuardModes)                Villagers: Following,       │
│                                             Patrolling + posición       │
│ HUECO           formación recordada      ← FormationRegistry            │
│ POSTURA         Pasivo/Defensivo/...     ← StanceRegistry               │
│ DIRECTIVA       alto el fuego, objetivo, ← DirectiveRegistry            │
│                 protegido (Defender)                                    │
│ PERTENENCIA     escuadra / unidad        ← SquadRegistry                │
│ SELECCIÓN       quién obedece ahora      ← CommanderSelection (memoria) │
└───────────────────────────────────────────────────────────────────────┘
   │  se LEEN (nunca se escriben) en la ejecución ▼
Ejecución: objetivos de IA de Guard Villagers + puerta de combate
(GuardStanceHandler) + conductor de monturas (MountDriver)
```

**Reglas:**

1. **Un guardia está en exactamente un modo**, y el modo *son* los dos flags nativos
   de Guard Villagers. No existe una copia paralela de Rally. Por eso los botones
   nativos de Guard Villagers (Seguir / Patrullar) y el Ledger siempre están de
   acuerdo.
2. **Postura, directiva, hueco y escuadra son ortogonales al modo.** Cambiar la
   postura nunca cambia el modo; es decir, nunca "rompe" un Rally ni un puesto.
3. **Solo las órdenes cambian el modo**, y solo con `GuardModes.free / follow / post`.
4. **La ejecución solo lee.** Prioridad en combate:
   **alto el fuego > orden explícita del comandante > amenaza al protegido (Defender)
   > correa del puesto > objetivo designado > tope de combate > postura**.
   Desde 3H.1, alto el fuego es absoluto: Atacar solo *designa* mientras dure.
5. **Si la pantalla nativa deja ambos flags a la vez**, `GuardModes.normalize` lo
   resuelve en ≤1 s a favor del último que se activó: gana el último clic del jugador,
   venga de la interfaz que venga.

**Significado de cada concepto (no son sinónimos):**

| Concepto | Significado | Modo resultante |
|---|---|---|
| Seguir | "ven conmigo" | FOLLOW |
| Reagrupar / Llamar | "ven a mi posición de mando y espera" | POST frente al comandante |
| Rally (Pergamino) | "reúnanse y síganme" (contexto de mando temporal) | FOLLOW; al terminarlo, POST donde estén |
| Mantener | "quédate aquí" | POST donde está |
| Muster / Reformar / Avanzar | "toma/recupera/avanza tu hueco" | POST en el hueco |
| Retirada | "rompe el combate y repliégate": punto temporal > cuartel de la escuadra > seguirme | POST en el punto o el cuartel; si no hay ninguno, FOLLOW |
| Defender | "quédate junto a él y pelea con quien lo ataque" | POST que se mueve con el protegido |
| Romper filas | "actúa libre" | FREE |
| Huir | *(no existe aún)* sobrevivir, no volver a casa | — |

## 2. Registro de decisiones

Cada entrada registra qué estaba mal, el principio que se adopta, qué cambió y qué
queda pendiente.

### D1. Una autoridad para el modo (3G)
- **Qué estaba mal:**
  - Rally escribía `Following`/`Patrolling` desde unos 6 lugares, cada uno con su
    propia idea de qué limpiar.
  - La pantalla nativa de Guard Villagers podía dejar un guardia "siguiendo" y
    "patrullando" a la vez. Guard Villagers hace ganar a Seguir, así que "quédate
    aquí" no funcionaba.
  - "Liberar" borraba Seguir como efecto secundario.
- **Principio:** el modo vive en los flags nativos y se escribe por una sola API.
- **Qué cambió:** `GuardModes` y `normalize()`; todas las órdenes pasan por ahí.
  Terminar un Rally solo toca a los guardias que *siguen* en el Rally.
- **Queda para después:** el botón "Patrol" nativo de Guard Villagers necesita dos
  clics la primera vez (es un bug suyo: guarda el estado del botón en el cliente). No
  lo tocamos.

### D2. Llamar significa "espera aquí" (3G)
- **Qué estaba mal:** un guardia llamado llegaba y se iba a deambular, o te seguía.
- **Principio:** una llamada explícita deja al guardia bajo mando inmediato.
- **Qué cambió:**
  - Llamar (fila) y Reagrupar teletransportan, montura incluida, y dejan al guardia en
    POST frente al comandante.
  - Reagrupar además lo acomoda en formación y lo recuerda como hueco.
  - Nueva orden **Seguir** para "ven conmigo".
- **Queda para después:** nada.

### D3. Formaciones escalables y persistentes (3G)
- **Qué estaba mal:**
  - Las formas estaban pensadas para ~8 guardias: una Línea de 100 medía 250 bloques.
  - Una formación era un teletransporte que se deshacía.
- **Principio:**
  - Una forma es una **regla para cualquier N**.
  - Una formación es un **estado deseado** que la IA intenta mantener: cada hueco es
    un POST.
  - El hueco 0 siempre es el líder/frente, lo que permite asignar huecos a mano en el
    futuro.
- **Qué cambió:**
  - Línea: filas de al menos 16 de ancho, con proporción ~3:1.
  - Columna: agrega más columnas lado a lado a medida que crece.
  - V: más de 15 guardias forman una cuña llena.
  - Círculo: anillos concéntricos.
  - Nueva orden **Avanzar**: toda la formación avanza manteniendo cada hueco (desde 3H:
    16 bloques, o hasta el punto marcado mirando hacia él).
  - La geometría está probada con 0 a 1000 guardias.
- **Queda para después:**
  - Una "formación" como objeto propio (hoy cada guardia recuerda la formación
    completa).
  - Asignación manual de huecos (Bastón de Mando).
  - Avanzar por terreno muy irregular: el hueco solo se ajusta ±3 bloques en altura.

### D4. Monturas: un guardia por montura, nunca atacar a los nuestros (3F–3G)
- **Qué estaba mal:**
  - El datapack puede montar dos guardias en un caballo.
  - Implacable podía atacar el caballo de un compañero o del comandante.
- **Principio:**
  - La montura hereda la intención del guardia; no tiene IA propia de Rally.
  - Todo lo que carga a alguien de nuestro bando es de nuestro bando.
- **Qué cambió:**
  - Si otro guardia comparte la montura, se baja.
  - `wouldAccept` excluye la montura propia y cualquier entidad que cargue al
    comandante o a sus guardias.
- **Queda para después:**
  - Monturas voladoras (hipogrifo), en pausa hasta probar los caballos.
  - Candicorn y Komodo deberían funcionar ya con el conductor genérico (cualquier
    `PathfinderMob`), pero falta confirmarlo en el juego.

### D5. Robustez frente a otros mods (3G)
- **Qué estaba mal:** un error en la entidad o el ítem de otro mod dentro de nuestro
  escaneo por segundo podía tumbar el tick del mundo.
- **Principio:** Rally nunca debe ser lo que rompe el servidor.
- **Qué cambió:** `SafeTick` aísla cada guardia en cada bucle: salta ese guardia y
  registra el error una sola vez. Las carreras Kamikaze se descartan ante un error.
- **Queda para después:** aplicar la misma regla a cualquier bucle nuevo.

### D6. Escuadras como unidad de gestión (3G)
- **Qué estaba mal:** el Ledger solo permitía micro-gestión guardia por guardia.
- **Principio:** selección → grupo → orden → ejecución. Las acciones de grupo se
  hacen *por nombre* en el servidor, así incluyen a los miembros lejanos.
- **Qué cambió:** pestaña **Escuadras** con Elegir, Renombrar y Disolver; renombrar
  hacia un nombre que ya existe se rechaza en vez de fusionar escuadras.
- **Queda para después:** aplicar una postura a una escuadra entera, incluidos los
  miembros descargados (el registro por UUID ya lo permite), y planes guardados por
  escuadra.

### D7. Mando sin el ítem: teclas contextuales (3H)
- **Qué estaba mal:** Minecraft no es por turnos. Abrir el Libro en plena batalla para
  elegir un objetivo o un guardia era demasiado lento.
- **Principio:**
  - "Apuntar y ordenar", como en cualquier RTS. Un verbo por tecla que cambia según lo
    que miras.
  - Las teclas son un control remoto de órdenes que ya existen; no traen lógica nueva.
- **Qué cambió:** `client/CommanderKeys` + `order/CommanderHotkeys`.
  - **G** (Ordenar), según lo que miras:
    - enemigo → Atacar;
    - cualquier otro ser vivo → Defender;
    - tu guardia → seleccionarlo solo a él;
    - bloque → ir allí y mantenerlo, en su última formación;
    - cielo → Seguir.
  - **Agachado + G:** sobre tu guardia, lo añade o quita de la selección; sobre una
    montura, monta o desmonta a un guardia; sobre un bloque, lo marca; al aire, borra la
    selección.
  - **H:** alto el fuego / fuego libre. **Agachado + H:** Retirada.
  - **J:** abre el Libro.
  - Quién obedece: la selección; si no hay, los que te siguen a menos de 64 bloques.
    Nunca "todos": las aldeas lejanas no se tocan.
  - El rayo se calcula en el servidor; el cliente solo dice "tecla X, agachado sí/no".
- **Queda para después:** hacer las teclas reasignables por escuadra (1–9 = escuadra N).

### D8. Una selección compartida (3H)
- **Qué estaba mal:** la selección solo vivía en la pantalla del Libro y se perdía al
  cerrarla.
- **Principio:** una sola selección por comandante, en el servidor. Libro, teclas y
  marcas visuales usan la misma.
- **Qué cambió:**
  - `CommanderSelection`, en memoria y por UUID.
  - El Libro la recibe al abrirse y la envía cuando cambia.
  - Chispas verdes sobre cada seleccionado, visibles solo para su comandante (como
    máximo 64 guardias, cada 10 ticks).
- **Queda para después:** nada.

### D9. Defender = un puesto que se mueve (3H)
- **Principio:** proteger a alguien no es un modo nuevo. Es un POST que se mueve con el
  protegido (`GuardModes.postKeepingWard`), más una amenaza con prioridad en la puerta
  de combate: quien lo hirió en los últimos 5 s, o el mob más cercano que lo persigue.
- **Reglas:**
  - Cualquier otra orden de modo (`free`, `follow`, `post` o `postAndMove`) borra el
    protegido.
  - Con alto el fuego sigue escoltando, pero no dispara.
  - Si el protegido muere o desaparece, se borra solo.
- **Queda para después:** mostrar el protegido en el mundo (una línea o una marca).

### D10. Subgrupos: "Escuadra/Unidad" (3H)
- **Principio:**
  - Dos niveles y no más.
  - Una unidad es una etiqueta dentro de la escuadra, no una escuadra nueva, así que
    "seleccionar Pueblo1" incluye a todas sus unidades.
  - El color es el de la escuadra.
- **Qué cambió:**
  - `SquadRegistry.units` (se guarda como "Units"), `pathOf`, `membersOf`.
  - Asignar `A/B` crea o reutiliza la unidad.
  - Renombrar o disolver una unidad no toca la escuadra.
  - Pestaña Escuadras con unidades sangradas y el botón **Ver ›**, que abre la lista
    filtrada.
- **Queda para después:** órdenes y posturas por nombre de unidad para miembros
  descargados.

### D11. Identidad y bajas (3H)
- **Qué estaba mal:**
  - Un guardia convertido en aldeano zombi (Guard Villagers retorna antes del evento de
    muerte de NeoForge) o eliminado por otro mod dejaba una fila "ausente" para siempre.
  - No se sabía cuántos guardias quedaban.
- **Qué cambió:**
  - `EntityLeaveLevelEvent` con motivo destructivo (KILLED o DISCARDED) limpia todos los
    registros del guardia y avisa "se perdió".
  - Toda baja añade: "↳ Escuadra › Unidad: quedan N · ejército: M".
  - Botón **Olvidar** (dos clics) para las filas ausentes.
  - ✎ renombra a un guardia.
  - Ficha completa al pasar el ratón por su fila.
- **Queda para después:** nada.

### D12. Guardias conscientes del arma (3H)
- **Principio:** el rol sale de lo que lleva en la mano (§4.2).
- **Qué cambió:** `compat/ScorchedGunsCompat` añade por reflexión el `GunAttackGoal` de
  Scorched Guns (prioridad 2, por encima de los objetivos cuerpo a cuerpo y de arco de
  Guard Villagers, que tienen prioridad 3), solo si `scguns` está cargado. Sin arma en la
  mano, ese objetivo nunca arranca.
  - Todo lo de Rally sigue valiendo porque funciona a través del objetivo del guardia.
  - `Mob.goalSelector` es público en NeoForge gracias a su access transformer; es la
    misma vía que usa Guard Villagers con el mosquete.
- **Queda para después:** bombardero, mago y artillero (ver `docs/BACKLOG.md`).

### D13. Reformar incluye a los recién llegados (3H)
- **Decisión:** nada entra en formación por sí solo (los francotiradores plantados
  siguen plantados). **Reformar** sobre una selección donde algunos no tienen hueco
  vuelve a formar a todos en la formación que recuerdan los demás.

### D14. Alto el fuego es absoluto (3H.1)
- **Qué estaba mal:** G sobre un enemigo levantaba el alto el fuego ("se pisa la cola").
  Contradecía la regla 4.
- **Principio:** solo H / Fuego libre levantan el alto el fuego. Atacar bajo alto el
  fuego **designa** el objetivo ("objetivo adquirido, esperen mi orden").
- **Qué cambió:** `AttackOrder` ya no toca `holdFire`; avisa cuántos quedaron esperando.

### D15. Un trabajo a la vez: Atacar o Defender (3H.1)
- **Principio:**
  - Atacar borra el protegido y Defender borra el objetivo.
  - Defender a un escolta es defender a quien él escolta (se sigue la cadena hasta la
    raíz).
  - Nadie se defiende a sí mismo.
  - Si A escolta a B y le ordenas a B escoltar a A, gana la última orden: A deja de
    escoltar.
  - `tendWard` también rompe cualquier ciclo que aparezca.

### D16. Cuarteles y retirada (3H.1, decisión del usuario)
- **Principio:**
  - Cada escuadra puede tener un **cuartel** = un estandarte reclamado.
  - Retirada resuelve, por guardia: punto temporal (el de esta pelea) > cuartel de su
    escuadra > seguir al comandante.
  - Un estandarte destruido (comprobado con el chunk cargado) es un cuartel perdido.
- **Qué cambió:** `SquadRegistry.Squad.hq` (se guarda), `order/RetreatPlanner`, y
  agachado + G sobre un estandarte. Agachado + G sobre el mismo punto marcado lo quita.

### D17. Reclutas a la formación (3H.1, decisión del usuario)
- **Principio:**
  - Asignar a un grupo formado = ocupar un lugar en su formación: la que recuerda la
    mayoría de sus miembros cargados.
  - Los plantados en otro sitio no se tocan.
  - Más allá de 128 bloques, entran con Reformar.
- **Qué cambió:** `order/FormationJoin`, llamado desde Asignar.

### D18. Tope de combate (3H.1)
- **Qué estaba mal:** la alarma de Guard Villagers (`setAlertOthers`) y Atacar mandaban a
  todos contra un solo objetivo: amontonados y fáciles de convertir.
- **Principio:**
  - Solo entran cuerpo a cuerpo los que caben alrededor del objetivo (3–8, según su
    ancho).
  - El resto hace pantalla (si se le ordenó ese objetivo) o elige otro enemigo libre.
  - Tiradores y defensa propia no tienen tope.
- **Qué cambió:** `order/Engagement`. La tabla se reconstruye una vez por segundo, en el
  mismo pase del bucle de posturas; la puerta solo la lee.

### D19. Escala (3H.1)
- **Principio:** nada por guardia y por tick; nada que pueda exceder el tamaño de un
  paquete.
- **Qué cambió:**
  - El bucle de posturas reparte su trabajo en 20 ticks (un guardia sigue revisándose
    una vez por segundo, sin el pico de lag de cada segundo).
  - Índice guardia → escuadra.
  - El Libro lista como máximo 2000 filas (seleccionados y cercanos primero); los
    totales de escuadra los calcula el servidor sobre todo el ejército.
  - Las listas que manda el cliente tienen tope de 4096.
- **Límite real:** la IA propia de Guard Villagers (búsqueda de caminos, objetivos) por
  cada guardia **cargado**. Los descargados no cuestan nada (registros por UUID).

### D20. Munición finita (3H.1)
- **Principio:** cada recarga de la IA de Scorched Guns se paga con munición real de la
  mano secundaria (1 ítem cada 4 balas). Los guardias inactivos se reabastecen de
  contenedores a 4 bloques; el cuartel con armería es donde se recarga.
- **Qué cambió:** `compat/GunAmmo` (envoltorio `Rationed` alrededor de `GunAttackGoal`).
- **Queda para después:** la mochila (arma secundaria, pilas grandes); ver
  `docs/VETERANIA.md` §5.

## 3. Matriz de compatibilidad (estado tras 3H.1)

| Combinación | Comportamiento definido |
|---|---|
| Stop/Patrullar × Puesto | Son **lo mismo**: un único flag nativo |
| Puesto × Rally | Modos excluyentes, gana la última orden. El Pergamino no reúne a los apostados (decisión del usuario). Terminar el Rally solo aposta a los que aún lo siguen |
| Postura × Rally / Puesto / Seguir | Ortogonal: la postura solo decide peleas. En un puesto se pelea dentro de la correa de 16 bloques |
| Alto el fuego × todo | Gana siempre; solo Fuego libre o Atacar lo levantan |
| Órdenes explícitas × Postura | La orden gana, incluso sobre Pasivo |
| Formación × Rally | Seguir deja el puesto pero recuerda el hueco; Reformar lo devuelve |
| Formación × Retirada | Se reforma en el punto de retirada con la misma forma |
| Formación × Mantener | Se planta donde está; el hueco recordado no cambia (Reformar vuelve a él) |
| Montura × Rally / Seguir | Te sigue montado; más allá de 48 bloques se teletransporta con la montura |
| Montura × Reagrupar / Llamar | Llega con su montura y queda apostado |
| Montura × Retirada / Formación | Cabalga a su hueco; exige 3 bloques libres de alto |
| Montura × combate | Carga al objetivo; el jinete arquero se detiene a unos 12 bloques |
| Kamikaze × todo | Orden explícita: libera al guardia (FREE), ignora la postura y nunca detona cerca de aliados |
| Órdenes × botones nativos de Guard Villagers | Escriben los mismos flags; los conflictos se resuelven en ≤1 s |
| Defender × cualquier orden de modo | La orden gana y borra el protegido |
| Defender × Alto el fuego | Escolta sin disparar |
| Defender × Objetivo designado | La amenaza al protegido va primero; el objetivo, cuando no hay amenaza |
| Teclas × Libro | Misma selección y mismas órdenes; el Libro abierto desactiva las teclas |
| Teclas × aldeas lejanas | Sin selección solo obedecen los que te siguen a menos de 64 bloques |
| Arma de Scorched Guns × todo | Solo cambia cómo pelea; objetivo, correa, alto el fuego y posturas siguen igual |
| Unidad × Escuadra | Seleccionar la escuadra incluye todas sus unidades; disolver una unidad deja a sus guardias en la escuadra |
| Reformar × recién llegados | Desde 3H.1 entran solos al asignarlos (128 bloques); Reformar incluye a los demás |
| Alto el fuego × Atacar | Atacar designa sin disparar; H libera |
| Atacar × Defender | Excluyentes: la última orden gana |
| Defender × Defender (cadenas y ciclos) | Se defiende la raíz; los ciclos se rompen |
| Retirada × cuartel / punto | Punto temporal > cuartel de la escuadra > seguirme |
| Muerte del comandante × seguidores | Se plantan donde cayó y van contra el asesino (48 bloques) |
| Tope de combate × objetivo designado | Los que no caben hacen pantalla a 4,5 bloques y entran cuando hay lugar |
| Tope de combate × defensa propia / tiradores | Sin tope |
| Montura de un guardia propio × comandante | No le puedes hacer daño |
| Formación × comandante volando | El ancla baja al suelo |
| Arma de fuego × munición | Recarga con munición de la mano secundaria; sin munición, cuerpo a cuerpo |

## 4. Principios para crecer sin romper

1. **Primitivas simples, interacciones excelentes.** Antes de agregar algo nuevo,
   preguntar qué sistemas existentes toca y verificarlo contra la matriz.
2. **Un rol = equipo + orden, no una entidad nueva ni un flag permanente.**
   - Kamikaze = TNT en la mano secundaria + la orden Kamikaze.
   - Un futuro dinamitero = arrojable en la mano secundaria + una orden de lanzar.
   - Un artillero = apostado junto a una montura de CBC.
   - Cualquier guardia puede tener cualquier rol según lo que lleve.
3. **Los ítems propios del mod son "controles remotos"** de las mismas órdenes y
   marcas: Libro, Pergamino, y el futuro Bastón de Mando o cuerno de señales. Nunca
   traen lógica propia paralela. `CommanderMarks` ya es el "puntero" que un Bastón de
   Mando reutilizaría.
4. **Integraciones con otros mods:**
   - Opcionales y aisladas (`compat/<mod>`), cargadas solo si el mod está presente.
   - Siempre que se pueda, se interactúa a través de abstracciones de vanilla o
     NeoForge (`PathfinderMob`, `TntBlock`, `ProjectileWeaponItem`, `onCaughtFire`),
     no de clases del otro mod.
   - Así un mod nuevo que respete esas abstracciones funciona sin código nuevo.
5. **Mixins:** solo cuando ninguna API pública sirve, estrechos, solo sobre guardias
   con dueño y documentados. Hoy hay uno: Héroe de la Aldea.
6. **Datos:** registros por UUID con carga tolerante a errores (un valor desconocido
   cae a un valor por defecto), e IDs en texto en la red.
7. **Escala:** nada se diseña para "8 guardias". Formas, selección y órdenes funcionan
   igual con 5 que con 1000; los escaneos son por radio (AABB) o por intervalo, nunca
   por guardia × mundo.

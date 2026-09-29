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
│ DIRECTIVA       alto el fuego, objetivo  ← DirectiveRegistry            │
│ PERTENENCIA     escuadra                 ← SquadRegistry                │
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
   **alto el fuego > orden explícita del comandante > correa del puesto > objetivo
   designado > postura**.
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
| Retirada | "rompe el combate y repliégate al punto marcado (o a mí)" | POST en el punto, en formación |
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
  - Nueva orden **Avanzar**: toda la formación avanza 8 bloques manteniendo cada
    hueco.
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

## 3. Matriz de compatibilidad (estado tras 3G)

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

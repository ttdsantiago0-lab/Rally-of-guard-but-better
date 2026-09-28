# Investigación: artillero de Create Big Cannons, monturas y Kamikaze (2026-09-28)

Informe de investigación **antes de implementar** (pedido del usuario: "primero observa
el código de CBC, al igual que el de las monturas"). Todo lo de abajo se leyó en el
código fuente real. No es memoria ni suposición.

| Mod | Repositorio y rama leídos |
|---|---|
| Create Big Cannons 5.11 (Create 6, NeoForge 1.21.1) | `Cannoneers-of-Create/CreateBigCannons`, rama `create-v6-1.21.1` |
| Ice and Fire Community Edition 2.1.3 (NeoForge 1.21.1) | `IAFEnvoy/IceAndFire-CE`, rama `1.21.1` |
| Lucky TNT Lib (NeoForge) | `SlimingHD/Neo-LuckyTNTLib`, rama `lib` (**es la de 1.20.6**; hay que confirmar contra el jar de 1.21.1) |
| Guard Villagers 1.21.1 | `seymourimadeit/guardvillagers`, rama `1.21.1` |

---

## 1. Artillero de CBC

### Qué hace CBC hoy

- **El autocañón "de manija" solo lo maneja un jugador.**
  - `MountedAutocannonContraption#canBeTurnedByPassenger` devuelve `isHandle && entity instanceof Player`.
  - `PitchOrientedContraptionEntity#getControllingPassenger` solo devuelve un `Player`.
  - Consecuencia: un guardia sentado en el cañón **no puede apuntar ni disparar**. No
    tiene sentido "sentar" a un guardia.
- **La montura de cañón (`CannonMountBlockEntity`) sí se controla desde fuera con API pública.**
  - `setYaw(float)` y `setPitch(float)` son públicos.
  - En su `tick()` el ángulo solo cambia por la velocidad de los ejes cinéticos, que
    vale 0 si no hay ejes conectados. Por lo tanto, un ángulo fijado desde fuera se
    mantiene.
  - La elevación se limita a lo que permite el cañón
    (`maximumElevation` / `maximumDepression`).
  - Requisito: la montura tiene que estar **ensamblada** (`isRunning()`).
- **Disparar:** `getContraption().tryFiringShot()` es público y llama a
  `AbstractMountedCannonContraption#fireShot`, que sirve para cañón grande y para
  autocañón. El autocañón también dispara solo mientras su recámara tenga cadencia >
  0, que se fija con la potencia de redstone (`onRedstoneUpdate` → `setFireRate`).
- **Munición:** la recámara expone un `IItemHandler`. Hoy la cargan jugadores o la
  logística de Create. Rally no la tocaría en la v1; eso encaja con la fase de
  logística.

### Diseño propuesto: "dotación de cañón"

No hace falta un rol nuevo. Es la combinación de piezas que ya existen:

> guardia **apostado** a 3 bloques o menos de una **montura CBC ensamblada** +
> **objetivo designado** (o el que dé su postura) + **alto el fuego / fuego libre**

1. **Módulo opcional `compat/cbc`.** Solo se carga si `createbigcannons` está
   presente, y la dependencia se declara `optional` en `neoforge.mods.toml`. Para
   compilarlo hacen falta los jars de CBC y Create en `libs/`, igual que Guard
   Villagers.
2. **Apuntado.** Cada tick, para cada montura con dotación:
   - el blanco es el objetivo del guardia;
   - se calcula yaw y pitch **anticipando el movimiento**: se apunta a
     `posición + velocidad × tiempo de vuelo`, lo que resuelve el problema del mago
     que vuela o se teletransporta;
   - la montura gira hacia ese ángulo **con velocidad máxima de giro** (unos
     4–6°/tick). No es un aimbot instantáneo: se le puede esquivar.
3. **Disparo.** `tryFiringShot()` con la cadencia del arma, solo si:
   - el error de puntería es menor a unos 2°;
   - **no** hay alto el fuego;
   - la línea de tiro no pasa por un aliado (un raycast simple).
4. **Contra magos de Ars Nouveau: el autocañón.** Proyectil rápido y cadencia alta.
   La artillería pesada no le pega a un blanco volador; sirve contra estructuras y
   ejércitos lentos.

**Riesgos:**
- La API de CBC es interna, no está publicada como API, así que puede romperse en
  actualizaciones.
- Sable y Aeronautics mueven las monturas en sub-niveles; hay que probarlo.

**Sin mixins.**

## 2. Monturas

### Qué hay hoy

- **Guard Villagers ya sabe pelear montado.** `GuardMeleeGoal#mobHitBox` agranda la
  caja de golpe con la del vehículo, y el arco funciona desde la silla.
- **Caballos de vanilla.** `getControllingPassenger` solo devuelve un `Player`. Un
  guardia montado no conduce: el caballo usa su propia IA (pasear, entrar en pánico).
  Por eso existe `GuardMountDisciplineHandler`, que desmonta a los que siguen o
  patrullan.
- **Hipogrifo** (`HippogryphEntity`, un `TamableAnimal` que implementa `IFlyingMount`):
  - `getControllingPassenger` solo devuelve al **jugador dueño**. Un guardia jinete no
    lo controla.
  - Tiene **navegación propia de tierra y de vuelo**: `switchNavigator` alterna entre
    `AdvancedPathNavigate` CLIMBING y FLYING y cambia a `FlyingMoveControl` según
    `isFlying()`. `setFlying(boolean)` es público.
  - `doesWantToLand()` exige `!isVehicle()`. **Con un guardia encima sigue volando**, no
    aterriza solo. Además, solo despega al azar sin pasajeros.
  - Con la orden de sentarse (`getCommand() == 1`) detiene su navegación.
  - Tiene su propio `MeleeAttackGoal` y `OwnerHurtTargetGoal`: puede morder al objetivo
    mientras el guardia pelea desde la silla.

### Diseño propuesto: "conductor de montura" (composición, no IA nueva)

Un solo handler pequeño. Para cada guardia tuyo que vaya montado en un
`PathfinderMob`:

1. **El destino del guardia pasa a la navegación de la montura.** El destino es el de
   la navegación del guardia (`getNavigation().getTargetPos()`): su puesto o hueco de
   formación, seguir al comandante, o su objetivo. Se llama a
   `montura.getNavigation().moveTo(...)` cada ~10 ticks. **Se usa la navegación propia
   de la montura** (de tierra en el caballo, de vuelo en el hipogrifo), sin IA de
   vuelo propia, tal como pediste.
2. **El objetivo del guardia se pasa a la montura** si es un `Mob` que lo acepte (el
   hipogrifo muerde).
3. **Monturas voladoras** (compat opcional con Ice and Fire): si el destino está lejos
   o más alto, `setFlying(true)`.
4. **Formaciones montadas.** Con el conductor, un guardia montado ya no se desmonta
   para Muster o Mantener: la montura va al hueco. Para caballería y hipogrifos, los
   huecos se eligen con 3 bloques libres de alto; eso de paso ataca el problema de
   asfixia bajo techos bajos (§29).

**Orden sugerido:** primero el caballo (vanilla, sin dependencias) y después el
hipogrifo (compat opcional).

**Riesgos:**
- Los objetivos de IA del caballo compiten con el conductor; por eso se refresca
  seguido.
- Hay reportes de problemas de IA del hipogrifo con algunas combinaciones de mods.

**Sin mixins.**

**Ya corregido en esta tanda:** las flechas de tus guardias ya no dañan a una montura
que lleve a un guardia tuyo (el bug anotado en §17.4).

## 3. Kamikaze (implementado)

- **Lucky TNT es compatible sin lista blanca.**
  - Sus bloques son `LTNTBlock extends TntBlock`, y `onCaughtFire` genera su propia
    entidad `PrimedLTNT extends PrimedTnt` con el guardia como dueño.
  - Rally usa ese mismo gancho genérico de NeoForge, así que cualquier TNT que herede
    de `TntBlock` funciona: vanilla, Lucky TNT y casi cualquier mod de TNT.
  - Después de encenderla, Rally le acorta la mecha a 10 ticks con `setFuse`.
- **Dinamita:** la de Lucky TNT es un ítem arrojadizo (`LDynamiteItem`), no un bloque.
  No está soportada en la v1.
- **A confirmar con el jar de Lucky TNT 1.21.1** que `LTNTBlock` sigue heredando de
  `TntBlock`. La rama pública de GitHub es la de 1.20.6.

## Fuentes

- [Create Big Cannons, rama create-v6-1.21.1](https://github.com/Cannoneers-of-Create/CreateBigCannons/tree/create-v6-1.21.1)
- [IceAndFire Community Edition](https://github.com/IAFEnvoy/IceAndFire-CE) ([CurseForge](https://www.curseforge.com/minecraft/mc-mods/iceandfire-ce))
- [Neo-LuckyTNTLib](https://github.com/SlimingHD/Neo-LuckyTNTLib) ([Lucky TNT en CurseForge](https://www.curseforge.com/minecraft/mc-mods/luckytnt))
- [Guard Villagers 1.21.1](https://github.com/seymourimadeit/guardvillagers/tree/1.21.1)

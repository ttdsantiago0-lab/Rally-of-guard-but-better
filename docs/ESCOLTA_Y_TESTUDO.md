# Escolta, testudo, escudo romano y lanza (David, 2026-11-06)

> "Quiero que tengan la habilidad de escoltar a alguien, pero no sé exactamente cómo sería. Sé que quiero que puedan
> hacer una formación romana de esas en que todos avanzan con escudos arriba. Como vamos a tener ítems tipo lanza y
> escudo romano, el escudo debería estar diseñado para que esta formación sea posible; también debería proteger,
> dependiendo de la veteranía, contra aliento de dragón, balas de Scorched Guns, etc."

Este documento separa lo **comprobado** (en código o en las fuentes de NeoForge y de los mods) de lo **propuesto**.
Todo lo propuesto entra por el protocolo del torneo: se mide en la simulación y se queda solo si gana.

## 1. Lo que ya existe (comprobado en el código)
- **DEFEND / "ward"** (fase 3H, `order/DefendOrder`, `GuardStanceHandler#tendWard`): un guardia toma a una entidad como
  protegida (por defecto, a su comandante; con la tecla G, a la entidad que se señale). Se queda a su lado (su puesto
  se mueve con ella) y ataca a quien la hirió en los últimos 5 s o, si no, al mob más cercano que la esté cazando.
  Es una escolta **de uno en uno**: cada guardia se coloca por su cuenta, nadie se pone entre la amenaza y el
  protegido, y nadie se organiza como grupo.
- **Formaciones**: línea, columna, V y círculo, y seguir en formación (`order/FormationFollow`).
- **Escudo arriba bajo fuego** (manual de campo, confirmado): sube el escudo cuando un tirador que lo ve está a
  ≤ 24 bloques, salvo que lo rodeen cuerpo a cuerpo.

## 2. Lo que dicen las fuentes (comprobado)
- **NeoForge 1.21.1, `LivingShieldBlockEvent`**: se dispara en cada golpe, *lleve o no el escudo arriba*. Permite
  decidir si bloquea (`setBlocked`) y **cuánto** daño para (`setBlockedDamage`, entre 0 y todo), además del desgaste
  del escudo. Es la pieza para el testudo y para la protección por veteranía, **sin mixins**.
- **Regla vanilla del escudo**: bloquea si el golpe viene de la mitad de delante (solo cuenta la dirección
  horizontal). Por eso, en Minecraft, "subir el escudo" no protege de lo que cae desde atrás o desde un lado.
- **Guard Villagers** sube cualquier ítem de la mano izquierda que responda a `SHIELD_BLOCK`
  (`canPerformAction(ItemAbilities.SHIELD_BLOCK)`). Un escudo romano nuestro que herede de `ShieldItem` lo usarán
  sus guardias sin tocar nada de Guard Villagers.
- **Ice and Fire 2.0-beta.17** (en el modpack): el aliento es daño `iceandfire:dragon_fire` / `dragon_ice` /
  `dragon_lightning`. **Ninguno** lleva la etiqueta `bypasses_shield` (solo `gorgon` lleva `bypasses_armor`), así que
  la regla vanilla del escudo se les aplica. Leído en su código (`IafDragonDestructionManager`): el aliento y las
  bolas de fuego llaman a `hurt` con el dragón como fuente (un escudo levantado y de cara al dragón para el golpe
  entero) y **después, pase lo que pase**, aplican su efecto: prenden fuego (fuego), congelan (`iceandfire:frozen`,
  hielo) o empujan (rayo). Es decir: el escudo ya para el golpe; lo que mata a un guardia con escudo es **no
  levantarlo** y **la quemadura o la congelación que vienen detrás**.
- **Quién hace subir el escudo**: Guard Villagers y nuestro manual de campo lo suben contra un `RangedAttackMob`.
  Los tiradores de Scorched Guns lo son (Redcoat, Blunderer, Hornlin, los artilleros de asalto…), así que contra
  ellos el escudo **ya sube**. Los dragones de Ice and Fire **no** lo son (tampoco el blaze ni el ghast de vanilla):
  hoy ningún guardia levanta el escudo contra un dragón que escupe fuego.
- **Scorched Guns 1.5** (en el modpack): la bala es daño `scguns:bullet`, sin `bypasses_shield`. Su propia regla de
  "desactivar el escudo" (`ProjectileHelper.handleShieldHit`) **solo mira a jugadores**; contra cualquier otra
  entidad llama al `hurt` normal. O sea: un guardia con el escudo arriba, de cara al tirador, debería parar balas.
  **Falta verlo en la simulación** (Scorched Guns aún no está en el CI: pide NeoForge ≥ 21.1.228).

## 3. Escolta (propuesta)
Tres maneras de escoltar, de la más simple a la más cerrada. Las tres usan al protegido como "puesto móvil", como
hoy hace DEFEND.

| Modo | Qué hacen | Cuándo |
|---|---|---|
| **Anillo** (por defecto) | Forman un anillo de 4–6 bloques alrededor del protegido y lo siguen a su paso. Cada uno vigila su sector; los tiradores atacan primero a lo que va hacia el protegido; nadie persigue a más de ~10 bloques del protegido (correa). | viaje normal |
| **Interponerse** | Con una amenaza a la vista, los escudos se colocan **entre ella y el protegido**, a 2–3 bloques de él, con el escudo arriba hacia la amenaza; los tiradores se quedan del otro lado. | contacto |
| **Testudo** | Bajo fuego de muchos tiradores (o de balas, o aliento), los portadores de escudo cierran una caja de 1 bloque de separación con el protegido **dentro**, y avanzan al paso. | fuego pesado |

El paso de un modo a otro lo decide la misma "foto" de la escuadra que ya usa el manual de campo: tiradores a la vista,
cuántos, si hay cuerpo a cuerpo cerca. **Cómo se mide**: en la simulación, un protegido (un aldeano o un guardia sin
armas) camina de un lado a otro de la arena a través de oleadas. Métricas: si llega vivo, cuánto tardó y cuánta vida
perdió. Se compara DEFEND de hoy, Anillo, Anillo + Interponerse, y con Testudo.

### 3b. Lo que ya está hecho y medido (escolta v1, ronda 17, 2026-11-06)
Hecho en `order/Escort` (interruptores `fieldManual.escortV1` y `escortShieldScreen`): el **anillo** parejo que va
1,5 s por delante del protegido y se recoloca cada vez que su sitio se mueve 1,5 bloques; la **correa** de 10
bloques; **una amenaza por guardia** (con el tope de cuerpo a cuerpo por blanco); el protegido cuenta como **de los
nuestros** para el fuego amigo; y la **pantalla de escudos** contra tiradores (modo "Interponerse"): los escudos se
colocan en arco a 2 bloques del protegido, entre él y los tiradores, mirándolos; si la escolta no tiene tiradores
propios, la mitad de los escudos va a por los del enemigo. Simulación: un aldeano sin IA camina 74 bloques
(≈ 62 s) por delante de 3 olas que van a por él; n = 30 por fila; `tools/torneo-data/t17/`,
`python3 tools/torneo.py escort …`. Llega vivo:

| Olas | Composición | Sin escolta (atacar) | DEFEND de antes | Escolta v1 sin pantalla | Escolta v1 |
|---|---|---|---|---|---|
| 12 mezcladas (zombi, esqueleto, creeper) | línea (4 escudos + 4 arcos) | 17 % | 13–30 % | 63–70 % | **77–81 %** |
| 12 mezcladas | escudos | 0 % | 7–13 % | 20 % | **30–37 %** |
| 12 mezcladas | arqueros | 7–10 % | 63–73 % | 73–90 % | 73–80 % |
| 10 esqueletos | línea | 0 % | 0 % | 7–10 % | **42–52 %** |
| 10 esqueletos | escudos | 0–7 % | 0 % | 7–27 % | **33 %** |
| 10 esqueletos | arqueros | 0 % | 0–3 % | 23–40 % | 40 % |
| 16 zombis | todas | 63–100 % | 100 % | 97–100 % | 100 % |

- **El fallo más grande era nuestro**: contra esqueletos, con el DEFEND de antes, las flechas de nuestros arqueros le
  hacían al aldeano 11–17 de sus 20 de vida. Ahora 0.
- Contra esqueletos sigue siendo difícil (un aldeano sin armadura con 10 esqueletos por ola tirándole): ahí es donde
  entrarían el testudo y el escudo romano.

## 4. Testudo (propuesta)
- **Forma**: un bloque compacto (por ejemplo 3 × 3 con el centro libre para el protegido, o 4 × 2), separación de 1
  bloque; la primera fila mira al frente y las de los lados miran hacia fuera.
- **Escudos**: todos arriba mientras dure; avanzan a paso lento (subir el escudo ya frena el paso en Minecraft).
- **Regla de cobertura** (`LivingShieldBlockEvent`): un guardia **en testudo, con escudo romano arriba y con un
  compañero de testudo pegado** bloquea los **proyectiles** que le lleguen **desde cualquier dirección**: los de arriba
  y los de los lados los cubre el techo y los flancos de la formación. El **cuerpo a cuerpo** sigue la regla vanilla
  (solo de frente): el testudo es para aguantar flechas, balas y aliento, no para trabarse con una horda (ya medimos
  que el escudo siempre arriba contra hordas de zombis es peor).
- **Con escudo normal**: el testudo se puede formar, pero sin la cobertura de 360°; solo la regla vanilla. El escudo
  romano es el que hace posible la formación, como pediste.

## 5. Escudo romano (scutum) y lanza (propuesta de ítems)
**Escudo romano** (`rallyguard:scutum`, hereda de `ShieldItem` para que Guard Villagers, Scorched Guns y todo lo que
mire `SHIELD_BLOCK` lo traten como escudo):
- es el único que da la cobertura de 360° contra proyectiles en testudo;
- más durabilidad que el escudo vanilla;
- **contra aliento y balas**: el escudo (cualquiera) ya para el golpe entero si está arriba y de cara (§2). Lo que
  añade el escudo romano: si paró un aliento, **apaga la quemadura y quita la congelación** que Ice and Fire aplica
  justo después. Así un guardia con escudo romano, bien plantado, aguanta a un dragón joven el tiempo que duren el
  escudo y la formación.
- Arte: necesita textura y un renderizador propio (los escudos se dibujan con un modelo especial). Empezaría con un
  marcador simple y lo cambiaríamos luego.

**Lanza** (y pilum, la lanza arrojadiza romana): ítem de cuerpo a cuerpo con más alcance que la espada (golpea desde
la segunda fila, por encima del hombro del escudo de delante). El pilum se arroja justo antes del choque. Ficha y
medidas cuando lleguemos a ella (el torneo ya tiene previsto "lanza y escudo propios").

## 6. La veteranía contra dragones y balas
La regla del proyecto es que la veteranía, la moral y los vínculos **solo cambian decisiones**, nunca vida, daño ni
velocidad. Al leer el código de Ice and Fire salió que no hace falta romperla: el escudo ya para el aliento y las
balas; lo que falla es **la decisión de levantarlo y de mirar hacia el peligro**. Propuesta:

| Rango | Contra qué levanta el escudo |
|---|---|
| 0 (recluta) | lo de hoy: tiradores (`RangedAttackMob`, incluidos los de Scorched Guns) a ≤ 24 bloques |
| 1–2 | además, los que escupen o lanzan (dragones, blaze, ghast; lista ampliable por etiqueta `rallyguard:shield_threats`) a ≤ 16 bloques, mirando hacia ellos |
| 3–4 | lo mismo a ≤ 32 bloques, y lo mantiene durante todo el aliento aunque el dragón pase por encima |
| 5 | además avisa al grupo: los de su escuadra a ≤ 8 bloques lo levantan con él |

Más el escudo romano, que quita la quemadura y la congelación del aliento que paró (§5). Si aun así quieres que el
rango cambie **cuánto** para el escudo (un número), dímelo: se puede, pero rompe la regla y lo dejaría solo para el
escudo romano.

## 7. Orden de trabajo propuesto
1. Escolta en la simulación: protegido que cruza la arena; medir DEFEND de hoy contra Anillo + Interponerse.
2. Testudo con escudo normal (forma y movimiento) y la regla de cobertura detrás de un interruptor; medir contra
   esqueletos y oleadas.
3. Ítem escudo romano (con marcador de arte) y su protección especial; Scorched Guns en el CI para medir balas.
4. Lanza y pilum.

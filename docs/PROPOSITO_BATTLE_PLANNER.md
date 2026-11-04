# PROPOSITO_BATTLE_PLANNER — para qué existe el Battle Planner (palabras de David, 2026-11-04)

> Este documento fija el **propósito**. No cambia el código por sí mismo: todo lo que se derive de él pasa por los
> criterios de aceptación de abajo y se mide en simulación. Si algo del plan contradice esto, gana esto.

## 1. La idea
Guard Villagers **no es un mod de guardias que aprenden**. Rally quiere lograr mucho **a partir de comportamientos básicos**
bien coordinados. El Battle Planner no es una pizarra para dibujar formaciones bonitas: es la forma de darle a los guardias
**una respuesta ante contingencias**.

El jugador es el **general**. Tiene que **predecir los riesgos** a los que estarán expuestos sus guardias y, de acuerdo con
eso, **darles contexto con instrucciones predictivas**. Si el general acierta, un grupo de guardias bien preparados que ya
estaban esperando **destruye a un jugador por más fuerte que sea**. Si falla (llega en un vehículo bombardeando y nadie lo
previó), **perdió el general**, no el sistema: esa derrota es legítima y es parte del diseño.

> Meta medible: **guardias preparados contra el enemigo previsto ganan; los mismos guardias contra un enemigo no previsto
> rinden como una doctrina normal; instrucciones contradictorias no rompen el juego, solo producen guardias que no hacen lo
> que el jugador quería.**

## 2. Ejemplo canónico (David)
Se avanza por una llanura hacia un objetivo. El jugador sabe que en su servidor hay un mago que **vuela, ataca con proyectiles
y anda en dragón**. Entiende que su debilidad son los hechizos de área de gravedad. Entonces el plan dice, en sustancia:

| Pieza | Qué dice el plan |
|---|---|
| **Disparador** | "Si aparece un lanzador volador / jinete aéreo..." (clase de amenaza: `FLYING_CASTER` + `MOUNTED_AIR`) |
| **Línea de defensa** | una pantalla (SCREEN) que **protege a la artillería pesada** |
| **Respuesta principal** | la artillería pesada / hechizos de área de gravedad atacan al dragón |
| **Oportunidad** | cuando el dragón o el jinete baje al alcance de mele, **ataca primero el guardia con el arma más poderosa** |
| **Todo lo demás** | no se activa. Mientras no llegue esa amenaza, el grupo se comporta con la doctrina normal |

Si en cambio llega **un jugador en un vehículo bombardeando**, ese disparador no existe: el general falló en predecir y los
guardias no estaban preparados para eso. Correcto.

## 3. Cómo se traduce al mod (sin inventar comportamientos nuevos)
Todo el ejemplo es **una serie de comportamientos que ya existen** (roles, formaciones, prioridad de objetivos, ADVANCE/HOLD,
`FOCUS_FIRE`, `TARGET_PRIORITY`, órdenes de unidad) **coordinados con jerarquías claras que no los dejan romperse**. La magia
del mod es esa infraestructura: que **muchos guardias coordinen entre sí**.

Por eso el plan se compone de **Contingencias**:

```
Contingency {
  disparador   : ThreatClass (+ condiciones: distancia, número, estado de moral propia...)
  respuesta    : { reasignar roles de Fuerza, formación, prioridad de objetivos, quién ataca primero,
                   postura de las unidades, instrucción al capitán }
  prioridad    : orden jerárquico para resolver choques con otras contingencias y con la doctrina
  condición de salida : cuándo se desactiva y se vuelve a la doctrina base
}
```

El disparador lo detecta quien ya tiene que detectar en el mod: **los capitanes reconocen amenazas** (D2 de
`DECISIONES_DAVID_2026-10-09.md`) y el RECON las publica (`ThreatMemory`). Las clases de amenaza están en `PERFILES_Y_FIRMAS.md`.

## 4. Reglas de diseño (criterios de aceptación)
1. **Activación condicional.** Una contingencia **no cambia nada** hasta que su disparador se cumple. Se prueba con una
   simulación de control: mismo plan, sin la amenaza -> el resultado debe ser **igual que sin plan** (±ruido).
2. **Jerarquía total, nunca ambigüedad.** Toda orden tiene un lugar en `ORDENES_JERARQUIA.md`. Dos contingencias
   contradictorias se resuelven por prioridad y se **registran en el informe** (razón de decisión), jamás por azar ni por
   cuelgue. Una prueba de simulación con contingencias contradictorias **debe terminar** sin excepciones, sin bucles y sin
   perder el rendimiento (`perfMsPerTick`).
3. **Predicción honesta.** Una amenaza no prevista **no se resuelve sola con magia**: cae en la doctrina base. El sistema no
   adivina por el jugador.
4. **Un plan bien hecho debe valer mucho.** Escenarios de aceptación en tres versiones: *plan acertado*, *sin plan*,
   *plan que prevé otra cosa*. Se exige: acertado > sin plan ≈ plan que prevé otra cosa. (Hoy `scenario-defensa`,
   `planner2-fill`, `planner2-ambush` y `planner2-delay` **dan lo contrario o ruido** en el lote v4: el plan rinde igual o peor
   que no tenerlo. Eso es lo primero que hay que explicar.)
5. **Coordinación como métrica de primera clase.** Ver §6.
6. **Todo lo que se agrega al plan se puede quitar**, y quitarlo devuelve la doctrina base sin residuos de estado.

## 5. Ideas nuevas de David (propuestas, aún sin diseño cerrado)
No cambian el propósito; lo extienden. Se anotan como propuestas para decidir **después** de medir la coordinación actual.

- **Biblioteca de comportamientos y formaciones personalizadas ("playbook").** El jugador puede crear o activar
  comportamientos / formaciones propias que prefiera a las predeterminadas, y **quitarlas y ponerlas a su gusto**. Un
  comportamiento personalizado es una *receta* hecha con piezas ya existentes (rol + formación + postura + prioridad), no
  código nuevo. Se guarda en datos (JSON/NBT), no en clases.
- **Libro del Comandante (Commander's Ledger) personalizado por legión.** Desde el Battle Planner, el jugador crea un
  Ledger propio para **una legión concreta**: contiene *sus* contingencias, *sus* formaciones y *su* doctrina. Dos legiones del
  mismo jugador pueden pensar distinto (una antiaérea, otra anti-asedio). Encaja con "estilos de mando" (`ESTILOS_DE_MANDO.md`):
  el estilo sale del líder; el Ledger de legión es el *guion* que ese líder interpreta.

Preguntas de diseño abiertas (están al final de la respuesta a David, no bloquean):
(a) ¿el Ledger de legión se **imprime** como ítem (se puede perder/robar) o es un dato de la Fuerza?; (b) ¿cuántas contingencias
por legión (límite duro)?; (c) ¿se pueden **compartir** playbooks entre jugadores (servidor)?

## 6. Métricas de coordinación (nuevas, para la simulación)
Se agregan al informe de cada lote (no sustituyen las actuales):
- **Tiempo de respuesta**: ticks desde que el disparador se cumple hasta que ≥ 80 % de los guardias asignados ya cambió de
  rol/postura.
- **Cobertura de roles**: % de roles de la respuesta que quedaron ocupados (si el plan pedía pantalla y artillería, ¿existieron?).
- **Concentración de fuego**: % del daño que cayó sobre el objetivo prioritario del plan.
- **Primer golpe correcto**: ¿quién asestó el primer golpe sobre el objetivo, y era el designado?
- **Bajas evitables**: guardias muertos fuera de su rol asignado.
- **Choques de órdenes resueltos / ping-pong**: ya existe (`no-pingpong-*`); se vuelve criterio de aceptación.
- **Plan acertado vs sin plan vs plan equivocado**: la tasa de victoria de los tres, con ≥ 10 repeticiones.

## 7. Qué cambia en el orden de trabajo (propuesta; decide David)
Hasta ahora el orden era E3B -> E3C (logística) -> E3D -> E4. David pide ahora **concentrarse en que la coordinación entre
guardias sea ideal**. Propuesta: insertar antes de E3C una fase corta **E3-COORD**:
1. Que el plan **sirva** (criterio 4): arreglar o explicar por qué `planner2-*`/`scenario-*` con plan no superan a sin plan.
2. El objeto `Contingency` y su disparador por clase de amenaza (sin interfaz nueva aún: por archivo/comando).
3. El escenario canónico del dragón/mago como prueba de aceptación (con mobs voladores del mod disponible).
4. Métricas de §6 en el informe.
5. Recién entonces: interfaz del playbook y Ledger por legión.

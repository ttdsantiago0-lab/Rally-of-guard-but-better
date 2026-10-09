# LOGISTICA_Y_EQUIPO — el HQ como depósito, el interés por las armas y el campamento de batalla (diseño, 2026-10-09)

Implementa `DECISIONES_DAVID_2026-10-09.md` D6. Es la fase **E3C**. Principio rector de David: *el equipo no se reparte por un menú; los guardias lo recogen, lo dejan y lo escogen ellos, y de ahí sale su historia de maestría.*

## 0. Qué existe hoy (verificado en el código, no supuesto)
- `HqRegistry` (registros de HQ por banderín, permanentes), `HqRelax` (al llegar a su puesto dentro de la zona del HQ el guardia rompe filas, aparca la montura y pasea cada 30–60 s), `HqLogistics` (abre puertas, aparca/recupera monturas).
- `PackRegistry` (mochila de guerra: **6 espacios** por guardia, persistente por UUID) y `GuardPack` (munición, mochilas de otros mods vía `Capabilities.ItemHandler.ITEM`, segunda arma, consumibles, y **reabastecimiento de munición** en un cofre/barril a ≤ 4 bloques).
- `WeaponMastery`/`MasteryEffects`: maestría por arquetipo de arma, ya con efectos.
- **No existe:** recoger objetos del suelo (los `Guard` de Guard Villagers no sobrescriben la recogida; verificado en el bytecode de 2.4.11), dejar botín en cofres, elegir armas de un cofre, ni un HQ temporal.

## 1. Viabilidad (respuesta a "dime qué tan posible es")
| Pieza | Viabilidad | Riesgo principal |
|---|---|---|
| Recoger botín tras un combate | **Alta** | Coste de escanear `ItemEntity`: se hace por unidad, escalonado, radio corto |
| Dejar lo inútil en un cofre del HQ | **Alta** | Perder o duplicar objetos: se resuelve con transferencias atómicas (simular y luego ejecutar) |
| Coger del cofre lo que "le interesa" | **Alta** (la parte difícil es el *criterio*, no la técnica) | Que vacíen el cofre o cambien de arma sin parar: histéresis, enfriamiento y límite por visita |
| Interés → práctica → maestría (bucle emergente) | **Media-alta** | Que se vuelva monótono (todos eligen la mejor arma): afinidad por personalidad y novedad |
| HQ de batalla temporal ligado al plan | **Alta** | Dejar registros huérfanos al cancelar el plan: ciclo de vida atado al plan |
| Reabastecimiento desde cofres en burros/elefantes | **Media** | Depende de que el animal exponga inventario (capacidad `ItemHandler.ENTITY`); se verifica en compilación y, si no está, solo cofres y se informa |

Ninguna pieza necesita otro mod ni un jar que Claude Code no vea: todo usa API de Minecraft/NeoForge y los datos van en JSON.

## 2. El bucle (la idea central)
```
combate ──► botín recogido ──► llega al HQ (Relajado) ──► VISITA AL DEPÓSITO
   ▲                                                         │ deja lo que no le sirve
   │                                                         │ coge lo que le interesa (1 arma + munición/comida)
   └── lo usa en combate ◄── práctica/maestría ◄── el equipo cambia ◄┘
```
Cada vuelta del bucle genera historia: "Marta cogió el arco largo que dejó Pedro, y tras tres batallas ya es Hábil con él".

## 3. Recoger botín (en campaña)
- **Quién:** guardias con espacio en la mochila, **fuera de combate**, sin orden de movimiento activa, con la unidad en estado "calmado" (no huyendo).
- **Qué:** `ItemEntity` a ≤ `loot.radius` (defecto 6) **sin `thrower`/propietario** (los objetos tirados por jugadores nunca se tocan), que pasen el **filtro de utilidad** (§3.1). Un máximo de `loot.perSecondPerUnit` recogidas por unidad (escalonado) para no hacer un barrido por guardia.
- **Cómo:** el guardia camina al objeto como un destino de baja prioridad y lo mete en su mochila (`PackRegistry`) con `ItemHandler`/`SimpleContainer` (no teletransporta). Sin espacio → deja de recoger.
- **Config:** `logistics.loot.enabled` (defecto `true`), `logistics.loot.radius`, `logistics.loot.maxItemsPerVisit`.

### 3.1 Utilidad de un objeto (una sola función, `ItemUtility`)
Puntúa un `ItemStack` para **este guardia**, por categorías: arma (por arquetipo), armadura (mejora si la slot está vacía o es mejor), munición del arma que lleva, comida, pociones de curación, objetos de valor (esmeralda, oro: se llevan al depósito por el jugador), resto = **basura** (nunca se recoge). Es la misma función para recoger, dejar y coger, así no hay contradicciones.

## 4. La visita al depósito (en el HQ)
Se dispara **una vez** al pasar a Relajado (con enfriamiento por guardia: `logistics.visitCooldown`, defecto 5 min de juego), nunca en combate ni con orden activa.

1. **Descubrir depósitos:** contenedores (`Container`/`ItemHandler` de bloque: cofre, barril, cofres de mods) dentro de `hq.radius`, cacheados por HQ y reescaneados cada 30 s o cuando se rompe/coloca un bloque cercano. Máximo `logistics.maxDepots` (defecto 8) por HQ. Convención sin botones: un contenedor **renombrado con "privado"** (yunque) lo ignoran.
2. **Dejar:** de su mochila deja lo de utilidad baja o duplicada (armas peores que la suya, armaduras que no usa, sobrante de comida), **siempre dejando al menos su kit mínimo** (munición para su arma, 2 comidas, 1 poción de curación si la tiene).
3. **Coger:** como máximo **1 arma y 1 pieza de armadura por visita**, y reponer munición/comida hasta su kit. El criterio del arma es el **Interés** (§5). Cambia de arma solo si `interés(nueva) > interés(actual) + margen` (histéresis `logistics.switchMargin`).
4. **Atomicidad:** cada traspaso es `simulate → comprobar espacio → ejecutar`. Si algo falla, no se mueve nada. **Invariante:** el número de cada objeto en (mochila + manos + armadura + depósitos) no cambia salvo por recogida/uso/consumo. La simulación v4 lo comprueba (`logistics-conservation`).
5. **Telemetría:** `DecisionReason.DEPOT_DEPOSIT / DEPOT_TAKE / LOOT_PICKUP`; resumen por HQ ("Marta dejó espada de hierro, cogió arco largo").

## 5. Interés por las armas (el corazón emergente)
`interés(arma, guardia) = afinidad + apego + calidad + novedad − penalización`
- **Afinidad:** por **rasgo/personalidad** (Tirador→arcos/ballestas, Duelista→espadas, Tanque→hacha y escudo, Jinete→lanza/tridente, Asalto→armas de ráfaga) y por la **genética** que ya existe (MCA) como pequeño sesgo estable.
- **Apego:** a mayor maestría ya acumulada en ese arquetipo, más quiere seguir con él (bucle positivo, **con tope** para que no se vuelvan monotemáticos).
- **Calidad:** daño base/rareza normalizada (un arma de leyenda atrae a todos un poco).
- **Novedad:** una pequeña curiosidad por un arquetipo que nunca ha usado, que **decae** con la veteranía (los reclutas prueban más; los veteranos se quedan con lo suyo).
- **Penalización:** arma en la que el guardia es ya muy bueno pero la nueva es de otro arquetipo (cambiar tiene coste, ligado a las **firmas**: perder la firma es un coste real).
Peso configurable en `config/rallyguard-logistics.json`. Resultado: dos guardias iguales ante el mismo cofre eligen distinto, y el cofre del HQ se convierte en un pequeño **mercado interno**.

## 6. HQ de batalla temporal (campamento)
- **Modelo:** `HqRegistry.Hq` gana `temporary` (booleano), `planId` (UUID del plan del Battle Planner) y `homeHqId` (el HQ original al que pertenece). Se persiste igual que los demás.
- **Creación:** al colocar el HQ de batalla en el planner (un marcador más en el mapa del planner, **sin botones nuevos fuera del planner**) se crea el registro temporal. Mientras el plan está activo, las unidades del plan usan ese HQ como destino de retirada y reabastecimiento (`RetreatPlanner.hqOf` prefiere el temporal; si no existe, el original).
- **Fin:** al terminar/cancelar el plan, el HQ temporal se **elimina**; las unidades vuelven a su HQ original (`homeHqId`); el botín recogido y no depositado se conserva en las mochilas.
- **Zona y relajación:** el campamento es una zona (`hq.radius`) como el HQ normal, pero las unidades **no se relajan** del todo (estado "en campamento": reaprovisionan, no pasean).
- **Reabastecimiento (§7):** el campamento recibe suministros llevados por animales de carga.

## 7. Animales de carga (burros, llamas, elefantes)
- **Idea:** un guardia del tipo logística (o el propio jugador) lleva un animal con inventario; al llegar al HQ de batalla el animal se "aparca" en el campamento y **su inventario cuenta como depósito** de ese campamento (misma lógica de §4, solo munición, comida, pociones y armas de repuesto).
- **Cómo se detecta:** `Capabilities.ItemHandler.ENTITY` (la capacidad de NeoForge); si la entidad no la expone, **no se usa y se anota en el informe** (no se inventa). Burros, mulas y llamas son `AbstractChestedHorse` de vanilla; el elefante y similares de otros mods entran solo si exponen la capacidad.
- **Límite:** `logistics.maxCarriers` por campamento (defecto 6); se miden los guardias que "viven" del animal.
- **Fuera de alcance de E3C (se anota para E4):** rutas de caravana automáticas entre HQ; por ahora el jugador (o una orden de mover) lleva al animal.

## 8. Mochila y límites (reglas fijas)
- **6 espacios** por guardia (ya existe), **máximo 1 mochila grande** (Sophisticated Backpacks) por guardia: la mochila extra es capacidad, no un arma ni armadura.
- **Muñeco de práctica** (objeto del HQ): da XP de maestría baja y **muy limitada** por día de juego a guardias relajados; así hay progreso en tiempos de paz sin explotar la granja. (Va aquí o en E3C-2 según lo que quepa.)
- **Superintendente** (v0, solo informe): el guardia de mayor puntaje de "logística" en el HQ lleva el inventario del campamento (qué armas hay, qué falta); sin acciones propias todavía.

## 9. Rendimiento
- Escaneo de `ItemEntity` por unidad, no por guardia, escalonado, `logistics.scanBudget` por tick.
- Descubrimiento de depósitos cacheado por HQ (no por visita).
- Todo corre en el turno de 1 Hz del guardia existente. Presupuesto: ≤ 0,05 ms/tick añadido con 200 guardias en el simulador.

## 10. Medición (simulación v4+)
- `logistics-conservation`: tras 5 ciclos combate→botín→HQ→combate con 40 guardias, el conteo de objetos cuadra (ni pérdidas ni duplicados).
- `logistics-interest`: 30 guardias con personalidades distintas ante un cofre con 8 armas: reparto de elecciones con entropía > 1,2 (no todos eligen lo mismo) y sin cambios de arma ≥ 3 veces por guardia por día.
- `battle-hq-lifecycle`: crear plan con HQ temporal, cancelar: 0 registros huérfanos, unidades de vuelta a su HQ.
- `carrier-resupply`: un burro con 128 flechas en el campamento recarga a 20 arqueros sin que ninguno se quede a < 20 % de munición.

## 11. Decisiones abiertas para David
1. ¿Los guardias pueden coger **armaduras** del cofre o solo armas, munición y comida? (Recomendado: ambas, de a 1 pieza.)
2. ¿Un contenedor renombrado "privado" para excluirlo es suficiente, o prefieres un bloque marcador (p. ej. un cartel "Depósito")?
3. ¿El jugador puede **pedirle** a un guardia que deje un objeto (arrastrar a la ficha), o queda totalmente emergente?

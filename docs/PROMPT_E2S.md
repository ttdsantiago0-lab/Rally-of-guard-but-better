# PROMPT_E2S para Claude Code — Escala: órdenes a cientos de guardias (ejecutar DESPUÉS de E2A)

Mismas reglas de trabajo que `docs/PROMPT_E2A.md` (lee solo lo necesario, sin web, los jar de otros mods NO están en tu sesión: usa `docs/API_MODS.md`, parse + chequeo de tipos por bloque, un commit y `git push` por bloque, sin preguntas, informe ≤30 líneas). Cupo: todo el necesario.

**Datos de partida** (`docs/FEEDBACK_E1.md` §A): con 200 guardias Rally cuesta 0,26 ms/tick de media (picos: markers 94 ms, mounts 35, combat_gate 28, stance_loop 23, ranged_goal 23, pack 20), pero el servidor se retrasa hasta 7 s (61 avisos "Can't keep up"). El cuello de botella probablemente NO es el código de Rally sino pathfinding vanilla, el cerebro de los aldeanos de MCA y la carga de chunks. **Medir antes de optimizar.** En el perfil de pruebas ya están Lithium, ModernFix, C2ME, FerriteCore y Entity Culling: no dupliques lo que ya hacen.

## S1 · Medir (primero)
- `ScaleProbe` dentro de la simulación v2: escenarios `scale-50/100/200/400` (guardias idénticos en suelo plano) y, para cada uno, las órdenes seguir, formación en V, avanzar, replegarse y atacar a un objetivo lejano; 60 s por orden.
- Métricas: MSPT del servidor (media, p95, p99, máximo), ms/tick de Rally por sistema (`Perf`), solicitudes de pathfinding por segundo y su coste, tiempo hasta que el 90 % esté en su hueco, uso de memoria y asignaciones por tick.
- Añade a `Perf` un contador de **solicitudes de ruta** y de **nodos explorados** (con un `Mob.getNavigation()` envuelto o contador propio en los puntos donde Rally pide rutas) y mide cuánto cuesta el cerebro/sensores de un guardia MCA parado (200 guardias sin órdenes). Documenta en `docs/SIMULACIONES.md` cómo perfilar un lote con Spark (David lo instalará si hace falta; no lo descargues tú).

## S2 · Directivas por escuadra (emisión O(1))
- Una orden es **un registro versionado por escuadra/subunidad**, no un mensaje por guardia. Emitir = escribir un registro y subir la versión. Cada guardia compara la versión de su escuadra en su turno escalonado (`entityId % 20`) y aplica el cambio solo si difiere.
- Un único paquete de red por escuadra al aplicar una orden; el cliente muestra el estado a partir de él.

## S3 · Presupuestos globales por tick
- Presupuestos configurables: `maxPathRequestsPerTick`, `maxPathNodesPerTick`, `maxTargetScansPerTick`. Cola priorizada: combate > cerca del jugador > resto, con envejecimiento (nadie espera para siempre). Si se agota, aplaza al siguiente tick; nunca bloquea el hilo del servidor.
- Reutiliza `SafeTick` y el reparto en 20 ticks existente; no añadas bucles nuevos por entidad.

## S4 · Movimiento barato en grupo
- **Marcha por rastro (breadcrumbs):** en columnas y desplazamientos largos los seguidores recorren el rastro del líder (o del capitán de su subunidad) sin pathfinding individual; solo repathean si se bloquean.
- **Formación en parada:** hueco fijo por guardia con histéresis (no repatear si está dentro de una tolerancia); recalcular huecos solo si el líder se mueve o gira más de un umbral.
- Formaciones por subunidad (E2-A) con huecos precomputados como desplazamientos relativos al líder, reutilizados mientras no cambie la forma.

## S5 · Niveles de detalle (LOD)
- **T0** (cerca del jugador o en combate): IA completa. **T1** (cargado, lejos y sin combate): tick reducido (navegación cada 20–60 ticks, sin sensores caros, objetivos desactivados salvo alerta). **T2** (chunk descargado o muy lejos): estado agregado de escuadra (posición, destino, ETA, estado) sin entidades, con transición sin saltos al volver a cargar. Radios y periodos en configuración. Cualquier guardia que reciba daño o vea un enemigo sube a T0 de inmediato.

## S6 · Objetivos y amistad compartidos
- **Un barrido de objetivos por escuadra por segundo**; los soldados escogen de esa lista cacheada (no cada uno su AABB). `FriendIndex` con hash espacial por chunk para fuego amigo y líneas de tiro; sin recorrer todas las entidades.

## S7 · Coste del cerebro de MCA/aldeanos
- Con los datos de S1, reduce el coste de actividades de aldeano irrelevantes mientras el guardia está en escuadra o en combate (horarios, chat, gossip, sensores) **sin romper su historia de MCA**. Usa un punto de extensión si existe; un mixin pequeño y apuntado está permitido; si no es posible hacerlo con seguridad, anótalo con el motivo.

## S8 · Cliente y red
- Marcadores/HUD (`markers`, pico 94 ms): tope por distancia y por fotograma, agrupar por escuadra (un marcador por escuadra a larga distancia), actualización en lotes.

## S9 · Memoria y GC
- Sin listas/streams/objetos temporales por tick en caminos calientes (pools, arrays reutilizables, `BlockPos.MutableBlockPos`).

## S10 · Aceptación (medida, no supuesta)
- Informa antes/después en `docs/SIMULACIONES.md` con una tabla por tamaño (50/100/200/400) y orden. Metas orientativas, configurables: Rally ≤ 2 ms/tick con 200 guardias en marcha y ≤ 0,5 ms parados; sin retraso de servidor atribuible a Rally; degradación gradual (no un precipicio) a 400. Si una meta no se cumple, documenta la causa medida y el siguiente cuello de botella.

**Corte si el cupo se agota** (sacrificable → intocable): S9 → S8 → S7 → S5 (T2) → S6 → … Intocables: S1, S2, S3, S4.

**Entrega:** un commit y push por bloque, informe ≤30 líneas con qué NO quedó.

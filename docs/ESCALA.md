# Rally of the Guard — Escala (E1, 2026-10-05)

**Objetivo de arquitectura:** 10 000 guardias *registrados*; cientos *distribuidos*; solo
los relevantes corren IA intensiva. No es una promesa de 10 000 mobs activos a 60 FPS.

## Reglas (no romperlas)
1. **Nada es O(n²).** Nada recorre la escuadra entera por cada guardia.
2. **Nada escanea la lista de entidades por tick.** El bucle de posturas recoge los
   guardias propios **una vez por segundo** y reparte el trabajo en 20 ticks (`i % 20`);
   el conductor de monturas reutiliza esa lista (`i % 10`).
3. **Tope por tick:** `scale.maxGuardsProcessedPerTick` (200 por defecto).
4. **Eventos = push:** la puerta de combate, el fuego amigo y la munición reaccionan a
   eventos; las tablas (tope de combate, índice de aliados) se reconstruyen en un pase.
5. **Registros por UUID:** un guardia descargado no cuesta nada.
6. **Paquetes con tope:** el Libro manda lotes de `scale.ledgerPageSize` filas (≤2000) y
   los totales de escuadra se calculan en el servidor sobre todo el ejército.
7. **Interruptores** en `config/rallyguard-common.toml` (`systems.*`) para aislar un
   sistema que dé problemas.

## Cómo medir
- `/rally debug spawn <n> [escuadra]`: crea n guardias tuyos (máximo 2000 por comando).
- Espera 30 s y escribe `/rally debug perf`: muestra el **ms/tick de Rally** por sistema
  (promedio, peor tick y llamadas) desde la última vez. No mide la IA de Guard Villagers
  ni el tick de vanilla (para eso usa spark).

## Resultados de David (rellenar)

| n guardias cargados | Rally ms/tick (promedio) | peor tick | TPS del servidor | notas |
|---|---|---|---|---|
| 100 | | | | |
| 500 | | | | |
| 1000 | | | | |
| 2000 | | | | |

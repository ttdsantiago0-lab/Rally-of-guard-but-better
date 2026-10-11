# FEEDBACK_E3B — primeras pruebas de David en el juego (2026-10-11)

Jar probado: E3B compilado (SHA-256 `cc1951f8…497c`), perfil `veamos (1)`, mundo plano.

## FALLO crítico (ARREGLADO por Cowork, commit `6b194f9`) — StackOverflowError en el tick del servidor
- **Síntoma:** el juego se cerraba varias veces (5 informes `crash-2026-10-11_01.*-server.txt`, todos `StackOverflowError: Exception in server tick loop`), también al lanzar `/rally sim batch quick` y al dar instrucciones a grupos. No era RAM.
- **Causa:** recursión mutua sin corte: `GroupProfile.cached` → `withMorale` → `Cohesion.of` → `Cohesion.compute` → `GroupProfile.cached` → … Ambos cachés escriben el valor **después** de calcular, así que con los dos caducados (cada segundo) la llamada interna volvía a entrar sin fin. Cualquier grupo de ≥ 2 guardias con `morale.enabled` lo disparaba.
- **Arreglo:** `GroupProfile.consistencyNoMorale(key, members, now)` (usa el valor en caché si es reciente; si no, calcula el perfil base `of(members)`, que no llama a morale) y `Cohesion.compute` ya no pasa por `cached`. Sin cambios de comportamiento.
- **Lección para el informe de Claude Code:** toda dependencia entre cachés debe ser **acíclica**; si A llama a B, B no puede llamar a A. Añadir una prueba de simulación `cache-cycle` (grupo de 20, 5 minutos, cachés caducando) y una guarda `ThreadLocal` de reentrada en cualquier caché que calcule llamando a otro sistema.

## Observaciones de interfaz (de David, P2)
1. **Inventario de guerra del guardia:** desde el inventario de guerra no se puede volver al guardia/Libro sin cerrar y reabrir el Ledger → squad → guardia. Pide un botón «Volver» o que Esc regrese a la ficha del guardia.
2. **Conteo de escuadras:** con una fuerza «Ejercito» de 3 unidades (`Ejercito/Infanteria`, `Ejercito/Arqueros`, `Ejercito/Mixto`) la interfaz dice «3 escuadras». Debería decir **1 escuadra** y, al abrirla, **3 subgrupos/unidades** con sus nombres.
3. **Comando de pruebas:** David pide un comando para crear ejércitos de prueba (infantería, arqueros, caballería, mixto; con equipo, rango y escuadrón) en vez de las ~25 líneas de `/item replace`. Sugerido: `/rally debug army <tipo> <n> <rango> [nombre]`.

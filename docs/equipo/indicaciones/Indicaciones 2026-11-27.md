# Indicaciones 2026-11-27

De David (últimas respuestas de diseño, pasadas por Claude). Decisiones: [[D-0018 Facciones vivas]] ·
[[D-0025 Técnica y alcance]] · [[D-0019 Tierra primero, mar y aire después]].

1. **Meta de escala de O9: 6 jugadores × 100 guardias (600) en un servidor normal, con todo el modpack, Alex's Caves
   incluido** (David no lo quita). El tope sin Alex's Caves ya pasa de 300 por bando; la palanca siguiente es que Rally
   frene la IA lejos del jugador sin cambiar quién gana.
2. **Los guardias enemigos usan el mismo motor que los tuyos.** Las guerrillas y facciones avanzadas defienden sus
   bases con una configuración por estructura (quién cuida la frontera con mantener posición, etc.). Cuando el motor
   de grupo funcione, eso se prueba en laboratorio contra distintas amenazas.
3. **El enemigo más peligroso es el bien coordinado** (D-0017): sigue siendo prioridad que la capa de grupo gane en
   igualdad.
4. **Tierra antes que mar y aire.** Lo naval y lo aéreo no entran al laboratorio todavía.
5. **Tormentas** (arena, nieve) afectan a los dos bandos; quedan para cuando haya facciones.
6. **La escala es un presupuesto por servidor, no por jugador** (David): ~600 guardias en total; con 10 jugadores serían
   ~60 cada uno. Pero hay que empujar el límite siempre: cada mejora del motor debe intentar subir esa cifra.
7. **La pregunta que más le importa a David:** ¿cuánto mejora el juego cuando entra Rally y, en concreto, la
   coordinación es real? Cada turno de orquestador deja en la bitácora una línea con la cifra vigente de "Rally completo
   contra Rally solo individual" (mismo número y equipo) y la de "Rally contra Guard Villagers sin Rally sin sesgos".

## 2026-11-28 · Primer momento jugable ([[Momentos jugables]], [[Discusión 02 La salida de cuatro]])

8. **Escenario "salida de cuatro":** 4 guardias (2 escudos, 2 arcos) contra una patrulla de saqueadores, uno empieza
   herido (≈ 25 % de vida), se da RETIRARSE hacia un refugio a ~40 bloques. Medir: % de corridas en que vuelven los 4,
   % en que vuelve el herido, tiempo, y lo mismo **con la capa de grupo apagada**. Si el grupo no protege al herido
   (que vaya en medio y un escudo lo cubra), esa es la primera pieza de coordinación a construir: es justo la que
   David va a mirar en su primera revisión manual.
9. **Corrección de David al escenario (D-0030):** el herido no es un guardia lento. Cuando caería muerto queda
   **fuera de combate** (inmóvil, cualquier golpe lo mata) y hay que llevarlo al refugio para salvarlo. Medir dos
   doctrinas: **utilitaria** (el grupo se retira sin él) y **moralista** (lo cubren y lo llevan). Si la moralista no
   cuesta demasiadas victorias, es la de por defecto. La retirada sigue hacia el refugio (HQ) aunque el jugador se
   quede quieto.

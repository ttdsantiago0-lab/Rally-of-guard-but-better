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
10. **Moral (D-0031), para cuando toque F6:** moral de grupo = confianza del motor en ganar (la misma estimación de la
    retirada por fuerza y de O10) + sucesos − veteranía. Criterios: la regresión no empeora; en peleas sin salida
    sobreviven más; reclutas contra un jefe enorme se quiebran aunque los números alcancen. La orden pesa según
    presencia del jugador y antigüedad de la orden. Coste: medirlo como O8 (es por grupo, debe ser barato).

## 2026-11-28 (tarde)

11. **El reto de coordinación según David:** que el jugador haga jugadas que no necesariamente dan más éxito, pero
    que se ejecuten **muy responsivamente**. Medida de la 2.ª dirección de D-0017: dar una orden (avanzar, mantener,
    retirarse, flanquear) a 16, 64 y 100 guardias en plena pelea y medir el tiempo hasta que la cumplen (mediana y
    p90), el % que la cumple, la cohesión al cumplirla y el coste del tick. Una orden que el motor no habría elegido
    también se ejecuta bien.
12. **Versión jugable en un par de meses:** David probará todo junto en 5 sesiones de 2 horas ([[Momentos jugables]]).
    El orquestador prioriza lo que esas sesiones necesitan: la salida de cuatro, la retirada con tecla y cuerno, el
    caído, la crónica, y la responsividad de las órdenes.


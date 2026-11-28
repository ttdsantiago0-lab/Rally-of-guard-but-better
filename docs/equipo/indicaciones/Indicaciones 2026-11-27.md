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

## 2026-11-29 · Laboratorios nuevos y metas del motor (David + Claude)

13. **Laboratorio de arsenal (O11, propuesto):** probar las armas de los mods con efectos especiales: clic secundario de
    Scorched Guns, armas de Ice and Fire con habilidades (dragonsteel, armas de dragón, etc.), y las de Cataclysm,
    Alex's Mobs y Alex's Caves que los guardias puedan usar. No exhaustivo: por cada arma, ¿la usa bien un guardia?,
    ¿su efecto especial se activa?, ¿cambia qué táctica conviene? Resultado: una tabla en `docs/MANUAL_DE_CAMPO.md`.
14. **Laboratorio de mazmorras (O12, propuesto):** grupos coordinados contra estructuras de mods: castillos y
    fortalezas de Ice and Fire (no muertos), cuevas de Alex's Caves, castillos de When Dungeons Arise. En la sim se
    colocan con `/place structure`. Medida: % que la limpia, bajas, tiempo; con y sin la capa de grupo.
15. **Prioridad cercana:** soporte en el laboratorio (curar o proteger al caído: es la pieza de la salida de cuatro) y
    la responsividad de órdenes (indicación 11).
16. **Metas del motor en tres niveles** (Claude, aprobado como guía): el orquestador las usa para decidir qué sigue.
    - **Mínimo (cumplido):** Rally gana a GV sin sesgos; coordinación +20 pp a 32 o más; 600 guardias con modpack.
    - **Bueno:** la coordinación paga desde 8 guardias y en peleas mixtas; una maniobra (pinza, emboscada o ciclo
      mongol) gana en su contexto a Rally solo individual; una orden a 100 guardias se cumple con p90 ≤ 3 s; 1000
      guardias con modpack bajo p99 50 ms; mazmorras limpiadas con menos bajas; doctrina por enemigo funcionando.
    - **Muy ambicioso:** 8 coordinados empatan con 16 solo individuales de igual calidad (2×); ante un mob nunca visto,
      ≥ 80 % del rendimiento por analogía; 2000 guardias en un servidor con IA por distancia; las batallas fuera de vista
      aciertan los vivos con ±10 %.
17. **El laboratorio sigue meses después de la primera versión jugable:** el motor no se congela al empezar los roles;
    Fable se reserva para lo técnicamente más difícil (diseño de la capa de grupo, lectura de código, arquitectura).
18. Sin prioridad: guardias disparando desde balcones en la aldea.
19. **Escudos de energía de Create (`shieldgrid-1.1.6.jar` en `ci/modpack.json`), importante para David:** adoptarlo.
    Primero entender cómo se usa y medir su efectividad contra las amenazas del modpack (flechas, balas de Scorched Guns,
    TNT, fuego de dragón, proyectiles de jefes). Después, ver si vale la pena un guardia detrás del escudo (o un rol que
    lo opere y lo mantenga). Encaja con el laboratorio de arsenal (O11) y con la artillería de TNT.


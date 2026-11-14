# Laboratorio de estrategias reales

2026-11-13 · Pedido de David: evaluar estrategias militares reales y ver si aportan algo al mod. Cada estrategia se
traduce a lo que Minecraft permite, se convierte en una ficha medible (n = 30, contra la base) y recibe un veredicto.
Las que necesitan acciones que los guardias no saben hacer quedan anotadas como "falta acción" para David.

## Qué cambia Minecraft respecto a la guerra real (medido o leído en el código)

- **Los mobs no tienen frente ni moral**: flanquear no "rompe" una línea; lo que se explota es a quién persiguen,
  la invulnerabilidad de medio segundo, el alcance de seguimiento, el terreno y la comida.
- **El primero que entra se queda con el mob** (ronda 47): el cebo funciona, el "ping-pong" por golpes no.
- **Contra un blanco solo caben unos 2 golpes completos por segundo**; los jefes de Cataclysm además tienen tope de
  daño por segundo y caída por distancia del tirador.
- **El terreno se puede construir y romper**, pero hoy los guardias no ponen ni quitan bloques.

## Catálogo

| # | Estrategia real | En Minecraft | Ficha | Estado |
|---|---|---|---|---|
| E1 | Arqueros montados partos y mongoles (disparar y alejarse) | jinete arquero que mantiene distancia y se aleja del cuerpo a cuerpo | F2, rondas 46 y 49 | **Confirmada, por defecto** (`riderKite`): 32 vindicadores 0 → 87 % |
| E2 | Muro de escudos, falange, testudo | escudos delante, arcos detrás; tortuga | rondas 17–19, 36 | Medida: tortuga útil en casos concretos; contra armas de fuego no aporta |
| E3 | Artillería y fuego de supresión (frenar el avance antes del choque) | arcos con Retroceso II que empujan hacia atrás al cuerpo a cuerpo | F11a, ronda 54 | En prueba |
| E4 | Pinza o envolvimiento (Cannas) | 16 guardias en 4 unidades; instrucción FLANQUEAR frente a mantener la línea | F11b, ronda 54 | En prueba |
| E5 | Emboscada y guerrilla | atacar desde la espera (ya existe `redwait` para el bando rojo; falta el azul) | F11c | Pendiente de ficha |
| E6 | Tercios (filas que disparan por encima de las delanteras) | tiro alto sobre la línea propia; altura | F11d | Pendiente de ficha |
| E7 | Ingenieros y asedio (trincheras, muros, puentes, TNT) | guardias que ponen o rompen bloques | — | **Falta acción**: decisión de David (es una capacidad nueva del guardia) |

## Fuente

Resumen de tácticas que David pegó en la conversación del 2026-11-13 (manuales romanos y griegos, caballería
esteparia, armas combinadas, juegos de estrategia). Ninguna fila se da por buena sin su ronda.

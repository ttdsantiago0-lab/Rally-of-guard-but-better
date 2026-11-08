# Sonda de techo: ¿el cuerpo de Guard Villagers da para lo que queremos?

2026-11-08 · Pedido de David: no quedarnos con los comportamientos que a Claude se le ocurran, medir el límite **real**
de los guardias de Guard Villagers (GV) y saber pronto si alcanza o si hay que modificarlos o hacer guardias propios.
Es la primera parte de la etapa 1 de `docs/PLAN_MAESTRO.md` y alimenta la decisión N3/N4 (§6 de ese plan).

## La idea: separar el cuerpo del cerebro

- **Cuerpo** = lo que la entidad puede hacer físicamente: velocidad, giro, frenar y esquivar, cadencia y carga del
  arco, velocidad y dispersión de la flecha, alcance y espera del golpe, saltar, nadar, trepar, pasar puertas y
  escaleras, montar, cuánto tarda en reaccionar.
- **Cerebro** = qué decide hacer con ese cuerpo. Es lo que venimos mejorando (quemarropa, escudo arriba, tortuga…).

Si el cerebro mejora sin parar pero el cuerpo tiene un techo bajo, nunca llegaremos a lo que quieres por mucho que
inventemos. Así que medimos el techo del cuerpo directamente, con tres herramientas.

## A. El guardia con trampa (oráculo)

Un controlador que solo existe en la simulación y que **hace trampa en la información y en las decisiones, nunca en
el cuerpo**:

| Trampa (cerebro perfecto) | Lo que NO cambia (cuerpo de GV) |
|---|---|
| Sabe dónde está cada enemigo y a qué velocidad va, también tras paredes | Velocidad de movimiento y giro |
| Puntería perfecta: anticipa la trayectoria, incluida la caída o el salto del blanco (el doble disparo de los profesionales) | Tiempo de carga del arco y cadencia |
| Elige siempre el mejor blanco | Daño, alcance y espera del golpe |
| Sube el escudo justo cuando viene la flecha | La física de la flecha y su dispersión |
| Mantiene la distancia perfecta (fuera del alcance enemigo, dentro del suyo) | La navegación de Minecraft |
| Come y se retira en el momento justo | La vida y la armadura |

Lo que da es el **máximo que cualquier cerebro podría sacar de ese cuerpo**. Se compara contra GV sin Rally y contra
Rally de hoy, en cada familia de mobs:

| Resultado | Qué significa | Qué hacemos |
|---|---|---|
| Rally ≈ oráculo | Ya estamos cerca del techo del cuerpo | Para mejorar más hay que tocar el cuerpo (N3/N4) |
| Oráculo ≫ Rally | Hay margen de cerebro | Seguimos con comportamientos; quitando trampas una a una vemos **cuál** da la ganancia (puntería, distancia, escudo…) y esa se programa sin trampa |
| El oráculo también pierde | Techo del cuerpo: ningún cerebro gana eso con este cuerpo | Cambiar el cuerpo (atributos, control de movimiento, ataques nuevos) o guardia propio |

## B. Pruebas del cuerpo (lo físico, medido)

Pruebas cortas, una capacidad cada una, comparadas con un jugador: velocidad punta y en zigzag, cuánto tarda en
frenar y cambiar de lado, carga y cadencia del arco, velocidad y dispersión de la flecha, alcance y espera del golpe,
salto y caída, nado, subir escaleras y pasar puertas, montar y desmontar, y **latencia de reacción** (ticks entre ver
algo y actuar). Resultado: una tabla "un jugador puede X, un guardia puede Y". Lo que falte y se pueda arreglar con
atributos o un mixin pequeño es amarillo; lo que necesite otro cuerpo es rojo.

## C. Buscar más allá de lo que se le ocurre a Claude

1. **Búsqueda automática.** Un comportamiento con perillas (distancias, tiempos, cuánto anticipar el tiro, patrón de
   esquiva, a quién atacar primero, cuándo comer) y una búsqueda evolutiva que prueba miles de combinaciones en las
   16 máquinas de GitHub. La máquina propone; nosotros solo leemos e interpretamos a los ganadores, y cada ganador se
   confirma con el protocolo normal (dos lotes de 30, otras semillas). El preset `lab` actual es la semilla de esto.
2. **Catálogo de técnicas de profesionales.** Cada técnica se clasifica: posible con el cuerpo actual, posible
   cambiando el cuerpo, o imposible para un mob. Primeras filas (a comprobar una por una, no son conclusiones):

| Técnica | Lectura inicial |
|---|---|
| Tiro anticipado al blanco que cae o salta (doble disparo) | Cerebro: ya anticipamos con el tiempo de vuelo; falta la velocidad vertical del blanco |
| Disparar mientras se esquiva en círculo | Comprobar si el objetivo de arco de GV ya esquiva y si basta |
| Segundo golpe tras el retroceso del enemigo | Cerebro: predecir dónde cae |
| Golpe crítico saltando | En Minecraft el crítico es solo del jugador: cuerpo (requiere código propio) |
| Combo de retroceso (soltar y volver a correr) | Los mobs no hacen el reinicio de sprint: cuerpo (atributo o código) |
| Hacha que deshabilita el escudo enemigo | En Minecraft un mob con hacha lo hace contra jugadores, no contra otros mobs; comprobar en GV |
| Escudo arriba solo cuando tensan el arco | Cerebro: ya en parte (escudo arriba bajo fuego) |
| Poner bloques de cobertura, subir en pilar, cubo de agua | Acción sobre el mundo: código nuestro (como las misiones del Battle Planner) |
| Concentrar fuego en el más débil o en el que cura | Cerebro |
| Retirarse a comer y rotar | Cerebro (pendiente: rotar de verdad) |
| Aprovechar la altura | Cerebro + lectura de terreno (`TerrainSense`) |
| Perla de Ender o pociones para escapar | Código nuestro |

## Diagnóstico temprano (2–3 semanas)

Primero tres familias que cubren casi todo: **horda cuerpo a cuerpo** (zombis), **tiradores** (esqueletos) y
**voladores** (fantasmas o ghasts). Por cada una:

- **Verde:** el cuerpo da; el margen está en el cerebro.
- **Amarillo:** al cuerpo le falta algo que se arregla con atributos o mixins pequeños (seguimos con GV).
- **Rojo:** el cuerpo no da; se planea N3 (Rally toma el control) o N4 (guardia propio) para esa familia.

Si dos de tres salen rojas, la decisión N3/N4 se adelanta y no esperamos al bestiario completo. Después se repite
con dragones, jefes de Cataclysm y pistoleros de Scorched Guns.

## Coste

El oráculo y las pruebas del cuerpo son código solo de simulación (un objetivo nuevo con etiqueta `oracle:` en los
escenarios, y presets `cuerpo-*`): unas 2–3 sesiones de Claude. La búsqueda automática, otras 2–3. Horas de máquina:
pocas, porque ahora corren 16 a la vez.

## Resultado del diagnóstico temprano (rondas 23–24, 2026-11-08)
La ronda 23 cayó en peleas que Rally ya ganaba (70–97 %; fantasmas 100 %): el techo no se veía. La 24 repitió en puntos
más duros (n = 20 por fila; `t24-techo-*` en `sim-results`). Gana / supervivientes:

| Familia | Punto | Rally de hoy | + cadencia física (1 s) | + apartarse | + puntería perfecta | Todo |
|---|---|---|---|---|---|---|
| Zombis | 8 arqueros contra 60 | 30 % / 18 % | 95 % / 80 % | **100 % / 100 %** | 55 % / 34 % | 100 % / 100 % |
| Zombis | 8 arqueros contra 85 | 0 % / 0 % | 40 % / 19 % | **100 % / 94 %** | 0 % / 0 % | 100 % / 97 % |
| Zombis | línea contra 55 | 5 % / 2 % | 70 % / 42 % | 100 % / 67 % | 10 % / 7 % | **100 % / 99 %** |
| Zombis | línea contra 75 | 0 % / 0 % | 0 % / 0 % | 82 % / 48 % | 0 % / 0 % | **100 % / 95 %** |
| Esqueletos | 8 arqueros contra 68 | 80 % / 46 % | **100 % / 89 %** | 80 % / 41 % | 80 % / 56 % | 80 % / 54 % |
| Esqueletos | 8 arqueros contra 95 | 20 % / 6 % | **70 % / 39 %** | 25 % / 14 % | 20 % / 8 % | 15 % / 6 % |
| Esqueletos | línea contra 150 | 92 % / 84 % | 100 % / 92 % | 95 % / 90 % | 90 % / 83 % | 90 % / 78 % |
| Fantasmas | 8 arqueros contra 35 | 95 % / 76 % | 95 % / 81 % | 54 % / 97 % | 90 % / 70 % | 50 % / 96 % |
| Fantasmas | línea contra 35 | 77 % / 78 % | 82 % / 82 % | 50 % / 93 % | 73 % / 70 % | 51 % / 95 % |

- **Zombis: verde.** El cuerpo da de sobra; faltaba cerebro: apartarse del cuerpo a cuerpo (y la cadencia).
- **Esqueletos: verde.** Lo que falta es cadencia: el arco permite una flecha por segundo y Rally exigía 1,5 s.
- **Voladores: amarillo.** Ninguna trampa mejora claramente a Rally: estamos cerca del techo del cuerpo. Apartarse
  convierte la pelea en un empate sin contacto (los fantasmas no bajan). Rally ya gana el 77–95 % contra 35.
- Elegir "al más débil" (ronda 23) empeora: deja libres a los que están encima.
- **Decisión de la puerta:** ninguna familia roja; seguimos con el cuerpo de Guard Villagers (N2). Lo que se programa
  sin trampa: apartarse del cuerpo a cuerpo (hecho, ronda 27) y la cadencia física del arco (pendiente de David).

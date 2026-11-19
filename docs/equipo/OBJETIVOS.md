# Objetivos del equipo (los cambia David)

2026-11-13 · Borrador de Claude, aprobado por David al encender el equipo (`EQUIPO_ON`).

## KPI abiertos

Cada objetivo se mide con n ≥ 30 por brazo, semillas pareadas, y su cifra es la del mejor brazo confirmado.
"Avance" = mejora mayor que 2,5 errores estándar sobre la mejor cifra anterior, confirmada en un segundo lote.

| Id | Objetivo | Medida | Hoy | Meta |
|---|---|---|---|---|
| O1 | Armas de fuego en abierto (versión dura desde 2026-11-17) | 8 arqueros contra 48 Pax, arena 160, % victorias y % vivos | **cumplido por defecto** (`spreadFire`, F48): 90 / 55 frente a 80 / 40 con SYNC (n = 30; con etiqueta 95 / 61, n = 60); confirmado con 2.ª semilla en F51 (n = 60, regresión en verde). **Cerrado** | ≥ 85 / ≥ 50 |
| O2 | Armas de fuego entre casas (victoria estricta desde 2026-11-19: ningún Pax vivo, el tope de tiempo no cuenta) | línea de 8 contra 16 Pax en la aldea, % victorias estrictas y % vivos | **cumplido por defecto** (`breachDoors` con cierre a 24 bloques, F53 + F57, n = 60, dos semillas): **97 / 70**; sin la regla 77 / 80. La brecha cuesta 1 guardia de 8 con arqueros (2,7 EE) a cambio de +20 a +40 puntos de victoria y la mitad de tiempo; regresión en verde. Siguiente: quién cruza primero la puerta | ≥ 85 / ≥ 70 |
| O3 | Retirada que salva | la **línea cuerpo a cuerpo** de 8 (la que pierde peleando) contra 80 vindicadores, % vivos al final y % dentro del refugio (`refugeBlueIn`). Los 8 devastadores salen de la medida (la línea les gana peleando, F54) | **aprueba en F54** (n = 30): casa con puerta a 16 bloques 5,00 de 8 vivos (62 %) frente a 0,97 peleando (+4,03, 12 EE), 56 % dentro; ningún muerto dentro, 3,0 mueren en el camino. Forma compacta: no ayuda. 2.º lote: F59 | más vivos que pelear y ≥ 50 % en el refugio |
| O4 | Caballería mixta (arena 160) | 4 cuerpo a cuerpo + 4 arcos montados contra 32 Pax, % victorias y % vivos | **cumplido por composición** (4 escudos + 4 arcos): 100 / 87 (n = 60); contra 48: 100 / 81 | ≥ 85 / ≥ 70 |
| O5 | Jefes con calidad | 16 guardias en netherita+ contra Tremorzilla, % victorias | ~78–80 (n ≈ 190, 16 escudos). **Estancado**: el tope de 8 en el cuerpo a cuerpo es el óptimo (curva 4-6-8-12-16 = 8-28-69-57-32 %, F43 + F47). Opción para David: aceptar el techo | ≥ 80 |
| O6 | Ignis sin bajas | 8 guardias en netherita+ contra Ignis, % vivos | **cumplido por defecto** (`kiteBig`, F48): 83 (6,65 de 8) frente a 50 sin la regla (n = 30; con etiqueta 80, n = 60); confirmado con 2.ª semilla en F51 (n = 60: 84 frente a 54). **Cerrado** | ≥ 70 |
| O7 | Laboratorio de estrategias reales | estrategias del catálogo con veredicto | 2 de 7 (estancado) | 7 de 7 |
| O8 | Coste a escala (2026-11-19; medida corregida en el turno 6) | ms por tick con 100–800 guardias (`t96-*`): Rally **propio** (sin anidados) media, p99 y peor tick en régimen sin recolección; y tick del servidor (media, p99, % de ticks > 50 ms) | F58 (n = 5): a 400 media 0,64 mixto / 0,92 arcos (cumple), p99 3,15 / **5,1** (arcos no), peor sin GC 6,3 / 10,1; servidor p99 20 / 34. **A 800:** Rally 2,08 de media, p99 11,25, peor 27,2 (FORCES en 5 de 5); servidor 27,7 de media, p99 51,7, 1,7 % de ticks > 50. Recorte del informe de fuerza: F60 | a 400: Rally propio ≤ 1,0 de media, p99 ≤ 3 y peor tick ≤ 10 ms; servidor p99 ≤ 50. A 800: Rally p99 ≤ 6 y peor tick ≤ 15 ms; servidor p99 ≤ 50 y ≤ 1 % de ticks > 50 |

## KPI duros (si fallan, se para todo)

- R1 La regresión pasa (`tools/regresion.py check`: ninguna fila empeora más que el ruido).
- R2 El build compila.

## Presupuesto (por día, hora UTC)

| Rol | Turnos por día | Modelo |
|---|---|---|
| orquestador | 16 | Fable (variable `MODELO_ORQUESTADOR`) |
| ejecutor | 20 | Sonnet u Opus (variable `MODELO_EJECUTOR`) |
| analista | 24 | Sonnet (variable `MODELO_ANALISTA`) |
| simulaciones | 3000 corridas | GitHub (gratis, repo público) |

Fable comparte el tope semanal del plan y lo gasta más rápido: el orquestador decide en un turno corto y nunca espera.
2026-11-17 (David: "ahora tenemos el plan Max, hay libertad de más turnos de Fable y tokens"): presupuesto 16 / 20 / 24
por día, espera 45 min para el orquestador y 10 para el ejecutor.

**Las metas de los objetivos las gobierna el orquestador** (David, 2026-11-17): "su objetivo es llevar la coordinación
de Rally a su máxima optimización, de modo que el resto de la arquitectura del mod pueda realizarse". El orquestador
puede subir, bajar, cerrar o abrir metas y objetivos en este archivo, siempre anotando en `COLA.md` el porqué y la
cifra que lo justifica. Lo que sigue siendo de David: el presupuesto de esta tabla, las reglas de parada y las
decisiones reservadas (LEEME regla 4).

2026-11-16 (David: "hasta mañana tengo que usar Fable tanto como sea posible"): hasta el 17 a las 12:00 UTC el
presupuesto es mucho mayor, sin espera, y los tres roles corren con Fable (variables del repo). Después vuelve solo
a lo de abajo.
- Temporal hasta 2026-11-21T00:00Z: orquestador 32, ejecutor 40, analista 48, espera 15
(2026-11-17, David: "tenemos un montón de tokens hasta el viernes, dale todo lo que necesites". Caduca solo el viernes
a medianoche UTC. Más turnos no es la meta: cada turno de Fable debe decidir algo que la cola no tenía.)

2026-11-15 (David): Fable vuelve a ser el orquestador (`MODELO_ORQUESTADOR = claude-fable-5-2`) y los turnos se reparten
en el día (la noche del 14 al 15 se gastaron los 6 del orquestador en 3 horas):
- Espera mínima orquestador: 45 min
- Espera mínima ejecutor: 10 min
- Espera mínima analista: 0 min
Si el orquestador no puede correr por la espera, el analista no lo vuelve a llamar en bucle: lo deja para el latido.

2026-11-14 (David: "todavía tengo tokens, quiero progreso"): presupuesto subido a 6 / 10 / 12. Si el uso semanal de Fable
pasa del 40 % antes del jueves, el orquestador baja a 4 turnos y lo anota en el issue de decisiones.

## Cuándo se para y se llama a David

David pidió **una lista de cosas sin avance, no una sola**. Por eso:

1. Un objetivo queda **estancado** tras 3 intentos seguidos (fichas o variantes, cada una medida) sin avance.
   El equipo lo aparta y sigue con los demás.
2. Se abre **un** issue "Decisiones para David — <fecha>" (etiqueta `decision-david`) cuando pasa cualquiera de estas:
   - hay 3 o más objetivos estancados;
   - todos los objetivos que quedan están estancados o esperan una decisión reservada;
   - un KPI duro falla dos turnos seguidos (se para todo: `pausa: true` en `estado.json`);
   - se agotó el presupuesto del día tres días seguidos sin avance en ningún objetivo.
3. El issue trae, por cada objetivo: la meta, lo intentado (fichas y cifras), el mejor resultado, por qué creemos que
   no avanza, y 2–4 opciones concretas para que David elija (incluida "cambiar la meta" o "aceptar el techo").
4. Mientras el issue esté abierto, el equipo sigue solo con lo que no está estancado. Si no queda nada, pone
   `pausa: true` y se detiene. Se reanuda cuando David comenta en el issue o pone `pausa: false`.
5. Una decisión reservada (LEEME regla 4) se anota en la lista del próximo issue; no abre un issue por sí sola salvo
   que bloquee todo.

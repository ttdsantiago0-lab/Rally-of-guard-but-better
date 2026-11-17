# Objetivos del equipo (los cambia David)

2026-11-13 · Borrador de Claude, aprobado por David al encender el equipo (`EQUIPO_ON`).

## KPI abiertos

Cada objetivo se mide con n ≥ 30 por brazo, semillas pareadas, y su cifra es la del mejor brazo confirmado.
"Avance" = mejora mayor que 2,5 errores estándar sobre la mejor cifra anterior, confirmada en un segundo lote.

| Id | Objetivo | Medida | Hoy | Meta |
|---|---|---|---|---|
| O1 | Armas de fuego en abierto (versión dura desde 2026-11-17; la de 32 Pax quedó cumplida en arena 160: 60 de 60) | 8 arqueros contra 48 Pax, arena 160, % victorias y % vivos | 83 / 40 (n = 30) | ≥ 85 / ≥ 50 |
| O2 | Armas de fuego entre casas | línea de 8 contra 16 Pax en la aldea, % victorias con ≥ 70 % vivos | 62 / 82 (sin cumplir ambos a la vez con 32) | ≥ 85 / ≥ 70 |
| O3 | Retirada que salva | contra 80 vindicadores u 8 devastadores (no se ganan), % vivos al final y % en el refugio | sin medir (ronda 52 v2) | más vivos que pelear y ≥ 50 % en el refugio |
| O4 | Caballería mixta (arena 160 desde 2026-11-17) | 4 espadas + 4 arcos montados contra 32 Pax, % victorias y % vivos | 100 / 43 (n = 30) | ≥ 85 / ≥ 70 |
| O5 | Jefes con calidad | 16 guardias en netherita+ contra Tremorzilla, % victorias | 20–50 | ≥ 80 |
| O6 | Ignis sin bajas | 8 guardias en netherita+ contra Ignis, % vivos | 44 | ≥ 70 |
| O7 | Laboratorio de estrategias reales | estrategias del catálogo con veredicto | 1 de 7 | 7 de 7 |

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

# Objetivos del equipo (los cambia David)

2026-11-13 · Borrador de Claude, aprobado por David al encender el equipo (`EQUIPO_ON`).

## KPI abiertos

Cada objetivo se mide con n ≥ 30 por brazo, semillas pareadas, y su cifra es la del mejor brazo confirmado.
"Avance" = mejora mayor que 2,5 errores estándar sobre la mejor cifra anterior, confirmada en un segundo lote.

| Id | Objetivo | Medida | Hoy | Meta |
|---|---|---|---|---|
| O1 | Armas de fuego en abierto | 8 arqueros contra 32 Pax, % victorias | 57 | ≥ 85 |
| O2 | Armas de fuego entre casas | línea de 8 contra 16 Pax en la aldea, % victorias con ≥ 70 % vivos | 62 / 82 (sin cumplir ambos a la vez con 32) | ≥ 85 / ≥ 70 |
| O3 | Retirada que salva | contra 80 vindicadores u 8 devastadores (no se ganan), % vivos al final y % en el refugio | sin medir (ronda 52 v2) | más vivos que pelear y ≥ 50 % en el refugio |
| O4 | Caballería mixta | 4 espadas + 4 arcos montados contra 32 Pax, % victorias | 54 | ≥ 85 |
| O5 | Jefes con calidad | 16 guardias en netherita+ contra Tremorzilla, % victorias | 20–50 | ≥ 80 |
| O6 | Ignis sin bajas | 8 guardias en netherita+ contra Ignis, % vivos | 44 | ≥ 70 |
| O7 | Laboratorio de estrategias reales | estrategias del catálogo con veredicto | 1 de 7 | 7 de 7 |

## KPI duros (si fallan, se para todo)

- R1 La regresión pasa (`tools/regresion.py check`: ninguna fila empeora más que el ruido).
- R2 El build compila.

## Presupuesto (por día, hora UTC)

| Rol | Turnos por día | Modelo |
|---|---|---|
| orquestador | 3 | Fable (variable `MODELO_ORQUESTADOR`) |
| ejecutor | 6 | Sonnet u Opus (variable `MODELO_EJECUTOR`) |
| analista | 8 | Sonnet (variable `MODELO_ANALISTA`) |
| simulaciones | 3000 corridas | GitHub (gratis, repo público) |

Fable comparte el tope semanal del plan y lo gasta más rápido: el orquestador decide en un turno corto y nunca espera.

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

# Cola de fichas del equipo (índice)

<!-- Generado por tools/cola_grafo.py. No edites las tablas a mano: edita la nota de la ficha y corre el script. -->

La cola es un grafo de notas (David, 2026-11-20): **lee este índice y abre solo las notas que te tocan.**

- Fichas: `docs/equipo/fichas/<id>.md` (hipótesis, brazos, estado, veredicto, seguimiento).
- Objetivos: `docs/equipo/objetivos/<O>.md` (cifra, meta y sus fichas; se regeneran).
- Bitácora de turnos por día: `docs/equipo/turnos/Turnos <fecha>.md` (el porqué de cada turno).
- Indicaciones de David: `docs/equipo/indicaciones/` (**el orquestador las lee todas**; son cortas).
- Plantilla de ficha nueva: `docs/equipo/fichas/_plantilla.md`. Después de editar notas: `python3 tools/cola_grafo.py`.

Norma estadística (aprobada por David el 2026-11-11): semillas pareadas entre brazos; métrica principal continua cuando exista; n ≥ 30 por brazo para valores por defecto; n = 5–8 solo para explorar.

## Indicaciones de David

- [[Indicaciones 2026-11-14]] (`docs/equipo/indicaciones/Indicaciones 2026-11-14.md`)
- [[Indicaciones 2026-11-15]] (`docs/equipo/indicaciones/Indicaciones 2026-11-15.md`)
- [[Indicaciones 2026-11-16]] (`docs/equipo/indicaciones/Indicaciones 2026-11-16.md`)
- [[Indicaciones 2026-11-17]] (`docs/equipo/indicaciones/Indicaciones 2026-11-17.md`)
- [[Indicaciones 2026-11-19]] (`docs/equipo/indicaciones/Indicaciones 2026-11-19.md`)
- [[Indicaciones 2026-11-21]] (`docs/equipo/indicaciones/Indicaciones 2026-11-21.md`)
- [[Indicaciones 2026-11-22]] (`docs/equipo/indicaciones/Indicaciones 2026-11-22.md`)

## Vivas (18)

| Ficha | Objetivo | Estado | Fecha | Qué |
|---|---|---|---|---|
| [[F0]] |  | pendiente | 2026-11-13 | Auditoría de lo ya confirmado |
| [[F2b-F2c]] |  | pendiente | 2026-11-13 | Supervivencia del caballo y jinetes con espada: hechos (rondas 50–51, n = 30, 2026-11-12) |
| [[F3]] |  | pendiente | 2026-11-13 | Maniobra títere antes del planificador |
| [[F4]] |  | pendiente | 2026-11-13 | Jefes: ventanas por daño y radio |
| [[F5 v3]] |  | pendiente | 2026-11-14 | La retirada sigue siendo prioridad |
| [[F6]] |  | pendiente | 2026-11-13 | Modelo de decisión retirada + moral |
| [[F7]] |  | pendiente | 2026-11-13 | Valor de la decisión del jugador |
| [[F8]] |  | pendiente | 2026-11-13 | Coste a 1000 guardias |
| [[F9]] |  | pendiente | 2026-11-13 | Pistoleros en terreno cerrado (nueva, 2026-11-11) |
| [[F10]] |  | pendiente | 2026-11-13 | Reconfirmación con n = 30: hechos (ronda 48, semillas 2001) |
| [[F12]] |  | pendiente | 2026-11-14 | Cebo y pinza contra mobs |
| [[F13]] |  | pendiente | 2026-11-14 | Guardias contra guardias: pinza contra carga |
| [[F21]] | O5, O6 | lanzada | 2026-11-16 | Jefes sin moral: ¿qué composición? (sin Java) |
| [[F52]] |  | pendiente | 2026-11-19 | Manual de campo: lo confirmado en F40–F48 (sin Java, sin lote) |
| [[F69]] | O3 | lanzada | 2026-11-22 | Disparador anticipado de retirada por margen de distancia |
| [[F80]] | O9 | lanzada | 2026-11-22 | Barrido del tope técnico: 400 v 400, 600 v 600, 800 v 800 |
| [[F84]] | O7 | lanzada | 2026-11-22 | Foco compartido contra pistoleros (2.º lote) y capa de grupo contra una horda cuerpo a cuerpo de verdad |
| [[F87]] | O4 | pendiente | 2026-11-22 | Caballería con espada en persecución: capacidad y primera cifra |

## Cerradas recientes (20 de 80; todas en las notas de objetivo)

| Ficha | Objetivo | Veredicto |
|---|---|---|
| [[F65]] |  | Veredicto F65 (analista, 2026-11-20; run 44672330392, commit 7c9cc93, seed 9700, n = 30 pareado, 180 corridas, datos en `tools/torneo-data/t103/`): |
| [[F66]] | O2 | Veredicto (analista, 2026-11-20; t104-aldea-muertes, seed 9800, n = 30 por brazo; la fila `control` repetida se excluye): |
| [[F67]] |  | Veredicto F67 (analista, 2026-11-20, lote 20261120-043241, commit e0e1409, n=30 por brazo, seed 9800 pareada): |
| [[F68]] | O2 | (en [[Turnos 2026-11-20]]) Veredictos nuevos: |
| [[F70]] |  | intermedio: manda el margen, con un valor mayor al que supuso el modelo; el reloj solo no manda. |
| [[F71]] | O2 | Veredicto F71 (analista, 2026-11-20; run 44680528058, commit c43127f, n=30 pareado por brazo, seed 10100, 240 corridas + 1 de control): |
| [[F72]] | O8 | Veredicto F72 (analista, 2026-11-20; lote `20261120-200314-t109-escala-conf`, run 44807378539 verde, commit eaa3ffe; copia en `tools/torneo-data/f72/`… |
| [[F72c]] | O8 | Veredicto F72c (analista, 2026-11-20; lote `20261120-215507-t109-escala-conf`, commit 9c1b40a, sin cambios en `src/` desde eaa3ffe; copia en `tools/to… |
| [[F73]] | O2 | 0 filas rojas, 0 sin datos |
| [[F74]] | O8 | APRUEBA |
| [[F75]] | O7 | medida base |
| [[F76]] | O7 | PARCIAL |
| [[F77]] | O7 | confirmado: en gran parte cadencia |
| [[F78]] | O9 | línea base: O9 cumple en la sim, con salvedad. |
| [[F79]] | O7 | (en [[Turnos 2026-11-22]]) +1,63 vivos de 8 contra pistoleros |
| [[F81]] | O7 | (en [[Turnos 2026-11-22]]) [[F81]]: hipótesis NO, pero NO medido con causa = avance de O7 (intentos 0). Cifra de coordinación sola: pistoleros |
| [[F82]] | O2 | regresión verde |
| [[F83]] | O10 | medida base, no llega al margen; va a Fable para la v1. |
| [[F85]] | O7 | (en [[Turnos 2026-11-22]]) NO |
| [[F86]] | O7 | explicado |

## Objetivos

- [[O1]] · Armas de fuego en abierto (versión dura desde 2026-11-17)
- [[O2]] · Armas de fuego entre casas (victoria estricta desde 2026-11-19: ningún Pax vivo,
- [[O3]] · Retirada que salva
- [[O4]] · Caballería mixta (arena 160)
- [[O5]] · Jefes con calidad
- [[O6]] · Ignis sin bajas
- [[O7]] · Laboratorio de estrategias reales; pregunta de fondo (David, 2026-11-21): **poco
- [[O8]] · Coste a escala (**David, 2026-11-21: criterio por media + p99 aceptado; banderas
- [[O9]] · Batalla grande a la vista (David, 2026-11-21)

## Bitácora

[[Turnos 2026-11-16]] · [[Turnos 2026-11-17]] · [[Turnos 2026-11-18]] · [[Turnos 2026-11-19]] · [[Turnos 2026-11-20]] · [[Turnos 2026-11-21]] · [[Turnos 2026-11-22]] (las anteriores en `docs/equipo/turnos/`)

<!-- fin del índice: lo que se escriba debajo de esta línea se migra a notas con tools/cola_grafo.py -->

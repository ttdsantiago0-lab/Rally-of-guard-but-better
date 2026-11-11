# Simulación Rally - lote 20261111-215259-t48-distancia-vindicatorcallwell16-village

Preset `t48-distancia-vindicatorcallwell16-village` · velocidad `sprint` · 330 corridas · 3 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t48-distancia-vindicatorcallwell16-village-linea-sinregla | 20 | 35 % ± 11 | 65 % ± 11 | 1.6 ± 0.5 | 5.5 ± 1.1 | 43 ± 5 | 264 ± 19 | 183 ± 6 | 69 % ± 2 | 9.0 ± 0.4 | 4.9 ± 0.3 | 26 |
| t48-distancia-vindicatorcallwell16-village-arco-sinregla | 9 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 8.8 ± 0.5 | 23 ± 2 | 235 ± 10 | 187 ± 6 | 62 % ± 2 | 13.6 ± 0.2 | 3.2 ± 0.4 | 45 |
| t48-distancia-vindicatorcallwell16-village-lineascutum-sinregla | 19 | 32 % ± 11 | 68 % ± 11 | 1.1 ± 0.4 | 5.9 ± 1.2 | 42 ± 6 | 273 ± 20 | 182 ± 7 | 71 % ± 2 | 9.4 ± 0.4 | 5.2 ± 0.3 | 29 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t48-distancia-vindicatorcallwell16-village-arco-regla semilla 2001: sprint → draw (8/6 vivos), normal → draw (8/6 vivos)
- t48-distancia-vindicatorcallwell16-village-arco-regla semilla 9778: sprint → draw (8/6 vivos), normal → draw (8/6 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 13 de 13 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t48-distancia-vindicatorcallwell16-village-linea-sinregla: enemy:scguns.bullet.annihilated=27 enemy:scguns.bullet.decimated=33 enemy:scguns.bullet.eliminated=25 enemy:scguns.bullet.executed=23 enemy:scguns.bullet.killed=21
- t48-distancia-vindicatorcallwell16-village-arco-sinregla: enemy:scguns.bullet.annihilated=18 enemy:scguns.bullet.decimated=22 enemy:scguns.bullet.eliminated=14 enemy:scguns.bullet.executed=12 enemy:scguns.bullet.killed=6
- t48-distancia-vindicatorcallwell16-village-lineascutum-sinregla: enemy:scguns.bullet.annihilated=22 enemy:scguns.bullet.decimated=24 enemy:scguns.bullet.eliminated=22 enemy:scguns.bullet.executed=31 enemy:scguns.bullet.killed=32

## Motivos de decisión (cambios registrados): ENGAGE 4933, LOW_HEALTH_RETREAT 424, NONE 265, OUT_OF_RANGE 2964, SEEK_ENEMY 187, STUCK 42, TOO_CLOSE 366

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t48-distancia-vindicatorcallwell16-village-arco-regla - 60 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t48-distancia-vindicatorcallwell16-village-arco-sinregla - 45 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t48-distancia-vindicatorcallwell16-village-linea-regla - 60 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t48-distancia-vindicatorcallwell16-village-linea-sinregla - 26 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t48-distancia-vindicatorcallwell16-village-lineascutum-regla - 60 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t48-distancia-vindicatorcallwell16-village-lineascutum-sinregla - 29 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)

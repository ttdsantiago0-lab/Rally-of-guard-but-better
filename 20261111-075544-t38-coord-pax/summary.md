# Simulación Rally - lote 20261111-075544-t38-coord-pax

Preset `t38-coord-pax` · velocidad `sprint` · 49 corridas · 6 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t38-coord-pax16-linea-volea60-base | 8 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 26 ± 0 | 329 ± 12 | 16 ± 4 | 53 % ± 1 | 36.9 ± 0.5 | 13.8 ± 0.2 | 0 |
| t38-coord-pax16-arco-volea60-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 20 ± 1 | 434 ± 3 | 2 ± 2 | 50 % ± 1 | 48.2 ± 0.6 | 6.2 ± 0.3 | 0 |
| t38-coord-pax16-linea-volea60-reparto | 8 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 26 ± 1 | 328 ± 11 | 24 ± 7 | 53 % ± 1 | 37.3 ± 0.5 | 13.4 ± 0.3 | 0 |
| t38-coord-pax16-arco-volea60-reparto | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 20 ± 1 | 434 ± 4 | 3 ± 2 | 50 % ± 1 | 48.9 ± 0.5 | 5.7 ± 0.2 | 0 |
| t38-coord-pax16-linea-volea60-sincro | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 26 ± 1 | 328 ± 13 | 16 ± 5 | 56 % ± 2 | 36.2 ± 0.5 | 13.6 ± 0.4 | 0 |
| t38-coord-pax16-arco-volea60-sincro | 8 | 100 % ± 0 | 0 % ± 0 | 7.5 ± 0.5 | 0.0 ± 0.0 | 25 ± 2 | 426 ± 4 | 18 ± 13 | 54 % ± 1 | 46.6 ± 0.6 | 7.1 ± 1.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t38-coord-pax16-linea-volea60-base semilla 1001: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 48 de 48 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t38-coord-pax16-linea-volea60-base: enemy:scguns.bullet.killed=1
- t38-coord-pax16-linea-volea60-reparto: enemy:scguns.bullet.executed=1
- t38-coord-pax16-arco-volea60-sincro: enemy:scguns.bullet.annihilated=1 enemy:scguns.bullet.decimated=2 enemy:scguns.bullet.eliminated=1

## Motivos de decisión (cambios registrados): ENGAGE 705, LOW_HEALTH_RETREAT 5, NONE 6, OUT_OF_RANGE 767, SEEK_ENEMY 4

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.

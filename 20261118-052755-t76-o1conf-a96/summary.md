# Simulación Rally - lote 20261118-052755-t76-o1conf-a96

Preset `t76-o1conf-a96` · velocidad `sprint` · 61 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t76-a96-pax32 | 30 | 63 % ± 9 | 37 % ± 9 | 2.1 ± 0.4 | 2.8 ± 0.9 | 35 ± 1 | 738 ± 18 | 180 ± 8 | 74 % ± 1 | 39.7 ± 0.2 | 13.8 ± 1.2 | 0 |
| t76-a96-pax32-spread | 30 | 80 % ± 7 | 20 % ± 7 | 3.3 ± 0.4 | 1.5 ± 0.6 | 33 ± 1 | 776 ± 14 | 156 ± 9 | 66 % ± 1 | 40.2 ± 0.4 | 14.1 ± 1.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t76-a96-pax32 semilla 7500: sprint → blue (2/0 vivos), normal → blue (3/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 43 de 43 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t76-a96-pax32: enemy:scguns.bullet.annihilated=39 enemy:scguns.bullet.decimated=38 enemy:scguns.bullet.eliminated=31 enemy:scguns.bullet.executed=31 enemy:scguns.bullet.killed=39
- t76-a96-pax32-spread: enemy:scguns.bullet.annihilated=34 enemy:scguns.bullet.decimated=26 enemy:scguns.bullet.eliminated=35 enemy:scguns.bullet.executed=16 enemy:scguns.bullet.killed=31

## Motivos de decisión (cambios registrados): ENGAGE 1816, LOW_HEALTH_RETREAT 144, NONE 62, OUT_OF_RANGE 1438, SEEK_ENEMY 9

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.

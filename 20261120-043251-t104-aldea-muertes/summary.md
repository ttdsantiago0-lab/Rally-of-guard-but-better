# Simulación Rally - lote 20261120-043251-t104-aldea-muertes

Preset `t104-aldea-muertes` · velocidad `sprint` · 61 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t104-pax16-defecto | 30 | 97 % ± 3 | 3 % ± 3 | 6.0 ± 0.3 | 0.2 ± 0.2 | 52 ± 2 | 356 ± 6 | 137 ± 8 | 56 % ± 1 | 17.5 ± 0.5 | 13.7 ± 0.4 | 0 |
| t104-pax32-defecto | 30 | 67 % ± 9 | 33 % ± 9 | 2.3 ± 0.4 | 2.4 ± 0.8 | 74 ± 3 | 479 ± 15 | 253 ± 10 | 60 % ± 1 | 17.3 ± 0.5 | 12.7 ± 0.6 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t104-pax16-defecto semilla 9800: sprint → blue (4/0 vivos), normal → blue (4/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 49 de 49 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t104-pax16-defecto: enemy:scguns.bullet.annihilated=12 enemy:scguns.bullet.decimated=13 enemy:scguns.bullet.eliminated=9 enemy:scguns.bullet.executed=12 enemy:scguns.bullet.killed=15
- t104-pax32-defecto: enemy:scguns.bullet.annihilated=32 enemy:scguns.bullet.decimated=33 enemy:scguns.bullet.eliminated=38 enemy:scguns.bullet.executed=34 enemy:scguns.bullet.killed=34

## Motivos de decisión (cambios registrados): ENGAGE 1570, LOW_HEALTH_RETREAT 304, NONE 17, OUT_OF_RANGE 569, SEEK_ENEMY 224, STUCK 95, TOO_CLOSE 391, UNIT_BROKEN 2

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.

# Simulación Rally - lote 20261123-071409-t150-municion-altura

Preset `t150-municion-altura` · velocidad `sprint` · 121 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t150-municion-pill48-hill-ammo3-defecto | 30 | 98 % ± 2 | 2 % ± 2 | 7.6 ± 0.1 | 0.0 ± 0.0 | 80 ± 6 | 664 ± 10 | 240 ± 9 | 63 % ± 2 | 5.9 ± 0.7 | 6.8 ± 0.2 | 0 |
| t150-municion-pill48-hill-ammo3-noHigh | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 68 ± 4 | 684 ± 9 | 221 ± 9 | 71 % ± 1 | 5.9 ± 0.9 | 6.8 ± 0.2 | 0 |
| t150-municion-pill48-hill-ammo5-defecto | 30 | 97 % ± 2 | 3 % ± 2 | 8.0 ± 0.0 | 0.1 ± 0.0 | 78 ± 7 | 810 ± 10 | 180 ± 7 | 70 % ± 2 | 8.6 ± 1.5 | 7.7 ± 0.3 | 0 |
| t150-municion-pill48-hill-ammo5-noHigh | 30 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 70 ± 4 | 845 ± 9 | 181 ± 7 | 75 % ± 1 | 6.5 ± 0.7 | 7.2 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t150-municion-pill48-hill-ammo3-defecto semilla 13300: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 120 de 120 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t150-municion-pill48-hill-ammo3-defecto: enemy:arrow=12
- t150-municion-pill48-hill-ammo3-noHigh: enemy:arrow=1
- t150-municion-pill48-hill-ammo5-defecto: enemy:arrow=1
- t150-municion-pill48-hill-ammo5-noHigh: enemy:arrow=3

## Motivos de decisión (cambios registrados): ENGAGE 1100, FORMATION_HOLD 514, INSTRUCTION 968, LOW_HEALTH_RETREAT 4, SEEK_ENEMY 577, STUCK 118, TOO_CLOSE 90

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.

# Simulación Rally - lote 20261123-072501-t151-municion-dura

Preset `t151-municion-dura` · velocidad `sprint` · 121 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t151-municion-dura-pill96-ammo3-defecto | 30 | 98 % ± 2 | 2 % ± 2 | 6.9 ± 0.2 | 0.0 ± 0.0 | 110 ± 12 | 744 ± 13 | 323 ± 14 | 65 % ± 2 | 6.2 ± 0.8 | 7.7 ± 0.5 | 0 |
| t151-municion-dura-pill96-ammo3-noHigh | 30 | 98 % ± 2 | 2 % ± 2 | 7.1 ± 0.1 | 0.0 ± 0.0 | 105 ± 12 | 782 ± 15 | 314 ± 9 | 76 % ± 2 | 8.0 ± 1.7 | 7.6 ± 0.4 | 0 |
| t151-municion-dura-pill144-ammo3-defecto | 30 | 98 % ± 2 | 2 % ± 2 | 6.7 ± 0.2 | 0.0 ± 0.0 | 112 ± 12 | 799 ± 12 | 363 ± 11 | 68 % ± 1 | 8.0 ± 1.7 | 7.7 ± 0.5 | 0 |
| t151-municion-dura-pill144-ammo3-noHigh | 30 | 100 % ± 0 | 0 % ± 0 | 7.0 ± 0.2 | 0.0 ± 0.0 | 120 ± 12 | 831 ± 11 | 340 ± 9 | 75 % ± 2 | 9.2 ± 1.6 | 7.3 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t151-municion-dura-pill96-ammo3-defecto semilla 13300: sprint → blue (6/0 vivos), normal → blue (7/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 120 de 120 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t151-municion-dura-pill96-ammo3-defecto: enemy:arrow=32
- t151-municion-dura-pill96-ammo3-noHigh: enemy:arrow=26
- t151-municion-dura-pill144-ammo3-defecto: enemy:arrow=39
- t151-municion-dura-pill144-ammo3-noHigh: enemy:arrow=30

## Motivos de decisión (cambios registrados): ENGAGE 1161, FORMATION_HOLD 590, INSTRUCTION 968, LOW_HEALTH_RETREAT 29, SEEK_ENEMY 764, STUCK 201, TOO_CLOSE 47

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.

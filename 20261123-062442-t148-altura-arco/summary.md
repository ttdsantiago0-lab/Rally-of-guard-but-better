# Simulación Rally - lote 20261123-062442-t148-altura-arco

Preset `t148-altura-arco` · velocidad `sprint` · 121 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t148-altura-arco-vind-hill-defecto | 30 | 100 % ± 0 | 0 % ± 0 | 5.5 ± 0.3 | 0.0 ± 0.0 | 18 ± 1 | 429 ± 1 | 94 ± 6 | 70 % ± 2 | 9.1 ± 0.2 | 6.3 ± 0.2 | 0 |
| t148-altura-arco-vind-hill-noHigh | 30 | 100 % ± 0 | 0 % ± 0 | 5.1 ± 0.2 | 0.0 ± 0.0 | 18 ± 0 | 430 ± 2 | 102 ± 5 | 73 % ± 2 | 9.8 ± 0.2 | 6.9 ± 0.2 | 0 |
| t148-altura-arco-pill48-hill-defecto | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 46 ± 2 | 912 ± 8 | 103 ± 3 | 56 % ± 1 | 8.1 ± 0.3 | 8.9 ± 0.2 | 0 |
| t148-altura-arco-pill48-hill-noHigh | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 49 ± 3 | 903 ± 9 | 98 ± 3 | 64 % ± 2 | 8.3 ± 0.4 | 8.8 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t148-altura-arco-vind-hill-defecto semilla 13300: sprint → blue (4/0 vivos), normal → blue (4/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 120 de 120 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t148-altura-arco-vind-hill-defecto: enemy:mob=76
- t148-altura-arco-vind-hill-noHigh: enemy:mob=88

## Motivos de decisión (cambios registrados): ENGAGE 1174, FORMATION_HOLD 89, INSTRUCTION 968, LOW_HEALTH_RETREAT 88, OUT_OF_RANGE 2, SEEK_ENEMY 498, STUCK 62, TOO_CLOSE 178

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.

# Simulación Rally - lote 20261125-204308-t217-cac-sync

Preset `t217-cac-sync` · velocidad `sprint` · 151 corridas · 5 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t217-cac-solo | 30 | 27 % ± 8 | 73 % ± 8 | 0.5 ± 0.2 | 2.9 ± 0.4 | 15 ± 0 | 158 ± 5 | 189 ± 1 | 0 % ± 0 | 3.3 ± 0.1 | 4.3 ± 0.2 | 0 |
| t217-cac-capa | 30 | 43 % ± 9 | 57 % ± 9 | 1.3 ± 0.3 | 2.2 ± 0.4 | 15 ± 0 | 166 ± 5 | 181 ± 3 | 0 % ± 0 | 3.4 ± 0.1 | 4.1 ± 0.2 | 0 |
| t217-cac-sync | 30 | 70 % ± 9 | 30 % ± 9 | 2.6 ± 0.4 | 1.1 ± 0.3 | 14 ± 0 | 182 ± 3 | 162 ± 5 | 0 % ± 0 | 3.7 ± 0.1 | 4.5 ± 0.2 | 0 |
| t217-8v4-capa | 30 | 100 % ± 0 | 0 % ± 0 | 7.5 ± 0.1 | 0.0 ± 0.0 | 11 ± 0 | 96 ± 0 | 64 ± 2 | 0 % ± 0 | 4.0 ± 0.1 | 3.7 ± 0.2 | 0 |
| t217-8v4-sync | 30 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.0 | 0.0 ± 0.0 | 9 ± 0 | 96 ± 0 | 44 ± 1 | 0 % ± 0 | 4.2 ± 0.1 | 3.3 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t217-cac-solo semilla 15100: sprint → red (0/4 vivos), normal → red (0/4 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 102 de 102 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t217-cac-solo: enemy:mob=224
- t217-cac-capa: enemy:mob=202
- t217-cac-sync: enemy:mob=161
- t217-8v4-capa: enemy:mob=15
- t217-8v4-sync: enemy:mob=2

## Motivos de decisión (cambios registrados): ENGAGE 1263, NONE 100

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.

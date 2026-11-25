# Simulación Rally - lote 20261125-205153-t217-cac-sync

Preset `t217-cac-sync` · velocidad `sprint` · 151 corridas · 5 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t217-cac-solo | 30 | 13 % ± 6 | 87 % ± 6 | 0.6 ± 0.3 | 3.5 ± 0.4 | 15 ± 0 | 152 ± 5 | 186 ± 3 | 0 % ± 0 | 3.3 ± 0.1 | 4.6 ± 0.2 | 0 |
| t217-cac-capa | 30 | 13 % ± 6 | 87 % ± 6 | 0.2 ± 0.1 | 3.3 ± 0.3 | 15 ± 0 | 154 ± 4 | 190 ± 1 | 0 % ± 0 | 3.3 ± 0.1 | 4.1 ± 0.2 | 0 |
| t217-cac-sync | 30 | 80 % ± 7 | 20 % ± 7 | 2.7 ± 0.3 | 0.6 ± 0.2 | 15 ± 0 | 187 ± 2 | 164 ± 4 | 0 % ± 0 | 3.8 ± 0.1 | 4.9 ± 0.3 | 0 |
| t217-8v4-capa | 30 | 100 % ± 0 | 0 % ± 0 | 7.7 ± 0.1 | 0.0 ± 0.0 | 10 ± 0 | 96 ± 0 | 58 ± 2 | 0 % ± 0 | 3.9 ± 0.1 | 3.4 ± 0.2 | 0 |
| t217-8v4-sync | 30 | 100 % ± 0 | 0 % ± 0 | 7.8 ± 0.1 | 0.0 ± 0.0 | 9 ± 0 | 96 ± 0 | 45 ± 2 | 0 % ± 0 | 4.4 ± 0.1 | 3.8 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t217-cac-solo semilla 18800: sprint → blue (5/0 vivos), normal → blue (3/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 92 de 92 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t217-cac-solo: enemy:mob=221
- t217-cac-capa: enemy:mob=233
- t217-cac-sync: enemy:mob=160
- t217-8v4-capa: enemy:mob=10
- t217-8v4-sync: enemy:mob=5

## Motivos de decisión (cambios registrados): ENGAGE 1259, NONE 95

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.

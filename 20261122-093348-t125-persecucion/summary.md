# Simulación Rally - lote 20261122-093348-t125-persecucion

Preset `t125-persecucion` · velocidad `sprint` · 122 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t125-persecucion-pie-fuga | 30 | 100 % ± 0 | 0 % ± 0 | 7.1 ± 0.2 | 0.0 ± 0.0 | 28 ± 3 | 145 ± 1 | 117 ± 3 | 0 % ± 0 | 23.4 ± 1.3 | 13.1 ± 0.6 | 0 |
| t125-persecucion-jinete-fuga | 30 | 100 % ± 0 | 0 % ± 0 | 7.2 ± 0.2 | 0.0 ± 0.0 | 23 ± 1 | 144 ± 0 | 99 ± 2 | 0 % ± 0 | 20.1 ± 1.4 | 11.2 ± 0.5 | 0 |
| t125-persecucion-pie-sinfuga | 30 | 88 % ± 6 | 12 % ± 6 | 5.2 ± 0.4 | 0.2 ± 0.1 | 24 ± 4 | 143 ± 1 | 141 ± 5 | 0 % ± 0 | 6.6 ± 0.4 | 6.9 ± 0.3 | 0 |
| t125-persecucion-jinete-sinfuga | 30 | 100 % ± 0 | 0 % ± 0 | 6.8 ± 0.2 | 0.0 ± 0.0 | 14 ± 0 | 144 ± 0 | 111 ± 2 | 0 % ± 0 | 4.4 ± 0.2 | 5.2 ± 0.1 | 1 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t125-persecucion-pie-fuga semilla 11600: sprint → blue (6/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 117 de 117 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t125-persecucion-pie-fuga: enemy:mob=27
- t125-persecucion-jinete-fuga: enemy:mob=23
- t125-persecucion-pie-sinfuga: enemy:mob=85
- t125-persecucion-jinete-sinfuga: enemy:mob=36

## Motivos de decisión (cambios registrados): ENGAGE 1811, FORMATION_HOLD 30, LOW_HEALTH_RETREAT 2338, NONE 276, SEEK_ENEMY 1626

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t125-persecucion-jinete-sinfuga - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)

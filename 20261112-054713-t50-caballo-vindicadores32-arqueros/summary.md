# Simulación Rally - lote 20261112-054713-t50-caballo-vindicadores32-arqueros

Preset `t50-caballo-vindicadores32-arqueros` · velocidad `sprint` · 241 corridas · 8 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t50-caballo-vindicadores32-arqueros-1-sinarmadura | 30 | 100 % ± 0 | 0 % ± 0 | 7.5 ± 0.1 | 0.0 ± 0.0 | 24 ± 1 | 864 ± 4 | 23 ± 3 | 89 % ± 1 | 19.5 ± 0.8 | 11.2 ± 0.7 | 0 |
| t50-caballo-vindicadores32-arqueros-2-hierro | 30 | 100 % ± 0 | 0 % ± 0 | 7.6 ± 0.1 | 0.0 ± 0.0 | 24 ± 1 | 861 ± 3 | 20 ± 3 | 89 % ± 1 | 19.8 ± 0.8 | 11.3 ± 0.7 | 0 |
| t50-caballo-vindicadores32-arqueros-3-diamante | 30 | 100 % ± 0 | 0 % ± 0 | 7.6 ± 0.1 | 0.0 ± 0.0 | 24 ± 0 | 862 ± 4 | 25 ± 4 | 87 % ± 1 | 20.7 ± 0.7 | 13.0 ± 0.6 | 0 |
| t50-caballo-vindicadores32-arqueros-4-superarmadura | 30 | 100 % ± 0 | 0 % ± 0 | 7.7 ± 0.1 | 0.0 ± 0.0 | 24 ± 1 | 860 ± 4 | 16 ± 3 | 87 % ± 1 | 20.8 ± 0.7 | 13.1 ± 0.7 | 0 |
| t50-caballo-vindicadores32-arqueros-5-vida50 | 30 | 100 % ± 0 | 0 % ± 0 | 7.6 ± 0.1 | 0.0 ± 0.0 | 24 ± 0 | 860 ± 4 | 18 ± 3 | 88 % ± 1 | 19.3 ± 0.6 | 11.7 ± 0.6 | 0 |
| t50-caballo-vindicadores32-arqueros-6-diamantevida50 | 30 | 100 % ± 0 | 0 % ± 0 | 7.7 ± 0.1 | 0.0 ± 0.0 | 24 ± 0 | 860 ± 4 | 19 ± 3 | 88 % ± 1 | 20.1 ± 0.8 | 11.9 ± 0.8 | 0 |
| t50-caballo-vindicadores32-arqueros-7-escudofrontal | 30 | 100 % ± 0 | 0 % ± 0 | 7.6 ± 0.1 | 0.0 ± 0.0 | 24 ± 0 | 858 ± 4 | 19 ± 3 | 87 % ± 1 | 20.5 ± 0.8 | 12.3 ± 0.5 | 0 |
| t50-caballo-vindicadores32-arqueros-8-inmortal | 30 | 100 % ± 0 | 0 % ± 0 | 7.8 ± 0.1 | 0.0 ± 0.0 | 24 ± 0 | 865 ± 4 | 17 ± 3 | 86 % ± 1 | 20.3 ± 0.7 | 12.1 ± 0.7 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t50-caballo-vindicadores32-arqueros-1-sinarmadura semilla 4001: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 240 de 240 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t50-caballo-vindicadores32-arqueros-1-sinarmadura: enemy:mob=14
- t50-caballo-vindicadores32-arqueros-2-hierro: enemy:mob=12
- t50-caballo-vindicadores32-arqueros-3-diamante: enemy:mob=13
- t50-caballo-vindicadores32-arqueros-4-superarmadura: enemy:mob=8
- t50-caballo-vindicadores32-arqueros-5-vida50: enemy:mob=11
- t50-caballo-vindicadores32-arqueros-6-diamantevida50: enemy:mob=8
- t50-caballo-vindicadores32-arqueros-7-escudofrontal: enemy:mob=10 enemy:scguns.bullet.eliminated=1
- t50-caballo-vindicadores32-arqueros-8-inmortal: enemy:mob=7

## Motivos de decisión (cambios registrados): ENGAGE 2511, LOW_HEALTH_RETREAT 22, NONE 36, OUT_OF_RANGE 1019, SEEK_ENEMY 16, TOO_CLOSE 210

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.

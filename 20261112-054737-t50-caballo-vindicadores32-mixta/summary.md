# Simulación Rally - lote 20261112-054737-t50-caballo-vindicadores32-mixta

Preset `t50-caballo-vindicadores32-mixta` · velocidad `sprint` · 241 corridas · 8 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t50-caballo-vindicadores32-mixta-1-sinarmadura | 30 | 90 % ± 6 | 10 % ± 6 | 2.5 ± 0.2 | 1.7 ± 1.0 | 41 ± 1 | 804 ± 17 | 151 ± 6 | 91 % ± 1 | 16.6 ± 0.6 | 11.6 ± 1.4 | 0 |
| t50-caballo-vindicadores32-mixta-2-hierro | 30 | 87 % ± 6 | 13 % ± 6 | 2.4 ± 0.2 | 0.8 ± 0.4 | 45 ± 2 | 817 ± 9 | 156 ± 6 | 88 % ± 1 | 18.4 ± 0.9 | 13.9 ± 1.4 | 0 |
| t50-caballo-vindicadores32-mixta-3-diamante | 30 | 87 % ± 6 | 13 % ± 6 | 2.2 ± 0.3 | 1.0 ± 0.5 | 46 ± 2 | 812 ± 9 | 156 ± 7 | 89 % ± 1 | 17.2 ± 0.8 | 11.1 ± 1.3 | 0 |
| t50-caballo-vindicadores32-mixta-4-superarmadura | 30 | 80 % ± 7 | 20 % ± 7 | 2.1 ± 0.3 | 2.8 ± 1.1 | 42 ± 2 | 780 ± 19 | 163 ± 7 | 89 % ± 1 | 16.6 ± 0.7 | 12.3 ± 1.3 | 0 |
| t50-caballo-vindicadores32-mixta-5-vida50 | 30 | 87 % ± 6 | 13 % ± 6 | 2.2 ± 0.2 | 1.3 ± 0.6 | 42 ± 2 | 809 ± 14 | 161 ± 7 | 89 % ± 1 | 16.9 ± 0.6 | 13.4 ± 1.2 | 0 |
| t50-caballo-vindicadores32-mixta-6-diamantevida50 | 30 | 80 % ± 7 | 20 % ± 7 | 2.1 ± 0.2 | 2.4 ± 1.0 | 42 ± 2 | 791 ± 16 | 162 ± 7 | 89 % ± 1 | 16.6 ± 0.7 | 12.1 ± 1.2 | 0 |
| t50-caballo-vindicadores32-mixta-7-escudofrontal | 30 | 90 % ± 6 | 10 % ± 6 | 2.4 ± 0.2 | 0.7 ± 0.4 | 43 ± 2 | 815 ± 9 | 157 ± 7 | 90 % ± 1 | 16.2 ± 0.6 | 11.9 ± 1.3 | 0 |
| t50-caballo-vindicadores32-mixta-8-inmortal | 30 | 77 % ± 8 | 23 % ± 8 | 2.1 ± 0.3 | 2.0 ± 0.8 | 44 ± 2 | 804 ± 14 | 165 ± 7 | 90 % ± 1 | 16.9 ± 0.8 | 10.7 ± 1.1 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t50-caballo-vindicadores32-mixta-1-sinarmadura semilla 4001: sprint → blue (1/0 vivos), normal → blue (1/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 203 de 203 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t50-caballo-vindicadores32-mixta-1-sinarmadura: enemy:mob=164 enemy:scguns.bullet.decimated=1 enemy:scguns.bullet.killed=1
- t50-caballo-vindicadores32-mixta-2-hierro: enemy:mob=166 enemy:scguns.bullet.executed=1 enemy:scguns.bullet.killed=1
- t50-caballo-vindicadores32-mixta-3-diamante: enemy:mob=168 enemy:scguns.bullet.annihilated=2 enemy:scguns.bullet.eliminated=2 enemy:scguns.bullet.executed=1 enemy:scguns.bullet.killed=1
- t50-caballo-vindicadores32-mixta-4-superarmadura: enemy:mob=174 enemy:scguns.bullet.decimated=1 enemy:scguns.bullet.eliminated=2 enemy:scguns.bullet.killed=1
- t50-caballo-vindicadores32-mixta-5-vida50: enemy:mob=174 enemy:scguns.bullet.annihilated=1
- t50-caballo-vindicadores32-mixta-6-diamantevida50: enemy:mob=174 enemy:scguns.bullet.annihilated=2 enemy:scguns.bullet.decimated=1
- t50-caballo-vindicadores32-mixta-7-escudofrontal: enemy:mob=164 enemy:scguns.bullet.decimated=3 enemy:scguns.bullet.eliminated=1
- t50-caballo-vindicadores32-mixta-8-inmortal: enemy:mob=176 enemy:scguns.bullet.annihilated=1 enemy:scguns.bullet.executed=1

## Motivos de decisión (cambios registrados): ENGAGE 2673, FORMATION_HOLD 309, LOW_HEALTH_RETREAT 153, NONE 30, OUT_OF_RANGE 542, SEEK_ENEMY 58, TOO_CLOSE 558

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.

# Simulación Rally - lote 20261114-125412-t54-pinza-vindicadores32

Preset `t54-pinza-vindicadores32` · velocidad `sprint` · 181 corridas · 6 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t54-pinza-vindicadores32-linea-base | 30 | 100 % ± 0 | 0 % ± 0 | 9.2 ± 0.3 | 0.0 ± 0.0 | 20 ± 1 | 829 ± 6 | 207 ± 4 | 85 % ± 1 | 14.4 ± 0.7 | 13.1 ± 0.4 | 0 |
| t54-pinza-vindicadores32-linea-mantener | 30 | 100 % ± 0 | 0 % ± 0 | 11.6 ± 0.3 | 0.0 ± 0.0 | 18 ± 0 | 823 ± 6 | 173 ± 7 | 86 % ± 1 | 14.0 ± 0.7 | 13.0 ± 0.4 | 0 |
| t54-pinza-vindicadores32-linea-pinza | 30 | 100 % ± 0 | 0 % ± 0 | 11.8 ± 0.2 | 0.0 ± 0.0 | 19 ± 1 | 822 ± 5 | 164 ± 5 | 86 % ± 1 | 14.9 ± 0.7 | 12.8 ± 0.3 | 0 |
| t54-pinza-vindicadores32-cab-base | 30 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 25 ± 1 | 846 ± 5 | 203 ± 2 | 82 % ± 1 | 19.6 ± 0.6 | 13.2 ± 0.5 | 0 |
| t54-pinza-vindicadores32-cab-mantener | 30 | 100 % ± 0 | 0 % ± 0 | 8.2 ± 0.1 | 0.0 ± 0.0 | 27 ± 1 | 836 ± 7 | 202 ± 3 | 79 % ± 1 | 21.3 ± 0.8 | 14.4 ± 0.5 | 0 |
| t54-pinza-vindicadores32-cab-pinza | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 25 ± 1 | 847 ± 7 | 203 ± 2 | 82 % ± 1 | 19.9 ± 0.7 | 13.4 ± 0.5 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t54-pinza-vindicadores32-linea-base semilla 5401: sprint → blue (8/0 vivos), normal → blue (12/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 180 de 180 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t54-pinza-vindicadores32-linea-base: enemy:mob=197 enemy:scguns.bullet.annihilated=1 enemy:scguns.bullet.decimated=2 enemy:scguns.bullet.eliminated=1 enemy:scguns.bullet.killed=2
- t54-pinza-vindicadores32-linea-mantener: enemy:mob=123 enemy:scguns.bullet.annihilated=1 enemy:scguns.bullet.decimated=3 enemy:scguns.bullet.eliminated=2 enemy:scguns.bullet.executed=1 enemy:scguns.bullet.killed=1
- t54-pinza-vindicadores32-linea-pinza: enemy:mob=119 enemy:scguns.bullet.annihilated=1 enemy:scguns.bullet.decimated=2 enemy:scguns.bullet.eliminated=2 enemy:scguns.bullet.killed=3
- t54-pinza-vindicadores32-cab-base: enemy:mob=243
- t54-pinza-vindicadores32-cab-mantener: enemy:mob=233 enemy:scguns.bullet.executed=1 enemy:scguns.bullet.killed=1
- t54-pinza-vindicadores32-cab-pinza: enemy:mob=238 enemy:scguns.bullet.eliminated=1

## Motivos de decisión (cambios registrados): ENGAGE 3338, FORMATION_HOLD 447, INSTRUCTION 1920, LOW_HEALTH_RETREAT 208, NONE 6, OUT_OF_RANGE 974, SEEK_ENEMY 294, TOO_CLOSE 59

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.

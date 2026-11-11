# Simulación Rally - lote 20261111-215241-t48-retirada-vindicadores32

Preset `t48-retirada-vindicadores32` · velocidad `sprint` · 191 corridas · 6 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t48-retirada-vindicadores32-linea-pelear | 30 | 100 % ± 0 | 0 % ± 0 | 3.5 ± 0.1 | 0.0 ± 0.0 | 38 ± 1 | 831 ± 8 | 131 ± 3 | 86 % ± 1 | 16.2 ± 0.7 | 15.5 ± 0.8 | 0 |
| t48-retirada-vindicadores32-linea-nueva | 30 | 100 % ± 0 | 0 % ± 0 | 3.8 ± 0.1 | 0.0 ± 0.0 | 38 ± 2 | 844 ± 7 | 117 ± 2 | 89 % ± 1 | 16.9 ± 0.7 | 15.9 ± 0.7 | 0 |
| t48-retirada-vindicadores32-cab-pelear | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 29.1 ± 0.5 | 12 ± 1 | 191 ± 8 | 196 ± 1 | 91 % ± 1 | 10.3 ± 0.2 | 4.9 ± 0.2 | 0 |
| t48-retirada-vindicadores32-cab-vieja | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 31.9 ± 0.1 | 14 ± 0 | 25 ± 2 | 195 ± 0 | 66 % ± 9 | 16.9 ± 0.1 | 4.4 ± 0.1 | 0 |
| t48-retirada-vindicadores32-cab-nueva | 30 | 2 % ± 2 | 98 % ± 2 | 0.0 ± 0.0 | 29.1 ± 0.7 | 23 ± 3 | 194 ± 18 | 197 ± 1 | 94 % ± 1 | 19.9 ± 0.4 | 7.8 ± 0.2 | 0 |
| t48-retirada-vindicadores32-linea-vieja | 24 | 4 % ± 3 | 96 % ± 3 | 0.1 ± 0.1 | 30.3 ± 0.2 | 58 ± 7 | 133 ± 11 | 237 ± 5 | 86 % ± 7 | 11.4 ± 0.5 | 8.6 ± 1.3 | 16 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t48-retirada-vindicadores32-linea-pelear semilla 2001: sprint → blue (2/0 vivos), normal → blue (2/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 63 de 63 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t48-retirada-vindicadores32-linea-pelear: enemy:mob=132 enemy:scguns.bullet.decimated=1 enemy:scguns.bullet.eliminated=1 enemy:scguns.bullet.executed=1
- t48-retirada-vindicadores32-linea-nueva: enemy:mob=122 enemy:scguns.bullet.decimated=1 enemy:scguns.bullet.eliminated=1 enemy:scguns.bullet.executed=1 enemy:scguns.bullet.killed=2
- t48-retirada-vindicadores32-cab-pelear: enemy:mob=240
- t48-retirada-vindicadores32-cab-vieja: enemy:mob=240
- t48-retirada-vindicadores32-cab-nueva: enemy:mob=239
- t48-retirada-vindicadores32-linea-vieja: enemy:mob=183 enemy:scguns.bullet.annihilated=3 enemy:scguns.bullet.executed=3 enemy:scguns.bullet.killed=1

## Motivos de decisión (cambios registrados): ENGAGE 1430, FORMATION_HOLD 367, INSTRUCTION 1040, LOW_HEALTH_RETREAT 64, NONE 6, OUT_OF_RANGE 186, SEEK_ENEMY 366, STUCK 1, TOO_CLOSE 343

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t48-retirada-vindicadores32-linea-vieja - 16 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)

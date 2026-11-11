# Simulación Rally - lote 20261111-163253-t44-retirada-vindicadores32

Preset `t44-retirada-vindicadores32` · velocidad `sprint` · 57 corridas · 9 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t44-retirada-vindicadores32-linea-pelear | 6 | 100 % ± 0 | 0 % ± 0 | 3.7 ± 0.2 | 0.0 ± 0.0 | 33 ± 1 | 822 ± 18 | 123 ± 5 | 90 % ± 2 | 14.5 ± 1.3 | 14.8 ± 2.1 | 0 |
| t44-retirada-vindicadores32-linea-nueva | 6 | 100 % ± 0 | 0 % ± 0 | 3.8 ± 0.2 | 0.0 ± 0.0 | 37 ± 2 | 845 ± 22 | 115 ± 5 | 86 % ± 2 | 18.3 ± 1.2 | 16.5 ± 1.1 | 0 |
| t44-retirada-vindicadores32-arco-pelear | 6 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 24 ± 1 | 840 ± 13 | 12 ± 3 | 88 % ± 2 | 15.6 ± 1.8 | 10.5 ± 1.8 | 0 |
| t44-retirada-vindicadores32-arco-vieja | 6 | 100 % ± 0 | 0 % ± 0 | 7.0 ± 0.4 | 0.0 ± 0.0 | 27 ± 2 | 850 ± 11 | 45 ± 15 | 89 % ± 2 | 15.0 ± 1.2 | 13.0 ± 1.5 | 0 |
| t44-retirada-vindicadores32-arco-nueva | 6 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 23 ± 1 | 848 ± 5 | 11 ± 4 | 89 % ± 1 | 16.5 ± 1.3 | 9.9 ± 1.0 | 0 |
| t44-retirada-vindicadores32-cab-pelear | 6 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 29.0 ± 0.9 | 11 ± 0 | 189 ± 9 | 202 ± 3 | 96 % ± 2 | 11.4 ± 0.4 | 5.5 ± 0.3 | 0 |
| t44-retirada-vindicadores32-cab-vieja | 6 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 32.0 ± 0.0 | 14 ± 1 | 22 ± 4 | 196 ± 1 | 50 % ± 22 | 16.8 ± 0.2 | 4.5 ± 0.1 | 0 |
| t44-retirada-vindicadores32-cab-nueva | 6 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 27.7 ± 2.6 | 29 ± 9 | 235 ± 63 | 199 ± 4 | 94 % ± 3 | 19.4 ± 1.8 | 7.1 ± 0.9 | 0 |
| t44-retirada-vindicadores32-linea-vieja | 4 | 13 % ± 13 | 88 % ± 13 | 0.3 ± 0.3 | 31.0 ± 0.4 | 49 ± 25 | 131 ± 43 | 221 ± 12 | 100 % ± 0 | 12.5 ± 1.7 | 8.1 ± 4.1 | 4 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t44-retirada-vindicadores32-linea-pelear semilla 1: sprint → blue (3/0 vivos), normal → blue (4/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 31 de 31 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t44-retirada-vindicadores32-linea-pelear: enemy:mob=25 enemy:scguns.bullet.killed=1
- t44-retirada-vindicadores32-linea-nueva: enemy:mob=25
- t44-retirada-vindicadores32-arco-vieja: enemy:mob=4 enemy:scguns.bullet.killed=2
- t44-retirada-vindicadores32-cab-pelear: enemy:mob=48
- t44-retirada-vindicadores32-cab-vieja: enemy:mob=48
- t44-retirada-vindicadores32-cab-nueva: enemy:mob=48
- t44-retirada-vindicadores32-linea-vieja: enemy:mob=30 enemy:scguns.bullet.decimated=1

## Motivos de decisión (cambios registrados): ENGAGE 484, FORMATION_HOLD 74, INSTRUCTION 304, LOW_HEALTH_RETREAT 33, NONE 2, ORDER_OVERRIDE 6, OUT_OF_RANGE 130, SEEK_ENEMY 86, TOO_CLOSE 85

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t44-retirada-vindicadores32-linea-vieja - 4 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)

# Simulación Rally - lote 20261109-201918-t35-numeros-dragonjoven

Preset `t35-numeros-dragonjoven` · velocidad `sprint` · 42 corridas · 8 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t35-numeros-dragonjoven-hierro-g2 | 5 | 100 % ± 0 | 0 % ± 0 | 2.0 ± 0.0 | 0.0 ± 0.0 | 18 ± 0 | 143 ± 2 | 2 ± 1 | 9 % ± 4 | 16.1 ± 2.0 | 4.6 ± 0.8 | 0 |
| t35-numeros-dragonjoven-netheritamas-g2 | 5 | 100 % ± 0 | 0 % ± 0 | 2.0 ± 0.0 | 0.0 ± 0.0 | 9 ± 0 | 143 ± 2 | 0 ± 0 | 8 % ± 5 | 19.8 ± 0.7 | 2.2 ± 0.3 | 1 |
| t35-numeros-dragonjoven-hierro-g4 | 5 | 80 % ± 20 | 20 % ± 20 | 3.2 ± 0.8 | 0.2 ± 0.2 | 61 ± 42 | 117 ± 25 | 51 ± 47 | 17 % ± 6 | 17.1 ± 2.1 | 8.3 ± 1.3 | 0 |
| t35-numeros-dragonjoven-netheritamas-g4 | 5 | 80 % ± 20 | 20 % ± 20 | 3.2 ± 0.8 | 0.2 ± 0.2 | 53 ± 44 | 114 ± 24 | 32 ± 27 | 12 % ± 8 | 26.6 ± 1.8 | 9.0 ± 1.7 | 0 |
| t35-numeros-dragonjoven-hierro-g8 | 5 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 16 ± 1 | 139 ± 1 | 3 ± 1 | 28 % ± 7 | 20.0 ± 1.2 | 9.3 ± 1.0 | 0 |
| t35-numeros-dragonjoven-netheritamas-g8 | 5 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 10 ± 1 | 146 ± 3 | 0 ± 0 | 15 % ± 3 | 23.0 ± 0.7 | 7.1 ± 1.7 | 0 |
| t35-numeros-dragonjoven-hierro-g16 | 5 | 100 % ± 0 | 0 % ± 0 | 16.0 ± 0.0 | 0.0 ± 0.0 | 14 ± 1 | 138 ± 1 | 18 ± 10 | 29 % ± 6 | 23.8 ± 0.9 | 10.9 ± 0.7 | 0 |
| t35-numeros-dragonjoven-netheritamas-g16 | 5 | 100 % ± 0 | 0 % ± 0 | 16.0 ± 0.0 | 0.0 ± 0.0 | 8 ± 0 | 145 ± 3 | 2 ± 2 | 19 % ± 2 | 28.8 ± 0.3 | 10.7 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t35-numeros-dragonjoven-hierro-g2 semilla 1: sprint → blue (2/0 vivos), normal → blue (1/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 38 de 38 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t35-numeros-dragonjoven-hierro-g4: enemy:dragon_fire=1 enemy:mob=2 env:inFire=1
- t35-numeros-dragonjoven-netheritamas-g4: enemy:dragon_fire=2 env:onFire=2

## Motivos de decisión (cambios registrados): ENGAGE 240, LOW_HEALTH_RETREAT 1, NONE 80, SEEK_ENEMY 6, TOO_CLOSE 5

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t35-numeros-dragonjoven-netheritamas-g2 - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- MUERTES QUE NO SON DEL ENEMIGO: t35-numeros-dragonjoven-hierro-g4 - 25 % de las bajas azules ({enemy:dragon_fire=1.0, enemy:mob=2.0, env:inFire=1.0})
- MUERTES QUE NO SON DEL ENEMIGO: t35-numeros-dragonjoven-netheritamas-g4 - 50 % de las bajas azules ({enemy:dragon_fire=2.0, env:onFire=2.0})

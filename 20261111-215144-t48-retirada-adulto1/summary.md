# Simulación Rally - lote 20261111-215144-t48-retirada-adulto1

Preset `t48-retirada-adulto1` · velocidad `sprint` · 187 corridas · 6 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t48-retirada-adulto1-linea-pelear | 30 | 97 % ± 3 | 3 % ± 3 | 7.4 ± 0.3 | 0.0 ± 0.0 | 26 ± 1 | 254 ± 0 | 29 ± 8 | 15 % ± 1 | 14.8 ± 0.4 | 5.7 ± 0.2 | 0 |
| t48-retirada-adulto1-linea-vieja | 30 | 27 % ± 8 | 73 % ± 8 | 0.8 ± 0.3 | 0.8 ± 0.1 | 89 ± 3 | 195 ± 9 | 229 ± 8 | 25 % ± 3 | 16.7 ± 0.6 | 5.6 ± 0.3 | 2 |
| t48-retirada-adulto1-linea-nueva | 30 | 100 % ± 0 | 0 % ± 0 | 7.6 ± 0.2 | 0.0 ± 0.0 | 27 ± 1 | 255 ± 1 | 20 ± 7 | 13 % ± 2 | 28.6 ± 0.6 | 11.0 ± 0.2 | 0 |
| t48-retirada-adulto1-cab-pelear | 30 | 97 % ± 3 | 3 % ± 3 | 6.7 ± 0.3 | 0.0 ± 0.0 | 27 ± 1 | 252 ± 2 | 49 ± 10 | 13 % ± 2 | 12.3 ± 0.8 | 5.5 ± 0.5 | 0 |
| t48-retirada-adulto1-cab-vieja | 30 | 3 % ± 3 | 97 % ± 3 | 0.0 ± 0.0 | 1.0 ± 0.0 | 55 ± 2 | 118 ± 9 | 221 ± 6 | 34 % ± 3 | 14.4 ± 0.8 | 5.3 ± 0.3 | 0 |
| t48-retirada-adulto1-cab-nueva | 30 | 93 % ± 5 | 7 % ± 5 | 6.1 ± 0.4 | 0.1 ± 0.0 | 34 ± 2 | 250 ± 3 | 71 ± 11 | 18 % ± 1 | 26.0 ± 1.3 | 12.5 ± 0.5 | 4 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t48-retirada-adulto1-linea-pelear semilla 2001: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 126 de 126 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t48-retirada-adulto1-linea-pelear: enemy:dragon_fire=9 enemy:mob=7 env:onFire=1
- t48-retirada-adulto1-linea-vieja: enemy:dragon_fire=106 enemy:mob=84 env:fall=1 env:inFire=14 env:onFire=12
- t48-retirada-adulto1-linea-nueva: enemy:dragon_fire=7 enemy:mob=4
- t48-retirada-adulto1-cab-pelear: enemy:dragon_fire=18 enemy:mob=14 env:inFire=6 env:onFire=1
- t48-retirada-adulto1-cab-vieja: enemy:dragon_fire=93 enemy:mob=131 env:inFire=11 env:onFire=4
- t48-retirada-adulto1-cab-nueva: enemy:dragon_fire=27 enemy:mob=29 env:onFire=2

## Motivos de decisión (cambios registrados): ENGAGE 1717, FORMATION_HOLD 985, INSTRUCTION 1008, LOW_HEALTH_RETREAT 499, NONE 6, OUT_OF_RANGE 9, SEEK_ENEMY 953, STUCK 14, TOO_CLOSE 336

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t48-retirada-adulto1-cab-nueva - 4 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t48-retirada-adulto1-linea-vieja - 2 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- MUERTES QUE NO SON DEL ENEMIGO: t48-retirada-adulto1-linea-vieja - 12 % de las bajas azules ({enemy:dragon_fire=106.0, enemy:mob=84.0, env:fall=1.0, env:inFire=14.0, env:onFire=12.0})
- MUERTES QUE NO SON DEL ENEMIGO: t48-retirada-adulto1-cab-pelear - 18 % de las bajas azules ({enemy:dragon_fire=18.0, enemy:mob=14.0, env:inFire=6.0, env:onFire=1.0})

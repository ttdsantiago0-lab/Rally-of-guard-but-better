# Simulación Rally - lote 20261111-073252-t40-techo

Preset `t40-techo` · velocidad `sprint` · 67 corridas · 12 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t40-techo-tremorzilla-g16-rally | 5 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 144 ± 14 | 887 ± 73 | 361 ± 19 | 91 % ± 3 | 20.0 ± 2.0 | 15.0 ± 1.2 | 1 |
| t40-techo-tremorzilla-g16-sincro | 4 | 50 % ± 29 | 50 % ± 29 | 4.5 ± 2.6 | 0.5 ± 0.3 | 107 ± 26 | 888 ± 64 | 297 ± 18 | 83 % ± 5 | 20.2 ± 2.9 | 15.0 ± 3.3 | 4 |
| t40-techo-tremorzilla-g32-rally | 5 | 80 % ± 20 | 20 % ± 20 | 13.4 ± 3.5 | 0.2 ± 0.2 | 87 ± 22 | 930 ± 140 | 520 ± 60 | 94 % ± 1 | 24.6 ± 1.2 | 19.1 ± 1.4 | 0 |
| t40-techo-tremorzilla-g32-oraculo | 5 | 100 % ± 0 | 0 % ± 0 | 23.4 ± 1.4 | 0.0 ± 0.0 | 72 ± 4 | 854 ± 44 | 405 ± 29 | 94 % ± 1 | 26.6 ± 1.1 | 20.7 ± 0.9 | 0 |
| t40-techo-tremorzilla-g32-sincro | 5 | 100 % ± 0 | 0 % ± 0 | 21.8 ± 1.8 | 0.0 ± 0.0 | 67 ± 3 | 822 ± 37 | 465 ± 38 | 87 % ± 1 | 24.4 ± 0.9 | 19.0 ± 0.9 | 0 |
| t40-techo-ignis-g4-rally | 5 | 40 % ± 24 | 60 % ± 24 | 0.6 ± 0.4 | 0.6 ± 0.2 | 87 ± 11 | 611 ± 52 | 151 ± 21 | 91 % ± 2 | 9.8 ± 0.5 | 7.5 ± 0.6 | 0 |
| t40-techo-ignis-g4-oraculo | 5 | 60 % ± 24 | 40 % ± 24 | 0.8 ± 0.4 | 0.4 ± 0.2 | 89 ± 9 | 610 ± 90 | 165 ± 25 | 87 % ± 2 | 8.9 ± 0.4 | 6.9 ± 0.7 | 0 |
| t40-techo-ignis-g4-sincro | 5 | 80 % ± 20 | 20 % ± 20 | 1.2 ± 0.4 | 0.2 ± 0.2 | 86 ± 5 | 646 ± 26 | 152 ± 24 | 92 % ± 1 | 9.8 ± 0.6 | 7.9 ± 0.7 | 0 |
| t40-techo-ignis-g8-rally | 5 | 100 % ± 0 | 0 % ± 0 | 3.8 ± 0.6 | 0.0 ± 0.0 | 97 ± 4 | 821 ± 51 | 278 ± 19 | 89 % ± 1 | 9.0 ± 0.1 | 7.9 ± 0.2 | 0 |
| t40-techo-ignis-g8-oraculo | 5 | 100 % ± 0 | 0 % ± 0 | 3.6 ± 0.5 | 0.0 ± 0.0 | 81 ± 5 | 660 ± 48 | 244 ± 15 | 84 % ± 3 | 9.1 ± 0.5 | 8.3 ± 0.5 | 0 |
| t40-techo-ignis-g8-sincro | 5 | 100 % ± 0 | 0 % ± 0 | 2.0 ± 0.6 | 0.0 ± 0.0 | 97 ± 8 | 779 ± 55 | 301 ± 20 | 84 % ± 3 | 8.7 ± 0.5 | 7.6 ± 0.5 | 0 |
| t40-techo-tremorzilla-g16-oraculo | 4 | 25 % ± 25 | 75 % ± 25 | 2.0 ± 2.0 | 0.8 ± 0.3 | 131 ± 21 | 876 ± 72 | 383 ± 29 | 96 % ± 1 | 22.2 ± 3.1 | 17.1 ± 3.9 | 3 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t40-techo-tremorzilla-g16-rally semilla 1: sprint → red (0/1 vivos), normal → red (0/1 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 41 de 41 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t40-techo-tremorzilla-g16-rally: enemy:mob=9 enemy:tremorzilla_beam=62 env:radiation=9
- t40-techo-tremorzilla-g16-sincro: enemy:mob=8 enemy:tremorzilla_beam=34 env:radiation=4
- t40-techo-tremorzilla-g32-rally: enemy:mob=8 enemy:tremorzilla_beam=83 env:radiation=2
- t40-techo-tremorzilla-g32-oraculo: enemy:mob=10 enemy:tremorzilla_beam=32 env:radiation=1
- t40-techo-tremorzilla-g32-sincro: enemy:mob=8 enemy:tremorzilla_beam=39 env:radiation=4
- t40-techo-ignis-g4-rally: enemy:cataclysm.flame_strike=5 enemy:mob=12
- t40-techo-ignis-g4-oraculo: enemy:cataclysm.flame_strike=6 enemy:mob=9 env:onFire=1
- t40-techo-ignis-g4-sincro: enemy:cataclysm.flame_strike=4 enemy:mob=9 env:inFire=1
- t40-techo-ignis-g8-rally: enemy:cataclysm.flame_strike=10 enemy:mob=9 env:onFire=2
- t40-techo-ignis-g8-oraculo: enemy:cataclysm.flame_strike=13 enemy:mob=8 env:onFire=1
- t40-techo-ignis-g8-sincro: enemy:cataclysm.flame_strike=14 enemy:mob=15 env:inFire=1
- t40-techo-tremorzilla-g16-oraculo: enemy:mob=6 enemy:tremorzilla_beam=43 env:radiation=7

## Motivos de decisión (cambios registrados): ENGAGE 2296, FORMATION_HOLD 2, LOW_HEALTH_RETREAT 1695, NONE 1511, OUT_OF_RANGE 426, SEEK_ENEMY 1792, STUCK 84, TOO_CLOSE 129

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t40-techo-tremorzilla-g16-oraculo - 3 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t40-techo-tremorzilla-g16-sincro - 4 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t40-techo-tremorzilla-g16-rally - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- MUERTES QUE NO SON DEL ENEMIGO: t40-techo-tremorzilla-g16-rally - 11 % de las bajas azules ({enemy:mob=9.0, enemy:tremorzilla_beam=62.0, env:radiation=9.0})
- MUERTES QUE NO SON DEL ENEMIGO: t40-techo-tremorzilla-g16-oraculo - 13 % de las bajas azules ({enemy:mob=6.0, enemy:tremorzilla_beam=43.0, env:radiation=7.0})

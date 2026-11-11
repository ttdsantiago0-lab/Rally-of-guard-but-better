# Simulación Rally - lote 20261111-080440-t40-techo

Preset `t40-techo` · velocidad `sprint` · 64 corridas · 12 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t40-techo-tremorzilla-g16-rally | 5 | 20 % ± 20 | 80 % ± 20 | 1.4 ± 1.4 | 0.8 ± 0.2 | 121 ± 8 | 979 ± 106 | 382 ± 37 | 93 % ± 1 | 20.1 ± 2.8 | 14.2 ± 2.3 | 0 |
| t40-techo-tremorzilla-g16-oraculo | 5 | 40 % ± 24 | 60 % ± 24 | 3.6 ± 2.4 | 0.6 ± 0.2 | 117 ± 20 | 1063 ± 93 | 353 ± 50 | 95 % ± 1 | 17.4 ± 0.7 | 13.4 ± 1.8 | 2 |
| t40-techo-tremorzilla-g16-sincro | 5 | 40 % ± 24 | 60 % ± 24 | 5.2 ± 3.2 | 0.6 ± 0.2 | 108 ± 25 | 825 ± 38 | 315 ± 57 | 91 % ± 2 | 20.2 ± 2.3 | 14.1 ± 1.8 | 1 |
| t40-techo-tremorzilla-g32-rally | 5 | 100 % ± 0 | 0 % ± 0 | 23.6 ± 1.7 | 0.0 ± 0.0 | 72 ± 5 | 851 ± 42 | 413 ± 30 | 94 % ± 1 | 25.6 ± 1.0 | 19.6 ± 1.0 | 0 |
| t40-techo-tremorzilla-g32-oraculo | 5 | 100 % ± 0 | 0 % ± 0 | 27.4 ± 3.0 | 0.0 ± 0.0 | 63 ± 3 | 771 ± 32 | 294 ± 73 | 94 % ± 1 | 26.2 ± 0.8 | 20.2 ± 0.9 | 0 |
| t40-techo-tremorzilla-g32-sincro | 5 | 100 % ± 0 | 0 % ± 0 | 22.6 ± 2.4 | 0.0 ± 0.0 | 79 ± 4 | 932 ± 36 | 460 ± 32 | 86 % ± 4 | 24.0 ± 1.2 | 18.0 ± 0.5 | 0 |
| t40-techo-ignis-g4-rally | 5 | 80 % ± 20 | 20 % ± 20 | 1.2 ± 0.4 | 0.2 ± 0.2 | 85 ± 8 | 625 ± 48 | 174 ± 19 | 91 % ± 2 | 9.6 ± 0.5 | 7.8 ± 0.7 | 0 |
| t40-techo-ignis-g4-oraculo | 5 | 60 % ± 24 | 40 % ± 24 | 1.2 ± 0.6 | 0.4 ± 0.2 | 89 ± 7 | 657 ± 43 | 150 ± 24 | 93 % ± 2 | 9.1 ± 0.6 | 7.8 ± 1.1 | 0 |
| t40-techo-ignis-g4-sincro | 5 | 80 % ± 20 | 20 % ± 20 | 1.2 ± 0.4 | 0.2 ± 0.2 | 72 ± 5 | 533 ± 59 | 129 ± 13 | 92 % ± 3 | 9.7 ± 0.6 | 8.0 ± 0.7 | 0 |
| t40-techo-ignis-g8-rally | 5 | 80 % ± 20 | 20 % ± 20 | 2.6 ± 0.7 | 0.2 ± 0.2 | 96 ± 7 | 802 ± 63 | 301 ± 24 | 87 % ± 2 | 9.1 ± 0.3 | 8.4 ± 0.3 | 0 |
| t40-techo-ignis-g8-oraculo | 5 | 80 % ± 20 | 20 % ± 20 | 2.6 ± 0.7 | 0.2 ± 0.2 | 93 ± 11 | 767 ± 97 | 298 ± 38 | 87 % ± 1 | 8.7 ± 0.2 | 8.1 ± 0.2 | 0 |
| t40-techo-ignis-g8-sincro | 5 | 80 % ± 20 | 20 % ± 20 | 2.4 ± 0.9 | 0.2 ± 0.2 | 85 ± 7 | 692 ± 64 | 250 ± 22 | 84 % ± 3 | 8.9 ± 0.2 | 7.7 ± 0.4 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t40-techo-tremorzilla-g16-rally semilla 1001: sprint → red (0/1 vivos), normal → red (0/1 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 43 de 43 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t40-techo-tremorzilla-g16-rally: enemy:mob=10 enemy:tremorzilla_beam=52 env:radiation=11
- t40-techo-tremorzilla-g16-oraculo: enemy:mob=15 enemy:tremorzilla_beam=43 env:radiation=4
- t40-techo-tremorzilla-g16-sincro: enemy:mob=5 enemy:tremorzilla_beam=46 env:radiation=3
- t40-techo-tremorzilla-g32-rally: enemy:mob=8 enemy:tremorzilla_beam=28 env:radiation=6
- t40-techo-tremorzilla-g32-oraculo: enemy:mob=5 enemy:tremorzilla_beam=18
- t40-techo-tremorzilla-g32-sincro: enemy:mob=15 enemy:tremorzilla_beam=30 env:radiation=2
- t40-techo-ignis-g4-rally: enemy:cataclysm.flame_strike=7 enemy:mob=7
- t40-techo-ignis-g4-oraculo: enemy:cataclysm.flame_strike=3 enemy:mob=11
- t40-techo-ignis-g4-sincro: enemy:cataclysm.flame_strike=4 enemy:mob=10
- t40-techo-ignis-g8-rally: enemy:cataclysm.flame_strike=13 enemy:explosion.player=1 enemy:mob=11 env:fall=1 env:onFire=1
- t40-techo-ignis-g8-oraculo: enemy:cataclysm.flame_strike=14 enemy:mob=11 env:fall=1 env:inFire=1
- t40-techo-ignis-g8-sincro: enemy:cataclysm.flame_strike=12 enemy:mob=16

## Motivos de decisión (cambios registrados): ENGAGE 2354, FORMATION_HOLD 5, LOW_HEALTH_RETREAT 1306, NONE 1334, OUT_OF_RANGE 426, SEEK_ENEMY 1656, STUCK 67, TOO_CLOSE 158, UNIT_BROKEN 1

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t40-techo-tremorzilla-g16-oraculo - 2 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t40-techo-tremorzilla-g16-sincro - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- MUERTES QUE NO SON DEL ENEMIGO: t40-techo-tremorzilla-g16-rally - 15 % de las bajas azules ({enemy:mob=10.0, enemy:tremorzilla_beam=52.0, env:radiation=11.0})
- MUERTES QUE NO SON DEL ENEMIGO: t40-techo-tremorzilla-g32-rally - 14 % de las bajas azules ({enemy:mob=8.0, enemy:tremorzilla_beam=28.0, env:radiation=6.0})

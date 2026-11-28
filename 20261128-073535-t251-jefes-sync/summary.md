# Simulación Rally - lote 20261128-073535-t251-jefes-sync

Preset `t251-jefes-sync` · velocidad `sprint` · 121 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t251-ignis | 30 | 15 % ± 6 | 85 % ± 6 | 0.9 ± 0.4 | 0.9 ± 0.1 | 225 ± 11 | 1899 ± 97 | 972 ± 36 | 0 % ± 0 | 11.8 ± 0.3 | 10.3 ± 0.3 | 0 |
| t251-ignis-sync | 30 | 83 % ± 7 | 17 % ± 7 | 5.1 ± 0.5 | 0.2 ± 0.1 | 116 ± 6 | 938 ± 47 | 464 ± 26 | 0 % ± 0 | 11.8 ± 0.3 | 11.2 ± 0.4 | 0 |
| t251-monst8 | 30 | 100 % ± 0 | 0 % ± 0 | 7.8 ± 0.1 | 0.0 ± 0.0 | 88 ± 2 | 607 ± 2 | 376 ± 6 | 0 % ± 0 | 9.7 ± 0.1 | 6.2 ± 0.1 | 0 |
| t251-monst8-sync | 30 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 60 ± 0 | 603 ± 1 | 190 ± 4 | 0 % ± 0 | 8.7 ± 0.2 | 5.2 ± 0.1 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t251-ignis semilla 25100: sprint → red (0/1 vivos), normal → red (0/1 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 91 de 91 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t251-ignis: enemy:cataclysm.flame_strike=210 enemy:explosion.player=3 enemy:indirectMagic=2 enemy:mob=201 env:inFire=17 env:onFire=21
- t251-ignis-sync: enemy:cataclysm.flame_strike=264 enemy:explosion.player=1 enemy:mob=48 env:inFire=7 other(cataclysm:ignis):cataclysm.flame_strike=8
- t251-monst8: enemy:mob=3 env:onFire=1 other(cataclysm:ignis):cataclysm.flame_strike=3
- t251-monst8-sync: enemy:mob=3

## Motivos de decisión (cambios registrados): ENGAGE 3098, FORMATION_HOLD 1, LOW_HEALTH_RETREAT 2387, NONE 414, SEEK_ENEMY 2617, STUCK 100

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- MUERTES QUE NO SON DEL ENEMIGO: t251-monst8 - 57 % de las bajas azules ({enemy:mob=3.0, env:onFire=1.0, other(cataclysm:ignis):cataclysm.flame_strike=3.0})

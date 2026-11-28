# Simulación Rally - lote 20261128-063632-t246-jefes-sync

Preset `t246-jefes-sync` · velocidad `sprint` · 121 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t246-ignis | 30 | 28 % ± 7 | 72 % ± 7 | 1.3 ± 0.4 | 0.8 ± 0.1 | 234 ± 10 | 1962 ± 96 | 1009 ± 39 | 0 % ± 0 | 11.3 ± 0.3 | 10.2 ± 0.3 | 0 |
| t246-ignis-sync | 30 | 60 % ± 9 | 40 % ± 9 | 3.2 ± 0.5 | 0.4 ± 0.1 | 137 ± 9 | 1106 ± 78 | 546 ± 34 | 0 % ± 0 | 11.0 ± 0.4 | 9.9 ± 0.5 | 0 |
| t246-monst | 30 | 100 % ± 0 | 0 % ± 0 | 15.6 ± 0.1 | 0.0 ± 0.0 | 82 ± 1 | 608 ± 2 | 404 ± 7 | 0 % ± 0 | 20.7 ± 0.4 | 17.2 ± 0.3 | 0 |
| t246-monst-sync | 30 | 100 % ± 0 | 0 % ± 0 | 15.9 ± 0.0 | 0.0 ± 0.0 | 60 ± 1 | 603 ± 0 | 212 ± 7 | 0 % ± 0 | 20.8 ± 0.6 | 17.1 ± 0.5 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t246-ignis semilla 24800: sprint → red (0/1 vivos), normal → red (0/1 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 89 de 89 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t246-ignis: enemy:cataclysm.flame_strike=187 enemy:explosion.player=4 enemy:indirectMagic=3 enemy:mob=223 env:fall=2 env:inFire=5 env:onFire=17
- t246-ignis-sync: enemy:cataclysm.flame_strike=282 enemy:mob=82 env:inFire=10 env:onFire=3 other(cataclysm:ignis):cataclysm.flame_strike=6
- t246-monst: enemy:mob=7 other(cataclysm:ignis):cataclysm.flame_strike=5
- t246-monst-sync: enemy:mob=2

## Motivos de decisión (cambios registrados): ENGAGE 3501, FORMATION_HOLD 1, LOW_HEALTH_RETREAT 2445, NONE 1026, SEEK_ENEMY 3589, STUCK 124

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- MUERTES QUE NO SON DEL ENEMIGO: t246-monst - 42 % de las bajas azules ({enemy:mob=7.0, other(cataclysm:ignis):cataclysm.flame_strike=5.0})

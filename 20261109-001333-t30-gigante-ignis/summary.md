# Simulación Rally - lote 20261109-001333-t30-gigante-ignis

Preset `t30-gigante-ignis` · velocidad `sprint` · 25 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t30-gigante-ignis-hierro | 6 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 96 ± 10 | 183 ± 15 | 549 ± 16 | 87 % ± 2 | 17.1 ± 0.9 | 12.6 ± 0.9 | 0 |
| t30-gigante-ignis-diamante | 6 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 149 ± 8 | 249 ± 19 | 599 ± 46 | 87 % ± 2 | 22.3 ± 1.7 | 17.6 ± 1.5 | 0 |
| t30-gigante-ignis-netherita | 6 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 136 ± 5 | 280 ± 39 | 649 ± 25 | 81 % ± 3 | 17.4 ± 1.5 | 15.2 ± 1.4 | 0 |
| t30-gigante-ignis-netheritamas | 6 | 50 % ± 13 | 50 % ± 13 | 5.5 ± 1.5 | 0.8 ± 0.2 | 258 ± 35 | 933 ± 159 | 738 ± 89 | 86 % ± 2 | 24.8 ± 3.8 | 19.5 ± 2.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t30-gigante-ignis-hierro semilla 1: sprint → red (0/1 vivos), normal → red (0/1 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 5 de 5 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t30-gigante-ignis-hierro: enemy:mob=95 env:onFire=1
- t30-gigante-ignis-diamante: enemy:explosion.player=1 enemy:indirectMagic=1 enemy:mob=92 env:inFire=1 env:onFire=1
- t30-gigante-ignis-netherita: enemy:indirectMagic=1 enemy:mob=90 env:onFire=5
- t30-gigante-ignis-netheritamas: enemy:cataclysm.flame_strike=8 enemy:mob=51 env:onFire=4

## Motivos de decisión (cambios registrados): COUNTERATTACK 51, ENGAGE 948, LOW_HEALTH_RETREAT 146, MORALE_SHAKEN 480, MORALE_WAVERING 426, NONE 245, OUT_OF_RANGE 3, RALLIED 50, REFUSE_ATTACK 59, ROUTED_FLEE 398, SEEK_ENEMY 30, STUCK 6, TOO_CLOSE 73, UNIT_BROKEN 4

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t30-gigante-ignis-netheritamas: 4/6 corridas llegaron al tope de tiempo

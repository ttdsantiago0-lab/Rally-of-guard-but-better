# Simulación Rally - lote 20261108-202531-t28-jefe-luxtructosaurio

Preset `t28-jefe-luxtructosaurio` · velocidad `sprint` · 22 corridas · 3 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t28-jefe-luxtructosaurio-g16 | 6 | 25 % ± 11 | 75 % ± 11 | 2.7 ± 1.5 | 1.0 ± 0.0 | 234 ± 33 | 107 ± 17 | 583 ± 31 | 80 % ± 1 | 34.7 ± 4.4 | 23.4 ± 3.6 | 2 |
| t28-jefe-luxtructosaurio-g32 | 6 | 50 % ± 0 | 50 % ± 0 | 12.8 ± 1.7 | 1.0 ± 0.0 | 300 ± 0 | 186 ± 17 | 1021 ± 38 | 82 % ± 2 | 47.4 ± 1.5 | 35.5 ± 1.5 | 1 |
| t28-jefe-luxtructosaurio-g64 | 6 | 50 % ± 0 | 50 % ± 0 | 26.3 ± 4.6 | 1.0 ± 0.0 | 300 ± 0 | 228 ± 16 | 1939 ± 122 | 82 % ± 1 | 47.0 ± 2.0 | 34.9 ± 1.5 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t28-jefe-luxtructosaurio-g16 semilla 1: sprint → draw (2/1 vivos), normal → red (0/1 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 15 de 15 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t28-jefe-luxtructosaurio-g16: enemy:explosion.player=4 enemy:mob=65 env:onFire=11
- t28-jefe-luxtructosaurio-g32: enemy:explosion.player=15 enemy:mob=76 env:inFire=3 env:onFire=21
- t28-jefe-luxtructosaurio-g64: enemy:explosion.player=11 enemy:mob=188 env:inFire=3 env:onFire=24

## Motivos de decisión (cambios registrados): COUNTERATTACK 28, ENGAGE 2104, LOW_HEALTH_RETREAT 526, MORALE_SHAKEN 1091, MORALE_WAVERING 902, NONE 1279, OUT_OF_RANGE 141, RALLIED 94, REFUSE_ATTACK 11, ROUTED_FLEE 671, SEEK_ENEMY 623, STUCK 47, TOO_CLOSE 26, UNIT_BROKEN 2

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t28-jefe-luxtructosaurio-g16 - 2 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t28-jefe-luxtructosaurio-g32 - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- t28-jefe-luxtructosaurio-g32: 6/6 corridas llegaron al tope de tiempo
- t28-jefe-luxtructosaurio-g64: 6/6 corridas llegaron al tope de tiempo
- MUERTES QUE NO SON DEL ENEMIGO: t28-jefe-luxtructosaurio-g16 - 14 % de las bajas azules ({enemy:explosion.player=4.0, enemy:mob=65.0, env:onFire=11.0})
- MUERTES QUE NO SON DEL ENEMIGO: t28-jefe-luxtructosaurio-g32 - 21 % de las bajas azules ({enemy:explosion.player=15.0, enemy:mob=76.0, env:inFire=3.0, env:onFire=21.0})
- MUERTES QUE NO SON DEL ENEMIGO: t28-jefe-luxtructosaurio-g64 - 12 % de las bajas azules ({enemy:explosion.player=11.0, enemy:mob=188.0, env:inFire=3.0, env:onFire=24.0})

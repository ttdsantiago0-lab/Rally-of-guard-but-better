# Simulación Rally - lote 20261108-202729-t28-jefe-monstruosidad

Preset `t28-jefe-monstruosidad` · velocidad `sprint` · 21 corridas · 3 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t28-jefe-monstruosidad-g16 | 6 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 122 ± 19 | 265 ± 39 | 502 ± 33 | 47 % ± 6 | 24.4 ± 2.0 | 14.9 ± 1.8 | 0 |
| t28-jefe-monstruosidad-g32 | 6 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 218 ± 20 | 411 ± 43 | 1092 ± 66 | 49 % ± 4 | 27.4 ± 1.8 | 18.1 ± 2.3 | 1 |
| t28-jefe-monstruosidad-g64 | 6 | 67 % ± 11 | 33 % ± 11 | 8.2 ± 2.5 | 0.7 ± 0.2 | 255 ± 29 | 600 ± 44 | 1849 ± 32 | 50 % ± 5 | 33.6 ± 0.8 | 27.0 ± 1.0 | 1 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t28-jefe-monstruosidad-g16 semilla 1: sprint → red (0/1 vivos), normal → red (0/1 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 6 de 6 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t28-jefe-monstruosidad-g16: enemy:explosion.player=5 enemy:mob=77 env:lava=13 env:onFire=1
- t28-jefe-monstruosidad-g32: enemy:mob=150 env:lava=30 env:onFire=12
- t28-jefe-monstruosidad-g64: enemy:explosion.player=6 enemy:mob=278 env:lava=47 env:onFire=4

## Motivos de decisión (cambios registrados): COUNTERATTACK 20, ENGAGE 1305, FORMATION_HOLD 3, LOW_HEALTH_RETREAT 267, MORALE_SHAKEN 713, MORALE_WAVERING 436, NONE 624, OUT_OF_RANGE 27, RALLIED 65, REFUSE_ATTACK 50, ROUTED_FLEE 324, SEEK_ENEMY 206, STUCK 9, TOO_CLOSE 25, UNIT_BROKEN 1

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t28-jefe-monstruosidad-g64 - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t28-jefe-monstruosidad-g32 - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- t28-jefe-monstruosidad-g64: 4/6 corridas llegaron al tope de tiempo
- MUERTES QUE NO SON DEL ENEMIGO: t28-jefe-monstruosidad-g16 - 15 % de las bajas azules ({enemy:explosion.player=5.0, enemy:mob=77.0, env:lava=13.0, env:onFire=1.0})
- MUERTES QUE NO SON DEL ENEMIGO: t28-jefe-monstruosidad-g32 - 22 % de las bajas azules ({enemy:mob=150.0, env:lava=30.0, env:onFire=12.0})
- MUERTES QUE NO SON DEL ENEMIGO: t28-jefe-monstruosidad-g64 - 15 % de las bajas azules ({enemy:explosion.player=6.0, enemy:mob=278.0, env:lava=47.0, env:onFire=4.0})

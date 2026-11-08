# Simulación Rally - lote 20261108-204420-t28b-jefes-g8-b

Preset `t28b-jefes-g8-b` · velocidad `sprint` · 27 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t28b-jefe-harbinger-g8 | 6 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 97 ± 8 | 195 ± 12 | 266 ± 10 | 95 % ± 1 | 17.3 ± 0.9 | 12.1 ± 1.4 | 0 |
| t28b-jefe-monstruosidad-g8 | 6 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 61 ± 9 | 141 ± 20 | 235 ± 27 | 31 % ± 5 | 20.3 ± 0.7 | 7.2 ± 1.1 | 0 |
| t28b-jefe-guardianender-g8 | 6 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 57 ± 4 | 20 ± 4 | 227 ± 6 | 78 % ± 16 | 16.2 ± 0.9 | 8.3 ± 1.0 | 0 |
| t28b-jefe-warden-g8 | 4 | 100 % ± 0 | 0 % ± 0 | 5.0 ± 1.2 | 0.0 ± 0.0 | 109 ± 27 | 503 ± 1 | 215 ± 58 | 89 % ± 2 | 26.0 ± 4.0 | 18.9 ± 2.8 | 4 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t28b-jefe-harbinger-g8 semilla 1: sprint → red (0/1 vivos), normal → red (0/1 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 4 de 4 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t28b-jefe-harbinger-g8: enemy:cataclysm.deathlaser=12 enemy:cataclysm.laser=4 enemy:explosion.player=2 enemy:indirectMagic=5 enemy:mob=12 env:inFire=3 env:onFire=3 env:wither=7
- t28b-jefe-monstruosidad-g8: enemy:explosion.player=3 enemy:mob=32 env:lava=11 env:onFire=2
- t28b-jefe-guardianender-g8: enemy:explosion.player=1 enemy:indirectMagic=1 enemy:mob=46
- t28b-jefe-warden-g8: enemy:mob=11 enemy:sonic_boom=1

## Motivos de decisión (cambios registrados): COUNTERATTACK 12, ENGAGE 463, FORMATION_HOLD 2, LOW_HEALTH_RETREAT 65, MORALE_SHAKEN 266, MORALE_WAVERING 164, NONE 138, OUT_OF_RANGE 1, RALLIED 16, REFUSE_ATTACK 4, ROUTED_FLEE 132, SEEK_ENEMY 10, STUCK 21, TOO_CLOSE 27, UNIT_BROKEN 1

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t28b-jefe-warden-g8 - 4 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- MUERTES QUE NO SON DEL ENEMIGO: t28b-jefe-harbinger-g8 - 27 % de las bajas azules ({enemy:cataclysm.deathlaser=12.0, enemy:cataclysm.laser=4.0, enemy:explosion.player=2.0, enemy:indirectMagic=5.0, enemy:mob=12.0, env:inFire=3.0, env:onFire=3.0, env:wither=7.0})
- MUERTES QUE NO SON DEL ENEMIGO: t28b-jefe-monstruosidad-g8 - 27 % de las bajas azules ({enemy:explosion.player=3.0, enemy:mob=32.0, env:lava=11.0, env:onFire=2.0})

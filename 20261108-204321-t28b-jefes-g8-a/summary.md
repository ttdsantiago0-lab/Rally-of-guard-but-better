# Simulación Rally - lote 20261108-204321-t28b-jefes-g8-a

Preset `t28b-jefes-g8-a` · velocidad `sprint` · 25 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t28b-jefe-dragonfuego-g8 | 6 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 63 ± 10 | 94 ± 15 | 247 ± 13 | 56 % ± 4 | 20.6 ± 2.4 | 11.1 ± 2.1 | 0 |
| t28b-jefe-dragonhielo-g8 | 6 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 48 ± 9 | 83 ± 20 | 232 ± 10 | 65 % ± 5 | 17.5 ± 2.0 | 8.3 ± 1.0 | 0 |
| t28b-jefe-dragonrayo-g8 | 6 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 62 ± 4 | 77 ± 15 | 231 ± 4 | 58 % ± 8 | 18.3 ± 0.7 | 11.6 ± 0.6 | 0 |
| t28b-jefe-ignis-g8 | 6 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 58 ± 6 | 102 ± 17 | 255 ± 9 | 87 % ± 2 | 15.8 ± 1.7 | 10.3 ± 1.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t28b-jefe-dragonfuego-g8 semilla 1: sprint → red (0/1 vivos), normal → red (0/1 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Causas de muerte del bando azul (causa:tipo=bajas)
- t28b-jefe-dragonfuego-g8: enemy:dragon_fire=11 enemy:mob=31 env:inFire=4 env:onFire=2
- t28b-jefe-dragonhielo-g8: enemy:dragon_ice=27 enemy:mob=21
- t28b-jefe-dragonrayo-g8: enemy:dragon_lightning=15 enemy:mob=33
- t28b-jefe-ignis-g8: enemy:mob=47 env:onFire=1

## Motivos de decisión (cambios registrados): COUNTERATTACK 2, ENGAGE 423, LOW_HEALTH_RETREAT 58, MORALE_SHAKEN 250, MORALE_WAVERING 127, NONE 133, RALLIED 5, REFUSE_ATTACK 8, ROUTED_FLEE 73, SEEK_ENEMY 11, STUCK 4, TOO_CLOSE 29

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- MUERTES QUE NO SON DEL ENEMIGO: t28b-jefe-dragonfuego-g8 - 13 % de las bajas azules ({enemy:dragon_fire=11.0, enemy:mob=31.0, env:inFire=4.0, env:onFire=2.0})

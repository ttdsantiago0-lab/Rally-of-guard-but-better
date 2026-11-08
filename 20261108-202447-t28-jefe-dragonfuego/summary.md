# Simulación Rally - lote 20261108-202447-t28-jefe-dragonfuego

Preset `t28-jefe-dragonfuego` · velocidad `sprint` · 21 corridas · 3 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t28-jefe-dragonfuego-g16 | 6 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 106 ± 5 | 163 ± 22 | 464 ± 21 | 55 % ± 6 | 18.7 ± 1.6 | 11.6 ± 1.2 | 0 |
| t28-jefe-dragonfuego-g32 | 6 | 67 % ± 21 | 33 % ± 21 | 16.8 ± 6.0 | 0.3 ± 0.2 | 118 ± 32 | 345 ± 15 | 497 ± 168 | 67 % ± 2 | 26.4 ± 2.8 | 17.6 ± 2.0 | 1 |
| t28-jefe-dragonfuego-g64 | 6 | 92 % ± 8 | 8 % ± 8 | 46.8 ± 9.4 | 0.2 ± 0.2 | 101 ± 43 | 354 ± 15 | 569 ± 237 | 62 % ± 3 | 32.4 ± 2.8 | 21.6 ± 2.7 | 1 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t28-jefe-dragonfuego-g16 semilla 1: sprint → red (0/1 vivos), normal → red (0/1 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 10 de 10 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t28-jefe-dragonfuego-g16: enemy:dragon_fire=25 enemy:mob=64 env:inFire=5 env:onFire=2
- t28-jefe-dragonfuego-g32: enemy:dragon_fire=35 enemy:mob=50 env:inFire=6
- t28-jefe-dragonfuego-g64: enemy:dragon_fire=46 enemy:mob=37 env:inFire=12 env:onFire=8

## Motivos de decisión (cambios registrados): COUNTERATTACK 7, ENGAGE 1900, LOW_HEALTH_RETREAT 282, MORALE_SHAKEN 1071, MORALE_WAVERING 599, NONE 735, OUT_OF_RANGE 25, RALLIED 31, REFUSE_ATTACK 27, ROUTED_FLEE 382, SEEK_ENEMY 297, STUCK 35, TOO_CLOSE 91

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t28-jefe-dragonfuego-g32 - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t28-jefe-dragonfuego-g64 - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- MUERTES QUE NO SON DEL ENEMIGO: t28-jefe-dragonfuego-g64 - 19 % de las bajas azules ({enemy:dragon_fire=46.0, enemy:mob=37.0, env:inFire=12.0, env:onFire=8.0})

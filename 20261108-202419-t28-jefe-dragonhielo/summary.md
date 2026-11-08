# Simulación Rally - lote 20261108-202419-t28-jefe-dragonhielo

Preset `t28-jefe-dragonhielo` · velocidad `sprint` · 24 corridas · 3 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t28-jefe-dragonhielo-g16 | 6 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 97 ± 12 | 139 ± 11 | 484 ± 23 | 74 % ± 4 | 20.4 ± 1.7 | 14.9 ± 1.8 | 1 |
| t28-jefe-dragonhielo-g32 | 6 | 33 % ± 21 | 67 % ± 21 | 6.7 ± 4.3 | 0.7 ± 0.2 | 168 ± 19 | 271 ± 35 | 816 ± 130 | 65 % ± 5 | 29.6 ± 2.1 | 21.9 ± 2.1 | 1 |
| t28-jefe-dragonhielo-g64 | 5 | 80 % ± 20 | 20 % ± 20 | 40.2 ± 12.8 | 0.2 ± 0.2 | 105 ± 38 | 364 ± 5 | 795 ± 391 | 68 % ± 6 | 31.5 ± 3.2 | 20.9 ± 2.3 | 4 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t28-jefe-dragonhielo-g16 semilla 1: sprint → red (0/1 vivos), normal → red (0/1 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 6 de 6 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t28-jefe-dragonhielo-g16: enemy:dragon_ice=65 enemy:mob=29 env:cactus=2
- t28-jefe-dragonhielo-g32: enemy:dragon_ice=102 enemy:mob=48 env:cactus=2
- t28-jefe-dragonhielo-g64: enemy:dragon_ice=99 enemy:mob=19 env:cactus=1

## Motivos de decisión (cambios registrados): COUNTERATTACK 7, ENGAGE 2201, LOW_HEALTH_RETREAT 225, MORALE_SHAKEN 1354, MORALE_WAVERING 668, NONE 871, OUT_OF_RANGE 16, RALLIED 41, REFUSE_ATTACK 34, ROUTED_FLEE 527, SEEK_ENEMY 316, STUCK 137, TOO_CLOSE 147, UNIT_BROKEN 1

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t28-jefe-dragonhielo-g64 - 4 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t28-jefe-dragonhielo-g32 - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t28-jefe-dragonhielo-g16 - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)

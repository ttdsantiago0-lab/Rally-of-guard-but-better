# Simulación Rally - lote 20261108-202856-t28-jefe-dragonrayo

Preset `t28-jefe-dragonrayo` · velocidad `sprint` · 21 corridas · 3 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t28-jefe-dragonrayo-g16 | 6 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 120 ± 13 | 164 ± 23 | 542 ± 25 | 68 % ± 3 | 19.7 ± 1.9 | 12.3 ± 1.6 | 0 |
| t28-jefe-dragonrayo-g32 | 6 | 50 % ± 22 | 50 % ± 22 | 11.8 ± 5.4 | 0.5 ± 0.2 | 156 ± 27 | 341 ± 14 | 698 ± 182 | 63 % ± 4 | 24.5 ± 0.9 | 18.1 ± 0.8 | 1 |
| t28-jefe-dragonrayo-g64 | 6 | 100 % ± 0 | 0 % ± 0 | 56.5 ± 4.4 | 0.0 ± 0.0 | 52 ± 5 | 367 ± 1 | 343 ± 163 | 72 % ± 5 | 20.9 ± 0.6 | 13.5 ± 0.7 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t28-jefe-dragonrayo-g16 semilla 1: sprint → red (0/1 vivos), normal → draw (5/1 vivos)
- t28-jefe-dragonrayo-g16 semilla 7778: sprint → ? (0/0 vivos), normal → draw (13/1 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 9 de 9 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t28-jefe-dragonrayo-g16: enemy:dragon_lightning=52 enemy:mob=44
- t28-jefe-dragonrayo-g32: enemy:dragon_lightning=61 enemy:mob=60
- t28-jefe-dragonrayo-g64: enemy:dragon_lightning=39 enemy:mob=6

## Motivos de decisión (cambios registrados): COUNTERATTACK 27, ENGAGE 1720, LOW_HEALTH_RETREAT 133, MORALE_SHAKEN 941, MORALE_WAVERING 474, NONE 542, OUT_OF_RANGE 5, RALLIED 26, REFUSE_ATTACK 39, ROUTED_FLEE 331, SEEK_ENEMY 220, STUCK 23, TOO_CLOSE 153, UNIT_BROKEN 2

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t28-jefe-dragonrayo-g32 - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- t28-jefe-dragonrayo-g16: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)

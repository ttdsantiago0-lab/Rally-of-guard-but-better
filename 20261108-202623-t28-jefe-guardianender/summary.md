# Simulación Rally - lote 20261108-202623-t28-jefe-guardianender

Preset `t28-jefe-guardianender` · velocidad `sprint` · 19 corridas · 3 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t28-jefe-guardianender-g16 | 6 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 99 ± 14 | 22 ± 4 | 475 ± 8 | 90 % ± 2 | 20.4 ± 1.9 | 13.3 ± 1.5 | 0 |
| t28-jefe-guardianender-g32 | 6 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 240 ± 8 | 59 ± 7 | 998 ± 30 | 87 % ± 2 | 24.3 ± 1.0 | 19.2 ± 1.4 | 0 |
| t28-jefe-guardianender-g64 | 6 | 50 % ± 0 | 50 % ± 0 | 13.3 ± 1.4 | 1.0 ± 0.0 | 300 ± 0 | 122 ± 11 | 1641 ± 48 | 84 % ± 1 | 31.0 ± 0.8 | 24.5 ± 0.5 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t28-jefe-guardianender-g16 semilla 1: sprint → red (0/1 vivos), normal → red (0/1 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 6 de 6 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t28-jefe-guardianender-g16: enemy:explosion.player=5 enemy:indirectMagic=5 enemy:mob=86
- t28-jefe-guardianender-g32: enemy:explosion.player=5 enemy:indirectMagic=19 enemy:mob=166 other(cataclysm:ender_guardian):explosion.player=2
- t28-jefe-guardianender-g64: enemy:explosion.player=10 enemy:indirectMagic=11 enemy:mob=283

## Motivos de decisión (cambios registrados): COUNTERATTACK 18, ENGAGE 1287, FORMATION_HOLD 7, LOW_HEALTH_RETREAT 152, MORALE_SHAKEN 708, MORALE_WAVERING 521, NONE 540, OUT_OF_RANGE 7, RALLIED 59, REFUSE_ATTACK 86, ROUTED_FLEE 444, SEEK_ENEMY 188, STUCK 24, TOO_CLOSE 37

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t28-jefe-guardianender-g64: 6/6 corridas llegaron al tope de tiempo

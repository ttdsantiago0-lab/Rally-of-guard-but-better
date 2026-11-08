# Simulación Rally - lote 20261108-202846-t28-jefe-gusanovacio

Preset `t28-jefe-gusanovacio` · velocidad `sprint` · 27 corridas · 3 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t28-jefe-gusanovacio-g16 | 6 | 50 % ± 13 | 50 % ± 13 | 3.7 ± 1.3 | 1.2 ± 0.3 | 271 ± 27 | 98 ± 10 | 876 ± 55 | 25 % ± 4 | 32.8 ± 2.4 | 18.6 ± 2.3 | 4 |
| t28-jefe-gusanovacio-g32 | 6 | 92 % ± 8 | 8 % ± 8 | 25.0 ± 2.8 | 0.3 ± 0.3 | 143 ± 35 | 184 ± 14 | 677 ± 165 | 31 % ± 3 | 31.3 ± 0.7 | 17.8 ± 1.4 | 0 |
| t28-jefe-gusanovacio-g64 | 5 | 100 % ± 0 | 0 % ± 0 | 58.4 ± 1.9 | 0.0 ± 0.0 | 124 ± 30 | 234 ± 27 | 856 ± 202 | 25 % ± 2 | 33.8 ± 1.2 | 21.1 ± 0.7 | 5 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t28-jefe-gusanovacio-g16 semilla 1: sprint → draw (11/2 vivos), normal → draw (3/1 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 16 de 16 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t28-jefe-gusanovacio-g16: enemy:mob=74
- t28-jefe-gusanovacio-g32: enemy:mob=42
- t28-jefe-gusanovacio-g64: enemy:mob=28

## Motivos de decisión (cambios registrados): COUNTERATTACK 4, ENGAGE 3123, FORMATION_HOLD 54, LOW_HEALTH_RETREAT 182, MORALE_SHAKEN 1842, MORALE_WAVERING 999, NONE 1104, OUT_OF_RANGE 442, RALLIED 134, REFUSE_ATTACK 78, ROUTED_FLEE 914, SEEK_ENEMY 404, STUCK 20, TOO_CLOSE 59

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t28-jefe-gusanovacio-g16 - 4 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t28-jefe-gusanovacio-g64 - 5 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- t28-jefe-gusanovacio-g16: 4/6 corridas llegaron al tope de tiempo

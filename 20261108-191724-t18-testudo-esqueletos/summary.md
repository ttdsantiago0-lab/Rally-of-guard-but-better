# Simulación Rally - lote 20261108-191724-t18-testudo-esqueletos

Preset `t18-testudo-esqueletos` · velocidad `sprint` · 241 corridas · 8 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t18-testudo-esqueletos-escudo-base | 30 | 95 % ± 3 | 5 % ± 3 | 7.3 ± 0.2 | 0.2 ± 0.1 | 110 ± 6 | 311 ± 8 | 93 ± 6 | 0 % ± 0 | 12.4 ± 0.8 | 12.9 ± 0.7 | 0 |
| t18-testudo-esqueletos-escudo-marchalinea | 30 | 90 % ± 4 | 10 % ± 4 | 7.8 ± 0.1 | 0.2 ± 0.1 | 119 ± 7 | 284 ± 7 | 82 ± 4 | 0 % ± 0 | 13.0 ± 0.5 | 8.5 ± 0.3 | 0 |
| t18-testudo-esqueletos-escudo-testudo | 30 | 50 % ± 2 | 50 % ± 2 | 4.7 ± 0.4 | 2.7 ± 0.2 | 177 ± 2 | 60 ± 6 | 168 ± 7 | 0 % ± 0 | 8.0 ± 0.1 | 6.4 ± 0.5 | 0 |
| t18-testudo-esqueletos-escudo-romano | 30 | 65 % ± 4 | 35 % ± 4 | 7.9 ± 0.1 | 1.3 ± 0.2 | 162 ± 6 | 95 ± 4 | 48 ± 3 | 0 % ± 0 | 8.3 ± 0.2 | 5.3 ± 0.3 | 0 |
| t18-testudo-esqueletos-linea-base | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 37 ± 0 | 523 ± 6 | 28 ± 2 | 79 % ± 1 | 17.6 ± 0.1 | 9.7 ± 0.1 | 0 |
| t18-testudo-esqueletos-linea-marchalinea | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 37 ± 0 | 567 ± 5 | 48 ± 2 | 88 % ± 1 | 16.5 ± 0.1 | 6.7 ± 0.2 | 0 |
| t18-testudo-esqueletos-linea-testudo | 30 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 35 ± 0 | 520 ± 5 | 45 ± 2 | 91 % ± 1 | 15.8 ± 0.2 | 3.8 ± 0.1 | 0 |
| t18-testudo-esqueletos-linea-romano | 30 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 34 ± 0 | 519 ± 6 | 39 ± 2 | 91 % ± 1 | 15.9 ± 0.1 | 3.8 ± 0.1 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t18-testudo-esqueletos-escudo-base semilla 1: sprint → blue (8/0 vivos), normal → blue (5/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 239 de 239 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t18-testudo-esqueletos-escudo-base: enemy:arrow=21
- t18-testudo-esqueletos-escudo-marchalinea: enemy:arrow=5
- t18-testudo-esqueletos-escudo-testudo: enemy:arrow=100
- t18-testudo-esqueletos-escudo-romano: enemy:arrow=3
- t18-testudo-esqueletos-linea-marchalinea: enemy:arrow=1
- t18-testudo-esqueletos-linea-testudo: enemy:arrow=4
- t18-testudo-esqueletos-linea-romano: enemy:arrow=3

## Motivos de decisión (cambios registrados): COUNTERATTACK 95, ENGAGE 4713, FORMATION_HOLD 1021, LOW_HEALTH_RETREAT 495, MORALE_SHAKEN 1978, MORALE_WAVERING 558, NONE 58, OUT_OF_RANGE 4, RALLIED 28, REFUSE_ATTACK 90, ROUTED_FLEE 667, SEEK_ENEMY 4127, STUCK 947, TOO_CLOSE 11, UNIT_BROKEN 2

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t18-testudo-esqueletos-escudo-testudo: 28/30 corridas llegaron al tope de tiempo
- t18-testudo-esqueletos-escudo-romano: 21/30 corridas llegaron al tope de tiempo

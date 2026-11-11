# Simulación Rally - lote 20261111-215339-t48-sincro-ignis

Preset `t48-sincro-ignis` · velocidad `sprint` · 61 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t48-sincro-coord-ignis-g8-base-sincro | 30 | 100 % ± 0 | 0 % ± 0 | 3.5 ± 0.2 | 0.0 ± 0.0 | 92 ± 4 | 757 ± 39 | 270 ± 14 | 87 % ± 1 | 9.0 ± 0.2 | 7.7 ± 0.2 | 0 |
| t48-sincro-coord-ignis-g8-base-sinsincro | 30 | 93 % ± 5 | 7 % ± 5 | 2.7 ± 0.2 | 0.1 ± 0.0 | 91 ± 4 | 738 ± 33 | 282 ± 14 | 87 % ± 1 | 8.7 ± 0.1 | 7.6 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t48-sincro-coord-ignis-g8-base-sincro semilla 2001: sprint → blue (4/0 vivos), normal → red (0/1 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 58 de 58 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t48-sincro-coord-ignis-g8-base-sincro: enemy:cataclysm.flame_strike=79 enemy:mob=54 env:fall=1 env:inFire=1
- t48-sincro-coord-ignis-g8-base-sinsincro: enemy:cataclysm.flame_strike=90 enemy:mob=64 env:fall=3 env:inFire=2

## Motivos de decisión (cambios registrados): ENGAGE 951, FORMATION_HOLD 2, LOW_HEALTH_RETREAT 572, NONE 2, OUT_OF_RANGE 1, SEEK_ENEMY 292, STUCK 5, TOO_CLOSE 230

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t48-sincro-coord-ignis-g8-base-sincro: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)

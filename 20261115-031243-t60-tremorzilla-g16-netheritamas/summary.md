# Simulación Rally - lote 20261115-031243-t60-tremorzilla-g16-netheritamas

Preset `t60-tremorzilla-g16-netheritamas` · velocidad `sprint` · 37 corridas · 1 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t60-tremorzilla-g16-netheritamas | 30 | 28 % ± 8 | 72 % ± 8 | 3.2 ± 1.0 | 0.7 ± 0.1 | 117 ± 9 | 924 ± 63 | 336 ± 17 | 88 % ± 1 | 19.2 ± 0.6 | 13.1 ± 0.7 | 6 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t60-tremorzilla-g16-netheritamas semilla 6001: sprint → blue (15/0 vivos), normal → blue (15/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 9 de 9 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t60-tremorzilla-g16-netheritamas: enemy:mob=78 enemy:tremorzilla_beam=274 env:radiation=31

## Motivos de decisión (cambios registrados): ENGAGE 1504, FORMATION_HOLD 1, LOW_HEALTH_RETREAT 1438, NONE 1071, OUT_OF_RANGE 268, SEEK_ENEMY 1362, STUCK 68, TOO_CLOSE 65

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t60-tremorzilla-g16-netheritamas - 6 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)

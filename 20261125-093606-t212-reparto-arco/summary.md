# Simulación Rally - lote 20261125-093606-t212-reparto-arco

Preset `t212-reparto-arco` · velocidad `sprint` · 62 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t212-rep-arco-capa | 30 | 60 % ± 9 | 40 % ± 9 | 2.4 ± 0.4 | 1.8 ± 0.5 | 36 ± 7 | 177 ± 4 | 170 ± 4 | 76 % ± 4 | 39.3 ± 1.8 | 7.6 ± 0.8 | 0 |
| t212-rep-arco-indiv | 30 | 43 % ± 9 | 57 % ± 9 | 1.9 ± 0.4 | 2.3 ± 0.4 | 37 ± 7 | 171 ± 4 | 176 ± 5 | 75 % ± 5 | 38.9 ± 1.8 | 7.4 ± 0.8 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t212-rep-arco-capa semilla 12300: sprint → red (0/7 vivos), normal → draw (2/4 vivos)
- t212-rep-arco-capa semilla 20077: sprint → ? (0/0 vivos), normal → blue (5/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 31 de 31 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t212-rep-arco-capa: enemy:arrow=169
- t212-rep-arco-indiv: enemy:arrow=183

## Motivos de decisión (cambios registrados): ENGAGE 1121, LOW_HEALTH_RETREAT 1727, NONE 1039, OUT_OF_RANGE 576, SEEK_ENEMY 16

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t212-rep-arco-capa: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)

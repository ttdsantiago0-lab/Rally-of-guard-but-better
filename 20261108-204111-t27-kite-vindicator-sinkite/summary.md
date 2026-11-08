# Simulación Rally - lote 20261108-204111-t27-kite-vindicator-sinkite

Preset `t27-kite-vindicator-sinkite` · velocidad `sprint` · 41 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t27-kite-vindicator-arco-sinkite-r8 | 20 | 100 % ± 0 | 0 % ± 0 | 7.6 ± 0.2 | 0.0 ± 0.0 | 8 ± 0 | 216 ± 1 | 21 ± 5 | 78 % ± 1 | 9.4 ± 0.3 | 5.2 ± 0.1 | 0 |
| t27-kite-vindicator-linea-sinkite-r12 | 20 | 5 % ± 5 | 95 % ± 5 | 0.1 ± 0.0 | 5.7 ± 0.5 | 15 ± 1 | 242 ± 8 | 199 ± 1 | 98 % ± 1 | 5.0 ± 0.2 | 3.5 ± 0.1 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t27-kite-vindicator-arco-sinkite-r8 semilla 1001: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 21 de 21 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t27-kite-vindicator-arco-sinkite-r8: enemy:mob=9
- t27-kite-vindicator-linea-sinkite-r12: enemy:mob=159

## Motivos de decisión (cambios registrados): ENGAGE 407, LOW_HEALTH_RETREAT 71, MORALE_SHAKEN 136, MORALE_WAVERING 1, REFUSE_ATTACK 1, SEEK_ENEMY 6, TOO_CLOSE 165

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.

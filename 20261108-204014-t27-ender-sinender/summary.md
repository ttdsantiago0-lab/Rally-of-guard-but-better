# Simulación Rally - lote 20261108-204014-t27-ender-sinender

Preset `t27-ender-sinender` · velocidad `sprint` · 81 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t27-ender-linea-sinender-r3 | 20 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 3.0 ± 0.0 | 80 ± 6 | 0 ± 0 | 185 ± 1 | 0 % ± 0 | 17.9 ± 1.7 | 16.1 ± 0.9 | 0 |
| t27-ender-linea-sinender-r6 | 20 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 6.0 ± 0.0 | 51 ± 5 | 0 ± 0 | 186 ± 1 | 0 % ± 0 | 8.4 ± 0.5 | 17.9 ± 1.5 | 0 |
| t27-ender-escudo-sinender-r3 | 20 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 3.0 ± 0.0 | 36 ± 1 | 0 ± 0 | 181 ± 0 | 0 % ± 0 | 9.3 ± 0.6 | 7.9 ± 0.4 | 0 |
| t27-ender-arco-sinender-r3 | 20 | 23 % ± 6 | 78 % ± 6 | 0.6 ± 0.2 | 3.0 ± 0.0 | 162 ± 6 | 0 ± 0 | 180 ± 3 | 0 % ± 0 | 30.2 ± 1.5 | 21.4 ± 0.8 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t27-ender-linea-sinender-r3 semilla 1001: sprint → red (0/3 vivos), normal → red (0/3 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 9 de 9 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t27-ender-linea-sinender-r3: enemy:mob=160
- t27-ender-linea-sinender-r6: enemy:mob=160
- t27-ender-escudo-sinender-r3: enemy:mob=160
- t27-ender-arco-sinender-r3: enemy:mob=149

## Motivos de decisión (cambios registrados): COUNTERATTACK 28, MORALE_SHAKEN 638, MORALE_WAVERING 167, NONE 817, ROUTED_FLEE 93, SEEK_ENEMY 1514, STUCK 4, UNIT_BROKEN 5

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.

# Simulación Rally - lote 20261122-084230-t121-persecucion

Preset `t121-persecucion` · velocidad `sprint` · 157 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t121-persecucion-pie-fuga | 25 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 11.4 ± 0.2 | 12 ± 0 | 116 ± 4 | 192 ± 0 | 0 % ± 0 | 3.5 ± 0.0 | 4.1 ± 0.1 | 15 |
| t121-persecucion-pie-sinfuga | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 11.3 ± 0.2 | 12 ± 0 | 120 ± 2 | 192 ± 0 | 0 % ± 0 | 3.5 ± 0.0 | 3.9 ± 0.1 | 0 |
| t121-persecucion-jinete-sinfuga | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 10.1 ± 0.3 | 15 ± 0 | 171 ± 3 | 192 ± 0 | 0 % ± 0 | 3.7 ± 0.0 | 4.5 ± 0.1 | 0 |
| t121-persecucion-jinete-fuga | 6 | 50 % ± 13 | 50 % ± 13 | 1.8 ± 0.7 | 6.5 ± 2.0 | 100 ± 18 | 253 ± 28 | 192 ± 3 | 0 % ± 0 | 23.9 ± 4.5 | 12.7 ± 2.5 | 49 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t121-persecucion-pie-fuga semilla 11300: sprint → red (0/12 vivos), normal → draw (1/12 vivos)
- t121-persecucion-pie-fuga semilla 19077: sprint → ? (0/0 vivos), normal → red (0/12 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 5 de 5 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t121-persecucion-pie-fuga: enemy:mob=200
- t121-persecucion-pie-sinfuga: enemy:mob=240
- t121-persecucion-jinete-sinfuga: enemy:mob=240
- t121-persecucion-jinete-fuga: enemy:mob=37

## Motivos de decisión (cambios registrados): ENGAGE 3290, FORMATION_HOLD 826, LOW_HEALTH_RETREAT 3739, NONE 821, SEEK_ENEMY 2254, STUCK 11

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t121-persecucion-jinete-fuga - 49 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t121-persecucion-pie-fuga - 15 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- t121-persecucion-jinete-fuga: 4/6 corridas llegaron al tope de tiempo
- t121-persecucion-pie-fuga: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)

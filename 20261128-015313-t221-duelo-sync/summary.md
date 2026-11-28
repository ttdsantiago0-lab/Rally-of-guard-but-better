# Simulación Rally - lote 20261128-015313-t221-duelo-sync

Preset `t221-duelo-sync` · velocidad `sprint` · 151 corridas · 5 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t221-espejo | 30 | 50 % ± 9 | 50 % ± 9 | 1.8 ± 0.4 | 1.8 ± 0.4 | 13 ± 0 | 182 ± 2 | 182 ± 2 | 0 % ± 0 | 4.2 ± 0.1 | 5.0 ± 0.1 | 0 |
| t221-capa-solo | 30 | 53 % ± 9 | 47 % ± 9 | 2.0 ± 0.4 | 1.8 ± 0.4 | 12 ± 0 | 181 ± 3 | 180 ± 3 | 0 % ± 0 | 4.2 ± 0.1 | 5.1 ± 0.1 | 0 |
| t221-sync-solo | 30 | 60 % ± 9 | 40 % ± 9 | 2.4 ± 0.5 | 1.3 ± 0.4 | 13 ± 0 | 185 ± 2 | 179 ± 2 | 0 % ± 0 | 4.2 ± 0.1 | 5.0 ± 0.1 | 0 |
| t221-sync-sync | 30 | 40 % ± 9 | 60 % ± 9 | 1.3 ± 0.3 | 2.1 ± 0.4 | 13 ± 0 | 180 ± 3 | 185 ± 2 | 0 % ± 0 | 4.1 ± 0.1 | 4.8 ± 0.1 | 0 |
| t221-16-sync-solo | 30 | 60 % ± 9 | 40 % ± 9 | 3.8 ± 0.7 | 2.4 ± 0.6 | 14 ± 0 | 370 ± 4 | 362 ± 4 | 0 % ± 0 | 4.6 ± 0.1 | 8.9 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t221-espejo semilla 19400: sprint → blue (1/0 vivos), normal → red (0/2 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 79 de 79 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t221-espejo: enemy:mob=187
- t221-capa-solo: enemy:mob=179
- t221-sync-solo: enemy:mob=168
- t221-sync-sync: enemy:mob=202
- t221-16-sync-solo: enemy:mob=366

## Motivos de decisión (cambios registrados): ENGAGE 2905, NONE 3, SEEK_ENEMY 42

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t221-espejo: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)

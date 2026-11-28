# Simulación Rally - lote 20261128-030141-t227-sesgo

Preset `t227-sesgo` · velocidad `sprint` · 91 corridas · 3 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t227-gv-gv | 30 | 53 % ± 9 | 47 % ± 9 | 1.5 ± 0.3 | 1.5 ± 0.4 | 17 ± 1 | 175 ± 4 | 177 ± 3 | 0 % ± 0 | 3.2 ± 0.1 | 3.5 ± 0.2 | 0 |
| t227-sync-gv | 30 | 80 % ± 7 | 20 % ± 7 | 3.0 ± 0.4 | 0.3 ± 0.1 | 15 ± 0 | 189 ± 2 | 159 ± 4 | 0 % ± 0 | 3.6 ± 0.1 | 4.4 ± 0.3 | 0 |
| t227-gv-sync | 30 | 20 % ± 7 | 80 % ± 7 | 0.5 ± 0.2 | 3.6 ± 0.5 | 14 ± 0 | 157 ± 5 | 187 ± 2 | 0 % ± 0 | 3.7 ± 0.1 | 4.5 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t227-gv-gv semilla 21000: sprint → red (0/4 vivos), normal → blue (1/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 24 de 24 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t227-gv-gv: enemy:mob=195
- t227-sync-gv: enemy:mob=151
- t227-gv-sync: enemy:mob=224

## Motivos de decisión (cambios registrados): ENGAGE 511, NONE 45

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t227-gv-gv: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)

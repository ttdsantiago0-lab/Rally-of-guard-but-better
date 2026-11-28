# Simulación Rally - lote 20261128-081118-t253-sesgo-escala

Preset `t253-sesgo-escala` · velocidad `sprint` · 181 corridas · 6 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t253-32-gv-gv | 30 | 50 % ± 9 | 50 % ± 9 | 2.9 ± 0.7 | 3.2 ± 0.8 | 24 ± 1 | 733 ± 9 | 736 ± 8 | 0 % ± 0 | 3.4 ± 0.1 | 7.7 ± 0.4 | 0 |
| t253-32-sync-gv | 30 | 100 % ± 0 | 0 % ± 0 | 17.6 ± 0.6 | 0.0 ± 0.0 | 16 ± 0 | 768 ± 0 | 558 ± 9 | 0 % ± 0 | 4.7 ± 0.2 | 9.8 ± 0.2 | 0 |
| t253-32-gv-sync | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 16.4 ± 0.6 | 17 ± 0 | 566 ± 8 | 768 ± 0 | 0 % ± 0 | 3.4 ± 0.0 | 8.7 ± 0.3 | 0 |
| t253-64-gv-gv | 30 | 50 % ± 9 | 50 % ± 9 | 5.8 ± 1.4 | 5.1 ± 1.2 | 29 ± 1 | 1484 ± 12 | 1471 ± 16 | 0 % ± 0 | 3.7 ± 0.1 | 7.3 ± 0.4 | 0 |
| t253-64-sync-gv | 30 | 100 % ± 0 | 0 % ± 0 | 31.0 ± 0.9 | 0.0 ± 0.0 | 19 ± 0 | 1536 ± 0 | 1146 ± 14 | 0 % ± 0 | 5.7 ± 0.2 | 10.9 ± 0.2 | 0 |
| t253-64-gv-sync | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 32.3 ± 1.1 | 18 ± 0 | 1130 ± 15 | 1536 ± 0 | 0 % ± 0 | 3.5 ± 0.0 | 9.1 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t253-32-gv-gv semilla 25300: sprint → blue (10/0 vivos), normal → red (0/3 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 60 de 60 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t253-32-gv-gv: enemy:mob=872
- t253-32-sync-gv: enemy:mob=431
- t253-32-gv-sync: enemy:mob=960
- t253-64-gv-gv: enemy:mob=1745
- t253-64-sync-gv: enemy:mob=991
- t253-64-gv-sync: enemy:mob=1920

## Motivos de decisión (cambios registrados): ENGAGE 6403, NONE 2040

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t253-32-gv-gv: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)

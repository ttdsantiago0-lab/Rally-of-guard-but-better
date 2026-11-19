# Simulación Rally - lote 20261119-072644-t92-refugio-linea

Preset `t92-refugio-linea` · velocidad `sprint` · 181 corridas · 6 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t92-vindicadores80-pelear | 30 | 37 % ± 7 | 63 % ± 7 | 1.0 ± 0.2 | 24.7 ± 3.2 | 105 ± 4 | 1580 ± 62 | 193 ± 5 | 90 % ± 1 | 13.8 ± 1.2 | 18.8 ± 2.3 | 0 |
| t92-vindicadores80-casa | 30 | 50 % ± 0 | 50 % ± 0 | 5.0 ± 0.2 | 79.5 ± 0.2 | 120 ± 0 | 27 ± 3 | 87 ± 6 | 43 % ± 9 | 6.2 ± 0.3 | 15.0 ± 1.1 | 0 |
| t92-vindicadores80-casa-compacta | 30 | 50 % ± 0 | 50 % ± 0 | 4.8 ± 0.3 | 79.5 ± 0.3 | 120 ± 0 | 33 ± 5 | 97 ± 10 | 39 % ± 9 | 5.3 ± 0.2 | 14.1 ± 1.8 | 0 |
| t92-devastadores8-pelear | 30 | 100 % ± 0 | 0 % ± 0 | 4.8 ± 0.2 | 0.0 ± 0.0 | 37 ± 1 | 826 ± 1 | 118 ± 7 | 93 % ± 1 | 12.6 ± 0.9 | 16.3 ± 0.8 | 0 |
| t92-devastadores8-casa | 30 | 50 % ± 0 | 50 % ± 0 | 5.1 ± 0.3 | 8.0 ± 0.0 | 120 ± 0 | 37 ± 5 | 95 ± 8 | 13 % ± 6 | 6.4 ± 0.2 | 15.7 ± 1.6 | 0 |
| t92-devastadores8-casa-compacta | 30 | 50 % ± 0 | 50 % ± 0 | 4.9 ± 0.3 | 8.0 ± 0.0 | 120 ± 0 | 39 ± 4 | 98 ± 10 | 20 % ± 7 | 6.1 ± 0.8 | 12.8 ± 1.4 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t92-vindicadores80-pelear semilla 9100: sprint → red (0/37 vivos), normal → draw (1/20 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 167 de 167 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t92-vindicadores80-pelear: enemy:mob=203 enemy:scguns.bullet.annihilated=4 enemy:scguns.bullet.decimated=1 enemy:scguns.bullet.eliminated=1 enemy:scguns.bullet.executed=1 enemy:scguns.bullet.killed=1
- t92-vindicadores80-casa: enemy:mob=84 enemy:scguns.bullet.annihilated=1 enemy:scguns.bullet.eliminated=3 enemy:scguns.bullet.killed=2
- t92-vindicadores80-casa-compacta: enemy:mob=86 enemy:scguns.bullet.annihilated=2 enemy:scguns.bullet.decimated=2 enemy:scguns.bullet.eliminated=2 enemy:scguns.bullet.executed=3 enemy:scguns.bullet.killed=1
- t92-devastadores8-pelear: enemy:mob=97
- t92-devastadores8-casa: enemy:mob=87
- t92-devastadores8-casa-compacta: enemy:mob=93

## Motivos de decisión (cambios registrados): ENGAGE 724, INSTRUCTION 960, LOW_HEALTH_RETREAT 155, NONE 21, ORDER_OVERRIDE 967, OUT_OF_RANGE 162, SEEK_ENEMY 88, STUCK 9, TOO_CLOSE 168

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t92-vindicadores80-casa: 30/30 corridas llegaron al tope de tiempo
- t92-vindicadores80-casa-compacta: 30/30 corridas llegaron al tope de tiempo
- t92-devastadores8-casa: 30/30 corridas llegaron al tope de tiempo
- t92-devastadores8-casa-compacta: 30/30 corridas llegaron al tope de tiempo
- t92-vindicadores80-pelear: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)

# Simulación Rally - lote 20261120-050957-t107-refugio-margen

Preset `t107-refugio-margen` · velocidad `sprint` · 241 corridas · 8 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t107-d30-pelear | 30 | 35 % ± 6 | 65 % ± 6 | 1.1 ± 0.2 | 21.8 ± 3.0 | 103 ± 5 | 1644 ± 61 | 189 ± 5 | 89 % ± 2 | 15.3 ± 1.7 | 20.7 ± 2.5 | 0 |
| t107-d30-casa-0 | 30 | 45 % ± 3 | 55 % ± 3 | 4.7 ± 0.4 | 79.6 ± 0.1 | 114 ± 4 | 26 ± 3 | 92 ± 11 | 33 % ± 9 | 6.8 ± 0.5 | 13.3 ± 1.8 | 0 |
| t107-d45-pelear | 30 | 32 % ± 6 | 68 % ± 6 | 0.9 ± 0.2 | 28.7 ± 4.0 | 95 ± 6 | 1537 ± 83 | 193 ± 5 | 92 % ± 1 | 14.3 ± 1.4 | 21.7 ± 2.3 | 0 |
| t107-d45-casa-0 | 30 | 47 % ± 2 | 53 % ± 2 | 4.9 ± 0.3 | 79.8 ± 0.1 | 119 ± 1 | 28 ± 4 | 84 ± 8 | 45 % ± 9 | 7.9 ± 0.4 | 13.9 ± 1.6 | 0 |
| t107-d45-casa-3 | 30 | 50 % ± 0 | 50 % ± 0 | 4.5 ± 0.2 | 79.7 ± 0.1 | 120 ± 0 | 95 ± 6 | 103 ± 6 | 94 % ± 1 | 6.6 ± 0.2 | 18.1 ± 1.6 | 0 |
| t107-d45-casa-6 | 30 | 50 % ± 0 | 50 % ± 0 | 3.8 ± 0.3 | 78.9 ± 0.2 | 120 ± 0 | 203 ± 5 | 127 ± 9 | 98 % ± 1 | 6.6 ± 0.2 | 20.8 ± 1.5 | 0 |
| t107-d60-casa-6 | 30 | 50 % ± 0 | 50 % ± 0 | 4.3 ± 0.3 | 79.3 ± 0.2 | 120 ± 0 | 97 ± 6 | 113 ± 7 | 99 % ± 0 | 8.7 ± 0.3 | 20.1 ± 1.4 | 0 |
| t107-d60-casa-9 | 30 | 48 % ± 2 | 52 % ± 2 | 3.9 ± 0.3 | 79.2 ± 0.2 | 119 ± 1 | 182 ± 6 | 123 ± 8 | 100 % ± 0 | 8.4 ± 0.3 | 22.5 ± 1.8 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t107-d30-pelear semilla 10000: sprint → draw (1/25 vivos), normal → red (0/25 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 208 de 208 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t107-d30-pelear: enemy:mob=203 enemy:scguns.bullet.annihilated=1 enemy:scguns.bullet.decimated=1 enemy:scguns.bullet.executed=1 enemy:scguns.bullet.killed=1
- t107-d30-casa-0: enemy:mob=94 enemy:scguns.bullet.annihilated=1 enemy:scguns.bullet.decimated=1 enemy:scguns.bullet.eliminated=1 enemy:scguns.bullet.killed=2
- t107-d45-pelear: enemy:mob=205 enemy:scguns.bullet.annihilated=2 enemy:scguns.bullet.decimated=3 enemy:scguns.bullet.eliminated=1 enemy:scguns.bullet.executed=1 enemy:scguns.bullet.killed=1
- t107-d45-casa-0: enemy:mob=86 enemy:scguns.bullet.annihilated=2 enemy:scguns.bullet.eliminated=4
- t107-d45-casa-3: enemy:mob=93 enemy:scguns.bullet.annihilated=2 enemy:scguns.bullet.decimated=3 enemy:scguns.bullet.eliminated=3 enemy:scguns.bullet.killed=4
- t107-d45-casa-6: enemy:mob=113 enemy:scguns.bullet.annihilated=1 enemy:scguns.bullet.decimated=4 enemy:scguns.bullet.eliminated=1 enemy:scguns.bullet.executed=5 enemy:scguns.bullet.killed=1
- t107-d60-casa-6: enemy:mob=94 enemy:scguns.bullet.annihilated=3 enemy:scguns.bullet.eliminated=4 enemy:scguns.bullet.executed=4 enemy:scguns.bullet.killed=5
- t107-d60-casa-9: enemy:mob=116 enemy:scguns.bullet.annihilated=1 enemy:scguns.bullet.decimated=3 enemy:scguns.bullet.eliminated=2 enemy:scguns.bullet.executed=2

## Motivos de decisión (cambios registrados): ENGAGE 1501, INSTRUCTION 1440, LOW_HEALTH_RETREAT 43, NONE 12, ORDER_OVERRIDE 1468, OUT_OF_RANGE 230, SEEK_ENEMY 34, STUCK 31, TOO_CLOSE 274, UNIT_BROKEN 3

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t107-d30-casa-0: 27/30 corridas llegaron al tope de tiempo
- t107-d45-casa-0: 28/30 corridas llegaron al tope de tiempo
- t107-d45-casa-3: 30/30 corridas llegaron al tope de tiempo
- t107-d45-casa-6: 30/30 corridas llegaron al tope de tiempo
- t107-d60-casa-6: 30/30 corridas llegaron al tope de tiempo
- t107-d60-casa-9: 29/30 corridas llegaron al tope de tiempo
- t107-d30-pelear: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)

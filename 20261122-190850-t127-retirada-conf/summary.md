# Simulación Rally - lote 20261122-190850-t127-retirada-conf

Preset `t127-retirada-conf` · velocidad `sprint` · 181 corridas · 6 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t127-d45-pelear | 30 | 18 % ± 5 | 82 % ± 5 | 0.4 ± 0.1 | 34.7 ± 3.1 | 90 ± 5 | 1423 ± 65 | 206 ± 3 | 95 % ± 1 | 12.3 ± 0.9 | 18.1 ± 2.0 | 0 |
| t127-d45-casa-0 | 30 | 48 % ± 2 | 52 % ± 2 | 5.0 ± 0.3 | 79.8 ± 0.1 | 117 ± 3 | 31 ± 3 | 87 ± 7 | 40 % ± 9 | 8.2 ± 0.5 | 13.3 ± 1.6 | 0 |
| t127-d45-dano | 30 | 48 % ± 2 | 52 % ± 2 | 2.5 ± 0.3 | 77.8 ± 0.3 | 117 ± 3 | 237 ± 8 | 168 ± 6 | 98 % ± 1 | 8.1 ± 0.3 | 20.7 ± 2.3 | 0 |
| t127-d45-early-4 | 30 | 50 % ± 0 | 50 % ± 0 | 4.5 ± 0.2 | 79.7 ± 0.1 | 120 ± 0 | 54 ± 6 | 105 ± 8 | 79 % ± 7 | 7.0 ± 0.2 | 16.4 ± 1.8 | 0 |
| t127-r16-espera | 30 | 100 % ± 0 | 0 % ± 0 | 6.1 ± 0.2 | 0.0 ± 0.0 | 17 ± 0 | 415 ± 5 | 79 ± 4 | 77 % ± 2 | 13.0 ± 0.7 | 9.8 ± 0.5 | 0 |
| t127-r16-early-4 | 30 | 100 % ± 0 | 0 % ± 0 | 6.1 ± 0.2 | 0.0 ± 0.0 | 17 ± 1 | 414 ± 4 | 78 ± 5 | 77 % ± 3 | 12.8 ± 0.6 | 9.9 ± 0.4 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t127-d45-pelear semilla 12000: sprint → red (0/40 vivos), normal → draw (1/31 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 158 de 158 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t127-d45-pelear: enemy:mob=221 enemy:scguns.bullet.decimated=3 enemy:scguns.bullet.executed=2 enemy:scguns.bullet.killed=2
- t127-d45-casa-0: enemy:mob=84 enemy:scguns.bullet.decimated=1 enemy:scguns.bullet.eliminated=2 enemy:scguns.bullet.killed=3
- t127-d45-dano: enemy:mob=158 enemy:scguns.bullet.annihilated=1 enemy:scguns.bullet.decimated=2 enemy:scguns.bullet.eliminated=2 enemy:scguns.bullet.executed=2 enemy:scguns.bullet.killed=1
- t127-d45-early-4: enemy:mob=100 enemy:scguns.bullet.decimated=1 enemy:scguns.bullet.eliminated=1 enemy:scguns.bullet.executed=1 enemy:scguns.bullet.killed=2
- t127-r16-espera: enemy:mob=54 enemy:scguns.bullet.annihilated=1 enemy:scguns.bullet.decimated=2
- t127-r16-early-4: enemy:mob=56 enemy:scguns.bullet.decimated=1 enemy:scguns.bullet.eliminated=1

## Motivos de decisión (cambios registrados): ENGAGE 1205, FORMATION_HOLD 40, INSTRUCTION 718, LOW_HEALTH_RETREAT 78, NONE 2, ORDER_OVERRIDE 740, OUT_OF_RANGE 196, SEEK_ENEMY 56, STUCK 25, TOO_CLOSE 134

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t127-d45-casa-0: 29/30 corridas llegaron al tope de tiempo
- t127-d45-dano: 29/30 corridas llegaron al tope de tiempo
- t127-d45-early-4: 30/30 corridas llegaron al tope de tiempo
- t127-d45-pelear: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)

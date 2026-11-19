# Simulación Rally - lote 20261119-083055-t97-refugio-conf

Preset `t97-refugio-conf` · velocidad `sprint` · 91 corridas · 3 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t97-pelear | 30 | 37 % ± 6 | 63 % ± 6 | 1.1 ± 0.2 | 24.4 ± 3.0 | 101 ± 5 | 1600 ± 62 | 186 ± 5 | 88 % ± 2 | 17.0 ± 1.5 | 22.0 ± 2.1 | 0 |
| t97-casa | 30 | 50 % ± 0 | 50 % ± 0 | 4.9 ± 0.3 | 79.6 ± 0.1 | 120 ± 0 | 29 ± 4 | 87 ± 8 | 33 % ± 9 | 6.8 ± 0.4 | 14.2 ± 1.6 | 0 |
| t97-casa-tarde | 30 | 42 % ± 3 | 58 % ± 3 | 1.6 ± 0.2 | 68.7 ± 0.6 | 113 ± 4 | 488 ± 13 | 193 ± 5 | 81 % ± 2 | 7.6 ± 0.5 | 27.0 ± 2.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t97-pelear semilla 9500: sprint → red (0/37 vivos), normal → draw (1/18 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 73 de 73 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t97-pelear: enemy:mob=195 enemy:scguns.bullet.annihilated=3 enemy:scguns.bullet.decimated=3 enemy:scguns.bullet.eliminated=1 enemy:scguns.bullet.executed=1 enemy:scguns.bullet.killed=3
- t97-casa: enemy:mob=85 enemy:scguns.bullet.annihilated=3 enemy:scguns.bullet.decimated=1 enemy:scguns.bullet.eliminated=2 enemy:scguns.bullet.killed=1
- t97-casa-tarde: enemy:mob=179 enemy:scguns.bullet.annihilated=2 enemy:scguns.bullet.decimated=3 enemy:scguns.bullet.eliminated=5 enemy:scguns.bullet.executed=2 enemy:scguns.bullet.killed=1

## Motivos de decisión (cambios registrados): ENGAGE 738, INSTRUCTION 358, LOW_HEALTH_RETREAT 42, NONE 8, ORDER_OVERRIDE 368, OUT_OF_RANGE 161, SEEK_ENEMY 17, STUCK 15, TOO_CLOSE 185

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t97-casa: 30/30 corridas llegaron al tope de tiempo
- t97-casa-tarde: 25/30 corridas llegaron al tope de tiempo
- t97-pelear: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)

# Simulación Rally - lote 20261116-211426-t48-distancia-pillagerpax32-flat

Preset `t48-distancia-pillagerpax32-flat` · velocidad `sprint` · 181 corridas · 6 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t48-distancia-pillagerpax32-flat-arco-regla | 30 | 77 % ± 8 | 23 % ± 8 | 2.5 ± 0.4 | 2.0 ± 0.8 | 36 ± 1 | 760 ± 14 | 197 ± 10 | 73 % ± 1 | 38.9 ± 0.3 | 13.2 ± 1.0 | 0 |
| t48-distancia-pillagerpax32-flat-arco-sinregla | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 24.6 ± 0.5 | 7 ± 0 | 230 ± 5 | 198 ± 2 | 91 % ± 1 | 27.7 ± 0.1 | 3.7 ± 0.2 | 0 |
| t48-distancia-pillagerpax32-flat-linea-regla | 30 | 97 % ± 3 | 3 % ± 3 | 6.0 ± 0.3 | 0.3 ± 0.3 | 26 ± 1 | 352 ± 9 | 71 ± 7 | 65 % ± 1 | 31.5 ± 0.5 | 16.1 ± 0.6 | 0 |
| t48-distancia-pillagerpax32-flat-linea-sinregla | 30 | 33 % ± 9 | 67 % ± 9 | 1.3 ± 0.4 | 4.3 ± 0.8 | 25 ± 1 | 196 ± 8 | 197 ± 6 | 92 % ± 1 | 16.4 ± 0.6 | 3.9 ± 0.3 | 0 |
| t48-distancia-pillagerpax32-flat-lineascutum-regla | 30 | 97 % ± 3 | 3 % ± 3 | 6.7 ± 0.4 | 0.1 ± 0.1 | 26 ± 1 | 359 ± 11 | 58 ± 9 | 67 % ± 1 | 31.1 ± 0.4 | 16.8 ± 0.6 | 0 |
| t48-distancia-pillagerpax32-flat-lineascutum-sinregla | 30 | 87 % ± 6 | 13 % ± 6 | 2.8 ± 0.3 | 0.5 ± 0.3 | 32 ± 1 | 237 ± 8 | 179 ± 5 | 92 % ± 1 | 13.8 ± 0.5 | 4.2 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t48-distancia-pillagerpax32-flat-arco-regla semilla 5901: sprint → red (0/4 vivos), normal → blue (2/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 117 de 117 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t48-distancia-pillagerpax32-flat-arco-regla: enemy:scguns.bullet.annihilated=38 enemy:scguns.bullet.decimated=33 enemy:scguns.bullet.eliminated=36 enemy:scguns.bullet.executed=27 enemy:scguns.bullet.killed=32
- t48-distancia-pillagerpax32-flat-arco-sinregla: enemy:scguns.bullet.annihilated=39 enemy:scguns.bullet.decimated=44 enemy:scguns.bullet.eliminated=46 enemy:scguns.bullet.executed=55 enemy:scguns.bullet.killed=56
- t48-distancia-pillagerpax32-flat-linea-regla: enemy:scguns.bullet.annihilated=12 enemy:scguns.bullet.decimated=11 enemy:scguns.bullet.eliminated=10 enemy:scguns.bullet.executed=12 enemy:scguns.bullet.killed=13 other(minecraft:pillager):scguns.bullet.executed=2
- t48-distancia-pillagerpax32-flat-linea-sinregla: enemy:scguns.bullet.annihilated=33 enemy:scguns.bullet.decimated=42 enemy:scguns.bullet.eliminated=48 enemy:scguns.bullet.executed=43 enemy:scguns.bullet.killed=35
- t48-distancia-pillagerpax32-flat-lineascutum-regla: enemy:scguns.bullet.annihilated=8 enemy:scguns.bullet.decimated=6 enemy:scguns.bullet.eliminated=7 enemy:scguns.bullet.executed=11 enemy:scguns.bullet.killed=7
- t48-distancia-pillagerpax32-flat-lineascutum-sinregla: enemy:scguns.bullet.annihilated=34 enemy:scguns.bullet.decimated=28 enemy:scguns.bullet.eliminated=31 enemy:scguns.bullet.executed=31 enemy:scguns.bullet.killed=32

## Motivos de decisión (cambios registrados): ENGAGE 2826, LOW_HEALTH_RETREAT 410, NONE 46, OUT_OF_RANGE 1566, SEEK_ENEMY 69

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t48-distancia-pillagerpax32-flat-arco-regla: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)

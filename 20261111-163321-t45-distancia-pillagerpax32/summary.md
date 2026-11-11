# Simulación Rally - lote 20261111-163321-t45-distancia-pillagerpax32

Preset `t45-distancia-pillagerpax32` · velocidad `sprint` · 97 corridas · 12 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t45-distancia-pillagerpax32-arco-fija | 8 | 88 % ± 13 | 13 % ± 13 | 2.9 ± 0.6 | 1.0 ± 1.0 | 32 ± 1 | 760 ± 27 | 180 ± 13 | 76 % ± 1 | 38.6 ± 0.4 | 12.7 ± 3.1 | 0 |
| t45-distancia-pillagerpax32-arco-aprendida | 8 | 13 % ± 13 | 88 % ± 13 | 0.3 ± 0.3 | 12.8 ± 3.3 | 22 ± 2 | 572 ± 66 | 245 ± 11 | 83 % ± 1 | 35.2 ± 0.2 | 7.5 ± 1.7 | 0 |
| t45-distancia-pillagerpax32-arco-sinregla | 8 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 24.1 ± 0.4 | 7 ± 0 | 229 ± 5 | 194 ± 2 | 91 % ± 1 | 27.6 ± 0.2 | 3.4 ± 0.3 | 0 |
| t45-distancia-pillagerpax32-linea-fija | 8 | 100 % ± 0 | 0 % ± 0 | 5.3 ± 0.6 | 0.0 ± 0.0 | 29 ± 1 | 386 ± 27 | 94 ± 15 | 64 % ± 2 | 34.1 ± 1.1 | 16.8 ± 1.6 | 0 |
| t45-distancia-pillagerpax32-linea-aprendida | 8 | 88 % ± 13 | 13 % ± 13 | 5.0 ± 1.1 | 1.0 ± 1.0 | 26 ± 3 | 377 ± 24 | 119 ± 30 | 76 % ± 2 | 28.2 ± 1.1 | 10.5 ± 1.1 | 0 |
| t45-distancia-pillagerpax32-linea-sinregla | 8 | 50 % ± 19 | 50 % ± 19 | 1.4 ± 0.6 | 2.0 ± 0.9 | 29 ± 2 | 202 ± 19 | 191 ± 8 | 94 % ± 2 | 15.3 ± 0.7 | 4.4 ± 0.8 | 0 |
| t45-distancia-pillagerpax32-lineascutum-fija | 8 | 100 % ± 0 | 0 % ± 0 | 6.6 ± 0.4 | 0.0 ± 0.0 | 25 ± 1 | 341 ± 20 | 61 ± 9 | 67 % ± 2 | 32.5 ± 1.0 | 17.4 ± 0.4 | 0 |
| t45-distancia-pillagerpax32-lineascutum-aprendida | 8 | 100 % ± 0 | 0 % ± 0 | 7.0 ± 0.4 | 0.0 ± 0.0 | 23 ± 1 | 396 ± 13 | 56 ± 12 | 80 % ± 2 | 26.6 ± 0.7 | 10.8 ± 0.5 | 0 |
| t45-distancia-pillagerpax32-lineascutum-sinregla | 8 | 88 % ± 13 | 13 % ± 13 | 2.5 ± 0.5 | 0.3 ± 0.3 | 31 ± 1 | 224 ± 9 | 163 ± 12 | 91 % ± 2 | 13.0 ± 0.6 | 3.6 ± 1.0 | 0 |
| t45-distancia-pillagerpax32-cab-fija | 8 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 13.1 ± 1.6 | 13 ± 3 | 197 ± 28 | 195 ± 4 | 97 % ± 1 | 15.5 ± 1.4 | 5.3 ± 0.7 | 0 |
| t45-distancia-pillagerpax32-cab-aprendida | 8 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 14.6 ± 0.9 | 9 ± 1 | 152 ± 8 | 192 ± 1 | 96 % ± 1 | 13.5 ± 0.7 | 4.7 ± 0.3 | 0 |
| t45-distancia-pillagerpax32-cab-sinregla | 8 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 14.0 ± 1.2 | 10 ± 1 | 150 ± 6 | 197 ± 3 | 97 % ± 2 | 13.1 ± 0.4 | 5.2 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t45-distancia-pillagerpax32-arco-fija semilla 1: sprint → blue (4/0 vivos), normal → red (0/11 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 50 de 50 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t45-distancia-pillagerpax32-arco-fija: enemy:scguns.bullet.annihilated=14 enemy:scguns.bullet.decimated=9 enemy:scguns.bullet.eliminated=9 enemy:scguns.bullet.executed=3 enemy:scguns.bullet.killed=6
- t45-distancia-pillagerpax32-arco-aprendida: enemy:scguns.bullet.annihilated=9 enemy:scguns.bullet.decimated=12 enemy:scguns.bullet.eliminated=15 enemy:scguns.bullet.executed=13 enemy:scguns.bullet.killed=13
- t45-distancia-pillagerpax32-arco-sinregla: enemy:scguns.bullet.annihilated=16 enemy:scguns.bullet.decimated=18 enemy:scguns.bullet.eliminated=8 enemy:scguns.bullet.executed=11 enemy:scguns.bullet.killed=11
- t45-distancia-pillagerpax32-linea-fija: enemy:scguns.bullet.annihilated=4 enemy:scguns.bullet.decimated=2 enemy:scguns.bullet.eliminated=5 enemy:scguns.bullet.executed=7 enemy:scguns.bullet.killed=4
- t45-distancia-pillagerpax32-linea-aprendida: enemy:scguns.bullet.annihilated=6 enemy:scguns.bullet.decimated=5 enemy:scguns.bullet.eliminated=2 enemy:scguns.bullet.executed=6 enemy:scguns.bullet.killed=5
- t45-distancia-pillagerpax32-linea-sinregla: enemy:scguns.bullet.annihilated=13 enemy:scguns.bullet.decimated=17 enemy:scguns.bullet.eliminated=5 enemy:scguns.bullet.executed=7 enemy:scguns.bullet.killed=11
- t45-distancia-pillagerpax32-lineascutum-fija: enemy:scguns.bullet.annihilated=2 enemy:scguns.bullet.decimated=2 enemy:scguns.bullet.executed=4 enemy:scguns.bullet.killed=3
- t45-distancia-pillagerpax32-lineascutum-aprendida: enemy:scguns.bullet.annihilated=2 enemy:scguns.bullet.decimated=2 enemy:scguns.bullet.executed=1 enemy:scguns.bullet.killed=3
- t45-distancia-pillagerpax32-lineascutum-sinregla: enemy:scguns.bullet.annihilated=8 enemy:scguns.bullet.decimated=9 enemy:scguns.bullet.eliminated=9 enemy:scguns.bullet.executed=7 enemy:scguns.bullet.killed=11
- t45-distancia-pillagerpax32-cab-fija: enemy:scguns.bullet.annihilated=12 enemy:scguns.bullet.decimated=10 enemy:scguns.bullet.eliminated=11 enemy:scguns.bullet.executed=17 enemy:scguns.bullet.killed=14
- t45-distancia-pillagerpax32-cab-aprendida: enemy:scguns.bullet.annihilated=16 enemy:scguns.bullet.decimated=12 enemy:scguns.bullet.eliminated=10 enemy:scguns.bullet.executed=12 enemy:scguns.bullet.killed=14
- t45-distancia-pillagerpax32-cab-sinregla: enemy:scguns.bullet.annihilated=7 enemy:scguns.bullet.decimated=13 enemy:scguns.bullet.eliminated=15 enemy:scguns.bullet.executed=15 enemy:scguns.bullet.killed=14

## Motivos de decisión (cambios registrados): ENGAGE 1302, FORMATION_HOLD 28, LOW_HEALTH_RETREAT 216, NONE 20, OUT_OF_RANGE 574, SEEK_ENEMY 31, TOO_CLOSE 1

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t45-distancia-pillagerpax32-arco-fija: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)

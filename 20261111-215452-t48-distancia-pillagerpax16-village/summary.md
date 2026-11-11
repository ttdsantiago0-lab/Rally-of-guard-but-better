# Simulación Rally - lote 20261111-215452-t48-distancia-pillagerpax16-village

Preset `t48-distancia-pillagerpax16-village` · velocidad `sprint` · 283 corridas · 5 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t48-distancia-pillagerpax16-village-arco-sinregla | 25 | 48 % ± 10 | 52 % ± 10 | 1.8 ± 0.4 | 3.6 ± 0.8 | 33 ± 3 | 330 ± 16 | 258 ± 8 | 76 % ± 1 | 11.5 ± 0.5 | 5.4 ± 0.4 | 16 |
| t48-distancia-pillagerpax16-village-linea-regla | 19 | 100 % ± 0 | 0 % ± 0 | 7.2 ± 0.2 | 0.0 ± 0.0 | 97 ± 5 | 330 ± 6 | 98 ± 6 | 10 % ± 1 | 30.5 ± 0.5 | 25.3 ± 0.2 | 29 |
| t48-distancia-pillagerpax16-village-linea-sinregla | 27 | 81 % ± 7 | 19 % ± 7 | 3.1 ± 0.3 | 1.6 ± 0.6 | 82 ± 7 | 308 ± 15 | 211 ± 5 | 75 % ± 2 | 8.0 ± 0.3 | 4.2 ± 0.3 | 16 |
| t48-distancia-pillagerpax16-village-lineascutum-regla | 27 | 100 % ± 0 | 0 % ± 0 | 7.3 ± 0.2 | 0.0 ± 0.0 | 88 ± 3 | 333 ± 6 | 85 ± 5 | 13 % ± 1 | 30.9 ± 0.4 | 25.8 ± 0.2 | 15 |
| t48-distancia-pillagerpax16-village-lineascutum-sinregla | 22 | 93 % ± 4 | 7 % ± 4 | 3.7 ± 0.3 | 1.0 ± 0.5 | 99 ± 9 | 318 ± 15 | 207 ± 7 | 74 % ± 2 | 7.2 ± 0.3 | 4.0 ± 0.3 | 25 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t48-distancia-pillagerpax16-village-arco-regla semilla 2001: sprint → draw (6/6 vivos), normal → draw (7/6 vivos)
- t48-distancia-pillagerpax16-village-arco-regla semilla 9778: sprint → draw (8/6 vivos), normal → draw (5/6 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 103 de 103 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t48-distancia-pillagerpax16-village-arco-sinregla: enemy:scguns.bullet.annihilated=27 enemy:scguns.bullet.decimated=22 enemy:scguns.bullet.eliminated=35 enemy:scguns.bullet.executed=40 enemy:scguns.bullet.killed=32
- t48-distancia-pillagerpax16-village-linea-regla: enemy:scguns.bullet.annihilated=3 enemy:scguns.bullet.decimated=1 enemy:scguns.bullet.eliminated=5 enemy:scguns.bullet.executed=1 enemy:scguns.bullet.killed=6
- t48-distancia-pillagerpax16-village-linea-sinregla: enemy:scguns.bullet.annihilated=30 enemy:scguns.bullet.decimated=32 enemy:scguns.bullet.eliminated=20 enemy:scguns.bullet.executed=28 enemy:scguns.bullet.killed=22
- t48-distancia-pillagerpax16-village-lineascutum-regla: enemy:scguns.bullet.annihilated=2 enemy:scguns.bullet.decimated=2 enemy:scguns.bullet.eliminated=6 enemy:scguns.bullet.executed=5 enemy:scguns.bullet.killed=3
- t48-distancia-pillagerpax16-village-lineascutum-sinregla: enemy:scguns.bullet.annihilated=22 enemy:scguns.bullet.decimated=19 enemy:scguns.bullet.eliminated=18 enemy:scguns.bullet.executed=15 enemy:scguns.bullet.killed=21

## Motivos de decisión (cambios registrados): ENGAGE 5436, FORMATION_HOLD 1, LOW_HEALTH_RETREAT 857, NONE 236, OUT_OF_RANGE 2864, SEEK_ENEMY 556, STUCK 542, TOO_CLOSE 646, UNIT_BROKEN 1

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t48-distancia-pillagerpax16-village-arco-regla - 60 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t48-distancia-pillagerpax16-village-lineascutum-regla - 15 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t48-distancia-pillagerpax16-village-lineascutum-sinregla - 25 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t48-distancia-pillagerpax16-village-linea-regla - 29 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t48-distancia-pillagerpax16-village-arco-sinregla - 16 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t48-distancia-pillagerpax16-village-linea-sinregla - 16 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)

# Simulación Rally - lote 20261112-191058-t53-aldea-pillagerpax32

Preset `t53-aldea-pillagerpax32` · velocidad `sprint` · 423 corridas · 9 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t53-aldea-pillagerpax32-arco-regla | 11 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 17.2 ± 0.8 | 34 ± 2 | 376 ± 19 | 234 ± 6 | 41 % ± 3 | 39.1 ± 0.5 | 15.5 ± 1.5 | 43 |
| t53-aldea-pillagerpax32-arco-sinregla | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 24.2 ± 0.5 | 12 ± 0 | 174 ± 6 | 213 ± 3 | 68 % ± 2 | 16.8 ± 0.3 | 4.9 ± 0.2 | 0 |
| t53-aldea-pillagerpax32-arco-buscarvista | 25 | 10 % ± 5 | 90 % ± 5 | 0.4 ± 0.2 | 13.4 ± 1.3 | 63 ± 10 | 385 ± 28 | 247 ± 7 | 67 % ± 1 | 27.8 ± 1.1 | 9.7 ± 1.0 | 19 |
| t53-aldea-pillagerpax32-linea-sinregla | 24 | 13 % ± 6 | 88 % ± 6 | 0.4 ± 0.2 | 11.8 ± 1.2 | 72 ± 10 | 250 ± 20 | 243 ± 6 | 67 % ± 2 | 9.1 ± 0.4 | 3.1 ± 0.2 | 17 |
| t53-aldea-pillagerpax32-lineascutum-buscarvista | 18 | 72 % ± 10 | 28 % ± 10 | 3.7 ± 0.6 | 1.3 ± 0.5 | 114 ± 9 | 517 ± 13 | 222 ± 16 | 66 % ± 2 | 11.6 ± 0.6 | 9.2 ± 0.6 | 34 |
| t53-aldea-pillagerpax32-lineascutum-regla | 11 | 77 % ± 8 | 23 % ± 8 | 5.4 ± 0.3 | 2.0 ± 0.8 | 157 ± 8 | 444 ± 12 | 148 ± 5 | 7 % ± 0 | 32.4 ± 0.9 | 24.6 ± 0.6 | 42 |
| t53-aldea-pillagerpax32-lineascutum-sinregla | 26 | 19 % ± 8 | 81 % ± 8 | 0.4 ± 0.2 | 11.0 ± 1.5 | 67 ± 9 | 262 ± 28 | 247 ± 9 | 66 % ± 2 | 9.6 ± 0.5 | 3.5 ± 0.2 | 16 |
| t53-aldea-pillagerpax32-linea-buscarvista | 18 | 58 % ± 12 | 42 % ± 12 | 3.0 ± 0.7 | 3.7 ± 1.3 | 97 ± 9 | 460 ± 23 | 237 ± 11 | 65 % ± 3 | 13.7 ± 1.1 | 10.5 ± 0.9 | 30 |
| t53-aldea-pillagerpax32-linea-regla | 5 | 90 % ± 10 | 10 % ± 10 | 5.4 ± 0.2 | 0.2 ± 0.2 | 126 ± 18 | 483 ± 22 | 155 ± 9 | 12 % ± 2 | 33.2 ± 0.9 | 24.8 ± 0.9 | 53 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t53-aldea-pillagerpax32-arco-regla semilla 6001: sprint → red (0/14 vivos), normal → red (0/15 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 54 de 54 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t53-aldea-pillagerpax32-arco-regla: enemy:scguns.bullet.annihilated=19 enemy:scguns.bullet.decimated=22 enemy:scguns.bullet.eliminated=17 enemy:scguns.bullet.executed=17 enemy:scguns.bullet.killed=13
- t53-aldea-pillagerpax32-arco-sinregla: enemy:scguns.bullet.annihilated=50 enemy:scguns.bullet.decimated=39 enemy:scguns.bullet.eliminated=44 enemy:scguns.bullet.executed=59 enemy:scguns.bullet.killed=48
- t53-aldea-pillagerpax32-arco-buscarvista: enemy:scguns.bullet.annihilated=47 enemy:scguns.bullet.decimated=37 enemy:scguns.bullet.eliminated=32 enemy:scguns.bullet.executed=34 enemy:scguns.bullet.killed=40
- t53-aldea-pillagerpax32-linea-sinregla: enemy:scguns.bullet.annihilated=33 enemy:scguns.bullet.decimated=34 enemy:scguns.bullet.eliminated=41 enemy:scguns.bullet.executed=31 enemy:scguns.bullet.killed=43
- t53-aldea-pillagerpax32-lineascutum-buscarvista: enemy:scguns.bullet.annihilated=13 enemy:scguns.bullet.decimated=18 enemy:scguns.bullet.eliminated=20 enemy:scguns.bullet.executed=8 enemy:scguns.bullet.killed=18
- t53-aldea-pillagerpax32-lineascutum-regla: enemy:scguns.bullet.annihilated=4 enemy:scguns.bullet.decimated=6 enemy:scguns.bullet.eliminated=8 enemy:scguns.bullet.executed=6 enemy:scguns.bullet.killed=5
- t53-aldea-pillagerpax32-lineascutum-sinregla: enemy:scguns.bullet.annihilated=41 enemy:scguns.bullet.decimated=43 enemy:scguns.bullet.eliminated=36 enemy:scguns.bullet.executed=38 enemy:scguns.bullet.killed=39
- t53-aldea-pillagerpax32-linea-buscarvista: enemy:scguns.bullet.annihilated=17 enemy:scguns.bullet.decimated=13 enemy:scguns.bullet.eliminated=20 enemy:scguns.bullet.executed=21 enemy:scguns.bullet.killed=19
- t53-aldea-pillagerpax32-linea-regla: enemy:scguns.bullet.annihilated=2 enemy:scguns.bullet.decimated=7 enemy:scguns.bullet.eliminated=2 enemy:scguns.bullet.executed=2

## Motivos de decisión (cambios registrados): ENGAGE 9528, LOW_HEALTH_RETREAT 1696, NONE 283, OUT_OF_RANGE 4578, SEEK_ENEMY 918, STUCK 667, TOO_CLOSE 1643, UNIT_BROKEN 1

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t53-aldea-pillagerpax32-linea-regla - 53 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t53-aldea-pillagerpax32-linea-buscarvista - 30 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t53-aldea-pillagerpax32-lineascutum-regla - 42 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t53-aldea-pillagerpax32-lineascutum-sinregla - 16 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t53-aldea-pillagerpax32-lineascutum-buscarvista - 34 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t53-aldea-pillagerpax32-arco-regla - 43 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t53-aldea-pillagerpax32-arco-buscarvista - 19 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t53-aldea-pillagerpax32-linea-sinregla - 17 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)

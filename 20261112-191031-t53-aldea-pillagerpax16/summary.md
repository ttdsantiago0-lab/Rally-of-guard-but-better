# Simulación Rally - lote 20261112-191031-t53-aldea-pillagerpax16

Preset `t53-aldea-pillagerpax16` · velocidad `sprint` · 401 corridas · 8 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t53-aldea-pillagerpax16-arco-sinregla | 30 | 17 % ± 7 | 83 % ± 7 | 0.6 ± 0.3 | 6.1 ± 0.8 | 28 ± 3 | 275 ± 15 | 239 ± 5 | 73 % ± 2 | 12.7 ± 0.5 | 4.6 ± 0.3 | 5 |
| t53-aldea-pillagerpax16-arco-buscarvista | 21 | 100 % ± 0 | 0 % ± 0 | 5.2 ± 0.4 | 0.0 ± 0.0 | 73 ± 4 | 409 ± 5 | 158 ± 11 | 65 % ± 1 | 20.0 ± 0.9 | 10.8 ± 0.6 | 26 |
| t53-aldea-pillagerpax16-linea-regla | 23 | 100 % ± 0 | 0 % ± 0 | 7.2 ± 0.1 | 0.0 ± 0.0 | 89 ± 4 | 324 ± 7 | 92 ± 5 | 13 % ± 1 | 30.5 ± 0.3 | 25.9 ± 0.2 | 23 |
| t53-aldea-pillagerpax16-linea-sinregla | 27 | 78 % ± 7 | 22 % ± 7 | 3.0 ± 0.3 | 1.9 ± 0.6 | 97 ± 8 | 299 ± 13 | 213 ± 7 | 71 % ± 2 | 7.7 ± 0.3 | 4.8 ± 0.4 | 14 |
| t53-aldea-pillagerpax16-linea-buscarvista | 26 | 100 % ± 0 | 0 % ± 0 | 6.7 ± 0.2 | 0.0 ± 0.0 | 61 ± 3 | 363 ± 4 | 115 ± 6 | 66 % ± 1 | 14.4 ± 0.4 | 11.7 ± 0.3 | 16 |
| t53-aldea-pillagerpax16-lineascutum-regla | 23 | 100 % ± 0 | 0 % ± 0 | 7.1 ± 0.2 | 0.0 ± 0.0 | 87 ± 4 | 329 ± 4 | 85 ± 6 | 15 % ± 1 | 30.4 ± 0.3 | 25.4 ± 0.3 | 21 |
| t53-aldea-pillagerpax16-lineascutum-sinregla | 27 | 89 % ± 6 | 11 % ± 6 | 3.0 ± 0.3 | 0.9 ± 0.5 | 87 ± 7 | 317 ± 10 | 210 ± 6 | 70 % ± 2 | 7.8 ± 0.3 | 4.3 ± 0.3 | 14 |
| t53-aldea-pillagerpax16-lineascutum-buscarvista | 26 | 100 % ± 0 | 0 % ± 0 | 6.1 ± 0.2 | 0.0 ± 0.0 | 78 ± 4 | 360 ± 7 | 127 ± 8 | 64 % ± 1 | 12.6 ± 0.4 | 10.7 ± 0.3 | 17 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t53-aldea-pillagerpax16-arco-regla semilla 6001: sprint → draw (8/6 vivos), normal → draw (3/6 vivos)
- t53-aldea-pillagerpax16-arco-regla semilla 13778: sprint → draw (7/6 vivos), normal → draw (7/6 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 172 de 172 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t53-aldea-pillagerpax16-arco-sinregla: enemy:scguns.bullet.annihilated=42 enemy:scguns.bullet.decimated=54 enemy:scguns.bullet.eliminated=41 enemy:scguns.bullet.executed=42 enemy:scguns.bullet.killed=42
- t53-aldea-pillagerpax16-arco-buscarvista: enemy:scguns.bullet.annihilated=14 enemy:scguns.bullet.decimated=17 enemy:scguns.bullet.eliminated=8 enemy:scguns.bullet.executed=8 enemy:scguns.bullet.killed=12
- t53-aldea-pillagerpax16-linea-regla: enemy:scguns.bullet.annihilated=5 enemy:scguns.bullet.decimated=2 enemy:scguns.bullet.eliminated=5 enemy:scguns.bullet.executed=5 enemy:scguns.bullet.killed=2
- t53-aldea-pillagerpax16-linea-sinregla: enemy:scguns.bullet.annihilated=31 enemy:scguns.bullet.decimated=28 enemy:scguns.bullet.eliminated=24 enemy:scguns.bullet.executed=32 enemy:scguns.bullet.killed=21
- t53-aldea-pillagerpax16-linea-buscarvista: enemy:scguns.bullet.annihilated=8 enemy:scguns.bullet.decimated=4 enemy:scguns.bullet.eliminated=8 enemy:scguns.bullet.executed=8 enemy:scguns.bullet.killed=5
- t53-aldea-pillagerpax16-lineascutum-regla: enemy:scguns.bullet.annihilated=3 enemy:scguns.bullet.decimated=4 enemy:scguns.bullet.eliminated=6 enemy:scguns.bullet.executed=4 enemy:scguns.bullet.killed=4
- t53-aldea-pillagerpax16-lineascutum-sinregla: enemy:scguns.bullet.annihilated=35 enemy:scguns.bullet.decimated=28 enemy:scguns.bullet.eliminated=22 enemy:scguns.bullet.executed=20 enemy:scguns.bullet.killed=31
- t53-aldea-pillagerpax16-lineascutum-buscarvista: enemy:scguns.bullet.annihilated=8 enemy:scguns.bullet.decimated=7 enemy:scguns.bullet.eliminated=8 enemy:scguns.bullet.executed=15 enemy:scguns.bullet.killed=11

## Motivos de decisión (cambios registrados): ENGAGE 9823, LOW_HEALTH_RETREAT 1131, NONE 361, OUT_OF_RANGE 4199, SEEK_ENEMY 744, STUCK 716, TOO_CLOSE 2782

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t53-aldea-pillagerpax16-arco-regla - 60 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t53-aldea-pillagerpax16-arco-buscarvista - 26 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t53-aldea-pillagerpax16-linea-regla - 23 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t53-aldea-pillagerpax16-linea-buscarvista - 16 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t53-aldea-pillagerpax16-lineascutum-buscarvista - 17 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t53-aldea-pillagerpax16-lineascutum-sinregla - 14 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t53-aldea-pillagerpax16-lineascutum-regla - 21 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t53-aldea-pillagerpax16-linea-sinregla - 14 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t53-aldea-pillagerpax16-arco-sinregla - 5 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)

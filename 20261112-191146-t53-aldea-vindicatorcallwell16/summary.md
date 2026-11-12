# Simulación Rally - lote 20261112-191146-t53-aldea-vindicatorcallwell16

Preset `t53-aldea-vindicatorcallwell16` · velocidad `sprint` · 504 corridas · 5 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t53-aldea-vindicatorcallwell16-linea-sinregla | 23 | 30 % ± 10 | 70 % ± 10 | 1.2 ± 0.4 | 5.7 ± 1.0 | 42 ± 5 | 263 ± 17 | 184 ± 5 | 66 % ± 2 | 8.7 ± 0.4 | 5.3 ± 0.3 | 22 |
| t53-aldea-vindicatorcallwell16-lineascutum-sinregla | 23 | 22 % ± 9 | 78 % ± 9 | 1.1 ± 0.5 | 6.8 ± 0.8 | 32 ± 3 | 247 ± 14 | 180 ± 6 | 68 % ± 2 | 9.6 ± 0.3 | 5.2 ± 0.3 | 22 |
| t53-aldea-vindicatorcallwell16-lineascutum-buscarvista | 2 | 50 % ± 50 | 50 % ± 50 | 3.0 ± 3.0 | 3.0 ± 3.0 | 56 ± 11 | 347 ± 45 | 144 ± 35 | 52 % ± 21 | 18.3 ± 0.1 | 13.0 ± 8.5 | 58 |
| t53-aldea-vindicatorcallwell16-linea-buscarvista | 7 | 57 % ± 20 | 43 % ± 20 | 3.1 ± 1.2 | 3.0 ± 1.4 | 58 ± 9 | 303 ± 33 | 142 ± 16 | 62 % ± 3 | 15.7 ± 1.8 | 10.2 ± 0.7 | 49 |
| t53-aldea-vindicatorcallwell16-arco-sinregla | 5 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 9.0 ± 0.7 | 19 ± 1 | 228 ± 12 | 185 ± 6 | 61 % ± 4 | 13.8 ± 0.2 | 3.8 ± 0.7 | 51 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t53-aldea-vindicatorcallwell16-arco-regla semilla 6001: sprint → draw (8/6 vivos), normal → draw (8/6 vivos)
- t53-aldea-vindicatorcallwell16-arco-regla semilla 13778: sprint → draw (8/6 vivos), normal → draw (8/6 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 17 de 17 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t53-aldea-vindicatorcallwell16-linea-sinregla: enemy:scguns.bullet.annihilated=30 enemy:scguns.bullet.decimated=36 enemy:scguns.bullet.eliminated=26 enemy:scguns.bullet.executed=39 enemy:scguns.bullet.killed=25
- t53-aldea-vindicatorcallwell16-lineascutum-sinregla: enemy:scguns.bullet.annihilated=28 enemy:scguns.bullet.decimated=34 enemy:scguns.bullet.eliminated=33 enemy:scguns.bullet.executed=30 enemy:scguns.bullet.killed=34
- t53-aldea-vindicatorcallwell16-lineascutum-buscarvista: enemy:scguns.bullet.annihilated=3 enemy:scguns.bullet.eliminated=2 enemy:scguns.bullet.executed=2 enemy:scguns.bullet.killed=3
- t53-aldea-vindicatorcallwell16-linea-buscarvista: enemy:scguns.bullet.annihilated=4 enemy:scguns.bullet.decimated=10 enemy:scguns.bullet.eliminated=7 enemy:scguns.bullet.executed=6 enemy:scguns.bullet.killed=7
- t53-aldea-vindicatorcallwell16-arco-sinregla: enemy:scguns.bullet.annihilated=6 enemy:scguns.bullet.decimated=11 enemy:scguns.bullet.eliminated=8 enemy:scguns.bullet.executed=7 enemy:scguns.bullet.killed=8

## Motivos de decisión (cambios registrados): ENGAGE 8579, LOW_HEALTH_RETREAT 690, NONE 458, OUT_OF_RANGE 4755, SEEK_ENEMY 327, STUCK 45, TOO_CLOSE 881

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t53-aldea-vindicatorcallwell16-arco-regla - 60 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t53-aldea-vindicatorcallwell16-arco-sinregla - 51 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t53-aldea-vindicatorcallwell16-arco-buscarvista - 60 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t53-aldea-vindicatorcallwell16-linea-regla - 60 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t53-aldea-vindicatorcallwell16-linea-sinregla - 22 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t53-aldea-vindicatorcallwell16-linea-buscarvista - 49 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t53-aldea-vindicatorcallwell16-lineascutum-regla - 60 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t53-aldea-vindicatorcallwell16-lineascutum-buscarvista - 58 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t53-aldea-vindicatorcallwell16-lineascutum-sinregla - 22 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)

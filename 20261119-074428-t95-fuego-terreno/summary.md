# Simulación Rally - lote 20261119-074428-t95-fuego-terreno

Preset `t95-fuego-terreno` · velocidad `sprint` · 123 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t95-aldea-defecto | 30 | 98 % ± 2 | 2 % ± 2 | 5.9 ± 0.3 | 0.1 ± 0.1 | 54 ± 5 | 362 ± 5 | 140 ± 7 | 58 % ± 1 | 17.2 ± 0.6 | 13.4 ± 0.6 | 0 |
| t95-aldea-sync | 30 | 100 % ± 0 | 0 % ± 0 | 6.2 ± 0.3 | 0.0 ± 0.0 | 48 ± 2 | 360 ± 5 | 130 ± 6 | 61 % ± 1 | 17.8 ± 0.6 | 13.7 ± 0.5 | 0 |
| t95-bosque-defecto | 30 | 20 % ± 7 | 80 % ± 7 | 0.6 ± 0.3 | 8.1 ± 1.2 | 33 ± 1 | 584 ± 18 | 222 ± 5 | 49 % ± 1 | 38.0 ± 0.3 | 12.3 ± 1.1 | 1 |
| t95-bosque-sync | 30 | 20 % ± 7 | 80 % ± 7 | 0.8 ± 0.3 | 10.1 ± 1.3 | 31 ± 1 | 582 ± 22 | 214 ± 8 | 56 % ± 1 | 38.2 ± 0.2 | 13.0 ± 1.0 | 1 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t95-aldea-defecto semilla 9300: sprint → blue (8/0 vivos), normal → blue (5/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 72 de 72 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t95-aldea-defecto: enemy:scguns.bullet.annihilated=15 enemy:scguns.bullet.decimated=11 enemy:scguns.bullet.eliminated=12 enemy:scguns.bullet.executed=13 enemy:scguns.bullet.killed=13
- t95-aldea-sync: enemy:scguns.bullet.annihilated=13 enemy:scguns.bullet.decimated=10 enemy:scguns.bullet.eliminated=13 enemy:scguns.bullet.executed=6 enemy:scguns.bullet.killed=12
- t95-bosque-defecto: enemy:scguns.bullet.annihilated=46 enemy:scguns.bullet.decimated=48 enemy:scguns.bullet.eliminated=46 enemy:scguns.bullet.executed=40 enemy:scguns.bullet.killed=41
- t95-bosque-sync: enemy:scguns.bullet.annihilated=42 enemy:scguns.bullet.decimated=35 enemy:scguns.bullet.eliminated=46 enemy:scguns.bullet.executed=44 enemy:scguns.bullet.killed=48

## Motivos de decisión (cambios registrados): ENGAGE 3235, LOW_HEALTH_RETREAT 344, NONE 40, OUT_OF_RANGE 1887, SEEK_ENEMY 125, STUCK 86, TOO_CLOSE 354

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t95-bosque-defecto - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t95-bosque-sync - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)

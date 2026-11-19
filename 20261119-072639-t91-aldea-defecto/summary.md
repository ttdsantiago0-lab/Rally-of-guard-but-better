# Simulación Rally - lote 20261119-072639-t91-aldea-defecto

Preset `t91-aldea-defecto` · velocidad `sprint` · 271 corridas · 9 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t91-pax16-off | 30 | 88 % ± 4 | 12 % ± 4 | 6.4 ± 0.2 | 0.8 ± 0.3 | 92 ± 9 | 342 ± 7 | 115 ± 6 | 58 % ± 1 | 11.5 ± 0.7 | 9.7 ± 0.5 | 0 |
| t91-pax16-defecto | 30 | 98 % ± 2 | 2 % ± 2 | 5.4 ± 0.3 | 0.2 ± 0.2 | 54 ± 5 | 355 ± 5 | 138 ± 9 | 58 % ± 1 | 17.4 ± 0.7 | 13.4 ± 0.6 | 0 |
| t91-pax16-noclose | 30 | 100 % ± 0 | 0 % ± 0 | 5.3 ± 0.3 | 0.0 ± 0.0 | 53 ± 2 | 363 ± 5 | 150 ± 7 | 56 % ± 1 | 17.2 ± 0.5 | 13.6 ± 0.5 | 0 |
| t91-pax16-escudos8-off | 30 | 85 % ± 4 | 15 % ± 4 | 7.2 ± 0.2 | 0.8 ± 0.2 | 115 ± 9 | 285 ± 7 | 111 ± 5 | 0 % ± 0 | 6.2 ± 0.2 | 5.2 ± 0.3 | 0 |
| t91-pax16-escudos8-defecto | 30 | 100 % ± 0 | 0 % ± 0 | 6.7 ± 0.2 | 0.0 ± 0.0 | 62 ± 2 | 300 ± 4 | 129 ± 5 | 0 % ± 0 | 8.3 ± 0.2 | 5.9 ± 0.2 | 0 |
| t91-pax16-escudos8-noclose | 30 | 97 % ± 2 | 3 % ± 2 | 6.4 ± 0.2 | 0.1 ± 0.1 | 68 ± 6 | 295 ± 5 | 128 ± 5 | 0 % ± 0 | 8.0 ± 0.2 | 5.9 ± 0.2 | 0 |
| t91-pax32-escudos8-off | 30 | 75 % ± 5 | 25 % ± 5 | 4.0 ± 0.4 | 3.0 ± 0.7 | 142 ± 7 | 389 ± 14 | 210 ± 11 | 0 % ± 0 | 5.8 ± 0.2 | 5.0 ± 0.3 | 0 |
| t91-pax32-escudos8-defecto | 30 | 95 % ± 4 | 5 % ± 4 | 3.9 ± 0.3 | 0.1 ± 0.1 | 87 ± 5 | 423 ± 8 | 227 ± 8 | 0 % ± 0 | 7.5 ± 0.2 | 5.3 ± 0.3 | 0 |
| t91-pax32-escudos8-noclose | 30 | 82 % ± 7 | 18 % ± 7 | 3.4 ± 0.4 | 0.7 ± 0.3 | 84 ± 5 | 404 ± 7 | 215 ± 7 | 0 % ± 0 | 7.7 ± 0.2 | 5.2 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t91-pax16-off semilla 9100: sprint → blue (8/0 vivos), normal → blue (7/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 263 de 263 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t91-pax16-off: enemy:scguns.bullet.annihilated=12 enemy:scguns.bullet.decimated=13 enemy:scguns.bullet.eliminated=7 enemy:scguns.bullet.executed=8 enemy:scguns.bullet.killed=7
- t91-pax16-defecto: enemy:scguns.bullet.annihilated=16 enemy:scguns.bullet.decimated=11 enemy:scguns.bullet.eliminated=17 enemy:scguns.bullet.executed=14 enemy:scguns.bullet.killed=21
- t91-pax16-noclose: enemy:scguns.bullet.annihilated=13 enemy:scguns.bullet.decimated=22 enemy:scguns.bullet.eliminated=21 enemy:scguns.bullet.executed=13 enemy:scguns.bullet.killed=13
- t91-pax16-escudos8-off: enemy:scguns.bullet.annihilated=7 enemy:scguns.bullet.decimated=5 enemy:scguns.bullet.eliminated=3 enemy:scguns.bullet.executed=8 enemy:scguns.bullet.killed=2
- t91-pax16-escudos8-defecto: enemy:scguns.bullet.annihilated=5 enemy:scguns.bullet.decimated=8 enemy:scguns.bullet.eliminated=12 enemy:scguns.bullet.executed=8 enemy:scguns.bullet.killed=6
- t91-pax16-escudos8-noclose: enemy:scguns.bullet.annihilated=13 enemy:scguns.bullet.decimated=8 enemy:scguns.bullet.eliminated=6 enemy:scguns.bullet.executed=14 enemy:scguns.bullet.killed=7
- t91-pax32-escudos8-off: enemy:scguns.bullet.annihilated=23 enemy:scguns.bullet.decimated=20 enemy:scguns.bullet.eliminated=23 enemy:scguns.bullet.executed=24 enemy:scguns.bullet.killed=29
- t91-pax32-escudos8-defecto: enemy:scguns.bullet.annihilated=22 enemy:scguns.bullet.decimated=31 enemy:scguns.bullet.eliminated=20 enemy:scguns.bullet.executed=23 enemy:scguns.bullet.killed=27
- t91-pax32-escudos8-noclose: enemy:scguns.bullet.annihilated=29 enemy:scguns.bullet.decimated=34 enemy:scguns.bullet.eliminated=28 enemy:scguns.bullet.executed=24 enemy:scguns.bullet.killed=23

## Motivos de decisión (cambios registrados): ENGAGE 5216, LOW_HEALTH_RETREAT 1053, NONE 39, OUT_OF_RANGE 746, SEEK_ENEMY 1268, STUCK 1050, TOO_CLOSE 745

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.

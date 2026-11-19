# Simulación Rally - lote 20261119-045911-t86-aldea-brecha

Preset `t86-aldea-brecha` · velocidad `sprint` · 181 corridas · 6 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t86-pax16 | 30 | 82 % ± 4 | 18 % ± 4 | 6.2 ± 0.3 | 1.1 ± 0.3 | 125 ± 9 | 338 ± 8 | 129 ± 7 | 58 % ± 1 | 9.2 ± 0.6 | 7.3 ± 0.6 | 0 |
| t86-pax16-brecha | 30 | 97 % ± 3 | 3 % ± 3 | 5.6 ± 0.4 | 0.1 ± 0.1 | 52 ± 2 | 357 ± 4 | 147 ± 8 | 57 % ± 1 | 16.9 ± 0.5 | 12.6 ± 0.4 | 0 |
| t86-pax16-escudos8 | 30 | 83 % ± 4 | 17 % ± 4 | 7.0 ± 0.2 | 1.1 ± 0.3 | 115 ± 9 | 281 ± 8 | 119 ± 5 | 0 % ± 0 | 6.2 ± 0.3 | 6.0 ± 0.3 | 0 |
| t86-pax16-escudos8-brecha | 30 | 95 % ± 3 | 5 % ± 3 | 7.0 ± 0.2 | 0.3 ± 0.2 | 71 ± 7 | 299 ± 6 | 120 ± 5 | 0 % ± 0 | 7.9 ± 0.2 | 5.9 ± 0.2 | 0 |
| t86-pax32-escudos8 | 30 | 78 % ± 5 | 22 % ± 5 | 4.1 ± 0.4 | 2.4 ± 0.6 | 139 ± 7 | 376 ± 13 | 199 ± 9 | 0 % ± 0 | 5.8 ± 0.2 | 5.1 ± 0.3 | 0 |
| t86-pax32-escudos8-brecha | 30 | 82 % ± 7 | 18 % ± 7 | 3.7 ± 0.4 | 0.8 ± 0.4 | 85 ± 5 | 399 ± 8 | 219 ± 8 | 0 % ± 0 | 7.4 ± 0.1 | 5.3 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t86-pax16 semilla 8600: sprint → blue (7/0 vivos), normal → blue (5/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 173 de 173 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t86-pax16: enemy:scguns.bullet.annihilated=13 enemy:scguns.bullet.decimated=9 enemy:scguns.bullet.eliminated=14 enemy:scguns.bullet.executed=8 enemy:scguns.bullet.killed=10
- t86-pax16-brecha: enemy:scguns.bullet.annihilated=18 enemy:scguns.bullet.decimated=17 enemy:scguns.bullet.eliminated=9 enemy:scguns.bullet.executed=16 enemy:scguns.bullet.killed=12
- t86-pax16-escudos8: enemy:scguns.bullet.annihilated=7 enemy:scguns.bullet.decimated=8 enemy:scguns.bullet.eliminated=5 enemy:scguns.bullet.executed=6 enemy:scguns.bullet.killed=5
- t86-pax16-escudos8-brecha: enemy:scguns.bullet.annihilated=7 enemy:scguns.bullet.decimated=4 enemy:scguns.bullet.eliminated=3 enemy:scguns.bullet.executed=10 enemy:scguns.bullet.killed=5
- t86-pax32-escudos8: enemy:scguns.bullet.annihilated=21 enemy:scguns.bullet.decimated=27 enemy:scguns.bullet.eliminated=24 enemy:scguns.bullet.executed=28 enemy:scguns.bullet.killed=16
- t86-pax32-escudos8-brecha: enemy:scguns.bullet.annihilated=25 enemy:scguns.bullet.decimated=21 enemy:scguns.bullet.eliminated=21 enemy:scguns.bullet.executed=38 enemy:scguns.bullet.killed=23

## Motivos de decisión (cambios registrados): ENGAGE 3694, LOW_HEALTH_RETREAT 633, NONE 25, OUT_OF_RANGE 458, SEEK_ENEMY 871, STUCK 815, TOO_CLOSE 615

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.

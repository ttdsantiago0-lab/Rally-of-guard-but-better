# Simulación Rally - lote 20261121-181107-t112-gv-dispara

Preset `t112-gv-dispara` · velocidad `sprint` · 241 corridas · 8 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t112-horda-rally8 | 30 | 100 % ± 0 | 0 % ± 0 | 6.2 ± 0.3 | 0.0 ± 0.0 | 26 ± 1 | 346 ± 7 | 121 ± 8 | 49 % ± 2 | 14.0 ± 0.6 | 6.7 ± 0.7 | 0 |
| t112-horda-sin8 | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 8.6 ± 0.6 | 20 ± 1 | 61 ± 6 | 186 ± 1 | 0 % ± 0 | 10.1 ± 0.2 | 2.5 ± 0.1 | 0 |
| t112-horda-sin16 | 30 | 43 % ± 9 | 57 % ± 9 | 2.1 ± 0.5 | 4.0 ± 0.8 | 26 ± 1 | 218 ± 10 | 352 ± 6 | 0 % ± 0 | 9.1 ± 0.1 | 6.9 ± 0.2 | 0 |
| t112-pistoleros-rally8 | 30 | 90 % ± 6 | 10 % ± 6 | 4.4 ± 0.4 | 0.4 ± 0.3 | 50 ± 2 | 1220 ± 13 | 130 ± 12 | 71 % ± 1 | 44.2 ± 0.5 | 22.8 ± 1.6 | 0 |
| t112-pistoleros-sin8 | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 41.9 ± 0.5 | 6 ± 0 | 9 ± 1 | 189 ± 1 | 0 % ± 0 | 17.8 ± 0.2 | 2.9 ± 0.1 | 0 |
| t112-pistoleros-sin16 | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 34.9 ± 0.5 | 7 ± 0 | 27 ± 1 | 373 ± 2 | 0 % ± 0 | 16.7 ± 0.1 | 6.5 ± 0.2 | 0 |
| t112-pistoleros-sin32 | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 20.5 ± 0.7 | 13 ± 0 | 70 ± 3 | 753 ± 3 | 0 % ± 0 | 14.3 ± 0.2 | 5.5 ± 0.2 | 0 |
| t112-pistoleros-sin48 | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 9.1 ± 0.6 | 21 ± 1 | 166 ± 4 | 1127 ± 3 | 0 % ± 0 | 11.8 ± 0.1 | 7.3 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t112-horda-rally8 semilla 10400: sprint → blue (5/0 vivos), normal → blue (4/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 57 de 57 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t112-horda-rally8: enemy:scguns.bullet.annihilated=12 enemy:scguns.bullet.decimated=13 enemy:scguns.bullet.eliminated=10 enemy:scguns.bullet.executed=11 enemy:scguns.bullet.killed=7
- t112-horda-sin8: enemy:scguns.bullet.annihilated=51 enemy:scguns.bullet.decimated=45 enemy:scguns.bullet.eliminated=49 enemy:scguns.bullet.executed=45 enemy:scguns.bullet.killed=50
- t112-horda-sin16: enemy:scguns.bullet.annihilated=94 enemy:scguns.bullet.decimated=74 enemy:scguns.bullet.eliminated=73 enemy:scguns.bullet.executed=84 enemy:scguns.bullet.killed=92 friend:arrow=1
- t112-pistoleros-rally8: enemy:scguns.bullet.annihilated=25 enemy:scguns.bullet.decimated=21 enemy:scguns.bullet.eliminated=21 enemy:scguns.bullet.executed=19 enemy:scguns.bullet.killed=21
- t112-pistoleros-sin8: enemy:scguns.bullet.annihilated=50 enemy:scguns.bullet.decimated=47 enemy:scguns.bullet.eliminated=54 enemy:scguns.bullet.executed=42 enemy:scguns.bullet.killed=46 friend:arrow=1
- t112-pistoleros-sin16: enemy:scguns.bullet.annihilated=95 enemy:scguns.bullet.decimated=94 enemy:scguns.bullet.eliminated=101 enemy:scguns.bullet.executed=96 enemy:scguns.bullet.killed=92 friend:arrow=2
- t112-pistoleros-sin32: enemy:scguns.bullet.annihilated=183 enemy:scguns.bullet.decimated=221 enemy:scguns.bullet.eliminated=204 enemy:scguns.bullet.executed=174 enemy:scguns.bullet.killed=174 friend:arrow=4
- t112-pistoleros-sin48: enemy:scguns.bullet.annihilated=307 enemy:scguns.bullet.decimated=285 enemy:scguns.bullet.eliminated=292 enemy:scguns.bullet.executed=284 enemy:scguns.bullet.killed=260 friend:arrow=12

## Motivos de decisión (cambios registrados): ENGAGE 1602, LOW_HEALTH_RETREAT 134, NONE 83, OUT_OF_RANGE 1247, SEEK_ENEMY 40, STUCK 2, TOO_CLOSE 30

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.

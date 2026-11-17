# Simulación Rally - lote 20261117-052343-t67-arena96

Preset `t67-arena96` · velocidad `sprint` · 97 corridas · 3 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t67-a96-o1 | 30 | 57 % ± 9 | 43 % ± 9 | 2.3 ± 0.4 | 3.6 ± 0.9 | 33 ± 1 | 738 ± 16 | 183 ± 9 | 75 % ± 1 | 39.4 ± 0.2 | 12.1 ± 1.2 | 0 |
| t67-a96-cabarcos | 30 | 100 % ± 0 | 0 % ± 0 | 5.4 ± 0.3 | 0.0 ± 0.0 | 38 ± 2 | 787 ± 9 | 100 ± 8 | 68 % ± 1 | 40.0 ± 0.5 | 19.7 ± 2.0 | 1 |
| t67-a96-cab | 27 | 63 % ± 9 | 37 % ± 9 | 1.9 ± 0.3 | 2.5 ± 0.7 | 36 ± 2 | 463 ± 9 | 170 ± 7 | 73 % ± 2 | 37.1 ± 0.4 | 13.7 ± 2.0 | 8 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t67-a96-o1 semilla 6701: sprint → red (0/10 vivos), normal → red (0/16 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 64 de 64 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t67-a96-o1: enemy:scguns.bullet.annihilated=37 enemy:scguns.bullet.decimated=20 enemy:scguns.bullet.eliminated=43 enemy:scguns.bullet.executed=42 enemy:scguns.bullet.killed=30
- t67-a96-cabarcos: enemy:scguns.bullet.annihilated=15 enemy:scguns.bullet.decimated=14 enemy:scguns.bullet.eliminated=20 enemy:scguns.bullet.executed=11 enemy:scguns.bullet.killed=15 env:inWall=3
- t67-a96-cab: enemy:scguns.bullet.annihilated=40 enemy:scguns.bullet.decimated=37 enemy:scguns.bullet.eliminated=30 enemy:scguns.bullet.executed=28 enemy:scguns.bullet.killed=29 env:inWall=1

## Motivos de decisión (cambios registrados): ENGAGE 2329, FORMATION_HOLD 32, LOW_HEALTH_RETREAT 204, NONE 51, OUT_OF_RANGE 1727, SEEK_ENEMY 14, STUCK 17

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t67-a96-cab - 8 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t67-a96-cabarcos - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)

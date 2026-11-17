# Simulación Rally - lote 20261117-200302-t68-aldea

Preset `t68-aldea` · velocidad `sprint` · 179 corridas · 5 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t68-pax16 | 27 | 100 % ± 0 | 0 % ± 0 | 6.5 ± 0.2 | 0.0 ± 0.0 | 70 ± 3 | 363 ± 5 | 126 ± 8 | 64 % ± 1 | 12.9 ± 0.4 | 11.0 ± 0.4 | 11 |
| t68-pax16-nostuck | 30 | 83 % ± 4 | 17 % ± 4 | 6.6 ± 0.2 | 0.9 ± 0.3 | 110 ± 10 | 350 ± 7 | 108 ± 7 | 66 % ± 1 | 10.8 ± 0.7 | 9.0 ± 0.6 | 0 |
| t68-pax16-rowB | 30 | 100 % ± 0 | 0 % ± 0 | 6.5 ± 0.2 | 0.0 ± 0.0 | 22 ± 1 | 274 ± 6 | 85 ± 7 | 58 % ± 1 | 23.6 ± 0.4 | 17.8 ± 0.3 | 0 |
| t68-pax32-rowB | 30 | 100 % ± 0 | 0 % ± 0 | 5.4 ± 0.3 | 0.0 ± 0.0 | 26 ± 1 | 313 ± 6 | 112 ± 6 | 64 % ± 1 | 24.1 ± 0.6 | 17.5 ± 0.5 | 0 |
| t68-pax32 | 17 | 91 % ± 6 | 9 % ± 6 | 4.9 ± 0.5 | 1.5 ± 1.0 | 109 ± 6 | 515 ± 23 | 215 ± 12 | 67 % ± 2 | 11.3 ± 0.9 | 10.5 ± 0.7 | 32 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t68-pax16 semilla 6801: sprint → blue (4/0 vivos), normal → draw (6/1 vivos)
- t68-pax16 semilla 14578: sprint → ? (0/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 133 de 133 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t68-pax16: enemy:scguns.bullet.annihilated=8 enemy:scguns.bullet.decimated=6 enemy:scguns.bullet.eliminated=6 enemy:scguns.bullet.executed=12 enemy:scguns.bullet.killed=8
- t68-pax16-nostuck: enemy:scguns.bullet.annihilated=9 enemy:scguns.bullet.decimated=4 enemy:scguns.bullet.eliminated=13 enemy:scguns.bullet.executed=7 enemy:scguns.bullet.killed=9
- t68-pax16-rowB: enemy:scguns.bullet.annihilated=7 enemy:scguns.bullet.decimated=14 enemy:scguns.bullet.eliminated=10 enemy:scguns.bullet.executed=7 enemy:scguns.bullet.killed=8
- t68-pax32-rowB: enemy:scguns.bullet.annihilated=15 enemy:scguns.bullet.decimated=10 enemy:scguns.bullet.eliminated=13 enemy:scguns.bullet.executed=20 enemy:scguns.bullet.killed=21
- t68-pax32: enemy:scguns.bullet.annihilated=13 enemy:scguns.bullet.decimated=9 enemy:scguns.bullet.eliminated=10 enemy:scguns.bullet.executed=12 enemy:scguns.bullet.killed=9

## Motivos de decisión (cambios registrados): ENGAGE 4757, LOW_HEALTH_RETREAT 498, NONE 35, OUT_OF_RANGE 1636, SEEK_ENEMY 423, STUCK 241, TOO_CLOSE 1613, UNIT_BROKEN 1

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t68-pax32 - 32 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t68-pax16 - 11 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- t68-pax16: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)

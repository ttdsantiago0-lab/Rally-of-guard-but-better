# Simulación Rally - lote 20261124-033345-t172-fuerza-pax

Preset `t172-fuerza-pax` · velocidad `sprint` · 61 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t172-pax-espera | 30 | 3 % ± 3 | 97 % ± 3 | 0.1 ± 0.1 | 15.9 ± 1.5 | 55 ± 3 | 817 ± 26 | 242 ± 5 | 46 % ± 1 | 37.3 ± 0.5 | 17.9 ± 0.9 | 0 |
| t172-pax-force | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 16.8 ± 1.2 | 55 ± 2 | 799 ± 22 | 245 ± 7 | 45 % ± 1 | 36.4 ± 0.3 | 21.1 ± 1.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t172-pax-espera semilla 15300: sprint → red (0/29 vivos), normal → red (0/19 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 1 de 1 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t172-pax-espera: enemy:scguns.bullet.annihilated=47 enemy:scguns.bullet.decimated=50 enemy:scguns.bullet.eliminated=49 enemy:scguns.bullet.executed=43 enemy:scguns.bullet.killed=49
- t172-pax-force: enemy:scguns.bullet.annihilated=43 enemy:scguns.bullet.decimated=57 enemy:scguns.bullet.eliminated=53 enemy:scguns.bullet.executed=44 enemy:scguns.bullet.killed=43

## Motivos de decisión (cambios registrados): ENGAGE 1918, FORMATION_HOLD 121, LOW_HEALTH_RETREAT 188, OUT_OF_RANGE 1704, SEEK_ENEMY 67, TOO_CLOSE 27

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.

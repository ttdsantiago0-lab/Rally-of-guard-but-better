# Simulación Rally - lote 20261112-055115-t51-espadas-vindicadores32

Preset `t51-espadas-vindicadores32` · velocidad `sprint` · 121 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t51-espadas-vindicadores32-mixta-hoy | 30 | 97 % ± 3 | 3 % ± 3 | 2.6 ± 0.2 | 0.2 ± 0.2 | 42 ± 1 | 822 ± 6 | 149 ± 6 | 90 % ± 1 | 16.3 ± 0.7 | 11.1 ± 1.2 | 0 |
| t51-espadas-vindicadores32-mixta-golpearysalir | 30 | 77 % ± 8 | 23 % ± 8 | 2.1 ± 0.3 | 2.9 ± 1.1 | 41 ± 1 | 795 ± 16 | 161 ± 8 | 91 % ± 1 | 16.2 ± 0.6 | 11.7 ± 1.0 | 0 |
| t51-espadas-vindicadores32-espadas-hoy | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 30.9 ± 0.2 | 9 ± 0 | 52 ± 2 | 197 ± 1 | 0 % ± 0 | 9.0 ± 0.1 | 5.1 ± 0.2 | 0 |
| t51-espadas-vindicadores32-espadas-golpearysalir | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 31.2 ± 0.2 | 10 ± 0 | 63 ± 2 | 197 ± 1 | 0 % ± 0 | 8.4 ± 0.1 | 5.3 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t51-espadas-vindicadores32-mixta-hoy semilla 4001: sprint → blue (3/0 vivos), normal → blue (3/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 52 de 52 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t51-espadas-vindicadores32-mixta-hoy: enemy:mob=162 enemy:scguns.bullet.killed=1
- t51-espadas-vindicadores32-mixta-golpearysalir: enemy:mob=175 enemy:scguns.bullet.eliminated=1
- t51-espadas-vindicadores32-espadas-hoy: enemy:mob=240
- t51-espadas-vindicadores32-espadas-golpearysalir: enemy:mob=240

## Motivos de decisión (cambios registrados): ENGAGE 1156, FORMATION_HOLD 196, LOW_HEALTH_RETREAT 59, NONE 8, OUT_OF_RANGE 130, SEEK_ENEMY 8, TOO_CLOSE 146

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.

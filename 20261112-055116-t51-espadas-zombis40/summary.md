# Simulación Rally - lote 20261112-055116-t51-espadas-zombis40

Preset `t51-espadas-zombis40` · velocidad `sprint` · 121 corridas · 4 escenarios.
> Mobs ajenos retirados del campo entre corridas (tercer bando oculto): {minecraft:zombie_villager=54}

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t51-espadas-zombis40-mixta-hoy | 30 | 100 % ± 0 | 0 % ± 0 | 7.0 ± 0.3 | 0.0 ± 0.0 | 32 ± 1 | 986 ± 7 | 98 ± 6 | 91 % ± 1 | 11.7 ± 0.3 | 11.8 ± 0.4 | 0 |
| t51-espadas-zombis40-mixta-golpearysalir | 30 | 100 % ± 0 | 0 % ± 0 | 6.2 ± 0.3 | 0.0 ± 0.0 | 35 ± 1 | 1004 ± 9 | 105 ± 6 | 89 % ± 1 | 12.9 ± 0.3 | 11.3 ± 0.4 | 0 |
| t51-espadas-zombis40-espadas-hoy | 30 | 7 % ± 5 | 93 % ± 5 | 0.5 ± 0.4 | 33.8 ± 2.1 | 37 ± 2 | 484 ± 34 | 351 ± 13 | 0 % ± 0 | 3.8 ± 0.1 | 6.4 ± 0.3 | 0 |
| t51-espadas-zombis40-espadas-golpearysalir | 30 | 3 % ± 3 | 97 % ± 3 | 0.3 ± 0.3 | 34.6 ± 1.6 | 36 ± 2 | 478 ± 27 | 322 ± 12 | 0 % ± 0 | 3.9 ± 0.1 | 7.2 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t51-espadas-zombis40-mixta-hoy semilla 4001: sprint → blue (4/0 vivos), normal → blue (4/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 63 de 63 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t51-espadas-zombis40-mixta-hoy: enemy:mob=8
- t51-espadas-zombis40-mixta-golpearysalir: enemy:mob=30
- t51-espadas-zombis40-espadas-hoy: enemy:mob=115
- t51-espadas-zombis40-espadas-golpearysalir: enemy:mob=110

## Motivos de decisión (cambios registrados): ENGAGE 1478, FORMATION_HOLD 210, LOW_HEALTH_RETREAT 1038, NONE 6, OUT_OF_RANGE 32, SEEK_ENEMY 398, TOO_CLOSE 2

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.

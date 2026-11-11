# Simulación Rally - lote 20261111-221113-t49-jinete-zombis40

Preset `t49-jinete-zombis40` · velocidad `sprint` · 61 corridas · 2 escenarios.
> Mobs ajenos retirados del campo entre corridas (tercer bando oculto): {minecraft:zombie_villager=9}

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t49-jinete-zombis40-hoy | 30 | 73 % ± 8 | 27 % ± 8 | 5.2 ± 0.6 | 4.1 ± 1.5 | 37 ± 1 | 950 ± 18 | 278 ± 15 | 93 % ± 1 | 4.3 ± 0.2 | 6.9 ± 0.3 | 0 |
| t49-jinete-zombis40-kite | 30 | 100 % ± 0 | 0 % ± 0 | 6.8 ± 0.3 | 0.0 ± 0.0 | 33 ± 1 | 990 ± 7 | 107 ± 6 | 89 % ± 1 | 12.1 ± 0.3 | 12.2 ± 0.4 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t49-jinete-zombis40-hoy semilla 3001: sprint → red (0/10 vivos), normal → red (0/13 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 52 de 52 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t49-jinete-zombis40-hoy: enemy:mob=38
- t49-jinete-zombis40-kite: enemy:mob=17

## Motivos de decisión (cambios registrados): ENGAGE 876, FORMATION_HOLD 67, LOW_HEALTH_RETREAT 452, NONE 4, OUT_OF_RANGE 22, SEEK_ENEMY 231, TOO_CLOSE 369

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.

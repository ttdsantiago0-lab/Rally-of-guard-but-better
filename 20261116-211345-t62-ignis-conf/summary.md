# Simulación Rally - lote 20261116-211345-t62-ignis-conf

Preset `t62-ignis-conf` · velocidad `sprint` · 91 corridas · 3 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t62-ignis-linea | 30 | 100 % ± 0 | 0 % ± 0 | 3.3 ± 0.2 | 0.0 ± 0.0 | 88 ± 4 | 735 ± 32 | 251 ± 14 | 87 % ± 1 | 8.7 ± 0.1 | 7.8 ± 0.1 | 0 |
| t62-ignis-1a3 | 30 | 100 % ± 0 | 0 % ± 0 | 4.7 ± 0.2 | 0.0 ± 0.0 | 82 ± 3 | 639 ± 18 | 231 ± 13 | 89 % ± 1 | 11.1 ± 0.2 | 9.3 ± 0.2 | 0 |
| t62-ignis-arcos | 30 | 100 % ± 0 | 0 % ± 0 | 4.8 ± 0.3 | 0.0 ± 0.0 | 77 ± 2 | 531 ± 6 | 232 ± 13 | 87 % ± 0 | 13.8 ± 0.1 | 9.7 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t62-ignis-linea semilla 6201: sprint → blue (4/0 vivos), normal → blue (1/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 90 de 90 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t62-ignis-linea: enemy:cataclysm.flame_strike=80 enemy:explosion.player=1 enemy:mob=59 env:onFire=2
- t62-ignis-1a3: enemy:cataclysm.flame_strike=34 enemy:mob=62 env:fall=1 env:onFire=1
- t62-ignis-arcos: enemy:cataclysm.flame_strike=20 enemy:mob=73 env:onFire=2

## Motivos de decisión (cambios registrados): ENGAGE 1630, LOW_HEALTH_RETREAT 778, NONE 8, OUT_OF_RANGE 3, SEEK_ENEMY 364, STUCK 3, TOO_CLOSE 557

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.

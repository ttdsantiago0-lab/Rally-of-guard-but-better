# Simulación Rally - lote 20261109-201648-t35-numeros-ignis

Preset `t35-numeros-ignis` · velocidad `sprint` · 31 corridas · 6 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t35-numeros-ignis-hierro-g8 | 5 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 61 ± 7 | 143 ± 22 | 244 ± 9 | 91 % ± 3 | 15.0 ± 2.2 | 10.3 ± 2.2 | 0 |
| t35-numeros-ignis-netheritamas-g8 | 5 | 80 % ± 20 | 20 % ± 20 | 3.6 ± 1.0 | 0.2 ± 0.2 | 114 ± 14 | 798 ± 62 | 288 ± 34 | 83 % ± 2 | 13.6 ± 1.1 | 12.6 ± 1.1 | 0 |
| t35-numeros-ignis-hierro-g16 | 5 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 98 ± 14 | 240 ± 41 | 584 ± 30 | 89 % ± 2 | 17.4 ± 1.8 | 13.6 ± 1.7 | 0 |
| t35-numeros-ignis-netheritamas-g16 | 5 | 80 % ± 20 | 20 % ± 20 | 8.0 ± 2.1 | 0.2 ± 0.2 | 113 ± 29 | 910 ± 212 | 504 ± 119 | 85 % ± 1 | 12.7 ± 0.6 | 12.5 ± 0.5 | 0 |
| t35-numeros-ignis-hierro-g32 | 5 | 20 % ± 20 | 80 % ± 20 | 0.8 ± 0.8 | 0.8 ± 0.2 | 138 ± 11 | 456 ± 47 | 1234 ± 32 | 85 % ± 2 | 17.1 ± 1.3 | 15.8 ± 1.2 | 0 |
| t35-numeros-ignis-netheritamas-g32 | 5 | 90 % ± 10 | 10 % ± 10 | 16.8 ± 2.6 | 0.2 ± 0.2 | 156 ± 23 | 1332 ± 204 | 1181 ± 189 | 79 % ± 1 | 15.8 ± 1.1 | 15.3 ± 1.0 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t35-numeros-ignis-hierro-g8 semilla 1: sprint → red (0/1 vivos), normal → red (0/1 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 14 de 14 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t35-numeros-ignis-hierro-g8: enemy:mob=40
- t35-numeros-ignis-netheritamas-g8: enemy:cataclysm.flame_strike=8 enemy:mob=14
- t35-numeros-ignis-hierro-g16: enemy:mob=78 env:onFire=2
- t35-numeros-ignis-netheritamas-g16: enemy:cataclysm.flame_strike=19 enemy:mob=21
- t35-numeros-ignis-hierro-g32: enemy:cataclysm.flame_strike=1 enemy:mob=152 env:onFire=3
- t35-numeros-ignis-netheritamas-g32: enemy:cataclysm.flame_strike=28 enemy:mob=44 env:fall=1 env:inFire=2 env:onFire=1

## Motivos de decisión (cambios registrados): ENGAGE 906, FORMATION_HOLD 2, LOW_HEALTH_RETREAT 545, NONE 194, SEEK_ENEMY 324, STUCK 18, TOO_CLOSE 201

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.

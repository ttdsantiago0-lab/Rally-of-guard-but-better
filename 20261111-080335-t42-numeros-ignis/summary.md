# Simulación Rally - lote 20261111-080335-t42-numeros-ignis

Preset `t42-numeros-ignis` · velocidad `sprint` · 31 corridas · 6 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t42-numeros-ignis-hierro-g8-sincro | 5 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 51 ± 4 | 199 ± 16 | 250 ± 11 | 89 % ± 1 | 10.0 ± 0.5 | 6.4 ± 0.8 | 0 |
| t42-numeros-ignis-netheritamas-g8-sincro | 5 | 100 % ± 0 | 0 % ± 0 | 2.6 ± 0.4 | 0.0 ± 0.0 | 106 ± 13 | 884 ± 114 | 347 ± 30 | 89 % ± 1 | 9.0 ± 0.5 | 7.6 ± 0.6 | 0 |
| t42-numeros-ignis-hierro-g16-sincro | 5 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 95 ± 11 | 417 ± 51 | 582 ± 15 | 86 % ± 1 | 10.9 ± 0.2 | 8.6 ± 0.6 | 0 |
| t42-numeros-ignis-netheritamas-g16-sincro | 5 | 100 % ± 0 | 0 % ± 0 | 6.6 ± 0.7 | 0.0 ± 0.0 | 144 ± 25 | 1285 ± 244 | 769 ± 115 | 80 % ± 1 | 8.8 ± 0.1 | 8.0 ± 0.2 | 0 |
| t42-numeros-ignis-hierro-g32-sincro | 5 | 20 % ± 20 | 80 % ± 20 | 0.4 ± 0.4 | 0.8 ± 0.2 | 131 ± 7 | 586 ± 31 | 1204 ± 36 | 86 % ± 1 | 12.4 ± 0.2 | 9.2 ± 0.2 | 0 |
| t42-numeros-ignis-netheritamas-g32-sincro | 5 | 80 % ± 12 | 20 % ± 12 | 13.2 ± 2.9 | 0.4 ± 0.2 | 204 ± 22 | 1891 ± 219 | 1537 ± 141 | 76 % ± 2 | 13.3 ± 0.1 | 12.0 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t42-numeros-ignis-hierro-g8-sincro semilla 1: sprint → red (0/1 vivos), normal → red (0/1 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 16 de 16 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t42-numeros-ignis-hierro-g8-sincro: enemy:mob=40
- t42-numeros-ignis-netheritamas-g8-sincro: enemy:cataclysm.flame_strike=11 enemy:mob=15 env:fall=1
- t42-numeros-ignis-hierro-g16-sincro: enemy:cataclysm.flame_strike=1 enemy:mob=75 env:fall=2 env:onFire=2
- t42-numeros-ignis-netheritamas-g16-sincro: enemy:cataclysm.flame_strike=25 enemy:mob=19 env:onFire=3
- t42-numeros-ignis-hierro-g32-sincro: enemy:cataclysm.flame_strike=1 enemy:mob=153 env:onFire=4
- t42-numeros-ignis-netheritamas-g32-sincro: enemy:cataclysm.flame_strike=39 enemy:mob=46 env:fall=4 env:inFire=2 env:onFire=3

## Motivos de decisión (cambios registrados): ENGAGE 1271, LOW_HEALTH_RETREAT 845, NONE 25, SEEK_ENEMY 580, STUCK 5, TOO_CLOSE 267, UNIT_BROKEN 1

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.

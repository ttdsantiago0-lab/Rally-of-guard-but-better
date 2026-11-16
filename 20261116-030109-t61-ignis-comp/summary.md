# Simulación Rally - lote 20261116-030109-t61-ignis-comp

Preset `t61-ignis-comp` · velocidad `sprint` · 122 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t61-ignis-linea | 30 | 97 % ± 3 | 3 % ± 3 | 3.0 ± 0.2 | 0.0 ± 0.0 | 97 ± 4 | 809 ± 35 | 301 ± 14 | 88 % ± 1 | 9.1 ± 0.1 | 8.1 ± 0.2 | 0 |
| t61-ignis-arcos | 30 | 100 % ± 0 | 0 % ± 0 | 4.5 ± 0.3 | 0.0 ± 0.0 | 78 ± 2 | 527 ± 5 | 221 ± 13 | 86 % ± 1 | 13.7 ± 0.1 | 9.4 ± 0.2 | 1 |
| t61-ignis-1a3 | 30 | 97 % ± 3 | 3 % ± 3 | 4.8 ± 0.3 | 0.0 ± 0.0 | 82 ± 2 | 640 ± 14 | 211 ± 10 | 91 % ± 1 | 11.1 ± 0.2 | 9.0 ± 0.3 | 0 |
| t61-ignis-3a1 | 30 | 67 % ± 9 | 33 % ± 9 | 1.1 ± 0.2 | 0.3 ± 0.1 | 110 ± 5 | 919 ± 41 | 365 ± 15 | 86 % ± 1 | 6.8 ± 0.1 | 6.0 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t61-ignis-linea semilla 6101: sprint → blue (3/0 vivos), normal → blue (4/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 108 de 108 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t61-ignis-linea: enemy:cataclysm.flame_strike=66 enemy:mob=77 env:fall=2 env:inFire=2 env:onFire=3
- t61-ignis-arcos: enemy:cataclysm.flame_strike=24 enemy:mob=73 env:fall=5 env:onFire=2
- t61-ignis-1a3: enemy:cataclysm.flame_strike=33 enemy:mob=59 env:fall=2 env:onFire=1
- t61-ignis-3a1: enemy:cataclysm.flame_strike=120 enemy:mob=81 env:fall=1 env:inFire=2 env:onFire=4

## Motivos de decisión (cambios registrados): ENGAGE 2173, FORMATION_HOLD 2, LOW_HEALTH_RETREAT 1256, NONE 3, OUT_OF_RANGE 4, SEEK_ENEMY 608, STUCK 9, TOO_CLOSE 673

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t61-ignis-arcos - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)

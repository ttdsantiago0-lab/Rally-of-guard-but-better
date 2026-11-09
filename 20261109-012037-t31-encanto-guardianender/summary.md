# Simulación Rally - lote 20261109-012037-t31-encanto-guardianender

Preset `t31-encanto-guardianender` · velocidad `sprint` · 42 corridas · 5 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t31-encanto-guardianender-hierro | 8 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 122 ± 12 | 36 ± 8 | 484 ± 9 | 89 % ± 2 | 19.1 ± 1.6 | 12.4 ± 1.5 | 1 |
| t31-encanto-guardianender-poder | 8 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 113 ± 13 | 35 ± 4 | 490 ± 18 | 90 % ± 2 | 17.9 ± 1.7 | 11.7 ± 1.8 | 0 |
| t31-encanto-guardianender-filo | 8 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 114 ± 8 | 46 ± 7 | 514 ± 12 | 91 % ± 2 | 21.8 ± 1.1 | 14.7 ± 1.1 | 0 |
| t31-encanto-guardianender-proteccion | 8 | 50 % ± 9 | 50 % ± 9 | 4.5 ± 1.0 | 0.9 ± 0.1 | 285 ± 10 | 197 ± 39 | 787 ± 33 | 83 % ± 3 | 23.7 ± 0.9 | 20.3 ± 1.0 | 0 |
| t31-encanto-guardianender-netheritamas | 8 | 100 % ± 0 | 0 % ± 0 | 15.1 ± 0.2 | 0.0 ± 0.0 | 80 ± 9 | 336 ± 1 | 273 ± 27 | 76 % ± 4 | 16.1 ± 1.6 | 12.9 ± 1.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t31-encanto-guardianender-hierro semilla 101: sprint → red (0/1 vivos), normal → red (0/1 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 15 de 15 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t31-encanto-guardianender-hierro: enemy:explosion.player=2 enemy:indirectMagic=13 enemy:mob=111 other(cataclysm:ender_guardian):explosion.player=2
- t31-encanto-guardianender-poder: enemy:explosion.player=2 enemy:indirectMagic=6 enemy:mob=119 other(cataclysm:ender_guardian):explosion.player=1
- t31-encanto-guardianender-filo: enemy:explosion.player=1 enemy:indirectMagic=9 enemy:mob=118
- t31-encanto-guardianender-proteccion: enemy:explosion.player=12 enemy:indirectMagic=13 enemy:mob=67
- t31-encanto-guardianender-netheritamas: enemy:indirectMagic=1 enemy:mob=6

## Motivos de decisión (cambios registrados): COUNTERATTACK 49, ENGAGE 1365, FORMATION_HOLD 6, LOW_HEALTH_RETREAT 184, MORALE_SHAKEN 751, MORALE_WAVERING 531, NONE 387, OUT_OF_RANGE 5, RALLIED 64, REFUSE_ATTACK 116, ROUTED_FLEE 457, SEEK_ENEMY 49, STUCK 17, TOO_CLOSE 39, UNIT_BROKEN 4

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t31-encanto-guardianender-hierro - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- t31-encanto-guardianender-proteccion: 6/8 corridas llegaron al tope de tiempo

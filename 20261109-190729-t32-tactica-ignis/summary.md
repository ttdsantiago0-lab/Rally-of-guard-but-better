# Simulación Rally - lote 20261109-190729-t32-tactica-ignis

Preset `t32-tactica-ignis` · velocidad `sprint` · 53 corridas · 6 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t32-tactica-ignis-linea | 8 | 44 % ± 11 | 56 % ± 11 | 3.0 ± 1.2 | 0.9 ± 0.1 | 272 ± 20 | 1096 ± 87 | 796 ± 63 | 82 % ± 1 | 21.6 ± 1.3 | 18.3 ± 1.2 | 1 |
| t32-tactica-ignis-arcos | 8 | 81 % ± 9 | 19 % ± 9 | 5.4 ± 1.6 | 0.4 ± 0.2 | 196 ± 32 | 706 ± 34 | 710 ± 67 | 88 % ± 1 | 27.1 ± 2.0 | 20.6 ± 1.6 | 2 |
| t32-tactica-ignis-escudos | 8 | 31 % ± 9 | 69 % ± 9 | 1.8 ± 0.5 | 1.0 ± 0.0 | 292 ± 4 | 1483 ± 105 | 863 ± 46 | 0 % ± 0 | 16.6 ± 1.5 | 17.6 ± 1.4 | 0 |
| t32-tactica-ignis-espadas | 8 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 174 ± 16 | 346 ± 49 | 897 ± 53 | 0 % ± 0 | 20.1 ± 1.5 | 16.6 ± 1.1 | 0 |
| t32-tactica-ignis-volea60 | 8 | 50 % ± 9 | 50 % ± 9 | 4.8 ± 1.5 | 0.9 ± 0.1 | 273 ± 25 | 848 ± 118 | 660 ± 68 | 79 % ± 2 | 29.7 ± 1.1 | 24.3 ± 1.4 | 1 |
| t32-tactica-ignis-tortuga | 8 | 63 % ± 13 | 38 % ± 13 | 4.0 ± 1.3 | 0.6 ± 0.2 | 258 ± 19 | 1151 ± 73 | 790 ± 52 | 82 % ± 2 | 22.6 ± 1.3 | 19.8 ± 0.9 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t32-tactica-ignis-linea semilla 1: sprint → red (0/1 vivos), normal → blue (11/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 33 de 33 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t32-tactica-ignis-linea: enemy:cataclysm.flame_strike=8 enemy:mob=91 env:inFire=1 env:onFire=4
- t32-tactica-ignis-arcos: enemy:cataclysm.flame_strike=12 enemy:mob=68 env:fall=2 env:inFire=2 env:onFire=1
- t32-tactica-ignis-escudos: enemy:cataclysm.flame_strike=20 enemy:explosion.player=3 enemy:mob=83 env:inFire=3 env:onFire=5
- t32-tactica-ignis-espadas: enemy:explosion.player=1 enemy:mob=117 env:fall=1 env:onFire=5 other(cataclysm:ignis):cataclysm.flame_strike=4
- t32-tactica-ignis-volea60: enemy:cataclysm.flame_strike=1 enemy:mob=81 env:onFire=8
- t32-tactica-ignis-tortuga: enemy:cataclysm.flame_strike=12 enemy:explosion.player=1 enemy:mob=76 env:onFire=7

## Motivos de decisión (cambios registrados): COUNTERATTACK 262, ENGAGE 2073, FORMATION_HOLD 41, LOW_HEALTH_RETREAT 176, MORALE_SHAKEN 1128, MORALE_WAVERING 1030, NONE 687, OUT_OF_RANGE 55, RALLIED 157, REFUSE_ATTACK 188, ROUTED_FLEE 1151, SEEK_ENEMY 155, STUCK 19, TOO_CLOSE 153, UNIT_BROKEN 2

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t32-tactica-ignis-arcos - 2 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t32-tactica-ignis-linea - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t32-tactica-ignis-volea60 - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- t32-tactica-ignis-linea: 5/8 corridas llegaron al tope de tiempo
- t32-tactica-ignis-escudos: 5/8 corridas llegaron al tope de tiempo
- t32-tactica-ignis-volea60: 6/8 corridas llegaron al tope de tiempo
- t32-tactica-ignis-linea: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)

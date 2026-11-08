# Simulación Rally - lote 20261108-200650-t22-best-bosses

Preset `t22-best-bosses` · velocidad `sprint` · 45 corridas · 11 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t22-best-bosses-netherite_monstrosity-r1 | 4 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 74 ± 21 | 137 ± 26 | 274 ± 27 | 51 % ± 8 | 17.4 ± 3.5 | 8.3 ± 2.3 | 0 |
| t22-best-bosses-ignis-r1 | 4 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 58 ± 5 | 93 ± 19 | 321 ± 9 | 91 % ± 2 | 12.1 ± 1.9 | 8.1 ± 1.3 | 0 |
| t22-best-bosses-the_harbinger-r1 | 4 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 114 ± 10 | 239 ± 47 | 285 ± 17 | 96 % ± 2 | 15.0 ± 2.4 | 10.4 ± 2.0 | 0 |
| t22-best-bosses-ender_guardian-r1 | 4 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 52 ± 7 | 23 ± 5 | 249 ± 20 | 98 % ± 2 | 13.4 ± 2.2 | 7.4 ± 1.1 | 0 |
| t22-best-bosses-cyclops-r1 | 4 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 48 ± 3 | 96 ± 7 | 292 ± 1 | 88 % ± 4 | 9.7 ± 0.7 | 6.5 ± 0.4 | 0 |
| t22-best-bosses-cyclops-r2 | 4 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 2.0 ± 0.0 | 24 ± 2 | 43 ± 4 | 298 ± 7 | 84 % ± 4 | 7.6 ± 0.8 | 6.3 ± 0.6 | 0 |
| t22-best-bosses-hydra-r1 | 4 | 38 % ± 13 | 63 % ± 13 | 1.0 ± 0.4 | 1.0 ± 0.0 | 172 ± 8 | 862 ± 106 | 250 ± 8 | 30 % ± 1 | 26.3 ± 1.9 | 14.7 ± 0.3 | 0 |
| t22-best-bosses-ravager-r1 | 4 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 11 ± 1 | 102 ± 1 | 19 ± 2 | 95 % ± 2 | 8.0 ± 0.8 | 6.2 ± 0.4 | 0 |
| t22-best-bosses-ravager-r2 | 4 | 75 % ± 25 | 25 % ± 25 | 5.0 ± 1.7 | 0.3 ± 0.3 | 33 ± 14 | 206 ± 2 | 117 ± 51 | 91 % ± 2 | 11.1 ± 2.2 | 8.0 ± 0.9 | 0 |
| t22-best-bosses-ravager-r4 | 4 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 3.0 ± 0.7 | 47 ± 1 | 277 ± 31 | 278 ± 15 | 94 % ± 3 | 10.8 ± 2.5 | 10.5 ± 1.0 | 0 |
| t22-best-bosses-warden-r1 | 4 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 86 ± 11 | 157 ± 34 | 218 ± 0 | 91 % ± 4 | 15.8 ± 1.0 | 7.1 ± 0.8 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t22-best-bosses-netherite_monstrosity-r1 semilla 1: sprint → red (0/1 vivos), normal → red (0/1 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 10 de 10 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t22-best-bosses-netherite_monstrosity-r1: enemy:explosion.player=1 enemy:mob=26 env:lava=5
- t22-best-bosses-ignis-r1: enemy:mob=31 env:onFire=1
- t22-best-bosses-the_harbinger-r1: enemy:cataclysm.deathlaser=9 enemy:cataclysm.laser=4 enemy:indirectMagic=1 enemy:mob=11 env:inFire=1 env:onFire=1 env:wither=5
- t22-best-bosses-ender_guardian-r1: enemy:indirectMagic=4 enemy:mob=28
- t22-best-bosses-cyclops-r1: enemy:mob=32
- t22-best-bosses-cyclops-r2: enemy:mob=32
- t22-best-bosses-hydra-r1: enemy:mob=28
- t22-best-bosses-ravager-r2: enemy:mob=12
- t22-best-bosses-ravager-r4: enemy:mob=32
- t22-best-bosses-warden-r1: enemy:mob=32

## Motivos de decisión (cambios registrados): COUNTERATTACK 11, ENGAGE 666, FORMATION_HOLD 2, LOW_HEALTH_RETREAT 110, MORALE_SHAKEN 332, MORALE_WAVERING 187, NONE 194, OUT_OF_RANGE 1, RALLIED 6, REFUSE_ATTACK 14, ROUTED_FLEE 136, SEEK_ENEMY 16, STUCK 16, TOO_CLOSE 172

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t22-best-bosses-hydra-r1: 3/4 corridas llegaron al tope de tiempo
- MUERTES QUE NO SON DEL ENEMIGO: t22-best-bosses-netherite_monstrosity-r1 - 16 % de las bajas azules ({enemy:explosion.player=1.0, enemy:mob=26.0, env:lava=5.0})
- MUERTES QUE NO SON DEL ENEMIGO: t22-best-bosses-the_harbinger-r1 - 22 % de las bajas azules ({enemy:cataclysm.deathlaser=9.0, enemy:cataclysm.laser=4.0, enemy:indirectMagic=1.0, enemy:mob=11.0, env:inFire=1.0, env:onFire=1.0, env:wither=5.0})

# Simulación Rally - lote 20261105-132105-t11-cab-waves

Preset `t11-cab-waves` · velocidad `sprint` · 34 corridas · 2 escenarios.
> Mobs ajenos retirados del campo entre corridas (tercer bando oculto): {minecraft:zombie_villager=6}

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t11-waves-mixta-cab-comida-base | 5 | 80 % ± 20 | 20 % ± 20 | 5.2 ± 1.7 | 1.6 ± 1.6 | 394 ± 33 | 2175 ± 106 | 280 ± 73 | 64 % ± 5 | 12.5 ± 2.3 | 11.0 ± 1.5 | 11 |
| t11-waves-mixta-cab-comida-antes | 5 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 9.8 ± 0.5 | 144 ± 20 | 624 ± 92 | 269 ± 21 | 81 % ± 7 | 6.3 ± 0.3 | 5.1 ± 0.3 | 11 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t11-waves-mixta-cab-comida-base semilla 1: sprint → blue (8/0 vivos), normal → draw (2/11 vivos)
- t11-waves-mixta-cab-comida-base semilla 7778: sprint → ? (0/0 vivos), normal → draw (5/1 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 4 de 4 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t11-waves-mixta-cab-comida-base: enemy:arrow=1 enemy:explosion.player=3 enemy:mob=3 env:inWall=5
- t11-waves-mixta-cab-comida-antes: enemy:explosion.player=19 enemy:mob=7 env:inWall=5

## Motivos de decisión (cambios registrados): COUNTERATTACK 13, ENGAGE 1223, FORMATION_HOLD 33, LOW_HEALTH_RETREAT 170, MORALE_SHAKEN 168, MORALE_WAVERING 82, NONE 825, OUT_OF_RANGE 29, RALLIED 1, REFUSE_ATTACK 57, ROUTED_FLEE 62, SEEK_ENEMY 385, STUCK 1, TOO_CLOSE 373

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t11-waves-mixta-cab-comida-antes - 11 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t11-waves-mixta-cab-comida-base - 11 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- t11-waves-mixta-cab-comida-base: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)
- MUERTES QUE NO SON DEL ENEMIGO: t11-waves-mixta-cab-comida-base - 42 % de las bajas azules ({enemy:arrow=1.0, enemy:explosion.player=3.0, enemy:mob=3.0, env:inWall=5.0})
- MUERTES QUE NO SON DEL ENEMIGO: t11-waves-mixta-cab-comida-antes - 16 % de las bajas azules ({enemy:explosion.player=19.0, enemy:mob=7.0, env:inWall=5.0})

# Simulación Rally - lote 20261109-001246-t30-gigante-dragonfuego

Preset `t30-gigante-dragonfuego` · velocidad `sprint` · 30 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t30-gigante-dragonfuego-hierro | 6 | 17 % ± 17 | 83 % ± 17 | 2.3 ± 2.3 | 0.8 ± 0.2 | 85 ± 13 | 201 ± 43 | 384 ± 67 | 64 % ± 2 | 23.3 ± 2.7 | 13.5 ± 2.2 | 1 |
| t30-gigante-dragonfuego-diamante | 6 | 83 % ± 17 | 17 % ± 17 | 7.2 ± 2.1 | 0.2 ± 0.2 | 116 ± 20 | 351 ± 17 | 311 ± 66 | 63 % ± 2 | 24.2 ± 3.1 | 18.1 ± 3.1 | 1 |
| t30-gigante-dragonfuego-netherita | 6 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 160 ± 16 | 244 ± 22 | 510 ± 25 | 66 % ± 3 | 25.2 ± 2.2 | 16.6 ± 1.7 | 2 |
| t30-gigante-dragonfuego-netheritamas | 6 | 100 % ± 0 | 0 % ± 0 | 14.3 ± 0.9 | 0.0 ± 0.0 | 34 ± 8 | 371 ± 2 | 141 ± 42 | 73 % ± 5 | 18.8 ± 1.4 | 10.4 ± 1.0 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t30-gigante-dragonfuego-hierro semilla 1: sprint → red (0/1 vivos), normal → draw (2/1 vivos)
- t30-gigante-dragonfuego-hierro semilla 7778: sprint → ? (0/0 vivos), normal → red (0/1 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 12 de 12 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t30-gigante-dragonfuego-hierro: enemy:dragon_fire=36 enemy:mob=33 env:inFire=9 env:onFire=4
- t30-gigante-dragonfuego-diamante: enemy:dragon_fire=23 enemy:mob=20 env:inFire=4 env:onFire=6
- t30-gigante-dragonfuego-netherita: enemy:dragon_fire=47 enemy:mob=38 env:inFire=6 env:onFire=5
- t30-gigante-dragonfuego-netheritamas: enemy:dragon_fire=7 enemy:mob=2 env:onFire=1

## Motivos de decisión (cambios registrados): COUNTERATTACK 7, ENGAGE 1314, LOW_HEALTH_RETREAT 217, MORALE_SHAKEN 880, MORALE_WAVERING 380, NONE 299, OUT_OF_RANGE 7, RALLIED 17, REFUSE_ATTACK 63, ROUTED_FLEE 256, SEEK_ENEMY 73, STUCK 19, TOO_CLOSE 60

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t30-gigante-dragonfuego-netherita - 2 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t30-gigante-dragonfuego-diamante - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t30-gigante-dragonfuego-hierro - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- t30-gigante-dragonfuego-hierro: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)
- MUERTES QUE NO SON DEL ENEMIGO: t30-gigante-dragonfuego-hierro - 16 % de las bajas azules ({enemy:dragon_fire=36.0, enemy:mob=33.0, env:inFire=9.0, env:onFire=4.0})
- MUERTES QUE NO SON DEL ENEMIGO: t30-gigante-dragonfuego-diamante - 19 % de las bajas azules ({enemy:dragon_fire=23.0, enemy:mob=20.0, env:inFire=4.0, env:onFire=6.0})
- MUERTES QUE NO SON DEL ENEMIGO: t30-gigante-dragonfuego-netherita - 11 % de las bajas azules ({enemy:dragon_fire=47.0, enemy:mob=38.0, env:inFire=6.0, env:onFire=5.0})

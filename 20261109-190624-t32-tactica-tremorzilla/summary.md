# Simulación Rally - lote 20261109-190624-t32-tactica-tremorzilla

Preset `t32-tactica-tremorzilla` · velocidad `sprint` · 93 corridas · 6 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t32-tactica-tremorzilla-escudos | 5 | 50 % ± 0 | 50 % ± 0 | 11.0 ± 2.0 | 1.0 ± 0.0 | 300 ± 0 | 1174 ± 122 | 246 ± 38 | 0 % ± 0 | 34.6 ± 3.1 | 29.5 ± 2.1 | 10 |
| t32-tactica-tremorzilla-volea60 | 3 | 50 % ± 0 | 50 % ± 0 | 11.3 ± 0.3 | 1.0 ± 0.0 | 300 ± 0 | 749 ± 118 | 167 ± 23 | 94 % ± 2 | 55.7 ± 1.7 | 36.3 ± 4.2 | 11 |
| t32-tactica-tremorzilla-tortuga | 3 | 17 % ± 17 | 83 % ± 17 | 2.7 ± 2.7 | 1.0 ± 0.0 | 200 ± 51 | 561 ± 19 | 338 ± 24 | 109 % ± 11 | 31.0 ± 5.6 | 20.8 ± 7.0 | 13 |
| t32-tactica-tremorzilla-linea | 2 | 25 % ± 25 | 75 % ± 25 | 6.5 ± 6.5 | 1.0 ± 0.0 | 234 ± 66 | 956 ± 333 | 319 ± 104 | 92 % ± 5 | 35.8 ± 4.5 | 25.3 ± 4.9 | 14 |
| t32-tactica-tremorzilla-arcos | 2 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 98 ± 0 | 208 ± 37 | 420 ± 11 | 88 % ± 4 | 32.1 ± 7.9 | 15.8 ± 11.7 | 13 |
| t32-tactica-tremorzilla-espadas | 1 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 99 ± 0 | 303 ± 0 | 541 ± 0 | 0 % ± 0 | 28.5 ± 0.0 | 25.8 ± 0.0 | 14 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t32-tactica-tremorzilla-linea semilla 1: sprint → draw (3/1 vivos), normal → draw (6/1 vivos)
- t32-tactica-tremorzilla-linea semilla 7778: sprint → draw (4/1 vivos), normal → draw (10/1 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 10 de 10 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t32-tactica-tremorzilla-escudos: enemy:mob=7 enemy:tremorzilla_beam=16 env:radiation=2
- t32-tactica-tremorzilla-volea60: enemy:mob=4 enemy:tremorzilla_beam=7 env:radiation=3
- t32-tactica-tremorzilla-tortuga: enemy:mob=11 enemy:tremorzilla_beam=27 env:radiation=2
- t32-tactica-tremorzilla-linea: enemy:mob=5 enemy:tremorzilla_beam=12 env:radiation=2
- t32-tactica-tremorzilla-arcos: enemy:mob=1 enemy:tremorzilla_beam=29 env:radiation=2
- t32-tactica-tremorzilla-espadas: enemy:mob=1 enemy:tremorzilla_beam=15

## Motivos de decisión (cambios registrados): COUNTERATTACK 381, ENGAGE 4375, FORMATION_HOLD 386, LOW_HEALTH_RETREAT 845, MORALE_SHAKEN 2195, MORALE_WAVERING 1429, NONE 2178, OUT_OF_RANGE 425, RALLIED 128, REFUSE_ATTACK 91, ROUTED_FLEE 1355, SEEK_ENEMY 686, STUCK 371, TOO_CLOSE 94

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t32-tactica-tremorzilla-linea - 14 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t32-tactica-tremorzilla-arcos - 13 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t32-tactica-tremorzilla-escudos - 10 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t32-tactica-tremorzilla-espadas - 14 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t32-tactica-tremorzilla-volea60 - 11 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t32-tactica-tremorzilla-tortuga - 13 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- t32-tactica-tremorzilla-escudos: 5/5 corridas llegaron al tope de tiempo
- t32-tactica-tremorzilla-volea60: 3/3 corridas llegaron al tope de tiempo
- MUERTES QUE NO SON DEL ENEMIGO: t32-tactica-tremorzilla-volea60 - 21 % de las bajas azules ({enemy:mob=4.0, enemy:tremorzilla_beam=7.0, env:radiation=3.0})
- MUERTES QUE NO SON DEL ENEMIGO: t32-tactica-tremorzilla-linea - 11 % de las bajas azules ({enemy:mob=5.0, enemy:tremorzilla_beam=12.0, env:radiation=2.0})

# Simulación Rally - lote 20261112-190144-t52-escape-dragonesjovenes3

Preset `t52-escape-dragonesjovenes3` · velocidad `sprint` · 540 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t52-escape-dragonesjovenes3-cab-huida | 2 | 50 % ± 0 | 50 % ± 0 | 4.5 ± 2.5 | 3.0 ± 0.0 | 120 ± 0 | 409 ± 8 | 146 ± 92 | 23 % ± 1 | 11.7 ± 2.9 | 10.9 ± 1.9 | 57 |
| t52-escape-dragonesjovenes3-linea-huida | 2 | 50 % ± 0 | 50 % ± 0 | 5.5 ± 0.5 | 3.0 ± 0.0 | 120 ± 0 | 379 ± 11 | 192 ± 24 | 15 % ± 1 | 10.7 ± 2.1 | 9.8 ± 2.3 | 57 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t52-escape-dragonesjovenes3-linea-pelear semilla 5001: sprint → draw (8/3 vivos), normal → draw (8/3 vivos)
- t52-escape-dragonesjovenes3-linea-pelear semilla 12778: sprint → draw (8/3 vivos), normal → draw (8/3 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 4 de 4 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t52-escape-dragonesjovenes3-cab-huida: enemy:dragon_fire=3 enemy:mob=1 env:inFire=1 env:onFire=2
- t52-escape-dragonesjovenes3-linea-huida: enemy:dragon_fire=3 enemy:mob=2

## Motivos de decisión (cambios registrados): ENGAGE 7611, FORMATION_HOLD 1300, INSTRUCTION 2836, LOW_HEALTH_RETREAT 1045, NONE 165, OUT_OF_RANGE 18, SEEK_ENEMY 6538, STUCK 1056, TOO_CLOSE 997

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t52-escape-dragonesjovenes3-linea-pelear - 60 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t52-escape-dragonesjovenes3-linea-retirada - 60 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t52-escape-dragonesjovenes3-linea-huida - 57 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t52-escape-dragonesjovenes3-arco-pelear - 60 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t52-escape-dragonesjovenes3-arco-retirada - 60 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t52-escape-dragonesjovenes3-arco-huida - 60 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t52-escape-dragonesjovenes3-cab-pelear - 60 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t52-escape-dragonesjovenes3-cab-retirada - 60 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t52-escape-dragonesjovenes3-cab-huida - 57 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- t52-escape-dragonesjovenes3-cab-huida: 2/2 corridas llegaron al tope de tiempo
- t52-escape-dragonesjovenes3-linea-huida: 2/2 corridas llegaron al tope de tiempo
- MUERTES QUE NO SON DEL ENEMIGO: t52-escape-dragonesjovenes3-cab-huida - 43 % de las bajas azules ({enemy:dragon_fire=3.0, enemy:mob=1.0, env:inFire=1.0, env:onFire=2.0})

# Simulación Rally - lote 20261108-191654-t5-check-creeper

Preset `t5-check-creeper` · velocidad `sprint` · 305 corridas · 10 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t5-final-creeper-mele-base | 29 | 57 % ± 3 | 43 % ± 3 | 4.9 ± 0.3 | 4.2 ± 0.6 | 116 ± 2 | 0 ± 0 | 113 ± 6 | 0 % ± 0 | 19.1 ± 1.3 | 37.4 ± 1.1 | 3 |
| t5-final-creeper-mele-antes | 30 | 98 % ± 2 | 2 % ± 2 | 3.6 ± 0.3 | 0.0 ± 0.0 | 21 ± 1 | 0 ± 0 | 158 ± 6 | 0 % ± 0 | 13.1 ± 0.2 | 8.3 ± 0.3 | 0 |
| t5-final-creeper-escudo-base | 30 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.0 | 0.0 ± 0.0 | 33 ± 2 | 67 ± 5 | 12 ± 3 | 0 % ± 0 | 12.2 ± 0.5 | 8.5 ± 0.4 | 2 |
| t5-final-creeper-escudo-antes | 30 | 100 % ± 0 | 0 % ± 0 | 4.5 ± 0.3 | 0.0 ± 0.0 | 20 ± 1 | 0 ± 0 | 135 ± 6 | 0 % ± 0 | 13.0 ± 0.2 | 7.9 ± 0.3 | 0 |
| t5-final-creeper-arco-base | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 25 ± 1 | 580 ± 3 | 0 ± 0 | 85 % ± 1 | 9.5 ± 0.2 | 6.7 ± 0.2 | 0 |
| t5-final-creeper-arco-antes | 30 | 100 % ± 0 | 0 % ± 0 | 3.1 ± 0.3 | 0.0 ± 0.0 | 21 ± 1 | 0 ± 0 | 163 ± 5 | 0 % ± 0 | 13.2 ± 0.2 | 8.0 ± 0.3 | 0 |
| t5-final-creeper-linea-base | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 26 ± 1 | 385 ± 16 | 3 ± 1 | 88 % ± 1 | 9.9 ± 0.1 | 8.0 ± 0.3 | 0 |
| t5-final-creeper-linea-antes | 30 | 100 % ± 0 | 0 % ± 0 | 4.0 ± 0.2 | 0.0 ± 0.0 | 21 ± 1 | 0 ± 0 | 156 ± 5 | 0 % ± 0 | 13.0 ± 0.2 | 7.7 ± 0.3 | 0 |
| t5-final-creeper-cab-base | 30 | 100 % ± 0 | 0 % ± 0 | 4.8 ± 0.2 | 0.0 ± 0.0 | 16 ± 0 | 148 ± 6 | 129 ± 5 | 89 % ± 1 | 11.8 ± 0.1 | 8.9 ± 0.1 | 0 |
| t5-final-creeper-cab-antes | 30 | 100 % ± 0 | 0 % ± 0 | 2.7 ± 0.2 | 0.0 ± 0.0 | 19 ± 0 | 0 ± 0 | 174 ± 5 | 0 % ± 0 | 12.8 ± 0.1 | 5.5 ± 0.1 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t5-final-creeper-mele-base semilla 1: sprint → draw (4/4 vivos), normal → draw (5/1 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 298 de 298 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t5-final-creeper-mele-base: enemy:explosion.player=89
- t5-final-creeper-mele-antes: enemy:explosion.player=133
- t5-final-creeper-escudo-base: enemy:explosion.player=2
- t5-final-creeper-escudo-antes: enemy:explosion.player=105
- t5-final-creeper-arco-antes: enemy:explosion.player=147
- t5-final-creeper-linea-base: enemy:explosion.player=1
- t5-final-creeper-linea-antes: enemy:explosion.player=121
- t5-final-creeper-cab-base: enemy:explosion.player=97
- t5-final-creeper-cab-antes: enemy:explosion.player=158

## Motivos de decisión (cambios registrados): ENGAGE 2187, FORMATION_HOLD 145, LOW_HEALTH_RETREAT 46, MORALE_SHAKEN 901, MORALE_WAVERING 270, NONE 805, OUT_OF_RANGE 1, RALLIED 13, REFUSE_ATTACK 4, ROUTED_FLEE 171, SEEK_ENEMY 3408, STUCK 234, TOO_CLOSE 376

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t5-final-creeper-escudo-base - 2 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t5-final-creeper-mele-base - 3 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- t5-final-creeper-mele-base: 25/29 corridas llegaron al tope de tiempo

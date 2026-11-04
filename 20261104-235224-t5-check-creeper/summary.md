# Simulación Rally - lote 20261104-235224-t5-check-creeper

Preset `t5-check-creeper` · velocidad `sprint` · 307 corridas · 10 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t5-final-creeper-mele-base | 30 | 95 % ± 4 | 5 % ± 4 | 2.7 ± 0.3 | 0.0 ± 0.0 | 23 ± 1 | 0 ± 0 | 182 ± 6 | 0 % ± 0 | 12.9 ± 0.2 | 7.5 ± 0.3 | 2 |
| t5-final-creeper-mele-antes | 30 | 97 % ± 2 | 3 % ± 2 | 2.8 ± 0.3 | 0.0 ± 0.0 | 23 ± 1 | 0 ± 0 | 174 ± 6 | 0 % ± 0 | 12.8 ± 0.2 | 7.1 ± 0.3 | 1 |
| t5-final-creeper-escudo-base | 30 | 100 % ± 0 | 0 % ± 0 | 5.9 ± 0.2 | 0.0 ± 0.0 | 14 ± 0 | 59 ± 3 | 110 ± 6 | 0 % ± 0 | 11.5 ± 0.2 | 5.5 ± 0.2 | 0 |
| t5-final-creeper-escudo-antes | 30 | 97 % ± 3 | 3 % ± 3 | 3.2 ± 0.3 | 0.1 ± 0.1 | 23 ± 1 | 0 ± 0 | 171 ± 6 | 0 % ± 0 | 12.5 ± 0.2 | 6.7 ± 0.3 | 0 |
| t5-final-creeper-arco-base | 30 | 95 % ± 4 | 5 % ± 4 | 5.0 ± 0.3 | 0.1 ± 0.1 | 15 ± 0 | 237 ± 10 | 119 ± 8 | 85 % ± 1 | 9.4 ± 0.2 | 5.4 ± 0.2 | 0 |
| t5-final-creeper-arco-antes | 30 | 92 % ± 5 | 8 % ± 5 | 3.1 ± 0.3 | 0.1 ± 0.1 | 22 ± 1 | 0 ± 0 | 165 ± 6 | 0 % ± 0 | 13.1 ± 0.2 | 7.1 ± 0.3 | 1 |
| t5-final-creeper-linea-base | 30 | 100 % ± 0 | 0 % ± 0 | 5.1 ± 0.3 | 0.0 ± 0.0 | 14 ± 0 | 161 ± 6 | 115 ± 9 | 86 % ± 1 | 10.8 ± 0.1 | 5.9 ± 0.2 | 0 |
| t5-final-creeper-linea-antes | 30 | 97 % ± 3 | 3 % ± 3 | 2.8 ± 0.2 | 0.0 ± 0.0 | 22 ± 1 | 0 ± 0 | 176 ± 4 | 0 % ± 0 | 12.6 ± 0.2 | 7.1 ± 0.2 | 0 |
| t5-final-creeper-cab-base | 30 | 95 % ± 4 | 5 % ± 4 | 4.0 ± 0.3 | 0.1 ± 0.1 | 19 ± 1 | 140 ± 9 | 137 ± 7 | 85 % ± 1 | 11.5 ± 0.2 | 8.6 ± 0.3 | 1 |
| t5-final-creeper-cab-antes | 30 | 92 % ± 3 | 8 % ± 3 | 2.1 ± 0.3 | 0.0 ± 0.0 | 21 ± 1 | 0 ± 0 | 171 ± 5 | 0 % ± 0 | 12.6 ± 0.2 | 5.3 ± 0.1 | 1 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t5-final-creeper-mele-base semilla 1: sprint → blue (2/0 vivos), normal → blue (1/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 282 de 282 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t5-final-creeper-mele-base: enemy:explosion.player=158
- t5-final-creeper-mele-antes: enemy:explosion.player=155
- t5-final-creeper-escudo-base: enemy:explosion.player=63
- t5-final-creeper-escudo-antes: enemy:explosion.player=143
- t5-final-creeper-arco-base: enemy:explosion.player=89
- t5-final-creeper-arco-antes: enemy:explosion.player=148
- t5-final-creeper-linea-base: enemy:explosion.player=85 env:fall=1
- t5-final-creeper-linea-antes: enemy:explosion.player=155
- t5-final-creeper-cab-base: enemy:explosion.player=112 env:inWall=9
- t5-final-creeper-cab-antes: enemy:explosion.player=167 env:inWall=11

## Motivos de decisión (cambios registrados): ENGAGE 1303, FORMATION_HOLD 126, LOW_HEALTH_RETREAT 244, MORALE_SHAKEN 1779, MORALE_WAVERING 394, NONE 357, RALLIED 3, REFUSE_ATTACK 14, ROUTED_FLEE 128, SEEK_ENEMY 2208, STUCK 16, TOO_CLOSE 467

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t5-final-creeper-mele-base - 2 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t5-final-creeper-arco-antes - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t5-final-creeper-cab-antes - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t5-final-creeper-mele-antes - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t5-final-creeper-cab-base - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)

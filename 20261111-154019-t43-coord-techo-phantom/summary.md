# Simulación Rally - lote 20261111-154019-t43-coord-techo-phantom

Preset `t43-coord-techo-phantom` · velocidad `sprint` · 83 corridas · 8 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t43-techo-phantom-arco-base-r20-base | 8 | 100 % ± 0 | 0 % ± 0 | 7.6 ± 0.2 | 0.0 ± 0.0 | 42 ± 3 | 474 ± 4 | 29 ± 2 | 43 % ± 2 | 17.1 ± 2.4 | 5.1 ± 0.4 | 2 |
| t43-techo-phantom-arco-base-r20-sincro | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 37 ± 1 | 483 ± 3 | 22 ± 2 | 54 % ± 3 | 17.6 ± 2.5 | 5.4 ± 0.4 | 1 |
| t43-techo-phantom-arco-base-r35-base | 8 | 100 % ± 0 | 0 % ± 0 | 7.0 ± 0.2 | 0.0 ± 0.0 | 61 ± 8 | 839 ± 4 | 66 ± 3 | 50 % ± 2 | 17.4 ± 2.6 | 6.2 ± 0.4 | 1 |
| t43-techo-phantom-arco-base-r35-sincro | 8 | 100 % ± 0 | 0 % ± 0 | 6.6 ± 0.2 | 0.0 ± 0.0 | 55 ± 4 | 839 ± 4 | 64 ± 4 | 57 % ± 2 | 16.8 ± 1.3 | 7.3 ± 1.1 | 1 |
| t43-techo-phantom-linea-base-r20-base | 8 | 100 % ± 0 | 0 % ± 0 | 7.8 ± 0.2 | 0.0 ± 0.0 | 49 ± 4 | 482 ± 4 | 35 ± 3 | 49 % ± 4 | 18.4 ± 2.0 | 9.0 ± 0.5 | 2 |
| t43-techo-phantom-linea-base-r20-sincro | 7 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 41 ± 4 | 474 ± 3 | 26 ± 2 | 56 % ± 3 | 15.6 ± 2.9 | 10.3 ± 1.0 | 4 |
| t43-techo-phantom-linea-base-r35-base | 7 | 100 % ± 0 | 0 % ± 0 | 6.3 ± 0.2 | 0.0 ± 0.0 | 67 ± 3 | 826 ± 2 | 76 ± 2 | 56 % ± 2 | 12.6 ± 1.3 | 8.6 ± 0.4 | 5 |
| t43-techo-phantom-linea-base-r35-sincro | 7 | 100 % ± 0 | 0 % ± 0 | 6.4 ± 0.3 | 0.0 ± 0.0 | 63 ± 3 | 829 ± 5 | 70 ± 5 | 63 % ± 3 | 11.7 ± 0.8 | 10.4 ± 0.8 | 4 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t43-techo-phantom-arco-base-r20-base semilla 1: sprint → blue (8/0 vivos), normal → draw (7/1 vivos)
- t43-techo-phantom-arco-base-r20-base semilla 7778: sprint → ? (0/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 61 de 61 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t43-techo-phantom-arco-base-r20-base: enemy:mob=3
- t43-techo-phantom-arco-base-r35-base: enemy:mob=8
- t43-techo-phantom-arco-base-r35-sincro: enemy:mob=11
- t43-techo-phantom-linea-base-r20-base: enemy:mob=2
- t43-techo-phantom-linea-base-r35-base: enemy:mob=12
- t43-techo-phantom-linea-base-r35-sincro: enemy:mob=11

## Motivos de decisión (cambios registrados): COUNTERATTACK 3, ENGAGE 2961, LOW_HEALTH_RETREAT 208, MORALE_SHAKEN 235, MORALE_WAVERING 91, NONE 58, OUT_OF_RANGE 502, RALLIED 10, REFUSE_ATTACK 27, ROUTED_FLEE 24, SEEK_ENEMY 249, STUCK 44, TOO_CLOSE 1638

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t43-techo-phantom-linea-base-r20-base - 2 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t43-techo-phantom-linea-base-r35-base - 5 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t43-techo-phantom-linea-base-r35-sincro - 4 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t43-techo-phantom-linea-base-r20-sincro - 4 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t43-techo-phantom-arco-base-r20-base - 2 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t43-techo-phantom-arco-base-r35-base - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t43-techo-phantom-arco-base-r20-sincro - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t43-techo-phantom-arco-base-r35-sincro - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- t43-techo-phantom-arco-base-r20-base: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)

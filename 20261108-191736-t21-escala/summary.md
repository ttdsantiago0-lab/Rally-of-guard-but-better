# Simulación Rally - lote 20261108-191736-t21-escala

Preset `t21-escala` · velocidad `sprint` · 12 corridas · 6 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t21-escala-g25-z50 | 1 | 100 % ± 0 | 0 % ± 0 | 25.0 ± 0.0 | 0.0 ± 0.0 | 21 ± 0 | 1142 ± 0 | 38 ± 0 | 89 % ± 0 | 7.8 ± 0.0 | 8.4 ± 0.0 | 0 |
| t21-escala-g50-z100 | 1 | 100 % ± 0 | 0 % ± 0 | 50.0 ± 0.0 | 0.0 ± 0.0 | 21 ± 0 | 2328 ± 0 | 74 ± 0 | 91 % ± 0 | 8.6 ± 0.0 | 10.1 ± 0.0 | 0 |
| t21-escala-g100-z200 | 1 | 100 % ± 0 | 0 % ± 0 | 100.0 ± 0.0 | 0.0 ± 0.0 | 23 ± 0 | 4539 ± 0 | 66 ± 0 | 95 % ± 0 | 10.2 ± 0.0 | 11.0 ± 0.0 | 0 |
| t21-escala-g150-z300 | 1 | 100 % ± 0 | 0 % ± 0 | 150.0 ± 0.0 | 0.0 ± 0.0 | 26 ± 0 | 6797 ± 0 | 105 ± 0 | 83 % ± 0 | 11.2 ± 0.0 | 15.0 ± 0.0 | 0 |
| scale-50 | 1 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 11.0 ± 0.0 | 26 ± 0 | 372 ± 0 | 586 ± 0 | 74 % ± 0 | 20.4 ± 0.0 | 10.8 ± 0.0 | 0 |
| scale-100 | 1 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 22.0 ± 0.0 | 51 ± 0 | 770 ± 0 | 1210 ± 0 | 76 % ± 0 | 25.3 ± 0.0 | 9.8 ± 0.0 | 1 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t21-escala-g25-z50 semilla 1: sprint → blue (25/0 vivos), normal → blue (25/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 4 de 4 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- scale-50: enemy:arrow=22 enemy:mob=3
- scale-100: enemy:arrow=46 enemy:mob=4

## Motivos de decisión (cambios registrados): ENGAGE 3289, LOW_HEALTH_RETREAT 895, MORALE_SHAKEN 1294, MORALE_WAVERING 614, NONE 232, ORDER_OVERRIDE 339, OUT_OF_RANGE 1006, REFUSE_ATTACK 212, ROUTED_FLEE 664, SEEK_ENEMY 279, TOO_CLOSE 186, UNIT_BROKEN 206

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: scale-100 - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: scale-200 - 2 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: scale-400 - 2 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)

# Simulación Rally - lote 20261108-203908-t29-estructura-aldea

Preset `t29-estructura-aldea` · velocidad `sprint` · 82 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t29-estructura-aldea-zombie-linea | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 62 ± 5 | 382 ± 3 | 15 ± 2 | 67 % ± 3 | 7.7 ± 0.2 | 6.3 ± 0.4 | 1 |
| t29-estructura-aldea-skeleton-linea | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 42 ± 4 | 279 ± 3 | 29 ± 2 | 70 % ± 2 | 10.1 ± 0.6 | 7.5 ± 0.8 | 0 |
| t29-estructura-aldea-skeleton-arco | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 27 ± 2 | 283 ± 2 | 32 ± 2 | 58 % ± 1 | 8.2 ± 0.4 | 5.8 ± 0.3 | 0 |
| t29-estructura-aldea-vindicator-linea | 10 | 100 % ± 0 | 0 % ± 0 | 7.6 ± 0.2 | 0.0 ± 0.0 | 26 ± 3 | 154 ± 2 | 27 ± 5 | 56 % ± 4 | 8.9 ± 0.3 | 5.1 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t29-estructura-aldea-zombie-linea semilla 1: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 40 de 40 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t29-estructura-aldea-vindicator-linea: enemy:mob=4

## Motivos de decisión (cambios registrados): ENGAGE 1246, LOW_HEALTH_RETREAT 4, MORALE_SHAKEN 14, NONE 3, OUT_OF_RANGE 2, SEEK_ENEMY 26, STUCK 7, TOO_CLOSE 636

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t29-estructura-aldea-zombie-arco - 20 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t29-estructura-aldea-vindicator-arco - 20 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t29-estructura-aldea-zombie-linea - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)

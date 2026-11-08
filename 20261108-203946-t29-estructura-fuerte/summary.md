# Simulación Rally - lote 20261108-203946-t29-estructura-fuerte

Preset `t29-estructura-fuerte` · velocidad `sprint` · 62 corridas · 6 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t29-estructura-fuerte-zombie-linea | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 18 ± 0 | 383 ± 2 | 15 ± 2 | 82 % ± 1 | 9.0 ± 0.2 | 7.7 ± 0.4 | 0 |
| t29-estructura-fuerte-zombie-arco | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 16 ± 0 | 389 ± 4 | 0 ± 0 | 83 % ± 1 | 11.5 ± 0.3 | 5.3 ± 0.5 | 0 |
| t29-estructura-fuerte-skeleton-linea | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 20 ± 1 | 250 ± 4 | 19 ± 2 | 76 % ± 1 | 16.5 ± 0.5 | 7.6 ± 0.2 | 1 |
| t29-estructura-fuerte-skeleton-arco | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 16 ± 1 | 280 ± 4 | 20 ± 2 | 67 % ± 2 | 17.4 ± 0.9 | 7.2 ± 0.3 | 0 |
| t29-estructura-fuerte-vindicator-linea | 10 | 100 % ± 0 | 0 % ± 0 | 7.5 ± 0.2 | 0.0 ± 0.0 | 11 ± 0 | 159 ± 2 | 33 ± 5 | 68 % ± 2 | 9.7 ± 0.2 | 6.1 ± 0.2 | 0 |
| t29-estructura-fuerte-vindicator-arco | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 10 ± 0 | 160 ± 1 | 0 ± 0 | 65 % ± 2 | 13.7 ± 0.4 | 5.5 ± 0.1 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t29-estructura-fuerte-zombie-linea semilla 1: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 60 de 60 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t29-estructura-fuerte-vindicator-linea: enemy:mob=5

## Motivos de decisión (cambios registrados): ENGAGE 518, LOW_HEALTH_RETREAT 5, MORALE_SHAKEN 14, SEEK_ENEMY 8, TOO_CLOSE 25

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t29-estructura-fuerte-skeleton-linea - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)

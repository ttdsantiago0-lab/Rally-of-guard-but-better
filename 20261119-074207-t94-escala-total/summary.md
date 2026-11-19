# Simulación Rally - lote 20261119-074207-t94-escala-total

Preset `t94-escala-total` · velocidad `sprint` · 55 corridas · 5 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t94-scale-50 | 3 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 11.7 ± 0.7 | 25 ± 1 | 365 ± 22 | 568 ± 4 | 81 % ± 5 | 24.2 ± 1.8 | 9.1 ± 0.4 | 4 |
| t94-400-melee | 4 | 100 % ± 0 | 0 % ± 0 | 106.5 ± 8.3 | 0.0 ± 0.0 | 41 ± 3 | 4800 ± 0 | 3557 ± 72 | 0 % ± 0 | 13.4 ± 1.2 | 19.4 ± 0.2 | 4 |
| t94-400-arcos | 1 | 100 % ± 0 | 0 % ± 0 | 124.0 ± 0.0 | 0.0 ± 0.0 | 64 ± 0 | 4736 ± 0 | 3060 ± 0 | 59 % ± 0 | 48.8 ± 0.0 | 19.2 ± 0.0 | 9 |
| t94-scale-100 | 1 | 100 % ± 0 | 0 % ± 0 | 19.0 ± 0.0 | 0.0 ± 0.0 | 55 ± 0 | 1193 ± 0 | 825 ± 0 | 62 % ± 0 | 40.8 ± 0.0 | 10.7 ± 0.0 | 9 |
| t94-scale-200 | 1 | 100 % ± 0 | 0 % ± 0 | 39.0 ± 0.0 | 0.0 ± 0.0 | 52 ± 0 | 2375 ± 0 | 1735 ± 0 | 60 % ± 0 | 38.7 ± 0.0 | 12.1 ± 0.0 | 8 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t94-scale-50 semilla 9300: sprint → red (0/11 vivos), normal → red (0/11 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 7 de 7 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t94-scale-50: enemy:arrow=75
- t94-400-melee: enemy:mob=374
- t94-400-arcos: enemy:arrow=76
- t94-scale-100: enemy:arrow=31
- t94-scale-200: enemy:arrow=58 enemy:mob=3

## Motivos de decisión (cambios registrados): COUNTERATTACK 82, ENGAGE 24263, LOW_HEALTH_RETREAT 11652, MORALE_SHAKEN 10713, MORALE_WAVERING 4746, NONE 6038, ORDER_OVERRIDE 2961, OUT_OF_RANGE 12533, RALLIED 11, REFUSE_ATTACK 1542, ROUTED_FLEE 6107, SEEK_ENEMY 2360, STUCK 7, TOO_CLOSE 2, UNIT_BROKEN 2331

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t94-scale-100 - 9 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t94-scale-200 - 8 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t94-scale-400 - 10 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t94-400-melee - 4 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t94-400-arcos - 9 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t94-scale-50 - 4 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- t94-400-arcos: Rally cuesta 2.23 ms/tick de media

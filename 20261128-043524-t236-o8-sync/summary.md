# Simulación Rally - lote 20261128-043524-t236-o8-sync

Preset `t236-o8-sync` · velocidad `sprint` · 62 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t236-400-melee | 30 | 73 % ± 7 | 27 % ± 7 | 58.5 ± 7.5 | 11.3 ± 4.0 | 111 ± 9 | 4773 ± 44 | 4265 ± 95 | 0 % ± 0 | 13.5 ± 0.7 | 19.9 ± 0.3 | 0 |
| t236-400-melee-sync | 30 | 87 % ± 5 | 13 % ± 5 | 70.6 ± 7.1 | 8.1 ± 3.4 | 91 ± 11 | 4767 ± 29 | 4056 ± 94 | 0 % ± 0 | 15.0 ± 0.9 | 18.3 ± 0.3 | 1 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t236-400-melee semilla 23200: sprint → blue (20/0 vivos), normal → red (0/93 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 54 de 54 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t236-400-melee: enemy:mob=4244
- t236-400-melee-sync: enemy:mob=3881

## Motivos de decisión (cambios registrados): COUNTERATTACK 1508, ENGAGE 33872, LOW_HEALTH_RETREAT 28782, MORALE_SHAKEN 20376, MORALE_WAVERING 7509, NONE 7011, ORDER_OVERRIDE 810, RALLIED 2, REFUSE_ATTACK 926, ROUTED_FLEE 11171, SEEK_ENEMY 12881, STUCK 44, UNIT_BROKEN 4636

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t236-400-melee-sync - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- t236-400-melee: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)

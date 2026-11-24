# Simulación Rally - lote 20261124-035606-t173-closer

Preset `t173-closer` · velocidad `sprint` · 80 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t173-arcos-hoy | 2 | 50 % ± 50 | 50 % ± 50 | 62.0 ± 62.0 | 62.0 ± 62.0 | 57 ± 5 | 3916 ± 908 | 3836 ± 1015 | 64 % ± 9 | 44.0 ± 0.9 | 16.7 ± 1.2 | 18 |
| t173-arcos-reuse | 2 | 50 % ± 50 | 50 % ± 50 | 49.5 ± 49.5 | 54.0 ± 54.0 | 56 ± 4 | 4050 ± 731 | 4060 ± 701 | 65 % ± 6 | 44.8 ± 2.4 | 18.0 ± 1.4 | 16 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t173-arcos-hoy semilla 15500: sprint → draw (42/49 vivos), normal → draw (1/95 vivos)
- t173-arcos-hoy semilla 23277: sprint → red (0/124 vivos), normal → draw (64/17 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 2 de 2 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t173-arcos-hoy: enemy:arrow=276
- t173-arcos-reuse: enemy:arrow=301

## Motivos de decisión (cambios registrados): COUNTERATTACK 2, ENGAGE 55483, LOW_HEALTH_RETREAT 24260, MORALE_SHAKEN 20647, MORALE_WAVERING 10569, NONE 18773, ORDER_OVERRIDE 8172, OUT_OF_RANGE 42477, RALLIED 33, REFUSE_ATTACK 3766, ROUTED_FLEE 14345, SEEK_ENEMY 2165, STUCK 2, TOO_CLOSE 10, UNIT_BROKEN 5504

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t173-arcos-hoy - 18 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t173-mixto-hoy - 20 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t173-mixto-reuse - 20 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t173-arcos-reuse - 16 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- t173-arcos-hoy: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)

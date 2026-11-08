# Simulación Rally - lote 20261108-193150-t23-techo-phantom-orapunta

Preset `t23-techo-phantom-orapunta` · velocidad `sprint` · 33 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t23-techo-phantom-arco-orapunta-r10 | 14 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 28 ± 2 | 240 ± 2 | 13 ± 1 | 39 % ± 3 | 21.8 ± 3.1 | 4.3 ± 0.2 | 2 |
| t23-techo-phantom-linea-orapunta-r10 | 15 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 40 ± 2 | 237 ± 2 | 12 ± 1 | 35 % ± 1 | 26.3 ± 2.2 | 6.3 ± 0.3 | 1 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t23-techo-phantom-arco-orapunta-r10 semilla 1: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 29 de 29 corridas ✓

## Motivos de decisión (cambios registrados): ENGAGE 749, LOW_HEALTH_RETREAT 11, MORALE_SHAKEN 5, MORALE_WAVERING 4, NONE 20, OUT_OF_RANGE 262, RALLIED 1, REFUSE_ATTACK 2, ROUTED_FLEE 2, SEEK_ENEMY 45, STUCK 62, TOO_CLOSE 322

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t23-techo-phantom-linea-orapunta-r10 - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t23-techo-phantom-arco-orapunta-r10 - 2 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)

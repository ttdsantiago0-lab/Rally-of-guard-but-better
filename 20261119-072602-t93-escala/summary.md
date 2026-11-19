# Simulación Rally - lote 20261119-072602-t93-escala

Preset `t93-escala` · velocidad `sprint` · 35 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t93-scale-50 | 4 | 25 % ± 25 | 75 % ± 25 | 3.5 ± 3.5 | 6.8 ± 2.7 | 30 ± 2 | 437 ± 59 | 513 ± 58 | 77 % ± 6 | 28.1 ± 1.2 | 9.9 ± 2.2 | 3 |
| t93-scale-100 | 3 | 67 % ± 33 | 33 % ± 33 | 11.0 ± 5.6 | 5.0 ± 5.0 | 40 ± 4 | 1079 ± 77 | 1003 ± 90 | 69 % ± 5 | 33.6 ± 2.1 | 11.0 ± 1.2 | 4 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t93-scale-50 semilla 9100: sprint → red (0/5 vivos), normal → red (0/9 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 3 de 3 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t93-scale-50: enemy:arrow=85 enemy:mob=1
- t93-scale-100: enemy:arrow=117

## Motivos de decisión (cambios registrados): ENGAGE 13094, LOW_HEALTH_RETREAT 4464, MORALE_SHAKEN 5979, MORALE_WAVERING 2486, NONE 1907, ORDER_OVERRIDE 1500, OUT_OF_RANGE 5366, RALLIED 2, REFUSE_ATTACK 976, ROUTED_FLEE 2970, SEEK_ENEMY 618, STUCK 2, TOO_CLOSE 9, UNIT_BROKEN 1069

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t93-scale-200 - 10 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t93-scale-400 - 10 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t93-scale-50 - 3 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t93-scale-100 - 4 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)

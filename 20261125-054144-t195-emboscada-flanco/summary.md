# Simulación Rally - lote 20261125-054144-t195-emboscada-flanco

Preset `t195-emboscada-flanco` · velocidad `sprint` · 109 corridas · 3 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t195-vind-24-base | 30 | 73 % ± 7 | 27 % ± 7 | 5.4 ± 0.7 | 2.1 ± 0.6 | 51 ± 8 | 630 ± 6 | 177 ± 15 | 92 % ± 1 | 12.1 ± 1.1 | 13.1 ± 1.0 | 5 |
| t195-vind-24-flanco | 29 | 31 % ± 7 | 69 % ± 7 | 1.6 ± 0.4 | 6.2 ± 0.9 | 91 ± 6 | 578 ± 12 | 271 ± 9 | 94 % ± 1 | 14.2 ± 1.0 | 19.2 ± 1.1 | 8 |
| t195-vind-24-flanco-visto | 28 | 75 % ± 7 | 25 % ± 7 | 5.1 ± 0.7 | 1.6 ± 0.5 | 60 ± 8 | 635 ± 6 | 183 ± 16 | 90 % ± 1 | 15.2 ± 1.2 | 16.4 ± 1.0 | 8 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t195-vind-24-base semilla 16900: sprint → blue (6/0 vivos), normal → blue (6/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 62 de 62 corridas ✓

## Vínculos del plan (segundos ejecutados / saltados y por qué)
- t195-vind-24-flanco: linkSkipped:AMBUSH:missing_cap=62s linkStep:AMBUSH=206s
- t195-vind-24-flanco-visto: linkSkipped:AMBUSH:missing_cap=69s linkStep:AMBUSH=276s

## Causas de muerte del bando azul (causa:tipo=bajas)
- t195-vind-24-base: enemy:mob=199
- t195-vind-24-flanco: enemy:mob=303
- t195-vind-24-flanco-visto: enemy:mob=193

## Motivos de decisión (cambios registrados): ENGAGE 1608, FORMATION_HOLD 57, LOW_HEALTH_RETREAT 157, MORALE_SHAKEN 693, MORALE_WAVERING 382, NONE 2, ORDER_OVERRIDE 1563, OUTNUMBERED 240, OUT_OF_RANGE 15, ROUTED_FLEE 525, SEEK_ENEMY 546, TOO_CLOSE 27, UNIT_BROKEN 49

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t195-vind-24-flanco-visto - 8 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t195-vind-24-base - 5 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t195-vind-24-flanco - 8 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)

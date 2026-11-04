# Simulación Rally - lote 20261104-200301-t3-confirm-skeleton

Preset `t3-confirm-skeleton` · velocidad `sprint` · 151 corridas · 5 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t3-final-skeleton-escudo-base | 30 | 23 % ± 6 | 77 % ± 6 | 1.4 ± 0.4 | 4.8 ± 0.6 | 95 ± 4 | 225 ± 14 | 193 ± 5 | 0 % ± 0 | 8.1 ± 0.4 | 9.1 ± 0.7 | 0 |
| t3-final-skeleton-escudo-circulo | 30 | 58 % ± 7 | 42 % ± 7 | 4.4 ± 0.6 | 1.8 ± 0.3 | 95 ± 5 | 272 ± 12 | 143 ± 12 | 0 % ± 0 | 9.8 ± 0.5 | 8.9 ± 0.6 | 0 |
| t3-final-skeleton-escudo-escudoarriba | 30 | 52 % ± 3 | 48 % ± 3 | 5.3 ± 0.4 | 2.6 ± 0.3 | 119 ± 1 | 211 ± 11 | 165 ± 6 | 0 % ± 0 | 7.8 ± 0.4 | 7.7 ± 0.6 | 0 |
| t3-final-skeleton-mele-base | 30 | 77 % ± 8 | 23 % ± 8 | 5.3 ± 0.6 | 1.6 ± 0.6 | 35 ± 1 | 474 ± 10 | 134 ± 7 | 0 % ± 0 | 6.7 ± 0.2 | 8.6 ± 0.3 | 0 |
| t3-final-skeleton-mele-circulo | 30 | 90 % ± 6 | 10 % ± 6 | 6.2 ± 0.4 | 0.3 ± 0.2 | 34 ± 3 | 476 ± 4 | 121 ± 6 | 0 % ± 0 | 6.7 ± 0.3 | 8.0 ± 0.4 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t3-final-skeleton-escudo-base semilla 1001: sprint → blue (5/0 vivos), normal → red (0/8 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 113 de 113 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t3-final-skeleton-escudo-base: enemy:arrow=199
- t3-final-skeleton-escudo-circulo: enemy:arrow=107
- t3-final-skeleton-escudo-escudoarriba: enemy:arrow=80
- t3-final-skeleton-mele-base: enemy:arrow=80
- t3-final-skeleton-mele-circulo: enemy:arrow=53

## Motivos de decisión (cambios registrados): COUNTERATTACK 37, ENGAGE 5073, LOW_HEALTH_RETREAT 488, MORALE_SHAKEN 3850, MORALE_WAVERING 1219, NONE 27, RALLIED 19, REFUSE_ATTACK 1341, ROUTED_FLEE 842, SEEK_ENEMY 698

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t3-final-skeleton-escudo-escudoarriba: 27/30 corridas llegaron al tope de tiempo
- t3-final-skeleton-escudo-base: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)

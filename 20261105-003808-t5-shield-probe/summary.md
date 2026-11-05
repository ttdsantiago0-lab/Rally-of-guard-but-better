# Simulación Rally - lote 20261105-003808-t5-shield-probe

Preset `t5-shield-probe` · velocidad `sprint` · 19 corridas · 3 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t5-final-skeleton-escudo-base | 6 | 17 % ± 17 | 83 % ± 17 | 1.0 ± 1.0 | 5.3 ± 1.5 | 93 ± 8 | 176 ± 24 | 195 ± 11 | 0 % ± 0 | 7.3 ± 0.3 | 7.8 ± 0.6 | 0 |
| t5-final-skeleton-escudo-antes | 6 | 25 % ± 17 | 75 % ± 17 | 1.7 ± 1.3 | 3.8 ± 0.9 | 88 ± 11 | 227 ± 28 | 180 ± 22 | 0 % ± 0 | 7.4 ± 0.4 | 7.7 ± 1.3 | 0 |
| t3-final-skeleton-escudo-escudoarriba | 6 | 50 % ± 0 | 50 % ± 0 | 3.5 ± 1.0 | 3.5 ± 0.8 | 120 ± 0 | 212 ± 17 | 190 ± 11 | 0 % ± 0 | 7.8 ± 0.8 | 10.2 ± 2.1 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t5-final-skeleton-escudo-base semilla 1: sprint → red (0/10 vivos), normal → draw (2/2 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 9 de 9 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t5-final-skeleton-escudo-base: enemy:arrow=42
- t5-final-skeleton-escudo-antes: enemy:arrow=38
- t3-final-skeleton-escudo-escudoarriba: enemy:arrow=27

## Motivos de decisión (cambios registrados): COUNTERATTACK 10, ENGAGE 546, MORALE_SHAKEN 392, MORALE_WAVERING 167, RALLIED 2, REFUSE_ATTACK 208, ROUTED_FLEE 159, SEEK_ENEMY 91, UNIT_BROKEN 1

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t3-final-skeleton-escudo-escudoarriba: 6/6 corridas llegaron al tope de tiempo
- t5-final-skeleton-escudo-base: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)

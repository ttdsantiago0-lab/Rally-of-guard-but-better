# Simulación Rally - lote 20261105-001614-t5-check-shield

Preset `t5-check-shield` · velocidad `sprint` · 241 corridas · 8 escenarios.
> Mobs ajenos retirados del campo entre corridas (tercer bando oculto): {minecraft:zombie_villager=42}

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t5-final-zombie-escudo-base | 30 | 57 % ± 9 | 43 % ± 9 | 3.4 ± 0.6 | 4.1 ± 1.0 | 44 ± 2 | 730 ± 10 | 139 ± 9 | 0 % ± 0 | 3.9 ± 0.1 | 5.7 ± 0.2 | 0 |
| t5-final-zombie-escudo-antes | 30 | 47 % ± 9 | 53 % ± 9 | 2.8 ± 0.6 | 5.9 ± 1.1 | 47 ± 3 | 713 ± 12 | 147 ± 10 | 0 % ± 0 | 4.1 ± 0.2 | 6.5 ± 0.3 | 0 |
| t5-final-zombie-linea-base | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 19 ± 0 | 504 ± 2 | 33 ± 2 | 92 % ± 1 | 5.6 ± 0.1 | 7.3 ± 0.3 | 0 |
| t5-final-zombie-linea-antes | 30 | 57 % ± 9 | 43 % ± 9 | 3.1 ± 0.5 | 3.7 ± 0.9 | 51 ± 3 | 532 ± 9 | 138 ± 9 | 94 % ± 1 | 5.1 ± 0.3 | 8.0 ± 0.5 | 0 |
| t5-final-skeleton-escudo-base | 30 | 18 % ± 5 | 82 % ± 5 | 1.2 ± 0.4 | 4.5 ± 0.5 | 98 ± 3 | 208 ± 12 | 197 ± 4 | 0 % ± 0 | 7.5 ± 0.2 | 8.7 ± 0.5 | 0 |
| t5-final-skeleton-escudo-antes | 30 | 15 % ± 5 | 85 % ± 5 | 1.0 ± 0.4 | 4.8 ± 0.5 | 98 ± 3 | 221 ± 10 | 195 ± 5 | 0 % ± 0 | 8.1 ± 0.5 | 8.9 ± 0.4 | 0 |
| t5-final-skeleton-linea-base | 30 | 70 % ± 8 | 30 % ± 8 | 4.3 ± 0.6 | 1.3 ± 0.4 | 70 ± 5 | 479 ± 11 | 143 ± 9 | 95 % ± 1 | 11.6 ± 0.7 | 9.8 ± 0.7 | 0 |
| t5-final-skeleton-linea-antes | 30 | 45 % ± 9 | 55 % ± 9 | 2.6 ± 0.6 | 2.4 ± 0.5 | 72 ± 4 | 433 ± 12 | 165 ± 9 | 97 % ± 1 | 9.8 ± 0.4 | 9.1 ± 0.6 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t5-final-zombie-escudo-base semilla 1: sprint → blue (7/0 vivos), normal → red (0/21 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 132 de 132 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t5-final-zombie-escudo-base: enemy:mob=69
- t5-final-zombie-escudo-antes: enemy:mob=71
- t5-final-zombie-linea-antes: enemy:mob=70
- t5-final-skeleton-escudo-base: enemy:arrow=205
- t5-final-skeleton-escudo-antes: enemy:arrow=209
- t5-final-skeleton-linea-base: enemy:arrow=112
- t5-final-skeleton-linea-antes: enemy:arrow=163

## Motivos de decisión (cambios registrados): COUNTERATTACK 143, ENGAGE 7079, LOW_HEALTH_RETREAT 736, MORALE_SHAKEN 4294, MORALE_WAVERING 1822, NONE 18, OUT_OF_RANGE 20, RALLIED 34, REFUSE_ATTACK 1470, ROUTED_FLEE 1390, SEEK_ENEMY 517, TOO_CLOSE 703, UNIT_BROKEN 2

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t5-final-zombie-escudo-base: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)

# Simulación Rally - lote 20261128-033634-t229-reg-olas

Preset `t229-reg-olas` · velocidad `sprint` · 41 corridas · 5 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t229-sync-t8-waves-mixta-linea-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 265 ± 2 | 2165 ± 39 | 53 ± 4 | 85 % ± 0 | 12.3 ± 0.2 | 13.3 ± 0.2 | 0 |
| t229-sync-t8-waves-mixta-arco-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 246 ± 1 | 2404 ± 6 | 52 ± 2 | 84 % ± 1 | 15.1 ± 0.2 | 13.7 ± 0.8 | 0 |
| t229-sync-t8-waves-mixta-escudo-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 497 ± 21 | 1649 ± 14 | 135 ± 8 | 0 % ± 0 | 15.5 ± 0.9 | 15.9 ± 0.5 | 0 |
| t229-sync-t16-moral-linea-nueva | 8 | 100 % ± 0 | 0 % ± 0 | 7.8 ± 0.2 | 0.0 ± 0.0 | 340 ± 3 | 4360 ± 45 | 145 ± 9 | 87 % ± 0 | 13.0 ± 0.2 | 13.6 ± 0.4 | 0 |
| t229-sync-t16-moral-r0-nueva | 8 | 100 % ± 0 | 0 % ± 0 | 7.8 ± 0.2 | 0.0 ± 0.0 | 344 ± 3 | 4369 ± 38 | 159 ± 15 | 86 % ± 1 | 13.0 ± 0.4 | 12.3 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t229-sync-t8-waves-mixta-linea-comida-base semilla 21800: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 40 de 40 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t229-sync-t16-moral-linea-nueva: enemy:explosion.player=2
- t229-sync-t16-moral-r0-nueva: enemy:explosion.player=1

## Motivos de decisión (cambios registrados): ENGAGE 3975, LOW_HEALTH_RETREAT 28, MORALE_SHAKEN 354, MORALE_WAVERING 46, NONE 3045, OUT_OF_RANGE 57, RALLIED 9, REFUSE_ATTACK 3, ROUTED_FLEE 9, SEEK_ENEMY 901, STUCK 4, TOO_CLOSE 104

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.

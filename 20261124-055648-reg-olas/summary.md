# Simulación Rally - lote 20261124-055648-reg-olas

Preset `reg-olas` · velocidad `sprint` · 41 corridas · 5 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t8-waves-mixta-linea-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 266 ± 1 | 2222 ± 13 | 57 ± 3 | 85 % ± 1 | 12.3 ± 0.2 | 13.1 ± 0.3 | 0 |
| t8-waves-mixta-arco-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 245 ± 1 | 2399 ± 6 | 52 ± 4 | 84 % ± 1 | 15.5 ± 0.2 | 13.4 ± 0.4 | 0 |
| t8-waves-mixta-escudo-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 7.6 ± 0.4 | 0.0 ± 0.0 | 543 ± 29 | 1613 ± 27 | 170 ± 18 | 0 % ± 0 | 15.7 ± 1.2 | 16.0 ± 1.3 | 0 |
| t16-moral-linea-nueva | 8 | 100 % ± 0 | 0 % ± 0 | 7.6 ± 0.3 | 0.0 ± 0.0 | 349 ± 5 | 4449 ± 87 | 178 ± 7 | 86 % ± 1 | 13.1 ± 0.2 | 13.9 ± 0.3 | 0 |
| t16-moral-r0-nueva | 8 | 100 % ± 0 | 0 % ± 0 | 7.5 ± 0.3 | 0.0 ± 0.0 | 360 ± 10 | 4449 ± 51 | 185 ± 16 | 87 % ± 1 | 13.7 ± 0.6 | 13.1 ± 0.5 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t8-waves-mixta-linea-comida-base semilla 15600: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 40 de 40 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t8-waves-mixta-escudo-comida-base: enemy:explosion.player=2 enemy:mob=1
- t16-moral-linea-nueva: enemy:explosion.player=3
- t16-moral-r0-nueva: enemy:explosion.player=4

## Motivos de decisión (cambios registrados): COUNTERATTACK 2, ENGAGE 4146, LOW_HEALTH_RETREAT 30, MORALE_SHAKEN 537, MORALE_WAVERING 72, NONE 2993, OUT_OF_RANGE 96, RALLIED 12, REFUSE_ATTACK 4, ROUTED_FLEE 29, SEEK_ENEMY 855, STUCK 10, TOO_CLOSE 111

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.

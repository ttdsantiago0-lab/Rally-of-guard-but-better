# Simulación Rally - lote 20261108-203508-reg-olas

Preset `reg-olas` · velocidad `sprint` · 44 corridas · 5 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t8-waves-mixta-linea-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 297 ± 2 | 2135 ± 26 | 64 ± 4 | 79 % ± 1 | 13.6 ± 0.3 | 13.3 ± 0.3 | 0 |
| t8-waves-mixta-arco-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 277 ± 2 | 2404 ± 10 | 59 ± 2 | 78 % ± 1 | 15.2 ± 0.2 | 11.9 ± 0.5 | 0 |
| t8-waves-mixta-escudo-comida-base | 7 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 524 ± 21 | 1593 ± 15 | 166 ± 12 | 0 % ± 0 | 17.6 ± 1.0 | 16.7 ± 1.0 | 3 |
| t16-moral-linea-nueva | 8 | 100 % ± 0 | 0 % ± 0 | 7.8 ± 0.2 | 0.0 ± 0.0 | 391 ± 5 | 4175 ± 45 | 234 ± 6 | 81 % ± 1 | 14.0 ± 0.4 | 13.6 ± 0.5 | 1 |
| t16-moral-r0-nueva | 8 | 100 % ± 0 | 0 % ± 0 | 7.3 ± 0.5 | 0.0 ± 0.0 | 448 ± 44 | 4193 ± 78 | 253 ± 43 | 83 % ± 1 | 15.1 ± 1.8 | 14.4 ± 1.5 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t8-waves-mixta-linea-comida-base semilla 1: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 39 de 39 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t16-moral-linea-nueva: enemy:explosion.player=1
- t16-moral-r0-nueva: enemy:explosion.player=3 enemy:mob=2

## Motivos de decisión (cambios registrados): COUNTERATTACK 9, ENGAGE 4562, LOW_HEALTH_RETREAT 87, MORALE_SHAKEN 739, MORALE_WAVERING 141, NONE 3108, OUT_OF_RANGE 122, RALLIED 15, REFUSE_ATTACK 8, ROUTED_FLEE 49, SEEK_ENEMY 1013, STUCK 14, TOO_CLOSE 102

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t8-waves-mixta-escudo-comida-base - 3 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t16-moral-linea-nueva - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)

# Simulación Rally - lote 20261124-072336-reg-olas

Preset `reg-olas` · velocidad `sprint` · 44 corridas · 5 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t8-waves-mixta-linea-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 267 ± 1 | 2249 ± 28 | 55 ± 4 | 86 % ± 1 | 12.2 ± 0.1 | 12.6 ± 0.3 | 0 |
| t8-waves-mixta-arco-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 243 ± 1 | 2406 ± 11 | 54 ± 1 | 85 % ± 1 | 15.1 ± 0.1 | 13.8 ± 0.5 | 0 |
| t8-waves-mixta-escudo-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 533 ± 13 | 1655 ± 16 | 154 ± 9 | 0 % ± 0 | 16.8 ± 0.8 | 17.6 ± 0.7 | 3 |
| t16-moral-linea-nueva | 8 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 345 ± 4 | 4340 ± 48 | 160 ± 10 | 85 % ± 1 | 13.5 ± 0.3 | 14.0 ± 0.3 | 0 |
| t16-moral-r0-nueva | 8 | 94 % ± 6 | 6 % ± 6 | 7.0 ± 0.9 | 0.3 ± 0.3 | 393 ± 47 | 4390 ± 58 | 192 ± 41 | 87 % ± 1 | 13.4 ± 0.5 | 13.0 ± 0.4 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t8-waves-mixta-linea-comida-base semilla 15600: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 40 de 40 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t16-moral-linea-nueva: enemy:explosion.player=1
- t16-moral-r0-nueva: enemy:explosion.player=2 enemy:mob=3

## Motivos de decisión (cambios registrados): COUNTERATTACK 5, ENGAGE 4314, LOW_HEALTH_RETREAT 26, MORALE_SHAKEN 502, MORALE_WAVERING 86, NONE 3232, OUT_OF_RANGE 89, RALLIED 10, REFUSE_ATTACK 9, ROUTED_FLEE 26, SEEK_ENEMY 1005, STUCK 16, TOO_CLOSE 98

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t8-waves-mixta-escudo-comida-base - 3 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)

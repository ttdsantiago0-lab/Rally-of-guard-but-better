# Simulación Rally - lote 20261109-001228-reg-olas

Preset `reg-olas` · velocidad `sprint` · 42 corridas · 5 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t8-waves-mixta-linea-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 278 ± 3 | 2178 ± 16 | 61 ± 5 | 78 % ± 1 | 12.7 ± 0.4 | 13.1 ± 0.2 | 0 |
| t8-waves-mixta-arco-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 259 ± 2 | 2422 ± 7 | 50 ± 3 | 75 % ± 1 | 14.5 ± 0.3 | 13.0 ± 0.3 | 0 |
| t8-waves-mixta-escudo-comida-base | 7 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 532 ± 16 | 1633 ± 13 | 177 ± 8 | 0 % ± 0 | 16.0 ± 1.0 | 16.9 ± 0.9 | 2 |
| t16-moral-linea-nueva | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 361 ± 3 | 4438 ± 60 | 166 ± 16 | 80 % ± 1 | 13.5 ± 0.4 | 13.5 ± 0.3 | 0 |
| t16-moral-r0-nueva | 8 | 100 % ± 0 | 0 % ± 0 | 7.4 ± 0.3 | 0.0 ± 0.0 | 364 ± 4 | 4437 ± 79 | 178 ± 11 | 81 % ± 0 | 13.7 ± 0.4 | 13.0 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t8-waves-mixta-linea-comida-base semilla 1: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 39 de 39 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t8-waves-mixta-linea-comida-base: enemy:explosion.player=1
- t8-waves-mixta-escudo-comida-base: enemy:explosion.player=1
- t16-moral-r0-nueva: enemy:explosion.player=5

## Motivos de decisión (cambios registrados): COUNTERATTACK 1, ENGAGE 4339, LOW_HEALTH_RETREAT 52, MORALE_SHAKEN 641, MORALE_WAVERING 99, NONE 3087, OUT_OF_RANGE 75, RALLIED 14, REFUSE_ATTACK 8, ROUTED_FLEE 22, SEEK_ENEMY 991, STUCK 18, TOO_CLOSE 74

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t8-waves-mixta-escudo-comida-base - 2 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)

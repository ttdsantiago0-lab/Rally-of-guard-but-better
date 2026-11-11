# Simulación Rally - lote 20261111-214111-reg-olas

Preset `reg-olas` · velocidad `sprint` · 45 corridas · 5 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t8-waves-mixta-linea-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 271 ± 2 | 2219 ± 26 | 58 ± 2 | 89 % ± 1 | 12.7 ± 0.3 | 12.7 ± 0.2 | 0 |
| t8-waves-mixta-arco-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 246 ± 2 | 2393 ± 9 | 53 ± 3 | 94 % ± 1 | 14.7 ± 0.2 | 13.1 ± 0.3 | 0 |
| t8-waves-mixta-escudo-comida-base | 7 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 523 ± 9 | 1641 ± 14 | 159 ± 5 | 0 % ± 0 | 15.5 ± 1.4 | 14.1 ± 0.8 | 5 |
| t16-moral-linea-nueva | 8 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 352 ± 5 | 4460 ± 82 | 175 ± 14 | 90 % ± 1 | 13.8 ± 0.6 | 13.7 ± 0.6 | 0 |
| t16-moral-r0-nueva | 8 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 345 ± 4 | 4400 ± 53 | 177 ± 10 | 89 % ± 1 | 12.3 ± 0.4 | 12.7 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t8-waves-mixta-linea-comida-base semilla 1: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 39 de 39 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t16-moral-r0-nueva: enemy:explosion.player=1

## Motivos de decisión (cambios registrados): COUNTERATTACK 4, ENGAGE 4409, LOW_HEALTH_RETREAT 31, MORALE_SHAKEN 528, MORALE_WAVERING 79, NONE 3226, OUT_OF_RANGE 63, RALLIED 7, REFUSE_ATTACK 6, ROUTED_FLEE 18, SEEK_ENEMY 1095, STUCK 12, TOO_CLOSE 103

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t8-waves-mixta-escudo-comida-base - 5 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)

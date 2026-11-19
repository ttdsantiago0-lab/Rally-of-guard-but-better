# Simulación Rally - lote 20261119-045851-reg-olas

Preset `reg-olas` · velocidad `sprint` · 43 corridas · 5 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t8-waves-mixta-linea-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 268 ± 2 | 2257 ± 26 | 54 ± 4 | 86 % ± 1 | 12.4 ± 0.3 | 12.2 ± 0.3 | 0 |
| t8-waves-mixta-arco-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 244 ± 1 | 2389 ± 10 | 50 ± 2 | 83 % ± 1 | 15.5 ± 0.3 | 13.0 ± 0.6 | 0 |
| t8-waves-mixta-escudo-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 496 ± 10 | 1618 ± 20 | 159 ± 14 | 0 % ± 0 | 16.4 ± 1.4 | 16.7 ± 0.8 | 2 |
| t16-moral-linea-nueva | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 348 ± 4 | 4430 ± 54 | 157 ± 6 | 86 % ± 1 | 13.4 ± 0.4 | 13.1 ± 0.3 | 0 |
| t16-moral-r0-nueva | 8 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 351 ± 5 | 4393 ± 48 | 159 ± 15 | 86 % ± 1 | 13.2 ± 0.5 | 13.0 ± 0.5 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t8-waves-mixta-linea-comida-base semilla 8600: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 40 de 40 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t8-waves-mixta-escudo-comida-base: enemy:explosion.player=1
- t16-moral-r0-nueva: enemy:explosion.player=1

## Motivos de decisión (cambios registrados): ENGAGE 4332, LOW_HEALTH_RETREAT 50, MORALE_SHAKEN 536, MORALE_WAVERING 75, NONE 3164, OUT_OF_RANGE 88, RALLIED 12, REFUSE_ATTACK 11, ROUTED_FLEE 16, SEEK_ENEMY 1059, STUCK 10, TOO_CLOSE 104

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t8-waves-mixta-escudo-comida-base - 2 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)

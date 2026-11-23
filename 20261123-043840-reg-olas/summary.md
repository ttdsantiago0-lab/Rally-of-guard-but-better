# Simulación Rally - lote 20261123-043840-reg-olas

Preset `reg-olas` · velocidad `sprint` · 44 corridas · 5 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t8-waves-mixta-linea-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 265 ± 2 | 2144 ± 36 | 60 ± 4 | 84 % ± 1 | 12.7 ± 0.3 | 13.6 ± 0.3 | 0 |
| t8-waves-mixta-arco-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 244 ± 1 | 2389 ± 5 | 49 ± 3 | 85 % ± 1 | 15.7 ± 0.2 | 13.6 ± 0.3 | 0 |
| t8-waves-mixta-escudo-comida-base | 7 | 93 % ± 7 | 7 % ± 7 | 8.0 ± 0.0 | 1.1 ± 1.1 | 561 ± 44 | 1636 ± 20 | 176 ± 12 | 0 % ± 0 | 17.6 ± 1.8 | 18.5 ± 2.1 | 4 |
| t16-moral-linea-nueva | 8 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 347 ± 4 | 4450 ± 71 | 159 ± 15 | 86 % ± 1 | 13.6 ± 0.5 | 13.3 ± 0.3 | 0 |
| t16-moral-r0-nueva | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 338 ± 3 | 4389 ± 30 | 177 ± 9 | 88 % ± 1 | 11.8 ± 0.3 | 12.4 ± 0.4 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t8-waves-mixta-linea-comida-base semilla 12400: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 39 de 39 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t16-moral-linea-nueva: enemy:explosion.player=1

## Motivos de decisión (cambios registrados): COUNTERATTACK 3, ENGAGE 4201, LOW_HEALTH_RETREAT 29, MORALE_SHAKEN 398, MORALE_WAVERING 82, NONE 3172, OUT_OF_RANGE 72, RALLIED 8, REFUSE_ATTACK 13, ROUTED_FLEE 19, SEEK_ENEMY 950, STUCK 16, TOO_CLOSE 94

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t8-waves-mixta-escudo-comida-base - 4 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)

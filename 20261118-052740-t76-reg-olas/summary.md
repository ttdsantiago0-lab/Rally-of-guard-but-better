# Simulación Rally - lote 20261118-052740-t76-reg-olas

Preset `t76-reg-olas` · velocidad `sprint` · 41 corridas · 5 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t76-spread-t8-waves-mixta-linea-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 268 ± 2 | 2217 ± 37 | 57 ± 3 | 84 % ± 1 | 12.5 ± 0.2 | 13.3 ± 0.3 | 0 |
| t76-spread-t8-waves-mixta-arco-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 243 ± 1 | 2400 ± 10 | 52 ± 3 | 85 % ± 1 | 14.8 ± 0.3 | 12.4 ± 0.5 | 0 |
| t76-spread-t8-waves-mixta-escudo-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 501 ± 17 | 1636 ± 11 | 146 ± 8 | 0 % ± 0 | 15.3 ± 0.7 | 15.2 ± 1.1 | 0 |
| t76-spread-t16-moral-linea-nueva | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 350 ± 5 | 4424 ± 55 | 159 ± 3 | 85 % ± 1 | 13.6 ± 0.3 | 13.2 ± 0.2 | 0 |
| t76-spread-t16-moral-r0-nueva | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 356 ± 6 | 4571 ± 72 | 170 ± 6 | 86 % ± 1 | 13.0 ± 0.4 | 12.8 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t76-spread-t8-waves-mixta-linea-comida-base semilla 7500: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 40 de 40 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t76-spread-t8-waves-mixta-escudo-comida-base: enemy:explosion.player=1

## Motivos de decisión (cambios registrados): COUNTERATTACK 1, ENGAGE 3970, LOW_HEALTH_RETREAT 35, MORALE_SHAKEN 315, MORALE_WAVERING 42, NONE 3032, OUT_OF_RANGE 77, RALLIED 6, REFUSE_ATTACK 5, ROUTED_FLEE 8, SEEK_ENEMY 951, STUCK 15, TOO_CLOSE 93

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.

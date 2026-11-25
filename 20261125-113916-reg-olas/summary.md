# Simulación Rally - lote 20261125-113916-reg-olas

Preset `reg-olas` · velocidad `sprint` · 42 corridas · 5 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t8-waves-mixta-linea-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 267 ± 2 | 2244 ± 28 | 59 ± 4 | 87 % ± 0 | 11.9 ± 0.2 | 12.3 ± 0.3 | 0 |
| t8-waves-mixta-arco-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 244 ± 1 | 2395 ± 10 | 53 ± 2 | 84 % ± 1 | 15.5 ± 0.3 | 13.2 ± 0.5 | 0 |
| t8-waves-mixta-escudo-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 502 ± 26 | 1620 ± 14 | 154 ± 10 | 0 % ± 0 | 15.5 ± 1.7 | 14.5 ± 1.8 | 1 |
| t16-moral-linea-nueva | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 347 ± 3 | 4468 ± 76 | 164 ± 12 | 86 % ± 1 | 13.4 ± 0.4 | 13.4 ± 0.3 | 0 |
| t16-moral-r0-nueva | 8 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 355 ± 4 | 4462 ± 57 | 149 ± 13 | 85 % ± 1 | 13.6 ± 0.6 | 13.5 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t8-waves-mixta-linea-comida-base semilla 18400: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 40 de 40 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t16-moral-r0-nueva: enemy:explosion.player=1

## Motivos de decisión (cambios registrados): COUNTERATTACK 2, ENGAGE 4071, LOW_HEALTH_RETREAT 42, MORALE_SHAKEN 383, MORALE_WAVERING 76, NONE 3053, OUT_OF_RANGE 55, RALLIED 6, REFUSE_ATTACK 9, ROUTED_FLEE 13, SEEK_ENEMY 999, STUCK 9, TOO_CLOSE 95

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t8-waves-mixta-escudo-comida-base - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)

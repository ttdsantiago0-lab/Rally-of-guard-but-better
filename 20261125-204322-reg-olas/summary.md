# Simulación Rally - lote 20261125-204322-reg-olas

Preset `reg-olas` · velocidad `sprint` · 42 corridas · 5 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t8-waves-mixta-linea-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 268 ± 1 | 2254 ± 27 | 62 ± 5 | 85 % ± 1 | 12.1 ± 0.2 | 12.7 ± 0.3 | 0 |
| t8-waves-mixta-arco-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 243 ± 1 | 2413 ± 8 | 54 ± 2 | 85 % ± 1 | 15.5 ± 0.3 | 13.8 ± 0.4 | 0 |
| t8-waves-mixta-escudo-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 508 ± 23 | 1630 ± 11 | 151 ± 10 | 0 % ± 0 | 15.2 ± 1.2 | 15.4 ± 0.9 | 1 |
| t16-moral-linea-nueva | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 347 ± 3 | 4493 ± 39 | 176 ± 7 | 87 % ± 1 | 12.9 ± 0.4 | 13.9 ± 0.5 | 0 |
| t16-moral-r0-nueva | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 352 ± 4 | 4468 ± 78 | 148 ± 9 | 86 % ± 1 | 13.5 ± 0.2 | 13.3 ± 0.4 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t8-waves-mixta-linea-comida-base semilla 17400: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 40 de 40 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t8-waves-mixta-escudo-comida-base: enemy:mob=1

## Motivos de decisión (cambios registrados): ENGAGE 4085, LOW_HEALTH_RETREAT 39, MORALE_SHAKEN 410, MORALE_WAVERING 58, NONE 3076, OUT_OF_RANGE 69, RALLIED 4, REFUSE_ATTACK 6, ROUTED_FLEE 5, SEEK_ENEMY 925, STUCK 5, TOO_CLOSE 102

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t8-waves-mixta-escudo-comida-base - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)

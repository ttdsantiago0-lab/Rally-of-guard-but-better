# Simulación Rally - lote 20261111-163248-reg-olas

Preset `reg-olas` · velocidad `sprint` · 42 corridas · 5 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t8-waves-mixta-linea-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 271 ± 2 | 2226 ± 33 | 52 ± 3 | 90 % ± 1 | 12.6 ± 0.3 | 13.3 ± 0.3 | 0 |
| t8-waves-mixta-arco-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 247 ± 1 | 2403 ± 6 | 51 ± 3 | 93 % ± 1 | 15.0 ± 0.2 | 13.2 ± 0.5 | 0 |
| t8-waves-mixta-escudo-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 528 ± 14 | 1623 ± 15 | 177 ± 11 | 0 % ± 0 | 16.1 ± 0.9 | 16.9 ± 1.2 | 1 |
| t16-moral-linea-nueva | 8 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 345 ± 1 | 4433 ± 51 | 172 ± 18 | 90 % ± 1 | 12.9 ± 0.1 | 13.5 ± 0.2 | 0 |
| t16-moral-r0-nueva | 8 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 363 ± 7 | 4384 ± 70 | 166 ± 9 | 89 % ± 1 | 13.9 ± 0.7 | 13.5 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t8-waves-mixta-linea-comida-base semilla 1: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 40 de 40 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t8-waves-mixta-escudo-comida-base: enemy:explosion.player=1
- t16-moral-linea-nueva: enemy:explosion.player=1
- t16-moral-r0-nueva: enemy:mob=1

## Motivos de decisión (cambios registrados): COUNTERATTACK 2, ENGAGE 4126, LOW_HEALTH_RETREAT 44, MORALE_SHAKEN 485, MORALE_WAVERING 113, NONE 3056, OUT_OF_RANGE 62, RALLIED 13, REFUSE_ATTACK 12, ROUTED_FLEE 27, SEEK_ENEMY 909, STUCK 18, TOO_CLOSE 102

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t8-waves-mixta-escudo-comida-base - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)

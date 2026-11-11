# Simulación Rally - lote 20261111-223638-reg-olas

Preset `reg-olas` · velocidad `sprint` · 44 corridas · 5 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t8-waves-mixta-linea-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 268 ± 2 | 2195 ± 27 | 65 ± 2 | 90 % ± 1 | 12.3 ± 0.5 | 12.8 ± 0.5 | 0 |
| t8-waves-mixta-arco-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 247 ± 1 | 2394 ± 10 | 57 ± 3 | 93 % ± 1 | 15.8 ± 0.3 | 13.3 ± 0.4 | 0 |
| t8-waves-mixta-escudo-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 485 ± 18 | 1634 ± 19 | 147 ± 11 | 0 % ± 0 | 14.3 ± 1.5 | 14.7 ± 1.5 | 3 |
| t16-moral-linea-nueva | 8 | 100 % ± 0 | 0 % ± 0 | 7.8 ± 0.2 | 0.0 ± 0.0 | 356 ± 3 | 4478 ± 41 | 158 ± 10 | 88 % ± 1 | 13.9 ± 0.3 | 13.8 ± 0.4 | 0 |
| t16-moral-r0-nueva | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 355 ± 7 | 4400 ± 50 | 167 ± 6 | 90 % ± 1 | 13.7 ± 0.6 | 13.2 ± 0.5 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t8-waves-mixta-linea-comida-base semilla 1: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 40 de 40 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t16-moral-linea-nueva: enemy:explosion.player=2

## Motivos de decisión (cambios registrados): COUNTERATTACK 2, ENGAGE 4342, LOW_HEALTH_RETREAT 43, MORALE_SHAKEN 493, MORALE_WAVERING 91, NONE 3164, OUT_OF_RANGE 108, RALLIED 10, REFUSE_ATTACK 9, ROUTED_FLEE 20, SEEK_ENEMY 1030, STUCK 21, TOO_CLOSE 88

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t8-waves-mixta-escudo-comida-base - 3 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)

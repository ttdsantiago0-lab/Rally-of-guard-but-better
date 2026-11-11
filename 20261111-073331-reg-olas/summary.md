# Simulación Rally - lote 20261111-073331-reg-olas

Preset `reg-olas` · velocidad `sprint` · 44 corridas · 5 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t8-waves-mixta-linea-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 278 ± 2 | 2167 ± 31 | 60 ± 3 | 76 % ± 1 | 12.3 ± 0.2 | 13.1 ± 0.4 | 0 |
| t8-waves-mixta-arco-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 257 ± 1 | 2410 ± 14 | 52 ± 3 | 76 % ± 1 | 14.5 ± 0.3 | 11.5 ± 0.2 | 0 |
| t8-waves-mixta-escudo-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 535 ± 20 | 1643 ± 23 | 167 ± 16 | 0 % ± 0 | 17.4 ± 0.6 | 17.8 ± 0.8 | 3 |
| t16-moral-linea-nueva | 8 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 361 ± 4 | 4424 ± 68 | 181 ± 8 | 82 % ± 1 | 13.2 ± 0.3 | 13.2 ± 0.5 | 0 |
| t16-moral-r0-nueva | 8 | 100 % ± 0 | 0 % ± 0 | 7.8 ± 0.2 | 0.0 ± 0.0 | 361 ± 4 | 4320 ± 44 | 177 ± 11 | 80 % ± 1 | 13.4 ± 0.4 | 13.0 ± 0.3 | 0 |

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
- t16-moral-r0-nueva: enemy:explosion.player=2

## Motivos de decisión (cambios registrados): ENGAGE 4517, LOW_HEALTH_RETREAT 33, MORALE_SHAKEN 572, MORALE_WAVERING 96, NONE 3176, OUT_OF_RANGE 99, RALLIED 12, REFUSE_ATTACK 3, ROUTED_FLEE 29, SEEK_ENEMY 1169, STUCK 22, TOO_CLOSE 121

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t8-waves-mixta-escudo-comida-base - 3 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)

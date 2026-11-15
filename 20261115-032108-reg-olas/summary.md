# Simulación Rally - lote 20261115-032108-reg-olas

Preset `reg-olas` · velocidad `sprint` · 43 corridas · 5 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t8-waves-mixta-linea-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 269 ± 2 | 2236 ± 32 | 64 ± 4 | 89 % ± 1 | 12.7 ± 0.3 | 12.8 ± 0.3 | 0 |
| t8-waves-mixta-arco-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 246 ± 1 | 2384 ± 11 | 55 ± 3 | 93 % ± 0 | 15.5 ± 0.4 | 13.2 ± 0.5 | 0 |
| t16-moral-linea-nueva | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 341 ± 1 | 4326 ± 31 | 159 ± 11 | 91 % ± 1 | 12.8 ± 0.2 | 12.5 ± 0.3 | 0 |
| t16-moral-r0-nueva | 8 | 100 % ± 0 | 0 % ± 0 | 7.6 ± 0.2 | 0.0 ± 0.0 | 353 ± 5 | 4401 ± 50 | 179 ± 14 | 89 % ± 0 | 12.8 ± 0.4 | 13.4 ± 0.4 | 0 |
| t8-waves-mixta-escudo-comida-base | 7 | 100 % ± 0 | 0 % ± 0 | 7.7 ± 0.3 | 0.0 ± 0.0 | 515 ± 29 | 1635 ± 11 | 182 ± 21 | 0 % ± 0 | 16.1 ± 1.4 | 16.5 ± 1.0 | 3 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t8-waves-mixta-linea-comida-base semilla 5601: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 39 de 39 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t16-moral-r0-nueva: enemy:explosion.player=2
- t8-waves-mixta-escudo-comida-base: enemy:explosion.player=2

## Motivos de decisión (cambios registrados): ENGAGE 4138, LOW_HEALTH_RETREAT 52, MORALE_SHAKEN 449, MORALE_WAVERING 88, NONE 3030, OUT_OF_RANGE 40, RALLIED 6, REFUSE_ATTACK 11, ROUTED_FLEE 15, SEEK_ENEMY 957, STUCK 8, TOO_CLOSE 97

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t8-waves-mixta-escudo-comida-base - 3 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)

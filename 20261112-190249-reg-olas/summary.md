# Simulación Rally - lote 20261112-190249-reg-olas

Preset `reg-olas` · velocidad `sprint` · 45 corridas · 5 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t8-waves-mixta-linea-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 7.8 ± 0.2 | 0.0 ± 0.0 | 271 ± 2 | 2225 ± 32 | 67 ± 6 | 88 % ± 1 | 12.6 ± 0.2 | 12.9 ± 0.3 | 0 |
| t8-waves-mixta-arco-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 245 ± 1 | 2382 ± 12 | 54 ± 3 | 94 % ± 0 | 15.1 ± 0.3 | 13.4 ± 0.4 | 0 |
| t8-waves-mixta-escudo-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 534 ± 22 | 1643 ± 13 | 158 ± 6 | 0 % ± 0 | 15.9 ± 1.4 | 16.6 ± 1.4 | 4 |
| t16-moral-linea-nueva | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 344 ± 3 | 4355 ± 72 | 161 ± 10 | 89 % ± 1 | 13.0 ± 0.3 | 13.6 ± 0.4 | 0 |
| t16-moral-r0-nueva | 8 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 349 ± 2 | 4401 ± 45 | 163 ± 11 | 89 % ± 1 | 12.9 ± 0.3 | 12.9 ± 0.4 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t8-waves-mixta-linea-comida-base semilla 1: sprint → blue (7/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 40 de 40 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t8-waves-mixta-linea-comida-base: enemy:explosion.player=2
- t16-moral-r0-nueva: enemy:explosion.player=1

## Motivos de decisión (cambios registrados): COUNTERATTACK 2, ENGAGE 4380, LOW_HEALTH_RETREAT 30, MORALE_SHAKEN 379, MORALE_WAVERING 70, NONE 3273, OUT_OF_RANGE 69, RALLIED 12, REFUSE_ATTACK 10, ROUTED_FLEE 17, SEEK_ENEMY 1118, STUCK 31, TOO_CLOSE 99

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t8-waves-mixta-escudo-comida-base - 4 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)

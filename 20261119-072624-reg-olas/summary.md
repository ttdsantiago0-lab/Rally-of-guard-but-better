# Simulación Rally - lote 20261119-072624-reg-olas

Preset `reg-olas` · velocidad `sprint` · 42 corridas · 5 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t8-waves-mixta-linea-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 268 ± 1 | 2203 ± 25 | 59 ± 4 | 85 % ± 1 | 12.2 ± 0.2 | 13.1 ± 0.2 | 0 |
| t8-waves-mixta-arco-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 242 ± 1 | 2388 ± 8 | 48 ± 2 | 85 % ± 1 | 15.0 ± 0.3 | 13.1 ± 0.8 | 0 |
| t8-waves-mixta-escudo-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 521 ± 19 | 1666 ± 14 | 157 ± 13 | 0 % ± 0 | 15.5 ± 1.2 | 16.5 ± 1.1 | 1 |
| t16-moral-linea-nueva | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 341 ± 2 | 4390 ± 43 | 171 ± 8 | 87 % ± 1 | 12.8 ± 0.3 | 13.3 ± 0.5 | 0 |
| t16-moral-r0-nueva | 8 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 348 ± 2 | 4377 ± 62 | 165 ± 6 | 87 % ± 1 | 13.0 ± 0.3 | 12.7 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t8-waves-mixta-linea-comida-base semilla 9100: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 40 de 40 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t16-moral-r0-nueva: enemy:explosion.player=1

## Motivos de decisión (cambios registrados): ENGAGE 4052, LOW_HEALTH_RETREAT 39, MORALE_SHAKEN 342, MORALE_WAVERING 63, NONE 3064, OUT_OF_RANGE 59, RALLIED 3, REFUSE_ATTACK 10, ROUTED_FLEE 4, SEEK_ENEMY 1009, STUCK 19, TOO_CLOSE 101

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t8-waves-mixta-escudo-comida-base - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)

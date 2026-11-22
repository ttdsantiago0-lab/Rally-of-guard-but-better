# Simulación Rally - lote 20261122-072918-reg-olas

Preset `reg-olas` · velocidad `sprint` · 44 corridas · 5 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t8-waves-mixta-linea-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 263 ± 1 | 2184 ± 37 | 56 ± 3 | 85 % ± 1 | 12.2 ± 0.4 | 13.3 ± 0.3 | 0 |
| t8-waves-mixta-arco-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 244 ± 1 | 2385 ± 9 | 48 ± 2 | 86 % ± 0 | 14.9 ± 0.3 | 13.3 ± 0.3 | 0 |
| t8-waves-mixta-escudo-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 523 ± 22 | 1654 ± 8 | 156 ± 11 | 0 % ± 0 | 15.1 ± 1.2 | 16.0 ± 1.0 | 3 |
| t16-moral-linea-nueva | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 343 ± 3 | 4412 ± 43 | 147 ± 15 | 87 % ± 1 | 13.2 ± 0.3 | 13.7 ± 0.4 | 0 |
| t16-moral-r0-nueva | 8 | 100 % ± 0 | 0 % ± 0 | 7.6 ± 0.3 | 0.0 ± 0.0 | 353 ± 3 | 4503 ± 56 | 166 ± 10 | 87 % ± 1 | 13.0 ± 0.3 | 13.2 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t8-waves-mixta-linea-comida-base semilla 10900: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 40 de 40 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t16-moral-r0-nueva: enemy:explosion.player=2

## Motivos de decisión (cambios registrados): ENGAGE 4266, LOW_HEALTH_RETREAT 29, MORALE_SHAKEN 420, MORALE_WAVERING 80, NONE 3181, OUT_OF_RANGE 61, RALLIED 11, REFUSE_ATTACK 9, ROUTED_FLEE 13, SEEK_ENEMY 980, STUCK 26, TOO_CLOSE 105

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t8-waves-mixta-escudo-comida-base - 3 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)

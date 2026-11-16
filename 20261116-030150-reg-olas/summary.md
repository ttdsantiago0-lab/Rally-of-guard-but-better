# Simulación Rally - lote 20261116-030150-reg-olas

Preset `reg-olas` · velocidad `sprint` · 43 corridas · 5 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t8-waves-mixta-linea-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 7.8 ± 0.2 | 0.0 ± 0.0 | 267 ± 1 | 2206 ± 32 | 70 ± 6 | 91 % ± 1 | 12.1 ± 0.3 | 13.1 ± 0.3 | 0 |
| t8-waves-mixta-arco-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 246 ± 1 | 2369 ± 9 | 52 ± 3 | 93 % ± 1 | 15.1 ± 0.3 | 13.6 ± 0.5 | 0 |
| t8-waves-mixta-escudo-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 509 ± 12 | 1654 ± 15 | 161 ± 9 | 0 % ± 0 | 15.0 ± 1.0 | 16.1 ± 1.0 | 2 |
| t16-moral-linea-nueva | 8 | 100 % ± 0 | 0 % ± 0 | 7.8 ± 0.2 | 0.0 ± 0.0 | 348 ± 4 | 4422 ± 77 | 165 ± 11 | 90 % ± 1 | 13.3 ± 0.2 | 13.2 ± 0.3 | 0 |
| t16-moral-r0-nueva | 8 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 345 ± 5 | 4328 ± 76 | 160 ± 5 | 89 % ± 1 | 12.7 ± 0.4 | 12.6 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t8-waves-mixta-linea-comida-base semilla 5601: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 40 de 40 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t8-waves-mixta-linea-comida-base: enemy:explosion.player=2
- t16-moral-linea-nueva: enemy:explosion.player=2
- t16-moral-r0-nueva: enemy:explosion.player=1

## Motivos de decisión (cambios registrados): ENGAGE 4307, LOW_HEALTH_RETREAT 54, MORALE_SHAKEN 462, MORALE_WAVERING 100, NONE 3178, OUT_OF_RANGE 87, RALLIED 10, REFUSE_ATTACK 13, ROUTED_FLEE 12, SEEK_ENEMY 1038, STUCK 8, TOO_CLOSE 117

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t8-waves-mixta-escudo-comida-base - 2 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)

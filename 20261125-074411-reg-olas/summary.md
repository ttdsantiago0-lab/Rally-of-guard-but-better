# Simulación Rally - lote 20261125-074411-reg-olas

Preset `reg-olas` · velocidad `sprint` · 43 corridas · 5 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t8-waves-mixta-linea-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 267 ± 2 | 2231 ± 18 | 63 ± 4 | 85 % ± 1 | 12.3 ± 0.3 | 13.2 ± 0.1 | 0 |
| t8-waves-mixta-arco-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 242 ± 1 | 2396 ± 10 | 52 ± 4 | 86 % ± 0 | 14.6 ± 0.2 | 13.0 ± 0.5 | 0 |
| t8-waves-mixta-escudo-comida-base | 7 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 496 ± 15 | 1661 ± 16 | 160 ± 13 | 0 % ± 0 | 13.8 ± 0.8 | 14.9 ± 1.3 | 3 |
| t16-moral-linea-nueva | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 349 ± 2 | 4434 ± 44 | 162 ± 7 | 86 % ± 1 | 13.3 ± 0.4 | 13.5 ± 0.3 | 0 |
| t16-moral-r0-nueva | 8 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 348 ± 3 | 4465 ± 52 | 169 ± 15 | 88 % ± 1 | 12.5 ± 0.3 | 12.1 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t8-waves-mixta-linea-comida-base semilla 17600: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 39 de 39 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t16-moral-r0-nueva: enemy:explosion.player=1

## Motivos de decisión (cambios registrados): ENGAGE 4421, LOW_HEALTH_RETREAT 37, MORALE_SHAKEN 548, MORALE_WAVERING 70, NONE 3180, OUT_OF_RANGE 82, RALLIED 11, REFUSE_ATTACK 10, ROUTED_FLEE 11, SEEK_ENEMY 1178, STUCK 16, TOO_CLOSE 102

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t8-waves-mixta-escudo-comida-base - 3 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)

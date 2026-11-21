# Simulación Rally - lote 20261121-034216-reg-olas

Preset `reg-olas` · velocidad `sprint` · 46 corridas · 5 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t8-waves-mixta-linea-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 265 ± 2 | 2183 ± 34 | 61 ± 3 | 84 % ± 1 | 12.0 ± 0.2 | 12.9 ± 0.2 | 0 |
| t8-waves-mixta-arco-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 242 ± 0 | 2399 ± 6 | 56 ± 2 | 86 % ± 1 | 15.3 ± 0.2 | 13.1 ± 0.3 | 0 |
| t8-waves-mixta-escudo-comida-base | 5 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 525 ± 15 | 1657 ± 21 | 153 ± 7 | 0 % ± 0 | 16.4 ± 1.1 | 15.7 ± 1.3 | 7 |
| t16-moral-linea-nueva | 8 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 345 ± 4 | 4397 ± 38 | 175 ± 12 | 87 % ± 1 | 13.3 ± 0.5 | 13.4 ± 0.2 | 0 |
| t16-moral-r0-nueva | 8 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 341 ± 3 | 4348 ± 45 | 156 ± 12 | 88 % ± 1 | 12.5 ± 0.4 | 12.4 ± 0.4 | 1 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t8-waves-mixta-linea-comida-base semilla 10400: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 37 de 37 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t16-moral-linea-nueva: enemy:explosion.player=1
- t16-moral-r0-nueva: enemy:explosion.player=1

## Motivos de decisión (cambios registrados): COUNTERATTACK 40, ENGAGE 4478, LOW_HEALTH_RETREAT 50, MORALE_SHAKEN 517, MORALE_WAVERING 103, NONE 3276, OUT_OF_RANGE 77, RALLIED 18, REFUSE_ATTACK 14, ROUTED_FLEE 78, SEEK_ENEMY 1138, STUCK 20, TOO_CLOSE 91

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t8-waves-mixta-escudo-comida-base - 7 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t16-moral-r0-nueva - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)

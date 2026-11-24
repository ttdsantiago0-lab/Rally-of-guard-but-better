# Simulación Rally - lote 20261124-034449-reg-olas

Preset `reg-olas` · velocidad `sprint` · 42 corridas · 5 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t8-waves-mixta-linea-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 265 ± 2 | 2203 ± 39 | 65 ± 5 | 85 % ± 1 | 12.2 ± 0.2 | 12.6 ± 0.2 | 0 |
| t8-waves-mixta-arco-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 245 ± 1 | 2391 ± 11 | 59 ± 2 | 84 % ± 1 | 14.9 ± 0.3 | 13.9 ± 0.3 | 0 |
| t8-waves-mixta-escudo-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 507 ± 17 | 1672 ± 22 | 165 ± 7 | 0 % ± 0 | 15.3 ± 0.9 | 15.4 ± 0.6 | 1 |
| t16-moral-linea-nueva | 8 | 100 % ± 0 | 0 % ± 0 | 7.8 ± 0.2 | 0.0 ± 0.0 | 350 ± 5 | 4442 ± 57 | 161 ± 9 | 86 % ± 1 | 13.2 ± 0.5 | 13.0 ± 0.4 | 0 |
| t16-moral-r0-nueva | 8 | 100 % ± 0 | 0 % ± 0 | 7.5 ± 0.2 | 0.0 ± 0.0 | 349 ± 5 | 4365 ± 65 | 170 ± 13 | 86 % ± 1 | 13.2 ± 0.4 | 12.5 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t8-waves-mixta-linea-comida-base semilla 15400: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 40 de 40 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t16-moral-linea-nueva: enemy:explosion.player=2
- t16-moral-r0-nueva: enemy:explosion.player=3 enemy:mob=1

## Motivos de decisión (cambios registrados): ENGAGE 4188, LOW_HEALTH_RETREAT 40, MORALE_SHAKEN 490, MORALE_WAVERING 47, NONE 3122, OUT_OF_RANGE 66, RALLIED 7, REFUSE_ATTACK 7, ROUTED_FLEE 7, SEEK_ENEMY 990, STUCK 17, TOO_CLOSE 112

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t8-waves-mixta-escudo-comida-base - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)

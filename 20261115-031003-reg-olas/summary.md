# Simulación Rally - lote 20261115-031003-reg-olas

Preset `reg-olas` · velocidad `sprint` · 42 corridas · 5 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t8-waves-mixta-linea-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 267 ± 2 | 2196 ± 35 | 65 ± 3 | 90 % ± 1 | 12.0 ± 0.3 | 12.5 ± 0.3 | 0 |
| t8-waves-mixta-arco-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 247 ± 1 | 2384 ± 9 | 51 ± 2 | 93 % ± 1 | 15.3 ± 0.4 | 13.5 ± 0.5 | 0 |
| t8-waves-mixta-escudo-comida-base | 7 | 93 % ± 7 | 7 % ± 7 | 7.6 ± 0.4 | 1.0 ± 1.0 | 528 ± 39 | 1597 ± 17 | 164 ± 31 | 0 % ± 0 | 15.0 ± 1.1 | 15.1 ± 0.9 | 2 |
| t16-moral-linea-nueva | 8 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 351 ± 4 | 4407 ± 57 | 173 ± 16 | 89 % ± 1 | 13.5 ± 0.4 | 13.9 ± 0.4 | 0 |
| t16-moral-r0-nueva | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 344 ± 3 | 4414 ± 48 | 159 ± 8 | 90 % ± 1 | 12.5 ± 0.3 | 12.7 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t8-waves-mixta-linea-comida-base semilla 5601: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 39 de 39 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t8-waves-mixta-escudo-comida-base: enemy:mob=2
- t16-moral-linea-nueva: enemy:explosion.player=1

## Motivos de decisión (cambios registrados): COUNTERATTACK 4, ENGAGE 3987, LOW_HEALTH_RETREAT 22, MORALE_SHAKEN 386, MORALE_WAVERING 63, NONE 3013, OUT_OF_RANGE 72, RALLIED 8, REFUSE_ATTACK 3, ROUTED_FLEE 20, SEEK_ENEMY 887, STUCK 11, TOO_CLOSE 126

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t8-waves-mixta-escudo-comida-base - 2 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)

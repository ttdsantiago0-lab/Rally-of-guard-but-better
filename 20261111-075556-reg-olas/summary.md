# Simulación Rally - lote 20261111-075556-reg-olas

Preset `reg-olas` · velocidad `sprint` · 42 corridas · 5 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t8-waves-mixta-linea-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 276 ± 3 | 2147 ± 43 | 52 ± 6 | 78 % ± 1 | 12.9 ± 0.4 | 12.8 ± 0.4 | 0 |
| t8-waves-mixta-arco-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 259 ± 1 | 2435 ± 7 | 50 ± 2 | 76 % ± 1 | 14.7 ± 0.2 | 12.1 ± 0.3 | 0 |
| t8-waves-mixta-escudo-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 7.5 ± 0.4 | 0.0 ± 0.0 | 563 ± 29 | 1636 ± 22 | 211 ± 26 | 0 % ± 0 | 16.6 ± 1.4 | 17.2 ± 1.6 | 1 |
| t16-moral-linea-nueva | 8 | 100 % ± 0 | 0 % ± 0 | 7.8 ± 0.2 | 0.0 ± 0.0 | 352 ± 8 | 4357 ± 73 | 172 ± 11 | 81 % ± 1 | 13.3 ± 0.6 | 14.2 ± 0.6 | 0 |
| t16-moral-r0-nueva | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 361 ± 4 | 4436 ± 54 | 164 ± 12 | 81 % ± 0 | 12.5 ± 0.3 | 12.7 ± 0.4 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t8-waves-mixta-linea-comida-base semilla 1: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 40 de 40 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t8-waves-mixta-escudo-comida-base: enemy:explosion.player=3
- t16-moral-linea-nueva: enemy:explosion.player=2

## Motivos de decisión (cambios registrados): COUNTERATTACK 7, ENGAGE 4154, LOW_HEALTH_RETREAT 45, MORALE_SHAKEN 428, MORALE_WAVERING 92, NONE 3048, OUT_OF_RANGE 86, RALLIED 10, REFUSE_ATTACK 5, ROUTED_FLEE 35, SEEK_ENEMY 882, STUCK 60, TOO_CLOSE 76

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t8-waves-mixta-escudo-comida-base - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)

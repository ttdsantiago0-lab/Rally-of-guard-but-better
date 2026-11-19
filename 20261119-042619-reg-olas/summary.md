# Simulación Rally - lote 20261119-042619-reg-olas

Preset `reg-olas` · velocidad `sprint` · 44 corridas · 5 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t8-waves-mixta-linea-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 270 ± 2 | 2209 ± 27 | 65 ± 4 | 90 % ± 1 | 12.3 ± 0.3 | 12.9 ± 0.2 | 0 |
| t8-waves-mixta-arco-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 247 ± 1 | 2378 ± 11 | 50 ± 3 | 93 % ± 1 | 15.8 ± 0.2 | 13.9 ± 0.4 | 0 |
| t8-waves-mixta-escudo-comida-base | 7 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 486 ± 11 | 1622 ± 19 | 152 ± 4 | 0 % ± 0 | 14.0 ± 0.7 | 14.7 ± 0.8 | 3 |
| t16-moral-linea-nueva | 8 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 347 ± 5 | 4395 ± 50 | 153 ± 11 | 89 % ± 1 | 13.4 ± 0.5 | 13.5 ± 0.4 | 1 |
| t16-moral-r0-nueva | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 351 ± 3 | 4419 ± 69 | 172 ± 11 | 89 % ± 1 | 13.3 ± 0.4 | 12.9 ± 0.5 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t8-waves-mixta-linea-comida-base semilla 8300: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 39 de 39 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t8-waves-mixta-escudo-comida-base: enemy:explosion.player=1
- t16-moral-linea-nueva: enemy:explosion.player=1

## Motivos de decisión (cambios registrados): COUNTERATTACK 3, ENGAGE 4332, LOW_HEALTH_RETREAT 41, MORALE_SHAKEN 505, MORALE_WAVERING 86, NONE 3198, OUT_OF_RANGE 58, RALLIED 9, REFUSE_ATTACK 8, ROUTED_FLEE 30, SEEK_ENEMY 1000, STUCK 16, TOO_CLOSE 85

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t16-moral-linea-nueva - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t8-waves-mixta-escudo-comida-base - 3 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)

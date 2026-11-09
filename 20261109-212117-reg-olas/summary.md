# Simulación Rally - lote 20261109-212117-reg-olas

Preset `reg-olas` · velocidad `sprint` · 45 corridas · 5 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t8-waves-mixta-linea-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 278 ± 2 | 2216 ± 32 | 57 ± 2 | 77 % ± 1 | 13.1 ± 0.3 | 13.1 ± 0.3 | 0 |
| t8-waves-mixta-arco-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 256 ± 1 | 2402 ± 13 | 50 ± 2 | 75 % ± 1 | 14.8 ± 0.2 | 13.2 ± 0.5 | 0 |
| t8-waves-mixta-escudo-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 543 ± 18 | 1644 ± 20 | 167 ± 15 | 0 % ± 0 | 18.4 ± 1.3 | 20.0 ± 0.8 | 3 |
| t16-moral-linea-nueva | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 358 ± 6 | 4434 ± 42 | 175 ± 8 | 82 % ± 1 | 13.1 ± 0.5 | 13.6 ± 0.5 | 0 |
| t16-moral-r0-nueva | 8 | 100 % ± 0 | 0 % ± 0 | 7.6 ± 0.3 | 0.0 ± 0.0 | 365 ± 4 | 4426 ± 60 | 191 ± 13 | 80 % ± 1 | 13.1 ± 0.3 | 13.2 ± 0.4 | 1 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t8-waves-mixta-linea-comida-base semilla 1: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 40 de 40 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t16-moral-r0-nueva: enemy:explosion.player=3

## Motivos de decisión (cambios registrados): COUNTERATTACK 1, ENGAGE 4543, LOW_HEALTH_RETREAT 59, MORALE_SHAKEN 680, MORALE_WAVERING 121, NONE 3225, OUT_OF_RANGE 94, RALLIED 11, REFUSE_ATTACK 12, ROUTED_FLEE 28, SEEK_ENEMY 974, STUCK 10, TOO_CLOSE 91

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t16-moral-r0-nueva - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t8-waves-mixta-escudo-comida-base - 3 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)

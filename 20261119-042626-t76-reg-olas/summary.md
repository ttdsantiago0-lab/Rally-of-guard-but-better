# Simulación Rally - lote 20261119-042626-t76-reg-olas

Preset `t76-reg-olas` · velocidad `sprint` · 42 corridas · 5 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t76-spread-t8-waves-mixta-linea-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 269 ± 1 | 2221 ± 36 | 51 ± 4 | 84 % ± 1 | 12.6 ± 0.2 | 13.1 ± 0.2 | 0 |
| t76-spread-t8-waves-mixta-arco-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 243 ± 1 | 2392 ± 12 | 50 ± 3 | 84 % ± 1 | 15.0 ± 0.3 | 13.3 ± 0.4 | 0 |
| t76-spread-t8-waves-mixta-escudo-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 492 ± 20 | 1631 ± 17 | 150 ± 10 | 0 % ± 0 | 15.6 ± 1.7 | 15.8 ± 1.2 | 1 |
| t76-spread-t16-moral-linea-nueva | 8 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 341 ± 1 | 4384 ± 64 | 159 ± 14 | 87 % ± 0 | 13.1 ± 0.2 | 13.6 ± 0.3 | 0 |
| t76-spread-t16-moral-r0-nueva | 8 | 100 % ± 0 | 0 % ± 0 | 7.8 ± 0.2 | 0.0 ± 0.0 | 349 ± 8 | 4448 ± 36 | 184 ± 13 | 87 % ± 1 | 12.8 ± 0.7 | 12.8 ± 0.6 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t76-spread-t8-waves-mixta-linea-comida-base semilla 8300: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 40 de 40 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t76-spread-t8-waves-mixta-escudo-comida-base: enemy:explosion.player=1
- t76-spread-t16-moral-linea-nueva: enemy:explosion.player=1
- t76-spread-t16-moral-r0-nueva: enemy:explosion.player=2

## Motivos de decisión (cambios registrados): COUNTERATTACK 1, ENGAGE 3997, LOW_HEALTH_RETREAT 35, MORALE_SHAKEN 387, MORALE_WAVERING 56, NONE 3029, OUT_OF_RANGE 68, RALLIED 6, REFUSE_ATTACK 6, ROUTED_FLEE 12, SEEK_ENEMY 951, STUCK 6, TOO_CLOSE 95

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t76-spread-t8-waves-mixta-escudo-comida-base - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)

# Simulación Rally - lote 20261119-042631-t85-reg-olas

Preset `t85-reg-olas` · velocidad `sprint` · 42 corridas · 5 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t85-kite-t8-waves-mixta-linea-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 268 ± 2 | 2218 ± 26 | 54 ± 4 | 91 % ± 1 | 12.0 ± 0.3 | 12.2 ± 0.3 | 0 |
| t85-kite-t8-waves-mixta-arco-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 245 ± 1 | 2370 ± 11 | 52 ± 4 | 94 % ± 1 | 15.2 ± 0.3 | 13.3 ± 0.4 | 0 |
| t85-kite-t8-waves-mixta-escudo-comida-base | 7 | 93 % ± 7 | 7 % ± 7 | 7.7 ± 0.3 | 0.3 ± 0.3 | 554 ± 31 | 1584 ± 64 | 199 ± 30 | 0 % ± 0 | 15.6 ± 1.3 | 16.3 ± 1.1 | 2 |
| t85-kite-t16-moral-linea-nueva | 8 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 349 ± 3 | 4376 ± 34 | 166 ± 9 | 90 % ± 0 | 13.3 ± 0.4 | 13.2 ± 0.3 | 0 |
| t85-kite-t16-moral-r0-nueva | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 347 ± 2 | 4410 ± 52 | 177 ± 9 | 89 % ± 1 | 12.3 ± 0.2 | 12.5 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t85-kite-t8-waves-mixta-linea-comida-base semilla 8300: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 39 de 39 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t85-kite-t8-waves-mixta-escudo-comida-base: enemy:arrow=1 enemy:mob=1
- t85-kite-t16-moral-linea-nueva: enemy:explosion.player=1

## Motivos de decisión (cambios registrados): COUNTERATTACK 4, ENGAGE 4120, LOW_HEALTH_RETREAT 40, MORALE_SHAKEN 425, MORALE_WAVERING 75, NONE 3065, OUT_OF_RANGE 62, RALLIED 9, REFUSE_ATTACK 19, ROUTED_FLEE 27, SEEK_ENEMY 1007, STUCK 22, TOO_CLOSE 111

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t85-kite-t8-waves-mixta-escudo-comida-base - 2 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)

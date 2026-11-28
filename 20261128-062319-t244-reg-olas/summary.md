# Simulación Rally - lote 20261128-062319-t244-reg-olas

Preset `t244-reg-olas` · velocidad `sprint` · 44 corridas · 5 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t244-sync-t8-waves-mixta-linea-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 265 ± 1 | 2195 ± 27 | 48 ± 5 | 86 % ± 1 | 12.3 ± 0.3 | 13.0 ± 0.3 | 0 |
| t244-sync-t8-waves-mixta-arco-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 243 ± 1 | 2384 ± 5 | 53 ± 3 | 83 % ± 1 | 15.2 ± 0.1 | 13.1 ± 0.8 | 0 |
| t244-sync-t8-waves-mixta-escudo-comida-base | 7 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 524 ± 22 | 1671 ± 9 | 159 ± 9 | 0 % ± 0 | 16.6 ± 1.5 | 17.3 ± 1.0 | 4 |
| t244-sync-t16-moral-linea-nueva | 8 | 100 % ± 0 | 0 % ± 0 | 7.8 ± 0.3 | 0.0 ± 0.0 | 346 ± 2 | 4423 ± 40 | 153 ± 5 | 85 % ± 0 | 13.1 ± 0.2 | 13.6 ± 0.5 | 0 |
| t244-sync-t16-moral-r0-nueva | 8 | 100 % ± 0 | 0 % ± 0 | 7.8 ± 0.2 | 0.0 ± 0.0 | 344 ± 4 | 4406 ± 52 | 169 ± 16 | 86 % ± 1 | 12.5 ± 0.4 | 13.1 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t244-sync-t8-waves-mixta-linea-comida-base semilla 24400: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 39 de 39 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t244-sync-t16-moral-linea-nueva: enemy:explosion.player=2
- t244-sync-t16-moral-r0-nueva: enemy:explosion.player=1

## Motivos de decisión (cambios registrados): COUNTERATTACK 17, ENGAGE 4065, LOW_HEALTH_RETREAT 45, MORALE_SHAKEN 364, MORALE_WAVERING 58, NONE 3087, OUT_OF_RANGE 70, RALLIED 6, REFUSE_ATTACK 9, ROUTED_FLEE 34, SEEK_ENEMY 929, STUCK 25, TOO_CLOSE 92

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t244-sync-t8-waves-mixta-escudo-comida-base - 4 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)

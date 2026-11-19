# Simulación Rally - lote 20261119-065117-reg-olas

Preset `reg-olas` · velocidad `sprint` · 43 corridas · 5 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t8-waves-mixta-linea-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 268 ± 2 | 2221 ± 25 | 60 ± 5 | 86 % ± 1 | 12.1 ± 0.4 | 12.7 ± 0.4 | 0 |
| t8-waves-mixta-arco-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 244 ± 1 | 2401 ± 10 | 54 ± 3 | 84 % ± 0 | 15.2 ± 0.3 | 13.8 ± 0.2 | 0 |
| t8-waves-mixta-escudo-comida-base | 8 | 94 % ± 6 | 6 % ± 6 | 7.9 ± 0.1 | 1.1 ± 1.1 | 544 ± 32 | 1613 ± 48 | 173 ± 18 | 0 % ± 0 | 16.0 ± 1.4 | 16.3 ± 1.2 | 2 |
| t16-moral-linea-nueva | 8 | 100 % ± 0 | 0 % ± 0 | 7.8 ± 0.2 | 0.0 ± 0.0 | 340 ± 4 | 4342 ± 82 | 166 ± 10 | 86 % ± 1 | 12.9 ± 0.3 | 13.4 ± 0.5 | 0 |
| t16-moral-r0-nueva | 8 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 353 ± 6 | 4525 ± 52 | 161 ± 9 | 87 % ± 1 | 12.9 ± 0.4 | 13.3 ± 0.6 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t8-waves-mixta-linea-comida-base semilla 8900: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 40 de 40 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t8-waves-mixta-escudo-comida-base: enemy:explosion.player=1
- t16-moral-linea-nueva: enemy:explosion.player=2
- t16-moral-r0-nueva: enemy:explosion.player=1

## Motivos de decisión (cambios registrados): COUNTERATTACK 1, ENGAGE 4368, LOW_HEALTH_RETREAT 65, MORALE_SHAKEN 664, MORALE_WAVERING 105, NONE 3124, OUT_OF_RANGE 54, RALLIED 15, REFUSE_ATTACK 10, ROUTED_FLEE 28, SEEK_ENEMY 922, STUCK 19, TOO_CLOSE 115

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t8-waves-mixta-escudo-comida-base - 2 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)

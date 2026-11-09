# Simulación Rally - lote 20261109-194611-t34-moral-harbinger

Preset `t34-moral-harbinger` · velocidad `sprint` · 17 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t34-moral-harbinger-conmoral | 8 | 100 % ± 0 | 0 % ± 0 | 15.6 ± 0.2 | 0.0 ± 0.0 | 123 ± 26 | 637 ± 52 | 142 ± 26 | 90 % ± 1 | 24.9 ± 2.3 | 16.5 ± 1.1 | 0 |
| t34-moral-harbinger-sinmoral | 8 | 100 % ± 0 | 0 % ± 0 | 15.9 ± 0.1 | 0.0 ± 0.0 | 55 ± 1 | 500 ± 2 | 92 ± 11 | 90 % ± 1 | 21.9 ± 1.4 | 15.9 ± 0.7 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t34-moral-harbinger-conmoral semilla 1: sprint → blue (15/0 vivos), normal → blue (16/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 16 de 16 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t34-moral-harbinger-conmoral: enemy:cataclysm.laser=1 enemy:mob=2
- t34-moral-harbinger-sinmoral: env:wither=1

## Motivos de decisión (cambios registrados): COUNTERATTACK 5, ENGAGE 716, LOW_HEALTH_RETREAT 17, MORALE_SHAKEN 308, MORALE_WAVERING 149, NONE 137, OUT_OF_RANGE 105, RALLIED 16, REFUSE_ATTACK 1, ROUTED_FLEE 192, SEEK_ENEMY 7, STUCK 70, TOO_CLOSE 32

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- MUERTES QUE NO SON DEL ENEMIGO: t34-moral-harbinger-sinmoral - 100 % de las bajas azules ({env:wither=1.0})

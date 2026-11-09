# Simulación Rally - lote 20261109-194736-t34-moral-ignis

Preset `t34-moral-ignis` · velocidad `sprint` · 18 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t34-moral-ignis-conmoral | 7 | 64 % ± 18 | 36 % ± 18 | 6.3 ± 2.0 | 0.4 ± 0.2 | 169 ± 32 | 630 ± 27 | 598 ± 82 | 83 % ± 2 | 25.2 ± 2.4 | 17.8 ± 1.7 | 2 |
| t34-moral-ignis-sinmoral | 8 | 100 % ± 0 | 0 % ± 0 | 10.3 ± 0.5 | 0.0 ± 0.0 | 82 ± 3 | 569 ± 24 | 460 ± 18 | 84 % ± 1 | 16.8 ± 0.8 | 13.8 ± 0.9 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t34-moral-ignis-conmoral semilla 1: sprint → red (0/1 vivos), normal → blue (13/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 13 de 13 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t34-moral-ignis-conmoral: enemy:cataclysm.flame_strike=12 enemy:explosion.player=1 enemy:mob=53 env:onFire=2
- t34-moral-ignis-sinmoral: enemy:cataclysm.flame_strike=8 enemy:mob=37 env:fall=1

## Motivos de decisión (cambios registrados): COUNTERATTACK 29, ENGAGE 690, LOW_HEALTH_RETREAT 149, MORALE_SHAKEN 174, MORALE_WAVERING 169, NONE 151, OUT_OF_RANGE 8, RALLIED 21, REFUSE_ATTACK 36, ROUTED_FLEE 184, SEEK_ENEMY 70, STUCK 2, TOO_CLOSE 200, UNIT_BROKEN 1

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t34-moral-ignis-conmoral - 2 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- t34-moral-ignis-conmoral: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)

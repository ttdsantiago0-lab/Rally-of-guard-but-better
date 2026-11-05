# Simulación Rally - lote 20261105-073324-t8-waves-b

Preset `t8-waves-b` · velocidad `sprint` · 22 corridas · 2 escenarios.
> Mobs ajenos retirados del campo entre corridas (tercer bando oculto): {minecraft:zombie_villager=4}

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t8-waves-mixta-escudo-comida-base | 10 | 90 % ± 10 | 10 % ± 10 | 6.0 ± 0.9 | 0.5 ± 0.5 | 406 ± 32 | 1643 ± 47 | 221 ± 31 | 0 % ± 0 | 10.2 ± 1.9 | 9.1 ± 1.6 | 1 |
| t8-waves-mixta-escudo-comida-sinesquiva | 10 | 40 % ± 16 | 60 % ± 16 | 1.8 ± 0.8 | 3.9 ± 1.4 | 320 ± 28 | 1299 ± 138 | 347 ± 21 | 0 % ± 0 | 8.0 ± 1.7 | 8.0 ± 1.9 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t8-waves-mixta-escudo-comida-base semilla 1: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 13 de 13 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t8-waves-mixta-escudo-comida-base: enemy:explosion.player=14 enemy:mob=3
- t8-waves-mixta-escudo-comida-sinesquiva: enemy:explosion.player=50 enemy:mob=4

## Motivos de decisión (cambios registrados): COUNTERATTACK 19, ENGAGE 1759, LOW_HEALTH_RETREAT 65, MORALE_SHAKEN 280, MORALE_WAVERING 62, NONE 1214, RALLIED 8, REFUSE_ATTACK 45, ROUTED_FLEE 59, SEEK_ENEMY 667, STUCK 45

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t8-waves-mixta-escudo-comida-base - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)

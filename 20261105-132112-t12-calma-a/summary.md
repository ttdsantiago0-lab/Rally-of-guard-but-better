# Simulación Rally - lote 20261105-132112-t12-calma-a

Preset `t12-calma-a` · velocidad `sprint` · 43 corridas · 4 escenarios.
> Mobs ajenos retirados del campo entre corridas (tercer bando oculto): {minecraft:zombie_villager=2}

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t12-waves-mixta-escudo-comida-base | 10 | 75 % ± 13 | 25 % ± 13 | 5.8 ± 1.0 | 1.9 ± 1.1 | 418 ± 53 | 1609 ± 55 | 295 ± 57 | 0 % ± 0 | 11.0 ± 3.0 | 10.5 ± 2.9 | 1 |
| t12-waves-mixta-escudo-comida-sincalma | 9 | 100 % ± 0 | 0 % ± 0 | 6.3 ± 0.7 | 0.0 ± 0.0 | 333 ± 15 | 1683 ± 24 | 226 ± 33 | 0 % ± 0 | 5.4 ± 0.6 | 6.6 ± 1.2 | 2 |
| t12-waves-mixta-linea-comida-base | 10 | 100 % ± 0 | 0 % ± 0 | 7.6 ± 0.3 | 0.0 ± 0.0 | 278 ± 3 | 1935 ± 41 | 171 ± 20 | 81 % ± 1 | 7.3 ± 0.9 | 8.2 ± 0.9 | 0 |
| t12-waves-mixta-linea-comida-sincalma | 10 | 100 % ± 0 | 0 % ± 0 | 7.3 ± 0.3 | 0.0 ± 0.0 | 286 ± 6 | 1935 ± 34 | 203 ± 20 | 79 % ± 1 | 7.3 ± 0.8 | 7.9 ± 0.7 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t12-waves-mixta-escudo-comida-base semilla 1: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 37 de 37 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t12-waves-mixta-escudo-comida-base: enemy:arrow=4 enemy:explosion.player=14 enemy:mob=3
- t12-waves-mixta-escudo-comida-sincalma: enemy:explosion.player=15
- t12-waves-mixta-linea-comida-base: enemy:explosion.player=4
- t12-waves-mixta-linea-comida-sincalma: enemy:explosion.player=6

## Motivos de decisión (cambios registrados): COUNTERATTACK 22, ENGAGE 4100, LOW_HEALTH_RETREAT 23, MORALE_SHAKEN 230, MORALE_WAVERING 37, NONE 2795, OUT_OF_RANGE 6, RALLIED 2, REFUSE_ATTACK 21, ROUTED_FLEE 47, SEEK_ENEMY 1484, STUCK 32, TOO_CLOSE 712

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t12-waves-mixta-escudo-comida-sincalma - 2 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t12-waves-mixta-escudo-comida-base - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)

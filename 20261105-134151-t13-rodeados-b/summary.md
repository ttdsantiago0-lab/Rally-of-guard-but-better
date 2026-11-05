# Simulación Rally - lote 20261105-134151-t13-rodeados-b

Preset `t13-rodeados-b` · velocidad `sprint` · 56 corridas · 3 escenarios.
> Mobs ajenos retirados del campo entre corridas (tercer bando oculto): {minecraft:zombie_villager=4}

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t13-mixta16-linea-base | 8 | 63 % ± 18 | 38 % ± 18 | 3.9 ± 1.2 | 2.5 ± 1.5 | 350 ± 6 | 2934 ± 112 | 432 ± 55 | 85 % ± 1 | 7.0 ± 1.1 | 7.8 ± 1.0 | 9 |
| t13-mixta16-linea-circulo | 9 | 78 % ± 15 | 22 % ± 15 | 5.4 ± 1.1 | 2.6 ± 1.8 | 331 ± 10 | 2905 ± 171 | 351 ± 44 | 86 % ± 1 | 6.6 ± 0.9 | 7.9 ± 0.9 | 6 |
| t13-mixta16-linea-retroceder | 3 | 67 % ± 17 | 33 % ± 17 | 3.3 ± 1.3 | 5.0 ± 4.5 | 607 ± 113 | 2044 ± 448 | 552 ± 125 | 80 % ± 3 | 19.0 ± 3.5 | 18.0 ± 2.7 | 20 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t13-mixta16-linea-base semilla 1: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 15 de 15 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t13-mixta16-linea-base: enemy:arrow=4 enemy:explosion.player=16 enemy:mob=6
- t13-mixta16-linea-circulo: enemy:arrow=1 enemy:explosion.player=16 enemy:mob=3
- t13-mixta16-linea-retroceder: enemy:arrow=2 enemy:explosion.player=6 enemy:mob=6

## Motivos de decisión (cambios registrados): COUNTERATTACK 114, ENGAGE 4212, FORMATION_HOLD 3, LOW_HEALTH_RETREAT 99, MORALE_SHAKEN 941, MORALE_WAVERING 369, NONE 1850, OUT_OF_RANGE 159, RALLIED 55, REFUSE_ATTACK 83, ROUTED_FLEE 302, SEEK_ENEMY 726, STUCK 36, TOO_CLOSE 1082

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t13-mixta16-linea-retroceder - 20 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t13-mixta16-linea-base - 9 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t13-mixta16-linea-circulo - 6 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- t13-mixta16-linea-retroceder: 2/3 corridas llegaron al tope de tiempo

# Simulación Rally - lote 20261105-073132-t6-waves-mixta-linea

Preset `t6-waves-mixta-linea` · velocidad `sprint` · 46 corridas · 4 escenarios.
> Mobs ajenos retirados del campo entre corridas (tercer bando oculto): {minecraft:zombie_villager=23}

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t6-waves-mixta-linea-sincomida-base | 9 | 6 % ± 6 | 94 % ± 6 | 0.0 ± 0.0 | 6.2 ± 1.3 | 191 ± 19 | 1122 ± 120 | 261 ± 11 | 80 % ± 2 | 6.2 ± 0.5 | 6.3 ± 0.7 | 2 |
| t6-waves-mixta-linea-sincomida-antes | 9 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 7.4 ± 1.3 | 157 ± 15 | 678 ± 43 | 245 ± 6 | 56 % ± 7 | 6.3 ± 0.9 | 6.9 ± 0.6 | 2 |
| t6-waves-mixta-linea-comida-base | 9 | 61 % ± 16 | 39 % ± 16 | 2.3 ± 0.9 | 2.7 ± 1.4 | 307 ± 17 | 1740 ± 50 | 372 ± 36 | 80 % ± 1 | 5.8 ± 0.4 | 6.7 ± 0.4 | 2 |
| t6-waves-mixta-linea-comida-antes | 9 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 7.7 ± 1.0 | 273 ± 56 | 899 ± 59 | 388 ± 30 | 65 % ± 4 | 5.7 ± 0.4 | 5.8 ± 0.6 | 3 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t6-waves-mixta-linea-sincomida-base semilla 1: sprint → red (0/5 vivos), normal → red (0/12 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 5 de 5 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t6-waves-mixta-linea-sincomida-base: enemy:arrow=9 enemy:explosion.player=34 enemy:mob=16
- t6-waves-mixta-linea-sincomida-antes: enemy:arrow=12 enemy:explosion.player=26 enemy:mob=16
- t6-waves-mixta-linea-comida-base: enemy:arrow=2 enemy:explosion.player=40 enemy:mob=3
- t6-waves-mixta-linea-comida-antes: enemy:arrow=3 enemy:explosion.player=45 enemy:mob=8

## Motivos de decisión (cambios registrados): COUNTERATTACK 42, ENGAGE 2126, LOW_HEALTH_RETREAT 354, MORALE_SHAKEN 325, MORALE_WAVERING 176, NONE 1332, RALLIED 12, REFUSE_ATTACK 113, ROUTED_FLEE 134, SEEK_ENEMY 400, STUCK 7, TOO_CLOSE 1096

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t6-waves-mixta-linea-comida-antes - 3 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t6-waves-mixta-linea-comida-base - 2 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t6-waves-mixta-linea-sincomida-base - 2 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t6-waves-mixta-linea-sincomida-antes - 2 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)

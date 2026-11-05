# Simulación Rally - lote 20261105-071122-t6-waves-mixta-arco

Preset `t6-waves-mixta-arco` · velocidad `sprint` · 49 corridas · 4 escenarios.
> Mobs ajenos retirados del campo entre corridas (tercer bando oculto): {minecraft:zombie_villager=25}

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t6-waves-mixta-arco-sincomida-base | 8 | 50 % ± 19 | 50 % ± 19 | 2.4 ± 1.0 | 4.3 ± 1.7 | 261 ± 26 | 1691 ± 207 | 221 ± 20 | 73 % ± 2 | 12.0 ± 0.9 | 10.3 ± 1.0 | 5 |
| t6-waves-mixta-arco-sincomida-antes | 10 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 9.7 ± 1.6 | 122 ± 29 | 421 ± 113 | 200 ± 10 | 67 % ± 4 | 8.7 ± 1.6 | 8.6 ± 1.0 | 0 |
| t6-waves-mixta-arco-comida-base | 6 | 50 % ± 22 | 50 % ± 22 | 3.5 ± 1.6 | 3.7 ± 1.7 | 301 ± 22 | 1898 ± 144 | 339 ± 57 | 74 % ± 2 | 11.5 ± 0.7 | 10.6 ± 1.1 | 8 |
| t6-waves-mixta-arco-comida-antes | 10 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 11.0 ± 1.2 | 155 ± 38 | 474 ± 142 | 300 ± 16 | 68 % ± 4 | 9.4 ± 1.7 | 10.8 ± 1.1 | 1 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t6-waves-mixta-arco-sincomida-base semilla 1: sprint → blue (7/0 vivos), normal → red (0/7 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 7 de 7 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t6-waves-mixta-arco-sincomida-base: enemy:arrow=5 enemy:explosion.player=24 enemy:mob=4
- t6-waves-mixta-arco-sincomida-antes: enemy:arrow=7 enemy:explosion.player=9 enemy:mob=30
- t6-waves-mixta-arco-comida-base: enemy:explosion.player=16 enemy:mob=8
- t6-waves-mixta-arco-comida-antes: enemy:explosion.player=13 enemy:mob=29

## Motivos de decisión (cambios registrados): COUNTERATTACK 28, ENGAGE 2379, LOW_HEALTH_RETREAT 227, MORALE_SHAKEN 359, MORALE_WAVERING 314, NONE 1215, OUT_OF_RANGE 38, RALLIED 28, REFUSE_ATTACK 92, ROUTED_FLEE 187, SEEK_ENEMY 217, TOO_CLOSE 1439

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t6-waves-mixta-arco-sincomida-base - 5 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t6-waves-mixta-arco-comida-base - 8 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t6-waves-mixta-arco-comida-antes - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- t6-waves-mixta-arco-sincomida-base: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)

# Simulación Rally - lote 20261108-204110-t27-kite-zombie-base

Preset `t27-kite-zombie-base` · velocidad `sprint` · 83 corridas · 4 escenarios.
> Mobs ajenos retirados del campo entre corridas (tercer bando oculto): {minecraft:zombie_villager=1}

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t27-kite-zombie-arco-base-r60 | 20 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.1 | 0.0 ± 0.0 | 45 ± 1 | 1479 ± 4 | 3 ± 1 | 96 % ± 0 | 9.2 ± 0.2 | 6.1 ± 0.3 | 0 |
| t27-kite-zombie-arco-base-r85 | 20 | 100 % ± 0 | 0 % ± 0 | 7.5 ± 0.1 | 0.0 ± 0.0 | 67 ± 1 | 2088 ± 5 | 27 ± 4 | 94 % ± 1 | 12.2 ± 0.6 | 11.6 ± 0.8 | 0 |
| t27-kite-zombie-linea-base-r55 | 20 | 100 % ± 0 | 0 % ± 0 | 5.0 ± 0.3 | 0.0 ± 0.0 | 57 ± 2 | 1397 ± 8 | 85 ± 4 | 96 % ± 1 | 8.3 ± 0.3 | 13.7 ± 0.6 | 0 |
| t27-kite-zombie-linea-base-r75 | 20 | 88 % ± 6 | 13 % ± 6 | 3.6 ± 0.4 | 2.5 ± 1.3 | 104 ± 9 | 1873 ± 17 | 123 ± 6 | 97 % ± 0 | 8.5 ± 0.3 | 14.5 ± 1.2 | 2 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t27-kite-zombie-arco-base-r60 semilla 1001: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 79 de 79 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t27-kite-zombie-arco-base-r85: enemy:mob=1
- t27-kite-zombie-linea-base-r55: enemy:mob=34
- t27-kite-zombie-linea-base-r75: enemy:mob=38

## Motivos de decisión (cambios registrados): COUNTERATTACK 12, ENGAGE 1182, LOW_HEALTH_RETREAT 350, MORALE_SHAKEN 281, MORALE_WAVERING 214, NONE 12, OUT_OF_RANGE 147, RALLIED 10, REFUSE_ATTACK 9, ROUTED_FLEE 74, SEEK_ENEMY 67, TOO_CLOSE 254

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t27-kite-zombie-linea-base-r75 - 2 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)

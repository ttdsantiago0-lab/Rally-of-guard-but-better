# Simulación Rally - lote 20261108-203003-t27-ender-sinender

Preset `t27-ender-sinender` · velocidad `sprint` · 82 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t27-ender-linea-sinender-r3 | 20 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 3.0 ± 0.0 | 81 ± 6 | 0 ± 0 | 186 ± 1 | 0 % ± 0 | 17.0 ± 1.4 | 18.8 ± 1.2 | 1 |
| t27-ender-linea-sinender-r6 | 20 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 6.0 ± 0.0 | 50 ± 3 | 0 ± 0 | 185 ± 1 | 0 % ± 0 | 9.4 ± 1.0 | 18.0 ± 1.5 | 0 |
| t27-ender-escudo-sinender-r3 | 20 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 3.0 ± 0.0 | 37 ± 1 | 0 ± 0 | 181 ± 0 | 0 % ± 0 | 8.9 ± 0.5 | 7.0 ± 0.4 | 0 |
| t27-ender-arco-sinender-r3 | 20 | 18 % ± 5 | 83 % ± 5 | 0.9 ± 0.3 | 3.0 ± 0.0 | 156 ± 6 | 0 ± 0 | 172 ± 7 | 0 % ± 0 | 31.7 ± 1.7 | 23.2 ± 1.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t27-ender-linea-sinender-r3 semilla 1: sprint → red (0/3 vivos), normal → red (0/3 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 7 de 7 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t27-ender-linea-sinender-r3: enemy:mob=160
- t27-ender-linea-sinender-r6: enemy:mob=160
- t27-ender-escudo-sinender-r3: enemy:mob=160
- t27-ender-arco-sinender-r3: enemy:mob=142

## Motivos de decisión (cambios registrados): COUNTERATTACK 22, MORALE_SHAKEN 641, MORALE_WAVERING 166, NONE 801, RALLIED 1, ROUTED_FLEE 86, SEEK_ENEMY 1533, STUCK 8, UNIT_BROKEN 2

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t27-ender-linea-sinender-r3 - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)

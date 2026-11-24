# Simulación Rally - lote 20261124-022427-t168-tremor-spread

Preset `t168-tremor-spread` · velocidad `sprint` · 101 corridas · 3 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t168-mixto-defecto | 28 | 5 % ± 3 | 95 % ± 3 | 0.4 ± 0.2 | 1.0 ± 0.0 | 105 ± 10 | 591 ± 54 | 192 ± 5 | 94 % ± 1 | 20.2 ± 1.2 | 12.1 ± 1.3 | 5 |
| t168-mixto-spread4 | 29 | 3 % ± 3 | 97 % ± 3 | 0.2 ± 0.2 | 1.0 ± 0.0 | 102 ± 4 | 644 ± 51 | 206 ± 7 | 93 % ± 1 | 18.3 ± 0.7 | 11.1 ± 0.8 | 5 |
| t168-mixto-spread7 | 30 | 17 % ± 6 | 83 % ± 6 | 1.5 ± 0.5 | 0.9 ± 0.0 | 141 ± 10 | 906 ± 70 | 189 ± 8 | 95 % ± 0 | 17.4 ± 0.7 | 12.1 ± 0.8 | 3 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t168-mixto-defecto semilla 14800: sprint → red (0/1 vivos), normal → draw (4/1 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 12 de 12 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t168-mixto-defecto: enemy:mob=34 enemy:tremorzilla_beam=164 env:radiation=16
- t168-mixto-spread4: enemy:mob=53 enemy:tremorzilla_beam=165 env:radiation=8
- t168-mixto-spread7: enemy:mob=38 enemy:tremorzilla_beam=152 env:radiation=6

## Motivos de decisión (cambios registrados): ENGAGE 2224, FORMATION_HOLD 3, LOW_HEALTH_RETREAT 1765, NONE 1093, OUT_OF_RANGE 315, SEEK_ENEMY 1709, STUCK 406, TOO_CLOSE 99

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t168-mixto-defecto - 5 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t168-mixto-spread7 - 3 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t168-mixto-spread4 - 5 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- t168-mixto-defecto: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)

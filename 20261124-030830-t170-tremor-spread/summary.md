# Simulación Rally - lote 20261124-030830-t170-tremor-spread

Preset `t170-tremor-spread` · velocidad `sprint` · 67 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t170-mixto-defecto | 30 | 12 % ± 6 | 88 % ± 6 | 0.7 ± 0.4 | 0.9 ± 0.1 | 106 ± 7 | 698 ± 62 | 196 ± 7 | 93 % ± 1 | 20.0 ± 0.9 | 13.1 ± 1.0 | 2 |
| t170-mixto-spread7 | 30 | 10 % ± 4 | 90 % ± 4 | 0.5 ± 0.2 | 1.0 ± 0.0 | 128 ± 9 | 803 ± 59 | 191 ± 5 | 93 % ± 1 | 17.9 ± 0.7 | 11.8 ± 0.7 | 3 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t170-mixto-defecto semilla 15200: sprint → red (0/1 vivos), normal → draw (2/1 vivos)
- t170-mixto-defecto semilla 22977: sprint → ? (0/0 vivos), normal → red (0/1 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 9 de 9 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t170-mixto-defecto: enemy:mob=39 enemy:tremorzilla_beam=170 env:radiation=9
- t170-mixto-spread7: enemy:mob=48 enemy:tremorzilla_beam=170 env:radiation=7

## Motivos de decisión (cambios registrados): ENGAGE 1486, LOW_HEALTH_RETREAT 1076, NONE 735, OUT_OF_RANGE 268, SEEK_ENEMY 1058, STUCK 237, TOO_CLOSE 87

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t170-mixto-spread7 - 3 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t170-mixto-defecto - 2 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- t170-mixto-defecto: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)

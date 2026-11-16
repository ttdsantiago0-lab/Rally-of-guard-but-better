# Simulación Rally - lote 20261116-211351-t62-tremor-conf

Preset `t62-tremor-conf` · velocidad `sprint` · 144 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t62-tremor-linea | 29 | 24 % ± 8 | 76 % ± 8 | 3.3 ± 1.1 | 0.8 ± 0.1 | 116 ± 7 | 873 ± 44 | 348 ± 21 | 89 % ± 2 | 20.1 ± 0.7 | 13.8 ± 0.6 | 8 |
| t62-tremor-escudos | 28 | 86 % ± 6 | 14 % ± 6 | 11.0 ± 0.9 | 0.2 ± 0.1 | 120 ± 13 | 1045 ± 61 | 216 ± 16 | 0 % ± 0 | 28.6 ± 0.7 | 23.9 ± 0.8 | 7 |
| t62-tremor-escudos-netherita | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 110 ± 4 | 675 ± 30 | 598 ± 17 | 0 % ± 0 | 21.5 ± 0.6 | 11.7 ± 0.6 | 7 |
| t62-tremor-escudos-diamante | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 102 ± 3 | 494 ± 28 | 575 ± 18 | 0 % ± 0 | 21.6 ± 0.7 | 11.4 ± 0.5 | 4 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t62-tremor-linea semilla 6201: sprint → draw (1/1 vivos), normal → red (0/1 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 32 de 32 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t62-tremor-linea: enemy:mob=70 enemy:tremorzilla_beam=276 env:radiation=23
- t62-tremor-escudos: enemy:mob=53 enemy:tremorzilla_beam=76 env:fall=2 env:radiation=9
- t62-tremor-escudos-netherita: enemy:mob=58 enemy:tremorzilla_beam=409 env:fall=1 env:radiation=12
- t62-tremor-escudos-diamante: enemy:mob=71 enemy:tremorzilla_beam=400 env:radiation=9

## Motivos de decisión (cambios registrados): ENGAGE 5482, FORMATION_HOLD 5, LOW_HEALTH_RETREAT 4716, NONE 3998, OUT_OF_RANGE 273, SEEK_ENEMY 4820, STUCK 943, TOO_CLOSE 89, UNIT_BROKEN 1

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t62-tremor-linea - 8 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t62-tremor-escudos - 7 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t62-tremor-escudos-diamante - 4 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t62-tremor-escudos-netherita - 7 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)

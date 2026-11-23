# Simulación Rally - lote 20261123-061500-t147-kite-hostil

Preset `t147-kite-hostil` · velocidad `sprint` · 131 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t147-kite-hostil-arcos-defecto | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 32 ± 4 | 155 ± 16 | 199 ± 3 | 91 % ± 1 | 24.5 ± 1.1 | 9.4 ± 0.4 | 0 |
| t147-kite-hostil-arcos-hostil | 30 | 3 % ± 2 | 97 % ± 2 | 0.4 ± 0.3 | 1.0 ± 0.0 | 53 ± 10 | 245 ± 50 | 167 ± 4 | 91 % ± 1 | 26.5 ± 0.8 | 10.5 ± 0.7 | 0 |
| t147-kite-hostil-mixto-defecto | 30 | 10 % ± 4 | 90 % ± 4 | 0.6 ± 0.3 | 1.0 ± 0.0 | 118 ± 9 | 760 ± 68 | 206 ± 6 | 95 % ± 1 | 18.6 ± 0.9 | 12.5 ± 0.9 | 2 |
| t147-kite-hostil-mixto-hostil | 27 | 7 % ± 4 | 93 % ± 4 | 0.6 ± 0.4 | 1.0 ± 0.0 | 118 ± 9 | 685 ± 64 | 170 ± 7 | 92 % ± 2 | 20.7 ± 1.0 | 12.4 ± 0.9 | 11 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t147-kite-hostil-arcos-defecto semilla 13400: sprint → red (0/1 vivos), normal → red (0/1 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 10 de 10 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t147-kite-hostil-arcos-defecto: enemy:mob=6 enemy:tremorzilla_beam=233 env:radiation=1
- t147-kite-hostil-arcos-hostil: enemy:mob=2 enemy:tremorzilla_beam=227
- t147-kite-hostil-mixto-defecto: enemy:mob=49 enemy:tremorzilla_beam=167 env:radiation=6
- t147-kite-hostil-mixto-hostil: enemy:mob=40 enemy:tremorzilla_beam=156 env:radiation=3

## Motivos de decisión (cambios registrados): ENGAGE 2345, FORMATION_HOLD 6, LOW_HEALTH_RETREAT 1426, NONE 1435, OUT_OF_RANGE 609, SEEK_ENEMY 1382, STUCK 182, TOO_CLOSE 73

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t147-kite-hostil-mixto-hostil - 11 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t147-kite-hostil-mixto-defecto - 2 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)

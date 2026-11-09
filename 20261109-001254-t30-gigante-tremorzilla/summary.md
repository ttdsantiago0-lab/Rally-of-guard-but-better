# Simulación Rally - lote 20261109-001254-t30-gigante-tremorzilla

Preset `t30-gigante-tremorzilla` · velocidad `sprint` · 47 corridas · 3 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t30-gigante-tremorzilla-hierro | 5 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 72 ± 11 | 93 ± 10 | 529 ± 13 | 93 % ± 2 | 31.9 ± 4.4 | 22.3 ± 4.5 | 5 |
| t30-gigante-tremorzilla-diamante | 1 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 30 ± 0 | 123 ± 0 | 469 ± 0 | 85 % ± 0 | 30.9 ± 0.0 | 16.4 ± 0.0 | 11 |
| t30-gigante-tremorzilla-netheritamas | 1 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 184 ± 0 | 707 ± 0 | 448 ± 0 | 93 % ± 0 | 32.3 ± 0.0 | 22.3 ± 0.0 | 11 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t30-gigante-tremorzilla-hierro semilla 1: sprint → draw (2/1 vivos), normal → red (0/1 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Causas de muerte del bando azul (causa:tipo=bajas)
- t30-gigante-tremorzilla-hierro: enemy:mob=4 enemy:tremorzilla_beam=71 env:radiation=5
- t30-gigante-tremorzilla-diamante: enemy:tremorzilla_beam=16
- t30-gigante-tremorzilla-netheritamas: enemy:mob=2 enemy:tremorzilla_beam=13 env:radiation=1

## Motivos de decisión (cambios registrados): COUNTERATTACK 1, ENGAGE 1747, FORMATION_HOLD 3, LOW_HEALTH_RETREAT 318, MORALE_SHAKEN 802, MORALE_WAVERING 373, NONE 1018, OUT_OF_RANGE 191, RALLIED 25, REFUSE_ATTACK 11, ROUTED_FLEE 217, SEEK_ENEMY 104, STUCK 62, TOO_CLOSE 70

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t30-gigante-tremorzilla-hierro - 5 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t30-gigante-tremorzilla-diamante - 11 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t30-gigante-tremorzilla-netherita - 12 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t30-gigante-tremorzilla-netheritamas - 11 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)

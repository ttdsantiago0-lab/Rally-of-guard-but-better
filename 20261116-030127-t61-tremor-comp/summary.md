# Simulación Rally - lote 20261116-030127-t61-tremor-comp

Preset `t61-tremor-comp` · velocidad `sprint` · 129 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t61-tremor-linea | 30 | 25 % ± 8 | 75 % ± 8 | 3.0 ± 0.9 | 0.8 ± 0.1 | 123 ± 9 | 928 ± 44 | 341 ± 17 | 89 % ± 1 | 20.8 ± 1.0 | 14.6 ± 1.0 | 1 |
| t61-tremor-escudos | 30 | 90 % ± 6 | 10 % ± 6 | 10.3 ± 0.7 | 0.1 ± 0.1 | 106 ± 8 | 1007 ± 50 | 234 ± 13 | 0 % ± 0 | 27.1 ± 0.6 | 21.5 ± 0.9 | 2 |
| t61-tremor-espadas | 29 | 3 % ± 3 | 97 % ± 3 | 0.2 ± 0.2 | 1.0 ± 0.0 | 106 ± 4 | 708 ± 39 | 744 ± 26 | 0 % ± 0 | 24.5 ± 1.0 | 14.5 ± 0.9 | 5 |
| t61-tremor-3a1 | 30 | 68 % ± 8 | 32 % ± 8 | 7.5 ± 1.0 | 0.3 ± 0.1 | 117 ± 11 | 1080 ± 60 | 284 ± 15 | 92 % ± 1 | 21.9 ± 0.7 | 17.6 ± 0.8 | 1 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t61-tremor-linea semilla 6101: sprint → blue (13/0 vivos), normal → red (0/1 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 57 de 57 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t61-tremor-linea: enemy:mob=83 enemy:tremorzilla_beam=274 env:radiation=34
- t61-tremor-escudos: enemy:mob=65 enemy:tremorzilla_beam=102 env:radiation=5
- t61-tremor-espadas: enemy:mob=47 enemy:tremorzilla_beam=386 env:radiation=24
- t61-tremor-3a1: enemy:mob=69 enemy:tremorzilla_beam=171 env:radiation=15

## Motivos de decisión (cambios registrados): ENGAGE 5361, FORMATION_HOLD 2, LOW_HEALTH_RETREAT 4355, NONE 3604, OUT_OF_RANGE 325, SEEK_ENEMY 4852, STUCK 425, TOO_CLOSE 86, UNIT_BROKEN 2

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t61-tremor-escudos - 2 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t61-tremor-linea - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t61-tremor-espadas - 5 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t61-tremor-3a1 - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- t61-tremor-linea: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)

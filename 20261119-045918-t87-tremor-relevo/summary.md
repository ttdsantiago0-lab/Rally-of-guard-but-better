# Simulación Rally - lote 20261119-045918-t87-tremor-relevo

Preset `t87-tremor-relevo` · velocidad `sprint` · 111 corridas · 3 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t87-escudos | 29 | 79 % ± 8 | 21 % ± 8 | 10.1 ± 1.0 | 0.2 ± 0.1 | 109 ± 9 | 968 ± 45 | 221 ± 17 | 0 % ± 0 | 28.1 ± 0.7 | 23.1 ± 0.7 | 5 |
| t87-cap4 | 29 | 53 % ± 3 | 47 % ± 3 | 7.7 ± 0.7 | 0.9 ± 0.1 | 284 ± 9 | 1936 ± 69 | 290 ± 15 | 0 % ± 0 | 41.0 ± 1.3 | 32.7 ± 1.0 | 7 |
| t87-cap6 | 27 | 61 % ± 7 | 39 % ± 7 | 7.0 ± 1.0 | 0.6 ± 0.1 | 207 ± 18 | 1554 ± 111 | 289 ± 20 | 0 % ± 0 | 31.2 ± 1.0 | 24.5 ± 1.1 | 13 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t87-escudos semilla 8600: sprint → red (0/1 vivos), normal → blue (13/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 73 de 73 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t87-escudos: enemy:mob=65 enemy:tremorzilla_beam=101 env:radiation=4
- t87-cap4: enemy:mob=69 enemy:tremorzilla_beam=152 env:radiation=20
- t87-cap6: enemy:mob=79 enemy:tremorzilla_beam=139 env:radiation=24

## Motivos de decisión (cambios registrados): ENGAGE 5129, FORMATION_HOLD 7, LOW_HEALTH_RETREAT 4643, NONE 5096, SEEK_ENEMY 6562, STUCK 2798, UNIT_BROKEN 3

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t87-escudos - 5 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t87-cap6 - 13 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t87-cap4 - 7 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- t87-cap4: 25/29 corridas llegaron al tope de tiempo
- t87-escudos: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)

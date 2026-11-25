# Simulación Rally - lote 20261125-095811-t212-reparto-arco

Preset `t212-reparto-arco` · velocidad `sprint` · 63 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t212-rep-arco-capa | 30 | 43 % ± 9 | 57 % ± 9 | 2.0 ± 0.4 | 2.7 ± 0.5 | 34 ± 5 | 167 ± 4 | 173 ± 4 | 79 % ± 4 | 38.5 ± 2.0 | 7.3 ± 0.8 | 1 |
| t212-rep-arco-indiv | 30 | 57 % ± 9 | 43 % ± 9 | 2.5 ± 0.5 | 1.9 ± 0.4 | 42 ± 6 | 173 ± 4 | 170 ± 4 | 70 % ± 4 | 42.6 ± 2.0 | 7.4 ± 0.8 | 1 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t212-rep-arco-capa semilla 12300: sprint → blue (5/0 vivos), normal → blue (4/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 30 de 30 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t212-rep-arco-capa: enemy:arrow=180
- t212-rep-arco-indiv: enemy:arrow=165

## Motivos de decisión (cambios registrados): ENGAGE 1097, LOW_HEALTH_RETREAT 1721, NONE 1013, OUT_OF_RANGE 502, SEEK_ENEMY 8

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t212-rep-arco-indiv - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t212-rep-arco-capa - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)

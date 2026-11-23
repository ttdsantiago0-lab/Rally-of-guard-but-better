# Simulación Rally - lote 20261123-035819-t135-foco-10

Preset `t135-foco-10` · velocidad `sprint` · 62 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t135-foco-capa-10 | 30 | 3 % ± 3 | 97 % ± 3 | 0.1 ± 0.1 | 6.7 ± 0.3 | 12 ± 1 | 157 ± 4 | 185 ± 1 | 96 % ± 1 | 27.0 ± 0.6 | 3.8 ± 0.2 | 1 |
| t135-foco-foco-10 | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 6.5 ± 0.3 | 13 ± 1 | 157 ± 4 | 183 ± 1 | 97 % ± 1 | 27.0 ± 0.6 | 3.7 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t135-foco-capa-10 semilla 12700: sprint → red (0/7 vivos), normal → blue (5/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 1 de 1 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t135-foco-capa-10: enemy:arrow=236
- t135-foco-foco-10: enemy:arrow=240

## Motivos de decisión (cambios registrados): ENGAGE 1152, LOW_HEALTH_RETREAT 924, NONE 303, OUT_OF_RANGE 65, SEEK_ENEMY 43

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t135-foco-capa-10 - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- t135-foco-capa-10: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)

# Simulación Rally - lote 20261124-001901-t156-foco-cupo-espejo

Preset `t156-foco-cupo-espejo` · velocidad `sprint` · 91 corridas · 3 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t156-foco-cupo-espejo-capa-8 | 30 | 47 % ± 9 | 53 % ± 9 | 2.0 ± 0.4 | 2.5 ± 0.5 | 39 ± 6 | 169 ± 4 | 172 ± 4 | 74 % ± 4 | 40.1 ± 1.8 | 7.3 ± 0.8 | 0 |
| t156-foco-cupo-espejo-siembra-8 | 30 | 2 % ± 2 | 98 % ± 2 | 0.1 ± 0.1 | 6.3 ± 0.3 | 17 ± 6 | 120 ± 4 | 185 ± 1 | 93 % ± 2 | 28.2 ± 1.0 | 4.1 ± 0.2 | 0 |
| t156-foco-cupo-espejo-siembra-cupo3-8 | 30 | 10 % ± 6 | 90 % ± 6 | 0.4 ± 0.2 | 4.4 ± 0.4 | 35 ± 6 | 147 ± 5 | 187 ± 2 | 81 % ± 4 | 35.7 ± 1.8 | 5.9 ± 0.7 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t156-foco-cupo-espejo-capa-8 semilla 13500: sprint → blue (7/0 vivos), normal → red (0/4 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 18 de 18 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t156-foco-cupo-espejo-capa-8: enemy:arrow=180
- t156-foco-cupo-espejo-siembra-8: enemy:arrow=238
- t156-foco-cupo-espejo-siembra-cupo3-8: enemy:arrow=228

## Motivos de decisión (cambios registrados): ENGAGE 1605, LOW_HEALTH_RETREAT 1980, NONE 1084, OUT_OF_RANGE 596, SEEK_ENEMY 28

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t156-foco-cupo-espejo-capa-8: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)

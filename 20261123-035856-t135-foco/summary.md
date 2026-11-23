# Simulación Rally - lote 20261123-035856-t135-foco

Preset `t135-foco` · velocidad `sprint` · 123 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t135-foco-capa-8 | 60 | 42 % ± 6 | 58 % ± 6 | 1.7 ± 0.3 | 2.7 ± 0.3 | 41 ± 4 | 167 ± 3 | 178 ± 3 | 73 % ± 3 | 42.2 ± 1.5 | 7.7 ± 0.6 | 1 |
| t135-foco-foco-8 | 60 | 42 % ± 6 | 58 % ± 6 | 2.0 ± 0.3 | 2.6 ± 0.3 | 41 ± 4 | 170 ± 3 | 175 ± 3 | 75 % ± 3 | 41.1 ± 1.4 | 7.3 ± 0.6 | 1 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t135-foco-capa-8 semilla 12700: sprint → red (0/4 vivos), normal → blue (3/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 50 de 50 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t135-foco-capa-8: enemy:arrow=381
- t135-foco-foco-8: enemy:arrow=363

## Motivos de decisión (cambios registrados): ENGAGE 2179, LOW_HEALTH_RETREAT 3489, NONE 2102, OUT_OF_RANGE 1068, SEEK_ENEMY 52

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t135-foco-capa-8 - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t135-foco-foco-8 - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- t135-foco-capa-8: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)

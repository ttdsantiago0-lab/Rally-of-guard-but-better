# Simulación Rally - lote 20261122-190841-t130-desigual

Preset `t130-desigual` · velocidad `sprint` · 185 corridas · 6 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t130-desigual-capa-8 | 30 | 43 % ± 9 | 57 % ± 9 | 1.9 ± 0.4 | 2.2 ± 0.4 | 51 ± 7 | 173 ± 4 | 175 ± 4 | 69 % ± 4 | 43.3 ± 2.2 | 8.0 ± 1.0 | 2 |
| t130-desigual-indiv-8 | 30 | 53 % ± 9 | 47 % ± 9 | 2.4 ± 0.5 | 2.2 ± 0.5 | 42 ± 6 | 172 ± 4 | 171 ± 4 | 70 % ± 4 | 43.6 ± 2.2 | 7.5 ± 0.9 | 1 |
| t130-desigual-capa-10 | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 6.8 ± 0.2 | 14 ± 2 | 156 ± 3 | 186 ± 1 | 94 % ± 2 | 27.9 ± 1.0 | 3.4 ± 0.2 | 0 |
| t130-desigual-indiv-10 | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 6.8 ± 0.3 | 17 ± 4 | 156 ± 4 | 187 ± 1 | 94 % ± 1 | 28.2 ± 1.0 | 4.0 ± 0.2 | 1 |
| t130-desigual-capa-12 | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 9.1 ± 0.3 | 11 ± 1 | 153 ± 4 | 185 ± 1 | 96 % ± 1 | 26.4 ± 0.4 | 3.9 ± 0.2 | 0 |
| t130-desigual-indiv-12 | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 9.0 ± 0.3 | 11 ± 1 | 156 ± 4 | 183 ± 1 | 95 % ± 1 | 25.9 ± 0.4 | 3.9 ± 0.1 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t130-desigual-capa-8 semilla 12300: sprint → red (0/4 vivos), normal → blue (6/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 29 de 29 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t130-desigual-capa-8: enemy:arrow=184
- t130-desigual-indiv-8: enemy:arrow=167
- t130-desigual-capa-10: enemy:arrow=240
- t130-desigual-indiv-10: enemy:arrow=240
- t130-desigual-capa-12: enemy:arrow=240
- t130-desigual-indiv-12: enemy:arrow=240

## Motivos de decisión (cambios registrados): ENGAGE 3570, LOW_HEALTH_RETREAT 3850, NONE 1830, OUT_OF_RANGE 846, SEEK_ENEMY 103

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t130-desigual-capa-8 - 2 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t130-desigual-indiv-8 - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t130-desigual-indiv-10 - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- t130-desigual-capa-8: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)

# Simulación Rally - lote 20261105-132329-t10-vet

Preset `t10-vet` · velocidad `sprint` · 55 corridas · 4 escenarios.
> Mobs ajenos retirados del campo entre corridas (tercer bando oculto): {minecraft:zombie_villager=2}

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t10-vet-linea-r0 | 6 | 83 % ± 17 | 17 % ± 17 | 6.2 ± 1.3 | 1.7 ± 1.7 | 294 ± 8 | 1946 ± 93 | 238 ± 75 | 79 % ± 1 | 7.7 ± 1.3 | 8.6 ± 1.3 | 8 |
| t10-vet-linea-r1 | 7 | 100 % ± 0 | 0 % ± 0 | 6.4 ± 0.7 | 0.0 ± 0.0 | 291 ± 6 | 1962 ± 80 | 188 ± 41 | 79 % ± 2 | 7.6 ± 1.2 | 8.5 ± 1.1 | 6 |
| t10-vet-linea-r3 | 7 | 100 % ± 0 | 0 % ± 0 | 6.9 ± 0.5 | 0.0 ± 0.0 | 290 ± 4 | 1957 ± 45 | 198 ± 34 | 80 % ± 1 | 7.9 ± 1.1 | 8.5 ± 0.9 | 6 |
| t10-vet-linea-r5 | 6 | 83 % ± 17 | 17 % ± 17 | 6.0 ± 1.2 | 1.5 ± 1.5 | 278 ± 15 | 1852 ± 179 | 221 ± 45 | 80 % ± 2 | 7.8 ± 1.6 | 8.2 ± 1.5 | 8 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t10-vet-linea-r0 semilla 1: sprint → blue (8/0 vivos), normal → blue (7/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 24 de 24 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t10-vet-linea-r0: enemy:explosion.player=6 enemy:mob=2
- t10-vet-linea-r1: enemy:explosion.player=11
- t10-vet-linea-r3: enemy:explosion.player=8
- t10-vet-linea-r5: enemy:explosion.player=10 enemy:mob=2

## Motivos de decisión (cambios registrados): COUNTERATTACK 18, COVER_AVAILABLE 361, ENGAGE 3767, FOCUS_FIRE 528, FORMATION_HOLD 14, LOW_HEALTH_RETREAT 56, MORALE_SHAKEN 93, MORALE_WAVERING 19, NONE 1811, OUTNUMBERED 8, OUT_OF_RANGE 62, PAIR_BOUND 250, RALLIED 2, REFUSE_ATTACK 11, ROUTED_FLEE 21, SEEK_ENEMY 711, SHOOT_AND_MOVE 139, STUCK 12, TARGET_PRIORITY 1326, THREAT_MEMORY 22, TOO_CLOSE 1013

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t10-vet-linea-r5 - 8 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t10-vet-linea-r0 - 8 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t10-vet-linea-r1 - 6 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t10-vet-linea-r3 - 6 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)

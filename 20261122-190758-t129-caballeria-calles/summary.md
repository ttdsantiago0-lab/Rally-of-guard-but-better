# Simulación Rally - lote 20261122-190758-t129-caballeria-calles

Preset `t129-caballeria-calles` · velocidad `sprint` · 181 corridas · 6 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t129-caballeria-village-pie | 30 | 32 % ± 8 | 68 % ± 8 | 1.3 ± 0.4 | 2.6 ± 0.4 | 32 ± 5 | 244 ± 7 | 175 ± 7 | 0 % ± 0 | 5.9 ± 0.2 | 4.0 ± 0.2 | 0 |
| t129-caballeria-village-jinete | 30 | 23 % ± 5 | 77 % ± 5 | 1.4 ± 0.3 | 7.4 ± 0.3 | 92 ± 15 | 127 ± 5 | 173 ± 6 | 0 % ± 0 | 6.2 ± 0.4 | 3.7 ± 0.4 | 0 |
| t129-caballeria-corridors-pie | 30 | 50 % ± 9 | 50 % ± 9 | 1.9 ± 0.4 | 2.4 ± 0.5 | 34 ± 6 | 238 ± 8 | 170 ± 6 | 0 % ± 0 | 7.8 ± 0.2 | 4.1 ± 0.3 | 0 |
| t129-caballeria-corridors-jinete | 30 | 2 % ± 2 | 98 % ± 2 | 0.1 ± 0.1 | 8.9 ± 0.3 | 27 ± 6 | 114 ± 5 | 195 ± 1 | 0 % ± 0 | 10.3 ± 0.1 | 5.3 ± 0.4 | 0 |
| t129-caballeria-flat-pie | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 9.6 ± 0.3 | 11 ± 0 | 121 ± 4 | 197 ± 1 | 0 % ± 0 | 6.1 ± 0.1 | 4.0 ± 0.2 | 0 |
| t129-caballeria-flat-jinete | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 10.0 ± 0.3 | 11 ± 0 | 102 ± 4 | 197 ± 1 | 0 % ± 0 | 8.1 ± 0.1 | 4.4 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t129-caballeria-village-pie semilla 12200: sprint → blue (3/0 vivos), normal → red (0/2 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 40 de 40 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t129-caballeria-village-pie: enemy:mob=193 enemy:scguns.bullet.annihilated=1 enemy:scguns.bullet.decimated=2 enemy:scguns.bullet.executed=3 enemy:scguns.bullet.killed=1
- t129-caballeria-village-jinete: enemy:mob=198 enemy:scguns.bullet.executed=1
- t129-caballeria-corridors-pie: enemy:mob=169 enemy:scguns.bullet.annihilated=1 enemy:scguns.bullet.decimated=6 enemy:scguns.bullet.eliminated=2 enemy:scguns.bullet.executed=3 enemy:scguns.bullet.killed=2
- t129-caballeria-corridors-jinete: enemy:mob=238
- t129-caballeria-flat-pie: enemy:mob=227 enemy:scguns.bullet.annihilated=2 enemy:scguns.bullet.decimated=2 enemy:scguns.bullet.eliminated=2 enemy:scguns.bullet.executed=2 enemy:scguns.bullet.killed=5
- t129-caballeria-flat-jinete: enemy:mob=238 enemy:scguns.bullet.eliminated=2

## Motivos de decisión (cambios registrados): ENGAGE 1467, FORMATION_HOLD 225, LOW_HEALTH_RETREAT 230, NONE 9, SEEK_ENEMY 118, STUCK 8

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t129-caballeria-village-pie: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)

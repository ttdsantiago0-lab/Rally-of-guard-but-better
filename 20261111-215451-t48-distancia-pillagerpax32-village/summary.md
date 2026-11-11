# Simulación Rally - lote 20261111-215451-t48-distancia-pillagerpax32-village

Preset `t48-distancia-pillagerpax32-village` · velocidad `sprint` · 287 corridas · 6 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t48-distancia-pillagerpax32-village-arco-sinregla | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 24.2 ± 0.6 | 13 ± 1 | 182 ± 6 | 214 ± 3 | 70 % ± 1 | 17.0 ± 0.3 | 5.1 ± 0.3 | 0 |
| t48-distancia-pillagerpax32-village-linea-sinregla | 20 | 3 % ± 3 | 98 % ± 3 | 0.2 ± 0.2 | 14.3 ± 1.0 | 53 ± 9 | 220 ± 17 | 229 ± 6 | 67 % ± 2 | 10.0 ± 0.5 | 3.3 ± 0.2 | 26 |
| t48-distancia-pillagerpax32-village-lineascutum-regla | 7 | 93 % ± 7 | 7 % ± 7 | 5.9 ± 0.3 | 1.1 ± 1.1 | 131 ± 9 | 457 ± 31 | 148 ± 12 | 10 % ± 1 | 31.9 ± 0.9 | 25.2 ± 0.5 | 50 |
| t48-distancia-pillagerpax32-village-lineascutum-sinregla | 23 | 17 % ± 7 | 83 % ± 7 | 0.7 ± 0.3 | 11.5 ± 1.3 | 87 ± 12 | 264 ± 26 | 235 ± 7 | 67 % ± 2 | 8.9 ± 0.6 | 2.9 ± 0.2 | 23 |
| t48-distancia-pillagerpax32-village-arco-regla | 17 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 19.3 ± 1.0 | 30 ± 1 | 356 ± 15 | 233 ± 5 | 41 % ± 2 | 39.2 ± 0.4 | 17.4 ± 1.2 | 30 |
| t48-distancia-pillagerpax32-village-linea-regla | 1 | 50 % ± 0 | 50 % ± 0 | 4.0 ± 0.0 | 4.0 ± 0.0 | 180 ± 0 | 500 ± 0 | 180 ± 0 | 7 % ± 0 | 35.1 ± 0.0 | 22.8 ± 0.0 | 59 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t48-distancia-pillagerpax32-village-arco-regla semilla 2001: sprint → draw (3/12 vivos), normal → red (0/19 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 15 de 15 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t48-distancia-pillagerpax32-village-arco-sinregla: enemy:scguns.bullet.annihilated=54 enemy:scguns.bullet.decimated=43 enemy:scguns.bullet.eliminated=36 enemy:scguns.bullet.executed=53 enemy:scguns.bullet.killed=54
- t48-distancia-pillagerpax32-village-linea-sinregla: enemy:scguns.bullet.annihilated=33 enemy:scguns.bullet.decimated=33 enemy:scguns.bullet.eliminated=26 enemy:scguns.bullet.executed=28 enemy:scguns.bullet.killed=36
- t48-distancia-pillagerpax32-village-lineascutum-regla: enemy:scguns.bullet.annihilated=1 enemy:scguns.bullet.decimated=4 enemy:scguns.bullet.eliminated=3 enemy:scguns.bullet.executed=2 enemy:scguns.bullet.killed=5
- t48-distancia-pillagerpax32-village-lineascutum-sinregla: enemy:scguns.bullet.annihilated=36 enemy:scguns.bullet.decimated=28 enemy:scguns.bullet.eliminated=29 enemy:scguns.bullet.executed=35 enemy:scguns.bullet.killed=41
- t48-distancia-pillagerpax32-village-arco-regla: enemy:scguns.bullet.annihilated=27 enemy:scguns.bullet.decimated=31 enemy:scguns.bullet.eliminated=29 enemy:scguns.bullet.executed=29 enemy:scguns.bullet.killed=20
- t48-distancia-pillagerpax32-village-linea-regla: enemy:scguns.bullet.annihilated=2 enemy:scguns.bullet.killed=2

## Motivos de decisión (cambios registrados): ENGAGE 5198, LOW_HEALTH_RETREAT 1093, NONE 196, OUT_OF_RANGE 2918, SEEK_ENEMY 550, STUCK 507, TOO_CLOSE 52

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t48-distancia-pillagerpax32-village-arco-regla - 30 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t48-distancia-pillagerpax32-village-linea-regla - 59 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t48-distancia-pillagerpax32-village-lineascutum-regla - 50 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t48-distancia-pillagerpax32-village-lineascutum-sinregla - 23 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t48-distancia-pillagerpax32-village-linea-sinregla - 26 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- t48-distancia-pillagerpax32-village-linea-regla: 1/1 corridas llegaron al tope de tiempo

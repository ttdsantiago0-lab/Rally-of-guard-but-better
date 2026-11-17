# Simulación Rally - lote 20261117-052148-t66-retaguardia-vindicadores80

Preset `t66-retaguardia-vindicadores80` · velocidad `sprint` · 323 corridas · 8 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t66-retaguardia-arco-pelear | 30 | 48 % ± 3 | 52 % ± 3 | 2.7 ± 0.3 | 13.2 ± 1.9 | 118 ± 2 | 1922 ± 29 | 190 ± 10 | 92 % ± 1 | 11.5 ± 0.7 | 24.7 ± 1.4 | 1 |
| t66-retaguardia-arco-huida | 25 | 38 % ± 4 | 62 % ± 4 | 1.3 ± 0.2 | 68.4 ± 0.6 | 110 ± 5 | 739 ± 8 | 213 ± 7 | 98 % ± 1 | 7.6 ± 0.2 | 30.6 ± 2.4 | 17 |
| t66-retaguardia-arco-reta2 | 29 | 45 % ± 3 | 55 % ± 3 | 1.9 ± 0.2 | 52.7 ± 1.4 | 118 ± 1 | 1125 ± 32 | 210 ± 5 | 96 % ± 1 | 7.9 ± 0.2 | 31.2 ± 1.9 | 4 |
| t66-retaguardia-arco-reta4 | 30 | 48 % ± 2 | 52 % ± 2 | 1.9 ± 0.2 | 42.2 ± 2.2 | 120 ± 0 | 1364 ± 40 | 202 ± 7 | 93 % ± 1 | 9.2 ± 0.4 | 27.9 ± 2.2 | 3 |
| t66-retaguardia-linea-pelear | 30 | 43 % ± 3 | 57 % ± 3 | 1.4 ± 0.2 | 41.4 ± 2.7 | 115 ± 2 | 1245 ± 48 | 206 ± 6 | 93 % ± 1 | 10.5 ± 0.7 | 22.4 ± 1.9 | 1 |
| t66-retaguardia-linea-reta2 | 19 | 16 % ± 5 | 84 % ± 5 | 0.6 ± 0.2 | 73.1 ± 0.6 | 82 ± 8 | 434 ± 8 | 219 ± 6 | 98 % ± 0 | 8.0 ± 0.6 | 20.3 ± 3.4 | 27 |
| t66-retaguardia-linea-huida | 16 | 16 % ± 6 | 84 % ± 6 | 0.4 ± 0.2 | 72.9 ± 0.6 | 79 ± 9 | 422 ± 14 | 214 ± 4 | 97 % ± 1 | 8.4 ± 0.3 | 17.9 ± 3.1 | 36 |
| t66-retaguardia-linea-reta4 | 10 | 10 % ± 7 | 90 % ± 7 | 0.2 ± 0.1 | 71.6 ± 0.9 | 65 ± 10 | 426 ± 17 | 228 ± 5 | 98 % ± 1 | 7.8 ± 0.2 | 20.8 ± 2.4 | 44 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t66-retaguardia-arco-pelear semilla 5201: sprint → draw (6/5 vivos), normal → blue (6/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 141 de 141 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t66-retaguardia-arco-pelear: enemy:mob=122 enemy:scguns.bullet.annihilated=6 enemy:scguns.bullet.decimated=11 enemy:scguns.bullet.eliminated=7 enemy:scguns.bullet.executed=5 enemy:scguns.bullet.killed=9
- t66-retaguardia-arco-huida: enemy:mob=144 enemy:scguns.bullet.annihilated=4 enemy:scguns.bullet.decimated=6 enemy:scguns.bullet.eliminated=1 enemy:scguns.bullet.executed=6 enemy:scguns.bullet.killed=6
- t66-retaguardia-arco-reta2: enemy:mob=160 enemy:scguns.bullet.annihilated=3 enemy:scguns.bullet.decimated=3 enemy:scguns.bullet.eliminated=2 enemy:scguns.bullet.executed=6 enemy:scguns.bullet.killed=4
- t66-retaguardia-arco-reta4: enemy:mob=169 enemy:scguns.bullet.annihilated=3 enemy:scguns.bullet.decimated=1 enemy:scguns.bullet.eliminated=4 enemy:scguns.bullet.executed=3 enemy:scguns.bullet.killed=3
- t66-retaguardia-linea-pelear: enemy:mob=176 enemy:scguns.bullet.annihilated=3 enemy:scguns.bullet.decimated=8 enemy:scguns.bullet.eliminated=4 enemy:scguns.bullet.executed=4 enemy:scguns.bullet.killed=4
- t66-retaguardia-linea-reta2: enemy:mob=132 enemy:scguns.bullet.annihilated=1 enemy:scguns.bullet.decimated=2 enemy:scguns.bullet.eliminated=1 enemy:scguns.bullet.executed=2 enemy:scguns.bullet.killed=2
- t66-retaguardia-linea-huida: enemy:mob=116 enemy:scguns.bullet.annihilated=2 enemy:scguns.bullet.eliminated=1 enemy:scguns.bullet.executed=1 enemy:scguns.bullet.killed=1
- t66-retaguardia-linea-reta4: enemy:mob=74 enemy:scguns.bullet.annihilated=1 enemy:scguns.bullet.decimated=1 enemy:scguns.bullet.eliminated=1 enemy:scguns.bullet.killed=1

## Motivos de decisión (cambios registrados): ENGAGE 3841, FORMATION_HOLD 162, INSTRUCTION 1316, LOW_HEALTH_RETREAT 145, ORDER_OVERRIDE 1274, OUT_OF_RANGE 1298, SEEK_ENEMY 827, TOO_CLOSE 106, UNIT_BROKEN 177

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t66-retaguardia-arco-huida - 17 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t66-retaguardia-linea-huida - 36 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t66-retaguardia-linea-reta2 - 27 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t66-retaguardia-linea-reta4 - 44 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t66-retaguardia-arco-reta4 - 3 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t66-retaguardia-arco-reta2 - 4 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t66-retaguardia-linea-pelear - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t66-retaguardia-arco-pelear - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- t66-retaguardia-arco-pelear: 27/30 corridas llegaron al tope de tiempo
- t66-retaguardia-arco-huida: 19/25 corridas llegaron al tope de tiempo
- t66-retaguardia-arco-reta2: 26/29 corridas llegaron al tope de tiempo
- t66-retaguardia-arco-reta4: 29/30 corridas llegaron al tope de tiempo
- t66-retaguardia-linea-pelear: 26/30 corridas llegaron al tope de tiempo
- t66-retaguardia-arco-pelear: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)

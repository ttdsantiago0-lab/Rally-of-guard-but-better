# Simulación Rally - lote 20261123-050019-t141-kitebig-jefes

Preset `t141-kitebig-jefes` · velocidad `sprint` · 186 corridas · 6 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t141-monstrosity-defecto | 30 | 100 % ± 0 | 0 % ± 0 | 7.3 ± 0.2 | 0.0 ± 0.0 | 52 ± 1 | 619 ± 4 | 92 ± 3 | 62 % ± 1 | 17.8 ± 0.1 | 13.0 ± 0.2 | 0 |
| t141-monstrosity-kiteoff | 30 | 100 % ± 0 | 0 % ± 0 | 6.0 ± 0.2 | 0.0 ± 0.0 | 48 ± 0 | 612 ± 3 | 165 ± 5 | 61 % ± 1 | 15.7 ± 0.2 | 10.7 ± 0.2 | 0 |
| t141-tremorzilla-defecto | 29 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 43 ± 6 | 172 ± 24 | 197 ± 5 | 92 % ± 1 | 24.7 ± 0.9 | 10.5 ± 1.0 | 4 |
| t141-tremorzilla-kiteoff | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 38 ± 5 | 177 ± 21 | 198 ± 3 | 92 % ± 1 | 23.8 ± 1.0 | 9.9 ± 0.7 | 0 |
| t141-enderguardian-defecto | 30 | 47 % ± 2 | 53 % ± 2 | 3.2 ± 0.3 | 1.0 ± 0.0 | 230 ± 7 | 0 ± 0 | 416 ± 16 | 95 % ± 0 | 18.2 ± 0.1 | 9.6 ± 0.3 | 1 |
| t141-enderguardian-kiteoff | 30 | 50 % ± 0 | 50 % ± 0 | 3.9 ± 0.3 | 1.0 ± 0.0 | 240 ± 0 | 0 ± 0 | 495 ± 15 | 97 % ± 0 | 14.2 ± 0.1 | 6.8 ± 0.1 | 1 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t141-monstrosity-defecto semilla 12800: sprint → blue (8/0 vivos), normal → blue (7/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 118 de 118 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t141-monstrosity-defecto: enemy:mob=14 env:lava=8
- t141-monstrosity-kiteoff: enemy:explosion.player=2 enemy:mob=39 env:lava=11 env:onFire=7
- t141-tremorzilla-defecto: enemy:mob=4 enemy:tremorzilla_beam=221 env:radiation=7
- t141-tremorzilla-kiteoff: enemy:mob=1 enemy:tremorzilla_beam=234 env:radiation=5
- t141-enderguardian-defecto: enemy:explosion.player=2 enemy:indirectMagic=3 enemy:mob=139
- t141-enderguardian-kiteoff: enemy:explosion.player=6 enemy:indirectMagic=20 enemy:mob=97

## Motivos de decisión (cambios registrados): ENGAGE 3098, FORMATION_HOLD 2, LOW_HEALTH_RETREAT 1362, NONE 583, OUT_OF_RANGE 425, SEEK_ENEMY 871, STUCK 14, TOO_CLOSE 677

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t141-tremorzilla-defecto - 4 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t141-enderguardian-defecto - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t141-enderguardian-kiteoff - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- t141-enderguardian-defecto: 28/30 corridas llegaron al tope de tiempo
- t141-enderguardian-kiteoff: 30/30 corridas llegaron al tope de tiempo
- MUERTES QUE NO SON DEL ENEMIGO: t141-monstrosity-defecto - 36 % de las bajas azules ({enemy:mob=14.0, env:lava=8.0})
- MUERTES QUE NO SON DEL ENEMIGO: t141-monstrosity-kiteoff - 31 % de las bajas azules ({enemy:explosion.player=2.0, enemy:mob=39.0, env:lava=11.0, env:onFire=7.0})

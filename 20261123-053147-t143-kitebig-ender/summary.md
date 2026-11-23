# Simulación Rally - lote 20261123-053147-t143-kitebig-ender

Preset `t143-kitebig-ender` · velocidad `sprint` · 124 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t143-kitebig-ender-arcos-defecto | 30 | 43 % ± 3 | 57 % ± 3 | 3.0 ± 0.3 | 1.0 ± 0.0 | 236 ± 2 | 0 ± 0 | 436 ± 18 | 96 % ± 0 | 18.2 ± 0.1 | 9.6 ± 0.3 | 3 |
| t143-kitebig-ender-arcos-kiteoff | 30 | 43 % ± 3 | 57 % ± 3 | 3.2 ± 0.3 | 1.0 ± 0.0 | 236 ± 2 | 0 ± 0 | 484 ± 14 | 97 % ± 0 | 14.3 ± 0.1 | 7.0 ± 0.2 | 0 |
| t143-kitebig-ender-mixto-defecto | 30 | 100 % ± 0 | 0 % ± 0 | 7.4 ± 0.2 | 0.0 ± 0.0 | 70 ± 1 | 335 ± 0 | 156 ± 8 | 89 % ± 1 | 11.3 ± 0.1 | 8.9 ± 0.2 | 0 |
| t143-kitebig-ender-mixto-kiteoff | 30 | 100 % ± 0 | 0 % ± 0 | 7.4 ± 0.2 | 0.0 ± 0.0 | 66 ± 1 | 335 ± 0 | 167 ± 7 | 87 % ± 2 | 9.0 ± 0.2 | 6.8 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t143-kitebig-ender-arcos-defecto semilla 13000: sprint → draw (8/1 vivos), normal → draw (5/1 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 112 de 112 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t143-kitebig-ender-arcos-defecto: enemy:explosion.player=1 enemy:indirectMagic=5 enemy:mob=143
- t143-kitebig-ender-arcos-kiteoff: enemy:explosion.player=7 enemy:indirectMagic=27 enemy:mob=109
- t143-kitebig-ender-mixto-defecto: enemy:indirectMagic=17 enemy:mob=1
- t143-kitebig-ender-mixto-kiteoff: enemy:indirectMagic=11 enemy:mob=7

## Motivos de decisión (cambios registrados): ENGAGE 2169, FORMATION_HOLD 3, LOW_HEALTH_RETREAT 1023, NONE 26, OUT_OF_RANGE 114, SEEK_ENEMY 612, STUCK 57, TOO_CLOSE 682

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t143-kitebig-ender-arcos-defecto - 3 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- t143-kitebig-ender-arcos-defecto: 26/30 corridas llegaron al tope de tiempo
- t143-kitebig-ender-arcos-kiteoff: 26/30 corridas llegaron al tope de tiempo

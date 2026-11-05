# Simulación Rally - lote 20261105-140153-t11-cab-waves

Preset `t11-cab-waves` · velocidad `sprint` · 22 corridas · 2 escenarios.
> Mobs ajenos retirados del campo entre corridas (tercer bando oculto): {minecraft:zombie_villager=15}

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t11-waves-mixta-cab-comida-base | 10 | 50 % ± 17 | 50 % ± 17 | 2.8 ± 1.0 | 4.0 ± 1.4 | 301 ± 8 | 1704 ± 89 | 386 ± 23 | 83 % ± 1 | 9.5 ± 1.1 | 8.6 ± 0.9 | 1 |
| t11-waves-mixta-cab-comida-antes | 10 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 7.9 ± 1.0 | 169 ± 18 | 755 ± 50 | 355 ± 24 | 91 % ± 2 | 7.2 ± 0.4 | 7.3 ± 0.6 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t11-waves-mixta-cab-comida-base semilla 1: sprint → blue (7/0 vivos), normal → red (0/11 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 5 de 5 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t11-waves-mixta-cab-comida-base: enemy:explosion.player=37 enemy:mob=8
- t11-waves-mixta-cab-comida-antes: enemy:arrow=3 enemy:explosion.player=42 enemy:mob=18

## Motivos de decisión (cambios registrados): COUNTERATTACK 18, ENGAGE 1023, FORMATION_HOLD 21, LOW_HEALTH_RETREAT 92, MORALE_SHAKEN 151, MORALE_WAVERING 103, NONE 822, OUT_OF_RANGE 3, RALLIED 11, REFUSE_ATTACK 25, ROUTED_FLEE 73, SEEK_ENEMY 249, TOO_CLOSE 399

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t11-waves-mixta-cab-comida-base - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- t11-waves-mixta-cab-comida-base: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)

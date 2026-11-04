# Simulación Rally - lote 20261104-194136-t3-final-zombie-a

Preset `t3-final-zombie-a` · velocidad `sprint` · 241 corridas · 8 escenarios.
> Mobs ajenos retirados del campo entre corridas (tercer bando oculto): {minecraft:zombie_villager=103}

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t3-final-zombie-mele-base | 30 | 40 % ± 9 | 60 % ± 9 | 2.4 ± 0.6 | 8.1 ± 1.4 | 30 ± 1 | 539 ± 17 | 156 ± 7 | 0 % ± 0 | 3.9 ± 0.2 | 5.2 ± 0.2 | 0 |
| t3-final-zombie-mele-mantener | 30 | 47 % ± 9 | 53 % ± 9 | 2.7 ± 0.5 | 5.5 ± 1.1 | 33 ± 1 | 578 ± 13 | 153 ± 8 | 0 % ± 0 | 5.6 ± 0.1 | 4.5 ± 0.1 | 0 |
| t3-final-zombie-mele-circulo | 30 | 63 % ± 9 | 37 % ± 9 | 3.7 ± 0.6 | 3.3 ± 0.9 | 31 ± 1 | 595 ± 9 | 134 ± 9 | 0 % ± 0 | 3.7 ± 0.1 | 4.0 ± 0.2 | 0 |
| t3-final-zombie-escudo-base | 30 | 50 % ± 9 | 50 % ± 9 | 3.1 ± 0.6 | 5.3 ± 1.2 | 49 ± 3 | 713 ± 14 | 145 ± 10 | 0 % ± 0 | 4.2 ± 0.2 | 6.6 ± 0.5 | 0 |
| t3-final-zombie-escudo-circulo | 30 | 70 % ± 9 | 30 % ± 9 | 4.6 ± 0.6 | 3.5 ± 1.1 | 42 ± 2 | 715 ± 10 | 115 ± 10 | 0 % ± 0 | 3.5 ± 0.1 | 4.4 ± 0.2 | 0 |
| t3-final-zombie-escudo-escudoarriba | 30 | 33 % ± 9 | 67 % ± 9 | 2.1 ± 0.6 | 5.8 ± 0.9 | 67 ± 4 | 732 ± 8 | 165 ± 10 | 0 % ± 0 | 6.4 ± 0.3 | 6.9 ± 0.3 | 0 |
| t3-final-zombie-arco-base | 30 | 83 % ± 7 | 17 % ± 7 | 4.9 ± 0.5 | 1.7 ± 0.7 | 47 ± 3 | 361 ± 8 | 84 ± 9 | 87 % ± 1 | 9.0 ± 0.4 | 7.3 ± 0.4 | 0 |
| t3-final-zombie-arco-quemarropa | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 14 ± 0 | 324 ± 2 | 2 ± 0 | 86 % ± 1 | 8.0 ± 0.2 | 3.1 ± 0.1 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t3-final-zombie-mele-base semilla 1: sprint → red (0/15 vivos), normal → blue (6/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 146 de 146 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t3-final-zombie-mele-base: enemy:mob=82 other(minecraft:zombie_villager):mob=1
- t3-final-zombie-mele-mantener: enemy:mob=89
- t3-final-zombie-mele-circulo: enemy:mob=55 other(minecraft:zombie_villager):mob=1
- t3-final-zombie-escudo-base: enemy:mob=70
- t3-final-zombie-escudo-circulo: enemy:mob=55
- t3-final-zombie-escudo-escudoarriba: enemy:mob=103
- t3-final-zombie-arco-base: enemy:mob=46

## Motivos de decisión (cambios registrados): COUNTERATTACK 216, ENGAGE 6127, FORMATION_HOLD 32, INSTRUCTION 240, LOW_HEALTH_RETREAT 1040, MORALE_SHAKEN 3238, MORALE_WAVERING 1325, NONE 8, OUT_OF_RANGE 15, RALLIED 12, REFUSE_ATTACK 1194, ROUTED_FLEE 779, SEEK_ENEMY 629, TOO_CLOSE 802

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t3-final-zombie-mele-base: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)

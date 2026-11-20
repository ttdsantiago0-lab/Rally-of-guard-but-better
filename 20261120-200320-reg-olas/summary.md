# Simulación Rally - lote 20261120-200320-reg-olas

Preset `reg-olas` · velocidad `sprint` · 44 corridas · 5 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t8-waves-mixta-linea-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 265 ± 2 | 2160 ± 15 | 63 ± 5 | 85 % ± 1 | 12.6 ± 0.4 | 13.0 ± 0.3 | 0 |
| t8-waves-mixta-arco-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 242 ± 1 | 2400 ± 11 | 51 ± 3 | 85 % ± 1 | 15.1 ± 0.2 | 13.5 ± 0.3 | 0 |
| t8-waves-mixta-escudo-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 518 ± 22 | 1621 ± 18 | 152 ± 9 | 0 % ± 0 | 15.4 ± 1.1 | 16.4 ± 1.1 | 2 |
| t16-moral-linea-nueva | 8 | 100 % ± 0 | 0 % ± 0 | 7.8 ± 0.2 | 0.0 ± 0.0 | 343 ± 3 | 4395 ± 56 | 158 ± 9 | 86 % ± 1 | 12.8 ± 0.3 | 13.4 ± 0.4 | 0 |
| t16-moral-r0-nueva | 8 | 100 % ± 0 | 0 % ± 0 | 7.6 ± 0.4 | 0.0 ± 0.0 | 346 ± 4 | 4424 ± 62 | 176 ± 16 | 87 % ± 1 | 12.7 ± 0.3 | 12.6 ± 0.5 | 1 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t8-waves-mixta-linea-comida-base semilla 10300: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 40 de 40 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t8-waves-mixta-linea-comida-base: enemy:explosion.player=1
- t16-moral-linea-nueva: enemy:explosion.player=2
- t16-moral-r0-nueva: enemy:explosion.player=2 enemy:mob=1

## Motivos de decisión (cambios registrados): COUNTERATTACK 16, ENGAGE 4164, LOW_HEALTH_RETREAT 34, MORALE_SHAKEN 440, MORALE_WAVERING 78, NONE 3169, OUT_OF_RANGE 74, RALLIED 5, REFUSE_ATTACK 2, ROUTED_FLEE 32, SEEK_ENEMY 916, STUCK 16, TOO_CLOSE 93

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t16-moral-r0-nueva - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t8-waves-mixta-escudo-comida-base - 2 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)

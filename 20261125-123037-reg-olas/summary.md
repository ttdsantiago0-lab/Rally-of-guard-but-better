# Simulación Rally - lote 20261125-123037-reg-olas

Preset `reg-olas` · velocidad `sprint` · 44 corridas · 5 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t8-waves-mixta-linea-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 267 ± 1 | 2206 ± 19 | 59 ± 4 | 85 % ± 1 | 12.1 ± 0.3 | 12.6 ± 0.4 | 0 |
| t8-waves-mixta-arco-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 242 ± 1 | 2393 ± 5 | 54 ± 3 | 85 % ± 1 | 14.5 ± 0.1 | 13.5 ± 0.4 | 0 |
| t8-waves-mixta-escudo-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 558 ± 23 | 1669 ± 23 | 167 ± 12 | 0 % ± 0 | 15.9 ± 1.1 | 17.2 ± 1.3 | 2 |
| t16-moral-linea-nueva | 8 | 100 % ± 0 | 0 % ± 0 | 7.8 ± 0.2 | 0.0 ± 0.0 | 345 ± 5 | 4398 ± 77 | 176 ± 8 | 85 % ± 1 | 13.1 ± 0.4 | 13.7 ± 0.3 | 0 |
| t16-moral-r0-nueva | 8 | 100 % ± 0 | 0 % ± 0 | 7.8 ± 0.2 | 0.0 ± 0.0 | 356 ± 4 | 4440 ± 52 | 181 ± 19 | 87 % ± 1 | 13.1 ± 0.2 | 13.3 ± 0.4 | 1 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t8-waves-mixta-linea-comida-base semilla 1: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 40 de 40 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t16-moral-linea-nueva: enemy:explosion.player=2
- t16-moral-r0-nueva: enemy:explosion.player=2

## Motivos de decisión (cambios registrados): COUNTERATTACK 1, ENGAGE 4459, LOW_HEALTH_RETREAT 59, MORALE_SHAKEN 571, MORALE_WAVERING 104, NONE 3198, OUT_OF_RANGE 73, RALLIED 14, REFUSE_ATTACK 19, ROUTED_FLEE 17, SEEK_ENEMY 1146, STUCK 29, TOO_CLOSE 131

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t8-waves-mixta-escudo-comida-base - 2 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t16-moral-r0-nueva - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)

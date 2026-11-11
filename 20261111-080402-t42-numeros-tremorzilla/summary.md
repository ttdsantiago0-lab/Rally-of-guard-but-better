# Simulación Rally - lote 20261111-080402-t42-numeros-tremorzilla

Preset `t42-numeros-tremorzilla` · velocidad `sprint` · 33 corridas · 6 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t42-numeros-tremorzilla-hierro-g8-sincro | 5 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 29 ± 1 | 128 ± 12 | 259 ± 9 | 89 % ± 3 | 16.9 ± 0.7 | 8.7 ± 0.2 | 0 |
| t42-numeros-tremorzilla-netheritamas-g8-sincro | 5 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 106 ± 4 | 585 ± 120 | 189 ± 8 | 91 % ± 2 | 20.6 ± 2.3 | 13.8 ± 3.0 | 1 |
| t42-numeros-tremorzilla-hierro-g16-sincro | 5 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 41 ± 15 | 155 ± 35 | 513 ± 10 | 91 % ± 3 | 17.3 ± 1.5 | 8.0 ± 0.9 | 0 |
| t42-numeros-tremorzilla-netheritamas-g16-sincro | 5 | 60 % ± 24 | 40 % ± 24 | 6.4 ± 2.9 | 0.4 ± 0.2 | 123 ± 26 | 1070 ± 104 | 291 ± 41 | 89 % ± 3 | 19.2 ± 1.6 | 14.8 ± 1.6 | 1 |
| t42-numeros-tremorzilla-hierro-g32-sincro | 5 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 86 ± 11 | 302 ± 69 | 1122 ± 48 | 88 % ± 3 | 21.1 ± 1.5 | 10.0 ± 2.0 | 0 |
| t42-numeros-tremorzilla-netheritamas-g32-sincro | 5 | 100 % ± 0 | 0 % ± 0 | 21.6 ± 2.9 | 0.0 ± 0.0 | 65 ± 3 | 793 ± 27 | 403 ± 46 | 87 % ± 2 | 24.8 ± 0.8 | 19.8 ± 0.9 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t42-numeros-tremorzilla-hierro-g8-sincro semilla 1: sprint → red (0/1 vivos), normal → red (0/1 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 8 de 8 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t42-numeros-tremorzilla-hierro-g8-sincro: enemy:tremorzilla_beam=40
- t42-numeros-tremorzilla-netheritamas-g8-sincro: enemy:mob=6 enemy:tremorzilla_beam=32 env:radiation=2
- t42-numeros-tremorzilla-hierro-g16-sincro: enemy:tremorzilla_beam=78 env:radiation=2
- t42-numeros-tremorzilla-netheritamas-g16-sincro: enemy:mob=11 enemy:tremorzilla_beam=32 env:radiation=5
- t42-numeros-tremorzilla-hierro-g32-sincro: enemy:mob=27 enemy:tremorzilla_beam=126 env:radiation=7
- t42-numeros-tremorzilla-netheritamas-g32-sincro: enemy:mob=16 enemy:tremorzilla_beam=34 env:radiation=2

## Motivos de decisión (cambios registrados): ENGAGE 1305, FORMATION_HOLD 4, LOW_HEALTH_RETREAT 687, NONE 764, OUT_OF_RANGE 223, SEEK_ENEMY 804, STUCK 20, TOO_CLOSE 61

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t42-numeros-tremorzilla-netheritamas-g8-sincro - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t42-numeros-tremorzilla-netheritamas-g16-sincro - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- MUERTES QUE NO SON DEL ENEMIGO: t42-numeros-tremorzilla-netheritamas-g16-sincro - 10 % de las bajas azules ({enemy:mob=11.0, enemy:tremorzilla_beam=32.0, env:radiation=5.0})

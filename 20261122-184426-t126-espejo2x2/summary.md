# Simulación Rally - lote 20261122-184426-t126-espejo2x2

Preset `t126-espejo2x2` · velocidad `sprint` · 250 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t126-espejo2x2-arco-ambos | 60 | 47 % ± 6 | 53 % ± 6 | 1.8 ± 0.3 | 2.4 ± 0.3 | 43 ± 4 | 172 ± 3 | 176 ± 2 | 72 % ± 3 | 42.3 ± 1.5 | 7.3 ± 0.6 | 2 |
| t126-espejo2x2-arco-rojoindiv | 60 | 55 % ± 6 | 45 % ± 6 | 2.1 ± 0.3 | 2.0 ± 0.3 | 41 ± 5 | 174 ± 3 | 173 ± 3 | 72 % ± 3 | 42.5 ± 1.4 | 7.5 ± 0.5 | 1 |
| t126-espejo2x2-arco-azulindiv | 60 | 48 % ± 7 | 52 % ± 7 | 2.2 ± 0.3 | 2.4 ± 0.3 | 35 ± 4 | 170 ± 3 | 170 ± 3 | 75 % ± 3 | 40.1 ± 1.4 | 7.0 ± 0.5 | 2 |
| t126-espejo2x2-arco-ninguno | 60 | 52 % ± 6 | 48 % ± 6 | 2.2 ± 0.3 | 2.1 ± 0.3 | 47 ± 5 | 174 ± 3 | 172 ± 3 | 70 % ± 3 | 42.9 ± 1.5 | 7.8 ± 0.6 | 4 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t126-espejo2x2-arco-ambos semilla 11700: sprint → red (0/5 vivos), normal → red (0/4 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 122 de 122 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t126-espejo2x2-arco-ambos: enemy:arrow=374
- t126-espejo2x2-arco-rojoindiv: enemy:arrow=353
- t126-espejo2x2-arco-azulindiv: enemy:arrow=348
- t126-espejo2x2-arco-ninguno: enemy:arrow=350

## Motivos de decisión (cambios registrados): ENGAGE 4446, LOW_HEALTH_RETREAT 7334, NONE 4424, OUT_OF_RANGE 2374, SEEK_ENEMY 85, TOO_CLOSE 1

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t126-espejo2x2-arco-ambos - 2 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t126-espejo2x2-arco-azulindiv - 2 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t126-espejo2x2-arco-ninguno - 4 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t126-espejo2x2-arco-rojoindiv - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)

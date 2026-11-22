# Simulación Rally - lote 20261122-082812-t120-espejo

Preset `t120-espejo` · velocidad `sprint` · 185 corridas · 6 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t120-espejo-arco-ambos | 30 | 70 % ± 9 | 30 % ± 9 | 3.1 ± 0.4 | 1.2 ± 0.4 | 44 ± 6 | 180 ± 3 | 165 ± 4 | 67 % ± 4 | 44.4 ± 2.1 | 8.0 ± 0.8 | 2 |
| t120-espejo-arco-rojoindiv | 30 | 33 % ± 9 | 67 % ± 9 | 1.4 ± 0.4 | 2.9 ± 0.4 | 43 ± 6 | 168 ± 4 | 179 ± 4 | 73 % ± 4 | 42.4 ± 2.1 | 6.7 ± 0.8 | 2 |
| t120-espejo-arco-azulindiv | 30 | 42 % ± 9 | 58 % ± 9 | 2.0 ± 0.5 | 2.5 ± 0.5 | 42 ± 8 | 170 ± 4 | 172 ± 4 | 76 % ± 5 | 39.7 ± 2.1 | 6.3 ± 0.7 | 0 |
| t120-espejo-melee-ambos | 30 | 40 % ± 9 | 60 % ± 9 | 2.0 ± 0.5 | 3.3 ± 0.6 | 28 ± 3 | 164 ± 5 | 176 ± 4 | 0 % ± 0 | 5.2 ± 0.3 | 5.6 ± 0.3 | 0 |
| t120-espejo-melee-rojoindiv | 30 | 50 % ± 9 | 50 % ± 9 | 2.6 ± 0.5 | 2.9 ± 0.6 | 34 ± 3 | 169 ± 6 | 173 ± 5 | 0 % ± 0 | 6.6 ± 0.5 | 6.5 ± 0.4 | 0 |
| t120-espejo-melee-azulindiv | 30 | 47 % ± 9 | 53 % ± 9 | 2.7 ± 0.6 | 3.1 ± 0.6 | 27 ± 3 | 166 ± 5 | 168 ± 5 | 0 % ± 0 | 5.5 ± 0.3 | 5.6 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t120-espejo-arco-ambos semilla 11200: sprint → red (0/6 vivos), normal → red (0/3 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 85 de 85 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t120-espejo-arco-ambos: enemy:arrow=147
- t120-espejo-arco-rojoindiv: enemy:arrow=199
- t120-espejo-arco-azulindiv: enemy:arrow=180
- t120-espejo-melee-ambos: enemy:mob=181
- t120-espejo-melee-rojoindiv: enemy:mob=163
- t120-espejo-melee-azulindiv: enemy:mob=158

## Motivos de decisión (cambios registrados): ENGAGE 3152, LOW_HEALTH_RETREAT 5513, NONE 1742, OUT_OF_RANGE 886, SEEK_ENEMY 1721

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t120-espejo-arco-ambos - 2 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t120-espejo-arco-rojoindiv - 2 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)

# Veteranía: estado real (actualizado 2026-10-09, E3B bloque V)

Fuente: código de la rama `claude/modest-johnson-qiv68p` tras E3A + E3B. **Nada medido en juego**; las cifras de ritmo salen de la fórmula y deben sustituirse por las de `/rally sim batch pace` y `/rally sim batch ceiling` (sección "Ritmo de veteranía" y "Techo de excelencia" de `summary.md`).

## 1. Minutos de combate por rango
XP por rango (config `veterancy.rankXp`): R1 100 · R2 350 · R3 900 · R4 2000 · R5 4500. El XP sale de **amenaza × contribución** (no de daño bruto), con tope por víctima, rendimientos decrecientes por tipo y **tope de 80 XP por minuto** (`xpPerMinuteCap`). Tabla por tasa media de XP por guardia y minuto de combate (10 min de combate = 1 día, `combatMinutesPerDay`):

| Rango | 20 XP/min | 40 XP/min | 60 XP/min | 80 XP/min (tope) |
|---|---|---|---|---|
| R1 (100 XP) | 5 min (0.5 d) | 2 min (0.2 d) | 2 min (0.2 d) | 1 min (0.1 d) |
| R2 (350 XP) | 18 min (1.8 d) | 9 min (0.9 d) | 6 min (0.6 d) | 4 min (0.4 d) |
| R3 (900 XP) | 45 min (4.5 d) | 22 min (2.2 d) | 15 min (1.5 d) | 11 min (1.1 d) |
| R4 (2000 XP) | 100 min (10.0 d) | 50 min (5.0 d) | 33 min (3.3 d) | 25 min (2.5 d) |
| R5 (4500 XP) | 225 min (22.5 d) | 112 min (11.2 d) | 75 min (7.5 d) | 56 min (5.6 d) |

La tasa real la da `summary.md` → "Ritmo de veteranía" (con las **fuentes de XP**: combate, golpes recibidos, al borde de la muerte, batallas largas, mando). El vínculo con la montura crece por **tiempo montado**, no por XP. Objetivo de diseño: R2 ≈ 1–2 días, R3 ≈ 1 semana, R4 ≈ varias semanas, R5 raro.

## 2. Qué hay hecho
| Pieza | Estado |
|---|---|
| Rangos 0–5 por amenaza y contribución, tope por minuto y por víctima, solo combate real | hecho (E3A V1/V2, E3B R7) |
| 8 rasgos con efecto (Tanque, Tirador, Duelista, Jinete, Firme, Asalto, Explorador, Líder) | hecho: escalera de decisión, núcleo R5, liderazgo |
| Tras R5: XP a **vías** con rendimientos decrecientes (maestría, supervivencia, mando, montura, historia) | hecho (E3A V0) |
| **Vínculo con la montura** (V1) | hecho: reacción a órdenes hasta ×2 más rápida montado, recoge su caballo aparcado desde más lejos, mantiene el puesto montado (tolerancia −30 %); nivel 0–5 en la ficha. **No** hecho: "la montura no se asusta" (no se verificó una API/evento seguro sin mixin) |
| **Supervivencia: memoria de amenazas** (V2) | hecho: por tipo, guardada por UUID, ≤ 16 tipos, se olvida a la mitad cada 3 días; R3–4 y R5 guardan distancia, se apartan cuerpo a cuerpo y se retiran antes de lo que temen; motivo `THREAT_MEMORY` |
| **Historia y valor de contratación** (V3) | hecho: relato (12 líneas, 3 en la ficha) y valor en esmeraldas en la ficha (informativo) |
| **Experiencia de unidad** (V4) | hecho: `UnitBond` (combate juntos; decae al cambiar miembros) entra en `Cohesion`, en la experiencia de unidad del liderazgo y en la calidad de ejecución (tolerancia y respuesta) |
| **Moral y veteranía** (V5) | hecho (bloque M): sensibilidad por rango, ejemplo de R4–5; pruebas `morale-r0/r5-losses`, `morale-even-r3-captain` |
| **Maestría de arma** (V6) | hecho: 3 efectos nuevos sin daño (temple, foco, disciplina); cada arquetipo con perfil propio (arco, ballesta, arma de fuego, espada, hacha, lanza, escudo); ficha con nivel por arma y qué cambia |
| "Por qué decidió" y próximo rango (V7) | hecho: `/rally debug why` añade moral, memoria, instrucción, escenario, maestría, vínculo; la ficha muestra el siguiente rango, su XP y qué desbloquea |
| Techo (V8) | hecho: moral (tiempo hasta el primer ROTO) y consistencia junto a las 7 métricas; criterio sin tocar |
| Genética desde MCA | por reflexión configurable (E3A V6); sin accesor documentado |
| Clases por ítem | explorador (RECON) e ingeniero: ver bloque C |
| Muñeco de práctica | no |

## 3. Pendiente de David
- Correr `/rally sim batch pace`, `/rally sim batch ceiling` y `/rally sim batch veterancy` y pegar aquí la tasa real y las cifras R0→R5.
- Decidir si "la montura no se asusta" merece un mixin pequeño (hoy no hay efecto).

_La versión anterior de este documento (análisis E3A del 2026-10-08) está en el historial de git._

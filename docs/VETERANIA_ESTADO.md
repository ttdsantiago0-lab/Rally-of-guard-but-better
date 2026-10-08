# Veteranía: estado real y plan v2 (2026-10-08)

Fuente: código en `34b4c95` (`Veterancy`, `VeterancyEvents`, `Trait`, `Tactics`, `RallyConfig`) comparado con `docs/VETERANIA.md`. **Nada de esto está medido en juego**; las duraciones son cálculos sobre la fórmula actual.

## 1. Qué tan rápido se sube (cálculo con los valores por defecto)
- XP necesaria: **R1 100 · R2 350 · R3 900 · R4 2000 · R5 4500**.
- Fuentes: **1 XP por punto de daño hecho**, 0,5 por daño recibido de un enemigo real, **10 por baja**, **25 la primera vez que ve un tipo de enemigo**, **15 por pelea de más de 12 s**.
- Rendimientos decrecientes: ×0,9 por cada baja previa **del mismo tipo** (tope de memoria: 64 tipos).
- Resultado: contra un solo tipo (p. ej. zombis, 20 PV) el total máximo es ≈ (20+10)/0,1 ≈ **300 XP**: **no pasa del rango 1**. Para el rango 2 hacen falta unas **25 bajas** de ≥ 2–3 tipos; para el 3, ~70 bajas de ≥ 3 tipos; para el 4, ~250 bajas de ~7 tipos; para el 5, **~15 tipos distintos saturados o jefes**.
- **Conclusión:** rangos 1–2 llegan rápido (por eso viste a un guardia "veterano" pronto); el 5 **no es rápido por la fórmula**. **Pero hay tres distorsiones**:
  1. El XP cuenta el **daño causado**, incluso sobrante: un arma de 40–66 por disparo sobre un zombi de 20 PV da 2–3 veces más XP que una espada. Eso **contradice** "veteranía = calidad de decisión, no daño".
  2. Un jefe con muchos puntos de vida (dragón, ravager, warden) da cientos de XP de golpe y puede saltar rangos en una sola pelea.
  3. El decaimiento depende solo de **bajas** del tipo; el daño a un enemigo que no muere no decae.
- **Ritmo deseado (propuesta, por confirmar):** R2 ≈ 1–2 días de juego de combate regular; R3 ≈ una semana; R4 ≈ varias semanas; R5 raro y significativo; después, "leyenda" sin tope duro.

## 2. Lo que hay hecho de la veteranía
| Pieza | Estado |
|---|---|
| Rangos 0–5 por XP, guardado por UUID, avisos al comandante | hecho |
| Rendimientos decrecientes por tipo; solo combate real | hecho (con las distorsiones de arriba) |
| Un rasgo por rango (Tanque, Tirador, Duelista, Jinete, Firme, Asalto, Explorador) elegido por historia + genética (UUID) | **elegido, pero sin efecto mecánico para 4 de 7** (Tirador, Duelista, Jinete, Asalto: no encontré dónde se usan; solo Tanque, Firme y Explorador entran en la escalera de decisión) |
| Capacidad de mando por rango + bonus tras el rango 5 (cada vez que se duplica el XP extra, máx. +10) | hecho |
| Escalera de decisión por rango; TerrainSense desde Veterano | hecho |
| Alcance y precisión de armas por rango | hecho (alcance, dispersión de volea) |
| Tótems con retraso y kamikaze por revivir | hecho |
| Genética desde la **personalidad de MCA** | **no**: sale del UUID (sin leer MCA) |
| **Salto de IA en el rango máximo** (§2 del diseño: altura, línea de visión, reposicionar, presión con escudo, fuego y movimiento) | **no** |
| **Clases por ítem** (bombardero, ingeniero, explorador…) | **no** |
| **Muñeco de práctica** (hasta rango 1) | **no** (E2B) |
| **Maestría de arma** | **no** (A8 del prompt E3A) |
| Historias visibles (batallas, bajas notables) y su uso en el precio | **no** |
| Los **líderes suben primero** | **no**: no hay XP de mando |

## 3. Plan v2 (entra en E3A, bloque V)
Detalle en `docs/PROMPT_E3A.md`: modelo de XP por **amenaza y contribución** (no por daño bruto), XP de **mando**, efectos reales de los 7 rasgos, tabla "qué cambia en cada rango" visible, salto de IA del rango máximo, personalidad MCA, historias, anti-abuso y simulación de ritmo.

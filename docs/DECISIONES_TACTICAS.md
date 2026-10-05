# Rally of the Guard — Playbook de decisiones tácticas (v0, 2026-10-05)

**Estado:** borrador para que David lo corrija en el chat. Se entrega a Claude Code en E2 como **especificación**. Los números son **ilustrativos y configurables**. Nada aquí es un simulador militar: son reglas simples que se sienten inteligentes.

---

## 0. Principios

1. **La veteranía mejora la calidad de las decisiones, no el daño bruto.** Un Maestro no pega más fuerte: elige mejor dónde, cuándo y contra qué.
2. **Un líder decide una vez; los soldados resuelven lo local.** Líder → directiva de escuadra (push) → cada guardia aplica con lo que ve. Nunca una decisión pesada por guardia por tick.
3. **Señales baratas.** Solo vida, distancia, nº de aliados cerca, munición, tipo de enemigo, y la valoración de terreno (§3). Nada de escaneos por tick.
4. **El capitán se calcula.** No es una entidad: es el guardia con mayor `commandCapacity` de su unidad.
5. **Nadie es inmortal.** Ni un Maestro, ni un ejército visto desde el jugador: un jugador solo no debe poder contra un ejército grande.
6. **10 000 es arquitectura:** registrados 10 000; distribuidos cientos; IA intensiva solo para los relevantes; las escuadras lejanas usan estado agregado.

---

## 1. Rangos y capacidad de mando (ilustrativos)

| Rango | Nombre | Qué cambia en la decisión | `commandCapacity` |
|---|---|---|---|
| 0 | Recluta | Combate básico: ataca al objetivo, sigue la formación a medias | 1–4 |
| 1 | Entrenado | Usa mejor su arma (rango óptimo, recarga a tiempo) | ~5–10 |
| 2 | Veterano | **Mantiene la formación** bajo presión; usa cobertura simple | ~20–30 |
| 3 | Élite | Mejores decisiones individuales: prioridad de amenaza, terreno | ~35–50 |
| 4 | Maestro | **Coordina**: decide por la unidad (cerco, repliegue, fuego y movimiento) | 50–100 |
| 5 | Legendario | Igual que Maestro, más capacidad; sigue escalando con rendimientos decrecientes | 100+ |

- **Capacidad = cuántos guardias puede coordinar bien.** Si el grupo supera la capacidad, hay subunidades y el de mayor capacidad en cada una es su capitán.
- XP solo en combate real: daño hecho o recibido, peleas de más de N segundos, variedad de enemigos, rendimientos decrecientes por tipo repetido. El muñeco de práctica llega a rango 1 y nada más.
- **Daño:** el único escalado de daño permitido es la **lanza grande** (E3), con curva suave y tope.

---

## 2. Escalera de decisión (por guardia, en su turno de 20 ticks)

Se evalúa de arriba abajo; la primera que se cumple decide. Cada rango desbloquea peldaños.

| # | Pregunta | Si sí | Desde rango |
|---|---|---|---|
| 1 | ¿Hay directiva del líder vigente? | Obedecerla (salvo peligro inmediato de vida baja) | 0 |
| 2 | ¿Tengo objetivo? | Si no, buscar el más amenazante dentro de mi rango; si no hay, mantener formación | 0 |
| 3 | ¿Estoy en mi hueco de formación? | Si me salí más de X bloques sin motivo, volver | 2 |
| 4 | ¿Estoy en cobertura? | Si no y hay cobertura buena a ≤ 6 bloques, ir (solo si mi arma es a distancia o mi vida < 50 %) | 2 |
| 5 | ¿Estoy en mi rango óptimo con esta arma? | Reposicionar (acercar/alejar) antes de atacar | 1 |
| 6 | ¿Hay amenaza prioritaria? (volador, a distancia, explosivo, tipo “jefe”) | Cambiar de objetivo respetando el cupo de `Engagement` | 3 |
| 7 | Decisión final | **avanzar / disparar / cubrirse / retirarse** (vida < umbral → `RetreatPlanner`) | 0 |

`DecisionLadder.decide(situation, rank)` es una función pura (fácil de probar), alimentada con una `Situation` construida con señales baratas.

---

## 3. Valoración de terreno (`TerrainSense`)

Conceptos **simples**, calculados **solo para el líder**, una vez por segundo por escuadra, sobre **≤ 8 posiciones candidatas** alrededor y con ≤ 12 lecturas de bloque cada una. Resultado cacheado 2–4 s.

| Concepto | Cómo se detecta (barato) | Etiqueta | Lo busca |
|---|---|---|---|
| **Altura** | Diferencia de Y entre la candidata y la posición del enemigo conocido | `HEIGHT_GOOD` si ≥ +3; `HEIGHT_BAD` si ≤ −3 | tiradores desde rango 2 |
| **Cobertura** | De la candidata hacia el enemigo: ¿hay ≥ 1 bloque sólido a media altura a ≤ 3 bloques en esa línea? (3 muestras) | `COVER_GOOD` / `COVER_BAD` | rango 2 |
| **Agua** | Bloque/líquido bajo la candidata y alrededor | `NEAR_WATER` (obstáculo para cuerpo a cuerpo enemigo; mala para armas de fuego) | rango 3 |
| **Puertas / muros / edificios** | ¿Cielo cubierto + bloques sólidos en ≥ 3 de 4 lados? | `DEFENSIBLE` (interior), `OPEN` (resto) | rango 2 |
| **Bosque** | Densidad de troncos/hojas en radio 4 (muestreo disperso) | `IN_FOREST` (cobertura parcial; mala visibilidad) | rango 3 |
| **Estrechamientos y puentes** | Ancho de suelo transitable ≤ 3 en el eje del enemigo | `CHOKEPOINT` (bueno para defender, malo para atacar a través) | rango 3 |

Reglas de uso:
- Cada candidata recibe una **puntuación de −2 a +2** sumando etiquetas con pesos por doctrina (§4) y rango.
- No se calcula si no hay enemigo conocido; las escuadras sin líder ≥ Veterano no lo usan.
- **Nunca** se recorre el volumen completo: muestreo fijo y topes de config (`terrainSamplesPerCandidate`, `terrainCandidates`).

---

## 4. Doctrinas del Battle Planner (E3)

Cada doctrina es una **configuración de pesos** sobre la escalera y el terreno, no código nuevo.

| Doctrina | Qué prioriza | Retirada | Terreno | Distancia | Nota |
|---|---|---|---|---|---|
| **Hostigar** | Disparar y moverse; no engancharse | Pronto (vida < 70 %) | altura y cobertura | larga | “golpear y alejarse” |
| **Evitar bajas** | Preservar guardias; entrar solo con ventaja | Muy pronto (< 80 %) | cobertura y estrechamientos | media-larga | rechaza trabar combate en inferioridad |
| **Distraer** | Atraer al enemigo hacia un punto | A discreción; nunca “hasta el final” | campo abierto con retirada | media | otra unidad flanquea |
| **Ataque total** | Matar sin pensar en bajas | Solo < 15 % | ignora terreno | corta | consumo máximo de recursos |
| **Defender posición** | Mantener la zona | Solo si cae el líder y no hay relevo | `DEFENSIBLE`, `CHOKEPOINT`, altura | media | usa HQ de la escuadra |
| **Emboscar** *(si cabe)* | Esperar escondido hasta la condición | Tras el primer intercambio | `IN_FOREST`, cobertura | corta | requiere la condición del plan |

---

## 5. Situaciones frecuentes: señal → decisión → quién

| Situación | Señal barata | Decisión | Desde |
|---|---|---|---|
| Zombi bebé / enemigo pequeño | Tamaño del objetivo y cupo de `Engagement` | Entran solo los con armadura; el resto hace pantalla | 1 |
| Enemigo volador o en altura | Tipo de enemigo / Δ Y | Tiradores disparan; cuerpo a cuerpo busca cobertura | 3 |
| Bajas leves, enemigo fuerte | Bajas < 20 %, vida enemiga alta | Todos a distancia (cambian a arma secundaria si existe) | 3 |
| Bajas graves | Vida total < umbral de la doctrina | Repliegue al punto temporal / cuartel | 2 |
| El enemigo huye / se aleja rápido | Distancia creciente 3 s seguidas | Reformar automático; no perseguir si la doctrina lo prohíbe | 2 |
| Líder muerto | Evento | Sucesión (máx. 3–4 relevos); luego desarticulación | 0 |
| Munición baja | Mochila < 25 % | Cuerpo a cuerpo, o retirarse a recargar al cuartel | 1 |
| Cueva o edificio cercano | `DEFENSIBLE` a ≤ 6 bloques y vida baja | Replegarse dentro y pelear desde ahí | 2 |
| Tótem usado varias veces | Contador | Posible modo kamikaze (respeto) | 3 |
| Jugador solo contra ejército | Relación de fuerzas | Sin trucos: simplemente ellos son muchos y cooperan | — |

---

## 6. Simulaciones reproducibles (E1 línea base, E2/E3 resultados)

| Esc. | Fuerza | Para qué sirve |
|---|---|---|
| A | 20 reclutas vs 20 zombis | línea base de la IA actual |
| B | 20 veteranos vs 20 zombis | efecto de la veteranía |
| C | 20 arqueros vs 20 zombis | efecto de la distancia |
| D | 20 arqueros + 1 capitán veterano vs 20 zombis | efecto del liderazgo |
| E (E3) | 20 guardias vs 20 zombis, una vez por doctrina | efecto de la doctrina |

Métricas: supervivientes, ticks de combate, bajas, % de formación mantenida, munición gastada, cambios de arma, distancia media, órdenes ejecutadas. Resultado esperado tras E2: **D > B > A en supervivientes; C ≥ A en distancia media y bajas.** Si no se cumple, se documenta; no se maquilla.

---

## 7. Preguntas abiertas para David

1. ¿Los umbrales de vida de cada doctrina (§4) te parecen bien o los quieres más agresivos?
2. ¿Los números de `commandCapacity` (§1) están donde los imaginabas?
3. ¿Quieres que “Distraer” pueda dejar a una unidad **sacrificable** (sin retirada)?
4. ¿Qué tipo de saqueo prefieres por defecto: todo, solo munición y comida, o nada?

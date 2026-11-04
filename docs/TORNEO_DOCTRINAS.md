# TORNEO_DOCTRINAS — protocolo para descubrir el oficio de los guardias (David + Cowork, 2026-11-04)

> Objetivo: que un grupo de veteranos **maneje objetivamente mejor cada tipo de amenaza**, no solo que cada uno sea
> más hábil. Los guardias no entienden; **nosotros** aprendemos con la simulación y escribimos ese saber en un
> **Manual de campo** (datos). La veteranía decide quién lo aplica y con qué calidad. Nada de esto da vida, daño ni
> velocidad: solo decisiones. Primero contra **mobs** (vanilla, luego las raids de Scorched Guns), después jugadores.

## 1. Los cuatro ejes de cada prueba
| Eje | Qué es | Valores iniciales |
|---|---|---|
| **Amenaza** | contra qué se pelea, por su naturaleza (no por su nombre) | ver §2 |
| **Composición** | qué tiene nuestro grupo: roles, armas, monturas, proporciones | ver §3 |
| **Objetivo** (la *doctrina*) | qué priorizamos | `MATAR_RAPIDO`, `SOBREVIVIR`, `MANTENER_POSICION`, `GANAR_TIEMPO` |
| **Respuesta** | qué combinación de comportamientos que **ya existen** usamos | ver §4 |

La pregunta del torneo siempre es: **para (amenaza, composición, objetivo), ¿qué respuesta rinde mejor?**
Una *doctrina* no es un comportamiento: es "jalar la mejor respuesta del manual según la amenaza y priorizando X".

## 2. Amenazas, clasificadas por su naturaleza
Cada amenaza se describe con rasgos que la IA del capitán sí puede observar (lo que ha visto: `SituationReport`,
`ThreatMemory`): **alcance** (mele / medio / largo), **movilidad** (lenta / normal / rápida / voladora),
**daño** (bajo / alto / explosivo / de área), **resistencia** (frágil / blindado / jefe), **número** (solo / grupo / horda),
**especial** (explota, invoca, cura, salta, se teletransporta, convierte a nuestros caídos).

Orden de trabajo:
1. **Vanilla, uno a uno** (ronda 1): zombi (horda mele lenta, convierte caídos), esqueleto (rango medio), creeper
   (explosivo, lento), araña (rápida, salta), bruja (pociones de área, cura), saqueador (ballesta), vindicador (mele
   alto daño), evocador (invoca vexes voladores), fantasma/blaze (voladores, el segundo a distancia).
2. **Mezclas vanilla** (zombis + esqueletos, la patrulla de saqueadores).
3. **Raids de Scorched Guns** (`data/scguns/raids/*.json`, 12 raids: antique, frontier, copper, iron, wrecker,
   ocean, sculk, piglin, diamond_steel, treated_brass, blood_moon…): zombis, esqueletos y husks **con armas de fuego**,
   armadura propia, multiplicadores de vida, efectos (velocidad) y un **jefe** de 65–250 de vida. Requisitos: Scorched
   Guns 1.5 pide NeoForge ≥ 21.1.228 (hoy compilamos con 21.1.215), `framework`, `curios`, `geckolib`.
4. **Jugador simulado** (bloque A de E3-COORD): volador con proyectiles, vehículo, asesino, mago de área.

**Mobs nativos:** en el torneo el rojo aparece **como en el juego** (rol `native`: su equipo, vida y daño reales). Las
pruebas antiguas igualaban al mob con un guardia (misma vida, arma y armadura) para medir decisiones; eso sigue
disponible, pero no sirve para aprender a pelear contra el mob real.

## 3. Composiciones (cómo se evalúa nuestro grupo)
Rol `mix:` en el escenario: `mix:shield=4,archer=4,melee@m=2` (`@m` = montado). Roles de hoy: `melee` (espada),
`shield` (espada + escudo + hierro completo), `axe`, `spear` (tridente, hasta tener la lanza propia), `archer`,
`crossbow`, `gunner` (arma de Scorched Guns), `fist`. Familias de composición:
- **Pura**: solo mele, solo escudos, solo arqueros, solo ballesteros, solo caballería, solo caballería de rango.
- **Línea + apoyo**: frente (escudo o mele) + tiradores detrás, en proporciones 2:1, 1:1, 1:2.
- **Con montura**: la misma composición con parte montada (velocidad para esquivar o golpear y salir).
- **Con armas de fuego**: cuando haya CI con Scorched Guns, cada arma con su valor (alcance, cadencia, daño, munición,
  dispersión) leído de `data/scguns/guns/*.json`; ver §8.

Cada composición recibe una **firma** (lo mismo que `GroupProfile`: arquetipos y proporciones). El manual se indexa
por firma, así una unidad real del jugador encuentra su fila más cercana.

## 4. Respuestas (comportamientos que ya existen; el torneo los combina, no inventa)
| Pieza | Valores |
|---|---|
| Postura / operación | mantener línea (HOLD_LINE), avanzar por turnos, flanquear, retroceder disparando, emboscada |
| Instrucción del intérprete | MANTENER, HOSTIGAR, FLANQUEAR (y las que se agreguen) |
| Formación | línea, columna, V, círculo; densidad |
| Modo de fuego | normal, quieto, avanzando, retrocediendo, volea |
| Prioridad de objetivos | el más peligroso, el más cercano, el más débil |
| Distancia | pegarse, distancia media, máxima distancia (kiting) |

Cuando una respuesta gana, se **aterriza a un nombre** legible ("Contra creeper: frente con escudos arriba
manteniendo la línea, arqueros quietos con fuego constante"; "a caballo: usar la diferencia de velocidad para no
dejarse golpear"). Si ninguna pieza existente sirve, eso es un hallazgo: falta un comportamiento (se propone, no se
inventa en silencio).

## 5. Cómo se mide cada objetivo
| Objetivo | Métrica principal | Secundarias |
|---|---|---|
| MATAR_RAPIDO | tiempo hasta matar al rojo (con victoria) | daño rojo recibido por segundo |
| SOBREVIVIR | supervivientes azules y vida restante | bajas evitables (fuera de su rol) |
| MANTENER_POSICION | % del tiempo con el puesto cubierto, sin ceder > N bloques | supervivientes |
| GANAR_TIEMPO | segundos hasta perder el puesto o caer | bajas por segundo ganado |
Siempre también: victoria, conversiones (caídos que se vuelven zombi), coste (ms/tick), ping-pong de órdenes.

## 6. Búsqueda y estadística (para no engañarnos)
1. **Calibrar**: por (amenaza, composición) se busca el número de enemigos con el que la respuesta *base* gana ≈ 50 %.
   Sin esto todo da 100 % o 0 % y nada se distingue (ya nos pasó).
2. **Ronda de descarte**: todas las respuestas, n = 6. Pasa el tercio superior.
3. **Ronda final**: las finalistas y la base, n ≥ 30. Una respuesta "gana" solo si supera a la base por más de 2
   errores estándar en su métrica.
4. **Confirmación**: la ganadora se repite en otro lote (otras semillas). Si se invierte, no se adopta (ya vimos
   inversiones con n = 10).
5. **Mismo campo limpio**: entre corridas se retira todo mob ajeno (el "tercer bando").

## 7. El Manual de campo y la veteranía
- Archivo `config/rallyguard-field-manual.json` (y su copia por defecto en el jar), **generado por el torneo**, una fila
  por (rasgos de amenaza, firma de composición, objetivo) → respuesta, con su evidencia (lote, n, ventaja).
- **Quién lo usa:** el capitán reconoce la amenaza (con lo visto) y busca su fila. Un recluta R0 pelea con la doctrina
  genérica. La veteranía cambia **qué tan bien y qué tan pronto** se aplica: un capitán que ya enfrentó ese tipo de
  enemigo (tipos vistos, memoria de amenazas) lo reconoce antes y ejecuta con su `Leadership.quality`; uno que no, tarda
  o cae a la genérica. Una unidad de veteranos aplica el manual como unidad; eso es lo que la hace mejor que la suma.
- **El Battle Planner** queda encima: el manual es el oficio (lo que saben); la contingencia es tu previsión (lo que tú
  sabes y ellos no). Las respuestas de las contingencias se eligen de entre las del manual.
- **El Ledger se depura con esto**: una orden que no gana en ninguna fila del manual es candidata a quitarse.

## 8. Protocolo para cada rol, arma o amenaza nueva (se repite siempre igual)
1. Ficha del rol/arma/amenaza: rasgos (§2) o valores (alcance, cadencia, daño, munición, dispersión, peso).
2. Escenarios generados con `tools/torneo.py` (amenaza × composiciones que la incluyen × respuestas).
3. Calibración → descarte → final → confirmación (§6).
4. Filas nuevas en el manual + nombre legible + informe en `RALLY_SESIONES.md`.
Pendientes de este protocolo: **lanza y escudo propios** (cuando existan los ítems), **asesino**, **mago de
proyectiles**, **mago de área**, **armas de Scorched Guns** (una por familia: pistola, rifle, escopeta, ametralladora,
lanzador), **raids de Scorched Guns**.

## 9. Moral visible (David, 2026-11-04)
Para distinguir "huye por miedo" de "el mod no funciona": encima del guardia aparece una señal según su moral
(partículas desde el servidor, sin código de cliente): **VACILANTE** = gotas de sudor; **ROTO / DESTROZADO** =
humo gris mientras huye; **reagrupado** = destello verde una vez. Interruptor `morale.showIcons` (activado).

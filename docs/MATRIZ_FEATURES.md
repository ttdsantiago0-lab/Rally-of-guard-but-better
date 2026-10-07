# Matriz de botones y capacidades (E2A, 2026-10-07)

Fuente: código de la rama `claude/dreamy-ride-iy0g3q` en `34b4c95`, textos de `en_us.json`, comandos, configuración y escenarios de simulación. **Compila (BUILD SUCCESSFUL); nada de esto está probado en juego salvo lo marcado "probado E1".**

Cómo responder: usa el ID (p. ej. `C-04 firme`, `H-02 cambio: …`). **Estado** = firme (no se reabre sin motivo) o borrador (puede cambiar). **Tipo** del punto pendiente = FALLO, IDEA o CAMBIO. **Medición** = cómo lo mediría la simulación (escenario o métrica) o "manual" si solo se prueba jugando.
Los estados y tipos de la tabla son **propuestos por mí**; corrígelos.

## A. Objetos y teclas
| ID | Qué es | Estado | Pendiente | Medición |
|---|---|---|---|---|
| A-01 | **Libro de Mando (Ledger)**, objeto; clic derecho abre el panel; agachado + clic derecho sobre algo = marcar objetivo o punto de formación (aire = borrar marcas) | firme | — | manual |
| A-02 | **G** (comando): órdenes rápidas al grupo seleccionado (posicionar/atacar/seguir/montar según apuntes) | firme (probado E1) | CAMBIO: tú dijiste que es "demasiado útil" y confuso; ¿qué hace exactamente cada apuntado? | manual |
| A-03 | **H**: soltar / mantener fuego | firme | — | manual |
| A-04 | **J**: abrir el Ledger | firme | — | manual |
| A-05 | **Pergamino de Reunión (Scroll of Rallying)**, efecto "Comandante" | borrador | IDEA: ¿sigue siendo necesario con el Ledger? | manual |

## B. Panel (Ledger): pestañas y selección
| ID | Qué es | Estado | Pendiente | Medición |
|---|---|---|---|---|
| B-01 | Pestañas **Guardias · Escuadrones · HQs** | firme | — | manual |
| B-02 | Selección: Todos / Ninguno / por escuadra / por unidad; paginación por lotes (`ledgerPageSize`) | firme | — | escala: tiempo de abrir el panel con 200 |
| B-03 | Guardia en escuadrón no aparece en Guardias (arreglo E1) | firme | — | manual |
| B-04 | Renombrar, olvidar (dos clics), ver guardia (distancia, armadura, escuadrón, montura, rango, ★capitán, mochila) | firme | — | manual |
| B-05 | Barras agrupadas: **Posición · Formación · Ataque · Estilo** (estilo de fuego solo si hay armas a distancia) | borrador | CAMBIO: tú pediste "más limpio"; ¿esto basta o prefieres menú radial/teclas? | manual |

## C. Órdenes de posición
| ID | Qué es | Estado | Pendiente | Medición |
|---|---|---|---|---|
| C-01 | **Recall (A mí)**: teletransporte en formación delante de ti y esperar | borrador | CAMBIO: ¿teletransporte siempre o solo si están lejos? | manual |
| C-02 | **Mantener terreno (Hold)** | firme | — | `hold` + enemigo: bajas y distancia |
| C-03 | **Reformar** (volver a la última formación) | firme | — | formación% tras combate |
| C-04 | **Romper filas (Break ranks)** | firme | — | manual |
| C-05 | **Seguir (Follow)** | firme (probado E1) | — | tiempo a posición con 50/100/200 |
| C-06 | **Avanzar (Advance)**: toda la formación avanza 16 bloques o al punto marcado | borrador | FALLO/CAMBIO: tú dijiste que no quedó claro (G en lugar, luego G a enemigo = no atacan) | `scale-*`, tiempo a 90 % en hueco |
| C-07 | **Retirada**: al punto marcado, si no al HQ del escuadrón, si no te siguen | firme | — | retiradas ejecutadas; bajas durante retirada |
| C-08 | Puesto (Post here / Release): guarda un punto y vuelve tras cada pelea | firme | FALLO ya corregido (ahogamiento bajo el agua) | manual: guardias junto a lago |

## D. Formación
| ID | Qué es | Estado | Pendiente | Medición |
|---|---|---|---|---|
| D-01 | Formas: **Línea, Columna, V, Círculo** | firme | — | `formation%`, tiempo hasta 90 % en hueco |
| D-02 | Subunidades: grupo mayor que la capacidad se divide, cada una con su capitán y formación (`minSubunitSize`, `maxSubunitSize`) | borrador | CAMBIO: ¿tamaños de subunidad por defecto? | `scale-200` V |
| D-03 | Reparto de huecos en el tiempo (`slotsPerTick` = 12) | firme | — | ms/tick durante V de 200 |

## E. Ataque y objetivos
| ID | Qué es | Estado | Pendiente | Medición |
|---|---|---|---|---|
| E-01 | **Atacar**: designar objetivo; en "Hold fire" lo marcan y no disparan hasta soltar | firme | — | manual |
| E-02 | Objetivo: más cercano / contraataque / marcado / jugador / grupo enemigo | borrador | IDEA: ¿"jugador" debe existir? (PvP) | manual |
| E-03 | **Atacar a un grupo o escuadrón enemigo** (reparte los guardias) | borrador | — | `rank5v3`, mirror 20 vs 20 |
| E-04 | **⚔ por escuadrón**: ataca con todo el escuadrón al objetivo elegido | borrador | — | manual |
| E-05 | **Kamikaze** (dinamita en la mano izquierda; nunca con amigos a <6 bloques) | borrador | IDEA: ¿mantener o pasar al backlog? | manual |

## F. Estilo de fuego (solo armas a distancia)
| ID | Qué es | Estado | Pendiente | Medición |
|---|---|---|---|---|
| F-01 | **Normal / Quieto / Avanzar / Retroceder / Volea** | firme (probado E1) | — | `still-vs-normal`, `volley-vs-normal` |
| F-02 | Volea (precisión sube con veteranía) | borrador | CAMBIO: tú dijiste "no es muy preciso" | tiros/aciertos |
| F-03 | Rango de arco, ballesta y arma de fuego según arma y rango (`bowMaxRange`, `gunMaxRange`, `rangePerRank`…) | borrador | CAMBIO: francotirador veterano debe intentar blancos muy lejanos | `dist40`, `dist80` |
| F-04 | Flechas con las propiedades reales del arma y munición (Potencia, Fuego, crítico) | firme | FALLO ya corregido en E2A: arco de dragón | manual: daño vs el del jugador |

## G. Posturas y doctrinas
| ID | Qué es | Estado | Pendiente | Medición |
|---|---|---|---|---|
| G-01 | Posturas: **Pasiva, Defensiva (por defecto), Agresiva, Implacable** | firme | — | manual |
| G-02 | Doctrinas: **Estándar, Hostigar, Evitar bajas, Distraer, Ataque total, Mantener posición, Emboscada** (pesos de la escalera) | borrador | CAMBIO: solo hay pesos; **no se guardan al reiniciar**; no hay Battle Planner aún | mirror por doctrina (E3-D) |
| G-03 | Los veteranos con libertad eligen estilo/doctrina solos; con orden del jugador, solo esa | borrador | IDEA/CAMBIO: tú lo pediste; no medido | `rank3v0` con/sin orden |

## H. Mando y veteranía
| ID | Qué es | Estado | Pendiente | Medición |
|---|---|---|---|---|
| H-01 | **Capacidad de mando** por rango y **capitán calculado** (★) | firme | CAMBIO: números por rango son ilustrativos | `leader-melee-20`, `leader-archer-20` |
| H-02 | **Sucesión**: máx. 3 relevos (4 en ejércitos grandes), luego "rout" (huyen una fracción) | borrador | — | muertes de líder, bajas |
| H-03 | **Rangos 0–5** (Recluta … Leyenda) por **XP de combate real** con rendimientos decrecientes | firme | — | `rank3v0`, `rank5v0`, `rank5v3` |
| H-04 | **Rasgos** (Tanque, Tirador, Duelista, Jinete, Firme, Asalto, Explorador) con genética/afinidad | borrador | CAMBIO: afinidad sale del UUID (no se puede leer la personalidad de MCA) | manual |
| H-05 | **Tótems con retraso**; revivir varias veces → temerario / kamikaze con explosivo | borrador | — | manual |
| H-06 | Maestría por tipo de arma (el veterano pasa a ser una amenaza real con su arma) | **no implementado** | IDEA firme de David (BACKLOG §10) | por arquetipo, vs jugador |

## I. Terreno y escalera de decisión
| ID | Qué es | Estado | Pendiente | Medición |
|---|---|---|---|---|
| I-01 | **TerrainSense** (cobertura, altura, agua, bosque, estrechamiento) solo para capitanes ≥ Veterano; ≤8 candidatas, 1 vez/s | firme | — | `cover-archer`, `height-archer`, `wall-melee`, `forest-mixed` |
| I-02 | **DecisionLadder** (función pura) | firme | — | peldaños usados, "Hallazgos" |
| I-03 | **Escape de atrapados v1** (saltar, nadar, picar si `mobGriefing`) | borrador | — | tiempo atascado en la simulación |
| I-04 | **Cráteres como cobertura**: una explosión a <24 bloques invalida la lectura de terreno del escuadrón (`TerrainSense`) | borrador | — (confirmado en el código) | `cover-archer` con explosión |

## J. HQ (cuarteles)
| ID | Qué es | Estado | Pendiente | Medición |
|---|---|---|---|---|
| J-01 | Crear / asignar / renombrar / borrar HQ; estandarte vinculado (agachado + comando) | firme (probado E1) | — | manual |
| J-02 | Entrar por una puerta, aparcar montura, volver a montar al salir; guardias repartidos dentro | borrador | FALLO/CAMBIO por confirmar tras tu prueba | manual |
| J-03 | Logística de HQ (reabastecer de cofres, mochilas) | **no implementado** (E2b) | — | — |

## K. Equipo, mochila y munición
| ID | Qué es | Estado | Pendiente | Medición |
|---|---|---|---|---|
| K-01 | **Mochila de guerra**: hoy **6 ranuras** (munición, comida, pociones, dinamita, segunda arma) | **inconsistente con tu decisión** | CAMBIO: tú dijiste 1 mochila (Sophisticated Backpacks) + 5 ranuras (E2b E2-0) | `ammoSpent`, `weaponSwaps` |
| K-02 | Sin munición: cambia de arma; si solo tiene arma a distancia vacía, va al HQ o te sigue | firme | FALLO corregido E2A | `ammo` en sim |
| K-03 | 1 ítem de munición por 4 disparos | firme | — | `ammoSpent` |

## L. Contratación
| ID | Qué es | Estado | Pendiente | Medición |
|---|---|---|---|---|
| L-01 | Contratar un guardia (pantalla "Hire Guard") por **3 esmeraldas** | **inconsistente**: tú dijiste 4 | CAMBIO: ¿3 o 4? ¿precio dinámico (backlog §9) | manual |
| L-02 | Contratación de un guardia ajeno: cancelada; dueño propio abre el menú de MCA | borrador | CAMBIO: `guardMcaInteraction` en `true` | manual |

## M. Montura
| ID | Qué es | Estado | Pendiente | Medición |
|---|---|---|---|---|
| M-01 | Montar/desmontar (agachado+G), `mountDriver`, solo monturas con tag (`mountsFromTagOnly`) | borrador | — | `mirror-mounted-5`, `mounted-vs-foot-5` |
| M-02 | Sin fuego amigo a montura (4 capas) | firme (probado E1) | — | log `mount hit` |
| M-03 | Vínculo guardia–montura (backlog §9) | **no implementado** | IDEA | — |

## N. Compatibilidad MCA
| ID | Qué es | Estado | Pendiente | Medición |
|---|---|---|---|---|
| N-01 | Diálogo de crimen de MCA Crime: un mixin opcional lo cancela con guardias que tienen dueño | borrador | FALLO por verificar jugando; tu captura mostraba "retener a alguien ilegalmente" (cautivo) | manual con cautivo |
| N-02 | Menú de MCA sobre guardia propio | borrador | — | manual |

## O. Simulación y depuración
| ID | Qué es | Estado | Pendiente | Medición |
|---|---|---|---|---|
| O-01 | `/rally debug spawn`, `perf`, `vet`, `rank` | firme | — | — |
| O-02 | `/rally sim batch <preset>` y `stop`; 41 escenarios; salidas `runs.jsonl`, `summary.json`, `summary.md` con Hallazgos | firme | — | es la medición misma |
| O-03 | Arena en tierra con terreno por escenario (llano, colina, cobertura, muro, bosque) | borrador | — | — |
| O-04 | Bandos espejo y mobs (zombie, husk, skeleton, pillager, vindicator; jefes golem y ravager) | borrador | los mobs no montan | `mirror-*` ≈ 50 % |

## P. Rendimiento
| ID | Qué es | Estado | Pendiente | Medición |
|---|---|---|---|---|
| P-01 | Picos de `markers`, `mounts`, `combat_gate` (E2A) | borrador | CAMBIO: verificar con `/rally debug perf` | `Perf` por sistema |
| P-02 | Escala a cientos de guardias (E2S) | **no implementado** | — | `scale-50…400` |

## Contradicciones y vacíos encontrados al leer el código
1. **Mochila**: el código tiene 6 ranuras; tu decisión es 1 mochila + 5 (K-01).
2. **Precio**: 3 esmeraldas en pantalla; tú dijiste 4 (L-01).
3. **Doctrinas y estilo de fuego de los veteranos no se guardan** al reiniciar (G-02).
4. **Pergamino de Reunión**: sin definición clara de para qué sirve ahora (A-05).

# GUERRILLAS_Y_DIPLOMACIA — investigación y diseño estacionado (2026-10-11)

**Estado:** IDEA ESTACIONADA con investigación. No entra en E3B/E3C/E3D. David la pidió para **no perder de vista** hacia dónde va la arquitectura (alianzas entre jugadores, traición, guerrillas contratables, ciclo de vida de una guerrilla). Se construye después de E3D, como addon sobre las primitivas del núcleo (saqueo, HQ, depósito, moral, estilos, resoluciones).
**Nota de honestidad:** la investigación se hizo con búsquedas web; de varias fuentes solo pude leer resúmenes parciales. Lo marcado **[pendiente leer]** debe revisarse más a fondo antes de diseñar en detalle.

## 1. Qué dice la investigación (resumen)
**Origen y organización.** Una guerrilla surge cuando un bando débil se enfrenta a uno fuerte (un ocupante, un Estado): "guerrilla" viene de "pequeña guerra" (partisanos españoles contra Napoleón). Los partisanos soviéticos de 1941 nacieron de soldados aislados tras las líneas más civiles locales. Las unidades son **pequeñas, móviles y ligeras**; su fuerza real es el **apoyo de la población** (Lawrence: bastan pocos combatientes si la mayoría simpatiza). Ese apoyo hace difícil al enemigo separar amigo de enemigo, y por eso castiga a comunidades enteras. [Encyclopedia.com](https://www.encyclopedia.com/social-sciences-and-law/political-science-and-government/military-affairs-nonnaval/guerrilla-warfare)

**Cómo operan.** Emboscadas, incursiones, sabotaje y ataques a líneas de comunicación y a **puestos débiles**; golpean y se retiran al campo. El objetivo es **desgastar la voluntad y la capacidad de mantener territorio**, no ganar batallas decisivas; **sobrevivir importa tanto como dañar**. Evitan la batalla decisiva (Tito evitó el choque con los alemanes). **Logística:** refugios/bases seguras (incluso al otro lado de una frontera), suministro de la población local y ayuda exterior.

**Cómo evolucionan.** El modelo de guerra popular prolongada tiene tres fases: (1) defensiva larga, con **bases** en terreno remoto y evitando batallas que no puedan ganar; (2) empate, con más **áreas base** y una **administración propia** en las zonas que controlan; (3) ofensiva con unidades casi regulares. **Pasar demasiado pronto a combate convencional suele acelerar la derrota** (IMRO, ELAS). [Encyclopedia.com](https://www.encyclopedia.com/social-sciences-and-law/political-science-and-government/military-affairs-nonnaval/guerrilla-warfare), [People's war (Wikipedia)](https://en.wikipedia.org/wiki/People%27s_war)

**Gobernar cuando se asientan.** La investigación sobre "gobernanza rebelde" señala que los grupos armados que controlan territorio asumen funciones de gobierno: **hacer reglas, hacerlas cumplir y proveer bienes y servicios**; el control territorial y la legitimidad determinan cómo se comportan con los civiles y qué efectos dejan. [New Directions in Rebel Governance Research](https://politicalsciencenow.com/new-directions-in-rebel-governance-research) **[pendiente leer el artículo completo]** (cuándo y por qué empiezan a gobernar, impuestos, tribunales).

**Contratación y lealtad (condotieri).** Un mercenario trabajaba por un contrato (*condotta*) con **duración fija**, seguida de una **fase de espera** hasta renovar; tras terminar de forma definitiva, **no podía atacar a su antiguo empleador durante dos años**. Su **reputación** era su bien más valioso. Evitaban batallas arriesgadas y la campaña de invierno porque perder hombres entrenados bajaba su valor y poder de negociación. Al darse cuenta de su monopolio de fuerza, **dictaban condiciones**; hubo cambios de bando y traiciones, y algunos llegaron a gobernar. [Condottiero (Wikipedia)](https://en.wikipedia.org/wiki/Condottiero)

**Bandolerismo [pendiente leer]:** hay literatura sobre bandolerismo en los Balcanes medievales (800–1500) y sobre las incursiones de los pindaris; valen para el patrón "incursión → botín → refugio". No pude leer el contenido.

## 2. La intuición de David y lo que implica (guerrillas no construyen su pueblo)
Una guerrilla es **parasitaria o simbiótica**: vive de, dentro de y contra comunidades que no son suyas. Por eso:
- **No tiene "pueblo propio" al nacer.** Tiene un **campamento oculto** (HQ con depósito, de E3C) y vive de **saqueo, extorsión/"protección", reclutamiento y apoyo local**.
- **Cuando se asienta** (fase 3 del modelo) ocupa estructuras existentes (puestos, ruinas, una aldea tomada) y entonces **deja de ser guerrilla y se vuelve una facción**: necesita territorio, sustento, reglas y servicios. Esa transición es una decisión de juego importante (ver §3).
- **El apoyo local es un recurso**, no un adorno: sin simpatizantes, la guerrilla vive en riesgo.

## 3. Ciclo de vida propuesto (BORRADOR)
| Fase | Qué es | Qué hace | Cómo se conecta con lo ya diseñado |
|---|---|---|---|
| **Origen** | Banda pequeña nacida de: desertores (moral rota), restos de un ejército derrotado, saqueadores reorganizados, o supervivientes de un pueblo arrasado | Se esconde, busca un lugar | Moral (M), resoluciones (D9) |
| **Banda** | Unidad ligera (`movilidad: ligera`) | Reconoce, elige un objetivo débil, golpea de noche y huye | Explorador `RECON`, sightings, estilos de mando |
| **Campamento** | Base oculta con depósito (HQ temporal/oculto) | Incursión → **saqueo** → vuelve → **vacía** en el cofre → reparte entre todos | Saqueo y depósito comunitario de E3C |
| **Red** | Varias bases + hostigamiento sostenido; "calor" (cuánta atención atrae) | Más golpes → más recompensa por ellos → patrullas de respuesta | Planner, `BattleOutlook` |
| **Asentamiento** | Toma una estructura, gobierna (tributo, reglas) | Pasa a `movilidad: asentada`; ya no huye (rendición, muerte, tierra quemada) | D9, Facciones |
| **Fin** | Disolución, rendición negociada, o absorción por una facción | El jugador puede contratar o traspasar a sus miembros | Contratación, `GuardOwnership` |
**Patrón de incursión sobre un pueblo:** reconocimiento → elegir el objetivo más débil/lejano de refuerzos → golpe rápido → saqueo (con cuota comunitaria) → retirada por rutas distintas → esconder el botín. El **estilo del jefe** decide el objetivo, el riesgo y cuándo huir con el botín.

## 4. Guerrillas contratables y alianzas (IDEAS)
### 4.1 Guerrillas dispuestas a cooperar o ser contratadas
- **Contrato (a lo condotta):** objetivo, duración, pago (objetos del depósito/esmeraldas), cláusulas (botín compartido o no) y **reputación**. Fase de espera tras el término; **regla de los 2 años** (no atacar a su antiguo contratante).
- **Golpes específicos:** contrato "de una sola operación" (p. ej. sabotear un puesto enemigo).
- **Lealtad:** depende de honor, reputación, pagos al día y de una oferta rival; el **estilo** (eje *honor*) decide la probabilidad de **romper el contrato**.
### 4.2 Alianzas entre jugadores contra un tercero
- **Qué exige:** relaciones entre dueños (hostil / neutral / tregua / aliado), fuego amigo aliado en `FriendIndex`, **imagen compartida de la batalla** (sightings entre aliados), y una regla de **mando combinado** (quién manda a la fuerza conjunta: el anfitrión, o el general de mayor puntaje).
- **Qué ya existe:** HQ compartible, `Factions`, propiedad por UUID, pantallas de contratación.
- **Viabilidad:** media. Se hace primero entre **facciones NPC y el jugador** (barato de probar) y después entre **dos jugadores** (requiere permisos y sincronización de servidor).
### 4.3 Traición
Tipos, de menor a mayor: **deserción individual** (moral), **defección de una unidad** (capitán pérfido), **rendición fingida** (ya diseñada), **ruptura de contrato**, **traición de un aliado** (rompe la alianza con efecto en reputación). Todo gobernado por el eje *honor* y la ambición del líder; con avisos al jugador cuando sus capitanes sean perceptivos.

## 5. Dependencias (por eso va después)
Moral y cohesión (M, E3B) → saqueo, depósito y HQ temporal (E3C) → estilos, resoluciones, `BattleOutlook` (E3D) → **guerrillas + diplomacia** (addon) → alianzas entre jugadores (último, por complejidad de servidor).

## 6. Siguiente paso (cuando toque)
Leer en detalle la gobernanza rebelde y el bandolerismo; luego escribir `GUERRILLAS_DISENO.md` con estados, relaciones, contratos y el catálogo de estilos de guerrilla (ya esbozado en `ESTILOS_DE_MANDO.md` §8).

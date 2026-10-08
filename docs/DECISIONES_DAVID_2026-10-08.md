# Decisiones de David tras revisar la matriz (2026-10-08)

Responde a `docs/MATRIZ_FEATURES.md`. Estados: **FIRME** (no se reabre sin motivo), **BORRADOR**. Tipos: FALLO, IDEA, CAMBIO.

## Principios (FIRMES)
1. **Tres niveles de mando:** *Los guardias individuales reciben intenciones. Los escuadrones ejecutan maniobras coordinadas. Las fuerzas ejecutan operaciones de armas combinadas.* Una **fuerza** = escuadrones con subgrupos creados.
2. **Quién coordina qué:** el **capitán** coordina cómo dispara y se organiza su subgrupo (ej. arqueros: volea + implacable). El **general** coordina cómo se coordinan los subgrupos bajo una doctrina (ej. objetivo "mantener terreno": arqueros de apoyo en quietos detrás de los subgrupos tanque). El **superintendente** toma decisiones macro con información de varios grupos (cómo se verá, por definir). El **Battle Planner** coordina varios grupos; solo lo dirigen el jugador, el superintendente, o ambos (a aclarar con el tiempo). Con el planner y sus teclas (p. ej. `G`+1) se da una orden a un grupo grande: retirada, retroceder mientras atacan, etc. **Aquí se controla verdaderamente un ejército.**
3. **Estilos de fuego y doctrinas son para grupos.** En lo individual no valen tantos comandos: el estilo de un guardia individual lo determina su **personalidad/arma** (codificado). Un veterano individual **sí** cambia de estilo según la situación, pero **dentro de un grupo se acopla a la doctrina del grupo.**
4. **Reglas de jerarquía deben ser extremadamente claras y robustas** (Puesto, Pasiva, Implacable, Romper filas, formaciones…).
5. **Veteranía:** el rango 5 **no es el tope**: se sigue acumulando capacidad de mando. **Maestría de arma (H-06) es central** (compatibilidad con otros mods).
6. **Controlar miles de guardias sin romper la jugabilidad** es el objetivo de largo plazo.
7. **Las correcciones deben ser el 10–20 % de cada entrega; el resto, avance.** No hay límite de tokens por ahora.

## Respuestas por ID
| ID | Decisión |
|---|---|
| A-01 | FIRME. Quiere la **tabla de teclas** → `docs/CONTROLES.md` |
| A-02 | Solo la tabla y la **jerarquía** G vs Libro; no cambiar nada ahora → `docs/CONTROLES.md` §2–3 |
| A-03 | IDEA: ¿aplica también a cuerpo a cuerpo? (el texto del juego dice que sí; confirmar jugando) |
| A-05 | **Pergamino de Reunión**: sin definición. Posible ítem del **Battle Planner** (BORRADOR) |
| B-01…B-04 | FIRMES. B-02: preguntó cómo escala con más guardias → sin medir; entra en E2S |
| B-04 | CAMBIO: **botón para montar/desmontar** en la ficha del guardia (refuerza que una montura es única de un guardia) |
| B-05 | CAMBIO: la barra de acciones es **esencial** pero debe ser **de grupo**, no individual; estilo individual = personalidad/arma |
| C-01 Recall | BORRADOR. Parecido a seguir en formación; poco usado |
| C-02 Hold | BORRADOR. Quizá pase a ser doctrina |
| C-03 Reformar | FIRME, útil |
| C-04 Romper filas | CAMBIO FIRME: dejar de ser un botón; **al llegar al HQ**, romper filas, **aparcar monturas** y quedar **"Relajados"** (definitivo) |
| C-05 Follow | CAMBIO FIRME: **seguir con formación**; con cientos no se puede caminar porque el jugador queda en el centro: opciones **detrás / delante / escolta / columna** |
| C-06 Advance | BORRADOR: pasa al Battle Planner y desaparece del panel; idea de "señalar una dirección y reconocer" se hará con guardias **recon** |
| C-07 Retirada | FIRME, excelente |
| C-08 Puesto | FIRME e importante; **jerarquía clarísima** (qué rol queda, Implacable, etc.) → `CONTROLES.md` |
| D-01 | FIRME; deben ser jerarquías más potentes: seguirme en formación, defender en formación |
| D-02 | Quitar el pendiente; solo IDEA de una interfaz más cómoda |
| D-03 | FIRME |
| E-01…E-04 | **Atacar/defender son las órdenes más importantes y menos desarrolladas**: cómo se gestionan ataques entre grupos, prioridades de objetivo, si alguien más ataca a mi objetivo… Va con veteranía y decisión. **Por ahora se quedan como están**; E-04 se actualizará en todos los módulos con el producto final |
| E-05 Kamikaze | FIRME por ahora. Futuro: que un guardia o su capitán, desesperado, decida el kamikaze. Preguntas: ¿lo ordena un general? ¿veteranía en ese rol? ¿tótem? |
| F | FIRME; el estilo de fuego pertenece a **grupos**; añadir estilos para tiradores y cuerpo a cuerpo y relacionarlos con el mando |
| G-01 | FIRME; **Pasiva debe intentar defenderse** (CAMBIO) |
| G-02 | FIRME: doctrinas esenciales, para **grupos grandes**, con ejecución según veteranía. Probar la última versión |
| G-03 | CAMBIO: individual cambia de estilo; en grupo se acopla a la doctrina |
| H-01…H-04 | FIRMES. H-03: implicaciones claras al subir de nivel |
| H-05 | EN PAUSA |
| H-06 | FIRME, central |
| I-01, I-02 | FIRMES (jerarquía en `DECISIONES`, principio 2) |
| I-03 | Futuro |
| I-04 | Es lo mismo que I-01 (TerrainSense); no es un ítem aparte |
| J-01 | FIRME |
| J-02 | FIRME: pueden haber **varias puertas**; el HQ puede ser **un campamento en el bosque o un castillo** |
| J-03 | Importantísimo y complejo; ver cómo sale |
| K-01 | FIRME: **6 espacios**; máximo **1 mochila de Sophisticated Backpacks** por guardia (no abusar del espacio) |
| K-02 | La reacción sin munición **debe ajustarse a la jerarquía del grupo**: un arquero de retaguardia puede ir a reabastecerse; uno de primera línea cambia a cuerpo a cuerpo. Falta definir |
| K-03 | FIRME |
| L-01 | **3 esmeraldas** se queda; luego precio calculado por características |
| L-02 | FIRME; futuro: abrir el menú de MCA de un guardia propio |
| M-01, M-02 | FIRMES |
| M-03 | FIRME, excelente |
| N-01 | FIRME: los guardias contratados **no deben mostrar ese diálogo** |
| N-02 | FIRME |
| O | FIRME; deben ser lo más robusto. Pide: (a) **acciones fuera de lo establecido** ("improvisación"): si funciona y no tiene nombre, **se registra y se establece**; (b) la arena debe tener **terreno variado** (montaña, llanura, río, lava…) |
| P | FIRME |
| Otros pedidos nuevos | **Paracaídas** para guardias (lanzarse de aviones, o si matan a su hipogrifo); **cruzar el mar en una campaña**; revisar formaciones con muchos guardias |

## Hallazgos de la simulación v2 (primera tanda quick, 2026-10-07)
Ver `docs/sim-results/20261007-quick/`.
- **Los guardias de bandos distintos NO se atacan**: 6 de 10 corridas "atascadas" (todas guardia contra guardia, daño 0, 48 s). Causa localizada: Guard Villagers trae `"Mob Blacklist" = [..., "guardvillagers:guard", ...]`, y `Guard.canAttack`/`Guard.setTarget` rechazan cualquier objetivo de esa lista; el código de Rally (`CommandTargeting.wouldAccept`) también la respeta. Mitigación ya aplicada **solo en `veamos-test`** (quitado `guardvillagers:guard`; copia `guardvillagers-common.toml.bak-antes-guard-blacklist`). Arreglo real: que Rally decida la hostilidad entre guardias de facciones distintas sin depender de la config.
- Contra mobs funciona: A, C y el esqueleto ganan los guardias; **5 guardias murieron contra 5 zombis** con el mismo equipo (`zombie-5`): por revisar si es equilibrio o fallo.
- "Esperado C ≤ A en bajas": se incumplió (12 vs 7) con 1 repetición; sin conclusión.

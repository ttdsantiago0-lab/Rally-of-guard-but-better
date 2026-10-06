# Rally of the Guard — Backlog de ideas y asesoría (vivo, desde 2026-09-30)

Este documento guarda **todas** las ideas del usuario (revisión del 2026-09-30 y
anteriores) para que ninguna se pierda. Junto a cada una va una recomendación de diseño.
Nada de aquí está aprobado para implementarse salvo lo marcado **Hecho (3H)**. Cambiar la
filosofía o la esencia del juego requiere el permiso del usuario.

**Triage:**

| Etiqueta | Significado |
|---|---|
| **Ahora** | Hecho en 3H o es el siguiente paso natural |
| **Siguiente** | Próximas 1–2 fases; el diseño ya encaja con lo que hay |
| **Después** | Buena idea; necesita antes otra pieza |
| **Addon** | Mejor como mod/módulo aparte que usa la API de Rally |
| **Investigar** | Primero hay que leer el código de otro mod |

**Brújula (la filosofía, para decidir rápido):**

- Pocas primitivas bien hechas: guardia, grupo, formación, postura, orden, objetivo,
  momento y condición.
- **Un rol = equipo + orden, nunca un botón ni una entidad nueva.**
- Escala igual de 5 a 1000 guardias.
- Robustez: si otro mod falla, Rally no se rompe.
- "No vacío, que forme historias": los números (veteranía, moral) deben producir
  anécdotas, no hojas de cálculo.

---

## 1. Control en batalla y la interfaz (lo pedido en la revisión)

| Idea | Estado | Nota |
|---|---|---|
| No poder herir/matar a tus propios guardias | **Hecho (3H)** | Siempre, no solo cuando te siguen |
| El Kamikaze no ataca de camino | **Hecho (3H)** | Corre sin arma; la puerta de combate le niega objetivos |
| Elegir un guardia concreto estando frente a él | **Hecho (3H)** | Tecla **G** mirándolo lo selecciona solo a él; el siguiente G le da la orden |
| Renombrar guardias fácilmente | **Hecho (3H)** | Botón ✎ con un guardia seleccionado: toma el texto del campo |
| Más información por guardia | **Hecho (3H)** | Pasar el ratón por su fila del Libro muestra la ficha completa |
| Ver quién muere y cuántos quedan | **Hecho (3H)** | Una línea gris tras el mensaje de muerte de Guard Villagers: "↳ Escuadra › Unidad: quedan N · ejército: M" |
| Quitar el "cementerio de nombres" | **Hecho (3H)** | Se limpia solo cuando un guardia se convierte en zombi o se elimina del mundo, y hay un botón **Olvidar** para filas ausentes |
| Subgrupos dentro de escuadras | **Hecho (3H)** | Escribe `Pueblo1/Arqueros`. La pestaña Escuadras los muestra con sangría; **Ver ›** abre la lista filtrada |
| Círculos pequeños que se recolocan | **Hecho (3H)** | El radio mínimo ahora respeta el espaciado |
| Avanzar 8 bloques es raro | **Hecho (3H)** | Ahora avanza 16, o hasta el punto marcado mirando hacia él |
| Elegir objetivo a mitad de batalla sin el ítem | **Hecho (3H)** | **G** sobre un enemigo lo ataca |
| Un objetivo que DEFENDER | **Hecho (3H)** | **G** sobre un aldeano, un amigo o un caballo: lo escoltan y pelean con quien lo amenace |
| Órdenes esenciales sin el ítem en la mano | **Hecho (3H)** | Teclas **G / H / J** (ver la matriz en ARQUITECTURA) |
| No listar por orden de creación | **Hecho (3H)** | Por escuadra y, dentro de cada una, el más cercano primero |
| Marca visual sobre el seleccionado ("triangulito") | **Hecho (3H)** | Chispas verdes sobre cada seleccionado, visibles solo para su comandante |
| Los nuevos miembros no heredan la formación | **Hecho (3H.1)** | Decisión del usuario: entran a la formación de su grupo al asignarlos (ver §6) |
| "Llamar" debería significar ir a un lugar concreto | **Hecho (3H.1)** | Cuarteles por escuadra (estandarte) + punto temporal + "te siguen" (ver §6) |

**¿Hay que montar a los guardias antes de usar el pergamino?** No. El pergamino y las
órdenes respetan la montura que el guardia ya tiene (lo teletransportan con ella), pero
no hacen que monte. Para montar: agachado + **G** apuntando a un caballo domado tuyo, y
el guardia al mando sin montura más cercano (hasta 24 bloques) camina y lo monta.
Agachado + **G** sobre un caballo con uno de tus guardias encima lo desmonta.

## 2. Guardias "conscientes del arma" y roles

Principio: **lo que lleva en la mano es su rol.** Nada de botones por rol.

| Rol / idea | Triage | Asesoría |
|---|---|---|
| Arco / ballesta | Ya funciona | Son los objetivos propios de Guard Villagers |
| **Pistolero (Scorched Guns)** | **Hecho (3H)** | Se añade la misma IA que usan sus mobs (`GunAttackGoal`, dificultad 4 = buena puntería), por reflexión y solo si el mod está instalado. Desde 3H.1 la munición es **finita**: va en la mano secundaria, cuesta 1 ítem cada 4 balas recargadas, y se reabastecen de cofres o barriles cerca cuando no pelean. Los jinetes con arma se detienen a distancia como los arqueros |
| **Bombardero** (dinamita de **Lucky TNT**, decisión del usuario; stacks de ~5) | Después de la veteranía | Arrojable en la mano secundaria + orden "Lanzar" (o que lo haga solo en postura Agresiva). Reutiliza la regla del Kamikaze: nunca con amigos en el radio. Hay que investigar la entidad de dinamita de cada mod (ya sabemos cómo hacerlo genérico con `TntBlock`) |
| **Artillero** (Create/CBC) | En pausa (decisión del usuario) | Guardia apostado junto a un `CannonMount`: `setYaw/setPitch/tryFiringShot` son públicos. Espera a que los caballos se prueben en el juego |
| **Mago** (Ars Nouveau) | Investigar | Un libro de hechizos en la mano → lanzar el hechizo grabado. Ars 'n Spells (Otectus) hace justo este puente; revisar cómo hacen que un mob lance. Efectos de área = cuidado con aliados (misma regla que el Kamikaze) |
| Soporte (curación, pociones) | Después | Pociones arrojables en la mano secundaria + postura Pasiva: lanzarlas a aliados heridos |
| Asalto a estructuras | Después | Combina con Kamikaze/bombardero y con una orden "Tomar posición" |
| Guardias voladores / dragones | Addon/Investigar | El hipogrifo está en pausa. Dragones: solo muy raramente, probablemente en el addon de guerrillas |
| Nigromancia (bastón de Ice and Fire) | Investigar | Que lo use el propio guardia como "arma"; convierte a los muertos en aliados temporales. Encaja con "rol = equipo" |
| Compat con Apotheosis / Cataclysm | Investigar | Casi siempre funciona sola si el arma es un `SwordItem`/`ProjectileWeaponItem`; solo requieren código las armas con IA propia |

## 3. Inventario, botín, veteranía y moral

| Idea | Triage | Asesoría |
|---|---|---|
| Inventario del guardia: armadura extra, manzanas doradas, dinamita, flechas, segunda arma | Siguiente | Guard Villagers ya tiene inventario. Rally puede leerlo para decidir: cambiar a la segunda arma si el enemigo está lejos o cerca, comer la manzana con poca vida, recargar flechas |
| Saquear las bajas | Después | Recoger ítems cerca tras un combate si tiene hueco. Riesgo de lag: hacerlo solo en postura Libre y con límite por segundo |
| **Veteranía** (sube precio y estadísticas; máximos fuertes pero variados) | **Siguiente fase (decisión del usuario: antes que las clases)** | Diseño completo en `docs/VETERANIA.md`: genética (MCA) + historia → rasgos; el salto de IA solo en el rango máximo; liderazgo y sucesión de mando; anti-abuso |
| **Moral** / romper la formación (vida × experiencia, probabilística) | Después | Una sola cifra oculta por guardia: moral = f(vida, rango, aliados cerca, comandante cerca). Si baja, hay una probabilidad por segundo de "huir" (el modo Huir del que habla ARQUITECTURA). Solo en combate, así no molesta |
| El último superviviente de una escuadra fuerte deserta | Después | Una consecuencia directa de la moral. Da historias |
| Poder de escuadra automático 1–5 | Siguiente | Código puro: media de rangos × armadura × número. Se mostraría en la pestaña Escuadras |
| Capitán | Después | El hueco 0 de la formación ya es "el líder". Un capitán vivo sube la moral; si muere, la baja |
| Récords de batalla / nombrar batallas | Después | Registrar bajas propias y enemigas por escuadra; se podría nombrar la batalla desde el chat de muertes |

## 4. Guerrillas (propuesta: **addon aparte**)

**Recomendación:** hacerlo como un **módulo o mod separado que usa la API de Rally**
(órdenes, formaciones, posturas y escuadras) en vez de meterlo dentro de Rally.

- Rally sigue siendo "tu ejército".
- El addon es "el mundo tiene otros ejércitos".
- Se puede activar o desactivar por modpack sin tocar tu partida.

Todas las ideas, tal cual:

1. Saquean y se hacen más fuertes. Aparecen con niveles de grupo 1–10.
2. Campamentos, incluso ocupando estructuras de otros mods, con cofres de botín útil.
3. Asaltan aldeas periódicamente, reclutan aldeanos, construyen campamentos más grandes y
   también en el Nether.
4. Raros pero posibles. Grupos errantes en estructuras de When Dungeons Arise o en barcos.
   Monturas hasta hipogrifos.
5. Se pueden comprar como mercenarios con un ítem difícil de fabricar.
6. ¿Addon? Sí (ver arriba).

**Orden sugerido:**

1. Un grupo errante (nivel 1–10) con formación y posturas de Rally.
2. Campamento en una estructura existente.
3. Asaltos periódicos.
4. Crecimiento y reclutamiento.
5. Mercenarios (ítem difícil).
6. Nether, barcos e hipogrifos.

## 5. Ideas "locas" (numeración propia de este documento, no la de tu lista)

| # | Idea | Triage | Asesoría |
|---|---|---|---|
| 1 | Compat con MCA Reputation / Conversations / Crime (moral oculta, frases de miedo ocasionales) | Investigar | Otectus hace MCA: Crime (cárceles) y quests de MCA. Una frase de miedo cuando la moral baja da vida sin UI |
| 2 | Vender guardias a otros jugadores | Después | Transferir el dueño (`GuardOwnership.setOwner`) con un ítem "contrato" que el comprador firma |
| 3 | Desertar a otros ejércitos por mala reputación | Después | Depende de la moral y la reputación |
| 4 | Explorador que alerta a una aldea | Siguiente | Rol = catalejo en la mano. Si ve hostiles a X bloques de su puesto, avisa al comandante (y en el futuro, a la aldea) |
| 5 | Bandera blanca que dispara la retirada | Siguiente | Un ítem que funciona como "control remoto" de Retirada (ARQUITECTURA §4.3). Muy barato |
| 6 | El estandarte marca una base (uno por área) | Siguiente | Encaja con el "punto de reunión por escuadra" de §1 |
| 7 | Señales solo en el futuro Battle Planner | Después | De acuerdo: nada de señales sueltas hasta que exista el Planner |
| 8 | Espía | Después | Invisibilidad o disfraz; marca objetivos para el comandante |
| 9 | Prisioneros (cárceles de MCA Crime) | Investigar | Otectus. Rendición = moral 0 + orden Capturar |
| 10 | Mensajero con skin reconocible | Después | Lleva órdenes a escuadras descargadas lejanas (hoy solo llegan por nombre en el servidor). Una forma diegética de "ordenar a una aldea lejana" |
| 11 | Uniformes con el diseño del estandarte del jugador (solo estética; soldado/comandante) | Después | Capa o tabardo teñido, solo visual |
| 12 | Estética "pensada con cariño" | Siempre | Sonidos, partículas y textos cortos y con carácter |
| 13 | Mirar otros mods de Otectus | Hecho (investigado) | Runic Skills (veteranía), Ars 'n Spells (mago), MCA: Crime (prisioneros), quests de MCA, Iron's Botany. Casi todos son Forge 1.20.1, así que sirven como inspiración y no como dependencia directa |
| — | El resto de tu lista | — | Está en las secciones de arriba: roles (artillero, mago, bombardero, soporte, asalto, voladores, nigromancia) y compat de armas en §2; capitán, poder de escuadra, desertor y récords en §3; dragones raros e hipogrifos en §2 y §4 |

## 6. Decisiones del usuario (2026-10-04)

1. **Los nuevos miembros entran a la formación**, "como en la vida real". **Hecho
   (3H.1)**: los que se asignan a una escuadra o unidad formada ocupan su lugar (hasta
   128 bloques; si están más lejos, entran con Reformar).
2. **Cuarteles generales.** **Hecho (3H.1)**: agachado + Ordenar sobre un estandarte lo
   vuelve el cuartel de las escuadras al mando. Retirada va, en este orden:
   1. al punto temporal marcado;
   2. al cuartel de su escuadra;
   3. si no hay ninguno, te siguen.

   Si se destruye el estandarte, se pierde el cuartel.
3. **Guerrillas:** addon aparte "si te conviene más" → sí, recomendado (§4).
4. **Veteranía antes que clases.** El ítem define la clase, sin botones nuevos.
5. **Ejército si muere el comandante.** Propuesta implementada (3H.1): los que te seguían
   se plantan donde caíste (cuidan tus cosas) y van contra quien te mató si está a
   menos de 48 bloques. Los apostados siguen igual. A futuro: sucesión de mando
   (`docs/VETERANIA.md` §3).

## 7. Ideas de la revisión del 2026-10-04 (numeradas como las escribió el usuario)

| # | Idea | Triage | Asesoría |
|---|---|---|---|
| 5, 13 | Árbol visual de jerarquías y capas de integración | **Hecho (artefacto)** | Página "Rally Map" (https://claude.ai/artifact/3q797tHUtwsw1MBjmBZafP, fuente `docs/rally-map.html`): árbol de sistemas y capas. Se actualiza en cada fase |
| 7, 12, 27 | Veteranía con IA avanzada (dragones de Ice and Fire, Dread Knight/Thrall, Scorched Guns); salto de IA solo en el rango máximo, balanceado, sin dependencias; guardias formidables que sigan escalando | Siguiente | `docs/VETERANIA.md` §1–2 |
| 8 | ¿Escala a 1000 o 10 000 guardias? | **Hecho (3H.1)** | Ver la respuesta en PORTING_PLAN §39. El bucle se reparte en 20 ticks, el Libro lista como máximo 2000 filas, el índice de escuadras es O(1) y el tope de combate se calcula en un solo pase |
| 9 | Munición finita, logística al volver a la base, cajas de munición (también como botín de guerrillas), gasto menor que un jugador, cambio a cuerpo a cuerpo sin munición, burros con cajas, pilas mayores de 64 | **Parcial (3H.1)** | Hecho: mano secundaria, 1 ítem cada 4 balas, reabastecimiento desde contenedores cercanos; sin munición pelean cuerpo a cuerpo con lo que tienen. Pendiente: la mochila (VETERANIA §5) para el arma secundaria y las pilas grandes; el ítem caja de munición; los burros |
| 10 | Mago de soporte (curación en grupo) | Después | Rol = libro de hechizos (VETERANIA §6) |
| 11 | Iconos de formación de Villager Recruits | **Hecho (3H.1), a nuestra manera** | Recruits es "All Rights Reserved", así que no se copian sus texturas. Los botones dibujan la forma desde la geometría real de Rally |
| 14, 16 | Guardias organizados: no amontonarse, información compartida, líderes veteranos que deciden mejor (ej.: zombi bebé contra 3 veteranos, que muera como mucho 1) | **Primer paso hecho (3H.1)** + Siguiente | El tope de combate cuerpo a cuerpo por objetivo ya está. Lo demás en VETERANIA §3 |
| 15 | El líder decide: cueva con poca vida, cubrirse de voladores, todos a distancia, reorganizarse si el enemigo se aleja | Siguiente (con líderes) | VETERANIA §3.3 |
| 15.1 | Ingeniero: trincheras, sacos de arena; el veterano construye con planos | Después (capa exterior) | Se puede: estructuras pequeñas como plantillas NBT que el mod trae, colocadas bloque a bloque gastando materiales de un cofre. El mod de planos que recuerdas es probablemente el **Schematicannon de Create** (esquema + cofre de materiales); un guardia podría leer el mismo formato `.nbt` |
| 15.2 / 28 | Ingeniero en paz | **Descartado por el usuario** | Nunca casas ni murallas: solo preparar el terreno militarmente. El más avanzado monta un radar o un escudo de otro mod y da la alarma si un jugador entra en rango |
| 17 | Bombardero con dinamita de Lucky TNT | Después de la veteranía | Ya conocemos su API (`LTNTBlock extends TntBlock`) |
| 18 | Emboscada: guardias bajo tierra que salen al pasar un objetivo | Después | Una orden nueva con condición ("momento + condición" de la brújula). Encaja con el futuro Battle Planner |
| 19 | Radares / trompeta de guerra | Después | La trompeta como "control remoto": alarma = todas las escuadras del área en Defensivo, apostadas |
| 20 | Nigromante que revive guardias muertos (skin y stats propios; mirar la mecánica de revivir de MCA Reborn) | Investigar | Rol = bastón; guardar "cadáveres" por UUID al morir |
| 21–23 | Guerrillas: cuevas y bosques, golpear y esconderse, campamentos muy ocultos, saltar de dimensión, huidas con ender pearls; islas flotantes de TNT; sacos de arena; trompeta; con más de 40–50 miembros, comportamientos variados según rasgos de MCA; rendirse / pelear hasta el final / volarlo todo; pasivas o propensas a atentados; aldeanos que se unen; jefe estratégico; minas antipersonales; paracaídas; barcos de When Dungeons Arise; muñeco de práctica y primer nivel de veteranía en el cuartel | **Addon** (salvo el muñeco y los sacos de arena) | Todo al addon de guerrillas, construido sobre las primitivas de Rally. El muñeco de práctica va con la veteranía (VETERANIA §4). Los sacos de arena son útiles para tu ejército también (ingeniero) |
| 22 | Ítem saco de arena: detiene balas pero se puede disparar a través; aguanta unos segundos contra aliento de dragón o Godzilla | Después | Un bloque con hitbox que bloquea proyectiles enemigos y deja pasar los propios. Requiere investigar cómo resuelven las colisiones los proyectiles de Scorched Guns |
| 24 | Grupos con teclas 1–9 / 1–15, unidos al Battle Planner | Siguiente (diseño) | Opinión: sí, pero los números 1–9 son la barra de objetos de Minecraft. Propuesta: **mantener G + número** selecciona el grupo N del plan activo (G suelto sigue siendo Ordenar), y Ctrl+G+N guarda la selección en N. Con el Battle Planner, los grupos del plan ocupan esos números |
| 25 | Sucesión de mando, máximo 3–4 relevos | Siguiente (con líderes) | VETERANIA §3.4 |
| 26 | Desertores en todas direcciones; nombres para líderes de guerrilla (adjetivos, colores, animales) | Addon + Después | La lista de nombres es trivial; la deserción depende de la moral |
| 28 | Tótems no instantáneos; quien "revive" seguido puede volverse kamikaze; respeto a la facción | Siguiente (con la veteranía) | VETERANIA §4 |
| 28 | Dimensión de hielo de Ice and Fire infestada de guerrillas que conviven o cooperan con sus mobs; oleadas si dejas una guerrilla crecer cerca de tu casa | Addon | Encaja con el addon de guerrillas |
| 28 | Sin importar lo poderoso que sea un jugador, que no pueda contra un ejército grande; veteranía difícil y no repetitiva | Principio | Va a la brújula (VETERANIA §0 y §4) |

## 8. Preguntas abiertas para el usuario

1. Prioridad entre la **mochila** (inventario extra) y los **líderes**, dentro de la fase
   de veteranía.
2. ¿Te sirve "mantener G + número" para los grupos (idea 24)?

- **Mago de apoyo v1** (sanador con foco en la mano; libro de hechizos de Ars Nouveau o vara de blaze): sacado de E2 el 2026-10-05; pendiente. Diseño en la historia de `docs/PLAN_ENTREGAS.md` (commit 0a065eb).

## 9. Ideas del 2026-10-06 (profundidades sobre lo existente; nada se descarta)

| Idea | Triage | Nota |
|---|---|---|
| **Cadena de información:** Scout → General → Captain → Soldier; Assassin, Engineer, Logistics, Guerrillas como roles | Siguiente (marco) | Un bus de **informes** (push, una vez por cambio): el scout publica un `SituationReport`, el general lo interpreta, el capitán lo traduce en directiva, el soldado ejecuta. Reutiliza E2a (capitán calculado, directivas) |
| **Superintendente** (rol único, ≤ 1, sin grupo; coordina grupos; opcional) | Después | Se obtiene por **vínculo**: horas junto al jugador, bajas, etc. No es veteranía |
| **Vínculo (`Bond`)** genérico: guardia↔montura, guardia↔jugador (superintendente) | Siguiente | Misma pieza para los dos usos. Montura vinculada: solo ese guardia, stats extra moderados |
| **Valoración / precio** (`Valuation`): stats + equipo + veteranía + historia; el veterano pone su precio (esmeraldas → bloques → ítem raro) | Siguiente | v1: precio en esmeraldas con fórmula única y visible en la contratación; precios en ítems raros después |
| **Bono por arma dentro de un presupuesto** (Scorched Guns): el veterano gana un % del DPS del arma repartido según su arquetipo (pesada → cadencia/recarga; ligera → daño); nunca +40 % bruto | Siguiente (con veteranía) | Tabla por arquetipo, no por arma; presupuesto en config |
| **Terreno vivo:** cráteres de explosión como cobertura; cuevas | Siguiente | `TerrainSense` ya lee bloques reales; añadir invalidación de caché por evento de explosión |
| **Atrapado / salir** (stuck): si no progresa y está encerrado → picar hacia aire libre (límite de bloques, respeta `mobGriefing`); luego apilar bloques o escalera; agua: descartado v1 | Siguiente (barato) | También corrige guardias atrapados hoy |
| **Scout (modo explorar):** calcula el tiempo de viaje, estudia **un** chunk (mobs, estructuras, evaluación simple), vuelve y reporta; en modo escolta rodea al ejército (adelante, atrás, flancos sin solaparse); bengalas opcionales | Addon / Después | No cargar chunks de más: solo lo ya cargado o 1 chunk con ticket corto; nunca generar chunks nuevos |
| **Asesino:** misión sobre **un** activo en **un** chunk (secuestro, robo, asesinato), con información del scout; interacción con MCA Crime/Reputation y testigos; soborno de guardias de otros ejércitos según tamaño y precio | Addon (después de guerrillas) | Contra jugadores: **desactivado por defecto** y solo con consentimiento (config); evita griefing en servidores |
| **Coliseo / duelos:** peleas de guardias, prisioneros, aldeanos, criaturas capturadas; espectadores; entrada de pago; **apuestas en esmeraldas**; eventos de aldea con criminales | Addon (v1 = duelo entre dos guardias en una zona marcada con estandartes) | Apuestas con **depósito en custodia en el servidor** (anti-duplicación); estructura de When Dungeons Arise solo como inspiración |
| **Aldea mercado:** publicar guardias en venta, eventos públicos | Addon / Después | Misma pieza de valoración y contrato |

## 10. Ideas del 2026-10-06 (segunda tanda)

| Idea | Triage | Nota |
|---|---|---|
| **Mercado con ofertas** (cada jugador publica lo que ofrece; el comprador acepta; también ofertas de compra; integra el mod de blueprints; mercados **indestructibles**) | Addon, después de `Valuation` | Modelo único `Listing{vendedor, objeto|guardia, precio, ámbito}`. Alcance local (por aldea) vs general: decisión de David pendiente; el modelo de datos es el mismo, solo cambia el filtro. Custodia en servidor (anti-duplicación); un guardia en venta cambia de dueño solo si el contrato se completa |
| **Botón de auxilio → señal de redstone** (el comandante lo pulsa cuando atacan y David no está; solo emite redstone; David construye lo que quiera: tren de refuerzos, compuertas, alarmas) | Siguiente (barato y de alto valor) | Bloque/acción en el HQ con niveles de alerta (fuerza de señal 1-15 = gravedad). Líder interino = el líder de grupo con más `commandCapacity` cerca de la aldea (ya calculado en E2a). **Restricción a verificar:** si el chunk no está cargado no hay combate ni guardias activos; hay que decidir cómo se carga la aldea atacada (ticket temporal al activar la alarma) y comprobar con pruebas que el tren de refuerzos llega con chunks cargados |
| Otras piezas de defensa (ideas mías, sin decidir) | Después | Aviso al jugador ausente (mensaje con coordenadas y estimación del enemigo); auxilio automático entre HQs vecinos (porcentaje configurable de su guarnición); reserva en cuartel que despliega con la señal; salida de redstone adicional para cerrar puertas/compuertas; doctrina de repliegue a la fortaleza interior; entrada de redstone que avisa "refuerzos llegados" |
| **Maestría de arma:** un guardia veterano con un arma en su nivel máximo es una amenaza siempre; los buffs de armas con efectos especiales superan lo que podría hacer un jugador (porque el jugador no tiene veteranía) | Siguiente (con `Valuation`) | Se obtiene por **tipo de arma** (maestría), no solo por rango global. Palancas preferidas: cadencia, recarga, precisión, duración/alcance del efecto especial; daño bruto solo hasta un tope de config. Tabla por arquetipo + sobrescrituras por arma en config. Costo del equilibrio: sube el precio, y perderlo duele |

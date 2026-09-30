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
| Los nuevos miembros no heredan la formación | **Hecho (3H), decisión abierta** | Nada entra en formación solo (así los francotiradores plantados siguen plantados). **Reformar** mete a los recién llegados. Si prefieres herencia automática, se puede añadir como opción por escuadra |
| "Llamar" debería significar ir a un lugar concreto | **Siguiente** | Hoy: marca un punto (agachado + G sobre un bloque) y usa **Retirada** (agachado + H), que se forma allí. Propuesta: un **punto de reunión por escuadra** guardado, para que "Retirada" de Pueblo1 vaya siempre a Pueblo1 |

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
| **Pistolero (Scorched Guns)** | **Hecho (3H)** | Se añade la misma IA que usan sus mobs (`GunAttackGoal`, dificultad 4 = buena puntería), por reflexión y solo si el mod está instalado. Munición infinita, igual que sus mobs. Los jinetes con arma se detienen a distancia como los arqueros |
| **Bombardero** (dinamita, stacks de ~5) | Siguiente | Arrojable en la mano secundaria + orden "Lanzar" (o que lo haga solo en postura Agresiva). Reutiliza la regla del Kamikaze: nunca con amigos en el radio. Hay que investigar la entidad de dinamita de cada mod (ya sabemos cómo hacerlo genérico con `TntBlock`) |
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
| **Veteranía** (sube precio y estadísticas; máximos fuertes pero variados) | Siguiente (diseño) | Recomendación: XP por baja → rangos 1–5. Cada rango da **un rasgo aleatorio** de una lista corta (Tirador, Robusto, Veloz, Sereno, Salvaje), así dos veteranos no son iguales ("FUERTES PERO VARIADAS"). El precio de contratación y de venta sale del rango. Estilo Runic Skills (Otectus), pero en pequeño |
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

## 6. Decisiones pendientes del usuario

1. ¿Herencia automática de formación para los nuevos miembros, como opción por escuadra?
   Hoy solo con **Reformar**.
2. ¿Punto de reunión fijo por escuadra (Llamar/Retirada van allí)?
3. Guerrillas: ¿como addon aparte (recomendado)?
4. Prioridad entre la veteranía (rasgos variados), el bombardero y el explorador para la
   próxima fase.

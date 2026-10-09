# Decisiones de David tras la visión global (2026-10-09)

Responde a las seis preguntas abiertas de `VISION_ARQUITECTURA.md` §14. Estado: **FIRME** salvo donde diga BORRADOR. Si algo contradice a `ARQUITECTURA_VETERANIA.md`, manda este documento (y por encima de ambos, `DECISIONES_DAVID_2026-10-08.md` solo en lo que no se toque aquí).

## D1 · Alcance y visión dependen del papel, no solo del rango (FIRME)
- El rango da **un poco** de visión y puntería a todos (se mantiene el +15 % por rango como tope general, ahora **escalado por papel**, ver `PERFILES_Y_FIRMAS.md` §1).
- **Cuerpo a cuerpo:** casi no gana alcance (no tiene) y poca visión extra.
- **Tirador especializado (sniper):** alcance **exagerado** a propósito. Es un papel, no solo un arma: lo define el arma + la maestría + el rasgo Tirador (o un ítem marcado en JSON).
- **Explorador:** **mucha visión, pésimo alcance**; ve lejos y señala, no pelea.
- Visión y alcance son **dos números distintos** (ver §1 del documento de perfiles). Visión = a qué distancia detecta/informa. Alcance = a qué distancia puede impactar.
- La alarma anti-inflado **no se desactiva**: se mide por papel (un sniper que gana a distancia a un melee es lo esperado; un melee R5 que gana 1v1 a un melee R0 en >85 % sigue siendo alarma).

## D2 · Maestría de arma: acumula desde el principio, las FIRMAS exigen rango mínimo (FIRME)
- La maestría **acumula desde el rango 0** (por usar el arma en combate real, como ahora) y da los efectos pequeños de E3A.
- **Las firmas de arma** (movimientos únicos, ver `PERFILES_Y_FIRMAS.md` §2) exigen **rango mínimo 2** y un nivel de maestría: son el "salto" de veteranía. Deben ser **significativas y vistosas**: no importa que estén "un poco rotas". Ejemplo del autor: un guardia veterano experto en el **Arco de Hueso de Dragón de Ice and Fire** dispara 2 o 3 flechas a la vez, más rápidas.
- Se diseñan **arma por arma** para que cada una se sienta única (tabla en JSON, sin recompilar; Claude Code solo programa los *tipos* de firma).
- **Los capitanes reconocen amenazas que solo una firma resuelve** (ejemplo del autor: un jugador volando lejos con hechizos de Ars Nouveau → el francotirador experto es la respuesta). Ver `PERFILES_Y_FIRMAS.md` §3.

## D3 · Radio de presencia del mando escala con el tamaño del ejército (FIRME el principio, BORRADOR los números)
- Un ejército grande ocupa mucho espacio: el capitán **debe estar dentro de su unidad** y mandar a toda ella; un **general** (que ordena a muchas unidades) necesita un radio mayor.
- Regla: el radio de presencia **no es fijo** sino `max(base, huella de la formación + margen)`, con tope. Fuera del radio las órdenes **llegan tarde y con menos calidad**, nunca se ignoran. Detalle y números en `PERFILES_Y_FIRMAS.md` §4.
- `morale.strength` por defecto = **0,4** (más suave en las primeras pruebas; se sube a 0,6 cuando se haya visto jugar).

## D4 · Estilos de mando: FIRMES en principios (actualizado 2026-10-09 tarde)
- Los estilos **se derivan del líder** (sin botón), se ven en la tarjeta del planner y se aprovechan eligiendo al líder. **Cuantos más estilos, mejor**: se modelan como combinación de ejes, no como tres etiquetas (ver `ESTILOS_DE_MANDO.md`). Las **guerrillas** tendrán estilos propios con el mismo sistema.
- **Nada de números fijos**: un estilo no es "se retira al X %". Es un **sesgo en cómo el líder juzga la batalla, coordina a sus subordinados y toma decisiones**, siempre en contexto (si ya van ganando, aunque lleven 50 % de bajas, continúan), en sintonía con la **moral** y con las **instrucciones superiores** (plan, doctrina, intención de la fase).
- Los roles de mando son lo que más aporta al mod: se implementan en **E3D** justo después de E3C.

## D5 · La moral aplica a TODOS los guardias, también facciones enemigas (FIRME)
Los mobs vanilla no tienen moral. Pruebas simétricas: el simulador usa la misma moral en ambos bandos.

## D6 · Orden: LOGÍSTICA primero (E3C), clases después (E4) (FIRME)
Diseño en `LOGISTICA_Y_EQUIPO.md`. Idea de David, que se vuelve el eje de E3C:
1. Al llegar al HQ, los guardias se relajan (ya existe) **y además dejan en los cofres el botín que no les sirve y cogen de los cofres lo que quieran**, que otros hayan dejado.
2. **De esa forma los guardias "se interesan" por ciertas armas y empiezan a masterizarlas** por sí mismos (equipo → práctica → maestría → firma).
3. El **HQ del Battle Planner es temporal** (campamento de batalla); el HQ original sigue siendo su hogar. En el HQ de batalla **se reabastecen de cofres llevados en burros y elefantes** (animales de carga).

## D7 · Saquear es una orden de fin de batalla con ejecución individual (FIRME, 2026-10-09)
- Para evitar lag con cientos de guardias, el saqueo **no es un comportamiento permanente**: es una **orden que se da solo cuando termina una batalla**, una vez por unidad, con ventana de tiempo y un único barrido de objetos.
- A nivel **individual**, cada guardia identifica su inventario de guerra, saquea; si todo está lleno (mochila incluida) **elige** qué quedarse; al llegar al HQ **vacía** lo que no quiere.
- Así, sin que el jugador lo note, un guardia puede **especializarse en otra arma** por afinidad; el jugador puede **moverlo de unidad** (el sistema avisa, no lo mueve solo).
- Esta mecánica es la **base de las guerrillas** (incursión → saquear → volver → vaciar); se diseña con una fuente de objetos intercambiable.

## D9 · Resoluciones de batalla y rendiciones (FIRME en el principio, BORRADOR en los detalles)
Un líder tiene más desenlaces que seguir o huir: continuar, romper contacto, pelear hasta morir, rendirse de verdad, **fingir rendirse y detonar**, o quemar lo que no puede llevarse. Depende de su estilo, honor, movilidad (una guerrilla asentada no puede huir) y moral. Rendición real: el jugador puede **contratar** a los rendidos o **traspasarlos** a su bando. Daño a bloques de la tierra quemada **apagado por defecto**. El jugador ve la **lectura de la batalla** (`outlook`) del capitán en la tarjeta del planner. Detalle en `ESTILOS_DE_MANDO.md` §10.

## D8 · Reparto comunitario del equipo (FIRME)
Armas, armaduras, tótems y todo lo demás se reparten como **bien común** del HQ (cuota justa, necesidad antes que codicia). Lo único "egoísta" permitido: armas con sinergia con su personalidad o rasgos. El jugador no manda dejar objetos; los quita a mano del inventario del guardia. Contenedor excluido = renombrado "privado".

## Qué NO cambia
Sin botones nuevos fuera del planner. Rango ≠ maestría. Nada da vida/daño/velocidad bruta por rango. Todo por eventos y con presupuesto de rendimiento.

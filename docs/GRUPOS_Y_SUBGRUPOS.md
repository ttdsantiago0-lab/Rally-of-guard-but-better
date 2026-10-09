# GRUPOS_Y_SUBGRUPOS — diseño (David, 2026-10-09; firme salvo lo marcado "borrador")

## 1. Qué puede hacer cada nivel (firme)
- **Guardia individual:** pocas acciones simples: **Atacar, Defender, Moverse, Quedarse quieto, Kamikaze, Montura (montar/desmontar)**. Nada más en su panel. El estilo individual de pelea lo decide su personalidad, su arma y su rango (escalera de decisión), no un botón.
- **Grupo (escuadrón o unidad):** postura **Pasivo / Defensivo / Agresivo / Implacable**, **formaciones**, **instrucciones generales** (Seguir, Reunir, Retirarse, Reformar, Hostigar, Mantener la línea, Avanzar…), y decisiones de **talante**: más o menos arriesgado, subir o bajar el ritmo, ahorrar fuerzas. Y, nuevo, la **geometría de la formación** (§2).
- **Grupo con subgrupos** (p. ej. Grupo 1 = caballería + arqueros + infantería): las instrucciones del grupo se **adaptan a la especialidad de cada subgrupo** (§4). El grupo recibe instrucciones generales en cierta formación; el subgrupo tiene una **especialidad que cambia lo que significa** esa instrucción.

## 2. Geometría de formación (nuevo, pensado para que el planner y los capitanes la moldeen)
Hoy `FormationShape` genera posiciones por forma (LINE, COLUMN, V, CIRCLE) y espaciado. Añadir un `FormationLayout` (parámetros, no formas nuevas):
- **Densidad:** `CERRADA` (espaciado ×0,6, escudos juntos), `NORMAL` (×1), `ABIERTA` (×1,6, para dispersar fuego y evitar explosiones). Se puede pasar de una a otra en ruta (transición gradual, ≤ 10 s, escalonada con `SlotQueue`).
- **Sobrecarga (reparto de peso):** `EQUILIBRADA`, `CENTRO`, `ALA_IZQUIERDA`, `ALA_DERECHA`, `AMBAS_ALAS`. Es un vector `(izquierda, centro, derecha)` normalizado; el generador de posiciones da más fondo (más filas) donde hay más peso y menos donde hay menos. Quién ocupa cada zona lo decide el rol: los de mayor aguante/armadura y escudo a la zona cargada, los tiradores detrás.
- **Huecos (carriles):** `FormationLayout.gaps`: lista de carriles (posición lateral, ancho 2–6 bloques) por los que los guardias no se colocan. Sirve para que una unidad "filtre" a otra (§ de `BATTLE_PLANNER_V2.md`). Cerrar el carril = quitar el hueco y re-asignar huecos.
- Se aplica a todas las formas y escala a 200 sin cambios en la arquitectura (`SlotQueue`). Acceso desde la tarjeta de grupo y el planner; **sin botones nuevos en el panel individual**.

## 3. Perfil de grupo y puntuación de consistencia (nuevo)
`GroupProfile` calcula, solo por eventos (cambia la composición, equipo o montura de un miembro) y en caché, estos datos de un grupo o subgrupo:
- **Composición:** conteo por **arquetipo de rol**: `INFANTERIA_LINEA` (espada/hacha, escudo opcional), `INFANTERIA_PESADA` (escudo + armadura alta), `LANCEROS`, `ARQUEROS`, `BALLESTEROS`, `TIRADORES` (armas de fuego, p. ej. Scorched Guns), `CABALLERIA_LIGERA` (montado, cuerpo a cuerpo, armadura baja), `CABALLERIA_PESADA` (montado, cuerpo a cuerpo, armadura alta), `CABALLERIA_DE_RANGO` (montado + arco/ballesta/arma de fuego), `OTROS`. Derivado del equipo y de la montura reales (propiedades genéricas, sin listas de otros mods).
- **Atributos agregados:** rango medio y dispersión, armadura media, alcance medio (por arma), velocidad esperada (a pie o montura), tipo de montura dominante, vida y munición medias, moral y cohesión (de `MORAL_Y_COHESION.md`).
- **Puntuación de consistencia (0–100):** 100 − penalización por entropía de las "firmas" (arquetipo + montura). Un grupo donde todos van montados en el mismo tipo de montura y con arco da ~95: **Caballería de rango**. Mezclar espadas, arcos, un caballo y un golem da <40: **Heterogéneo**. Fórmula en config (pesos), no escondida.
- **Identidad:** etiqueta dominante si la consistencia ≥ `groupProfile.minIdentityScore` (65): p. ej. "Arqueros", "Infantería pesada", "Caballería de rango"; si no, "Mixto (consistencia 38)". Se muestra también el 2º arquetipo y **avisos**: "3 arqueros sin cobertura en un grupo de infantería pesada", "monturas distintas: ritmo desigual", "armas de fuego sin munición compartida".
- **Capacidades:** `Capability` (ya existente) se deriva de este perfil (una sola fuente de verdad; `force/Forces` hoy las calcula de otra forma: unifica).
- **Efecto sobre la ejecución (acotado, nunca estadísticas):** la consistencia entra en la calidad de ejecución de las órdenes especializadas (`Leadership.quality`): un grupo incoherente responde más lento a instrucciones de especialidad y rompe formación antes; tope ±15 %. Un jugador puede meter lo que quiera en un subgrupo, y el sistema solo lo **refleja** (puntuación + efecto leve), no se lo impide.
- **Dónde se ve:** una **tarjeta pequeña** en el planner (y en el Libro de Mando para grupos): icono de identidad, consistencia en barra, moral/cohesión, 2–3 avisos. Es el "dato de un vistazo" para gestionar grupos.

## 4. Misma instrucción, distinto significado según la especialidad (nuevo)
`InstructionInterpreter.resolve(instruction, GroupProfile) → Behavior`, tabla en `config/rallyguard-instructions.json` (valores por defecto incluidos; se puede retocar sin recompilar). `Behavior` = {doctrina (pesos), estilo de fuego, forma y densidad, banda de distancia, reglas de enganche, disparador de retirada, ritmo}. Ejemplos mínimos (borrador de contenido; la estructura es firme):

| Instrucción | Arqueros / ballesteros | Caballería de rango | Infantería pesada / lanceros | Caballería pesada |
|---|---|---|---|---|
| **Hostigar** | Voleas a alcance máximo, retroceden poco a poco, no se enganchan. | Mantienen distancia, disparan en movimiento, se alejan si les cierran. | **Rodean y amenazan** los flancos con escudo y lanza, sin cargar de frente. | Cargan, golpean y se retiran (golpe y fuga) por el flanco. |
| **Mantener la línea** | Fila detrás de la infantería, fuego sostenido. | Reserva móvil tras la línea. | Línea cerrada, escudos arriba. | Reserva para contraataque. |
| **Avanzar** | Por turnos, la mitad dispara mientras la otra avanza. | Avanzan por los flancos a distancia de tiro. | Avance cerrado a paso. | Avanzan tras la infantería, listos para cargar. |
| **Retirarse** | Retirada escalonada con fuego (cobertura). | Se retiran primero, rápidos, cubriendo con disparos. | Retirada ordenada, los últimos en salir. | Cubren flancos de la retirada. |
| **Flanquear** | Ocupan altura/lateral y disparan. | Rodean a distancia y fijan blancos. | Fijan el frente. | **Flanqueo principal**: rodean y cargan por detrás. |
Grupo (varios subgrupos) → el grupo emite la instrucción **general** y cada subgrupo la interpreta con su perfil. Un grupo homogéneo la interpreta con su propia identidad. Si el perfil es Heterogéneo, la interpreta con la identidad del **arquetipo dominante** y avisa.
Reglas de implementación: el interpretador **solo compone** lo que ya existe (`Doctrine`, `FireMode`, `FormationShape`/layout, `UnitOrders.Kind`, `Operations`); no crea sistemas paralelos. Una nueva instrucción = una fila en la tabla.

## 5. Preguntas abiertas (Claude Code las responde en su informe, no se detiene)
- ¿Qué cuenta como "armadura alta"? (Propuesta: suma de puntos de armadura ≥ 12; configurable.)
- ¿Qué hacer con guardias que cambian de equipo en combate? (Propuesta: recalcular perfil con ≤ 1 s de retraso y no más de 1 vez por segundo por grupo.)
- Los números de la tabla de §4 son un primer borrador: la simulación (`groupprofile-*`, `instruction-*`) debe mostrar que cada celda se comporta distinto a las demás.

# PROMPT_E3A para Claude Code — Fuerzas y Battle Planner (2026-10-08)

**Cupo:** ilimitado por ahora. **Reparto obligatorio: correcciones ≤ 20 % del esfuerzo; el resto, avance, y la veteranía (bloque V) es el centro de profundidad del mod.** Si dudas entre pulir un arreglo y avanzar un bloque, avanza.

Reglas (las mismas de E2A, resumidas): lee solo lo necesario; sin web; **los jar de otros mods NO están en tu sesión** → usa `docs/API_MODS.md` y anota preguntas abiertas en el informe en vez de detenerte; no puedes compilar → parse + chequeo de tipos por bloque y APIs de primer uso en `PORTING_PLAN.md` (≤40 líneas por bloque; no lo leas entero, usa `tail -n 120`/`grep`); **un commit y `git push` por bloque**; sin preguntas a David; informe final ≤30 líneas con qué NO quedó y qué probar.

**Lee primero (en este orden):** `docs/DECISIONES_DAVID_2026-10-08.md`, `docs/BATTLE_PLANNER.md`, `docs/CONTROLES.md`, `docs/MATRIZ_FEATURES.md`, `docs/FEEDBACK_E1.md`, `docs/API_MODS.md`, `docs/DECISIONES_TACTICAS.md`, `docs/VETERANIA_ESTADO.md`, `docs/VETERANIA.md`, `docs/sim-results/20261007-quick/summary.md`, y solo los archivos que vayas a tocar. **Los principios de `DECISIONES_DAVID` son firmes.**

## Bloque F · Correcciones (≤20 %)
- **F1 · Guardias de facciones distintas se atacan.** Causa medida: la lista `Mob Blacklist` de Guard Villagers contiene `guardvillagers:guard`; `Guard.canAttack` y `Guard.setTarget` rechazan esos objetivos y `CommandTargeting.wouldAccept` también la respeta. Haz que la hostilidad entre guardias de **facciones/dueños distintos** sea decidida por Rally y no dependa de esa config (mixin pequeño y apuntado con plugin que verifique la firma, como el de MCA Crime; o la vía más segura que encuentres). Define "facción" (dueño, o id de bando de simulación). Criterio: en `mirror-*` el daño de ambos bandos > 0 y la tasa de victoria ≈ 50 %. (David ya quitó esa entrada de la config en su perfil de pruebas, pero el mod no debe depender de eso.)
- **F2 · Objetivo fuera de alcance:** una orden de Atacar (G o botón) sobre un objetivo más lejos que el alcance del arma debe hacer que se **acerquen hasta el alcance óptimo** y disparen; hoy no reaccionan.
- **F3 · Volea al alcance físico máximo del arma** (calculado con `Ballistics`, no un tope de configuración).
- **F4 · Retirada a casa/HQ:** el radio de "ya llegué" es demasiado grande; redúcelo y haz que entren de verdad.
- **F5 · Postura Pasiva intenta defenderse** (sin tomar la iniciativa).
- **F6 · Persistir** doctrinas y estilo de fuego (junto con el registro de escuadrones).
- **F7 · Simulación:** el reintento por "atascada" no debe esconder el fallo: marca "sin enfrentamiento" como hallazgo propio; guarda `runs.jsonl` de forma incremental (que no se pierda nada si se aborta); revisa por qué `zombie-5` mata a 5 guardias con el mismo equipo (¿paridad de equipo, vida, golpe?).
- **F8 · Botón montar/desmontar** en la ficha del guardia (Libro de Mando).
- **F9 · Formaciones con muchos activos:** añade escenarios `formation-v-100` y `formation-v-200` a la simulación (cohesión a los 30 s) y corrige lo evidente.

## Bloque V · Veteranía v2 (prioridad alta, no sacrificable)
Parte de `docs/VETERANIA_ESTADO.md`. La veteranía es el **centro de profundidad** del mod y debe ser **predominante**: se nota en cómo decide, ordena y sobrevive el guardia, no en daño bruto.
- **V1 · XP por amenaza y contribución.** Sustituye "1 XP por punto de daño": XP por **valor de amenaza del enemigo** (vida, daño, tipo, si vuela o dispara a distancia), **limitado por la vida real de la víctima** (sin XP por daño sobrante), con **tope por minuto**, reparto por **contribución** (daño, asistencias, cubrir a un compañero), bonificación por **superioridad numérica en contra** y por sobrevivir cerca de la muerte; sin XP por golpear a un enemigo inmóvil o repetido. Los jefes dan **mucho pero acotado** (que no salten de rango en una sola pelea). Ritmo objetivo configurable: R2 ≈ 1–2 días de juego de combate regular, R3 ≈ una semana, R4 ≈ varias semanas, R5 raro.
- **V2 · XP de mando (los líderes suben primero).** XP por coordinar: capitanes y generales ganan al sostener la formación de su unidad, evitar bajas, ganar encuentros con su mando, y por la **supervivencia de su unidad**. Va aparte del XP de combate.
- **V3 · Efectos reales de los 7 rasgos** (hoy solo Tanque, Firme y Explorador tienen efecto): **Tirador** (blancos lejanos, prefiere altura y línea de vista, prioriza voladores), **Duelista** (contraataque/esquiva cuerpo a cuerpo), **Jinete** (mantiene formación y carga montado), **Asalto** (avanza primero, mejor al abrir un encuentro), más los otros tres. Sin daño bruto; cambian decisiones y reacciones.
- **V4 · "Qué cambia en cada rango"** visible: tabla en la ficha del guardia y texto al subir de rango.
- **V5 · Salto de IA en el rango máximo** (§2 de `VETERANIA.md`): elegir posición antes de atacar, altura, reposicionar tras disparar, fuego y movimiento por parejas, presión con escudo cuerpo a cuerpo; los tiradores veteranos priorizan voladores. Sin depender de otros mods.
- **V6 · Genética desde MCA** si está instalado (personalidad y rasgos, por la API o reflexión con comprobación de firma); si no, el sistema actual.
- **V7 · Historias:** registro corto por guardia (batallas, bajas notables, relevos, vínculos) visible en su ficha; base del precio calculado futuro.
- **V8 · Anti-abuso y ritmo medible:** nada sube por granjas; **escenario de simulación "ritmo de veteranía"** que estime cuánto combate normal lleva cada rango y lo imprima en `summary.md`.

## Bloque A1 · Fuerzas
`Force` = conjunto de escuadrones con subgrupos creados. **General** = guardia de mayor `commandCapacity` de la fuerza (calculado, nunca una entidad nueva). Doctrina por fuerza. Registro persistente (como `SquadRegistry`), operaciones de crear/disolver/añadir escuadrón. Sucesión y moral ya existen a nivel de escuadrón: engánchalos.

## Bloque A2 · Directivas e informes
- La orden de una fuerza se compila a **directivas versionadas por escuadrón** (`SquadDirectives`): un registro por escuadrón, no un mensaje por guardia; los guardias comparan la versión en su turno escalonado.
- **Bus de informes hacia arriba**: cada escuadrón publica un `SituationReport` (estado, bajas, munición, enemigos vistos) **solo cuando cambia**; el general los resume en `KnownEnemies` de la fuerza (con marca de tiempo). Es la base de la niebla de información y de los scouts futuros.
- Documenta la **precedencia** (capitán vs jugador vs fuerza) en `docs/ORDENES_JERARQUIA.md`, de acuerdo con `CONTROLES.md`, y pruébala con un escenario de simulación.

## Bloque A3 · Comportamientos de fuerza (papeles y maniobras)
Papeles por composición: **yunque, martillo, pantalla, apoyo, flanco, reserva**. El general propone el reparto según la doctrina y rearma los papeles cuando caen capitanes. Maniobras que deben existir: **mantener la línea** (apoyo en quietos detrás de los tanques), **retroceder atacando**, **avance por turnos**, **flanqueo**, **repliegue ordenado al HQ**. Usan los pesos de `Doctrine`/`DecisionLadder`; los capitanes traducen la maniobra a estilo de fuego y formación del subgrupo. Un guardia individual cambia de estilo según la situación; **dentro de un grupo se acopla a la doctrina del grupo.**

## Bloque A4 · Battle Planner v1 (según `docs/BATTLE_PLANNER.md`)
Pantalla cliente con **mapa cenital** (colores de `MapColor` por columna, solo chunks cargados, generado en lotes por tick y cacheado; sin leer el mundo entero), marcadores de escuadrones/HQs/enemigos conocidos, selección de fuerzas, objetivo (mantener, atacar zona o grupo, escoltar, patrullar, repliegue), doctrina, papeles, **lista lineal de fases con disparadores simples** (tiempo, bajas > X %, enemigo visto), ejecutar/cancelar, **guardar/cargar planes** (por jugador, persistentes). Autoridad en el servidor (paquetes validados por dueño). Teclas: `G`+número selecciona fuerza; atajos de operación (retirada, retroceder atacando, mantener, avanzar). **Ítem y colocación (decisión de David):** el ítem es un **pliego/mapa** que se **coloca sobre una mesa** en un **centro de operaciones** (dentro de la aldea, o en una carpa pequeña en campaña); se hace clic sobre él y se abre el planner. Debe estar dentro de una zona de HQ (la carpa es un HQ pequeño). El Pergamino de Reunión pasa a ser ese ítem (borrador); su "reunir en abanico" pasa a plantilla "Reunir". Mapa dibujado por código y **sin dependencias externas** (si en el futuro se integra el mapa de Xaero, será opcional).

**Vínculos entre grupos (órdenes de interacción):** cada vínculo `{tipo, grupo origen, grupo destino, disparador, parámetros}`. Implementa al menos: **Relevo** (si el grupo X cae por debajo de N % o es destruido, Y ocupa su puesto y papel), **Cobertura** (Y cubre el movimiento o la retirada de X), **Refuerzo** (si X está en contacto más de T s, Y se une), **Enlace** (reunirse en un punto), **Pantalla** (Y protege el frente/flanco de X), **Escolta** (Y acompaña a un grupo, como ingenieros), **Mantener hasta el relevo**, **Retirada escalonada** (A cubre a B y luego intercambian). Cada uno con disparadores de la lista de fases. Los demás (pinza coordinada, reserva liberada por señal, persecución limitada, prioridad de objetivos entre grupos) quedan documentados en `docs/BATTLE_PLANNER.md`.

## Bloque A5 · Seguir en formación (para cientos)
`FollowOrder` v2: modos **detrás / delante / escolta / columna / abanico**; el jugador no queda en el centro; reparto de huecos en el tiempo (`SlotQueue`), subunidades con capitán. Debe escalar a 200.

## Bloque A6 · HQ como zona y estado "Relajados"
HQ = zona alrededor del estandarte (radio configurable), **no depende de un edificio**: campamento en el bosque o castillo; entrada por **cualquier puerta o hueco**; interior opcional. **Al llegar:** romper filas automáticamente, **aparcar las monturas**, estado **Relajados** (sin formación, descansan/rondan); **al salir** con una orden, montan y parten. "Romper filas" deja de ser un botón del panel.

## Bloque A7 · Interfaz de grupo limpia
El panel de acciones es **de grupo**. Quita del panel individual el estilo de fuego y la doctrina (el estilo individual lo determina su **personalidad y su arma**, codificado). Un solo bloque de acciones por selección, con disponibilidad según su composición (ya existe `OrderSemantics`). Avanzar y Mantener se mueven al planner (déjalos accesibles solo mientras no exista).

## Bloque A8 · Maestría de arma v1 (H-06, central)
Maestría **por tipo de arma** guardada por UUID; se gana usando el arma en combate real (rendimientos decrecientes). Efecto vía **presupuesto** configurable por arquetipo: cadencia, recarga, dispersión, y la **duración/alcance de los efectos especiales** antes que el daño bruto; con tope. A nivel máximo el guardia es una **amenaza real** con esa arma. Tabla por arquetipo + sobrescrituras por arma en `config/rallyguard-weapon-mastery.json` (sin recompilar). Compatibilidad con Scorched Guns por `ScorchedGunsCompat` y con armas de otros mods por propiedades genéricas (encantamientos, componentes), nada específico de un mod.

## Bloque A9 · Simulación v3
- **Terrenos de arena**: montaña, llanura, río (guardias nadan, sin ahogarse), lava (solo como obstáculo con borde), acantilado, colina con ventaja de altura.
- **Escenarios de fuerza**: escuadrón contra escuadrón y fuerza contra fuerza, matriz de doctrinas, papeles y distancias; `scale-50/100/200/400`.
- **Laboratorio de improvisación (mínimo)**: un modo que, con baja probabilidad configurable, aplica una **acción fuera de lo establecido** a un guardia/subgrupo (registrada con nombre provisional) y la compara en `summary.md`; si mejora los resultados de forma repetida, queda marcada como "candidata a táctica". No se promueve sola.
- `summary.md`: añade **comparación con el lote anterior** y una sección "Qué aprendimos" que el programa rellena con las diferencias significativas.

## Corte si el cupo se agota (sacrificable → intocable)
A9 (laboratorio) → A8 → A7 → A5 → A6 → A4 (mapa) → … **Intocables:** F1–F5, V1–V4, A1, A2, A3.

**Entrega:** un commit y push por bloque; informe ≤30 líneas.

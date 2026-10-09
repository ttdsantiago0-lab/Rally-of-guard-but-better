# ESTILOS_DE_MANDO — cómo piensa, coordina y decide cada líder (diseño, 2026-10-09)

**Estado:** firme en los principios (D4 de `DECISIONES_DAVID_2026-10-09.md`), borrador en los números. Se implementa en **E3D**, después de E3C.
**Motivo (David):** "los roles de mando son los que más aportan al mod". Un estilo **no es un porcentaje de miedo**: es una manera de leer la batalla, repartir el trabajo entre subordinados y responder a las instrucciones de arriba. Y no hay números fijos: todo depende del contexto, de la moral y de la intención del plan.

## 1. Principios
1. **Un estilo es un sesgo de juicio, no un bono.** No da vida, daño ni velocidad. Cambia qué *ve* el líder, qué *prefiere* y cómo *coordina*.
2. **Nada de "se retira al X %".** Las decisiones salen de una valoración de la batalla (§3) en la que el estilo inclina la balanza.
3. **Contexto manda.** Con 50 % de bajas pero victoria a la vista, hasta un líder prudente sigue; con 20 % de bajas pero una emboscada, hasta uno audaz repliega.
4. **Se deriva del líder** (rasgos, historial, XP de mando) y se ve en la tarjeta del planner; el jugador lo aprovecha **eligiendo a quién pone al mando**. Sin botones.
5. **Muchos estilos, pocos ejes.** Se modela como combinación de ejes: así salen decenas de estilos sin programar cada uno. Las **guerrillas** usan el mismo sistema con sus propios estilos.
6. **Profundo, no vistoso:** un estilo se nota en la *coordinación* (quién hace qué y cuándo), no solo en el umbral de huida.

## 2. Los ejes (cada líder tiene un valor −1..+1 en cada uno)
| Eje | −1 | +1 | Qué decide |
|---|---|---|---|
| **Riesgo** | Prudente | Audaz | Cuánto coste acepta por una ventaja; cuándo corta |
| **Disciplina** | Metódico (literal) | Oportunista (flexible) | Cuánto se ajusta al plan frente a explotar lo imprevisto |
| **Mando** | Centralizado | Delegador | Cuánto dicta cada movimiento frente a dar intención y dejar ejecutar |
| **Ritmo** | Paciente | Presionante | Cuánto espera a tener ventaja frente a mantener iniciativa |
| **Fuerzas** | Especialista | Armas combinadas | Cuánto usa cada capacidad en su papel frente a combinarlas |
| **Honor** | Pérfido | Leal | Si cumple lo que señala (rendición, tregua) o lo usa como engaño. Sale del repertorio de David (rendición fingida) |
Los nombres que ve el jugador son **combinaciones** (ejemplos de partida): *Prudente* (riesgo −), *Audaz* (riesgo +, ritmo +), *Metódico* (disciplina −, mando −), *Oportunista* (disciplina +, mando +), *Estratega* (fuerzas +, ritmo −), *Martillo* (riesgo +, fuerzas −), *Escudero* (riesgo −, ritmo −, fuerzas −)… Añadir un estilo = añadir una fila de JSON (`config/rallyguard-command-styles.json`: nombre, ejes, texto). Un líder puede estar entre estilos (valores intermedios).

Todos los estilos pueden **combinarse con la movilidad** de la fuerza (§11): una banda ligera puede huir; una guerrilla asentada, no.

## 3. El modelo de decisión (aquí se ve el estilo)
### 3.1 `BattleOutlook`: ¿cómo va la batalla?
Cada segundo y solo para líderes (capitán/general), con datos que ya existen (`SquadPicture`, bajas, moral, operaciones):
`outlook ∈ [−1, +1]` = combinación de: **relación de fuerzas** (ponderada por rango/arma/formación), **impulso** (bajas causadas vs sufridas en los últimos ~20 s), **progreso del objetivo** de la fase, **cohesión y moral** propias y enemigas, **terreno** y **refuerzos**.
- **Calidad de la lectura = puntaje de liderazgo**: un líder malo tiene lectura con ruido y retraso; uno bueno lee a tiempo y con acierto.
- **Estilo = qué pesos infla**: el Audaz da más peso a impulso y progreso del objetivo; el Prudente a sus propias bajas y a la incertidumbre (lo que no ve); el Metódico al calendario del plan (¿vamos a tiempo?); el Oportunista a las ventanas inesperadas; el Presionante al impulso; el Paciente a la relación de fuerzas.

### 3.2 Seguir, ajustar o romper contacto
No hay umbral fijo. La decisión es: **seguir** si `outlook + valorObjetivo + empujeMoral − aversiónAlRiesgo(estilo, intención)` supera el umbral de salida con **histéresis** (el que ya usa `ForceRules`).
- **`valorObjetivo`** viene de la **intención de la fase** (Battle Planner): "mantener posición", "destruir", "ganar tiempo", "romper línea" tienen distinto coste aceptable.
- **`empujeMoral`**: con las unidades firmes el líder aguanta más; con unidades vacilantes la decisión se adelanta (la moral ya estaba diseñada en `MORAL_Y_COHESION.md`).
- **Instrucciones de arriba**: el general que ordena "resistir a toda costa" sube `valorObjetivo`; "evitar bajas" lo baja. El estilo del capitán decide *cuánto de esa instrucción interpreta literalmente*.
- **Resultado:** con victoria a la vista (`outlook` alto) hasta un Prudente continúa aunque lleve 50 % de bajas; con una emboscada (`outlook` en picada) hasta un Audaz rompe contacto, pero lo hace *a su modo* (§4).

## 4. Dónde se nota el estilo (coordinación y estrategia, no solo huir)
| Decisión | Prudente | Audaz | Metódico | Oportunista | Estratega |
|---|---|---|---|---|---|
| **Reserva** | Retiene una reserva sustancial y la usa para cubrir | Casi toda la fuerza al frente; reserva mínima | Reserva exactamente la del plan | Reserva flexible: la suelta donde aparece una brecha | Reserva móvil con papel designado |
| **Inicio de fase** | Espera señal clara de supresión | Entra con el primer hueco | Cambia de fase al cumplirse la condición escrita | Adelanta o retrasa según lo que ve | Solapa fases (arqueros y asalto casi a la vez) |
| **Coordinación con subordinados** | Da órdenes de cobertura mutua, avanza en "saltos" | Delega: da la intención, deja a cada unidad improvisar | Sincroniza: espera a que cada unidad confirme | Reasigna papeles en caliente | Reparte capacidades: sniper, línea, pantalla, jinetes |
| **Uso de capacidades** | Prefiere `HOLD_POSITION`, `SCREEN`, `RETREAT_COVER` | Prefiere `MOUNTED_CHARGE`, `MELEE_LINE` | Usa cada una en el papel asignado por el plan | Mueve capacidades al hueco | Combina: acosa con jinetes mientras fija con línea |
| **Persecución** | Corta pronto, no se separa del grupo | Persigue y remata | No persigue lo que el plan no marca | Persigue si ve ventaja clara | Persigue con jinetes, mantiene la línea |
| **Retirada** | Ordenada, por escalones, con retaguardia | Tardía, pero violenta: *contraataque de salida* si hay jinetes | Según el plan de retirada escrito | A la menor ventana de escape | Retira lo que sobra y conserva la capacidad clave |
| **Respuesta a una amenaza clasificada** (p. ej. un mago volador) | Se cubre y avisa | Intenta cargar si hay alguien que llegue | Sigue el procedimiento (`COUNTER_ASSIGNED`) | Improvisa con lo disponible | Asigna al francotirador y reposiciona la línea |
| **Si cae el capitán** | Se repliega a la sucesión ordenada | Sucesión rápida y sigue avanzando | Vuelve al plan con el relevo | Cualquier subordinado toma la iniciativa | El segundo al mando reordena capacidades |

## 5. Ejemplo narrado (sin cifras fijas): asalto a un puesto
Escenario `ASALTO_A_ESTRUCTURA`; 40 guardias; plan de 3 fases; mismo equipo, mismas semillas.
- **Prudente:** abre con supresión de arqueros hasta tener `outlook` claramente positivo, entra con 28 y deja 12 en reserva cubriendo. A mitad llega la sorpresa: el enemigo tiene refuerzos. El `outlook` cae, rompe contacto por escalones con la reserva cubriendo. **No toma el puesto**, pierde poco, y el jugador lo sabe: "este capitán protege a los suyos".
- **Audaz:** entra con el primer hueco; la supresión aún no termina. Con refuerzos enemigos, el impulso favorable (sus jinetes ya remataban) mantiene el `outlook` aceptable y **sigue**; toma el puesto con bajas altas. Si el impulso hubiera estado en contra, habría hecho un *contraataque de salida* y roto contacto.
- **Metódico:** respeta las fases al segundo; no usa la brecha lateral porque no estaba en el plan. Con los refuerzos, **sigue el procedimiento de retirada del plan**: es previsible y limpio.
- **Estratega:** solapa fases, reparte capacidades, asigna al francotirador a los refuerzos pesados, mantiene la línea con la infantería. Pierde menos que el Audaz y toma el puesto más tarde.
- **Mismo caso a 50 % de bajas pero ganando** (el enemigo reducido a un cuarto): los cuatro *continúan*. Lo que cambia es *cómo*: el Audaz persigue a los que huyen, el Prudente consolida la entrada.

## 6. Cómo se relaciona con lo que ya está decidido
- **Puntaje de liderazgo** = calidad de ejecución y de lectura de la batalla. **Estilo** = hacia dónde se inclina. Dos líderes con el mismo puntaje y distinto estilo actúan distinto.
- **Moral** (M, defecto `morale.strength` 0,4): entra en `empujeMoral` y en la lectura del `outlook`.
- **Battle Planner:** la intención de fase y las reglas de fuerza son el marco; el estilo decide cuánto se mueve el líder *dentro* del marco. Tarjeta del planner: "capitán Marta · Prudente/Estratega · lectura: buena".
- **Sucesión:** al caer el líder el estilo cambia con el relevo (el planner lo avisa).
- **Veteranía:** el estilo es la "personalidad de mando" que se forma con el historial y la vía de mando.

## 7. De dónde sale el estilo de un líder (sin botón)
Semilla por **rasgos** (Firme→Metódico, Asalto→Audaz, Tanque→Escudero, Explorador→Oportunista), desplazada por **historia** (un capitán que perdió a muchos tiende a Prudente; uno que ganó con riesgo, a Audaz) y por **XP de mando**. Se recalcula por eventos (cambia el líder, termina una batalla), con tope de variación por batalla para que no oscile.

## 8. Guerrillas
Mismo sistema, otro catálogo de estilos (por ejemplo *Acosador* —riesgo −, ritmo +—, *Emboscador* —paciente, disciplina −—, *Saqueador* —riesgo +, fuerzas −—, *Fantasma* —prudente, delegador—). El jefe de una banda guerrillera tiene su propio `BattleOutlook` (más sesgado a escape y botín), y los estilos afectan **qué incursión elige** y **cuándo huye con el saqueo**. (Se implementa en el addon; E3D deja el catálogo en JSON listo para añadirlos.)

## 9. Medición (simulación)
- `style-compare`: mismo escenario, mismos guardias, líder con 5 estilos. Se miden bajas, tiempo, objetivo logrado, uso de reserva, retiradas, **y diferencias de comportamiento** (orden de fases, uso de capacidades, distancia de persecución).
- **Aceptación:** (a) los estilos producen resultados *distintos* y ninguno es estrictamente mejor en todos los escenarios (si uno lo es, es un bono disfrazado); (b) en `batalla-ganada-con-bajas-altas` los 5 estilos continúan; (c) en `emboscada-sorpresa` ninguno se queda a morir; (d) 0 oscilaciones seguir↔retirar en 60 s (histéresis); (e) el sobrecoste del `BattleOutlook` ≤ 0,03 ms/tick con 200 guardias (solo líderes, 1 Hz).

## 10. Resolución de la batalla: más desenlaces que "seguir o huir" (David, 2026-10-09)
Cuando la lectura es mala, el líder no solo elige entre seguir y retirarse. Elige una **resolución** (`BattleResolution`), que depende de su estilo, su **movilidad**, su facción y su moral:
| Resolución | Qué ocurre | Quién la elige típicamente |
|---|---|---|
| `CONTINUE` / `ADJUST` | Sigue o reajusta (reserva, carriles, papeles) | cualquiera con lectura buena |
| `DISENGAGE` | Rompe contacto ordenadamente (retaguardia, escalones) | móviles, prudentes, estrategas |
| `FIGHT_TO_DEATH` | Combate hasta el final: sin retirada ni rendición | **temerarios**, fanáticos, bandas sin escapatoria y sin esperanza de piedad |
| `SURRENDER` | Se rinde de verdad | **fuerzas grandes y asentadas** (no pueden huir) con honor medio/alto, o moral destrozada |
| `FEIGNED_SURRENDER` | Finge rendirse para tender una trampa | **radicales pérfidos** (honor −): se acercan a ti y detonan |
| `SCORCHED_EARTH` | Destruye lo que no se puede llevar antes de que lo tomes (provisiones, depósito, estructura) | radicales/saqueadores sin salida |
- **Movilidad:** cada fuerza tiene `mobility` (ligera, normal, asentada). Una **guerrilla grande ya asentada** no puede `DISENGAGE`; si el `outlook` cae, el abanico es `SURRENDER`, `FIGHT_TO_DEATH` o `SCORCHED_EARTH` según estilo y honor. Una banda ligera casi siempre huye antes.
- **Rendición real (`SURRENDER`):** la unidad pasa al estado `SURRENDERED`: deja de atacar, no huye, queda a merced del jugador. Entonces el jugador puede **contratar** a sus miembros (con el precio de la valoración de contratación, usando la pantalla de contratación que ya existe), **traspasar** a los que quiera a su bando (propiedad por `GuardOwnership`, empiezan como reclutas de su facción con su historia) o **dejarlos ir**. Si el jugador se aleja o ataca, el estado caduca o se rompe.
- **Rendición fingida (`FEIGNED_SURRENDER`):** se ve igual que la real, pero la unidad tiene la bandera `trap`. Un capitán o explorador **perceptivo** del jugador (visión, rango, puntaje de liderazgo) puede `TRAP_SUSPECTED` y avisar en la tarjeta ("rendición dudosa"); si nadie lo nota y el jugador (o sus guardias) entra en el radio de la trampa, detonan.
- **Tierra quemada (`SCORCHED_EARTH`):** destruyen lo que no se pueden llevar. **Por seguridad del mundo de David:** el daño a bloques está **apagado por defecto** (`resolution.scorched.blockDamage=false`); por defecto la explosión daña entidades y destruye el contenido de los depósitos, con efectos visuales; David lo enciende cuando quiera.
- **Se aprovecha lo ya diseñado:** el estilo (honor/riesgo) decide *cuál*; la moral destrozada empuja a `SURRENDER`; el balance entre honor y movilidad es lo que hace que una guerrilla pequeña huya, una grande se rinda, una temeraria muera y una radical engañe.
- **Dónde vive:** la mecánica general (`BattleResolution`, `SURRENDERED`, contratación y traspaso) va al **núcleo** en E3D para guardias de facciones enemigas; las guerrillas del addon la reutilizan con su catálogo de estilos.

## 11. Mediciones nuevas
- `surrender-hire`: unidad asentada de 25 con `outlook` en picada → `SURRENDER`; el jugador contrata a 10 y traspasa a 5; conservación de objetos y 0 guardias duplicados.
- `feigned-surrender`: capitán perceptivo detecta la trampa ≥ 70 % de las veces; si no, la trampa se activa **solo** dentro de su radio.
- `fight-to-death` y `scorched-earth`: con `blockDamage=false` no se rompe un solo bloque.
- Aceptación global: ningún estilo/resolución causa bucles ni estados colgados (toda resolución tiene caducidad).

## 12. Preguntas resueltas y abiertas
Resueltas (2026-10-09): más complejidad = mejor, mientras no rompa el código; el jugador **sí** verá el `outlook` (barra) del capitán en la tarjeta del planner; se añade el eje **Honor**; las guerrillas también tienen estilos y resoluciones.
Abierta: ¿cuando una guerrilla se rinde, quieres que sus miembros conserven **objetos** (el jugador los recupera al "registrarlos") o que se rindan con el equipo puesto y sin botín extra? Recomiendo: conservan lo puesto y el botín va al jugador, repartido igual que el saqueo.

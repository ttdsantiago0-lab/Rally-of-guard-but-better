# Laboratorio de simulaciones: descubrir lo que no se nos ocurre

2026-11-08 · Pedido de David (con su borrador escrito con ChatGPT). Fuente de verdad del laboratorio. Integra
`docs/SONDA_DE_TECHO.md` (laboratorio A) y no reemplaza nada de `docs/SIMULACIONES.md` ni del torneo
(`docs/TORNEO_DOCTRINAS.md`): los extiende.

## La idea de David, en una frase

Las grandes técnicas (kitear, la tortuga, el tiro a quemarropa) son **combinaciones de acciones** a las que alguien
les puso nombre. El laboratorio debe encontrar esas combinaciones solo, entre pocos guardias o entre grupos y roles
distintos, y encontrar también los escenarios donde los guardias fallan aunque cada variable por separado parezca
fácil. Así el conocimiento crece de forma continua y no depende de lo que se nos ocurra.

## 1. Lo que ya existe y se reutiliza (auditoría)

| Pieza | Dónde | Qué aporta al laboratorio |
|---|---|---|
| Simulación sin pantalla | `SimAutorun`, `SimRunner` | El motor de ejecución único de todos los laboratorios |
| Escenarios como datos | `rallyguard_sim/scenarios.json` (≈1000 escenarios, presets) | Formato común: bandos, cantidades, tipo, rol, rango, formación, montura, modo de fuego, distancia, posición, terreno, minutos, etiquetas |
| Generador de escenarios | `tools/torneo.py` (gen-*) | Ya genera familias de escenarios; se convierte en el generador del laboratorio |
| Análisis estadístico | `tools/torneo.py` (report, confirm, analyze) | Medias ± error estándar, protocolo de confirmación (dos lotes, otras semillas) |
| Ejecución en paralelo y registro | CI (`build.yml`), rama `sim-results` | 16 presets a la vez gratis; cada lote en su carpeta, nunca se sobrescribe, con `commit.txt` |
| Telemetría por acción | `extra.act:*`, causas de muerte, motivos de decisión | La materia prima para describir **qué hicieron** los guardias, no solo si ganaron |
| Laboratorio de improvisación | preset `lab` (4 acciones fuera de guion, marca "candidata a táctica") | La semilla del descubrimiento de tácticas |
| Arena | `SimArena` (llano, colina, cobertura, muro, bosque; repara el suelo) | Sirve para pruebas controladas; **no** representa el mundo real (ver §3) |
| Condiciones controladas | medianoche fija, sin ciclo de clima | Ya evita que el sol queme zombis y esqueletos y decida una batalla |
| Oleadas, escolta, tortuga | `t6`, `t17`, `t18` | Ya hay "misiones" simples y eventos durante la pelea |

Encontrado en la auditoría y corregido hoy: la noche fija dejaba encendida la **aparición natural de mobs**
(patrullas, fantasmas, mobs nocturnos) que podían colarse en una corrida sin que ningún escenario lo pidiera. Desde
este commit las corridas sin pantalla apagan `doMobSpawning`, `doPatrolSpawning`, `doTraderSpawning` y `doInsomnia`.
Queda vivo, a propósito, el refuerzo propio de los zombis (es parte del mob, no del entorno).

## 2. Variables

**Sí:** número de guardias; número de enemigos; tipos y mezclas; atributos de los enemigos (vida, velocidad, daño:
multiplicadores); equipo de los guardias (armas, tipo de ataque, armadura, escudo, comida, tótem); salud y recursos
iniciales; distancias iniciales; posiciones y despliegue; terreno real (§3); obstáculos y accesibilidad; objetivos y
restricciones de la misión; información disponible (alcance de detección; el oráculo = información total);
estrategia y coordinación; incidentes (muere una unidad o el líder, se corta una ruta, llega otra amenaza).

**Controladas, no exploradas:** hora (medianoche fija), clima (despejado), aparición natural (apagada). Solo entran
como variable cuando la pregunta es sobre ellas (p. ej. "¿conviene pelear al amanecer contra no muertos?").

**Fuera por ahora:** personalidad (capa posterior; ver `PLAN_MAESTRO.md`).

La lista es abierta: una variable nueva entra en el registro de factores (§4, paso L1) cuando una observación la
justifica.

## 3. Terreno real

Objetivo: probar en el Minecraft de verdad, no en escenarios simplificados, y que cada terreno sea **reproducible**.

1. **Catálogo de terrenos.** La simulación genera un mundo con una semilla fija y busca lugares por bioma y
   estructura (bosque, llanura, montaña y pendiente, cueva, río, costa, aldea, estructuras), con las funciones de
   búsqueda de Minecraft (las mismas de `/locate`). Con los mods del modpack en la CI, también sus biomas y
   estructuras.
2. **Ficha de cada terreno**, medida automáticamente: desnivel, cobertura (árboles, muros), agua, obstáculos,
   visibilidad (líneas de tiro abiertas), accesibilidad (una sonda de navegación prueba rutas entre puntos), espacios
   estrechos y abiertos. La ficha permite elegir y clasificar terrenos por lo que importa, no por el nombre del bioma.
3. **Reproducir.** La primera vez se guarda el trozo como plantilla de estructura; antes de cada corrida se vuelve a
   pegar (como ya hace la arena al reparar el suelo). Mismo terreno, misma semilla de la corrida, mismo resultado
   esperado, aunque la generación de mundo cambie entre versiones.
4. **Sin muros.** En terreno real no hay muro perimetral: escaparse o perderse pasa a ser un resultado medido.
5. **El mundo de David.** Más adelante, trozos de su mundo como plantillas (ya está en el backlog).

La arena artificial se queda para las pruebas del cuerpo (laboratorio A), donde justamente se quiere aislar una
capacidad.

## 4. Cómo se exploran las combinaciones (sin explosión aleatoria)

Cada corrida corta cuesta ~1 s de máquina y corren 16 máquinas a la vez: miles de corridas por noche. Aun así, las
combinaciones de 15 variables son infinitas, así que el laboratorio trabaja en este orden:

| Paso | Método | Para qué |
|---|---|---|
| 1. Registro de factores | Cada variable con su rango relevante y su unidad (un archivo de datos) | Definir el espacio |
| 2. Cobertura combinatoria | Arreglos de cobertura por pares y por tríos | Cubrir todas las parejas y tríos de valores con pocas corridas: es lo que destapa interacciones como "funciona en abierto, falla en bosque" |
| 3. Cribado | Efectos principales e interacciones con su error estándar (regresión) | Descartar las variables que no cambian nada y señalar las combinaciones que sí |
| 4. Caza de fallos | Búsqueda adversaria: un optimizador mueve las variables del escenario para que los guardias pierdan | Encontrar fallos sistemáticos que nadie diseñó |
| 5. Vecindario del fallo | Variaciones pequeñas alrededor de cada fallo | Encontrar la frontera: dónde empieza a fallar y por qué |
| 6. Galería de fallos | Los escenarios interesantes se guardan con nombre | Se repiten con cada versión nueva (suite de regresión) |

## 5. Cómo se descubren combinaciones de acciones (tu "kitear")

1. **Vocabulario de acciones** que Rally ya sabe mandar a un guardia: acercarse, alejarse a una distancia, esquivar
   de lado, mantener posición, reagruparse, separarse, elegir blanco (el más cercano, el más débil, el que dispara,
   el que ataca a un compañero), subir o bajar el escudo, cambiar de arma, comer, cubrir a otro, tapar un hueco,
   cambiar de rol dentro del grupo.
2. **Políticas como listas de reglas**: "si un enemigo cuerpo a cuerpo está a menos de *d1*, alejarse a *d2* sin
   dejar de disparar". Las reglas pueden ser por guardia o por grupo (unos atraen, otros disparan), y las
   distancias y tiempos son perillas.
3. **Búsqueda evolutiva con diversidad** (en inglés, MAP-Elites): no busca una sola política ganadora, sino la mejor
   de cada **estilo** (por ejemplo: cerca o lejos del enemigo, mucho o poco movimiento, juntos o dispersos). Así salen
   a la vez el estilo "kitear", el "muro" y otros que no tienen nombre todavía.
4. **Nombrar y confirmar.** Cada estilo ganador se describe en palabras ("los escudos atraen, los arcos disparan
   desde atrás y rotan"), se le pone nombre y pasa por el protocolo normal: dos lotes de 30 con otras semillas,
   frente a la base, en varios terrenos. Si se confirma, entra al Manual de campo como regla del juego, igual que el
   tiro a quemarropa.
5. Grupos, roles raros y magos entran cuando existan como rol en el código: el vocabulario crece con el mod.

## 6. Los cinco laboratorios (una sola infraestructura)

| Laboratorio | Pregunta | Etapa del plan |
|---|---|---|
| **A. Techo físico** (`SONDA_DE_TECHO.md`) | ¿Cuánto da el cuerpo de GV? GV original / mismo cuerpo con oráculo / cuerpo modificado con oráculo | 1 (primero) |
| **C. Táctico y coordinación** | ¿Qué combinaciones de acciones ganan, según enemigo, terreno, composición y objetivo? (§5) | 1 en adelante (continuo) |
| **D. Límites y estrés** | ¿Dónde se rompe? Muchos enemigos, terreno complejo, aislados, rutas cortadas, escuadras grandes; coste por tick y estabilidad | 1 (caza de fallos) y 3 (escala) |
| **E. Adversarios jugadores** | Agentes de prueba deterministas con estilos de jugador: tirar a distancia, fortificarse, trampas, explosivos, separar guardias, desgastar, ir a por el comandante | 2 (el "jugador que vale 10") |
| **B. Misiones** | ¿Cumplen el objetivo, no solo ganan? Ir de A a B, escoltar, defender, rescatar, eliminar, transportar, explorar, misiones combinadas | 4 (con el Battle Planner) |

Todos comparten: el generador, el formato de escenario, los terrenos, el motor, el registro, las métricas comunes
(ganar, sobrevivir, daño, tiempo, coste por tick) más las propias de cada laboratorio (misión cumplida, llegada,
protegido vivo).

## 7. Registro y calidad de los resultados

- Cada corrida guarda: commit del código, huella de la configuración, escenario y sus parámetros, terreno, semilla,
  estrategia y sus parámetros, y resultados. Los lotes nunca se sobrescriben (ya es así en `sim-results`).
- Cada experimento tiene un identificador y un **cuaderno** (`docs/lab/<id>.md`) con cuatro partes separadas:
  - **Observaciones:** qué pasó (lo escribe la máquina: tablas y cifras).
  - **Hipótesis:** por qué creemos que pasó (Claude).
  - **Conclusiones:** solo lo confirmado con el protocolo.
  - **Decisiones:** qué cambia en el mod (David).
- `docs/AVANCE.md` sigue siendo el índice de lo que cambió y `docs/MANUAL_DE_CAMPO.md` el de lo confirmado.

## 8. Evolución incremental

| Paso | Qué | Sesiones de Claude (estimado) |
|---|---|---|
| L0 | Identificador de experimento, huellas de escenario y estrategia en cada corrida, plantilla del cuaderno | 1 |
| L1 | Registro de factores + cobertura por pares/tríos + cribado en `tools/` | 2–3 |
| L2 | Laboratorio A: oráculo y pruebas del cuerpo → **diagnóstico temprano** | 2–3 |
| L3 | Terreno real: catálogo, ficha, plantillas (vanilla primero, luego modpack) | 3–4 |
| L4 | Caza de fallos + vecindario + galería de fallos (que es también la suite de regresión) | 2–3 |
| L5 | Vocabulario de acciones + políticas por reglas + búsqueda con diversidad | 3–5 |
| L6 | Laboratorios E, D y B, cada uno con su etapa | con su etapa |

L0–L2 van primero porque el diagnóstico temprano decide si seguimos con el cuerpo de Guard Villagers. L3 antes que
L5 porque una táctica descubierta en terreno plano puede no valer en un bosque.

## 9. Límites honestos

- Una táctica solo puede usar acciones que existan en el vocabulario: el laboratorio combina, no inventa
  movimientos nuevos. Si el oráculo (A) muestra que falta una acción física, se programa y se añade.
- Lo que la máquina encuentra se confirma siempre con el protocolo; un ganador de una búsqueda sin confirmar es una
  hipótesis.
- Si una táctica gana pero se ve rara o no es divertida, decides tú.

## 10. Inteligencia de batalla y números lógicos (David, 2026-11-09)

Meta: después de muchas permutaciones, llegar a números que tengan sentido y progresen (por ejemplo, unos 5 guardias
para un dragón joven, unos 10 para uno grande, menos si son maestros con armas y armaduras poderosas). Para eso cada
batalla mide, además de quién gana (desde `875f3cd`, en `extra` de cada corrida):

| Pregunta de David | Medida |
|---|---|
| ¿Cuánto daño máximo hace un guardia? | `dps` contra el muñeco de práctica (ronda 33: zombi quieto sin armadura de 1024 de vida), por arma y equipo |
| ¿Cuánto le hace a un monstruo y cuánto duran vivos? | `dmgDealt`, `guardSeconds`, `dps` y `survivalSeconds` en cada batalla; dps contra el monstruo ÷ dps contra el muñeco = cuánto le quitan sus defensas |
| ¿Se quedan quietos recibiendo el aliento o el láser? | `hurt:<tipo>`: daño recibido por tipo de ataque (qué ataque mata) |
| ¿Se amontonan bajo un ataque de área? | `aoeHits` (un atacante alcanza a 3 o más en el mismo tick) y `nnDist` (distancia al compañero más cercano) |
| ¿Atacan cuando el jefe es inmune? | `hitsOnRed` y `wastedHits` (golpes anulados por las reglas del jefe) |
| ¿Alguien atrae al jefe mientras los demás pegan? | `aggroShield`, `aggroOther`, `aggroSwitches` |

Criterios de David para el cociente de batalla, como experimentos del laboratorio C:
1. **Distancia y posición:** esquivar de lado cuando un dragón carga el aliento o usar el escudo en el momento justo;
   salir del radio de las pisadas de Tremorzilla.
2. **Armas y fases:** dejar de atacar y defenderse cuando un jefe de Cataclysm entra en una fase inmune; contra lo que
   vuela, pasar a armas a distancia en vez de saltar con la espada.
3. **Equipo y roles:** no amontonarse; que el de más vida y defensa atraiga al jefe mientras los demás flanquean;
   rodear en círculo amplio a Tremorzilla para que su rayo solo alcance a uno a la vez.

Cada criterio se convierte en una medida de arriba y en una regla candidata que se prueba encendida y apagada, como
el resto del manual. Claude puede añadir medidas nuevas cuando un resultado lo pida.


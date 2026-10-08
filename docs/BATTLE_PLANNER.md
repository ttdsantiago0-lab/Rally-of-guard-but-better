# Battle Planner: visión (v0, 2026-10-08)

Borrador ambicioso de Cowork para que David lo corrija. Parte de su jerarquía: **intenciones → maniobras → operaciones**.

## 0. Idea en una frase
Un **mapa táctico** donde el jugador (o el superintendente) no mueve guardias: **escribe una operación**. Las fuerzas la ejecutan, informan, y el plan se ajusta en vivo.

## 1. Niveles (lo que cada uno decide)
| Nivel | Quién | Recibe | Decide | Ejemplo |
|---|---|---|---|---|
| Intención | guardia | un objetivo, un puesto, una orden de grupo | cómo llegar y pelear con su arma/personalidad | "ve ahí", "defiende a ese aldeano" |
| Maniobra | **escuadrón / subgrupo (capitán)** | una maniobra | su formación, estilo de fuego, ritmo | arqueros en volea e implacables; avance por turnos |
| Operación | **fuerza (general)** | un plan con doctrina | cómo se reparten los subgrupos | mantener un muro: tanques al frente, arqueros quietos detrás |
| Campaña | **superintendente / jugador** | objetivos y recursos | qué fuerzas, a dónde, con qué logística | defender dos aldeas a la vez, mandar refuerzos |

**Fuerza** = escuadrones con subgrupos creados. El **general** (capacidad de mando más alta de la fuerza) se calcula, no se crea. El **superintendente** es opcional y único, y nunca es necesario para que algo funcione.

## 2. Qué ve el jugador
- **Mapa cenital** del terreno cargado (altura y bloques), con **escuadrones, HQs, rutas y lo que se sabe del enemigo**. Lo desconocido está oscurecido (**niebla de información**); los scouts lo irán revelando.
- Panel de la fuerza: composición (cuerpo a cuerpo, arqueros, tiradores, montados), estado (vida, munición, moral), general y capitanes.
- Teclas: `G`+número selecciona fuerza; atajos de operación: **retirada, retroceder atacando, mantener, avanzar**.

## 3. Anatomía de un plan
1. **Objetivo**: mantener un punto, atacar una zona o un grupo enemigo, escoltar a alguien, patrullar una ruta, emboscar, repliegue.
2. **Papeles**: yunque, martillo, pantalla, apoyo, flanco, reserva. Se asignan por composición (el general propone, el jugador corrige).
3. **Doctrina** por fuerza (Hostigar, Evitar bajas, Distraer, Ataque total, Mantener posición, Emboscar…), con sus pesos de decisión y los límites de retirada.
4. **Fases**: lista corta de pasos ("1 avanzar a la loma; 2 apoyo en quietos; 3 flanqueo"), cada una con su **disparador** (tiempo, bajas > X %, vista del enemigo, informe del scout, señal de redstone).
5. **Contingencias**: "si el líder cae → sucesión", "si bajas > 40 % → repliegue al HQ", "si llega refuerzo → entra por el flanco".

## 4. Ejecución
- El plan se **compila a directivas por escuadrón** (un registro versionado por escuadrón, no un mensaje por guardia: es lo que lo hace escalar a cientos).
- El **general** interpreta la doctrina; los **capitanes** traducen a maniobras; los soldados las resuelven con su veteranía (`DecisionLadder`).
- **Informes hacia arriba** (nuevo bus): el escuadrón informa estado/enemigo visto una vez por cambio; el general los resume; el mapa se actualiza.

## 5. Lo que lo hace único
- **Ensayo**: antes de ejecutar, un modelo agregado barato (el mismo del estado resumido de escuadrones lejanos) estima ventaja y bajas, y marca los riesgos del plan.
- **Biblioteca de planes**: plantillas (defender aldea, emboscada, asedio, escolta, expedición) y planes propios guardados y compartibles.
- **Modo autónomo**: si el jugador no está, una fuerza ejecuta sus **órdenes permanentes** (defender A, la reserva de B acude, botón de auxilio → señal de redstone).
- **Aprende de las simulaciones**: cada doctrina y plantilla se calibra en la plataforma de simulación; los datos deciden los pesos, no la intuición.

## 6. Qué entra y cuándo
| Versión | Contenido |
|---|---|
| **v1 (E3A)** | Fuerzas (escuadrones + subgrupos) con general calculado; planner con mapa, selección, objetivos básicos, doctrina y papeles; fases lineales con disparadores simples; directivas por escuadrón; informes de estado; teclas de grupo |
| v2 | Niebla de información con scouts; ensayo del plan; plantillas; condiciones y contingencias |
| v3 | Superintendente; logística y transporte (incluso mar); modo autónomo; asesinos y recon como roles |

## 7. Qué se descarta del panel actual cuando exista
Avanzar (C-06), Mantener (C-02) y las acciones de estilo individual pasan al planner; el panel individual queda mínimo (ficha, montar/desmontar, mochila).

## 8. Ítem y centro de operaciones (decisión de David, 2026-10-08)
El Battle Planner es un **pliego o mapa** que se **coloca en el centro de una mesa** dentro de un **centro de operaciones**: en la aldea, o en una **carpa pequeña** en campaña. Al hacer clic se abre el panel de estrategia. Debe estar dentro de una zona de HQ (la carpa cuenta como un HQ pequeño). El **Pergamino de Reunión** pasa a ser ese ítem (borrador). El mapa se dibuja por código; si en el futuro se integra el mapa de Xaero, será opcional.

## 9. Órdenes entre grupos (vínculos)
Un vínculo es `{tipo, origen, destino, disparador, parámetros}`. Catálogo propuesto:
| Vínculo | Qué hace |
|---|---|
| **Relevo** | si el grupo X cae por debajo de N % o desaparece, Y ocupa su puesto y papel |
| **Mantener hasta el relevo** | X no se retira hasta que Y llegue |
| **Cobertura** | Y cubre con fuego el movimiento o la retirada de X |
| **Retirada escalonada** | A cubre a B, luego B cubre a A |
| **Avance por turnos** | uno avanza mientras el otro cubre |
| **Refuerzo** | si X está en contacto más de T s, Y se une |
| **Enlace** | reunirse en un punto a una hora o señal |
| **Pantalla** | Y protege el frente/flanco de X |
| **Escolta** | Y acompaña a un grupo (ingenieros, heridos, suministros) |
| **Pinza** | dos grupos atacan a la vez desde lados opuestos cuando ambos están en posición |
| **Reserva** | un grupo espera hasta que lo libera un disparador |
| **Persecución limitada** | persigue hasta una distancia o tiempo y regresa |
| **Prioridad de objetivos** | reparto de objetivos entre grupos para no concentrar todo en uno |
| **Señal** | disparador por redstone, scout, bajas o tiempo |
v1 implementa los ocho primeros; el resto después.

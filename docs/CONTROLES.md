# Controles y jerarquía de órdenes (estado real, 2026-10-08)

Fuente: `CommanderHotkeys`, `GuardModes`, `CommandTargeting` y los textos del juego en `34b4c95`. **No se ha probado todo en juego**; donde dudo lo marco.

## 1. Teclas

| Entrada | Sobre qué apuntas (hasta 64 bloques) | Efecto |
|---|---|---|
| **G** | un enemigo | **ATACAR** (objetivo designado, igual que el botón Atacar) |
| **G** | un aldeano, un amigo, tu caballo, otro jugador que no te ataca | **DEFENDER** (se quedan a su lado y pelean contra lo que lo amenace) |
| **G** | uno de tus guardias | **Selecciona solo ese guardia** (la siguiente G le habla a él) |
| **G** | un bloque | Ir y mantener ese sitio, en la última formación usada |
| **G** | el cielo / nada | **SEGUIR** (follow) |
| **Agachado + G** | uno de tus guardias | Añadir/quitar de la selección |
| **Agachado + G** | una montura libre (caballo domado, criatura montable) | el guardia a tus órdenes más cercano **sin montura** la monta |
| **Agachado + G** | una montura que ya lleva a uno de tus guardias | ese guardia **desmonta** |
| **Agachado + G** | un bloque | **Marcar el punto** (ancla de Formar/Retirada) |
| **Agachado + G** | nada | Borrar la selección |
| **H** | — | **Mantener fuego ⇄ Fuego a discreción** |
| **Agachado + H** | — | **RETIRADA** al punto marcado (o hacia ti) manteniendo la formación |
| **J** | — | Abrir el Libro de Mando |
| **Libro de Mando, clic derecho** | — | Abre el panel |
| **Libro de Mando, agachado + clic derecho** | un ser | Marcar objetivo |
| | un bloque | Marcar punto de formación |
| | el aire | Borrar marcas |
| **Agachado + comando sobre un estandarte** | con un escuadrón a tus órdenes | Ese estandarte pasa a ser el **HQ** del escuadrón |
| **Pergamino de Reunión, agachado + clic derecho** | — | Alterna "reunir": agrupa a los guardias propios cercanos en abanico a tu alrededor y los pone a seguirte; otra vez los libera |
| **Pergamino sobre una mesa** (mesa de trabajo, cartografía, flechero, herrería, atril) dentro de la zona de un HQ (E3A) | — | Lo deja sobre la mesa y abre el **Battle Planner**; clic sobre el pliego lo vuelve a abrir |
| **G + número (1–9)** (E3A) | — | Selecciona tu **fuerza N** (sus guardias pasan a ser la selección); la barra de objetos no cambia |
| **G + agachado + número** (E3A) | — | Maniobra de la fuerza activa: 1 mantener la línea · 2 retroceder atacando · 3 avance por turnos · 4 flanqueo · 5 repliegue ordenado al HQ |

Desde E3A la orden de **G** se envía al **soltar** la tecla (así G puede combinarse con números).

**Quién obedece**: la selección actual (Libro de Mando o Agachado + G). Si no hay selección, los guardias que te siguen a ≤64 bloques. **Nunca "todos"**: aldeas lejanas no se tocan.

## 2. Teclas ↔ Libro de Mando: no hay jerarquía entre ellas
Las teclas son un control remoto: cada verbo es una orden que ya existe (Atacar, Formar, Seguir, Retirada, Mantener/Soltar fuego) o una guarda (DEFENDER). **La última orden escrita gana**, venga de G, del Libro, o del menú nativo de Guard Villagers (el juego lo reconcilia en ~1 s).

## 3. Jerarquía entre instrucciones (capas, de arriba abajo)

| Capa | Qué decide | Elementos | Regla |
|---|---|---|---|
| 1. Quién | quién recibe la orden | selección, seguidores a ≤64 | ver arriba |
| 2. Dónde (modo) | dónde quiere estar | **LIBRE** (patrulla/roam), **SEGUIR**, **PUESTO** (mantiene un punto) | Solo las órdenes cambian de modo. Formar, Mantener, Avanzar, Reunir, Puesto = PUESTO; Seguir = SEGUIR; **Romper filas = LIBRE** |
| 3. Si combate (puertas) | si puede pelear | **Mantener fuego** > **Pasiva** > resto | Mantener fuego: ni siquiera responden (marcan el objetivo y esperan). Pasiva: no inicia; una orden de Atacar explícita sí se cumple (según el texto del juego) |
| 4. A quién | a quién ataca | objetivo designado (Atacar / G) > contraataque a quien lo golpea > postura (Defensiva, Agresiva, Implacable) > cupo de combate (`Engagement`) | Un guardia en PUESTO solo atiende lo que esté a ≤16 bloques de su puesto y vuelve al terminar |
| 5. Cómo | cómo pelea | estilo de fuego (arqueros/tiradores), doctrina, escalera de decisión (cobertura, rango, prioridad de amenaza) | desde E2A; sin probar |
| 6. Cuándo huye | cuándo se repliega | `RetreatPlanner`: vida baja → punto marcado, si no HQ del escuadrón, si no te sigue | |

Ejemplos de lo que preguntaste:
- **Puesto + Implacable**: ataca a cualquier criatura (no a jugadores ni a tus guardias ni mascotas), **pero solo dentro de 16 bloques de su puesto**, y vuelve a él.
- **Romper filas vs patrulla nativa de Guard Villagers**: *no son lo mismo*. La patrulla/puesto nativo fija un punto (= nuestro PUESTO). **Romper filas = LIBRE**: sin puesto ni formación, actúa según su postura.
- **Pasiva**: hoy no se defiende. Tú pediste que al menos intente defenderse: es un cambio pendiente (ver `DECISIONES_DAVID_2026-10-08.md`).

## 4. Preguntas abiertas de jerarquía
1. Cuando un **capitán** emite una directiva de escuadrón y el jugador da una orden individual, ¿quién gana? Hoy debería ganar la última escrita; **no está verificado ni documentado**. E3A debe probarlo en simulación y fijarlo en `docs/ORDENES_JERARQUIA.md`.
2. Postura Pasiva con orden de Atacar y Mantener fuego a la vez.

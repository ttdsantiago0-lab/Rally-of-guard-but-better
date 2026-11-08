# Tributo: el recurso estratégico

Diseño v0 · 2026-11-07. Fuente de verdad del tributo. Decisiones de David marcadas **(David)**; lo demás son
propuestas o hipótesis que se prueban en la etapa 5 de `docs/PLAN_MAESTRO.md`. Ninguna cifra es definitiva hasta
medirla.

## 1. Lo decidido

- **(David, 2026-11-06)** Solo lo generan aldeanos protegidos. No da poderes al jugador: hace que el arma, la armadura
  o la montura **de un guardia** gane las estadísticas únicas de su maestría (consagración). La veteranía, la moral y
  los vínculos siguen cambiando solo decisiones.
- **(David, 2026-11-07)** Es el recurso que impulsa todo lo demás: los intercambios en los mercados de las aldeas se
  hacen con él, y también contratos, mercenarios, consagración, maestría, monturas y munición mejorada.
- **(David)** Se transporta a mano y cuesta transportarlo: no se guarda fácil en una mochila. Grandes cantidades van en
  bloques o cargas, en carretas tiradas por caballos, barcos, trenes o aeronaves. Se procesa en origen y en destino.
- **(David)** Incita rutas de mercado. Una carga grande es valiosísima: si te la roba una guerrilla u otro jugador, te
  afecta.
- **(David)** Sin chunks cargados: la caravana se simula y, si un jugador pasa por donde debería estar, se la encuentra.
- **(David)** Nunca se consigue una vez y ya: se consume por uso. Ejemplo: las balas de un arma de Scorched Guns pegan
  mucho más **solo si esa arma está amaestrada**, y aun así cada bala gasta tributo.
- **(David, 2026-11-07)** **El tributo no entra en mochilas** ("hay que mantenerlo balanceado"): ver §3.
- **(David, 2026-11-07)** **Los jugadores también pueden consagrar** (cambia lo del 2026-11-06, "no da poderes al
  jugador"): para el jugador es un gran impulso pero muy caro, con mantenimiento periódico. Lo consagrado de los
  guardias no paga mantenimiento, o muy rara vez.
- **(David)** Hipótesis de escala (para probar, no fijas): 7 por stack; unas mochilas llenas = 200–300; equipar del
  todo a un soldado ≈ 50; con mochilas no compensa abastecer a más de ~10 guardias; un ejército con veteranos serios
  necesita cargas.

## 2. Estados: dos físicos y un registro

ChatGPT propuso cuatro (producido, acuñado, carga sellada, reserva). Con dos físicos basta para dar todas las
decisiones que buscas; el cuarto es solo un número.

| Estado | Qué es | Cómo se mueve |
|---|---|---|
| **Tributo** (ítem) | La unidad. Stack 7 (componente `max_stack_size` de 1.21) | En el inventario del jugador o del guardia. No entra en shulkers ni bolsas (`canFitInsideContainerItems = false`, que vanilla respeta) |
| **Carga sellada** (bloque) | Una caja con N tributos (hipótesis: 49 = 7 × 7). Se sella en un mercado y tarda | Nunca como ítem. Si la rompes sin más, se abre y el tributo cae al suelo. Intacta solo se mueve a pulso con Carry On (una a la vez, lento), en artefactos de Create (trenes, carretas de Create, aeronaves de Create Aeronautics) o en las carretas/caravanas de Rally |
| **Reserva** (registro) | Lo que un mercado tiene a tu nombre, guardado en el mundo (`SavedData`) | No se mueve: se gasta ahí (consagrar, contratos) o se retira como ítems o cargas |

"Procesar en ambos sitios": sellar en el mercado de origen (tiempo + el mercado tiene que existir y estar protegido) y
registrar en el de destino. Entre medias, la carga es vulnerable: ese es el juego.

## 3. Mochilas (decidido: no entra)

En tu modpack está Sophisticated Backpacks, con mejoras de stack (hasta 3 por mochila en tu configuración): con ellas
una mochila guardaría miles de tributos y el problema logístico desaparece. Su configuración de servidor tiene una lista
`disallowedItems` (leído en `config/sophisticatedbackpacks-server.toml` de tu perfil); Rally la rellenaría al instalarse.

**Decidido (David, 2026-11-07): el tributo no entra en mochilas.** Así, el tope de un jugador es su inventario (36 × 7 = 252, justo tus
200–300) más el cofre de Ender (27 × 7 = 189). Equipar a unos 5 guardias a pulso; más, con cargas.

## 4. En qué se gasta

| Uso | Forma | Hipótesis |
|---|---|---|
| Consagrar arma, armadura, escudo o montura de un guardia | una vez, alto | ~50 por soldado completo (tu ejemplo) |
| Maestría del arma consagrada | progresiva, por nivel | se mide en campaña |
| Munición mejorada (balas de un arma amaestrada) | por disparo | en **subunidades** enteras (1 tributo = 100 partes) que salen de la mochila del guardia; sin tributo, el arma dispara normal, nunca se rompe |
| Conexión máxima con la montura | desarrollo + mantenimiento | se mide |
| Contratos y mercenarios | por contrato | etapa 7 |
| Intercambios en el mercado | precio en tributo | se mide |
| Mantenimiento periódico de lo consagrado | **jugador: sí, caro; guardia: no, o muy rara vez** (David, 2026-11-07) | la cifra se mide |

## 5. Producción (hipótesis para medir)

Producción de una aldea por día de juego = f(aldeanos vivos, guarnición presente, días sin incidentes, mercado y
estandarte). Una aldea saqueada o sin estandarte deja de producir. Rendimientos decrecientes por aldea, para que una
granja de aldeanos encerrados no sea una mina.

## 6. Anti-trampas (lo que la auditoría de la etapa 5 debe comprobar)

- No se fabrica ni sale de ninguna receta, trituradora o fabricador mecánico de Create.
- Una carga no se duplica al moverla (Carry On, artefactos de Create, materializar una caravana).
- Las granjas de aldeanos sin protección real no producen.
- El cofre de Ender no se convierte en teletransporte de grandes cantidades (tope de 189 por jugador).

## 7. Caravanas (etapa 6)

Un registro con identificador, origen, destino, ruta (puntos), progreso, velocidad, carga, escolta, dueño, riesgo y
estado. La posición se calcula con el tiempo. Cuando un jugador entra en su radio (hipótesis ~96 bloques) se crean las
entidades en el punto calculado del camino, en terreno ya cargado; cuando se aleja, se guardan y vuelve a ser registro.
Un ataque cuando nadie mira se resuelve con las tablas de simulación (composición de la escolta × atacante → ganar,
bajas, carga perdida). Sin tickets de chunk.

## 8. Cómo sabremos que funciona

Prueba de decisión (tú, en el juego): mover tributo de una aldea a otra te obliga a elegir ruta, escolta o cuánto
llevar. Si no te hace decidir nada, se rediseña antes de conectarle más cosas.

## 9. Pendiente de David
- ¿Un guardia enemigo puede soltar un arma consagrada? Propuesta de Claude: suelta el arma **sin** la consagración (o
  la conserva con probabilidad muy baja), para que asaltar facciones dé botín sin saltarse la economía del tributo.

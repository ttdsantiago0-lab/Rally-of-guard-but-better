# Rally of the Guard — Veteranía, liderazgo y "soldados entrenados" (propuesta, 2026-10-04)

**Estado: propuesta para discutir. Nada de esto está implementado**, salvo lo marcado
**Hecho (3H.1)**. El usuario decidió que **la veteranía va antes que las clases** y que
**el ítem determina la clase**, no botones nuevos.

## 0. El problema que resuelve

La veteranía tiene que:

- hacer que un guardia que sobrevivió cien batallas **se sienta distinto**: formidable,
  capaz de matar a un jugador, nunca de un solo golpe;
- crear historias ("el último de Pueblo1", "el tanque que volvió de la muerte cinco
  veces");
- ser **difícil de conseguir**, sin que entrenar sea repetir lo mismo;
- **no poder romperse**: ni con cofres llenos de tótems, ni con granjas de XP, ni con 10 000
  guardias.

Objetivo de diseño del usuario: *"sin importar qué tan poderoso sea un jugador, no sea
posible enfrentarse a un ejército grande"*.

## 1. Tres capas, en este orden

```
Genética (quién es)  ─┐
                       ├─► Rasgos (qué se le da bien)  ─► Comportamiento (cómo pelea)
Historia (qué vivió) ─┘                                    + Liderazgo (cómo ordena)
```

1. **Genética (al nacer, fija):**
   - Si MCA Reborn está instalado: su personalidad y sus rasgos. Verificado en el
     código fuente de MCA, rama 1.20.1:
     - Personalidades: ATHLETIC, CONFIDENT, FRIENDLY, FLIRTY, WITTY, SHY, GLOOMY,
       SENSITIVE, GREEDY, ODD, LAZY, GRUMPY, PEPPY.
     - Rasgos: LEFT_HANDED, WEAK, TOUGH, DWARFISM, ALBINISM, entre otros.
   - Sin MCA: Rally tira 1–2 afinidades al azar con la misma lista corta.
   - La genética **sesga**, no decide:
     - un ATHLETIC aprende antes a explorador o caballería;
     - un CONFIDENT, el asalto;
     - un TOUGH, a tanque;
     - un GLOOMY o un SHY huyen antes.
2. **Historia (lo que vive, acumulativo):** contadores baratos por guardia, guardados por
   UUID igual que hoy los registros:
   - bajas por tipo de arma (espada, arco, arma de fuego);
   - veces que estuvo por debajo de 3 corazones y sobrevivió;
   - batallas sobrevividas y horas junto a su comandante;
   - tótems usados;
   - escuadras que lideró.
3. **Rasgos (resultado):**
   - Cada rango (1–5) desbloquea **un rasgo** elegido por la combinación de genética e
     historia, no por azar puro. Por eso dos veteranos no son iguales.
   - Ejemplos:
     - **Tanque:** salió vivo de muchas veces casi muerto (+corazones, se pone tótems
       más rápido).
     - **Tirador:** muchas bajas a distancia (+daño y alcance con *esa* clase de arma).
     - **Duelista:** muchas bajas cuerpo a cuerpo (esquiva, contraataca).
     - **Jinete:** muchas horas montado.
     - **Sereno:** sobrevivió a muchas batallas perdidas (no rompe la formación).
   - **Sigue escalando después del rango 5** con rendimientos decrecientes. "Que pueda
     escalar aún después de llegar a veterano", pero sin volverse infinito.

## 2. El salto de IA (solo en el rango máximo)

Decisión del usuario: el salto *perceptible* de inteligencia solo llega al **nivel más
alto** y debe estar **balanceado y sin depender de otros mods**. Propuesta: Rally trae
sus propios comportamientos, **inspirados** en IAs que ya funcionan, nunca copiados ni
requeridos:

| Inspiración | Qué se toma | Para qué rasgo |
|---|---|---|
| Dragones de Ice and Fire (`fire_dragon` y compañía) | Elegir la posición antes de atacar, atacar desde la altura, retirarse para reposicionar | Tirador/francotirador: busca altura y línea de visión antes de disparar |
| Dread Knight / Dread Thrall de Ice and Fire (los "undead guards" cuerpo a cuerpo que recordabas; nombres verificados en el código) | Presión constante cuerpo a cuerpo, con escudo | Duelista y tanque |
| Mobs de Scorched Guns | Ráfagas, cobertura, distancia de combate | Ya en uso (3H) para quien lleva arma de fuego |
| Tácticas reales | Fuego y movimiento: unos cubren, otros avanzan | Líderes (§3) |

Objetivo explícito: que **un mago de Ars Nouveau volando se sienta en peligro**. Los
tiradores veteranos priorizan objetivos voladores y buscan línea de visión; los que solo
tienen cuerpo a cuerpo buscan cobertura en vez de quedarse mirando.

## 3. Liderazgo: escuadras que piensan

**Hecho (3H.1), el primer ladrillo:** el tope de combatientes cuerpo a cuerpo por
objetivo (`order/Engagement`).

- Ya no se amontonan 10 espadas sobre un zombi bebé.
- Entran tantos como caben alrededor (3 en un zombi bebé, 8 en un ravager).
- El resto hace pantalla o busca otro enemigo.
- Los tiradores no tienen tope.

Propuesta en capas, cada una barata:

1. **Líder = el más veterano de la escuadra** (hueco 0 de su formación). Sin líder, la
   escuadra pelea como hoy.
2. **Información compartida:** lo que ve un miembro lo "sabe" la escuadra, por ejemplo
   "hay enemigos voladores" o "estamos perdiendo". La escuadra mantiene un pequeño
   *cuadro de situación* (enemigos conocidos, bajas, cobertura cercana) que el líder
   actualiza una vez por segundo. Así se escala: **un** escaneo por escuadra, no uno por
   guardia. Un **explorador** comparte el suyo con todas las escuadras del comandante.
3. **Decisiones del líder** (reglas simples, una por situación, solo con un líder
   veterano):
   - **Enemigo volador o en altura:** los que tienen arma a distancia disparan; los de
     cuerpo a cuerpo buscan cobertura.
   - **Pocas bajas y un enemigo fuerte:** "todos a distancia" (cambian al arma
     secundaria cuando exista la mochila, §5).
   - **Por debajo del X % de vida total:** repliegue a cobertura (cueva, muro) y pelean
     desde ahí.
   - **El enemigo se alejó rápido:** reorganizarse (Reformar automático).
   - **Zombi bebé:** solo entran los que tienen armadura; los desarmados retroceden. Así
     tres veteranos pierden a lo sumo uno.
4. **Sucesión de mando** (idea 25 del usuario):
   - Si cae el líder, manda el siguiente más veterano.
   - Como máximo **3 relevos (4 en ejércitos de miles)**; después la escuadra se
     desarticula: rompe filas, cae la moral y algunos huyen.
   - Después de la batalla, si murieron dos miembros de una escuadra, se asigna un
     nuevo líder y su veteranía influye **gradualmente** en el comportamiento del grupo.

## 4. Anti-abuso (robustez)

- **Tótems:** un guardia no se pone tótems al instante.
  - Necesita tiempo de equipar, y el rasgo Tanque lo acorta.
  - Si "revive" varias veces seguidas, puede entrar en **modo kamikaze**: impredecible,
    para que los jugadores lo respeten.
- **Granjas de XP:**
  - La experiencia de veteranía solo cuenta en combate real: enemigo que hizo daño,
    pelea de más de N segundos, variedad de enemigos.
  - Rendimientos decrecientes por tipo de enemigo repetido. Entrenar no puede ser
    repetir lo mismo.
- **Muñeco de práctica en el cuartel (idea del usuario):** descubre afinidades y lleva al
  rango 1, **nunca más allá**. Lo demás se gana en batalla.
- **Escala:** contadores por UUID y decisiones por escuadra (una vez por segundo), nunca
  por guardia y por tick.

## 5. Inventario: la "mochila"

Guard Villagers da 6 espacios por guardia: 4 de armadura y 2 manos (verificado en su
código). Por eso hoy la munición va en la mano secundaria (3H.1). Propuesta: una
**mochila de Rally** por guardia (un contenedor propio guardado por UUID), con:

- arma secundaria (cambio automático: sin munición o enemigo cerca → espada);
- munición en pilas mayores de 64 según el tipo;
- manzanas doradas o pociones (las come con poca vida);
- armaduras de repuesto (la "maleta" del veterano);
- dinamita para el bombardero.

El tamaño crece con la veteranía, y las **cajas de munición** en burros o mulas amplían
la logística lejos de la base. **Siguiente paso** recomendado tras la veteranía.

## 6. Clases = ítem (decisión del usuario)

| Ítem en la mano | Clase | Lo que desbloquea la veteranía |
|---|---|---|
| Espada / hacha | Infantería | Duelista, tanque |
| Arco / ballesta / arma de fuego | Tirador | Francotirador (altura, prioridad a voladores) |
| Dinamita de Lucky TNT (decisión del usuario) | Bombardero | Lanzamientos más precisos; nunca cerca de aliados |
| Libro de hechizos de Ars Nouveau | Mago | 1 hechizo; el más avanzado, hasta 3. Soporte: curación en grupo |
| Pala / herramientas | Ingeniero | Novato: cavar trincheras. Experto: sacos de arena, cobertura. Máximo: radar o escudo de otro mod. **Nunca casas ni murallas** (decisión del usuario) |
| Catalejo | Explorador | Comparte el cuadro de situación (§3.2) |

Encantamientos en arcos y otras armas: se usan tal cual (el arma es del jugador). La
veteranía no crea hechizos ni encantamientos; solo usa mejor lo que tiene.

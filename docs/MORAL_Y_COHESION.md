# MORAL_Y_COHESION — diseño (2026-10-09)

Base: el sistema de moral de Total War (manual oficial de Rome II: presencia del general y de héroes, bajas, fatiga, combates largos, superioridad numérica enemiga, muerte del general, estados saludable → penalizada → vacilante → rota → en fuga → destrozada, reagrupación por el general) y el análisis histórico de moral frente a cohesión de A Collection of Unmitigated Pedantry ("Total Generalship", parte IIIc: moral y cohesión son cosas distintas; el choque de contacto rompe unidades antes del combate; las bajas rompen a ~10 % en muchos ejércitos antiguos; la mayoría de las muertes ocurre en la persecución; el líder visible permite reagrupar; la contagiosidad rompe ejércitos desmoralizados; una unidad cohesionada pero sin fe puede negarse a atacar sin huir). Todo lo de abajo está escrito con nuestras palabras y adaptado a Minecraft.

## 1. Principios (firmes salvo lo marcado)
1. **La moral cambia decisiones, nunca estadísticas.** No da ni quita vida, daño ni velocidad (la huida usa la velocidad normal). Es otra forma de "calidad de decisión", igual que el rango.
2. **Moral y cohesión son dos medidas separadas.** *Moral* (por guardia, 0–100): ganas de seguir luchando. *Cohesión* (por unidad, 0–100): cuánto se mantiene unida como unidad (huecos en la formación, capitán vivo, tiempo juntos, composición coherente — ver `GRUPOS_Y_SUBGRUPOS.md`). Una unidad puede estar cohesionada y desmoralizada (aguanta en su sitio pero se niega a atacar) o desmoralizada y dispersa (huye).
3. **Nunca debe sentirse injusto ni borrar al jugador del juego.** Configuración `morale.enabled` y `morale.strength` (0 = solo se mide y se muestra, 1 = efecto completo; por defecto 0,6). Un guardia en fuga **no pierde** equipo ni se despide; vuelve al HQ o al capitán y se reagrupa.
4. **Barato:** moral del guardia actualizada por eventos (daño recibido, muerte cercana, llegada/salida del capitán) y como máximo 1 vez por segundo, escalonada; cohesión por unidad 1 vez por segundo en el turno del capitán. Cero escaneos por tick.

## 2. Factores
**Bajan la moral (pesos en config):** bajas de la unidad (el efecto crece con fuerza entre el 10 % y el 30 % de bajas, no es lineal); muerte de un compañero cercano (más si era el capitán o el general; el efecto cae con la distancia y se acumula con rendimientos decrecientes); estar en inferioridad numérica local (proporción de enemigos conocidos vs. aliados cercanos); ser golpeado por la espalda o el flanco (daño recibido fuera del arco frontal); combate cuerpo a cuerpo prolongado sin respiro (fatiga: segundos en contacto con poca vida); vida baja; aislamiento (ningún aliado en N bloques); amenaza fuera de escala (jefe, dragón, explosión grande: según `Threat` de `veterancy/Threat`); no tener capitán ni general vivo; órdenes de retirada imposibles de cumplir (cercado); ver huir a vecinos (**contagio**, acotado y con rendimientos decrecientes).
**Suben la moral:** presencia del capitán o del general en un radio (proporcional a su Puntaje de liderazgo); estar cerca del estandarte/HQ; rango alto y rasgos (Firme y Tanque reducen sensibilidad; ver §4); momentum (bajas causadas recientes, victorias de la unidad); refuerzos que llegan; terreno ventajoso (cobertura, altura, `TerrainSense`); formación íntegra; descanso (estado "Relajados" en HQ recupera, 1 punto cada pocos segundos).

## 3. Estados y qué cambia en cada uno
| Estado | Moral | Efecto en la decisión (solo preferencias) |
|---|---|---|
| FIRME | ≥ 60 | Normal. |
| INQUIETO | 40–60 | Sube `retreatHealth` un poco; prefiere distancia; evita cargar; mantiene el sitio. |
| VACILANTE | 20–40 | Rompe al contacto con probabilidad alta salvo que el capitán esté en radio; no inicia ataques; si la unidad es cohesionada "se niega a atacar" pero no huye (mantiene posición). |
| ROTO | < 20 | Deja de combatir salvo si está acorralado; huye al capitán/HQ por la ruta más segura (`RetreatPlanner`); suelta la formación. |
| DESTROZADO | roto + sin capitán ni general + bajas > 50 % | No responde a reagrupar en esta batalla; solo se recupera descansando en el HQ ≥ `morale.shatteredRestSeconds`. |
Entre estados hay **histéresis** (entrar a 40, volver a FIRME en 55; igual en los demás) y duración mínima, para que no oscilen.

## 4. Veteranía y moral (la veteranía es el centro del mod)
La sensibilidad a los factores negativos se multiplica por `moraleSensitivity(rank)` = 1,00 / 0,90 / 0,80 / 0,68 / 0,55 / 0,45 para los rangos 0–5 (tabla en config) y por 0,85 con rasgo Firme o Tanque. **Los rangos 4–5 en radio pequeño dan "ejemplo"**: suben el valor base de moral de sus vecinos (acotado a +10 y con rendimientos decrecientes). Es la forma de que "un legendario cambia el ánimo de su unidad" sin dar números de combate. Los guardias de rango 0 sin capitán son los primeros en romperse.

## 5. Reagrupar (sin botones nuevos)
- Un guardia ROTO o VACILANTE que ve a su capitán o al estandarte a ≤ `morale.rallyRadius` (16) y cuya moral supera el umbral de reagrupar vuelve a la unidad (el capitán **puede mejorar la velocidad del reagrupar** según su puntaje de liderazgo).
- El jugador puede acelerarlo con lo que ya existe: Pergamino de Reunión / Reunir (planner plantilla RALLY) sobre la unidad rota.
- Los DESTROZADOS ignoran todo hasta descansar en el HQ.

## 6. Persecución
Los guardias de rango ≥ 3 no se dispersan persiguiendo (ya existe `Leadership.pursuitFactor`). **Nuevo:** si el enemigo es una facción de guardias (simulación o futuros bandos) y un enemigo está ROTO, el bando que gana causa la mayoría de las bajas en persecución — la simulación lo mide ("bajas en persecución") para comprobar que la moral crea fases de batalla reales.

## 7. Integración con lo que ya existe
- `command/CaptainSuccession` ya publica `SquadDirectives.Kind.ROUTED` cuando una unidad pierde más capitanes de los que puede reponer: pasa a ser **el estado ROTO de la unidad**, no un caso especial.
- `force/SituationReports.Report` añade `morale` (media), `cohesion`, `routed` (conteo) — se publican solo cuando cambian.
- `force/ForceRules`: nuevas métricas de regla `UNIT_MORALE_BELOW` y `UNIT_ROUTED`; Relevo/Refuerzo/Retirada escalonada pueden dispararse con ellas (con histéresis ya existente).
- `tactics/DecisionReason`: añadir `MORALE_SHAKEN`, `MORALE_WAVERING`, `ROUTED_FLEE`, `RALLIED`, `REFUSE_ATTACK`.
- Ficha del guardia y tarjeta de grupo: estado de moral con color (verde/amarillo/naranja/rojo) y la causa principal ("sin capitán", "flanqueados", "bajas 23 %").

## 8. Cómo se mide (simulación)
Escenarios `morale-*`: (a) misma unidad con capitán vivo vs. capitán muerto al segundo 10; (b) rango 0 vs. rango 5 con las mismas bajas; (c) ataque por la espalda vs. frontal; (d) contagio (un vecino huye); (e) refuerzos que llegan; (f) reagrupar tras ROTO con el capitán visible; (g) DESTROZADO nunca vuelve al combate antes de descansar. Métricas: tiempo hasta primer ROTO, % de la unidad ROTA a los 60 s, tiempo de reagrupar, bajas en persecución, **y siempre**: la moral no cambia vida/daño/velocidad (comprobación automática de igualdad de atributos). Alarma: si en un enfrentamiento parejo de rango 3+ con capitán más del 20 % de los guardias llegan a ROTO, el informe marca "moral demasiado sensible".

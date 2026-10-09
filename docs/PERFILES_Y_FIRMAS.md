# PERFILES_Y_FIRMAS — visión/alcance por papel, firmas de arma, amenazas y presencia (diseño, 2026-10-09)

Desarrolla `DECISIONES_DAVID_2026-10-09.md` D1–D3. Es **diseño firme en los principios y borrador en los números** (los números viven en JSON y se afinan con el simulador). Todo se decide por **eventos o a 1 Hz**, nunca por tick, y con presupuesto medido por `Perf`.

## 1. Perfil sensorial por papel (D1)

### 1.1 Dos números distintos
- **Visión** (`vision`): hasta dónde un guardia *detecta e informa* un objetivo. No es lo mismo que el atributo `FOLLOW_RANGE` de la IA (subirlo a 64+ en cientos de guardias dispara el coste del pathfinding). **Regla de implementación:** el `FOLLOW_RANGE` NO se toca; la visión larga se implementa como un **escaneo escalonado a 1 Hz con línea de vista** que solo hacen los guardias cuyo papel lo pide (explorador, francotirador, capitán), con un tope de escaneos por tick.
- **Alcance** (`reach`): hasta dónde puede **impactar**. Para arco/ballesta/arma de fuego sale de `Ballistics`/`WeaponProfile` (velocidad inicial, caída); para cuerpo a cuerpo es el alcance del arma y no se modifica.
- **Puntería** (`aim`): dispersión. Ya existe como efecto de maestría (`spread`); aquí se suma el aporte del papel.

### 1.2 Tabla de multiplicadores (BORRADOR; archivo `config/rallyguard-role-senses.json`, se escribe con estos valores si falta)
Cada fila: factor a rango 0 → factor a rango 5. Se interpola con la curva de rango existente. Todo se limita por `senses.maxFactor` (por papel).

| Papel | Visión | Alcance | Puntería (menos dispersión) | Notas |
|---|---|---|---|---|
| Cuerpo a cuerpo (espada, hacha, tridente en mano) | 1,00 → 1,20 | — (sin cambios) | — | Poca visión extra: el rango se nota en cómo decide, no en ver más. |
| Arquero / ballestero (rango normal) | 1,00 → 1,35 | 1,00 → 1,25 | 0 → −25 % | El +15 %/rango anterior queda como techo de este papel. |
| Pistolero | 1,00 → 1,30 | según arma (±10 %) | 0 → −20 % | El alcance real lo marca el arma de Scorched Guns. |
| **Francotirador** (papel especializado, §1.3) | 1,00 → **2,00** | 1,00 → **2,50** | 0 → **−60 %** | Exagerado a propósito (D1). Pero cadencia baja, exige línea de vista y quieto/apoyado. |
| **Explorador** (clase `RECON`) | 1,00 → **3,00** | 0,40 (fijo, pésimo) | −0 % | Ve lejos, **no pelea**: evita el contacto y publica avistamientos. |
| Caballería cargando | 1,00 → 1,20 | — | — | |
| Capitán / general | +10 % / +20 % de visión sobre su papel | — | — | Ven más porque reciben avistamientos de la unidad, no porque sean mejores ojos. |

### 1.3 Cómo se llega a ser francotirador (sin botón)
Papel **derivado**, nunca asignado a mano:
`francotirador = arma a distancia (o ítem marcado "sniper" en el JSON) + maestría de esa arma ≥ 0,5 + rango ≥ 2 + (rasgo Tirador o ≥ N bajas a distancia larga)`.
- Si el jugador pone un arma de largo alcance en manos de un guardia con ese historial, el sistema lo reconoce y los capitanes lo cuentan con la capacidad `LONG_RANGE_COUNTER` (§3).
- Un francotirador **no** es mejor en todo: cadencia ×0,5, penalización fuerte si se mueve, vulnerable de cerca (su `Doctrine` prefiere `HOLD_POSITION`/`AMBUSH`). La compensación es táctica, no de stats de vida/daño.

### 1.4 Escalado seguro (para no romper el rendimiento ni el equilibrio)
- `FOLLOW_RANGE` intacto. Visión larga = escaneo escalonado (máximo `senses.scansPerTick`, defecto 4) con raycast limitado.
- Alcance extra = más velocidad inicial del proyectil + compensación de caída ya calculada por `Ballistics`; fuera del alcance físico del proyectil el guardia no dispara (no gasta munición).
- **Alarma por papel:** el informe de la simulación marca como "posible inflado" solo comparaciones del **mismo papel** (R5 vs R0 del mismo papel). Un sniper venciendo a un meleé a 80 bloques no es alarma; un meleé R5 venciendo >85 % a un meleé R0 sí.

## 2. Firmas de arma (D2): el "salto" de veteranía

### 2.1 Idea
Una **firma** es un movimiento propio de un arma que solo ejecuta quien la domina. Es lo que hace que un guardia R4–R5 con su arco de toda la vida **se sienta distinto** de uno R2 con el mismo arco, sin darle vida ni daño base. Puede estar "un poco rota": se mide y se ajusta, pero **no se anula** por equilibrio fino.

### 2.2 Cuándo se desbloquea
`firma disponible = rango ≥ minRank (≥ 2) AND maestría(arma) ≥ minMastery`. Hay **tres escalones** por arma:
1. **Hábil** (rango ≥ 2, maestría ≥ 0,35) — versión pequeña.
2. **Experto** (rango ≥ 3, maestría ≥ 0,65) — versión notable.
3. **Maestro** (rango ≥ 4, maestría ≥ 0,90) — versión espectacular.

Un guardia puede tener maestría alta de un arma y rango bajo (aún sin firma), o rango alto con otra arma sin maestría (sin firma con ella). Eso conecta con la logística (E3C): cambiar de arma tiene un coste real.

### 2.3 Tipos de firma (lo que programa Claude Code; los datos van en JSON)
| Tipo | Qué hace | Arma típica |
|---|---|---|
| `MULTI_SHOT` | Dispara N proyectiles a la vez en abanico estrecho, más rápidos (`speedMul`), consumiendo `ammoCost` flechas | arco, ballesta |
| `PIERCING_VOLLEY` | La salva atraviesa una unidad de blancos (`pierce`), útil contra formaciones apretadas | arco, ballesta |
| `MARKED_SHOT` | Disparo certero a largo alcance: sin dispersión, velocidad alta, ignora el viento/caída; muy lento | francotirador, pistola de precisión |
| `BURST_CONTROL` | Ráfaga corta con retroceso compensado | armas de Scorched Guns |
| `CLEAVE` | Golpe en arco que alcanza a los adyacentes (`radius`, `maxTargets`) | hacha, espada pesada |
| `RIPOSTE` | Tras bloquear con escudo, contragolpe inmediato con empuje | espada + escudo |
| `LUNGE` | Embestida corta al objetivo (rompe distancia) | lanza, tridente |
| `RAPID_RELOAD` | Recarga casi instantánea una vez cada X segundos | ballesta |

Cada firma tiene: `cooldownTicks`, `triggerCondition` (p. ej. solo contra objetivo prioritario, solo con ≥2 enemigos a la vista, solo en línea de vista), `ammoCost`, y un `DecisionReason` propio (`SIGNATURE_MULTISHOT`, …). **Siempre se usa a través de los caminos existentes de disparo/ataque** (`RangedFire`, `ArrowFactory`, `MasteryEffects`): una firma nunca reemplaza la puerta de combate ni salta la regla de fuego amigo.

### 2.4 Tabla por defecto y ejemplos (BORRADOR; `config/rallyguard-weapon-signatures.json`)
Estructura: arquetipo genérico → firmas por escalón; más `overrides` por id de ítem de cualquier mod, sin dependencia de compilación (como en `rallyguard-weapon-mastery.json`).
- `bow`: Hábil = `MULTI_SHOT` 2 flechas (`speedMul` 1,15); Experto = 2 flechas (1,3) + `PIERCING_VOLLEY` contra formaciones; Maestro = 3 flechas (1,4).
- `crossbow`: Hábil = `RAPID_RELOAD`; Experto = `MULTI_SHOT` 2; Maestro = 3 + `PIERCING`.
- `gun`: `BURST_CONTROL` y, para armas marcadas `sniper`, `MARKED_SHOT`.
- `sword`+escudo: `RIPOSTE`; `axe`: `CLEAVE`; `trident`: `LUNGE`.
- **Override de ejemplo del autor:** `iceandfire:dragonbone_bow` (la clase `DragonBowItem` está en el jar instalado): Maestro = `MULTI_SHOT` 3 flechas, `speedMul` 1,6, `pierce` 1. Un "arma de leyenda" tiene firma propia más fuerte que el arco normal.
- Se añaden armas de otros mods **solo con filas de JSON** (no hay que tocar código). Claude Code no puede ver los jars: **no inventa ids**; deja un `overrides` de ejemplo con el id de arriba (verificado en el jar) y documenta cómo añadir más.

### 2.5 Medición
Escenarios `signature-bow`, `signature-axe`: mismo equipo, rango 2 vs rango 5 con firma. Se miden: uso de la firma, impactos por flecha, bajas del enemigo, munición gastada. **Aceptación:** la firma mejora ≥2 de las 7 métricas de excelencia sin quitar una; el coste de munición se informa. Las firmas están **exentas** de la alarma "R5 > 85 %" mientras el escenario no sea 1v1 con equipo idéntico sin firma.

## 3. Capitanes reconocen amenazas (el sniper contra el mago volador)

### 3.1 Clases de amenaza (en `SquadPicture` / `Tactics`)
`AERIAL_OUT_OF_REACH` (volador fuera del alcance de la mayoría), `CASTER_LONG_RANGE` (lanzador a distancia, p. ej. un jugador con hechizos de Ars Nouveau), `SIEGE`, `MOUNTED_CHARGE`, `SWARM`, `BOSS`. Se deducen de datos que ya se calculan: distancia, altura, si vuela, daño por hechizo/proyectil observado, alcance de las armas propias.

### 3.2 Matriz "quién puede alcanzar a quién"
Una vez por segundo y solo cuando hay una amenaza clasificada, el capitán calcula para cada subordinado `canReach(threat)` (alcance físico + línea de vista + munición). Resultados posibles:
1. **Alguien puede** → `COUNTER_ASSIGNED`: ese guardia (el de mayor alcance efectivo, normalmente el francotirador) recibe una directiva de contraataque; el resto adopta `HOLD`/cobertura para no ser blanco fácil.
2. **Nadie puede** → `THREAT_OUT_OF_REACH`: la regla ya no es "perseguir" sino **salir de su línea de vista / retirarse a cobertura** y, si el plan lo permite, solicitar apoyo. Nunca gastar guardias intentando alcanzar lo inalcanzable.
3. **Capacidad ausente** → el planner y el log informan "falta capacidad `LONG_RANGE_COUNTER`" (regla ya firme: nunca fallar en silencio).

### 3.3 Capacidad nueva
`LONG_RANGE_COUNTER` (y `ANTI_AIR` como sinónimo funcional para voladores) se añade al enum `Capability`. La derivan los francotiradores (§1.3), los pistoleros de precisión y los arqueros R4+ con firma `MULTI_SHOT`/`MARKED_SHOT`.

### 3.4 Qué cambia con el rango del capitán
La **calidad** de este reconocimiento depende del puntaje de liderazgo: un capitán novato tarda más en clasificar y asigna con menos acierto (modula el retraso de respuesta ya existente); uno veterano clasifica antes y reparte mejor. Nunca da stats.

## 4. Radio de presencia del mando (D3)

### 4.1 Problema
Ejército grande = unidad extensa. Un radio fijo de 16 bloques deja fuera a la mitad de la unidad.

### 4.2 Regla (BORRADOR, valores en config)
- **Capitán:** `presencia = clamp(huellaUnidad + margen, 16, 40)`, donde `huellaUnidad` es el radio del rectángulo de `FormationLayout` de su unidad. El capitán debe estar **dentro de su unidad** (ya lo organiza `Leadership`: el mejor puntaje entre los miembros).
- **General:** `presencia = clamp(distancia entre centroides de sus unidades + margen, 32, 96)`. Un general tiene puntaje de liderazgo alto por definición (mejor capitán de la fuerza).
- **Escalado con el número de subordinados:** el margen crece con `√(subordinados)` para que 200 guardias no pidan 200 bloques.
- **Fuera del radio:** la orden **llega por relevo** (la pasa el capitán más cercano a la unidad) con un retraso proporcional a la distancia fuera del radio (tope de 3 s) y la unidad cae a su escalera normal si no la recibe. **Nunca se ignora una orden** (orden nueva > autonomía), pero una unidad fuera de presencia ejecuta con menor calidad.
- Todo se cachea y se recalcula por eventos (cambia la formación o la composición), no por tick.

### 4.3 Qué se ve en el juego
En la ficha de la fuerza y de la unidad: "mando: dentro / en el límite / fuera de presencia" y los radios actuales. Sin botones nuevos.

## 5. Escenarios de simulación que acompañan
- `sniper-vs-flyer`: un lanzador volador a 70–90 bloques; aceptación: el contraataque lo asigna el francotirador, el resto no corre tras él, bajas propias ≤ referencia sin sniper.
- `scout-vision`: el explorador publica el avistamiento; la unidad lo usa a ≥ 60 bloques.
- `signature-*` (§2.5).
- `presence-large`: unidad de 120 en formación abierta; el capitán queda dentro; el porcentaje de guardias "fuera de presencia" < 5 % tras reformar.

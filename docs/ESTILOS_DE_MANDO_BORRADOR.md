# ESTILOS_DE_MANDO_BORRADOR — la misma batalla, sin estilo y con estilo (BORRADOR, 2026-10-09)

Responde a D4: no se programa todavía. Sirve para decidir mirando una imagen clara. **Nada de lo de aquí da vida, daño o velocidad**: un estilo solo cambia *cuánto riesgo acepta el líder* y *cuánto improvisa*, a través de los mismos parámetros acotados que ya modula el puntaje de liderazgo.

## 1. Qué es un estilo
Una preferencia del **capitán/general** (no del guardia suelto) que mueve cinco perillas ya existentes en `ForceRules`/`UnitOrders`:

| Perilla | Prudente | Metódico | Audaz |
|---|---|---|---|
| Umbral de retirada de la unidad (bajas %) | 30 % (antes) | 40 % (norma) | 55 % (después) |
| Correa de persecución (exceso sobre el plan) | −30 % | −10 % | +40 % |
| Oportunismo (¿cambia el plan ante una ventaja que no estaba?) | medio | **bajo** | alto |
| Reserva retenida (fracción de la fuerza que no entra en la primera fase) | 30 % | 20 % | 5 % |
| Tiempo de respuesta ante un cambio de plan | normal | **rápido si el plan lo prevé**, lento si no | rápido |

Un estilo se **deriva** del líder (no hay botón): rasgos de los guardias (Firme→Metódico, Asalto→Audaz, Explorador/Tanque con vida baja→Prudente) más su historial (un capitán que perdió muchos hombres tiende a Prudente). El jugador lo **ve** en la tarjeta del planner ("Capitán Marta · Prudente") y lo **aprovecha** eligiendo a quién pone al mando.

## 2. La misma batalla, narrada
**Escenario:** `ASALTO_A_ESTRUCTURA`, 40 guardias (24 infantería, 12 arqueros, 4 jinetes) contra un puesto de saqueadores con 30 enemigos. Plan de 3 fases: (1) pantalla y fuego desde el flanco, (2) asalto a la puerta, (3) consolidar. Mismo equipo, mismas semillas.

### Hoy (sin estilo)
La unidad cumple el plan con la calidad que da el puntaje de liderazgo del capitán. A las 40 % de bajas se retira (regla con histéresis). Si los jinetes ven una brecha lateral, solo la usan si el planner la tenía como carril (`OPEN_LANE`).
*Resultado típico:* ordenado, algo lento; 11 bajas, 3 min 10 s.

### Con capitán Prudente
Retiene 12 guardias como reserva fuera del alcance enemigo. Las dos primeras fases se hacen con 28. Cuando las bajas llegan al 30 % **se retira antes** y deja a la reserva cubriendo. Los jinetes no persiguen a los que huyen.
*Resultado:* 8 bajas, 4 min 20 s, **el puesto no cae** en el primer intento (se retiró con la ventaja aún pendiente). Útil cuando el jugador no quiere perder veteranos.

### Con capitán Metódico
Sigue las fases tal cual. La brecha lateral que no estaba en el plan **no la usa** (oportunismo bajo), pero el reparto de papeles (yunque/martillo/pantalla) se mantiene perfecto y la transición entre fases es la más limpia.
*Resultado:* 10 bajas, 3 min 30 s, **predecible**; es el estilo que más agradece un plan bien escrito.

### Con capitán Audaz
Abre la fase 2 antes de que los arqueros terminen de suprimir; los jinetes persiguen más allá de la puerta. Se retira recién a 55 % de bajas.
*Resultado:* 15 bajas, 2 min 05 s, **cae el puesto**, pero con coste alto y riesgo de desastre si el enemigo tiene refuerzos (`BATALLA_PERDIDA` salta más veces).

## 3. Qué se ve para decidir
En el informe de la sim v4 (si se programa) aparecería una tabla `style-compare`: bajas, tiempo, objetivo logrado, uso de reserva y cuántas veces se activó la retirada, por estilo. Con eso se juzga si los estilos **se sienten distintos y ninguno es estrictamente mejor** (si Audaz gana siempre, el estilo es un bono disfrazado, no una preferencia).

## 4. Cómo interactúa con lo que ya está decidido
- **Moral:** un Audaz con moral baja se vuelve imprudente más tarde; un Prudente se rompe antes. Ambos usan el mismo estado `FIRME…DESTROZADO`.
- **Planner:** el jugador no elige el estilo, elige al líder; el planner avisa "la fase 2 asume apertura rápida; el capitán asignado es Prudente".
- **Veteranía:** el rango sube la calidad de ejecución; el estilo cambia *qué* ejecuta bien. Un Metódico R5 es el ejecutor perfecto de un plan escrito; un Audaz R5, el que aprovecha ventajas.
- **Sucesión:** si cae el capitán, el estilo cambia con el relevo (momento narrativo útil: "el nuevo capitán es más Audaz").

## 5. Preguntas para David antes de programarlo
1. ¿Te gusta que el estilo se **derive** del líder (sin botón) y se vea en la tarjeta del planner?
2. ¿Quieres **tres** estilos o empezamos con dos (Prudente/Audaz) y el Metódico sale de "sin estilo"?
3. En la tabla de arriba, ¿algún número te choca (p. ej. la retirada al 55 % del Audaz)?

# Feedback de David tras probar E1 (2026-10-07) y datos de los registros

Fuente: pruebas en `veamos-test`, `latest.log` y `rally-sim/*.json`. Nada de esto se ha corregido aún.

## A. Datos medidos (no opiniones)
- `/rally debug perf` con 200 guardias: Rally total 0,262 ms/tick (29 427 ticks). Mayores: mounts 0,170; stance_loop 0,040; pack 0,030; markers 0,010. **Picos únicos:** markers 94 ms, mounts 35 ms, combat_gate 28 ms, stance_loop 23 ms, ranged_goal 23 ms, pack 20 ms.
- El servidor registró 61 avisos "Can't keep up" (hasta 141 ticks de retraso) durante la prueba: la carga no viene del coste medio de Rally.
- **49 de 53 muertes de guardias fueron por ahogamiento** (zona de lago cerca de -1450, 60, -1020). Causa no diagnosticada: puede ser el punto de aparición del comando de depuración, la orden de quedarse quietos (posted) dentro del agua, o los guardias montados.
- Simulaciones A–D (3 repeticiones, semilla 1): A sobrev. 18,3 / B 18,0 / C 19,3 / D 20,7. Tiros y aciertos C 49/59, D 41/55. Distancia media al enemigo ≈ 13 en las cuatro. Formación 7–17 %. **B ≈ A es lo esperado hoy** (la veteranía aún no existe). Variación entre repeticiones alta (A: 17–20), diferencias C–D dentro del ruido.

## B. Fallos y ajustes (candidatos a "ARREGLOS DE E1" en E2a)
1. Guardias ahogándose: nadar/salir del agua; no quedarse "posted" bajo el agua; revisar `/rally debug spawn` para que no los coloque sobre agua.
2. Con munición solo en la mochila el guardia sigue "disparando" sin flechas y no cambia a la espada. Cambiar de arma cuando el arma a distancia no tiene munición disponible (incluida la mochila).
3. Arco de dragón y flechas de dragón: el daño del guardia es menor que el del jugador. Verificar cómo se calcula el daño del proyectil frente al del ítem.
4. Alcance: un veterano debería poder intentar disparar de mucho más lejos (sobre todo francotiradores). El alcance ya viene de la configuración de cada arma de Scorched Guns; revisar el rango de detección y de apuntado.
5. Volea: poco precisa; debe mejorar con veteranía.
6. HQ: les cuesta entrar a una casa con una sola entrada; quienes entran por munición deben **aparcar el caballo** y, al salir del HQ, **volver a montar** y partir. Un guardia se quedó en el HQ y no salió con la orden. No debe haber conflictos HQ ↔ órdenes.
7. Diálogo de crimen de MCA ("Stop where you stand…"): si cometo un delito cerca de los guardias de Rally no debe aparecer; deben entenderse como guardias de mi mod.
8. Menú de guardias: un guardia que ya está en un escuadrón solo aparece en Escuadrones, no en la ventana de guardias.
9. Menú de MCA sobre un guardia propio: sigue sin abrirse (no se incorporó). El botón "hablar" no es crítico, pero MCA y Rally deben coexistir.
10. Orden V (formación) con 200 guardias: no se forma; "seguir" funciona al instante. Con uno solo, funciona bien. Revisar escala de formaciones.
11. Seguimiento cuando el jugador está en el aire: aceptable.
12. Montados y orden G a un objetivo: atacan, se mueven y vuelven (comportamiento de arqueros; confusión de pruebas, no un fallo).

## C. Diseño pedido (para E2a/E3, no son arreglos)
- **Órdenes universales:** avanzar, retirarse, mantener, etc. deben tener un significado definido para **todos los roles**, no solo arqueros; si un grupo mixto recibe una orden que a un rol no le aplica, ese rol la degrada de forma sensata (p. ej. el cuerpo a cuerpo avanza en lugar de "fuego a distancia"). La jerarquía de órdenes debe ser muy robusta y la interfaz más limpia (ya hay demasiados botones).
- **Doctrinas por rol:** las opciones de disparo (retroceder disparando, avanzar disparando, quieto, volea…) son específicas de arqueros, pero el sistema de doctrinas debe cubrir todas las armas y situaciones, aparecer en el Battle Planner, y los veteranos con libertad elegir solos; si el jugador da una orden, usan solo esa.
- **Atacar a grupos:** hoy hay que seleccionar uno a uno; hace falta una forma de ordenar un ataque a un escuadrón entero.
- **Órdenes con muchos guardias:** diseñar para escala (cientos), no solo 20.
- **Simulaciones:** David quiere simulaciones para pulir cada esquina (comportamiento, veteranía, tamaños de grupo, escuadras), incluyendo una corrida larga sin atención (≈2 h) de la que Claude Code saque conclusiones propias.

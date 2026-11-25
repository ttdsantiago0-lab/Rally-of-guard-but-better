# Preguntas de diseño 2026-11

Responde debajo de cada pregunta, en la línea **R:**. No hace falta responder todas; marca con `?` las que quieres
discutir y con `—` las que no te importan todavía. Cada sección enlaza a su tema en el [[00 Mapa del mod]].

## Visión general

1. En una frase cada una: ¿qué siente el jugador a las 10 horas, a las 100 y a las 1000?
   - **R:** 10 h: la emoción de explorar con un guardia contratado y verlo subir de nivel (o indiferencia: sabe que hay guardias y sigue con su vida). 100 h: sabe que existen porque hay guerrillas poderosas y otros jugadores con séquitos; son parte integral del mundo. El que se dedica al mod tiene un grupo con armaduras, comparte el loot, quizá un HQ, caballos, guardias cuidando la casa o espiando a otro jugador; en el extremo, en vez de descubrir Create o Ars Nouveau intenta gobernar una aldea. 1000 h: todos conviven con ejércitos de otros jugadores, grandes asentamientos, tributo, puertos, guerrillas o facciones, y al menos tienen guardias en casa. El de Create/Ars tiene guardias protegiendo sus cosas y sus naves y alguna fuente de tributo. El dedicado al mod tiene red logística, varios tipos de ejército, fuerza naval y aérea, y usa otros mods para crecer o mantener el ejército (Create para recursos, rituales de Ars para tótems, dragones de Ice and Fire para la dragon forge). *(David, 2026-11-19)* → [[D-0001 Visión del mod]]
2. ¿El jugador es el general que planea, el comandante que pelea en el frente, o ambos? ¿Cuánto pelea él mismo?
   - **R:** Las tres cosas: general que planea, comandante que pelea o soldado individual. Un jugador avanzado va en dragón, rápido, sin ejército detrás, y si se cruza algo inesperado pelea solo; el que quiere explorar solo lo hace. Ante una invasión de su casa es comandante junto a sus guardias; si prepara un ataque es general. El mod no fuerza una forma de jugar (igual contra guerrillas). *(David, 2026-11-19)* → [[D-0001 Visión del mod]]
3. ¿Cuál es el momento que quieres que tus amigos se cuenten después de jugar?
   - **R:** Que gracias al guardia en el early sobrevivió a un enemigo pequeño, o que el guardia murió rápido y el jugador salió corriendo. Y grandes historias: "planeé esto, por eso puse a los magos ahí, pero me asustaste cuando llegaste con cañones desde el aire, puse retirada y huí, pero era tarde". *(David, 2026-11-19)* → [[D-0001 Visión del mod]]
4. ¿Es más un mod de dificultad (el mundo se vuelve peligroso) o de poder (construyes un ejército)? Da una proporción.
   - **R:** 30 dificultad / 70 poder. Si nadie hace grandes ejércitos, lo máximo es una guerrilla peligrosa que se evita. Si un jugador crece en poder, los otros no querrán quedarse atrás. *(David, 2026-11-19)* → [[D-0001 Visión del mod]]
5. ¿El mod se piensa para tu modpack o debe funcionar solo con Guard Villagers?
   - **R:** Que la gente quiera probarlo con todas sus compatibilidades de mobs, pero solo con Guard Villagers debe funcionar: guardias, caballos, espadas, arcos, escudos y poco más. *(David, 2026-11-19)* → [[D-0001 Visión del mod]]
6. ¿Hay un final (conquista, victoria) o es un mundo sin fin?
   - **R:** Conquista se alinea con la filosofía, pero prefiere que sea un lienzo geopolítico y económico; el resto lo deciden los jugadores. *(David, 2026-11-19)* → [[D-0001 Visión del mod]]
7. ¿Qué juegos se acercan más a lo que imaginas y qué tomas de cada uno?
   - **R:** Total War: guerra entre ejércitos, moral, estrategia. Age of Mythology: puertos, roles, cuarteles, logística. No conoce un juego de avanzar explorando con un compañero. *(David, 2026-11-19)* → [[D-0001 Visión del mod]]
8. ¿Qué tono: realista, heroico, sombrío, épico?
   - **R:** Épico. *(David, 2026-11-19)* → [[D-0001 Visión del mod]]
9. ¿Cuál es la línea roja: qué nunca debe pasar en el mod?
   - **R:** El mod nunca debe decirle al jugador cómo jugar; solo acompañarlo. *(David, 2026-11-19)* → [[D-0001 Visión del mod]]
10. ¿Cuánto micromanejo aceptas y qué debe ser siempre automático?
   - **R:** Automático: los guardias deben ser competentes plug and play. Si el jugador quiere un guardia de tótem que solo tome balas, puede. Si quiere llegar a la minucia (defensa de un fuerte, cadena logística), la coordinación debe aguantar. *(David, 2026-11-19)* → [[D-0001 Visión del mod]]
11. ¿Qué tanto quieres que se note la "inteligencia" de los guardias (mensajes, iconos, gestos, nada)?
   - **R:** Nada de gestos ni iconos: el comportamiento debe bastar. *(David, 2026-11-19)* → [[D-0001 Visión del mod]]
12. ¿Cuál de todos los sistemas es el corazón, el que no puede faltar?
   - **R:** El motor de Rally: la coordinación entre guardias. *(David, 2026-11-19)* → [[D-0001 Visión del mod]]

## Motor táctico · [[Motor táctico]]

13. ¿Quién elige la maniobra (pinza, cebo, línea): tú, el comandante o el motor? ¿Depende del tamaño del ejército?
   - **R:** El motor usa la maniobra si es la mejor solución y no hay orden que diga lo contrario; si el jugador evalúa distinto, Rally se acopla y lo hace lo mejor posible. El tamaño lo dirán las simulaciones (hay estrategias de ventaja numérica y otras no). Propuesta: experimento con enseñanzas de libros (El arte de la guerra y demás) para ver si tienen relación directa con Minecraft; una iteración puede ser "si no sabemos qué hacer, qué dice la literatura". *(David, 2026-11-19)* → [[D-0003 El motor ejecuta, el jugador decide]] · [[D-0005 Para qué sirven los laboratorios]]
14. ¿El compromiso del plan por defecto debería ser 0, 50 o 100 %?
   - **R:** El compromiso 0–100 % es para el Battle Planner (certeza de que un plan salga según predicciones). Las instrucciones fuera del Battle Planner van al 100 %. *(David, 2026-11-19)* → [[D-0003 El motor ejecuta, el jugador decide]]
15. ¿Cuándo está bien que un comandante desobedezca tu orden?
   - **R:** La moral, si se incorpora. Y en el Battle Planner: p. ej. un plan que espera un ataque marítimo con 50 % de certeza; si llegan aéreos, se intenta mantener la espera, pero si hay bajas se rompe y hacen lo que dice el motor. Quizá el plan tenga detonantes: "si usan dragones, esto no aplica, hagan B" o "sobrevivan" (= lo que diga Rally). Con más roles, armas y amenazas las combinaciones serán muchas: las simulaciones son necesarias para entrenar el motor. *(David, 2026-11-19)* → [[D-0003 El motor ejecuta, el jugador decide]]
16. ¿Cuántos niveles de mando quieres (general, comandante, sargento) y qué decide cada uno?
   - **R:** Decidirlo tras miles de simulaciones según el beneficio práctico o técnico de cada escalón. Si da igual, es estético (quizá algún stat): líderes de squad y sub-squad y como máximo un coordinador encima: 3 niveles más el jugador, salvo que algo técnico o práctico sea mejor. *(David, 2026-11-19)* → [[D-0004 Niveles de mando por evidencia]]
17. ¿El límite de ~10 comandantes por general es una regla de juego o técnica?
   - **R:** Igual que la anterior: lo técnico lo delimita primero, luego lo práctico (mejores resultados), y al final la jugabilidad e interfaz decide cómo organizarlo o limitarlo. 10 le hace sentido, pero no es regla. *(David, 2026-11-19)* → [[D-0004 Niveles de mando por evidencia]]
18. ¿Puedes diseñar y guardar tus propias formaciones? ¿Se comparten entre jugadores?
   - **R:** Sí, a través del Battle Planner. No se comparten por ahora. *(David, 2026-11-19)* → [[D-0007 Identidad e historia de las unidades]]
19. ¿Cómo te enteras de lo que decidió el motor durante la batalla?
   - **R:** Primero, en el comportamiento: si dijiste "mantengan la posición" y se retiran a la primera, algo está roto. Además, historia a varios niveles: individual (las batallas forjan a los guardias), de grupo (moral alta, récord, historias) y general: batallas registradas con ganadores, que se puedan enmarcar o guardar físicamente, estatuas (idea nueva) y un resumen táctico con cifras clave para decidir. *(David, 2026-11-19)* → [[D-0007 Identidad e historia de las unidades]]
20. ¿Cuánta aleatoriedad toleras entre dos batallas iguales?
   - **R:** No lo sabe. No puede ser extrema o todo pierde sentido. Pregunta abierta: de dónde sale (personalidad, clima, dos estrategias con resultados parecidos en el motor, bioma) o algo que simule todo eso de forma agradable. *(David, 2026-11-19)* → [[D-0003 El motor ejecuta, el jugador decide]]
21. ¿Qué hace un guardia que no tiene ninguna orden?
   - **R:** Por defecto sigue al jugador y lo defiende (hoy deambula). Tema de discusión. *(David, 2026-11-19)* → [[D-0003 El motor ejecuta, el jugador decide]]
22. ¿Las unidades tienen nombre, estandarte o color?
   - **R:** Sí: más que color o estandarte, uniformes, banners y quizá banderas como ítem (idea). *(David, 2026-11-19)* → [[D-0007 Identidad e historia de las unidades]]
23. ¿El motor aprende entre batallas o solo aprenden los veteranos?
   - **R:** El motor se construye aprendiendo de miles de simulaciones. El veterano probablemente tiene la mayor información que el motor puede darle. El motor nunca podrá simular todos los escenarios, por eso la idea de un motor que evalúe amenazas e improvise soluciones a lo nunca visto, y que evolucione; no sabe si es posible: hay debate. *(David, 2026-11-19)* → [[D-0005 Para qué sirven los laboratorios]]
24. ¿Qué maniobra quieres ver funcionar primero en el juego?
   - **R:** Algo sencillo: todos con lanza avanzando en tortuga con el jugador en medio, totalmente coordinados. Pero no le gusta la pregunta: es un sistema, no se avanza a punta de una cosa. Quiere ver la coordinación funcionar en todas las maniobras. Aclaración sobre los laboratorios: no se trata solo de ver qué funciona mejor, sino de entender cómo hacer técnicamente en Java que los guardias se coordinen para las estrategias que queremos. *(David, 2026-11-19)* → [[D-0005 Para qué sirven los laboratorios]]
25. Contra ejércitos de guardias, ¿la pinza debe poder vencer a un ejército mejor equipado? ¿Hasta qué diferencia de equipo?
   - **R:** No es regla estricta, es una hipótesis. Probarlo: entender cómo se usa en la vida real y cómo debería verse en Rally; mientras la respuesta no sea "es un problema físico de los guardias o de Minecraft", el motor puede hacerlo mejor: iterar hasta que sea mejor. Regla para aplicar a discreción en los laboratorios (p. ej. el de formaciones de libros), en paralelo con el método actual; evaluar si sirve, seguir o descartar; puede no ser concluyente y seguir. *(David, 2026-11-19)* → [[D-0005 Para qué sirven los laboratorios]]

## Retirada · [[Retirada]]

26. ¿Quién decide retirarse: tú, el comandante o cada guardia?
   - **R:** Como ejército, el jugador; pero la moral, la certeza y la falta de órdenes pueden causar retiradas sin orden. *(David, 2026-11-19)* → [[D-0006 Retirada]]
27. En la retirada con retaguardia, ¿quién se queda: lo eliges tú, los escudos, los más sanos, o voluntarios?
   - **R:** Lo elige el motor. Quizá el Battle Planner permita una excepción: "en retirada con retaguardia, priorizar tal". *(David, 2026-11-19)* → [[D-0006 Retirada]]
28. ¿Qué significa "a salvo": llegar a una base, romper contacto, esconderse?
   - **R:** Lo evalúa el medidor de peligro: una fortaleza o un lugar donde la amenaza ya no prevalece. Ej.: un dragón persigue, entran a una cueva, pasa el tiempo y la alerta baja poco a poco. *(David, 2026-11-19)* → [[D-0006 Retirada]]
29. Los caballos de relevo, ¿los dejas tú en el mundo o los lleva la logística sola?
   - **R:** (Pregunta en el chat: ¿los descartamos o los dejamos para enemigos lentos?) Primero identificar por qué no sirven; luego se decide. Necesita más contexto. *(David, 2026-11-19)* → [[D-0006 Retirada]]
30. ¿Huir tiene castigo (moral, reputación, sueldo)?
   - **R:** Moral y reputación. Sueldo, no sabe: ¿sueldo mínimo? ¿moral alta = sueldo más alto o mejor equipo? Sin pensar del todo. *(David, 2026-11-19)* → [[D-0006 Retirada]]
31. ¿Existe la huida dispersa (cada uno por su lado)?
   - **R:** Sí; estos comportamientos deben probarse y explorarse en los laboratorios. *(David, 2026-11-19)* → [[D-0006 Retirada]]
32. ¿Un ejército que huyó puede reagruparse solo? ¿Dónde?
   - **R:** Sí. Hay un HQ temporal; si no es seguro y el HQ base no existe o está invadido, quizá buscan al jugador; si el jugador murió o está lejos, intentan reagruparse. En teoría podrían volverse facción, unirse al ejército que los venció según la moral, o seguir leales y volver a él como puedan. Es fantasía: descubrir técnicamente qué se puede y queda bien. *(David, 2026-11-19)* → [[D-0006 Retirada]]
33. Tras una misión, ¿el regreso a casa es parte del juego o se resuelve automáticamente?
   - **R:** No muy claro. En paz deben volver a casa, pero el jugador controla: libres (lootean y vuelven), sin interés en loot (directo al HQ), asentarse en el lugar conquistado o iniciar otra campaña. Todo juega con la moral (tema delicado). *(David, 2026-11-19)* → [[D-0006 Retirada]]

## Moral · [[Moral]]

34. ¿La moral se ve? ¿Cómo (barra, gestos, partículas, nada)?
   - **R:** Antes había algo que mostraba el miedo cuando un guardia huye por moral. Por ahora queda sujeto a lo técnico de rendimiento: la prioridad es que cientos de guardias corran bien. *(David, 2026-11-22)* → [[D-0008 Moral desde el motor]]
35. ¿Qué la rompe? Ordena: ver caer al líder, explosión cerca, perder el batallón, un jefe enorme, el hambre.
   - **R:** Aún no está claro: cuando había moral por tantos factores perdían demasiadas batallas. El balance no es copiar Total War, sino adaptarlo a nuestras capacidades. Opciones: un porcentaje (el x % de guardias con x experiencia se rompe ante ciertos sucesos) o, más realista, que se rompa según la evaluación interna del motor de la situación; tiene la teoría de que eso se sentiría más conectado. Nota (no decisión): guerrillas o facciones derrotadas podrían aceptar unirse a un ejército. *(David, 2026-11-22)* → [[D-0008 Moral desde el motor]]
36. ¿Qué la restaura: tu presencia, un estandarte, una victoria, comida, descanso?
   - **R:** Todo, pero aún no está claro. Lo que sí se sabe: un veterano maestro tiene moral casi irrompible; ver si hay pequeños sucesos que incluso a ellos los quiebran, y los pocos maestros o veteranos que no se rompen ante esas situaciones van ganando nuevos poderes. *(David, 2026-11-22)* → [[D-0008 Moral desde el motor]]
37. ¿La moral cambia solo si huyen, o también la puntería, la velocidad o la obediencia?
   - **R:** Hay que discutirlo a nivel de filosofía del mod: la verdadera función de la moral es que el juego sea inmersivo. ¿Qué significa eso en Minecraft y en nuestro motor de Rally, y cómo se logra? Esa es la pregunta a resolver. *(David, 2026-11-22)* → [[D-0008 Moral desde el motor]]
38. "Los veteranos nunca rompen": ¿es definitivo o solo para las pruebas?
   - **R:** Ver la 36: casi irrompible, con pequeños sucesos que quizá sí los quiebran. *(David, 2026-11-22)* → [[D-0008 Moral desde el motor]]
39. ¿Las facciones enemigas tienen moral propia que puedes romper?
   - **R:** Sí. *(David, 2026-11-22)* → [[D-0008 Moral desde el motor]]
40. ¿Se puede ganar una batalla rompiendo la moral del enemigo sin matarlos a todos?
   - **R:** No es una decisión todavía, pero si tienes 30 guardias rodeados por cientos, deberían poder rendirse y quedar presos; eso hace parte de la moral. *(David, 2026-11-22)* → [[D-0008 Moral desde el motor]]
41. ¿El miedo a un tipo de enemigo (dragones, por ejemplo) se hereda o se cura?
   - **R:** Se puede forjar carácter frente a ciertas cosas. Realista: un guardia que pelea contra muchos dragones se puede volver matadragones, y que eso signifique algo más. Quiere que los guardias se sientan bastante únicos. *(David, 2026-11-22)* → [[D-0008 Moral desde el motor]]

## Veteranía · [[Veteranía]]

42. ¿Cuántas horas de juego para tener un veterano? ¿Y un maestro?
   - **R:** El maestro parte de ser veterano y no es por tiempo sino por uso. Lo de veterano no es un número que él diga: hay que ver qué tan fuertes quedan los guardias con las simulaciones y, a partir de eso, qué es justo exigir para llegar a ese nivel. *(David, 2026-11-22)* → [[D-0009 Veteranía por uso]]
43. ¿La muerte de un guardia es permanente? ¿Hay forma de salvarlo (caído, rescate, revivir)?
   - **R:** Necromancer; quizá la parca de MCA. *(David, 2026-11-22)* → [[D-0009 Veteranía por uso]] · [[D-0010 Necromancer]]
44. ¿La veteranía se puede perder (heridas, derrotas, tiempo sin pelear)?
   - **R:** No. *(David, 2026-11-22)* → [[D-0009 Veteranía por uso]]
45. ¿Los rasgos como "cazadragones" se ven en el guardia (título, insignia)?
   - **R:** Aún no se compromete con un sistema de títulos: podrían ser medallas, títulos o algo relacionado con su aspecto. *(David, 2026-11-22)* → [[D-0009 Veteranía por uso]]
46. ¿Hasta dónde pueden subir las estadísticas de un veterano frente a uno nuevo?
   - **R:** Depende de qué tan fuertes queden después de terminar el motor de Rally. *(David, 2026-11-22)* → [[D-0009 Veteranía por uso]]
47. ¿Quieres una crónica legible de cada guardia (batallas, muertes, logros)?
   - **R:** Que sea técnicamente viable para cientos de guardias. Entre más completa, más inmersiva y mejor, pero debe ser realista. *(David, 2026-11-22)* → [[D-0009 Veteranía por uso]]
48. ¿Los guardias tienen nombre propio? ¿Lo pones tú?
   - **R:** Tienen nombres propios, y se les puede asignar uno. *(David, 2026-11-22)* → [[D-0009 Veteranía por uso]]
49. ¿Hay jubilación: un veterano viejo que entrena a otros?
   - **R:** Aprobado: concepto interesante en los HQ. Un guardia herido que quedó fuera de batalla, o uno con tantas batallas que por ciertos logros + probabilidad adquiere el carácter de jubilado; su mayor valor es en el HQ, haciendo que los guardias ganen XP más rápido. Serían assets interesantes. *(David, 2026-11-22)* → [[D-0009 Veteranía por uso]]
50. ¿Un veterano puede volverse comandante solo, o lo nombras tú?
   - **R:** Aún no sabe si habrá comandantes automáticos o si él los nombra. *(David, 2026-11-22)* → [[D-0009 Veteranía por uso]]
51. ¿Los veteranos cuestan más (sueldo, comida, tributo)?
   - **R:** Sí. El sueldo es tributo; la comida, no sabe. *(David, 2026-11-22)* → [[D-0009 Veteranía por uso]]

## Estilos de mando · [[Estilos de mando]]

52. ¿El estilo de un líder (prudente, agresivo) lo eliges tú o sale solo de su historia?
   - **R:** De su historia. *(David, 2026-11-22)* → [[D-0009 Veteranía por uso]]
53. ¿Quieres personalidades fuertes, con nombre y manías, o líderes funcionales?
   - **R:** Funcionales, aunque ciertas situaciones, si coinciden, pueden llevar a manías y demás. *(David, 2026-11-22)* → [[D-0011 Líderes funcionales con historia]]
54. ¿Un líder puede negarse a una misión que juzga suicida?
   - **R:** Sí puede estar inconforme; quiere escuchar ideas de Claude sobre cómo sería, una vez respondidas todas las preguntas. (Nota de David: cuando dice "es de discusión" significa hablarlo en particular después de terminar todas las preguntas.) *(David, 2026-11-22)* → [[D-0011 Líderes funcionales con historia]]
55. ¿La personalidad de MCA debe influir en los guardias?
   - **R:** Sí. *(David, 2026-11-22)* → [[D-0011 Líderes funcionales con historia]]
56. ¿Los líderes enemigos tienen estilo reconocible para que aprendas a leerlos?
   - **R:** Sí. Ej.: un guerrillero tiende a atacar de noche, y ya lo sabes. Depende de qué tan lejos se lleve técnicamente. *(David, 2026-11-22)* → [[D-0011 Líderes funcionales con historia]]

## Tributo · [[Tributo]]

57. ¿Qué compra el tributo, además de consagrar? Haz una lista de prioridades.
   - **R:** Paga el salario de los guardias; quizá moneda de cambio con facciones; replicar una blueprint cuesta materiales + tributo; mercado en las aldeas donde se compra con tributo; quizá una aldea con más tributo permite más aldeanos. Son ideas por discutir. Lo seguro: el tributo es central, pensando en la logística, el transporte y en ser un activo estratégico. *(David, 2026-11-22)* → [[D-0012 El tributo es central]]
58. ¿Se puede robar el tributo de otro jugador o de una facción?
   - **R:** Sí. *(David, 2026-11-22)* → [[D-0012 El tributo es central]]
59. ¿Cómo se ve una caravana de tributo y quién la protege?
   - **R:** Varios caballos jalando carretas con el tributo; quizá opciones más modernas, como trenes. *(David, 2026-11-22)* → [[D-0012 El tributo es central]]
60. ¿El tributo depende de cómo tratas a los aldeanos?
   - **R:** Algo tiene que ver; parte de la discusión. *(David, 2026-11-22)* → [[D-0012 El tributo es central]]
61. ¿Hay un tope de tributo por jugador o por aldea?
   - **R:** No cree, aunque se puede discutir el balance según qué tan expansionista será el mod. *(David, 2026-11-22)* → [[D-0012 El tributo es central]]
62. En multijugador, ¿el tributo es por jugador o por bando?
   - **R:** Por jugador. *(David, 2026-11-22)* → [[D-0012 El tributo es central]]
63. ¿La consagración tiene niveles?
   - **R:** ¿Consagrarse es lo mismo que ser maestro? Diría que sí tiene niveles. *(David, 2026-11-22)* → [[D-0012 El tributo es central]]
64. ¿Qué pasa con un arma consagrada si su portador muere?
   - **R:** Puede conservar sus stats, pero decrecen rápido. Ej.: Magnum consagrada, 100 de daño por tiro; en 3 tiros se pierde. *(David, 2026-11-22)* → [[D-0012 El tributo es central]]

## Economía · [[Economía]]

65. ¿Los guardias cobran sueldo? ¿En qué moneda (esmeraldas, tributo, comida)?
   - **R:** Con esmeraldas se contratan aldeanos; sin comida los ejércitos sufren. *(David, 2026-11-22)* → [[D-0013 Economía y mercado]]
66. ¿Qué pasa si no pagas: se van, se rebelan, bajan la moral?
   - **R:** No está seguro: quizá si pasa mucho se van y arman sus propios grupos; ¿revolución? *(David, 2026-11-22)* → [[D-0013 Economía y mercado]]
67. ¿Se pueden contratar mercenarios o guerrillas?
   - **R:** Sí (mercenarios, guerrillas u otras facciones que se metan). *(David, 2026-11-22)* → [[D-0013 Economía y mercado]]
68. ¿Existe un mercado de veteranos entre jugadores?
   - **R:** Sí. En el mercado se publican guardias buscando trabajo (sería chévere poder evaluar el potencial de un guardia como soldado propio, qué caracteres podría tener y si hay alguno raro); intercambio de guardias, armaduras, ítems, artillería y blueprints de lo construido (quizá estructuras preferidas por facción). También un aldeano mercader con caravanas escoltadas por guardias fuertes, parecido al de la llama pero con mercancía de alto valor (p. ej. dragonsteel), caro y en tributo. *(David, 2026-11-22)* → [[D-0013 Economía y mercado]]
69. ¿El tamaño del ejército lo limita el dinero, la población o una regla fija?
   - **R:** El dinero; también la logística (manejar ejércitos grandes es difícil, comida) y el tamaño de la aldea y la cantidad de aldeanos, que son los que generan el tributo. *(David, 2026-11-22)* → [[D-0013 Economía y mercado]]
70. ¿Los precios cambian según la guerra (escasez, saqueo)?
   - **R:** Sería increíble un mercado dinámico, pero es bastante ambicioso. *(David, 2026-11-22)* → [[D-0013 Economía y mercado]]
71. ¿Hay botín de guerra y cómo se reparte?
   - **R:** Sí: algunos guardias lootean tras las batallas, lo llevan al HQ y se reparte (cómo, aún no). Si hay un arma mejor del tipo que usa el guardia, la toma; aunque un veterano quizá prefiere su arma con la que gana maestría (discutible). *(David, 2026-11-22)* → [[D-0013 Economía y mercado]]
72. ¿Hay impuestos o tributo que pagan las aldeas conquistadas?
   - **R:** El impuesto es tributo. *(David, 2026-11-22)* → [[D-0012 El tributo es central]]

## Logística · [[Logística]]

73. ¿La comida es obligatoria siempre, o solo en campañas largas?
   - **R:** Siempre. *(David, 2026-11-22)* → [[D-0014 Logística con riesgo]]
74. ¿Las flechas y la munición son siempre finitas?
   - **R:** Sí, excepto con encantamientos como Infinity. *(David, 2026-11-22)* → [[D-0014 Logística con riesgo]]
75. ¿El reabastecimiento desde la base es automático o lo organizas tú?
   - **R:** Lo organiza el jugador; se puede automatizar con Create y demás. *(David, 2026-11-22)* → [[D-0014 Logística con riesgo]]
76. ¿Quieres animales de carga en el juego normal o como mejora?
   - **R:** Sí, quiere animales de carga, y armaduras nuevas para ciertos animales. *(David, 2026-11-22)* → [[D-0014 Logística con riesgo]]
77. ¿Campamentos temporales: los montas tú o el comandante?
   - **R:** El comandante; el jugador elige dónde moverlo o dónde plantarse. *(David, 2026-11-22)* → [[D-0014 Logística con riesgo]]
78. ¿Las armas y armaduras se desgastan y se reparan? ¿Quién repara?
   - **R:** Sí. En las aldeas hay herreros, y quizá en los HQ; también se desgastan, se pierden y toca coger otra. *(David, 2026-11-22)* → [[D-0014 Logística con riesgo]]
79. ¿Cuánta logística es divertida y desde cuándo es tedio?
   - **R:** Entretenida: quien no quiere estresarse con trenes de Create puede arriesgarse con su caravana, siempre nervioso de ataques. El jugador técnico usa tecnología: barcos, terraformar ríos y rutas de comercio. Los adversarios podrían no empezar guerras pero sí atacar rutas comerciales. *(David, 2026-11-22)* → [[D-0014 Logística con riesgo]]
80. ¿El saqueo tras la batalla lo hacen los guardias solos?
   - **R:** Sí. *(David, 2026-11-22)* → [[D-0014 Logística con riesgo]]

## Caballería · [[Caballería]]

81. ¿Quieres crianza de caballos con linajes y calidades?
   - **R:** Le encantaría. Hay mods de eso, pero no sabe si encajan con lo que se quiere. *(David, 2026-11-22)* → [[D-0015 Caballería temible]]
82. ¿Qué beneficios exactos da el vínculo jinete-caballo (vida, velocidad, obediencia, otro)?
   - **R:** Todo. Los caballos están muy menospreciados en el juego, pero el corcel de un guardia debe ser de temer. *(David, 2026-11-22)* → [[D-0015 Caballería temible]]
83. ¿La muerte de un caballo vinculado afecta al jinete?
   - **R:** Sí. *(David, 2026-11-22)* → [[D-0015 Caballería temible]]
84. ¿Qué rol debería tener la caballería con espada, si no carga contra hordas ni contra armas?
   - **R:** Es barata de usar y ceremoniosa; quizá para persecuciones, peleas en lugares pequeños, el jinete que se queda sin munición, y espadas que hagan más daño que otros ítems. *(David, 2026-11-22)* → [[D-0015 Caballería temible]]
85. ¿Otras monturas: camellos para el desierto, elefantes, monturas de mods?
   - **R:** Le gustaría. *(David, 2026-11-22)* → [[D-0015 Caballería temible]]
86. ¿Monturas voladoras (hipogrifos, dragones domados) entran en algún momento?
   - **R:** Sí, con mucho cuidado. *(David, 2026-11-22)* → [[D-0015 Caballería temible]]
87. ¿La caballería mongol (atacar, irse, volver) debe ser una orden tuya o una costumbre de ciertas unidades?
   - **R:** Costumbre, pero se pueden dar órdenes específicas con el Battle Planner. *(David, 2026-11-22)* → [[D-0015 Caballería temible]]

## Armas de fuego · [[Armas de fuego]]

88. ¿Tus guardias deben poder usar armas de fuego? ¿Desde cuándo (tecnología, tributo)?
   - **R:** Sí, desde siempre. *(David, 2026-11-22)* → [[D-0016 Armas y estructuras]]
89. ¿Se mezclan épocas (arcos y pistolas) o hay una progresión clara?
   - **R:** Se mezclan. *(David, 2026-11-22)* → [[D-0016 Armas y estructuras]]
90. ¿La munición de armas de fuego es un recurso de logística aparte?
   - **R:** Sí: toda la línea de creación de Scorched Guns. *(David, 2026-11-22)* → [[D-0016 Armas y estructuras]]
91. ¿Los pistoleros enemigos son una facción propia?
   - **R:** No sabe; las facciones son más bien religiosos, condes, guerrillas (Claude había propuesto más). Undead podría dar pie a la dimensión de Ice and Fire. *(David, 2026-11-22)* → [[D-0016 Armas y estructuras]]
92. Entre casas, ¿esperas que los guardias tomen las casas, rodeen o quemen?
   - **R:** Deberían ser capaces de tomar todo tipo de órdenes contra estructuras. *(David, 2026-11-22)* → [[D-0016 Armas y estructuras]]
93. ¿Quieres armas propias de Rally o solo usar las de Scorched Guns?
   - **R:** Scorched Guns. Las armas propias de Rally son específicas: lanzas, cosas de arena para el coliseo, etc. *(David, 2026-11-22)* → [[D-0016 Armas y estructuras]]

## Jefes · [[Jefes]]

94. ¿Los guardias deben poder matar un jefe sin ti?
   - **R:** Sí, si es un buen grupo. *(David, 2026-11-22)* → [[D-0016 Armas y estructuras]]
95. ¿Qué número de guardias te parece razonable contra un dragón anciano? ¿Y contra Tremorzilla?
   - **R:** No se trata del número de guardias. Guardias con Rally solo individual deberían tener un winrate más bajo que guardias con Rally que usan coordinación y estrategias de equipo. Ej.: un guardia decide a quién dar la siguiente flecha (prioridad para un misil a la vez), pero tras x misiles por objetivo quizá lo más efectivo no es mandar 10 al mismo, sino 1 a un segundo que se acerca: eso es coordinación. El motor debe bastar en dos direcciones: (1) mejorar de verdad las estadísticas frente a guardias sin motor, quitando sesgos, por cooperación genuina y no solo mejor comportamiento individual; (2) que un jugador dé una instrucción a 100 guardias y el motor lo soporte, no se rompa, lo haga organizado y le permita hacer realidad una estrategia que ningún algoritmo de laboratorio entendería. *(David, 2026-11-25)* → [[D-0017 Qué es coordinación]]
96. ¿Los jefes dejan algo especial para el ejército (materiales, trofeos, estandartes)?
   - **R:** Le gustaría que sí. *(David, 2026-11-25)* → [[D-0017 Qué es coordinación]]
97. ¿Un jefe puede ser parte de una facción enemiga?
   - **R:** Le gustaría que las guerrillas o facciones tuvieran jefes; también podría tener que ver con el rol de necromancer. *(David, 2026-11-25)* → [[D-0017 Qué es coordinación]]
98. ¿Quieres armas especiales contra jefes (balistas, arpones, cañones)?
   - **R:** La artillería ya se ha pensado; sí, implementada de manera conservadora. *(David, 2026-11-25)* → [[D-0017 Qué es coordinación]]
99. ¿Los cazadragones deben ser raros y valiosos?
   - **R:** Los guardias con caracteres raros deben ser valiosos, no solo los matadragones. Poner todas las denominaciones en una tabla para que David apruebe, cambie, niegue o agregue. *(David, 2026-11-25)* → [[D-0017 Qué es coordinación]]

## Facciones · [[Facciones]]

100. ¿Cuántas facciones quieres y en qué se basan (bioma, cultura, mod)?
   - **R:** 
101. Además de la tormenta de arena del desierto, ¿qué tecnología o truco tendría cada facción?
   - **R:** 
102. ¿Las guerrillas atacan tus bases o solo caravanas y patrullas?
   - **R:** 
103. ¿Las facciones crecen con el tiempo o según tu fuerza?
   - **R:** 
104. ¿Se pueden aliar o contratar? ¿Pueden traicionarte?
   - **R:** 
105. ¿Hay diplomacia (tratados, rehenes, tributo entre facciones)?
   - **R:** 
106. ¿Las facciones conquistan aldeas por su cuenta?
   - **R:** 
107. ¿Una facción puede desaparecer? ¿Renace?
   - **R:** 
108. ¿Los guardias enemigos usan el mismo motor que los tuyos?
   - **R:** 
109. ¿Cómo encuentras a una guerrilla: rastros, rumores, exploradores?
   - **R:** 
110. ¿La tormenta de arena solo corta el vuelo y la vista, o también afecta a tus guardias?
   - **R:** 
111. ¿Qué hace a un enemigo peligroso incluso para un jugador muy fuerte, además de la tormenta?
   - **R:** 

## Naval aéreo y asedio · [[Naval aéreo y asedio]]

112. Del 1 al 10, ¿qué prioridad tiene lo naval frente al combate en tierra?
   - **R:** 
113. ¿Prefieres Small Ships (habría que agregarlo al modpack) o construir sobre Create Aeronautics, que ya tienes?
   - **R:** 
114. ¿Los guardias tripulan barcos, abordan y reparan?
   - **R:** 
115. ¿El arpón y los abordajes (capturar barcos) son más importantes que hundirlos?
   - **R:** 
116. ¿Los guardias deben poder poner y romper bloques (ingenieros: trincheras, muros, puentes)? Es una decisión grande.
   - **R:** 
117. ¿Asedio a fortalezas: TNT, catapultas, escaleras, túneles?
   - **R:** 
118. ¿Los guardias deben defender murallas que construyes tú?
   - **R:** 
119. ¿Torretas de Create o cañones de Create Big Cannons operados por guardias?
   - **R:** 
120. ¿Lo aéreo (globos, naves de Aeronautics) entra alguna vez?
   - **R:** 

## Aldeanos y población · [[Aldeanos y población]]

121. ¿De dónde salen los reclutas: aldeanos, guardias existentes, contratados?
   - **R:** 
122. ¿La población de tus aldeas limita el ejército?
   - **R:** 
123. ¿Los aldeanos sienten la guerra (miedo, mudanza, rabia)?
   - **R:** 
124. ¿Proteger una aldea da reputación y tributo?
   - **R:** 
125. ¿MCA (familias, matrimonios) se conecta con los guardias?
   - **R:** 
126. ¿Los guardias tienen familia en la aldea y eso pesa en su moral?
   - **R:** 

## Interfaz · [[Interfaz]]

127. ¿El Battle Planner es un mapa en pantalla, una mesa física en el mundo, o ambos?
   - **R:** 
128. ¿La planificación pausa el juego o es en tiempo real?
   - **R:** 
129. ¿Qué debe mostrar el HUD durante una batalla?
   - **R:** 
130. ¿Cuántas teclas como máximo?
   - **R:** 
131. ¿Quieres un tutorial o que se aprenda jugando?
   - **R:** 
132. ¿Cómo das una orden rápida en medio del combate?
   - **R:** 
133. ¿El Libro de Mando sigue siendo el centro o se reemplaza?
   - **R:** 
134. ¿Quieres ver el historial de una batalla después (repetición, informe)?
   - **R:** 

## Battle Planner · [[Battle Planner]]

135. ¿Qué tipos de misión quieres (escoltar, defender, asaltar, explorar, saquear, rescatar)?
   - **R:** 
136. ¿Las misiones las creas tú o te las propone el mundo (pedidos de aldeas, amenazas)?
   - **R:** 
137. ¿Qué informa el plan cuando falla?
   - **R:** 
138. ¿Campañas de varios días con objetivos encadenados?
   - **R:** 
139. ¿Puedes ver la batalla desde el planificador mientras ocurre?
   - **R:** 
140. ¿El plan puede incluir tiempos (a la hora X, al anochecer)?
   - **R:** 
141. ¿El compromiso del plan se pone por misión o por unidad?
   - **R:** 
142. ¿Puedes guardar planes y reutilizarlos?
   - **R:** 

## Multijugador · [[Multijugador]]

143. ¿Cooperativo, competitivo o ambos? ¿Cuál primero?
   - **R:** 
144. ¿Dos jugadores pueden compartir un ejército o mandar juntos?
   - **R:** 
145. ¿Guerra entre jugadores: con reglas (horarios, declaración) o libre?
   - **R:** 
146. ¿Qué impide que un jugador con mucho tiempo arrase a los nuevos?
   - **R:** 
147. ¿Los ejércitos siguen activos con el jugador desconectado?
   - **R:** 
148. ¿Cuántos guardias por jugador en un servidor?
   - **R:** 

## Equilibrio

149. ¿El mundo se vuelve más peligroso con los días, con tu fuerza o con nada?
   - **R:** 
150. ¿Quieres niveles de dificultad?
   - **R:** 
151. ¿Un jugador muy bien equipado debe poder vencer solo a un pelotón de guardias?
   - **R:** 
152. ¿Cuántos guardias es "mucho" para ti en una batalla normal? ¿Y en una épica?
   - **R:** 
153. ¿Qué ventaja máxima debe dar el mejor equipo frente al peor (por ejemplo, 1 contra 4)?
   - **R:** 
154. ¿Qué tan rápido debería perder un ejército si lo abandonas?
   - **R:** 

## Técnica

155. ¿Seguimos sobre Guard Villagers indefinidamente, o en algún punto hacemos un guardia propio?
   - **R:** 
156. ¿Cuántos guardias a la vez debe aguantar un servidor normal sin lag?
   - **R:** 
157. ¿Qué mods del modpack son intocables (tienen que funcionar sí o sí con Rally)?
   - **R:** 
158. ¿Publicarás el mod (Modrinth, CurseForge) o es privado para tu grupo?
   - **R:** 
159. ¿Cuándo quieres la primera versión jugable con amigos?
   - **R:** 

# Parte 2 · Ideas sueltas y tensiones (añadida el 2026-11-16)

## Visión de septiembre (tensiones) · [[Visión de septiembre]]

160. La visión de septiembre pedía logística simple, sin monedas ni caravanas. Hoy hay tributo, caravanas y sueldos. ¿Cuál vale ahora?
   - **R:** Tributo, caravanas y sueldos (vale lo de hoy). *(David, 2026-11-19)* → [[D-0002 Tensiones de septiembre resueltas]]
161. ¿Sigue en pie "sin jerarquía impuesta, el jugador nombra sus grupos como quiera", o el general y los comandantes son parte del juego?
   - **R:** El general y los comandantes son parte del juego. *(David, 2026-11-19)* → [[D-0002 Tensiones de septiembre resueltas]]
162. ¿Sigue en pie "el jugador es el general" (orden manual paso a paso) frente a un motor que decide maniobras solo? ¿Cómo conviven?
   - **R:** Un motor de maniobras muy corroboradas que sabe la mejor manera de ejecutar y coordinar en cada circunstancia, pero la decisión de qué se hace la tiene el jugador. Hay un modo "Base" con más libertad individual o, en grupo con líder, a través del líder. Si el jugador decide huir por miedo aunque lo mejor fuera pelear, Rally obedece y ejecuta la huida lo mejor posible. Es un espacio de discusión para hacerlo cohesivo. *(David, 2026-11-19)* → [[D-0002 Tensiones de septiembre resueltas]] · [[D-0003 El motor ejecuta, el jugador decide]]
163. ¿Sigue en pie "ninguna estrategia es una mecánica especial"? ¿La pinza y el ciclo mongol son combinaciones de piezas o maniobras con nombre?
   - **R:** Combinaciones de piezas. *(David, 2026-11-19)* → [[D-0002 Tensiones de septiembre resueltas]]

## Roles y clases · [[Roles y clases]]

164. ¿Qué tres roles quieres primero (artillero, mago, bombardero, soporte, nigromante, espía, asesino, ingeniero de campaña)?
   - **R:** 
165. ¿El rol sigue siendo "el objeto en la mano"? ¿Cambia si el guardia suelta el objeto?
   - **R:** 
166. ¿Un guardia puede tener rol principal y secundario?
   - **R:** 
167. ¿El nigromante revive a tus guardias muertos, convierte enemigos caídos, o ambos? ¿Cuánto duran?
   - **R:** 
168. ¿La magia (Ars Nouveau) debe ser rara y cara o común?
   - **R:** 
169. ¿El ingeniero de campaña puede poner bloques en batalla (trincheras, sacos de arena) aunque descartaste construir en paz?
   - **R:** 

## Ítems nuevos y crafteos · [[Ítems y crafteos]]

170. ¿Minas: explotan al pisarlas, las ponen tus guardias, las ven los enemigos?
   - **R:** 
171. ¿Lanzas: arma nueva de Rally (alcance largo, contra caballería) o la tomamos de otro mod?
   - **R:** 
172. ¿Qué otros escudos además del romano (pavés, escudo de torre, escudo con pinchos)?
   - **R:** 
173. ¿El saco de arena es un ítem del jugador, de los guardias o de ambos?
   - **R:** 
174. ¿Crafteos con materiales vanilla, con los de tu modpack (Create, Ice and Fire) o propios?
   - **R:** 
175. ¿Hay una mesa o taller de guerra propio de Rally?
   - **R:** 

## Artillería y torretas · [[Artillería y torretas]]

176. ¿Los guardias deben operar cañones de Create Big Cannons? ¿Moverlos o solo disparar?
   - **R:** 
177. ¿Torretas de Create con guardias dentro: prioridad alta o lejana?
   - **R:** 
178. ¿Catapultas y balistas propias de Rally, o solo las de otros mods?
   - **R:** 
179. ¿La artillería necesita munición de la logística?
   - **R:** 

## Coliseo · [[Coliseo]]

180. ¿El coliseo es parte de Rally o un mod aparte?
   - **R:** 
181. ¿Pelear en el coliseo da veteranía? ¿Puede morir un guardia ahí?
   - **R:** 
182. ¿Quién pelea: tus guardias, prisioneros, criaturas capturadas?
   - **R:** 
183. ¿Las apuestas son contra otros jugadores o contra la aldea?
   - **R:** 

## Crímenes y prisioneros · [[Crímenes y prisioneros]]

184. ¿Tus guardias pueden cometer crímenes (robar, asesinar) por orden tuya, o solo los enemigos?
   - **R:** 
185. ¿Qué se hace con un prisionero: cárcel, rescate, reclutarlo, coliseo?
   - **R:** 
186. ¿Las misiones de asesino y espía son del Battle Planner o algo aparte?
   - **R:** 
187. ¿La reputación con los aldeanos baja si tu ejército hace cosas malas?
   - **R:** 

## Historias y memoria · [[Historias y memoria]]

188. ¿Cómo quieres leer la historia de tus guardias: un libro, un registro en el Libro de Mando, rumores en la aldea?
   - **R:** 
189. ¿Los rumores viajan de verdad (con caravanas) o es solo un texto?
   - **R:** 
190. ¿Las palomas mensajeras son algo que de verdad quieres o una curiosidad?
   - **R:** 
191. ¿Invocar un jefe de Cataclysm con un ritual es un final de partida, un evento o un arma?
   - **R:** 

## Uniformes y estética · [[Uniformes y estética]]

192. ¿Los uniformes son solo estética o sirven para reconocer bandos (incluso entre jugadores)?
   - **R:** 
193. ¿El comandante se ve distinto del soldado? ¿Y el veterano?
   - **R:** 
194. ¿Pagarías arte por encargo (texturas, modelos)? ¿Para qué primero?
   - **R:** 

## Renegados y vigilancia · [[Renegados]]

195. ¿Los renegados (bandidos, kamikazes) siguen siendo un enemigo aparte o se vuelven una facción más?
   - **R:** 
196. ¿Cómo quieres que te avise la vigilancia cuando no estás (chat, sonido, nada)?
   - **R:** 
197. ¿Un grupo de vigilancia puede pedir refuerzos solo?
   - **R:** 

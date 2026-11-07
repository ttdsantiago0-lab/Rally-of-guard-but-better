# Datos del torneo

- `t1-cal.json`, `t1-rank/`, `t1-rank-report.md`, `t1-final/`: ronda 1. **No sirven para comparar composiciones**:
  Guard Villagers pisaba el kit de cada guardia en su primer tick (espada o ballesta, armadura al azar, escudo ~47 %).
  Se guardan como historia y como prueba del fallo (en `t1-final/` hay guardias "mele" parando golpes con escudo).
  Lo que sí siguen mostrando: la volea fallaba (8–22 % de acierto) y nadie atacaba a los creepers.
- `t2/`: duelos, volea y creepers (desde el commit 389473e, kits reales; cada corrida trae `extra.kit:*`).
- `t3/`: la ronda 1 rehecha (calibración, descarte, final, confirmación).
- `t16/`: moral, regla de intercambio (dos lotes de `t16-moral-linea` y uno de `t16-moral-r0`; corridos en el espacio
  de trabajo de Claude porque GitHub Actions estaba sin cupo).
- `t17/`: escolta (aldeano que cruza la arena). `172859` = prueba de humo. `173042` (zombis), `173208` (esqueletos),
  `173354` (mezcla) = escolta v1 sin pantalla; zombis y mezcla de esos tres salieron con la versión fácil (10 zombis,
  8 mezclados, n = 10: el servidor guarda su propia copia de los escenarios y no la renovaba). `174851`, `175127`,
  `175549` = con la pantalla de escudos (12 respuestas, n = 30). Los siguientes = pantalla + escudos cazadores.
- `t18/`: tortuga con escudos normales (`testudo:anyShield` = lo que valdría el escudo romano). Los dos primeros lotes
  (`181411`, `181740`) son con 16 esqueletos / olas de 12 (demasiado fácil) y con la primera versión de "miembro de la
  tortuga" (perdía la correa al salir 2,5 bloques); los siguientes, con 40 esqueletos / olas de 20.
- `t19/`: el escudo romano de verdad (rol `scutum`) y "tortuga y carga". El lote de olas es **parcial** (n ≈ 18): el
  "sprint" de la simulación dura 12 horas de juego y ese lote las pasó; desde ahí iba a velocidad normal. Corregido.
- `t20/`: escudo arriba contra blazes (`off:threats` = sin la regla).

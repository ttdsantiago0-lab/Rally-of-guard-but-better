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

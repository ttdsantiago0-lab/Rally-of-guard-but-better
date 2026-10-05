# Rally of the Guard — Arnés de simulación (E1, línea base)
**Cómo correrlo** (admin, mundo de pruebas): `/rally sim <A|B|C|D|ALL> [semilla=1] [repeticiones=3]`

Se construye una arena de piedra 64 bloques al este y a 200 de altura. Allí aparecen 20
guardias en LÍNEA (postura Agresivo) y 20 zombis con casco a 30 bloques, y el combate
corre en tiempo real hasta que cae un bando o pasan 2 minutos. Al terminar se limpia todo.

| Escenario | Guardias |
|---|---|
| A | 20 reclutas con espada de hierro |
| B | Como A, con rango de prueba 3 (`/rally debug rank`) |
| C | 20 arqueros con 64 flechas en la mochila |
| D | Como C, más 1 capitán de rango 5 con espada |

**Métricas** (promedio de las repeticiones): supervivientes, ticks de combate, bajas,
% de formación mantenida (muestreo 1/s), munición gastada, cambios de arma, distancia
media al enemigo más cercano, órdenes ejecutadas, disparos e impactos.

**Salida:** chat y `rally-sim/<escenario>-<semilla>.json` (carpeta del juego); al final, la comparación A, C, B, D.

**Qué esperar.** Hoy (E1): B ≈ A y D ≈ C, porque el rango no cambia nada hasta E2 (es la
línea base). Tras E2: D > B > A, y C ≥ A en distancia al enemigo y en bajas (menos). Si no
se cumple, se reporta tal cual.

| Escenario | Supervivientes | Bajas | Combate (ticks) | Formación % | Distancia | Disparos | Impactos |
|---|---|---|---|---|---|---|---|
| A (E1) | | | | | | | |
| B (E1) | | | | | | | |
| C (E1) | | | | | | | |
| D (E1) | | | | | | | |

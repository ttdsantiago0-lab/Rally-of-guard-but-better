# F83 · modelo sin entidades v0 (Lanchester por emparejamiento)

Generado por `tools/fuera_de_vista.py` en 7 s. Filas leídas 11733; usadas 5881 en 81 emparejamientos.

Modelo: dA/dt = −b·R·A^(2−p), dR/dt = −a·A·R^(2−q); p, q = ley del arma del que dispara (1 lineal, 2 cuadrada), pasos de 1 s hasta un bando a 0 o `capSeconds` (empate). a, b por emparejamiento (rejilla log); clave = Rally/GV, kit azul (fracciones de `extra.kit:*`), tier, nº azul, enemigo (+arma), terreno, nº rojo.

Leyes ajustadas: `arco` = 1.25, `cuerpo` = 2.0, `mixto` = 1.75, `r:cuerpo` = 1.5, `r:pistola` = 1.25, `r:tiro` = 1.5

Excluidas (no campo abierto o jefes): jefes 1396, refugio 1392, espejo guardia v guardia 591, aldea 480, montados 382, instrucción 259, oleadas 77, escolta 64, marcha 28; ignoradas: control/stuck/retried 1183

## Totales

| validación | n | ganador acertado | error medio vivos azules (fracción del bando) | (de 8) | error medio segundos |
|---|---|---|---|---|---|
| dentro de muestra (semillas pares) | 2942 | 85.4 % | 0.152 | 1.21 | 15.7 |
| fuera de muestra (semillas impares) | 2933 | 84.5 % | 0.153 | 1.23 | 16.0 |
| deja fuera un tamaño (rojos ≤ 32 → ≥ 48) | 1410 | 77.7 % | 0.278 | 2.23 | 25.0 |

Techo de acierto de ganador de cualquier modelo determinista por emparejamiento (clase mayoritaria, semillas pares): 85.4 %.

## Por emparejamiento (fuera de muestra)

| emparejamiento | n | ganador acertado | error vivos (fracción) | sesgo vivos | error s | sesgo s | a | b |
|---|---|---|---|---|---|---|---|---|
| gv 12 [bow=0.3,ironLegs=0.7,iron_sword=0.7,shield=0.7] v 32 pillager[scguns:pax] · forest | 15 | 67 % | 0.08 | -0.08 | 13 | +13 | 0.24 | 0.018 |
| gv 12 [bow=1.0] v 48 pillager[scguns:pax] · flat | 15 | 100 % | 0.00 | +0.00 | 2 | -2 | 0.029 | 0.022 |
| gv 16 [bow=0.5,ironLegs=0.5,iron_sword=0.5,shield=0.5] v 32 pillager[scguns:pax] · forest | 45 | 67 % | 0.10 | -0.10 | 4 | -3 | 0.36 | 0.036 |
| gv 16 [bow=1.0] v 48 pillager[scguns:pax] · flat | 29 | 100 % | 0.00 | +0.00 | 0 | -0 | 0.022 | 0.019 |
| gv 24 [bow=0.5,ironLegs=0.5,iron_sword=0.5,shield=0.5] v 32 pillager[scguns:pax] · forest | 15 | 100 % | 0.16 | -0.14 | 20 | -20 | 0.28 | 0.036 |
| gv 24 [bow=1.0] v 48 pillager[scguns:pax] · flat | 15 | 100 % | 0.00 | +0.00 | 2 | -2 | 0.036 | 0.028 |
| gv 32 [bow=1.0] v 48 pillager[scguns:pax] · flat | 15 | 100 % | 0.00 | +0.00 | 2 | +0 | 0.022 | 0.021 |
| gv 48 [bow=1.0] v 48 pillager[scguns:pax] · flat | 15 | 100 % | 0.00 | +0.00 | 6 | -5 | 0.036 | 0.041 |
| gv 8 [bow=0.5,ironLegs=0.5,iron_sword=0.5,shield=0.5] v 32 pillager[scguns:pax] · forest | 45 | 96 % | 0.02 | -0.02 | 5 | -5 | 0.6 | 0.028 |
| gv 8 [bow=1.0] v 48 pillager[scguns:pax] · flat | 30 | 100 % | 0.00 | +0.00 | 0 | +0 | 0.021 | 0.015 |
| rally 8 [bow=0.2,ironLegs=0.8,iron_sword=0.8,shield=0.8] v 32 pillager[scguns:pax] · forest | 15 | 100 % | 0.13 | -0.01 | 4 | +2 | 0.15 | 0.00084 |
| rally 8 [bow=0.2,ironLegs=0.8,iron_sword=0.8,shield=0.8] v 48 pillager[scguns:pax] · forest | 15 | 93 % | 0.25 | +0.06 | 6 | -1 | 0.23 | 0.0011 |
| rally 8 [bow=0.5,ironLegs=0.5,iron_sword=0.5,shield=0.5] v 12 minecraft:wither_skeleton · flat | 8 | 100 % | 0.00 | -0.00 | 1 | -1 | 0.088 | 1.3e-05 |
| rally 8 [bow=0.5,ironLegs=0.5,iron_sword=0.5,shield=0.5] v 16 pillager[scguns:pax] · forest | 15 | 100 % | 0.16 | +0.05 | 5 | -1 | 0.053 | 0.00092 |
| rally 8 [bow=0.5,ironLegs=0.5,iron_sword=0.5,shield=0.5] v 16 pillager[scguns:pax] · village | 327 | 82 % | 0.16 | +0.02 | 38 | -3 | 0.02 | 0.00074 |
| rally 8 [bow=0.5,ironLegs=0.5,iron_sword=0.5,shield=0.5] v 16 vindicator · corridors | 15 | 80 % | 0.28 | -0.00 | 51 | +37 | 0.027 | 0.0036 |
| rally 8 [bow=0.5,ironLegs=0.5,iron_sword=0.5,shield=0.5] v 16 vindicator · flat | 195 | 100 % | 0.13 | +0.00 | 10 | +0 | 0.063 | 0.0046 |
| rally 8 [bow=0.5,ironLegs=0.5,iron_sword=0.5,shield=0.5] v 16 vindicator · forest | 30 | 100 % | 0.01 | -0.00 | 5 | +0 | 0.077 | 0.01 |
| rally 8 [bow=0.5,ironLegs=0.5,iron_sword=0.5,shield=0.5] v 21 zombie · flat | 8 | 100 % | 0.00 | -0.00 | 1 | -0 | 0.11 | 5.3e-07 |
| rally 8 [bow=0.5,ironLegs=0.5,iron_sword=0.5,shield=0.5] v 24 creeper · flat | 8 | 100 % | 0.00 | -0.00 | 2 | -1 | 0.092 | 1.3e-05 |
| rally 8 [bow=0.5,ironLegs=0.5,iron_sword=0.5,shield=0.5] v 24 minecraft:drowned · flat | 8 | 100 % | 0.00 | -0.00 | 1 | +0 | 0.092 | 4.1e-06 |
| rally 8 [bow=0.5,ironLegs=0.5,iron_sword=0.5,shield=0.5] v 24 minecraft:spider · flat | 7 | 100 % | 0.00 | -0.00 | 1 | -0 | 0.12 | 2.2e-06 |
| rally 8 [bow=0.5,ironLegs=0.5,iron_sword=0.5,shield=0.5] v 25 minecraft:spider · flat | 1 | 100 % | 0.00 | -0.00 | 1 | +1 | 0.11 | 5.3e-07 |
| rally 8 [bow=0.5,ironLegs=0.5,iron_sword=0.5,shield=0.5] v 32 minecraft:stray · flat | 8 | 100 % | 0.00 | -0.00 | 1 | +1 | 0.084 | 8.8e-06 |
| rally 8 [bow=0.5,ironLegs=0.5,iron_sword=0.5,shield=0.5] v 32 pillager[scguns:pax] · flat | 30 | 97 % | 0.21 | +0.06 | 3 | -1 | 0.11 | 0.0012 |
| rally 8 [bow=0.5,ironLegs=0.5,iron_sword=0.5,shield=0.5] v 32 pillager[scguns:pax] · forest | 180 | 100 % | 0.17 | +0.01 | 5 | -1 | 0.092 | 0.00092 |
| rally 8 [bow=0.5,ironLegs=0.5,iron_sword=0.5,shield=0.5] v 32 pillager[scguns:pax] · village | 140 | 55 % | 0.25 | +0.03 | 40 | +8 | 0.039 | 0.0012 |
| rally 8 [bow=0.5,ironLegs=0.5,iron_sword=0.5,shield=0.5] v 32 vindicator · corridors | 15 | 60 % | 0.07 | -0.07 | 61 | +19 | 0.046 | 0.0034 |
| rally 8 [bow=0.5,ironLegs=0.5,iron_sword=0.5,shield=0.5] v 32 vindicator · flat | 15 | 100 % | 0.00 | -0.00 | 3 | +3 | 0.1 | 0.0039 |
| rally 8 [bow=0.5,ironLegs=0.5,iron_sword=0.5,shield=0.5] v 32 vindicator · forest | 134 | 89 % | 0.11 | -0.00 | 25 | -6 | 0.074 | 0.0033 |
| rally 8 [bow=0.5,ironLegs=0.5,iron_sword=0.5,shield=0.5] v 34 skeleton · flat | 8 | 100 % | 0.00 | -0.00 | 1 | +1 | 0.084 | 1.9e-05 |
| rally 8 [bow=0.5,ironLegs=0.5,iron_sword=0.5,shield=0.5] v 40 minecraft:husk · flat | 8 | 100 % | 0.00 | -0.00 | 1 | -0 | 0.1 | 1.9e-07 |
| rally 8 [bow=0.5,ironLegs=0.5,iron_sword=0.5,shield=0.5] v 48 pillager[scguns:pax] · forest | 45 | 98 % | 0.18 | +0.01 | 6 | +2 | 0.11 | 0.00081 |
| rally 8 [bow=0.5,ironLegs=0.5,iron_sword=0.5,shield=0.5] v 48 vindicator · forest | 30 | 33 % | 0.23 | -0.23 | 36 | -0 | 0.063 | 0.0023 |
| rally 8 [bow=0.5,ironLegs=0.5,iron_sword=0.5,shield=0.5] v 6 minecraft:vindicator · flat | 8 | 100 % | 0.00 | -0.00 | 0 | +0 | 0.081 | 6e-06 |
| rally 8 [bow=0.5,ironLegs=0.5,iron_sword=0.5,shield=0.5] v 64 pillager[scguns:pax] · forest | 15 | 100 % | 0.20 | -0.18 | 6 | +3 | 0.17 | 0.0013 |
| rally 8 [bow=0.5,ironLegs=0.5,iron_sword=0.5,shield=0.5] v 64 vindicator · forest | 30 | 50 % | 0.13 | -0.13 | 33 | -4 | 0.063 | 0.0015 |
| rally 8 [bow=0.5,iron_sword=0.5] v 12 zombie · flat | 8 | 100 % | 0.00 | -0.00 | 1 | -1 | 0.096 | 1.5e-05 |
| rally 8 [bow=0.5,iron_sword=0.5] v 24 creeper · flat | 16 | 100 % | 0.10 | -0.05 | 1 | +0 | 0.11 | 0.0032 |
| rally 8 [bow=0.5,iron_sword=0.5] v 40 skeleton · flat | 16 | 100 % | 0.06 | -0.00 | 2 | -1 | 0.11 | 0.00027 |
| rally 8 [bow=0.8,ironLegs=0.2,iron_sword=0.2,shield=0.2] v 32 pillager[scguns:pax] · forest | 15 | 100 % | 0.13 | -0.02 | 5 | -0 | 0.043 | 0.0024 |
| rally 8 [bow=0.8,ironLegs=0.2,iron_sword=0.2,shield=0.2] v 32 pillager[scguns:pax] · village | 15 | 67 % | 0.19 | -0.01 | 27 | +27 | 0.0036 | 0.00046 |
| rally 8 [bow=1.0] v 13 zombie · flat | 8 | 100 % | 0.00 | -0.00 | 1 | +0 | 0.077 | 1.9e-05 |
| rally 8 [bow=1.0] v 16 pillager[scguns:pax] · forest | 15 | 100 % | 0.10 | -0.02 | 2 | -0 | 0.039 | 0.0039 |
| rally 8 [bow=1.0] v 24 creeper · flat | 8 | 100 % | 0.00 | -0.00 | 1 | -1 | 0.055 | 1.9e-05 |
| rally 8 [bow=1.0] v 32 pillager[scguns:pax] · flat | 105 | 86 % | 0.29 | +0.02 | 5 | +0 | 0.041 | 0.0032 |
| rally 8 [bow=1.0] v 32 pillager[scguns:pax] · forest | 45 | 73 % | 0.11 | -0.11 | 6 | -1 | 0.068 | 0.015 |
| rally 8 [bow=1.0] v 46 skeleton · flat | 8 | 100 % | 0.00 | -0.00 | 1 | +0 | 0.051 | 1.9e-05 |
| rally 8 [bow=1.0] v 48 pillager[scguns:pax] · flat | 570 | 76 % | 0.27 | +0.02 | 9 | +3 | 0.043 | 0.0028 |
| rally 8 [ironLegs=1.0,iron_sword=1.0,shield=1.0] v 10 blaze · flat | 8 | 100 % | 0.02 | -0.02 | 7 | +3 | 0.036 | 0.00024 |
| rally 8 [ironLegs=1.0,iron_sword=1.0,shield=1.0] v 16 pillager[scguns:pax] · forest | 15 | 93 % | 0.22 | +0.02 | 3 | -1 | 0.084 | 0.0016 |
| rally 8 [ironLegs=1.0,iron_sword=1.0,shield=1.0] v 16 pillager[scguns:pax] · village | 75 | 76 % | 0.10 | +0.01 | 44 | +2 | 0.021 | 0.00028 |
| rally 8 [ironLegs=1.0,iron_sword=1.0,shield=1.0] v 24 creeper · flat | 8 | 100 % | 0.00 | -0.00 | 10 | -4 | 0.11 | 5.3e-07 |
| rally 8 [ironLegs=1.0,iron_sword=1.0,shield=1.0] v 28 skeleton · flat | 8 | 25 % | 0.09 | -0.04 | 28 | +28 | 0.029 | 0.00015 |
| rally 8 [ironLegs=1.0,iron_sword=1.0,shield=1.0] v 32 pillager[scguns:pax] · forest | 15 | 87 % | 0.28 | -0.05 | 4 | +0 | 0.17 | 0.0015 |
| rally 8 [ironLegs=1.0,iron_sword=1.0,shield=1.0] v 32 pillager[scguns:pax] · village | 75 | 63 % | 0.18 | +0.01 | 36 | -3 | 0.046 | 0.00053 |
| rally 8 [ironLegs=1.0,iron_sword=1.0,shield=1.0] v 34 zombie · flat | 1 | 100 % | 0.00 | -0.00 | 5 | -5 | 0.16 | 0.0027 |
| rally 8 [ironLegs=1.0,iron_sword=1.0,shield=1.0] v 37 zombie · flat | 3 | 100 % | 0.00 | +0.00 | 19 | -16 | 0.84 | 0.018 |
| rally 8 [ironLegs=1.0,iron_sword=1.0,shield=1.0] v 6 blaze · flat | 8 | 100 % | 0.00 | -0.00 | 3 | -1 | 0.03 | 1.5e-05 |
| rally 8 [iron_sword=1.0] v 12 guard · flat | 40 | 95 % | 0.01 | -0.01 | 9 | -1 | 0.024 | 0.026 |
| rally 8 [iron_sword=1.0] v 12 vindicator · corridors | 75 | 83 % | 0.07 | -0.07 | 20 | -6 | 0.084 | 0.023 |
| rally 8 [iron_sword=1.0] v 12 vindicator · flat | 75 | 100 % | 0.00 | +0.00 | 1 | -0 | 0.051 | 0.043 |
| rally 8 [iron_sword=1.0] v 12 vindicator · village | 30 | 70 % | 0.14 | -0.14 | 60 | +16 | 0.051 | 0.012 |
| rally 8 [iron_sword=1.0] v 16 pillager[scguns:pax] · village | 15 | 100 % | 0.00 | +0.00 | 2 | +1 | 0.081 | 0.028 |
| rally 8 [iron_sword=1.0] v 24 creeper · flat | 6 | 67 % | 0.08 | +0.01 | 8 | +8 | 0.029 | 0.00084 |
| rally 8 [iron_sword=1.0] v 26 zombie · flat | 1 | 100 % | 0.00 | -0.00 | 2 | -2 | 0.15 | 0.0022 |
| rally 8 [iron_sword=1.0] v 28 skeleton · flat | 8 | 100 % | 0.12 | -0.08 | 4 | +3 | 0.12 | 0.0011 |
| rally 8 [iron_sword=1.0] v 29 zombie · flat | 1 | 100 % | 0.01 | +0.01 | 1 | -1 | 0.16 | 0.0043 |
| rally 8 [iron_sword=1.0] v 31 zombie · flat | 5 | 100 % | 0.00 | +0.00 | 3 | +2 | 0.32 | 0.011 |
| rally 8 [iron_sword=1.0] v 32 pillager[scguns:pax] · village | 15 | 100 % | 0.00 | +0.00 | 1 | -0 | 0.12 | 0.016 |
| rally 8 [iron_sword=1.0] v 6 guard · flat | 60 | 100 % | 0.05 | -0.00 | 5 | -0 | 0.044 | 0.0018 |

## Los 10 peor predichos (fuera de muestra: (1 − acierto) + error de vivos)

- rally 8 [bow=0.5,ironLegs=0.5,iron_sword=0.5,shield=0.5] v 48 vindicator · forest — acierto 33 %, error 0.23: resultado partido (blue 31/60, red 25/60); kite 24/corrida; stand-off 17/corrida; terreno forest
- rally 8 [ironLegs=1.0,iron_sword=1.0,shield=1.0] v 28 skeleton · flat — acierto 25 %, error 0.09: límite de tiempo 56 %; resultado partido (draw 9/16, blue 7/16)
- rally 8 [bow=0.5,ironLegs=0.5,iron_sword=0.5,shield=0.5] v 32 pillager[scguns:pax] · village — acierto 55 %, error 0.25: límite de tiempo 20 %; stand-off 42/corrida; etiquetas standoff:losRelease; terreno village
- rally 8 [bow=0.5,ironLegs=0.5,iron_sword=0.5,shield=0.5] v 64 vindicator · forest — acierto 50 %, error 0.13: resultado partido (red 32/60, blue 25/60); kite 22/corrida; stand-off 17/corrida; terreno forest
- rally 8 [ironLegs=1.0,iron_sword=1.0,shield=1.0] v 32 pillager[scguns:pax] · village — acierto 63 %, error 0.18: límite de tiempo 35 %; resultado partido (blue 93/150, draw 52/150); etiquetas standoff:losRelease; terreno village
- rally 8 [bow=0.8,ironLegs=0.2,iron_sword=0.2,shield=0.2] v 32 pillager[scguns:pax] · village — acierto 67 %, error 0.19: límite de tiempo 60 %; resultado partido (draw 18/30, blue 8/30); stand-off 78/corrida; etiquetas standoff:losRelease; terreno village
- rally 8 [bow=1.0] v 48 pillager[scguns:pax] · flat — acierto 76 %, error 0.27: stand-off 155/corrida
- rally 8 [bow=0.5,ironLegs=0.5,iron_sword=0.5,shield=0.5] v 16 vindicator · corridors — acierto 80 %, error 0.28: terreno corridors
- rally 8 [bow=0.5,ironLegs=0.5,iron_sword=0.5,shield=0.5] v 32 vindicator · corridors — acierto 60 %, error 0.07: límite de tiempo 27 %; resultado partido (red 16/30, draw 8/30); terreno corridors
- gv 16 [bow=0.5,ironLegs=0.5,iron_sword=0.5,shield=0.5] v 32 pillager[scguns:pax] · forest — acierto 67 %, error 0.10: resultado partido (red 60/90, blue 30/90); terreno forest

## Deja fuera un tamaño, por clave

| clave (sin nº rojo) | rojos predichos | n | ganador acertado | error vivos (fracción) |
|---|---|---|---|---|
| rally 8 [bow=0.2,ironLegs=0.8,iron_sword=0.8,shield=0.8] v pillager[scguns:pax] · forest | 48 | 30 | 93 % | 0.23 |
| rally 8 [bow=0.5,ironLegs=0.5,iron_sword=0.5,shield=0.5] v pillager[scguns:pax] · forest | 48 | 90 | 96 % | 0.25 |
| rally 8 [bow=0.5,ironLegs=0.5,iron_sword=0.5,shield=0.5] v vindicator · forest | 48 | 60 | 42 % | 0.20 |
| rally 8 [bow=0.5,ironLegs=0.5,iron_sword=0.5,shield=0.5] v pillager[scguns:pax] · forest | 64 | 30 | 93 % | 0.31 |
| rally 8 [bow=0.5,ironLegs=0.5,iron_sword=0.5,shield=0.5] v vindicator · forest | 64 | 60 | 53 % | 0.12 |
| rally 8 [bow=1.0] v pillager[scguns:pax] · flat | 48 | 1140 | 79 % | 0.29 |

# F111 · modelo sin entidades v1 (P(azul gana) = logística del margen final de la v0)

Mismas filas, claves, exclusiones, leyes y a, b que la v0 de arriba. Margen m = vivos azules/azules iniciales − vivos rojos/rojos iniciales al final de la simulación v0 (bando a 0 o `capSeconds`). P = 1/(1+e^(−k·m)), una sola pendiente k global (sin término independiente) que minimiza el Brier en las semillas pares. Empate (tope de tiempo) = "no gana".

Pendiente ajustada k = 3.055 (deja fuera un tamaño: k = 4.335, ajustada en rojos ≤ 32).

## Brier

| validación | n | Brier v1 | referencia: clase mayoritaria del emparejamiento (0/1) | frecuencia del emparejamiento (informativo) | moneda |
|---|---|---|---|---|---|
| dentro de muestra (pares) | 2942 | 0.1176 | — | — | 0.25 |
| fuera de muestra (pares → impares) | 2933 | 0.1210 | 0.1483 | 0.1086 | 0.25 |
| deja fuera un tamaño (rojos ≤ 32 → ≥ 48) | 1410 | 0.1764 | 0.2234 | 0.1821 | 0.25 |

## Calibración fuera de muestra (5 tramos de P)

| tramo de P | n | P media predicha | frecuencia real de victoria azul | diferencia |
|---|---|---|---|---|
| 0.0–0.2 | 339 | 0.111 | 0.068 | +0.043 |
| 0.2–0.4 | 260 | 0.302 | 0.215 | +0.086 |
| 0.4–0.6 | 3 | 0.452 | 0.000 | +0.452 |
| 0.6–0.8 | 326 | 0.764 | 0.715 | +0.049 |
| 0.8–1.0 | 2005 | 0.879 | 0.866 | +0.013 |

Error de vivos azules (sin cambio, es el de la v0): fuera de muestra 0.153 (n 2933); deja fuera un tamaño 0.278 (n 1410).

Máxima diferencia de calibración en tramos con n ≥ 50: 0.086.

Veredicto (regla de F111): **APRUEBA** — Brier v1 0.1210 ≤ referencia 0.1483; tramos n ≥ 50 todos a ≤ 0,10.


# F113 · modelo sin entidades v2 (término independiente + factor de daño azul; vivos con escala)

Mismas filas, claves, particiones y a, b que la v0/v1. P = 1/(1+e^(−(k·m + b))), m = margen final de la v0 con el ritmo de bajas del azul multiplicado por d (un solo d global, nada por emparejamiento). d por rejilla log 1/4..4 + sección áurea; k, b por descenso por coordenadas, minimizando el Brier en pares. Vivos v2 = min(1, s · vivos v0), una escala global s ajustada en pares (error medio absoluto).

Filas: ajuste (pares) 2942, prueba (impares) 2933; deja fuera un tamaño: ajuste 959, prueba 1410.

Prueba de vida (b = 0, d = 1, k reajustado = 3.057): Brier fuera 0.1210 frente a v1 0.1210 → **VÁLIDA** (±0,001).

Parámetros v2: d = 0.891, k = 3.307, b = -0.022 (deja fuera un tamaño: d = 0.309, k = 1.561, b = +2.971). Escala de vivos s = 1.081 (deja fuera un tamaño: 1.096).

## Brier

| validación | n | v1 | v2 | frecuencia del emparejamiento (informativo) |
|---|---|---|---|---|
| dentro de muestra (pares) | 2942 | — | 0.1164 | — |
| fuera de muestra (pares → impares) | 2933 | 0.1210 | 0.1196 | 0.1086 |
| deja fuera un tamaño (rojos ≤ 32 → ≥ 48) | 1410 | 0.1764 | 0.1800 | 0.1821 |

## Calibración v2 fuera de muestra (5 tramos de P)

| tramo de P | n | P media predicha | frecuencia real de victoria azul | diferencia |
|---|---|---|---|---|
| 0.0–0.2 | 434 | 0.100 | 0.104 | -0.003 |
| 0.2–0.4 | 168 | 0.244 | 0.202 | +0.041 |
| 0.4–0.6 | 15 | 0.590 | 1.000 | -0.410 |
| 0.6–0.8 | 912 | 0.762 | 0.745 | +0.018 |
| 0.8–1.0 | 1404 | 0.915 | 0.909 | +0.006 |

Signo de la diferencia por tramo (con n > 0): −+−++. Máxima diferencia con n ≥ 50: 0.041.

## Vivos azules (error medio absoluto, fracción del bando)

| validación | n | v0/v1 | v2 (escala s) |
|---|---|---|---|
| fuera de muestra (pares → impares) | 2933 | 0.153 | 0.151 |
| deja fuera un tamaño (rojos ≤ 32 → ≥ 48) | 1410 | 0.278 | 0.268 |

Veredicto (regla de F113): **NO** — Brier fuera 0.1196 (≤ 0,11 aprueba, ≥ 0,115 NO), dejando fuera un tamaño 0.1800 (≤ 0,176), peor tramo n ≥ 50 0.041 (≤ 0,08). **VIVOS NO APRUEBA** — error de vivos v2 fuera 0.151 (≤ 0,12).


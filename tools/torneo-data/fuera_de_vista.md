# F83 · modelo sin entidades v0 (Lanchester por emparejamiento)

Generado por `tools/fuera_de_vista.py` en 5 s. Filas leídas 8340; usadas 3607 en 68 emparejamientos.

Modelo: dA/dt = −b·R·A^(2−p), dR/dt = −a·A·R^(2−q); p, q = ley del arma del que dispara (1 lineal, 2 cuadrada), pasos de 1 s hasta un bando a 0 o `capSeconds` (empate). a, b por emparejamiento (rejilla log); clave = Rally/GV, kit azul (fracciones de `extra.kit:*`), tier, nº azul, enemigo (+arma), terreno, nº rojo.

Leyes ajustadas: `arco` = 2.0, `cuerpo` = 1.25, `mixto` = 1.5, `r:cuerpo` = 1.5, `r:pistola` = 1.5, `r:tiro` = 1.75

Excluidas (no campo abierto o jefes): jefes 1221, refugio 1212, aldea 480, montados 382, instrucción 259, oleadas 77, escolta 64, marcha 28, espejo guardia v guardia 8; ignoradas: control/stuck/retried 1002

## Totales

| validación | n | ganador acertado | error medio vivos azules (fracción del bando) | (de 8) | error medio segundos |
|---|---|---|---|---|---|
| dentro de muestra (semillas pares) | 1802 | 83.0 % | 0.162 | 1.29 | 17.1 |
| fuera de muestra (semillas impares) | 1799 | 81.8 % | 0.164 | 1.31 | 17.1 |
| deja fuera un tamaño (rojos ≤ 32 → ≥ 48) | 780 | 41.5 % | 0.374 | 2.99 | 51.3 |

Techo de acierto de ganador de cualquier modelo determinista por emparejamiento (clase mayoritaria, semillas pares): 83.0 %.

## Por emparejamiento (fuera de muestra)

| emparejamiento | n | ganador acertado | error vivos (fracción) | sesgo vivos | error s | sesgo s | a | b |
|---|---|---|---|---|---|---|---|---|
| gv 12 [bow=0.3,ironLegs=0.7,iron_sword=0.7,shield=0.7] v 32 pillager[scguns:pax] · forest | 15 | 67 % | 0.08 | -0.08 | 22 | -22 | 0.34 | 0.088 |
| gv 12 [bow=1.0] v 48 pillager[scguns:pax] · flat | 15 | 100 % | 0.00 | +0.00 | 0 | +0 | 0.32 | 0.022 |
| gv 16 [bow=0.5,ironLegs=0.5,iron_sword=0.5,shield=0.5] v 32 pillager[scguns:pax] · forest | 45 | 67 % | 0.10 | -0.10 | 19 | -19 | 0.22 | 0.088 |
| gv 16 [bow=1.0] v 48 pillager[scguns:pax] · flat | 29 | 100 % | 0.00 | +0.00 | 3 | -3 | 0.46 | 0.043 |
| gv 24 [bow=0.5,ironLegs=0.5,iron_sword=0.5,shield=0.5] v 32 pillager[scguns:pax] · forest | 15 | 100 % | 0.12 | -0.10 | 22 | -22 | 0.19 | 0.1 |
| gv 24 [bow=1.0] v 48 pillager[scguns:pax] · flat | 15 | 100 % | 0.00 | +0.00 | 1 | -1 | 0.33 | 0.032 |
| gv 32 [bow=1.0] v 48 pillager[scguns:pax] · flat | 15 | 100 % | 0.00 | +0.00 | 10 | -10 | 0.68 | 0.092 |
| gv 48 [bow=1.0] v 48 pillager[scguns:pax] · flat | 15 | 100 % | 0.00 | +0.00 | 15 | -15 | 0.46 | 0.088 |
| gv 8 [bow=0.5,ironLegs=0.5,iron_sword=0.5,shield=0.5] v 32 pillager[scguns:pax] · forest | 45 | 96 % | 0.02 | -0.02 | 3 | +1 | 0.13 | 0.019 |
| gv 8 [bow=1.0] v 48 pillager[scguns:pax] · flat | 30 | 100 % | 0.00 | +0.00 | 1 | -1 | 0.32 | 0.02 |
| rally 8 [bow=0.2,ironLegs=0.8,iron_sword=0.8,shield=0.8] v 32 pillager[scguns:pax] · forest | 15 | 100 % | 0.13 | -0.01 | 4 | +2 | 0.039 | 0.003 |
| rally 8 [bow=0.2,ironLegs=0.8,iron_sword=0.8,shield=0.8] v 48 pillager[scguns:pax] · forest | 15 | 93 % | 0.25 | +0.06 | 7 | +0 | 0.046 | 0.0039 |
| rally 8 [bow=0.5,ironLegs=0.5,iron_sword=0.5,shield=0.5] v 12 minecraft:wither_skeleton · flat | 8 | 100 % | 0.00 | -0.00 | 1 | -1 | 0.063 | 6e-06 |
| rally 8 [bow=0.5,ironLegs=0.5,iron_sword=0.5,shield=0.5] v 16 pillager[scguns:pax] · forest | 15 | 100 % | 0.16 | +0.05 | 5 | -0 | 0.038 | 0.0019 |
| rally 8 [bow=0.5,ironLegs=0.5,iron_sword=0.5,shield=0.5] v 16 pillager[scguns:pax] · village | 267 | 81 % | 0.16 | +0.01 | 40 | -1 | 0.015 | 0.0017 |
| rally 8 [bow=0.5,ironLegs=0.5,iron_sword=0.5,shield=0.5] v 21 zombie · flat | 8 | 100 % | 0.00 | -0.00 | 0 | -0 | 0.068 | 6e-06 |
| rally 8 [bow=0.5,ironLegs=0.5,iron_sword=0.5,shield=0.5] v 24 creeper · flat | 8 | 100 % | 0.00 | -0.00 | 2 | -0 | 0.057 | 1.9e-05 |
| rally 8 [bow=0.5,ironLegs=0.5,iron_sword=0.5,shield=0.5] v 24 minecraft:drowned · flat | 8 | 100 % | 0.00 | -0.00 | 1 | -0 | 0.06 | 8.8e-06 |
| rally 8 [bow=0.5,ironLegs=0.5,iron_sword=0.5,shield=0.5] v 24 minecraft:spider · flat | 7 | 100 % | 0.00 | -0.00 | 1 | -0 | 0.077 | 1.9e-06 |
| rally 8 [bow=0.5,ironLegs=0.5,iron_sword=0.5,shield=0.5] v 25 minecraft:spider · flat | 1 | 100 % | 0.00 | -0.00 | 1 | +1 | 0.068 | 6e-06 |
| rally 8 [bow=0.5,ironLegs=0.5,iron_sword=0.5,shield=0.5] v 32 minecraft:stray · flat | 8 | 100 % | 0.00 | -0.00 | 1 | +1 | 0.051 | 1.9e-05 |
| rally 8 [bow=0.5,ironLegs=0.5,iron_sword=0.5,shield=0.5] v 32 pillager[scguns:pax] · flat | 30 | 97 % | 0.21 | +0.06 | 4 | -1 | 0.065 | 0.0024 |
| rally 8 [bow=0.5,ironLegs=0.5,iron_sword=0.5,shield=0.5] v 32 pillager[scguns:pax] · forest | 105 | 100 % | 0.17 | -0.02 | 5 | +0 | 0.057 | 0.0021 |
| rally 8 [bow=0.5,ironLegs=0.5,iron_sword=0.5,shield=0.5] v 32 pillager[scguns:pax] · village | 140 | 55 % | 0.25 | +0.02 | 40 | +9 | 0.026 | 0.0024 |
| rally 8 [bow=0.5,ironLegs=0.5,iron_sword=0.5,shield=0.5] v 34 skeleton · flat | 8 | 100 % | 0.00 | -0.00 | 1 | +0 | 0.051 | 2.4e-05 |
| rally 8 [bow=0.5,ironLegs=0.5,iron_sword=0.5,shield=0.5] v 40 minecraft:husk · flat | 8 | 100 % | 0.00 | -0.00 | 1 | -1 | 0.057 | 1.9e-05 |
| rally 8 [bow=0.5,ironLegs=0.5,iron_sword=0.5,shield=0.5] v 48 pillager[scguns:pax] · forest | 45 | 98 % | 0.18 | +0.02 | 6 | +2 | 0.063 | 0.0016 |
| rally 8 [bow=0.5,ironLegs=0.5,iron_sword=0.5,shield=0.5] v 6 minecraft:vindicator · flat | 8 | 100 % | 0.00 | -0.00 | 0 | +0 | 0.068 | 6e-06 |
| rally 8 [bow=0.5,ironLegs=0.5,iron_sword=0.5,shield=0.5] v 64 pillager[scguns:pax] · forest | 15 | 100 % | 0.20 | -0.17 | 6 | +3 | 0.088 | 0.0027 |
| rally 8 [bow=0.5,iron_sword=0.5] v 12 zombie · flat | 8 | 100 % | 0.00 | -0.00 | 1 | -0 | 0.068 | 1.9e-05 |
| rally 8 [bow=0.5,iron_sword=0.5] v 24 creeper · flat | 16 | 100 % | 0.10 | -0.04 | 1 | +1 | 0.068 | 0.0038 |
| rally 8 [bow=0.5,iron_sword=0.5] v 40 skeleton · flat | 16 | 100 % | 0.06 | +0.00 | 2 | -0 | 0.063 | 0.00053 |
| rally 8 [bow=0.8,ironLegs=0.2,iron_sword=0.2,shield=0.2] v 32 pillager[scguns:pax] · forest | 15 | 100 % | 0.13 | -0.02 | 5 | +1 | 0.16 | 0.0017 |
| rally 8 [bow=0.8,ironLegs=0.2,iron_sword=0.2,shield=0.2] v 32 pillager[scguns:pax] · village | 15 | 67 % | 0.19 | -0.01 | 27 | +27 | 0.029 | 0.00063 |
| rally 8 [bow=1.0] v 13 zombie · flat | 8 | 100 % | 0.00 | -0.00 | 0 | -0 | 0.22 | 6e-06 |
| rally 8 [bow=1.0] v 16 pillager[scguns:pax] · forest | 15 | 100 % | 0.10 | -0.01 | 2 | -1 | 0.096 | 0.003 |
| rally 8 [bow=1.0] v 24 creeper · flat | 8 | 100 % | 0.00 | -0.00 | 1 | -0 | 0.2 | 6e-06 |
| rally 8 [bow=1.0] v 32 pillager[scguns:pax] · flat | 105 | 86 % | 0.29 | +0.02 | 5 | -1 | 0.15 | 0.0022 |
| rally 8 [bow=1.0] v 32 pillager[scguns:pax] · forest | 45 | 73 % | 0.11 | -0.11 | 6 | -0 | 0.38 | 0.012 |
| rally 8 [bow=1.0] v 46 skeleton · flat | 8 | 100 % | 0.00 | -0.00 | 1 | +0 | 0.29 | 8.8e-06 |
| rally 8 [bow=1.0] v 48 pillager[scguns:pax] · flat | 315 | 68 % | 0.29 | +0.00 | 10 | +4 | 0.21 | 0.002 |
| rally 8 [ironLegs=1.0,iron_sword=1.0,shield=1.0] v 10 blaze · flat | 8 | 100 % | 0.01 | -0.01 | 8 | +4 | 0.022 | 0.00084 |
| rally 8 [ironLegs=1.0,iron_sword=1.0,shield=1.0] v 16 pillager[scguns:pax] · forest | 15 | 93 % | 0.22 | +0.01 | 3 | -1 | 0.036 | 0.0057 |
| rally 8 [ironLegs=1.0,iron_sword=1.0,shield=1.0] v 16 pillager[scguns:pax] · village | 75 | 76 % | 0.09 | +0.02 | 44 | +4 | 0.0096 | 0.001 |
| rally 8 [ironLegs=1.0,iron_sword=1.0,shield=1.0] v 24 creeper · flat | 8 | 100 % | 0.00 | -0.00 | 10 | -4 | 0.033 | 2.4e-07 |
| rally 8 [ironLegs=1.0,iron_sword=1.0,shield=1.0] v 28 skeleton · flat | 8 | 25 % | 0.09 | -0.05 | 28 | +28 | 0.0053 | 0.00036 |
| rally 8 [ironLegs=1.0,iron_sword=1.0,shield=1.0] v 32 pillager[scguns:pax] · forest | 15 | 87 % | 0.28 | -0.06 | 4 | -1 | 0.046 | 0.006 |
| rally 8 [ironLegs=1.0,iron_sword=1.0,shield=1.0] v 32 pillager[scguns:pax] · village | 75 | 63 % | 0.18 | +0.00 | 36 | -3 | 0.015 | 0.0022 |
| rally 8 [ironLegs=1.0,iron_sword=1.0,shield=1.0] v 34 zombie · flat | 1 | 100 % | 0.00 | +0.00 | 5 | -5 | 0.044 | 0.0065 |
| rally 8 [ironLegs=1.0,iron_sword=1.0,shield=1.0] v 37 zombie · flat | 3 | 100 % | 0.00 | +0.00 | 20 | -19 | 0.063 | 0.015 |
| rally 8 [ironLegs=1.0,iron_sword=1.0,shield=1.0] v 6 blaze · flat | 8 | 100 % | 0.00 | -0.00 | 3 | -1 | 0.026 | 0.00019 |
| rally 8 [iron_sword=1.0] v 16 pillager[scguns:pax] · village | 15 | 100 % | 0.00 | +0.00 | 2 | +0 | 0.0077 | 0.029 |
| rally 8 [iron_sword=1.0] v 24 creeper · flat | 6 | 67 % | 0.08 | -0.00 | 8 | +8 | 0.0051 | 0.0011 |
| rally 8 [iron_sword=1.0] v 26 zombie · flat | 1 | 100 % | 0.00 | +0.00 | 2 | -2 | 0.043 | 0.0046 |
| rally 8 [iron_sword=1.0] v 28 skeleton · flat | 8 | 100 % | 0.12 | -0.08 | 3 | +2 | 0.034 | 0.0041 |
| rally 8 [iron_sword=1.0] v 29 zombie · flat | 1 | 100 % | 0.00 | +0.00 | 0 | -0 | 0.053 | 0.011 |
| rally 8 [iron_sword=1.0] v 31 zombie · flat | 5 | 100 % | 0.00 | +0.00 | 3 | +2 | 0.038 | 0.012 |
| rally 8 [iron_sword=1.0] v 32 pillager[scguns:pax] · village | 15 | 100 % | 0.00 | +0.00 | 1 | -0 | 0.0068 | 0.017 |

## Los 10 peor predichos (fuera de muestra: (1 − acierto) + error de vivos)

- rally 8 [ironLegs=1.0,iron_sword=1.0,shield=1.0] v 28 skeleton · flat — acierto 25 %, error 0.09: límite de tiempo 56 %; resultado partido (draw 9/16, blue 7/16)
- rally 8 [bow=0.5,ironLegs=0.5,iron_sword=0.5,shield=0.5] v 32 pillager[scguns:pax] · village — acierto 55 %, error 0.25: límite de tiempo 20 %; stand-off 42/corrida; etiquetas standoff:losRelease; terreno village
- rally 8 [bow=1.0] v 48 pillager[scguns:pax] · flat — acierto 68 %, error 0.29: resultado partido (blue 448/630, red 182/630); stand-off 149/corrida
- rally 8 [ironLegs=1.0,iron_sword=1.0,shield=1.0] v 32 pillager[scguns:pax] · village — acierto 63 %, error 0.18: límite de tiempo 35 %; resultado partido (blue 93/150, draw 52/150); etiquetas standoff:losRelease; terreno village
- rally 8 [bow=0.8,ironLegs=0.2,iron_sword=0.2,shield=0.2] v 32 pillager[scguns:pax] · village — acierto 67 %, error 0.19: límite de tiempo 60 %; resultado partido (draw 18/30, blue 8/30); stand-off 78/corrida; etiquetas standoff:losRelease; terreno village
- gv 16 [bow=0.5,ironLegs=0.5,iron_sword=0.5,shield=0.5] v 32 pillager[scguns:pax] · forest — acierto 67 %, error 0.10: resultado partido (red 60/90, blue 30/90); terreno forest
- rally 8 [bow=1.0] v 32 pillager[scguns:pax] · flat — acierto 86 %, error 0.29: stand-off 144/corrida
- rally 8 [iron_sword=1.0] v 24 creeper · flat — acierto 67 %, error 0.08: límite de tiempo 83 %
- rally 8 [ironLegs=1.0,iron_sword=1.0,shield=1.0] v 32 pillager[scguns:pax] · forest — acierto 87 %, error 0.28: terreno forest
- gv 12 [bow=0.3,ironLegs=0.7,iron_sword=0.7,shield=0.7] v 32 pillager[scguns:pax] · forest — acierto 67 %, error 0.08: resultado partido (red 20/30, blue 10/30); terreno forest

## Deja fuera un tamaño, por clave

| clave (sin nº rojo) | rojos predichos | n | ganador acertado | error vivos (fracción) |
|---|---|---|---|---|
| rally 8 [bow=0.2,ironLegs=0.8,iron_sword=0.8,shield=0.8] v pillager[scguns:pax] · forest | 48 | 30 | 93 % | 0.23 |
| rally 8 [bow=0.5,ironLegs=0.5,iron_sword=0.5,shield=0.5] v pillager[scguns:pax] · forest | 48 | 90 | 96 % | 0.23 |
| rally 8 [bow=0.5,ironLegs=0.5,iron_sword=0.5,shield=0.5] v pillager[scguns:pax] · forest | 64 | 30 | 93 % | 0.25 |
| rally 8 [bow=1.0] v pillager[scguns:pax] · flat | 48 | 630 | 29 % | 0.41 |

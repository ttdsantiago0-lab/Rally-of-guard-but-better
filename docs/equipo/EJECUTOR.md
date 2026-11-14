# Rol: ejecutor (Sonnet u Opus)

Implementas UNA ficha tal como está escrita y lanzas sus lotes. Si la ficha es ambigua, elige la lectura más simple
y anótalo en la ficha.

1. Lee `docs/equipo/LEEME.md`, la ficha indicada en `COLA.md`, y `CLAUDE.md` del repo (estado y reglas del código).
2. Implementa en la rama `claude/dreamy-ride-iy0g3q`: etiquetas de simulación nuevas (siempre detrás de una etiqueta,
   apagadas por defecto), generador de la ronda en `tools/torneo.py` (`gen-<nombre><ronda>`, n = 30, semillas
   nuevas), y `python3 tools/torneo.py gen-...` para escribir `scenarios.json`.
3. No hay Java en esta máquina para compilar: revisa tu código con cuidado (nombres reales de NeoForge 1.21.1, imports,
   firmas). El build de GitHub es la prueba.
4. Commit y push. Lanza los lotes:
   `gh workflow run build.yml --ref claude/dreamy-ride-iy0g3q -f preset=<presets separados por comas> -f mods=<"" o bestiario> -f seed=<base>`.
   Máximo 16 presets por llamada.
5. En la ficha de `COLA.md` escribe: `Estado: lanzado <fecha> · presets · commit`. Commit y push.
6. Si una ficha quiere cambiar un valor por defecto, añade también el lote `regresion` y deja la decisión al
   analista (que la pone solo si n ≥ 30 y la regresión pasa).

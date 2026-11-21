# Cómo usar esta bóveda

1. Instala Obsidian (obsidian.md, gratis).
2. **Open folder as vault** y elige la carpeta del repo entero: `C:\Proyectos personales\rally-of-the-guard-neoforge`
   (desde el 2026-11-20). Así el diseño (`diseno/`) y el laboratorio (`docs/equipo/`) son un solo grafo. El código y los
   datos crudos ya están excluidos en la configuración.
3. Empieza por [[00 Mapa del mod]].

Lo mínimo de Obsidian:
- `[[Nombre]]` enlaza a otra nota; si no existe, al hacer clic se crea.
- **Ctrl+O** abre una nota por nombre; **Ctrl+G** muestra el grafo, el mapa conceptual de todo.
- `#etiqueta` agrupa notas; las propiedades de arriba (`claridad: 6`) se pueden filtrar.

Plantilla de decisión (crea una nota en `Decisiones/`):

```
# D-0001 Título corto
Tema: [[Moral]]
Fecha: 2026-11-16
Decisión: ...
Por qué: ...
Qué cambia en el mod: ...
Pendiente de medir: sí/no
```

La bóveda vive dentro del repo, así que el equipo autónomo puede leer tus decisiones cuando la subas con `git push`.
La carpeta `.obsidian/` (tu configuración) no se sube.

## Cómo crece el grafo (2026-11-20)

- **Tus respuestas → decisiones.** Cada tanda que respondes queda en [[Preguntas de diseño 2026-11]] y se resume en
  notas de `Decisiones/` (una por idea, no por tanda). Cada decisión declara `temas:` y `preguntas:` arriba;
  `python3 tools/diseno_grafo.py` pone la decisión en la nota de cada tema y un enlace al final de cada respuesta.
- **Lo que toca al combate** pasa al equipo como nota en `docs/equipo/indicaciones/`, enlazada a la decisión.
- **El laboratorio** es otro trozo del mismo grafo: objetivos `O1`…`O8` (enlazados a su tema de diseño) y una nota por
  ficha (`F30`, `F50`…) con su hipótesis y su veredicto. Empieza por `docs/equipo/COLA.md`.
- En el gráfico (Ctrl+G) verás: tema → decisión → indicación → objetivo → fichas.

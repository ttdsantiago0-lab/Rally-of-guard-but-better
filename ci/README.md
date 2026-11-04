# ci/

- `modpack.json`: los mods del perfil de pruebas de David (`veamos (1)`), **solo por hash** (SHA-1 para buscar en
  Modrinth, SHA-512 para verificar). Los jar no se guardan en el repositorio (no son nuestros). 163 de 172 están en
  Modrinth; 46 de esos son solo de cliente.
- `fetch_mods.py <destino> [--only slug,slug] [--server-only]`: los descarga de Modrinth y los verifica.
- La simulación de Actions (job `sim` de `.github/workflows/build.yml`) corre hoy solo con Guard Villagers + Rally
  (resultados reproducibles y rápidos). El paquete completo se usará después para una prueba de arranque.
- Regenerar `modpack.json` cuando cambie el modpack: hashes de cada `.jar` de `mods/` (Cowork lo hace).

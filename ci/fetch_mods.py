#!/usr/bin/env python3
"""Descarga los mods del modpack de pruebas desde Modrinth, verificados por hash.

Uso:  python3 ci/fetch_mods.py <carpeta_destino> [--only slug1,slug2] [--server-only]
- Busca cada jar de ci/modpack.json en Modrinth por su SHA-1 (POST /v2/version_files).
- Con --server-only omite los proyectos que Modrinth marca server_side = "unsupported" (solo cliente).
- Con --only descarga solo esos proyectos (por slug), p. ej. --only guard-villagers,ice-and-fire
- Verifica SHA-512 de cada descarga; si no coincide, falla. Los jar que no estén en Modrinth se listan
  (no se descargan de ningún otro sitio).
"""
import hashlib, json, os, sys, urllib.request

API = "https://api.modrinth.com/v2"
UA = {"User-Agent": "rally-of-the-guard-ci (private modpack test)"}

def call(url, body=None):
    data = None if body is None else json.dumps(body).encode()
    req = urllib.request.Request(url, data=data, headers={**UA, "Content-Type": "application/json"})
    with urllib.request.urlopen(req, timeout=60) as r:
        return json.load(r)

def main():
    dest = sys.argv[1]
    only = None
    if "--only" in sys.argv:
        only = set(sys.argv[sys.argv.index("--only") + 1].split(","))
    server_only = "--server-only" in sys.argv
    here = os.path.dirname(os.path.abspath(__file__))
    mods = json.load(open(os.path.join(here, "modpack.json"), encoding="utf-8"))["mods"]
    by_sha1 = {m["sha1"]: m for m in mods}
    versions = call(API + "/version_files", {"hashes": list(by_sha1), "algorithm": "sha1"})
    pids = sorted({v["project_id"] for v in versions.values()})
    projects = {}
    for i in range(0, len(pids), 100):
        ids = json.dumps(pids[i:i + 100])
        for p in call(API + "/projects?ids=" + urllib.request.quote(ids)):
            projects[p["id"]] = p
    os.makedirs(dest, exist_ok=True)
    got, skipped, missing = 0, [], [m["file"] for s, m in by_sha1.items() if s not in versions]
    for sha1, v in versions.items():
        m = by_sha1[sha1]
        p = projects.get(v["project_id"], {})
        slug = p.get("slug", v["project_id"])
        if only is not None and slug not in only:
            continue
        if server_only and p.get("server_side") == "unsupported":
            skipped.append(slug)
            continue
        f = next(f for f in v["files"] if f["hashes"]["sha1"] == sha1)
        req = urllib.request.Request(f["url"], headers=UA)
        with urllib.request.urlopen(req, timeout=120) as r:
            blob = r.read()
        if hashlib.sha512(blob).hexdigest() != m["sha512"]:
            sys.exit(f"SHA-512 no coincide: {m['file']}")
        with open(os.path.join(dest, m["file"]), "wb") as out:
            out.write(blob)
        got += 1
    print(f"descargados {got}; solo-cliente omitidos {len(skipped)}; no estan en Modrinth {len(missing)}")
    for f in missing:
        print("  falta:", f)
    if only is not None:
        found = {projects.get(v["project_id"], {}).get("slug") for v in versions.values()}
        lost = only - found
        if lost:
            sys.exit("no estan en el modpack: " + ",".join(sorted(lost)))

if __name__ == "__main__":
    main()

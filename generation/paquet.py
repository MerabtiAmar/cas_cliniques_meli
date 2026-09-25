"""Assemble le contenu embarqué dans l'application : cas vérifiés, cas d'annales, QROC et normes.

Usage : python generation/paquet.py [--tous] [sortie.json]
  --tous : inclut aussi les cas au statut « genere » (non vérifiés), pour tester l'application.
  sortie : par défaut application/app/src/main/assets/contenu.json

Seuls les cas qui passent valider.py sans erreur bloquante sont retenus.
"""
import json
import sys
from datetime import date
from pathlib import Path

sys.stdout.reconfigure(encoding="utf-8")
sys.path.insert(0, str(Path(__file__).resolve().parent))
from valider import charger_normes, valider_cas  # noqa: E402

RACINE = Path(__file__).resolve().parent.parent
GENERATION = RACINE / "generation"
SORTIE = RACINE / "application" / "app" / "src" / "main" / "assets" / "contenu.json"
STATUTS_PUBLIES = {"valide_etudiant", "verifie_ia", "annales"}


def fichiers_de_cas():
    yield from sorted((GENERATION / "annales" / "cas").glob("*.json"))
    yield from sorted((GENERATION / "pilote").glob("*.json"))
    for lot in sorted((GENERATION / "lots").glob("lot_*"), key=lambda p: int(p.name.split("_")[1])):
        yield from sorted(lot.glob("L*.json"))


def main(args):
    tous = "--tous" in args
    args = [a for a in args if a != "--tous"]
    sortie = Path(args[0]) if args else SORTIE
    normes = charger_normes()
    statuts = STATUTS_PUBLIES | ({"genere"} if tous else set())

    cas, ecartes = [], []
    for f in fichiers_de_cas():
        donnees = json.loads(f.read_text(encoding="utf-8"))
        if donnees.get("statut") not in statuts:
            ecartes.append(f"{f.stem} (statut {donnees.get('statut')})")
            continue
        erreurs, _, _ = valider_cas(f, normes)
        if erreurs:
            ecartes.append(f"{f.stem} ({len(erreurs)} erreur(s) de validation)")
            continue
        cas.append(donnees)

    qroc = json.loads((GENERATION / "annales" / "qroc.json").read_text(encoding="utf-8"))
    paquet = {
        "type": "paquet",
        "version": int(date.today().strftime("%Y%m%d")),
        "genere_le": date.today().isoformat(),
        "normes": normes,
        "cas": cas,
        "qroc": qroc,
    }
    sortie.parent.mkdir(parents=True, exist_ok=True)
    sortie.write_text(json.dumps(paquet, ensure_ascii=False, separators=(",", ":")), encoding="utf-8")
    print(f"{len(cas)} cas, {len(qroc)} QROC → {sortie} ({sortie.stat().st_size // 1024} Ko)")
    if ecartes:
        print("Écartés : " + ", ".join(ecartes))


if __name__ == "__main__":
    main(sys.argv[1:])

"""Convertit des cas JSON en un document Markdown lisible, pour la relecture.

Usage : python generation/rendu.py <dossier> <sortie.md>
Le corrigé de chaque question est replié dans une balise <details>.
"""
import json
import sys
from pathlib import Path

sys.stdout.reconfigure(encoding="utf-8")
sys.path.insert(0, str(Path(__file__).resolve().parent))
from valider import charger_normes, patient_a_l_etape, statut_examen  # noqa: E402

FLECHES = {"HAUT": " ↑", "BAS": " ↓"}


def format_valeur(v):
    if isinstance(v, float):
        return f"{v:g}".replace(".", ",")
    return str(v)


def rendre_examens(examens, patient, normes):
    lignes, groupe = [], None
    for ex in examens:
        if ex.get("groupe") != groupe:
            groupe = ex.get("groupe")
            lignes += ["", f"**{groupe}**", "", "| Paramètre | Résultat |", "|---|---|"]
        fleche = ""
        norme = normes.get(ex.get("cle")) if ex.get("cle") else None
        if norme and isinstance(ex.get("valeur"), (int, float)):
            fleche = FLECHES.get(statut_examen(norme, ex["valeur"], patient)[0], "")
        lignes.append(f"| {ex.get('parametre')} | {format_valeur(ex.get('valeur'))} {ex.get('unite', '')}{fleche} |")
    return lignes


def rendre_cas(cas, normes):
    patient = cas["patient"]
    out = [f"## Cas {cas['id']} — {cas['maladie_principale']}", "",
           f"*Difficulté {cas.get('difficulte', '?')}/3 · ~{cas.get('duree_minutes', '?')} min · "
           f"{cas['points_total']} points · Sources mobilisées : {', '.join(cas['chapitres'])}*", ""]
    for etape in cas["etapes"]:
        out += [etape["texte"], ""]
        if etape.get("examens"):
            out += rendre_examens(etape["examens"], patient_a_l_etape(patient, etape), normes) + [""]
        for q in etape.get("questions", []):
            drapeau = " ⚠️ *à vérifier*" if q.get("a_verifier") else ""
            out += [f"**Q{q['n']}. {q['enonce']}** *({q['points']} pt)*{drapeau}", "",
                    "<details><summary>Corrigé</summary>", ""]
            for el in q["elements_attendus"]:
                sources = " ; ".join(el["sources"])
                out.append(f"- **{el['texte']}** ({el['points']:g} pt) — {el['justification']} "
                           f"<sub>[{sources}]</sub>")
            if q.get("signes_negatifs"):
                out.append(f"- *Signes négatifs à citer :* {' ; '.join(q['signes_negatifs'])}")
            if q.get("piege"):
                out.append(f"- *Piège :* {q['piege']}")
            out += ["", "</details>", ""]
    out += ["**À retenir**", ""] + [f"- {s}" for s in cas["synthese"]] + ["", "---", ""]
    return out


def main(dossier, sortie):
    normes = charger_normes()
    fichiers = sorted(Path(dossier).glob("*.json"))
    lignes = [f"# Cas cliniques — {Path(dossier).name}", "",
              "↑ / ↓ : valeur au-dessus ou au-dessous de la norme (generation/normes.json).", ""]
    for f in fichiers:
        lignes += rendre_cas(json.loads(f.read_text(encoding="utf-8")), normes)
    Path(sortie).write_text("\n".join(lignes), encoding="utf-8")
    print(f"{len(fichiers)} cas → {sortie}")


if __name__ == "__main__":
    if len(sys.argv) != 3:
        print(__doc__)
        sys.exit(2)
    main(sys.argv[1], sys.argv[2])

"""Valide les cas cliniques générés (format, barème, sources, cohérence des bilans).

Usage : python generation/valider.py <dossier_ou_fichier> [...]
Code de sortie 1 s'il reste au moins une erreur bloquante.
"""
import json
import sys
from pathlib import Path

sys.stdout.reconfigure(encoding="utf-8")

RACINE = Path(__file__).resolve().parent.parent
CORPUS = RACINE / "corpus"
NORMES = RACINE / "generation" / "normes.json"

COMPETENCES = {
    "caracteriser_articulaire", "interpreter_hemogramme", "interpreter_bilan_inflammatoire",
    "interpreter_bilan", "semiologie", "diagnostic_positif", "diagnostic_differentiel",
    "diagnostic_etiologique", "examen_complementaire", "complication", "traitement",
    "surveillance", "physiopathologie",
}
TRANSVERSALES = {"caracteriser_articulaire", "interpreter_hemogramme", "interpreter_bilan_inflammatoire",
                 "interpreter_bilan", "semiologie"}


def charger_normes():
    if not NORMES.exists():
        return {}
    return json.loads(NORMES.read_text(encoding="utf-8"))["parametres"]


def statut_examen(norme, valeur, patient):
    """Renvoie (statut, intervalle affiché) pour une valeur numérique."""
    sexe, age = patient.get("sexe"), patient.get("age")
    if norme.get("regle") == "age":
        if age is None or sexe not in ("F", "M"):
            return "?", "règle de l'âge : âge/sexe manquants"
        limite = (age + 10) / 2 if sexe == "F" else age / 2
        return ("HAUT" if valeur > limite else "normal"), f"< {limite:g}"
    bas = norme.get(f"min_{sexe}", norme.get("min"))
    haut = norme.get(f"max_{sexe}", norme.get("max"))
    intervalle = f"[{'' if bas is None else bas} – {'' if haut is None else haut}]"
    if bas is not None and valeur < bas:
        return "BAS", intervalle
    if haut is not None and valeur > haut:
        return "HAUT", intervalle
    return "normal", intervalle


def patient_a_l_etape(patient, etape):
    """Le patient vieillit quand une étape se passe « N ans plus tard »."""
    return {**patient, "age": etape.get("age", patient.get("age"))}


def source_valide(src):
    chemin, _, localisation = src.partition("|")
    if chemin == "hors_sources":
        return "hors_sources"
    if not localisation.strip():
        return "sans localisation"
    return "ok" if (CORPUS / chemin).is_file() else "fichier introuvable"


def valider_cas(chemin, normes):
    erreurs, alertes, bilans = [], [], []
    try:
        cas = json.loads(chemin.read_text(encoding="utf-8"))
    except json.JSONDecodeError as e:
        return [f"JSON invalide : {e}"], [], []

    for cle in ("id", "maladie_principale", "chapitres", "points_total", "patient", "etapes", "synthese"):
        if cle not in cas:
            erreurs.append(f"champ manquant : {cle}")
    if erreurs:
        return erreurs, alertes, bilans

    chapitres = cas["chapitres"]
    for ch in chapitres:
        if not (CORPUS / f"{ch}.md").is_file():
            erreurs.append(f"chapitre inconnu : {ch}")
    if len(set(chapitres)) < 3:
        erreurs.append(f"transversalité insuffisante : {len(set(chapitres))} source(s) dans `chapitres` (minimum 3)")

    patient = cas["patient"]
    questions = [q for e in cas["etapes"] for q in e.get("questions", [])]
    if not 8 <= len(questions) <= 11:
        erreurs.append(f"{len(questions)} questions (attendu : 8 à 11)")
    numeros = [q.get("n") for q in questions]
    if numeros != list(range(1, len(questions) + 1)):
        erreurs.append(f"numérotation des questions incorrecte : {numeros}")

    total = 0.0
    for q in questions:
        etiquette = f"Q{q.get('n')}"
        if q.get("competence") not in COMPETENCES:
            erreurs.append(f"{etiquette} : compétence inconnue « {q.get('competence')} »")
        elements = q.get("elements_attendus") or []
        if not elements:
            erreurs.append(f"{etiquette} : aucun élément attendu")
        somme = sum(el.get("points", 0) for el in elements)
        if abs(somme - q.get("points", 0)) > 0.01:
            erreurs.append(f"{etiquette} : éléments = {somme:g} pt, question = {q.get('points')} pt")
        total += q.get("points", 0)
        hors_sources = False
        for i, el in enumerate(elements, 1):
            if not el.get("justification", "").strip():
                alertes.append(f"{etiquette}.{i} : justification vide")
            if not el.get("sources"):
                erreurs.append(f"{etiquette}.{i} : aucune source")
            for src in el.get("sources", []):
                etat = source_valide(src)
                if etat == "hors_sources":
                    hors_sources = True
                elif etat != "ok":
                    erreurs.append(f"{etiquette}.{i} : source « {src} » → {etat}")
        if hors_sources and not q.get("a_verifier"):
            erreurs.append(f"{etiquette} : source hors_sources mais a_verifier = false")
    if abs(total - cas["points_total"]) > 0.01:
        erreurs.append(f"barème : somme des questions = {total:g}, points_total = {cas['points_total']}")
    if not TRANSVERSALES & {q.get("competence") for q in questions}:
        erreurs.append("aucune question sur une compétence transversale")

    age_precedent = patient.get("age") or 0
    for num_etape, etape in enumerate(cas["etapes"], 1):
        patient_etape = patient_a_l_etape(patient, etape)
        if (patient_etape.get("age") or 0) < age_precedent:
            erreurs.append(f"étape {num_etape} : âge {patient_etape.get('age')} inférieur à celui de l'étape précédente")
        age_precedent = patient_etape.get("age") or 0
        for ex in etape.get("examens", []):
            cle, valeur = ex.get("cle"), ex.get("valeur")
            if cle is None or not normes:
                bilans.append((num_etape, ex.get("parametre"), valeur, ex.get("unite", ""), "", "hors normes.json"))
                continue
            norme = normes.get(cle)
            if norme is None:
                erreurs.append(f"étape {num_etape} : clé d'examen inconnue « {cle} »")
                continue
            if ex.get("unite") != norme["unite"]:
                alertes.append(f"étape {num_etape} : {cle} en « {ex.get('unite')} » (normes.json : « {norme['unite']} »)")
            if isinstance(valeur, (int, float)):
                statut, intervalle = statut_examen(norme, valeur, patient_etape)
            else:
                statut, intervalle = "texte", ""
            bilans.append((num_etape, ex.get("parametre"), valeur, ex.get("unite", ""), intervalle, statut))
    return erreurs, alertes, bilans


def main(cibles):
    normes = charger_normes()
    if not normes:
        print("⚠️ generation/normes.json absent : contrôle des bilans désactivé\n")
    fichiers = []
    for cible in cibles:
        p = Path(cible)
        fichiers += sorted(p.glob("*.json")) if p.is_dir() else [p]
    total_erreurs = 0
    for f in fichiers:
        erreurs, alertes, bilans = valider_cas(f, normes)
        total_erreurs += len(erreurs)
        print(f"=== {f.name} : {'OK' if not erreurs else f'{len(erreurs)} erreur(s)'}, {len(alertes)} alerte(s)")
        for e in erreurs:
            print(f"  ❌ {e}")
        for a in alertes:
            print(f"  ⚠️ {a}")
        if bilans:
            print("  Bilans (étape | paramètre | valeur | norme | statut) :")
            for etape, param, valeur, unite, intervalle, statut in bilans:
                print(f"    {etape} | {param} | {valeur} {unite} | {intervalle} | {statut}")
        print()
    print(f"{len(fichiers)} cas, {total_erreurs} erreur(s) bloquante(s)")
    return 1 if total_erreurs else 0


if __name__ == "__main__":
    if len(sys.argv) < 2:
        print(__doc__)
        sys.exit(2)
    sys.exit(main(sys.argv[1:]))

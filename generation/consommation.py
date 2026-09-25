"""Consommation réelle d'une session Claude Code (session principale + sous-agents), en équivalent API.

Lit les champs `usage` des journaux locaux de Claude Code. Sur l'abonnement Pro, le calibrage du 25/09/2026
donne : une fenêtre de 5 h ≈ 30 $ et une semaine ≈ 215 $ d'équivalent API (approximation).

Usage : python generation/consommation.py [id_de_session]   (par défaut : la session la plus récente)
"""
import glob
import json
import os
import sys

sys.stdout.reconfigure(encoding="utf-8")
# Claude Code range les journaux d'un projet sous ~/.claude/projects/<chemin du projet, séparateurs remplacés par « - »>
RACINE = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
PROJET = os.path.expanduser("~/.claude/projects/" + "".join(c if c.isalnum() else "-" for c in RACINE))
# $/MTok : entrée, écriture cache 5 min, écriture cache 1 h, lecture cache, sortie
PRIX = {"opus-5-5": (4, 5, 8, 0.20, 20), "sonnet-5": (2, 2.5, 4, 0.20, 10), "haiku-4-5": (1, 1.25, 2, 0.10, 5),
        "opus-5": (5, 6.25, 10, 0.50, 25), "fable-5-1": (10, 12.5, 20, 0.25, 50)}
SEMAINE, FENETRE = 215.0, 30.0


def lire(chemin):
    """Un enregistrement par réponse du modèle ; la dernière ligne écrite porte l'usage complet."""
    par_reponse = {}
    for ligne in open(chemin, encoding="utf-8"):
        try:
            d = json.loads(ligne)
        except json.JSONDecodeError:
            continue
        m = d.get("message") or {}
        u = m.get("usage")
        if d.get("type") != "assistant" or not u:
            continue
        cc = u.get("cache_creation") or {}
        rec = {"model": m.get("model", "?"), "in": u.get("input_tokens", 0),
               "w5": cc.get("ephemeral_5m_input_tokens", 0) if cc else u.get("cache_creation_input_tokens", 0),
               "w1h": cc.get("ephemeral_1h_input_tokens", 0),
               "read": u.get("cache_read_input_tokens", 0), "out": u.get("output_tokens", 0)}
        cle = (m.get("id"), d.get("requestId"))
        if cle in par_reponse:
            for k in ("in", "w5", "w1h", "read", "out"):
                par_reponse[cle][k] = max(par_reponse[cle][k], rec[k])
        else:
            par_reponse[cle] = rec
    return list(par_reponse.values())


def cout(r):
    # la clé la plus longue d'abord, pour que « opus-5-5 » ne soit pas lu comme « opus-5 »
    for k in sorted(PRIX, key=len, reverse=True):
        if k in r["model"]:
            p = PRIX[k]
            return (r["in"] * p[0] + r["w5"] * p[1] + r["w1h"] * p[2] + r["read"] * p[3] + r["out"] * p[4]) / 1e6
    return 0.0


def afficher(nom, rs):
    total = sum(cout(r) for r in rs)
    sortie = sum(r["out"] for r in rs)
    print(f"{nom:45s} {len(rs):4d} appels | sortie {sortie / 1e3:6.0f}k | {total:6.2f} $ "
          f"≈ {100 * total / SEMAINE:4.1f} % semaine, {100 * total / FENETRE:5.1f} % fenêtre 5 h")
    return total


def main():
    sessions = sorted(glob.glob(f"{PROJET}/*.jsonl"), key=os.path.getmtime)
    principal = f"{PROJET}/{sys.argv[1]}.jsonl" if len(sys.argv) > 1 else sessions[-1]
    sid = os.path.basename(principal)[:-6]
    print(f"Session {sid}\n")
    tous = lire(principal)
    afficher("Session principale", tous)
    for f in sorted(glob.glob(f"{PROJET}/{sid}/subagents/*.jsonl")):
        rs = lire(f)
        tous += rs
        afficher(f"Sous-agent {os.path.basename(f)[6:14]} ({','.join(sorted({r['model'][7:] for r in rs}))})", rs)
    print()
    afficher("TOTAL", tous)


if __name__ == "__main__":
    main()

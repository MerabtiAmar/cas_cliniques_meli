# Signalements de l'étudiant

Déposer ici les fichiers `signalements-AAAA-MM-JJ.json` exportés depuis l'application (*Signalements > Exporter*).

Traitement, pour chaque signalement :
1. Retrouver le cas (`id`, `question`, `element` : indice à partir de 0) ou la QROC (`generation/annales/qroc.json`), et vérifier si la `version` signalée est toujours la version courante.
2. Vérifier le commentaire contre les sources du corpus, comme dans `generation/VERIFICATION.md`.
3. Si l'erreur est confirmée, corriger le cas, augmenter sa `version` de 1 et le repasser au statut `verifie_ia` ; sinon, noter pourquoi dans le fichier de suivi.
4. Lancer `python generation/valider.py` sur le dossier du cas, puis `python generation/paquet.py`.
5. Consigner la décision dans `traitement.md` (date, identifiant, signalement, décision, correction).

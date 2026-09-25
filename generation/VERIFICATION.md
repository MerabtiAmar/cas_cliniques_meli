# Consignes de vérification croisée d'un lot

Ces consignes s'adressent à l'agent qui vérifie un lot de cas générés par un autre agent. Le lecteur final est un étudiant de 6e année qui révise son examen et ne relit pas les cas : une erreur que tu laisses passer, il l'apprendra. Le modèle du rapport est `generation/pilote/verification.md`.

## 1. Ce que tu lis

- `generation/CONSIGNES.md` (les règles que le générateur devait suivre) et le `PLAN.md` du lot (le fil prévu pour chaque cas).
- **Toutes** les sources de `corpus/` : les 13 cours, les fiches complémentaires, `referentiel_transversal.md`, `examens/cas_cliniques.md`, `examens/qroc_par_cours.md`.
- `generation/normes.json`.
- Chaque cas du lot, en entier.

Lance `python generation/valider.py <dossier du lot>` depuis la racine du projet : il doit rester 0 erreur bloquante. Relis le tableau des bilans qu'il imprime.

## 2. Ce que tu vérifies, élément par élément

1. **Exactitude médicale** de chaque élément attendu, de chaque piège et de chaque point de synthèse.
2. **Fidélité aux sources** : ouvre la localisation citée et compare-la au texte. Une source qui ne dit pas ce qu'on lui fait dire est une erreur, même si l'affirmation est juste. Quand le cours de la faculté et une source externe divergent, c'est le cours (ou le corrigé des annales s'il tranche) qui fait foi.
3. **Chiffres, critères et seuils** : aucun ne doit être inventé. Recalcule les critères de classification (points, nombre de critères) à partir des données réelles du cas.
4. **Ancrage dans le cas** : chaque justification cite des signes ou des valeurs réellement présents dans la vignette ou dans `examens`. Chaque signe négatif listé figure bien dans la vignette.
5. **Cohérence des bilans** : chaque valeur `BAS` ou `HAUT` du rapport de `valider.py` est voulue et interprétée ; aucune valeur `normal` n'est décrite comme anormale (et inversement). Les interprétations composées (anémie microcytaire, syndrome néphrotique impur, cytolyse à N fois la normale…) sont cohérentes avec les valeurs et `normes.json`.
6. **Déductibilité** : le diagnostic attendu est justifiable avec les seules données fournies, sans indice manquant ni contradictoire.
7. **Barème** : la répartition des points est proportionnée à l'importance des éléments (le diagnostic positif et les éléments de sécurité pèsent plus lourd qu'un détail).
8. **Cohérence interne et temporelle** : âges par étape (champ `age` d'une étape « N ans plus tard »), traitements en cours, chronologie.
9. **Conformité au plan et aux consignes** : fil du `PLAN.md` respecté (ou adaptation justifiée), transversalité réelle (§3 des consignes), format compact (§7), pas de recopie d'un cas des annales.

## 3. Gravité

- **CRITIQUE** : erreur médicale qui ferait perdre des points ou apprendrait une conduite dangereuse (mauvais diagnostic, traitement contre-indiqué, seuil faux).
- **MAJEUR** : source qui ne soutient pas l'élément, incohérence entre le corrigé et les données du cas, barème nettement déséquilibré, diagnostic non déductible.
- **MINEUR** : imprécision, formulation ambiguë, incohérence formelle sans conséquence sur la réponse.

## 4. Le rapport

Écris `verification.md` dans le dossier du lot, en suivant la structure de `generation/pilote/verification.md` :

1. **Méthode** (un paragraphe) : ce que tu as lu et exécuté.
2. **Tableau de synthèse** : cas, maladie principale, verdict (✅ Conforme, 🟡 Corrections mineures, 🟠 Corrections majeures, 🔴 À reprendre), nombre de problèmes critiques, majeurs et mineurs.
3. **Points d'hésitation** : les éléments marqués `a_verifier`, les pièges qui signalent une divergence entre sources, et tout ce que le générateur a signalé ; ton verdict pour chacun.
4. **Problèmes identifiés**, cas par cas. Pour chaque problème : gravité, question et élément concernés (`Q<n>, élément <i>`), **constat**, **correction proposée** rédigée de façon à être appliquée telle quelle (le texte de remplacement exact, pas une piste), **source** qui fonde la correction.
5. **Points à trancher par l'étudiant** : seulement ce que les sources ne permettent pas de trancher.

Ne modifie pas les cas : tu produis seulement le rapport. Sois exhaustif sur les erreurs et bref sur ce qui est juste.

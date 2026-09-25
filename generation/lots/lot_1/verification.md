# Vérification du lot 1 (L1-01 à L1-10)

Méthode : lecture intégrale des 10 cas du lot, du `PLAN.md` du lot, de `generation/CONSIGNES.md` et de `generation/VERIFICATION.md` ; lecture intégrale des 13 cours, des 4 fiches complémentaires (dont `10_ppr.md`, `10_horton.md`, `07_still.md`, rédigées à partir de sources externes SFR 2024 / PNDS), du `referentiel_transversal.md`, des 9 cas d'annales et des QROC classées par cours, et de `generation/normes.json` ; exécution de `python generation/valider.py generation/lots/lot_1` (10 cas, 0 erreur bloquante, 0 alerte) et relecture complète du tableau des bilans biologiques imprimé (chaque valeur `BAS`/`HAUT`/`normale` recontrôlée contre `normes.json` et contre l'interprétation du corrigé) ; vérification manuelle, élément par élément, de l'exactitude médicale, de la fidélité aux sources citées (chaque source ouverte et comparée au texte, y compris la pagination des fiches et des cours), de l'ancrage dans les données du cas, du recalcul des critères de classification (Yamaguchi pour L1-05, ACR/EULAR 2012 pour L1-04, ACR 1990 pour L1-02/L1-03), des doses de corticoïdes rapportées au poids du patient, de la cohérence des champs `age` par étape, et de la bonne signalisation (piège) des divergences PPR/Horton/Still entre les fiches externes et les corrigés d'annales N°01, N°03, N°06 et N°08.

## Tableau de synthèse

| Cas | Maladie principale | Verdict | Critique | Majeur | Mineur |
|---|---|---|---|---|---|
| L1-01 | PPR puis Horton | ✅ Conforme | 0 | 0 | 0 |
| L1-02 | Horton révélé par une NOIA | ✅ Conforme | 0 | 0 | 0 |
| L1-03 | Horton à forme extra-céphalique (aortite) | ✅ Conforme | 0 | 0 | 0 |
| L1-04 | PPR isolée | 🟡 Corrections mineures | 0 | 0 | 1 |
| L1-05 | Still de l'adulte à forme viscérale | ✅ Conforme | 0 | 0 | 0 |
| L1-06 | Still de l'adulte à forme articulaire chronique | ✅ Conforme | 0 | 0 | 0 |
| L1-07 | FMF compliquée d'amylose AA | ✅ Conforme | 0 | 0 | 0 |
| L1-08 | Néphropathie lupique | ✅ Conforme | 0 | 0 | 0 |
| L1-09 | Lupus à expression hématologique + Gougerot-Sjögren secondaire | ✅ Conforme | 0 | 0 | 0 |
| L1-10 | Neurolupus / AVC lié au SAPL | ✅ Conforme | 0 | 0 | 0 |

Constat général : le lot est d'une qualité élevée, nettement supérieure à ce qu'un contrôle superficiel aurait suggéré. Les points les plus sensibles du lot ont été spécifiquement revérifiés :

- **Doses de corticoïdes** : recalculées contre le poids de chaque patient pour toutes les questions de traitement (PPR 0,2–0,3 mg/kg/j, Horton 0,7 mg/kg/j, Still 1 mg/kg/j et bolus 15 mg/kg/j, lupus 0,5–1 mg/kg/j, cyclophosphamide 0,5–0,8 g/m²/mois, méthotrexate jusqu'à 0,3 mg/kg/semaine). Une seule imprécision trouvée (L1-04, voir ci-dessous).
- **Critères de classification recalculés** à partir des données réelles du cas : PPR ACR/EULAR 2012 (L1-04 : 6 points sans échographie, 7 avec, seuils 4 et 5, correctement recalculés y compris l'absence de données échographiques de hanche) ; Yamaguchi du Still (L1-05 : 4 majeurs et 4 mineurs, PNN à 87 % correctement recalculé) ; ACR 1990 du Horton (L1-02 : 4/5 avant biopsie ; L1-03 : seulement 2/5, un point pédagogique fort puisque la forme extra-céphalique ne remplit pas les critères classiques et repose sur l'imagerie).
- **Classes de néphropathie lupique** (L1-08) : le cours de la faculté ne détaille aucune classification ISN/RPS ; le cas décrit la biopsie (« glomérulonéphrite proliférative diffuse avec lésions actives ») sans inventer de numéro de classe non sourcé — comportement correct au regard des consignes (« n'invente aucun chiffre, critère ou seuil qui ne figure pas dans les sources »).
- **Critères du SAPL** (L1-10) : le cas gère correctement la distinction entre le critère clinique rempli immédiatement (thrombose artérielle) et le critère biologique qui ne peut être affirmé qu'après confirmation à 12 semaines — la question 3 le demande explicitement et la question 8 referme la boucle 12 semaines plus tard.
- **Raisonnement devant une thrombopénie** (L1-09, L1-10) : les deux cas éliminent méthodiquement le SAPL (plaquettes hors de la fourchette 80 000–100 000/mm³ typique, TCA normal, antiphospholipides négatifs) et la microangiopathie thrombotique (absence de schizocytes, hémolyse absente) avant de retenir la thrombopénie immunologique du lupus — démarche complète et bien sourcée.
- **Cohérence des champs `age`** par étape : sur les 5 cas comportant un rebondissement à plusieurs années d'intervalle (L1-03, L1-05, L1-07, L1-09), le champ `age` de l'étape est à chaque fois recalculé correctement et les seuils de VS dépendants de l'âge sont recalculés avec la valeur mise à jour (par exemple L1-03 Q8 : seuil recalculé à 71 ans, pas à l'âge initial de 68 ans). C'est précisément le type d'erreur relevé dans le pilote (PIL-005) ; elle n'apparaît dans aucun cas du présent lot.
- Les 3 fiches externes (`10_ppr.md`, `10_horton.md`, `07_still.md`) sont fidèlement citées, et chaque fois qu'un point de la fiche diverge du corrigé des annales N°01, N°03, N°06 ou N°08, le cas le signale par un `piège` explicite qui indique la version à suivre à l'examen (jamais plus de 3 par cas, conformément au format compact).

---

## Points d'hésitation signalés par le générateur — vérifiés en priorité

### L1-06 Q8 — Contraception sous méthotrexate (`a_verifier: true`)
**Verdict : correctement traité.** L'élément est explicitement `hors_sources`, avec l'explication : « le cours ne cite l'effet tératogène que pour le cyclophosphamide ». C'est exact : `cours/13_traitements.md` ne mentionne l'effet tératogène que pour le cyclophosphamide, pas pour le méthotrexate. Sur le fond, le méthotrexate est bien tératogène (fait médical correct mais hors corpus) : le cas applique honnêtement l'usage exceptionnel prévu par les consignes plutôt que d'affirmer le fait comme sourcé.

### L1-08 Q9 — Conduite devant une neutropénie fébrile (`a_verifier: true`)
**Verdict : correctement traité.** Le corpus (`cours/13_traitements.md`) ne décrit que la surveillance du cyclophosphamide (NFS, syndrome infectieux), pas la conduite à tenir devant une neutropénie fébrile constituée. Le cas le signale honnêtement en `hors_sources` plutôt que d'attribuer cette conduite standard (hospitalisation, hémocultures, antibiothérapie probabiliste large spectre) à une source qui ne la contient pas. Sur le fond, cette conduite est correcte et le raisonnement qui l'entoure (CRP très élevée + neutropénie dix jours après une cure → infection plutôt que poussée lupique) est un point fort du cas, bien sourcé par `cours/01_lupus.md|p.4` et `referentiel_transversal.md|§3`.

### L1-10 Q1 — Causes métaboliques de convulsions (`a_verifier: true`)
**Verdict : correctement traité.** Le corpus ne détaille pas les causes métaboliques de convulsions ; le cas le signale en `hors_sources` tout en gardant l'élément clinique pertinent (glycémie, ionogramme, calcémie, créatininémie), repris et interprété avec les vraies sources (`referentiel_transversal.md|§1`) dans la question suivante (Q4). Usage conforme et bien circonscrit.

### Divergences PPR/Horton/Still signalées par piège — toutes vérifiées comme fidèles à la fiche source
- **L1-01** : Q2 (signes généraux : arguments de PPR selon le corrigé vs drapeaux rouges d'ACG selon la SFR), Q5 (dose de corticoïdes PPR : schéma général du cours vs 0,2–0,3 mg/kg/j de la SFR), Q8 (durée du Horton : 18–24 mois du corrigé vs 12 mois du PNDS). Les trois reproduisent mot pour mot les divergences de `fiches_complementaires/10_ppr.md` et `10_horton.md`, avec la consigne correcte (« pour l'examen, suivez le corrigé/le cours »).
- **L1-02** : Q1 (critères ACR 1990 du cours vs ACR/EULAR 2022 du PNDS), Q3 (durée du bolus : cours 3–5 j vs PNDS 1–3 j), Q8 (méthotrexate/tocilizumab en 1re ligne selon le cours vs la SFR/PNDS). Conformes à la fiche.
- **L1-03** : Q4 (les critères ACR 1990 ne s'appliquent pas à la forme extra-céphalique), Q6 (durée du Horton). Conformes.
- **L1-04** : Q2 (faiblesse des ceintures : PPR selon le cours des vascularites vs myosite à évoquer selon la SFR), Q6 (dose PPR), Q8 (épargne cortisonique : méthotrexate puis biothérapie selon le cours vs tocilizumab en 1re intention selon la SFR). Conformes.
- **L1-05** : Q6 (durée du bolus du Still : 3 sources différentes, correctement citées et départagées), Q8 (proportions des formes évolutives : 1/3-1/3-1/3 du corrigé N°01 vs 30/30/40 % de l'infographie vs les fourchettes du PNDS). Conformes.
- **L1-06** : Q7 (fibrinogène et SAM : le corrigé du cas N°06 cite une hausse du fibrinogène comme argument de SAM, alors que le PNDS, les QROC et le cas N°01 retiennent un fibrinogène bas ; le cas retient à juste titre la position du PNDS/QROC comme argument principal tout en citant le corrigé N°06). Conforme et bien nuancé : c'est le point le plus délicat du corpus sur ce thème et il est traité avec rigueur.

Aucun de ces pièges ne contient d'erreur : ils reflètent fidèlement les divergences documentées dans les fiches et donnent la position à privilégier à l'examen, conformément à la règle de fond des consignes (« quand le cours et une source externe divergent, suis le cours, ou le corrigé des annales s'il tranche »).

---

## Problèmes identifiés

### L1-01 — PPR puis Horton
Aucun problème identifié. Diagnostic déductible sans indice manquant, barème proportionné (9 questions, somme = 20 points vérifiée), signes négatifs tous présents dans la vignette, sources vérifiées exactes (y compris la pagination des fiches PPR et Horton). Le cas ne recopie pas les cas d'annales N°03/N°08 : patiente différente (femme, 71 ans, sans diabète), rebondissement différent (céphalées + claudication de la mâchoire combinées, pas de complication ophtalmologique), valeurs biologiques propres.

### L1-02 — Horton révélé par une neuropathie optique ischémique antérieure
Aucun problème identifié. La cohérence clinique (NOIA constituée à droite précédée d'épisodes de flou visuel, œil gauche protégé par le traitement) est fidèle à `fiches_complementaires/10_horton.md`. Le barème et les doses (bolus, relais à 0,7 mg/kg/j pour un patient de 70 kg → 50 mg/j) sont exacts. Les complications de la corticothérapie prolongée (diabète cortico-induit, ostéoporose fracturaire, HTA, hypokaliémie, syndrome cushingoïde) sont toutes justifiées par des valeurs du cas.

### L1-03 — Horton à forme extra-céphalique (aortite)
Aucun problème identifié. C'est le cas le plus techniquement exigeant du lot (recalcul des critères ACR 1990 dans une forme atypique, âge recalculé à 3 ans d'intervalle pour le seuil de VS) et il est traité sans erreur. La démarche devant une fièvre prolongée du sujet âgé mobilise correctement les 4 cadres nosologiques attendus par les annales (Cas N°06 Q3), avec les particularités algériennes (Wright, Widal-Félix).

### L1-04 — Pseudo-polyarthrite rhizomélique isolée

**MINEUR — Q6, élément 1 : dose de prednisone arrondie de façon imprécise**
- **Constat** : l'élément indique « Prednisone à faible dose : 0,2 à 0,3 mg/kg/j, soit environ 15 à 20 mg/j » pour un patient dont le poids est donné à 78 kg (étape 1). Le calcul exact donne 0,2 × 78 = 15,6 mg/j à 0,3 × 78 = 23,4 mg/j : la borne haute réellement issue de la formule est environ 23 mg/j, pas 20 mg/j (écart d'environ 15 %). Ce n'est pas une erreur dangereuse (l'intervalle reste dans la fourchette internationale de 12,5 à 25 mg/j citée par la fiche SFR), mais l'arrondi ne correspond pas au calcul annoncé.
- **Correction proposée** : remplacer « soit environ 15 à 20 mg/j » par « soit environ 15 à 23 mg/j ».
- **Source** : calcul arithmétique à partir de `patient.age`/poids du cas (étape 1 : « poids 78 kg ») et de la fourchette de `fiches_complementaires/10_ppr.md|Traitement (Corticothérapie)` (« Prednisone 0,2 à 0,3 mg/kg/j »).

Aucun autre problème identifié dans ce cas. Le recalcul des critères ACR/EULAR 2012 (6 points sans échographie, 7 avec, en tenant compte correctement de l'absence d'échographie de hanche) est exact, de même que la gestion de la corticodépendance et le calcul de la dose maximale de méthotrexate (0,3 mg/kg/semaine pour un poids non donné à cette étape — l'élément ne calcule d'ailleurs aucune dose chiffrée pour L1-04 Q9, donc pas de risque d'erreur ici).

### L1-05 — Maladie de Still de l'adulte à forme viscérale
Aucun problème identifié. L'escalade clinique (péricardite → myopéricardite avec troponine et FEVG effondrée) est cohérente et bien distincte du cas d'annales N°01 (patiente différente, pas d'antécédent d'épisode fébrile dans l'enfance, myocardite en plus de la péricardite, cytolyse hépatique, évolution ultérieure en forme polycyclique). Le calcul des critères de Yamaguchi (4 majeurs, 4 mineurs) est exact, de même que les doses (bolus 15 mg/kg/j ≈ 800 mg/j pour 55 kg, relais 1 mg/kg/j = 55 mg/j).

### L1-06 — Maladie de Still de l'adulte à forme articulaire chronique
Aucun problème identifié. La distinction entre AJI systémique (9 ans), maladie de Still de l'adulte (31 ans) et forme articulaire chronique actuelle (35 ans) respecte exactement la règle du PNDS (FS-AJI avant 16 ans, MSA après), contrairement à l'erreur du corrigé du cas N°01 signalée dans la fiche. Le raisonnement toxicité méthotrexate vs SAM (Q7) est le point le plus délicat du cas et il est traité avec rigueur.

### L1-07 — Fièvre méditerranéenne familiale compliquée d'une amylose AA
Aucun problème identifié. Les localisations, le mécanisme physiopathologique, les sites de biopsie et la coloration (rouge Congo) sont fidèles à `cours/09_amyloses.md`, avec une pagination systématiquement vérifiée exacte. La progression vers l'insuffisance rénale chronique terminale (créatinine, DFG, kaliémie, bicarbonates, anémie) est cohérente avec `normes.json`.

### L1-08 — Lupus érythémateux systémique révélé par une néphropathie lupique
Aucun problème identifié. Les doses de cyclophosphamide (0,5 à 0,8 g/m²/mois, 6 mois puis 1 cure/3 mois, ≤ 12 cures) et d'hydroxychloroquine (400 mg/j) reproduisent exactement `cours/13_traitements.md`. Le raisonnement fièvre + neutropénie sous cyclophosphamide vs poussée lupique (CRP très élevée, complément qui se normalise, anti-ADN et protéinurie en baisse) est un exemple particulièrement solide du raisonnement attendu par le référentiel transversal (§3).

### L1-09 — Lupus à expression hématologique associé à un Gougerot-Sjögren secondaire
Aucun problème identifié. Le raisonnement devant la thrombopénie à 14 000/mm³ élimine méthodiquement le SAPL (fourchette typique 80 000-100 000/mm³, non atteinte ici) et la microangiopathie thrombotique avant de retenir l'origine lupique — exactement la démarche demandée. L'association au Gougerot-Sjögren secondaire est bien construite (xérophtalmie, xérostomie, parotidomégalies récidivantes, anti-SSA/SSB) et la question sur la toxicité oculaire de l'hydroxychloroquine (3 ans plus tard) est bien distinguée de la cataracte et du glaucome cortisoniques.

### L1-10 — Neurolupus : crise convulsive révélant un AVC ischémique lié aux antiphospholipides
Aucun problème identifié. La question 3 gère très correctement la nuance entre le critère clinique du SAPL, rempli immédiatement (thrombose artérielle cérébrale), et le critère biologique, qui ne peut être affirmé qu'après confirmation à 12 semaines — la question 8 revient explicitement sur ce point 12 semaines plus tard. La physiopathologie du TCA allongé avec effet procoagulant in vivo (Q5) est fidèle à `cours/02_sapl.md|p.2`. Le syndrome catastrophique des antiphospholipides (Q9) reprend exactement les 4 critères et la mortalité (40-50 %) du cours.

---

## Points à trancher par l'étudiant

Aucun point de doute médical n'a été laissé sans arbitrage dans les cas eux-mêmes : chaque divergence entre une fiche externe (PPR, Horton, Still) et un corrigé d'annales est déjà résolue par un `piège` qui indique la position à retenir à l'examen. Il ne reste donc rien à classer « à trancher » au-delà de ce que les cas signalent déjà (voir la section « Points d'hésitation » ci-dessus), qui reste par nature non tranchable par les sources (contradictions réelles entre le corrigé, le cours et le PNDS/la SFR).

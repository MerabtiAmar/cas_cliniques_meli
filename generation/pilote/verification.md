# Vérification des cas pilotes PIL-001 à PIL-005

Méthode : lecture intégrale des 5 cas, des 13 cours, des 2 fiches complémentaires, du référentiel transversal, des 9 cas d'annales, des QROC classées par cours et de `normes.json` ; exécution de `python generation/valider.py generation/pilote` (0 erreur bloquante, 0 alerte) ; vérification manuelle, élément par élément, de l'exactitude médicale, de la fidélité aux sources citées (localisation ouverte et comparée au texte), de l'ancrage dans les données du cas et de la cohérence des bilans avec `normes.json`.

## Tableau de synthèse

| Cas | Maladie principale | Verdict | Critique | Majeur | Mineur |
|---|---|---|---|---|---|
| PIL-001 | Maladie de Still de l'adulte | 🟡 Corrections mineures | 0 | 1 | 0 |
| PIL-002 | Lupus érythémateux systémique | ✅ Conforme | 0 | 0 | 0 |
| PIL-003 | Sclérodermie systémique | ✅ Conforme | 0 | 0 | 0 |
| PIL-004 | Syndrome de Gougerot-Sjögren | ✅ Conforme | 0 | 0 | 0 |
| PIL-005 | Maladie de Behçet | 🟡 Corrections mineures | 0 | 0 | 1 |

Constat général : le corpus des 5 cas est d'une qualité élevée. Les citations vérifiées (plus d'une centaine de rapprochements source/texte effectués, y compris les numéros de QROC et leur session) se sont révélées exactes à une exception mineure près. Le calcul des critères de classification (Yamaguchi pour PIL-001, ICBD pour PIL-005) est fait correctement à partir des données réelles du cas. Aucune valeur de bilan mal interprétée (HAUT/BAS/normal) n'a été trouvée : tous les rapprochements avec `normes.json` sont cohérents.

---

## Points d'hésitation signalés par le générateur — vérifiés en priorité

### PIL-002 Q9 — Hydroxychloroquine pendant la grossesse
**Verdict : correctement traité, aucune correction nécessaire.** L'élément est explicitement marqué `a_verifier: true` avec une source `hors_sources` assumée : « La compatibilité de l'hydroxychloroquine avec la grossesse n'est pas explicitée dans le corpus ». C'est l'usage exceptionnel prévu par les consignes. Sur le fond, l'hydroxychloroquine est effectivement reconnue comme sûre et recommandée pendant la grossesse chez la lupique (son arrêt augmente le risque de poussée) — cette information reste hors corpus mais elle est correcte, et le cas la présente avec la prudence requise plutôt que de l'affirmer comme sourcée.

### PIL-004 — Type de cryoglobulinémie associé au lymphome
**Verdict : fidèle à la source, correctement nuancé.** `cours/06_gougerot_sjogren.md` p.14 écrit textuellement : « pic monoclonal des gammaglobulines, cryoglobulinémie de type 1 » dans le paragraphe sur la dégénérescence lymphomateuse B. L'élément de Q10 cite cette phrase mot pour mot et le piège explique lui-même la controverse : en pratique clinique, la cryoglobulinémie associée au Gougerot-Sjögren et à son évolution lymphomateuse est le plus souvent de type II (mixte, IgM monoclonale à activité facteur rhumatoïde), mais **le cours de la faculté prime pour l'examen** (règle explicite des consignes section 4). Le cas suit la bonne règle : rien à corriger. À charge pour l'étudiant de répondre « type 1 » à l'examen tout en sachant que la littérature externe nuance ce point.

### PIL-005 — Anticoagulation de la thrombose au cours du Behçet / thrombose des veines rénales
**Verdict : correctement traité.** Le piège de Q6 indique explicitement que le cours ne traite pas de l'anticoagulation et que la place d'un anticoagulant est « hors corpus, à vérifier » — conforme à `cours/03_behcet.md`, qui ne mentionne aucun anticoagulant et où le traitement de l'angio-Behçet repose sur l'immunosuppression (corticoïdes + immunosuppresseurs ± anti-TNF-α), ce qui est aussi la position réelle (les thromboses du Behçet sont inflammatoires et adhérentes à la paroi, pas emboligènes ; l'anticoagulation est débattue et dangereuse en présence d'anévrysme pulmonaire, non recherché ici mais exclu par l'angioscanner). De même, la thrombose des veines rénales comme diagnostic différentiel du syndrome néphrotique (Q10) est médicalement pertinente et d'autant plus logique que l'albuminémie est très basse (19 g/L) ; elle est correctement écartée par l'écho-Doppler rénale et honnêtement signalée comme hors corpus.

### PIL-003 — Corticothérapie et risque de crise rénale sclérodermique
**Verdict : conforme, bien construit.** L'enchaînement Q8 (corticothérapie à 1 mg/kg/j pour la myosite, avec la précaution explicitement formulée) → Q9 (crise rénale sclérodermique trois semaines plus tard) reproduit exactement l'enseignement de `cours/04_sclerodermie.md` p.2 et de la QROC Sclérodermie Q14 (« la corticothérapie à forte dose est à éviter... car elle peut favoriser une crise rénale sclérodermique »). Aucune incohérence : le cas illustre le risque plutôt que de le contredire.

### PIL-001 — SAM sans fibrinogène ni VS
**Verdict : conforme, bien géré.** Le bilan de contrôle (étape 3) omet volontairement le fibrinogène et la VS, et le piège de Q8 explique en détail la contradiction entre les corrigés d'annales (VS et fibrinogène en hausse) et le PNDS (VS et fibrinogène en baisse au cours du SAM), en indiquant la position à privilégier à l'examen. C'est un traitement exemplaire d'une contradiction réelle du corpus.

---

## Problèmes identifiés

### PIL-001 — Maladie de Still de l'adulte

**MAJEUR — Q8, élément 3 : « Cytopénies : bicytopénie devenue pancytopénie »**
- **Constat** : cette formulation est incohérente avec les propres données du cas. Au bilan initial (étape 2), le patient présente une anémie (seule cytopénie : Hb 10,2 g/dL) associée à une **hyperleucocytose** (24 500/mm³) et une **thrombocytose** (520 000/mm³) — donc aucune bicytopénie, une seule lignée est basse. Au bilan de contrôle (étape 3), les trois lignées s'effondrent (Hb 7,4 ; leucocytes 2 600 ; plaquettes 58 000) : c'est bien une pancytopénie, mais elle remplace une **cytopénie isolée** (l'anémie), pas une bicytopénie.
- **Correction proposée** : remplacer le texte de l'élément par « Cytopénies : apparition d'une pancytopénie (anémie, leucopénie, thrombopénie), alors qu'il existait initialement une hyperleucocytose et une thrombocytose » — la justification associée (déjà correcte) n'a pas besoin d'être modifiée, seul le mot « bicytopénie » doit être retiré ou remplacé par « cytopénie isolée ».
- **Source** : cohérence interne du cas (`PIL-001.json`, étape 2 vs étape 3, examens biologiques) ; aucune source externe requise, il s'agit d'une erreur de lecture des propres données du cas.

Aucun autre problème identifié dans ce cas après vérification exhaustive de chaque élément.

### PIL-002 — Lupus érythémateux systémique

Aucun problème identifié. Les citations vérifiées (cours/01_lupus, cours/02_sapl, cours/13_traitements, cas N°07, QROC Lupus/SAPL/Traitements) correspondent au texte cité, les valeurs biologiques sont cohérentes avec `normes.json`, le diagnostic (LES puis SAPL secondaire obstétrical) est déductible sans indice manquant ni contradictoire, et la transversalité (SAPL, grossesse, anti-SSA/lupus néonatal) est réelle et testée par des questions dédiées.

### PIL-003 — Sclérodermie systémique

Aucun problème identifié. L'enchaînement clinique (Raynaud secondaire → forme diffuse → myosite de chevauchement → crise rénale sclérodermique) est médicalement cohérent et fidèle à `cours/04_sclerodermie.md` et `cours/08_myopathies.md`. Le barème récompense correctement le diagnostic positif (2,5 pts) et la précaution corticothérapie/crise rénale (0,75 pt sur 2 à la Q8) est proportionnée à son importance pédagogique.

### PIL-004 — Syndrome de Gougerot-Sjögren

Aucun problème identifié. C'est le cas le plus transversal des cinq (sarcoïdose/Heerfordt, amylose, vascularites cryoglobulinémiques, lymphome) et chaque lien est testé par une question dédiée et non simplement mentionné. Le passage d'une néphropathie tubulo-interstitielle initiale à une glomérulonéphrite lors de la vascularite cryoglobulinémique (Q9) est un point fin et correctement sourcé (`cours/06_gougerot_sjogren.md` p.14).

### PIL-005 — Maladie de Behçet

**MINEUR — Q8, élément 5 : décalage entre l'âge utilisé dans la justification et le champ `patient.age`**
- **Constat** : `patient.age` est fixé à 27 dans tout le fichier, alors que la vignette précise que la thrombose veineuse cérébrale survient « deux ans plus tard » (le patient a donc 29 ans). La justification de l'élément recalcule elle-même le seuil avec l'âge réel narratif : « VS 64 mm (> âge/2, soit 14,5 à 29 ans) », ce qui est correct sur le plan clinique mais ne correspond plus au `patient.age` déclaré (27/2 = 13,5, valeur qu'utiliserait un correcteur ou un script s'appuyant sur le champ JSON). Sans conséquence sur la conclusion (64 mm dépasse les deux seuils), donc sans impact médical, mais c'est une incohérence formelle.
- **Correction proposée** : soit ne pas rappeler d'âge précis dans la justification (« VS 64 mm, supérieure au seuil attendu pour l'âge ») pour rester agnostique du décalage temporel, soit documenter explicitement dans le cas que l'âge évolue au fil des étapes (ce que le format actuel ne prévoit pas). La correction la plus simple et directement applicable est de remplacer « (> âge/2, soit 14,5 à 29 ans) » par « (> âge/2) » sans valeur chiffrée de seuil.
- **Source** : cohérence interne du format (`PIL-005.json`, champ `patient.age` vs texte de l'étape 3 et justification de Q8).

Ce même type de décalage temporel existe dans la structure de PIL-002 (23 → 25 ans) et PIL-004 (54 → 58 ans), mais sans qu'aucune justification n'y recalcule explicitement un seuil avec l'âge réel : il n'y a donc pas d'erreur similaire à corriger dans ces deux cas, seulement dans PIL-005.

---

## Points non tranchés

Aucun point de doute n'a été laissé sans vérification dans les sources ; il n'y a donc rien à classer « À TRANCHER PAR L'ÉTUDIANT » au-delà des divergences déjà explicitement gérées par les cas eux-mêmes (pièges de PIL-001 Q8/Q10, PIL-004 Q10, PIL-005 Q6/Q10), elles-mêmes jugées conformes ci-dessus.

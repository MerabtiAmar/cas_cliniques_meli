# Consignes de génération des cas cliniques

Ces consignes s'adressent à l'agent qui génère les cas. Le lecteur final est un étudiant de 6e année (Faculté de médecine de Tizi-Ouzou, module « Maladies systémiques ») qui révise un examen. Il est le seul à relire le contenu. Une erreur non signalée lui coûte cher : chaque élément de réponse doit donc être juste, justifié et sourcé.

## 1. Sources à lire avant de générer

Tous les chemins sont relatifs à `corpus/`.

- `cours/*.md` : les 13 cours de la faculté, **tous**, pas seulement celui de la maladie principale.
- `fiches_complementaires/*.md` : fiches rédigées à partir de sources externes (PNDS) pour les chapitres dont le cours manque.
- `referentiel_transversal.md` : valeurs normales, interprétation des bilans, sémiologie transversale.
- `examens/cas_cliniques.md` : les 9 vrais cas d'examen avec leur corrigé. **C'est le modèle de style à imiter.**
- `examens/qroc_par_cours.md` : les QROC réellement posées, qui montrent ce que les enseignants jugent important.
- `../generation/normes.json` : les clés et unités à utiliser pour les examens biologiques.

## 2. Ce qu'est un bon cas (format de l'examen)

- **Vignette** dans le style de la faculté : « Mme A.X âgée de 43 ans, aux antécédents de…, consulte pour… », avec les constantes (TA, FC, FR, T°) et l'examen clinique. On y trouve des signes positifs **et des signes négatifs explicites** (« pas d'adénopathie », « la bandelette urinaire est négative »), parce que les corrigés exigent de les citer.
- **2 à 4 étapes** révélées progressivement : la vignette initiale, puis les résultats du bilan, puis un rebondissement évolutif (« Vous la revoyez 3 ans plus tard pour… », « Au 3e jour, la fièvre réapparaît… »).
- **8 à 11 questions** au total, dont la plupart se terminent par « Justifiez votre réponse ». Les types de questions sont ceux des annales : caractériser, interpréter un bilan, poser le diagnostic positif, citer les diagnostics différentiels ou étiologiques, demander un examen complémentaire en précisant le résultat attendu, identifier une complication, proposer un traitement et sa surveillance, expliquer un mécanisme.
- **Barème sur 20 points.**
- Le contexte algérien est bienvenu (sérologies de Widal-Félix, brucellose, leishmaniose, tuberculose dans les diagnostics d'élimination), comme dans les annales.
- Ne recopie pas un cas des annales. En revanche, reprendre un **type de question** des annales est souhaitable, puisque l'examen se répète.

## 3. Transversalité (obligatoire)

Un cas mobilise **au moins 3 sources** listées dans `chapitres` : la maladie principale, plus au moins deux autres parmi :
- une complication ou une maladie associée relevant d'un autre cours (par exemple un lupus qui se complique d'un SAPL, un Gougerot-Sjögren qui évolue vers un lymphome ou une vascularite cryoglobulinémique) ;
- un diagnostic différentiel qui relève d'un autre cours (par exemple une parotidomégalie : sarcoïdose ou amylose) ;
- le cours `13_traitements` (effets secondaires de la corticothérapie ou du cyclophosphamide, hydroxychloroquine, grossesse) ;
- `referentiel_transversal` : au moins **une question** porte sur une compétence transversale (caractériser l'atteinte articulaire, interpréter l'hémogramme ou le bilan inflammatoire, syndrome myogène, Raynaud secondaire, type d'atteinte rénale).

## 4. Le corrigé

Pour chaque question :
- `elements_attendus` : une liste d'éléments courts, formulés comme dans les corrigés des annales (puces). Chaque élément comporte :
  - `texte` : l'élément attendu ;
  - `points` : sa part du barème. La somme des éléments est égale aux points de la question ;
  - `justification` : ce qui, **dans les données de ce patient**, justifie l'élément (cite les valeurs et les signes du cas) ;
  - `sources` : au moins une source, au format `"<chemin relatif à corpus/>|<localisation>"`, par exemple `"cours/01_lupus.md|p.3"`, `"fiches_complementaires/07_still.md|Complications"`, `"referentiel_transversal.md|Interpréter un hémogramme"` ou `"examens/cas_cliniques.md|Cas N°06 Q5"`.
- `signes_negatifs` : les signes négatifs du cas qu'il fallait citer pour valider la justification (liste vide si la question ne s'y prête pas).
- `piege` (facultatif) : l'erreur fréquente à éviter.
- `a_verifier` : `true` si un élément ne repose sur aucune source du corpus. Sa source s'écrit alors `"hors_sources|<explication>"`. Cet usage doit rester **exceptionnel**.

Règles de fond :
- Quand un cours de la faculté et une source externe divergent, **suis le cours de la faculté**, ou le corrigé des annales s'il tranche.
- N'invente aucun chiffre, critère ou seuil qui ne figure pas dans les sources.
- La réponse doit être **déductible des données du cas** : pas de diagnostic impossible à justifier avec ce qui est donné.

## 5. Cohérence des examens biologiques

- Chaque résultat d'examen est une entrée structurée de `examens`. Quand le paramètre existe dans `generation/normes.json`, renseigne sa clé dans `cle` et utilise **la même unité**. Sinon, mets `"cle": null`.
- Chaque interprétation du corrigé doit être cohérente avec les valeurs et les normes. Par exemple, « anémie microcytaire hypochrome arégénérative » impose un VGM < 80, une CCMH < 32 et des réticulocytes < 25 000/mm³.
- Donne aussi des valeurs **normales** et des résultats **négatifs** utiles au raisonnement (« FAN négatifs », « complément non consommé », « haptoglobine normale ») : ce sont eux qui permettent d'éliminer des diagnostics.
- Pour la VS, la norme dépend de l'âge et du sexe : renseigne `patient.age` et `patient.sexe`.

## 6. Format JSON (un fichier par cas)

```json
{
  "id": "PIL-001",
  "version": 1,
  "statut": "genere",
  "maladie_principale": "Maladie de Still de l'adulte",
  "chapitres": ["fiches_complementaires/07_still", "referentiel_transversal", "cours/13_traitements", "cours/01_lupus"],
  "liens_transversaux": ["Démarche devant une fièvre prolongée", "Effets secondaires de la corticothérapie"],
  "difficulte": 2,
  "duree_minutes": 25,
  "points_total": 20,
  "patient": {"sexe": "M", "age": 29},
  "etapes": [
    {
      "texte": "Mr K.B âgé de 29 ans, sans antécédents, est hospitalisé pour…",
      "examens": [
        {"groupe": "Hémogramme", "parametre": "Hémoglobine", "cle": "hemoglobine", "valeur": 10.8, "unite": "g/dL"},
        {"groupe": "Immunologie", "parametre": "Anticorps antinucléaires", "cle": null, "valeur": "négatifs", "unite": ""}
      ],
      "questions": [
        {
          "n": 1,
          "enonce": "Caractérisez les manifestations articulaires. Justifiez votre réponse.",
          "competence": "caracteriser_articulaire",
          "points": 2,
          "elements_attendus": [
            {
              "texte": "Rhumatisme inflammatoire",
              "points": 0.5,
              "justification": "Douleurs nocturnes qui réveillent le patient, dérouillage matinal d'une heure",
              "sources": ["referentiel_transversal.md|Caractériser une atteinte articulaire"]
            }
          ],
          "signes_negatifs": ["Absence de déformation"],
          "piege": "",
          "a_verifier": false
        }
      ]
    }
  ],
  "synthese": ["Point clé 1", "Point clé 2", "Point clé 3"]
}
```

- `chapitres` : chemins relatifs à `corpus/`, **sans** l'extension `.md`. La maladie principale vient en premier.
- `competence` prend l'une des valeurs suivantes : `caracteriser_articulaire`, `interpreter_hemogramme`, `interpreter_bilan_inflammatoire`, `interpreter_bilan`, `semiologie`, `diagnostic_positif`, `diagnostic_differentiel`, `diagnostic_etiologique`, `examen_complementaire`, `complication`, `traitement`, `surveillance`, `physiopathologie`.
- `n` numérote les questions de 1 à N **sur l'ensemble du cas**, sans repartir à 1 à chaque étape.
- `age` (facultatif, par étape) : âge du patient à cette étape, quand elle se passe « N ans plus tard ». Il sert à calculer la norme de la VS. `patient.age` reste l'âge au début du cas.
- `examens` peut être une liste vide. Dans `texte`, ne répète pas les valeurs déjà présentes dans `examens`.
- `valeur` est un nombre quand le résultat est numérique (décimales avec un point), sinon un texte.

## 7. Format compact (production)

Le pilote (5 cas validés par l'étudiant, dans `generation/pilote/`) est la **référence de qualité**. Ses cas sont toutefois trop longs (~38 Ko chacun), ce qui coûte cher en quota et en temps de lecture. En production, on garde la même rigueur et le même nombre de questions, mais on vise **15 à 22 Ko par cas** :
- Vignette initiale de 150 à 250 mots, étapes suivantes de 40 à 120 mots.
- `justification` : 25 mots au plus. Cite les valeurs ou les signes du patient, sans reformuler le cours.
- `sources` : 1 ou 2 par élément, les plus précises. Évite de citer le même passage deux fois.
- `piege` : seulement quand il apporte quelque chose, 3 par cas au plus. Il est obligatoire quand une source externe diverge des corrigés.
- `synthese` : 3 puces.
- `examens` : seulement les paramètres utiles au raisonnement, y compris les normaux et les négatifs qui servent à éliminer un diagnostic. Pas de bilan exhaustif.

`statut` : `genere` à l'écriture, `verifie_ia` après la vérification croisée et l'application des corrections, `valide_etudiant` après relecture par l'étudiant, `signale` si l'étudiant signale une erreur.

## 8. Méthode de travail

1. Lis toutes les sources de la section 1.
2. Écris chaque cas dans un fichier `<id>.json`, dans le dossier demandé.
3. Lance `python generation/valider.py <dossier>` depuis la racine du projet et corrige **toutes** les erreurs. Relis ensuite le rapport des examens biologiques : chaque valeur marquée `BAS` ou `HAUT` doit être voulue et interprétée dans le corrigé, et chaque valeur `normal` ne doit pas être décrite comme anormale.
4. Relis chaque cas en te mettant à la place d'un examinateur de la faculté : le diagnostic est-il déductible ? Le barème est-il juste ? Les signes négatifs sont-ils présents dans la vignette ?

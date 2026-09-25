# Inventaire et prétraitement du corpus

Module : **Maladies systémiques** — 6e année de médecine, Faculté de médecine de Tizi-Ouzou (Service de médecine interne, CHU TO).
Les fichiers d'origine n'ont pas été modifiés. Ce dossier contient la version nettoyée et dédupliquée du corpus.

## 1. Fichiers d'origine (16) et décision

| Fichier d'origine | Nature | Auteur | Texte extractible | Décision |
|---|---|---|---|---|
| `cours/3- Le_Lupus_érythémateux_systémique_(LES)..pdf` | Résumé Word du professeur, 5 p. | Pr Salah Mansour | oui | **Retenu** → `cours/01_lupus.md` |
| `cours/4- SAPL + notes.pdf` | Résumé Word + notes, 4 p. | Dr Aberkane | oui | **Retenu** → `cours/02_sapl.md` |
| `cours/4. Maladie de Behcet.pdf` | Résumé Word, 5 p. | Dr Lakabi | oui, sauf critères et traitement (images) | **Retenu** + transcription des images → `cours/03_behcet.md` |
| `cours/5. Sclérodermie systémique.pdf` | Résumé Word, 3 p. | Dr L. Idir | oui | **Retenu** → `cours/04_sclerodermie.md` |
| `cours/05- Sclérodermie systémique_.pdf` | Diapos, 33 p. | Dr L. Idir | oui (même texte) | Doublon : n'apporte que des photos cliniques |
| `cours/02- Granulomatoses systémiques(2).pdf` | Diapos, 29 p. | Dr H. Benazzoug | 14 p. sur 29, le reste en image | **Retenu** + transcription des tableaux → `cours/05_granulomatoses.md` |
| `cours/03- Syndrome de Gougerot- Sjogren_.pdf` | Diapos, 19 p. | Dr A. Belaid | oui | **Retenu** → `cours/06_gougerot_sjogren.md` |
| `cours/3. Sd. Gougerot-Sjogren.pdf` | Scan CamScanner, 3 p. | Dr A. Belaid | non (0 caractère) | Doublon : transcription des diapos ci-dessus |
| `cours/1. Myopathies inflammatoires.pdf` | Scan CamScanner d'un résumé Word, 4 p. | Dr H. Benazzoug | non (0 caractère) | **Retenu**, transcrit à la main → `cours/08_myopathies.md` |
| `cours/9. Les amyloses.pdf` | Résumé Word, 3 p. | Dr A. Belaid | oui | **Retenu** → `cours/09_amyloses.md` |
| `cours/9. Les Amyloses .pdf` | Diapos, 16 p. | Dr A. Belaid | oui (même texte) | Doublon |
| `cours/Image longue 16-11-2025 16.39.jpg` | Capture du résumé des amyloses | Dr A. Belaid | image | Doublon |
| `cours/6. Les vascularites systémiques.pdf` | Résumé Word, 4 p. | Dr Hamdani | oui | **Retenu** → `cours/10_vascularites.md` |
| `cours/10. Maladies rares 2025.pdf` | Diapos, 28 p. | non indiqué | 11 p. sur 28, le reste en image | **Retenu** + transcription des tableaux → `cours/11_maladies_rares.md` |
| `cours/8. Les déficits immunitaires.pdf` | Résumé Word, 3 p. | Dr Hamdani | oui | **Retenu** → `cours/12_deficits_immunitaires.md` |
| `cours/7. Principes du traitement et suivi des MS.pdf` | Résumé Word, 2 p. | Dr L. Idir | oui | **Retenu** → `cours/13_traitements.md` |
| `QST CLASSEES PAR COURS Maladies sytémiques.pdf` | Annales 2024-2026 + corrigé proposé (non officiel), 98 p. | « D/L » | oui | **Retenu** → `examens/qroc_par_cours.md`, `examens/cas_cliniques.md` |

Bilan : 16 fichiers → **12 cours distincts** (4 doublons : amyloses ×3, sclérodermie ×2, Gougerot-Sjögren ×2).
Taille du corpus nettoyé : ~107 000 caractères pour les cours (~30 000 tokens), ~80 000 pour les annales (~23 000 tokens).

## 2. Chapitres de l'examen et couverture par les cours

Le sommaire des annales compte 13 chapitres. Colonnes « QROC » et « Cas » : nombre de questions posées de 2024 à 2026.

| # | Chapitre (examen) | Fichier | QROC | Cas cliniques | Couverture par les cours |
|---|---|---|---|---|---|
| 01 | Lupus érythémateux systémique | `01_lupus.md` | 17 | N°07 (avec le SAPL) | Bonne |
| 02 | SAPL | `02_sapl.md` | 15 | N°07 | Bonne |
| 03 | Maladie de Behçet | `03_behcet.md` | 25 | N°09 | Bonne |
| 04 | Sclérodermie systémique | `04_sclerodermie.md` | 20 | — | Bonne |
| 05 | Granulomatoses (sarcoïdose) | `05_granulomatoses.md` | 7 | N°04 | **Faible** : cours générique, sans les spécificités de la sarcoïdose |
| 06 | Gougerot-Sjögren | `06_gougerot_sjogren.md` | 16 | N°05 | Bonne |
| 07 | Maladies auto-inflammatoires (Still) | `07_still_auto_inflammatoires.md` | 12 | N°01, N°06 | **Absente** : aucun cours, une seule infographie en anglais |
| 08 | Myopathies inflammatoires | `08_myopathies.md` | 21 | N°02 | Bonne |
| 09 | Amyloses | `09_amyloses.md` | 10 | — | Bonne |
| 10 | Vascularites (dont Horton/PPR) | `10_vascularites.md` | 18 | N°03, N°08 | Moyenne : la PPR n'est qu'évoquée |
| 11 | Maladies rares | `11_maladies_rares.md` | 11 | — | Moyenne : l'essentiel est dans des tableaux-images |
| 12 | Déficits immunitaires | `12_deficits_immunitaires.md` | 8 | — | Bonne |
| 13 | Principes du traitement | `13_traitements.md` | 10 | (transversal : N°03, N°04, N°08) | Bonne |
| | **Total** | | **190** | **9** | |

## 3. Compétences transversales exigées par les cas cliniques mais absentes des cours

Ces compétences reviennent dans presque tous les cas ; aucun cours ne les traite. Il faut un « référentiel transversal » (à construire à partir des corrigés).

- Caractériser une atteinte articulaire (rythme inflammatoire, topographie, symétrie, déformation, atteinte axiale) : 6 cas sur 9
- Interpréter un hémogramme avec les seuils (anémie, VGM, CCMH, réticulocytes, leucocytose, PNN, lymphopénie, plaquettes) : 4 cas sur 9
- Interpréter un bilan inflammatoire (VS > (âge + 10)/2, CRP > 5, fibrinogène > 4, hypergammaglobulinémie > 15) : 4 cas sur 9
- Phénomène de Raynaud : arguments pour une origine secondaire : 2 cas sur 9
- Type d'atteinte rénale : glomérulaire ou tubulo-interstitielle, syndrome néphrotique pur ou impur : 2 cas sur 9
- Justifier un diagnostic en citant les **signes négatifs** (règle explicite du corrigé)

## 4. Format des épreuves

- **Cas clinique progressif** : vignette (âge, sexe, antécédents, motif, examen, constantes), puis blocs de biologie ou d'imagerie révélés au fil des questions, puis un rebondissement évolutif (« vous la revoyez trois ans plus tard… »). 8 à 11 questions, barème en points, presque toujours « Justifiez votre réponse ».
- **QROC** : « Citer au moins N… », « Décrire… », « Quel diagnostic évoquer devant… ». Réponses attendues sous forme de listes courtes.
- **Forte répétition d'une session à l'autre** : par exemple les caractéristiques de l'aphtose buccale (4 fois), les complications des vascularites à ANCA (5 fois), les anomalies capillaroscopiques (5 fois). Les cas N°03 et N°08 (PPR puis Horton) sont presque identiques.

## 5. À faire pour compléter le corpus

1. Récupérer le cours « Maladies auto-inflammatoires / maladie de Still » (priorité 1 : 2 cas sur 9).
2. Récupérer un cours ou une fiche sur la sarcoïdose et sur la PPR (cas N°04, N°03, N°08).
3. Construire le référentiel transversal (valeurs normales et sémiologie) à partir des corrigés.
4. Faire relire les transcriptions manuelles des images (Behçet, granulomatoses, maladies rares, myopathies).

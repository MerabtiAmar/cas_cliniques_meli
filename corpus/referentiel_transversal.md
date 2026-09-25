# Référentiel transversal — Maladies systémiques (6e année, Tizi-Ouzou)

Référentiel rédigé par Claude à partir des corrigés d'examen et des cours — à vérifier par l'étudiant

> Les corrigés des annales sont des corrigés « proposés », non officiels (réalisés par « D/L ») : leurs erreurs probables sont signalées par ⚠. [annales en-tête cas_cliniques.md]
> Légende : `[annales Cas N°xx Qy]` = `examens/cas_cliniques.md` ; `[QROC <chapitre> Qn]` = `examens/qroc_par_cours.md` (numérotation interne au chapitre) ; `[cours <fichier> p.n]` = `cours/` ; `[valeur usuelle — à vérifier]` = hors corpus. [corpus 00_inventaire.md]

---

## 1. Valeurs normales et seuils

Les unités sont celles des énoncés de la faculté, reprises dans `generation/normes.json`. [annales Cas N°04 énoncé]

| Paramètre | Unité | Normes | Seuils pathologiques / formulation attendue | Source |
|---|---|---|---|---|
| **Hémogramme** | | | | |
| Hémoglobine | g/dL | F 12–16 ; H 13–17 | Anémie si Hb < norme. Les corrigés écrivent « Hb < 14 » (F et H) mais aussi « Hb entre 12-16 » (F) : voir les contradictions ci-dessous | [annales Cas N°05 Q5] [annales Cas N°01 Q2] [annales Cas N°06 Q5] ; H 13–17 : [valeur usuelle — à vérifier] |
| VGM | fL | 80–100 | Microcytose < 80 ; macrocytose > 100 | [annales Cas N°05 Q5] [annales Cas N°01 Q2] [annales Cas N°07 Q6] |
| CCMH | g/dL | 32–36 | Hypochromie < 32 | [annales Cas N°01 Q2] [annales Cas N°06 Q5] ; borne 36 : [valeur usuelle — à vérifier] |
| Réticulocytes | /mm³ | 25 000–120 000 (bornes utilisées par les corrigés) | Arégénérative < 25 000 ; régénérative > 120 000 | [annales Cas N°06 Q5] [annales Cas N°07 Q6] |
| Leucocytes | /mm³ | 4 000–10 000 | Leucopénie < 4 000 ; hyperleucocytose > 10 000 | [annales Cas N°05 Q5] [annales Cas N°07 Q6] [annales Cas N°06 Q5] |
| PNN | /mm³ | 1 500–7 000 | Polynucléose neutrophile > 7 000 ; neutropénie < 1 500 | [annales Cas N°06 Q5] [annales Cas N°01 Q8] ; 1 500 : [valeur usuelle — à vérifier] |
| Lymphocytes | /mm³ | 1 000–4 000 | Lymphopénie < 1 000 | [annales Cas N°04 Q1] [annales Cas N°05 Q5] ; 4 000 : [valeur usuelle — à vérifier] |
| Éosinophiles | /mm³ | < 500 | Hyperéosinophilie > 500 ; GEPA (Churg-Strauss) > 1 500 | [valeur usuelle — à vérifier] [cours 10_vascularites.md p.2] |
| Plaquettes | /mm³ | 150 000–400 000 | Thrombopénie < 150 000 ; thrombocytose > 400 000 ; SAPL : thrombopénie « modérée », typiquement 80 000–100 000 | [annales Cas N°05 Q5] [annales Cas N°06 Q5] [annales Cas N°07 Q6] [cours 02_sapl.md p.2] |
| **Syndrome inflammatoire** | | | | |
| VS 1re heure | mm | F ≤ (âge + 10)/2 ; H ≤ âge/2 | VS accélérée au-delà ; Horton (critère ACR) : VS > 50 mm | F : [annales Cas N°06 Q6] ; H : [valeur usuelle — à vérifier] ; [cours 10_vascularites.md p.3] |
| CRP | mg/L | ≤ 5 | Élevée si > 5 | [annales Cas N°06 Q6] [annales Cas N°08 Q6] |
| Fibrinogène | g/L | 2–4 | > 4 : syndrome inflammatoire ; abaissé : SAM, CIVD | [annales Cas N°06 Q6] [annales Cas N°01 Q8] [QROC Still Q4] ; borne 2 : [valeur usuelle — à vérifier] |
| **Électrophorèse des protéines** | | | | |
| Protides totaux | g/L | 60–80 | Hypoprotidémie < 60 (syndrome néphrotique) | [annales Cas N°07 Q7] [QROC Lupus Q4] ; borne 80 : [valeur usuelle — à vérifier] |
| Albumine | g/L | 35–50 | Hypoalbuminémie < 35 (inflammation) ; < 30 (syndrome néphrotique) | [annales Cas N°06 Q6] [annales Cas N°07 Q7] ; borne 50 : [valeur usuelle — à vérifier] |
| α1-globulines | g/L | 2–4 | Non interprétées dans les corrigés | [valeur usuelle — à vérifier] |
| α2-globulines | g/L | 5–9 | Hyper-α2 = syndrome inflammatoire ou néphrotique ; jamais interprétées dans les corrigés (5,1 g/L non commenté au Cas N°04) | [valeur usuelle — à vérifier] [annales Cas N°04 énoncé] |
| β-globulines | g/L | 6–12 | Non interprétées dans les corrigés | [valeur usuelle — à vérifier] |
| γ-globulines | g/L | 8–15 | Hypergammaglobulinémie > 15 ; hypogammaglobulinémie < 8 | [annales Cas N°06 Q6] [annales Cas N°04 Q1] [annales Cas N°07 Q7] |
| **Fer et hémolyse** | | | | |
| Ferritine | µg/L | F 15–150 ; H 30–280 | Hyperferritinémie ; au Still et au SAM elle est donnée en multiples de la normale (2 N, 10 N, 18 N) | H : [annales Cas N°08 énoncé] (N < 280) et [annales Cas N°03 énoncé] (N < 200) ; F : [valeur usuelle — à vérifier] |
| Ferritine glycosylée | % | > 20 (usuel 50–80) | < 20 % : très évocatrice de maladie de Still | [annales Cas N°01 Q5] [QROC Still Q8] [cours 07_still_auto_inflammatoires.md p.11] ; 50–80 : [valeur usuelle — à vérifier] |
| Fer sérique | µg/dL | 60–170 | Absent des annales | [valeur usuelle — à vérifier] |
| Coefficient de saturation de la transferrine | % | 20–40 | 22 % qualifié de « normal » | [annales Cas N°01 Q2] ; bornes : [valeur usuelle — à vérifier] |
| Haptoglobine | g/L | 0,5–2 | Effondrée si hémolyse ; « normale » = argument contre une hémolyse | [annales Cas N°01 Q2] ; bornes : [valeur usuelle — à vérifier] |
| **Lipides et glucides** | | | | |
| Triglycérides | g/L | ≤ 1,5 | Hypertriglycéridémie > 1,5 (syndrome métabolique ; SAM) | [annales Cas N°03 Q7] [QROC Still Q4] |
| Cholestérol total | g/L | ≤ 2 | Hypercholestérolémie > 2 | [annales Cas N°03 Q7] |
| HDL-cholestérol | g/L | ≥ 0,4 | HDL bas < 0,4 | [annales Cas N°03 Q7] |
| Glycémie à jeun | g/L | 0,70–1,10 | Hyperglycémie (2,83 g/L chez un diabétique = diabète déséquilibré) | [annales Cas N°03 Q7] ; bornes : [valeur usuelle — à vérifier] |
| HbA1c | % | 4–6 | 9 % = « diabète mal équilibré » | [annales Cas N°03 Q7] ; bornes : [valeur usuelle — à vérifier] |
| **Bilan rénal** | | | | |
| Urée | g/L | 0,15–0,45 | Non interprétée dans les corrigés (0,34 non commentée) | [valeur usuelle — à vérifier] [annales Cas N°04 énoncé] |
| Créatinine | mg/L | F 5–10 ; H 7–12 | 9 mg/L « dans les normes » chez une femme de 56 ans | [annales Cas N°05 Q6] ; bornes : [valeur usuelle — à vérifier] |
| DFG | mL/min/1,73 m² | ≥ 90 | 71 = « altération du DFG » malgré une créatinine normale | [annales Cas N°05 Q6] ; borne 90 : [valeur usuelle — à vérifier] |
| Protéinurie des 24 h | g/24h | < 0,3 | > 0,3 : pathologique ; ≤ 2 : « modérée » (tubulo-interstitielle selon le corrigé) ; > 3 : syndrome néphrotique | [annales Cas N°04 Q7] [annales Cas N°05 Q6] [annales Cas N°07 Q7] |
| **Ionogramme et calcium** | | | | |
| Calcémie | mg/L | 85–105 | Hypercalcémie > 105 | [annales Cas N°04 Q7] ; borne 85 : [valeur usuelle — à vérifier] |
| Kaliémie | mmol/L | 3,5–5 | Hypokaliémie (corticothérapie ; tubulopathie du Gougerot-Sjögren) | [valeur usuelle — à vérifier] [cours 13_traitements.md p.1] [cours 06_gougerot_sjogren.md p.10] |
| Natrémie | mmol/L | 135–145 | Absente des annales | [valeur usuelle — à vérifier] |
| **Bilan hépatique** | | | | |
| ASAT | UI/L | ≤ 40 | Cytolyse ; 132 UI/L ≈ « presque 3 fois la normale » | [annales Cas N°06 Q10] ; borne 40 : [valeur usuelle — à vérifier] |
| ALAT | UI/L | ≤ 45 | Cytolyse ; 146 UI/L ≈ « presque 3 fois la normale » | [annales Cas N°06 Q10] ; borne 45 : [valeur usuelle — à vérifier] |
| PAL | UI/L | 30–145 | Cholestase si > 145 | [annales Cas N°04 énoncé] |
| GGT | UI/L | < 45 | Cholestase si ≥ 45 | [annales Cas N°04 énoncé] |
| Bilirubine totale | mg/L | ≤ 10 | Ictère ; bilirubine libre élevée si hémolyse | [valeur usuelle — à vérifier] |
| **Hémostase** | | | | |
| TP | % | ≥ 70 | Abaissé : insuffisance hépatocellulaire, CIVD | [valeur usuelle — à vérifier] |
| TCA (ratio malade/témoin) | ratio | ≤ 1,2 | Allongé ; SAPL : TCA allongé isolé (TP normal), non corrigé par le plasma témoin | [valeur usuelle — à vérifier] [QROC SAPL Q6] [annales Cas N°07 Q10] |
| **Enzymes musculaires** | | | | |
| LDH | UI/L | 140–280 | Augmentée > 280 (myosite, SAM, hémolyse) | [annales Cas N°02 Q5] [QROC Still Q12] |
| CPK | UI/L | ≤ 190 | Myosite : 2 à 20 × N ; un taux normal n'élimine pas le diagnostic | [annales Cas N°02 Q5] ; borne 190 : [valeur usuelle — à vérifier] |
| **Autres** | | | | |
| Enzyme de conversion de l'angiotensine (ECA) | UI/L | 20–70 | Élevée > 70 (sarcoïdose) | [annales Cas N°04 énoncé] [annales Cas N°04 Q1] |
| C3 | g/L | 0,9–1,8 | « Consommé » (abaissé) : lupus en phase active | [valeur usuelle — à vérifier] [cours 01_lupus.md p.4] |
| C4 | g/L | 0,1–0,4 | Idem C3 | [valeur usuelle — à vérifier] [cours 01_lupus.md p.4] |
| Vitamine D (25-OH) | ng/mL | ≥ 30 | Carence (« vitamine D baisse » au Cas N°03) | [valeur usuelle — à vérifier] [annales Cas N°03 énoncé] |

### Contradictions et points non tranchés entre corrigés

- ⚠ **Seuil d'anémie chez la femme : 14 ou 12 g/dL.** Anémie définie par « Hb < 14 » chez des femmes [annales Cas N°06 Q5] [annales Cas N°07 Q6]
  - Mais « absence d'anémie : Hb entre 12-16 » chez une femme de 56 ans [annales Cas N°05 Q5]
  - Au Cas N°04, une Hb à 13,9 puis 12,8 g/dL chez une femme n'est pas citée parmi les anomalies, ce qui va dans le sens du seuil de 12 [annales Cas N°04 Q1] [annales Cas N°04 Q7]
  - `normes.json` retient 12 g/dL ; dans un cas généré, éviter une Hb entre 12 et 14 g/dL chez la femme [annales Cas N°05 Q5] [annales Cas N°06 Q5]
- ⚠ **Seuil d'anémie chez l'homme** : « Hb < 14 » [annales Cas N°01 Q2] ; une Hb à 14 est jugée « sans anomalie » [annales Cas N°09 Q4] ; le seuil classique est 13 g/dL, retenu dans `normes.json` ; dans un cas généré, éviter une Hb entre 13 et 14 g/dL chez l'homme [valeur usuelle — à vérifier]
- ⚠ **PNN** : > 7 000/mm³ = polynucléose [annales Cas N°01 Q8] [annales Cas N°06 Q5], mais PNN à 8 000 déclarés « sans anomalie » dans le corrigé qui conclut pourtant à une « légère hyperleucocytose à neutrophiles » ; retenir > 7 000 [annales Cas N°09 Q4]
- ⚠ **Ferritine, borne haute (énoncés, deux hommes)** : N < 200 [annales Cas N°03 énoncé] contre N < 280 µg/L [annales Cas N°08 énoncé]
- ⚠ **Fibrinogène et SAM** : le SAM abaisse le fibrinogène [QROC Still Q4] [QROC Still Q12], et un « fibrinogène élevé (> 4) et non abaissé » permet d'écarter le SAM [annales Cas N°01 Q8] ; pourtant le corrigé du Cas N°06 cite la hausse du fibrinogène (4,8 → 6,9 g/L) parmi les arguments de SAM. Retenir : un fibrinogène bas ou qui chute est un argument de SAM [annales Cas N°06 Q10]
- **Réticulocytes entre 25 000 et 120 000/mm³** : aucun corrigé ne tranche (arégénérative < 25 000, régénérative > 120 000) [annales Cas N°06 Q5] [annales Cas N°07 Q6]
  - Classiquement, une anémie est arégénérative si les réticulocytes sont < 120 000/mm³ [valeur usuelle — à vérifier]
  - Dans un cas généré, éviter cette zone ; les Cas N°03 et N°08 donnent 46 000/mm³ sans question dessus [annales Cas N°03 énoncé] [annales Cas N°08 énoncé]
- **Créatinine** : 11,7 mg/L chez une femme n'est pas commentée par le corrigé du Cas N°04, alors qu'elle dépasse la borne usuelle féminine retenue (10 mg/L) [annales Cas N°04 Q7] [valeur usuelle — à vérifier]
- ⚠ **Protéinurie tubulo-interstitielle** : « ne dépasse pas 2 g » [annales Cas N°05 Q6] ; classiquement < 1 g/24 h [valeur usuelle — à vérifier]
- ⚠ **Calcémie** : 113 mg/L est qualifiée d'hypercalcémie « modérée, > 105 et comprise entre [115-125] », ce qui est incohérent (113 < 115) [annales Cas N°04 Q7] ; hypercalcémie sévère classique : > 140 mg/L (3,5 mmol/L) [valeur usuelle — à vérifier]
- ⚠ **Unités erronées** : « protéinurie sup à 300 mg/L » [annales Cas N°04 Q7] et « protéinurie des 24h > 3g/l » [QROC Lupus Q4] désignent des quantités par 24 h (g/24h) [annales Cas N°07 Q7]
- ⚠ **Vocabulaire hépatique** : l'élévation des ASAT/ALAT est appelée « cholestase anictérique » alors que c'est une cytolyse [annales Cas N°06 Q7] ; l'élévation des PAL et GGT est rangée dans la « cytolyse » alors que c'est une cholestase (le Cas N°04 associe cytolyse et cholestase) [annales Cas N°04 Q7]
- **Évolution de la maladie de Still (hors seuil)** : 1/3 monocyclique, 1/3 récurrente, 1/3 chronique selon le corrigé [annales Cas N°01 Q9] ; 30 %, 30 % et 40 % selon l'infographie du cours [cours 07_still_auto_inflammatoires.md p.11]

### Autres seuils chiffrés du corpus

- Test de Schirmer pathologique : < 5 mm en 5 minutes [annales Cas N°05 Q3] [cours 06_gougerot_sjogren.md p.11]
- Temps de rupture du film lacrymal pathologique : < 5 secondes [annales Cas N°05 Q3] [cours 06_gougerot_sjogren.md p.11]
- Test au sucre : le morceau doit fondre en moins de 3 minutes [cours 06_gougerot_sjogren.md p.6]
- Asymétrie tensionnelle significative entre les deux bras : > 20 mmHg [annales Cas N°08 Q9]
- Aphtose buccale récidivante du Behçet : > 3 poussées/an [QROC Behçet Q1] ; ≥ 3 épisodes en 12 mois (ICBD) [cours 03_behcet.md p.4]
- Test pathergique positif : papule > 2 mm au point de piqûre à 48 h [cours 03_behcet.md p.4]
- SAPL obstétrical : ≥ 3 fausses couches précoces (< 10 SA), ≥ 1 mort fœtale in utero (> 10 SA), prématurité < 34 SA ; anticorps confirmés à 12 semaines d'intervalle [cours 02_sapl.md p.2]
- Maladie de Horton (ACR) : âge > 50 ans, VS > 50 mm à la 1re heure [cours 10_vascularites.md p.3]

---

## 2. Interpréter un hémogramme

- Méthode : nommer l'anomalie, puis justifier chaque qualificatif par le seuil et la valeur du patient entre parenthèses (« VGM inférieur à 80 fL (78) ») [annales Cas N°06 Q5]
- Commenter aussi les lignées normales (« absence de thrombopénie : plaquettes entre 150 000 et 400 000 ») [annales Cas N°05 Q5]

### Anémie

- Anémie : Hb < 12 g/dL (F) ou < 13 g/dL (H) ; les corrigés écrivent souvent « Hb < 14 » (voir section 1) [annales Cas N°05 Q5] [annales Cas N°06 Q5] [valeur usuelle — à vérifier]
- Taille : microcytaire si VGM < 80 fL, normocytaire entre 80 et 100, macrocytaire si > 100 [annales Cas N°01 Q2] [annales Cas N°05 Q5] [annales Cas N°07 Q6]
- Couleur : hypochrome si CCMH < 32 g/dL, normochrome si ≥ 32 [annales Cas N°01 Q2] [annales Cas N°06 Q5]
- Régénération : arégénérative si réticulocytes < 25 000/mm³ ; régénérative si > 120 000/mm³ [annales Cas N°06 Q5] [annales Cas N°07 Q6]
- Hémolyse auto-immune : test de Coombs direct positif = anémie hémolytique auto-immune (lupus) [annales Cas N°07 Q6] [cours 01_lupus.md p.4]
- Coombs négatif : le corrigé écrit « non hémolytique : test de Coombs négatif » [annales Cas N°06 Q5]
  - ⚠ Un Coombs négatif n'élimine que l'hémolyse auto-immune : l'hémolyse mécanique des MAT (schizocytes) et celle de la maladie de Wilson sont à Coombs négatif [QROC SAPL Q8] [QROC Maladies rares Q1]
  - Pour écarter une hémolyse, citer aussi une haptoglobine normale [annales Cas N°01 Q2]
- Signes d'hémolyse à citer : haptoglobine effondrée, LDH et bilirubine libre augmentées, réticulocytose [valeur usuelle — à vérifier]
- Anémie inflammatoire : microcytaire hypochrome, haptoglobine normale, coefficient de saturation normal, syndrome inflammatoire associé (« probablement inflammatoire ») [annales Cas N°01 Q2]
  - La ferritine y est normale ou élevée, alors qu'elle est basse dans la carence martiale [valeur usuelle — à vérifier]
  - Le lupus peut donner une anémie inflammatoire ou une anémie hémolytique auto-immune [cours 01_lupus.md p.4]
- Anémie hémolytique « à tendance macrocytaire » : l'afflux de réticulocytes augmente le VGM [annales Cas N°07 Q6] [valeur usuelle — à vérifier]
- Formulations types des corrigés [annales Cas N°01 Q2] [annales Cas N°06 Q5] [annales Cas N°07 Q6] [annales Cas N°05 Q5]
  - « Anémie microcytaire hypochrome probablement inflammatoire » [annales Cas N°01 Q2]
  - « Anémie microcytaire hypochrome arégénérative : Hb < 14 g/dL (9,5), VGM < 80 fL (78), CCMH < 32 g/dL (30), réticulocytes < 25 000 (8000) ; non hémolytique : test de Coombs négatif » [annales Cas N°06 Q5]
  - « Anémie hémolytique auto-immune (Coombs positif) à tendance macrocytaire (VGM > 100 (105)), régénérative (réticulocytes > 120 000) » [annales Cas N°07 Q6]
  - « Absence d'anémie : Hb entre 12-16, VGM entre 80-100 » [annales Cas N°05 Q5]

### Leucocytes

- Leucopénie < 4 000/mm³ ; hyperleucocytose > 10 000/mm³ [annales Cas N°05 Q5] [annales Cas N°06 Q5] [annales Cas N°07 Q6]
- Hyperleucocytose à PNN : leucocytes > 10 000 et PNN > 7 000 (« hyperleucocytose neutrophile ») [annales Cas N°06 Q5] [annales Cas N°01 Q8]
- La polynucléose neutrophile est un élément du syndrome inflammatoire, typique de la maladie de Still [annales Cas N°01 Q3] [cours 07_still_auto_inflammatoires.md p.11]
- Neutropénie < 1 500/mm³, agranulocytose < 500/mm³ [valeur usuelle — à vérifier]
- Causes de neutropénie dans le corpus : lupus (leucopénie modérée par lymphopénie ou neutropénie), Gougerot-Sjögren (plus rare), toxicité médullaire du cyclophosphamide, du méthotrexate et de l'azathioprine [cours 01_lupus.md p.4] [cours 06_gougerot_sjogren.md p.10] [cours 13_traitements.md p.2]
- Lymphopénie < 1 000/mm³ [annales Cas N°04 Q1] [annales Cas N°05 Q5]
- Causes de lymphopénie dans le corpus : sarcoïdose [annales Cas N°04 Q3], Gougerot-Sjögren (« signe d'activité ») [cours 06_gougerot_sjogren.md p.10], lupus [cours 01_lupus.md p.4]
- Hyperéosinophilie > 1 500/mm³ avec asthme sévère : granulomatose éosinophilique avec polyangéite [cours 10_vascularites.md p.2]

### Plaquettes

- Thrombopénie < 150 000/mm³ ; thrombocytose > 400 000/mm³ [annales Cas N°05 Q5] [annales Cas N°06 Q5] [annales Cas N°07 Q6]
- La thrombocytose accompagne le syndrome inflammatoire (Cas N°06 : 490 000/mm³) [annales Cas N°06 Q5]
- SAPL : thrombopénie modérée (80 000–100 000/mm³), de consommation, bien tolérée [cours 02_sapl.md p.2] [QROC SAPL Q2]
- Microangiopathie thrombotique : thrombopénie de consommation, anémie hémolytique mécanique, schizocytes au frottis [QROC SAPL Q8] [QROC SAPL Q14]
- Lupus : thrombopénie périphérique immunologique (15 à 25 %) [cours 01_lupus.md p.4]

### Associations à reconnaître

- Anémie + leucopénie + thrombopénie chez une femme jeune : cytopénies auto-immunes du lupus [annales Cas N°07 Q6] [cours 01_lupus.md p.4]
- SAM : cytopénies (anémie, thrombopénie, leucopénie) [QROC Still Q4]
  - Le corrigé du Cas N°06 compte comme « cytopénie » une baisse relative des leucocytes (20 000 → 15 600), bien qu'ils restent > 10 000 : comparer au bilan précédent [annales Cas N°06 Q10]
  - Le corrigé du Cas N°01 écarte le SAM malgré une anémie (Hb 11), car les leucocytes sont > 10 000 et les PNN > 7 000 [annales Cas N°01 Q8]
- Gougerot-Sjögren : lymphopénie, anémie auto-immune, thrombopénie auto-immune [QROC Gougerot-Sjögren Q10]

---

## 3. Syndrome inflammatoire biologique

- Éléments à citer, chacun avec son seuil et la valeur entre parenthèses [annales Cas N°06 Q6]
  - VS accélérée : > (âge + 10)/2 chez la femme (22 ans : > 16 mm) [annales Cas N°06 Q6] ; > âge/2 chez l'homme [valeur usuelle — à vérifier]
  - CRP > 5 mg/L [annales Cas N°06 Q6] [annales Cas N°08 Q6]
  - Fibrinogène > 4 g/L [annales Cas N°06 Q6] [annales Cas N°08 Q6] [annales Cas N°01 Q2]
  - Hypoalbuminémie < 35 g/L [annales Cas N°06 Q6]
  - Hypergammaglobulinémie > 15 g/L (polyclonale) [annales Cas N°06 Q6] [annales Cas N°08 Q6]
  - Hyperferritinémie [annales Cas N°03 Q6]
  - Signes hématologiques : hyperleucocytose à PNN, thrombocytose, anémie inflammatoire [annales Cas N°06 Q5] [annales Cas N°01 Q2] [annales Cas N°01 Q3]
  - Hyper-α2-globulinémie, élément classique mais jamais cité dans les corrigés [valeur usuelle — à vérifier]
- Formulation de conclusion : « Il s'agit probablement d'un syndrome inflammatoire » ou « Bilan inflammatoire positif : VS, CRP, fibrinogène élevés, hypergammaglobulinémie, hyperferritinémie » [annales Cas N°06 Q6] [annales Cas N°03 Q6]
- Exemple de syndrome inflammatoire absent : femme de 47 ans, VS 21 mm (limite (47 + 10)/2 = 28,5), CRP 5 mg/L, fibrinogène 3,4 g/L [annales Cas N°04 énoncé] [annales Cas N°06 Q6]
- VS élevée avec CRP normale : penser à une hypergammaglobulinémie (Gougerot-Sjögren : VS 60 mm, CRP 6 mg/L, γ 21 g/L) [annales Cas N°05 énoncé] [valeur usuelle — à vérifier]
- Lupus : VS souvent élevée, CRP modérée [cours 01_lupus.md p.4] ; une CRP très élevée chez un lupique doit faire chercher une infection ou une sérite [valeur usuelle — à vérifier]
- Horton (VS > 50 mm) contre athérosclérose (pas de syndrome inflammatoire biologique) : c'est l'argument biologique qui les distingue [cours 10_vascularites.md p.3] [annales Cas N°03 Q5]
- Behçet : la présence d'un syndrome inflammatoire dépend de l'atteinte [QROC Behçet Q12] [QROC Behçet Q9]
  - Présent dans l'angio-, le neuro- et l'entéro-Behçet, l'érythème noueux et l'atteinte articulaire [QROC Behçet Q12] [annales Cas N°09 Q4]
  - Absent dans la pseudo-folliculite, les ulcérations cutanées, les papulo-pustules et l'hyperréactivité cutanée [QROC Behçet Q9]
- SAM : « inflammation au plafond » (VS, CRP, ferritine, triglycérides augmentés) mais fibrinogène bas [QROC Still Q12]
- Étiologies d'une hyperferritinémie : maladie de Still, SAM, hépatites, lymphomes, hémochromatose, rhumatismes inflammatoires [QROC Still Q7]
- Hypergammaglobulinémie dans la sarcoïdose, contre hypogammaglobulinémie dans la granulomatose sarcoïdose-like du déficit immunitaire commun variable [cours 12_deficits_immunitaires.md p.2]

---

## 4. Caractériser une atteinte articulaire

Ordre de réponse attendu : rythme, évolution, nombre, topographie, symétrie, atteinte axiale, déformation, destruction. [annales Cas N°02 Q1] [annales Cas N°07 Q1]

- **Rythme inflammatoire** : douleurs nocturnes qui réveillent le malade (la nuit ou « très tôt le matin ») et dérouillage matinal [annales Cas N°02 Q1] [annales Cas N°03 Q1] [annales Cas N°08 Q1]
  - Dérouillage matinal : difficulté à ouvrir les mains au réveil pendant environ une heure, mains ou articulations « soudées » [annales Cas N°02 Q1] [annales Cas N°06 Q4]
  - Signes locaux d'arthrite : gonflement douloureux et chaud [annales Cas N°06 Q4]
- **Rythme mécanique** : douleur à l'effort ou à la mise en charge, calmée par le repos, sans réveil nocturne, dérouillage < 15 min [valeur usuelle — à vérifier]
  - À ne pas confondre avec une claudication ischémique (douleur de la mâchoire ou d'un membre à l'effort, cédant au repos), qui n'est pas articulaire [annales Cas N°03 Q3]
- **Aiguë ou chronique** : chronique au-delà de 6 semaines [valeur usuelle — à vérifier] ; les corrigés disent « chronique » pour 3, 4 et 5 mois d'évolution [annales Cas N°07 Q1] [annales Cas N°06 Q4] [annales Cas N°05 Q1]
- **Arthralgie ou arthrite** : arthrite = gonflement articulaire (synovite ou épanchement) ; sinon, parler de « polyarthralgies inflammatoires » [valeur usuelle — à vérifier] [QROC Myopathies Q4]
- **Nombre** : monoarthrite (1 articulation), oligoarthrite (2 à 4), polyarthrite (≥ 4 ou ≥ 5 selon les sources) [valeur usuelle — à vérifier]
- **Topographie** : la classer par taille d'articulation, puis préciser ceintures et rachis [annales Cas N°02 Q1] [annales Cas N°05 Q1]
  - Petites articulations : MCP (métacarpo-phalangiennes) [annales Cas N°02 Q1] [annales Cas N°05 Q1]
  - Moyennes : poignets, coudes [annales Cas N°02 Q1]
  - Grosses : genoux [annales Cas N°05 Q1] [annales Cas N°06 Q4]
  - IPP, IPD et MTP sont des petites articulations ; les chevilles sont des moyennes articulations [valeur usuelle — à vérifier]
  - Acromélique = distale (mains) ; rhizomélique = racines des membres [annales Cas N°07 Q1] [annales Cas N°03 Q2]
  - Ceintures scapulaire et pelvienne (épaules, hanches) : pseudo-polyarthrite rhizomélique [annales Cas N°03 Q1] [annales Cas N°08 Q1]
  - Atteinte axiale (rachis) : toujours préciser « sans atteinte axiale » ou « a priori sans atteinte axiale » [annales Cas N°03 Q1] [annales Cas N°07 Q1] [annales Cas N°08 Q1]
- **Symétrie** : « bilatérale et symétrique » (atteinte des deux mains) [annales Cas N°02 Q1] [annales Cas N°07 Q1]
- **Fixe ou migratrice** : migratrice au Still (« ayant commencé au niveau des genoux puis touchant les coudes… ») ; fixe au Behçet [annales Cas N°06 Q4] [cours 03_behcet.md p.3]
- **Déformation** : « sans déformation » [annales Cas N°02 Q1] [annales Cas N°06 Q4] [annales Cas N°07 Q1]
  - Exceptions : main de Jaccoud du lupus (hyperlaxité capsulo-ligamentaire) ; rétraction des doigts par la sclérose cutanée dans la sclérodermie [cours 01_lupus.md p.2] [cours 04_sclerodermie.md p.2]
- **Destruction** : à rechercher sur les radiographies [annales Cas N°05 Q2] ; pas de destruction dans le lupus [cours 01_lupus.md p.2]
- **Éléments manquants à rechercher** (Cas N°05 Q2) [annales Cas N°05 Q2]
  - La présence d'un dérouillage matinal et sa durée [annales Cas N°05 Q2]
  - Des déformations [annales Cas N°05 Q2]
  - Des signes de destruction articulaire sur la radiographie [annales Cas N°05 Q2]
- **Formulation type** : « Rhumatisme inflammatoire chronique (douleurs nocturnes depuis 3 mois, dérouillage matinal d'une heure), des petites et moyennes articulations (poignets, MCP), bilatéral et symétrique, sans déformation, a priori sans atteinte axiale » [annales Cas N°07 Q1] [annales Cas N°02 Q1]

### Profils articulaires par maladie

- Lupus [QROC Lupus Q9] [cours 01_lupus.md p.2]
  - Oligo- ou polyarthrite inflammatoire bilatérale et symétrique, aiguë (rarement subaiguë ou chronique) [QROC Lupus Q9] [cours 01_lupus.md p.2]
  - Petites et moyennes articulations surtout distales (MCP, IPP, carpes, genoux, chevilles), rachis épargné [QROC Lupus Q9] [cours 01_lupus.md p.2]
  - Non érosive, non destructrice, non déformante (sauf main de Jaccoud) [QROC Lupus Q9] [cours 01_lupus.md p.1]
- Myosites : polyarthralgies inflammatoires bilatérales et symétriques, périphériques, des petites et moyennes articulations, non déformantes et non destructrices [QROC Myopathies Q4] [cours 08_myopathies.md p.1]
- Gougerot-Sjögren : polyarthralgies inflammatoires des grosses, moyennes et petites articulations, non déformantes et non destructrices [cours 06_gougerot_sjogren.md p.9]
- Behçet : arthralgies ou oligoarthrites inflammatoires fixes des articulations porteuses (genoux, chevilles), destructions exceptionnelles [cours 03_behcet.md p.3]
- Sclérodermie : arthralgies, raideur, rarement arthrites [cours 04_sclerodermie.md p.2]
- Amylose AL : polyarthralgies inflammatoires bilatérales et symétriques des grosses, moyennes et petites articulations, canal carpien, « épaulettes » [cours 09_amyloses.md p.2]
- Maladie de Still : arthralgies ou arthrites ; la forme chronique est articulaire et destructrice [cours 07_still_auto_inflammatoires.md p.11]
- Pseudo-polyarthrite rhizomélique : ceintures, sujet âgé, signes généraux, syndrome inflammatoire [annales Cas N°03 Q2] [annales Cas N°08 Q2]
- Vascularites : arthralgies, mono- ou polyarthrite [cours 10_vascularites.md p.1]

---

## 5. Syndrome myogène

- Arguments positifs à citer [annales Cas N°02 Q4]
  - Installation progressive [annales Cas N°02 Q4]
  - Atteinte bilatérale et symétrique [annales Cas N°02 Q4]
  - Muscles proximaux : ceintures scapulaire et pelvienne [annales Cas N°02 Q4] [cours 08_myopathies.md p.1]
  - Difficulté à se lever seule (signe du tabouret), à s'habiller et à se coiffer [annales Cas N°02 Q4]
  - Myalgies spontanées ou provoquées [annales Cas N°02 Q4]
- Traduction clinique dans l'énoncé [annales Cas N°02 énoncé]
  - Ceinture pelvienne : impossibilité de se lever sans appui ou de la position accroupie [annales Cas N°02 énoncé]
  - Ceinture scapulaire : difficulté à se coiffer [annales Cas N°02 énoncé]
  - Douleur à la pression des masses musculaires, déficit proximal au testing [annales Cas N°02 énoncé]
- **Signes négatifs, obligatoires** : absence de troubles sensitifs, réflexes ostéotendineux et cutané-plantaire conservés [annales Cas N°02 Q4]
- Exemple de formulation : « l'absence de signes neurologiques (signe négatif) est en faveur d'un syndrome myogène » [annales Cas N°02 Q4]
- Diagnostics différentiels à écarter par les signes négatifs [valeur usuelle — à vérifier]
  - Syndrome neurogène périphérique : déficit distal, ROT abolis, troubles sensitifs, amyotrophie, fasciculations [valeur usuelle — à vérifier]
  - Atteinte centrale : ROT vifs, signe de Babinski [valeur usuelle — à vérifier]
  - Myasthénie (fatigabilité à l'effort répété), citée parmi les diagnostics différentiels des myosites [cours 08_myopathies.md p.3] [valeur usuelle — à vérifier]
- Exception : la myosite à inclusions associe des signes distaux et proximaux, asymétriques [cours 08_myopathies.md p.2]
- EMG : tracé de type myogène, sans anomalie nerveuse [cours 08_myopathies.md p.2]
- Biologie pour affirmer le syndrome myogène [annales Cas N°02 Q5]
  - CPK augmentées de 2 à 20 fois la normale ; un taux normal n'élimine pas le diagnostic [annales Cas N°02 Q5]
  - Aldolase augmentée, témoin d'une atteinte musculaire persistante (surtout si CPK normales) [annales Cas N°02 Q5]
  - LDH augmentées (> 280 UI/L ; normale 140-280) [annales Cas N°02 Q5]
  - ASAT et ALAT augmentées, peu spécifiques [annales Cas N°02 Q5]
  - Des ASAT/ALAT élevées avec des CPK élevées peuvent être d'origine musculaire : ne pas conclure d'emblée à une cytolyse hépatique [valeur usuelle — à vérifier]
- Signes de gravité : dysphagie (muscles pharyngo-laryngés), dyspnée (atteinte respiratoire), atteinte cardiaque, perte d'autonomie [QROC Myopathies Q3] [cours 08_myopathies.md p.3]
  - La dysphagie a deux mécanismes : compression, ou atteinte des muscles pharyngo-laryngés [annales Cas N°02 Q7]
- Pièges [cours 13_traitements.md p.1] [cours 07_still_auto_inflammatoires.md p.11]
  - Myopathie cortisonique sous corticoïdes, à discuter devant une rechute sous traitement [cours 13_traitements.md p.1] [cours 08_myopathies.md p.4]
  - Myalgies avec CK normales dans la maladie de Still [cours 07_still_auto_inflammatoires.md p.11]

---

## 6. Phénomène de Raynaud

- Reconnaître : changement de coloration des doigts « variant du bleu au blanc », récent, dans les énoncés [annales Cas N°02 énoncé] [annales Cas N°07 énoncé]
- Les trois phases classiques : syncopale (blanche), asphyxique (bleue), hyperhémique (rouge), déclenchées par le froid [valeur usuelle — à vérifier]
- **Arguments pour une origine secondaire** [annales Cas N°02 Q3] [QROC Sclérodermie Q9]
  - N'épargne pas le pouce (touche tous les doigts, pouce compris) [annales Cas N°02 Q3] [QROC Sclérodermie Q9]
  - Perte du caractère saisonnier [annales Cas N°02 Q3] [QROC Sclérodermie Q9]
  - Troubles trophiques (ulcérations, nécrose) [annales Cas N°02 Q3] [QROC Sclérodermie Q9] [cours 04_sclerodermie.md p.1]
  - Caractère douloureux [annales Cas N°02 Q3]
  - Absence de facteur déclenchant [QROC Sclérodermie Q9]
  - Présence d'autres signes (extra-Raynaud) ou de manifestations systémiques : rhumatisme inflammatoire, photosensibilité, atteinte rénale probablement glomérulaire [annales Cas N°02 Q3] [annales Cas N°07 Q2] [QROC Sclérodermie Q9]
  - Début tardif (après 30-40 ans), asymétrie, capillaroscopie anormale, anticorps antinucléaires positifs [valeur usuelle — à vérifier]
- Capillaroscopie secondaire (sclérodermie) : mégacapillaires, raréfaction des capillaires, hémorragies sous-unguéales [QROC Sclérodermie Q1] [cours 04_sclerodermie.md p.1]
- **Arguments pour une origine primaire (maladie de Raynaud)** : femme jeune, début à l'adolescence, antécédents familiaux, atteinte symétrique épargnant le pouce, caractère saisonnier, pas de trouble trophique, examen clinique et capillaroscopie normaux, anticorps antinucléaires négatifs [valeur usuelle — à vérifier]
- Étiologies dans le corpus [cours 04_sclerodermie.md p.1] [cours 01_lupus.md p.3]
  - Sclérodermie : 90 à 95 % des cas, souvent inaugural, sévère [cours 04_sclerodermie.md p.1]
  - Lupus : 35 % des cas [cours 01_lupus.md p.3]
  - Gougerot-Sjögren [cours 06_gougerot_sjogren.md p.8]
  - Vascularite cryoglobulinémique [cours 06_gougerot_sjogren.md p.14]
  - Dermatomyosite [annales Cas N°02 Q2]
- Traitement [cours 04_sclerodermie.md p.3] [annales Cas N°02 Q11]
  - Protection contre le froid, éviction des produits corrosifs, éviter les bêtabloquants, les vasoconstricteurs et le tabac [cours 04_sclerodermie.md p.3] [annales Cas N°02 Q11]
  - Inhibiteurs calciques, antagonistes des récepteurs de l'endothéline, analogues de la prostaglandine, inhibiteurs de la PDE5 (sildénafil) [cours 04_sclerodermie.md p.3] [QROC Traitements Q6]

---

## 7. Atteinte rénale

### Glomérulaire ou tubulo-interstitielle

- **Syndrome glomérulaire** : protéinurie (bandelette « Protéines +++ »), hématurie (« Sang + »), HTA, œdèmes, ± insuffisance rénale [annales Cas N°07 Q3] [annales Cas N°07 énoncé] [cours 01_lupus.md p.2]
  - Exemples : lupus (atteinte « surtout glomérulaire », biopsie rénale indispensable), amylose, vascularites à ANCA (glomérulonéphrite rapidement progressive), vascularite cryoglobulinémique [cours 01_lupus.md p.2] [cours 01_lupus.md p.5] [QROC Amyloses Q7] [QROC Vascularites Q4] [QROC Gougerot-Sjögren Q3]
- **Atteinte tubulo-interstitielle** [annales Cas N°05 Q6] [annales Cas N°04 Q7]
  - Leucocyturie (« Leucocytes +++ ») avec ECBU négatif [annales Cas N°05 Q6] [annales Cas N°04 Q7]
  - Protéinurie modérée (1 croix, 0,95 à 1,2 g/24 h, « ne dépasse pas 2 g ») [annales Cas N°05 Q6] [annales Cas N°04 Q7]
  - Altération du DFG, même avec une créatinine normale [annales Cas N°05 Q6]
  - Signes négatifs : pas d'hématurie, pas d'HTA, pas d'œdème [valeur usuelle — à vérifier]
- Gougerot-Sjögren : néphropathie tubulo-interstitielle (acidose métabolique hyperchlorémique, hypokaliémie, hypercalciurie), plus rarement glomérulaire [cours 06_gougerot_sjogren.md p.10]
- Sarcoïdose : atteinte tubulo-interstitielle avec hypercalcémie [annales Cas N°04 Q7]
- Protéinurie pathologique : > 0,3 g/24 h (Cas N°06 : 280 mg non commentée) [annales Cas N°04 Q7] [annales Cas N°06 énoncé]

### Syndrome néphrotique pur ou impur

- Définition : protéinurie > 3 g/24 h, albuminémie < 30 g/L, protidémie < 60 g/L [annales Cas N°07 Q7] [QROC Lupus Q4]
- Hypogammaglobulinémie < 8 g/L associée [annales Cas N°07 Q7]
- Hyper-α2-globulinémie et hyperlipidémie associées [valeur usuelle — à vérifier]
- **Impur** : hématurie, HTA et/ou insuffisance rénale ; **pur** : aucun de ces signes [annales Cas N°07 Q7] [QROC Lupus Q4]
- Œdèmes néphrotiques : blancs, mous, prenant le godet [annales Cas N°07 Q3]
- Lupus : syndrome néphrotique pur ou impur [QROC Lupus Q4]
- Amylose : syndrome néphrotique pur, HTA souvent absente, hématurie exceptionnelle, reins de taille normale ou augmentée, évolution vers l'insuffisance rénale chronique [QROC Amyloses Q2] [cours 09_amyloses.md p.1]

### Microangiopathie thrombotique (MAT)

- Triade : anémie hémolytique mécanique (schizocytes au frottis, Coombs négatif), thrombopénie de consommation, atteinte rénale [QROC SAPL Q8] [QROC SAPL Q5] [cours 02_sapl.md p.2]
- Marqueurs d'hémolyse à citer : LDH augmentées, haptoglobine effondrée [valeur usuelle — à vérifier]
- SAPL : insuffisance rénale aiguë ou chronique, protéinurie, hématurie, HTA ; MAT sévère et multiviscérale dans le syndrome catastrophique [cours 02_sapl.md p.2] [cours 02_sapl.md p.3]
- Crise rénale sclérodermique [cours 04_sclerodermie.md p.2] [QROC Sclérodermie Q10]
  - HTA sévère, insuffisance rénale rapidement progressive oligo-anurique, schizocytes [cours 04_sclerodermie.md p.2] [QROC Sclérodermie Q10]
  - Favorisée par la corticothérapie à forte dose ; traitement par IEC [QROC Sclérodermie Q14] [cours 04_sclerodermie.md p.3]
- Maladie de Still : la MAT et la CIVD font partie des complications [cours 07_still_auto_inflammatoires.md p.11]

---

## 8. Épanchements des séreuses

- Pleurésie [annales Cas N°07 Q4] [annales Cas N°07 Q8]
  - Clinique : douleur basithoracique aggravée par l'inspiration, matité, abolition du murmure vésiculaire [annales Cas N°07 énoncé] [annales Cas N°07 Q8]
  - Pleurésie lupique : exsudat (riche en protéines, donc d'origine inflammatoire) à prédominance lymphocytaire, uni- ou bilatéral, très corticosensible [annales Cas N°07 Q4] [QROC Lupus Q11] [cours 01_lupus.md p.4]
- Péricardite [annales Cas N°01 Q1]
  - Douleur rétrosternale soulagée par la position penchée en avant, tachycardie [annales Cas N°01 Q1]
  - Microvoltage à l'ECG, rectitude du bord gauche du cœur à la radiographie, épanchement à l'échocardiographie [annales Cas N°01 Q1]
- Exsudat : protides > 30 g/L ; critères de Light : rapport protides pleural/sérique > 0,5, rapport LDH pleural/sérique > 0,6 [valeur usuelle — à vérifier]
- Transsudat : protides < 30 g/L (syndrome néphrotique, insuffisance cardiaque, cirrhose) [valeur usuelle — à vérifier]
- Au Cas N°07, la patiente a un syndrome néphrotique, mais le corrigé retient l'exsudat lymphocytaire de la pleurésie lupique [annales Cas N°07 Q4]
- Cytologie [annales Cas N°07 Q4] [valeur usuelle — à vérifier]
  - Lymphocytaire : lupus [annales Cas N°07 Q4] [QROC Lupus Q11]
  - Lymphocytaire aussi dans la tuberculose et les lymphomes (diagnostics différentiels) ; neutrophile dans les infections bactériennes [valeur usuelle — à vérifier]
- Séreuses par maladie [QROC Lupus Q10] [cours 07_still_auto_inflammatoires.md p.11]
  - Lupus : péricarde et plèvre ; la péricardite lupique (30 %) est de bon pronostic, très corticosensible, sans évolution vers la constriction [QROC Lupus Q10] [QROC Lupus Q14] [cours 01_lupus.md p.3]
  - Maladie de Still : péricardite (atteinte cardiaque la plus fréquente, complication pouvant engager le pronostic vital), épanchement pleural [annales Cas N°01 Q3] [QROC Still Q11] [cours 07_still_auto_inflammatoires.md p.11]
  - Sclérodermie : péricardite de petite abondance, rarement tamponnade [cours 04_sclerodermie.md p.2]
- Microvoltage à l'ECG, diagnostic différentiel : amylose cardiaque (microvoltage, onde Q de pseudo-nécrose) [cours 09_amyloses.md p.1]
- Signes négatifs cités pour une péricardite [annales Cas N°01 Q1]
  - Pas de souffle [annales Cas N°01 Q1]
  - Pas d'anomalie de repolarisation (pas de SCA) [annales Cas N°01 Q1]
  - Pas de végétations et hémocultures négatives (pas d'endocardite) [annales Cas N°01 Q1]
  - Auscultation et imagerie pulmonaires normales (pas d'origine pulmonaire) [annales Cas N°01 Q1]

---

## 9. Fièvre prolongée

- Définition : fièvre de plus de 3 semaines [valeur usuelle — à vérifier] ; dans les énoncés : « fièvre au long cours », « depuis 04 mois » [annales Cas N°01 énoncé] [annales Cas N°06 énoncé]
- Types de fièvre [annales Cas N°01 Q3] [annales Cas N°06 Q1]
  - Intermittente : pics avec retour à la normale, par exemple « 39-40 °C vespérale avec apyrexie matinale » (Still) [annales Cas N°01 Q3]
  - Le corrigé du Cas N°06 conclut, avec un point d'interrogation, à une « fièvre intermittente prolongée » (courbe non transcrite) [annales Cas N°06 Q1]
  - Autres types : continue ou en plateau, rémittente, ondulante (brucellose) [valeur usuelle — à vérifier]
- **Deux urgences infectieuses à rechercher en premier : endocardite et tuberculose** [annales Cas N°06 Q2]
  - Endocardite : souffle, hémocultures, échocardiographie à la recherche de végétations [annales Cas N°01 Q1]
  - Une antibiothérapie préalable (amoxicilline au Cas N°06) peut négativer les hémocultures [valeur usuelle — à vérifier]
  - Tuberculose : IDR à la tuberculine, Quantiféron [annales Cas N°04 énoncé]
- **Cadres nosologiques** (au moins 4, avec 2 exemples chacun) [annales Cas N°06 Q3]
  - Maladies infectieuses : tuberculose, endocardite [annales Cas N°06 Q3]
  - Maladies auto-inflammatoires : fièvre méditerranéenne familiale, maladie de Still [annales Cas N°06 Q3]
  - Maladies auto-immunes : lupus érythémateux systémique, polyarthrite rhumatoïde [annales Cas N°06 Q3]
  - Maladies hématologiques : lymphome, leucémie [annales Cas N°06 Q3]
- Cadres complémentaires [annales Cas N°01 Q3] [cours 10_vascularites.md p.1]
  - Néoplasies solides (TDM thoraco-abdomino-pelvienne sans masse) [annales Cas N°01 Q3]
  - Vascularites : fièvre pseudo-infectieuse sans preuve d'origine infectieuse, amaigrissement ; Horton chez le sujet âgé [cours 10_vascularites.md p.1] [annales Cas N°08 Q3]
  - Sarcoïdose : le syndrome de Heerfordt comporte une fièvre [QROC Sarcoïdose Q6]
- Bilan d'élimination « à l'algérienne », calqué sur l'énoncé du Cas N°01 [annales Cas N°01 énoncé] [annales Cas N°01 Q3]
  - Sérologies VHB, VHC, CMV, VIH [annales Cas N°01 énoncé]
  - Sérodiagnostic de Widal et Félix, sérologie de la leishmaniose [annales Cas N°01 énoncé]
  - Hémocultures [annales Cas N°01 énoncé]
  - FAN, ANCA, anti-CCP, complément [annales Cas N°01 Q3]
  - TDM thoraco-abdomino-pelvienne [annales Cas N°01 Q3]
- Brucellose : fait partie des causes infectieuses de granulomatose du cours [cours 05_granulomatoses.md p.22] ; fièvre ondulante, à rechercher en Algérie (sérologie de Wright) [valeur usuelle — à vérifier]
- Maladie de Still [annales Cas N°06 Q7] [cours 07_still_auto_inflammatoires.md p.11]
  - Diagnostic d'exclusion : enquête infectieuse, néoplasique et auto-immune négative [annales Cas N°06 Q7] [cours 07_still_auto_inflammatoires.md p.11]
  - Diagnostics différentiels : endocardite, candidose systémique, leucémie [QROC Still Q6]
  - Diagnostics différentiels infectieux du cours : mycoplasme, Yersinia, Borrelia, rubéole, VHB, Coxsackie, CMV, EBV, VIH… [cours 07_still_auto_inflammatoires.md p.11]
- La fièvre est la caractéristique commune des maladies auto-inflammatoires [QROC Still Q10]
- Fièvre dans une maladie connue [cours 06_gougerot_sjogren.md p.9] [QROC Behçet Q15]
  - Gougerot-Sjögren : la fièvre oriente plutôt vers une complication (lymphome) [cours 06_gougerot_sjogren.md p.9] [annales Cas N°05 Q9]
  - Behçet : angio-, neuro- et entéro-Behçet, érythème noueux et atteinte articulaire s'accompagnent de fièvre [QROC Behçet Q15]
  - Lupus : fièvre lors des poussées [cours 01_lupus.md p.4]
- Rebond fébrile sous corticoïdes dans une maladie de Still : évoquer un SAM [annales Cas N°06 Q10]

---

## 10. Corticothérapie

- Posologies [cours 13_traitements.md p.1]
  - Prednisone 0,5 à 1 mg/kg/j en traitement d'attaque (4 à 6 semaines) si atteinte viscérale, puis diminution progressive [cours 13_traitements.md p.1]
  - Bolus de méthylprednisolone de 500 mg à 1 g/j pendant 3 à 5 jours si poussée grave, perfusé en 30 min à 3 h [cours 13_traitements.md p.1]
  - Horton : 0,7 mg/kg/j pendant 18 à 24 mois avec dégression progressive [annales Cas N°03 Q9] [annales Cas N°08 Q7]
  - Still : 1 mg/kg/j s'il existe une atteinte viscérale, sinon anti-inflammatoires [annales Cas N°06 Q9]
  - Myosites : 1 mg/kg/j pendant 4 à 6 semaines [cours 08_myopathies.md p.3]
- **Effets secondaires** (cours) [cours 13_traitements.md p.1]
  - Peau : acné, hirsutisme, retard de cicatrisation, fragilité capillaire, érythrose, vergetures [cours 13_traitements.md p.1]
  - Syndrome cushingoïde (redistribution facio-tronculaire des graisses) [cours 13_traitements.md p.1]
  - Métabolisme : lipolyse avec hypertriglycéridémie et hypercholestérolémie ; hyperglycémie [cours 13_traitements.md p.1]
  - Muscle : balance azotée négative, myopathie cortisonique [cours 13_traitements.md p.1]
  - Hypokaliémie ; rétention hydrosodée (œdèmes, HTA, décompensation d'une insuffisance cardiaque) [cours 13_traitements.md p.1]
  - Œil : glaucome, cataracte [cours 13_traitements.md p.1]
  - Psychisme : insomnie, euphorie, dépression, aggravation d'une psychose [cours 13_traitements.md p.1]
  - Os : ostéonécrose aseptique (tête fémorale), ostéoporose [cours 13_traitements.md p.1]
  - Digestif : épigastralgies, nausées, vomissements [cours 13_traitements.md p.1]
  - Freinage de l'axe corticotrope [cours 13_traitements.md p.1]
  - Troubles du rythme et arrêt cardiaque si un bolus est passé en moins de 30 min [cours 13_traitements.md p.1]
- Listes attendues aux annales [annales Cas N°04 Q9] [QROC Traitements Q7]
  - Hyperglycémie, ostéonécrose aseptique, hypokaliémie, freinage de l'axe corticotrope, lipolyse avec dyslipidémie [annales Cas N°04 Q9] [annales Cas N°08 Q11]
  - HTA, diabète cortico-induit, ostéoporose, myopathie cortisonique, infections, glaucome, cataracte [QROC Traitements Q7]
- **Mesures associées** [cours 13_traitements.md p.2]
  - Dose minimale efficace, durée la plus courte possible, prise le matin [cours 13_traitements.md p.2]
  - Régime hyposodé, pauvre en sucres, riche en protides [cours 13_traitements.md p.2]
  - Supplémentation en potassium, vitamine D et calcium ; IPP si antécédent d'ulcère [cours 13_traitements.md p.2]
  - Activité physique régulière [cours 13_traitements.md p.2]
  - Dépistage et traitement d'une ostéoporose, d'un diabète, d'une HTA ; surveillance de la kaliémie [cours 13_traitements.md p.2]
  - Arrêt progressif, qui prévient l'insuffisance surrénalienne et l'effet rebond [cours 13_traitements.md p.2]
- Sujet âgé diabétique (question des annales) [annales Cas N°03 Q10]
  - Contrôle du diabète, régime hyposodé et contrôle des apports glucidiques [annales Cas N°03 Q10]
  - Supplémentation en vitamine D et en calcium, activité physique adaptée [annales Cas N°03 Q10]
  - Surveillance de la glycémie et de la TA, correction des carences [annales Cas N°03 Q10]
- Bilan avant corticothérapie prolongée : recherche d'un foyer infectieux et d'une tuberculose latente, déparasitage (anguillulose), glycémie, kaliémie, TA, ostéodensitométrie [valeur usuelle — à vérifier]
- Situations particulières [QROC Sclérodermie Q14] [annales Cas N°04 Q8]
  - Sclérodermie : éviter les fortes doses (risque de crise rénale sclérodermique) [QROC Sclérodermie Q14] [cours 04_sclerodermie.md p.2]
  - Behçet : glaucome favorisé par les corticoïdes [cours 03_behcet.md p.2]
  - Hypercalcémie de la sarcoïdose : réhydratation et corticothérapie, avec surveillance de la fonction rénale et de la calcémie [annales Cas N°04 Q8]

---

## 11. Règles de rédaction des corrigés

- **Règle des signes négatifs, citée textuellement** : « NOTE : L'importance des signes négatifs : Dans les justifications de vos cas cliniques, n'oubliez surtout pas de mentionner les signes négatifs. Par exemple, préciser « l'absence de signes neurologiques (signe négatif) est en faveur d'un syndrome myogène » est indispensable pour valider pleinement votre justification. Pensez également à bien réviser vos notions médicales de base » [annales Cas N°02 Q4]
- Exemples de signes négatifs valorisés dans les corrigés [annales Cas N°02 Q4] [annales Cas N°01 Q1] [annales Cas N°01 Q8]
  - Syndrome myogène : absence de troubles sensitifs, ROT et réflexe cutané-plantaire conservés [annales Cas N°02 Q4]
  - Péricardite : pas de souffle, pas d'anomalie de repolarisation (pas de SCA), pas de végétations et hémocultures négatives (pas d'endocardite) [annales Cas N°01 Q1]
  - Écarter un SAM : absence d'hépatomégalie et d'adénopathies, triglycérides et LDH normaux, pas de cytolyse, pas de cytopénie, fibrinogène non abaissé [annales Cas N°01 Q8]
  - Still (exclusion) : sérologies et hémocultures négatives, pas de masse à la TDM, FAN, ANCA et anti-CCP normaux, complément non consommé [annales Cas N°01 Q3]
  - Gougerot-Sjögren primitif : anti-DNA, anti-Sm et anti-CCP négatifs, donc pas de lupus associé [annales Cas N°05 Q7]
  - Sarcoïdose : sérologies négatives, absence de tuberculose [annales Cas N°04 Q3]
  - Atteinte articulaire : « sans déformation », « sans atteinte axiale » [annales Cas N°02 Q1] [annales Cas N°03 Q1]
- Justifier chaque terme par le seuil et la valeur du patient entre parenthèses [annales Cas N°06 Q5] [annales Cas N°06 Q6]
- Plan de justification d'un diagnostic : terrain, signes cliniques (par appareil), biologie, bilan immunologique, preuve histologique, puis élimination des diagnostics différentiels [annales Cas N°05 Q7] [annales Cas N°07 Q8] [annales Cas N°04 Q3]
- Rester prudent : les corrigés écrivent « probablement » quand un argument manque [annales Cas N°01 Q2] [annales Cas N°06 Q6]
- Question sur l'évolution : comparer au bilan précédent (fibrinogène 4,8 → 6,9 g/L ; leucocytes 20 000 → 15 600) [annales Cas N°06 Q10]
- Respecter le nombre demandé (« NB : il faut citer 03 manifestations seulement ») [QROC SAPL Q7]
- Le barème est affiché par question (par exemple « 3 points » pour caractériser l'atteinte articulaire) [annales Cas N°07 Q1]
- Ne pas reproduire les erreurs probables des corrigés : voir les contradictions de la section 1 [annales Cas N°06 Q7] [annales Cas N°04 Q7]

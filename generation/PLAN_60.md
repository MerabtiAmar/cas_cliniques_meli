# Plan de production : 60 cas (5 du pilote + 55 à générer)

Validé par l'utilisateur le 25/09/2026. **Validation autonome** : pas de relecture humaine lot par lot, on enchaîne les lots dès qu'ils sont vérifiés. Examen dans moins d'un mois : on génère par ordre de fréquence à l'examen, pour que les premiers lots soient les plus utiles.

## Avant le lot 1

1. Rédiger les fiches manquantes, sur le modèle de `corpus/fiches_complementaires/07_still.md` (validée par l'étudiant) :
   - `05_sarcoidose.md` : [PNDS Sarcoïdose pulmonaire, 2026](https://www.has-sante.fr/jcms/p_3921776/fr/sarcoidose-pulmonaire)
   - `10_ppr.md` : [Recommandations SFR PPR, 2024](https://www.larhumatologie.fr/app/uploads/2025/08/1-s2.0-S1169833024001443-main-compressed.pdf)
   - `10_horton.md` : [PNDS Artérite à cellules géantes, 2024](https://www.has-sante.fr/upload/docs/application/pdf/2024-03/pnds_acg_vf_1.pdf)
2. Structurer en JSON les 190 QROC et les 9 vrais cas de `corpus/examens/`, pour l'application.

## Répartition (maladie principale ; chaque cas reste transversal, cf. CONSIGNES §3)

| Chapitre principal | Nouveaux | Pilote | Total | Pistes de diversité |
|---|---|---|---|---|
| Horton, PPR, Takayasu | 7 | 0 | 7 | PPR isolée puis Horton ; NOIA ; aortite ; Horton vs athérosclérose ; Takayasu chez la femme jeune |
| Vascularites des petits et moyens vaisseaux | 4 | 0 | 4 | GPA (ORL, rein, poumon) ; GEPA (asthme, éosinophilie) ; PAN (multinévrite, HTA) ; vascularite à IgA de l'adulte |
| Still, maladies auto-inflammatoires | 5 | 1 | 6 | Forme articulaire chronique ; péricardite ou tamponnade ; diagnostic d'exclusion ; FMF compliquée d'amylose AA ; SAM |
| Lupus | 6 | 1 | 7 | Neurolupus ; néphropathie ; lupus induit ; hématologique ; grossesse et lupus néonatal ; hémorragie intra-alvéolaire |
| SAPL | 3 | 0 | 3 | SAPL primitif obstétrical ; AVC du sujet jeune ; SAPL catastrophique |
| Behçet | 5 | 1 | 6 | Entéro-Behçet ; neuro-Behçet parenchymateux ; syndrome de Hughes-Stovin ; uvéite ; arrêt de la colchicine |
| Sclérodermie | 4 | 1 | 5 | HTAP ; ulcères digitaux et CREST ; atteinte digestive (grêle) ; PID |
| Myopathies inflammatoires | 5 | 0 | 5 | DM paranéoplasique ; syndrome des anti-synthétases (anti-Jo1 et PID) ; DM anti-MDA5 ; MNAI sous statine (anti-HMGCR) ; myosite à inclusions |
| Gougerot-Sjögren | 3 | 1 | 4 | Atteinte neurologique ; grossesse et BAV congénital ; atteinte tubulo-interstitielle |
| Sarcoïdose, granulomatoses | 5 | 0 | 5 | Löfgren ; Heerfordt ; hypercalcémie ; atteinte cardiaque ; granulomatose infectieuse (tuberculose) |
| Amyloses | 3 | 0 | 3 | AL (cœur, rein, macroglossie) ; AA (inflammation chronique) ; TTR (cardiaque, neuropathie) |
| Maladies rares | 3 | 0 | 3 | Fabry ; Wilson ; Gaucher, NEM |
| Déficits immunitaires | 2 | 0 | 2 | DICV ; déficit en C1-inhibiteur |
| **Total** | **55** | **5** | **60** | |

## Lots (≈ 10 cas par agent, puis vérification croisée par Sonnet)

| Lot | Contenu | Prérequis |
|---|---|---|
| 1 | Horton/PPR ×4, Still ×3, Lupus ×3 | Fiches PPR et Horton |
| 2 | Behçet ×3, Sarcoïdose ×3, Myopathies ×2, Gougerot-Sjögren ×2 | Fiche sarcoïdose |
| 3 | SAPL ×3, Sclérodermie ×2, Vascularites petits/moyens ×2, Lupus ×2, Still ×1 | |
| 4 | Horton/PPR/Takayasu ×3, Myopathies ×2, Sarcoïdose ×2, Behçet ×2 | |
| 5 | Maladies rares ×3, Déficits immunitaires ×2, Amyloses ×3 | |
| 6 | Vascularites petits/moyens ×2, Sclérodermie ×2, Myopathies ×1, Lupus ×1, Still ×1, Gougerot-Sjögren ×1 | |

Contrôle : lot 1 = 10, lot 2 = 10, lot 3 = 10, lot 4 = 9, lot 5 = 8, lot 6 = 8, soit 55.

Pour chaque lot :
- dossier `generation/lots/lot_N/`, identifiants `L<N>-<nn>` ;
- `valider.py` à 0 erreur ;
- vérification croisée (rapport `verification.md`), puis application de **toutes** les corrections et passage au statut `verifie_ia` (statut final : l'étudiant ne relit pas ; il signale les erreurs depuis l'application) ;
- relevé de la consommation réelle (`python generation/consommation.py`) : l'objectif est d'environ 1,2 $ équivalent API par cas, au format compact.

## Application Kotlin (en parallèle, après le lot 1)

Kotlin et Jetpack Compose, Room, minSdk 31, contenu JSON embarqué avec import de fichier, sans réseau. Modes : cas progressif (étape par étape, corrigé masqué puis auto-évaluation avec la grille), QROC en répétition espacée, examen blanc, bouton « signaler ».

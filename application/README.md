# Application « Cas cliniques » (Android)

Application de révision hors ligne pour le module « Maladies systémiques » : les cas cliniques générés et vérifiés, les 9 cas d'annales et les 190 QROC d'annales.

## Fonctions

- **Cas progressif** : les étapes se dévoilent au fil des questions. Après chaque réponse, le corrigé s'affiche sous forme de grille : l'étudiant coche les éléments qu'il a cités et obtient sa note sur 20. Les bilans biologiques sont marqués ↑ / ↓ selon `generation/normes.json`. Le diagnostic n'apparaît dans la liste qu'une fois le cas fait.
- **QROC en répétition espacée** : les doublons des annales sont regroupés, et les questions posées plusieurs fois passent en premier. Si la date de l'examen est renseignée, chaque carte déjà vue repasse avant la veille.
- **Examen blanc** : un cas pas encore fait et des QROC de chapitres variés, en temps limité. La copie est ramassée à la fin du temps et corrigée ensuite.
- **Progression** : notes par thème, compétences les moins réussies, QROC maîtrisées ou oubliées.
- **Signaler** : sous chaque corrigé et chaque réponse de QROC. Les signalements s'exportent en JSON (voir plus bas).
- **Importer** : un cas, une liste de cas ou un paquet JSON. Pour un même identifiant, c'est la version la plus récente qui l'emporte.

Aucune donnée ne quitte le téléphone, sauf l'export des signalements, que l'étudiant déclenche lui-même.

## Compiler et installer

1. Ouvrir le dossier `application/` dans Android Studio (JDK 17 ou plus récent). Studio crée `local.properties` avec le chemin du SDK.
2. Brancher le téléphone (Android 12 ou plus récent, débogage USB activé), puis *Run*.
   Pour obtenir un APK à copier sur le téléphone : *Build > Build APK(s)*. Le type `release` est signé avec la clé de débogage, ce qui suffit pour une installation personnelle.

Versions : Kotlin 2.2.21, AGP 8.13, Compose BOM 2025.09, Room 2.7 (fichier `gradle/libs.versions.toml`). Si Android Studio propose de mettre à jour AGP, on peut accepter.

## Mettre à jour le contenu

Le contenu embarqué est `app/src/main/assets/contenu.json`, produit par :

```
python generation/paquet.py
```

Le script ne retient que les cas au statut `valide_etudiant`, `verifie_ia` ou `annales` qui passent `valider.py`. Avec `--tous`, il inclut aussi les cas au statut `genere`, pour tester.

Deux façons de livrer de nouveaux cas :
- recompiler l'application après `paquet.py` ;
- ou, sans recompiler, copier un JSON (un cas `L3-01.json`, ou un paquet produit par `paquet.py`) sur le téléphone et l'importer depuis *Réglages et contenu*.

## Signalements

*Signalements > Exporter* produit `signalements-AAAA-MM-JJ.json`. Déposez ce fichier dans `generation/signalements/` du projet : les cas signalés sont corrigés, leur `version` augmente, et la correction revient par un nouveau paquet.

## Structure

- `core/` : logique en Kotlin pur (modèles du format JSON, normes, notation, répétition espacée, examen blanc, signalements). C'est un build autonome, testé sans SDK Android : `gradle -p core test`. Ses tests lisent aussi tout le contenu réel de `generation/`.
- `app/` : l'application Android (Compose, Room, navigation).
- `banc-compilation/` : compile le code de `app` sans SDK Android, contre Compose Multiplatform desktop et des stubs des API Android (`gradle -p banc-compilation compileKotlin`). C'est utile dans un environnement sans accès au dépôt Google. Ce banc ne vérifie ni Room ni la configuration Android.

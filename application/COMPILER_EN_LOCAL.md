# Compiler l'APK et tester l'application en local

Procédure destinée à Claude Code sur le PC de l'étudiant (Windows, Android Studio installé). Elle complète `README.md`.

L'application n'a jamais été compilée avec le SDK Android : l'environnement cloud n'a pas accès au dépôt Google. Le code Kotlin et Compose a seulement été compilé hors SDK (`banc-compilation/`), et le module `core` est testé. La première compilation locale peut donc révéler des erreurs de configuration Gradle ou de Room. Il faut les corriger au plus près, sans refondre le code.

## 1. Récupérer le dépôt

- `git status` d'abord. S'il y a des modifications locales, ne les écrase pas : fais un `git stash` et signale-le.
- `git fetch origin`, puis `git checkout main` et `git pull origin main`. Si la dernière PR n'est pas encore fusionnée, travaille sur sa branche (`git checkout claude/sharp-mayer-rkz3oj` puis `git pull`).
- Le pull supprime `.claude/scheduled_tasks.lock` du suivi git (il est désormais ignoré) : c'est voulu.

## 2. Prérequis

- JDK 17 ou plus récent. Celui d'Android Studio convient (`C:\Program Files\Android\Android Studio\jbr`) ; exporte `JAVA_HOME` si `java -version` ne répond pas.
- SDK Android : `%LOCALAPPDATA%\Android\Sdk` par défaut. Crée `application/local.properties` s'il manque, avec `sdk.dir=C\:\\Users\\<utilisateur>\\AppData\\Local\\Android\\Sdk` (échapper `:` et `\`). Ce fichier n'est pas versionné.
- Plateforme Android 36 et build-tools : si Gradle les réclame, installe-les avec `sdkmanager "platforms;android-36" "build-tools;36.0.0"` (dans `cmdline-tools\latest\bin`), ou depuis le SDK Manager d'Android Studio.
- Python 3 avec le module `markdown` (seulement pour `pdf.py`, non nécessaire ici).

## 3. Contrôler le contenu

Depuis la racine du dépôt :

```
python generation/valider.py generation/pilote generation/lots/lot_1 generation/lots/lot_2 generation/lots/lot_3 generation/lots/lot_4 generation/lots/lot_5 generation/lots/lot_6
python generation/paquet.py
```

Attendu : 60 cas, 0 erreur bloquante ; puis « 69 cas, 190 QROC ». `paquet.py` réécrit `contenu.json` avec la date du jour : ne commite pas ce fichier si seules la date et la version ont changé.

## 4. Tests et compilation

Depuis `application/` (sous Windows, `gradlew.bat` ; sous Git Bash, `./gradlew`) :

1. `gradlew -p core test` : 22 tests, tous verts.
2. `gradlew :app:assembleDebug` : produit `app/build/outputs/apk/debug/app-debug.apk`.
3. `gradlew :app:lintDebug` : corrige les erreurs ; les avertissements peuvent rester.
4. Si possible, `gradlew :app:assembleRelease` : APK signé avec la clé de débogage, suffisant pour une installation personnelle (`app/build/outputs/apk/release/app-release.apk`).

En cas d'échec :
- Une version introuvable (AGP, Kotlin, KSP, Compose BOM, Room…) : choisis dans `gradle/libs.versions.toml` la version disponible la plus proche. Garde Kotlin, le plugin Compose et KSP alignés (KSP `2.2.21-2.0.x` avec Kotlin 2.2.21), et AGP compatible avec la version d'Android Studio installée.
- Une erreur Room ou KSP (requête SQL, type non géré, conflit d'annotations) : corrige `app/src/main/java/dz/meli/cascliniques/donnees/Base.kt` sans changer le schéma des tables sans nécessité.
- Une erreur Kotlin ou Compose : corrige l'appel concerné. Si la correction change une API Android que `banc-compilation/stubs/` imite, mets le stub à jour pour que le banc cloud reste utile.

## 5. Tester sur un appareil

Si `adb devices` liste un téléphone (débogage USB activé) ou un émulateur :

1. `adb install -r app/build/outputs/apk/debug/app-debug.apk`
2. `adb shell am start -n dz.meli.cascliniques/.MainActivity`
3. `adb logcat -d | findstr /C:"FATAL EXCEPTION" /C:"AndroidRuntime"` (ou `grep -E` sous Git Bash) : aucune ligne attendue.
4. Test de robustesse : `adb shell monkey -p dz.meli.cascliniques --throttle 200 -v 1500`. Attendu : « Monkey finished », sans plantage.
5. Parcours à vérifier à la main :
   - ouvrir un cas, répondre, voir le corrigé, cocher la grille, obtenir la note ;
   - réviser quelques QROC ;
   - lancer un examen blanc de 5 QROC et rendre la copie ;
   - signaler une erreur, puis l'exporter ;
   - fermer et rouvrir l'application : la progression est conservée.

## 6. Livrer

- Copie l'APK sur le Bureau sous le nom `CasCliniques.apk` (le dossier `build/` n'est pas versionné).
- Commite les seules corrections nécessaires sur une branche `fix/compilation-android`, puis pousse. Ouvre une PR vers `main` si `gh` est disponible.
- Termine par un compte rendu : versions utilisées, erreurs rencontrées et corrections, résultats des tests (core, lint, monkey), et chemin de l'APK.

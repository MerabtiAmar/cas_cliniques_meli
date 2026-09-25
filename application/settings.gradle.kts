pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "CasCliniques"

// `core` : logique en Kotlin pur (modèles, normes, notation, répétition espacée, signalements).
// C'est un build autonome, testable sans SDK Android : `gradle -p core test`.
includeBuild("core")
include(":app")

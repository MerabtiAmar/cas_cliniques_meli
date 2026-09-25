// Banc de compilation de l'application SANS SDK Android (environnement cloud sans accès au dépôt Google).
// Compile les sources de `app` et de `core` contre Compose Multiplatform desktop 1.7.3 (Maven Central),
// avec des stubs aux vraies signatures pour les quelques API propres à Android (dossier stubs/).
// Ne vérifie ni Room (SQL, KSP) ni la configuration Android : seulement le code Kotlin et Compose.
// Usage : gradle -p application/banc-compilation compileKotlin
plugins {
    kotlin("jvm") version "2.2.21"
    kotlin("plugin.serialization") version "2.2.21"
    id("org.jetbrains.kotlin.plugin.compose") version "2.2.21"
}

sourceSets.main {
    kotlin.srcDirs("../app/src/main/java", "../core/src/main/kotlin", "stubs")
}

dependencies {
    // 1.7.3 : dernière version dont le runtime Compose ne dépend pas d'artefacts du dépôt Google.
    val cmp = "1.7.3"
    implementation("org.jetbrains.compose.runtime:runtime-desktop:$cmp")
    implementation("org.jetbrains.compose.foundation:foundation-desktop:$cmp")
    implementation("org.jetbrains.compose.ui:ui-desktop:$cmp")
    implementation("org.jetbrains.compose.material3:material3-desktop:$cmp")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
}

// Les artefacts androidx.* ne sont publiés que sur le dépôt Google.
configurations.all {
    exclude(group = "androidx.annotation")
    exclude(group = "androidx.collection")
}

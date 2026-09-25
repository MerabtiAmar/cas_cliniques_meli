import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
}

group = "dz.meli.cascliniques"
version = "1.0"

// Bytecode Java 17, compatible avec Android, quel que soit le JDK qui lance Gradle.
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

dependencies {
    api(libs.kotlinx.serialization.json)
    testImplementation(libs.kotlin.test)
}

tasks.test {
    // Les tests chargent le vrai contenu du dépôt (dossier generation/).
    systemProperty("racine.projet", rootDir.resolve("../..").canonicalPath)
}

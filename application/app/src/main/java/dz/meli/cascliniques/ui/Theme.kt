package dz.meli.cascliniques.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext
import dz.meli.cascliniques.donnees.Depot

val LocalDepot = staticCompositionLocalOf<Depot> { error("Dépôt non fourni") }

/** Couleurs dynamiques du téléphone (Android 12 et plus, donc toujours disponibles avec minSdk 31). */
@Composable
fun ThemeCas(contenu: @Composable () -> Unit) {
    val contexte = LocalContext.current
    val couleurs = if (isSystemInDarkTheme()) dynamicDarkColorScheme(contexte) else dynamicLightColorScheme(contexte)
    MaterialTheme(colorScheme = couleurs, content = contenu)
}

package dz.meli.cascliniques.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

object Routes {
    const val ACCUEIL = "accueil"
    const val LISTE_CAS = "cas"
    const val CAS = "cas/{id}"
    const val QROC = "qroc"
    const val EXAMENS = "examens"
    const val EXAMEN = "examen/{id}"
    const val PROGRESSION = "progression"
    const val SIGNALEMENTS = "signalements"
    const val REGLAGES = "reglages"

    fun cas(id: String) = "cas/$id"
    fun examen(id: Long) = "examen/$id"
}

@Composable
fun Navigation() {
    val nav = rememberNavController()
    val retour: () -> Unit = { nav.popBackStack() }
    NavHost(navController = nav, startDestination = Routes.ACCUEIL) {
        composable(Routes.ACCUEIL) { EcranAccueil(ouvrir = { nav.navigate(it) }) }
        composable(Routes.LISTE_CAS) { EcranListeCas(retour, ouvrirCas = { nav.navigate(Routes.cas(it)) }) }
        composable(Routes.CAS) { entree -> EcranCas(entree.arguments?.getString("id").orEmpty(), retour) }
        composable(Routes.QROC) { EcranQroc(retour) }
        composable(Routes.EXAMENS) { EcranExamens(retour, ouvrirExamen = { nav.navigate(Routes.examen(it)) }) }
        composable(Routes.EXAMEN) { entree -> EcranExamen(entree.arguments?.getString("id")?.toLongOrNull() ?: 0L, retour) }
        composable(Routes.PROGRESSION) { EcranProgression(retour) }
        composable(Routes.SIGNALEMENTS) { EcranSignalements(retour) }
        composable(Routes.REGLAGES) { EcranReglages(retour) }
    }
}

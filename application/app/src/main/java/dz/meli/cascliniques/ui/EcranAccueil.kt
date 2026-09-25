package dz.meli.cascliniques.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dz.meli.cascliniques.core.fileDuJour
import dz.meli.cascliniques.donnees.Tentative

@Composable
fun EcranAccueil(ouvrir: (String) -> Unit) {
    val depot = LocalDepot.current
    val paquet by depot.contenu.collectAsStateWithLifecycle()
    val cartes by depot.cartes.collectAsStateWithLifecycle()
    val revisions by depot.revisions.collectAsStateWithLifecycle()
    val nouvelles by depot.nouvellesParJour.collectAsStateWithLifecycle()
    val dateExamen by depot.dateExamen.collectAsStateWithLifecycle()
    val tentatives by depot.tentatives.collectAsStateWithLifecycle()
    val signalements by depot.signalements.collectAsStateWithLifecycle()

    val aujourdhui = depot.aujourdhui()
    val casFaits = tentatives.filter { it.fin != null && it.mode == Tentative.MODE_ENTRAINEMENT }.map { it.casId }.toSet()
    val aReviser = revisions?.let { fileDuJour(cartes, it, aujourdhui, nouvelles).size }
    val nonExportes = signalements.count { !it.exporte }

    Page("Cas cliniques · Maladies systémiques", retour = null) { marges ->
        Column(
            Modifier.padding(marges).padding(16.dp).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            dateExamen?.let { jour ->
                val restants = jour - aujourdhui
                if (restants >= 0) {
                    Text(
                        if (restants == 0L) "Examen aujourd'hui. Bon courage !" else "Examen dans $restants jour${if (restants > 1) "s" else ""}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }
            Tuile("Cas cliniques", "${casFaits.size} faits sur ${paquet.cas.size}, étape par étape avec corrigé") { ouvrir(Routes.LISTE_CAS) }
            Tuile(
                "Révision des QROC",
                aReviser?.let { "$it carte${if (it > 1) "s" else ""} à revoir aujourd'hui · ${cartes.size} questions d'annales" } ?: "…",
            ) { ouvrir(Routes.QROC) }
            Tuile("Examen blanc", "Un cas et des QROC en temps limité, corrigés à la fin") { ouvrir(Routes.EXAMENS) }
            Tuile("Progression", "Notes, compétences à retravailler, QROC maîtrisées") { ouvrir(Routes.PROGRESSION) }
            Tuile(
                "Signalements",
                if (nonExportes > 0) "$nonExportes erreur${if (nonExportes > 1) "s" else ""} signalée${if (nonExportes > 1) "s" else ""} à exporter" else "Erreurs signalées dans les cas et les QROC",
            ) { ouvrir(Routes.SIGNALEMENTS) }
            Tuile("Réglages et contenu", "Date de l'examen, rythme des QROC, import de nouveaux cas") { ouvrir(Routes.REGLAGES) }
        }
    }
}

@Composable
private fun Tuile(titre: String, detail: String, onClick: () -> Unit) {
    ElevatedCard(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text(titre, style = MaterialTheme.typography.titleMedium)
            Text(detail, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

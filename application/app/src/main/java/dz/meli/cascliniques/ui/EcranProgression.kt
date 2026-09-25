package dz.meli.cascliniques.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dz.meli.cascliniques.core.Libelles
import dz.meli.cascliniques.core.bilanParCompetence
import dz.meli.cascliniques.core.formaterNombre
import kotlin.math.roundToInt

/** Où en est l'étudiant : notes par thème, compétences les moins réussies, QROC maîtrisées. */
@Composable
fun EcranProgression(retour: () -> Unit) {
    val depot = LocalDepot.current
    val paquet by depot.contenu.collectAsStateWithLifecycle()
    val tentatives by depot.tentatives.collectAsStateWithLifecycle()
    val reponses by depot.toutesLesReponses.collectAsStateWithLifecycle()
    val cartes by depot.cartes.collectAsStateWithLifecycle()
    val revisions by depot.revisions.collectAsStateWithLifecycle()

    // Pour chaque cas, la dernière tentative terminée (entraînement ou examen) reflète le niveau actuel.
    val dernieres = tentatives.filter { it.fin != null }.groupBy { it.casId }.mapValues { (_, l) -> l.maxBy { it.fin ?: 0L } }
    val parTentative = reponses.groupBy { it.tentativeId }
    val corrections = dernieres.values.mapNotNull { t ->
        val cas = paquet.cas.find { it.id == t.casId } ?: return@mapNotNull null
        cas to parTentative[t.id].orEmpty().associate { it.question to it.indicesCoches }
    }
    val competences = bilanParCompetence(corrections)
    val parTheme = dernieres.values.mapNotNull { t ->
        val cas = paquet.cas.find { it.id == t.casId } ?: return@mapNotNull null
        t.noteSur20?.let { Libelles.theme(cas.chapitrePrincipal) to it }
    }.groupBy({ it.first }, { it.second })

    val etats = revisions.orEmpty()
    val vues = cartes.count { it.id in etats }
    val maitrisees = cartes.count { (etats[it.id]?.intervalle ?: 0) >= 7 }
    val difficiles = cartes.filter { (etats[it.id]?.echecs ?: 0) >= 2 }

    Page("Progression", retour) { marges ->
        Column(
            Modifier.padding(marges).padding(16.dp).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text("Cas cliniques", style = MaterialTheme.typography.titleMedium)
            Text("${dernieres.size} cas faits sur ${paquet.cas.size}")
            if (parTheme.isNotEmpty()) {
                for ((theme, notes) in parTheme.toSortedMap()) {
                    Ligne(theme, "${formaterNombre(notes.average().times(4).roundToInt() / 4.0)}/20 (${notes.size} cas)")
                }
            }

            HorizontalDivider()
            Text("Compétences, des moins réussies aux mieux réussies", style = MaterialTheme.typography.titleMedium)
            if (competences.isEmpty()) {
                Text("Terminez un cas pour voir vos points faibles.", style = MaterialTheme.typography.bodyMedium)
            }
            for (bilan in competences) {
                Column {
                    Ligne(Libelles.competence(bilan.competence), "${(bilan.taux * 100).roundToInt()} %")
                    LinearProgressIndicator(progress = { bilan.taux.toFloat() }, modifier = Modifier.fillMaxWidth())
                }
            }

            HorizontalDivider()
            Text("QROC", style = MaterialTheme.typography.titleMedium)
            Ligne("Cartes déjà vues", "$vues / ${cartes.size}")
            Ligne("Maîtrisées (intervalle d'une semaine ou plus)", "$maitrisees")
            if (difficiles.isNotEmpty()) {
                Text("Oubliées au moins deux fois", style = MaterialTheme.typography.titleSmall)
                Puces(difficiles.map { it.qroc.enonce })
            }
        }
    }
}

@Composable
private fun Ligne(libelle: String, valeur: String) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(libelle, Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
        Text(valeur, Modifier.padding(start = 8.dp), style = MaterialTheme.typography.labelLarge)
    }
}

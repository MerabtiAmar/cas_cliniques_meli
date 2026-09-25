package dz.meli.cascliniques.ui

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dz.meli.cascliniques.core.Cas
import dz.meli.cascliniques.core.Libelles
import dz.meli.cascliniques.core.formaterNombre
import dz.meli.cascliniques.donnees.Tentative

@Composable
fun EcranListeCas(retour: () -> Unit, ouvrirCas: (String) -> Unit) {
    val depot = LocalDepot.current
    val paquet by depot.contenu.collectAsStateWithLifecycle()
    val tentatives by depot.tentatives.collectAsStateWithLifecycle()
    var filtre by rememberSaveable { mutableStateOf<String?>(null) }

    val themes = remember(paquet) { paquet.cas.map { Libelles.theme(it.chapitrePrincipal) }.distinct().sorted() }
    val entrainements = tentatives.filter { it.mode == Tentative.MODE_ENTRAINEMENT }
    val meilleures = entrainements.mapNotNull { t -> t.noteSur20?.let { t.casId to it } }
        .groupBy({ it.first }, { it.second })
        .mapValues { (_, notes) -> notes.max() }
    val enCours = entrainements.filter { it.fin == null && it.questionCourante > 1 }.map { it.casId }.toSet()
    val affiches = paquet.cas.filter { filtre == null || Libelles.theme(it.chapitrePrincipal) == filtre }

    Page("Cas cliniques", retour) { marges ->
        LazyColumn(contentPadding = marges) {
            item {
                Row(
                    Modifier.horizontalScroll(rememberScrollState()).padding(horizontal = 12.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    FilterChip(selected = filtre == null, onClick = { filtre = null }, label = { Text("Tous (${paquet.cas.size})") })
                    for (theme in themes) {
                        FilterChip(selected = filtre == theme, onClick = { filtre = theme }, label = { Text(theme) })
                    }
                }
            }
            items(affiches, key = { it.id }) { cas ->
                LigneCas(cas, meilleures[cas.id], cas.id in enCours) { ouvrirCas(cas.id) }
            }
        }
    }
}

@Composable
private fun LigneCas(cas: Cas, meilleureNote: Double?, enCours: Boolean, onClick: () -> Unit) {
    Card(onClick = onClick, modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 4.dp)) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("${cas.id} · ${Libelles.theme(cas.chapitrePrincipal)}", style = MaterialTheme.typography.titleSmall)
                // Le diagnostic n'est dévoilé qu'une fois le cas fait, pour ne pas gâcher l'exercice.
                if (meilleureNote != null) {
                    Text(cas.maladiePrincipale, style = MaterialTheme.typography.bodySmall)
                } else {
                    Text("Diagnostic dévoilé à la fin du cas", style = MaterialTheme.typography.bodySmall, fontStyle = FontStyle.Italic)
                }
                val details = listOfNotNull(
                    cas.origine.libelle,
                    cas.difficulte?.let { "difficulté $it/3" },
                    cas.dureeMinutes?.let { "~$it min" },
                )
                Text(details.joinToString(" · "), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
            }
            when {
                meilleureNote != null -> Text(
                    "${formaterNombre(meilleureNote)}/20",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
                enCours -> Text("en cours", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.tertiary)
            }
        }
    }
}

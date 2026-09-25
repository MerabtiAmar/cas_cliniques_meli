package dz.meli.cascliniques.ui

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dz.meli.cascliniques.core.CarteQroc
import dz.meli.cascliniques.core.Libelles
import dz.meli.cascliniques.core.Note
import dz.meli.cascliniques.core.fileDuJour

/** Révision des QROC d'annales en répétition espacée. */
@Composable
fun EcranQroc(retour: () -> Unit) {
    val depot = LocalDepot.current
    val cartes by depot.cartes.collectAsStateWithLifecycle()
    val revisions by depot.revisions.collectAsStateWithLifecycle()
    val nouvelles by depot.nouvellesParJour.collectAsStateWithLifecycle()
    var chapitre by rememberSaveable { mutableStateOf<String?>(null) }
    // « Tout le chapitre » : révise aussi les cartes qui ne sont pas encore dues.
    var libre by rememberSaveable { mutableStateOf(false) }
    val file = remember { mutableStateListOf<CarteQroc>() }
    var tour by remember { mutableIntStateOf(0) }
    val chargees = revisions != null && cartes.isNotEmpty()

    // La file de la séance est composée une fois (par filtre) : elle ne bouge pas à chaque réponse.
    LaunchedEffect(chapitre, libre, chargees) {
        val etats = revisions ?: return@LaunchedEffect
        val selection = cartes.filter { chapitre == null || it.qroc.chapitre == chapitre }
        file.clear()
        if (libre) {
            val aujourdhui = depot.aujourdhui()
            file.addAll(selection.sortedWith(compareBy<CarteQroc> { etats[it.id]?.echeance ?: aujourdhui }.thenByDescending { it.occurrences.size }))
        } else {
            file.addAll(fileDuJour(selection, etats, depot.aujourdhui(), nouvelles))
        }
    }

    val chapitres = remember(cartes) { cartes.map { it.qroc.chapitre }.distinct().sorted() }

    Page("QROC · ${file.size} restante${if (file.size > 1) "s" else ""}", retour) { marges ->
        Column(Modifier.padding(marges).verticalScroll(rememberScrollState())) {
            Row(
                Modifier.horizontalScroll(rememberScrollState()).padding(horizontal = 12.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                FilterChip(selected = chapitre == null, onClick = { chapitre = null }, label = { Text("Tous les chapitres") })
                for (c in chapitres) {
                    FilterChip(selected = chapitre == c, onClick = { chapitre = c }, label = { Text(Libelles.chapitre(c)) })
                }
            }
            val carte = file.firstOrNull()
            when {
                !chargees -> Chargement()
                carte == null -> Column(Modifier.fillMaxWidth().padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        if (libre) "Chapitre terminé." else "Rien à réviser pour l'instant. Revenez demain, ou révisez tout le chapitre.",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                    )
                    OutlinedButton(onClick = { libre = !libre }, modifier = Modifier.fillMaxWidth()) {
                        Text(if (libre) "Revenir aux cartes du jour" else "Réviser tout le chapitre")
                    }
                }
                else -> CarteRevision(carte, tour) { note ->
                    depot.noter(carte, note)
                    file.removeAt(0)
                    // Une carte oubliée revient en fin de séance.
                    if (note == Note.A_REVOIR) file.add(carte)
                    tour++
                }
            }
        }
    }
}

@Composable
private fun CarteRevision(carte: CarteQroc, tour: Int, onNote: (Note) -> Unit) {
    val depot = LocalDepot.current
    val revisions by depot.revisions.collectAsStateWithLifecycle()
    var revelee by remember(carte.id, tour) { mutableStateOf(false) }
    var brouillon by remember(carte.id, tour) { mutableStateOf("") }
    var signaler by remember { mutableStateOf(false) }
    val qroc = carte.qroc

    Card(Modifier.fillMaxWidth().padding(12.dp)) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(Libelles.chapitre(qroc.chapitre), style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
            val n = carte.occurrences.size
            Text(
                (if (n > 1) "Posée $n fois" else "Posée 1 fois") +
                    carte.sessions.takeIf { it.isNotEmpty() }?.let { " (${it.joinToString(", ")})" }.orEmpty(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline,
            )
            Text(qroc.enonce, style = MaterialTheme.typography.titleMedium)
            OutlinedTextField(
                value = brouillon,
                onValueChange = { brouillon = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Votre réponse (facultatif)") },
                minLines = 2,
            )
            if (!revelee) {
                Button(onClick = { revelee = true }, modifier = Modifier.fillMaxWidth()) { Text("Voir la réponse") }
            } else {
                Text("Réponse attendue", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.tertiary)
                Puces(qroc.reponse)
                if (qroc.origineReponse == "claude") {
                    Text(
                        "Réponse rédigée faute de corrigé dans les annales.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                    )
                }
                if (qroc.sources.isNotEmpty()) {
                    Text(
                        qroc.sources.joinToString(" ; ") { Libelles.source(it) },
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline,
                    )
                }
                TextButton(onClick = { signaler = true }) { Text("Signaler une erreur") }
                val etat = revisions?.get(carte.id)
                val planificateur = depot.planificateur()
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    for (note in Note.entries) {
                        val jours = planificateur.reviser(etat, carte.id, note, depot.aujourdhui()).intervalle
                        FilledTonalButton(onClick = { onNote(note) }, modifier = Modifier.weight(1f)) {
                            Text(
                                "${note.libelle}\n${if (jours == 0) "< 1 j" else "$jours j"}",
                                style = MaterialTheme.typography.labelSmall,
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                }
            }
        }
    }

    if (signaler) {
        DialogueSignalement(
            "QROC ${qroc.id} (${qroc.session})",
            onEnvoyer = { texte ->
                depot.signaler("qroc", qroc.id, null, null, null, texte)
                signaler = false
            },
            onAnnuler = { signaler = false },
        )
    }
}

@file:OptIn(ExperimentalMaterial3Api::class)

package dz.meli.cascliniques.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dz.meli.cascliniques.core.Etape
import dz.meli.cascliniques.core.Libelles
import dz.meli.cascliniques.core.Norme
import dz.meli.cascliniques.core.Patient
import dz.meli.cascliniques.core.Question
import dz.meli.cascliniques.core.Statut
import dz.meli.cascliniques.core.evaluer
import dz.meli.cascliniques.core.formaterNombre
import dz.meli.cascliniques.core.pointsObtenus
import dz.meli.cascliniques.core.valeurAffichee

/** Squelette commun : barre de titre avec retour (« ← ») et actions. */
@Composable
fun Page(
    titre: String,
    retour: (() -> Unit)?,
    actions: @Composable RowScope.() -> Unit = {},
    contenu: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(titre, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                navigationIcon = {
                    if (retour != null) {
                        TextButton(onClick = retour) { Text("←", style = MaterialTheme.typography.titleLarge) }
                    }
                },
                actions = actions,
            )
        },
    ) { marges -> contenu(marges) }
}

@Composable
fun Chargement(marges: PaddingValues = PaddingValues()) {
    Box(Modifier.fillMaxSize().padding(marges), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
}

@Composable
fun Message(texte: String, marges: PaddingValues = PaddingValues()) {
    Box(Modifier.fillMaxSize().padding(marges).padding(24.dp), contentAlignment = Alignment.Center) {
        Text(texte, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

fun points(x: Double): String = "${formaterNombre(x)} pt"

/** Texte de l'étape puis ses résultats d'examens, groupés, avec ↑ / ↓ par rapport aux normes. */
@Composable
fun BlocEtape(numero: Int, etape: Etape, patient: Patient, normes: Map<String, Norme>) {
    Column(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
        if (numero > 1) {
            HorizontalDivider(Modifier.padding(bottom = 8.dp))
            Text("Suite", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
        }
        Text(etape.texte, style = MaterialTheme.typography.bodyLarge)
        var groupe: String? = null
        for (examen in etape.examens) {
            if (examen.groupe != groupe) {
                groupe = examen.groupe
                Spacer(Modifier.height(8.dp))
                Text(examen.groupe, style = MaterialTheme.typography.titleSmall)
            }
            val evaluation = examen.evaluer(normes, patient, etape)
            val anormal = evaluation != null && evaluation.statut != Statut.NORMAL
            val fleche = when (evaluation?.statut) {
                Statut.HAUT -> " ↑"
                Statut.BAS -> " ↓"
                else -> ""
            }
            Row(Modifier.fillMaxWidth().padding(vertical = 2.dp)) {
                Text(examen.parametre, Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.width(8.dp))
                Text(
                    "${examen.valeurAffichee()} ${examen.unite}$fleche".trim(),
                    Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = if (anormal) FontWeight.SemiBold else FontWeight.Normal,
                    color = if (anormal) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}

/**
 * Une question : énoncé, réponse de l'étudiant, puis (si demandé) le corrigé sous forme de grille
 * à cocher pour l'auto-évaluation.
 */
@Composable
fun CarteQuestion(
    question: Question,
    texte: String,
    onTexte: ((String) -> Unit)?,
    afficherCorrige: Boolean,
    coches: Set<Int>,
    onCoche: ((Int, Boolean) -> Unit)?,
    onSignaler: () -> Unit,
    pied: @Composable () -> Unit = {},
) {
    Card(Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 6.dp)) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Text(
                    "Q${question.n}. ${question.enonce}",
                    Modifier.weight(1f),
                    style = MaterialTheme.typography.titleSmall,
                )
                Text(points(question.points), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
            }
            if (onTexte != null) {
                OutlinedTextField(
                    value = texte,
                    onValueChange = onTexte,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Votre réponse") },
                    minLines = 3,
                )
            } else if (texte.isNotBlank()) {
                Surface(color = MaterialTheme.colorScheme.surfaceVariant, shape = MaterialTheme.shapes.small) {
                    Text(texte, Modifier.fillMaxWidth().padding(8.dp), style = MaterialTheme.typography.bodyMedium)
                }
            }
            if (afficherCorrige) Corrige(question, coches, onCoche, onSignaler)
            pied()
        }
    }
}

@Composable
private fun Corrige(question: Question, coches: Set<Int>, onCoche: ((Int, Boolean) -> Unit)?, onSignaler: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "Corrigé · ${Libelles.competence(question.competence)}",
                Modifier.weight(1f),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.tertiary,
            )
            TextButton(onClick = onSignaler) { Text("Signaler") }
        }
        if (question.aVerifier) {
            Text(
                "⚠ Un élément de cette question ne repose sur aucune source du corpus.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
            )
        }
        question.elementsAttendus.forEachIndexed { i, element ->
            Row(verticalAlignment = Alignment.Top) {
                Checkbox(
                    checked = i in coches,
                    onCheckedChange = onCoche?.let { rappel -> { coche: Boolean -> rappel(i, coche) } },
                )
                Column(Modifier.weight(1f).padding(top = 12.dp)) {
                    Text(
                        "${element.texte} (${points(element.points)})",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                    if (element.justification.isNotBlank()) {
                        Text(element.justification, style = MaterialTheme.typography.bodySmall)
                    }
                    if (element.sources.isNotEmpty()) {
                        Text(
                            element.sources.joinToString(" ; ") { Libelles.source(it) },
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.outline,
                        )
                    }
                }
            }
        }
        if (question.signesNegatifs.isNotEmpty()) {
            Text(
                "Signes négatifs à citer : " + question.signesNegatifs.joinToString(" ; "),
                style = MaterialTheme.typography.bodySmall,
                fontStyle = FontStyle.Italic,
            )
        }
        if (question.piege.isNotBlank()) {
            Surface(color = MaterialTheme.colorScheme.errorContainer, shape = MaterialTheme.shapes.small) {
                Text(
                    "Piège : ${question.piege}",
                    Modifier.padding(8.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                )
            }
        }
        Text(
            "Votre score : ${points(question.pointsObtenus(coches))} sur ${points(question.points)}",
            style = MaterialTheme.typography.labelLarge,
        )
    }
}

/** Liste à puces simple. */
@Composable
fun Puces(lignes: List<String>) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        for (ligne in lignes) {
            Row {
                Text("•  ", style = MaterialTheme.typography.bodyMedium)
                Text(ligne, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

/** Saisie d'un signalement d'erreur. */
@Composable
fun DialogueSignalement(titre: String, onEnvoyer: (String) -> Unit, onAnnuler: () -> Unit) {
    var commentaire by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onAnnuler,
        title = { Text("Signaler une erreur") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(titre, style = MaterialTheme.typography.bodySmall)
                OutlinedTextField(
                    value = commentaire,
                    onValueChange = { commentaire = it },
                    label = { Text("Qu'est-ce qui est faux ? (source, valeur…)") },
                    minLines = 3,
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onEnvoyer(commentaire) }, enabled = commentaire.isNotBlank()) { Text("Enregistrer") }
        },
        dismissButton = { TextButton(onClick = onAnnuler) { Text("Annuler") } },
    )
}

/** Encadré de résultat en fin de cas. */
@Composable
fun CarteResultat(contenu: @Composable () -> Unit) {
    Card(
        Modifier.fillMaxWidth().padding(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) { contenu() }
    }
}

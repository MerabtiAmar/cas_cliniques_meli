@file:OptIn(ExperimentalMaterial3Api::class)

package dz.meli.cascliniques.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dz.meli.cascliniques.core.FormatInconnu
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

private const val JOUR_MS = 86_400_000L

@Composable
fun EcranReglages(retour: () -> Unit) {
    val depot = LocalDepot.current
    val contexte = LocalContext.current
    val scope = rememberCoroutineScope()
    val paquet by depot.contenu.collectAsStateWithLifecycle()
    val dateExamen by depot.dateExamen.collectAsStateWithLifecycle()
    val nouvelles by depot.nouvellesParJour.collectAsStateWithLifecycle()
    var choixDate by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf<String?>(null) }
    var imports by remember { mutableStateOf(depot.nombreImports) }

    val importer = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) {
            scope.launch {
                message = try {
                    val texte = withContext(Dispatchers.IO) {
                        contexte.contentResolver.openInputStream(uri)?.bufferedReader()?.use { it.readText() }
                    } ?: throw FormatInconnu("Fichier illisible.")
                    val resultat = depot.importer(texte)
                    buildString {
                        append("Importé : ${resultat.resume}.")
                        for (refus in resultat.refuses) append("\n\nRefusé ${refus.id} : ${refus.problemes.joinToString(" ; ")}")
                    }
                } catch (e: FormatInconnu) {
                    e.message
                } catch (e: Exception) {
                    "Import impossible : ${e.message}"
                }
                imports = depot.nombreImports
            }
        }
    }

    Page("Réglages et contenu", retour) { marges ->
        Column(
            Modifier.padding(marges).padding(16.dp).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text("Examen", style = MaterialTheme.typography.titleMedium)
            val format = DateTimeFormatter.ofPattern("EEEE d MMMM yyyy", Locale.FRENCH)
            Text(dateExamen?.let { "Date : " + LocalDate.ofEpochDay(it).format(format) } ?: "Date non renseignée")
            Text(
                "Connue, elle garantit que chaque QROC déjà vue repasse avant la veille de l'examen.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = { choixDate = true }) { Text("Choisir la date") }
                if (dateExamen != null) TextButton(onClick = { depot.definirDateExamen(null) }) { Text("Effacer") }
            }

            HorizontalDivider()
            Text("Révision des QROC", style = MaterialTheme.typography.titleMedium)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Nouvelles cartes par jour", Modifier.weight(1f))
                TextButton(onClick = { depot.definirNouvellesParJour((nouvelles - 5).coerceAtLeast(0)) }) { Text("−") }
                Text("$nouvelles", style = MaterialTheme.typography.titleMedium)
                TextButton(onClick = { depot.definirNouvellesParJour((nouvelles + 5).coerceAtMost(100)) }) { Text("+") }
            }

            HorizontalDivider()
            Text("Contenu", style = MaterialTheme.typography.titleMedium)
            Text("${paquet.cas.size} cas cliniques, ${paquet.qroc.size} QROC" + (paquet.genereLe?.let { " · contenu du $it" } ?: ""))
            Text(
                "Importez un fichier JSON (un cas, une liste de cas ou un paquet) pour ajouter des cas ou remplacer un cas corrigé. " +
                    "À identifiant égal, la version la plus récente l'emporte.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Button(onClick = { importer.launch(arrayOf("application/json", "text/plain", "application/octet-stream")) }, modifier = Modifier.fillMaxWidth()) {
                Text("Importer un fichier")
            }
            if (imports > 0) {
                OutlinedButton(
                    onClick = {
                        scope.launch {
                            depot.supprimerImports()
                            imports = depot.nombreImports
                            message = "Contenus importés supprimés : retour au contenu d'origine."
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                ) { Text("Supprimer les $imports fichier(s) importé(s)") }
            }
        }
    }

    if (choixDate) {
        val etat = rememberDatePickerState(initialSelectedDateMillis = (dateExamen ?: depot.aujourdhui()) * JOUR_MS)
        DatePickerDialog(
            onDismissRequest = { choixDate = false },
            confirmButton = {
                TextButton(onClick = {
                    etat.selectedDateMillis?.let { depot.definirDateExamen(it / JOUR_MS) }
                    choixDate = false
                }) { Text("Valider") }
            },
            dismissButton = { TextButton(onClick = { choixDate = false }) { Text("Annuler") } },
        ) {
            DatePicker(state = etat)
        }
    }
    message?.let { texte ->
        AlertDialog(
            onDismissRequest = { message = null },
            confirmButton = { TextButton(onClick = { message = null }) { Text("OK") } },
            text = { Text(texte) },
        )
    }
}

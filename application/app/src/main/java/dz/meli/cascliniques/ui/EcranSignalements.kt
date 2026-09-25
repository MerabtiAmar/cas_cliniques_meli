package dz.meli.cascliniques.ui

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

/**
 * Erreurs signalées. L'export produit un fichier JSON à déposer dans `generation/signalements/`
 * du projet, pour que les cas et les QROC concernés soient corrigés.
 */
@Composable
fun EcranSignalements(retour: () -> Unit) {
    val depot = LocalDepot.current
    val contexte = LocalContext.current
    val scope = rememberCoroutineScope()
    val signalements by depot.signalements.collectAsStateWithLifecycle()

    val exporter = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("application/json")) { uri ->
        if (uri != null) {
            val aExporter = signalements
            scope.launch {
                val ok = withContext(Dispatchers.IO) {
                    runCatching {
                        contexte.contentResolver.openOutputStream(uri)?.bufferedWriter()?.use { it.write(depot.texteExport(aExporter)) }
                    }.isSuccess
                }
                if (ok) depot.acces.marquerExportes(aExporter.map { it.id })
                Toast.makeText(contexte, if (ok) "${aExporter.size} signalement(s) exporté(s)" else "Échec de l'export", Toast.LENGTH_LONG).show()
            }
        }
    }

    Page(
        "Signalements",
        retour,
        actions = {
            TextButton(onClick = { exporter.launch("signalements-${LocalDate.now()}.json") }, enabled = signalements.isNotEmpty()) {
                Text("Exporter")
            }
        },
    ) { marges ->
        if (signalements.isEmpty()) {
            Message("Aucun signalement. Le bouton « Signaler » apparaît sous chaque corrigé.", marges)
        } else {
            LazyColumn(contentPadding = marges) {
                item {
                    Text(
                        "Exportez le fichier puis déposez-le dans generation/signalements/ du projet : les cas concernés seront corrigés et renvoyés dans une mise à jour.",
                        Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
                items(signalements, key = { it.id }) { s ->
                    Card(Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 4.dp)) {
                        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Column(Modifier.weight(1f)) {
                                val cible = if (s.cible == "cas") "Cas ${s.cibleId}${s.question?.let { ", Q$it" }.orEmpty()}" else "QROC ${s.cibleId}"
                                Text(cible, style = MaterialTheme.typography.titleSmall)
                                Text(s.commentaire, style = MaterialTheme.typography.bodyMedium)
                                Text(
                                    s.date.take(16).replace('T', ' ') + if (s.exporte) " · exporté" else "",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.outline,
                                )
                            }
                            TextButton(onClick = { scope.launch { depot.acces.supprimerSignalement(s.id) } }) { Text("Supprimer") }
                        }
                    }
                }
            }
        }
    }
}

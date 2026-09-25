package dz.meli.cascliniques.ui

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dz.meli.cascliniques.core.CarteQroc
import dz.meli.cascliniques.core.Cas
import dz.meli.cascliniques.core.Libelles
import dz.meli.cascliniques.core.Norme
import dz.meli.cascliniques.core.Question
import dz.meli.cascliniques.core.formaterNombre
import dz.meli.cascliniques.donnees.Depot
import dz.meli.cascliniques.donnees.ExamenBlanc
import dz.meli.cascliniques.donnees.Reponse
import dz.meli.cascliniques.donnees.Tentative
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.util.Date

/** Configuration d'un nouvel examen blanc et historique des précédents. */
@Composable
fun EcranExamens(retour: () -> Unit, ouvrirExamen: (Long) -> Unit) {
    val depot = LocalDepot.current
    val paquet by depot.contenu.collectAsStateWithLifecycle()
    val examens by depot.examensBlancs.collectAsStateWithLifecycle()
    val tentatives by depot.tentatives.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    var theme by rememberSaveable { mutableStateOf<String?>(null) }
    var nbQroc by rememberSaveable { mutableStateOf(10) }
    val themes = remember(paquet) { paquet.cas.map { Libelles.theme(it.chapitrePrincipal) }.distinct().sorted() }

    Page("Examen blanc", retour) { marges ->
        LazyColumn(contentPadding = marges) {
            item {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        "Un cas clinique que vous n'avez pas encore fait et des QROC de chapitres variés, en temps limité. " +
                            "Les corrigés s'affichent après avoir rendu la copie.",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Text("Chapitre du cas", style = MaterialTheme.typography.titleSmall)
                    Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(selected = theme == null, onClick = { theme = null }, label = { Text("Au hasard") })
                        for (t in themes) FilterChip(selected = theme == t, onClick = { theme = t }, label = { Text(t) })
                    }
                    Text("Nombre de QROC", style = MaterialTheme.typography.titleSmall)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        for (n in listOf(0, 5, 10, 15, 20)) {
                            FilterChip(selected = nbQroc == n, onClick = { nbQroc = n }, label = { Text("$n") })
                        }
                    }
                    Button(
                        onClick = { scope.launch { depot.creerExamenBlanc(theme, nbQroc)?.let(ouvrirExamen) } },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = paquet.cas.isNotEmpty(),
                    ) { Text("Commencer") }
                }
            }
            if (examens.isNotEmpty()) {
                item { Text("Examens précédents", Modifier.padding(horizontal = 16.dp), style = MaterialTheme.typography.titleSmall) }
            }
            items(examens, key = { it.id }) { examen ->
                val tentative = tentatives.find { it.id == examen.tentativeId }
                val quand = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(Date(examen.debut))
                val resultat = when (examen.phase) {
                    ExamenBlanc.PHASE_TERMINE -> buildString {
                        append("Cas : ${tentative?.noteSur20?.let(::formaterNombre) ?: "?"}/20")
                        if (examen.idsQroc.isNotEmpty()) {
                            append(" · QROC : ${formaterNombre(Depot.lireNombres(examen.notesQroc).values.sum())}/${examen.idsQroc.size}")
                        }
                    }
                    ExamenBlanc.PHASE_CORRECTION -> "correction en cours"
                    else -> "épreuve en cours"
                }
                Card(onClick = { ouvrirExamen(examen.id) }, modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 4.dp)) {
                    Column(Modifier.padding(12.dp)) {
                        Text("$quand · cas ${tentative?.casId ?: "?"}", style = MaterialTheme.typography.titleSmall)
                        Text(resultat, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}

/** Déroulement d'un examen blanc : épreuve chronométrée, puis correction, puis résultat. */
@Composable
fun EcranExamen(examenId: Long, retour: () -> Unit) {
    val depot = LocalDepot.current
    val paquet by depot.contenu.collectAsStateWithLifecycle()
    val cartes by depot.cartes.collectAsStateWithLifecycle()
    val fluxExamen = remember(examenId) { depot.acces.examenBlanc(examenId) }
    val examen by fluxExamen.collectAsStateWithLifecycle(initialValue = null)
    val e = examen
    val tentativeId = e?.tentativeId ?: -1L
    val fluxTentative = remember(tentativeId) { depot.acces.tentative(tentativeId) }
    val fluxReponses = remember(tentativeId) { depot.acces.reponses(tentativeId) }
    val tentative by fluxTentative.collectAsStateWithLifecycle(initialValue = null)
    val reponses by fluxReponses.collectAsStateWithLifecycle(initialValue = null)
    val t = tentative
    val r = reponses
    val cas = t?.let { tt -> paquet.cas.find { it.id == tt.casId } }

    // Chronomètre : rafraîchi chaque seconde pendant l'épreuve.
    var maintenant by remember { mutableLongStateOf(System.currentTimeMillis()) }
    LaunchedEffect(e?.phase) {
        while (e?.phase == ExamenBlanc.PHASE_EPREUVE) {
            maintenant = System.currentTimeMillis()
            delay(1_000)
        }
    }
    val restantMs = e?.let { it.debut + it.dureeMinutes * 60_000L - maintenant } ?: 0L
    val titre = when (e?.phase) {
        ExamenBlanc.PHASE_EPREUVE -> "Temps restant ${chrono(restantMs)}"
        ExamenBlanc.PHASE_CORRECTION -> "Correction"
        else -> "Examen blanc"
    }

    Page(titre, retour) { marges ->
        if (e == null || t == null || r == null || cas == null) {
            Chargement(marges)
        } else {
            val qroc = e.idsQroc.mapNotNull { id -> cartes.find { it.id == id } }
            DeroulementExamen(e, t, cas, r.associateBy { it.question }, qroc, paquet.normes, restantMs, marges)
        }
    }
}

private fun chrono(ms: Long): String {
    val s = (ms / 1000).coerceAtLeast(0)
    return "%d:%02d".format(s / 60, s % 60)
}

@Composable
private fun DeroulementExamen(
    examen: ExamenBlanc,
    tentative: Tentative,
    cas: Cas,
    reponses: Map<Int, Reponse>,
    qroc: List<CarteQroc>,
    normes: Map<String, Norme>,
    restantMs: Long,
    marges: PaddingValues,
) {
    val depot = LocalDepot.current
    val epreuve = examen.phase == ExamenBlanc.PHASE_EPREUVE
    val nb = cas.questions.size
    // Pendant l'épreuve, les étapes se dévoilent au fil des questions ; ensuite, tout est visible.
    val courante = if (epreuve) tentative.questionCourante else nb + 1
    val etapeDe = remember(cas) { cas.etapes.flatMapIndexed { i, etape -> etape.questions.map { it.n to i } }.toMap() }
    val derniereEtape = if (courante > nb) cas.etapes.lastIndex else etapeDe[courante] ?: 0
    var reponsesQroc by remember(examen.id) { mutableStateOf(Depot.lireTextes(examen.reponsesQroc)) }
    var notesQroc by remember(examen.id) { mutableStateOf(Depot.lireNombres(examen.notesQroc)) }
    var confirmer by remember { mutableStateOf(false) }
    var aSignaler by remember { mutableStateOf<Question?>(null) }

    fun rendreLaCopie() {
        depot.enregistrer(examen.copy(reponsesQroc = Depot.ecrireTextes(reponsesQroc), phase = ExamenBlanc.PHASE_CORRECTION))
        depot.passerA(tentative, nb + 1)
    }

    // Temps écoulé : la copie est ramassée.
    if (epreuve && restantMs <= 0) LaunchedEffect(Unit) { rendreLaCopie() }
    // Sauvegarde des réponses aux QROC après une pause de frappe.
    if (epreuve) {
        LaunchedEffect(reponsesQroc) {
            delay(800)
            depot.enregistrer(examen.copy(reponsesQroc = Depot.ecrireTextes(reponsesQroc)))
        }
    }

    LazyColumn(Modifier.fillMaxSize(), contentPadding = marges) {
        cas.etapes.forEachIndexed { i, etape ->
            if (i <= derniereEtape) {
                item(key = "etape-$i") { BlocEtape(i + 1, etape, cas.patient, normes) }
                items(etape.questions.filter { it.n <= courante }, key = { "q-${it.n}" }) { q ->
                    QuestionExamen(examen, tentative, q, reponses[q.n], estCourante = epreuve && q.n == courante) { aSignaler = q }
                }
            }
        }
        if (qroc.isNotEmpty()) {
            item(key = "titre-qroc") {
                Text("QROC", Modifier.padding(16.dp), style = MaterialTheme.typography.titleLarge)
            }
            items(qroc, key = { "qroc-${it.id}" }) { carte ->
                QrocExamen(
                    carte = carte,
                    epreuve = epreuve,
                    reponse = reponsesQroc[carte.id].orEmpty(),
                    onReponse = { reponsesQroc = reponsesQroc + (carte.id to it) },
                    note = notesQroc[carte.id],
                    onNote = { valeur: Double ->
                        notesQroc = notesQroc + (carte.id to valeur)
                        depot.enregistrer(examen.copy(reponsesQroc = Depot.ecrireTextes(reponsesQroc), notesQroc = Depot.ecrireNombres(notesQroc)))
                    }.takeIf { examen.phase == ExamenBlanc.PHASE_CORRECTION },
                )
            }
        }
        item(key = "fin") {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                when (examen.phase) {
                    ExamenBlanc.PHASE_EPREUVE -> Button(onClick = { confirmer = true }, modifier = Modifier.fillMaxWidth()) {
                        Text("Rendre la copie")
                    }
                    ExamenBlanc.PHASE_CORRECTION -> Button(
                        onClick = {
                            depot.terminer(tentative, cas)
                            depot.enregistrer(
                                examen.copy(
                                    reponsesQroc = Depot.ecrireTextes(reponsesQroc),
                                    notesQroc = Depot.ecrireNombres(notesQroc),
                                    phase = ExamenBlanc.PHASE_TERMINE,
                                ),
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                    ) { Text("Voir le résultat") }
                    else -> CarteResultat {
                        Text("Cas : ${tentative.noteSur20?.let(::formaterNombre) ?: "…"} / 20", style = MaterialTheme.typography.headlineSmall)
                        if (qroc.isNotEmpty()) {
                            Text("QROC : ${formaterNombre(notesQroc.values.sum())} / ${qroc.size}", style = MaterialTheme.typography.titleMedium)
                        }
                        Text("Diagnostic : ${cas.maladiePrincipale}", style = MaterialTheme.typography.titleMedium)
                        Text("À retenir", style = MaterialTheme.typography.titleSmall)
                        Puces(cas.synthese)
                    }
                }
            }
        }
    }

    if (confirmer) {
        val sansReponse = (1..nb).count { reponses[it]?.texte.isNullOrBlank() } + qroc.count { reponsesQroc[it.id].isNullOrBlank() }
        AlertDialog(
            onDismissRequest = { confirmer = false },
            title = { Text("Rendre la copie ?") },
            text = { Text(if (sansReponse > 0) "$sansReponse question(s) sans réponse. Vous ne pourrez plus modifier vos réponses." else "Vous ne pourrez plus modifier vos réponses.") },
            confirmButton = { TextButton(onClick = { confirmer = false; rendreLaCopie() }) { Text("Rendre") } },
            dismissButton = { TextButton(onClick = { confirmer = false }) { Text("Continuer") } },
        )
    }
    aSignaler?.let { q ->
        DialogueSignalement(
            "Cas ${cas.id}, question ${q.n}",
            onEnvoyer = { texte ->
                depot.signaler("cas", cas.id, cas.version, q.n, null, texte)
                aSignaler = null
            },
            onAnnuler = { aSignaler = null },
        )
    }
}

@Composable
private fun QuestionExamen(
    examen: ExamenBlanc,
    tentative: Tentative,
    question: Question,
    reponse: Reponse?,
    estCourante: Boolean,
    onSignaler: () -> Unit,
) {
    val depot = LocalDepot.current
    val epreuve = examen.phase == ExamenBlanc.PHASE_EPREUVE
    var texte by remember(tentative.id, question.n) { mutableStateOf(reponse?.texte.orEmpty()) }
    var coches by remember(tentative.id, question.n) { mutableStateOf(reponse?.indicesCoches.orEmpty()) }
    if (estCourante) {
        LaunchedEffect(texte) {
            delay(600)
            if (texte.isNotEmpty() || reponse != null) depot.enregistrerTexte(tentative.id, question.n, texte)
        }
    }
    CarteQuestion(
        question = question,
        texte = texte,
        onTexte = { v: String -> texte = v }.takeIf { estCourante },
        afficherCorrige = !epreuve,
        coches = coches,
        onCoche = { i: Int, coche: Boolean ->
            coches = if (coche) coches + i else coches - i
            depot.enregistrerCoches(tentative.id, question.n, coches)
        }.takeIf { examen.phase == ExamenBlanc.PHASE_CORRECTION },
        onSignaler = onSignaler,
    ) {
        if (estCourante) {
            Button(
                onClick = {
                    depot.enregistrerTexte(tentative.id, question.n, texte)
                    depot.passerA(tentative, question.n + 1)
                },
                modifier = Modifier.fillMaxWidth(),
            ) { Text("Question suivante") }
        }
    }
}

@Composable
private fun QrocExamen(
    carte: CarteQroc,
    epreuve: Boolean,
    reponse: String,
    onReponse: (String) -> Unit,
    note: Double?,
    onNote: ((Double) -> Unit)?,
) {
    Card(Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 6.dp)) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(Libelles.chapitre(carte.qroc.chapitre), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
            Text(carte.qroc.enonce, style = MaterialTheme.typography.titleSmall)
            if (epreuve) {
                OutlinedTextField(
                    value = reponse,
                    onValueChange = onReponse,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Votre réponse") },
                    minLines = 2,
                )
            } else {
                if (reponse.isNotBlank()) Text("Votre réponse : $reponse", style = MaterialTheme.typography.bodyMedium)
                Text("Réponse attendue", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.tertiary)
                Puces(carte.qroc.reponse)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    for ((valeur, libelle) in listOf(0.0 to "Faux", 0.5 to "Partiel", 1.0 to "Juste")) {
                        FilterChip(
                            selected = note == valeur,
                            onClick = { onNote?.invoke(valeur) },
                            enabled = onNote != null,
                            label = { Text(libelle) },
                        )
                    }
                }
            }
        }
    }
}

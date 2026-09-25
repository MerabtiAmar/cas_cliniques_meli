package dz.meli.cascliniques.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dz.meli.cascliniques.core.Cas
import dz.meli.cascliniques.core.Norme
import dz.meli.cascliniques.core.Question
import dz.meli.cascliniques.core.formaterNombre
import dz.meli.cascliniques.donnees.Reponse
import dz.meli.cascliniques.donnees.Tentative
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/** Cas progressif en entraînement : une question à la fois, corrigé et auto-évaluation après chaque réponse. */
@Composable
fun EcranCas(casId: String, retour: () -> Unit) {
    val depot = LocalDepot.current
    val paquet by depot.contenu.collectAsStateWithLifecycle()
    val charge by depot.charge.collectAsStateWithLifecycle()
    val cas = paquet.cas.find { it.id == casId }
    var tentativeId by rememberSaveable(casId) { mutableStateOf<Long?>(null) }
    LaunchedEffect(cas?.id, tentativeId) {
        if (cas != null && tentativeId == null) tentativeId = depot.tentativeEntrainement(cas)
    }
    val id = tentativeId

    Page("Cas $casId", retour) { marges ->
        when {
            !charge -> Chargement(marges)
            cas == null -> Message("Ce cas ne figure plus dans le contenu chargé.", marges)
            id == null -> Chargement(marges)
            else -> DeroulementCas(cas, id, paquet.normes, marges, onRecommencer = { tentativeId = it })
        }
    }
}

@Composable
private fun DeroulementCas(cas: Cas, tentativeId: Long, normes: Map<String, Norme>, marges: PaddingValues, onRecommencer: (Long) -> Unit) {
    val depot = LocalDepot.current
    val fluxTentative = remember(tentativeId) { depot.acces.tentative(tentativeId) }
    val fluxReponses = remember(tentativeId) { depot.acces.reponses(tentativeId) }
    val tentative by fluxTentative.collectAsStateWithLifecycle(initialValue = null)
    val reponses by fluxReponses.collectAsStateWithLifecycle(initialValue = null)
    val t = tentative
    val r = reponses
    if (t == null || r == null) {
        Chargement(marges)
    } else {
        ContenuCas(cas, t, r.associateBy { it.question }, normes, marges, onRecommencer)
    }
}

@Composable
private fun ContenuCas(
    cas: Cas,
    tentative: Tentative,
    reponses: Map<Int, Reponse>,
    normes: Map<String, Norme>,
    marges: PaddingValues,
    onRecommencer: (Long) -> Unit,
) {
    val depot = LocalDepot.current
    val etapeDe = remember(cas) { cas.etapes.flatMapIndexed { i, e -> e.questions.map { it.n to i } }.toMap() }
    val nb = cas.questions.size
    val courante = tentative.questionCourante
    val derniereEtape = if (courante > nb) cas.etapes.lastIndex else etapeDe[courante] ?: 0
    var aSignaler by remember { mutableStateOf<Question?>(null) }
    val liste = rememberLazyListState()

    // Index des éléments de la liste, pour amener à l'écran la question en cours (ou le début de sa nouvelle étape).
    fun index(cible: String): Int {
        var i = 0
        cas.etapes.forEachIndexed { e, etape ->
            if (e > derniereEtape) return i
            if (cible == "etape-$e") return i
            i++
            for (q in etape.questions) {
                if (q.n > courante) break
                if (cible == "q-${q.n}") return i
                i++
            }
        }
        return i
    }
    LaunchedEffect(courante) {
        val cible = when {
            courante > nb -> "resultat"
            cas.etapes[etapeDe[courante] ?: 0].questions.firstOrNull()?.n == courante && courante > 1 -> "etape-${etapeDe[courante]}"
            else -> "q-$courante"
        }
        if (courante > 1) liste.animateScrollToItem(index(cible))
    }

    LazyColumn(Modifier.fillMaxSize(), state = liste, contentPadding = marges) {
        cas.etapes.forEachIndexed { e, etape ->
            if (e <= derniereEtape) {
                item(key = "etape-$e") { BlocEtape(e + 1, etape, cas.patient, normes) }
                items(etape.questions.filter { it.n <= courante }, key = { "q-${it.n}" }) { q ->
                    QuestionEntrainement(cas, tentative, q, reponses[q.n], q.n == courante, q.n == nb) { aSignaler = q }
                }
            }
        }
        if (courante > nb) {
            item(key = "resultat") { Resultat(cas, tentative, onRecommencer) }
        }
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
private fun QuestionEntrainement(
    cas: Cas,
    tentative: Tentative,
    question: Question,
    reponse: Reponse?,
    estCourante: Boolean,
    estDerniere: Boolean,
    onSignaler: () -> Unit,
) {
    val depot = LocalDepot.current
    val corrigeVu = reponse?.corrigeVu == true
    val modifiable = estCourante && !corrigeVu
    var texte by remember(tentative.id, question.n) { mutableStateOf(reponse?.texte.orEmpty()) }
    var coches by remember(tentative.id, question.n) { mutableStateOf(reponse?.indicesCoches.orEmpty()) }

    if (modifiable) {
        // Sauvegarde après une pause de frappe : la réponse survit à une fermeture de l'application.
        LaunchedEffect(texte) {
            delay(600)
            if (texte.isNotEmpty() || reponse != null) depot.enregistrerTexte(tentative.id, question.n, texte)
        }
    }

    CarteQuestion(
        question = question,
        texte = texte,
        onTexte = { v: String -> texte = v }.takeIf { modifiable },
        afficherCorrige = corrigeVu,
        coches = coches,
        onCoche = { i: Int, coche: Boolean ->
            coches = if (coche) coches + i else coches - i
            depot.enregistrerCoches(tentative.id, question.n, coches)
        }.takeIf { tentative.fin == null },
        onSignaler = onSignaler,
    ) {
        if (estCourante) {
            when {
                !corrigeVu -> Button(onClick = { depot.voirCorrige(tentative.id, question.n, texte) }, modifier = Modifier.fillMaxWidth()) {
                    Text("Voir le corrigé")
                }
                estDerniere -> Button(onClick = { depot.terminer(tentative, cas) }, modifier = Modifier.fillMaxWidth()) {
                    Text("Voir ma note")
                }
                else -> Button(onClick = { depot.passerA(tentative, question.n + 1) }, modifier = Modifier.fillMaxWidth()) {
                    Text("Question suivante")
                }
            }
        }
    }
}

@Composable
private fun Resultat(cas: Cas, tentative: Tentative, onRecommencer: (Long) -> Unit) {
    val depot = LocalDepot.current
    val scope = rememberCoroutineScope()
    val note = tentative.noteSur20
    CarteResultat {
        if (note == null) {
            Text("Calcul de la note…")
        } else {
            Text("Note : ${formaterNombre(note)} / 20", style = MaterialTheme.typography.headlineSmall)
            Text("Diagnostic : ${cas.maladiePrincipale}", style = MaterialTheme.typography.titleMedium)
            if (cas.liensTransversaux.isNotEmpty()) {
                Text("Liens transversaux : " + cas.liensTransversaux.joinToString(" ; "), style = MaterialTheme.typography.bodyMedium)
            }
            Text("À retenir", style = MaterialTheme.typography.titleSmall)
            Puces(cas.synthese)
            Column(Modifier.padding(top = 8.dp)) {
                OutlinedButton(onClick = { scope.launch { onRecommencer(depot.recommencer(cas)) } }, modifier = Modifier.fillMaxWidth()) {
                    Text("Recommencer ce cas")
                }
            }
        }
    }
}

package dz.meli.cascliniques.core

import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonPrimitive
import kotlin.math.abs

/** Contenu lu dans un fichier importé, avec les cas refusés et la raison du refus. */
data class ResultatImport(val paquet: Paquet, val refuses: List<Refus>) {
    val resume: String
        get() = buildString {
            append("${paquet.cas.size} cas, ${paquet.qroc.size} QROC")
            if (refuses.isNotEmpty()) append(", ${refuses.size} refusé(s)")
        }
}

data class Refus(val id: String, val problemes: List<String>)

class FormatInconnu(message: String) : Exception(message)

/** Lecture du contenu JSON : paquet embarqué, ou fichier importé par l'étudiant. */
object Lecteur {

    val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        isLenient = true
    }

    fun lirePaquet(texte: String): Paquet = json.decodeFromString(Paquet.serializer(), texte)

    /**
     * Accepte un paquet complet, un cas seul, une liste de cas ou une liste de QROC.
     * Les cas mal formés (barème faux, numérotation, étape vide) sont refusés, pas importés.
     */
    fun importer(texte: String): ResultatImport {
        val racine = try {
            json.parseToJsonElement(texte)
        } catch (e: SerializationException) {
            throw FormatInconnu("Ce fichier n'est pas un JSON valide : ${e.message}")
        }
        val paquet = when {
            racine is JsonObject && "etapes" in racine -> Paquet(cas = listOf(decoder<Cas>(racine)))
            racine is JsonObject && racine["type"]?.jsonPrimitive?.content == "signalements" ->
                throw FormatInconnu("Ce fichier contient des signalements, pas des cas.")
            racine is JsonObject && ("cas" in racine || "qroc" in racine) -> decoder<Paquet>(racine)
            racine is JsonArray && racine.isEmpty() -> Paquet()
            racine is JsonArray && racine.all { it is JsonObject && "etapes" in it } ->
                Paquet(cas = racine.map { decoder<Cas>(it) })
            racine is JsonArray && racine.all { it is JsonObject && "enonce" in it && "reponse" in it } ->
                Paquet(qroc = racine.map { decoder<Qroc>(it) })
            else -> throw FormatInconnu("Format non reconnu : ni paquet, ni cas, ni liste de QROC.")
        }
        val (valides, refuses) = paquet.cas.partition { it.problemes().isEmpty() }
        return ResultatImport(paquet.copy(cas = valides), refuses.map { Refus(it.id, it.problemes()) })
    }

    private inline fun <reified T> decoder(element: JsonElement): T = try {
        json.decodeFromJsonElement(element)
    } catch (e: IllegalArgumentException) {
        throw FormatInconnu("Contenu incomplet ou mal formé : ${e.message}")
    }
}

/** Contrôles de structure repris de `generation/valider.py` (ceux qui empêcheraient l'application de fonctionner). */
fun Cas.problemes(): List<String> {
    val problemes = mutableListOf<String>()
    if (id.isBlank()) problemes += "identifiant vide"
    if (etapes.isEmpty()) problemes += "aucune étape"
    val numeros = questions.map { it.n }
    if (numeros != (1..questions.size).toList()) problemes += "numérotation des questions incorrecte : $numeros"
    for (q in questions) {
        if (q.elementsAttendus.isEmpty()) problemes += "Q${q.n} : aucun élément attendu"
        val somme = q.elementsAttendus.sumOf { it.points }
        if (abs(somme - q.points) > 0.01) problemes += "Q${q.n} : éléments = $somme pt, question = ${q.points} pt"
    }
    val total = questions.sumOf { it.points }
    if (abs(total - pointsTotal) > 0.01) problemes += "barème : somme des questions = $total, points_total = $pointsTotal"
    return problemes
}

/**
 * Fusionne un contenu importé dans le contenu existant. Pour un même identifiant, la version la plus
 * récente l'emporte (à version égale, le contenu importé remplace l'existant : c'est une correction).
 */
fun Paquet.fusionner(autre: Paquet): Paquet {
    val cas = LinkedHashMap<String, Cas>()
    for (c in this.cas) cas[c.id] = c
    for (c in autre.cas) {
        val existant = cas[c.id]
        if (existant == null || c.version >= existant.version) cas[c.id] = c
    }
    val qroc = LinkedHashMap<String, Qroc>()
    for (q in this.qroc + autre.qroc) qroc[q.id] = q
    return copy(
        version = maxOf(version, autre.version),
        normes = normes + autre.normes,
        cas = cas.values.toList(),
        qroc = qroc.values.toList(),
    )
}

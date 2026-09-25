package dz.meli.cascliniques.core

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.doubleOrNull

/**
 * Modèles du contenu, calqués sur le format JSON décrit dans `generation/CONSIGNES.md` §6.
 * Les champs absents des anciens fichiers ont une valeur par défaut, pour que l'import reste tolérant.
 */

/** Ensemble du contenu embarqué ou importé (produit par `generation/paquet.py`). */
@Serializable
data class Paquet(
    val type: String = "paquet",
    val version: Long = 0,
    @SerialName("genere_le") val genereLe: String? = null,
    val normes: Map<String, Norme> = emptyMap(),
    val cas: List<Cas> = emptyList(),
    val qroc: List<Qroc> = emptyList(),
)

@Serializable
data class Cas(
    val id: String,
    val version: Int = 1,
    val statut: String = "genere",
    @SerialName("maladie_principale") val maladiePrincipale: String,
    val chapitres: List<String> = emptyList(),
    @SerialName("liens_transversaux") val liensTransversaux: List<String> = emptyList(),
    val difficulte: Int? = null,
    @SerialName("duree_minutes") val dureeMinutes: Int? = null,
    @SerialName("points_total") val pointsTotal: Double = 20.0,
    val patient: Patient = Patient(),
    val etapes: List<Etape> = emptyList(),
    val synthese: List<String> = emptyList(),
) {
    val questions: List<Question> get() = etapes.flatMap { it.questions }

    /** Chapitre de la maladie principale (le premier de la liste, par convention). */
    val chapitrePrincipal: String? get() = chapitres.firstOrNull()

    val origine: Origine
        get() = when {
            id.startsWith("EXA") -> Origine.ANNALES
            id.startsWith("PIL") -> Origine.PILOTE
            else -> Origine.LOT
        }
}

enum class Origine(val libelle: String) {
    ANNALES("Annales"),
    PILOTE("Pilote"),
    LOT("Généré"),
}

@Serializable
data class Patient(
    val sexe: String? = null,
    val age: Int? = null,
)

@Serializable
data class Etape(
    val texte: String,
    /** Âge du patient à cette étape, quand elle se passe « N ans plus tard ». */
    val age: Int? = null,
    val examens: List<Examen> = emptyList(),
    val questions: List<Question> = emptyList(),
)

@Serializable
data class Examen(
    val groupe: String = "",
    val parametre: String,
    /** Clé dans les normes (`generation/normes.json`), ou null si le paramètre n'y figure pas. */
    val cle: String? = null,
    /** Nombre ou texte (« négatifs », « positifs à 1/1 280 »…). */
    val valeur: JsonPrimitive = JsonPrimitive(""),
    val unite: String = "",
) {
    val valeurNumerique: Double? get() = if (valeur.isString) null else valeur.doubleOrNull
}

@Serializable
data class Question(
    val n: Int,
    val enonce: String,
    val competence: String = "",
    val points: Double,
    @SerialName("elements_attendus") val elementsAttendus: List<Element> = emptyList(),
    @SerialName("signes_negatifs") val signesNegatifs: List<String> = emptyList(),
    val piege: String = "",
    @SerialName("a_verifier") val aVerifier: Boolean = false,
)

@Serializable
data class Element(
    val texte: String,
    val points: Double,
    val justification: String = "",
    val sources: List<String> = emptyList(),
)

@Serializable
data class Qroc(
    val id: String,
    val chapitre: String,
    val session: String = "",
    val page: Int? = null,
    val enonce: String,
    val reponse: List<String> = emptyList(),
    @SerialName("origine_reponse") val origineReponse: String = "",
    /** Identifiant de la QROC dont celle-ci est une reformulation (même question posée à une autre session). */
    @SerialName("doublon_de") val doublonDe: String? = null,
    val sources: List<String> = emptyList(),
)

@Serializable
data class Norme(
    val libelle: String = "",
    val unite: String = "",
    val min: Double? = null,
    val max: Double? = null,
    @SerialName("min_F") val minF: Double? = null,
    @SerialName("max_F") val maxF: Double? = null,
    @SerialName("min_M") val minM: Double? = null,
    @SerialName("max_M") val maxM: Double? = null,
    /** "age" pour la VS : limite (âge + 10)/2 chez la femme, âge/2 chez l'homme. */
    val regle: String? = null,
    val source: String = "",
)

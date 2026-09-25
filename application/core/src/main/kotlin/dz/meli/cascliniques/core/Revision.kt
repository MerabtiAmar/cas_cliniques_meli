package dz.meli.cascliniques.core

import kotlinx.serialization.Serializable
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * Une carte de révision : une QROC et toutes ses reformulations posées à d'autres sessions.
 * Le nombre d'occurrences mesure l'importance de la question pour les enseignants.
 */
data class CarteQroc(val qroc: Qroc, val occurrences: List<Qroc>) {
    val id: String get() = qroc.id
    val sessions: List<String> get() = occurrences.map { it.session }.filter { it.isNotBlank() }.distinct()
}

/** Regroupe les QROC doublons sous leur question d'origine (en suivant les chaînes de `doublon_de`). */
fun regrouperDoublons(qrocs: List<Qroc>): List<CarteQroc> {
    val parId = qrocs.associateBy { it.id }
    fun origine(q: Qroc): Qroc {
        var courant = q
        val vus = mutableSetOf(q.id)
        while (true) {
            val parent = courant.doublonDe?.let(parId::get) ?: return courant
            if (!vus.add(parent.id)) return courant
            courant = parent
        }
    }
    return qrocs.groupBy { origine(it).id }
        .map { (id, groupe) -> CarteQroc(parId.getValue(id), groupe) }
}

/** Auto-évaluation après avoir vu la réponse. */
enum class Note(val libelle: String) {
    A_REVOIR("À revoir"),
    DIFFICILE("Difficile"),
    BIEN("Bien"),
    FACILE("Facile"),
}

/** État de révision d'une carte. Les dates sont des jours depuis le 1er janvier 1970 (`LocalDate.toEpochDay()`). */
@Serializable
data class EtatRevision(
    val id: String,
    val repetitions: Int = 0,
    val intervalle: Int = 0,
    val facilite: Double = 2.5,
    val echeance: Long,
    val derniereRevision: Long? = null,
    val echecs: Int = 0,
)

/**
 * Répétition espacée (variante de SM-2 à quatre boutons).
 * @param intervalleMax plafond de l'intervalle, en jours.
 * @param dateExamen si elle est connue, aucune révision n'est programmée après la veille de l'examen :
 *   chaque carte est revue au moins une fois dans les derniers jours.
 */
class Planificateur(private val intervalleMax: Int = 30, private val dateExamen: Long? = null) {

    fun reviser(etat: EtatRevision?, id: String, note: Note, aujourdhui: Long): EtatRevision {
        val precedent = etat ?: EtatRevision(id = id, echeance = aujourdhui)
        var facilite = precedent.facilite
        var repetitions = precedent.repetitions
        var echecs = precedent.echecs
        val intervalle: Int = when (note) {
            Note.A_REVOIR -> {
                facilite -= 0.2
                repetitions = 0
                echecs += 1
                0 // à revoir dans la même séance
            }
            Note.DIFFICILE -> {
                facilite -= 0.15
                repetitions += 1
                max(1, (precedent.intervalle * 1.2).roundToInt())
            }
            Note.BIEN -> {
                repetitions += 1
                when (repetitions) {
                    1 -> 1
                    2 -> 3
                    else -> max(precedent.intervalle + 1, (precedent.intervalle * facilite).roundToInt())
                }
            }
            Note.FACILE -> {
                facilite += 0.15
                repetitions += 1
                when (repetitions) {
                    1 -> 3
                    2 -> 7
                    else -> max(precedent.intervalle + 1, (precedent.intervalle * facilite * 1.3).roundToInt())
                }
            }
        }
        facilite = facilite.coerceIn(1.3, 3.0)
        var borne = min(intervalle, intervalleMax)
        if (dateExamen != null && borne > 0) {
            val joursAvantVeille = (dateExamen - 1 - aujourdhui).toInt()
            if (joursAvantVeille >= 1) borne = min(borne, joursAvantVeille)
        }
        return precedent.copy(
            repetitions = repetitions,
            intervalle = borne,
            facilite = facilite,
            echeance = aujourdhui + borne,
            derniereRevision = aujourdhui,
            echecs = echecs,
        )
    }
}

/**
 * Cartes à réviser aujourd'hui : d'abord celles qui sont dues (les plus en retard en premier),
 * puis des cartes nouvelles, les plus souvent posées à l'examen en premier.
 */
fun fileDuJour(
    cartes: List<CarteQroc>,
    etats: Map<String, EtatRevision>,
    aujourdhui: Long,
    nouvellesMax: Int,
): List<CarteQroc> {
    val dues = cartes
        .filter { etats[it.id]?.let { e -> e.echeance <= aujourdhui } == true }
        .sortedBy { etats.getValue(it.id).echeance }
    val nouvelles = cartes
        .filter { it.id !in etats }
        .sortedByDescending { it.occurrences.size }
        .take(nouvellesMax)
    return dues + nouvelles
}

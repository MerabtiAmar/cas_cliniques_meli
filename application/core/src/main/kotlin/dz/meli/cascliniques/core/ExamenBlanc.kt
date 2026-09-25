package dz.meli.cascliniques.core

import kotlin.random.Random

/** Sujet d'examen blanc : un cas clinique et une série de QROC, en temps limité. */
data class SujetExamen(val cas: Cas, val qroc: List<CarteQroc>, val dureeMinutes: Int)

/** Temps accordé par QROC dans le calcul de la durée de l'épreuve. */
const val MINUTES_PAR_QROC = 2

/**
 * Compose un examen blanc.
 * - Le cas est tiré parmi ceux que l'étudiant n'a pas encore faits (tous, s'il les a tous faits),
 *   éventuellement restreint à un thème.
 * - Les QROC sont tirées en couvrant le plus de chapitres possible, en favorisant les questions
 *   posées plusieurs fois, et en évitant le chapitre du cas quand c'est possible.
 * @return null s'il n'y a aucun cas disponible.
 */
fun composerExamen(
    cas: List<Cas>,
    cartes: List<CarteQroc>,
    casDejaFaits: Set<String>,
    nbQroc: Int,
    graine: Long,
    theme: String? = null,
): SujetExamen? {
    val hasard = Random(graine)
    val candidats = cas.filter { theme == null || Libelles.theme(it.chapitrePrincipal) == theme }
    val nonFaits = candidats.filter { it.id !in casDejaFaits }
    val choisi = (nonFaits.ifEmpty { candidats }).randomOrNull(hasard) ?: return null

    val parChapitre = cartes
        .groupBy { it.qroc.chapitre }
        .mapValues { (_, groupe) -> tirageDegressif(groupe, hasard) }
        .toMutableMap()
    // Les chapitres sont parcourus dans un ordre aléatoire, celui du cas en dernier.
    val ordre = parChapitre.keys.shuffled(hasard).sortedBy { it == choisi.chapitrePrincipal }
    val qroc = mutableListOf<CarteQroc>()
    while (qroc.size < nbQroc && parChapitre.values.any { it.isNotEmpty() }) {
        for (chapitre in ordre) {
            if (qroc.size >= nbQroc) break
            val file = parChapitre.getValue(chapitre)
            if (file.isNotEmpty()) qroc += file.removeAt(0)
        }
    }
    val duree = (choisi.dureeMinutes ?: 25) + MINUTES_PAR_QROC * qroc.size
    return SujetExamen(choisi, qroc, duree)
}

/** Ordre aléatoire pondéré par le nombre d'occurrences : une question posée 3 fois sort plus souvent en tête. */
private fun tirageDegressif(cartes: List<CarteQroc>, hasard: Random): MutableList<CarteQroc> =
    cartes.map { it to hasard.nextDouble().let { u -> Math.pow(u, 1.0 / it.occurrences.size) } }
        .sortedByDescending { it.second }
        .map { it.first }
        .toMutableList()

package dz.meli.cascliniques.core

import kotlin.math.roundToInt

/**
 * Auto-évaluation : pour chaque question, l'étudiant coche les éléments de la grille qu'il a cités.
 * La clé de la carte est le numéro de la question, la valeur l'ensemble des indices d'éléments cochés.
 */
typealias Grille = Map<Int, Set<Int>>

fun Question.pointsObtenus(coches: Set<Int>): Double =
    elementsAttendus.withIndex().filter { it.index in coches }.sumOf { it.value.points }

fun Cas.pointsObtenus(grille: Grille): Double =
    questions.sumOf { q -> q.pointsObtenus(grille[q.n].orEmpty()) }

/** Note ramenée sur 20 (tous les cas actuels sont notés sur 20, mais le format ne l'impose pas). */
fun Cas.noteSur20(grille: Grille): Double =
    if (pointsTotal <= 0.0) 0.0 else arrondirAuQuart(pointsObtenus(grille) * 20.0 / pointsTotal)

fun arrondirAuQuart(x: Double): Double = (x * 4).roundToInt() / 4.0

/** Réussite par compétence sur un ensemble de cas corrigés : points obtenus / points possibles. */
data class BilanCompetence(val competence: String, val obtenus: Double, val possibles: Double) {
    val taux: Double get() = if (possibles == 0.0) 0.0 else obtenus / possibles
}

fun bilanParCompetence(corrections: List<Pair<Cas, Grille>>): List<BilanCompetence> {
    val obtenus = mutableMapOf<String, Double>()
    val possibles = mutableMapOf<String, Double>()
    for ((cas, grille) in corrections) {
        for (q in cas.questions) {
            // Seules les questions réellement corrigées comptent (une question sans entrée n'a pas été faite).
            val coches = grille[q.n] ?: continue
            obtenus.merge(q.competence, q.pointsObtenus(coches), Double::plus)
            possibles.merge(q.competence, q.points, Double::plus)
        }
    }
    return possibles.keys
        .map { BilanCompetence(it, obtenus[it] ?: 0.0, possibles.getValue(it)) }
        .sortedBy { it.taux }
}

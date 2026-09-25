package dz.meli.cascliniques.core

import java.util.Locale

/** Position d'un résultat par rapport à sa norme. */
enum class Statut { BAS, NORMAL, HAUT }

/** Résultat de la comparaison d'une valeur à sa norme, avec l'intervalle à afficher. */
data class Evaluation(val statut: Statut, val intervalle: String)

/**
 * Compare une valeur à sa norme, comme `statut_examen` dans `generation/valider.py`.
 * Renvoie null quand la règle de l'âge ne peut pas s'appliquer (âge ou sexe inconnus).
 */
fun Norme.evaluer(valeur: Double, sexe: String?, age: Int?): Evaluation? {
    if (regle == "age") {
        if (age == null || sexe !in setOf("F", "M")) return null
        val limite = if (sexe == "F") (age + 10) / 2.0 else age / 2.0
        return Evaluation(if (valeur > limite) Statut.HAUT else Statut.NORMAL, "< ${formaterNombre(limite)}")
    }
    val bas = when (sexe) {
        "F" -> minF ?: min
        "M" -> minM ?: min
        else -> min
    }
    val haut = when (sexe) {
        "F" -> maxF ?: max
        "M" -> maxM ?: max
        else -> max
    }
    val intervalle = "[${bas?.let(::formaterNombre) ?: ""} – ${haut?.let(::formaterNombre) ?: ""}]"
    val statut = when {
        bas != null && valeur < bas -> Statut.BAS
        haut != null && valeur > haut -> Statut.HAUT
        else -> Statut.NORMAL
    }
    return Evaluation(statut, intervalle)
}

/** Évalue un examen d'une étape donnée (le patient peut avoir vieilli d'une étape à l'autre). */
fun Examen.evaluer(normes: Map<String, Norme>, patient: Patient, etape: Etape): Evaluation? {
    val norme = cle?.let { normes[it] } ?: return null
    val nombre = valeurNumerique ?: return null
    return norme.evaluer(nombre, patient.sexe, etape.age ?: patient.age)
}

/** Nombre à la française : virgule décimale, sans zéros inutiles (10.8 → « 10,8 », 14000.0 → « 14 000 »). */
fun formaterNombre(x: Double): String {
    if (x == Math.rint(x) && kotlin.math.abs(x) < 1e15) {
        val entier = x.toLong()
        // Les milliers ne sont séparés qu'à partir de 10 000, comme dans les annales (« 8200 », « 14 000 »).
        return if (kotlin.math.abs(entier) >= 10_000) String.format(Locale.FRANCE, "%,d", entier).replace(' ', ' ').replace(' ', ' ')
        else entier.toString()
    }
    return x.toBigDecimal().stripTrailingZeros().toPlainString().replace('.', ',')
}

/** Valeur d'un examen telle qu'on l'affiche : nombre formaté ou texte brut. */
fun Examen.valeurAffichee(): String = valeurNumerique?.let(::formaterNombre) ?: valeur.content

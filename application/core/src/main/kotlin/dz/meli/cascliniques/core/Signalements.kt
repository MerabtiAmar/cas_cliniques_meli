package dz.meli.cascliniques.core

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Erreur signalée par l'étudiant depuis l'application. Les signalements sont exportés dans un fichier
 * JSON, à déposer dans `generation/signalements/` pour que les cas concernés soient corrigés.
 */
@Serializable
data class Signalement(
    /** "cas" ou "qroc". */
    val cible: String,
    val id: String,
    /** Version du cas au moment du signalement (le cas a pu être corrigé depuis). */
    val version: Int? = null,
    /** Numéro de la question dans le cas. */
    val question: Int? = null,
    /** Indice de l'élément attendu dans la question (à partir de 0), si le signalement le vise. */
    val element: Int? = null,
    val commentaire: String,
    /** Date et heure ISO 8601. */
    val date: String,
)

@Serializable
data class ExportSignalements(
    val type: String = "signalements",
    @SerialName("exporte_le") val exporteLe: String,
    val signalements: List<Signalement>,
)

fun exporterSignalements(signalements: List<Signalement>, maintenant: String): String =
    Lecteur.json.encodeToString(ExportSignalements.serializer(), ExportSignalements(exporteLe = maintenant, signalements = signalements))

package dz.meli.cascliniques.core

/** Libellés lisibles des chapitres, des compétences et des sources. */
object Libelles {

    /** Clé : chemin relatif à `corpus/`, sans extension (comme dans `Cas.chapitres` et `Qroc.chapitre`). */
    val chapitres: Map<String, String> = linkedMapOf(
        "cours/01_lupus" to "Lupus",
        "cours/02_sapl" to "SAPL",
        "cours/03_behcet" to "Behçet",
        "cours/04_sclerodermie" to "Sclérodermie",
        "cours/05_granulomatoses" to "Granulomatoses",
        "cours/06_gougerot_sjogren" to "Gougerot-Sjögren",
        "cours/07_still_auto_inflammatoires" to "Maladies auto-inflammatoires",
        "cours/08_myopathies" to "Myopathies inflammatoires",
        "cours/09_amyloses" to "Amyloses",
        "cours/10_vascularites" to "Vascularites",
        "cours/11_maladies_rares" to "Maladies rares",
        "cours/12_deficits_immunitaires" to "Déficits immunitaires",
        "cours/13_traitements" to "Principes du traitement",
        "fiches_complementaires/05_sarcoidose" to "Sarcoïdose (fiche)",
        "fiches_complementaires/07_still" to "Still (fiche)",
        "fiches_complementaires/10_horton" to "Horton (fiche)",
        "fiches_complementaires/10_ppr" to "PPR (fiche)",
        "referentiel_transversal" to "Référentiel transversal",
        "examens/cas_cliniques" to "Annales : cas cliniques",
        "examens/qroc_par_cours" to "Annales : QROC",
    )

    /**
     * Regroupement des chapitres de maladie principale en thèmes de l'examen, pour filtrer les cas.
     * Les fiches complémentaires rejoignent le chapitre du cours auquel elles se rattachent.
     */
    fun theme(chapitre: String?): String = when (chapitre) {
        null -> "Autre"
        "fiches_complementaires/05_sarcoidose" -> "Granulomatoses"
        "fiches_complementaires/07_still" -> "Maladies auto-inflammatoires"
        "fiches_complementaires/10_horton", "fiches_complementaires/10_ppr" -> "Vascularites"
        else -> chapitres[chapitre] ?: chapitre
    }

    fun chapitre(chemin: String): String = chapitres[chemin.removeSuffix(".md")] ?: chemin

    val competences: Map<String, String> = mapOf(
        "caracteriser_articulaire" to "Caractériser une atteinte articulaire",
        "interpreter_hemogramme" to "Interpréter un hémogramme",
        "interpreter_bilan_inflammatoire" to "Interpréter le bilan inflammatoire",
        "interpreter_bilan" to "Interpréter un bilan",
        "semiologie" to "Sémiologie",
        "diagnostic_positif" to "Diagnostic positif",
        "diagnostic_differentiel" to "Diagnostic différentiel",
        "diagnostic_etiologique" to "Diagnostic étiologique",
        "examen_complementaire" to "Examen complémentaire",
        "complication" to "Complication",
        "traitement" to "Traitement",
        "surveillance" to "Surveillance",
        "physiopathologie" to "Physiopathologie",
    )

    fun competence(cle: String): String = competences[cle] ?: cle

    /** « cours/01_lupus.md|p.3 » → « Lupus, p.3 » ; « hors_sources|… » → « Hors sources : … ». */
    fun source(source: String): String {
        val chemin = source.substringBefore('|')
        val localisation = source.substringAfter('|', "").trim()
        if (chemin == "hors_sources") return "Hors sources : $localisation"
        val titre = chapitre(chemin)
        return if (localisation.isEmpty()) titre else "$titre, $localisation"
    }
}

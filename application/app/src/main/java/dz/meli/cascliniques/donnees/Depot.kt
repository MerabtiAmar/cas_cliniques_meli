package dz.meli.cascliniques.donnees

import android.content.Context
import androidx.room.Room
import dz.meli.cascliniques.core.CarteQroc
import dz.meli.cascliniques.core.Cas
import dz.meli.cascliniques.core.EtatRevision
import dz.meli.cascliniques.core.Lecteur
import dz.meli.cascliniques.core.Note
import dz.meli.cascliniques.core.Paquet
import dz.meli.cascliniques.core.Planificateur
import dz.meli.cascliniques.core.ResultatImport
import dz.meli.cascliniques.core.Signalement
import dz.meli.cascliniques.core.composerExamen
import dz.meli.cascliniques.core.exporterSignalements
import dz.meli.cascliniques.core.fusionner
import dz.meli.cascliniques.core.noteSur20
import dz.meli.cascliniques.core.regrouperDoublons
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import java.io.File
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Point d'accès unique aux données : le contenu (paquet embarqué + fichiers importés) et la
 * progression de l'étudiant (base Room). Aucune donnée ne quitte le téléphone, sauf l'export
 * des signalements, que l'étudiant déclenche lui-même.
 */
class Depot(private val context: Context, private val scope: CoroutineScope) {

    private val base = Room.databaseBuilder(context, Base::class.java, "cas_cliniques.db").build()
    val acces: Acces = base.acces()

    private val _contenu = MutableStateFlow(Paquet())
    val contenu: StateFlow<Paquet> = _contenu.asStateFlow()

    private val _charge = MutableStateFlow(false)
    val charge: StateFlow<Boolean> = _charge.asStateFlow()

    val cartes: StateFlow<List<CarteQroc>> = contenu
        .map { regrouperDoublons(it.qroc) }
        .stateIn(scope, SharingStarted.Eagerly, emptyList())

    /** États de révision des QROC ; null tant que la base n'a pas répondu. */
    val revisions: StateFlow<Map<String, EtatRevision>?> = acces.revisions()
        .map { liste -> liste.associate { it.id to it.versEtat() } }
        .stateIn(scope, SharingStarted.Eagerly, null)

    // Flux partagés, créés une seule fois (un Flow recréé à chaque recomposition relancerait la collecte).
    val tentatives: StateFlow<List<Tentative>> = acces.tentatives().stateIn(scope, SharingStarted.Eagerly, emptyList())
    val toutesLesReponses: StateFlow<List<Reponse>> = acces.toutesLesReponses().stateIn(scope, SharingStarted.Eagerly, emptyList())
    val signalements: StateFlow<List<SignalementLocal>> = acces.signalements().stateIn(scope, SharingStarted.Eagerly, emptyList())
    val examensBlancs: StateFlow<List<ExamenBlanc>> = acces.examensBlancs().stateIn(scope, SharingStarted.Eagerly, emptyList())

    private val preferences = context.getSharedPreferences("reglages", Context.MODE_PRIVATE)
    private val _dateExamen = MutableStateFlow(preferences.getLong(CLE_DATE_EXAMEN, -1).takeIf { it >= 0 })
    /** Date de l'examen en jours depuis 1970, ou null si l'étudiant ne l'a pas renseignée. */
    val dateExamen: StateFlow<Long?> = _dateExamen.asStateFlow()
    private val _nouvellesParJour = MutableStateFlow(preferences.getInt(CLE_NOUVELLES, 15))
    val nouvellesParJour: StateFlow<Int> = _nouvellesParJour.asStateFlow()

    private val dossierImports = File(context.filesDir, "imports")

    /**
     * Toutes les écritures passent par cette file, exécutée dans l'ordre d'arrivée : deux frappes ou
     * deux cases cochées coup sur coup ne peuvent pas s'écrire dans le désordre.
     */
    private val ecritures = Channel<suspend () -> Unit>(Channel.UNLIMITED)

    private fun ecrire(bloc: suspend () -> Unit) {
        ecritures.trySend(bloc)
    }

    init {
        scope.launch { charger() }
        scope.launch { for (bloc in ecritures) runCatching { bloc() } }
    }

    fun aujourdhui(): Long = LocalDate.now().toEpochDay()

    // ---------------------------------------------------------------- contenu

    /** Recharge le paquet embarqué puis applique, dans l'ordre, les fichiers importés. */
    suspend fun charger() = withContext(Dispatchers.IO) {
        var paquet = try {
            context.assets.open(FICHIER_CONTENU).bufferedReader().use { Lecteur.lirePaquet(it.readText()) }
        } catch (e: java.io.FileNotFoundException) {
            Paquet()
        }
        dossierImports.listFiles().orEmpty().sortedBy { it.name }.forEach { fichier ->
            runCatching { Lecteur.importer(fichier.readText()) }.onSuccess { paquet = paquet.fusionner(it.paquet) }
        }
        _contenu.value = paquet
        _charge.value = true
    }

    /** Importe un fichier choisi par l'étudiant ; il est conservé pour les prochains lancements. */
    suspend fun importer(texte: String): ResultatImport = withContext(Dispatchers.IO) {
        val resultat = Lecteur.importer(texte)
        if (resultat.paquet.cas.isNotEmpty() || resultat.paquet.qroc.isNotEmpty()) {
            dossierImports.mkdirs()
            File(dossierImports, "import-${System.currentTimeMillis()}.json").writeText(texte)
            charger()
        }
        resultat
    }

    val nombreImports: Int get() = dossierImports.listFiles()?.size ?: 0

    suspend fun supprimerImports() = withContext(Dispatchers.IO) {
        dossierImports.deleteRecursively()
        charger()
    }

    fun cas(id: String): Cas? = contenu.value.cas.find { it.id == id }

    // ---------------------------------------------------------------- cas cliniques

    /** Reprend la tentative d'entraînement en cours sur ce cas, ou en commence une. */
    suspend fun tentativeEntrainement(cas: Cas): Long =
        acces.tentativeEnCours(cas.id)?.id
            ?: acces.insererTentative(
                Tentative(casId = cas.id, versionCas = cas.version, mode = Tentative.MODE_ENTRAINEMENT, debut = System.currentTimeMillis()),
            )

    fun enregistrerTexte(tentativeId: Long, question: Int, texte: String) = ecrire {
        acces.creerReponse(tentativeId, question)
        acces.majTexte(tentativeId, question, texte)
    }

    fun enregistrerCoches(tentativeId: Long, question: Int, coches: Set<Int>) = ecrire {
        acces.creerReponse(tentativeId, question)
        acces.majCoches(tentativeId, question, coches.sorted().joinToString(","))
    }

    /** Enregistre la réponse telle qu'elle est au moment où l'étudiant découvre le corrigé. */
    fun voirCorrige(tentativeId: Long, question: Int, texte: String) = ecrire {
        acces.creerReponse(tentativeId, question)
        acces.majTexte(tentativeId, question, texte)
        acces.marquerCorrigeVu(tentativeId, question)
    }

    fun passerA(tentative: Tentative, question: Int) = ecrire {
        acces.majTentative(tentative.copy(questionCourante = question))
    }

    /** Clôt la tentative ; la note est calculée à partir de la grille cochée, une fois les écritures en attente faites. */
    fun terminer(tentative: Tentative, cas: Cas) = ecrire {
        val reponses = acces.reponses(tentative.id).first()
        val note = cas.noteSur20(reponses.associate { it.question to it.indicesCoches })
        acces.majTentative(tentative.copy(questionCourante = cas.questions.size + 1, fin = System.currentTimeMillis(), noteSur20 = note))
    }

    /** Recommencer un cas : une nouvelle tentative, les anciennes restent dans l'historique. */
    suspend fun recommencer(cas: Cas): Long =
        acces.insererTentative(
            Tentative(casId = cas.id, versionCas = cas.version, mode = Tentative.MODE_ENTRAINEMENT, debut = System.currentTimeMillis()),
        )

    // ---------------------------------------------------------------- QROC

    fun planificateur() = Planificateur(dateExamen = dateExamen.value)

    fun noter(carte: CarteQroc, note: Note) {
        val etat = planificateur().reviser(revisions.value?.get(carte.id), carte.id, note, aujourdhui())
        ecrire { acces.enregistrerRevision(etat.versEntite()) }
    }

    // ---------------------------------------------------------------- examen blanc

    /** Compose et enregistre un examen blanc ; renvoie son identifiant, ou null s'il n'y a aucun cas. */
    suspend fun creerExamenBlanc(theme: String?, nbQroc: Int): Long? {
        val faits = tentatives.value.filter { it.fin != null }.map { it.casId }.toSet()
        val sujet = composerExamen(contenu.value.cas, cartes.value, faits, nbQroc, System.currentTimeMillis(), theme)
            ?: return null
        val tentative = acces.insererTentative(
            Tentative(casId = sujet.cas.id, versionCas = sujet.cas.version, mode = Tentative.MODE_EXAMEN, debut = System.currentTimeMillis()),
        )
        return acces.insererExamenBlanc(
            ExamenBlanc(
                tentativeId = tentative,
                qroc = sujet.qroc.joinToString(",") { it.id },
                debut = System.currentTimeMillis(),
                dureeMinutes = sujet.dureeMinutes,
            ),
        )
    }

    fun enregistrer(examen: ExamenBlanc) = ecrire {
        acces.majExamenBlanc(examen)
    }

    // ---------------------------------------------------------------- signalements

    fun signaler(cible: String, id: String, version: Int?, question: Int?, element: Int?, commentaire: String) {
        ecrire {
            acces.insererSignalement(
                SignalementLocal(
                    cible = cible, cibleId = id, version = version, question = question, element = element,
                    commentaire = commentaire.trim(), date = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                ),
            )
        }
    }

    /** Texte JSON des signalements (format `core/Signalements.kt`), à écrire dans le fichier choisi. */
    fun texteExport(signalements: List<SignalementLocal>): String = exporterSignalements(
        signalements.map { Signalement(it.cible, it.cibleId, it.version, it.question, it.element, it.commentaire, it.date) },
        LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
    )

    // ---------------------------------------------------------------- réglages

    fun definirDateExamen(jour: Long?) {
        preferences.edit().putLong(CLE_DATE_EXAMEN, jour ?: -1).apply()
        _dateExamen.value = jour
    }

    fun definirNouvellesParJour(n: Int) {
        preferences.edit().putInt(CLE_NOUVELLES, n).apply()
        _nouvellesParJour.value = n
    }

    companion object {
        const val FICHIER_CONTENU = "contenu.json"
        private const val CLE_DATE_EXAMEN = "date_examen"
        private const val CLE_NOUVELLES = "nouvelles_par_jour"

        private val mapTexte = MapSerializer(String.serializer(), String.serializer())
        private val mapNombre = MapSerializer(String.serializer(), Double.serializer())

        fun lireTextes(json: String): Map<String, String> = runCatching { Lecteur.json.decodeFromString(mapTexte, json) }.getOrDefault(emptyMap())
        fun ecrireTextes(valeurs: Map<String, String>): String = Lecteur.json.encodeToString(mapTexte, valeurs)
        fun lireNombres(json: String): Map<String, Double> = runCatching { Lecteur.json.decodeFromString(mapNombre, json) }.getOrDefault(emptyMap())
        fun ecrireNombres(valeurs: Map<String, Double>): String = Lecteur.json.encodeToString(mapNombre, valeurs)
    }
}

private fun RevisionQroc.versEtat() = EtatRevision(id, repetitions, intervalle, facilite, echeance, derniereRevision, echecs)

private fun EtatRevision.versEntite() = RevisionQroc(id, repetitions, intervalle, facilite, echeance, derniereRevision, echecs)

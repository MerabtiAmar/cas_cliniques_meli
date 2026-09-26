package dz.meli.cascliniques.donnees

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Update
import androidx.room.Upsert
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.flow.Flow

/** Un passage sur un cas, en entraînement ou dans un examen blanc. */
@Entity(tableName = "tentatives")
data class Tentative(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val casId: String,
    val versionCas: Int,
    /** [MODE_ENTRAINEMENT] ou [MODE_EXAMEN]. */
    val mode: String,
    val debut: Long,
    /** Numéro de la question en cours ; au-delà de la dernière, toutes ont été traitées. */
    val questionCourante: Int = 1,
    val fin: Long? = null,
    val noteSur20: Double? = null,
) {
    companion object {
        const val MODE_ENTRAINEMENT = "entrainement"
        const val MODE_EXAMEN = "examen"
    }
}

/** Réponse de l'étudiant à une question, et les éléments de la grille qu'il s'attribue. */
@Entity(tableName = "reponses", primaryKeys = ["tentativeId", "question"])
data class Reponse(
    val tentativeId: Long,
    val question: Int,
    val texte: String = "",
    /** Indices des éléments cochés, séparés par des virgules. */
    val coches: String = "",
    val corrigeVu: Boolean = false,
) {
    val indicesCoches: Set<Int> get() = coches.split(',').mapNotNull { it.toIntOrNull() }.toSet()
}

/** État de répétition espacée d'une carte QROC (voir `core/Revision.kt`). */
@Entity(tableName = "revisions")
data class RevisionQroc(
    @PrimaryKey val id: String,
    val repetitions: Int,
    val intervalle: Int,
    val facilite: Double,
    val echeance: Long,
    val derniereRevision: Long?,
    val echecs: Int,
    val premiereRevision: Long?,
)

@Entity(tableName = "signalements")
data class SignalementLocal(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val cible: String,
    val cibleId: String,
    val version: Int?,
    val question: Int?,
    val element: Int?,
    val commentaire: String,
    val date: String,
    val exporte: Boolean = false,
)

/** Examen blanc : un cas (réponses dans `reponses`, via la tentative) et une série de QROC. */
@Entity(tableName = "examens_blancs")
data class ExamenBlanc(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val tentativeId: Long,
    /** Identifiants des cartes QROC, séparés par des virgules. */
    val qroc: String,
    /** Réponses aux QROC : objet JSON identifiant → texte. */
    val reponsesQroc: String = "{}",
    /** Auto-évaluation des QROC : objet JSON identifiant → 0, 0,5 ou 1. */
    val notesQroc: String = "{}",
    val debut: Long,
    val dureeMinutes: Int,
    /** [PHASE_EPREUVE], [PHASE_CORRECTION] ou [PHASE_TERMINE]. */
    val phase: String = PHASE_EPREUVE,
) {
    val idsQroc: List<String> get() = qroc.split(',').filter { it.isNotBlank() }

    companion object {
        const val PHASE_EPREUVE = "epreuve"
        const val PHASE_CORRECTION = "correction"
        const val PHASE_TERMINE = "termine"
    }
}

@Dao
interface Acces {
    @Query("SELECT * FROM tentatives ORDER BY debut DESC")
    fun tentatives(): Flow<List<Tentative>>

    @Query("SELECT * FROM tentatives WHERE id = :id")
    fun tentative(id: Long): Flow<Tentative?>

    @Query("SELECT * FROM tentatives WHERE casId = :casId AND mode = 'entrainement' AND fin IS NULL ORDER BY debut DESC LIMIT 1")
    suspend fun tentativeEnCours(casId: String): Tentative?

    @Insert
    suspend fun insererTentative(tentative: Tentative): Long

    @Update
    suspend fun majTentative(tentative: Tentative)

    @Query("SELECT * FROM reponses WHERE tentativeId = :tentativeId")
    fun reponses(tentativeId: Long): Flow<List<Reponse>>

    @Query("SELECT * FROM reponses")
    fun toutesLesReponses(): Flow<List<Reponse>>

    @Query("INSERT OR IGNORE INTO reponses (tentativeId, question, texte, coches, corrigeVu) VALUES (:tentativeId, :question, '', '', 0)")
    suspend fun creerReponse(tentativeId: Long, question: Int)

    @Query("UPDATE reponses SET texte = :texte WHERE tentativeId = :tentativeId AND question = :question")
    suspend fun majTexte(tentativeId: Long, question: Int, texte: String)

    @Query("UPDATE reponses SET coches = :coches WHERE tentativeId = :tentativeId AND question = :question")
    suspend fun majCoches(tentativeId: Long, question: Int, coches: String)

    @Query("UPDATE reponses SET corrigeVu = 1 WHERE tentativeId = :tentativeId AND question = :question")
    suspend fun marquerCorrigeVu(tentativeId: Long, question: Int)

    @Query("SELECT * FROM revisions")
    fun revisions(): Flow<List<RevisionQroc>>

    @Upsert
    suspend fun enregistrerRevision(revision: RevisionQroc)

    @Query("SELECT * FROM signalements ORDER BY id DESC")
    fun signalements(): Flow<List<SignalementLocal>>

    @Insert
    suspend fun insererSignalement(signalement: SignalementLocal)

    @Query("DELETE FROM signalements WHERE id = :id")
    suspend fun supprimerSignalement(id: Long)

    @Query("UPDATE signalements SET exporte = 1 WHERE id IN (:ids)")
    suspend fun marquerExportes(ids: List<Long>)

    @Query("SELECT * FROM examens_blancs WHERE id = :id")
    fun examenBlanc(id: Long): Flow<ExamenBlanc?>

    @Query("SELECT * FROM examens_blancs ORDER BY debut DESC")
    fun examensBlancs(): Flow<List<ExamenBlanc>>

    @Insert
    suspend fun insererExamenBlanc(examen: ExamenBlanc): Long

    @Update
    suspend fun majExamenBlanc(examen: ExamenBlanc)
}

@Database(
    entities = [Tentative::class, Reponse::class, RevisionQroc::class, SignalementLocal::class, ExamenBlanc::class],
    version = 2,
    exportSchema = false,
)
abstract class Base : RoomDatabase() {
    abstract fun acces(): Acces
}

/** Version 2 : date de première révision des QROC, pour le quota de cartes nouvelles par jour. */
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE revisions ADD COLUMN premiereRevision INTEGER")
    }
}

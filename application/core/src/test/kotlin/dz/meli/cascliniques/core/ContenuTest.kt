package dz.meli.cascliniques.core

import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.jsonObject
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** Vérifie que tout le contenu réel du dépôt se lit avec les modèles de l'application. */
class ContenuTest {

    private val generation = File(System.getProperty("racine.projet") ?: "../..", "generation")

    private fun fichiersDeCas(): List<File> =
        generation.walkTopDown()
            .filter { it.isFile && it.extension == "json" && it.parentFile.name != "annales" }
            .filter { it.name.matches(Regex("(EXA|PIL|L\\d+)-\\d+\\.json")) }
            .sortedBy { it.path }
            .toList()

    @Test
    fun `tous les cas du dépôt se lisent et passent les contrôles de structure`() {
        val fichiers = fichiersDeCas()
        assertTrue(fichiers.size >= 14, "au moins les 9 cas d'annales et les 5 cas pilotes : ${fichiers.size}")
        for (f in fichiers) {
            val resultat = Lecteur.importer(f.readText())
            assertEquals(emptyList(), resultat.refuses, f.name)
            val cas = resultat.paquet.cas.single()
            assertEquals(f.nameWithoutExtension, cas.id)
            assertTrue(cas.questions.size in 8..11, "${cas.id} : ${cas.questions.size} questions")
        }
    }

    @Test
    fun `les QROC se lisent et les doublons se regroupent`() {
        val resultat = Lecteur.importer(File(generation, "annales/qroc.json").readText())
        val qroc = resultat.paquet.qroc
        assertEquals(190, qroc.size)
        val cartes = regrouperDoublons(qroc)
        assertEquals(qroc.count { it.doublonDe == null }, cartes.size)
        assertEquals(qroc.size, cartes.sumOf { it.occurrences.size })
    }

    @Test
    fun `les normes du dépôt se lisent`() {
        val racine = Lecteur.json.parseToJsonElement(File(generation, "normes.json").readText()).jsonObject
        val normes = Lecteur.json.decodeFromJsonElement(MapSerializer(String.serializer(), Norme.serializer()), racine.getValue("parametres"))
        assertEquals("g/dL", normes.getValue("hemoglobine").unite)
        assertEquals("age", normes.getValue("vs").regle)
    }
}

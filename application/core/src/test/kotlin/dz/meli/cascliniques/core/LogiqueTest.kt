package dz.meli.cascliniques.core

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class NormesTest {

    private val hemoglobine = Norme(unite = "g/dL", minF = 12.0, maxF = 16.0, minM = 13.0, maxM = 17.0)
    private val vs = Norme(unite = "mm", regle = "age")

    @Test
    fun `les bornes dépendent du sexe`() {
        assertEquals(Statut.NORMAL, hemoglobine.evaluer(12.5, "F", 30)!!.statut)
        assertEquals(Statut.BAS, hemoglobine.evaluer(12.5, "M", 30)!!.statut)
        assertEquals("[12 – 16]", hemoglobine.evaluer(12.5, "F", 30)!!.intervalle)
    }

    @Test
    fun `la VS suit la règle de l'âge comme valider py`() {
        // Femme de 31 ans : limite (31 + 10) / 2 = 20,5 (rapport de valider.py sur L1-09).
        assertEquals(Evaluation(Statut.HAUT, "< 20,5"), vs.evaluer(46.0, "F", 31))
        assertEquals(Statut.NORMAL, vs.evaluer(14.0, "M", 30)!!.statut)
        assertNull(vs.evaluer(14.0, null, 30))
    }

    @Test
    fun `l'âge de l'étape prime sur celui du début du cas`() {
        val examen = Examen(parametre = "VS", cle = "vs", valeur = kotlinx.serialization.json.JsonPrimitive(16))
        val patient = Patient("M", 27)
        assertEquals(Statut.HAUT, examen.evaluer(mapOf("vs" to vs), patient, Etape("…"))!!.statut)
        assertEquals(Statut.NORMAL, examen.evaluer(mapOf("vs" to vs), patient, Etape("…", age = 33))!!.statut)
    }

    @Test
    fun `nombres à la française`() {
        assertEquals("10,8", formaterNombre(10.8))
        assertEquals("8200", formaterNombre(8200.0))
        assertEquals("14 000", formaterNombre(14000.0))
        assertEquals("0,86", formaterNombre(0.86))
    }
}

class NotationTest {

    private val cas = Cas(
        id = "T-1", maladiePrincipale = "Test", pointsTotal = 20.0,
        etapes = listOf(
            Etape(
                "…",
                questions = listOf(
                    Question(1, "Q1", "diagnostic_positif", 12.0, listOf(Element("a", 8.0), Element("b", 4.0))),
                    Question(2, "Q2", "traitement", 8.0, listOf(Element("c", 5.0), Element("d", 3.0))),
                ),
            ),
        ),
    )

    @Test
    fun `note sur 20 à partir des éléments cochés`() {
        assertEquals(0.0, cas.noteSur20(emptyMap()))
        assertEquals(11.0, cas.noteSur20(mapOf(1 to setOf(0), 2 to setOf(1))))
        assertEquals(20.0, cas.noteSur20(mapOf(1 to setOf(0, 1), 2 to setOf(0, 1))))
    }

    @Test
    fun `bilan par compétence, les moins réussies en premier`() {
        val bilan = bilanParCompetence(listOf(cas to mapOf(1 to setOf(0, 1), 2 to setOf(1))))
        assertEquals(listOf("traitement", "diagnostic_positif"), bilan.map { it.competence })
        assertEquals(3.0 / 8.0, bilan.first().taux)
    }

    @Test
    fun `un cas au barème faux est refusé à l'import`() {
        val faux = cas.copy(pointsTotal = 18.0)
        assertTrue(faux.problemes().any { it.startsWith("barème") })
        assertEquals(emptyList(), cas.problemes())
    }
}

class RevisionTest {

    private val jour = 20_000L

    @Test
    fun `intervalles croissants quand la réponse est bien sue`() {
        val p = Planificateur(intervalleMax = 60)
        var etat = p.reviser(null, "Q", Note.BIEN, jour)
        assertEquals(1, etat.intervalle)
        etat = p.reviser(etat, "Q", Note.BIEN, etat.echeance)
        assertEquals(3, etat.intervalle)
        etat = p.reviser(etat, "Q", Note.BIEN, etat.echeance)
        assertEquals(8, etat.intervalle) // 3 × 2,5 = 7,5 → 8
    }

    @Test
    fun `un oubli remet la carte à zéro et baisse la facilité`() {
        val p = Planificateur()
        val etat = p.reviser(EtatRevision("Q", repetitions = 4, intervalle = 20, echeance = jour), "Q", Note.A_REVOIR, jour)
        assertEquals(0, etat.intervalle)
        assertEquals(jour, etat.echeance)
        assertEquals(0, etat.repetitions)
        assertEquals(2.3, etat.facilite, 1e-9)
        assertEquals(1, etat.echecs)
    }

    @Test
    fun `aucune révision programmée après la veille de l'examen`() {
        val p = Planificateur(intervalleMax = 30, dateExamen = jour + 5)
        val etat = p.reviser(EtatRevision("Q", repetitions = 3, intervalle = 10, echeance = jour), "Q", Note.FACILE, jour)
        assertEquals(4, etat.intervalle)
    }

    @Test
    fun `la file du jour met les cartes dues avant les nouvelles, les plus posées d'abord`() {
        fun carte(id: String, n: Int) = CarteQroc(Qroc(id, "c", enonce = id), List(n) { Qroc(id, "c", enonce = id) })
        val cartes = listOf(carte("A", 1), carte("B", 3), carte("C", 1), carte("D", 2))
        val etats = mapOf("C" to EtatRevision("C", echeance = jour - 2), "A" to EtatRevision("A", echeance = jour + 3))
        assertEquals(listOf("C", "B", "D"), fileDuJour(cartes, etats, jour, nouvellesMax = 2).map { it.id })
    }
}

class ImportTest {

    private val casJson = """
        {"id": "L9-01", "version": 2, "maladie_principale": "Test", "chapitres": ["cours/01_lupus"],
         "points_total": 2, "patient": {"sexe": "F", "age": 30},
         "etapes": [{"texte": "…", "examens": [{"groupe": "Hémogramme", "parametre": "Hb", "cle": "hemoglobine", "valeur": 10.8, "unite": "g/dL"},
                                              {"groupe": "Immunologie", "parametre": "FAN", "cle": null, "valeur": "négatifs", "unite": ""}],
                     "questions": [{"n": 1, "enonce": "?", "competence": "semiologie", "points": 2,
                                    "elements_attendus": [{"texte": "x", "points": 2, "justification": "", "sources": []}]}]}],
         "synthese": [], "champ_futur": true}
    """.trimIndent()

    @Test
    fun `un cas seul, avec valeurs numériques et textuelles`() {
        val resultat = Lecteur.importer(casJson)
        val cas = resultat.paquet.cas.single()
        assertEquals(10.8, cas.etapes[0].examens[0].valeurNumerique)
        assertNull(cas.etapes[0].examens[1].valeurNumerique)
        assertEquals("négatifs", cas.etapes[0].examens[1].valeurAffichee())
    }

    @Test
    fun `une liste de cas et un paquet sont reconnus`() {
        assertEquals(2, Lecteur.importer("[$casJson, ${casJson.replace("L9-01", "L9-02")}]").paquet.cas.size)
        assertEquals(1, Lecteur.importer("""{"type": "paquet", "cas": [$casJson]}""").paquet.cas.size)
    }

    @Test
    fun `les fichiers qui ne sont pas du contenu sont refusés avec un message`() {
        assertFailsWith<FormatInconnu> { Lecteur.importer("""{"type": "signalements", "signalements": []}""") }
        assertFailsWith<FormatInconnu> { Lecteur.importer("pas du json") }
        assertFailsWith<FormatInconnu> { Lecteur.importer("""{"id": "X", "etapes": "oups"}""") }
    }

    @Test
    fun `la fusion garde la version la plus récente`() {
        val v2 = Lecteur.importer(casJson).paquet
        val v1 = Paquet(cas = v2.cas.map { it.copy(version = 1, maladiePrincipale = "Ancienne") })
        assertEquals("Test", v1.fusionner(v2).cas.single().maladiePrincipale)
        assertEquals("Test", v2.fusionner(v1).cas.single().maladiePrincipale)
    }

    @Test
    fun `export des signalements relisible`() {
        val texte = exporterSignalements(listOf(Signalement("cas", "L1-01", 1, 3, 0, "seuil faux", "2026-09-25T10:00")), "2026-09-25T10:05")
        assertTrue("\"exporte_le\"" in texte)
        assertFailsWith<FormatInconnu> { Lecteur.importer(texte) }
    }
}

class ExamenBlancTest {

    private fun cas(id: String, chapitre: String) = Cas(id = id, maladiePrincipale = id, chapitres = listOf(chapitre), dureeMinutes = 25)
    private fun carte(id: String, chapitre: String) = CarteQroc(Qroc(id, chapitre, enonce = id), listOf(Qroc(id, chapitre, enonce = id)))

    private val tousLesCas = listOf(cas("A", "cours/01_lupus"), cas("B", "cours/03_behcet"))
    private val cartes = listOf(
        carte("L1", "cours/01_lupus"), carte("L2", "cours/01_lupus"),
        carte("B1", "cours/03_behcet"), carte("S1", "cours/04_sclerodermie"),
    )

    @Test
    fun `le cas tiré n'a pas encore été fait et les QROC couvrent plusieurs chapitres`() {
        val sujet = assertNotNull(composerExamen(tousLesCas, cartes, casDejaFaits = setOf("A"), nbQroc = 3, graine = 1))
        assertEquals("B", sujet.cas.id)
        assertEquals(3, sujet.qroc.map { it.qroc.chapitre }.distinct().size)
        assertEquals(25 + 3 * MINUTES_PAR_QROC, sujet.dureeMinutes)
    }

    @Test
    fun `même graine, même sujet, et thème respecté`() {
        val a = composerExamen(tousLesCas, cartes, emptySet(), 4, graine = 42)
        val b = composerExamen(tousLesCas, cartes, emptySet(), 4, graine = 42)
        assertEquals(a, b)
        assertEquals("A", composerExamen(tousLesCas, cartes, emptySet(), 2, graine = 7, theme = "Lupus")!!.cas.id)
        assertNull(composerExamen(tousLesCas, cartes, emptySet(), 2, graine = 7, theme = "Amyloses"))
    }
}

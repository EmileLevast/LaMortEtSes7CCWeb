import androidx.compose.ui.graphics.Color
import kotlinx.serialization.Serializable

@Serializable
class Arme(
    override val nom: String = "inconnu",
    val seuils: List<Seuil> = mutableListOf(),
    val coupCritiques: String = "",
    val maximumEnergie: Int = 0,
    val contraintes: String = "Aucune",
    val fajMax: Int = 0,
    val poids: Int = 0,
    val capaciteSpeciale: String = "",
    override val nomComplet:String = ""
) : ApiableItem() {


    override val _id = nom.hashCode()
    override var isAttached = false
    override val color: Color
        get() = Color(0xFF7F7F7F)

    override fun getStatsAsStrings(): String {
        var textSeuils = ""
        seuils.forEach {
            textSeuils += "|   ${it.toPrettyString()}\n"
        }
        return "Seuils:\n" + textSeuils +
                (if (coupCritiques.isNotBlank()) "CC : ${strSimplify(coupCritiques, false)}\n" else "") +
                "Max énergie : $maximumEnergie\n" +
                "FAJ Max : $fajMax\n" +
                (if (contraintes.isNotBlank()) "${strSimplify(contraintes, false)}\n" else "") +
                "Poids : $poids\n" +
                "${strSimplify(capaciteSpeciale, false)}\n"
    }

    override fun getStatsSimplifiedAsStrings(): String {
        var textSeuils = ""
        seuils.forEach {
            textSeuils += "|   ${it.toPrettyString()}\n"
        }
        return "Seuils:\n" + textSeuils +
                (if (coupCritiques.isNotBlank()) "CC : ${strSimplify(coupCritiques, true)}\n" else "") +
                "FAJ Max : $fajMax\n" +
                (if (contraintes.isNotBlank()) " ${strSimplify(contraintes, true)}\n" else "") +
                "Poids : $poids\n" +
                "${strSimplify(capaciteSpeciale, true)}\n"
    }

    override fun parseFromString(listStringElement: List<String>): ApiableItem {
        return Arme(
            listStringElement[0].cleanupForDB(),
            parseSeuils(listStringElement[1]),
            listStringElement[2],
            listStringElement[3].getIntOrZero(),
            listStringElement[4],
            listStringElement[5].getIntOrZero(),
            listStringElement[6].getIntOrZero(),
            listStringElement[7],
            listStringElement[8]
            )
    }



    override fun getParsingRulesAttributesAsList(): List<String> {
        return listOf(
            "Nom: String",
            "Seuils: Format = |Int/Int=Effect:Int|EffectType:Int...\\n|Int/Int=Effect:Int|EffectType:Int ",
            "Coups critiques :String",
            "Maximum Energie : Int",
            "Contraintes : String",
            "FAJ Max : Int",
            "Poids : Int",
            "Capacite speciale : String",
            "nom complet : String"
        )
    }

    override fun getDeparsedAttributes(): List<String> {

        var textSeuils = ""
        seuils.forEach {
            textSeuils += "|$it\n"
        }
        textSeuils = textSeuils.trim('\n')


        return listOf<String>(
            nom,
            textSeuils,
            coupCritiques,
            maximumEnergie.toString(),
            contraintes,
            fajMax.toString(),
            poids.toString(),
            capaciteSpeciale,
            nomComplet
        )
    }

}

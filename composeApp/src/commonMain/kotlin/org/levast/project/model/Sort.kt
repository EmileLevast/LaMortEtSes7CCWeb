import androidx.compose.ui.graphics.Color
import kotlinx.serialization.Serializable
import lamortetses7ccweb.composeapp.generated.resources.Res
import lamortetses7ccweb.composeapp.generated.resources.logoarme
import lamortetses7ccweb.composeapp.generated.resources.logomagie
import lamortetses7ccweb.composeapp.generated.resources.logomiracle
import lamortetses7ccweb.composeapp.generated.resources.logopyromancie
import org.jetbrains.compose.resources.DrawableResource

@Serializable
class Sort(
    override val nom:String="inconnu",
    val sortType:SpellType=SpellType.AME,
    val utilisation:Int=0,
    val cout:String="Aucune",
    val intelligenceMin:Int=0,
    val contraintes:String="Aucune",
    val seuils:List<Seuil> = mutableListOf(),//en cl� c'est le facteur et en valeur c'est la liste des seuils associ�s
    val coupCritiques:String="",
    val iajMax:Int=0,
    val description:String="",
    override val nomComplet:String = ""
) :ApiableItem(){

    override val _id: Int = nom.hashCode()

    override var isAttached: Boolean = false
    override val color: Color
        get() = Color(0xFF00AEEF)

    override fun getStatsAsStrings(): String {
        return statsAsStringAccordingToSimplifyOrNot(false)
    }

    private fun statsAsStringAccordingToSimplifyOrNot(isSimplify:Boolean): String {
        var textSeuils = ""
        seuils.forEach {
            textSeuils += "|   ${it.toPrettyString()}\n"
        }

        val coupCritiquesParsed = strSimplify(coupCritiques, isSimplify)

        return sortType.symbol + "\n" +
                "Utilisations : $utilisation\n" +
                "Cout : $cout\n" +
                "Intelligence Minimum : $intelligenceMin\n" +
                (if (contraintes.isNotBlank()) "$contraintes\n" else "") +
                "Seuils:\n" + textSeuils +
                (if (coupCritiquesParsed.isNotBlank()) "CC : $coupCritiquesParsed\n" else "") +
                "IAJ Max : $iajMax\n" +
                "${strSimplify(description, isSimplify)}\n"
    }

    override fun getStatsSimplifiedAsStrings(): String {
        return statsAsStringAccordingToSimplifyOrNot(true)
    }

    override fun getParsingRulesAttributesAsList(): List<String> {
        return listOf(
            "Nom: String",
            "Type: SpellType = (ame, necromancie, psionique, pyromancie, miracle) ",
            "Utilisation : Int",
            "Cout : String",
            "Intelligence Min : Int",
            "contraintes : String",
            "Seuils: Format = |Int/Int=Effect:Int|EffectType:Int...\\n|Int/Int=Effect:Int|EffectType:Int  ",
            "Coups critiques :String",
            "IAJ Max : Int",
            "Description : String",
            "nom complet : String"
        )
    }



    override fun parseFromString(listStringElement: List<String>): ApiableItem {
        return Sort(
            listStringElement[0].cleanupForDB(),
            parseSpellType(listStringElement[1]),
            listStringElement[2].getIntOrZero(),
            listStringElement[3],
            listStringElement[4].getIntOrZero(),
            listStringElement[5],
            parseSeuils(listStringElement[6]),
            listStringElement[7],
            listStringElement[8].getIntOrZero(),
            listStringElement[9],
            listStringElement[10]
        )
    }

    override fun getDeparsedAttributes(): List<String> {

        var textSeuils = ""
        seuils.forEach {
            textSeuils += "|$it\n"
        }

        return listOf<String>(
            nom,
            sortType.name,
            utilisation.toString(),
            cout,
            intelligenceMin.toString(),
            contraintes,
            textSeuils,
            coupCritiques,
            iajMax.toString(),
            description,
            nomComplet
        )
    }

    override fun getBackgroundBorder(): String = "border${sortType.name.lowercase()}.svg"


    override fun getImageDrawable(): DrawableResource {
        return when(sortType){
            SpellType.AME -> Res.drawable.logomagie
            SpellType.PYROMANCIE -> Res.drawable.logopyromancie
            SpellType.PSIONIQUE -> Res.drawable.logomagie
            SpellType.MIRACLE -> Res.drawable.logomiracle
            SpellType.NECROMANCIE -> Res.drawable.logomagie
            SpellType.ARACHNOMANCIE -> Res.drawable.logomagie
        }
    }

}
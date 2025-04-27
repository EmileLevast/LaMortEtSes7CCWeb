import androidx.compose.ui.graphics.Color
import kotlinx.serialization.Serializable
import lamortetses7ccweb.composeapp.generated.resources.Res
import lamortetses7ccweb.composeapp.generated.resources.logoarme
import lamortetses7ccweb.composeapp.generated.resources.logoarmure
import lamortetses7ccweb.composeapp.generated.resources.logobouclier
import org.jetbrains.compose.resources.DrawableResource

@Serializable
class Bouclier(
    override val nom: String ="inconnu",
    val defense:Map<EffectType,String> = mapOf(),
    val contraintes:String="Aucune contraintes",
    val poids:Int=0,
    val capaciteSpeciale:String="",
    override val nomComplet:String = ""
)
    :ApiableItem(){

    override val _id: Int = nom.hashCode()
    override var isAttached: Boolean = false
    override val color: Color
        get() = Color(0x990F056B)

    override fun getStatsAsStrings(): String {
        return "Defense: ${convertEffectTypeStatsToString(defense)}"+"\n" +
                strSimplify(contraintes,false)+"\n" +
                "Poids:$poids"+"\n"+
                strSimplify(capaciteSpeciale,false)
    }

    override fun getStatsSimplifiedAsStrings(): String {
        return "Defense: ${convertEffectTypeStatsToString(defense)}"+"\n" +
                strSimplify(contraintes,true)+"\n" +
                "Poids:$poids"+"\n"+
                strSimplify(capaciteSpeciale,true)
    }

    override fun parseFromString(listStringElement : List<String>):ApiableItem {
           return Bouclier(
               listStringElement[0].cleanupForDB(),
               parseDefense(listStringElement[1]),
               listStringElement[2],
               listStringElement[3].run{ if(isNotBlank()) toInt() else{0} },
               listStringElement[4],
               listStringElement[5]
            )
    }

    override fun getParsingRulesAttributesAsList(): List<String> {
        return listOf(
            "Nom: String",
            "Defense : Format = EffectType:Int|EffectType:Int... (EffectType = Po/Ph/F/Ma)",
            "Contraintes : String",
            "Poids : Int",
            "Capacite speciale : String",
            "nom complet : String"
        )
    }

    override fun getDeparsedAttributes(): List<String> {
        return listOf(
            nom,
            deparseDefense(defense),
            contraintes,
            poids.toString(),
            capaciteSpeciale,
            nomComplet
        )
    }

    override fun getImageDrawable(): DrawableResource {
        return Res.drawable.logobouclier
    }

}


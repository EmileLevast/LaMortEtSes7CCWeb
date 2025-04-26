import androidx.compose.ui.graphics.Color
import kotlinx.serialization.Serializable
import lamortetses7ccweb.composeapp.generated.resources.Res
import lamortetses7ccweb.composeapp.generated.resources.logoarme
import lamortetses7ccweb.composeapp.generated.resources.logomonstre
import org.jetbrains.compose.resources.DrawableResource

@Serializable
data class Monster(
    override val nom: String= "inconnu",
    val vie: Int=0,
    val force:Map<Int,Int> = mapOf(),
    val defense:Map<EffectType,String> = mapOf(),
    val intelligence:Int = 0,
    val energie:Int = 0,
    val listDrops:Map<String,Int> = mapOf(),
    val ames:Int=0,
    val capaciteSpeciale:String="",
    override val nomComplet:String = ""
) : ApiableItem(){

    override val _id = nom.hashCode()
    override var isAttached = false
    override val color: Color
        get() = Color(0xFFBB0B0B)

    private fun constructForceSeuils():String{
        return force.values.last().toString()
    }

    private fun constructListDropsString():String{
        val listDroptext = StringBuilder()
        listDrops.forEach {
            listDroptext.append("  ${it.key} ${if(it.value == 0 ){"∅"}else{"/ ${it.value}+"}}\n")
        }

        return listDroptext.toString()
    }

    override fun getStatsAsStrings(): String {
        val textForceSeuils = constructForceSeuils()
        return "Vie : $vie\n"+
                "Force : $textForceSeuils\n"+
                "Defense:" + convertEffectTypeStatsToString(defense) +"\n"+
                "Intelligence:" + intelligence +"\n"+
                "Energie : $energie\n" +
                "Drops: \n" +
                constructListDropsString() +
                "Ames : $ames\n"+
                "${strSimplify(capaciteSpeciale,false)}\n"
    }

    override fun getStatsSimplifiedAsStrings(): String {
        val textForceSeuils = constructForceSeuils()
        return "Vie : $vie\n"+
                "Force : $textForceSeuils\n"+
                "Defense:" + convertEffectTypeStatsToString(defense) +"\n"+
                "Intelligence:" + intelligence +"\n"+
                "Energie : $energie\n" +
                "Drops: \n" +
                constructListDropsString() +
                "Ames : $ames\n"+
                "${strSimplify(capaciteSpeciale,true)}\n"
    }

    override fun parseFromString(listStringElement : List<String>):ApiableItem {
        return Monster(
            listStringElement[0].cleanupForDB(),
            listStringElement[1].toInt(),
            parseSeuilsForce(listStringElement[2]),
            parseDefense(listStringElement[3]),
            listStringElement[4].toInt(),
            listStringElement[5].toInt(),
            parseDrops(listStringElement[6]),
            listStringElement[7].toInt(),
            listStringElement[8],
            listStringElement[9]
        )
    }

    override fun getParsingRulesAttributesAsList(): List<String> {
        return listOf(
            "Nom: Chaine de caractères",
            "Vie: Entier",
            "Force : Format = Int",
            "Defense : Format = EffectType:Int|EffectType:Int... (EffectType = Po/Ph/F/Ma)",
            "Intelligence : Int",
            "Energie : Int",
            "Drops : Format = String:Int|String:Int... ",
            "Ames : Int",
            "Capacite speciale : String",
            "nom complet : String"
            )
    }

    override fun getDeparsedAttributes(): List<String> {
        return listOf(
            nom,
            vie.toString(),
            force.values.last().toString(),
            deparseDefense(defense),
            intelligence.toString(),
            energie.toString(),
            deparseListDrops(listDrops),
            ames.toString(),
            capaciteSpeciale,
            nomComplet
        )
    }

    override fun getImageDrawable(): DrawableResource {
        return Res.drawable.logomonstre
    }
}
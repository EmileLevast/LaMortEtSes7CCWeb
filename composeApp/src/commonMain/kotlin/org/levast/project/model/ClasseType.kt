import androidx.compose.ui.graphics.Color
import kotlinx.serialization.Serializable
import lamortetses7ccweb.composeapp.generated.resources.Res
import lamortetses7ccweb.composeapp.generated.resources.logojoueur
import org.jetbrains.compose.resources.DrawableResource

@Serializable
class ClasseType(
    override val nom:String="inconnu",
    override val nomComplet:String = "",
    var carac: Carac = Carac(),
    var chaineEquipementSerialisee: String ="",
    ) : ApiableItem() {

    override val _id = nom.hashCode()

    override val color: Color
        get() = Color(0xFFCC091C)
    override var isAttached = false

    override fun getImageDrawable(): DrawableResource {
        return Res.drawable.logojoueur
    }

    override fun getStatsAsStrings(): String = "" +
            "${nomComplet}\n" +
            "${carac.toPrettyString()}\n" +
            chaineEquipementSerialisee.deserializeToListElements().joinToString("\n")

    override fun getParsingRulesAttributesAsList(): List<String> {
        return listOf(
            "Nom: String",
            "nom complet : String",
            "caracOrigin : vie/force/EffectType:Int|Effect:Int.../intelligence/energie/humanite/ame",
            "equipement : $TYPE_LISTE_CHAINE",
        )
    }

    override fun getDeparsedAttributes(): List<String> {
        return listOf<String>(
            nom,
            nomComplet,
            carac.toFormattedString(),
            chaineEquipementSerialisee
        )
    }

    override fun parseFromString(listStringElement: List<String>): ApiableItem {
        return ClasseType(
            listStringElement[0].cleanupForDB(),
            listStringElement[1],
            Carac.fromFormattedString(listStringElement[2]),
            listStringElement[3]
        )
    }
}
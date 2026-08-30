import androidx.compose.ui.graphics.Color
import kotlinx.serialization.Serializable
import lamortetses7ccweb.composeapp.generated.resources.Res
import lamortetses7ccweb.composeapp.generated.resources.logojoueur
import org.jetbrains.compose.resources.DrawableResource

@Serializable
class Race(
    override val nom:String="inconnu",
    override val nomComplet:String = "",
    var carac: Carac = Carac(),
    var capacites:MutableMap<String,Int> = mutableMapOf(),
    override var imageNom: String="",
    override var histoire: String=""
    ) : ApiableItem() {

    override val _id = nom.hashCode()

    override val color: Color
        get() = Color(0xFFD2A12A)
    override var isAttached = false

    override fun getImageDrawable(): DrawableResource {
        return Res.drawable.logojoueur
    }

    override fun getStatsAsStrings(): String = "" +
            "${nomComplet}\n" +
            "${carac.toPrettyString()}\n" +
            capacites.getAsString()

    override fun getParsingRulesAttributesAsList(): List<String> {
        return listOf(
            "Nom: String",
            "caracteristiques : vie/force/EffectType:Int|Effect:Int.../intelligence/energie/humanite/ame",
            "nom complet : String",
            "capacites: ${CHAR_SEP_EQUIPEMENT}String:Int$CHAR_SEP_EQUIPEMENT${CHAR_SEP_EQUIPEMENT}String:Int${CHAR_SEP_EQUIPEMENT}",
            "nom Image : String",
            "histoire : String"
        )
    }

    override fun getDeparsedAttributes(): List<String> {
        return listOf<String>(
            nom,
            carac.toFormattedString(),
            nomComplet,
            capacites.getAsString(),
            imageNom,
            histoire
        )
    }

    override fun parseFromString(listStringElement: List<String>): ApiableItem {
        return Race(
            listStringElement[0].cleanupForDB(),
            listStringElement[2],
            Carac.fromFormattedString(listStringElement[1]),
            getDeparseStringAsMapStrInt(listStringElement[3]),
            listStringElement[4],
            listStringElement[5]
        )
    }
}
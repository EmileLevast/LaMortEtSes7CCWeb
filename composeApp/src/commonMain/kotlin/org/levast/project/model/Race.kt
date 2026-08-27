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
    ) : ApiableItem() {

    override val _id = nom.hashCode()

    override val color: Color
        get() = Color(0xFFD2A12A)
    override var isAttached = false

    override fun getImageDrawable(): DrawableResource {
        return Res.drawable.logojoueur
    }

    override fun getStatsAsStrings(): String = "" +
            "${nomComplet}\nCarac : ${carac.toCSV()}\n" +
            capacites.getAsString()

    override fun getParsingRulesAttributesAsList(): List<String> {
        TODO("Not yet implemented")
    }

    override fun getDeparsedAttributes(): List<String> {
        TODO("Not yet implemented")
    }

    override fun parseFromString(listStringElement: List<String>): ApiableItem {
        TODO("Not yet implemented")
    }
}
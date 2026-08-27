import androidx.compose.ui.graphics.Color
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.DrawableResource

@Serializable
class Race(
    override val nom:String="inconnu",
    override val nomComplet:String = ""
) : ApiableItem() {

    override val _id = nom.hashCode()

    override val color: Color
        get() = TODO("Not yet implemented")
    override var isAttached = false

    override fun getImageDrawable(): DrawableResource {
        TODO("Not yet implemented")
    }

    override fun getStatsAsStrings(): String {
        TODO("Not yet implemented")
    }

    override fun getParsingRulesAttributesAsList(): List<String> {
        TODO("Not yet implemented")
    }

    override fun getDeparsedAttributes(): List<String> {
        TODO("Not yet implemented")
    }

    override fun getHead(): String {
        return super.getHead()
    }

    override fun getBody(): String {
        return super.getBody()
    }

    override fun parseFromString(listStringElement: List<String>): ApiableItem {
        TODO("Not yet implemented")
    }

    override fun getStatsSimplifiedAsStrings(): String {
        return super.getStatsSimplifiedAsStrings()
    }

    override fun getBackgroundBorder(): String {
        return super.getBackgroundBorder()
    }
}
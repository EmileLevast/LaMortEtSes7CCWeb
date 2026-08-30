import androidx.compose.ui.graphics.Color
import model.HeadBodyShowable
import org.jetbrains.compose.resources.DrawableResource
import org.levast.project.viewModel.stateviewmodel.FilterUser

interface IListItem : HeadBodyShowable {
    val _id:Int
    val nom:String
    val nomComplet:String
    val color: Color
    var isAttached:Boolean

    var histoire:String
    var imageNom:String

    fun getImageDrawable() : DrawableResource
    fun getStatsAsStrings():String
    fun getStatsSimplifiedAsStrings():String
    fun getParsingRulesAttributesAsList():List<String>
    fun getDeparsedAttributes():List<String>

    override fun getHead(): String = nomComplet.ifBlank { nom }

    override fun getBody(): String = getStatsSimplifiedAsStrings()

}

fun getListItemFiltered(itemsToFilter :List<IListItem>,filterUser: FilterUser, itemsPinned : List<String>?):List<IListItem>{
    return when (filterUser) {
        FilterUser.EQUIPES -> {
            itemsToFilter.filter { itemsPinned?.contains(it.nom) == true }
        }
        FilterUser.SORTS -> {
            itemsToFilter.filterIsInstance<Sort>()
        }
        FilterUser.ARMES -> {
            itemsToFilter.filterIsInstance<Arme>()
        }
        FilterUser.ARMURES -> {
            itemsToFilter.filterIsInstance<Armure>()
        }
        FilterUser.SPECIAL -> {
            itemsToFilter.filterIsInstance<Special>()
        }
        FilterUser.BOUCLIERS -> {
            itemsToFilter.filterIsInstance<Bouclier>()
        }
        else -> itemsToFilter
    }
}


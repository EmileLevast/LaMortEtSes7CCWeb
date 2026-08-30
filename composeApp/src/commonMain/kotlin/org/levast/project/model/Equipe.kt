

import androidx.compose.ui.graphics.Color
import kotlinx.serialization.Serializable
import lamortetses7ccweb.composeapp.generated.resources.Res
import lamortetses7ccweb.composeapp.generated.resources.logoequipe
import org.jetbrains.compose.resources.DrawableResource

@Serializable
class Equipe(
    override val nom:String="inconnu",
    var chaineJoueurSerialisee: String ="",
    override val nomComplet:String = "",
    var chaineDecouvertSerialisee: String ="",
    var chaineDecouvertAnonyme: String ="",
    override var imageNom: String="",
    override var histoire: String=""
) : ApiableItem() {

    override val _id = nom.hashCode()
    override var isAttached = false
    override val color: Color
        get() = Color(0xFF29BD38)

    override fun getStatsAsStrings():String{
        return "Equipe : \n"+chaineJoueurSerialisee.formatToPrettyString()
    }

    override fun parseFromString(listStringElement : List<String>):ApiableItem{
        return Equipe(
            listStringElement[0].cleanupForDB(),
            listStringElement[1],
            listStringElement[2],
            listStringElement[3],
            listStringElement[4],
            listStringElement[5],
            listStringElement[6]
        )
    }

    fun getMembreEquipe():List<String>{
        return chaineJoueurSerialisee.deserializeToListElements()
    }

    override fun getParsingRulesAttributesAsList(): List<String> {

        return listOf(
            "Nom: String",
            "membres : $TYPE_LISTE_CHAINE",
            "nom complet : String",
            "elements découverts : $TYPE_LISTE_CHAINE",
            "elements anonyme: $TYPE_LISTE_CHAINE",
            "nom Image : String",
            "histoire : String"
            )
    }

    override fun getDeparsedAttributes(): List<String> {
        return listOf(
            nom,
            chaineJoueurSerialisee,
            nomComplet,
            chaineDecouvertSerialisee,
            chaineDecouvertAnonyme,
            imageNom,
            histoire
        )
    }


    override fun getBody()=  chaineJoueurSerialisee.deserializeToListElements().joinToString("\n")

    override fun getImageDrawable(): DrawableResource {
        return Res.drawable.logoequipe
    }

    fun addMembreEquipe(membre: String){
        chaineJoueurSerialisee+="$CHAR_SEP_EQUIPEMENT$membre$CHAR_SEP_EQUIPEMENT"
    }

}
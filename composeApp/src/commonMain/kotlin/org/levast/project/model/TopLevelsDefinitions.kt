import androidx.compose.ui.graphics.ImageBitmap

//Update this list whenever you want to add a specific item to the database don't change it during execution
//TODO ajouter ici un element instancie dans cette liste a chaque creation d'une nouvelle classe
val unmutableListApiItemDefinition = listOf(Arme(),Armure(),Monster(),Bouclier(),Sort(),Special(),Joueur(), Equipe())

///Endpoints
const val ENDPOINT_RECHERCHE_STRICTE = "precis"
const val QUERY_PARAMETER_NOM = "nom"
const val QUERY_PARAMETER_ID = "id"
const val ENDPOINT_RECHERCHE_TOUT = "all"
const val ENDPOINT_MAJ_CARACS_JOUEUR = "maj_caracs_joueur"
const val ENDPOINT_MAJ_NOTES_JOUEUR = "maj_notes_joueur"
//endpoints des comptes utilisateur
const val ENDPOINT_COMPTE_UTILISATEUR_ROOT = "compte_utilisateur"
const val ENDPOINT_COMPTE_UTILISATEUR_GET_ALL = "all"
const val ENDPOINT_COMPTE_UTILISATEUR_INSERT = "insert"
const val ENDPOINT_COMPTE_UTILISATEUR_UPDATE = "update"
const val ENDPOINT_COMPTE_UTILISATEUR_DELETE = "delete"

const val CHAR_SEP_EQUIPEMENT = "|"
const val BALISE_SIMPLE_RULES = "[SIMPLE]"
const val TYPE_LISTE_CHAINE = "${CHAR_SEP_EQUIPEMENT}String$CHAR_SEP_EQUIPEMENT${CHAR_SEP_EQUIPEMENT}String${CHAR_SEP_EQUIPEMENT}"

//image url
const val IMAGENAME_CARD_BACKGROUND = "fondCarte.jpg"

enum class EffectType(val shortname:String, val symbol:String){
    FIRE("F"," Feu"),
    MAGIC("Ma"," Magique"),
    POISON("Po"," Poison"),
    PHYSICAL("Ph"," Physique");
}

//une map qui contient toutes les images déjà téléchargées
val mapImagesDownload = mutableMapOf<String,ImageBitmap?>()

fun convertEffectTypeStatsToString(statsToConvert:Map<EffectType,String> ) :String {
    val listMapDefense = StringBuilder()
    statsToConvert.forEach {
        listMapDefense.append("${it.key.symbol}:${it.value}")
    }
    return listMapDefense.toString()
}

fun strSimplify(str:String, isSimpleRulesOn:Boolean):String{
    return if(str.contains(BALISE_SIMPLE_RULES) && isSimpleRulesOn){
        val indexFin = str.indexOf(BALISE_SIMPLE_RULES)+BALISE_SIMPLE_RULES.length
        if(indexFin == str.length){
            str
        }else{
            str.substring(indexFin)
        }
    }else if(str.contains(BALISE_SIMPLE_RULES)){
        str.substring(0,str.indexOf(BALISE_SIMPLE_RULES))
    }else{
        str
    }
}


fun String.getIntOrZero(): Int {
    return try {
        if (isNotBlank()) toInt() else { 0 }
    } catch (e: Exception) {
        println(" Erreur de conversion en Int depuis une string :\n " +
                e.stackTraceToString()
        )
        0
    }
}

fun String.getIntOrZeroOrNull(): Int? {
    return try {
        if (isNotBlank()) toInt() else { null }
    } catch (e: Exception) {
        println(" conversion impossible en entier : renvoi null :\n " +
                e.stackTraceToString()
        )
        null
    }
}


enum class SpellType(val shortname:String, val symbol:String){
    AME("ame","Sort d'âme"),
    PYROMANCIE("pyromancie","Sort de Pyromancie"),
    PSIONIQUE("psionique","Sort Psionique"),
    MIRACLE("miracle","Miracle"),
    NECROMANCIE("necromancie","Sort de Nécromancie"),
    ARACHNOMANCIE("arachnomancie","Sort d'Arachnomancie");
}

enum class SpecialItemType{
    ANNEAU,
    TALISMAN,
    AMBRE,
    BRAISE,
    TECHNIQUE,
    OUTIL;
}

fun parseDefense(inputElement: String): MutableMap<EffectType, String> {

    val mapDefenseType = mutableMapOf<EffectType, String>()

    if (inputElement.isNotEmpty()) {
        inputElement.split("|").forEach { currentDefense ->
            currentDefense.split(":").let { currentEffectType ->
                //on check si le type correspond bien a un vrai type
                mapDefenseType[EffectType.entries
                    .find { enumEffectType -> enumEffectType.shortname == currentEffectType.first() }!!] =
                    currentEffectType.last()
            }
        }
    }

    return mapDefenseType
}

fun deparseDefense(defense: Map<EffectType, String>): String {
    var res = ""
    defense.forEach {
        res += it.key.shortname + ":" + it.value + "|"
    }
    return res.removeSuffix("|")
}

fun String.deserializeToListElements() = this.removeSurrounding(CHAR_SEP_EQUIPEMENT).ifBlank { null }?.split(CHAR_SEP_EQUIPEMENT+CHAR_SEP_EQUIPEMENT)
fun String.formatToPrettyString() = this.replace(CHAR_SEP_EQUIPEMENT+CHAR_SEP_EQUIPEMENT,"\n")

fun getNbrUtilisationAccordingItem(equipement : IListItem,nbrUtilisation : Int?):String{
    if(nbrUtilisation != null){
        return nbrUtilisation.toString()
    }else if( equipement is Sort){//si c'est un sort et qu'il n'y a pas d'utilisations renseignees alors on affiche le nbr d'utilisation du sort
        return equipement.utilisation.toString()
    }else{
        return "1"//si c'est pas un sort et qu'il n'y a pas d'utilisations renseignees alors on affiche 1
    }
}
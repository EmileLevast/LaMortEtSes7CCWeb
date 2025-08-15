import aws.sdk.kotlin.services.secretsmanager.SecretsManagerClient
import aws.sdk.kotlin.services.secretsmanager.model.GetSecretValueRequest
import aws.sdk.kotlin.services.secretsmanager.model.GetSecretValueResponse
import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.ServerApi
import com.mongodb.ServerApiVersion
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.levast.project.DB_NAME
import org.levast.project.ENV_DETECTION_CONFIG_DB
import org.levast.project.KEY_SECRET_ACCESS_DB
import org.levast.project.logger
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo
import org.litote.kmongo.regex

lateinit var database: CoroutineDatabase

val collectionsApiableItem:MutableMap<String,CoroutineCollection<out ApiableItem>> = mutableMapOf()

fun initDatabase(){

    //on cherche cette variable d'environnement
    val isRunningMongoDBLocally = System.getenv(ENV_DETECTION_CONFIG_DB)

    logger.info("ELT variable d'environnement $ENV_DETECTION_CONFIG_DB = $isRunningMongoDBLocally")

    // si cette variable n'est pas null ou vide alors c'est qu'on est sur un environnement local ( test sur mon PC notamment)
    if(!isRunningMongoDBLocally.isNullOrBlank()){

        database = KMongo.createClient().coroutine.getDatabase(DB_NAME)

    }else{
        //Sinon on trouve pas la variable c'est qu'on est sur un environnement de production qui se connecte à notre bdd de prod sur atlas
        val jsonString = runBlocking {
            getSecret()
        }
        val jsonObject = if(!jsonString.isNullOrBlank()) Json.parseToJsonElement(jsonString).jsonObject else null
        val secretsDatabase = jsonObject?.get(KEY_SECRET_ACCESS_DB)?.jsonPrimitive?.content

        if(secretsDatabase.isNullOrBlank()){
            logger.error( "ELT infos DB non trouves")
        }else {

            logger.info("ELT infos DB recuperees")

            val connectionString = "mongodb+srv://emilelevast3441:${secretsDatabase}@clustermortetses7cc.wyc210d.mongodb.net/?retryWrites=true&w=majority&appName=ClusterMortEtSes7CC"
            val serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build()
            val mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(ConnectionString(connectionString))
                .serverApi(serverApi)
                .build()
            // Create a new client and connect to the server
            database = KMongo.createClient(mongoClientSettings).coroutine.getDatabase(DB_NAME)

            unmutableListApiItemDefinition.forEach {
                collectionsApiableItem[it.nameForApi!!] = database.getCollection(it.nameForApi!!)
            }
        }
    }


}

//TODO ajouter ici une ligne dans le when a chaque fois qu'eun nouvelle collection dans la bdd est cree
/**
 * strict est un booleen quand il est à true c'est à dire qu'on cherche exactement l'element qui matche le nom
 */
suspend fun insertListElements(instanceOfCollectionItemDefinition:ApiableItem,listToInsert:List<ApiableItem>): Any {
    return when(instanceOfCollectionItemDefinition){
        is Arme ->  database.getCollection<Arme>().insertMany(listToInsert as List<Arme>)
        is Armure -> database.getCollection<Armure>().insertMany(listToInsert as List<Armure>)
        is Monster -> database.getCollection<Monster>().insertMany(listToInsert as List<Monster>)
        is Bouclier -> database.getCollection<Bouclier>().insertMany(listToInsert as List<Bouclier>)
        is Sort -> database.getCollection<Sort>().insertMany(listToInsert as List<Sort>)
        is Special -> database.getCollection<Special>().insertMany(listToInsert as List<Special>)
        is Joueur -> database.getCollection<Joueur>().insertMany(listToInsert as List<Joueur>)
        is Equipe -> database.getCollection<Equipe>().insertMany(listToInsert as List<Equipe>)
        else-> Unit
    }
}

//TODO ajouter ici une ligne dans le when a chaque fois qu'eun nouvelle collection dans la bdd est cree
/**
 * strict est un booleen quand il est à true c'est à dire qu'on cherche exactement l'element qui matche le nom
 */
suspend fun getCollectionElements(instanceOfCollectionItemDefinition:ApiableItem, nameOfItemSearched:String, strict:Boolean =false):List<ApiableItem>{
    return when(instanceOfCollectionItemDefinition){
        is Arme -> getElementAccordingToType(nameOfItemSearched, instanceOfCollectionItemDefinition,strict)
        is Armure -> getElementAccordingToType(nameOfItemSearched, instanceOfCollectionItemDefinition,strict)
        is Monster -> getElementAccordingToType(nameOfItemSearched, instanceOfCollectionItemDefinition,strict)
        is Bouclier -> getElementAccordingToType(nameOfItemSearched, instanceOfCollectionItemDefinition,strict)
        is Sort -> getElementAccordingToType(nameOfItemSearched, instanceOfCollectionItemDefinition,strict)
        is Special -> getElementAccordingToType(nameOfItemSearched, instanceOfCollectionItemDefinition,strict)
        is Joueur -> getElementAccordingToType(nameOfItemSearched, instanceOfCollectionItemDefinition,strict)
        is Equipe -> getElementAccordingToType(nameOfItemSearched, instanceOfCollectionItemDefinition,strict)
        else-> getElementAccordingToType(nameOfItemSearched, instanceOfCollectionItemDefinition as Monster,strict)
    }
}

//TODO ici aussi
/**
 * strict est un booleen quand il est à true c'est à dire qu'on cherche exactement l'element qui matche le nom
 */
suspend fun getCollectionElementsAsString(instanceOfCollectionItemDefinition:ApiableItem, nameOfItemSearched:String, strict:Boolean =false):List<String>{
    return when(instanceOfCollectionItemDefinition){
        is Arme -> getElementAccordingToTypeAsString(nameOfItemSearched, instanceOfCollectionItemDefinition,strict)
        is Armure -> getElementAccordingToTypeAsString(nameOfItemSearched, instanceOfCollectionItemDefinition,strict)
        is Monster -> getElementAccordingToTypeAsString(nameOfItemSearched, instanceOfCollectionItemDefinition,strict)
        is Bouclier -> getElementAccordingToTypeAsString(nameOfItemSearched, instanceOfCollectionItemDefinition,strict)
        is Sort -> getElementAccordingToTypeAsString(nameOfItemSearched, instanceOfCollectionItemDefinition,strict)
        is Special -> getElementAccordingToTypeAsString(nameOfItemSearched, instanceOfCollectionItemDefinition,strict)
        is Joueur -> getElementAccordingToTypeAsString(nameOfItemSearched, instanceOfCollectionItemDefinition,strict)
        is Equipe -> getElementAccordingToTypeAsString(nameOfItemSearched, instanceOfCollectionItemDefinition,strict)
        else-> getElementAccordingToTypeAsString(nameOfItemSearched, instanceOfCollectionItemDefinition as Monster,strict)
    }
}

suspend inline fun <reified T:ApiableItem> getElementAccordingToType(
    nameOfItemWanted: String,
    instanceOfCollectionItemDefinition: T,
    strict: Boolean
):List<T>{
    return if(strict){
        database.getCollection<T>(T::class.simpleName!!).find(ApiableItem::nom eq nameOfItemWanted).toList()
    }else
    {
        val regexp = if(nameOfItemWanted.contains('*')) nameOfItemWanted else ".*$nameOfItemWanted.*"
        database.getCollection<T>(T::class.simpleName!!).find(ApiableItem::nom regex regexp).toList()
    }
}

suspend inline fun <reified T:ApiableItem> getElementAccordingToTypeAsString(
    nameOfItemWanted: String,
    instanceOfCollectionItemDefinition: T,
    strict: Boolean
):List<String>{
    return if(strict){
        database.getCollection<T>(T::class.simpleName!!).find(ApiableItem::nom eq nameOfItemWanted).toList().map { Json.encodeToString(it) }
    }else
    {
        val regexp = if(nameOfItemWanted.contains('*')) nameOfItemWanted else ".*$nameOfItemWanted.*"
        database.getCollection<T>(T::class.simpleName!!).find(ApiableItem::nom regex regexp).toList().map { Json.encodeToString(it) }
    }
}



suspend fun getSecret() :String?{
    val secretName = "prod/jdrdb/access"

    // Create a Secrets Manager client
    val builderSecretsManager = SecretsManagerClient.builder()
    builderSecretsManager.config.region = "eu-north-1"
    val client: SecretsManagerClient = builderSecretsManager.build()


    val getSecretValueRequest: GetSecretValueRequest = GetSecretValueRequest({
        this.secretId = secretName
    })

    val getSecretValueResponse: GetSecretValueResponse

    try {
        getSecretValueResponse = client.getSecretValue(getSecretValueRequest)
    } catch (e: Exception) {
        // For a list of exceptions thrown, see
        // https://docs.aws.amazon.com/secretsmanager/latest/apireference/API_GetSecretValue.html
        throw e
    }

    return getSecretValueResponse.secretString

}
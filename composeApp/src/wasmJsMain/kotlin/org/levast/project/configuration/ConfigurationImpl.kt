package org.levast.project.configuration


// At the top level of your kotlin file:

class ConfigurationImpl() : IConfiguration {

    // TODO: Vérifier si cette adresse IP par défaut est toujours pertinente pour les environnements de test
    private var properties: AppProperties = AppProperties("192.168.138.178","",)

    override fun getEndpointServer() = "http://lesfeusperegrins-env.eba-gmrjrq6d.eu-north-1.elasticbeanstalk.com/"

    override fun getIpAdressTargetServer() =  properties.ipAdressServer

    override fun setIpAdressTargetServer(adresseIp: String) {
        properties.ipAdressServer=adresseIp
    }

    override fun setUserName(nomUser: String) {
        properties.userName=nomUser
    }

    override fun getUserName(): String = properties.userName

    override fun setMode(isUserMode: Boolean?) {
        properties.isUserMode = isUserMode
    }

    override fun getMode(): Boolean? = properties.isUserMode
}
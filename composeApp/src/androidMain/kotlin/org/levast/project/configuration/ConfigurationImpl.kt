package org.levast.project.configuration

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

// At the top level of your kotlin file:
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ConfigurationImpl() : IConfiguration {

    private lateinit var properties:AppProperties
    private var context:Context?=null

    override fun getEndpointServer() = "http://${properties.ipAdressServer}:${properties.portServer}"

    fun setupContextForPreferences(context: Context){
        this.context=context
        loadAppProperties()
    }

    private fun loadAppProperties(){
        properties = AppProperties(runBlocking {
            context?.dataStore?.data?.map { preferences ->
                preferences[KEY_IP_ADDRESS] ?: "10.0.2.2"
            }?.first()?:"10.0.2.2"
        },
            runBlocking {
                context?.dataStore?.data?.map { preferences ->
                    preferences[KEY_USER_NAME] ?: ""
                }?.first() ?: ""
            })
    }

    override fun getIpAdressTargetServer() =  properties.ipAdressServer

    override fun setIpAdressTargetServer(adresseIp: String) {
        properties.ipAdressServer=adresseIp

        runBlocking {
            context?.dataStore?.edit { settings ->
                settings[KEY_IP_ADDRESS] = adresseIp
            }
        }
    }

    override fun setUserName(nomUser: String) {
        properties.userName=nomUser

        runBlocking {
            context?.dataStore?.edit { settings ->
                settings[KEY_USER_NAME] = nomUser
            }
        }
    }

    override fun getUserName(): String = properties.userName

}
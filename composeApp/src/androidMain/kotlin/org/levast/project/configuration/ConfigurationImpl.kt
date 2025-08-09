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

    override fun getEndpointServer() = "http://${properties.adressServer}:${properties.getPortServer()}"

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

    override fun getAdressTargetServer() =  properties.adressServer

    override fun setadressTargetServer(adresseIp: String) {
        properties.adressServer=adresseIp

        saveToDatastore(adresseIp, KEY_IP_ADDRESS)
    }

    override fun setUserName(nomUser: String) {
        properties.userName=nomUser

        saveToDatastore(nomUser, KEY_USER_NAME)
    }

    override fun getUserName(): String = properties.userName

    override fun setMode(isUserMode: Boolean?) {
        properties.isUserMode=isUserMode

        if(isUserMode != null){
            saveToDatastore(isUserMode, KEY_MODE)
        }else{
            runBlocking {
                context?.dataStore?.edit { settings ->
                    settings.remove(KEY_MODE)
                }
            }
        }
    }

    override fun getMode(): Boolean? = properties.isUserMode


    private fun <T> saveToDatastore(updatedValue: T, keyToUse: Preferences.Key<T>) {
        runBlocking {
            context?.dataStore?.edit { settings ->
                settings[keyToUse] = updatedValue
            }
        }
    }
}
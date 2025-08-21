package org.levast.project.configuration

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

val KEY_IP_ADDRESS = stringPreferencesKey("key_ip_address")
val KEY_USER_NAME = stringPreferencesKey("key_user_name")
val KEY_MODE = booleanPreferencesKey("key_mode")
val KEY_HTTPS_MODE = booleanPreferencesKey("key_https_mode")
val KEY_USER_AUTH = stringPreferencesKey("key_user_auth")
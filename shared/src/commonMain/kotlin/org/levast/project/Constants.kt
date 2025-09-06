package org.levast.project

const val SERVER_PORT = 8080
const val SERVER_KTOR_PORT = 5000
const val SERVER_KTOR_PORT_SSL = 443
const val IP_ADRESS_SERVER = "13.51.248.211"
const val DNS_ADRESS_SERVER = "lesfeusperegrins.click"
const val ERROR_NETWORK_MESSAGE = "Le réseau semble inacessible : allume le serveur connard"
const val ENV_DETECTION_CONFIG_DB = "FEUS_PEREGRINS_LOCAL"
const val DB_NAME = "JDRProd"
const val KEY_SECRET_ACCESS_DB = "jdrdbaccess"

//ROLES en base de donnee
const val ROLE_ADMIN:String = "admin"

//NETWORK
const val DEBOUNCE_TIME_OUT_REQUEST_MS: Long = 1000
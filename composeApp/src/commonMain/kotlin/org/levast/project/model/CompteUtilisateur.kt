package org.levast.project.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CompteUtilisateur(
    var nom: String,
    var motDePasse: String,
    var roles: List<String>,
) {

    @SerialName("_id") val id: Int = nom.hashCode();

}
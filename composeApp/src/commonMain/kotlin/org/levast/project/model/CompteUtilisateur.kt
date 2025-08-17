package org.levast.project.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class CompteUtilisateur(
    var nom: String,
    var motDePasse: String,
    var nomJoueurAssocie: String,
) {

    @SerialName("_id") val id: Int = nom.hashCode();

}
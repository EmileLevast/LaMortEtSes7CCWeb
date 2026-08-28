package org.levast.project.viewModel.stateviewmodel

import ClasseType
import Equipe
import Joueur
import Race
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import org.levast.project.model.CompteUtilisateur

class JoueurState() {
    var nom by mutableStateOf("")

    var classeType by mutableStateOf<ClasseType?>(null)
    var race by mutableStateOf<Race?>(null)
    var compte by mutableStateOf<CompteUtilisateur?>(null)
    var equipe by mutableStateOf<Equipe?>(null)

    var classeTypeField by mutableStateOf("")
    var raceField by mutableStateOf("")
    var compteField by mutableStateOf("")
    var mdpField by mutableStateOf("")
    var equipeField by mutableStateOf("")

    fun toJoueur(): Joueur{
        return Joueur(nom,classeType= classeType?: ClasseType(), race=race?: Race())
    }

    constructor(joueur : Joueur) : this() {
        nom = joueur.nom
        classeType = joueur.classeType
        race = joueur.race
        classeTypeField = joueur.classeType.nom
        raceField = joueur.race.nom
    }
}
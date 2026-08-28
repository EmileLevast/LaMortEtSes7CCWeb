package org.levast.project.viewModel.stateviewmodel

import ClasseType
import Equipe
import Joueur
import Race
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import org.levast.project.model.CompteUtilisateur

class JoueurState {
    var nom by mutableStateOf("")
    var classeType by mutableStateOf<ClasseType?>(null)
    var race by mutableStateOf<Race?>(null)
    var compte by mutableStateOf<CompteUtilisateur?>(null)
    var equipe by mutableStateOf<Equipe?>(null)

    fun toJoueur(): Joueur{
        return Joueur(nom,classeType= classeType?: ClasseType(), race=race?: Race())
    }

    constructor(joueur : Joueur) {
        nom = joueur.nom
        classeType = joueur.classeType
        race = joueur.race
    }
}
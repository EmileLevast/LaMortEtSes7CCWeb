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
    var classeTypeField by mutableStateOf("")
    var raceField by mutableStateOf("")
    var compteField by mutableStateOf("")
    var mdpField by mutableStateOf("")
    var equipeField by mutableStateOf("")
}
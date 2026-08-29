package org.levast.project.viewModel

import ClasseType
import Equipe
import Joueur
import Race
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.levast.project.configuration.getApiApp
import org.levast.project.model.CompteUtilisateur
import org.levast.project.viewModel.stateviewmodel.JoueurState

class GestionJoueurViewModel : ViewModel() {

    private val apiApp = getApiApp()

    private val _uiStateAllJoueurs : MutableStateFlow<List<Joueur>> = MutableStateFlow(emptyList())
    val uiStateAllJoueurs : StateFlow<List<Joueur>> = _uiStateAllJoueurs.asStateFlow()

    private val _uiStateAllEquipes : MutableStateFlow<List<Equipe>> = MutableStateFlow(emptyList())
    val uiStateAllEquipes : StateFlow<List<Equipe>> = _uiStateAllEquipes.asStateFlow()

    private val _uiStateAllCompte : MutableStateFlow<List<CompteUtilisateur>> =
        MutableStateFlow(emptyList())
    val uiStateAllComptes : StateFlow<List<CompteUtilisateur>> = _uiStateAllCompte.asStateFlow()

    private val _uiStateAllRaces : MutableStateFlow<List<Race>> = MutableStateFlow(emptyList())
    val uiStateAllRaces : StateFlow<List<Race>> = _uiStateAllRaces.asStateFlow()

    private val _uiStateAllClasseTypes : MutableStateFlow<List<ClasseType>> =
        MutableStateFlow(emptyList())
    val uiStateAllClasseTypes : StateFlow<List<ClasseType>> = _uiStateAllClasseTypes.asStateFlow()

    fun downloadNeededData(){
        viewModelScope.launch(Dispatchers.Default) {
            launch {
                _uiStateAllJoueurs.value = apiApp.searchAllJoueur(listOf(".*"))
            }
            launch {
                _uiStateAllCompte.value = apiApp.getAllCompteUtilisateur()
            }
            launch {
                _uiStateAllEquipes.value = apiApp.searchEquipe(".*") ?: listOf()
            }
            launch {
                _uiStateAllRaces.value = apiApp.searchSomethings(Race(),".*") ?: listOf()
            }
            launch {
                _uiStateAllClasseTypes.value = apiApp.searchSomethings(ClasseType(),".*") ?: listOf()
            }
        }
    }

    fun createJoueur(joueurState: JoueurState){
        //Créer ou maj compte
            //il y'a déjà les méthode apis

    //Créer ou maj Equipe

        //Récupérer l'objet race
        //Récupérer l'objet classe
    //Créer l'objet joueur

    }


}
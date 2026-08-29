package org.levast.project.viewModel

import ClasseType
import Equipe
import Joueur
import Race
import androidx.compose.runtime.mutableStateOf
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

    private val _uiStateAllJoueurs: MutableStateFlow<List<Joueur>> = MutableStateFlow(emptyList())
    val uiStateAllJoueurs: StateFlow<List<Joueur>> = _uiStateAllJoueurs.asStateFlow()

    private val _uiStateAllEquipes: MutableStateFlow<List<Equipe>> = MutableStateFlow(emptyList())
    val uiStateAllEquipes: StateFlow<List<Equipe>> = _uiStateAllEquipes.asStateFlow()

    private val _uiStateAllCompte: MutableStateFlow<List<CompteUtilisateur>> =
        MutableStateFlow(emptyList())
    val uiStateAllComptes: StateFlow<List<CompteUtilisateur>> = _uiStateAllCompte.asStateFlow()

    private val _uiStateAllRaces: MutableStateFlow<List<Race>> = MutableStateFlow(emptyList())
    val uiStateAllRaces: StateFlow<List<Race>> = _uiStateAllRaces.asStateFlow()

    private val _uiStateAllClasseTypes: MutableStateFlow<List<ClasseType>> =
        MutableStateFlow(emptyList())
    val uiStateAllClasseTypes: StateFlow<List<ClasseType>> = _uiStateAllClasseTypes.asStateFlow()

    fun downloadNeededData() {
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
                _uiStateAllRaces.value = apiApp.searchSomethings(Race(), ".*") ?: listOf()
            }
            launch {
                _uiStateAllClasseTypes.value =
                    apiApp.searchSomethings(ClasseType(), ".*") ?: listOf()
            }
        }
    }

    fun createJoueur(joueurState: JoueurState) {

        createOrUpdateCompte(joueurState)
        createOrUpdateEquipe(joueurState)

        val race = uiStateAllRaces.value.find { it.nom == joueurState.raceField }
        val classeType = uiStateAllClasseTypes.value.find { it.nom == joueurState.classeTypeField }
        if(race != null && classeType != null){
            val caracInitial = race.carac+classeType.carac

            val joueur = Joueur(
                nom = joueurState.nom,
                caracOrigin = caracInitial,
                caracActuel = caracInitial,
                niveau = 1,
                race = race,
                classeType = classeType)

            viewModelScope.launch {
                apiApp.insertItem(joueur)
            }
        }


    }

    private fun createOrUpdateCompte(joueurState: JoueurState) {
        val existingCompte = uiStateAllComptes.value.find { it.nom == joueurState.compteField }
        //Si le nom du compte existe déjà
        if (existingCompte != null) {
            //S'il ne contient pas le nom du joueur on le mets à jour
            if (!existingCompte.roles.contains(joueurState.nom)) {
                existingCompte.addRole(joueurState.nom)
                viewModelScope.launch {
                    apiApp.updateCompteUtilisateur(existingCompte)
                }
            }
        } else {
            val compteUtilisateur = CompteUtilisateur(
                joueurState.compteField,
                joueurState.mdpField,
                listOf(joueurState.nom)
            )
            viewModelScope.launch {
                apiApp.insertCompteUtilisateur(compteUtilisateur)
            }
        }
    }

    private fun createOrUpdateEquipe(joueurState: JoueurState) {
        val existingEquipe = uiStateAllEquipes.value.find { it.nom == joueurState.equipeField }
        if (existingEquipe != null) {
            //Si l'équipe ne contient pas déjà ce personnage
            if(!existingEquipe.getMembreEquipe().contains(joueurState.nom)){
                viewModelScope.launch {
                    apiApp.updateItem(existingEquipe)
                }
            }
        } else {
            //Sinon on crée l'équipe
            val equipe = Equipe(
                joueurState.equipeField,
                joueurState.nom
            )
            viewModelScope.launch {
                apiApp.insertItem(equipe)
            }
        }
    }

    fun deleteJoueur(joueur: Joueur){
        viewModelScope.launch {
            apiApp.deleteItem(joueur)
        }
    }


}
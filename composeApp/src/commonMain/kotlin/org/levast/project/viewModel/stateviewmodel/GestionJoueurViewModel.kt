package org.levast.project.viewModel.stateviewmodel

import Joueur
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.levast.project.configuration.getApiApp

class GestionJoueurViewModel : ViewModel() {

    private val apiApp = getApiApp()

    private val _uiStateAllJoueurs : MutableStateFlow<List<Joueur>> = MutableStateFlow(emptyList())
    val uiStateAllJoueurs : StateFlow<List<Joueur>> = _uiStateAllJoueurs.asStateFlow()

    fun retrieveAllJoueurs(){
        viewModelScope.launch(Dispatchers.Default) {
            _uiStateAllJoueurs.value = apiApp.searchAllJoueur(listOf(".*"))
        }
    }
}
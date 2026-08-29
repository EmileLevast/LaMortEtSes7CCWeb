package org.levast.project.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import org.levast.project.DEBOUNCE_TIME_OUT_REQUEST_MS
import org.levast.project.configuration.injectApiApp
import org.levast.project.model.CompteUtilisateur
import org.levast.project.viewModel.stateviewmodel.CompteUtilisateurStateFlow

class CompteUtilisateurViewModel : ViewModel() {

    val apiApp = injectApiApp()

    val insertCompte = CompteUtilisateurStateFlow(
        viewModelScope,
        apiApp::insertCompteUtilisateur,
        this::getAllComptesRequest
    )
    val updateCompte = CompteUtilisateurStateFlow(
        viewModelScope,
        apiApp::updateCompteUtilisateur,
        this::getAllComptesRequest
    )
    val deleteCompte = CompteUtilisateurStateFlow(
        viewModelScope,
        apiApp::deleteCompteUtilisateur,
        this::getAllComptesRequest
    )

    private val _stateGetAllCompteUtilisateur =
        MutableSharedFlow<Boolean>() // private mutable shared flow
    private val stateGetAllCompteUtilisateur: SharedFlow<Boolean> =
        _stateGetAllCompteUtilisateur.asSharedFlow() // publicly exposed as read-only shared flow

    private val _uiStateAllComptes =
        MutableStateFlow(listOf<CompteUtilisateur>()) // private mutable shared flow
    val uiStateAllComptes: StateFlow<List<CompteUtilisateur>> =
        _uiStateAllComptes.asStateFlow() // publicly exposed as read-only shared flow

    init {
        viewModelScope.launch(Dispatchers.Default) {
            stateGetAllCompteUtilisateur.debounce(DEBOUNCE_TIME_OUT_REQUEST_MS).collect { _ ->
                _uiStateAllComptes.value = apiApp.getAllCompteUtilisateur()
            }
        }
    }

    fun getAllComptesRequest() {
        viewModelScope.launch(Dispatchers.Default) {
            _stateGetAllCompteUtilisateur.emit(true)
        }
    }

}
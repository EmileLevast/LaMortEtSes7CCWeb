package org.levast.project.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import org.levast.project.DEBOUNCE_TIME_OUT_REQUEST_MS
import org.levast.project.configuration.getApiApp
import org.levast.project.model.CompteUtilisateur

class CompteUtilisateurViewModel : ViewModel(){

    val apiApp = getApiApp()

    private val _stateInsertCompteUtilisateur = MutableSharedFlow<CompteUtilisateur>() // private mutable shared flow
    val stateInsertCompteUtilisateur : SharedFlow<CompteUtilisateur> = _stateInsertCompteUtilisateur.asSharedFlow() // publicly exposed as read-only shared flow

    private val _stateUpdateCompteUtilisateur = MutableSharedFlow<CompteUtilisateur>() // private mutable shared flow
    val stateUpdateCompteUtilisateur : SharedFlow<CompteUtilisateur> = _stateUpdateCompteUtilisateur.asSharedFlow() // publicly exposed as read-only shared flow

    private val _stateDeleteCompteUtilisateur = MutableSharedFlow<CompteUtilisateur>() // private mutable shared flow
    val stateDeleteCompteUtilisateur : SharedFlow<CompteUtilisateur> = _stateDeleteCompteUtilisateur.asSharedFlow() // publicly exposed as read-only shared flow

    private val _stateGetAllCompteUtilisateur = MutableSharedFlow<CompteUtilisateur>() // private mutable shared flow
    val stateGetAllCompteUtilisateur : SharedFlow<CompteUtilisateur> = _stateGetAllCompteUtilisateur.asSharedFlow() // publicly exposed as read-only shared flow

    private val _uiStateAllCompteUtilisateur = MutableSharedFlow<CompteUtilisateur>() // private mutable shared flow
    val uiStateAllCompteUtilisateur : SharedFlow<CompteUtilisateur> = _uiStateAllCompteUtilisateur.asSharedFlow() // publicly exposed as read-only shared flow

    init {
        viewModelScope.launch(Dispatchers.Default) {
            launch {
                stateInsertCompteUtilisateur.debounce(DEBOUNCE_TIME_OUT_REQUEST_MS).collect{ compteUtilisateur ->
                    apiApp.insertCompteUtilisateur(compteUtilisateur)
                }
            }
            launch {
                stateUpdateCompteUtilisateur.debounce(DEBOUNCE_TIME_OUT_REQUEST_MS).collect{ compteUtilisateur ->
                    apiApp.updateCompteUtilisateur(compteUtilisateur)
                }
            }
            launch {
                stateDeleteCompteUtilisateur.debounce(DEBOUNCE_TIME_OUT_REQUEST_MS).collect{ compteUtilisateur ->
                    apiApp.deleteCompteUtilisateur(compteUtilisateur)
                }
            }
            launch {
                stateGetAllCompteUtilisateur.debounce(DEBOUNCE_TIME_OUT_REQUEST_MS).collect{ _ ->
                    apiApp.getAllCompteUtilisateur()
                }
            }
        }
    }


}
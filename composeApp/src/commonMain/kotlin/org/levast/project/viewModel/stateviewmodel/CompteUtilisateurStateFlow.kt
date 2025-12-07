package org.levast.project.viewModel.stateviewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.levast.project.model.CompteUtilisateur
import org.levast.project.network.ApiApp

class CompteUtilisateurStateFlow (scope : CoroutineScope, apiApp: ApiApp) {

    private val _stateRequest = MutableSharedFlow<CompteUtilisateur>() // private mutable shared flow
    private val stateRequest : SharedFlow<CompteUtilisateur> = _stateRequest.asSharedFlow() // publicly exposed as read-only shared flow

    init {
        scope.launch {
            stateRequest.collect{ compteUtilisateur ->
            }
        }
    }

    suspend fun sendRequest(compteUtilisateur: CompteUtilisateur){
        _stateRequest.emit(compteUtilisateur)
    }
}
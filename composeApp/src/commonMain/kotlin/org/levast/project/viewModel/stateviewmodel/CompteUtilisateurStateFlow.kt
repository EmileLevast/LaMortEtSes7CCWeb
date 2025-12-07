package org.levast.project.viewModel.stateviewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import org.levast.project.DEBOUNCE_TIME_OUT_REQUEST_MS
import org.levast.project.model.CompteUtilisateur

class CompteUtilisateurStateFlow (
    private val scope: CoroutineScope,
    networkMethod: suspend (CompteUtilisateur) -> Boolean,
    refreshElements: () -> Unit,
) {

    private val _stateRequest = MutableSharedFlow<CompteUtilisateur>() // private mutable shared flow
    private val stateRequest : SharedFlow<CompteUtilisateur> = _stateRequest.asSharedFlow() // publicly exposed as read-only shared flow

    init {
        scope.launch(Dispatchers.Default) {
            stateRequest.debounce(DEBOUNCE_TIME_OUT_REQUEST_MS).collect{ compteUtilisateur ->
                networkMethod(compteUtilisateur)
                refreshElements()
            }
        }
    }

    fun sendRequest(compteUtilisateur: CompteUtilisateur){
        scope.launch(Dispatchers.Default) {
            _stateRequest.emit(compteUtilisateur)
        }
    }
}
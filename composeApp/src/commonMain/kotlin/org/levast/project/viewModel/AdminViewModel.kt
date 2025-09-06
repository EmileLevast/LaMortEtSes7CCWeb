package org.levast.project.viewModel

import IListItem
import Joueur
import androidx.lifecycle.Lifecycle.Event
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.update
import org.levast.project.viewModel.stateviewmodel.AdminModelState
import org.levast.project.viewModel.stateviewmodel.FilterAdminScreen
import org.levast.project.viewModel.stateviewmodel.FilterModelState
import org.levast.project.viewModel.stateviewmodel.FilterUser

class AdminViewModel : ViewModel(){
    // Game UI state
    private val _uiState = MutableStateFlow(AdminModelState())
    val uiState: StateFlow<AdminModelState> = _uiState.asStateFlow()

    // Game UI state
    private val _uiStateJoueur = MutableSharedFlow<Joueur>() // private mutable shared flow
    val uiStateJoueur : SharedFlow<Joueur> = _uiStateJoueur.asSharedFlow() // publicly exposed as read-only shared flow

    fun addAllToItems(vararg items : IListItem){
        updateListItems(_uiState.value.listitems+items)
    }

    fun keepPinnedItemsOnly(){
        updateListItems(_uiState.value.listitems.filter { item -> _uiState.value.listPinneditems.contains(item.nom) })
    }

    fun pinItem(itemNom:String){
        updateListPinnedItems(_uiState.value.listPinneditems+itemNom)
    }

    fun removePin(itemToRemove:String){
        updateListPinnedItems(_uiState.value.listPinneditems.filter { itemNom -> itemNom != itemToRemove })
    }

    private fun updateListItems(items: List<IListItem>){
        _uiState.update { currentState -> currentState.copy(listitems = items) }
    }

    private fun updateListPinnedItems(items: List<String>){
        _uiState.update { currentState -> currentState.copy(listPinneditems = items) }
    }

    fun changeAdminScreen(filterAdminScreen : FilterAdminScreen){
        _uiState.update { currentState -> currentState.copy(filterAdminScreen = filterAdminScreen) }
    }

    fun changeMode(isAdminModeOn : Boolean?){
        _uiState.update { currentState -> currentState.copy(isAdminModeOn = isAdminModeOn) }
    }

    fun changeIsWideScreen(isWideScreen : Boolean){
        _uiState.update { currentState -> currentState.copy(isWideScreen = isWideScreen) }
    }

    suspend fun setJoueurToUpdate(joueur: Joueur){
        _uiStateJoueur.emit(joueur)
    }
}
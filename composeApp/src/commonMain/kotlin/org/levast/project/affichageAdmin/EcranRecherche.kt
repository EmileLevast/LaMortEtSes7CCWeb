package org.levast.project.affichageAdmin

import IListItem
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.levast.project.affichageMobile.EcranListItem
import org.levast.project.configuration.getApiApp
import org.levast.project.configuration.getGraphicConstants
import org.levast.project.viewModel.AdminViewModel




@Composable
fun EcranRecherche(
    onClickItem: (IListItem) -> Unit,
    scrollStateRecherche : LazyGridState,
    adminViewModel: AdminViewModel = viewModel{ AdminViewModel() }
) {
    var nameSearched by remember { mutableStateOf("") }
    var loading by mutableStateOf(false)
    var isDetailedModeOn by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()
    val graphicsConsts = getGraphicConstants()

    val adminUiState by adminViewModel.uiState.collectAsState()

    val apiApp = getApiApp()

    val rechercheItems: () -> Unit = {
        coroutineScope.launch {
            if (!loading) {
                loading = true

                val itemsFound = withContext(Dispatchers.Default) {//dans un thread à part on maj toute l'equipe
                    apiApp.searchAnything(nameSearched)
                }
                withContext(Dispatchers.Default) {
                    adminViewModel.addAllToItems(*itemsFound.toTypedArray())
                }
                loading = false
            }
        }
    }

    //fonction pour ajouter des elements a epingler ou les enlever //true pour epingler l'element
    val togglePinnedItem: (String,Boolean) -> Unit = { nom, toPin ->
        if(toPin){
            adminViewModel.pinItem(nom)
        }else{
            adminViewModel.removePin(nom)
        }

    }

    Column(Modifier.fillMaxSize()) {
        Row(Modifier.padding(20.dp).align(Alignment.CenterHorizontally), horizontalArrangement = Arrangement.spacedBy(graphicsConsts.cellSpace)) {
            TextField(
                modifier = Modifier.onKeyEvent {
                    if (it.key == Key.Enter) {
                        rechercheItems()
                        true
                    } else {
                        false
                    }
                },
                value = nameSearched,
                onValueChange = {
                    if (it.isBlank() || (it.isNotBlank() && it.last() != '\n')) {
                        nameSearched = it
                    }
                },
            )
            Button({
                rechercheItems()
            }){
                Text("Valider")
            }
            Button({
                adminViewModel.keepPinnedItemsOnly()
            }){
                Text("Vider")
            }
            Switch(
                checked = isDetailedModeOn,
                onCheckedChange = {
                    isDetailedModeOn = it
                }
            )

        }
        EcranListItem(
            adminUiState.listitems,
            scrollStateRecherche,
            true,
            isDetailedModeOn,
            adminUiState.listPinneditems,
            togglePinnedItem,
            null,
            onSave = {}, //on a rien a sauvegarder en tant qu'admin sur l'ecran de recherche
            isWideScreen = adminUiState.isWideScreen,
            isEditModeOn = true,
            onEditModeClick = onClickItem
        )
    }
}
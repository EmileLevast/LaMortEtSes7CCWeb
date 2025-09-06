package org.levast.project.affichageMobile

import Equipe
import IListItem
import Joueur
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import getListItemFiltered
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import lamortetses7ccweb.composeapp.generated.resources.Res
import lamortetses7ccweb.composeapp.generated.resources.UnknownImage
import lamortetses7ccweb.composeapp.generated.resources.refreshSymbol
import org.jetbrains.compose.resources.painterResource
import org.levast.project.DEBOUNCE_TIME_OUT_REQUEST_MS
import org.levast.project.configuration.getApiApp
import org.levast.project.viewModel.AdminViewModel
import org.levast.project.viewModel.FilterViewModel
import org.levast.project.viewModel.stateviewmodel.FilterUser

@OptIn(FlowPreview::class)
@Composable
fun EcranJoueur(
    selectedJoueur: Joueur,
    selectedEquipe: Equipe,
    isLoadingJoueur: Boolean,
    joueurLoaded: () -> Unit,
    isRefreshedJoueur: Boolean,
    refreshJoueur: () -> Unit,
    isWideScreen:Boolean,
    filterViewModel: FilterViewModel = viewModel { FilterViewModel() },
    adminViewModel: AdminViewModel = viewModel { AdminViewModel() }
) {

    val apiApp = getApiApp()
    val coroutineScope = rememberCoroutineScope()


    //Variable pour enregistrer les equipements à afficher
    val (equipements, setEquipements) = remember { mutableStateOf<List<IListItem>>(emptyList()) }

    val scrollListState by remember { mutableStateOf(LazyGridState()) }
    var listPinnedItems by remember { mutableStateOf<List<String>>(emptyList()) }
    var mapUtilisationItems by remember { mutableStateOf(selectedJoueur.utilisationsRestantesItem.toMap()) }

    //View Model pour savoir l'écran qu'a sélectionné le joueur
    val filterUiState by filterViewModel.uiState.collectAsState()

    //Lorsqu'on clique sur un item pour l'ajouter à la liste des items sélectionnés
    //fonction pour ajouter des elements a epingler ou les enlever //true pour epingler l'element
    val togglePinnedItem: (String, Boolean) -> Unit = { nomItem, toPin ->
        if (toPin) {
            selectedJoueur.equip(nomItem)
        } else {
            selectedJoueur.unequip(nomItem)
        }
        listPinnedItems = selectedJoueur.getAllEquipmentSelectionneAsList()
        coroutineScope.launch(Dispatchers.Default) {
            adminViewModel.setJoueurToUpdate(selectedJoueur)
        }

    }

    val onSave: () -> Unit = {
        coroutineScope.launch(Dispatchers.Default) {
            adminViewModel.setJoueurToUpdate(selectedJoueur)

        }
    }

    val useItem: (IListItem, Int) -> Unit = { equipement, nbrUtilisationRestantes ->
        if (selectedJoueur.setUtilisationsItem(equipement, nbrUtilisationRestantes)) {
            coroutineScope.launch(Dispatchers.Default) {
                adminViewModel.setJoueurToUpdate(selectedJoueur)

            }
            mapUtilisationItems =
                selectedJoueur.utilisationsRestantesItem.toMap() //TODO c'est censé relancer le changement des utilisations
        }
    }

    remember {
        coroutineScope.launch(Dispatchers.Default) {
            adminViewModel.uiStateJoueur.debounce(DEBOUNCE_TIME_OUT_REQUEST_MS).collect { stateJoueur ->
                apiApp.updateJoueur(stateJoueur)
            }
        }
    }

    LaunchedEffect(selectedJoueur) {
        coroutineScope.launch {

            val updatedEquipments = withContext(Dispatchers.Default) {
                apiApp.searchAllEquipementJoueur(selectedJoueur)//on met a jour tout ses equipements
            }
            setEquipements(updatedEquipments)//on les mets sur l'ecran
            listPinnedItems = selectedJoueur.getAllEquipmentSelectionneAsList()
            joueurLoaded()
        }
    }

    Box(Modifier.fillMaxSize()) {

        /**
         * Selection des différents écrans
         */
        //si la selection c'est tout les equipements
        when (filterUiState.filterUser) {
            FilterUser.DECOUVERTES -> {
                EcranDecouverteEquipe(selectedEquipe, isRefreshedJoueur, selectedJoueur, isWideScreen)
            }//si la selection c'est l'affichage des statistiques
            FilterUser.STATISTIQUES -> {
                EcranStatistiques(selectedJoueur, isWideScreen) {
                    onSave()
                }
            }
            //sinon on considere que c'est l'affichage de tout l'equipement
            else -> {
                FilterListItem(
                    equipements,
                    scrollListState,
                    listPinnedItems,
                    filterUser = filterUiState.filterUser,
                    togglePinItem = togglePinnedItem,
                    itemsUtilisations = mapUtilisationItems,
                    onUtilisationItem = useItem,
                    isWideScreen = isWideScreen,
                )
            }
        }

        ProfileImage(selectedJoueur, isLoadingJoueur, refreshJoueur, isWideScreen)
    }

}

@Composable
fun FilterListItem(
    items: List<IListItem>,
    scrollListState: LazyGridState,
    listPinnedItems: List<String>? = null,
    filterUser: FilterUser,
    togglePinItem: (String, Boolean) -> Unit = { _: String, _: Boolean -> },
    itemsUtilisations: Map<String, Int>? = null,
    onUtilisationItem: (IListItem, Int) -> Unit,
    isWideScreen:Boolean,
) {

    EcranListItem(
        getListItemFiltered(items, filterUser, listPinnedItems),
        scrollListState,
        true,
        listPinnedItems = listPinnedItems,
        togglePinItem = togglePinItem,
        itemsUtilisations = itemsUtilisations,
        onUtilisationItem = onUtilisationItem,
        isWideScreen = isWideScreen,
    )
}

@Composable
fun ProfileImage(
    selectedJoueur: Joueur,
    isLoadingJoueur: Boolean,
    refreshJoueur: () -> Unit,
    isWideScreen: Boolean
) {
    val infiniteTransition = rememberInfiniteTransition(label = "infinite transition")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = if (isLoadingJoueur) 360f else 0f,
        animationSpec = infiniteRepeatable(tween(3000, easing = LinearEasing)),
        label = "rotate"
    )

    //Affichage de l'image et du nom de profil
    Box(Modifier.fillMaxSize()) {
        IconProfilRefreshable(
            selectedJoueur, Modifier.size(if(isWideScreen) 150.dp else 70.dp).align(Alignment.TopEnd)
                .graphicsLayer {
                    rotationZ = rotation
                }, refreshJoueur
        )
    }
}

@Composable
fun IconProfilRefreshable(
    selectedJoueur: Joueur,
    modifier: Modifier = Modifier,
    refreshJoueur: () -> Unit
) {

    val apiApp = getApiApp()


    Box(modifier.height(IntrinsicSize.Min)) {
        Box(Modifier.fillMaxSize(0.55f).align(Alignment.Center).clickable { refreshJoueur() }) {
            AsyncImage(
                model = apiApp.createUrlImageFromItem(selectedJoueur),
                modifier = Modifier.clip(CircleShape).align(Alignment.Center),
                error = painterResource(Res.drawable.UnknownImage),
                contentDescription = null
            )
        }

        Image(
            painterResource(Res.drawable.refreshSymbol),
            "refresh",
            Modifier.align(Alignment.Center),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.tertiary)
        )

    }
}


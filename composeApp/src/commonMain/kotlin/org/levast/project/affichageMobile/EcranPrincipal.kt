package org.levast.project.affichageMobile

import Equipe
import Joueur
import org.levast.project.affichage.AlertDialogChangeNetworkConfiguration
import org.levast.project.affichage.LayoutDrawerMenu
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import kotlinx.coroutines.CoroutineScope
import org.levast.project.configuration.getApiApp
import org.levast.project.configuration.getConfiguration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import lamortetses7ccweb.composeapp.generated.resources.Res
import lamortetses7ccweb.composeapp.generated.resources.UnknownImage
import lamortetses7ccweb.composeapp.generated.resources.joueurbandeau
import lamortetses7ccweb.composeapp.generated.resources.joueurmenu
import lamortetses7ccweb.composeapp.generated.resources.mjbandeau
import lamortetses7ccweb.composeapp.generated.resources.mjmenu
import model.HeadBodyShowable
import org.jetbrains.compose.resources.painterResource
import org.levast.project.affichageAdmin.EcranAdmin
import org.levast.project.configuration.IConfiguration
import org.levast.project.viewModel.AdminViewModel
import org.levast.project.viewModel.FilterViewModel
import org.levast.project.viewModel.stateviewmodel.FilterAdminScreen
import org.levast.project.viewModel.stateviewmodel.FilterModelState
import org.levast.project.viewModel.stateviewmodel.FilterUser

@Composable
fun EcranPrincipal(
    adminViewModel: AdminViewModel = viewModel { AdminViewModel() }
) {
    val apiApp = getApiApp()
    val config = getConfiguration()

    val filterViewModel: FilterViewModel = viewModel { FilterViewModel() }
    val coroutineScope = rememberCoroutineScope()
    val (equipes, setEquipes) = remember { mutableStateOf<List<Equipe>>(emptyList()) }
    val (triggerEquipe, setTriggerEquipe) = remember { mutableStateOf(false) }
    val (selectEquipe, setSelectEquipe) = remember { mutableStateOf<Equipe?>(null) }

    //Variables de sélection du Joueur actuel
    var selectedJoueur: Joueur? by remember { mutableStateOf(null) }
    var nameSavedUser: String? by remember { mutableStateOf(config.getUserName()) }

    //MENU
    var openChangeIpDialog by remember { mutableStateOf(false) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val onCloseChangeIpDialog: () -> Unit = {
        apiApp.initJsonClient()//on reinitialize le client avec les nouvelles configurations
        openChangeIpDialog = false
    }
    val filterUiState by filterViewModel.uiState.collectAsState()

    //Admin
    val adminUiState by adminViewModel.uiState.collectAsState()

    val onResetSelectJoueur: () -> Unit = {
        nameSavedUser = ""
        selectedJoueur = null
    }

    val onLaunchingDialogIp: (Boolean) -> Unit = { isOpeningIpDialog ->
        openChangeIpDialog = isOpeningIpDialog
    }


    LaunchedEffect(triggerEquipe) {

        coroutineScope.launch {
            withContext(Dispatchers.Default) {
                setEquipes(//dans un thread à part on maj toute l'equipe
                    apiApp.searchEquipe(".*") ?: listOf()
                )
            }

        }
    }

    LaunchedEffect(equipes) {
        coroutineScope.launch(Dispatchers.Default) {
            if (nameSavedUser?.isNotBlank() == true) {//S'il y'a un joueur d'enregistré
                //Alors on set automatiquement l'équipe
                setSelectEquipe(equipes.find {
                    it.getMembreEquipe().contains(nameSavedUser)
                })
            }
        }
    }

    LayoutDrawerMenu({ innerpadding ->
        Box(Modifier.fillMaxSize().padding(innerpadding)) {
            Image(
                painterResource(
                    drawBackgroundBandeau(adminUiState.isAdminModeOn, adminUiState.isWideScreen)
                ), "bandeau du mj",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
                    .graphicsLayer {
                        this.alpha = 0.6f
                    })

            if (adminUiState.filterAdminScreen != FilterAdminScreen.NONE && adminUiState.isAdminModeOn == true) {
                EcranAdmin()
            } else if (selectEquipe == null) {
                Column(
                    Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button({ setTriggerEquipe(triggerEquipe.not()) }) {
                        Text("Rafraîchissez-vous")
                    }
                    LayoutListSelectableItem(equipes) { setSelectEquipe(it) }
                }
            } else {
                Column(Modifier.fillMaxSize()) {
                    EcranChoixJoueur(selectEquipe, selectedJoueur, {
                        selectedJoueur = it
                        config.setUserName(it.nom)
                        nameSavedUser = it.nom
                    }, adminUiState.isWideScreen)
                }

            }
        }

    }, {

        /**
         * MENU
         */
        /**
         * MENU
         */
        Column {

            if (adminUiState.isAdminModeOn == true) {
                bandeauMj()
            }

            selectedJoueur?.let {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        model = apiApp.createUrlImageFromItem(it),
                        modifier = Modifier.padding(4.dp).clip(CircleShape)
                            .wrapContentWidth(Alignment.End)
                            .fillMaxWidth(0.2f)
                            .border(
                                BorderStroke(2.dp, MaterialTheme.colorScheme.secondary), CircleShape
                            ),
                        contentDescription = null,
                        error = painterResource(Res.drawable.UnknownImage),

                        )

                    Text(
                        it.nomComplet.ifBlank { it.nom },
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.tertiary
                    )

                }

            }

            LazyColumn {
                item {
                    optionsNavigationDrawer(
                        filterViewModel,
                        drawerState,
                        filterUiState,
                        config,
                        setTriggerEquipe,
                        triggerEquipe,
                        setSelectEquipe,
                        coroutineScope,
                        onLaunchingDialogIp,
                        onResetSelectJoueur,
                        selectedJoueur,
                        selectEquipe
                    )
                }

            }

        }
    }, drawerState)

    if (openChangeIpDialog) {
        AlertDialogChangeNetworkConfiguration(onCloseChangeIpDialog)
    }


}

fun drawBackgroundBandeau(
    isAdminMode: Boolean?,
    iSWideScreen: Boolean
) = if (isAdminMode == false && iSWideScreen)
    Res.drawable.joueurbandeau
else if (isAdminMode == false && !iSWideScreen)
    Res.drawable.joueurmenu
else if (isAdminMode == true && iSWideScreen)
    Res.drawable.mjbandeau
else Res.drawable.mjmenu

@Composable
private fun bandeauMj() {

    Box(Modifier.height(IntrinsicSize.Min)) {
        Image(
            painterResource(Res.drawable.mjbandeau),
            null,
            Modifier.fillMaxWidth(),
            contentScale = ContentScale.FillWidth
        )
        Text(
            "Maître du jeu",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = Color.Yellow,
            style = MaterialTheme.typography.titleMedium,
        )
    }

}

@Composable
private fun optionsNavigationDrawer(
    filterViewModel: FilterViewModel,
    drawerState: DrawerState,
    filterUiState: FilterModelState,
    config: IConfiguration,
    setTriggerEquipe: (Boolean) -> Unit,
    triggerEquipe: Boolean,
    setSelectEquipe: (Equipe?) -> Unit,
    coroutineScope: CoroutineScope,
    onLaunchingDialogIp: (Boolean) -> Unit,
    onResetSelectJoueur: () -> Unit,
    selectedJoueur: Joueur?,
    selectedEquipe: Equipe?,
    adminViewModel: AdminViewModel = viewModel { AdminViewModel() }
) {

    AdminOptions(coroutineScope, drawerState)


    if (selectedJoueur != null) {



        HorizontalDivider()

        //Le profil utilisateur
        ItemSimpleMenuButton(
            "Statistiques",
            FilterUser.STATISTIQUES,
            filterViewModel,
            drawerState,
            filterUiState,
        )
        HorizontalDivider()


        //Les catégories d'items
        ItemSimpleMenuButton(
            "Équipés",
            FilterUser.EQUIPES,

            filterViewModel,
            drawerState,
            filterUiState
        )
        ItemSimpleMenuButton(
            "Armes",
            FilterUser.ARMES,
            filterViewModel,
            drawerState,
            filterUiState
        )
        ItemSimpleMenuButton(
            "Sorts",
            FilterUser.SORTS,
            filterViewModel,
            drawerState,
            filterUiState
        )
        ItemSimpleMenuButton(
            "Armures",
            FilterUser.ARMURES,
            filterViewModel,
            drawerState,
            filterUiState
        )
        ItemSimpleMenuButton(
            "Spéciaux",
            FilterUser.SPECIAL,
            filterViewModel,
            drawerState,
            filterUiState
        )
        ItemSimpleMenuButton(
            "Boucliers",
            FilterUser.BOUCLIERS,
            filterViewModel,
            drawerState,
            filterUiState
        )
        ItemSimpleMenuButton(
            "Tous les items",
            FilterUser.TOUT_EQUIPEMENT,
            filterViewModel,
            drawerState,
            filterUiState
        )
        ItemSimpleMenuButton(
            "Découvertes",
            FilterUser.DECOUVERTES,
            filterViewModel,
            drawerState,
            filterUiState
        )

    }

    HorizontalDivider()

    if (selectedEquipe != null) {
        TextButton({
            config.setUserName("")
            setTriggerEquipe(triggerEquipe.not())
            setSelectEquipe(null)
            onResetSelectJoueur()
            coroutineScope.launch {
                drawerState.close()
            }
        }) {
            Icon(Icons.Default.Refresh, contentDescription = "Reset equipe")
            Text("Changer d'équipe")
        }
    }

    if (selectedJoueur != null) {
        TextButton({
            onResetSelectJoueur()
            coroutineScope.launch {
                drawerState.close()
            }
        }) {
            Icon(Icons.Default.Refresh, contentDescription = "Reset joueur")
            Text("Changer de joueur")
        }
    }

    TextButton({
        adminViewModel.changeMode(null)
        coroutineScope.launch {
            drawerState.close()
        }
    }) {
        Icon(Icons.Default.AccountBox, contentDescription = "Changer de mode")
        Text("Reset Mode")
    }
    TextButton({
        onLaunchingDialogIp(true)
        coroutineScope.launch {
            drawerState.close()
        }
    }) {
        Icon(Icons.Default.Warning, contentDescription = "Adresse Ip")
        Text("Maintenance")
    }
}

@Composable
private fun AdminOptions(
    coroutineScope: CoroutineScope,
    drawerState: DrawerState,
    adminViewModel: AdminViewModel = viewModel { AdminViewModel() }
) {
    val adminUiState by adminViewModel.uiState.collectAsState()

    if(adminUiState.isAdminModeOn == true){
        val onClickResearchOption: () -> Unit = {
            if (adminUiState.filterAdminScreen != FilterAdminScreen.RESEARCH) {
                adminViewModel.changeAdminScreen(FilterAdminScreen.RESEARCH)
            } else {
                adminViewModel.changeAdminScreen(FilterAdminScreen.NONE)
            }
            coroutineScope.launch {
                drawerState.close()
            }
        }

        if (adminUiState.filterAdminScreen == FilterAdminScreen.RESEARCH) {
            OutlinedButton(onClickResearchOption) {
                ContentOptionButtonResearch()
            }
        } else {
            TextButton(onClickResearchOption) {
                ContentOptionButtonResearch()
            }
        }
    }


}

@Composable
private fun ContentOptionButtonResearch() {
    Icon(Icons.Default.Search, contentDescription = "Rechercher item")
    Text("Rechercher")
}

@Composable
fun <T : HeadBodyShowable> LayoutListSelectableItem(
    elementsAfficher: List<T>,
    onSelectElement: (T) -> Unit
) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        items(elementsAfficher) {
            Card(Modifier.padding(15.dp).clickable { onSelectElement(it) }) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(10.dp)
                ) {
                    Text(
                        it.getHead(),
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                    Text(
                        it.getBody(),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun ItemSimpleMenuButton(
    text: String,
    filter: FilterUser,
    filterViewModel: FilterViewModel,
    drawerState: DrawerState,
    filterUiState: FilterModelState,
    adminViewModel: AdminViewModel = viewModel { AdminViewModel() }
) {
    val scope = rememberCoroutineScope()
    val adminUiState by adminViewModel.uiState.collectAsState()

    if (filterUiState.filterUser == filter && adminUiState.filterAdminScreen == FilterAdminScreen.NONE) {
        OutlinedButton({
            filterViewModel.changeFilterUser(filter)
            scope.launch {
                drawerState.close()
            }
        }) {
            Text(text)
        }
    } else {
        TextButton({
            filterViewModel.changeFilterUser(filter)
            adminViewModel.changeAdminScreen(FilterAdminScreen.NONE)
            scope.launch {
                drawerState.close()
            }
        }) {
            Text(text)
        }
    }

}
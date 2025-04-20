package org.levast.project.affichageAdmin

import ApiableItem
import CHAR_SEP_EQUIPEMENT
import Equipe
import IListItem
import Joueur
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.levast.project.configuration.getApiApp
import org.levast.project.configuration.getGraphicConstants

@Composable
fun layoutEdition(
    itemToEdit: IListItem,
    backClick: (Boolean) -> Unit
) {
    var show by remember { mutableStateOf(false) }
    val graphicsConsts = getGraphicConstants()

    val deparsedAttributs = itemToEdit.getDeparsedAttributes()
    val parsingRulesAttributs = itemToEdit.getParsingRulesAttributesAsList()
    val apiApp = getApiApp()
    val coroutineScope = rememberCoroutineScope()
    val listAttributs = remember {
        mutableStateListOf<String>().apply {
            addAll(deparsedAttributs)
        }
    }

    var listeEquipes by remember { mutableStateOf<List<Equipe>>(emptyList()) }
    var listeJoueurs by remember { mutableStateOf<List<Joueur>>(emptyList()) }

    var message by remember { mutableStateOf<String?>(null) }
    var openAlertDialogDeletion by remember { mutableStateOf(false) }

    //pour sizer l'image selon la taille du titre
    var RowHeightDp by remember {
        mutableStateOf(0.dp)
    }
    // Get local density from composable
    val localDensity = LocalDensity.current

    LaunchedEffect(message) {
        if (message != null) {
            show = true
            delay(5000)
            message = null
            show = false
        }
    }

    remember {
        coroutineScope.launch {
            listeEquipes = apiApp.searchEquipe(".*") ?: listOf()
            listeJoueurs = apiApp.searchJoueur(".*") ?: listOf()
        }
    }




    Box {

        Row(Modifier.fillMaxSize()) {

            Column(
                Modifier.fillMaxHeight().weight(2f),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Row {
                    Text(
                        modifier = Modifier.onGloballyPositioned { coordinates ->
                            // Set column height using the LayoutCoordinates
                            RowHeightDp = with(localDensity) { coordinates.size.height.toDp() }
                        },
                        text = itemToEdit.nomComplet.ifBlank { itemToEdit.nom },
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge,
                    )
                }



                Box (Modifier.fillMaxWidth()){

                    Row(horizontalArrangement = Arrangement.Start) {
                        IconButton(onClick = {
                            backClick(true)
                        })
                        {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Retour")
                        }
                    }

                    Row(Modifier.align(Alignment.Center),horizontalArrangement = Arrangement.spacedBy(graphicsConsts.cellSpace)) {
                        FilledTonalButton( {
                            val itemParsed = (itemToEdit as ApiableItem).parseFromString(listAttributs)
                            coroutineScope.launch(Dispatchers.Default) {
                                val res = apiApp.updateItem(itemParsed)
                                withContext(Dispatchers.Default) {
                                    message = if (res) {
                                        "${itemParsed.nom} majed"
                                    } else {
                                        "${itemParsed.nom} - erreur mise à jour"
                                    }
                                }
                            }
                        }){
                            Text("Mise à jour")
                        }
                        FilledTonalButton( {
                            val itemParsed = (itemToEdit as ApiableItem).parseFromString(listAttributs)
                            coroutineScope.launch(Dispatchers.Default) {
                                val res = apiApp.insertItem(itemParsed)
                                withContext(Dispatchers.Default) {
                                    message = if (res) {
                                        "${itemParsed.nom} créé"
                                    } else {
                                        "${itemParsed.nom} - erreur création"
                                    }
                                }
                            }
                        }){
                            Text("Créer")
                        }
                        FilledTonalButton( {
                            openAlertDialogDeletion = true
                        }){
                            Text("Supprimer")
                        }
                    }
                }

                //Pour chaque règle de formatage
                LazyColumn(
                    Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    itemsIndexed(parsingRulesAttributs) { index, parsingRules ->
                        if (listAttributs.size > index) {
                            TextField(
                                value = listAttributs[index],
                                onValueChange = { listAttributs[index] = it },
                                label = { Text(parsingRules) }
                            )
                        } else {
                            TextField(
                                value = "ERROR - NO VALUE",
                                onValueChange = { },
                            )
                        }
                    }
                }


            }
            Column(
                Modifier.fillMaxHeight().weight(1f),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                layoutListeSelectables(
                    Modifier,

                    listeEquipes,
                    { equipe -> (equipe as Equipe).getDecouvertes().contains(itemToEdit.nom) },
                    { isSelect, equipe ->
                        val equipeToUpdate = equipe as? Equipe
                        if (equipeToUpdate != null) {
                            if (isSelect) {
                                equipeToUpdate.chaineDecouvertSerialisee += CHAR_SEP_EQUIPEMENT + itemToEdit.nom + CHAR_SEP_EQUIPEMENT
                            } else {
                                equipeToUpdate.chaineDecouvertSerialisee =
                                    equipeToUpdate.chaineDecouvertSerialisee.replace(
                                        "${CHAR_SEP_EQUIPEMENT}${itemToEdit.nom}$CHAR_SEP_EQUIPEMENT",
                                        ""
                                    )
                            }
                            coroutineScope.launch(Dispatchers.Default) {
                                apiApp.updateItem(equipeToUpdate)
                            }
                        }
                    },
                    graphicsConsts.brushEquipe
                )
                layoutListeSelectables(
                    Modifier,
                    listeJoueurs,
                    { joueur ->
                        (joueur as Joueur).getAllEquipmentAsList().contains(itemToEdit.nom)
                    },
                    { isSelect, joueur ->
                        val joueurToUpdate = joueur as? Joueur
                        if (joueurToUpdate != null) {
                            if (isSelect) {
                                joueurToUpdate.chaineEquipementSerialisee += CHAR_SEP_EQUIPEMENT + itemToEdit.nom + CHAR_SEP_EQUIPEMENT
                            } else {
                                joueurToUpdate.chaineEquipementSerialisee =
                                    joueurToUpdate.chaineEquipementSerialisee.replace(
                                        "${CHAR_SEP_EQUIPEMENT}${itemToEdit.nom}$CHAR_SEP_EQUIPEMENT",
                                        ""
                                    )
                            }
                            coroutineScope.launch(Dispatchers.Default) {
                                apiApp.updateItem(joueurToUpdate)
                            }
                        }
                    },
                    graphicsConsts.brushJoueursCard
                )
            }
        }
        if (show && message != null) {
            Card(
                modifier = Modifier.align(Alignment.CenterStart),
                colors = CardDefaults.elevatedCardColors( if(message!!.contains("erreur")) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.primaryContainer),
            ) {
                Text(
                    text = message!!, color = if(message!!.contains("erreur")) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
        if (openAlertDialogDeletion) {
            val itemParsed = (itemToEdit as ApiableItem).parseFromString(listAttributs)

            AlertDialog(
                title = { Text("Supprimer ${itemParsed.nom}") },
                onDismissRequest = { openAlertDialogDeletion = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            openAlertDialogDeletion = false
                            coroutineScope.launch(Dispatchers.Default) {
                                val res = apiApp.deleteItem(itemParsed)
                                withContext(Dispatchers.Default) {
                                    message = if (res) {
                                        "${itemParsed.nom} suppression"
                                    } else {
                                        "${itemParsed.nom} - erreur suppression"
                                    }
                                }
                            }
                        }
                    ) {
                        Text("Supprimer")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            openAlertDialogDeletion = false
                        }
                    ) {
                        Text("Annuler")
                    }
                }
            )
        }
    }
}

@Composable
fun layoutListeSelectables(
    modifier: Modifier,
    listSelectables: List<IListItem>,
    isDefaultChecked: (IListItem) -> Boolean,
    onSelect: (Boolean, IListItem) -> Unit,
    brushToUse: Brush
) {
    val graphicsConsts = getGraphicConstants()

    LazyVerticalGrid(
        modifier = Modifier.then(modifier),
        columns = GridCells.Adaptive(100.dp),
        verticalArrangement = Arrangement.spacedBy(graphicsConsts.cellSpace),
        horizontalArrangement = Arrangement.spacedBy(graphicsConsts.cellSpace),

        ) {
        items(listSelectables) {
            Card(
                modifier = Modifier.width(IntrinsicSize.Min)
            ) {
                var checked by remember { mutableStateOf(isDefaultChecked(it)) }
                Column(
                    Modifier.width(IntrinsicSize.Min).background(brushToUse),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = it.nomComplet.ifBlank { it.nom }, color = Color.White,
                        style = MaterialTheme.typography.bodySmall,
                    )
                    Checkbox(
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color.Black
                        ),
                        checked = checked,
                        onCheckedChange = { isSelected ->
                            checked = isSelected
                            onSelect(isSelected, it)
                        },
                    )
                }
            }
        }
    }
}




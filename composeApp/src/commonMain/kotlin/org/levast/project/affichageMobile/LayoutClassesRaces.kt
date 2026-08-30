package org.levast.project.affichageMobile

import Equipe
import IListItem
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import org.levast.project.affichage.layoutBigImage
import org.levast.project.viewModel.GestionJoueurViewModel

@Composable
fun LayoutClassesRace(
    isWideScreen: Boolean,
    goToPreviousScreen: () -> Unit,
    gestionJoueurViewModel: GestionJoueurViewModel = viewModel { GestionJoueurViewModel() }
){
    val allRaces by gestionJoueurViewModel.uiStateAllRaces.collectAsState()
    val allClasseTypes by gestionJoueurViewModel.uiStateAllClasseTypes.collectAsState()
    var itemToShow by remember { mutableStateOf<IListItem?>(null) }

    LaunchedEffect(Unit) {
        gestionJoueurViewModel.downloadNeededData()
    }

    IconButton(onClick = {
        if(itemToShow == null){
            goToPreviousScreen()
        }else{
            itemToShow = null
        }
    })
    {
        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Retour")
    }

    if(itemToShow!= null){
        layoutBigImage(
            itemToShow!!,
            {_,_ -> itemToShow=  null},
            false,
            null,
            null,
            isWideScreen
        )
    }

    Row(Modifier.fillMaxSize()) {
        LayoutListSelectableItem (allRaces, Modifier.weight(1f)){itemToShow = it}
        LayoutListSelectableItem(allClasseTypes, Modifier.weight(1f)) {}
    }



}
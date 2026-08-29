package org.levast.project.affichageMobile

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
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import org.levast.project.viewModel.GestionJoueurViewModel

@Composable
fun LayoutClassesRace(
    goToPreviousScreen: () -> Unit,
    gestionJoueurViewModel: GestionJoueurViewModel = viewModel { GestionJoueurViewModel() }
){

    LaunchedEffect(Unit) {
        gestionJoueurViewModel.downloadNeededData()
    }

    val allRaces by gestionJoueurViewModel.uiStateAllRaces.collectAsState()
    val allClasseTypes by gestionJoueurViewModel.uiStateAllClasseTypes.collectAsState()

    IconButton(onClick = {
        goToPreviousScreen()
    })
    {
        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Retour")
    }

    Row(Modifier.fillMaxSize()) {
        LayoutListSelectableItem (allRaces, Modifier.weight(1f)){}
        LayoutListSelectableItem(allClasseTypes, Modifier.weight(1f)) {}
    }
}
package org.levast.project.affichageMobile.gestionUtilisateur

import Joueur
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.levast.project.viewModel.GestionJoueurViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import org.levast.project.model.CompteUtilisateur
import org.levast.project.viewModel.stateviewmodel.JoueurState

@Composable
fun EcranGestionJoueur(
    gestionJoueurViewModel: GestionJoueurViewModel = viewModel { GestionJoueurViewModel() }
) {
    val allJoueurs by gestionJoueurViewModel.uiStateAllJoueurs.collectAsState()
    val allEquipes by gestionJoueurViewModel.uiStateAllEquipes.collectAsState()
    val allCompte by gestionJoueurViewModel.uiStateAllComptes.collectAsState()
    val allRaces by gestionJoueurViewModel.uiStateAllRaces.collectAsState()
    val allClasseTypes by gestionJoueurViewModel.uiStateAllClasseTypes.collectAsState()
    var joueurCreating by remember { mutableStateOf<JoueurState?>(null) }


    //TODO je viens de créer le viewModel de joueurs ici, il faudrait l'utiliser aussi au niveau de l'écran principal pour ne download les joueurs qu'nue fois
    LaunchedEffect(Unit) {
        gestionJoueurViewModel.downloadNeededData()
    }

    if (joueurCreating == null) {
        LayouShowAllJoueurs(
            {joueurCreating = JoueurState()},
            allJoueurs,
            {
                gestionJoueurViewModel.deleteJoueur(it)
            }
        ) {
            gestionJoueurViewModel.downloadNeededData()
        }
    }else{
        LayoutCreationJoueur(
            joueurCreating!!,
            gestionJoueurViewModel::createJoueur,
            allEquipes,
            allCompte,
            allRaces,
            allClasseTypes
        ){
            joueurCreating = null
        }
    }

}

@Composable
fun LayouShowAllJoueurs(
    onJoueurCreating: (Joueur) -> Unit,
    allJoueurs: List<Joueur>,
    deleteJoueur: (Joueur) -> Unit,
    refreshAllJoueurs: () -> Unit
) {

    var isShowingDeleteDialog by remember { mutableStateOf<Joueur?>(null) }

    Column(Modifier.fillMaxSize()) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Button(
                onClick = {
                        onJoueurCreating(Joueur())
                }
            ) {
                Text("Nouveau joueur")
            }

            IconButton(onClick = {
                refreshAllJoueurs()
            })
            {
                Icon(Icons.Rounded.Refresh, "recharger les joueurs")
            }
        }
        LazyVerticalGrid(
            modifier = Modifier.weight(1f),
            columns = GridCells.Adaptive(minSize = 220.dp)
        ) {
            items(allJoueurs) {
                Card(modifier = Modifier.padding(2.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                        Text(
                            it.nom,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = {
                            isShowingDeleteDialog = it
                        })
                        {
                            Icon(Icons.Rounded.Delete, "Supprimer Joueur")
                        }
                    }

                    Text(it.getStatsAsStrings())
                }
            }
        }

        DeletionDialog(isShowingDeleteDialog?.nom ?: "Erreur", isShowingDeleteDialog != null, { isShowingDeleteDialog = null }) {
            isShowingDeleteDialog?.let{deleteJoueur(it)}
        }
    }
}
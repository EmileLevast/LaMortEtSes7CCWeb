package org.levast.project.affichageMobile.gestionUtilisateur

import Joueur
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
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
import org.levast.project.viewModel.stateviewmodel.GestionJoueurViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import org.levast.project.model.CompteUtilisateur

@Composable
fun EcranGestionJoueur(
    gestionJoueurViewModel: GestionJoueurViewModel = viewModel { GestionJoueurViewModel() }
) {
    val allJoueurs by gestionJoueurViewModel.uiStateAllJoueurs.collectAsState()
    var (isCreatingJoueur, setCreatingJoueur) = remember { mutableStateOf<Joueur?>(null) }


    //TODO je viens de créer le viewModel de joueurs ici, il faudrait l'utiliser aussi au niveau de l'écran principal pour ne download les joueurs qu'nue fois
    LaunchedEffect(Unit) {
        gestionJoueurViewModel.retrieveAllJoueurs()
    }

    if (isCreatingJoueur == null) {
        LayouShowAllJoueurs(
            isCreatingJoueur,
            setCreatingJoueur,
            allJoueurs
        ) {
            gestionJoueurViewModel.retrieveAllJoueurs()
        }
    }

}

@Composable
fun LayouShowAllJoueurs(
    isCreatingJoueur: Joueur?,
    setCreatingJoueur: (Joueur) -> Unit,
    allJoueurs: List<Joueur>,
    refreshAllJoueurs: () -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        Row(Modifier.fillMaxWidth()) {
            Button(
                onClick = {
                    if (isCreatingJoueur == null) {
                        setCreatingJoueur(Joueur())
                    }
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
                    Text(
                        it.nom,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(it.getStatsAsStrings())
                }
            }
        }
    }
}
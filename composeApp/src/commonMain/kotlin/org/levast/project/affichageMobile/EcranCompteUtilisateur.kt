package org.levast.project.affichageMobile

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.levast.project.viewModel.CompteUtilisateurViewModel

@Composable
fun EcranCompteUtilisateur(
    compteUtilisateurViewModel: CompteUtilisateurViewModel = viewModel { CompteUtilisateurViewModel() }
) {

    //FIXME là y'a un soucis d'archi, on utilise une variable pour declencher le launchedEffect qui lui meme declenche pour declencher une variable, faut raccroucir la chaine
    var getAllCompteUtilisateur by remember { mutableStateOf(true) }
    val compteUtilisateurUiState by compteUtilisateurViewModel.uiStateAllComptes.collectAsState()

    LaunchedEffect(getAllCompteUtilisateur) {
        compteUtilisateurViewModel.getAllComptesRequest()
    }

    LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 128.dp)) {
        items(compteUtilisateurUiState) {
            Card{
                Text(it.nom, fontWeight = FontWeight.Bold )
                Text(it.motDePasse)
            }
        }
    }
}
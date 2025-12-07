package org.levast.project.affichageMobile

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.levast.project.model.CompteUtilisateur
import org.levast.project.viewModel.CompteUtilisateurViewModel

@Composable
fun EcranCompteUtilisateur(
    compteUtilisateurViewModel: CompteUtilisateurViewModel = viewModel { CompteUtilisateurViewModel() }
) {

    var isShowingDeleteDialog by remember { mutableStateOf<CompteUtilisateur?>(null) }
    var isShowingUpdateCard by remember { mutableStateOf<CompteUtilisateur?>(null) }
    var isShowingInsertCard by remember { mutableStateOf<CompteUtilisateur?>(null) }
    val compteUtilisateurUiState by compteUtilisateurViewModel.uiStateAllComptes.collectAsState()

    remember {
        compteUtilisateurViewModel.getAllComptesRequest()
    }

    LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 128.dp)) {
        items(compteUtilisateurUiState) {
            Card {
                Text(it.nom, fontWeight = FontWeight.Bold)
                Text(it.motDePasse)
            }
        }
    }

    isShowingDeleteDialog?.let { compte ->
        AlertDialog(
            title = { Text("Supprimer ${compte.nom}") },
            onDismissRequest = { isShowingDeleteDialog = null },
            confirmButton = {
                TextButton(
                    onClick = {
                        compteUtilisateurViewModel.deleteCompte.sendRequest(compte)
                        isShowingDeleteDialog = null
                    }
                ) {
                    Text("Supprimer")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        isShowingDeleteDialog = null
                    }
                ) {
                    Text("Annuler")
                }
            }
        )
    }
}
package org.levast.project.affichageMobile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
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

    LaunchedEffect(Unit) {
        compteUtilisateurViewModel.getAllComptesRequest()
    }

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

        Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Button(
                    onClick = {
                        if (isShowingInsertCard == null) {
                            isShowingInsertCard = CompteUtilisateur("", "", listOf())
                        }
                    }
                ) {
                    Text("Nouveau compte")
                }

                IconButton(onClick = {
                    compteUtilisateurViewModel.getAllComptesRequest()
                })
                {
                    Icon(Icons.Rounded.Refresh, "recharger les comptes")
                }
            }


            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize(),
                columns = GridCells.Adaptive(minSize = 220.dp),
            ) {
                items(compteUtilisateurUiState) {

                    Card(modifier = Modifier.padding(2.dp), onClick = { isShowingUpdateCard = it }) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                            Text(
                                it.nom,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f).padding(2.dp)
                            )

                            IconButton(onClick = {
                                isShowingDeleteDialog = it
                            })
                            {
                                Icon(Icons.Rounded.Delete, "Supprimer compte")
                            }
                        }

                        Text(it.motDePasse)
                        Text(it.roles.joinToString(","), fontStyle = FontStyle.Italic)
                    }
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

        isShowingUpdateCard?.let {
            CardCompteUtilisateur(
                "Maj",
                it,
                confirm = {
                    compteUtilisateurViewModel.updateCompte.sendRequest(it)
                    isShowingUpdateCard = null
                },
                cancel = {
                    isShowingUpdateCard = null
                }
            )
        }

        isShowingInsertCard?.let {
            CardCompteUtilisateur(
                "Création",
                it,
                confirm = {
                    compteUtilisateurViewModel.insertCompte.sendRequest(it)
                    isShowingInsertCard = null
                }
            ) {
                isShowingInsertCard = null
            }
        }
    }

}

@Composable
fun CardCompteUtilisateur(
    titreCard: String,
    compteUtilisateur: CompteUtilisateur,
    confirm: (CompteUtilisateur) -> Unit,
    cancel: () -> Unit
) {

    var compteToSend by remember {
        mutableStateOf<CompteUtilisateur>(
            compteUtilisateur
        )
    }

    Column(
        Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.padding(10.dp).width(IntrinsicSize.Min),
            colors = CardDefaults.cardColors()
                .copy(containerColor = MaterialTheme.colorScheme.secondaryContainer),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
        ) {
            Text(
                titreCard,
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(7.dp)
            )
            TextField(
                value = compteToSend.nom,
                onValueChange = { compteToSend = compteToSend.copy(nom = it) },
                label = { Text("Nom d'utilisateur") })
            TextField(
                value = compteToSend.motDePasse,
                onValueChange = { compteToSend = compteToSend.copy(motDePasse = it) },
                label = { Text("Mot de passe") })
            TextField(
                value = compteToSend.roles.joinToString(","),
                onValueChange = { compteToSend = compteToSend.copy(roles = it.split(",")) },
                label = { Text("Roles (séparés par des virgules)", fontStyle = FontStyle.Italic) },
            )
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                TextButton(onClick = {
                    cancel()
                }) {
                    Text("Annuler")
                }
                TextButton(onClick = {
                    confirm(compteToSend)
                }) {
                    Text("Valider")
                }

            }
        }
    }

}
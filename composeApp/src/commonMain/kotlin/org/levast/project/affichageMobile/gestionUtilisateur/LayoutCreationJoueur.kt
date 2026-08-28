package org.levast.project.affichageMobile.gestionUtilisateur

import ClasseType
import Equipe
import Race
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.levast.project.model.CompteUtilisateur
import org.levast.project.viewModel.stateviewmodel.JoueurState

@Composable
fun LayoutCreationJoueur(
    joueurState: JoueurState,
    equipes: List<Equipe>,
    comptes: List<CompteUtilisateur>,
    races : List<Race>,
    classeTypes : List<ClasseType>,
) {

    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        TextField(
            label = {
                Text("Nom du joueur")
            },
            value = joueurState.nom,
            onValueChange = {
                joueurState.nom = it
            })

        MinimalDropdownMenu(races.map { it.nom }, { joueurState.raceField = it }) {
            Text(
                text = joueurState.raceField,
            )
        }

        MinimalDropdownMenu(classeTypes.map { it.nom }, { joueurState.classeTypeField = it }) {

            Text(
                text = joueurState.classeTypeField,
            )
        }

        MinimalDropdownMenu(equipes.map { it.nom }, { joueurState.equipeField = it }) {
            TextField(
                label = {
                    Text("equipe")
                },
                value = joueurState.equipeField,
                onValueChange = {
                    joueurState.equipeField = it
                })
        }

        MinimalDropdownMenu(comptes.map { it.nom }, { joueurState.compteField = it }) {
            TextField(
                label = {
                    Text("compte")
                },
                value = joueurState.compteField,
                onValueChange = {
                    joueurState.compteField = it
                })
        }

        //Si le compte n'existe pas dans les comptes existants
        if (!comptes.map { it.nom }.contains(joueurState.compteField)) {
            //alors on demande le mot de passe
            TextField(
                label = {
                    Text("mot de passe")
                },
                value = joueurState.mdpField,
                onValueChange = {
                    joueurState.mdpField = it
                })
        }
    }
}

@Composable
fun MinimalDropdownMenu(options:List<String>, onClickOption : (String) -> Unit,content: @Composable () -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically){
            content()
            IconButton(onClick = { expanded = !expanded }) {
                Icon(Icons.Default.MoreVert, contentDescription = "More options")
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach {
                DropdownMenuItem(
                    text = { Text(it) },
                    onClick = {
                        onClickOption(it)
                    expanded = false
                    }
                )
            }
        }
    }
}
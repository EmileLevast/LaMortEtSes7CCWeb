package org.levast.project.affichageMobile.gestionUtilisateur

import Equipe
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import org.levast.project.model.CompteUtilisateur
import org.levast.project.viewModel.stateviewmodel.JoueurState

@Composable
fun LayoutCreationJoueur(joueurState: JoueurState, equipes: List<Equipe>, comptes: List<CompteUtilisateur>) {

    TextField(
        label = {
            Text("Nom du joueur")
        },
        value = joueurState.nom,
        onValueChange = {
            joueurState.nom = it
        })

    TextField(
        label = {
            Text("race")
        },
        value = joueurState.raceField,
        onValueChange = {
            joueurState.raceField = it
        })

    TextField(
        label = {
            Text("classe")
        },
        value = joueurState.classeTypeField,
        onValueChange = {
            joueurState.classeTypeField = it
        })

    TextField(
        label = {
            Text("equipe")
        },
        value = joueurState.equipeField,
        onValueChange = {
            joueurState.equipeField = it
        })

    TextField(
        label = {
            Text("compte")
        },
        value = joueurState.compteField,
        onValueChange = {
            joueurState.compteField = it
        })

    //Si le compte n'existe pas dans les comptes existants
    if(!comptes.map { it.nom }.contains(joueurState.compteField)){
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
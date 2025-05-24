package org.levast.project.affichageMobile

import Equipe
import IListItem
import Joueur
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.levast.project.configuration.getApiApp


@Composable
fun EcranDecouverteEquipe(
    selectedEquipe: Equipe,
    refreshDecouvertes: Boolean,
    joueur: Joueur,
    isWideScreen: Boolean
) {
    val (decouvertesEquipe, setDecouvertesEquipe) = remember {
        mutableStateOf<List<IListItem>>(
            emptyList()
        )
    }

    val apiApp = getApiApp()
    val scrollListState by remember { mutableStateOf(LazyGridState()) }


    LaunchedEffect(refreshDecouvertes, selectedEquipe) {
        val updatedEquipes =
            withContext(Dispatchers.Default) {//dans un thread à part on maj toute l'equipe
                apiApp.searchEquipe(selectedEquipe.nom)
            }
        val updatedDecouvertes =
            withContext(Dispatchers.Default) {//dans un thread à part on recherche toutes les decouvertes de l'equipe
                apiApp.searchAllDecouvertesEquipe(updatedEquipes?.firstOrNull() ?: selectedEquipe)
            }

        setDecouvertesEquipe(updatedDecouvertes)//on les mets sur l'ecran
    }

    EcranListItem(
        decouvertesEquipe,
        scrollListState,
        false,
        isWideScreen = isWideScreen,
        joueur = joueur,
    )

}
package org.levast.project.affichageAdmin

import IListItem
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun EcranAdmin(
) {
    var selectedItemToEdit by remember { mutableStateOf<IListItem?>(null) }
    val scrollStateRecherche = rememberLazyGridState()


    val onClickItem = { itemClicked: IListItem, _:Int ->
        selectedItemToEdit = itemClicked
    }

    val onClickBackFromEdition = { _: Boolean ->
        selectedItemToEdit = null
    }

    if (selectedItemToEdit != null) {
        layoutEdition(selectedItemToEdit!!, onClickBackFromEdition)
    } else {
        EcranRecherche(onClickItem, scrollStateRecherche)
    }
}
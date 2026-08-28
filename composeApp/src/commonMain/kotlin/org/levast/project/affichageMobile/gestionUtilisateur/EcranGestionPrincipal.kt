package org.levast.project.affichageMobile.gestionUtilisateur

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun EcranGestionPrincipal(
) {
    var selectedTab by remember { mutableStateOf(UtilisateurTab.JOUEUR) }

    Scaffold { contentPadding ->
        Column {
            SecondaryTabRow(selectedTab.ordinal, modifier = Modifier.padding(contentPadding)) {
                UtilisateurTab.entries.forEach {
                    Tab(selected = it == selectedTab, onClick = {
                        selectedTab = it
                    }, text = {
                        Text(it.label)
                    })
                }
            }
            if (selectedTab == UtilisateurTab.JOUEUR) {
                EcranGestionJoueur()
            } else {
                EcranCompteUtilisateur()
            }
        }

    }

}

enum class UtilisateurTab(val label: String) {
    JOUEUR("Joueurs"),
    COMPTE("Comptes")
}

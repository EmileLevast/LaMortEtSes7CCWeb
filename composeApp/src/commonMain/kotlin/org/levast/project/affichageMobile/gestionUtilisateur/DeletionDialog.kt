package org.levast.project.affichageMobile.gestionUtilisateur

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun DeletionDialog(nomASupprimer: String, show: Boolean, hide: () -> Unit, onValidation:()->Unit) {
    if (show) {
        AlertDialog(
            title = { Text("Supprimer $nomASupprimer") },
            onDismissRequest = { hide() },
            confirmButton = {
                TextButton(
                    onClick = {
                        onValidation()
                        hide()
                    }
                ) {
                    Text("Supprimer")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        hide()
                    }
                ) {
                    Text("Annuler")
                }
            }
        )
    }
}
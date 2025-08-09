package org.levast.project.affichage

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import org.levast.project.configuration.getConfiguration


@Composable
fun AlertDialogChangeIp(
    onDismissRequest: ()->Unit
) {
    val config = getConfiguration()

    var ipAdressInput by remember { mutableStateOf(config.getAdressTargetServer()) }

    AlertDialog(
        title = {
            Text(text = "ChangeIp")
        },
        text = {
            TextField(
                value = ipAdressInput,
                onValueChange = { ipAdressInput = it },
                label = { Text("ip") }
            )
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    config.setadressTargetServer(ipAdressInput)
                    onDismissRequest()
                }
            ) {
                Text("Confirmer")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Annuler")
            }
        }
    )
}

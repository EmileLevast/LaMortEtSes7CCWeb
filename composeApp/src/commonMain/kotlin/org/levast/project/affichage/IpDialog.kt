package org.levast.project.affichage

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import org.levast.project.DNS_ADRESS_SERVER
import org.levast.project.configuration.UserAuthentication
import org.levast.project.configuration.getConfiguration


@Composable
fun AlertDialogChangeNetworkConfiguration(
    onDismissRequest: () -> Unit
) {
    val config = getConfiguration()

    var ipAdressInput by remember { mutableStateOf(config.getAdressTargetServer()) }

    var username by remember { mutableStateOf(config.getUserAuthentication()?.userName ?: "") }
    var password by remember { mutableStateOf(config.getUserAuthentication()?.password ?: "") }

    val (isDNSOptionSelected, setDNSOptionSelected) = remember { mutableStateOf(config.getAdressTargetServer() == DNS_ADRESS_SERVER) }

    AlertDialog(
        title = {
            Text(text = "ChangeIp")
        },
        text = {

            Column(Modifier.selectableGroup()) {
                Row{
                    TextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Nom d'utilisateur") })
                    TextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Mot de passe") })
                }
                Row(
                    Modifier.selectable(
                        selected = (isDNSOptionSelected),
                        onClick = {
                            setDNSOptionSelected(true)
                        },
                        role = Role.RadioButton
                    )
                ) {
                    RadioButton(
                        selected = (isDNSOptionSelected),
                        onClick = null // null recommended for accessibility with screen readers
                    )
                    Text(DNS_ADRESS_SERVER)
                }
                Row(
                    Modifier.selectable(
                        selected = (!isDNSOptionSelected),
                        onClick = { setDNSOptionSelected(false) },
                        role = Role.RadioButton
                    )
                ) {
                    RadioButton(
                        selected = (!isDNSOptionSelected),
                        onClick = null // null recommended for accessibility with screen readers
                    )
                    TextField(
                        value = ipAdressInput,
                        onValueChange = { ipAdressInput = it },
                        label = { Text("ip") }
                    )
                }

            }


        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    //on mets l'adresse du DNS si elle selectionnee sinon on mets celle indiquée par l'utilisateur
                    config.setadressTargetServer(if(isDNSOptionSelected) DNS_ADRESS_SERVER else ipAdressInput)
                    //si l'utilisateur choisis de pointer vers le DNS alors on mets le protocole HTTPS sinon HTTP
                    config.setHttpsMode(isDNSOptionSelected)

                    config.setUserAuthentication(UserAuthentication(username, password))

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

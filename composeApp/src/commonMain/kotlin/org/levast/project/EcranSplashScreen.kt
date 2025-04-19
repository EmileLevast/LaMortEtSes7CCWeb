package org.levast.project

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import org.levast.project.affichageMobile.EcranPrincipal
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.levast.project.affichageAdmin.EcranAccueilAdmin
import org.levast.project.configuration.getConfiguration

@Composable
fun EcranSplashScreen(){

    val configuration = getConfiguration()

    var isModeUser:Boolean? by remember { mutableStateOf(configuration.getMode()) }

    if(isModeUser == null){
        Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceEvenly, horizontalAlignment = Alignment.CenterHorizontally){
            Button({
                isModeUser = false
                configuration.setMode(isModeUser!!)
            }){
                Text("MJ")
            }
            Button({
                isModeUser = true
                configuration.setMode(isModeUser!!)
            }){
                Text("Joueur")
            }
        }
    }else if(isModeUser!!){
        EcranPrincipal()
    }else{
        EcranAccueilAdmin()
    }
}
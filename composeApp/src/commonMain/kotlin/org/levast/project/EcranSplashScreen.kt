package org.levast.project

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import org.levast.project.affichageMobile.EcranPrincipal
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.levast.project.affichageAdmin.EcranAccueilAdmin

@Composable
fun EcranSplashScreen(){

    var isModeUser:Boolean? by remember { mutableStateOf(null) }

    if(isModeUser == null){
        Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceEvenly, horizontalAlignment = Alignment.CenterHorizontally){
            Button({
                isModeUser = false
            }){
                Text("MJ")
            }
            Button({
                isModeUser = false
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
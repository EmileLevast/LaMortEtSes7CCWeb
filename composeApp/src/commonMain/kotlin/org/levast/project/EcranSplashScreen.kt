package org.levast.project

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import org.levast.project.affichageMobile.EcranPrincipal
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import lamortetses7ccweb.composeapp.generated.resources.Res
import lamortetses7ccweb.composeapp.generated.resources.joueurbandeau
import lamortetses7ccweb.composeapp.generated.resources.joueurmenu
import lamortetses7ccweb.composeapp.generated.resources.mjbandeau
import lamortetses7ccweb.composeapp.generated.resources.mjmenu
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.levast.project.configuration.getConfiguration

@Composable
fun EcranSplashScreen() {

    val configuration = getConfiguration()

    //pour sizer l'image selon la taille du titre
    var iSWideScreen by remember {
        mutableStateOf(false)
    }
    // Get local density from composable
    val localDensity = LocalDensity.current

    var isModeUser: Boolean? by remember { mutableStateOf(configuration.getMode()) }

    val onChangeMode: (Boolean?) -> Unit = { updatedMode ->
        isModeUser = updatedMode
        configuration.setMode(updatedMode)
    }
    Column(
        Modifier.fillMaxSize().onGloballyPositioned { coordinates ->
            iSWideScreen =
                with(localDensity) { coordinates.size.width.toDp() > 500.dp }
        }
    ) {
        if (isModeUser == null) {
            Box(Modifier.weight(1f).fillMaxHeight(0.5f), contentAlignment = Alignment.Center) {
                imageBandeau(
                    if (iSWideScreen) Res.drawable.joueurbandeau else Res.drawable.joueurmenu,
                    Modifier
                )
                Button({
                    isModeUser = true
                    configuration.setMode(isModeUser!!)
                }) {
                    Text("Joueur")
                }
            }
            Box(Modifier.weight(1f).fillMaxHeight(0.5f), contentAlignment = Alignment.Center) {
                imageBandeau(
                    if (iSWideScreen) Res.drawable.mjbandeau else Res.drawable.mjmenu,
                    Modifier.rotate(180f)
                )
                Button({
                    isModeUser = false
                    configuration.setMode(isModeUser!!)
                }) {
                    Text("MJ")
                }
            }

        } else {
            EcranPrincipal(isModeUser, onChangeMode, iSWideScreen)
        }
    }
}

@Composable
fun imageBandeau(image: DrawableResource, modifier: Modifier) {
    Image(
        painterResource(image), "bandeau",
        contentScale = ContentScale.FillBounds,
        modifier = modifier.fillMaxSize()
            .graphicsLayer {
                this.alpha = 0.65f
            })
}
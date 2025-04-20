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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import lamortetses7ccweb.composeapp.generated.resources.Res
import lamortetses7ccweb.composeapp.generated.resources.joueurbandeau
import lamortetses7ccweb.composeapp.generated.resources.joueurmenu
import lamortetses7ccweb.composeapp.generated.resources.mjbandeau
import lamortetses7ccweb.composeapp.generated.resources.mjmenu
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.levast.project.configuration.getConfiguration
import org.levast.project.viewModel.AdminViewModel

@Composable
fun EcranSplashScreen(
    adminViewModel: AdminViewModel = viewModel { AdminViewModel() }
) {

    val configuration = getConfiguration()
    val adminUiState by adminViewModel.uiState.collectAsState()


    // Get local density from composable
    val localDensity = LocalDensity.current


    val onChangeMode: (Boolean?) -> Unit = { isAdminModeOn ->
        adminViewModel.changeMode(isAdminModeOn)
    }
    Column(
        Modifier.fillMaxSize().onGloballyPositioned { coordinates ->
            adminViewModel.changeIsWideScreen(
                with(localDensity) { coordinates.size.width.toDp() > 500.dp })
        }
    ) {
        if (adminUiState.isAdminModeOn == null) {
            Box(Modifier.weight(1f).fillMaxHeight(0.5f), contentAlignment = Alignment.Center) {
                imageBandeau(
                    if (adminUiState.isWideScreen) Res.drawable.joueurbandeau else Res.drawable.joueurmenu,
                    Modifier
                )
                Button({
                    adminViewModel.changeMode(false)
                }) {
                    Text("Joueur")
                }
            }
            Box(Modifier.weight(1f).fillMaxHeight(0.5f), contentAlignment = Alignment.Center) {
                imageBandeau(
                    if (adminUiState.isWideScreen) Res.drawable.mjbandeau else Res.drawable.mjmenu,
                    Modifier.rotate(180f)
                )
                Button({
                    adminViewModel.changeMode(true)
                }) {
                    Text("MJ")
                }
            }

        } else {
            EcranPrincipal()
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
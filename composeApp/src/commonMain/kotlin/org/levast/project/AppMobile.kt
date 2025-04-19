import org.levast.project.affichageMobile.EcranPrincipal
import androidx.compose.runtime.Composable
import org.levast.project.configuration.getConfiguration

@Composable
fun AppMobile(){

    getConfiguration()
    EcranPrincipal()
}


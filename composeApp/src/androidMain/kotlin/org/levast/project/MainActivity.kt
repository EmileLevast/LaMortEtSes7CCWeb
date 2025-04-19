package org.levast.project

import App
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import org.levast.project.configuration.ConfigurationImpl
import org.levast.project.configuration.getConfiguration

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            (getConfiguration() as ConfigurationImpl).setupContextForPreferences(LocalContext.current)
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
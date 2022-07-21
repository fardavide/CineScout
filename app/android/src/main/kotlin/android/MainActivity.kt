package android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.compose.rememberNavController
import cinescout.design.Destination
import cinescout.design.theme.CineScoutTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            CineScoutTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    App(onFinish = this::finish)
                }
            }
        }
    }
}

@Composable
private fun App(onFinish: () -> Unit) {
    val navController = rememberNavController()
    val onBack = { navController.popOrFinish(onFinish) }
}

private fun pop(destination: Destination) = NavOptions.Builder()
    .setPopUpTo(destination.id, inclusive = true)
    .build()

private fun NavController.popOrFinish(onFinish: () -> Unit) {
    if (popBackStack().not()) onFinish()
}

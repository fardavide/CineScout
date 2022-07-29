package cinescout.android

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.compose.rememberNavController
import cinescout.auth.tmdb.domain.usecase.NotifyTmdbAppAuthorized
import cinescout.design.Destination
import cinescout.design.theme.CineScoutTheme
import cinescout.home.presentation.ui.HomeScreen
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val notifyTmdbAppAuthorized: NotifyTmdbAppAuthorized by inject()

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

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent?.dataString == "cinescout://tmdb") {
            lifecycleScope.launchWhenCreated {
                notifyTmdbAppAuthorized()
            }
        }
    }
}

@Composable
private fun App(onFinish: () -> Unit) {
    val navController = rememberNavController()
    val onBack = { navController.popOrFinish(onFinish) }
    HomeScreen()
}

private fun pop(destination: Destination) = NavOptions.Builder()
    .setPopUpTo(destination.id, inclusive = true)
    .build()

private fun NavController.popOrFinish(onFinish: () -> Unit) {
    if (popBackStack().not()) onFinish()
}

@Composable
@Preview(showBackground = true)
private fun AppPreview() {
    CineScoutTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            App(onFinish = {})
        }
    }
}

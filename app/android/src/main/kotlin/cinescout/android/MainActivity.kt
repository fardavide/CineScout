package cinescout.android

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import cinescout.auth.domain.model.TraktAuthorizationCode
import cinescout.auth.domain.usecase.NotifyTraktAppAuthorized
import cinescout.design.theme.CineScoutTheme
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val notifyTraktAppAuthorized: NotifyTraktAppAuthorized by inject()

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
        val dataString = intent?.dataString
        if (dataString != null) {
            if ("cinescout://trakt" in dataString) {
                lifecycleScope.launch {
                    val code = TraktAuthorizationCode(dataString.substringAfter("code="))
                    notifyTraktAppAuthorized(code)
                }
            }
        }
    }
}

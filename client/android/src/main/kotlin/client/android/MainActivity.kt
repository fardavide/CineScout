@file:Suppress("PackageDirectoryMismatch", "UnusedImport") // IDE will remove collect
package studio.forface.cinescout

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.setContent
import androidx.lifecycle.lifecycleScope
import client.Navigator
import client.ViewState
import client.android.ui.CineScoutApp
import co.touchlab.kermit.Logger
import entities.TmdbOauthCallback
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val navigator by inject<Navigator>()
    private val logger by inject<Logger>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CineScoutApp(getKoin())
        }

        lifecycleScope.launchWhenCreated {
            navigator.screen.collect {
                if (it == ViewState.None) {
                    navigator.toHome()
                    super.onBackPressed()
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val approved = intent?.getStringExtra(TMDB_OAUTH_EXTRA) != null
        // TODO handle token approved
    }

    override fun onBackPressed() {
        navigator.back()
    }

    companion object {
        const val TMDB_OAUTH_EXTRA = "tmdb_oauth"
    }
}

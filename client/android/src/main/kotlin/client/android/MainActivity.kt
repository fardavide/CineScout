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
        val uri = intent!!.data
        logger.i(uri.toString(), "onNewIntent")

        if (uri != null && uri.toString().startsWith(TmdbOauthCallback)) {
            val oauthVerifier: String = uri.getQueryParameter("oauth_verifier")!!
            logger.i(oauthVerifier, "oauthVerifier")
        }
    }

    override fun onBackPressed() {
        navigator.back()
    }
}

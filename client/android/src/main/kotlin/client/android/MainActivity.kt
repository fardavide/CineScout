@file:Suppress("PackageDirectoryMismatch")
package studio.forface.cinescout

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.setContent
import androidx.lifecycle.lifecycleScope
import client.ViewState
import client.android.AndroidNavigator
import client.android.CineScoutApp
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val navigator by inject<AndroidNavigator>()

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

    override fun onBackPressed() {
        navigator.back()
    }
}

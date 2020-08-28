@file:Suppress("PackageDirectoryMismatch", "UnusedImport") // IDE will remove collect
package studio.forface.cinescout

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.setContent
import androidx.lifecycle.lifecycleScope
import client.Navigator
import client.ViewState
import client.android.ui.CineScoutApp
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.android.inject
import kotlinx.coroutines.flow.collect

class MainActivity : AppCompatActivity() {

    private val navigator by inject<Navigator>()

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

@file:Suppress("PackageDirectoryMismatch")
package studio.forface.cinescout

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.setContent
import client.android.CineScoutApp
import org.koin.android.ext.android.getKoin

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CineScoutApp(getKoin())
        }
    }

    // TODO
    //  override fun onBackPressed() {
    //      if (!navigationViewModel.onBack()) {
    //          super.onBackPressed()
    //      }
    //  }
}

package cinescout.utils.compose

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

class OpenUrlHandler internal constructor(private val context: Context) {

    fun open(url: String) {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }
}

@Composable
fun rememberOpenUrlHandler(): OpenUrlHandler {
    val context = LocalContext.current
    return remember(context) { OpenUrlHandler(context) }
}

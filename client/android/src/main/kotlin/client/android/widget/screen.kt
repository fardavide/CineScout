package client.android.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.vectorResource
import client.resource.StringResources
import client.resource.Strings
import studio.forface.cinescout.R

/**
 * A screen that show an error icon with a message, using the given [message] or [StringResources.GenericError]
 */
@Composable
fun ErrorScreen(message: String? = null) {
    MessageScreen {
        ErrorMessage(message)
    }
}

/**
 * A screen that show a loading message, using [StringResources.LoadingMessage]
 */
@Composable
fun LoadingScreen() {
    MessageScreen {
        LoadingMessage()
    }
}

@Composable
fun MessageScreen(content: @Composable () -> Unit) {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        content()
    }
}

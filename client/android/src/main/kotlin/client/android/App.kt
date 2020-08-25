package client.android

import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import client.android.theme.CineScoutTheme
import client.data
import org.koin.core.Koin

@Composable
fun CineScoutApp(koin: Koin) {
    CineScoutTheme {
        AppContent(navigator = koin.get())
    }
}

@Composable
private fun AppContent(navigator: AndroidNavigator) {

    Crossfade(current = navigator.screen.data) {

    }

   // Crossfade(navigationViewModel.currentScreen) { screen ->
   //     Surface(color = MaterialTheme.colors.background) {
   //         when (screen) {
   //             is Screen.Home -> HomeScreen(
   //                 navigateTo = navigationViewModel::navigateTo,
   //                 postsRepository = postsRepository
   //             )
   //             is Screen.Interests -> InterestsScreen(
   //                 navigateTo = navigationViewModel::navigateTo,
   //                 interestsRepository = interestsRepository
   //             )
   //             is Screen.Article -> ArticleScreen(
   //                 postId = screen.postId,
   //                 postsRepository = postsRepository,
   //                 onBack = { navigationViewModel.onBack() }
   //             )
   //         }
   //     }
   // }
}

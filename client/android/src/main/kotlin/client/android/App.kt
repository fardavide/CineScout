package client.android

import androidx.compose.runtime.Composable
import client.android.theme.CineScoutTheme

@Composable
fun CineScoutApp() {
    CineScoutTheme {
        AppContent()
    }
}

@Composable
private fun AppContent() {
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

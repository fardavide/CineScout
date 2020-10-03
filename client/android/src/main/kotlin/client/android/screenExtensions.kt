package client.android

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.VectorAsset
import androidx.compose.ui.res.vectorResource
import client.Screen
import client.resource.Strings
import studio.forface.cinescout.R
import util.unsupported

val Screen.title: String
    get() = when (this) {
        Screen.Home -> Strings.AppName
        is Screen.MovieDetails -> movie.name.s
        Screen.Search -> Strings.SearchAction
        Screen.Suggestions -> Strings.SuggestionsAction
        Screen.Watchlist -> Strings.WatchlistAction
    }

@Composable
val Screen.icon: VectorAsset
    get() {
        val id = when (this) {
            Screen.Home -> R.drawable.ic_3d_glasses_color
            is Screen.MovieDetails -> unsupported
            Screen.Search -> R.drawable.ic_search_color
            Screen.Suggestions -> R.drawable.ic_diamond_color
            Screen.Watchlist -> R.drawable.ic_bookmark_color
        }
        return vectorResource(id)
    }

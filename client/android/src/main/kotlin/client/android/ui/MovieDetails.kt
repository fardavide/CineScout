package client.android.ui

import androidx.compose.foundation.Box
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import client.android.widget.CenteredText
import client.resource.Strings
import dev.chrisbanes.accompanist.coil.CoilImageWithCrossfade
import entities.Poster
import entities.movies.Movie

@Composable
fun MovieDetails(movie: Movie) {

    Column(
        Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalGravity = Alignment.CenterHorizontally
    ) {

        MoviePoster(poster = movie.poster)
        MovieTitle(title = movie.name.s)
        MovieBody(
            genres = movie.genres.joinToString { it.name.s },
            actors = movie.actors.take(5).joinToString { it.name.s })
    }
}

@Composable private fun MoviePoster(poster: Poster?) {
    poster ?: return

    Box(Modifier.fillMaxWidth(0.5f)) {
        CoilImageWithCrossfade(data = poster.get(Poster.Size.Original))
    }
}

@Composable private fun MovieTitle(title: String) {

    CenteredText(style = MaterialTheme.typography.h4, text = title)
}

@Composable
private fun MovieBody(genres: String, actors: String) {

    Row {
        Column {

            Text(text = Strings.GenresTitle)
            Text(style = MaterialTheme.typography.h6, text = genres)
        }
    }

    Row {
        Column {

            Text(text = Strings.ActorsTitle)
            Text(style = MaterialTheme.typography.h6, textAlign = TextAlign.Center, text = actors)
        }
    }
}

package client.android.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import client.android.ui.MovieBody
import client.android.widget.CenteredText
import dev.chrisbanes.accompanist.coil.CoilImage
import entities.Poster
import entities.movies.Movie

@Composable
fun MovieList(movies: List<Movie>, toMovieDetails: (Movie) -> Unit) {

    LazyColumnFor(contentPadding = PaddingValues(top = 8.dp, bottom = 8.dp), items = movies) { movie ->
        MovieItem(movie = movie, toMovieDetails = toMovieDetails)
    }
}

@Composable
private fun MovieItem(movie: Movie, toMovieDetails: (Movie) -> Unit) {

    Box(Modifier.padding(horizontal = 32.dp)) {

        Row(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .clip(MaterialTheme.shapes.large)
                .clickable(onClick = { toMovieDetails(movie) })
        ) {

            Poster(poster = movie.poster)
            Column(Modifier.padding(start = 16.dp)) {

                CenteredText(text = movie.name.s, style = MaterialTheme.typography.h6)
                MovieBody(
                    genres = movie.genres.joinToString { it.name.s },
                    actors = movie.actors.take(3).joinToString { it.name.s },
                    textStyle = MaterialTheme.typography.subtitle1
                )
            }
        }

        Divider()
    }
}

@Composable
private fun Poster(poster: Poster?) {

    CoilImage(
        modifier = Modifier.height(156.dp).clip(MaterialTheme.shapes.small),
        data = poster?.get(Poster.Size.W500) ?: "",
    )
}

@Composable
private fun Divider() {
    Row(
        Modifier
        .fillMaxWidth()
        .height(1.dp)
        .background(MaterialTheme.colors.onBackground.copy(alpha = 0.1f))
    ) {}
}

package client.android.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Box
import androidx.compose.foundation.Icon
import androidx.compose.foundation.Image
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import client.android.Get
import client.android.theme.default
import client.android.widget.CenteredText
import client.resource.Strings
import client.viewModel.RateMovieViewModel
import dev.chrisbanes.accompanist.coil.CoilImageWithCrossfade
import entities.Poster
import entities.movies.Movie
import studio.forface.cinescout.R

@Composable
fun MovieDetails(buildViewModel: Get<RateMovieViewModel>, movie: Movie, onBack: () -> Unit) {

    val scope = rememberCoroutineScope()
    val viewModel = remember(movie) {
        buildViewModel(scope)
    }

    MainScaffold(
        topBar = { TopBar(movie.name.s) },
        bottomBar = { BottomBar { IconButton(onClick = onBack) { Icon(Icons.default.ArrowBack) } } },
        floatingActionButton = {
            ExtendedFloatingActionButton(text = { Text(text = Strings.RateMovieAction) }, onClick = {})
        },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true
    ) {

        Column(
            Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp),
            horizontalGravity = Alignment.CenterHorizontally
        ) {

            MoviePoster(poster = movie.poster)
            MovieTitle(title = movie.name.s)
            MovieBody(
                genres = movie.genres.joinToString { it.name.s },
                actors = movie.actors.take(5).joinToString { it.name.s }
            )
//        RateBar(onLike = { viewModel[movie] = Rating.Positive }, onDislike = { viewModel[movie] = Rating.Negative })
        }
    }
}

@Composable private fun MoviePoster(poster: Poster?) {
    poster ?: return

    Box(Modifier.fillMaxHeight(0.3f).clip(MaterialTheme.shapes.medium)) {
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

@Composable
private fun RateBar(onLike: () -> Unit, onDislike: () -> Unit) {

    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {

        RateButton(color = Color.Red, imageId = R.drawable.ic_dislike_bw, onClick = onDislike)
        RateButton(color = Color.Green, imageId = R.drawable.ic_like_bw, onClick = onLike)
    }
}

@Composable
private fun RateButton(color: Color, @DrawableRes imageId: Int, onClick: () -> Unit) {

    FloatingActionButton(
        modifier = Modifier.padding(16.dp),
        backgroundColor = color,
        onClick = onClick
    ) {
        Image(
            modifier = Modifier.size(48.dp),
            colorFilter = ColorFilter(Color.White, BlendMode.SrcIn),
            asset = vectorResource(id = imageId)
        )
    }
}

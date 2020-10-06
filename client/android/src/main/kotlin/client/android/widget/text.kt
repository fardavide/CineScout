package client.android.widget

import androidx.compose.foundation.Text
import androidx.compose.foundation.currentTextStyle
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyleRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import entities.movies.Movie

/**
 * A [Text] widget that fill the max width and align the text in the center
 */
@Composable
fun CenteredText(modifier: Modifier = Modifier, text: String, style: TextStyle = currentTextStyle()) {
    Text(modifier = modifier.fillMaxWidth(), textAlign = TextAlign.Center, style = style, text = text)
}

/**
 * A [Text] widget that fill the max width and align the text in the center
 */
@Composable
fun CenteredText(modifier: Modifier = Modifier, text: AnnotatedString, style: TextStyle = currentTextStyle()) {
    Text(modifier = modifier.fillMaxWidth(), textAlign = TextAlign.Center, style = style, text = text)
}

/**
 * Show a [CenteredText] with the given [movie]'s name and year, using a different [TextStyle] for each
 */
@Composable
fun MovieTitle(movie: Movie, titleTextStyle: TextStyle, yearTextStyle: TextStyle, centered: Boolean = true) {
    val movieName = movie.name.s
    val text = "$movieName ${movie.year}"
    val title = AnnotatedString(
        text = text,
        spanStyles = listOf(
            SpanStyleRange(titleTextStyle.toSpanStyle(), 0, movieName.length),
            SpanStyleRange(yearTextStyle.toSpanStyle(), movieName.length + 1, text.length),
        )
    )
    if (centered) CenteredText(text = title)
    else Text(text = title)
}

package cinescout.lists.presentation.mapper

import cinescout.lists.presentation.model.ListItemUiModel
import cinescout.media.domain.model.TmdbPosterImage
import cinescout.movies.domain.model.MovieWithPersonalRating
import cinescout.rating.domain.model.ScreenplayWithPersonalRating
import cinescout.screenplay.domain.model.Screenplay
import org.koin.core.annotation.Factory

@Factory
internal class ListItemUiModelMapper {

    fun toUiModel(screenplay: Screenplay) = ListItemUiModel(
        personalRating = null,
        rating = screenplay.rating.average.value.toString(),
        title = screenplay.title,
        tmdbId = screenplay.tmdbId
    )

    fun toUiModel(screenplayWithRating: ScreenplayWithPersonalRating) = ListItemUiModel(
        personalRating = screenplayWithRating.personalRating.value.toString(),
        rating = screenplayWithRating.screenplay.rating.average.value.toString(),
        title = screenplayWithRating.screenplay.title,
        tmdbId = screenplayWithRating.screenplay.tmdbId
    )

    fun toUiModel(movieWithPersonalRating: MovieWithPersonalRating) = ListItemUiModel.Movie(
        personalRating = movieWithPersonalRating.personalRating.value.toString(),
        posterUrl = movieWithPersonalRating.movie.posterImage.orNull()?.getUrl(TmdbPosterImage.Size.MEDIUM),
        rating = movieWithPersonalRating.movie.rating.average.value.toString(),
        title = movieWithPersonalRating.movie.title,
        tmdbId = movieWithPersonalRating.movie.tmdbId
    )
}

package cinescout.lists.presentation.mapper

import cinescout.lists.presentation.model.ListItemUiModel
import cinescout.movies.domain.model.MovieWithPersonalRating
import cinescout.movies.domain.model.TmdbPosterImage

class ListItemUiModelMapper {

    fun toUiModel(movieWithPersonalRating: MovieWithPersonalRating) = ListItemUiModel(
        personalRating = movieWithPersonalRating.personalRating.value.toString(),
        posterUrl = movieWithPersonalRating.movie.posterImage.orNull()?.getUrl(TmdbPosterImage.Size.MEDIUM),
        rating = movieWithPersonalRating.movie.rating.average.value.toString(),
        title = movieWithPersonalRating.movie.title,
        tmdbId = movieWithPersonalRating.movie.tmdbId
    )
}

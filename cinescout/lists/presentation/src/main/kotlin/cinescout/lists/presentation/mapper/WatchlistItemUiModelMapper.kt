package cinescout.lists.presentation.mapper

import cinescout.lists.presentation.model.WatchlistItemUiModel
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.TmdbPosterImage

class WatchlistItemUiModelMapper {

    fun toUiModel(movie: Movie) = WatchlistItemUiModel(
        posterUrl = movie.posterImage.orNull()?.getUrl(TmdbPosterImage.Size.MEDIUM),
        title = movie.title,
        tmdbId = movie.tmdbId
    )
}

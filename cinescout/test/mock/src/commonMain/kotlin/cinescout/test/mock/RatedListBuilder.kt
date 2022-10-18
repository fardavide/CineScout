package cinescout.test.mock

import arrow.core.Validated
import cinescout.common.model.Rating
import cinescout.common.model.getOrThrow
import cinescout.movies.domain.model.Movie
import cinescout.tvshows.domain.model.TvShow

@MockAppBuilderDsl
class RatedListBuilder internal constructor() {

    internal var movies: Map<Movie, Rating> = emptyMap()
        private set

    internal var tvShows: Map<TvShow, Rating> = emptyMap()
        private set

    fun movie(movie: Movie, rating: Validated<Double, Rating>) {
        movies = movies + (movie to rating.getOrThrow())
    }

    fun tvShow(tvShow: TvShow, rating: Validated<Double, Rating>) {
        tvShows = tvShows + (tvShow to rating.getOrThrow())
    }
}

package cinescout.test.mock.builder

import arrow.core.Validated
import cinescout.screenplay.domain.model.Movie
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.getOrThrow
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

package cinescout.test.mock

import cinescout.movies.domain.model.Movie
import cinescout.tvshows.domain.model.TvShow

@MockAppBuilderDsl
class ListBuilder internal constructor() {

    internal var movies: List<Movie> = emptyList()
        private set

    internal var tvShows: List<TvShow> = emptyList()
        private set

    fun movie(movie: Movie) {
        movies = movies + movie
    }

    fun tvShow(tvShow: TvShow) {
        tvShows = tvShows + tvShow
    }
}

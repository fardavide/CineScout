package cinescout.test.mock.builder

import cinescout.suggestions.domain.model.SuggestedMovie
import cinescout.suggestions.domain.model.SuggestedTvShow

@MockAppBuilderDsl
class ForYouBuilder internal constructor() {

    internal var movies: List<SuggestedMovie> = emptyList()
        private set
    internal var tvShows: List<SuggestedTvShow> = emptyList()
        private set

    fun movie(movie: SuggestedMovie) {
        movies = movies + movie
    }

    fun tvShow(tvShow: SuggestedTvShow) {
        tvShows = tvShows + tvShow
    }
}

package cinescout.test.mock

import cinescout.movies.domain.model.Movie

@MockAppBuilderDsl
class ForYouBuilder internal constructor() {

    internal var items: List<Movie> = emptyList()
        private set

    fun item(movie: Movie) {
        items = items + movie
    }
}

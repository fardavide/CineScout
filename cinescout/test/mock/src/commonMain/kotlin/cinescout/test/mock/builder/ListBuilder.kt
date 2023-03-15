package cinescout.test.mock.builder

import cinescout.screenplay.domain.model.Movie
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.TvShow

@MockAppBuilderDsl
class ListBuilder internal constructor() {

    internal var list: List<Screenplay> = emptyList()
        private set

    fun movie(movie: Movie) {
        list = list + movie
    }

    fun tvShow(tvShow: TvShow) {
        list = list + tvShow
    }
}

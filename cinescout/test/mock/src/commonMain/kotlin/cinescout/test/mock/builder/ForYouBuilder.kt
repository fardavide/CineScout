package cinescout.test.mock.builder

import cinescout.suggestions.domain.model.SuggestedMovie
import cinescout.suggestions.domain.model.SuggestedScreenplay
import cinescout.suggestions.domain.model.SuggestedTvShow

@MockAppBuilderDsl
class ForYouBuilder internal constructor() {

    internal var list: List<SuggestedScreenplay> = emptyList()
        private set

    fun movie(movie: SuggestedMovie) {
        list = list + movie
    }

    fun tvShow(tvShow: SuggestedTvShow) {
        list = list + tvShow
    }
}

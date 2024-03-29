package cinescout.test.mock.builder

import arrow.core.Either
import cinescout.screenplay.domain.model.Movie
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.TvShow
import cinescout.screenplay.domain.model.getOrThrow

@MockAppBuilderDsl
class RatedListBuilder internal constructor() {

    internal var map: Map<Screenplay, Rating> = emptyMap()
        private set

    fun movie(movie: Movie, rating: Either<Double, Rating>) {
        map = map + (movie to rating.getOrThrow())
    }

    fun tvShow(tvShow: TvShow, rating: Either<Double, Rating>) {
        map = map + (tvShow to rating.getOrThrow())
    }
}

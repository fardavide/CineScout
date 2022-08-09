package cinescout.movies.domain.testdata

import cinescout.movies.domain.model.DiscoverMoviesParams
import cinescout.movies.domain.model.ReleaseYear

object DiscoverMoviesParamsTestData {

    val Random = DiscoverMoviesParams(
        genre = GenreTestData.Action,
        releaseYear = ReleaseYear(2020)
    )
}

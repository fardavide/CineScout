package cinescout.movies.domain.testdata

import arrow.core.some
import cinescout.movies.domain.model.DiscoverMoviesParams
import cinescout.movies.domain.model.ReleaseYear

object DiscoverMoviesParamsTestData {

    val FromInception = DiscoverMoviesParams(
        castMember = MovieCreditsTestData.Inception.cast.first().some(),
        crewMember = MovieCreditsTestData.Inception.crew.first().some(),
        genre = MovieWithDetailsTestData.Inception.genres.first().some(),
        keyword = MovieKeywordsTestData.Inception.keywords.first().some(),
        releaseYear = ReleaseYear(MovieTestData.Inception.releaseDate.year).some()
    )
}

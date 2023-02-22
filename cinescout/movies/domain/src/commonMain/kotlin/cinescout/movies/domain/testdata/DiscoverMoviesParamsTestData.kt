package cinescout.movies.domain.testdata

import arrow.core.some
import cinescout.movies.domain.model.DiscoverMoviesParams
import cinescout.movies.domain.model.ReleaseYear
import cinescout.movies.domain.sample.MovieSample
import cinescout.movies.domain.sample.MovieWithDetailsSample

object DiscoverMoviesParamsTestData {

    val FromInception = DiscoverMoviesParams(
        castMember = MovieCreditsTestData.Inception.cast.first().some(),
        crewMember = MovieCreditsTestData.Inception.crew.first().some(),
        genre = MovieWithDetailsSample.Inception.genres.first().some(),
        keyword = MovieKeywordsTestData.Inception.keywords.first().some(),
        releaseYear = MovieSample.Inception.releaseDate.map { ReleaseYear(it.year) }
    )
}

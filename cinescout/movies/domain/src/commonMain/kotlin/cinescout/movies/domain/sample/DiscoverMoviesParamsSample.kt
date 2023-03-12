package cinescout.movies.domain.sample

import arrow.core.some
import cinescout.movies.domain.model.DiscoverMoviesParams
import cinescout.movies.domain.model.ReleaseYear
import cinescout.screenplay.domain.sample.ScreenplaySample

object DiscoverMoviesParamsSample {

    val FromInception = DiscoverMoviesParams(
        castMember = MovieCreditsSample.Inception.cast.first().some(),
        crewMember = MovieCreditsSample.Inception.crew.first().some(),
        genre = MovieWithDetailsSample.Inception.genres.first().some(),
        keyword = MovieKeywordsSample.Inception.keywords.first().some(),
        releaseYear = ScreenplaySample.Inception.releaseDate.map { ReleaseYear(it.year) }
    )
}

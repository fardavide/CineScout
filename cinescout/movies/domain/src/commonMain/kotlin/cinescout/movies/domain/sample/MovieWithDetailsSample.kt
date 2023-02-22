package cinescout.movies.domain.sample

import cinescout.common.testdata.GenreTestData
import cinescout.movies.domain.model.MovieWithDetails

object MovieWithDetailsSample {

    val Inception = MovieWithDetails(
        movie = MovieSample.Inception,
        genres = listOf(GenreTestData.Action, GenreTestData.Adventure, GenreTestData.ScienceFiction)
    )

    val TheWolfOfWallStreet = MovieWithDetails(
        movie = MovieSample.TheWolfOfWallStreet,
        genres = listOf(GenreTestData.Action, GenreTestData.Crime, GenreTestData.Drama)
    )

    val War = MovieWithDetails(
        movie = MovieSample.War,
        genres = listOf(GenreTestData.Action, GenreTestData.Adventure, GenreTestData.ScienceFiction)
    )
}

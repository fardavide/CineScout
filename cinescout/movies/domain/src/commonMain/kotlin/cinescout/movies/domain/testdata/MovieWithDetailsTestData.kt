package cinescout.movies.domain.testdata

import cinescout.common.testdata.GenreTestData
import cinescout.movies.domain.model.MovieWithDetails
import cinescout.movies.domain.sample.MovieSample

object MovieWithDetailsTestData {

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

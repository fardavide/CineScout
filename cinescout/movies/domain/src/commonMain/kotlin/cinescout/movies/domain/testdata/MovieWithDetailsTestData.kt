package cinescout.movies.domain.testdata

import cinescout.common.testdata.GenreTestData
import cinescout.movies.domain.model.MovieWithDetails

object MovieWithDetailsTestData {

    val Inception = MovieWithDetails(
        movie = MovieTestData.Inception,
        genres = listOf(GenreTestData.Action, GenreTestData.Adventure, GenreTestData.ScienceFiction)
    )

    val TheWolfOfWallStreet = MovieWithDetails(
        movie = MovieTestData.TheWolfOfWallStreet,
        genres = listOf(GenreTestData.Action, GenreTestData.Crime, GenreTestData.Drama)
    )

    val War = MovieWithDetails(
        movie = MovieTestData.War,
        genres = listOf(GenreTestData.Action, GenreTestData.Adventure, GenreTestData.ScienceFiction)
    )
}

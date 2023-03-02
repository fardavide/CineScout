package cinescout.movies.domain.sample

import cinescout.movies.domain.model.MovieWithDetails
import cinescout.screenplay.domain.sample.GenreSample

object MovieWithDetailsSample {

    val Inception = MovieWithDetails(
        movie = MovieSample.Inception,
        genres = listOf(GenreSample.Action, GenreSample.Adventure, GenreSample.ScienceFiction)
    )

    val TheWolfOfWallStreet = MovieWithDetails(
        movie = MovieSample.TheWolfOfWallStreet,
        genres = listOf(GenreSample.Action, GenreSample.Crime, GenreSample.Drama)
    )

    val War = MovieWithDetails(
        movie = MovieSample.War,
        genres = listOf(GenreSample.Action, GenreSample.Adventure, GenreSample.ScienceFiction)
    )
}

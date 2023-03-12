package cinescout.movies.domain.sample

import cinescout.movies.domain.model.MovieWithDetails
import cinescout.screenplay.domain.sample.GenreSample
import cinescout.screenplay.domain.sample.ScreenplaySample

object MovieWithDetailsSample {

    val Inception = MovieWithDetails(
        movie = ScreenplaySample.Inception,
        genres = listOf(GenreSample.Action, GenreSample.Adventure, GenreSample.ScienceFiction)
    )

    val TheWolfOfWallStreet = MovieWithDetails(
        movie = ScreenplaySample.TheWolfOfWallStreet,
        genres = listOf(GenreSample.Action, GenreSample.Crime, GenreSample.Drama)
    )

    val War = MovieWithDetails(
        movie = ScreenplaySample.War,
        genres = listOf(GenreSample.Action, GenreSample.Adventure, GenreSample.ScienceFiction)
    )
}

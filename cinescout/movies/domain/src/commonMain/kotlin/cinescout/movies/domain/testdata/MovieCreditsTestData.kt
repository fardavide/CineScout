package cinescout.movies.domain.testdata

import arrow.core.some
import cinescout.movies.domain.model.MovieCredits

object MovieCreditsTestData {

    val Inception = MovieCredits(
        movieId = TmdbMovieIdTestData.Inception,
        cast = listOf(
            MovieCredits.CastMember(
                character = "Dom Cobb".some(),
                person = PersonTestData.LeonardoDiCaprio
            ),
            MovieCredits.CastMember(
                character = "Cobb".some(),
                person = PersonTestData.JosephGordonLevitt
            )
        ),
        crew = listOf(
            MovieCredits.CrewMember(
                job = "Director".some(),
                person = PersonTestData.ChristopherNolan
            )
        )
    )

    val TheWolfOfWallStreet = MovieCredits(
        movieId = TmdbMovieIdTestData.TheWolfOfWallStreet,
        cast = listOf(
            MovieCredits.CastMember(
                character = "Jordan Belfort".some(),
                person = PersonTestData.LeonardoDiCaprio
            ),
            MovieCredits.CastMember(
                character = "Donnie Azoff".some(),
                person = PersonTestData.JonahHill
            )
        ),
        crew = listOf(
            MovieCredits.CrewMember(
                job = "Director".some(),
                person = PersonTestData.MartinScorsese
            )
        )
    )

    val War = MovieCredits(
        movieId = TmdbMovieIdTestData.War,
        cast = listOf(
            MovieCredits.CastMember(
                character = "Major Kabir Dhaliwal".some(),
                person = PersonTestData.HrithikRoshan
            ),
            MovieCredits.CastMember(
                character = "Captain Khalid Rahmani".some(),
                person = PersonTestData.TigerShroff
            )
        ),
        crew = listOf(
            MovieCredits.CrewMember(
                job = "Casting Director".some(),
                person = PersonTestData.SimoneBär
            )
        )
    )
}

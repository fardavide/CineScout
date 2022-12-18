package cinescout.movies.domain.testdata

import arrow.core.some
import cinescout.common.model.CastMember
import cinescout.common.model.CrewMember
import cinescout.common.testdata.PersonTestData
import cinescout.movies.domain.model.MovieCredits
import cinescout.movies.domain.sample.TmdbMovieIdSample

object MovieCreditsTestData {

    val Inception = MovieCredits(
        movieId = TmdbMovieIdSample.Inception,
        cast = listOf(
            CastMember(
                character = "Dom Cobb".some(),
                person = PersonTestData.LeonardoDiCaprio
            ),
            CastMember(
                character = "Arthur".some(),
                person = PersonTestData.JosephGordonLevitt
            ),
            CastMember(
                character = "Saito".some(),
                person = PersonTestData.KenWatanabe
            ),
            CastMember(
                character = "Eames".some(),
                person = PersonTestData.TomHardy
            ),
            CastMember(
                character = "Ariadne".some(),
                person = PersonTestData.ElliotPage
            ),
            CastMember(
                character = "Yusuf".some(),
                person = PersonTestData.DileepRao
            ),
            CastMember(
                character = "Robert Fischer, Jr.".some(),
                person = PersonTestData.CillianMurphy
            )
        ),
        crew = listOf(
            CrewMember(
                job = "Director".some(),
                person = PersonTestData.ChristopherNolan
            )
        )
    )

    val TheWolfOfWallStreet = MovieCredits(
        movieId = TmdbMovieIdSample.TheWolfOfWallStreet,
        cast = listOf(
            CastMember(
                character = "Jordan Belfort".some(),
                person = PersonTestData.LeonardoDiCaprio
            ),
            CastMember(
                character = "Donnie Azoff".some(),
                person = PersonTestData.JonahHill
            )
        ),
        crew = listOf(
            CrewMember(
                job = "Director".some(),
                person = PersonTestData.MartinScorsese
            )
        )
    )

    val War = MovieCredits(
        movieId = TmdbMovieIdSample.War,
        cast = listOf(
            CastMember(
                character = "Major Kabir Dhaliwal".some(),
                person = PersonTestData.HrithikRoshan
            ),
            CastMember(
                character = "Captain Khalid Rahmani".some(),
                person = PersonTestData.TigerShroff
            )
        ),
        crew = listOf(
            CrewMember(
                job = "Casting Director".some(),
                person = PersonTestData.SimoneBar
            )
        )
    )
}

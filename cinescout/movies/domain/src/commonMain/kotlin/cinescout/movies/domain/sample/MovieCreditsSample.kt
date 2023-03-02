package cinescout.movies.domain.sample

import arrow.core.some
import cinescout.movies.domain.model.MovieCredits
import cinescout.screenplay.domain.model.CastMember
import cinescout.screenplay.domain.model.CrewMember
import cinescout.screenplay.domain.sample.PersonSample

object MovieCreditsSample {

    val Inception = MovieCredits(
        movieId = TmdbMovieIdSample.Inception,
        cast = listOf(
            CastMember(
                character = "Dom Cobb".some(),
                person = PersonSample.LeonardoDiCaprio
            ),
            CastMember(
                character = "Arthur".some(),
                person = PersonSample.JosephGordonLevitt
            ),
            CastMember(
                character = "Saito".some(),
                person = PersonSample.KenWatanabe
            ),
            CastMember(
                character = "Eames".some(),
                person = PersonSample.TomHardy
            ),
            CastMember(
                character = "Ariadne".some(),
                person = PersonSample.ElliotPage
            ),
            CastMember(
                character = "Yusuf".some(),
                person = PersonSample.DileepRao
            ),
            CastMember(
                character = "Robert Fischer, Jr.".some(),
                person = PersonSample.CillianMurphy
            )
        ),
        crew = listOf(
            CrewMember(
                job = "Director".some(),
                person = PersonSample.ChristopherNolan
            )
        )
    )

    val TheWolfOfWallStreet = MovieCredits(
        movieId = TmdbMovieIdSample.TheWolfOfWallStreet,
        cast = listOf(
            CastMember(
                character = "Jordan Belfort".some(),
                person = PersonSample.LeonardoDiCaprio
            ),
            CastMember(
                character = "Donnie Azoff".some(),
                person = PersonSample.JonahHill
            )
        ),
        crew = listOf(
            CrewMember(
                job = "Director".some(),
                person = PersonSample.MartinScorsese
            )
        )
    )

    val War = MovieCredits(
        movieId = TmdbMovieIdSample.War,
        cast = listOf(
            CastMember(
                character = "Major Kabir Dhaliwal".some(),
                person = PersonSample.HrithikRoshan
            ),
            CastMember(
                character = "Captain Khalid Rahmani".some(),
                person = PersonSample.TigerShroff
            )
        ),
        crew = listOf(
            CrewMember(
                job = "Casting Director".some(),
                person = PersonSample.SimoneBar
            )
        )
    )
}

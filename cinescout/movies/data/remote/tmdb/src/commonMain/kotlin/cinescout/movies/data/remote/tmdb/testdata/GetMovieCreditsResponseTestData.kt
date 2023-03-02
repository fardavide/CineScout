package cinescout.movies.data.remote.tmdb.testdata

import cinescout.movies.data.remote.tmdb.model.GetMovieCredits
import cinescout.movies.domain.sample.MovieCreditsSample
import cinescout.screenplay.domain.sample.PersonSample

object GetMovieCreditsResponseTestData {

    val Inception = GetMovieCredits.Response(
        movieId = MovieCreditsSample.Inception.movieId,
        cast = listOf(
            GetMovieCredits.Response.CastMember(
                character = MovieCreditsSample.Inception.cast[0].character.orNull(),
                id = PersonSample.LeonardoDiCaprio.tmdbId,
                name = PersonSample.LeonardoDiCaprio.name,
                profilePath = PersonSample.LeonardoDiCaprio.profileImage.orNull()?.path
            ),
            GetMovieCredits.Response.CastMember(
                character = MovieCreditsSample.Inception.cast[1].character.orNull(),
                id = PersonSample.JosephGordonLevitt.tmdbId,
                name = PersonSample.JosephGordonLevitt.name,
                profilePath = PersonSample.JosephGordonLevitt.profileImage.orNull()?.path
            )
        ),
        crew = listOf(
            GetMovieCredits.Response.CrewMember(
                id = PersonSample.ChristopherNolan.tmdbId,
                job = MovieCreditsSample.Inception.crew[0].job.orNull(),
                name = PersonSample.ChristopherNolan.name,
                profilePath = PersonSample.ChristopherNolan.profileImage.orNull()?.path
            )
        )
    )

    val TheWolfOfWallStreet = GetMovieCredits.Response(
        movieId = MovieCreditsSample.TheWolfOfWallStreet.movieId,
        cast = listOf(
            GetMovieCredits.Response.CastMember(
                character = MovieCreditsSample.TheWolfOfWallStreet.cast[0].character.orNull(),
                id = PersonSample.LeonardoDiCaprio.tmdbId,
                name = PersonSample.LeonardoDiCaprio.name,
                profilePath = PersonSample.LeonardoDiCaprio.profileImage.orNull()?.path
            ),
            GetMovieCredits.Response.CastMember(
                character = MovieCreditsSample.TheWolfOfWallStreet.cast[1].character.orNull(),
                id = PersonSample.JonahHill.tmdbId,
                name = PersonSample.JonahHill.name,
                profilePath = PersonSample.JonahHill.profileImage.orNull()?.path
            )
        ),
        crew = listOf(
            GetMovieCredits.Response.CrewMember(
                id = PersonSample.MartinScorsese.tmdbId,
                job = MovieCreditsSample.TheWolfOfWallStreet.crew[0].job.orNull(),
                name = PersonSample.MartinScorsese.name,
                profilePath = PersonSample.MartinScorsese.profileImage.orNull()?.path
            )
        )
    )
}

package cinescout.movies.data.remote.tmdb.testdata

import cinescout.movies.data.remote.tmdb.model.GetMovieCredits
import cinescout.movies.domain.testdata.MovieCreditsTestData
import cinescout.movies.domain.testdata.PersonTestData

object GetMovieCreditsResponseTestData {

    val Inception = GetMovieCredits.Response(
        movieId = MovieCreditsTestData.Inception.movieId,
        cast = listOf(
            GetMovieCredits.Response.CastMember(
                character = MovieCreditsTestData.Inception.cast[0].character.orNull(),
                id = PersonTestData.LeonardoDiCaprio.tmdbId,
                name = PersonTestData.LeonardoDiCaprio.name,
                profilePath = PersonTestData.LeonardoDiCaprio.profileImage.orNull()?.path
            ),
            GetMovieCredits.Response.CastMember(
                character = MovieCreditsTestData.Inception.cast[1].character.orNull(),
                id = PersonTestData.JosephGordonLevitt.tmdbId,
                name = PersonTestData.JosephGordonLevitt.name,
                profilePath = PersonTestData.JosephGordonLevitt.profileImage.orNull()?.path
            )
        ),
        crew = listOf(
            GetMovieCredits.Response.CrewMember(
                id = PersonTestData.ChristopherNolan.tmdbId,
                job = MovieCreditsTestData.Inception.crew[0].job.orNull(),
                name = PersonTestData.ChristopherNolan.name,
                profilePath = PersonTestData.ChristopherNolan.profileImage.orNull()?.path
            )
        )
    )

    val TheWolfOfWallStreet = GetMovieCredits.Response(
        movieId = MovieCreditsTestData.TheWolfOfWallStreet.movieId,
        cast = listOf(
            GetMovieCredits.Response.CastMember(
                character = MovieCreditsTestData.TheWolfOfWallStreet.cast[0].character.orNull(),
                id = PersonTestData.LeonardoDiCaprio.tmdbId,
                name = PersonTestData.LeonardoDiCaprio.name,
                profilePath = PersonTestData.LeonardoDiCaprio.profileImage.orNull()?.path
            ),
            GetMovieCredits.Response.CastMember(
                character = MovieCreditsTestData.TheWolfOfWallStreet.cast[1].character.orNull(),
                id = PersonTestData.JonahHill.tmdbId,
                name = PersonTestData.JonahHill.name,
                profilePath = PersonTestData.JonahHill.profileImage.orNull()?.path
            )
        ),
        crew = listOf(
            GetMovieCredits.Response.CrewMember(
                id = PersonTestData.MartinScorsese.tmdbId,
                job = MovieCreditsTestData.TheWolfOfWallStreet.crew[0].job.orNull(),
                name = PersonTestData.MartinScorsese.name,
                profilePath = PersonTestData.MartinScorsese.profileImage.orNull()?.path
            )
        )
    )
}

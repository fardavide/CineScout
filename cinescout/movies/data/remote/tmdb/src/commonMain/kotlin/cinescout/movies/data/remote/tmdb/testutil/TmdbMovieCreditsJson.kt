package cinescout.movies.data.remote.tmdb.testutil

import cinescout.movies.data.remote.tmdb.model.GetMovieCredits.Response
import cinescout.movies.domain.testdata.MovieCreditsTestData
import cinescout.movies.domain.testdata.PersonTestData

object TmdbMovieCreditsJson {

    val Inception = """
        {
            "${Response.Id}": "${MovieCreditsTestData.Inception.movieId.value}",
            "${Response.Cast}": [
                {
                    "${Response.CastMember.Character}": "${MovieCreditsTestData.Inception.cast[0].character.orNull()}",
                    "${Response.Id}": "${PersonTestData.LeonardoDiCaprio.tmdbId.value}",
                    "${Response.Name}": "${PersonTestData.LeonardoDiCaprio.name}",
                    "${Response.ProfilePath}": "${PersonTestData.LeonardoDiCaprio.profileImage.orNull()?.path}"
                },
                {
                    "${Response.CastMember.Character}": "${MovieCreditsTestData.Inception.cast[1].character.orNull()}",
                    "${Response.Id}": "${PersonTestData.JosephGordonLevitt.tmdbId.value}",
                    "${Response.Name}": "${PersonTestData.JosephGordonLevitt.name}",
                    "${Response.ProfilePath}": "${PersonTestData.JosephGordonLevitt.profileImage.orNull()?.path}"
                }
            ],
            "${Response.Crew}": [
                {
                    "${Response.Id}": "${PersonTestData.ChristopherNolan.tmdbId.value}",
                    "${Response.CrewMember.Job}": "${MovieCreditsTestData.Inception.crew[0].job.orNull()}",
                    "${Response.Name}": "${PersonTestData.ChristopherNolan.name}",
                    "${Response.ProfilePath}": "${PersonTestData.ChristopherNolan.profileImage.orNull()?.path}"
                }
            ]
        }
    """
}

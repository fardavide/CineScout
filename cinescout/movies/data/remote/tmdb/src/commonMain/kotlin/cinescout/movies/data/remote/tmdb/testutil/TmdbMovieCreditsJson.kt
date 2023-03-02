package cinescout.movies.data.remote.tmdb.testutil

import cinescout.movies.data.remote.tmdb.model.GetMovieCredits.Response
import cinescout.movies.domain.sample.MovieCreditsSample
import cinescout.screenplay.domain.sample.PersonSample

object TmdbMovieCreditsJson {

    val Inception = """
        {
            "${Response.Id}": "${MovieCreditsSample.Inception.movieId.value}",
            "${Response.Cast}": [
                {
                    "${Response.CastMember.Character}": "${MovieCreditsSample.Inception.cast[0].character.orNull()}",
                    "${Response.Id}": "${PersonSample.LeonardoDiCaprio.tmdbId.value}",
                    "${Response.Name}": "${PersonSample.LeonardoDiCaprio.name}",
                    "${Response.ProfilePath}": "${PersonSample.LeonardoDiCaprio.profileImage.orNull()?.path}"
                },
                {
                    "${Response.CastMember.Character}": "${MovieCreditsSample.Inception.cast[1].character.orNull()}",
                    "${Response.Id}": "${PersonSample.JosephGordonLevitt.tmdbId.value}",
                    "${Response.Name}": "${PersonSample.JosephGordonLevitt.name}",
                    "${Response.ProfilePath}": "${PersonSample.JosephGordonLevitt.profileImage.orNull()?.path}"
                }
            ],
            "${Response.Crew}": [
                {
                    "${Response.Id}": "${PersonSample.ChristopherNolan.tmdbId.value}",
                    "${Response.CrewMember.Job}": "${MovieCreditsSample.Inception.crew[0].job.orNull()}",
                    "${Response.Name}": "${PersonSample.ChristopherNolan.name}",
                    "${Response.ProfilePath}": "${PersonSample.ChristopherNolan.profileImage.orNull()?.path}"
                }
            ]
        }
    """.trimIndent()

    val TheWolfOfWallStreet = """
        {
            "${Response.Id}": "${MovieCreditsSample.TheWolfOfWallStreet.movieId.value}",
            "${Response.Cast}": [
                {
                    "${Response.CastMember.Character}": 
                        "${MovieCreditsSample.TheWolfOfWallStreet.cast[0].character.orNull()}",
                    "${Response.Id}": "${PersonSample.LeonardoDiCaprio.tmdbId.value}",
                    "${Response.Name}": "${PersonSample.LeonardoDiCaprio.name}",
                    "${Response.ProfilePath}": "${PersonSample.LeonardoDiCaprio.profileImage.orNull()?.path}"
                },
                {
                    "${Response.CastMember.Character}": 
                        "${MovieCreditsSample.TheWolfOfWallStreet.cast[1].character.orNull()}",
                    "${Response.Id}": "${PersonSample.JonahHill.tmdbId.value}",
                    "${Response.Name}": "${PersonSample.JonahHill.name}",
                    "${Response.ProfilePath}": "${PersonSample.JonahHill.profileImage.orNull()?.path}"
                }
            ],
            "${Response.Crew}": [
                {
                    "${Response.Id}": "${PersonSample.MartinScorsese.tmdbId.value}",
                    "${Response.CrewMember.Job}": "${MovieCreditsSample.TheWolfOfWallStreet.crew[0].job.orNull()}",
                    "${Response.Name}": "${PersonSample.MartinScorsese.name}",
                    "${Response.ProfilePath}": "${PersonSample.MartinScorsese.profileImage.orNull()?.path}"
                }
            ]
        }
    """.trimIndent()

    val War = """
        {
            "${Response.Id}": "${MovieCreditsSample.War.movieId.value}",
            "${Response.Cast}": [
                {
                    "${Response.CastMember.Character}": 
                        "${MovieCreditsSample.War.cast[0].character.orNull()}",
                    "${Response.Id}": "${PersonSample.HrithikRoshan.tmdbId.value}",
                    "${Response.Name}": "${PersonSample.HrithikRoshan.name}",
                    "${Response.ProfilePath}": "${PersonSample.HrithikRoshan.profileImage.orNull()?.path}"
                },
                {
                    "${Response.CastMember.Character}": 
                        "${MovieCreditsSample.War.cast[1].character.orNull()}",
                    "${Response.Id}": "${PersonSample.TigerShroff.tmdbId.value}",
                    "${Response.Name}": "${PersonSample.TigerShroff.name}",
                    "${Response.ProfilePath}": "${PersonSample.TigerShroff.profileImage.orNull()?.path}"
                }
            ],
            "${Response.Crew}": [
                {
                    "${Response.Id}": "${PersonSample.SimoneBar.tmdbId.value}",
                    "${Response.CrewMember.Job}": "${MovieCreditsSample.War.crew[0].job.orNull()}",
                    "${Response.Name}": "${PersonSample.SimoneBar.name}",
                    "${Response.ProfilePath}": "${PersonSample.SimoneBar.profileImage.orNull()?.path}"
                }
            ]
        }
    """.trimIndent()
}

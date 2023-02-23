package cinescout.movies.data.remote.tmdb.testutil

import cinescout.common.testdata.PersonTestData
import cinescout.movies.data.remote.tmdb.model.GetMovieCredits.Response
import cinescout.movies.domain.sample.MovieCreditsSample

object TmdbMovieCreditsJson {

    val Inception = """
        {
            "${Response.Id}": "${MovieCreditsSample.Inception.movieId.value}",
            "${Response.Cast}": [
                {
                    "${Response.CastMember.Character}": "${MovieCreditsSample.Inception.cast[0].character.orNull()}",
                    "${Response.Id}": "${PersonTestData.LeonardoDiCaprio.tmdbId.value}",
                    "${Response.Name}": "${PersonTestData.LeonardoDiCaprio.name}",
                    "${Response.ProfilePath}": "${PersonTestData.LeonardoDiCaprio.profileImage.orNull()?.path}"
                },
                {
                    "${Response.CastMember.Character}": "${MovieCreditsSample.Inception.cast[1].character.orNull()}",
                    "${Response.Id}": "${PersonTestData.JosephGordonLevitt.tmdbId.value}",
                    "${Response.Name}": "${PersonTestData.JosephGordonLevitt.name}",
                    "${Response.ProfilePath}": "${PersonTestData.JosephGordonLevitt.profileImage.orNull()?.path}"
                }
            ],
            "${Response.Crew}": [
                {
                    "${Response.Id}": "${PersonTestData.ChristopherNolan.tmdbId.value}",
                    "${Response.CrewMember.Job}": "${MovieCreditsSample.Inception.crew[0].job.orNull()}",
                    "${Response.Name}": "${PersonTestData.ChristopherNolan.name}",
                    "${Response.ProfilePath}": "${PersonTestData.ChristopherNolan.profileImage.orNull()?.path}"
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
                    "${Response.Id}": "${PersonTestData.LeonardoDiCaprio.tmdbId.value}",
                    "${Response.Name}": "${PersonTestData.LeonardoDiCaprio.name}",
                    "${Response.ProfilePath}": "${PersonTestData.LeonardoDiCaprio.profileImage.orNull()?.path}"
                },
                {
                    "${Response.CastMember.Character}": 
                        "${MovieCreditsSample.TheWolfOfWallStreet.cast[1].character.orNull()}",
                    "${Response.Id}": "${PersonTestData.JonahHill.tmdbId.value}",
                    "${Response.Name}": "${PersonTestData.JonahHill.name}",
                    "${Response.ProfilePath}": "${PersonTestData.JonahHill.profileImage.orNull()?.path}"
                }
            ],
            "${Response.Crew}": [
                {
                    "${Response.Id}": "${PersonTestData.MartinScorsese.tmdbId.value}",
                    "${Response.CrewMember.Job}": "${MovieCreditsSample.TheWolfOfWallStreet.crew[0].job.orNull()}",
                    "${Response.Name}": "${PersonTestData.MartinScorsese.name}",
                    "${Response.ProfilePath}": "${PersonTestData.MartinScorsese.profileImage.orNull()?.path}"
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
                    "${Response.Id}": "${PersonTestData.HrithikRoshan.tmdbId.value}",
                    "${Response.Name}": "${PersonTestData.HrithikRoshan.name}",
                    "${Response.ProfilePath}": "${PersonTestData.HrithikRoshan.profileImage.orNull()?.path}"
                },
                {
                    "${Response.CastMember.Character}": 
                        "${MovieCreditsSample.War.cast[1].character.orNull()}",
                    "${Response.Id}": "${PersonTestData.TigerShroff.tmdbId.value}",
                    "${Response.Name}": "${PersonTestData.TigerShroff.name}",
                    "${Response.ProfilePath}": "${PersonTestData.TigerShroff.profileImage.orNull()?.path}"
                }
            ],
            "${Response.Crew}": [
                {
                    "${Response.Id}": "${PersonTestData.SimoneBar.tmdbId.value}",
                    "${Response.CrewMember.Job}": "${MovieCreditsSample.War.crew[0].job.orNull()}",
                    "${Response.Name}": "${PersonTestData.SimoneBar.name}",
                    "${Response.ProfilePath}": "${PersonTestData.SimoneBar.profileImage.orNull()?.path}"
                }
            ]
        }
    """.trimIndent()
}

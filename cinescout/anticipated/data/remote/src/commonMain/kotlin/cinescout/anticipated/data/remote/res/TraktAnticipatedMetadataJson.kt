package cinescout.anticipated.data.remote.res

import cinescout.screenplay.domain.sample.ScreenplayIdsSample

object TraktAnticipatedMetadataJson {

    val OneMovie = """
      [
        {
          "list_count": 27111,
          "movie": {
            "title": "Avatar 3",
            "year": 2024,
            "ids": {
              "trakt": ${ScreenplayIdsSample.Avatar3.trakt.value},
              "slug": "avatar-3-2024",
              "imdb": "tt1757678",
              "tmdb": ${ScreenplayIdsSample.Avatar3.tmdb.value}
            }
          }
        }
      ]
    """.trimIndent()

    val OneTvShow = """
      [
        {
          "list_count": 4271,
          "show": {
            "title": "The Walking Dead: Dead City",
            "year": 2023,
            "ids": {
              "trakt": ${ScreenplayIdsSample.TheWalkingDeadDeadCity.trakt.value},
              "slug": "the-walking-dead-dead-city",
              "tvdb": 417549,
              "imdb": "tt18546730",
              "tmdb": ${ScreenplayIdsSample.TheWalkingDeadDeadCity.tmdb.value},
              "tvrage": {}
            }
          }
        }
      ]
    """.trimIndent()
}
